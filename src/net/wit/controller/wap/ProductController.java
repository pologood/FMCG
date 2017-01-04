package net.wit.controller.wap;

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
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.aliyuncs.http.HttpRequest;
import net.sf.json.JSONArray;
import net.wit.*;
import net.wit.FileInfo.FileType;
import net.wit.Message;
import net.wit.Order;
import net.wit.controller.ajax.LBSController;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.ProductListModel;
import net.wit.controller.app.model.ReviewModel;
import net.wit.controller.app.model.TenantModel;
import net.wit.controller.wap.model.ProductModel;
import net.wit.display.DisplayEngine;
import net.wit.display.vo.ProductVo;
import net.wit.entity.*;
import net.wit.entity.Product.OrderType;
import net.wit.entity.Tag.Type;
import net.wit.service.*;
import net.wit.util.*;
import net.wit.weixin.main.MenuManager;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 商品
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("wapProductController")
@RequestMapping("/wap/product")
public class ProductController extends BaseController {

    @Resource(name = "adPositionServiceImpl")
    private AdPositionService adPositionService;

    @Resource(name = "productServiceImpl")
    private ProductService productService;

    @Resource(name = "productCategoryServiceImpl")
    private ProductCategoryService productCategoryService;

    @Resource(name = "goodsServiceImpl")
    private GoodsService goodsService;

