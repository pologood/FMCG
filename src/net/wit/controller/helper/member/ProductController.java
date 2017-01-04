/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.helper.member;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wit.*;
import net.wit.Filter.Operator;
import net.wit.Message;
import net.wit.controller.helper.model.DataBlock;
import net.wit.entity.*;
import net.wit.entity.Ad.LinkType;
import net.wit.entity.Product.OrderType;
import net.wit.entity.Tag.Type;
import net.wit.entity.TenantRelation.Status;
import net.wit.service.*;
import net.wit.util.QRBarCodeUtil;
import net.wit.util.SettingUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.zxing.WriterException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;


/**
 * Controller - 商品
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("helperMemberProductController")
@RequestMapping("/helper/member/product")
public class ProductController extends BaseController {

    @Resource(name = "productServiceImpl")
    private ProductService productService;

    @Resource(name = "productCategoryServiceImpl")
    private ProductCategoryService productCategoryService;

    @Resource(name = "productCategoryTenantServiceImpl")
    private ProductCategoryTenantService productCategoryTenantService;

    @Resource(name = "goodsServiceImpl")
    private GoodsService goodsService;

    @Resource(name = "brandServiceImpl")
    private BrandService brandService;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "brandSeriesServiceImpl")
    private BrandSeriesService brandSeriesService;

    @Resource(name = "promotionServiceImpl")
    private PromotionService promotionService;

    @Resource(name = "tagServiceImpl")
    private TagService tagService;

    @Resource(name = "memberRankServiceImpl")
    private MemberRankService memberRankService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "productImageServiceImpl")
    private ProductImageService productImageService;

    @Resource(name = "tenantRelationServiceImpl")
    private TenantRelationService tenantRelationService;

    @Resource(name = "specificationServiceImpl")
    private SpecificationService specificationService;

    @Resource(name = "specificationValueServiceImpl")
    private SpecificationValueService specificationValueService;

    @Resource(name = "unionServiceImpl")
    private UnionService unionService;

    @Resource(name = "productCategoryMemberServiceImpl")
    private ProductCategoryMemberService productCategoryMemberService;

    @Resource(name = "activityDetailServiceImpl")
    private ActivityDetailService activityDetailService;

    @Resource(name = "activityRulesServiceImpl")
    private ActivityRulesService activityRulesService;

    /**
     * 选择页面类型
     */
    public enum ChooseType {

        /**
         * 全部产品
         */
        all,
        /**
         * 收藏商品
         */
        collect,
        /**
         * 最惠商品
         */
        rate,
        /**
         * 热卖商品
         */
        hot,

        /**
         * 猜你喜欢
         */
        like,
        /**
         * 最近三个月
         */
        threeMonth
    }

    /**
     * 获取批量改价的商品数据
     * @param ids   商品id
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public
    @ResponseBody
    DataBlock view(Long... ids) {
        List<Object> list = new ArrayList<>();
        for (Long id : ids) {
            List<Map<String, Object>> maps = new ArrayList<>();
            Product product = productService.find(id);
            Map<String, Object> map = new HashMap<>();
            map.put("id", product.getId());
            map.put("fullName", product.getFullName());
            map.put("price", product.getPrice());
            map.put("marketPrice",product.getMarketPrice());
            Map<String,Object> memberPriceMap=new HashMap<>();
            for(MemberRank memberRank:memberRankService.findAll()){
                memberPriceMap.put("memberPrice_"+memberRank.getId().toString(),product.getMemberPrice().get(memberRank));
            }
            map.put("memberPrice",memberPriceMap);
            map.put("stock",product.getStock());
            map.put("availableStock",product.getAvailableStock());
            map.put("allocatedStock",product.getAllocatedStock());
            maps.add(map);
            for (Product product1 : product.getGoods().getProducts()) {
                if (product1 != product) {
                    map = new HashMap<>();
                    map.put("id", product1.getId());
                    map.put("fullName", product1.getFullName());
                    map.put("price", product1.getPrice());
                    map.put("marketPrice",product1.getMarketPrice());
                    memberPriceMap=new HashMap<>();
                    for(MemberRank memberRank:memberRankService.findAll()){
                        memberPriceMap.put("memberPrice_"+memberRank.getId().toString(),product1.getMemberPrice().get(memberRank));
                    }
                    map.put("memberPrice",memberPriceMap);
                    map.put("stock",product1.getStock());
                    map.put("availableStock",product1.getAvailableStock());
                    map.put("allocatedStock",product1.getAllocatedStock());
                    maps.add(map);
                }
            }
            list.add(maps);
        }
        return DataBlock.success(list, "执行成功");
    }

    /**
     * 批量改价
     * @param unitPrice 统一价格
     * @param ids   商品id
     */
    @RequestMapping(value = "/price", method = RequestMethod.GET)
    public @ResponseBody Message price(BigDecimal unitPrice, Long[] ids, HttpServletRequest request) {
        if(ids==null){
            return Message.error("无效商品Id");
        }
        for (Long id : ids) {
            Product product = productService.find(id);
            if(product==null){
                return Message.error("无效商品Id");
            }
            if (unitPrice != null) {
                product.setPrice(unitPrice);
            } else {
                String price = request.getParameter("price_" + id);
                if (StringUtils.isBlank(price)) {
                    return Message.error("无效商品价格");
                }
                product.setPrice(new BigDecimal(price));
            }

            for(MemberRank memberRank:memberRankService.findAll()){
                String unitMemberPrice=request.getParameter("unitMemberPrice_"+memberRank.getId());
                String memberPrice = request.getParameter("memberPrice_" + id + "_" + memberRank.getId());
                if(StringUtils.isNotBlank(unitMemberPrice)){
                    product.getMemberPrice().put(memberRank,new BigDecimal(unitMemberPrice));
                    if(memberRank.getIsDefault()){
                        product.setWholePrice(new BigDecimal(unitMemberPrice));
                    }
                }else{
                    if (StringUtils.isNotBlank(memberPrice)) {
                        product.getMemberPrice().put(memberRank, new BigDecimal(memberPrice));
                        if (memberRank.getIsDefault()) {
                            product.setWholePrice(new BigDecimal(memberPrice));
                        }
                    } else {
                        product.getMemberPrice().remove(memberRank);
                    }
                }
            }

            productService.update(product);
        }

        return Message.success("执行成功");
    }

    /**
     * 下级地区
     */
    @RequestMapping(value = "/getSeries", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, String> getSeries(Long id) {
        Map<String, String> data = new HashMap<String, String>();
        BrandSeries brandSeries = brandSeriesService.find(id);
        data.put("id", id.toString());
        data.put("name", brandSeries.getFullName());
        return data;
    }

    /**
     * 下级地区
     */
    @RequestMapping(value = "/brandSeries", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<Long, String> brandSeries(Long parentId) {
        List<BrandSeries> brandSerieses = new ArrayList<BrandSeries>();
        Map<Long, String> options = new HashMap<Long, String>();
        if (parentId != null) {
            BrandSeries parent = brandSeriesService.find(parentId);
            brandSerieses = new ArrayList<BrandSeries>(parent.getChildren());
        } else {
            brandSerieses = brandSeriesService.findRoots();
        }
        for (BrandSeries brandSeries : brandSerieses) {
            options.put(brandSeries.getId(), brandSeries.getName());
        }
        return options;
    }

    /**
     * 检查编号是否唯一
     */
    @RequestMapping(value = "/check_sn", method = RequestMethod.GET)
    public
    @ResponseBody
    boolean checkSn(String previousSn, String sn) {
        return !StringUtils.isEmpty(sn) && productService.snUnique(previousSn, sn);
    }

    /**
     * 获取参数组
     */
    @RequestMapping(value = "/parameter_groups", method = RequestMethod.GET)
    public
    @ResponseBody
    Set<ParameterGroup> parameterGroups(Long id) {
        ProductCategory productCategory = productCategoryService.find(id);
        return productCategory.getParameterGroups();
    }

    /**
     * 获取属性
     */
    @RequestMapping(value = "/attributes", method = RequestMethod.GET)
    public
    @ResponseBody
    Set<Attribute> attributes(Long id) {
        ProductCategory productCategory = productCategoryService.find(id);
        return productCategory.getAttributes();
    }

    /**
     * 添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(ModelMap model, Long productCategoryId) {
        Member member = memberService.getCurrent();
        //List<BrandSeries> brandSerieses = new ArrayList<BrandSeries>();

        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return "redirect:/member/tenant/add.jhtml";
        }
        ProductCategory productCategory = productCategoryService.find(productCategoryId);
        model.addAttribute("productCategory", productCategory);
        model.addAttribute("productCategoryId", productCategoryId);
        model.addAttribute("productCategoryTree", productCategoryService.findTree());
        model.addAttribute("productCategoryTreeTenant", productCategoryTenantService.findTree(tenant));
        model.addAttribute("brands", productCategory.getBrands());

        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("tenant", Operator.eq, tenant));
        filters.add(new Filter("status", Operator.eq, TenantRelation.Status.success));
        List<TenantRelation> relations = tenantRelationService.findList(null, filters, null);

        filters = new ArrayList<>();
        filters.add(new Filter("member", Operator.eq, member));
        List<ProductCategoryMember> productCategoryMembers = productCategoryMemberService.findList(null, filters, null);
        model.addAttribute("productCategoryMembers", productCategoryMembers);

        model.addAttribute("suppliers", relations);
        model.addAttribute("parameter_groups", productCategory.getParameterGroups());
        model.addAttribute("attributes", productCategory.getAttributes());

        model.addAttribute("tags", tagService.findList(Type.product));
        model.addAttribute("brandSerieses", brandSeriesService.findRoots());
        model.addAttribute("memberRanks", memberRankService.findAll());
        model.addAttribute("specifications", specificationService.findAll());
        model.addAttribute("pageActive", 2);
        model.addAttribute("member", member);
        return "/helper/member/product/add";
    }

    /**
     * 添加
     */
    @RequestMapping(value = "/addProductCategory", method = RequestMethod.GET)
    public String addProductCategory(ModelMap model, Long productCategoryId) {
        Member member = memberService.getCurrent();
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return "redirect:/member/tenant/add.jhtml";
        }
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("member", Operator.eq, member));
        List<ProductCategoryMember> productCategoryMembers = productCategoryMemberService.findList(null, filters, null);
        model.addAttribute("productCategoryTree", productCategoryService.findTree());
        model.addAttribute("pageActive", 2);
        model.addAttribute("member", member);
        model.addAttribute("productCategoryMembers", productCategoryMembers);
        return "/helper/member/product/addProductCategory";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Product product, Long productCategoryId, Long productCategoryTenantId, Long brandId, Long supplierId, Long[] tagIds, Long[] seriesIds, Long[] specificationIds,
                       String specification1Title, String specification2Title, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        Member member = memberService.getCurrent();
        if (member == null) {
            return "redirect:/helper/index.jhtml";
        }
        if (member.getTenant() == null) {
            return "redirect:/member/tenant/add.jhtml";
        }
        for (Iterator<ProductImage> iterator = product.getProductImages().iterator(); iterator.hasNext(); ) {
            ProductImage productImage = iterator.next();
            if (productImage == null || productImage.isEmpty()) {
                iterator.remove();
            }
        }
        product.setSupplier(tenantService.find(supplierId));
        product.setProductCategory(productCategoryService.find(productCategoryId));
        product.setProductCategoryTenant(productCategoryTenantService.find(productCategoryTenantId));
        product.setBrand(brandService.find(brandId));
        product.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
        product.setBrandSeries(new HashSet<BrandSeries>(brandSeriesService.findList(seriesIds)));
        product.setTenant(member.getTenant());
        if (StringUtils.isNotEmpty(product.getSn()) && productService.snExists(product.getSn())) {
            addFlashMessage(redirectAttributes,Message.error("货号不能重复"));
            return "redirect:/member/tenant/add.jhtml";
        }
        if (product.getMarketPrice() == null) {
            BigDecimal defaultMarketPrice = calculateDefaultMarketPrice(product.getPrice());
            product.setMarketPrice(defaultMarketPrice);
        }
        if (product.getWholePrice() == null) {
            product.setWholePrice(product.getPrice());
        }
        if (product.getPoint() == null) {
            long point = calculateDefaultPoint(product.getPrice());
            product.setPoint(point);
        }
        if (!isValid(product)) {
            return ERROR_VIEW;
        }
        //product.setFullName(null);
        product.setAllocatedStock(0);
        product.setScore(0F);
        product.setTotalScore(0L);
        product.setScoreCount(0L);
        product.setPriority(0L);
        product.setHits(0L);
        product.setWeekHits(0L);
        product.setMonthHits(0L);
        product.setSales(0L);
        product.setWeekSales(0L);
        product.setMonthSales(0L);
        product.setWeekHitsDate(new Date());
        product.setMonthHitsDate(new Date());
        product.setWeekSalesDate(new Date());
        product.setMonthSalesDate(new Date());
        product.setReviews(null);
        product.setConsultations(null);
        product.setFavoriteMembers(null);
        product.setPromotions(null);
        product.setCartItems(null);
        product.setOrderItems(null);
        product.setGiftItems(null);
        product.setProductNotifies(null);
//		product.setIsMarketable(true);
        if (product.getFee() == null) {
            product.setFee(BigDecimal.ZERO);
        }

        for (MemberRank memberRank : memberRankService.findAll()) {
            String price = request.getParameter("memberPrice_" + memberRank.getId());
            if (StringUtils.isNotEmpty(price) && new BigDecimal(price).compareTo(new BigDecimal(0)) >= 0) {
                product.getMemberPrice().put(memberRank, new BigDecimal(price));
                if (memberRank.getIsDefault()) {
                    product.setWholePrice(new BigDecimal(price));
                }
            } else {
                product.getMemberPrice().remove(memberRank);
            }
        }

        for (ProductImage productImage : product.getProductImages()) {
            if (productImage.getLocal() != null) {
                productImage.setLocalFile(new File(productImage.getLocal()));
                productImageService.build(productImage);
            }
        }
        Collections.sort(product.getProductImages());
        if (product.getImage() == null && product.getThumbnail() != null) {
            product.setImage(product.getThumbnail());
        }

        for (ParameterGroup parameterGroup : product.getProductCategory().getParameterGroups()) {
            for (Parameter parameter : parameterGroup.getParameters()) {
                String parameterValue = request.getParameter("parameter_" + parameter.getId());
                if (StringUtils.isNotEmpty(parameterValue)) {
                    product.getParameterValue().put(parameter, parameterValue);
                } else {
                    product.getParameterValue().remove(parameter);
                }
            }
        }

        for (Attribute attribute : product.getProductCategory().getAttributes()) {
            String attributeValue = request.getParameter("attribute_" + attribute.getId());
            if (StringUtils.isNotEmpty(attributeValue)) {
                product.setAttributeValue(attribute, attributeValue);
            } else {
                product.setAttributeValue(attribute, null);
            }
        }

        Goods goods = new Goods();
        List<Product> products = new ArrayList<Product>();
        if (specificationIds != null && specificationIds.length > 0) {
            String[] specificationPrice = request.getParameterValues("prices");
            String[] specificationStock = request.getParameterValues("stocks");
            String[] specificationBarcode = request.getParameterValues("barcodes");
            String[] specificationCost = request.getParameterValues("costs");
            //String[] specificationWholePrice = request.getParameterValues("specification_wholePrice");
            String[] specificationMarketPrice = request.getParameterValues("marketPrices");
            for (int i = 0; i < specificationIds.length; i++) {
                Specification specification = specificationService.find(specificationIds[i]);
                String[] specificationValueIds = request.getParameterValues("specification_" + specification.getId());
                if (specificationValueIds != null && specificationValueIds.length > 0) {
                    for (int j = 0; j < specificationValueIds.length; j++) {
                        if (i == 0) {
                            if (j == 0) {
                                product.setGoods(goods);
                                product.setSpecifications(new HashSet<Specification>());
                                product.setSpecificationValues(new HashSet<SpecificationValue>());
                                product.setSupplier(tenantService.find(supplierId));
                                products.add(product);
                            } else {
                                Product specificationProduct = new Product();
                                BeanUtils.copyProperties(product, specificationProduct);
                                specificationProduct.setId(null);
                                specificationProduct.setCreateDate(null);
                                specificationProduct.setModifyDate(null);
                                specificationProduct.setSn(null);
                                specificationProduct.setFullName(null);
                                specificationProduct.setAllocatedStock(0);
                                specificationProduct.setIsList(false);
                                specificationProduct.setScore(0F);
                                specificationProduct.setPriority(0L);
                                specificationProduct.setTotalScore(0L);
                                specificationProduct.setScoreCount(0L);
                                specificationProduct.setHits(0L);
                                specificationProduct.setWeekHits(0L);
                                specificationProduct.setMonthHits(0L);
                                specificationProduct.setSales(0L);
                                specificationProduct.setWeekSales(0L);
                                specificationProduct.setMonthSales(0L);
                                specificationProduct.setWeekHitsDate(new Date());
                                specificationProduct.setMonthHitsDate(new Date());
                                specificationProduct.setWeekSalesDate(new Date());
                                specificationProduct.setMonthSalesDate(new Date());
                                specificationProduct.setGoods(goods);
                                specificationProduct.setReviews(null);
                                specificationProduct.setConsultations(null);
                                specificationProduct.setFavoriteMembers(null);
                                specificationProduct.setSpecifications(new HashSet<Specification>());
                                specificationProduct.setSpecificationValues(new HashSet<SpecificationValue>());
                                specificationProduct.setPromotions(null);
                                specificationProduct.setCartItems(null);
                                specificationProduct.setOrderItems(null);
                                specificationProduct.setGiftItems(null);
                                specificationProduct.setProductNotifies(null);
                                specificationProduct.setSupplier(tenantService.find(supplierId));
                                products.add(specificationProduct);
                            }
                        }
                        Product specificationProduct = products.get(j);
                        specificationProduct.setOrder(j);
                        SpecificationValue specificationValue = specificationValueService.findByName(specification, specificationValueIds[j]);
                        if (specificationValue == null) {
                            specificationValue = new SpecificationValue();
                            specificationValue.setName(specificationValueIds[j]);
                            specificationValue.setSpecification(specification);
                            specificationValueService.save(specificationValue);
                        }
                        for (MemberRank memberRank : memberRankService.findAll()) {
                            String[] specificationMemberPrice = request.getParameterValues("specification_memberPrice_" + memberRank.getId()+"s");
                            if (specificationMemberPrice!=null && specificationMemberPrice.length>0) {
                                String price = specificationMemberPrice[j];
                                if (StringUtils.isNotEmpty(price) && new BigDecimal(price).compareTo(new BigDecimal(0)) >= 0) {
                                    specificationProduct.getMemberPrice().put(memberRank, new BigDecimal(price));
                                } else {
                                    specificationProduct.getMemberPrice().remove(memberRank);
                                }
                            }
                        }

                        specificationProduct.setMarketPrice(product.getMarketPrice());
                        specificationProduct.setPrice(new BigDecimal(specificationPrice[j]));
                        specificationProduct.setStock(new Integer(specificationStock[j]));
                        if (specificationCost!=null && specificationCost.length>0) {
                            specificationProduct.setCost(new BigDecimal(specificationCost[j]));
                        }
                        specificationProduct.setWholePrice(product.getWholePrice());
                        if (specificationMarketPrice!=null && specificationMarketPrice.length>0) {
                            specificationProduct.setMarketPrice(new BigDecimal(specificationMarketPrice[j]));
                        }
                        specificationProduct.setOrder(j);
                        specificationProduct.setBarcode(specificationBarcode[j]);
                        specificationProduct.getSpecifications().add(specification);
                        specificationProduct.getSpecificationValues().add(specificationValue);
                    }
                }
            }
        } else {
        	product.setOrder(1);
            product.setGoods(goods);
            product.setSpecifications(null);
            product.setSpecificationValues(null);
            product.setSupplier(tenantService.find(supplierId));
            products.add(product);
        }
        goods.getProducts().clear();
        goods.getProducts().addAll(products);
        goods.setSpecification1Title(specification1Title);
        goods.setSpecification2Title(specification2Title);
        goodsService.save(goods);

        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        //TODO 发布新品领取积分，每日一次
        activityDetailService.addPoint(null,member.getTenant(),activityRulesService.find(49L));

        //TODO 进入“商品”，并尝试上架一件商品
        if(!activityDetailService.isActivity(null,memberService.getCurrent().getTenant(),activityRulesService.find(15L))){
            activityDetailService.addPoint(null,memberService.getCurrent().getTenant(),activityRulesService.find(15L));
        }

        if(product.getSpecifications()!=null&&product.getSpecifications().size()>0){
            if(!activityDetailService.isActivity(null,memberService.getCurrent().getTenant(),activityRulesService.find(18L))){
                activityDetailService.addPoint(null,memberService.getCurrent().getTenant(),activityRulesService.find(18L));
            }
        }

        List<Product> productPage = productService.openList(null,memberService.getCurrent().getTenant(),null,true,null,null,null,null,null,null,null,null,null,null,null,OrderType.dateDesc);

        if(productPage!=null){
            if(productPage.size()>20){
                if(!activityDetailService.isActivity(null,memberService.getCurrent().getTenant(),activityRulesService.find(16L))){
                    activityDetailService.addPoint(null,memberService.getCurrent().getTenant(),activityRulesService.find(16L));
                }
            }

            if(productPage.size()>50){
                if(!activityDetailService.isActivity(null,memberService.getCurrent().getTenant(),activityRulesService.find(17L))){
                    activityDetailService.addPoint(null,memberService.getCurrent().getTenant(),activityRulesService.find(17L));
                }
            }
        }

        if (product.getIsMarketable()) {
            return "redirect:isMarketableList.jhtml";
        } else {
            return "redirect:notMarketablelist.jhtml";
        }
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        Member member = memberService.getCurrent();
        //List<BrandSeries> brandSerieses = new ArrayList<BrandSeries>();

        Tenant tenant = member.getTenant();
        if (member.getTenant() == null) {
            return "redirect:/member/tenant/add.jhtml";
        }
        model.addAttribute("productCategoryTree", productCategoryService.findTree());
        model.addAttribute("productCategoryTreeTenant", productCategoryTenantService.findTree(tenant));
        Product product = productService.find(id);
        ProductCategory productCategory = productCategoryService.find(product.getProductCategory().getId());
        model.addAttribute("brands", productCategory.getBrands());

        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("tenant", Operator.eq, tenant));
        filters.add(new Filter("status", Operator.eq, TenantRelation.Status.success));
        List<TenantRelation> relations = tenantRelationService.findList(null, filters, null);

        model.addAttribute("suppliers", relations);

        model.addAttribute("parameter_groups", productCategory.getParameterGroups());
        model.addAttribute("attributes", productCategory.getAttributes());

        model.addAttribute("brand", product.getBrand());
        model.addAttribute("tags", tagService.findList(Type.product));
        model.addAttribute("brandSerieses", brandSeriesService.findRoots());
        model.addAttribute("memberRanks", memberRankService.findAll());
        model.addAttribute("specifications", specificationService.findAll());
        model.addAttribute("product", product);
        model.addAttribute("pageActive", 2);
        model.addAttribute("member", member);
        return "/helper/member/product/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Product product, Long productCategoryId, Long productCategoryTenantId, Long brandId, Long supplierId, Long[] seriesIds, Long[] tagIds, Long[] specificationIds, Long[] specificationProductIds, HttpServletRequest request,
                          String specification1Title, String specification2Title, String specificationType, RedirectAttributes redirectAttributes) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return ERROR_VIEW;
        }
        for (Iterator<ProductImage> iterator = product.getProductImages().iterator(); iterator.hasNext(); ) {
            ProductImage productImage = iterator.next();
            if (productImage == null || productImage.isEmpty()) {
                iterator.remove();
            }
        }
        Boolean isMarketable = product.getIsMarketable();
        if (isMarketable != null) {
            if (isMarketable) {
                product.setIsMarketable(true);
            } else {
                product.setIsMarketable(false);
            }
        } else {
            isMarketable = false;
            product.setIsMarketable(false);
        }

        product.setProductCategory(productCategoryService.find(productCategoryId));
        product.setProductCategoryTenant(productCategoryTenantService.find(productCategoryTenantId));
        product.setBrand(brandService.find(brandId));
        product.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
        product.setBrandSeries(new HashSet<BrandSeries>(brandSeriesService.findList(seriesIds)));
        if (product.getFee() == null) {
            product.setFee(BigDecimal.ZERO);
        }

        Product pProduct = productService.find(product.getId());
        if (pProduct == null) {
            return ERROR_VIEW;
        }
        product.setTenant(pProduct.getTenant());
        if (StringUtils.isNotEmpty(product.getSn()) && !productService.snUnique(pProduct.getSn(), product.getSn())) {
            return ERROR_VIEW;
        }
        if (product.getMarketPrice() == null) {
            BigDecimal defaultMarketPrice = calculateDefaultMarketPrice(product.getPrice());
            product.setMarketPrice(defaultMarketPrice);
        }
        if (product.getWholePrice() == null) {
            product.setWholePrice(product.getPrice());
        }
        if (product.getPoint() == null) {
            long point = calculateDefaultPoint(product.getPrice());
            product.setPoint(point);
        }

        for (MemberRank memberRank : memberRankService.findAll()) {
            String price = request.getParameter("memberPrice_" + memberRank.getId());
            if (StringUtils.isNotEmpty(price) && new BigDecimal(price).compareTo(new BigDecimal(0)) >= 0) {
                product.getMemberPrice().put(memberRank, new BigDecimal(price));
                if (memberRank.getIsDefault()) {
                    product.setWholePrice(new BigDecimal(price));
                }
            } else {
                product.getMemberPrice().remove(memberRank);
            }
        }
        if (!isValid(product)) {
            return ERROR_VIEW;
        }

        for (ProductImage productImage : product.getProductImages()) {
            if (productImage.getLocal() != null) {
                productImage.setLocalFile(new File(productImage.getLocal()));
                productImageService.build(productImage);
            }
        }
        Collections.sort(product.getProductImages());
        if (product.getImage() == null && product.getThumbnail() != null) {
            product.setImage(product.getThumbnail());
        }
        for (ParameterGroup parameterGroup : product.getProductCategory().getParameterGroups()) {
            for (Parameter parameter : parameterGroup.getParameters()) {
                String parameterValue = request.getParameter("parameter_" + parameter.getId());
                if (StringUtils.isNotEmpty(parameterValue)) {
                    product.getParameterValue().put(parameter, parameterValue);
                } else {
                    product.getParameterValue().remove(parameter);
                }
            }
        }
        for (Attribute attribute : product.getProductCategory().getAttributes()) {
            String attributeValue = request.getParameter("attribute_" + attribute.getId());
            if (StringUtils.isNotEmpty(attributeValue)) {
                product.setAttributeValue(attribute, attributeValue);
            } else {
                product.setAttributeValue(attribute, null);
            }
        }
        if (product.getFee()==null) {
            product.setFee(BigDecimal.ZERO);
        }

        Goods goods = pProduct.getGoods();
        List<Product> products = new ArrayList<Product>();
        if (specificationType.equals("many") && specificationIds != null && specificationIds.length > 0) {
            String[] specificationPrice = request.getParameterValues("prices");
            String[] specificationStock = request.getParameterValues("stocks");
            String[] specificationBarcode = request.getParameterValues("barcodes");
            String[] specificationCost = request.getParameterValues("costs");
            String[] specificationMarketPrice = request.getParameterValues("marketPrices");
            for (int i = 0; i < specificationIds.length; i++) {
                Specification specification = specificationService.find(specificationIds[i]);
                String[] specificationValueIds = request.getParameterValues("specification_" + specification.getId());
                if (specificationValueIds != null && specificationValueIds.length > 0) {
                    for (int j = 0; j < specificationValueIds.length; j++) {
                        if (i == 0) {
                            if (specificationProductIds != null && j < specificationProductIds.length) {
                                Product specificationProduct = productService.find(specificationProductIds[j]);
                                if (j == 0) {
                                    if (specificationProductIds[j] == pProduct.getId()) {
                                        specificationProduct = pProduct;
                                    }
                                    BeanUtils.copyProperties(product, specificationProduct, new String[]{"id", "sn", "createDate", "modifyDate", "fullName", "allocatedStock", "score", "totalScore", "scoreCount", "hits", "weekHits", "monthHits", "sales", "weekSales",
                                            "monthSales", "weekHitsDate", "monthHitsDate", "weekSalesDate", "monthSalesDate", "goods", "reviews", "consultations", "favoriteMembers", "specifications", "specificationValues", "promotions", "cartItems",
                                            "productImage", "orderItems", "giftItems", "productNotifies", "supplierProductSn", "packagUnits"});
                                    specificationProduct.setIsList(true);
                                } else {
                                    if (specificationProduct == null || (specificationProduct.getGoods() != null && !specificationProduct.getGoods().equals(goods))) {
                                        return ERROR_VIEW;
                                    }
                                    specificationProduct.setName(product.getName());
                                    specificationProduct.setProductCategoryTenant(product.getProductCategoryTenant());
                                    specificationProduct.setDescriptionapp(product.getDescriptionapp());
                                    specificationProduct.setUnit(product.getUnit());
                                    specificationProduct.setTags(new HashSet<Tag>(product.getTags()));
                                    specificationProduct.setProductImages(new ArrayList<ProductImage>(product.getProductImages()));
                                    specificationProduct.setIsGift(pProduct.getIsGift());
                                    specificationProduct.setIsList(false);
                                    specificationProduct.setIsTop(pProduct.getIsTop());
                                    specificationProduct.setIsMarketable(true);
                                    specificationProduct.setMarketPrice(product.getMarketPrice());
                                    specificationProduct.setSupplier(product.getSupplier());
                                    specificationProduct.setCost(product.getCost());
                                    specificationProduct.setFee(product.getFee());
                                    specificationProduct.setIntroduction(product.getIntroduction());
                                }
                                specificationProduct.setSpecifications(new HashSet<Specification>());
                                specificationProduct.setSpecificationValues(new HashSet<SpecificationValue>());
                                specificationProduct.setSupplier(tenantService.find(supplierId));
                                products.add(specificationProduct);
                            } else {
                                Product specificationProduct = new Product();
                                BeanUtils.copyProperties(product, specificationProduct);
                                specificationProduct.setId(null);
                                specificationProduct.setCreateDate(null);
                                specificationProduct.setModifyDate(null);
                                specificationProduct.setSn(null);
                                specificationProduct.setFullName(null);
                                specificationProduct.setAllocatedStock(0);
                                if (j == 0) {
                                    specificationProduct.setIsList(true);
                                } else {
                                    specificationProduct.setIsList(false);
                                }
                                specificationProduct.setScore(0F);
                                specificationProduct.setTotalScore(0L);
                                specificationProduct.setScoreCount(0L);
                                specificationProduct.setHits(0L);
                                specificationProduct.setWeekHits(0L);
                                specificationProduct.setMonthHits(0L);
                                specificationProduct.setSales(0L);
                                specificationProduct.setWeekSales(0L);
                                specificationProduct.setMonthSales(0L);
                                specificationProduct.setWeekHitsDate(new Date());
                                specificationProduct.setMonthHitsDate(new Date());
                                specificationProduct.setWeekSalesDate(new Date());
                                specificationProduct.setMonthSalesDate(new Date());
                                specificationProduct.setGoods(goods);
                                specificationProduct.setReviews(null);
                                specificationProduct.setConsultations(null);
                                specificationProduct.setFavoriteMembers(null);
                                specificationProduct.setSpecifications(new HashSet<Specification>());
                                specificationProduct.setSpecificationValues(new HashSet<SpecificationValue>());
                                specificationProduct.setPromotions(null);
                                specificationProduct.setCartItems(null);
                                specificationProduct.setOrderItems(null);
                                specificationProduct.setGiftItems(null);
                                specificationProduct.setProductNotifies(null);
                                specificationProduct.setSupplier(tenantService.find(supplierId));
                                products.add(specificationProduct);
                            }
                        }
                        Product specificationProduct = products.get(j);
                        specificationProduct.setOrder(j);
                        SpecificationValue specificationValue = specificationValueService.findByName(specification,specificationValueIds[j]);
                        if (specificationValue==null) {
                            specificationValue = new SpecificationValue();
                            specificationValue.setName(specificationValueIds[j]);
                            specificationValue.setSpecification(specification);
                            specificationValue.setProducts(null);
                            specificationValueService.save(specificationValue);
                        }
                        specificationProduct.setPrice(new BigDecimal(specificationPrice[j]));
                        ResourceBundle wit = PropertyResourceBundle.getBundle("wit");
                        if(!wit.getString("system.version.type").equals("1")){//聚德惠版本的库存不给修改
                            specificationProduct.setStock(new Integer(specificationStock[j]));
                        }
                        if (specificationCost!=null && specificationCost.length>0) {
                            specificationProduct.setCost(new BigDecimal(specificationCost[j]));
                        }
                        specificationProduct.setWholePrice(product.getWholePrice());
                        specificationProduct.setBarcode(specificationBarcode[j]);
                        specificationProduct.setFee(product.getFee());
                        specificationProduct.setMemberPrice(new HashMap<MemberRank, BigDecimal>(product.getMemberPrice()));
                        specificationProduct.setMarketPrice(product.getMarketPrice());
                        if (specificationMarketPrice!=null && specificationMarketPrice.length>0) {
                            specificationProduct.setMarketPrice(new BigDecimal(specificationMarketPrice[j]));
                        }

                        for (MemberRank memberRank : memberRankService.findAll()) {
                            String[] specificationMemberPrice = request.getParameterValues("memberPrice_" + memberRank.getId()+"s");
                            if (specificationMemberPrice!=null && specificationMemberPrice.length>0) {
                                String price = specificationMemberPrice[j];
                                if (StringUtils.isNotEmpty(price) && new BigDecimal(price).compareTo(new BigDecimal(0)) >= 0) {
                                    specificationProduct.getMemberPrice().put(memberRank, new BigDecimal(price));
                                } else {
                                    specificationProduct.getMemberPrice().remove(memberRank);
                                }
                            }
                        }

                        specificationProduct.getSpecifications().add(specification);
                        specificationProduct.getSpecificationValues().add(specificationValue);
                        specificationProduct.setProductCategoryTenant(product.getProductCategoryTenant());
                    }
                }
            }
        } else {
            product.setSpecifications(null);
            product.setSpecificationValues(null);
            try {
                pProduct.setFullName(product.getFullName());
            } catch (Exception e) {
                pProduct.setFullName(null);
            }
            ResourceBundle wit = PropertyResourceBundle.getBundle("wit");
            if(wit.getString("system.version.type").equals("1")) {//聚德惠版本的库存不给修改
                BeanUtils.copyProperties(product, pProduct, new String[]{"id", "createDate", "modifyDate", "fullName", "allocatedStock", "score", "totalScore", "scoreCount", "hits", "weekHits", "monthHits", "sales", "weekSales", "monthSales",
                        "weekHitsDate", "monthHitsDate", "weekSalesDate", "monthSalesDate", "goods", "reviews", "consultations", "favoriteMembers", "promotions", "cartItems", "orderItems", "giftItems", "productNotifies", "supplierProductSn", "packagUnits","stock"});
            }else{
                BeanUtils.copyProperties(product, pProduct, new String[]{"id", "createDate", "modifyDate", "fullName", "allocatedStock", "score", "totalScore", "scoreCount", "hits", "weekHits", "monthHits", "sales", "weekSales", "monthSales",
                        "weekHitsDate", "monthHitsDate", "weekSalesDate", "monthSalesDate", "goods", "reviews", "consultations", "favoriteMembers", "promotions", "cartItems", "orderItems", "giftItems", "productNotifies", "supplierProductSn", "packagUnits"});
            }
            pProduct.setSupplier(tenantService.find(supplierId));
            pProduct.setOrder(1);
            products.add(pProduct);
        }

        goods.getProducts().clear();
        goods.getProducts().addAll(products);
        goods.setSpecification1Title(specification1Title);
        goods.setSpecification2Title(specification2Title);
        goodsService.update(goods);

        if(!activityDetailService.isActivity(null,memberService.getCurrent().getTenant(),activityRulesService.find(20L))){
            activityDetailService.addPoint(null,memberService.getCurrent().getTenant(),activityRulesService.find(20L));
        }

        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        if (isMarketable) {
            return "redirect:isMarketableList.jhtml";
        } else {
            return "redirect:notMarketablelist.jhtml";
        }
    }

    /**
     * 已上架列表
     */
    @RequestMapping(value = "/isMarketableList", method = RequestMethod.GET)
    public String isMarketableList(Long productCategoryId, Long productCategoryTenantId, Long brandId, String searchValue, Long promotionId, Long tagId, Long supplierId, @RequestParam(defaultValue = "true") Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift,
                                   Boolean isOutOfStock, Boolean isStockAlert, Pageable pageable, ModelMap model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return ERROR_VIEW;
        }
        if(isList==null) isList=true;
        ProductCategory productCategory = productCategoryService.find(productCategoryId);
        ProductCategoryTenant productCategoryTenant = productCategoryTenantService.find(productCategoryTenantId);
        Brand brand = brandService.find(brandId);
        Promotion promotion = promotionService.find(promotionId);
        List<Tag> tags = tagService.findList(tagId);
        model.addAttribute("union", unionService.findAll());
        model.addAttribute("productCategoryTree", productCategoryService.findTree());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("promotions", promotionService.findAll());
        model.addAttribute("tags", tagService.findList(Type.product));
        model.addAttribute("productCategoryId", productCategoryId);
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
        model.addAttribute("searchValue",searchValue);
