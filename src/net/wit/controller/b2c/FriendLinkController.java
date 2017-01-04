/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.b2c;

import javax.annotation.Resource;

import net.wit.entity.FriendLink.Type;
import net.wit.service.FriendLinkService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller - 友情链接
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("b2cFriendLinkController")
@RequestMapping("/b2c/friend_link")
public class FriendLinkController extends BaseController {

	@Resource(name = "friendLinkServiceImpl")
	private FriendLinkService friendLinkService;

	/**
	 * 首页
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		model.addAttribute("textFriendLinks", friendLinkService.findList(Type.text));
		model.addAttribute("imageFriendLinks", friendLinkService.findList(Type.image));
		return "/b2c/friend_link/index";
	}

}