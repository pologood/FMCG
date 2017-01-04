package net.wit.controller.b2c;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.wit.entity.Promotion.Type;

import net.wit.Filter;
import net.wit.Order;
import net.wit.controller.app.model.PromotionSecKillModel;
import net.wit.entity.Member;
import net.wit.entity.Product.OrderType;
import net.wit.entity.ProductCategory;
import net.wit.entity.Promotion;

@Controller("b2cIndexController")
@RequestMapping("/b2c/index")
public class IndexController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;
	
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	
	@Resource(name = "brandServiceImpl")
	private BrandService brandService;
	
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	
	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;

	@Resource(name = "articleCategoryServiceImpl")
	private ArticleCategoryService articleCategoryService;
	
	@Resource(name = "promotionServiceImpl")
    private PromotionService promotionService;

	@Resource(name = "productChannelServiceImpl")
	private ProductChannelService productChannelService;

	/**
	 * 主页面
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String index(ModelMap model) {
		Member member=memberService.getCurrent();
//		if(member!=null){
			model.addAttribute("member",member);
//		}
		String[] position = {"100","101","102","103","104","105","106","107","108","109","114","115"};
		List<ProductCategory> rootCategorys=productCategoryService.findRoots();
		
		
        List<Order> orders = new ArrayList<Order>();
        orders.add(Order.desc("modifyDate"));
        List<Filter> filters = new ArrayList<>();
		List<Promotion> promotion_started=promotionService.findList(Type.seckill, true, false, null, null, filters, orders);
		List<Promotion> promotion_unstart=promotionService.findList(Type.seckill, false, false, null, null, filters, orders);
		
		List<PromotionSecKillModel> promotions_started = null;
		List<PromotionSecKillModel> promotions_unstart = null;
		if (promotion_started.size() > 0) {
			promotions_started = PromotionSecKillModel.bindData(promotion_started);
		}
		if (promotion_unstart.size() > 0) {
			promotions_unstart = PromotionSecKillModel.bindData(promotion_unstart);
		}
		model.addAttribute("ad_article",articleService.findList(articleCategoryService.find(16l),null , 6, null, null));
		model.addAttribute("sale_article",articleService.findList(articleCategoryService.find(20l),null , 6, null, null));
		model.addAttribute("area",areaService.getCurrent());
		model.addAttribute("brand",brandService.findList(tagService.find(24L)));
		model.addAttribute("rootCategory",rootCategorys);
		model.addAttribute("promotions_started",promotions_started);
		model.addAttribute("promotions_unstart",promotions_unstart);
		model.addAttribute("promotions_ended",promotionService.findList(Type.seckill, true, true, null, null, null, null));
        model.addAttribute("dayProducts", productService.openList(10,areaService.getCurrent(),null,null,null,tagService.findList(new Long[]{18l}),null,null,null,null,null,null,filters,null,OrderType.weight));
        model.addAttribute("flashProducts", productService.openList(10,areaService.getCurrent(),null,null,null,tagService.findList(new Long[]{33l}),null,null,null,null,null,null,filters,null,OrderType.weight));
        model.addAttribute("newProducts", productService.openList(5,areaService.getCurrent(),null,null,null,tagService.findList(new Long[]{20l}),null,null,null,null,null,null,filters,null,OrderType.weight));
        model.addAttribute("specialProducts", productService.openList(5,areaService.getCurrent(),null,null,null,tagService.findList(new Long[]{19l}),null,null,null,null,null,null,filters,null,OrderType.weight));
		model.addAttribute("productChannels",productChannelService.findAll());
		model.addAttribute("type","index");
		model.addAttribute("position",position);
        return "/b2c/index";
	}
}
