/**
 *====================================================
 * 文件名称: DeliveryCenterController.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2015年1月13日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.controller.app.b2c;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.controller.app.model.GuideModel;
import net.wit.entity.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Filter.Operator;
import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.DeliveryCenterListModel;
import net.wit.controller.app.model.DeliveryCenterModel;
import net.wit.service.AreaService;
import net.wit.service.DeliveryCenterService;
import net.wit.service.EmployeeService;
import net.wit.service.MemberService;
import net.wit.service.TenantService;

/**
 * @ClassName: DeliveryCenterController
 * @Description:
 * @author Administrator
 * @date 2015年1月13日 下午1:21:42
 */
@Controller("appB2cDeliveryCenterController")
@RequestMapping("/app/b2c/delivery_center")
public class DeliveryCenterController extends BaseController {

	@Resource(name = "deliveryCenterServiceImpl")
	private DeliveryCenterService deliveryCenterService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "employeeServiceImpl")
	private EmployeeService employeeService;

	/**
	 * 获取发货地址详情
	 * 
	 * id 发货地址 id
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock view(Long id) {
		DeliveryCenter deliveryCenter = deliveryCenterService.find(id);
		DeliveryCenterModel model = new DeliveryCenterModel();
		model.copyFrom(deliveryCenter);
		return DataBlock.success(model,"执行成功");
	}
	
	/**
	 * 获取发货地址列表
	 * 
	 * tenantId 店铺 id
	 * lng lat 经伟度 
	 * 
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(Long tenantId,Location location,Pageable pageable,HttpServletRequest request) {
	    Tenant tenant = tenantService.find(tenantId);
	    if (tenant==null) {
	    	return DataBlock.error("无效店铺 id");
	    }
	    
 		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("tenant", Operator.eq, tenant));
		pageable.setFilters(filters);
	    
	    Page<DeliveryCenter> page = deliveryCenterService.findPage(pageable);
	    return DataBlock.success(DeliveryCenterListModel.bindData(page.getContent(),location),"执行成功");
	}


	/**
	 * 获取实体店员工列表
	 */
	@RequestMapping(value = "/employee/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock employeeList(Long id,Pageable pageable){
		Member member=memberService.getCurrent();
		DeliveryCenter deliveryCenter=deliveryCenterService.find(id);
		List<Filter> filters = new ArrayList<>();
		filters.add(new Filter("deliveryCenter", Operator.eq, deliveryCenter));
		pageable.setFilters(filters);
		Page<Employee> page = employeeService.findPage(pageable);
		List<GuideModel> models = new ArrayList<>();
		for (Employee employee:page.getContent()){
			if(employee.getMember()!=null){
				GuideModel model=new GuideModel();
				model.copyFrom(employee,memberService.findFans(member).size(),member);
				models.add(model);
			}
		}
		return DataBlock.success(models,"执行成功");
	}
	
}
