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
import java.util.Date;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wit.entity.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.Filter.Operator;
import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.EmployeeModel;
import net.wit.controller.app.model.MemberModel;
import net.wit.entity.Member.BindStatus;
import net.wit.entity.SmsSend.Type;
import net.wit.service.AreaService;
import net.wit.service.DeliveryCenterService;
import net.wit.service.EmployeeService;
import net.wit.service.MemberRankService;
import net.wit.service.MemberService;
import net.wit.service.SmsSendService;
import net.wit.service.TenantService;
import net.wit.support.EntitySupport;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;

/**
 * @ClassName: DeliveryCenterController
 * @Description:
 * @author Administrator
 * @date 2015年1月13日 下午1:21:42
 */
@Controller("appB2cEmployeeController")
@RequestMapping("/app/B2c/employee")
public class EmployeeController extends BaseController {

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "employeeServiceImpl")
	private EmployeeService employeeService;
	
	/**
	 * 获取员工
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
	 * 获取员工列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(Long id, Role role, Pageable pageable, HttpServletRequest request) {
		
	    Tenant tenant = tenantService.find(id);
	    if (tenant==null) {
	    	return DataBlock.error("还没有开通店铺，快去申请吧。");
	    }
	    
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("tenant", Operator.eq, tenant));
		//filters.add(new Filter("role", Operator.like, "%"+role.getId()+"%"));
		pageable.setFilters(filters);
	    
	    Page<Employee> page = employeeService.findPage(pageable);
	    return DataBlock.success(EmployeeModel.bindData(page.getContent(),null),"执行成功");
	}

}
