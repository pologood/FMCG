/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.plugin.oss;

import java.math.BigDecimal;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.controller.admin.BaseController;
import net.wit.entity.PluginConfig;
import net.wit.service.PluginConfigService;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 阿里云存储
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminPluginOssController")
@RequestMapping("/admin/storage_plugin/oss")
public class OssController extends BaseController {

	@Resource(name = "ossPlugin")
	private OssPlugin ossPlugin;
	@Resource(name = "pluginConfigServiceImpl")
	private PluginConfigService pluginConfigService;

	/**
	 * 安装
	 */
	@RequestMapping(value = "/install", method = RequestMethod.POST)
	public @ResponseBody
	Message install() {
		String specificationVersion = System.getProperty("java.specification.version");
		if (StringUtils.isNotEmpty(specificationVersion)) {
			BigDecimal version = new BigDecimal(specificationVersion);
			if (version.compareTo(new BigDecimal("1.6")) < 0) {
				return Message.error("admin.plugin.oss.unsupportedJavaVersion");
			}
		}
		if (!ossPlugin.getIsInstalled()) {
			PluginConfig pluginConfig = new PluginConfig();
			pluginConfig.setPluginId(ossPlugin.getId());
			pluginConfig.setIsEnabled(false);
			pluginConfigService.save(pluginConfig);
		}
		return SUCCESS_MESSAGE;
	}

	/**
	 * 卸载
	 */
	@RequestMapping(value = "/uninstall", method = RequestMethod.POST)
	public @ResponseBody
	Message uninstall() {
		if (ossPlugin.getIsInstalled()) {
			PluginConfig pluginConfig = ossPlugin.getPluginConfig();
			pluginConfigService.delete(pluginConfig);
		}
		return SUCCESS_MESSAGE;
	}

	/**
	 * 设置
	 */
	@RequestMapping(value = "/setting", method = RequestMethod.GET)
	public String setting(ModelMap model) {
		PluginConfig pluginConfig = ossPlugin.getPluginConfig();
		model.addAttribute("pluginConfig", pluginConfig);
		return "/net/wit/plugin/oss/setting";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(String accessId, String accessKey, String bucketName, String endpoint, String urlPrefix, String isCorpEnabled,@RequestParam(defaultValue = "false") Boolean isEnabled, Integer order, RedirectAttributes redirectAttributes) {
		PluginConfig pluginConfig = ossPlugin.getPluginConfig();
		pluginConfig.setAttribute("accessId", accessId);
		pluginConfig.setAttribute("accessKey", accessKey);
		pluginConfig.setAttribute("bucketName", bucketName);
		pluginConfig.setAttribute("endpoint", endpoint);
		pluginConfig.setAttribute("urlPrefix", StringUtils.removeEnd(urlPrefix, "/"));
		pluginConfig.setAttribute("isCorpEnabled", isCorpEnabled);
		pluginConfig.setIsEnabled(isEnabled);
		pluginConfig.setOrder(order);
		pluginConfigService.update(pluginConfig);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:/admin/storage_plugin/list.jhtml";
	}

}