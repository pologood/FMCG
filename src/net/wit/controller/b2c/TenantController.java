package net.wit.controller.b2c;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.wit.*;
import net.wit.Order;
import net.wit.entity.*;
import net.wit.entity.Coupon.Type;
import net.wit.entity.Product.OrderType;
import net.wit.helper.HttpClientHelper;
import net.wit.service.*;
import net.wit.util.Constants;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("b2cTenantController")
@RequestMapping("/b2c/tenant")
public class TenantController extends BaseController {

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "productCategoryTenantServiceImpl")
	private ProductCategoryTenantService productCategoryTenantService;

	@Resource(name = "tenantCategoryServiceImpl")
	private TenantCategoryService tenantCategoryService;

	@Resource(name = "productChannelServiceImpl")
	private ProductChannelService productChannelService;

	@Resource(name = "communityServiceImpl")
	private CommunityService communityService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;

	@Resource(name = "brandServiceImpl")
	private BrandService brandService;
	
	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Long id,ModelMap model){
		Tenant tenant=tenantService.find(id);
		Member member=memberService.getCurrent();
		Boolean collected=false;
		if(member!=null){
			if(member.getFavoriteTenants().contains(tenant)){
				collected=true;
			}
		}
		List<Map<String, Object>> coupons=new ArrayList<Map<String, Object>>();
		Long num=0L;
		Long tenantNum=0L;
		if(tenant!=null){
			for(Coupon coupon:tenant.getCoupons()){
				if(coupon.getType()==Type.tenantCoupon&&coupon.getEndDate().getTime()>new Date().getTime()){
					Map<String, Object> map=new HashMap<String, Object>();
					map.put("amount", coupon.getAmount());
					map.put("name", coupon.getIntroduction());
					map.put("startDate", coupon.getStartDate());
					map.put("endDate", coupon.getEndDate());
					map.put("minPrice", coupon.getMinimumPrice());
					map.put("status",coupon.getStatus());
					if(coupon.getReceiveTimes()==1){
						if(couponCodeService.findCouponCodeByCouponAndMember(coupon, member)==null){
							map.put("isGet", "no");
						}else{
							map.put("isGet", "yes");
						};
					}else if(coupon.getReceiveTimes()>1){
						if(couponCodeService.findCoupon(member, coupon).size()>=coupon.getReceiveTimes()){
							map.put("isGet", "yes");
						}else{
							map.put("isGet", "no");
						}
					}
					num += couponCodeService.findCoupon(member, coupon).size();
					tenantNum += coupon.getReceiveTimes();
					map.put("id", coupon.getId());
					coupons.add(map);
				}
				
			}
		}
		
		List<Filter> filters=new ArrayList<>();
		filters.add(new Filter("tenant", Filter.Operator.eq , tenant));
		List<Tag> tags = tagService.findList(new Long[]{5l});
		List<Product> recommendProducts = productService.findList(null, null, null, tags, null, null, null, true, true, null, false, null, null, null, 5, filters, new ArrayList<net.wit.Order>());
		
		model.addAttribute("member",member);
		model.addAttribute("collected",collected);
		model.addAttribute("tenant",tenant);
		model.addAttribute("area",areaService.getCurrent());
		model.addAttribute("recommendProducts",recommendProducts);
		model.addAttribute("coupons",coupons);
		model.addAttribute("num",num);
		if(num==tenantNum){
			model.addAttribute("isNewCoupon","false");
		}else{
			model.addAttribute("isNewCoupon","true");
		}
		return "b2c/tenant/index";
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String search(Long tenantId ,Long productCategoryTenantId,OrderType orderType,ModelMap model,Pageable pageable,String keyWord){
		Member member=memberService.getCurrent();
		Tenant tenant=tenantService.find(tenantId);
		Boolean collected=false;
		if(member!=null){
			if(member.getFavoriteTenants().contains(tenant)){
				collected=true;
			}
		}
		if(orderType==null){
			orderType= OrderType.weight;
		}
		ProductCategoryTenant productCategoryTenant=productCategoryTenantService.find(productCategoryTenantId);
		if(productCategoryTenant!=null){
			List<Filter> filters=new ArrayList<>();
			filters.add(new Filter("productCategoryTenant", Filter.Operator.eq,productCategoryTenant));
			pageable.setFilters(filters);
		}
		OrderType price_order= OrderType.priceAsc;
		if(orderType== OrderType.priceAsc){
			orderType=OrderType.priceDesc;
			price_order=orderType;
		}else if(orderType== OrderType.priceDesc){
			orderType=OrderType.priceAsc;
			price_order=orderType;
		}
		Page<Product> page = productService.openPage(pageable,tenant,null,true,true,null,null,null,keyWord,null,null,null,null,null,orderType);
		model.addAttribute("page",page);
		model.addAttribute("tenant",tenant);
		model.addAttribute("member",member);
		model.addAttribute("collected",collected);
		model.addAttribute("tenant",tenant);
		model.addAttribute("area",areaService.getCurrent());
		model.addAttribute("keyWord",keyWord);
		model.addAttribute("productCategoryTenant",productCategoryTenant);
		model.addAttribute("orderType",orderType);
		model.addAttribute("price_order",price_order);
		return "b2c/tenant/search";
	}


	/**
	 * 获取区域下的商圈
	 */
	@RequestMapping(value = "/getCommunity", method = RequestMethod.GET)
	@ResponseBody
	public List<Community> getCommunity(Long areaId) {
		try {
			Area area = null;
			List<Community> communityList = new ArrayList<Community>();
			area = areaService.find(areaId);
			if (area == null) {
				area = areaService.getCurrent();
			}
			communityList = communityService.findList(area);
			return communityList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取商家详细信息
	 */
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	public String detail(@PathVariable Long id, Long productCategoryTenantId, Model model, Long[] tagIds, OrderType orderType, BigDecimal startPrice, BigDecimal endPrice, Pageable pageable) {
		Tenant tenant = tenantService.find(id);
		if (tenant == null) {
			return ERROR_VIEW;
		}
		Area area = areaService.getCurrent();
		ProductCategoryTenant productCategoryTenant = productCategoryTenantService.find(productCategoryTenantId);
		List<Tag> tags = tagService.findList(tagIds);

		try {
			// 判断是否在平台拥有视频帐号（1.没帐号，2有帐号，没节点。3正常）
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			String url = Constants.Video.existUserAndNote;
			Map<String, String> params = new HashMap<String, String>();
			// params.put("username", member.getUsername());
			// ////为了演示正常，采用0592000198帐号下的华庄酒业有限公司做演示/////
			if ("happywine".equals(tenant.getMember().getUsername())) {
				params.put("username", "0592000198");
			} else {
				params.put("username", tenant.getMember().getUsername());
			}
			// params.put("username", "waf03");
			String responseText = HttpClientHelper.get(httpClient, url, params);
			JSONObject jsonObject = JSONObject.fromObject(responseText);
			// 1.成功 -1 用户名不存在 -2 节点不存在 -3摄像头不存在
			model.addAttribute("result", jsonObject.getString("result"));
			model.addAttribute("descr", jsonObject.getString("descr"));

		} catch (Exception e) {

			model.addAttribute("result", "0");
			model.addAttribute("descr", "连接平台服务器异常！");
		}
		model.addAttribute("page", productService.findMyPage(tenant,null, null, productCategoryTenant, null, null, tags, null, startPrice, startPrice, true, true, null, null, null, null, orderType, pageable));
		model.addAttribute("tenant", tenant);
		model.addAttribute("tenantCategory", tenant.getTenantCategory());
		model.addAttribute("productCategoryTenant", productCategoryTenant);
		if (productCategoryTenant == null) {
			model.addAttribute("rootProductCategoryTenants", productCategoryTenantService.findRoots(tenant));
		}
		model.addAttribute("articles", tenant.getArticles());
		model.addAttribute("area", area);
		model.addAttribute("tags", tags);
		model.addAttribute("orderType", orderType);
		model.addAttribute("orderTypes", OrderType.values());
		return "/b2c/tenant/detail";
	}

	/**
	 * 商铺分类列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Long[] tagIds, OrderType orderType, Long areaId, Long communityId, String hotCommunity, Model model, Pageable pageable) {
		Area area = areaService.find(areaId);
		if (area == null) {
			area = areaService.getCurrent();
		}
		Area currentArea = areaService.getCurrent();
		Community community = communityService.find(communityId);
		List<Tag> tags = tagService.findList(tagIds);
		List<Tag> hotTags = new ArrayList<Tag>();
		// 推荐商家
		Tag hotTag = tagService.find(6L);
		hotTags.add(hotTag);
		List<Community> hotCommunities = communityService.findHot(area, hotTags);
		List<Community> communityList = communityService.findList(area);
		List<TenantCategory> tenantCategoriesRoot = tenantCategoryService.findRoots();
		model.addAttribute("communityList", communityList);
		model.addAttribute("hotCommunity", hotCommunity);
		model.addAttribute("communityId", communityId);
		model.addAttribute("orderType", orderType);
		model.addAttribute("orderTypes", OrderType.values());
		model.addAttribute("currentArea", area);
		model.addAttribute("hotCommunities", hotCommunities);
		model.addAttribute("tenantCategories", tenantCategoriesRoot);
		model.addAttribute("area", currentArea);
		model.addAttribute("tags", tags);
		model.addAttribute("page", tenantService.findPage(null, tags, area, community, null, null, null, pageable));
		return "/b2c/tenant/list";
	}

	/**
	 * 商铺分类列表
	 */
	@RequestMapping(value = "/list/{tenantCategoryId}", method = RequestMethod.GET)
	public String list(@PathVariable Long tenantCategoryId, Long[] tagIds, OrderType orderType, Long areaId, Long communityId, String hotCommunity, Model model, Pageable pageable) {
		Area area = areaService.find(areaId);
		if (area == null) {
			area = areaService.getCurrent();
		}
		Area currentArea = areaService.getCurrent();
		Community community = communityService.find(communityId);
		TenantCategory tenantCategory = tenantCategoryService.find(tenantCategoryId);
		Set<TenantCategory> tenantCategories = new HashSet<TenantCategory>();
		tenantCategories.add(tenantCategory);
		List<Tag> tags = tagService.findList(tagIds);
		List<Tag> hotTags = new ArrayList<Tag>();
		// 推荐商家
		Tag hotTag = tagService.find(6L);
		hotTags.add(hotTag);
		List<Community> hotCommunities = communityService.findHot(area, hotTags);
		List<TenantCategory> tenantCategoriesRoot = tenantCategoryService.findRoots();
		if (tenantCategory.getParent() != null) {
			model.addAttribute("parentTenantCategory", tenantCategory.getParent());
		}
		List<Community> communityList = communityService.findList(area);
		model.addAttribute("communityList", communityList);
		model.addAttribute("hotCommunity", hotCommunity);
		model.addAttribute("communityId", communityId);
		model.addAttribute("tenantCategory", tenantCategory);
		model.addAttribute("orderType", orderType);
		model.addAttribute("orderTypes", OrderType.values());
		model.addAttribute("currentArea", area);
		model.addAttribute("hotCommunities", hotCommunities);
		model.addAttribute("tenantCategories", tenantCategoriesRoot);
		model.addAttribute("area", currentArea);
		model.addAttribute("tags", tags);
		model.addAttribute("page", tenantService.findPage(tenantCategories, tags, area, community, null, null, null, pageable));
		return "/b2c/tenant/list";
	}

	/**
	 * 商铺列表地图模式
	 */
	@RequestMapping(value = "/map_search", method = RequestMethod.GET)
	public String map_search(Long[] tagIds, Long areaId, Pageable pageable, Model model, HttpServletRequest request, HttpServletResponse response) {
		pageable.setPageSize(10);
		Area area = areaService.find(areaId);
		if (area == null) {
			area = areaService.getCurrent();
		}
		Area currentArea = areaService.getCurrent();
		List<Tag> tags = tagService.findList(tagIds);
		Page<Tenant> page = tenantService.findPage(null, tags, area, null, null, null, null, pageable);
		model.addAttribute("area", area);
		model.addAttribute("page", page);
		model.addAttribute("currentArea", currentArea);
		model.addAttribute("tags", tags);
		return "/b2c/tenant/map_search";
	}

}
