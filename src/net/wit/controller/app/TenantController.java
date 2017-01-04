/**
 *====================================================
 * 文件名称: TenantController.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014-9-11			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.controller.app;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.wap.model.CouponModel;
import net.wit.controller.wap.model.PromotionModel;
import net.wit.entity.*;
import net.wit.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import net.wit.controller.app.model.DataBlock;
import net.wit.weixin.main.MenuManager;

/**
 * @ClassName: TenantController
 * @Description: TODO(店铺管理)
 * @author Administrator
 * @date 2014-9-11 上午11:31:34
 */
@Controller("appTenantController")
@RequestMapping("/app/tenant")
public class TenantController extends BaseController {

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "qrcodeServiceImpl")
	private QrcodeService qrcodeService;

	@Resource(name = "couponServiceImpl")
	private CouponService couponService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "tenantRelationServiceImpl")
	private TenantRelationService tenantRelationService;

	@Resource(name = "deliveryCenterServiceImpl")
	private DeliveryCenterService deliveryCenterService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "promotionServiceImpl")
	private PromotionService promotionService;

	@Resource(name = "productCategoryTenantServiceImpl")
	private ProductCategoryTenantService productCategoryTenantService;

	@Resource(name="employeeServiceImpl")
	private EmployeeService employeeService;

	@Resource(name = "activityPlanningServiceImpl")
	private ActivityPlanningService activityPlanningService;

	/**
	 * 根据
	 */
	@RequestMapping(value = "/qrcode/json",method = RequestMethod.GET)
	@ResponseBody
	public DataBlock qrcodeJson(Long tenantId,HttpServletRequest request, HttpServletResponse response) {
		Tenant tenant = tenantService.find(tenantId);
		try {
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
			// 第三方用户唯一凭证
			Qrcode qrcode = qrcodeService.findbyTenant(tenant);
			String url = "";
			if (qrcode==null || qrcode.getUrl()==null) {
			  url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/share/index.jhtml?extension=" + (tenant != null ? tenant.getMember().getUsername() : ""));
			} else {
			  url = qrcode.getUrl();
			}
            return DataBlock.success(url,"获取成功");
		} catch (Exception e) {
			return DataBlock.error("获取二维码失败");
		}
	}
	/**
	 * 商家首页
	 *
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/index/{id}", method = RequestMethod.GET)
	public String indexNew(@PathVariable Long id, String type,Long memberId, ModelMap model, Pageable pageable, HttpServletRequest request) {
		Tenant tenant = tenantService.find(id);

		if (tenant == null) {
			return "/app/index";
		}

		ActivityPlanning activityPlanning = activityPlanningService.getCurrent(tenant, ActivityPlanning.Type.random);

		String isActivityTenant = "no";
		if(activityPlanning!=null){
			isActivityTenant="yes";
		}

		couponService.refreshStatus(tenant);
		Member member = null;
		String flag = "no";
		if (memberService.isAuthenticated()) {
			member = memberService.getCurrent();
		}

		if(!memberService.isAuthenticated()&&memberId!=null){
			member = memberService.find(memberId);
		}

		if(member!=null){
			model.addAttribute("memberId", member.getId());
			if (member.getFavoriteTenants().contains(tenant)) {
				flag = "yes";
			}
		}
		List<Filter> filters = new ArrayList<>();
		filters.add(new Filter("tenant", Filter.Operator.eq, tenant));
		pageable.setFilters(filters);
		Page<Employee> employeePage = employeeService.findPage(pageable,tagService.find(34L), null);
		model.addAttribute("employeePage", employeePage);
		model.addAttribute("tenant", tenant);
		model.addAttribute("flag", flag);
		model.addAttribute("thumbnail", tenant.getThumbnail());
		model.addAttribute("name", tenant.getName());
		model.addAttribute("favoriteNum", tenant.getFavoriteMembers().size());
		model.addAttribute("hits", tenantService.viewHits(id));

		Set<Coupon> coupons=new HashSet<>();
		for(Coupon coupon:tenant.getCoupons()){
			if(coupon.getExpired()&&coupon.getType().equals(Coupon.Type.tenantCoupon)){
				coupons.add(coupon);
			}
		}

		model.addAttribute("coupons", CouponModel.bindData(coupons));
		model.addAttribute("address", tenant.getAddress());
		model.addAttribute("telephone", tenant.getTelephone());
		model.addAttribute("union",tenantRelationService.relationExists(tenant, TenantRelation.Status.success));
		String weixin_code = "";

		if (tenant.getTenantWechat() != null) {
			weixin_code = tenant.getTenantWechat().getWeixin_code();
		}

		model.addAttribute("weixin", weixin_code);
		model.addAttribute("productCategoryTenants", tenant.getProductCategoryTenants());
		model.addAttribute("id", id);
		model.addAttribute("type", type);
		model.addAttribute("deliveryCenters", deliveryCenterService.findPage(tenant.getMember(),null,pageable));
		String desc = tenant.getIntroduction();
		if("".equals(desc)||desc==null){
			desc = tenant.getTenantCategory().getName();
		}
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/app/tenant/index/"+tenant.getId()+".jhtml");
		model.addAttribute("title", tenant.getName());
		model.addAttribute("desc", desc);
		model.addAttribute("link", url);
		model.addAttribute("imgUrl", tenant.getThumbnail());
		model.addAttribute("isActivityTenant",isActivityTenant);
		model.addAttribute("member",memberService.getCurrent());
		model.addAttribute("tscpath", "/app/tenant/index3.0");

		return "/app/tenant/index3.0";
	}


	/**
	 * C类商家首页
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/indexC", method = RequestMethod.GET)
	public String indexnewC(ModelMap model) {
		//model.addAttribute("productChannels", channels);
		//model.addAttribute("area", areaService.getCurrent());

		//model.addAttribute("deliverys", deliveryCenterService.findList(null, tagService.findList(new Long[]{6l}), areaService.getCurrent(), true, null, null, 10, null, null));

		model.addAttribute("tscpath", "/wap/tenant/indexC");
		return "/wap/tenant/indexC";
	}

	/**
	 * 商家首页
	 *
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/index_static/{id}", method = RequestMethod.GET)
	public String indexStatic(@PathVariable Long id,String type, ModelMap model,Pageable pageable, HttpServletRequest request) {
		Tenant tenant = tenantService.find(id);

		if (tenant == null) {
			return "/wap/index";
		}

		String flag = "no";
		if (memberService.isAuthenticated()) {
			Member member = memberService.getCurrent();
			model.addAttribute("memberId", member.getId());
			if (member.getFavoriteTenants().contains(tenant)) {
				flag = "yes";
			}
		}
		model.addAttribute("flag", flag);
		model.addAttribute("thumbnail", tenant.getThumbnail());
		model.addAttribute("name", tenant.getName());
		model.addAttribute("favoriteNum", tenant.getFavoriteMembers().size());
		model.addAttribute("coupons", CouponModel.bindData(tenant.getCoupons()));

		model.addAttribute("address", tenant.getAddress());
		model.addAttribute("telephone", tenant.getTelephone());

		String weixin_code = "";

		if (tenant.getTenantWechat() != null) {
			weixin_code = tenant.getTenantWechat().getWeixin_code();
		}

		model.addAttribute("weixin", weixin_code);
		model.addAttribute("productCategoryTenants", tenant.getProductCategoryTenants());
		model.addAttribute("id", id);
		model.addAttribute("type", type);
		model.addAttribute("deliveryCenters", deliveryCenterService.findPage(tenant.getMember(),null,pageable));
		String desc = tenant.getIntroduction();
		if("".equals(desc)||desc==null){
			desc = tenant.getTenantCategory().getName();
		}
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/tenant/index/"+tenant.getId()+".jhtml");
		model.addAttribute("title", tenant.getName());
		model.addAttribute("desc", desc);
		model.addAttribute("link", url);
		model.addAttribute("imgUrl", tenant.getThumbnail());

		return "/wap/tenant/index_static";
	}

	/**
	 * 获得店铺中经营的商品
	 *
	 * @param id        店铺编号
	 * @param type      查询类型   index-店家推荐  推荐标签编码【5】； all-全部  ；  new-新品 推荐标签编码【2】 ； promotion-促销 推荐标签编码【15】
	 * @param orderType 排序
	 * @return
	 */
	@RequestMapping(value = "/get_tenant_product/{id}", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getTenantProduct(@PathVariable Long id, String type, Pageable pageable, Product.OrderType orderType) {
		Tenant tenant = tenantService.find(id);
		if (tenant == null) {
			return null;
		}
		Member member = memberService.getCurrent();
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();

		List<Tag> tags = new ArrayList<>();
		if ("index".equals(type)) {
			Tag tag = tagService.find(5l);
			tags.add(tag);
		} else if ("new".equals(type)) {
			Tag tag = tagService.find(2l);
			tags.add(tag);
		} else if ("promotion".equals(type)) {
			Tag tag = tagService.find(15l);
			tags.add(tag);
		}

		Page<Product> productPage = productService.openPage(pageable, tenant, null, true, true, null, null, tags, null, null, null, null, null, null, orderType);

		for (Product product : productPage.getContent()) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", product.getId());
			map.put("thumbnail", product.getThumbnail());
			map.put("fullName", product.getName());
			map.put("price", product.calcEffectivePrice(member));
			map.put("wholePrice", product.getWholePrice());

			List<Map<String, Object>> tagList = new ArrayList<Map<String, Object>>();
			for (Tag tag1 : product.getTags()) {
				Map<String, Object> tagMap = new HashMap<>();
				tagMap.put("id", tag1.getId());
				tagMap.put("name", tag1.getName());
				tagList.add(tagMap);
			}
			map.put("tags", tagList);
			mapList.add(map);
		}
		return mapList;
	}

	/**
	 * 根据店铺分类查找商品
	 *
	 * @param id
	 * @param pageable
	 * @param productCategoryId
	 * @param tagIds
	 * @param orderType
	 * @return
	 */
	@RequestMapping(value = "/get_category_product/{id}", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> getCategoryProducts(@PathVariable Long id, Pageable pageable, Long productCategoryId, Long tagIds, Product.OrderType orderType) {
		Tenant tenant = tenantService.find(id);
		if (tenant == null) {
			return null;
		}
		List<Tag> tags = tagService.findList(tagIds);
		ProductCategoryTenant productCategoryTenant = productCategoryTenantService.find(productCategoryId);
		Page<Product> page = productService.findMyPage(tenant, null, null, productCategoryTenant, null, null, tags, null, null, null, true, true, null, null, null, null, orderType, pageable);

		List<Map<String, Object>> mapList = new ArrayList<>();
		for (Product product : page.getContent()) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", product.getId());
			map.put("thumbnail", product.getThumbnail());
			map.put("fullName", product.getName());
			map.put("price", product.calcEffectivePrice(memberService.getCurrent()));
			map.put("wholePrice", product.getWholePrice());

			List<Map<String, Object>> tagList = new ArrayList<Map<String, Object>>();
			for (Tag tag1 : product.getTags()) {
				Map<String, Object> tagMap = new HashMap<>();
				tagMap.put("id", tag1.getId());
				tagMap.put("name", tag1.getName());
				tagList.add(tagMap);
			}
			map.put("tags", tagList);
			mapList.add(map);
		}
		return mapList;
	}

	@RequestMapping(value = "/get_promotion_product/{id}", method = RequestMethod.GET)
	@ResponseBody
	public List<PromotionModel> getPromotionProduct(@PathVariable Long id, Pageable pageable) {
		Tenant tenant = tenantService.find(id);
		if (tenant == null) {
			return null;
		}
		Page<Promotion> promotionPage =promotionService.findPage(tenant,pageable);
		return PromotionModel.bindData(promotionPage.getContent());
	}

}
