/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.b2b;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wit.FileInfo;
import net.wit.FileInfo.FileType;
import net.wit.FileInfo.OrderType;
import net.wit.plugin.StoragePlugin;
import net.wit.Message;
import net.wit.Setting;
import net.wit.service.FileService;
import net.wit.service.PluginService;
import net.wit.util.FreemarkerUtils;
import net.wit.util.ImageUtils;
import net.wit.util.JsonUtils;
import net.wit.util.SettingUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import freemarker.template.TemplateException;

/**
 * Controller - 文件处理
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("b2bFileController")
@RequestMapping("/b2b/file")
public class FileController extends BaseController {
	/** 目标扩展名 */
	private static final String DEST_EXTENSION = "jpg";

	/** 目标文件类型 */
	private static final String DEST_CONTENT_TYPE = "image/jpeg";
	
	@Resource(name = "pluginServiceImpl")
	private PluginService pluginService;

	@Resource(name = "fileServiceImpl")
	private FileService fileService;

	/**
	 * 上传
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "text/html; charset=UTF-8")
	public void upload(FileType fileType, MultipartFile file, HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (!fileService.isValid(fileType, file)) {
			data.put("message", Message.warn("admin.upload.invalid"));
		} else {
			String url = fileService.upload(fileType, file, false);
			if (url == null) {
				data.put("message", Message.warn("admin.upload.error"));
			} else {
				data.put("message", SUCCESS_MESSAGE);
				data.put("url", url);
			}
		}
		try {
			response.setContentType("text/html; charset=UTF-8");
			JsonUtils.writeValue(response.getWriter(), data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 上传
	 */
	@RequestMapping(value = "/upload-crop", method = RequestMethod.POST, produces = "text/html; charset=UTF-8")
	public void upload(FileType fileType, MultipartFile file,HttpServletRequest request,  HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (!fileService.isValid(fileType, file)) {
			data.put("message", Message.warn("admin.upload.invalid"));
		} else {
			if (file == null) {
				data.put("message", Message.warn("admin.upload.error"));
			} else {
				Setting setting = SettingUtils.get();
				String uploadPath;
				String url = null;
				if (fileType == FileType.flash || fileType == FileType.media || fileType == FileType.file) {
					url = fileService.upload(fileType, file, false);
					data.put("fileType", fileType);
				} else {
					uploadPath = setting.getImageUploadPath();
					data.put("fileType", FileType.image);
					Map<String, Object> model = new HashMap<String, Object>();
					model.put("uuid", UUID.randomUUID().toString());
					String path;
					try {
						path = FreemarkerUtils.process(uploadPath, model);
						String destPath = path + UUID.randomUUID() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
						File tempFile = new File(System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".tmp");
						if (!tempFile.getParentFile().exists()) {
							tempFile.getParentFile().mkdirs();
						}
						file.transferTo(tempFile);
						StoragePlugin storagePlugin = pluginService.getStoragePlugin("filePlugin");
						storagePlugin.upload(destPath, tempFile, file.getContentType());
						url = storagePlugin.getUrl(destPath);
						ServletContext servletContext = request.getSession().getServletContext();
						data.put("local",servletContext.getRealPath(destPath));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (TemplateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (url == null) {
					data.put("message", Message.warn("admin.upload.error"));
				} else {
					data.put("message", SUCCESS_MESSAGE);
					data.put("url", url);
				}
			}
		}
		try {
			response.setContentType("text/html; charset=UTF-8");
			JsonUtils.writeValue(response.getWriter(), data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 裁切原图
	 */
	@RequestMapping(value = "/jcrop", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> jcrop(String local,float x,float y,float w,float h, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		String url = null;
		if (w==0) {
			data.put("message",Message.warn("请先选择范围再裁切"));
		} else {
			File sourceTempFile = new File(local);
			try {
				String tempPath = System.getProperty("java.io.tmpdir");
				File destTempFile = new File(tempPath + "/upload_" + UUID.randomUUID() + "." + DEST_EXTENSION);
				if (!destTempFile.getParentFile().exists()) {
					destTempFile.getParentFile().mkdirs();
				}
				BufferedImage srcBufferedImage;
				try {
					srcBufferedImage = ImageIO.read(sourceTempFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					data.put("message",Message.warn("打开原图片出错了"));
					return data;
				}
				
				float scale = 1f;
	            if ((600f/400f)>=( srcBufferedImage.getWidth()/srcBufferedImage.getHeight()))
	            {  
	               //重新设置img的width和height  
					scale = 400f/srcBufferedImage.getHeight();
	            }  
	            else  
	            {  
	               //重新设置img的width和height  
					scale = 600f/srcBufferedImage.getWidth();
	            }  				
				
			    int imgX=Math.round(x/scale);
			    int imgY=Math.round(y/scale);
			    int imgW=Math.round(w/scale);
			    int imgH=Math.round(h/scale);
				
				ImageUtils.crop(sourceTempFile, destTempFile,imgX,imgY,imgW,imgH);
				Setting setting = SettingUtils.get();
				String uploadPath;
				String path;
				uploadPath = setting.getImageUploadPath();
				Map<String, Object> model = new HashMap<String, Object>();
				try {
					path = FreemarkerUtils.process(uploadPath, model);
					String destPath = path + UUID.randomUUID() + "." + DEST_EXTENSION;
					StoragePlugin storagePlugin = pluginService.getStoragePlugin("filePlugin");
					storagePlugin.upload(destPath, destTempFile, DEST_CONTENT_TYPE);
					ServletContext servletContext = request.getSession().getServletContext();
					url = storagePlugin.getUrl(destPath);
					data.put("local",servletContext.getRealPath(destPath));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TemplateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} finally {
			}
			if (url == null) {
				data.put("message", Message.warn("admin.upload.error"));
			} else {
				data.put("message", SUCCESS_MESSAGE);
				data.put("url", url);
				FileUtils.deleteQuietly(sourceTempFile);
			}
		}
		return data;
	}

	
	/**
	 * 提交上传
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> submit(String url,String local,Integer w,Integer h,Boolean isUpload, HttpServletRequest request,HttpServletResponse response) {
		
		Map<String, Object> data = new HashMap<String, Object>();
		if (!isUpload) {
			data.put("message", SUCCESS_MESSAGE);
			data.put("url", url);
			data.put("local", local);
		}
		File sourceTempFile = new File(local);
		if (w==null || w==0) {
			url = fileService.upload(FileType.image, sourceTempFile, false, DEST_EXTENSION,DEST_CONTENT_TYPE);
			data.put("message", SUCCESS_MESSAGE);
			data.put("url", url);
			FileUtils.deleteQuietly(sourceTempFile);
		} else {
			url = fileService.upload(FileType.image, sourceTempFile, false, w, h,DEST_EXTENSION,DEST_CONTENT_TYPE);
			data.put("message", SUCCESS_MESSAGE);
			data.put("url", url);
			FileUtils.deleteQuietly(sourceTempFile);
		}
		return data;
	}

	/**
	 * 浏览
	 */
	@RequestMapping(value = "/browser", method = RequestMethod.GET)
	public @ResponseBody
	List<FileInfo> browser(String path, FileType fileType, OrderType orderType) {
		return fileService.browser(path, fileType, orderType);
	}

}