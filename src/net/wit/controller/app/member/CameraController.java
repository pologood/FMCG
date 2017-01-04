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
package net.wit.controller.app.member;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.Filter;
import net.wit.Filter.Operator;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.CameraModel;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.EmployeeModel;
import net.wit.entity.Camera;
import net.wit.entity.DeliveryCenter;
import net.wit.entity.Employee;
import net.wit.entity.Location;
import net.wit.entity.Member;
import net.wit.entity.Member.BindStatus;
import net.wit.entity.SafeKey;
import net.wit.entity.SmsSend;
import net.wit.entity.SmsSend.Type;
import net.wit.entity.Tenant;
import net.wit.service.AreaService;
import net.wit.service.CameraService;
import net.wit.service.DeliveryCenterService;
import net.wit.service.EmployeeService;
import net.wit.service.MemberRankService;
import net.wit.service.MemberService;
import net.wit.service.SmsSendService;
import net.wit.support.EntitySupport;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;

/**
 * @ClassName: DeliveryCenterController
 * @Description:
 * @author Administrator
 * @date 2015年1月13日 下午1:21:42
 */
@Controller("appMemberCameraController")
@RequestMapping("/app/member/camera")
public class CameraController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "cameraServiceImpl")
	private CameraService cameraService;

	@Resource(name = "deliveryCenterServiceImpl")
	private DeliveryCenterService deliveryCenterService;

	
	/**
	 * 获取摄像头
	 */
	@RequestMapping(value = "/auth", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock auth() {
		Map<String,String> data = new HashMap<String,String>();
	    Member member = memberService.getCurrent();
	    if (member==null) {
	    	return DataBlock.error(DataBlock.SESSION_INVAILD);
	    }
		data.put("username",member.getUsername());
		data.put("password","dahua@120521");
		return DataBlock.success(data,"执行成功");
	}

	/**
	 * 获取摄像头
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(Pageable pageable,HttpServletRequest request) {
	    Member member = memberService.getCurrent();
	    if (member==null) {
	    	return DataBlock.error(DataBlock.SESSION_INVAILD);
	    }
	    Tenant tenant = member.getTenant();
	    if (tenant==null) {
	    	return DataBlock.error("还没有开通店铺，快去申请吧。");
	    }
		
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("tenant", Operator.eq, tenant));
		pageable.setFilters(filters);
	    Page<Camera> page = cameraService.findPage(pageable);
	    return DataBlock.success(CameraModel.bindData(page.getContent()),"执行成功");
	}

	/**
	 * 获取摄像头
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock update(Long id,Long deliveryCenterId,String name,HttpServletRequest request) {
	    Member member = memberService.getCurrent();
	    if (member==null) {
	    	return DataBlock.error(DataBlock.SESSION_INVAILD);
	    }
	    Tenant tenant = member.getTenant();
	    if (tenant==null) {
	    	return DataBlock.error("还没有开通店铺，快去申请吧。");
	    }
	    Camera camera = cameraService.find(id);
	    if (deliveryCenterId!=null) {
	    	camera.setDeliveryCenter(deliveryCenterService.find(deliveryCenterId));
	    }
	    camera.setTenant(camera.getDeliveryCenter().getTenant());
        cameraService.update(camera);
	    return DataBlock.success("success","执行成功");
	}
	
	/**
	 * 获取摄像头
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock delete(Long id,HttpServletRequest request) {
	    Member member = memberService.getCurrent();
	    if (member==null) {
	    	return DataBlock.error(DataBlock.SESSION_INVAILD);
	    }
	    Tenant tenant = member.getTenant();
	    if (tenant==null) {
	    	return DataBlock.error("还没有开通店铺，快去申请吧。");
	    }
	    Camera camera = cameraService.find(id);
	    cameraService.delete(camera);
	    return DataBlock.success("success","执行成功");
	}
	
	/**
	 * 添加会员
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock add(Long id,Long deliveryCenterId,String name,HttpServletRequest request, HttpServletResponse response) {
	    Member member = memberService.getCurrent();
	    if (member==null) {
	    	return DataBlock.error(DataBlock.SESSION_INVAILD);
	    }
	    Tenant tenant = member.getTenant();
	    if (tenant==null) {
	    	return DataBlock.error("还没有开通店铺，快去申请吧。");
	    }
	    DeliveryCenter deliveryCenter = deliveryCenterService.find(deliveryCenterId);
	    if (deliveryCenter==null) {
	    	return DataBlock.error("无效门店id。");
	    }
		
	    Camera camera = new Camera();
	    if (deliveryCenterId!=null) {
	    	camera.setDeliveryCenter(deliveryCenterService.find(deliveryCenterId));
	    }
	    camera.setTenant(camera.getDeliveryCenter().getTenant());
        cameraService.save(camera);
		return DataBlock.success("success","添加成功");
	}
}
