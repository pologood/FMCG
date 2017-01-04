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
package net.wit.controller.ajax;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.Message;
import net.wit.entity.DeliveryCenter;
import net.wit.entity.Member;
import net.wit.service.AreaService;
import net.wit.service.DeliveryCenterService;
import net.wit.service.MemberService;
import net.wit.util.JsonUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName: DeliveryCenterController
 * @Description:
 * @author Administrator
 * @date 2015年1月13日 下午1:21:42
 */
@Controller("ajaxDeliveryCenterController")
@RequestMapping("/ajax/deliveryCenter")
public class DeliveryCenterController extends BaseController {

	@Resource(name = "deliveryCenterServiceImpl")
	private DeliveryCenterService deliveryCenterService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	
	/**
	 * 获取发货地址
	 */
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Message detail(@PathVariable Long id) {
		DeliveryCenter deliveryCenter = deliveryCenterService.find(id);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("lng", deliveryCenter.getLocation() == null ? "" : deliveryCenter.getLocation().getLng().toString());
		map.put("lat", deliveryCenter.getLocation() == null ? "" : deliveryCenter.getLocation().getLat().toString());
		map.put("tenantName", deliveryCenter.getTenant().getName());
		map.put("tenantId", deliveryCenter.getTenant().getId().toString());
		map.put("id", deliveryCenter.getId().toString());
		return Message.success(JsonUtils.toJson(map));
	}

	/**
	 * 获取发货地址详情
	 */
	@RequestMapping(value = "/info/{id}", method = RequestMethod.GET)
	@ResponseBody
	public DeliveryCenter info(@PathVariable Long id) {
		DeliveryCenter deliveryCenter = deliveryCenterService.find(id);
		return deliveryCenter;
	}
	
	/**
	 * 获取发货地址
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public List<DeliveryCenter> list(HttpServletRequest request) {
	    Member member = memberService.getCurrent();
	    return deliveryCenterService.findMyAll(member);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody Message save(DeliveryCenter deliveryCenter,Long areaId, HttpServletRequest request) {
	    Member member = memberService.getCurrent();
	    if (member==null) {
	 	   return Message.error("会话已经期过，请重新登录");
	    }
	    try {
		    DeliveryCenter saveEntity = deliveryCenter;
		    if (deliveryCenter.getId()!=null) {
		    	saveEntity = deliveryCenterService.find(deliveryCenter.getId());
		    	saveEntity.setSn(deliveryCenter.getSn());
		    	saveEntity.setName(deliveryCenter.getName());
		    	saveEntity.setAddress(deliveryCenter.getAddress());
		    	if (areaId!=null) {
		    	   saveEntity.setArea(areaService.find(areaId));
		    	   saveEntity.setAreaName(saveEntity.getArea().getFullName());
		    	}
		    	
		    	if (deliveryCenter.getLocation()!=null) {
		    		saveEntity.setLocation(deliveryCenter.getLocation());
		    	}
		    		
		    } else {
		    	saveEntity.setScore(0F);
		    	saveEntity.setTotalScore(0L);
		    	saveEntity.setScoreCount(0L);
		    	saveEntity.setContact(member.getUsername());
		    	saveEntity.setMobile(member.getMobile());
		    	saveEntity.setPhone(member.getMobile());
		    	if (areaId!=null) {
			    	   saveEntity.setArea(areaService.find(areaId));
			    	   saveEntity.setAreaName(saveEntity.getArea().getFullName());
			    	}
		    }
		    saveEntity.setTenant(member.getTenant());
		    deliveryCenterService.save(saveEntity);
		    
		    return Message.success("保存成功");
	    } catch (Exception e) {
	    	return Message.success("保存失败");	
	    }
	}
}
