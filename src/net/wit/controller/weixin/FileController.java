/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.weixin;

import freemarker.template.TemplateException;
import net.wit.FileInfo;
import net.wit.FileInfo.FileType;
import net.wit.FileInfo.OrderType;
import net.wit.Setting;
import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.DataBlock;
import net.wit.plugin.StoragePlugin;
import net.wit.service.FileService;
import net.wit.service.PluginService;
import net.wit.util.FreemarkerUtils;
import net.wit.util.SettingUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Controller - 文件处理
 * @author rsico Team
 * @version 3.0
 */
@Controller("weixinFileController")
@RequestMapping("/weixin/file")
public class FileController extends BaseController {

	@Resource(name = "fileServiceImpl")
	private FileService fileService;
	
	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;  

	/**
	 * 图片上传接口
	 * @param file 文件
	 * @return 图片url
	 */
	@RequestMapping(value = "/upload_image", method = RequestMethod.POST)
	public @ResponseBody DataBlock uploadImage(MultipartFile file) {
		FileType fileType = FileType.image;
		if (!fileService.isValid(fileType, file)) {
			return DataBlock.error("admin.upload.error");
		} else {
			String url = fileService.upload(null, file, false);
			if (url == null) {
				return DataBlock.error("admin.upload.error");
			} else {
				return DataBlock.success(url, "执行成功");
			}
		}
	}

	/**
	 * 浏览
	 */
	@RequestMapping(value = "/browser", method = RequestMethod.GET)
	public @ResponseBody DataBlock browser(String path, FileType fileType, OrderType orderType) {
		 List<FileInfo> infos = fileService.browser(path, fileType, orderType);
		 return DataBlock.success(infos,"执行成功");
	}

}