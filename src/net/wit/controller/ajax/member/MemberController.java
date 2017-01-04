/**
 *====================================================
 * 文件名称: MemberInfoController.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014-9-11			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.controller.ajax.member;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Setting;
import net.wit.Filter.Operator;
import net.wit.controller.ajax.BaseController;
import net.wit.controller.ajax.model.MemberInfoModel;
import net.wit.entity.Host;
import net.wit.entity.Member;
import net.wit.entity.Tenant;
import net.wit.service.MemberRankService;
import net.wit.service.MemberService;
import net.wit.service.TenantService;
import net.wit.util.JsonUtils;
import net.wit.util.QRBarCodeUtil;
import net.wit.util.SettingUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.zxing.WriterException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * @ClassName: MemberInfoController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014-9-11 上午11:31:34
 */
@Controller("memberAreaController")
@RequestMapping("/ajax/member")
public class MemberController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;

	/**
	 * 会员信息
	 */
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	@ResponseBody
	public Message info(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		Member member = memberService.getCurrent();
		if (member == null) {
			return Message.error("未登录!");
		}
		return Message.success(JsonUtils.toJson(convertToMemberInfoModel(member)));
	}

	/**
	 * 我的推广
	 */
	@RequestMapping(value = "/share_count", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> shareCount(HttpServletRequest request) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("message", Message.success("success"));
		Member member = memberService.getCurrent();
		Filter filter = new Filter("member", Operator.eq, member);
		Long share = memberService.count(filter);
		BigDecimal amount = memberService.sumExtAmount(member);
		BigDecimal rebate = memberService.sumExtProfit(member);
		data.put("share", share);
		data.put("amount", amount);
		data.put("rebate", rebate);
		Setting setting = SettingUtils.get();
		data.put("qrcode", setting.getSiteUrl() + "/ajax/qrcode.jhtml");
		return data;
	}

	/**
	 * 根据
	 */
	@RequestMapping(value = "/qrcode", method = RequestMethod.GET)
	public void qrcode(HttpServletRequest request, HttpServletResponse response) {
		try {
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
			// 第三方用户唯一凭证
			String appId = bundle.getString("APPID");// 睿商圈
			String redirectUri = URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/wap/bound/index.jhtml");
			String url = 
					  "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
					+ redirectUri
					+ "&response_type=code&scope=snsapi_base&state=123&from=singlemessage&isappinstalled=1#wechat_redirect";

			String tempFile = System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".jpg";
			response.reset();
			response.setContentType("image/jpeg;charset=utf-8");
			try {
				QRBarCodeUtil.encodeQRCode(url, tempFile, 400, 400);
			} catch (WriterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ServletOutputStream output = response.getOutputStream();// 得到输出流
			InputStream imageIn = new FileInputStream(new File(tempFile));
			// 得到输入的编码器，将文件流进行jpg格式编码
			JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(imageIn);
			// 得到编码后的图片对象
			BufferedImage image = decoder.decodeAsBufferedImage();
			// 得到输出的编码器
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);
			encoder.encode(image);// 对图片进行输出编码
			imageIn.close();// 关闭文件流
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private MemberInfoModel convertToMemberInfoModel(Member member) {
		MemberInfoModel model = new MemberInfoModel();
		model.setBalance(member.getBalance());
		model.setUsername(member.getUsername());
		model.setPoint(member.getPoint());
		model.setCouponCodes(0L);
		model.setStatus(Tenant.Status.none);
		model.setFavorite(0L);
		Filter filter = new Filter("member", Operator.eq, member);
		Long share = memberService.count(filter);
		model.setExtension(share);
		Setting setting = SettingUtils.get();
		model.setFee(setting.getFunctionFee());
		Tenant tenant = member.getTenant();
		if (tenant != null) {
			Long favorite =  new Long(tenant.getFavoriteMembers().size());
			model.setFavorite(favorite);
			model.setTenantId(tenant.getId());
			model.setTenantName(tenant.getName());
			if (tenant.getDefaultDeliveryCenter() != null) {
				model.setDeliveryId(tenant.getDefaultDeliveryCenter().getId());
			}
			model.setLogo(tenant.getLogo());
			model.setStatus(tenant.getStatus());
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
			// 第三方用户唯一凭证
			String appId = bundle.getString("APPID");// 睿商圈
			model.setAppId(appId);
		}
		if (tenant != null && tenant.getHost() != null) {
			Host host = tenant.getHost();
			model.setHostname(host.getHostname());
			model.setPort(host.getPort());
			model.setDbid(host.getDbid());
		}
		if (member.getMemberRank() == null) {
			model.setMemberRankName(memberRankService.findDefault().getName());
		} else {
			model.setMemberRankName(member.getMemberRank().getName());
		}
		return model;
	}
}
