/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.ajax;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import net.wit.Message;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 手机闪屏
 * @author rsico Team
 * @version 3.0
 */

@Controller("ajaxAdvController")
@RequestMapping("/ajax/adv")
public class AdvController extends BaseController {

	@RequestMapping(value = "/flash", method = RequestMethod.GET)
	@ResponseBody
	public Message flash(HttpServletRequest request) {
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		return Message.success(bundle.getString("flashMobile"));
	}

}