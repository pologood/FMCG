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
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wit.Filter;
import net.wit.Pageable;
import net.wit.controller.app.model.MemberListModel;
import net.wit.service.*;
import net.wit.weixin.main.MenuManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.zxing.WriterException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import net.wit.Setting;
import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.FreightModel;
import net.wit.controller.app.model.TenantModel;
import net.wit.entity.Freight;
import net.wit.entity.Location;
import net.wit.entity.Member;
import net.wit.entity.Tenant;
import net.wit.support.EntitySupport;
import net.wit.util.QRBarCodeUtil;
import net.wit.util.SettingUtils;

/**
 * @ClassName: ExtensionController
 * @Description: TODO(推广管理)
 * @author Administrator
 * @date 2014-9-11 上午11:31:34
 */
@Controller("appMemberExtensionController")
@RequestMapping("/app/member/extension")
public class ExtensionController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "activityDetailServiceImpl")
	private ActivityDetailService activityDetailService;

	@Resource(name = "activityRulesServiceImpl")
	private ActivityRulesService activityRulesService;
	
	/**
	 * 查看
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock view(Long id) {
		Tenant tenant = tenantService.find(id);
		if (tenant == null) {
			return DataBlock.error("无效店铺id");
			
		}
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("brokerage", tenant.getGeneralize());
		data.put("agency", tenant.getAgency());
		data.put("isUnion", tenant.getIsUnion());
		return DataBlock.success(data,"执行成功");
	}
	
	/**
	 * 修改
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock update(BigDecimal brokerage,BigDecimal agency,Boolean isUnion) {

		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Tenant tenant = member.getTenant();
		if (new BigDecimal("0.01").compareTo(brokerage)>0) {
			return DataBlock.error("推广佣金不能小于1%");
		}
		if (isUnion!=null && isUnion) {
	 		if (tenant.getBrokerage().compareTo(agency)>0) {
				return DataBlock.error("联盟佣金不能小于"+tenant.getBrokerage().multiply(new BigDecimal(100))+"%");
			} 
		} else {
			isUnion = false;
		}
		tenant.setGeneralize(brokerage);
		tenant.setAgency(agency);
		tenant.setIsUnion(isUnion);
		tenantService.update(tenant);

		if (!activityDetailService.isActivity(null, tenant, activityRulesService.find(23L))) {
			activityDetailService.addPoint(null, tenant, activityRulesService.find(23L));
		}

		if (!activityDetailService.isActivity(null, tenant, activityRulesService.find(24L))) {
			activityDetailService.addPoint(null, tenant, activityRulesService.find(24L));
		}

		return DataBlock.success("success","执行成功");
		
	}

	/**
	 * 我的推广
	 * @return
	 * rebate 推广分润金额
	 * count 推广人数
     */
	@RequestMapping(value = "/my", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock my(){
		Member member=memberService.getCurrent();
		Map<String,Object> map=new HashMap<>();
		map.put("extendMemberName",member.getMember()==null?null:member.getMember().getDisplayName());
		map.put("rebate",member.getRebateAmount());
		map.put("count",memberService.findList(member).size());
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/share/promoting/intro.jhtml?extension=" + (member != null ? member.getUsername() : ""));
		map.put("link", url);
		map.put("title","好货多多,您的好友喊您来挑货啦!");
		map.put("desc", "推广新用户,注册有惊喜。发展会员,享受永久分润。会员越多收入越多。");
		map.put("imgUrl", member.getHeadImg());
		return DataBlock.success(map,"执行成功");
	}

	/**
	 * 我邀请的会员
     */
	@RequestMapping(value = "/member/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock memberList(Pageable pageable){
		Member member=memberService.getCurrent();
		List<Filter> filters=new ArrayList<>();
		filters.add(new Filter("member", Filter.Operator.eq,member));
		pageable.setFilters(filters);
		List<MemberListModel> models=MemberListModel.bindData(memberService.findPage(pageable).getContent(),member.getLocation());
		return DataBlock.success(models,"执行成功");
	}
	
}
