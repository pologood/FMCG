/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.common;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wit.Message;
import net.wit.Setting;
import net.wit.entity.Area;
import net.wit.entity.Brand;
import net.wit.entity.Community;
import net.wit.entity.Member;
import net.wit.entity.ProductCategory;
import net.wit.entity.TenantCategory;
import net.wit.service.AreaService;
import net.wit.service.BrandService;
import net.wit.service.CaptchaService;
import net.wit.service.CommunityService;
import net.wit.service.MemberService;
import net.wit.service.ProductCategoryService;
import net.wit.service.RSAService;
import net.wit.service.TagService;
import net.wit.service.TenantCategoryService;
import net.wit.util.QRBarCodeUtil;
import net.wit.util.SettingUtils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 共用
 * @author rsico Team
 * @version 3.0
 */
@Controller("commonCommonController")
@RequestMapping("/common")
public class CommonController {

	/** 二维码 */
	public final static String QRCODE = "qrcode";

	/** 一维码 */
	public final static String BARCODE = "barcode";

	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "captchaServiceImpl")
	private CaptchaService captchaService;

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	
	@Resource(name = "tenantCategoryServiceImpl")
	private TenantCategoryService tenantCategoryService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "brandServiceImpl")
	private BrandService brandService;

	@Resource(name = "communityServiceImpl")
	private CommunityService communityService;
	
	

	/**
	 * 登录检测
	 */
	@RequestMapping(value = "/check", method = RequestMethod.GET)
	public @ResponseBody Boolean check() {
		return memberService.isAuthenticated();
	}

	/** 网站关闭 */
	@RequestMapping("/site_close")
	public String siteClose() {
		Setting setting = SettingUtils.get();
		if (setting.getIsSiteEnabled()) {
			return "redirect:/";
		} else {
			return "/common/site_close";
		}
	}

	/** 公钥 */
	@RequestMapping(value = "/public_key", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> publicPostKey(HttpServletRequest request) {
		RSAPublicKey publicKey = rsaService.generateKey(request);
		Map<String, String> data = new HashMap<String, String>();
		data.put("modulus", Base64.encodeBase64String(publicKey.getModulus().toByteArray()));
		data.put("exponent", Base64.encodeBase64String(publicKey.getPublicExponent().toByteArray()));
		return data;
	}

	/** 公钥 */
	@RequestMapping(value = "/public_key", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> publicGetKey(HttpServletRequest request) {
		RSAPublicKey publicKey = rsaService.generateKey(request);
		Map<String, String> data = new HashMap<String, String>();
		data.put("modulus", Base64.encodeBase64String(publicKey.getModulus().toByteArray()));
		data.put("exponent", Base64.encodeBase64String(publicKey.getPublicExponent().toByteArray()));
		return data;
	}

	/** 父级地区 */
	@RequestMapping(value = "/parent", method = RequestMethod.GET)
	public @ResponseBody Area parent(Long id) {
		Area area = areaService.find(id);
		if (area != null) {
			return area.getParent();
		}
		return null;
	}

	/** 下级地区 */
	@RequestMapping(value = "/area", method = RequestMethod.GET)
	public @ResponseBody Map<Long, String> area(Long parentId) {
		List<Area> areas = new ArrayList<Area>();
		Area parent = areaService.find(parentId);
		if (parent != null) {
			areas = new ArrayList<Area>(parent.getChildren());
		} else {
			areas = areaService.findRoots();
		}
		Map<Long, String> options = new HashMap<Long, String>();
		for (Area area : areas) {
//			if(area.getIsAudit()){
				options.put(area.getId(), area.getName());
//			}
		}
		return options;
	}

	/** 下级分类 */
	@RequestMapping(value = "/productCategroy", method = RequestMethod.GET)
	public @ResponseBody Map<Long, String> productCategroy(Long parentId) {
		List<ProductCategory> productCategroys = new ArrayList<ProductCategory>();
		ProductCategory parent = productCategoryService.find(parentId);
		if (parent != null) {
			productCategroys = new ArrayList<ProductCategory>(parent.getChildren());
		} else {
			productCategroys = productCategoryService.findRoots();
		}
		Map<Long, String> options = new HashMap<Long, String>();
		for (ProductCategory productCategory : productCategroys) {
			options.put(productCategory.getId(), productCategory.getName());
		}
		return options;
	}
	
	/** 下级分类 */
	@RequestMapping(value = "/child_category", method = RequestMethod.GET)
	public @ResponseBody Map<Long, String> childCategroy(Long parentId) {
		ProductCategory parent = productCategoryService.find(parentId);
		List<ProductCategory> childCategorys = new ArrayList<ProductCategory>();
		if (parent != null) {
			childCategorys=productCategoryService.findChildren(parent);
		} 
		Map<Long, String> options = new HashMap<Long, String>();
		for (ProductCategory productCategory : childCategorys) {
			options.put(productCategory.getId(), productCategory.getName());
		}
		return options;
	}
	/** 同级地区 */
	@RequestMapping(value = "/sibling", method = RequestMethod.GET)
	public @ResponseBody Map<Long, String> sibling(Long id) {
		List<Area> areas = new ArrayList<Area>();
		Area area = areaService.find(id);
		Map<Long, String> options = new HashMap<Long, String>();
		Area parent = area.getParent();
		if (parent != null) {
			areas = new ArrayList<Area>(parent.getChildren());
		} else {
			areas = areaService.findRoots();
		}
		for (Area temp : areas) {
			options.put(temp.getId(), temp.getName());
		}
		return options;
	}

	/** 同级地区 */
	@RequestMapping(value = "/switchCity", method = RequestMethod.GET)
	public @ResponseBody Message switchCity(Long id, HttpServletRequest request) {
		Area area = areaService.find(id);
		if (area != null) {
			// System.out.println("area set="+request.getSession().getId());
			request.getSession().setAttribute(Member.AREA_ATTRIBUTE_NAME, area);
			return Message.success("正在为您切换至" + area.getName());
		} else {
			return Message.error("");
		}
	}

	/** 区域下商圈 */
	@RequestMapping(value = "/community", method = RequestMethod.GET)
	@ResponseBody
	public List<Community> community(Long areaId) {
		Area area = areaService.find(areaId);
		List<Community> list = communityService.findList(area);
		return list;
	}

	/** 商品分类 */
	@RequestMapping(value = "/productCategory", method = RequestMethod.GET)
	public @ResponseBody Map<Long, String> productCategory(Long parentId) {
		List<ProductCategory> productCategorys = new ArrayList<>();
		ProductCategory parent = productCategoryService.find(parentId);
		if (parent != null) {
			productCategorys = new ArrayList<>(parent.getChildren());
		} else {
			productCategorys = productCategoryService.findRoots();
		}
		Map<Long, String> options = new HashMap<>();
		for (ProductCategory productCategory : productCategorys) {
			options.put(productCategory.getId(), productCategory.getName());
		}
		return options;
	}
	/** 商品分类 */
	@RequestMapping(value = "/tenantCategory", method = RequestMethod.GET)
	public @ResponseBody Map<Long, String> tenantCategory(Long parentId) {
		List<TenantCategory> tenantCategorys = new ArrayList<TenantCategory>();
		TenantCategory parent = tenantCategoryService.find(parentId);
		if (parent != null) {
			tenantCategorys = new ArrayList<TenantCategory>(parent.getChildren());
		} else {
			tenantCategorys = tenantCategoryService.findRoots();
		}
		Map<Long, String> options = new HashMap<Long, String>();
		for (TenantCategory tenantCategory : tenantCategorys) {
			options.put(tenantCategory.getId(), tenantCategory.getName());
		}
		return options;
	}

	/** 地区 */
	@RequestMapping(value = "/area_key", method = RequestMethod.GET)
	public @ResponseBody Area area_key(Long id) {
		return areaService.find(id);
	}

	/** 验证码 */
	@RequestMapping(value = "/captcha", method = RequestMethod.GET)
	public void image(String captchaId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (StringUtils.isEmpty(captchaId)) {
			captchaId = request.getSession().getId();
		}
		String pragma = new StringBuffer().append("yB").append("-").append("der").append("ewoP").reverse().toString();
		String value = new StringBuffer().append("ten").append(".").append("xxp").append("ohs").reverse().toString();
		response.addHeader(pragma, value);
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");

		ServletOutputStream servletOutputStream = null;
		try {
			servletOutputStream = response.getOutputStream();
			BufferedImage bufferedImage = captchaService.buildImage(captchaId);
			ImageIO.write(bufferedImage, "jpg", servletOutputStream);
			servletOutputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(servletOutputStream);
		}
	}

	@RequestMapping(value = "/check_captcha", method = RequestMethod.POST)
	public @ResponseBody Message checkCaptcha(String captchaId,String captcha){
		try {
			if (!captchaService.isValid(Setting.CaptchaType.resetPassword, captchaId, captcha)) {
                return Message.error("验证码输入错误");
            }
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("未知错误");
		}
		return Message.success("执行成功");
	}

	/** 一维码 */
	@RequestMapping(value = "/qbarcode", method = RequestMethod.GET)
	public void qbarcode(String contents, String codetype, Integer width, Integer height, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (StringUtils.isEmpty(contents)) {
			return;
		}
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Cache-Control", "no-store");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		ServletOutputStream servletOutputStream = null;
		try {
			servletOutputStream = response.getOutputStream();
			String codepath = System.getProperty("java.io.tmpdir") + "/code_" + UUID.randomUUID() + ".jpg";
			if (QRCODE.equals(codetype)) {
				if (width == null) {
					width = 200;
				}
				if (height == null) {
					height = 200;
				}
				QRBarCodeUtil.encodeQRCode(contents, codepath, width, height);
			} else {
				if (width == null) {
					width = 300;
				}
				if (height == null) {
					height = 48;
				}
				QRBarCodeUtil.encodeBarCode(contents, codepath, width, height, 30);
			}
			BufferedImage bufferedImage = ImageIO.read(new FileInputStream(codepath));
			ImageIO.write(bufferedImage, "jpg", servletOutputStream);
			servletOutputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(servletOutputStream);
		}
	}

	/** 错误提示 */
	@RequestMapping("/error")
	public String error() {
		return "/common/error";
	}

	/** 资源不存在 */
	@RequestMapping("/resource_not_found")
	public String resourceNotFound() {
		return "/common/resource_not_found";
	}

}