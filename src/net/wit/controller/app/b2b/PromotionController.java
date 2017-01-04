/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.app.b2b;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Filter.Operator;
import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.PromotionModel;
import net.wit.controller.app.model.TenantListModel;
import net.wit.entity.Member;
import net.wit.entity.Promotion;
import net.wit.entity.Tenant;
import net.wit.entity.TenantRelation;
import net.wit.service.AreaService;
import net.wit.service.BrandService;
import net.wit.service.MemberRankService;
import net.wit.service.MemberService;
import net.wit.service.ProductCategoryTenantService;
import net.wit.service.ProductService;
import net.wit.service.PromotionService;
import net.wit.service.TagService;
import net.wit.service.TenantRelationService;
import net.wit.service.TenantService;

/**
 * Controller - 商家
 * @author rsico Team
 * @version 3.0
 */
@Controller("appB2bPromotionController")
@RequestMapping("/app/b2b/promotion")
public class PromotionController extends BaseController {


	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	/**
	 * 活动
	 */
	@RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(@PathVariable Long id, Pageable pageable) {

		
		Tenant tenant = tenantService.find(id);
		if (tenant==null) {
			DataBlock.error("企业ID无效");
		}
		
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("tenant", Operator.eq, tenant));
		Set<Promotion.Type> types = new HashSet<Promotion.Type>();
		types.add(Promotion.Type.seckill);
		types.add(Promotion.Type.buyfree);
		filters.add(new Filter("type",Operator.in,types));
		pageable.setFilters(filters);
		Page<Promotion> page = promotionService.findPage(null,null, true, false, null, null, null, null, null, pageable);
		return DataBlock.success(PromotionModel.bindData(page.getContent()),"执行成功");
	}

	/**
	 * 活动
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(Pageable pageable) {
		
		List<Filter> filters = new ArrayList<Filter>();
		Set<Promotion.Type> types = new HashSet<Promotion.Type>();
		types.add(Promotion.Type.seckill);
		types.add(Promotion.Type.buyfree);
		filters.add(new Filter("type",Operator.in,types));
		pageable.setFilters(filters);
		Page<Promotion> page = promotionService.findPage(null,null, true, false, null, null, null, null, null, pageable);
		return DataBlock.success(PromotionModel.bindData(page.getContent()),"执行成功");
	}

}