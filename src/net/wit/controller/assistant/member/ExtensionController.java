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
package net.wit.controller.assistant.member;

import net.wit.Filter;
import net.wit.Pageable;
import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.controller.assistant.model.MemberListModel;
import net.wit.entity.Member;
import net.wit.entity.Tenant;
import net.wit.service.ActivityDetailService;
import net.wit.service.ActivityRulesService;
import net.wit.service.MemberService;
import net.wit.service.TenantService;
import net.wit.weixin.main.MenuManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @ClassName: ExtensionController
 * @Description: TODO(推广管理)
 * @author Administrator
 * @date 2014-9-11 上午11:31:34
 */
@Controller("assistantMemberExtensionController")
@RequestMapping("/assistant/member/extension")
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
		String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/weixin/member/promoting.jhtml?extension=" + (member != null ? member.getUsername() : ""));
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
