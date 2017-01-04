/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import java.math.BigDecimal;
import java.util.HashSet;

import javax.annotation.Resource;

import net.wit.FileInfo.FileType;
import net.wit.Message;
import net.wit.Pageable;
import net.wit.entity.Community;
import net.wit.entity.Location;
import net.wit.entity.ProductImage;
import net.wit.entity.Tag;
import net.wit.entity.Tag.Type;
import net.wit.service.AreaService;
import net.wit.service.CommunityService;
import net.wit.service.FileService;
import net.wit.service.ProductImageService;
import net.wit.service.TagService;
import net.wit.util.LBSUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 社区
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminCommunityController")
@RequestMapping("/admin/community")
public class CommunityController extends BaseController {

	@Resource(name = "communityServiceImpl")
	private CommunityService communityService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	@Resource(name = "fileServiceImpl")
	private FileService fileService;
	@Resource(name = "productImageServiceImpl")
	private ProductImageService productImageService;
	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("tags", tagService.findList(Type.community));
		model.addAttribute("community", new Community());
		return "/admin/community/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Community community,Long[] tagIds,Long areaId,String lat, String lng, MultipartFile file, RedirectAttributes redirectAttributes) {
		if (StringUtils.isNotEmpty(lat) && StringUtils.isNotEmpty(lng)) {
			BigDecimal blat = new BigDecimal(lat);
			BigDecimal blng = new BigDecimal(lng);
			Location location = new Location(blat,blng);
			community.setLocation(location);
		}
		if (file != null && !file.isEmpty()) {
			if (!fileService.isValid(FileType.image, file)) {
				addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
				return "redirect:add.jhtml";
			} else {
				ProductImage img = new ProductImage();
				img.setFile(file);
				productImageService.build(img);
				community.setImage(img.getThumbnail());
			}
 				
		} else {
			community.setImage(null);
		}
		community.setArea(areaService.find(areaId));
		if (!isValid(community)) {
			return ERROR_VIEW;
		}
		community.setLocation(LBSUtil.bd_decrypt(community.getLocation()));
		community.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
		communityService.save(community);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("tags", tagService.findList(Type.community));
		model.addAttribute("community", communityService.find(id));
		return "/admin/community/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Community community, Long[] tagIds,Long areaId,String lat, String lng,MultipartFile file, RedirectAttributes redirectAttributes) {
		if (StringUtils.isNotEmpty(lat) && StringUtils.isNotEmpty(lng)) {
			BigDecimal blat = new BigDecimal(lat);
			BigDecimal blng = new BigDecimal(lng);
			Location location = new Location(blat,blng);
			community.setLocation(location);
		}
		if (file != null && !file.isEmpty()) {
			if (!fileService.isValid(FileType.image, file)) {
				addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
				return "redirect:edit.jhtml";
			} else {
				ProductImage img = new ProductImage();
				img.setFile(file);
				productImageService.build(img);
				community.setImage(img.getThumbnail());
			}
		}
		community.setArea(areaService.find(areaId));
		if (!isValid(community)) {
			return ERROR_VIEW;
		}
//		Community tmpCommunity = communityService.find(community.getId());
//		if (community.getLocation()!=null) {
//			if (tmpCommunity.getLocation()==null || !community.getLocation().equals(tmpCommunity.getLocation()))
//			{
//				community.setLocation(LBSUtil.bd_decrypt(community.getLocation()));
//			}
//		}

		community.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
		communityService.update(community, "hits", "pageNumber");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Pageable pageable, ModelMap model) {
		model.addAttribute("page", communityService.findPage(pageable));
		return "/admin/community/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		communityService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}