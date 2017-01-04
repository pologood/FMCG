package net.wit.controller.store;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

@Controller("storeIndexController")
@RequestMapping("/store/index")
public class IndexController extends BaseController {

	/**
	 * 主页面
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String index(HttpServletRequest request,ModelMap model) {
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		if (request.getRemoteHost().contains("vsstoo")) {
			return "redirect:/b2c/index.jhtml";
		}
		return "/store/login/index";
	}

}
