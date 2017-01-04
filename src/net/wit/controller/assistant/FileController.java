/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.assistant;

import freemarker.template.TemplateException;
import net.wit.FileInfo;
import net.wit.FileInfo.FileType;
import net.wit.FileInfo.OrderType;
import net.wit.Setting;
import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.DataBlock;
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
@Controller("assistantFileController")
@RequestMapping("/assistant/file")
public class FileController extends BaseController {

	@Resource(name = "fileServiceImpl")
	private FileService fileService;

	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;

	/**
	 * 上传 file
	 */
	@RequestMapping(value = "/upload_image", method = RequestMethod.POST)
	public @ResponseBody DataBlock uploadImage(FileType fileType,MultipartFile file) {
		fileType = FileType.image;
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
	 * 上传文件
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody DataBlock upload(HttpServletRequest request) {
		File tempFile = new File(System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".tmp");
		if (!tempFile.getParentFile().exists()) {
			tempFile.getParentFile().mkdirs();
		}
		FileType fileType = FileType.image;
		String originalFileName = "10.jpg";
		// 输入流
		InputStream in;
		try {
			in = request.getInputStream();
			FileOutputStream out = new FileOutputStream(tempFile);

			byte[] buffer = new byte[1024];
			int length = -1;
			/* 从文件读取数据至缓冲区 */
			while ((length = in.read(buffer)) != -1) {
				/* 将资料写入FileOutputStream中 */
				out.write(buffer, 0, length);
			}
			in.close();
			out.flush();
			out.close();

			String contentType="";

			if (FileType.image.equals(fileType) ) {
				contentType = "image/jpeg";
			} else {
				contentType = "image/jpeg";
			}
			if (!fileService.isValid(fileType,in.available(), originalFileName)) {
				return DataBlock.error("admin.upload.invalid");
			} else {
				String url = fileService.upload(fileType,tempFile,false,FilenameUtils.getExtension(originalFileName),contentType);
				if (url == null) {
					return DataBlock.warn("admin.upload.error");
				} else {
					return DataBlock.success(url, "执行成功");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return DataBlock.error("admin.upload.invalid");
		}
	}

	/**
	 * 上传文件
	 */
	@RequestMapping(value = "/upload_to_temp_image", method = RequestMethod.POST)
	public @ResponseBody DataBlock uploadTempImage(FileType fileType,MultipartFile file,HttpServletRequest request) {
		File tempFile = new File(System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".tmp");
		if (!tempFile.getParentFile().exists()) {
			tempFile.getParentFile().mkdirs();
		}
		try {
			file.transferTo(tempFile);
			Setting setting = SettingUtils.get();
			String uploadPath = setting.getImageUploadPath();
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("uuid", UUID.randomUUID().toString());
			String path;
			try {
				path = FreemarkerUtils.process(uploadPath, model);
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
			if (url == null) {
				return DataBlock.warn("admin.upload.error");
			} else {
				return DataBlock.success(servletContext.getRealPath(destPath), "执行成功");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return DataBlock.error("上传图片失败了，再试试。");
		}
	}

	/**
	 * 上传文件
	 */
	@RequestMapping(value = "/upload_to_temp", method = RequestMethod.POST)
	public @ResponseBody DataBlock uploadTemp(HttpServletRequest request) {
		File tempFile = new File(System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".tmp");
		if (!tempFile.getParentFile().exists()) {
			tempFile.getParentFile().mkdirs();
		}

		// 输入流
		InputStream in;
		try {
			in = request.getInputStream();
			FileOutputStream out = new FileOutputStream(tempFile);

			byte[] buffer = new byte[1024];
			int length = -1;
			/* 从文件读取数据至缓冲区 */
			while ((length = in.read(buffer)) != -1) {
				/* 将资料写入FileOutputStream中 */
				out.write(buffer, 0, length);
			}
			in.close();
			out.flush();
			out.close();

			Setting setting = SettingUtils.get();
			Map<String, Object> data = new HashMap<String, Object>();
			String uploadPath = setting.getImageUploadPath();
			data.put("fileType", FileType.image);
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("uuid", UUID.randomUUID().toString());
			String path;
			try {
				path = FreemarkerUtils.process(uploadPath, model);
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
				return DataBlock.success(servletContext.getRealPath(destPath), "执行成功");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return DataBlock.error("上传图片失败了，再试试。");
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