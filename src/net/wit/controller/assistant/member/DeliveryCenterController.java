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
package net.wit.controller.assistant.member;

import net.wit.Filter;
import net.wit.Filter.Operator;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.controller.assistant.model.DeliveryCenterListModel;
import net.wit.controller.assistant.model.DeliveryCenterModel;
import net.wit.controller.assistant.model.GuideModel;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: DeliveryCenterController
 * @Description:
 * @author Administrator
 * @date 2015年1月13日 下午1:21:42
 */
@Controller("assistantMemberDeliveryCenterController")
@RequestMapping("/assistant/member/delivery_center")
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
	 * 删除发货地址
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock delete(Long id) {
		DeliveryCenter deliveryCenter = deliveryCenterService.find(id);
		if (deliveryCenter==null) {
			return DataBlock.error("无效的门店ID");
		}
		if (deliveryCenter.getIsDefault()) {
			return DataBlock.error("不能删除默认发货地址");
		}
		if (deliveryCenter.getEmployees().size()>0) {
			return DataBlock.error("有员工的门店不能删除。");
		}
		deliveryCenterService.delete(deliveryCenter);
		return DataBlock.success("success","执行成功");
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody DataBlock save(DeliveryCenter deliveryCenter, BigDecimal lat, BigDecimal lng, Long areaId, HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}

		try {
			if (deliveryCenter.getIsDefault()!=null && deliveryCenter.getIsDefault()) {
				for (DeliveryCenter d:member.getTenant().getDeliveryCenters()) {
					d.setIsDefault(false);
					deliveryCenterService.save(d);
				}
			}
			DeliveryCenter saveEntity = deliveryCenter;
			if (deliveryCenter.getId()!=null) {
				saveEntity = deliveryCenterService.find(deliveryCenter.getId());
				//saveEntity.setSn(deliveryCenter.getSn());
				saveEntity.setName(deliveryCenter.getName());
				saveEntity.setAddress(deliveryCenter.getAddress());
				saveEntity.setMobile(deliveryCenter.getMobile());
				if (deliveryCenter.getIsDefault()!=null) {
					saveEntity.setIsDefault(deliveryCenter.getIsDefault());
				}
				if (areaId!=null) {
					saveEntity.setArea(areaService.find(areaId));
					saveEntity.setAreaName(saveEntity.getArea().getFullName());
				}
			} else {
				saveEntity = new DeliveryCenter();
				saveEntity.setIsDefault(false);
				saveEntity.setScore(0F);
				saveEntity.setTotalScore(0L);
				saveEntity.setScoreCount(0L);
				saveEntity.setAssistant(0L);
				saveEntity.setSn(deliveryCenter.getSn());
				saveEntity.setName(deliveryCenter.getName());
				saveEntity.setAddress(deliveryCenter.getAddress());
				saveEntity.setMobile(member.getMobile());
				if (deliveryCenter.getIsDefault()!=null) {
					saveEntity.setIsDefault(deliveryCenter.getIsDefault());
				}
				if (member.getName()!=null) {
					saveEntity.setContact(member.getName());
				} else
				{
					saveEntity.setContact(member.getDisplayName());
				}
				saveEntity.setZipCode("000000");
				if (areaId!=null) {
					saveEntity.setArea(areaService.find(areaId));
					saveEntity.setAreaName(saveEntity.getArea().getFullName());
				}
			}
			saveEntity.setTenant(member.getTenant());
			saveEntity.setLocation(new Location(lat,lng));
			deliveryCenterService.save(saveEntity);
			DeliveryCenter def = saveEntity.getTenant().getDefaultDeliveryCenter();
			if (def!=null) {
				Tenant tenant = def.getTenant();
				tenant.setArea(def.getArea());
				tenant.setAddress(def.getAddress());
				tenantService.save(tenant);
			} else {
				saveEntity.setIsDefault(true);
				deliveryCenterService.save(saveEntity);
			}
			//activityDetailService.addPoint(null, member.getTenant(), activityRulesService.find(9L));
			return DataBlock.success("success","保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			return DataBlock.error("保存失败");
		}
	}

}
