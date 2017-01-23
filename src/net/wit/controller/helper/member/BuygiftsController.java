/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.helper.member;

import net.wit.Filter;
import net.wit.Filter.Operator;
import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.app.model.PromotionBuyfreeModel;
import net.wit.controller.weixin.model.DataBlock;
import net.wit.entity.Ad.LinkType;
import net.wit.entity.*;
import net.wit.entity.Product.OrderType;
import net.wit.entity.Promotion.Type;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Controller - 买赠搭配
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("helperMemberBuygiftsController")
@RequestMapping("/helper/member/buygifts")
public class BuygiftsController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "promotionServiceImpl")
    private PromotionService promotionService;

    @Resource(name = "productServiceImpl")
    private ProductService productService;

    @Resource(name = "productCategoryServiceImpl")
    private ProductCategoryService productCategoryService;

    @Resource(name = "productCategoryTenantServiceImpl")
    private ProductCategoryTenantService productCategoryTenantService;

    @Resource(name = "tagServiceImpl")
    private TagService tagService;

    @Resource(name = "brandServiceImpl")
    private BrandService brandService;

    @Resource(name = "unionServiceImpl")
    private UnionService unionService;
    @Resource(name = "activityDetailServiceImpl")
    private ActivityDetailService activityDetailService;

    @Resource(name = "activityRulesServiceImpl")
    private ActivityRulesService activityRulesService;


    @RequestMapping(value = "/listbuygift", method = RequestMethod.GET)
    public String listBuygift(Pageable pageable,  ModelMap model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return ERROR_VIEW;
        }

        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return ERROR_VIEW;
        }

        List<Promotion> promotionList = promotionService.findList(Type.buyfree,tenant,null);

        for(Promotion promotion:promotionList){
            if(promotion.getProducts().size()>0){

            }else {
                promotionService.delete(promotion);
            }

            if(promotion.hasEnded()){
                promotionService.delete(promotion);
            }
        }

        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("tenant", Operator.eq, tenant));
        filters.add(new Filter("type", Operator.eq, Type.buyfree));
        pageable.setFilters(filters);
        Page<Promotion> page = promotionService.findPage(pageable);

        List<PromotionBuyfreeModel> promotions = null;
        if (page.getContent().size() > 0) {
            promotions = PromotionBuyfreeModel.bindData(page.getContent());
        }

        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");

        String url = bundle.getString("WeiXinSiteUrl") + "/wap/product/content/ID/product.jhtml?extension=" + (member != null ? member.getUsername() : "");
        model.addAttribute("member", member);
        model.addAttribute("page", page);
        model.addAttribute("promotion", promotions);
        model.addAttribute("url", url);
        model.addAttribute("title", "亲,“" + tenant.getShortName() + "”买赠搭配开始了，快快抢购吧。");
        model.addAttribute("tenantId", tenant.getId());
        model.addAttribute("shareAppKey", bundle.getString("shareAppKey"));
        return "/helper/member/buygifts/listbuygift";
    }

    /**
     * 所有商品列表
     */
    @RequestMapping(value = "/listproduct", method = RequestMethod.GET)
    public String listproduct(Long productCategoryId, Long productCategoryTenantId, Long brandId, String searchValue, Long promotionId,
                              Long tagId, @RequestParam(defaultValue = "true") Boolean isMarketable, Boolean isList, Boolean isTop,
                              Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert,String type, Pageable pageable, ModelMap model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return ERROR_VIEW;
        }
        ProductCategory productCategory = productCategoryService.find(productCategoryId);
        List<ProductCategoryTenant> productCategoryTenant = productCategoryTenantService.findTree(member.getTenant());
        Brand brand = brandService.find(brandId);
        Promotion promotion = promotionService.find(promotionId);
        List<Tag> tags = tagService.findList(tagId);
        model.addAttribute("union", unionService.findAll());
        model.addAttribute("productCategoryTree", productCategoryService.findTree());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("promotions", promotionService.findAll());
        model.addAttribute("tags", tagService.findList(Tag.Type.product));
        model.addAttribute("productCategoryId", productCategoryId);
        model.addAttribute("productCategoryTenantId", productCategoryTenantId);
        model.addAttribute("brandId", brandId);
        model.addAttribute("promotionId", promotionId);
        model.addAttribute("tagId", tagId);
        model.addAttribute("isMarketable", isMarketable);
        model.addAttribute("isList", isList);
        model.addAttribute("isTop", isTop);
        model.addAttribute("isGift", isGift);
        model.addAttribute("isOutOfStock", isOutOfStock);
        model.addAttribute("isStockAlert", isStockAlert);
        model.addAttribute("linkTypes", LinkType.values());
        model.addAttribute("productCategoryTenants", productCategoryTenant);
        model.addAttribute("page",
                productService.findMyPage(member.getTenant(), searchValue, productCategory, productCategoryTenantService.find(productCategoryTenantId), brand,
                        promotion, tags, null, null, null, isMarketable, isList, isTop, isGift, isOutOfStock,
                        isStockAlert, OrderType.dateDesc, pageable));
        model.addAttribute("pageActive", 2);
        model.addAttribute("member", member);
        model.addAttribute("type", type);
        model.addAttribute("tenantId", member.getTenant().getId());
        return "/helper/member/buygifts/listproduct";
    }

    @RequestMapping(value = "/listgift", method = RequestMethod.GET)
    public String listGifts(Pageable pageable, ModelMap model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return ERROR_VIEW;
        }

        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return ERROR_VIEW;
        }
        Page<Product> page = productService.findMyPage(member.getTenant(), null, null, null, null, null, null, null, null, null,null, true, null, true, null, null, Product.OrderType.dateDesc, pageable);
        model.addAttribute("page", page);
        return "/helper/member/buygifts/listgift";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model,Pageable pageable, RedirectAttributes redirectAttributes) {
        Member member = memberService.getCurrent();

        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return "redirect:/helper/index.jhtml";
        }
        Product product = productService.find(id);

        if (product == null) {
            return ERROR_VIEW;
        }

        List<Promotion> promotions = promotionService.findList(Type.buyfree, tenant, product);
        for (Promotion p : promotions) {
            if (p != null) {
                addFlashMessage(redirectAttributes, Message.error("此商品已有折扣方案，请不要重复添加。"));
                return "redirect:listproduct.jhtml";
            }
        }

