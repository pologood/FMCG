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

import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.controller.assistant.model.FreightModel;
import net.wit.entity.Freight;
import net.wit.entity.Member;
import net.wit.entity.Tenant;
import net.wit.service.ActivityDetailService;
import net.wit.service.ActivityRulesService;
import net.wit.service.MemberService;
import net.wit.service.TenantService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @ClassName: TenantController
 * @Description: TODO(店铺管理)
 * @author Administrator
 * @date 2014-9-11 上午11:31:34
 */
@Controller("assistantMembertfreightController")
@RequestMapping("/assistant/member/freight")
public class FreightController extends BaseController {

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
		FreightModel model = new FreightModel();
		model.copyFrom(tenant.getFreight());
		return DataBlock.success(model,"执行成功");
	}
	
	/**
	 * 修改
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock update(Freight freight) {

		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		
		Tenant tenant = member.getTenant();
		tenant.setFreight(freight);
		tenantService.update(tenant);

		if(!activityDetailService.isActivity(null,tenant, activityRulesService.find(6L))){
			activityDetailService.addPoint(null,tenant,activityRulesService.find(6L));
		}
		return DataBlock.success("success","修改运费成功");
		
	}
	
}
