/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.b2b;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.entity.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.wit.Message;
import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Product.OrderType;
import net.wit.entity.Promotion.Type;
import net.wit.service.AreaService;
import net.wit.service.BrandService;
import net.wit.service.MemberService;
import net.wit.service.ProductCategoryService;
import net.wit.service.ProductChannelService;
import net.wit.service.ProductService;
import net.wit.service.PromotionService;
import net.wit.service.SearchService;
import net.wit.service.TagService;


/**
 * Controller - 商品
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("b2bProductController")
@RequestMapping("/b2b/product")
public class ProductController extends BaseController {

    @Resource(name = "productServiceImpl")
    private ProductService productService;

    @Resource(name = "productCategoryServiceImpl")
    private ProductCategoryService productCategoryService;

    @Resource(name = "brandServiceImpl")
    private BrandService brandService;

    @Resource(name = "promotionServiceImpl")
    private PromotionService promotionService;

    @Resource(name = "tagServiceImpl")
    private TagService tagService;

    @Resource(name = "searchServiceImpl")
    private SearchService searchService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

     @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "productChannelServiceImpl")
    private ProductChannelService productChannelService;

    /**
     * 商品详情页面
     */
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public String content(@PathVariable Long id, HttpServletRequest request, ModelMap model,RedirectAttributes redirectAttributes) {
        Product product = productService.find(id);
        if(product==null){
        	addFlashMessage(redirectAttributes, Message.error("没找到退货申请!"));
        	return "redirect:/b2b/product/list/0.jhtml";
        }
        Set<Review> reviews = product.getReviews();
        List<Review> reviewImageSize = new ArrayList<Review>();
        for (Review review : reviews) {
            if (review.getImages() != null) {
                reviewImageSize.add(review);
            }
        }
        List<Object> highScores = new ArrayList<>();
        List<Object> middleScores = new ArrayList<>();
        List<Object> lowScores = new ArrayList<>();
        List<Object> images = new ArrayList<>();
        for (Review review : reviews) {
            if (review.getScore() == 3) {
                middleScores.add(review.getScore());
            } else if (review.getScore() > 3) {
                highScores.add(review.getScore());
            } else {
                lowScores.add(review.getScore());
            }
            if (review.getImages() != null) {
                images.add(review.getImages());
            }
        }
        model.addAttribute("images", images);
        model.addAttribute("highScore", highScores);
        model.addAttribute("middleScore", middleScores);
        model.addAttribute("lowScore", lowScores);
        model.addAttribute("reviewImageSize", reviewImageSize);
        model.addAttribute("product", product);
        model.addAttribute("tenant", product.getTenant());
        model.addAttribute("member", memberService.getCurrent());
        model.addAttribute("price", product.calcEffectivePrice(memberService.getCurrent()));
        model.addAttribute("productCategory", product.getProductCategory());

        model.addAttribute("area", areaService.getCurrent());
        return "/b2b/product/detail";
    }

    /**
     * 一级分类
     *
     * @param parentId
     * @param model
     * @return
     */
    @RequestMapping(value = "/one_category", method = RequestMethod.GET)
    public String productOne(Long parentId, ModelMap model) {
        ProductCategory parent = productCategoryService.find(parentId);
        if (parent != null) {
            model.addAttribute("parent", parent);
        }
        model.addAttribute("parentId", parentId);
        return "/b2b/product/oneCategory";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list/{productCategoryId}", method = RequestMethod.GET)
    public String list(@PathVariable Long productCategoryId, Long brandId, Long areaId,Long communityId, Long productCategoryTagId, OrderType orderType, Integer pageNumber, Integer pageSize,String keyword,
                       HttpServletRequest request, ModelMap model) {

        ProductCategory productCategory = productCategoryService.find(productCategoryId);

        ProductCategory productCategorys = productCategoryService.find(productCategoryTagId);

        if(productCategorys!=null&&productCategorys.getChildren().size()>0){
            productCategory = productCategoryService.find(productCategoryTagId);
            productCategorys = null;
            productCategoryTagId = null;
        }

        Brand brand = brandService.find(brandId);
        Set<Brand> brands = new HashSet<>();
        if(productCategory!=null){
            brands = productCategory.getBrands();
        }

        if(productCategorys!=null){
            brands = productCategorys.getBrands();
        }

        if(brands.size()>0){
            model.addAttribute("brands", brands);
        }else {
            model.addAttribute("brands", brandService.findList(tagService.find(24L)));
        }

        Area area = areaService.find(areaId);
        if (area == null) {
            area = areaService.getCurrent();
        }

        Map<Attribute, String> attributeValue = new HashMap<Attribute, String>();
        Set<Attribute> attributes = new HashSet<>();
        Set<ProductCategory> productCategories = new HashSet<>();
        if(productCategory!=null){
            attributes=productCategory.getAttributes();
            for (Attribute attribute : attributes) {
                String value = request.getParameter("attribute_" + attribute.getId());
                if (StringUtils.isNotEmpty(value) && attribute.getOptions().contains(value)) {
                    attributeValue.put(attribute, value);
                }
            }
            productCategories.add(productCategory);
        }

        String _price = "priceAsc";
        if(orderType==OrderType.priceAsc){
            _price = "priceDesc";
        }else if(orderType==OrderType.priceDesc){
            _price = "priceAsc";
        }
        Pageable pageable = new Pageable(pageNumber, pageSize);

        orderType = orderType==null?OrderType.weight:orderType;

        Page<Product> page = productService.openPage(pageable,area,productCategories,brand,null,null,keyword,null,null,null,null,attributeValue,null,orderType);
        model.addAttribute("orderTypes", OrderType.values());
        model.addAttribute("productCategory", productCategory);
        model.addAttribute("productCategorys", productCategorys);
        model.addAttribute("orderType", orderType);
        model.addAttribute("price", _price);
        model.addAttribute("productCategoryTagId", productCategoryTagId);
        model.addAttribute("brand", brand);
        model.addAttribute("area", area);
        model.addAttribute("attributeValue", attributeValue);
        model.addAttribute("orderType", orderType);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("productCategoryId", productCategoryId);
        //model.addAttribute("productCategoryTagId", tagService.find(productCategoryTagId));
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("member",memberService.getCurrent());

        return "/b2b/product/list";
    }

    /**
     * 列表-频道
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listByChannel(Long productChannelId, Long productCategoryTagId, Long brandId, Long areaId, Long promotionId, Long[] tagIds, BigDecimal startPrice, BigDecimal endPrice, OrderType orderType, Integer pageNumber, Integer pageSize,
                                HttpServletRequest request, ModelMap model) {
        Brand brand = brandService.find(brandId);
        Area area = areaService.find(areaId);
        Promotion promotion = promotionService.find(promotionId);
        List<Tag> tags = tagService.findList(tagIds);
        Pageable pageable = new Pageable(pageNumber, pageSize);
        Area currentArea = areaService.getCurrent();
        ProductChannel channel = productChannelService.find(productChannelId);
        Set<ProductCategory> productCategorys = new HashSet<ProductCategory>();
        if (channel != null) {
            productCategorys = channel.getProductCategorys();
        }
        model.addAttribute("currentArea", currentArea);
        model.addAttribute("orderTypes", OrderType.values());
        model.addAttribute("brand", brand);
        model.addAttribute("area", area);
        model.addAttribute("promotion", promotion);
        model.addAttribute("tags", tags);
        model.addAttribute("startPrice", startPrice);
        model.addAttribute("endPrice", endPrice);
        model.addAttribute("orderType", orderType);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("channel", channel);
        model.addAttribute("productCategoryTagId", tagService.find(productCategoryTagId));
        if (area == null) {
            model.addAttribute("page", productService.findPageByChannel(productCategorys, brand, promotion, tags, null, startPrice, endPrice, true, true, null, false, null, null, null, currentArea, false, orderType, null, null, pageable));
        } else {
            model.addAttribute("page", productService.findPageByChannel(productCategorys, brand, promotion, tags, null, startPrice, endPrice, true, true, null, false, null, null, null, area, false, orderType, null, null, pageable));
        }
        return "b2b/channel/" + channel.getTemplateId();
    }

    /**
     * 搜索
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(String keyword, Long areaId, BigDecimal startPrice, BigDecimal endPrice, OrderType orderType, Long[] tagIds, Pageable pageable, ModelMap model) {
        Area area = areaService.find(areaId);
        Area currentArea = areaService.getCurrent();
        List<Tag> tags = tagService.findList(tagIds);
        model.addAttribute("currentArea", currentArea);
        model.addAttribute("orderTypes", OrderType.values());
        model.addAttribute("productKeyword", keyword);
        model.addAttribute("startPrice", startPrice);
        model.addAttribute("endPrice", endPrice);
        model.addAttribute("area", area);
        model.addAttribute("tags", tags);
        model.addAttribute("orderType", orderType);
        model.addAttribute("pageNumber", pageable.getPageNumber());
        model.addAttribute("page", productService.search(keyword, null, startPrice, endPrice, orderType, pageable));
        return "b2b/product/search";
    }

    /**
     * 浏览记录
     */
    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Product> history(Long[] ids) {
        return productService.findList(ids);
    }

    /**
     * 点击数
     */
    @RequestMapping(value = "/hits/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Long hits(@PathVariable Long id) {
        return productService.viewHits(id);
    }

    /**
     * 限时抢购
     *
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/flash_sale", method = RequestMethod.GET)
    public String flashSale(HttpServletRequest request, ModelMap model,Pageable pageable) {
        List<ProductCategory> rootCategorys = productCategoryService.findRoots();
    	List<Promotion> page=promotionService.findList(Type.seckill, true, false, null, null, null, null);
    	model.addAttribute("page",page);
        model.addAttribute("rootCategory", rootCategorys);
        model.addAttribute("area", areaService.getCurrent());
        model.addAttribute("type", "flash_sale");
        return "b2b/product/flash_sale";
    }

    //可能感兴趣
    @RequestMapping(value = "/interest", method = RequestMethod.GET)
    public String interested(ModelMap model) {
        Member member = memberService.getCurrent();
        List<Tag> tags = tagService.findList(new Long[]{1l});
        List<Product> hot = productService.findList(null, null, null, tags, null, null, null, true, true, null, false, null, null, null, 10, null, new ArrayList<Order>());
        Pageable pageable = new Pageable(1, 10);
        List<Product> collect = productService.findPage(member, pageable).getContent();
        tags = tagService.findList(new Long[]{5l});
        List<Product> like = productService.findList(null, null, null, tags, null, null, null, true, true, null, false, null, null, null, 10, null, new ArrayList<Order>());
        model.addAttribute("hot", hot);
        model.addAttribute("collect", collect);
        model.addAttribute("like", like);
        return "b2b/product/interest";
    }

}