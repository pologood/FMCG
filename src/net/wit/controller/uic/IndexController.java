package net.wit.controller.uic;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.wit.entity.Member;
import net.wit.entity.Tenant;
import net.wit.service.AreaService;
import net.wit.service.MemberService;
import net.wit.service.ProductService;
import net.wit.service.TenantService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("indexController")
@RequestMapping("/")
public class IndexController extends BaseController{

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;
	/**
	 * 主页面
	 */
	@RequestMapping(value = "/index",method = RequestMethod.GET)
	public String index(String redirectUrl, HttpServletRequest request, ModelMap model, HttpSession session) {
		Tenant tenant = tenantService.getCurrent();
		if (tenant!=null) {
			return "redirect:/b2b/"+tenant.getId()+"/index.jhtml";
		}
		String host = request.getServerName();
		String[] hosts = host.split("\\.");
		if (hosts.length > 2 && hosts[0].equals("box")) {
			return "redirect:/box/index.jhtml";
		} else {
			return "redirect:/b2b/index.jhtml";
		}
	}
}
