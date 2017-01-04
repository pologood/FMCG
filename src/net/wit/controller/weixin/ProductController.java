package net.wit.controller.weixin;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.controller.weixin.model.DataBlock;
import net.wit.controller.weixin.model.ProductListModel;
import net.wit.controller.weixin.model.ProductReviewModel;
import net.wit.controller.weixin.model.ProductViewModel;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.SettingUtils;
import net.wit.weixin.main.MenuManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * 商品
 * Created by WangChao on 2016-10-12.
 */
@Controller("weixinProductController")
@RequestMapping("/weixin/product")
public class ProductController {
    @Resource(name = "productServiceImpl")
    private ProductService productService;

    @Resource(name = "productCategoryServiceImpl")
    private ProductCategoryService productCategoryService;

    @Resource(name = "productCategoryTenantServiceImpl")
    private ProductCategoryTenantService productCategoryTenantService;

    @Resource(name = "brandServiceImpl")
    private BrandService brandService;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "communityServiceImpl")
    private CommunityService communityService;

    @Resource(name = "tagServiceImpl")
    private TagService tagService;

    @Resource(name = "reviewServiceImpl")
    private ReviewService reviewService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "cartServiceImpl")
    private CartService cartService;

    @Resource(name = "visitRecordServiceImpl")
    private VisitRecordService visitRecordService;

    /**
     * 商品详情页
     * @param extension 推广人
     * @param id 商品Id
     */
    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public String details(String extension, Long id, HttpServletRequest request, Model model){
        if (extension != null) {
            Member extensions = memberService.findByUsername(extension);
            if (extensions != null) {
                request.getSession().setAttribute(Member.EXTENSION_ATTRIBUTE_NAME, extensions.getUsername());
            }
        }
        model.addAttribute("id",id);
        return "weixin/product/details";
    }
    /**
     * 分类页分享
     */
    @RequestMapping(value = "/category/share", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock categoryShare(){
        Member member=memberService.getCurrent();
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String url = MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/weixin/product/category.jhtml&extension=" + (member != null ? member.getUsername() : "")));
        Map<String,Object> map=new HashMap<>();
        map.put("link", url);
        return DataBlock.success(map,"执行成功");
    }
    /**
     * 分类页
     * @param extension 推广人
     */
    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public String category(String extension, HttpServletRequest request){
        if (extension != null) {
            Member extensions = memberService.findByUsername(extension);
            if (extensions != null) {
                request.getSession().setAttribute(Member.EXTENSION_ATTRIBUTE_NAME, extensions.getUsername());
            }
        }
        return "weixin/product/category";
    }

    /**
     * 商品详情
     * @param id 商品Id
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock view(Long id) {
        Product product = productService.find(id);
        if (product == null) {
            return DataBlock.error("商品ID不存在");
        }
        ProductViewModel model = new ProductViewModel();
        model.copyFrom(product, memberService.getCurrent(),cartService.getCurrent());
        model.bindSpecifications(product);
        return DataBlock.success(model, "执行成功");
    }

    /**
     * 商品详情页分享
     * @param id 商品Id
     */
    @RequestMapping(value = "/share", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock share(Long id){
        Member member=memberService.getCurrent();
        Product product=productService.find(id);
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String url = MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/weixin/product/details.jhtml?id="+id+"&extension=" + (member != null ? member.getUsername() : "")));
        Map<String,Object> map=new HashMap<>();
        map.put("link", url);
        map.put("title", product.getName());
        map.put("desc", "我在" + product.getTenant().getName() + "发现一个不错的商品，赶快来看看吧！！！");
        map.put("imgUrl", product.getThumbnail());
        return DataBlock.success(map,"执行成功");
    }


    /**
     * 商品列表
     * productCategoryId 平台分类 id
     * communityId 商圈
     * keyword 搜索关键词
     * tagIds 商品签标
     * brandId 品牌
     * startPrice endPrice 介位段
     * isTop 是否置顶
     * isGift 是否是赠品
     * isOutOfStock 是否库存不足
     * orderType 排序 {综合排序 weight,置顶降序 topDesc, 价格升序 priceAsc,价格降序 priceDesc,销量降序 salesDesc,评分降序 scoreDesc, 日期降序 dateDesc,人气降序 hitsDesc}
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list(Long productCategoryId, Long communityId, String keyword, Long[] tagIds, Long brandId, Long areaId, BigDecimal startPrice, BigDecimal endPrice, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Product.OrderType orderType, Pageable pageable) {
        Page<Product> page;
        List<ProductListModel> models;
        try {
            ProductCategory productCategory = productCategoryService.find(productCategoryId);
            Brand brand = brandService.find(brandId);
            Area area = areaService.find(areaId);
            if(area==null){
                area = areaService.getCurrent();
            }
            List<Tag> tags = tagService.findList(tagIds);
            Set<ProductCategory> productCategories = new HashSet<>();
            if (productCategory != null) {
                productCategories.add(productCategory);
            }
            Community community = communityService.find(communityId);
            page = productService.openPage(pageable, area, productCategories, brand, null, tags, keyword, startPrice, endPrice, null, null, null,community, orderType);
            models = new ArrayList<>();
            for (Product product : page.getContent()) {
                ProductListModel model = new ProductListModel();
                model.copyFrom(product);
                Long positiveCount = reviewService.count(null, product, Review.Type.positive, null);
                if (product.getReviews().size() > 0) {
                    model.setPositivePercent(new BigDecimal((positiveCount * 1.0 / product.getReviews().size())*100).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                }
                models.add(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DataBlock.error("/weixin/product/list接口异常：");
        }
        return DataBlock.success(models, page, "执行成功");
    }

    /**
     * 获取指定商家的商品列表
     * id 商家编号
     * productCategoryTenantId 商家分类 id
     * keyword 搜索关键词
     * tagIds 商品签标
     * brandId 品牌
     * startPrice endPrice 介位段
     * orderType 排序 {综合排序 weight,置顶降序 topDesc, 价格升序 priceAsc,价格降序 priceDesc,销量降序 salesDesc,评分降序 scoreDesc, 日期降序 dateDesc,人气降序 hitsDesc}
     */
    @RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list(@PathVariable Long id, Long productCategoryTenantId, String keyword, Long[] tagIds, Long brandId, BigDecimal startPrice, BigDecimal endPrice, Product.OrderType orderType, Pageable pageable) {
        Page<Product> page = null;
        List<ProductListModel> models = null;
        try {
            Tenant tenant = tenantService.find(id);
            if (tenant==null) {
                DataBlock.error("店铺ID无效");
            }
            ProductCategoryTenant productCategoryTenant = productCategoryTenantService.find(productCategoryTenantId);
            Brand brand = brandService.find(brandId);
            List<Tag> tags = tagService.findList(tagIds);
            page = productService.findMyPage(tenant,keyword, null, productCategoryTenant, brand, null, tags, null, startPrice, endPrice, true, true, null, null, null,null, orderType, pageable);
            models = new ArrayList<>();
            for (Product product:page.getContent()) {
                ProductListModel model = new ProductListModel();
                model.copyFrom(product);
                Long positiveCount=reviewService.count(null,product,Review.Type.positive,null);
                if(product.getReviews().size()>0){
                    model.setPositivePercent(new BigDecimal((positiveCount*1.0/product.getReviews().size())*100).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
                }
                models.add(model);
            }
        } catch (Exception e) {
            System.out.println("weixin/product/list 接口异常");
            e.printStackTrace();
        }
        return DataBlock.success(models,page,"执行成功");
    }

    /**
     * 邻家好货，指联盟商品的商品
     * id 商家Id
     */
    @RequestMapping(value = "/unions", method = RequestMethod.GET)
    public @ResponseBody DataBlock unions(Long id,Pageable pageable) {
        Tenant tenant = tenantService.find(id);
        if (tenant==null) {
            DataBlock.error("企业ID无效");
        }
        Long [] tagIds = {5L};
        List<Tag> tags = tagService.findList(tagIds);
        Page<Product> page = productService.openPage(pageable, tenant, null, true, true, null, null, tags, null, null, null, null, null, null, Product.OrderType.weight);
        List<ProductListModel> models = new ArrayList<>();
        for (Product product:page.getContent()) {
            ProductListModel model = new ProductListModel();
            model.copyFrom(product);
            Long positiveCount=reviewService.count(null,product,Review.Type.positive,null);
            if(product.getReviews().size()>0){
                model.setPositivePercent(new BigDecimal((positiveCount*1.0/product.getReviews().size())*100).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
            }
            models.add(model);
        }
        return DataBlock.success(models,page,"执行成功");
    }

    /**
     * 获取推荐，搭配商品列表
     * id 商品Id
     */
    @RequestMapping(value = "/recommend", method = RequestMethod.GET)
    public @ResponseBody DataBlock recommend(Long id, Pageable pageable) {
        Product product = productService.find(id);
        if (product == null) {
            return DataBlock.error("商品ID不存在");
        }
        Long[] tagIds = {5L};
        List<Tag> tags = tagService.findList(tagIds);
        Page<Product> page = productService.openPage(pageable, product.getTenant(), null, true, true, null, null, tags, null, null, null, null, null, null, Product.OrderType.weight);
        List<ProductListModel> models = new ArrayList<>();
        for (Product p : page.getContent()) {
            ProductListModel model = new ProductListModel();
            model.copyFrom(p);
            Long positiveCount = reviewService.count(null, p, Review.Type.positive, null);
            if (p.getReviews().size() > 0) {
                model.setPositivePercent(new BigDecimal((positiveCount * 1.0 / p.getReviews().size()) * 100).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
            }
            models.add(model);
        }
        return DataBlock.success(models, page, "执行成功");
    }

    /**
     * 商品评价列表
     * id   商品Id
     */
    @RequestMapping(value = "/review/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock List(Long id, Pageable pageable) {
        Product product = productService.find(id);
        if (product == null) {
            DataBlock.error("商品id无效");
        }
        Page<Review> page = reviewService.findPage(null, product, null, null, pageable);
        return DataBlock.success(ProductReviewModel.bindData(page.getContent()), page, "执行成功");
    }

    /**
     * 热门搜索
     */
    @RequestMapping(value = "/hot_search", method = RequestMethod.GET)
    public @ResponseBody DataBlock hotSearch(){
        Setting setting= SettingUtils.get();
        return DataBlock.success(setting.getHotSearches(),"执行成功");
    }

}