    @Resource(name = "brandServiceImpl")
    private BrandService brandService;

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

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "productImageServiceImpl")
    private ProductImageService productImageService;

    @Resource(name = "specificationServiceImpl")
    private SpecificationService specificationService;

    @Resource(name = "specificationValueServiceImpl")
    private SpecificationValueService specificationValueService;

    @Resource(name = "fileServiceImpl")
    private FileService fileService;

    @Resource(name = "searchServiceImpl")
    private SearchService searchService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "cartServiceImpl")
    private CartService cartService;

    @Resource(name = "communityServiceImpl")
    private CommunityService communityService;

    @Resource(name = "consultationServiceImpl")
    private ConsultationService consultationService;

    @Resource(name = "productDisplay")
    private DisplayEngine<Product, ProductVo> productDisplay;

    @Resource(name = "productChannelServiceImpl")
    private ProductChannelService productChannelService;

    @Resource(name = "deliveryCenterServiceImpl")
    private DeliveryCenterService deliveryCenterService;

    @Resource(name = "employeeServiceImpl")
    private EmployeeService employeeService;

    @Resource(name = "reviewServiceImpl")
    private ReviewService reviewService;

    @Resource(name = "activityPlanningServiceImpl")
    private ActivityPlanningService activityPlanningService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list/{productCategoryId}", method = RequestMethod.GET)
    public String list(@PathVariable Long productCategoryId, Long memberId, Long communityId, Long brandId, Long areaId, Long promotionId, Long[] tagIds, Location location, BigDecimal distance, OrderType orderType, Pageable pageable, ModelMap model) {
        ProductCategory productCategory = productCategoryService.find(productCategoryId);
        if (productCategory == null) {
            throw new ResourceNotFoundException();
        }
        Brand brand = brandService.find(brandId);
        Area area = areaService.find(areaId);
        if (area == null) {
            area = areaService.getCurrent();
        }
        Promotion promotion = promotionService.find(promotionId);
        List<Tag> tags = tagService.findList(tagIds);
        Member member = memberService.getCurrent();
        model.addAttribute("member", member);
        List<Brand> brands = brandService.findAllByProductCategory(productCategory);
        model.addAttribute("orderTypes", OrderType.values());
        model.addAttribute("orderType", orderType);
        model.addAttribute("areas", areaService.find(areaService.getCurrent().getId()).getChildren());
        List<ProductCategory> productCategoryRootList = productCategoryService.findRoots();
        model.addAttribute("productCategoryRootList", productCategoryRootList);
        model.addAttribute("productCategory", productCategory);
        List<Community> communityList = communityService.findList(area);
        model.addAttribute("brand", brand);
        model.addAttribute("communityList", communityList);
        model.addAttribute("brands", brands);
        model.addAttribute("area", area);
        model.addAttribute("promotion", promotion);
        model.addAttribute("tagIds", tagIds);
        model.addAttribute("tags", tags);
        model.addAttribute("location", location);
        model.addAttribute("communityId", communityId);
        model.addAttribute("distance", distance);
        model.addAttribute("pageable", pageable);
        return "/wap/product/list";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/listNew/{productCategoryId}", method = RequestMethod.GET)
    public String listNew(@PathVariable Long productCategoryId, Long memberId, Long communityId, Long brandId, Long areaId, Long promotionId, Long[] tagIds, Location location, BigDecimal distance, OrderType orderType, Pageable pageable, ModelMap model) {
        ProductCategory productCategory = productCategoryService.find(productCategoryId);
        if (productCategory == null) {
            throw new ResourceNotFoundException();
        }
        Brand brand = brandService.find(brandId);
        Area area = areaService.find(areaId);
        if (area == null) {
            area = areaService.getCurrent();
        }
        Promotion promotion = promotionService.find(promotionId);
        List<Tag> tags = tagService.findList(tagIds);
        Member member = memberService.getCurrent();
        model.addAttribute("member", member);
        List<Brand> brands = brandService.findAllByProductCategory(productCategory);
        model.addAttribute("orderTypes", OrderType.values());
        model.addAttribute("orderType", orderType);
        model.addAttribute("areas", areaService.find(areaService.getCurrent().getId()).getChildren());
        List<ProductCategory> productCategoryRootList = productCategoryService.findRoots();
        model.addAttribute("productCategoryRootList", productCategoryRootList);
        model.addAttribute("productCategory", productCategory);
        List<Community> communityList = communityService.findList(area);
        model.addAttribute("brand", brand);
        model.addAttribute("communityList", communityList);
        model.addAttribute("brands", brands);
        model.addAttribute("area", area);
        model.addAttribute("promotion", promotion);
        model.addAttribute("tagIds", tagIds);
        model.addAttribute("tags", tags);
        model.addAttribute("location", location);
        model.addAttribute("communityId", communityId);
        model.addAttribute("distance", distance);
        model.addAttribute("productCategoryId", productCategoryId);
        model.addAttribute("pageable", pageable);
        return "/wap/product/listNew";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Long productCategoryId, Long communityId, Long memberId, Long brandId, Long areaId, Long productCategoryTagId, Long promotionId, Long[] tagIds, BigDecimal startPrice, BigDecimal endPrice, Location location, BigDecimal distatce,
                       OrderType orderType, Pageable pageable, BigDecimal distance, HttpServletRequest request, ModelMap model) {
        ProductCategory productCategory = productCategoryService.find(productCategoryId);
        Brand brand = brandService.find(brandId);
        Area area = areaService.find(areaId);
        if (area == null) {
            area = areaService.getCurrent();
        }
        Promotion promotion = promotionService.find(promotionId);
        List<Tag> tags = tagService.findList(tagIds);
        Member member = memberService.getCurrent();
        model.addAttribute("member", member);
        List<Brand> brands = brandService.findAllByProductCategory(productCategory);
        model.addAttribute("orderTypes", OrderType.values());
        model.addAttribute("orderType", orderType);
        List<ProductCategory> productCategoryRootList = productCategoryService.findRoots();
        model.addAttribute("productCategoryRootList", productCategoryRootList);
        model.addAttribute("productCategory", productCategory);
        model.addAttribute("brand", brand);
        model.addAttribute("brands", brands);
        model.addAttribute("area", area);
        List<Community> communityList = communityService.findList(area);
        model.addAttribute("communityList", communityList);
        model.addAttribute("areas", areaService.find(areaService.getCurrent().getId()).getChildren());
        model.addAttribute("promotion", promotion);
        model.addAttribute("tagIds", tagIds);
        model.addAttribute("tags", tags);
        model.addAttribute("location", location);
        model.addAttribute("communityId", communityId);
        model.addAttribute("distance", distance);
        model.addAttribute("pageable", pageable);
        return "/wap/product/list";
    }

    /**
     * 列表加载更多json
     */
    @RequestMapping(value = "/addMore", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock addMore(Long productCategoryId, OrderType orderType, Pageable pageable) {
        Set<ProductCategory> productCategories = new HashSet<>();
        productCategories.add(productCategoryService.find(productCategoryId));
        Page<Product> page = productService.openPage(pageable, areaService.getCurrent(), productCategories, null, null, null, null, null, null, null, null, null,null, orderType);
        return DataBlock.success(ProductListModel.bindData(page.getContent()), "执行成功");
    }

    /**
     * 搜索
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(String keyword, Long productCategoryId, Long communityId, Long brandId, Long brandSeriesId, Long promotionId, Long areaId, Location location, BigDecimal distance, OrderType orderType, Long[] tagIds, Pageable pageable, ModelMap model) {
        Area area = areaService.find(areaId);
        if (area == null) {
            area = areaService.getCurrent();
        }
        ProductCategory productCategory = productCategoryService.find(productCategoryId);
        Brand brand = brandService.find(brandId);
        BrandSeries brandSeries = brandSeriesService.find(brandSeriesId);
        List<Tag> tags = tagService.findList(tagIds);
        model.addAttribute("communityId", communityId);
        model.addAttribute("productCategory", productCategory);
        model.addAttribute("brand", brand);
        model.addAttribute("brandSeries", brandSeries);
        List<Community> communityList = communityService.findList(area);
        model.addAttribute("communityList", communityList);
        List<ProductCategory> productCategoryRootList = productCategoryService.findRoots();
        model.addAttribute("productCategoryRootList", productCategoryRootList);
        model.addAttribute("area", area);
        model.addAttribute("orderTypes", OrderType.values());
        model.addAttribute("keyword", keyword);
        model.addAttribute("tags", tags);
        model.addAttribute("areas", areaService.find(areaService.getCurrent().getId()).getChildren());
        model.addAttribute("orderType", orderType);
        model.addAttribute("distance", distance);
        model.addAttribute("location", location);
        model.addAttribute("pageable", pageable);
        return "wap/product/search";
    }

    /**
     * 根据关键字查询当前区域内的商品
     */
    @RequestMapping(value = "/searchProduct", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock searchProduct(String keyword, Pageable pageable) {
        //Page<Product> page = productService.findListByKeyword(areaService.getCurrent(), keyword, pageable);
        Page<Product> page = productService.openPage(pageable, areaService.getCurrent(), null, null, null, null, keyword, null, null, null, null, null,null, OrderType.dateDesc);
        return DataBlock.success(ProductListModel.bindData(page.getContent()), "执行成功");
    }

    /**
     * 商品页面
     */
    @RequestMapping(value = "/content/{id}", method = RequestMethod.GET)
    public String content(@PathVariable Long id, String backUrl, Model model, HttpServletRequest request) {
        Product product = productService.find(id);
        Cart cart = cartService.getCurrent();
        Long count = consultationService.count(null, product, true);
        Member member = memberService.getCurrent();
        if (member != null) {
            if (member.getFavoriteProducts().contains(product)) {
                model.addAttribute("hasFavorite", 0);
            } else {
                model.addAttribute("hasFavorite", 1);
            }
        }
        model.addAttribute("product", product);
        model.addAttribute("cart", cart);
        model.addAttribute("backUrl", backUrl);
        model.addAttribute("count", count);
        model.addAttribute("desc", product.getDescriptionapp() != null ? URLEncoder.encode(product.getDescriptionapp()) : "");
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String header = request.getHeader("User-Agent");
        String browseVersion = BrowseUtil.checkBrowse(header);
        if (BrowseUtil.WEIXIN.equalsIgnoreCase(browseVersion)) {
            model.addAttribute("sharedUrl", MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/wap/product/content/" + id + ".jhtml?extension=" + (member != null ? member.getUsername() : ""))));
        } else {
            model.addAttribute("sharedUrl", URLEncoder.encode(MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/product/content/" + id + ".jhtml?extension=" + (member != null ? member.getUsername() : ""))));
        }
        return "wap/product/content";
    }


    /**
     * 商品预览页面
     */
    @RequestMapping(value = "/content/{id}/preview1", method = RequestMethod.GET)
    public String preview(@PathVariable Long id, String backUrl, Model model, HttpServletRequest request) {
        Product product = productService.find(id);
        Cart cart = cartService.getCurrent();
        Long count = consultationService.count(null, product, true);
        Member member = memberService.getCurrent();
        if (member != null) {
            if (member.getFavoriteProducts().contains(product)) {
                model.addAttribute("hasFavorite", 0);
            } else {
                model.addAttribute("hasFavorite", 1);
            }
        }
        model.addAttribute("product", product);
        model.addAttribute("cart", cart);
        model.addAttribute("count", count);
        model.addAttribute("desc", product.getDescriptionapp() != null ? URLEncoder.encode(product.getDescriptionapp()) : "");
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String header = request.getHeader("User-Agent");
        String browseVersion = BrowseUtil.checkBrowse(header);
        if (BrowseUtil.WEIXIN.equalsIgnoreCase(browseVersion)) {
            model.addAttribute("sharedUrl", MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/wap/product/content/" + id + ".jhtml?extension=" + (member != null ? member.getUsername() : ""))));
        } else {
            model.addAttribute("sharedUrl", URLEncoder.encode(MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/product/content/" + id + ".jhtml?extension=" + (member != null ? member.getUsername() : ""))));
        }
        return "wap/product/preview";
    }

    /**
     * 商品评论列表
     */
    @RequestMapping(value = "/reviewList", method = RequestMethod.GET)
    public String reviewList(Long productId, HttpServletRequest request, Model model) {
        Product product = productService.find(productId);
        Set<Review> reviews = product.getReviews();
        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        return "wap/product/reviewList";
    }

    /**
     * 商品咨询列表
     */
    @RequestMapping(value = "/consultationsList", method = RequestMethod.GET)
    public String consultationsList(Long productId, Pageable pageable, HttpServletRequest request, Model model) {
        Product product = productService.find(productId);
        Page<Consultation> page = consultationService.findPage(net.wit.entity.Consultation.Type.product, null, product, true, pageable);
        model.addAttribute("product", product);
        model.addAttribute("page", page);
        return "wap/product/consultationsList";
    }

    /**
     * 商品简介
     */
    @RequestMapping(value = "/introduction", method = RequestMethod.GET)
    public String introduction(Long productId, HttpServletRequest request, Model model) {
        Product product = productService.find(productId);
        model.addAttribute("product", product);
        return "wap/product/introduction";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Product product, Long productCategoryId, Long brandId, Long[] tagIds, Long[] specificationIds, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return ERROR_VIEW;
        }
        if (member.getTenant() == null) {
            return ERROR_VIEW;
        }
        for (Iterator<ProductImage> iterator = product.getProductImages().iterator(); iterator.hasNext(); ) {
            ProductImage productImage = iterator.next();
            if (productImage == null || productImage.isEmpty()) {
                iterator.remove();
                continue;
            }
            if (productImage.getFile() != null && !productImage.getFile().isEmpty()) {
                if (!fileService.isValid(FileType.image, productImage.getFile())) {
                    addFlashMessage(redirectAttributes, Message.error("admin.upload.invalid"));
                    return "redirect:add.jhtml";
                }
            }
        }
        product.setProductCategory(productCategoryService.find(productCategoryId));
        product.setBrand(brandService.find(brandId));
        product.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
        product.setTenant(member.getTenant());
        if (!isValid(product)) {
            return ERROR_VIEW;
        }
        if (StringUtils.isNotEmpty(product.getSn()) && productService.snExists(product.getSn())) {
            return ERROR_VIEW;
        }
        if (product.getMarketPrice() == null) {
            BigDecimal defaultMarketPrice = calculateDefaultMarketPrice(product.getPrice());
            product.setMarketPrice(defaultMarketPrice);
        }
        if (product.getPoint() == null) {
            long point = calculateDefaultPoint(product.getPrice());
            product.setPoint(point);
        }
        product.setFullName(null);
        product.setAllocatedStock(0);
        product.setScore(0F);
        product.setTotalScore(0L);
        product.setScoreCount(0L);
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

        for (MemberRank memberRank : memberRankService.findAll()) {
            String price = request.getParameter("memberPrice_" + memberRank.getId());
            if (StringUtils.isNotEmpty(price) && new BigDecimal(price).compareTo(new BigDecimal(0)) >= 0) {
                product.getMemberPrice().put(memberRank, new BigDecimal(price));
            } else {
                product.getMemberPrice().remove(memberRank);
            }
        }

        for (ProductImage productImage : product.getProductImages()) {
            productImageService.build(productImage);
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
                                products.add(specificationProduct);
                            }
                        }
                        Product specificationProduct = products.get(j);
                        SpecificationValue specificationValue = specificationValueService.find(Long.valueOf(specificationValueIds[j]));
                        specificationProduct.getSpecifications().add(specification);
                        specificationProduct.getSpecificationValues().add(specificationValue);
                    }
                }
            }
        } else {
            product.setGoods(goods);
            product.setSpecifications(null);
            product.setSpecificationValues(null);
            products.add(product);
        }
        goods.getProducts().clear();
        goods.getProducts().addAll(products);
        goodsService.save(goods);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        model.addAttribute("productCategoryTree", productCategoryService.findTree());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("tags", tagService.findList(Type.product));
        model.addAttribute("memberRanks", memberRankService.findAll());
        model.addAttribute("specifications", specificationService.findAll());
        model.addAttribute("product", productService.find(id));
        return "wap/product/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Product product, Long productCategoryId, Long brandId, Long[] tagIds, Long[] specificationIds, Long[] specificationProductIds, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return ERROR_VIEW;
        }
        for (Iterator<ProductImage> iterator = product.getProductImages().iterator(); iterator.hasNext(); ) {
            ProductImage productImage = iterator.next();
            if (productImage == null || productImage.isEmpty()) {
                iterator.remove();
                continue;
            }
            if (productImage.getFile() != null && !productImage.getFile().isEmpty()) {
                if (!fileService.isValid(FileType.image, productImage.getFile())) {
                    addFlashMessage(redirectAttributes, Message.error("admin.upload.invalid"));
                    return "redirect:edit.jhtml?id=" + product.getId();
                }
            }
        }
        product.setProductCategory(productCategoryService.find(productCategoryId));
        product.setBrand(brandService.find(brandId));
        product.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
        if (!isValid(product)) {
            return ERROR_VIEW;
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
        if (product.getPoint() == null) {
            long point = calculateDefaultPoint(product.getPrice());
            product.setPoint(point);
        }

        for (MemberRank memberRank : memberRankService.findAll()) {
            String price = request.getParameter("memberPrice_" + memberRank.getId());
            if (StringUtils.isNotEmpty(price) && new BigDecimal(price).compareTo(new BigDecimal(0)) >= 0) {
                product.getMemberPrice().put(memberRank, new BigDecimal(price));
            } else {
                product.getMemberPrice().remove(memberRank);
            }
        }

        for (ProductImage productImage : product.getProductImages()) {
            productImageService.build(productImage);
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

        Goods goods = pProduct.getGoods();
        List<Product> products = new ArrayList<Product>();
        if (specificationIds != null && specificationIds.length > 0) {
            for (int i = 0; i < specificationIds.length; i++) {
                Specification specification = specificationService.find(specificationIds[i]);
                String[] specificationValueIds = request.getParameterValues("specification_" + specification.getId());
                if (specificationValueIds != null && specificationValueIds.length > 0) {
                    for (int j = 0; j < specificationValueIds.length; j++) {
                        if (i == 0) {
                            if (j == 0) {
                                BeanUtils.copyProperties(product, pProduct, new String[]{"id", "createDate", "modifyDate", "fullName", "allocatedStock", "score", "totalScore", "scoreCount", "hits", "weekHits", "monthHits", "sales", "weekSales",
                                        "monthSales", "weekHitsDate", "monthHitsDate", "weekSalesDate", "monthSalesDate", "goods", "reviews", "consultations", "favoriteMembers", "specifications", "memberPrice", "point", "specificationValues",
                                        "promotions", "cartItems", "orderItems", "giftItems", "productNotifies"});
                                pProduct.setSpecifications(new HashSet<Specification>());
                                pProduct.setSpecificationValues(new HashSet<SpecificationValue>());
                                products.add(pProduct);
                            } else {
                                if (specificationProductIds != null && j < specificationProductIds.length) {
                                    Product specificationProduct = productService.find(specificationProductIds[j]);
                                    if (specificationProduct == null || (specificationProduct.getGoods() != null && !specificationProduct.getGoods().equals(goods))) {
                                        return ERROR_VIEW;
                                    }
                                    specificationProduct.setSpecifications(new HashSet<Specification>());
                                    specificationProduct.setSpecificationValues(new HashSet<SpecificationValue>());
                                    products.add(specificationProduct);
                                } else {
                                    Product specificationProduct = new Product();
                                    BeanUtils.copyProperties(pProduct, specificationProduct);
                                    BeanUtils.copyProperties(product, specificationProduct, new String[]{"id", "createDate", "modifyDate", "fullName", "allocatedStock", "score", "totalScore", "scoreCount", "hits", "weekHits", "monthHits", "sales",
                                            "weekSales", "monthSales", "weekHitsDate", "monthHitsDate", "weekSalesDate", "monthSalesDate", "goods", "reviews", "consultations", "favoriteMembers", "specifications", "memberPrice", "point",
                                            "specificationValues", "promotions", "cartItems", "orderItems", "giftItems", "productNotifies"});
                                    specificationProduct.setId(null);
                                    specificationProduct.setCreateDate(null);
                                    specificationProduct.setModifyDate(null);
                                    specificationProduct.setSn(null);
                                    specificationProduct.setFullName(null);
                                    specificationProduct.setAllocatedStock(0);
                                    specificationProduct.setIsList(false);
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
                                    products.add(specificationProduct);
                                }
                            }
                        }
                        Product specificationProduct = products.get(j);
                        SpecificationValue specificationValue = specificationValueService.find(Long.valueOf(specificationValueIds[j]));
                        specificationProduct.getSpecifications().add(specification);
                        specificationProduct.getSpecificationValues().add(specificationValue);
                    }
                }
            }
        } else {
            product.setSpecifications(null);
            product.setSpecificationValues(null);
            BeanUtils.copyProperties(product, pProduct, new String[]{"id", "createDate", "modifyDate", "fullName", "allocatedStock", "score", "totalScore", "scoreCount", "hits", "weekHits", "monthHits", "sales", "weekSales", "monthSales",
                    "weekHitsDate", "monthHitsDate", "weekSalesDate", "monthSalesDate", "goods", "reviews", "consultations", "favoriteMembers", "promotions", "cartItems", "orderItems", "giftItems", "productNotifies", "memberPrice", "point"});
            products.add(pProduct);
        }
        goods.getProducts().clear();
        goods.getProducts().addAll(products);
        goodsService.update(goods);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Message delete(Long id) {
        productService.delete(id);
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
     * 浏览记录
     */
    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Product> history(Long[] ids) {
        return productService.findList(ids);
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list2", method = RequestMethod.GET)
    public String list(Long productCategoryTagId, Long brandId, Long areaId, Long promotionId, Long[] tagIds, BigDecimal startPrice, BigDecimal endPrice, OrderType orderType, Integer pageNumber, Integer pageSize, HttpServletRequest request, ModelMap model) {
        Brand brand = brandService.find(brandId);
        Area area = areaService.find(areaId);
        Promotion promotion = promotionService.find(promotionId);
        List<Tag> tags = tagService.findList(tagIds);
        Pageable pageable = new Pageable(pageNumber, pageSize);
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
        model.addAttribute("productCategoryTagId", tagService.find(productCategoryTagId));
        model.addAttribute("page", productService.searchPage(null, brand, promotion, tags, null, startPrice, endPrice, true, true, null, false, null, null, null, area, false, orderType, null, null, pageable));
        return "/tenant/product/content";
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
     * 每日好货
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/dayGoods", method = RequestMethod.GET)
    public String dayGoods(Long tagId, Long adPositionId, Model model) {
        AdPosition adPosition = adPositionService.find(adPositionId);
        List<Tag> tags = new ArrayList<Tag>();
        Tag tag = tagService.find(tagId);
        tags.add(tag);
        List<Order> orders = new ArrayList<Order>();
        orders.add(Order.desc("isTop"));
        orders.add(Order.desc("modifyDate"));
        model.addAttribute("now", new Date());
        model.addAttribute("adPosition", adPosition);
        model.addAttribute("dayGoods", productService.findListByTag(areaService.getCurrent(), tags, 10, orders));
        return "/wap/product/dayGoods";
    }

    /**
     * 新品推荐
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/newGoods", method = RequestMethod.GET)
    public String newGoods(Long tagId, Long adPositionId, Model model) {
        AdPosition adPosition = adPositionService.find(adPositionId);
        List<Tag> tags = new ArrayList<Tag>();
        Tag tag = tagService.find(tagId);
        tags.add(tag);
        List<Order> orders = new ArrayList<Order>();
        orders.add(Order.desc("isTop"));
        orders.add(Order.desc("modifyDate"));
        model.addAttribute("now", new Date());
        model.addAttribute("adPosition", adPosition);
        List<Product> newGoods = productService.findListByTag(areaService.getCurrent(), tags, 10, orders);
        model.addAttribute("newGoods", newGoods);
        return "/wap/product/newGoods";
    }

    /**
     * 最惠
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/rateGoods", method = RequestMethod.GET)
    public String rateGoods(Long tagId, Long adPositionId, Model model) {
        AdPosition adPosition = adPositionService.find(adPositionId);
        List<Tag> tags = new ArrayList<Tag>();
        Tag tag = tagService.find(tagId);
        tags.add(tag);
        List<Order> orders = new ArrayList<Order>();
        orders.add(Order.desc("isTop"));
        orders.add(Order.desc("modifyDate"));
        model.addAttribute("now", new Date());
        model.addAttribute("adPosition", adPosition);
        model.addAttribute("rateGoods", productService.findListByTag(areaService.getCurrent(), tags, 10, orders));
        return "/wap/product/rateGoods";
    }

    /**
     * 新品上市
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/newGoods1", method = RequestMethod.GET)
    public String newGoods1(Long productCategoryId, Long adPositionId, Model model) {
        AdPosition adPosition = adPositionService.find(adPositionId);
        model.addAttribute("adPosition", adPosition);
        model.addAttribute("areaId", areaService.getCurrent().getId());
        model.addAttribute("productCategoryId", productCategoryId);
        return "/wap/product/newGoods1";
    }

    /**
     * 获取当前时间的新品
     *
     * @return
     */
    @RequestMapping(value = "/findNewGoods", method = RequestMethod.GET)
    public
    @ResponseBody
    DataBlock findNewGoods(String type, Long productCategoryId, Pageable pageable) {
        List<Tag> tags = new ArrayList<Tag>();
        Tag tag = tagService.find(20l);
        tags.add(tag);
        List<Order> orders = new ArrayList<Order>();
        orders.add(Order.desc("isTop"));
        orders.add(Order.desc("modifyDate"));
        Date beginDate = null;
        Date endDate = null;
        if ("nowDate".equals(type)) {
            beginDate = DateUtil.setStartDay(new Date());
            endDate = DateUtil.setEndDay(new Date());
        } else if ("nowWeek".equals(type)) {
            beginDate = DateUtil.getMondayOfThisWeek();
            endDate = DateUtil.getSundayOfThisWeek();
        }
        //productService.findListByTag(areaService.getCurrent(), tags, beginDate, endDate, pageable);

        ProductChannel productChannel = null;
        if (productCategoryId != null) {
            productChannel = productChannelService.find(productCategoryId);
        }

        Set<ProductCategory> productCategories = null;
        if (productChannel != null) {
            productCategories = productChannel.getProductCategorys();
        }
        Page<Product> newGoods = productService.openPage(pageable, areaService.getCurrent(), productCategories, null, null, tags, null, null, null, beginDate, endDate, null,null, OrderType.dateDesc);
        return DataBlock.success(ProductModel.bindData(newGoods.getContent()), "success");
    }

    /**
     * 特价商品
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/rateGoods1", method = RequestMethod.GET)
    public String rateGoods1(Long adPositionId, Long productCategoryId, Model model) {
        AdPosition adPosition = adPositionService.find(adPositionId);
        model.addAttribute("adPosition", adPosition);
        model.addAttribute("areaId", areaService.getCurrent().getId());
        model.addAttribute("productCategoryId", productCategoryId);
        return "/wap/product/rateGoods1";
    }

    /**
     * 获取特价商品
     *
     * @return
     */
    @RequestMapping(value = "/findRateGoods", method = RequestMethod.GET)
    public
    @ResponseBody
    DataBlock findRateGoods(Promotion.Type type, Long productCategoryId, Pageable pageable) {
        List<Tag> tags = new ArrayList<Tag>();
        Tag tag = tagService.find(19l);
        tags.add(tag);
        List<Order> orders = new ArrayList<Order>();
        orders.add(Order.desc("isTop"));
        orders.add(Order.desc("modifyDate"));
        Date beginDate = null;
        Date endDate = null;
        if ("nowDate".equals(type)) {
            beginDate = DateUtil.setStartDay(new Date());
            endDate = DateUtil.setEndDay(new Date());
        } else if ("nowWeek".equals(type)) {
            beginDate = DateUtil.getMondayOfThisWeek();
            endDate = DateUtil.getSundayOfThisWeek();
        }
        //productService.findListByTag(areaService.getCurrent(), tags, beginDate, endDate, pageable);

        ProductChannel productChannel = null;
        if (productCategoryId != null) {
            productChannel = productChannelService.find(productCategoryId);
        }

        Set<ProductCategory> productCategories = null;
        if (productChannel != null) {
            productCategories = productChannel.getProductCategorys();
        }
        Page<Product> newGoods = productService.openPage(pageable, areaService.getCurrent(), productCategories, null, null, tags, null, null, null, null, null, null,null, OrderType.dateDesc);

        return DataBlock.success(ProductModel.bindData(newGoods.getContent()), "success");


//        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
//
//        ProductChannel productChannel = null;
//        if(productCategoryId!=null){
//            productChannel = productChannelService.find(productCategoryId);
//        }
//
//        if(productChannel!=null){
//            for(ProductCategory productCategory:productChannel.getProductCategorys()){
//                rateGoods(productCategory,type,pageable,mapList);
//            }
//        }else{
//            rateGoods(null,type,pageable,mapList);
//        }
//
//        return mapList;
    }

    private void rateGoods(ProductCategory productCategory, Promotion.Type type, Pageable pageable, List<Map<String, Object>> rateGoods) {
        Page<Promotion> page = promotionService.findPage(type, areaService.getCurrent(), null, null, null, null, null, null, productCategory, pageable);
        for (Promotion promotion : page.getContent()) {
            if (type == null) {
                if (!promotion.getType().equals(Promotion.Type.buyfree) && !promotion.getType().equals(Promotion.Type.seckill)) {
                    continue;
                }
            } else if (type.equals(Promotion.Type.buyfree)) {
                if (!promotion.getType().equals(Promotion.Type.buyfree)) {
                    continue;
                }
            } else if (type.equals(Promotion.Type.seckill)) {
                if (!promotion.getType().equals(Promotion.Type.seckill)) {
                    continue;
                }
            }

            Product product = promotion.getDefaultProduct();
            if (product == null) {
                continue;
            }
            if (product.getId() == null) {
                continue;
            }
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", promotion.getId());
            map.put("name", promotion.getName());
            String title = promotion.getTitle();
            if (title == null) {
                title = promotion.getName();
            }
            map.put("title", title);
            map.put("beginDate", promotion.getBeginDate());
            map.put("endDate", promotion.getEndDate());
            map.put("fullName", product.getName());
            map.put("thumbnail", product.getThumbnail());
            map.put("score", product.getScore());
            map.put("hits", product.getHits());
            map.put("productId", product.getId());

            if (promotion.getType().equals(Promotion.Type.buyfree)) {
                map.put("price", product.calcEffectivePrice(memberService.getCurrent()));
                map.put("marketPrice", product.getMarketPrice());
            } else if (promotion.getType().equals(Promotion.Type.seckill)) {
                map.put("price", new BigDecimal(promotion.getPriceExpression()));
                map.put("marketPrice", product.getMarketPrice());
            }
            if (promotion.getTenant() != null) {
                map.put("tenantName", promotion.getTenant().getShortName());
            }
            map.put("type", promotion.getType());
            rateGoods.add(map);
        }
    }

    @RequestMapping(value = "/city_youpin", method = RequestMethod.GET)
    public String cityYoupin(Model model) {
        List<Filter> filters = new ArrayList<>();
        List<Product> cityYoupin = productService.openList(10, areaService.getCurrent(), null, null, null, tagService.findList(new Long[]{18l}), null, null, null, null, null, null, filters,null, OrderType.weight);
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        for (Product product : cityYoupin) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", product.getId());
            map.put("image", product.getLarge());
            map.put("introduction", product.getDescriptionapp());
            map.put("price", product.calcEffectivePrice(memberService.getCurrent()));
            mapList.add(map);
        }

        JSONArray jsonArray = JSONArray.fromObject(mapList);
        model.addAttribute("cityYoupin", jsonArray);
        return "/wap/product/cityYoupin";
    }

    /**
     * 商品详情页面
     */
    @RequestMapping(value = "/content/{id}/product", method = RequestMethod.GET)
    public String productDetails(@PathVariable Long id, String extension, Model model, HttpServletRequest request) {
        return "redirect:/wap/product/display/"+id+".jhtml?extension="+extension;

//        details(id, backUrl, model);
//        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
//        String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/product/content/" + id + "/product.jhtml?extension=" + (memberService.getCurrent() != null ? memberService.getCurrent().getUsername() : ""));
//        model.addAttribute("link", url);
//        model.addAttribute("hits", productService.viewHits(id));
//        return "/wap/product/productDetails";
    }

    /**
     * 商品详情预览页面
     */
    @RequestMapping(value = "/content/{id}/preview", method = RequestMethod.GET)
    public String previewDetails(@PathVariable Long id, String backUrl, Model model, HttpServletRequest request) {
        details(id, backUrl, model);
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/product/content/" + id + "/preview.jhtml");
        model.addAttribute("link", url);
        return "/wap/product/previewDetails";
    }

    private void details(Long id, String backUrl, Model model) {
        Page<Promotion> page = promotionService.findPage(null, areaService.getCurrent(), null, null, null, null, null, null, null, null);

        Product product = productService.find(id);
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Promotion promotion : page.getContent()) {
            Product promotionDefaultProduct = promotion.getDefaultProduct();
            if (promotionDefaultProduct != null && product.getId().equals(promotionDefaultProduct.getId())) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name", promotion.getName());
                map.put("type", promotion.getType());
                mapList.add(map);
            }
        }

        ActivityPlanning activityPlanning = activityPlanningService.getCurrent(product.getTenant(), ActivityPlanning.Type.random);
        if (activityPlanning != null) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", activityPlanning.getName());
            map.put("type", Promotion.Type.activity);
            mapList.add(map);
        }

        model.addAttribute("promotions", mapList);
        Cart cart = cartService.getCurrent();
        //Long count = consultationService.count(null, product, true);
        Member member = memberService.getCurrent();
        if (member != null) {
            if (member.getFavoriteProducts().contains(product)) {
                model.addAttribute("hasFavorite", 0);
            } else {
                model.addAttribute("hasFavorite", 1);
            }
        }

        boolean hasSpecication = product.getSpecifications().size() > 0;
        model.addAttribute("hasSpecication", hasSpecication);
        model.addAttribute("product", product);
        model.addAttribute("cart", cart != null ? cart.getCartItems().size() : 0);
        model.addAttribute("backUrl", backUrl);
        model.addAttribute("price", product.calcEffectivePrice(member));
        TenantModel tenantModel = new TenantModel();
        tenantModel.copyFrom(product.getTenant());
        model.addAttribute("deliveryCenters", deliveryCenterService.findMyAll(product.getTenant().getMember()));
        model.addAttribute("tenant", tenantModel);
        model.addAttribute("type", "details");
        model.addAttribute("status", product.getTenant().getStatus());
        model.addAttribute("isMarketable", product.getIsMarketable());
        model.addAttribute("title", product.getName());
        model.addAttribute("desc", "我在" + product.getTenant().getName() + "发现一个不错的商品，赶快来看看吧！！！");
        model.addAttribute("imgUrl", product.getThumbnail());

    }


    /**
     * 根据商品ID获取商品的规格和尺寸
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Map<String, Object>> view(Long id) {
        Product product = productService.find(id);
        if (product == null) {
            return null;
        }
        ProductModel model = new ProductModel();
        model.copyFrom(product);
        model.bind(product.getGoods());

        List<Map<String, Object>> colorMapList = new ArrayList<Map<String, Object>>();

        Map<String, Object> colorMap = new HashMap<String, Object>();

        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", model.getId());
        map.put("thumbnail", model.getThumbnail());
        map.put("price", model.getPrice());
        map.put("color", model.getColor());
        map.put("spec", model.getSpec());
        map.put("stock", model.getStock());
        mapList.add(map);

        colorMap.put("color", model.getColor());
        colorMapList.add(colorMap);

        for (ProductModel product1 : model.getProducts()) {
            Map<String, Object> maps = new HashMap<String, Object>();
            Map<String, Object> colorMaps = new HashMap<String, Object>();
            maps.put("id", product1.getId());
            maps.put("thumbnail", product1.getThumbnail());
            maps.put("price", product1.getPrice());
            maps.put("color", product1.getColor());
            maps.put("spec", product1.getSpec());
            maps.put("stock", product1.getStock());
            mapList.add(maps);
            colorMaps.put("color", product1.getColor());
            colorMapList.add(colorMaps);

        }
        clearList(colorMapList);

        List<Map<String, Object>> mapLists = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> mm : colorMapList) {
            Map<String, Object> maps = new HashMap<String, Object>();
            maps.put("color", mm.get("color"));
            List<Map<String, Object>> specList = new ArrayList<Map<String, Object>>();
            for (Map<String, Object> m : mapList) {
                if (m.get("color").toString().equals(mm.get("color").toString())) {
                    Map<String, Object> smap = new HashMap<String, Object>();
                    smap.put("id", m.get("id"));
                    smap.put("thumbnail", m.get("thumbnail"));
                    smap.put("price", m.get("price"));
                    smap.put("stock", m.get("stock"));
                    smap.put("spec", m.get("spec"));
                    specList.add(smap);
                }
            }
            maps.put("spec", specList);
            mapLists.add(maps);
        }
        return mapList;
    }


    /**
     * 根据商品ID获取商品的规格和尺寸
     */
    @RequestMapping(value = "/view1", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Map<String, Object>> view1(Long id) {
        Product product = productService.find(id);
        if (product == null) {
            return null;
        }
        ProductModel model = new ProductModel();
        model.copyFrom(product);
        model.bind(product.getGoods());

        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", model.getId());
        map.put("thumbnail", model.getThumbnail());
        map.put("price", model.getPrice());
        map.put("color", model.getColor());
        map.put("spec", model.getSpec());
        map.put("stock", model.getStock());
        mapList.add(map);

        for (ProductModel product1 : model.getProducts()) {
            Map<String, Object> maps = new HashMap<String, Object>();
            Map<String, Object> colorMaps = new HashMap<String, Object>();
            maps.put("id", product1.getId());
            maps.put("thumbnail", product1.getThumbnail());
            maps.put("price", product1.getPrice());
            maps.put("color", product1.getColor());
            maps.put("spec", product1.getSpec());
            maps.put("stock", product1.getStock());
            mapList.add(maps);
            colorMaps.put("color", product1.getColor());

        }

        return mapList;
    }


    public static void clearList(List<Map<String, Object>> list) {
        if (list == null) {
            return;
        }
        Set<Object> set = new HashSet<Object>();
        for (Iterator<Map<String, Object>> it = list.iterator(); it.hasNext(); ) {
            //里面的map至少有一个元素，不然报错
            Object value = it.next().entrySet().iterator().next().getValue();
            if (set.contains(value)) {
                it.remove();
            } else {
                set.add(value);
            }


        }
    }

    /**
     * 商品参数页面
     */
    @RequestMapping(value = "/productParameters/{id}", method = RequestMethod.GET)
    public String productParameters(@PathVariable Long id, String backUrl, Model model, HttpServletRequest request) {
        parameters(id, model);
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/product/content/" + id + "/product.jhtml?extension=" + (memberService.getCurrent() != null ? memberService.getCurrent().getUsername() : ""));
        model.addAttribute("link", url);
        return "/wap/product/productParameters";
    }

    /**
     * 商品评价页面
     */
    @RequestMapping(value = "/productReviews/{id}", method = RequestMethod.GET)
    public String productReviews(@PathVariable Long id, String backUrl, Model model, HttpServletRequest request) {
        reviews(id, model);
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/product/content/" + id + "/product.jhtml?extension=" + (memberService.getCurrent() != null ? memberService.getCurrent().getUsername() : ""));
        model.addAttribute("link", url);
        model.addAttribute("id", id);
        return "/wap/product/productReviews";
    }

    /**
     * 预览
     * 商品参数页面
     */
    @RequestMapping(value = "/previewParameters/{id}", method = RequestMethod.GET)
    public String previewParameters(@PathVariable Long id, Model model) {
        parameters(id, model);

        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/product/content/" + id + "/preview.jhtml");
        model.addAttribute("link", url);
        return "/wap/product/previewParameters";
    }

    /**
     * 商品详情页v2
     */
    @RequestMapping(value = "/display/{id}", method = RequestMethod.GET)
    public String productDisplay(@PathVariable Long id, String extension, Model model, HttpServletRequest request) {

        if (extension != null) {
            Member extensions = memberService.findByUsername(extension);
            if (extensions != null) {
                request.getSession().setAttribute(Member.EXTENSION_ATTRIBUTE_NAME, extensions.getUsername());
            }
        }
        Product product = productService.find(id);
        product.setHits(product.getHits()+1);
        productService.update(product);
        Cart cart = cartService.getCurrent();
        ProductModel productModel = new ProductModel();
        productModel.copyFrom(product);
        model.addAttribute("product",productModel);
        model.addAttribute("tenant",product.getTenant());
        model.addAttribute("cartcount", cart != null ? cart.getQuantity() : 0);
        model.addAttribute("reviews", product.getReviews());
        model.addAttribute("status", product.getTenant().getStatus());
        model.addAttribute("isMarketable", product.getIsMarketable());
        List<Object> scores = new ArrayList<>();
        for (Review review : product.getReviews()) {
            if (review.getScore() >= 3) {
                scores.add(review.getScore());
            }
        }
        double score = 0, _scores = scores.size(), _reviews = product.getReviews().size();

        if (product.getReviews().size() > 0) {
            score = ((_scores / _reviews) * 100);
        }
        model.addAttribute("score", score);
        Member member = memberService.getCurrent();
        if (member != null) {
            if (member.getFavoriteProducts().contains(product)) {
                model.addAttribute("hasFavorite", 0);
            } else {
                model.addAttribute("hasFavorite", 1);
            }
        }

        model.addAttribute("deliveryCenters", deliveryCenterService.findMyAll(product.getTenant().getMember()));
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/product/display/" + id + ".jhtml?extension=" + (member != null ? member.getUsername() : ""));
        model.addAttribute("link", url);
        model.addAttribute("title", product.getName());
        model.addAttribute("desc", "我在" + product.getTenant().getName() + "发现一个不错的商品，赶快来看看吧！！！");
        model.addAttribute("imgUrl", product.getThumbnail());
        model.addAttribute("tscpath", "/wap/product/display");
        return "/wap/product/display";
    }

    /**
     * 预览
     * 商品评价页面
     */
    @RequestMapping(value = "/previewReviews/{id}", method = RequestMethod.GET)
    public String previewReviews(@PathVariable Long id, Model model) {
        reviews(id, model);
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/product/content/" + id + "/preview.jhtml");
        model.addAttribute("link", url);
        return "/wap/product/previewReviews";
    }

    private void parameters(Long id, Model model) {
        Product product = productService.find(id);
        Cart cart = cartService.getCurrent();
        Member member = memberService.getCurrent();
        if (member != null) {
            if (member.getFavoriteProducts().contains(product)) {
                model.addAttribute("hasFavorite", 0);
            } else {
                model.addAttribute("hasFavorite", 1);
            }
        }
        boolean hasSpecication = product.getSpecifications().size() > 0;
        model.addAttribute("hasSpecication", hasSpecication);
        model.addAttribute("product", product);
        model.addAttribute("cart", cart != null ? cart.getCartItems().size() : 0);
        model.addAttribute("desc", product.getDescriptionapp() != null ? URLEncoder.encode(product.getDescriptionapp()) : "");
        model.addAttribute("type", "parameters");

        TenantModel tenantModel = new TenantModel();
        tenantModel.copyFrom(product.getTenant());

        model.addAttribute("tenant", tenantModel);
        model.addAttribute("status", product.getTenant().getStatus());
        model.addAttribute("isMarketable", product.getIsMarketable());
        model.addAttribute("title", product.getName());
        model.addAttribute("desc", "我在" + product.getTenant().getName() + "发现一个不错的商品，赶快来看看吧！！！");
        model.addAttribute("imgUrl", product.getThumbnail());
    }

    private void reviews(Long id, Model model) {
        Product product = productService.find(id);
        Cart cart = cartService.getCurrent();
        model.addAttribute("cart", cart != null ? cart.getCartItems().size() : 0);
        Set<Review> reviews = product.getReviews();
        model.addAttribute("product", product);
        model.addAttribute("reviews", reviews);
        List<Object> scores = new ArrayList<>();
        for (Review review : reviews) {
            if (review.getScore() >= 3) {
                scores.add(review.getScore());
            }
        }
        double score = 0, _scores = scores.size(), _reviews = reviews.size();

        if (reviews.size() > 0) {
            score = ((_scores / _reviews) * 100);
        }
        model.addAttribute("score", score);
        model.addAttribute("type", "reviews");
        boolean hasSpecication = product.getSpecifications().size() > 0;
        model.addAttribute("hasSpecication", hasSpecication);

//        TenantModel tenantModel = new TenantModel();
//        tenantModel.copyFrom(product.getTenant());
//
//        model.addAttribute("tenant", tenantModel);
        model.addAttribute("status", product.getTenant().getStatus());
        model.addAttribute("isMarketable", product.getIsMarketable());
        model.addAttribute("title", product.getName());
        model.addAttribute("desc", "我在" + product.getTenant().getName() + "发现一个不错的商品，赶快来看看吧！！！");
        model.addAttribute("imgUrl", product.getThumbnail());
    }

    /**
     * 明星
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/starGoods", method = RequestMethod.GET)
    public String starGoods(Long areaId, Long tagId, Long adPositionId, Model model) {
        AdPosition adPosition = adPositionService.find(adPositionId);
        List<Tag> tags = new ArrayList<Tag>();
        Tag tag = tagService.find(tagId);
        tags.add(tag);
        Area area = areaService.find(areaId);
        if (area == null) {
            area = areaService.getCurrent();
        }
        List<Tenant> list = tenantService.findList(area, null, tagService.find(6l), 20);
        if (list == null || list.size() == 0) {
            model.addAttribute("messageNotify", "暂无数据");
            return "/wap/product/starGoods";
        }
        Random rand = new Random();
        Tenant tenant = list.get(rand.nextInt(list.size()));
        model.addAttribute("tenant", tenant);
        List<Order> orders = new ArrayList<Order>();
        orders.add(Order.desc("isTop"));
        orders.add(Order.desc("modifyDate"));
        model.addAttribute("now", new Date());
        model.addAttribute("adPosition", adPosition);
        model.addAttribute("starGoods", productService.findMyList(tenant, null, null, null, null, tags, null, null, null, true, true, null, false, null, null, 20, null));
        return "/wap/product/starGoods";
    }


    /**
     * 获取导购信息
     */
    @RequestMapping(value = "/contact/{id}", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock contact(@PathVariable Long id) {
        DeliveryCenter deliveryCenter = deliveryCenterService.find(id);

        if (deliveryCenter == null) {
            return DataBlock.error("无效的实体店铺");
        }

        List<Employee> employees = employeeService.findByDeliveryCenterId(id);

        List<Map<String, Object>> maps = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();
        map.put("pos", "店主");
        map.put("name", deliveryCenter.getContact());
        map.put("telnum", deliveryCenter.getPhone() == null ? deliveryCenter.getMobile() : deliveryCenter.getPhone());
        map.put("salenum", "");
        map.put("fansnum", deliveryCenter.getTenant().getMember().getFans());
        maps.add(map);
        for (Employee employee : employees) {
            Member member = employee.getMember();
            String pos = "";
            if (employee.getRole().indexOf("owner") != -1) {
                pos = "店主";
            } else if (employee.getRole().indexOf("cashier") != -1) {
                pos = "收银";
            } else if (employee.getRole().indexOf("guide") != -1) {
                pos = "导购";
            } else if (employee.getRole().indexOf("manager") != -1) {
                pos = "店长";
            } else if (employee.getRole().indexOf("account") != -1) {
                pos = "财务";
            }

            Map<String, Object> _map = new HashMap<>();
            _map.put("pos", pos);
            _map.put("name", member.getDisplayName() == null ? "" : member.getDisplayName());
            _map.put("telnum", member.getPhone() == null ? member.getMobile() : member.getPhone());
            _map.put("salenum", employee.getQuertity());
            _map.put("fansnum", member.getFans());
            maps.add(_map);
        }

        return DataBlock.success(maps, "success");
    }

    /**
     * 评价列表
     */
    @RequestMapping(value = "/get_list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock pagelist(Long id, Pageable pageable, ModelMap model) {
        Product product = productService.find(id);
        if (product == null) {
            DataBlock.error("商品id无效");
        }
        Page page = reviewService.findPage(null, product, null, true, pageable);
        return DataBlock.success(ReviewModel.bindData(page.getContent()), "执行成功");
    }
}