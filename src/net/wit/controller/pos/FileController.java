/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.pos;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import freemarker.template.TemplateException;
import net.wit.FileInfo;
import net.wit.FileInfo.FileType;
import net.wit.FileInfo.OrderType;
import net.wit.Setting;
import net.wit.controller.app.model.DataBlock;
import net.wit.plugin.StoragePlugin;
import net.wit.service.FileService;
import net.wit.service.PluginService;
import net.wit.util.FreemarkerUtils;
import net.wit.util.SettingUtils;

/**
 * Controller - 文件处理
 * @author rsico Team
 * @version 3.0
 */
@Controller("posFileController")
@RequestMapping("/pos/file")
public class FileController extends BaseController {

	@Resource(name = "fileServiceImpl")
	private FileService fileService;
	
	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;  

	/**
	 * 上传文件
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody DataBlock upload(MultipartFile file,HttpServletRequest request) {
		String url = fileService.upload(null, file, false);
		return DataBlock.success(url,"success");
	}

	/**
	 * 上传文件
	 */
	@RequestMapping(value = "/upload_to_temp", method = RequestMethod.POST)
	public @ResponseBody DataBlock uploadTemp(MultipartFile file,HttpServletRequest request) {
		File tempFile = new File(System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".tmp");
		if (!tempFile.getParentFile().exists()) {
			tempFile.getParentFile().mkdirs();
		}
			try {
				file.transferTo(tempFile);
			} catch (IllegalStateException e1) { 
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return DataBlock.error("上传图片失败了，再试试。");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return DataBlock.error("上传图片失败了，再试试。");
			}
				Setting setting = SettingUtils.get();
				Map<String, Object> data = new HashMap<String, Object>();
				String uploadPath = setting.getImageUploadPath();
				data.put("fileType", FileType.image);
				Map<String, Object> model = new HashMap<String, Object>();
				model.put("uuid", UUID.randomUUID().toString());
				String path;
						try {
							path = FreemarkerUtils.process(uploadPath, model);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return DataBlock.error("上传图片失败了，再试试。");
						} catch (TemplateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							return DataBlock.error("上传图片失败了，再试试。");
						}
				String destPath = path + UUID.randomUUID() + ".jpg";
				
				StoragePlugin storagePlugin = pluginService.getStoragePlugin("filePlugin");
				storagePlugin.upload(destPath, tempFile,"image/jpeg");
				String url = storagePlugin.getUrl(destPath);
				ServletContext servletContext = request.getSession().getServletContext();
				data.put("local",servletContext.getRealPath(destPath));
				
				if (url == null) {
					return DataBlock.warn("admin.upload.error");
				} else {
					return DataBlock.success(servletContext.getRealPath(destPath), "success");
				}
	}

	/**
	 * 浏览
	 */
	@RequestMapping(value = "/browser", method = RequestMethod.GET)
	public @ResponseBody DataBlock browser(String path, FileType fileType, OrderType orderType) {
		 List<FileInfo> infos = fileService.browser(path, fileType, orderType);
		 return DataBlock.success(infos,"success");
	}

}