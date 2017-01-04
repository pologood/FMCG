package net.wit.controller.helper.member;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.helper.BaseController;
import net.wit.entity.Ad;
import net.wit.entity.AdPosition;
import net.wit.entity.Product;
import net.wit.entity.Member;
import net.wit.entity.Tenant;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("helperMemberAdController")
@RequestMapping("/helper/member/ad")
public class AdController extends BaseController {

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "adServiceImpl")
	private AdService adService;

	@Resource(name = "adPositionServiceImpl")
	private AdPositionService adPositionService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "activityDetailServiceImpl")
	private ActivityDetailService activityDetailService;

	@Resource(name = "activityRulesServiceImpl")
	private ActivityRulesService activityRulesService;

	/**
	 * 广告添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String adAdd(Long tenantId, ModelMap model) {
		Member member = memberService.getCurrent();
		Long[] ads={80L,119L,120L,121L,122L,123L,145L};//广告位编码
		List<AdPosition> list = adPositionService.findList(ads);
		model.addAttribute("types", net.wit.entity.Ad.Type.values());

		model.addAttribute("linkTypes", Ad.LinkType.values());
		model.addAttribute("adPositions", list);
		Tenant tenant = tenantService.find(tenantId);
		List<Product> products = productService.findMyList(tenant, null, null, null, null, null, null, null, null, true,
				null, null, null, null, null,null, null);
		model.addAttribute("products", products);
		model.addAttribute("member", member);
		return "/helper/member/ad/add";
	}

	/**
	 * 广告保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String adSave(Ad ad, Long adPositionId, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		AdPosition adPosition = adPositionService.find(adPositionId);
		ad.setAdPosition(adPosition);
		ad.setTenant(tenant);
		if (!isValid(ad)) {
			return ERROR_VIEW;
		}
		if (ad.getType() == net.wit.entity.Ad.Type.text) {
			ad.setPath(null);
		} else {
			ad.setContent(null);
		}
		adService.save(ad);
		activityDetailService.addPoint(null, tenant, activityRulesService.find(5L));
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 广告编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String adEdit(Long id, Long tenantId, ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		Long[] ads={80L,119L,120L,121L,122L,123L,145L};//广告位编码
		List<AdPosition> list=adPositionService.findList(ads);
		model.addAttribute("adPositions", list);
		model.addAttribute("types", net.wit.entity.Ad.Type.values());
		model.addAttribute("linkTypes", Ad.LinkType.values());
		model.addAttribute("ad", adService.find(id));
		Tenant tenant = tenantService.find(tenantId);
		List<Product> products = productService.findMyList(tenant, null, null, null, null, null, null, null, null, true,
				null, null, null, null, null,null, null);
		model.addAttribute("products", products);
		return "/helper/member/ad/edit";
	}

	/**
	 * 广告更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String adUpdate(Ad ad, Long adPositionId, RedirectAttributes redirectAttributes, String path) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		ad.setAdPosition(adPositionService.find(adPositionId));
		ad.setTenant(tenant);
		if (!isValid(ad)) {
			return ERROR_VIEW;
		}
		if (ad.getType() == net.wit.entity.Ad.Type.text) {
			ad.setPath(null);
		} else {
			ad.setContent(null);
		}
		adService.update(ad);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 广告列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String adList(Pageable pageable, ModelMap model) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		Page<Ad> page=adService.findMyPage(tenant, pageable);
		Long num=page.getTotal();
		model.addAttribute("num",num);
		model.addAttribute("page", page);
		model.addAttribute("tenant", tenant);
		model.addAttribute("member", member);
		return "/helper/member/ad/list";
	}

	/**
	 * 广告删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Message adDelete(Long[] ids) {
		adService.delete(ids);
		return SUCCESS_MESSAGE;
	}

}
