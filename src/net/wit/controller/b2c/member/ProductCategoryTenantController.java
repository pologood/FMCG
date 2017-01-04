package net.wit.controller.b2c.member;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.controller.b2c.BaseController;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.ProductCategoryTenant;
import net.wit.entity.Tag.Type;
import net.wit.entity.Tenant;
import net.wit.service.MemberService;
import net.wit.service.ProductCategoryTenantService;
import net.wit.service.TagService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 商品分类
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("b2c/memberProductCategoryController")
@RequestMapping("/b2c/member/product_category")
public class ProductCategoryTenantController extends BaseController {

	@Resource(name = "productCategoryTenantServiceImpl")
	private ProductCategoryTenantService productCategoryTenantService;
	@Resource(name = "tagServiceImpl")
	private TagService tagService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		model.addAttribute("member", member);
		model.addAttribute("productCategoryTree", productCategoryTenantService.findTree(tenant));
		model.addAttribute("tags", tagService.findList(Type.productCategory));
		return "/b2c/member/product_category/add";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(ProductCategoryTenant productCategoryTenant, Long parentId,Long[] tagIds, Long[] brandIds, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		productCategoryTenant.setParent(productCategoryTenantService.find(parentId));
		if (!isValid(productCategoryTenant)) {
			return ERROR_VIEW;
		}
		productCategoryTenant.setTreePath(null);
		productCategoryTenant.setGrade(null);
		productCategoryTenant.setChildren(null);
		productCategoryTenant.setProducts(null);
		productCategoryTenant.setTenant(tenant);
		productCategoryTenantService.save(productCategoryTenant);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		ProductCategoryTenant productCategoryTenant = productCategoryTenantService.find(id);
		model.addAttribute("member", member);
		model.addAttribute("productCategoryTree", productCategoryTenantService.findTree(tenant));
		model.addAttribute("productCategory", productCategoryTenant);
		model.addAttribute("children", productCategoryTenantService.findChildren(productCategoryTenant,tenant));
		model.addAttribute("tags", tagService.findList(Type.productCategory));
		return "/b2c/member/product_category/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(ProductCategoryTenant productCategoryTenant, Long parentId, Long[] tagIds, Long[] brandIds, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		productCategoryTenant.setParent(productCategoryTenantService.find(parentId));
		if (!isValid(productCategoryTenant)) {
			return ERROR_VIEW;
		}
		if (productCategoryTenant.getParent() != null) {
			ProductCategoryTenant parent = productCategoryTenant.getParent();
			if (parent.equals(productCategoryTenant)) {
				return ERROR_VIEW;
			}
			List<ProductCategoryTenant> children = productCategoryTenantService.findChildren(parent,tenant);
			if (children != null && children.contains(parent)) {
				return ERROR_VIEW;
			}
		}
		productCategoryTenantService.update(productCategoryTenant, "treePath", "grade", "children", "products", "parameterGroups", "attributes", "promotions","tenant");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap model) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		model.addAttribute("member", member);
		model.addAttribute("productCategoryTree", productCategoryTenantService.findTree(tenant));
		return "/b2c/member/product_category/list";
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long id) {
		ProductCategoryTenant productCategoryTenant = productCategoryTenantService.find(id);
		if (productCategoryTenant == null) {
			return ERROR_MESSAGE;
		}
		Set<ProductCategoryTenant> children = productCategoryTenant.getChildren();
		if (children != null && !children.isEmpty()) {
			return Message.error("admin.productCategory.deleteExistChildrenNotAllowed");
		}
		Set<Product> products = productCategoryTenant.getProducts();
		if (products != null && !products.isEmpty()) {
			return Message.error("admin.productCategory.deleteExistProductNotAllowed");
		}
		productCategoryTenantService.delete(id);
		return SUCCESS_MESSAGE;
	}
}