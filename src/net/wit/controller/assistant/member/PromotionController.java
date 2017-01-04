/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.assistant.member;

import net.wit.Filter;
import net.wit.Filter.Operator;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.controller.assistant.model.PromotionBuyfreeModel;
import net.wit.controller.assistant.model.PromotionMailModel;
import net.wit.controller.assistant.model.PromotionSecKillModel;
import net.wit.entity.*;
import net.wit.entity.Promotion.Type;
import net.wit.service.*;
import net.wit.weixin.main.MenuManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * Controller - 促销
 * @author rsico Team
 * @version 3.0
 */
@Controller("assistantMemberPromotionController")
@RequestMapping("/assistant/member/promotion")
public class PromotionController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "activityDetailServiceImpl")
	private ActivityDetailService activityDetailService;

	@Resource(name = "activityRulesServiceImpl")
	private ActivityRulesService activityRulesService;
	
	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;
	
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(Type type,Pageable pageable,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}

		Tenant tenant = member.getTenant();
		if (tenant==null) {
			return DataBlock.error(DataBlock.TENANT_INVAILD);
		}
 		//List<Filter> filters = new ArrayList<Filter>();
		//filters.add(new Filter("tenant", Operator.eq, tenant));
		//filters.add(new Filter("type", Operator.eq, type));
		//pageable.setFilters(filters);
		Page<Promotion> page =promotionService.findPage(tenant,type,pageable);
		if (type.equals(Type.seckill)) {
			return DataBlock.success(PromotionSecKillModel.bindData(page.getContent()),page,"执行成功");
		} else 
		if (type.equals(Type.buyfree)) {
				return DataBlock.success(PromotionBuyfreeModel.bindData(page.getContent()),page,"执行成功");
		} else {
			PromotionMailModel model = new  PromotionMailModel();
			if (!page.getContent().isEmpty()) {
				model.copyFrom(page.getContent().get(0));
			}
			return DataBlock.success(model,page,"执行成功");
		}
		
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock add(Type type,PromotionSecKillModel model,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}

		Tenant tenant = member.getTenant();
		if (tenant==null) {
			return DataBlock.error(DataBlock.TENANT_INVAILD);
		}
		if (Type.mail.equals(type)) {
			List<Filter> filters = new ArrayList<Filter>();
			filters.add(new Filter("tenant", Operator.eq, tenant));
			List<Promotion> promotions =promotionService.findList(Type.mail,null,null,null,null,filters,null);
			Promotion promotion = null;
			for (Promotion p:promotions) {
			  if (promotion==null) {
				  promotion = p;
			  } else {
				  promotionService.delete(p);
			  }
			}
			if (promotion==null) {
				promotion = new Promotion();
			}

			String _name="消费满" + model.getPrice() + "元包邮";
			if(model.getPrice().compareTo(BigDecimal.ZERO)==0){
				_name= "全场包邮";
			}

			promotion.setTenant(tenant);
			promotion.setName(_name);
			promotion.setTitle(promotion.getName());
			promotion.setMinimumQuantity(null);
			promotion.setMaximumQuantity(null);
			promotion.setMinimumPrice(model.getPrice());
			promotion.setMaximumPrice(null);
			promotion.setIsFreeShipping(true);
			promotion.setIsCouponAllowed(true);
			promotion.setType(type);
			promotion.setMember(member);
			promotionService.save(promotion);

			if (!activityDetailService.isActivity(null, tenant, activityRulesService.find(22L))) {
				activityDetailService.addPoint(null, tenant, activityRulesService.find(22L));
			}


			return DataBlock.success(promotion.getId(),"提交成功");
			
		} 
		if (Type.seckill.equals(type)) {
			
			Product product = productService.find(model.getProductId());
			if (product==null) {
				return DataBlock.error("无效促销商品id");
			}
			List<Promotion> promotions =promotionService.findList(Type.seckill,tenant,product);
			Promotion promotion = null;
			for (Promotion p:promotions) {
			  //if (promotion==null) {
				//  promotion = p;
			  //} else {
				  promotionService.delete(p);
			  //}
			}
			if (promotion==null) {
				promotion = new Promotion();
			}
			promotion.setTenant(tenant);
			promotion.setName("限时折扣");
			promotion.setTitle("限时折扣");
			promotion.setMinimumQuantity(null);
			promotion.setMaximumQuantity(model.getMaximumQuantity());
			promotion.setMinimumPrice(null);
			promotion.setMaximumPrice(null);
			promotion.setIsFreeShipping(false);
			promotion.setIsCouponAllowed(true);
			promotion.setType(type);
			promotion.setMember(member);
			promotion.setPriceExpression(model.getPrice().toString());
			promotion.setBeginDate(model.getBeginDate());
			promotion.setEndDate(model.getEndDate());
			PromotionProduct promotionProduct = new PromotionProduct();
			promotionProduct.setPrice(model.getPrice());
			promotionProduct.setProduct(product);
			promotionProduct.setPromotion(promotion);
			promotionProduct.setQuantity(0);
			promotion.getPromotionProducts().clear();
			promotion.getPromotionProducts().add(promotionProduct);
			promotionService.save(promotion);
			if (!activityDetailService.isActivity(null, tenant, activityRulesService.find(27L))) {
				activityDetailService.addPoint(null, tenant, activityRulesService.find(27L));
			}
		}
		return DataBlock.success("success","提交成功");
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock delete(Long id,HttpServletRequest request) {
		Promotion promotion = promotionService.find(id);
		promotionService.delete(promotion);
		return DataBlock.success("success","删除成功");
	}
	
	@RequestMapping(value = "/share", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock share(String type,Long id,HttpServletRequest request) {
		Promotion promotion = promotionService.find(id);
		if (promotion == null) {
			return DataBlock.error("活动ID不存在");
		}
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Tenant tenant = member.getTenant();
		if (tenant==null) {
			return DataBlock.error(DataBlock.TENANT_INVAILD);
		}
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		String url = "";
	    Map<String,String> data = new HashMap<String,String>();
	    if (promotion.getType().equals(Type.mail)) {
		    DeliveryCenter delivery = tenant.getDefaultDeliveryCenter();
		    if (delivery==null) {
		    	return DataBlock.error("请添加门店再推广");
		    }
		    if (!"app".equals(type)) {
		    	url = MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/weixin/tenant/index.jhtml?id="+tenant.getId().toString()+"&extension=" + (member != null ? member.getUsername() : "")));
		    } else {
		    	url = bundle.getString("WeiXinSiteUrl") + "/weixin/tenant/index.jhtml?id="+tenant.getId().toString()+"&extension=" + (member != null ? member.getUsername() : "");
		    }
	       data.put("title","亲,“"+tenant.getShortName()+"”全场包邮了，快快抢购吧。");
	       data.put("thumbnail",tenant.getLogo());
	       data.put("description",promotion.getName());
	    } else {
	    	Product product = promotion.getDefaultProduct();
	    	if (product==null) {
	    		return DataBlock.error("商品无效");
	    	}
		    if (!"app".equals(type)) {
		    	url = MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/weixin/product/details.jhtml?id="+product.getId()+"&extension=" + (member != null ? member.getUsername() : "")));
		    }
		    else {
			    url = bundle.getString("WeiXinSiteUrl") + "/weixin/product/details.jhtml?id="+product.getId()+"&extension=" + (member != null ? member.getUsername() : "");
		    }
		    data.put("title","亲,“"+tenant.getShortName()+"”活动开始了，快快抢购吧。");
		    data.put("thumbnail",product.getThumbnail());
		    data.put("description",product.getFullName());
	    }
	    data.put("url",url);
		return DataBlock.success(data,"执行成功");
	}
	
}