package net.wit.controller.store.member;

import net.wit.*;
import net.wit.Filter.Operator;
import net.wit.Message;
import net.wit.entity.*;
import net.wit.entity.TenantRelation.Status;
import net.wit.service.*;
import net.wit.support.EntitySupport;
import net.wit.util.SettingUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Controller("storeMemberRelationController")
@RequestMapping("/store/member/relation")
public class RelationController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;
	
	@Resource(name = "tenantRelationServiceImpl")
	private TenantRelationService tenantRelationService;

	@Resource(name = "tenantCategoryServiceImpl")
	private TenantCategoryService tenantCategoryService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "smsSendServiceImpl")
	private SmsSendService smsSendService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;
	
	@Resource(name="accountServiceImpl")
    private AccountService accountService;
	
	@Resource(name="supplierServiceImpl")
    private SupplierService supplierService;
	
	@Resource(name="productServiceImpl")
    private ProductService productService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Status status,Long memberRankId,Pageable pageable, ModelMap model) {
		if(memberRankId!=null){
			List<Filter> filters = pageable.getFilters();
			filters.add(new Filter("memberRank", Operator.eq, memberRankId));
		}
		Member member = memberService.getCurrent();
		model.addAttribute("status", status);
		model.addAttribute("statuss", Status.values());
		model.addAttribute("member", member);
		model.addAttribute("page", tenantRelationService.findPage(member.getTenant(), status,pageable));
		model.addAttribute("pageActive",2);
		model.addAttribute("menu","my_user");
		return "/store/member/relation/list";
	}

	/**
	 * 我的供应商
	 */
	@RequestMapping(value = "/parent", method = RequestMethod.GET)
	public String myParent(Status status,Long memberRankId,Pageable pageable, ModelMap model) {
		if(memberRankId!=null){
			List<Filter> filters = pageable.getFilters();
			filters.add(new Filter("memberRank", Operator.eq, memberRankId));
		}
		Member member = memberService.getCurrent();
		model.addAttribute("status", status);
		model.addAttribute("statuss", Status.values());
		model.addAttribute("member", member);
		model.addAttribute("page", tenantRelationService.findMyParent(member.getTenant(), status,pageable));
		model.addAttribute("menu","my_supplier");
		return "/store/member/relation/parent";
	}

	//添加
	@RequestMapping(value = "/addParent", method = RequestMethod.GET)
	public String addParent() {
		return "/store/member/relation/add_parent";
	}

	//添加
	@RequestMapping(value = "/addParent", method = RequestMethod.POST)
	public String addParent(String name, String address, Long tenantCategoryId, String licensePhoto, String linkman, String mobile, Long areaId,HttpServletRequest request,RedirectAttributes redirectAttributes) {
		Member current=memberService.getCurrent();
		Member member=memberService.findByTel(mobile);
		if(member==null){
			//注册会员
			Setting setting = SettingUtils.get();
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
			if (!setting.getIsRegisterEnabled()) {
				addFlashMessage(redirectAttributes, Message.error("系统关闭注册"));
				return "redirect:addParent.jhtml";
			}
			String password = mobile.substring(mobile.length()-6, mobile.length());
			member = EntitySupport.createInitMember();
			member.setArea(areaService.find(areaId));
			member.setUsername(mobile);
			member.setName(name);
			member.setPassword(DigestUtils.md5Hex(password));
			member.setPoint(setting.getRegisterPoint());
			member.setLockedDate(null);
			member.setRegisterIp(request.getRemoteAddr());
			member.setLoginIp(request.getRemoteAddr());
			member.setLoginDate(new Date());
			member.setSafeKey(null);
			member.setPaymentPassword(DigestUtils.md5Hex(password));
			member.setMemberRank(memberRankService.findDefault());
			member.setFavoriteProducts(null);
			member.setMember(null);
			member.setEmail("@");
			member.setMobile(mobile);
			member.setBindMobile(Member.BindStatus.binded);
			memberService.save(member);
			SmsSend smsSend=new SmsSend();
			smsSend.setMobiles(member.getUsername());
			smsSend.setContent("注册成功,账号:" + member.getUsername() +" 默认密码:"+password+ "【" + bundle.getString("signature") + "】");
			smsSend.setType(SmsSend.Type.captcha);
			smsSendService.smsSend(smsSend);
			//开通店铺
			Tenant tenant = EntitySupport.createInitTenant();
			tenant.setTenantType(Tenant.TenantType.tenant);
			tenant.setTenantCategory(tenantCategoryService.find(tenantCategoryId));
			tenant.setArea(areaService.find(areaId));
			tenant.setName(name);
			tenant.setShortName(name);
			tenant.setAddress(address);
			tenant.setLinkman(linkman);
			tenant.setTelephone(mobile);
			tenant.setLicensePhoto(licensePhoto);
			tenant.setTenantType(Tenant.TenantType.suppier);
			tenant.setStatus(Tenant.Status.confirm);
			tenantService.save(tenant, member,null);
			//设为我的供应商
			TenantRelation tenantRelation=new TenantRelation();
			tenantRelation.setTenant(current.getTenant());
			tenantRelation.setParent(member.getTenant());
			tenantRelation.setStatus(Status.success);
			tenantRelation.setMemberRank(member.getMemberRank());
			tenantRelationService.save(tenantRelation);
			addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
			return "redirect:parent.jhtml";
		}else{
			if(member.getTenant()==null){
				//开通店铺
				Tenant tenant = EntitySupport.createInitTenant();
				tenant.setTenantType(Tenant.TenantType.tenant);
				tenant.setTenantCategory(tenantCategoryService.find(tenantCategoryId));
				tenant.setArea(areaService.find(areaId));
				tenant.setName(name);
				tenant.setShortName(name);
				tenant.setAddress(address);
				tenant.setLinkman(linkman);
				tenant.setTelephone(mobile);
				tenant.setLicensePhoto(licensePhoto);
				tenant.setTenantType(Tenant.TenantType.suppier);
				tenant.setStatus(Tenant.Status.confirm);
				tenantService.save(tenant, member,null);
				//设为我的供应商
				TenantRelation tenantRelation=new TenantRelation();
				tenantRelation.setTenant(current.getTenant());
				tenantRelation.setParent(member.getTenant());
				tenantRelation.setStatus(Status.success);
				tenantRelation.setMemberRank(member.getMemberRank());
				tenantRelationService.save(tenantRelation);
				addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
				return "redirect:parent.jhtml";
			}else{
				if(tenantRelationService.findRelations(current.getTenant(),member.getTenant()).size()==0){
					//设为我的供应商
					TenantRelation tenantRelation=new TenantRelation();
					tenantRelation.setTenant(current.getTenant());
					tenantRelation.setParent(member.getTenant());
					tenantRelation.setStatus(Status.success);
					tenantRelation.setMemberRank(current.getMemberRank());
					tenantRelationService.save(tenantRelation);
					addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
					return "redirect:parent.jhtml";
				}else{
					addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
					return "redirect:parent.jhtml";
				}
			}
		}
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/editParent", method = RequestMethod.GET)
	public String editParent(Long id, ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("tenantRelation", tenantRelationService.find(id));
		model.addAttribute("products",productService.findAll());
		model.addAttribute("supplierId",id);
		model.addAttribute("member", member);
		model.addAttribute("menu","my_supplier");
		return "/store/member/relation/edit_parent";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/editParent", method = RequestMethod.POST)
	public String editParent(Long id, @RequestParam(defaultValue = "success") Status status,Long memberRankId,String linkman,String address, RedirectAttributes redirectAttributes) {
		TenantRelation tenantRelation = tenantRelationService.find(id);
		if (tenantRelation == null) {
			return ERROR_VIEW;
		}
		Tenant tenant=null;
		if(tenantRelation.getParent()!=null){
			tenant=tenantRelation.getParent();
			tenant.setLinkman(linkman);
			tenant.setAddress(address);
			tenantService.update(tenant);
		}
		tenantRelation.setStatus(status);
		tenantRelationService.update(tenantRelation);
		Member member=tenantRelation.getParent().getMember();
		member.setMemberRank(memberRankService.find(memberRankId));
		memberService.update(member);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:parent.jhtml";
	}


	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("tenantRelation", tenantRelationService.find(id));
		model.addAttribute("pageActive",2);
		model.addAttribute("member", member);
		return "/store/member/relation/edit";
	}
	
	/**
	 * 更新
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String update(Long id, @RequestParam(defaultValue = "none") Status status,Long memberRankId, RedirectAttributes redirectAttributes) {
		TenantRelation tenantRelation = tenantRelationService.find(id);
		if (tenantRelation == null) {
			return ERROR_VIEW;
		}
		tenantRelation.setMemberRank(memberRankService.find(memberRankId));
		tenantRelation.setStatus(status);
		tenantRelationService.update(tenantRelation);
		Member member=tenantRelation.getTenant().getMember();
		member.setMemberRank(memberRankService.find(memberRankId));
		memberService.update(member);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}
	
	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		for(Long id:ids){
			TenantRelation tenantRelation =tenantRelationService.find(id);
			Tenant parent=tenantRelation.getParent();
			Member member=tenantRelation.getTenant().getMember();
			Set<Tenant> tenants = member.getFavoriteTenants();
			if (tenants.contains(parent)) {
				tenants.remove(parent);
				member.setFavoriteTenants(tenants);
				memberService.update(member);
			} 
		}
		tenantRelationService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	
	/**
	 * 编辑
	 */
	@RequestMapping(value = "/settle_account", method = RequestMethod.GET)
	public String index(Long id, ModelMap model,Long sellerId,Pageable pageable) {
		Member member = memberService.getCurrent();
		List<Account> tenants=accountService.findTenants(member.getTenant());
        Page<Account> page=new Page<Account>();
        Tenant seller=new Tenant();;
        BigDecimal total_sale_amount=BigDecimal.ZERO;
        BigDecimal total_settlement_amount=BigDecimal.ZERO;
        if(tenants.size()>0){
        	if(sellerId==null){
        		seller=tenants.get(0).getTenant();
        	}else{
        		seller=tenantService.find(sellerId);
        	}
        	page=accountService.findByTenant(member.getTenant(),null,null,seller,pageable);
	        total_sale_amount=supplierService.totalSaleAmount(member.getTenant(),seller,null,null,null);
	        total_settlement_amount =supplierService.totalSettlementAmount(member.getTenant(),seller,null,null,null);
        }
        model.addAttribute("page",page);
        model.addAttribute("member",member);
        model.addAttribute("tenants",tenants);
        model.addAttribute("seller",seller);
        model.addAttribute("total_sale_amount",total_sale_amount);
        model.addAttribute("total_settlement_amount",total_settlement_amount);
		model.addAttribute("menu","my_user");
		return "/store/member/relation/settle_account";
	}
}
