package net.wit.controller.assistant.member;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.AdModel;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.assistant.model.RelatedProductModel;
import net.wit.entity.*;
import net.wit.entity.Ad.LinkType;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller("assistantMemberAdController")
@RequestMapping("/assistant/member/ad")
public class AdController extends BaseController {
	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "adServiceImpl")
	private AdService adService;

	@Resource(name = "adPositionServiceImpl")
	private AdPositionService adPositionService;

	@Resource(name = "activityDetailServiceImpl")
	private ActivityDetailService activityDetailService;

	@Resource(name = "activityRulesServiceImpl")
	private ActivityRulesService activityRulesService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;

	/**
	 * 广告添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody DataBlock adAdd(Long adPositionId,String image,String url,String title,LinkType linkType,Long linkId, ModelMap model) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Tenant tenant = member.getTenant();
		AdPosition adPosition = adPositionService.find(adPositionId);
		Ad ad = new Ad();
		ad.setType(Ad.Type.image);
		ad.setAdPosition(adPosition);
		ad.setTenant(tenant);
		ad.setPath(image);
		ad.setUrl(url);
		ad.setTenant(tenant);
		ad.setAdPosition(adPosition);
		ad.setTitle(title);
		ad.setLinkId(linkId);
		ad.setLinkType(linkType);
		if (!isValid(ad)) {
			return DataBlock.error("数据参数无效");
		}
		adService.save(ad);

		if(!activityDetailService.isActivity(null,tenant, activityRulesService.find(5L))){
			activityDetailService.addPoint(null,tenant,activityRulesService.find(5L));
		}
		return DataBlock.success(ad.getId(),"保存成功");
	}

	/**
	 * 修改广告
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody DataBlock adAdd(Long id,Long adPositionId,String image,String url,String title,LinkType linkType,Long linkId, ModelMap model) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Tenant tenant = member.getTenant();
		AdPosition adPosition = adPositionService.find(adPositionId);
		Ad ad = adService.find(id);
		ad.setType(Ad.Type.image);
		ad.setAdPosition(adPosition);
		ad.setTenant(tenant);
		ad.setUrl(url);
		ad.setPath(image);
		ad.setTenant(tenant);
		ad.setAdPosition(adPosition);
		ad.setTitle(title);
		ad.setLinkId(linkId);
		ad.setLinkType(linkType);
		if (!isValid(ad)) {
			return DataBlock.error("数据参数无效");
		}
		adService.save(ad);
		return DataBlock.success(ad.getId(),"保存成功");
	}

	/**
	 * 广告列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody DataBlock list(Long adPositionId,Pageable pageable) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Tenant tenant = member.getTenant();
		if (tenant==null) {
			return DataBlock.error(DataBlock.TENANT_INVAILD);
		}
		AdPosition adPosition = adPositionService.find(adPositionId);
		Page<Ad> page = adService.findPage(tenant,adPosition, pageable);
		return DataBlock.success(AdModel.bindData(page.getContent()),"执行成功");
	}

	/**
	 * 广告删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody DataBlock adDelete(Long[] ids) {
		adService.delete(ids);
		return DataBlock.success("success","删除成功");
	}
	/**
	 * 广告位关联商品
	 */
	/**
	 * 根据id获取商品详情
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public
	@ResponseBody
	DataBlock view(Long id) {
		Product product = productService.find(id);
		if (product == null) {
			return DataBlock.error("商品ID不存在");
		}
		RelatedProductModel model = new RelatedProductModel();
		model.copyFrom(product);
		return DataBlock.success(model, "执行成功");
	}
}
