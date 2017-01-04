package net.wit.controller.pad;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.controller.pad.model.DetailProductModel;
import net.wit.controller.pad.model.ProductModel;
import net.wit.controller.pad.model.PromotionTenantModel;
import net.wit.entity.*;
import net.wit.entity.Product.OrderType;
import net.wit.service.*;
import net.wit.weixin.main.MenuManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 商铺商品
 * Created by ruanx on 2016/11/15.
 */
@Controller("padProductController")
@RequestMapping("/pad/tenant/product")
public class ProductController extends BaseController {
    @Resource(name = "equipmentServiceImpl")
    private EquipmentService equipmentService;

    @Resource(name = "productServiceImpl")
    private ProductService productService;

    @Resource(name = "tagServiceImpl")
    private TagService tagService;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "cartServiceImpl")
    private CartService cartService;

    @Resource(name = "deliveryCenterServiceImpl")
    private DeliveryCenterService deliveryCenterService;

    @Resource(name = "promotionServiceImpl")
    private PromotionService promotionService;

    //屏店铺商品列表,根据店铺id
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list(Long id, Pageable pageable,Long tagIds,OrderType orderType) {
        Tenant tenant = tenantService.find(id);
        Cart cart = cartService.getCurrent();
        List<Tag> tags = null;
        if (tenant == null) {
            return DataBlock.error("还没有开通店铺，快去申请吧。");
        }
        if(tagIds!=0){
            tags = tagService.findList(tagIds);
        }
        Map map = new HashMap();
        map.put("id",tenant.getId());
        map.put("name",tenant.getName());
        if(tagIds.equals(15L)){
            Page<Promotion> promotionPage =promotionService.findPage(tenant,pageable);
            map.put("products", PromotionTenantModel.bindData(promotionPage.getContent(),cart));
            return DataBlock.success(map,promotionPage, "执行成功");
        }else {
            Page<Product> page = productService.openPage(pageable, tenant, null, true, true, null, null, tags, null, null, null, null, null, null, orderType);
            map.put("products", ProductModel.bindData(page.getContent(), cart));
            return DataBlock.success(map, page, "执行成功");
        }
    }

//    //商品搜索
//    @RequestMapping(value = "/search", method = RequestMethod.GET)
//    @ResponseBody
//    public DataBlock search(String keyword, Pageable pageable) {
//        Cart cart = cartService.getCurrent();
//        Page<Product> page = productService.searchPage(null, null, null, null, null, null, null, true, true, null, false, null, null, null, null, null, null, null, keyword, pageable);
//        Map pmap = new HashMap();
//        pmap.put("products",ProductModel.bindData(page.getContent(),cart));
//        return DataBlock.success(pmap,page, "执行成功");
//    }

    //商品搜索(商盟内)
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock search(String keyword, Pageable pageable) {
        Page<Map<String,Object>> page = null;
        Cart cart = cartService.getCurrent();
        page = productService.searchPage(keyword, pageable);
        Map pmap = new HashMap();
        pmap.put("products",ProductModel.bindDatas(page.getContent(),cart));
        return DataBlock.success(pmap,page, "执行成功");
}

    /**
     * 根据id获取商品简介
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public @ResponseBody
    DataBlock view(Long id) {
        Product product = productService.find(id);
        //获取商品所属商家id
        Long tId = productService.findByProduct(id);
        Tenant tenant = tenantService.find(tId);
        if (product == null) {
            return DataBlock.error("商品ID不存在");
        }
        DetailProductModel model = new DetailProductModel();
        model.copyFrom(product,tenant);
        model.bind(product.getGoods());
        return DataBlock.success(model,"执行成功");
    }

    /**
     * 根据商品id获取商品库存
     */
    @RequestMapping(value = "/stock", method = RequestMethod.GET)
    public @ResponseBody
    DataBlock stock(Long id) {
        Map map =new HashMap();
        Product product = productService.find(id);
        Integer stock = product.getAvailableStock();
        if (product == null) {
            stock = 0;
        }
        if (!product.getIsMarketable()) {
            stock = 0;
        }
        if (product.getIsGift()) {
            stock = 0;
        }
        Cart cart = cartService.getCurrent();
        map.put("stock",stock);
        //购物车已添加刚商品数量
        if (cart != null) {
            for (CartItem cartItem:cart.getCartItems()) {
                if(cartItem.getProduct().getId().equals(id)){
                    map.put("stock",stock-cartItem.getQuantity());
                }
            }
        }
        return DataBlock.success(map,"执行成功");
    }

    /**
     * 根据id获取商品详情页
     */
    @RequestMapping(value = "/introduction", method = RequestMethod.GET)
    public String introduction(Long id,ModelMap model) {
        Product product = productService.find(id);
        model.addAttribute("introduction",product.getIntroduction());
        return "/pad/product/introduction";
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
        net.wit.controller.wap.model.ProductModel productModel = new net.wit.controller.wap.model.ProductModel();
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
}
