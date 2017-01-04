package net.wit.controller.admin;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.wit.FileInfo.FileType;
import net.wit.Message;
import net.wit.Pageable;
import net.wit.entity.Admin;
import net.wit.entity.ReportFile;
import net.wit.service.AdminService;
import net.wit.service.FileService;
import net.wit.service.ReportFileService;
import net.wit.util.JsonUtils;

/**
 * Controller - 文件处理
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminIWReportController")
@RequestMapping("/admin/IWReport")
public class IWReportController extends BaseController{
	/** 目标扩展名 */
	private static final String DEST_EXTENSION = "cpt";

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;
	
	@Resource(name = "fileServiceImpl")
	private FileService fileService;
	
	@Resource(name = "reportFileServiceImpl")
	private ReportFileService reportFileService;
	     
	/**
	 * 上传
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "text/html; charset=UTF-8")
	public void upload(FileType fileType, MultipartFile multipartFile, HttpServletResponse response) {
		Map<String, Object> data = new HashMap<String, Object>();
		System.out.println("upload");
		if (!fileService.isValid(fileType, multipartFile)) {
			data.put("message", Message.warn("admin.upload.invalid"));
		} else {
			String url = fileService.uploadLocal(fileType, multipartFile);
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
	
	@RequestMapping(value = "/definereport", method = RequestMethod.GET)
	public String definereport(Long id,ModelMap model) {
		ReportFile reportFile=reportFileService.find(id);
		model.addAttribute("reportFile", reportFile);
		return "/admin/report/definereport";
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable,ModelMap model) {
		model.addAttribute("page", reportFileService.findPage(pageable));
		return "/admin/report/list";
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		Admin admin = adminService.getCurrent();
		model.addAttribute("admin", admin);
		return "/admin/report/add";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(ReportFile reportFile, MultipartFile multipartFile,HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Admin admin = adminService.getCurrent();
		try {
			if(!multipartFile.isEmpty()){
				String originalFilename=multipartFile.getOriginalFilename();
				String type=originalFilename.substring(originalFilename.lastIndexOf(".")+1).toLowerCase();
				if(!"cpt".equals(type)){
					addFlashMessage(redirectAttributes, Message.error("上传失败：格式不正确"));
					return "redirect:add.jhtml";
				}
				String filename=UUID.randomUUID().toString()+"_"+originalFilename;
				
				String filePath = request.getSession().getServletContext().getRealPath("/") + "WEB-INF/reportlets/" + filename;  
				File destFile = new File(filePath);
				if (!destFile.getParentFile().exists()) {
					destFile.getParentFile().mkdirs();
				}
				multipartFile.transferTo(destFile);
				
				StringBuffer source=new StringBuffer("ReportServer?reportlet=/"+filename);
				
				ReportFile saveReportFile=new ReportFile();
				saveReportFile.setAdmin(admin);
				saveReportFile.setCreateDate(new Date());
				saveReportFile.setModifyDate(new Date());
				saveReportFile.setName(reportFile.getName());
				saveReportFile.setDescription(reportFile.getDescription());
				saveReportFile.setSource(source.toString());
				saveReportFile.setUrl(filePath);
				saveReportFile.setTitle(filename);
				reportFileService.save(saveReportFile);
				addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			addFlashMessage(redirectAttributes, ERROR_MESSAGE);
		}
		return "redirect:list.jhtml";
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Message delete(Long[] ids) {
		try {
			if (ids != null) {
				for(Long id:ids){
					ReportFile reportFile=reportFileService.find(id);
					if(reportFile!=null){
						String url=reportFile.getUrl();
						if(url!=null){
							File destFile = new File(url);
							if(destFile.exists()&&destFile.isFile()){
								destFile.delete();
							}
						}
					}
				}
				reportFileService.delete(ids);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("删除失败");
		}
		return SUCCESS_MESSAGE;
	}
	
}
