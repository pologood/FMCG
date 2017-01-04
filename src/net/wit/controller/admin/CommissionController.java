package net.wit.controller.admin;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Admin;
import net.wit.entity.Commission;
import net.wit.service.AdminService;
import net.wit.service.CommissionService;

@Controller("adminCommissionController")
@RequestMapping("/admin/commission")
public class CommissionController extends BaseController {

	@Resource(name = "commissionServiceImpl")
	private CommissionService commissionService;

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/statisticslist", method = RequestMethod.GET)
	public String statisticslist(Pageable pageable, ModelMap model) {
//		Admin admin = adminService.getCurrent();
//		Page<CommissionRecords> page = commissionRecodsService.findPage(admin, pageable);
		model.addAttribute("page", "60");
		return "/admin/commission/statisticslist";
	}

	
	/**
	 * 列表
	 */
	@RequestMapping(value = "/commissionlist", method = RequestMethod.GET)
	public String commissionlist(Pageable pageable, ModelMap model) {
		Admin admin = adminService.getCurrent();
		Page<Commission> page = commissionService.findPage(admin, pageable);
		model.addAttribute("page", page);
		return "/admin/commission/commissionlist";
	}
}
