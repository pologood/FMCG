package net.wit.controller.helper;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.UUID;

@Controller("helperIndexController")
@RequestMapping("/helper/index")
public class IndexController extends BaseController {

	/**
	 * 主页面
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute("captchaId", UUID.randomUUID().toString());

		//return "/helper/login/index";

		return "redirect:/store/index.jhtml";
	}

}
