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
import java.util.HashSet;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wit.entity.*;
import net.wit.service.*;
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
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.EmployeeModel;
import net.wit.entity.Member.BindStatus;
import net.wit.entity.Member.Gender;
import net.wit.entity.SmsSend.Type;
import net.wit.support.EntitySupport;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;

/**
 * @ClassName: DeliveryCenterController
 * @Description:
 * @author Administrator
 * @date 2015年1月13日 下午1:21:42
 */
@Controller("appMemberEmployeeController")
@RequestMapping("/app/member/employee")
public class EmployeeController extends BaseController {
	public static final String REGISTER_SECURITYCODE_SESSION = "register_safe_key";
	public static final String REGISTER_CONTENT_SESSION = "register_mobile";

	@Resource(name = "deliveryCenterServiceImpl")
	private DeliveryCenterService deliveryCenterService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "activityDetailServiceImpl")
	private ActivityDetailService activityDetailService;

	@Resource(name = "activityRulesServiceImpl")
	private ActivityRulesService activityRulesService;

	@Resource(name = "employeeServiceImpl")
	private EmployeeService employeeService;
	
	@Resource(name = "smsSendServiceImpl")
	private SmsSendService smsSendService;
	
	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;


	@Resource(name = "roleServiceImpl")
	private RoleService roleService;
	/**
	 * 获取发货地址
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock view(Long id) {
		Employee emp = employeeService.find(id);
		EmployeeModel model = new EmployeeModel();
		model.copyFrom(emp,null);
		return DataBlock.success(model,"执行成功");
	}

	/**
	 * 获取发货地址
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(Location location,Pageable pageable,HttpServletRequest request) {
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
	    
		pageable.setFilters(filters);
	    Page<Employee> page = employeeService.findPage(pageable);
		//店主返回店主owner，其他返回店长manager
		for(int i=0;i<page.getContent().size();i++){
			Member _Member=page.getContent().get(i).getMember();
			String role=",owner";
			if (!_Member.equals(tenant.getMember())){
				role=",manager";
			}
			page.getContent().get(i).setRole(role);
		}
	    return DataBlock.success(EmployeeModel.bindData(page.getContent(),null),"执行成功");
	}

	/**
	 * 获取发货地址
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock update(Long[] tagIds,Long id,Long deliveryCenterId,Long[] roles,String username,String gender,String address,Boolean isSetGuiderStar,HttpServletRequest request) {
	    Member member = memberService.getCurrent();
	    if (member==null) {
	    	return DataBlock.error(DataBlock.SESSION_INVAILD);
	    }
	    Tenant tenant = member.getTenant();
	    if (tenant==null) {
	    	return DataBlock.error("还没有开通店铺，快去申请吧。");
	    }
	    Employee employee = employeeService.find(id);
	    Member member_employee=EntitySupport.createInitMember();
	    if(employee!=null){
	    	member_employee=employee.getMember();
	    	member_employee.setName(username);
	    	member_employee.setAddress(address);
	    	if("male".equals(gender)){
	    		member_employee.setGender(Gender.male);
	    	}else{
	    		member_employee.setGender(Gender.female);
	    	}
	    }
	    memberService.update(member_employee);
	    employee.setMember(member_employee);
	    if (deliveryCenterId!=null) {
		    employee.setDeliveryCenter(deliveryCenterService.find(deliveryCenterId));
	    }
	    if (roles!=null) {
    	    String role="";
	        for (Long rl:roles) {
	        	role = role.concat(",".concat(rl.toString()));
	        }
            employee.setRole(role);
	    }

		employee.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
        employeeService.update(employee);
	    return DataBlock.success("success","执行成功");
	}
	
	/**
	 * 获取发货地址
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
	    Employee employee = employeeService.find(id);
	    if (employee.getMember().equals(tenant.getMember())) {
	    	return DataBlock.error("店主不能删除");
	    }
	    
	    employee.getMember().setTenant(null);
	    memberService.update(employee.getMember());
	    employeeService.delete(employee);
	    return DataBlock.success("success","执行成功");
	}
	
	/**
	 * 发送手机
	 */
	@RequestMapping(value = "/send_mobile", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock sendMobile(String mobile,HttpServletRequest request) {
		if (mobile==null) {
		   return DataBlock.error("手机号无效");	
		}
		HttpSession session = request.getSession();
		Setting setting = SettingUtils.get();
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		int challege = SpringUtils.getIdentifyingCode();
		String securityCode = String.valueOf(challege);
		SafeKey tmp = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
		if (tmp!=null && !tmp.hasExpired()) {
			securityCode = tmp.getValue();
			if (!tmp.canReset()) {
				return DataBlock.error("系统忙，稍等几秒重试");
			}
		}
		SafeKey safeKey = new SafeKey();
		safeKey.setValue(securityCode);
		safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()) : null);
		session.setAttribute(REGISTER_SECURITYCODE_SESSION, safeKey);
		session.setAttribute(REGISTER_CONTENT_SESSION, mobile);
		SmsSend smsSend=new SmsSend();
		smsSend.setMobiles(mobile);
		smsSend.setContent("验证码 :" + securityCode + ",用于确认店主与员工关系时使用，请放心告知店主，店家助手、导购无忧。【" + bundle.getString("signature") + "】");
		smsSend.setType(Type.captcha);
		smsSendService.smsSend(smsSend);
		return DataBlock.success("success","发送成功");
	}

	/**
	 * 检查验证码
	 */
	@RequestMapping(value = "/check_captcha", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock checkCaptcha(String captcha,Boolean remove,HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		SafeKey safeKey = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
		if (safeKey == null) {
			return DataBlock.error("验证码过期了");
		}
		if (safeKey.hasExpired()) {
			return DataBlock.error("验证码过期了");
		}
		if (!safeKey.getValue().equals(captcha)) {
			return DataBlock.error("验证码不正确");
		}
        return DataBlock.success("success","执行成功");
	}
	
	/**
	 * 添加会员
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock add(String mobile,Long deliveryCenterId,String captcha,HttpServletRequest request, HttpServletResponse response) {
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
		HttpSession session = request.getSession();
		SafeKey safeKey = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
		session.removeAttribute(REGISTER_SECURITYCODE_SESSION);
		session.removeAttribute(REGISTER_CONTENT_SESSION);
		if (safeKey == null) {
			return DataBlock.error("验证码过期了");
		}
		if (safeKey.hasExpired()) {
			return DataBlock.error("验证码过期了");
		}
		if (!safeKey.getValue().equals(captcha)) {
			return DataBlock.error("验证码不正确");
		}

		Member employee = null;
		if (!memberService.usernameExists(mobile) && !memberService.mobileExists(mobile)) {
			String password = mobile.substring(mobile.length()-6, mobile.length());

			Setting setting = SettingUtils.get();
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
			if (!setting.getIsRegisterEnabled()) {
				return DataBlock.error("系统关闭注册");
			}
			employee = EntitySupport.createInitMember();
    
			employee.setArea(deliveryCenter.getArea());
			employee.setUsername(mobile);
			employee.setPassword(DigestUtils.md5Hex(password));
			employee.setPoint(setting.getRegisterPoint());
			employee.setAmount(new BigDecimal(0));
			employee.setBalance(new BigDecimal(0));
			employee.setIsEnabled(true);
			employee.setIsLocked(Member.LockType.none);
			employee.setLoginFailureCount(0);
			employee.setLockedDate(null);
			employee.setRegisterIp(request.getRemoteAddr());
			employee.setLoginIp(request.getRemoteAddr());
			employee.setLoginDate(new Date());
			employee.setSafeKey(null);
			employee.setBindEmail(Member.BindStatus.none);
			employee.setBindMobile(Member.BindStatus.none);
			employee.setPaymentPassword(DigestUtils.md5Hex(password));
			employee.setRebateAmount(new BigDecimal(0));
			employee.setProfitAmount(new BigDecimal(0));
			employee.setMemberRank(memberRankService.findDefault());
			employee.setFavoriteProducts(null);
			employee.setFreezeBalance(new BigDecimal(0));
			employee.setPrivilege(0);
			employee.setTotalScore((long) 0);
			
		    employee.setMember(member);
		    employee.setMobile(mobile);
		    employee.setEmail("@");
		    employee.setMobile(mobile);
		    employee.setBindMobile(BindStatus.binded);
		    
		    SmsSend smsSend=new SmsSend();
			smsSend.setMobiles(employee.getUsername());
			smsSend.setContent("注册成功,账号:" + employee.getUsername() +" 默认密码:"+password+ "【" + bundle.getString("signature") + "】");
			smsSend.setType(Type.captcha);
			smsSendService.smsSend(smsSend);
		} else {
			employee = memberService.findByUsername(mobile);
			if (employee==null) {
				employee = memberService.findByBindTel(mobile);
			}
			if (employee.getTenant()!=null && !employee.getTenant().equals(tenant) && tenant.getMember().equals(employee) && employee.getTenant().getStatus().equals(Tenant.Status.success)) {
				return DataBlock.error("此会员正在开店中");
			}
			List<Filter> filters = new ArrayList<Filter>();
			filters.add(new Filter("tenant", Operator.eq, tenant));
			filters.add(new Filter("member", Operator.eq, employee));
			Pageable pageable = new Pageable();
			pageable.setFilters(filters);
		    Page<Employee> page = employeeService.findPage(pageable);
		    if (page.getContent().size()>0) {
		    	return DataBlock.error("已经是你的员工了");
		    }
		}
	    employee.setTenant(tenant);
		memberService.save(employee);
		
		
		Employee emp = new Employee();
		emp.setDeliveryCenter(deliveryCenter);
		emp.setMember(employee);
		emp.setTenant(tenant);
//	    emp.setRole(",guide");
		//设置默认为导购
		Pageable pageable = new Pageable();
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("tenant", Filter.Operator.eq, tenant));
		pageable.setFilters(filters);
		Page<Role> roleList=roleService.findPage(Role.RoleType.helper,pageable);
//		if (roleList.getContent().size()<0){
//			filters.clear();
//			filters.add(new Filter("roleType", Filter.Operator.eq, 1));
//			pageable.setFilters(filters);
//			roleList=roleService.findPage(Role.RoleType.guide,pageable);
//		}
		if(roleList.getContent().size()>0)
		{
			emp.setRole(",guide,"+roleList.getContent().get(0).getId());
		}else{
			return DataBlock.error("导购权限未设置,请先设置角色权限");
		}

	    emp.setQuertity(0);
	    employeeService.save(emp);

		if(!activityDetailService.isActivity(null,tenant, activityRulesService.find(8L))){
			activityDetailService.addPoint(null,tenant,activityRulesService.find(8L));
		}

		if(!activityDetailService.isActivity(null,tenant, activityRulesService.find(9L))){
			activityDetailService.addPoint(null,tenant,activityRulesService.find(9L));
		}

		return DataBlock.success("success","注册成功");
	}
}