//        Page<Product> page = productService.findMyPage(member.getTenant(), null, null, null, null, null, null, null, null, null,null, true, null, true, null, null, Product.OrderType.dateDesc, pageable);
        List<Product> products=productService.findMyList(tenant, null, null, null, null, null, null, null, null, null, true, null, true, null, null, null, Product.OrderType.dateDesc);
//        model.addAttribute("page", page);
        model.addAttribute("products",products);
        model.addAttribute("product", product);
        return "/helper/member/buygifts/edit";
    }

    @RequestMapping(value = "/add_buyfree", method = RequestMethod.POST)
    public String addBuyfree(PromotionBuyfreeModel model,String activityName,String beginD,String endD,RedirectAttributes redirectAttributes) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return ERROR_VIEW;
        }

        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return ERROR_VIEW;
        }

        Product product = productService.find(model.getProductId());
        Product giftProduct = productService.find(model.getGiftProductId());
        if (product==null) {
            return ERROR_VIEW;
        }
        if (giftProduct==null) {
            return ERROR_VIEW;
        }
        List<Promotion> promotions =promotionService.findList(Type.buyfree,tenant,product);
        Promotion promotion = null;
        for (Promotion p:promotions) {
            promotionService.delete(p);
        }
        if (promotion==null) {
            promotion = new Promotion();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date beDate = null, eDate = null;
        try {
            beDate = sdf.parse(beginD);
            eDate = sdf.parse(endD);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        promotion.setTenant(tenant);
        promotion.setName(activityName);
        //promotion.setName("买"+model.getMinimumQuantity().toString()+"赠"+model.getGiftQuantity().toString());
        promotion.setTitle(model.getName());
        promotion.setMinimumQuantity(model.getMinimumQuantity());
        promotion.setMaximumQuantity(model.getMaximumQuantity());
        promotion.setMinimumPrice(null);
        promotion.setMaximumPrice(null);
        promotion.setIsFreeShipping(false);
        promotion.setIsCouponAllowed(true);
        promotion.setType(Type.buyfree);
        promotion.setMember(member);
        promotion.setPriceExpression(null);
        promotion.setBeginDate(beDate);
        promotion.setEndDate(eDate);
        PromotionProduct promotionProduct = new PromotionProduct();
        promotionProduct.setPrice(product.getPrice());
        promotionProduct.setProduct(product);
        promotionProduct.setPromotion(promotion);
        promotionProduct.setQuantity(0);
        promotion.getPromotionProducts().clear();
        promotion.getPromotionProducts().add(promotionProduct);
        GiftItem giftItem = new GiftItem();
        giftItem.setGift(giftProduct);
        giftItem.setQuantity(model.getGiftQuantity());
        giftItem.setPromotion(promotion);
        promotion.getGiftItems().clear();
        promotion.getGiftItems().add(giftItem);
        promotionService.save(promotion);

        if(!activityDetailService.isActivity(null,tenant,activityRulesService.find(25L))){
            activityDetailService.addPoint(null,tenant,activityRulesService.find(25L));
        }
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:listbuygift.jhtml";
    }

    @RequestMapping(value = "/delete_all", method = RequestMethod.POST)
    @ResponseBody
    public Message deleteAll(Long[] ids) {
        try {
            promotionService.delete(ids);
        } catch (Exception e) {
            return ERROR_MESSAGE;
        }
        return SUCCESS_MESSAGE;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Message delete(Long[] ids) {
        try {
            Member member = memberService.getCurrent();
            if (member == null) {
                return ERROR_MESSAGE;
            }

            Tenant tenant = member.getTenant();
            if (tenant==null) {
                return ERROR_MESSAGE;
            }

            List<Product> products = productService.findList(ids);
            if (products==null) {
                return ERROR_MESSAGE;
            }

            for(Product product:products){
                product.setIsGift(false);
                productService.update(product);
            }
            return SUCCESS_MESSAGE;
        } catch (Exception e) {
            return ERROR_MESSAGE;
        }
    }

    @RequestMapping(value = "/add_gift", method = RequestMethod.POST)
    @ResponseBody
    public Message addGift(Long id) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return ERROR_MESSAGE;
        }

        Tenant tenant = member.getTenant();
        if (tenant==null) {
            return ERROR_MESSAGE;
        }

        Product product = productService.find(id);
        if (product==null) {
            return ERROR_MESSAGE;
        }
        product.setIsGift(true);
        productService.update(product);
        return SUCCESS_MESSAGE;
    }

    @RequestMapping(value = "/delete_gift", method = RequestMethod.POST)
    @ResponseBody
    public Message deleteGift(Long id) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return ERROR_MESSAGE;
        }

        Tenant tenant = member.getTenant();
        if (tenant==null) {
            return ERROR_MESSAGE;
        }

        Product product = productService.find(id);
        if (product==null) {
            return ERROR_MESSAGE;
        }
        product.setIsGift(false);
        productService.update(product);
        return SUCCESS_MESSAGE;
    }

}