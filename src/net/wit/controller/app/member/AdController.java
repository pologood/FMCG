package net.wit.controller.app.member;

import javax.annotation.Resource;

import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.app.model.AdModel;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.BaseController;
import net.wit.entity.Ad;
import net.wit.entity.Ad.LinkType;
import net.wit.entity.AdPosition;
import net.wit.entity.Member;
import net.wit.entity.Tenant;

@Controller("appMemberAdController")
@RequestMapping("/app/member/ad")
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

}
