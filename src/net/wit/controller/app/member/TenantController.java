/**
 *====================================================
 * 文件名称: TenantController.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014-9-11			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.controller.app.member;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.zxing.WriterException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.TenantModel;
import net.wit.entity.DeliveryCenter;
import net.wit.entity.Location;
import net.wit.entity.Member;
import net.wit.entity.Qrcode;
import net.wit.entity.Tenant;
import net.wit.entity.TenantWechat;
import net.wit.support.EntitySupport;
import net.wit.util.QRBarCodeUtil;
import net.wit.weixin.main.MenuManager;

/**
 * @ClassName: TenantController
 * @Description: TODO(店铺管理)
 * @author Administrator
 * @date 2014-9-11 上午11:31:34
 */
@Controller("appMemberTenantController")
@RequestMapping("/app/member/tenant")
public class TenantController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "activityDetailServiceImpl")
	private ActivityDetailService activityDetailService;

	@Resource(name = "activityRulesServiceImpl")
	private ActivityRulesService activityRulesService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	

	@Resource(name = "qrcodeServiceImpl")
	private QrcodeService qrcodeService;
	
	@Resource(name = "tenantCategoryServiceImpl")
	private TenantCategoryService tenantCategoryService;
	
	/**
	 * 查看
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock view(Long id,Boolean promotion) {
		Tenant tenant = tenantService.find(id);
		if (tenant == null) {
			return DataBlock.error("无效店铺id");
			
		}
		TenantModel model = new TenantModel();
		model.copyFrom(tenant);
		if (promotion!=null) {
			if (promotion) {
				   model.bindPromoton(tenant.getVaildPromotions());
				}
		}
		return DataBlock.success(model,"执行成功");
	}
	
	
	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock add(String shortName,String thumbnail,Long tenantCategoryId,Location location) {
		//System.out.println(thumbnail);
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		
		Tenant tenant = member.getTenant();
		if (tenant == null) {
			tenant = EntitySupport.createInitTenant();
			if (member.getArea() != null) {
				tenant.setArea(member.getArea());
			}
			tenant.setScore(0F);
			tenant.setTotalScore(0L);
			tenant.setScoreCount(0L);
			tenant.setHits(0L);
			tenant.setWeekHits(0L);
			tenant.setMonthHits(0L);
			tenant.setAddress(member.getAddress());
			tenant.setLinkman(member.getName());
			tenant.setTelephone(member.getMobile());
		}
		tenant.setPoint(0L);
		tenant.setName(shortName);
		tenant.setShortName(shortName);
		tenant.setThumbnail(thumbnail);

		if (tenant.getCode() == null) {
			tenant.setCode("1");
		}
	
		if (tenant.getArea()==null) {
	        tenant.setArea(areaService.getCurrent());
		}
		if (tenantCategoryId!=null) {
			tenant.setTenantCategory(tenantCategoryService.find(tenantCategoryId));
		}
		tenantService.save(tenant, member, location);

		if(member.getMember()!=null&&member.getMember().getTenant()!=null){
			if(!activityDetailService.isActivity(null,member.getMember().getTenant(),activityRulesService.find(45L))){
				//TODO 首次推荐好友开店领取积分
				activityDetailService.addPoint(null, member.getMember().getTenant(), activityRulesService.find(45L));
			}

			if(!activityDetailService.isActivity(null,member.getMember().getTenant(),activityRulesService.find(51L))){
				//TODO 推荐好友开店领取积分，每日一次
				activityDetailService.addPoint(null, member.getMember().getTenant(), activityRulesService.find(51L));
			}
		}

		TenantModel model = new TenantModel();
		model.copyFrom(tenant);
		return DataBlock.success(model,"执行成功");
		
	}

	/**
	 * 分享店铺地址
	 */
	@RequestMapping(value = "/share", method = RequestMethod.GET)
	public @ResponseBody DataBlock share(Long id,String type) {
		Tenant tenant = tenantService.find(id);
		if (tenant == null) {
			return DataBlock.error("店铺ID不存在");
		}
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		String url = "";
	    DeliveryCenter delivery = tenant.getDefaultDeliveryCenter();
	    if (delivery==null) {
	    	return DataBlock.error("没有实体店不能做活动推广");
	    }
	    if ("preview".equals(type)) 
	    {
	    	url = bundle.getString("WeiXinSiteUrl") + "/wap/tenant/index_static/"+id.toString()+".jhtml?extension=" + (member != null ? member.getUsername() : "");
	    } else
	    if (!"app".equals(type) ) {
			if (!tenant.getStatus().equals(Tenant.Status.success)) {
				return DataBlock.error("没有开通不能分享");
			}
	    	url = MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/wap/tenant/index/"+id.toString()+".jhtml?extension=" + (member != null ? member.getUsername() : "")));
	    } else
	    {
	    	url = bundle.getString("WeiXinSiteUrl") + "/wap/tenant/index/"+id.toString()+".jhtml?extension=" + (member != null ? member.getUsername() : "");
	    }

	    Map<String,String> data = new HashMap<String,String>();
	    data.put("url",url);
	    data.put("title","欢迎您光临“"+tenant.getShortName()+"”，您身边的优质实体店，购实惠，购惊喜，购保证");
	    data.put("thumbnail",tenant.getThumbnail()+"@200w_200h_1e_1c_100Q");
	    data.put("description",tenant.getIntroduction());
		return DataBlock.success(data,"执行成功");
	}

	/**
	 * 分享地址
	 */
	@RequestMapping(value = "/invite", method = RequestMethod.GET)
	public @ResponseBody DataBlock invite(Long id,String type) {
		Tenant tenant = tenantService.find(id);
		if (tenant == null) {
			return DataBlock.error("店铺ID不存在");
		}
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}

		if (tenant.getThumbnail()==null) {
			return DataBlock.error("请上传店招再分享");
		}
		if (!tenant.getStatus().equals(Tenant.Status.success)) {
			return DataBlock.error("没有开通不能分享");
		}

		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");

		String wxMenu = bundle.getString("wxMenu");

		String _url = bundle.getString("WeiXinSiteUrl") + "/wap/invite/index.jhtml?extension=" + (member != null ? member.getUsername() : "");
		if (wxMenu.equals("101")) {
			_url = bundle.getString("WeiXinSiteUrl") + "/www/invite/index_ztb.html?extension=" + (member != null ? member.getUsername() : "");
		}

		String url = "";
	    if (!"app".equals(type)) {
	    	url = MenuManager.codeUrlO2(URLEncoder.encode(_url));
	    } else {
	    	url = _url;
	    }

	    Map<String,String> data = new HashMap<String,String>();
	    data.put("url",url);
	    data.put("title","亲,“"+tenant.getShortName()+"”推荐您也来开店。");
	    data.put("thumbnail",tenant.getThumbnail()+"@200w_200h_1e_1c_100Q");
	    data.put("description",tenant.getIntroduction());
		return DataBlock.success(data,"执行成功");
	}

	/**
	 * 修改
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock update(String shortName,String name,String thumbnail,String logo,String address,String linkMan,String introduction,String weixin,Long tenantCategoryId,Boolean toPay,Boolean tamPo,Boolean noReason,Location location) {

		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}

		Tenant tenant = member.getTenant();
		if (shortName!=null) {
		    tenant.setName(shortName);
		    tenant.setShortName(shortName);

			if(!activityDetailService.isActivity(null,tenant, activityRulesService.find(1L))){
				activityDetailService.addPoint(null,tenant,activityRulesService.find(1L));
			}
		}
		if (name!=null) {
		    tenant.setName(name);
		    tenant.setShortName(name);

			if(!activityDetailService.isActivity(null,tenant, activityRulesService.find(1L))){
				activityDetailService.addPoint(null,tenant,activityRulesService.find(1L));
			}
		}
		if (thumbnail!=null) {
		    tenant.setThumbnail(thumbnail);

			if(!activityDetailService.isActivity(null,tenant, activityRulesService.find(4L))){
				activityDetailService.addPoint(null,tenant,activityRulesService.find(4L));
			}
		}
		if (logo!=null) {
		    tenant.setLogo(logo);

			if(!activityDetailService.isActivity(null,tenant, activityRulesService.find(3L))){
				activityDetailService.addPoint(null,tenant,activityRulesService.find(3L));
			}
		}
		if (address != null) {
			tenant.setAddress(address);

			if(!activityDetailService.isActivity(null,tenant, activityRulesService.find(2L))){
				activityDetailService.addPoint(null,tenant,activityRulesService.find(2L));
			}
		}
		if (linkMan != null) {
			tenant.setLinkman(linkMan);
		}
		if (toPay != null) {
			tenant.setToPay(toPay);
		}
		if (tamPo != null) {
			tenant.setTamPo(tamPo);
		}
		if (noReason != null) {
			tenant.setNoReason(noReason);
		}
		if (introduction != null) {
			tenant.setIntroduction(introduction);
		}
		if (weixin != null) {
			TenantWechat wechat = tenant.getTenantWechat();
			if (wechat==null) {
				wechat = new TenantWechat();
			}
			wechat.setWeixin_code(weixin);
			tenant.setTenantWechat(wechat);
		}
		if (tenantCategoryId != null) {
			tenant.setTenantCategory(tenantCategoryService.find(tenantCategoryId));
		}

		tenantService.update(tenant);
		return DataBlock.success("success","执行成功");

	}

	/**
	 * 根据
	 */
	@RequestMapping(value = "/qrcode/json",method = RequestMethod.GET)
	@ResponseBody
	public DataBlock qrcodeJson(HttpServletRequest request, HttpServletResponse response) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}

		try {
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
			if (!member.getTenant().getStatus().equals(Tenant.Status.success)) {
				return DataBlock.error("没有开通不能分享");
			}
			// 第三方用户唯一凭证
			Qrcode qrcode = qrcodeService.findbyTenant(member.getTenant());
			String url = "";
			if (qrcode==null || qrcode.getUrl()==null) {
			  url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/share/index.jhtml?extension=" + (member != null ? member.getUsername() : ""));
			} else {
			  url = qrcode.getUrl();	
			}

			if(!activityDetailService.isActivity(null,member.getTenant(),activityRulesService.find(41L))){
				activityDetailService.addPoint(null,member.getTenant(),activityRulesService.find(41L));
			}

			if(!activityDetailService.isActivity(null,member.getTenant(),activityRulesService.find(42L))){
				activityDetailService.addPoint(null,member.getTenant(),activityRulesService.find(42L));
			}

            return DataBlock.success(url,"获取成功");
		} catch (Exception e) {
			return DataBlock.error("获取二维码失败");
		}
	}
	/**
	 * 根据
	 */
	@RequestMapping(value = "/qrcode",method = RequestMethod.GET)
	public void qrcode(HttpServletRequest request, HttpServletResponse response) {
		try {
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
			// 第三方用户唯一凭证
			String url = URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/wap/bound/index.jhtml");

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
	
}