//        model.addAttribute("page",
//                productService.findMyPage(member.getTenant(), searchValue, productCategory, productCategoryTenant, brand, promotion, tags, null, null, null, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, OrderType.dateDesc, pageable));

        Set<ProductCategory> productCategories=new HashSet<>();
        if(productCategory!=null){
            productCategories.add(productCategory);
        }
        List<Filter> filters=new ArrayList<>();
        filters.add(new Filter("supplier",Operator.eq,tenantService.find(supplierId)));

//        filters.add(new Filter("isGift",Operator.eq,isGift));
//        filters.add(new Filter("isTop",Operator.eq,isTop));
//        filters.add(new Filter("isList",Operator.eq,isList));
        //filters.add(new Filter("isOutOfStock",Operator.eq,isOutOfStock));
        //filters.add(new Filter("isStockAlert",Operator.eq,isStockAlert));

        pageable.setFilters(filters);
//        Page<Product> page = productService.openPage(pageable,member.getTenant(),productCategories,isMarketable,isList,brand,promotion,tags,searchValue,null,null,null,null,null,OrderType.dateDesc);
        Page<Product> page=productService.findMyPage(member.getTenant(), searchValue, productCategory, productCategoryTenant, brand, promotion, tags, null, null, null, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, OrderType.dateDesc, pageable);
        model.addAttribute("page",page);
        model.addAttribute("pageActive", 2);
        model.addAttribute("member", member);
        filters=new ArrayList<>();
        filters.add(new Filter("tenant",Operator.eq,member.getTenant()));
        filters.add(new Filter("status",Operator.eq, Status.success));
        model.addAttribute("suppliers",tenantRelationService.findList(null,filters,null));
        model.addAttribute("supplierId",supplierId);
        model.addAttribute("memberRanks",memberRankService.findAll());
        return "/helper/member/product/isMarketableList";
    }
    /**
     * 已上架列表
     */
    @RequestMapping(value = "/isMarketableList_export", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> isMarketableListExport(Long productCategoryId, Long productCategoryTenantId, Long brandId, String searchValue, Long promotionId, Long tagId, Long supplierId, @RequestParam(defaultValue = "true") Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift,
                                   Boolean isOutOfStock, Boolean isStockAlert, Pageable pageable, ModelMap model) {
        Member member = memberService.getCurrent();
        if(isList==null) isList=true;
        ProductCategory productCategory = productCategoryService.find(productCategoryId);
//        ProductCategoryTenant productCategoryTenant = productCategoryTenantService.find(productCategoryTenantId);
        Brand brand = brandService.find(brandId);
        Promotion promotion = promotionService.find(promotionId);
        List<Tag> tags = tagService.findList(tagId);
        Set<ProductCategory> productCategories=new HashSet<>();
        if(productCategory!=null){
            productCategories.add(productCategory);
        }
        List<Filter> filters=new ArrayList<>();
        filters.add(new Filter("supplier",Operator.eq,tenantService.find(supplierId)));
        filters.add(new Filter("isGift",Operator.eq,isGift));
        filters.add(new Filter("isTop",Operator.eq,isTop));
        filters.add(new Filter("isList",Operator.eq,isList));
        filters.add(new Filter("isOutOfStock",Operator.eq,isOutOfStock));
        filters.add(new Filter("isStockAlert",Operator.eq,isStockAlert));
        List<Product> products=productService.openList(null, member.getTenant(), productCategories, 
        		isMarketable, isList, brand, promotion, tags, searchValue, null, null, null,
        		null, null, filters, OrderType.dateDesc);
        List<Map<String, Object>> maps=new ArrayList<Map<String, Object>>();
        for(Product product:products){
        	Map<String, Object> map=new HashMap<String, Object>();
        	map.put("sn", product.getSn());
        	map.put("barcode", product.getBarcode());
        	map.put("name", product.getFullName());
        	map.put("tenantName", product.getTenant().getName());
        	if(product.getSupplier()!=null){
        		map.put("supplierName", product.getSupplier().getName());
        	}else{
        		map.put("supplierName", "--");
        	}
        	map.put("productCategory", product.getProductCategory().getName());
        	map.put("unit", product.getUnit());
        	map.put("price", product.getPrice());
        	map.put("cost", product.getCost());
        	if(product.getIsMarketable()!=null&& product.getIsMarketable()){
        		map.put("isMarket","已上架");
        	}else{
        		map.put("isMarket","已下架");
        	}
        	if(product.getStock()<0){
        		map.put("stock", 0);
        	}else{
        		map.put("stock", product.getStock());
        	}
        	map.put("availableStock", product.getAvailableStock());
        	maps.add(map);
        }
        return maps;
    }

    /**
     * 已下架列表
     */
    @RequestMapping(value = "/notMarketablelist", method = RequestMethod.GET)
    public String notMarketablelist(Long productCategoryId, Long productCategoryTenantId, Long brandId, Long supplierId, String searchValue, Long promotionId, Long tagId, @RequestParam(defaultValue = "false") Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift,
                                    Boolean isOutOfStock, Boolean isStockAlert, Pageable pageable, ModelMap model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return ERROR_VIEW;
        }
        if (member.getTenant() == null) {
            return "redirect:/member/tenant/add.jhtml";
        }
        if(isList==null) isList=true;
        ProductCategory productCategory = productCategoryService.find(productCategoryId);
        ProductCategoryTenant productCategoryTenant = productCategoryTenantService.find(productCategoryTenantId);
        Brand brand = brandService.find(brandId);
        Promotion promotion = promotionService.find(promotionId);
        List<Tag> tags = tagService.findList(tagId);
        model.addAttribute("union", unionService.findAll());
        model.addAttribute("productCategoryTree", productCategoryService.findTree());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("promotions", promotionService.findAll());
        model.addAttribute("tags", tagService.findList(Type.product));
        model.addAttribute("productCategoryId", productCategoryId);
        model.addAttribute("brandId", brandId);
        model.addAttribute("promotionId", promotionId);
        model.addAttribute("tagId", tagId);
        model.addAttribute("isMarketable", isMarketable);
        model.addAttribute("isList", isList);
        model.addAttribute("isTop", isTop);
        model.addAttribute("isGift", isGift);
        model.addAttribute("isOutOfStock", isOutOfStock);
        model.addAttribute("isStockAlert", isStockAlert);
        model.addAttribute("searchValue",searchValue);
        Set<ProductCategory> productCategories=new HashSet<>();
        if(productCategory!=null){
            productCategories.add(productCategory);
        }
        List<Filter> filters=new ArrayList<>();
        filters.add(new Filter("supplier",Operator.eq,tenantService.find(supplierId)));

        filters.add(new Filter("isGift",Operator.eq,isGift));
        filters.add(new Filter("isTop",Operator.eq,isTop));
//        filters.add(new Filter("isList",Operator.eq,isList));
        //filters.add(new Filter("isOutOfStock",Operator.eq,isOutOfStock));
        //filters.add(new Filter("isStockAlert",Operator.eq,isStockAlert));

        pageable.setFilters(filters);
        Page<Product> page = productService.openPage(pageable,member.getTenant(),productCategories,isMarketable,isList,brand,promotion,tags,searchValue,null,null,null,null,null,OrderType.dateDesc);
        model.addAttribute("page",page);
        model.addAttribute("pageActive", 2);
        model.addAttribute("member", member);
        filters=new ArrayList<>();
        filters.add(new Filter("tenant",Operator.eq,member.getTenant()));
        filters.add(new Filter("status",Operator.eq, Status.success));
        model.addAttribute("suppliers",tenantRelationService.findList(null,filters,null));
        model.addAttribute("supplierId",supplierId);
        model.addAttribute("memberRanks",memberRankService.findAll());
        return "/helper/member/product/notMarketablelist";
    }


    /*
     * 上架
     */
    @RequestMapping(value = "/upMarketable", method = RequestMethod.GET)
    @ResponseBody
    public Message upMarketable(Long id) {
        Product product = productService.find(id);
        product.setIsMarketable(true);
        productService.update(product);
        return Message.success("上架成功");
    }

    /*
     * 批量上架
     */
    @RequestMapping(value = "/upMarketables", method = RequestMethod.GET)
    @ResponseBody
    public Message upMarketables(Long[] ids) {
        for (Long id : ids) {
            Product product = productService.find(id);
            product.setIsMarketable(true);
            productService.update(product);
        }
        return Message.success("上架成功");
    }

    /*
     * 下架
     */
    @RequestMapping(value = "/downMarketable", method = RequestMethod.GET)
    @ResponseBody
    public Message downMarketable(Long id) {
        Product product = productService.find(id);
        product.setIsMarketable(false);
        productService.update(product);
        //TODO 进入“商品”，下架商品
        if(!activityDetailService.isActivity(null,memberService.getCurrent().getTenant(),activityRulesService.find(19L))){
            activityDetailService.addPoint(null,memberService.getCurrent().getTenant(),activityRulesService.find(19L));
        }
        return Message.success("下架成功");
    }

    /*
     * 批量下架
     */
    @RequestMapping(value = "/downMarketables", method = RequestMethod.GET)
    @ResponseBody
    public Message downMarketables(Long[] ids) {
        for (Long id : ids) {
            Product product = productService.find(id);
            product.setIsMarketable(false);
            productService.update(product);
        }
        //TODO 进入“商品”，下架商品
        if (!activityDetailService.isActivity(null, memberService.getCurrent().getTenant(), activityRulesService.find(19L))) {
            activityDetailService.addPoint(null, memberService.getCurrent().getTenant(), activityRulesService.find(19L));
        }
        return Message.success("下架成功");
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Message delete(Long[] ids) {

//        for (Long id : ids) {
//            Product product = productService.find(id);
//            if (product.getSales().compareTo(0L) > 0) {
//                return ERROR_MESSAGE;
//            }
//        }

        productService.delete(ids);
        return SUCCESS_MESSAGE;
    }

    /**
     * 计算默认市场价
     *
     * @param price 价格
     */
    private BigDecimal calculateDefaultMarketPrice(BigDecimal price) {
        Setting setting = SettingUtils.get();
        Double defaultMarketPriceScale = setting.getDefaultMarketPriceScale();
        return setting.setScale(price.multiply(new BigDecimal(defaultMarketPriceScale.toString())));
    }

    /**
     * 计算默认积分
     *
     * @param price 价格
     */
    private long calculateDefaultPoint(BigDecimal price) {
        Setting setting = SettingUtils.get();
        Double defaultPointScale = setting.getDefaultPointScale();
        return price.multiply(new BigDecimal(defaultPointScale.toString())).longValue();
    }

    /**
     * 根据
     */
    @RequestMapping(value = "/qrcode", method = RequestMethod.GET)
    public void qrcode(Long id, HttpServletRequest request, HttpServletResponse response) {
        try {
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            // 第三方用户唯一凭证
            String appId = bundle.getString("APPID");// 睿商圈
            String redirectUri = URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/wap/product/content/" + id.toString() + "/product.jhtml");
            String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri=" + redirectUri + "&response_type=code&scope=snsapi_base&state=123&from=singlemessage&isappinstalled=1#wechat_redirect";

            String tempFile = System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".jpg";
            response.reset();
            response.setContentType("image/jpeg;charset=utf-8");
            try {
                QRBarCodeUtil.encodeQRCode(url, tempFile, 200, 200);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            ServletOutputStream output = response.getOutputStream();// 得到输出流
            InputStream imageIn = new FileInputStream(new File(tempFile));
            // 得到输入的编码器，将文件流进行jpg格式编码
            JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(imageIn);
            // 得到编码后的图片对象
            BufferedImage image = decoder.decodeAsBufferedImage();
            // 得到输出的编码器
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);
            encoder.encode(image);// 对图片进行输出编码
            imageIn.close();// 关闭文件流
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取供应商的商品
     * @param id 店铺Id
     */
    @RequestMapping(value = "/supplier/products", method = RequestMethod.GET)
    public @ResponseBody DataBlock supplierProducts(Long id) {
        Tenant tenant = tenantService.find(id);
        if (tenant == null) {
            return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("supplier", Operator.eq, tenant));
        filters.add(new Filter("isList", Operator.eq, true));
        filters.add(new Filter("isMarketable", Operator.eq, true));
        List<Product> products = productService.findList(null, filters, null);

        List<Object> list = new ArrayList<>();
        for(Product product:products){
            List<Map<String,Object>> maps=new ArrayList<>();
            Map<String,Object> map=new HashMap<>();
            map.put("id",product.getId());
            map.put("fullName",product.getFullName());
            maps.add(map);
            for(Product product1:product.getGoods().getProducts()){
                if(product1!=product){
                    map=new HashMap<>();
                    map.put("id",product1.getId());
                    map.put("fullName",product1.getFullName());
                    maps.add(map);
                }
            }
            list.add(maps);
        }
        return DataBlock.success(list, "执行成功");
    }

    /**
     * 商品批量上架
     * @param ids 商品id
     */
    @RequestMapping(value = "/spUpMarket", method = RequestMethod.GET)
    public @ResponseBody DataBlock spUpMarket(Long[] ids,HttpServletRequest request){
        Member member=memberService.getCurrent();
        if(member==null){
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        if(ids==null){
            return DataBlock.error("请选择需要上架的商品");
        }
        List<Long> list=new ArrayList<>();
        for (Long id:ids){
            if(list.contains(id)){
                continue;
            }
            Product product=productService.find(id);
            if(product==null){
                return DataBlock.error("商品id无效");
            }
            Goods goods=new Goods();
            Product product1=new Product();
            BeanUtils.copyProperties(product, product1, new String[]{"id", "sn", "stockMemo", "isLimit", "limitCounts", "barcode", "productCategoryTenant", "brandSeries", "productImages", "reviews", "consultations", "tags", "favoriteMembers", "coupons", "promotionProducts", "cartItems", "orderItems", "productItems", "giftItems", "productNotifies","memberPrice","parameterValue","packagUnits"});
            String upMarketStock=request.getParameter("upMarketStock_"+id);
            if(StringUtils.isBlank(upMarketStock)){
                return DataBlock.error("上架数量不能为空");
            }
            product1.setStock(Integer.parseInt(upMarketStock));
            product1.setAllocatedStock(0);
            product1.setIsMarketable(true);
            product1.setIsList(true);
            product1.setIsTop(false);
            product1.setScore(0F);
            product1.setTotalScore(0L);
            product1.setScoreCount(0L);
            product1.setHits(0L);
            product1.setWeekHits(0L);
            product1.setMonthHits(0L);
            product1.setSales(0L);
            product1.setWeekSales(0L);
            product1.setMonthSales(0L);
            product1.setWeekHitsDate(new Date());
            product1.setMonthHitsDate(new Date());
            product1.setWeekSalesDate(new Date());
            product1.setMonthSalesDate(new Date());
            product1.setGoods(goods);
            product1.setTenant(member.getTenant());

            Map<Parameter, String> parameterValue=new HashMap<>();
            for(Parameter parameter:product.getParameterValue().keySet()){
                parameterValue.put(parameter,product.getParameterValue().get(parameter));
            }
            product1.setParameterValue(parameterValue);

            List<ProductImage> productImages=new ArrayList<>();
            for(ProductImage productImage:product.getProductImages()){
                productImages.add(productImage);
            }
            product1.setProductImages(productImages);

            Map<MemberRank, BigDecimal> memberPrice=new HashMap<>();
            for(MemberRank memberRank:product.getMemberPrice().keySet()){
                memberPrice.put(memberRank,product.getMemberPrice().get(memberRank));
            }
            product1.setMemberPrice(memberPrice);

            List<PackagUnit> packagUnits=new ArrayList<>();
            for(PackagUnit packagUnit:product.getPackagUnits()){
                PackagUnit packagUnit1=new PackagUnit();
                packagUnit1.setName(packagUnit.getName());
                packagUnit1.setPrice(packagUnit.getPrice());
                packagUnit1.setCoefficient(packagUnit.getCoefficient());
                packagUnit1.setProduct(product1);
                packagUnit1.setWholePrice(packagUnit.getWholePrice());
                packagUnits.add(packagUnit1);
            }
            product1.setPackagUnits(packagUnits);

            Set<Specification> specifications = new HashSet<>();
            for(Specification specification:product.getSpecifications()){
                specifications.add(specification);
            }
            product1.setSpecifications(specifications);

            Set<SpecificationValue> specificationValues = new HashSet<>();
            for(SpecificationValue specificationValue:product.getSpecificationValues()){
                specificationValues.add(specificationValue);
            }
            product1.setSpecificationValues(specificationValues);

            goods.getProducts().add(product1);
            list.add(id);
            for(Long id1:ids){
                if(list.contains(id1)){
                    continue;
                }
                Product product2=productService.find(id1);
                if(product.getGoods().getProducts().contains(product2)){
                    Product product3=new Product();
                    BeanUtils.copyProperties(product2, product3, new String[]{"id", "sn", "stockMemo", "isLimit", "limitCounts", "barcode", "productCategoryTenant", "brandSeries", "productImages", "reviews", "consultations", "tags", "favoriteMembers", "coupons", "promotionProducts", "cartItems", "orderItems", "productItems", "giftItems", "productNotifies","memberPrice","parameterValue","packagUnits"});
                    String upMarketStock1=request.getParameter("upMarketStock_"+id1);
                    if(StringUtils.isBlank(upMarketStock)){
                        return DataBlock.error("上架数量不能为空");
                    }
                    product3.setStock(Integer.parseInt(upMarketStock1));
                    product3.setAllocatedStock(0);
                    product3.setIsMarketable(true);
                    product3.setIsList(false);
                    product3.setIsTop(false);
                    product3.setScore(0F);
                    product3.setTotalScore(0L);
                    product3.setScoreCount(0L);
                    product3.setHits(0L);
                    product3.setWeekHits(0L);
                    product3.setMonthHits(0L);
                    product3.setSales(0L);
                    product3.setWeekSales(0L);
                    product3.setMonthSales(0L);
                    product3.setWeekHitsDate(new Date());
                    product3.setMonthHitsDate(new Date());
                    product3.setWeekSalesDate(new Date());
                    product3.setMonthSalesDate(new Date());
                    product3.setGoods(goods);
                    product3.setTenant(member.getTenant());

                    Map<Parameter, String> parameterValue1=new HashMap<>();
                    for(Parameter parameter:product2.getParameterValue().keySet()){
                        parameterValue1.put(parameter,product2.getParameterValue().get(parameter));
                    }
                    product3.setParameterValue(parameterValue1);

                    List<ProductImage> productImages1=new ArrayList<>();
                    for(ProductImage productImage:product2.getProductImages()){
                        productImages1.add(productImage);
                    }
                    product3.setProductImages(productImages1);

                    Map<MemberRank, BigDecimal> memberPrice1=new HashMap<>();
                    for(MemberRank memberRank:product2.getMemberPrice().keySet()){
                        memberPrice1.put(memberRank,product.getMemberPrice().get(memberRank));
                    }
                    product3.setMemberPrice(memberPrice1);

                    List<PackagUnit> packagUnits1=new ArrayList<>();
                    for(PackagUnit packagUnit:product2.getPackagUnits()){
                        PackagUnit packagUnit1=new PackagUnit();
                        packagUnit1.setName(packagUnit.getName());
                        packagUnit1.setPrice(packagUnit.getPrice());
                        packagUnit1.setCoefficient(packagUnit.getCoefficient());
                        packagUnit1.setProduct(product1);
                        packagUnit1.setWholePrice(packagUnit.getWholePrice());
                        packagUnits1.add(packagUnit1);
                    }
                    product3.setPackagUnits(packagUnits1);

                    Set<Specification> specifications1 = new HashSet<>();
                    for(Specification specification:product2.getSpecifications()){
                        specifications1.add(specification);
                    }
                    product3.setSpecifications(specifications1);

                    Set<SpecificationValue> specificationValues1 = new HashSet<>();
                    for(SpecificationValue specificationValue:product2.getSpecificationValues()){
                        specificationValues1.add(specificationValue);
                    }
                    product3.setSpecificationValues(specificationValues1);

                    goods.getProducts().add(product3);
                    list.add(id1);
                }
            }
            goods.setSpecification1Title(product.getGoods().getSpecification1Title());
            goods.setSpecification2Title(product.getGoods().getSpecification2Title());
            goodsService.save(goods);
        }
        return DataBlock.success(null,"执行成功");
    }

    /**
     * 检查商品名称是否已存在
     * @param fullNames 商品全名
     */
    @RequestMapping(value = "/checkFullName", method = RequestMethod.GET)
    public @ResponseBody DataBlock checkFullName(String[] fullNames){
        List<String> list=new ArrayList<>();
        for(String fullName:fullNames){
            List<Filter> filters=new ArrayList<>();
            filters.add(new Filter("fullName",Operator.eq,fullName));
            List<Product> products=productService.findList(null,filters,null);
            if(products.size()>0){
                list.add(fullName);
            }
        }
        return DataBlock.success(list,"执行成功");
    }

}