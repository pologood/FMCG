/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.app.b2c;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.TenantListModel;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.Tag;
import net.wit.entity.Tenant;
import net.wit.entity.TenantRelation;
import net.wit.service.MemberRankService;
import net.wit.service.MemberService;
import net.wit.service.ProductService;
import net.wit.service.TagService;
import net.wit.service.TenantRelationService;
import net.wit.service.TenantService;

/**
 * Controller - 商家
 * @author rsico Team
 * @version 3.0
 */
@Controller("appB2cRelationController")
@RequestMapping("/app/b2c/relation")
public class RelationController extends BaseController {

	@Resource(name = "productServiceImpl")
	private ProductService productService;
	
	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "tenantRelationServiceImpl")
	private TenantRelationService tenantRelationService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody DataBlock addTenant(Long id) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		if (member.getTenant()==null) {
			return DataBlock.error("您不是商家不能加盟。");
		}
		Tenant tenant = tenantService.find(id);
		if (tenant == null) {
			return DataBlock.error("商家ID无效");
		}
		
		Tenant myTenant = member.getTenant();

		Page<TenantRelation> rl = tenantRelationService.findRelation(tenant, myTenant, null);
		if (rl.getTotal()>0) {
			return DataBlock.error("已经加盟了");
		}		
		
		TenantRelation relation = new TenantRelation();
		relation.setParent(tenant);
		relation.setStatus(TenantRelation.Status.none);
		relation.setTenant(tenant);
		relation.setMemberRank(memberRankService.findDefault());
		tenantRelationService.save(relation);
		return DataBlock.success("success","申请成功");
	}

	/**
	 * 加盟的商家,代购商家
	 */
	@RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(@PathVariable Long id, Pageable pageable) {
		Tenant tenant = tenantService.find(id);
		if (tenant==null) {
			return DataBlock.error("无效店铺id");
		}
		Page<TenantRelation> page = tenantRelationService.findParent(tenant,null, pageable);
		List<TenantListModel> models = TenantListModel.bindRelations(page.getContent());
		Long[] tagIds = {5L};  
		List<Tag> tags = tagService.findList(tagIds);
		for (TenantListModel model:models) {
			Tenant myTenant = tenantService.find(model.getId());
			List<Product> products = productService.findMyList(tenant, null, null, null, null, tags, null, null,null, true, true, null, false, null, null,3, Product.OrderType.weight);
			model.bindProducts(products);
		    model.bindPromoton(myTenant.getVaildPromotions());
		}
		return DataBlock.success(models,"执行成功");
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	DataBlock delete(Long id) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Tenant tenant = tenantService.find(id);
		List<Long> ids = tenantRelationService.findRelations(member.getTenant(), tenant);
		for (Long mid:ids) {
		   tenantRelationService.delete(mid);
		}
		return DataBlock.success("success","删除成功");
	}
}