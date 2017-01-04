package net.wit.controller.b2b.member;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wit.CommonAttributes;
import net.wit.FileInfo.FileType;
import net.wit.Message;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.controller.b2c.BaseController;
import net.wit.entity.Ad;
import net.wit.entity.Area;
import net.wit.entity.Article;
import net.wit.entity.SmsSend;
import net.wit.entity.BaseEntity.Save;
import net.wit.entity.Community;
import net.wit.entity.Member;
import net.wit.entity.Member.Gender;
import net.wit.entity.MemberAttribute;
import net.wit.entity.MemberAttribute.Type;
import net.wit.entity.ProductImage;
import net.wit.entity.Tag;
import net.wit.entity.Tenant;
import net.wit.service.AdPositionService;
import net.wit.service.AdService;
import net.wit.service.AreaService;
import net.wit.service.ArticleCategoryService;
import net.wit.service.ArticleService;
import net.wit.service.CartService;
import net.wit.service.CommunityService;
import net.wit.service.FileService;
import net.wit.service.MemberAttributeService;
import net.wit.service.MemberRankService;
import net.wit.service.MemberService;
import net.wit.service.ProductImageService;
import net.wit.service.RSAService;
import net.wit.service.SmsSendService;
import net.wit.service.TagService;
import net.wit.service.TenantCategoryService;
import net.wit.service.TenantService;
import net.wit.support.EntitySupport;
import net.wit.util.SettingUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("b2bMemberTenantController")
@RequestMapping("/b2b/member/tenant")
public class TenantController extends BaseController{
	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;

	@Resource(name="tenantServiceImpl")
	private TenantService tenantService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;
	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;
	@Resource(name = "memberAttributeServiceImpl")
	private MemberAttributeService memberAttributeService;
	@Resource(name = "cartServiceImpl")
	private CartService cartService;
	@Resource(name = "smsSendServiceImpl")
	private SmsSendService smsSendService;
	@Resource(name = "tenantCategoryServiceImpl")
	private TenantCategoryService tenantCategoryService;
	@Resource(name = "fileServiceImpl")
	private FileService fileService;
	@Resource(name = "productImageServiceImpl")
	private ProductImageService productImageService;
	@Resource(name = "communityServiceImpl")
	private CommunityService communityService;
	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;
	@Resource(name = "articleCategoryServiceImpl")
	private ArticleCategoryService articleCategoryService;
	@Resource(name = "tagServiceImpl")
	private TagService tagService;
	@Resource(name = "adServiceImpl")
	private AdService adService;
	@Resource(name = "adPositionServiceImpl")
	private AdPositionService adPositionService;
	
	/**
	 * 获取社区信息
	 */
	@RequestMapping(value = "/get_community", method = RequestMethod.GET)
	public @ResponseBody
	Map<Long, String> getCommunity(Long areaId) {
		Map<Long, String> data = new HashMap<Long, String>();
		Area area = areaService.find(areaId);
		List<Community> communitys = communityService.findList(area);
		for (Community community : communitys) {
			data.put(community.getId(),community.getName());
		}
		return data;
	}
	/**
	 * 添加
	 */ 
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
	    if (tenant==null) {
		   tenant = EntitySupport.createInitTenant();
		   if (member.getArea()!=null) {
			   tenant.setArea(member.getArea());
		   }
		   tenant.setTenantType(Tenant.TenantType.tenant);
		   tenant.setAddress(member.getAddress());
		   tenant.setLinkman(member.getName());
		   tenant.setTelephone(member.getMobile());
		   tenant.setName(member.getUsername()+"的店铺");
	    } 
		model.addAttribute("tenantCategoryTree", tenantCategoryService.findTree());
		model.addAttribute("tenant", tenant);
		model.addAttribute("member", member);
		return "b2b/member/tenant/add";
	}
	/**
	 * 修改
	 */ 
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(String type,ModelMap model) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
	    if (tenant==null) {
		   tenant = EntitySupport.createInitTenant();
		   if (member.getArea()!=null) {
			   tenant.setArea(member.getArea());
		   }
		   tenant.setTenantType(Tenant.TenantType.tenant);
		   tenant.setAddress(member.getAddress());
		   tenant.setLinkman(member.getName());
		   tenant.setTelephone(member.getMobile());
		   tenant.setName(member.getName()+"的店铺");
	    } 
		model.addAttribute("tenantCategoryTree", tenantCategoryService.findTree());
		model.addAttribute("tenant", tenant);
		model.addAttribute("member", member);
		model.addAttribute("type", type);
		return "b2b/member/tenant/edit";
	}
	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Tenant tenant, Long communityId,Long tenantCategoryId, long areaId, MultipartFile file,MultipartFile licensePhoto,RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		Tenant saveTenant = member.getTenant();
		if (saveTenant==null ) 
		{
			saveTenant = EntitySupport.createInitTenant();
		}
			
		BeanUtils.copyProperties(tenant, saveTenant, new String[] {"shortName","code","score","totalScore","scoreCount","hits","weekHits","monthHits","createDate","modifyDate","status","logo","licensePhoto","totalAssistant"});
		if (file != null && !file.isEmpty()) {
			if (!fileService.isValid(FileType.image, file)) {
				addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
				return "redirect:add.jhtml";
			} else {
				ProductImage img = new ProductImage();
				img.setFile(file);
				productImageService.build(img);
				saveTenant.setLogo(img.getThumbnail());
			}
		}
		if (licensePhoto != null && !licensePhoto.isEmpty()) {
			if (!fileService.isValid(FileType.image, licensePhoto)) {
				addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
				return "redirect:add.jhtml";
			} else {
				ProductImage img = new ProductImage();
				img.setFile(licensePhoto);
				productImageService.build(img);
				saveTenant.setLicensePhoto(img.getThumbnail());
			}
 		}
		saveTenant.setQq(tenant.getQq());
		saveTenant.setArea(areaService.find(areaId));
		saveTenant.setShortName(tenant.getName());
		saveTenant.setTenantCategory(tenantCategoryService.find(tenantCategoryId));	
		saveTenant.setStatus(Tenant.Status.none);
		tenantService.save(saveTenant,member,null);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:status.jhtml";
	}
	/**
	 * 保存
	 */
	@RequestMapping(value = "/save_tenant", method = RequestMethod.POST)
	public String save_tenant(String type,Tenant tenant, Long tenantCategoryId, long areaId, MultipartFile file,MultipartFile licensePhoto,RedirectAttributes redirectAttributes) {
		Tenant saveTenant = null;
		Member member = memberService.getCurrent();
		saveTenant = tenantService.find(tenant.getId());
		if(type.equals("1")){
			saveTenant.setIntroduction(tenant.getIntroduction());
			tenantService.save(saveTenant,member,null);
			addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		}else{
			if (tenant.getId()!=null && tenant.getId()!=0) {
				if (file != null && !file.isEmpty()) {
					if (!fileService.isValid(FileType.image, file)) {
						addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
						return "redirect:add.jhtml";
					} else {
						ProductImage img = new ProductImage();
						img.setFile(file);
						productImageService.build(img);
						saveTenant.setLogo(img.getThumbnail());
					}
				}
				saveTenant.setTenantType(tenant.getTenantType());
				saveTenant.setName(tenant.getName());
				saveTenant.setAddress(tenant.getAddress());
				saveTenant.setLinkman(tenant.getLinkman());
				saveTenant.setTelephone(tenant.getTelephone());
				saveTenant.setLicenseCode(tenant.getLicenseCode());
				saveTenant.setArea(areaService.find(areaId));
				saveTenant.setShortName(tenant.getName());
				saveTenant.setTenantCategory(tenantCategoryService.find(tenantCategoryId));	
				tenantService.save(saveTenant,member,null);
				addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
			}
		}
		
		return "redirect:edit.jhtml?type="+type;
	}
	/**
	 * 员工列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber,ModelMap model) {
		Member agent = memberService.getCurrent();
		if (agent==null) {
			return ERROR_VIEW;
		}
		Tenant tenant = agent.getTenant();
		if (tenant==null) {
			return ERROR_VIEW;
		}
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("page", memberService.findPage(agent, pageable));
		model.addAttribute("agent",agent);
		model.addAttribute("member",agent);
		return "b2b/member/tenant/list";
	}
	/**
	 * 员工移除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long id) {
		Member agent = memberService.getCurrent();
		Member member = memberService.find(id);
		if(member == agent){
			return Message.error("不能删除自身");
		}
		member.setTenant(null);
		member.setMember(null);
		memberService.save(member);
		return SUCCESS_MESSAGE;
	}
	/**
	 * 注册页面
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		model.addAttribute("genders", Gender.values());
		model.addAttribute("captchaId", UUID.randomUUID().toString());
		return "/b2b/member/tenant/register";
	}
	/**
	 * 注册提交
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public @ResponseBody
	Message submit(String captchaId, String captcha, String username, String email, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		String password = rsaService.decryptParameter("enPassword", request);
		rsaService.removePrivateKey(request);

		Setting setting = SettingUtils.get();
		ResourceBundle bundle=PropertyResourceBundle.getBundle("config");

		if (!setting.getIsRegisterEnabled()) {
			return Message.error("shop.register.disabled");
		}
		if (!isValid(Member.class, "username", username, Save.class) || !isValid(Member.class, "password", password, Save.class) || !isValid(Member.class, "email", email, Save.class)) {
			return Message.error("shop.common.invalid");
		}
		if (username.length() < setting.getUsernameMinLength() || username.length() > setting.getUsernameMaxLength()) {
			return Message.error("shop.common.invalid");
		}
		if (password.length() < setting.getPasswordMinLength() || password.length() > setting.getPasswordMaxLength()) {
			return Message.error("shop.common.invalid");
		}
		if (memberService.usernameDisabled(username) || memberService.usernameExists(username)) {
			return Message.error("shop.register.disabledExist");
		}
		if (!setting.getIsDuplicateEmail() && memberService.emailExists(email)) {
			return Message.error("shop.register.emailExist");
		}

		Member member = EntitySupport.createInitMember();
		List<MemberAttribute> memberAttributes = memberAttributeService.findList();
		for (MemberAttribute memberAttribute : memberAttributes) {
			String parameter = request.getParameter("memberAttribute_" + memberAttribute.getId());
			if (memberAttribute.getType() == Type.name || memberAttribute.getType() == Type.address || memberAttribute.getType() == Type.zipCode || memberAttribute.getType() == Type.phone || memberAttribute.getType() == Type.mobile || memberAttribute.getType() == Type.text || memberAttribute.getType() == Type.select) {
				if (memberAttribute.getIsRequired() && StringUtils.isEmpty(parameter)) {
					return Message.error("shop.common.invalid");
				}
				member.setAttributeValue(memberAttribute, parameter);
			} else if (memberAttribute.getType() == Type.gender) {
				Gender gender = StringUtils.isNotEmpty(parameter) ? Gender.valueOf(parameter) : null;
				if (memberAttribute.getIsRequired() && gender == null) {
					return Message.error("shop.common.invalid");
				}
				member.setGender(gender);
			} else if (memberAttribute.getType() == Type.birth) {
				try {
					Date birth = StringUtils.isNotEmpty(parameter) ? DateUtils.parseDate(parameter, CommonAttributes.DATE_PATTERNS) : null;
					if (memberAttribute.getIsRequired() && birth == null) {
						return Message.error("shop.common.invalid");
					}
					member.setBirth(birth);
				} catch (ParseException e) {
					return Message.error("shop.common.invalid");
				}
			} else if (memberAttribute.getType() == Type.area) {
				Area area = StringUtils.isNotEmpty(parameter) ? areaService.find(Long.valueOf(parameter)) : null;
				if (area != null) {
					member.setArea(area);
				} else if (memberAttribute.getIsRequired()) {
					return Message.error("shop.common.invalid");
				}
			} else if (memberAttribute.getType() == Type.checkbox) {
				String[] parameterValues = request.getParameterValues("memberAttribute_" + memberAttribute.getId());
				List<String> options = parameterValues != null ? Arrays.asList(parameterValues) : null;
				if (memberAttribute.getIsRequired() && (options == null || options.isEmpty())) {
					return Message.error("shop.common.invalid");
				}
				member.setAttributeValue(memberAttribute, options);
			}
		}
		Member agent = memberService.getCurrent();
		if (agent==null) {
			return Message.error("您的会话已经失效，请重新登录");
		}
		Tenant tenant = agent.getTenant();
		if (tenant==null) {
			return Message.error("您的店铺没有通过认证，不能添加员工");
		}
		if (tenant.getStatus()!=Tenant.Status.success) {
			return Message.error("您的店铺没有通过认证，不能添加员工");
		}
		
		member.setUsername(username.toLowerCase());
		member.setPassword(DigestUtils.md5Hex(password));
		member.setEmail(email);
		member.setPoint(setting.getRegisterPoint());
		member.setAmount(new BigDecimal(0));
		member.setBalance(new BigDecimal(0));
		member.setIsEnabled(true);
		member.setIsLocked(Member.LockType.none);
		member.setLoginFailureCount(0);
		member.setLockedDate(null);
		member.setRegisterIp(request.getRemoteAddr());
		member.setLoginIp(request.getRemoteAddr());
		member.setLoginDate(new Date());
		member.setSafeKey(null);
		member.setBindEmail(Member.BindStatus.none);
		member.setBindMobile(Member.BindStatus.none);
		member.setPaymentPassword(DigestUtils.md5Hex(password));
		member.setRebateAmount(new BigDecimal(0));
		member.setProfitAmount(new BigDecimal(0));
		member.setMemberRank(memberRankService.findDefault());
		member.setFavoriteProducts(null);
		member.setTenant(tenant);
		member.setMember(tenant.getMember());
		memberService.save(member);
		
		SmsSend smsSend=new SmsSend();
		smsSend.setMobiles(member.getMobile());
		smsSend.setContent("【"+setting.getSiteName()+"】账号申请通过,账号:"+member.getUsername()+",初始密码:"+password+",网址:"+setting.getSiteUrl()+",请登录修改密码。【"+bundle.getString("signature")+"】");
		smsSend.setType(SmsSend.Type.captcha);
		smsSendService.smsSend(smsSend);

		return Message.success("注册会员成功");
	}

	/**
	 * 查询审核状态
	 */
	@RequestMapping(value = "/status")
		public String find(ModelMap model) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
	    if (tenant==null) {
		   tenant = EntitySupport.createInitTenant();
	    }
		model.addAttribute("tags", tenant.getTags());
		model.addAttribute("tenantCategoryTree", tenantCategoryService.findTree());
		model.addAttribute("tenant", tenant);
		return "/b2b/member/tenant/status";
	}
	/**
	 * 宣传栏列表
	 */
	@RequestMapping(value = "/article/list", method = RequestMethod.GET)
	public String articleList(Pageable pageable, ModelMap model) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		model.addAttribute("member", member);
		model.addAttribute("page", articleService.findMyPage(tenant,pageable));
		return "/b2b/member/tenant/article/list";
	}

	/**
	 * 宣传栏删除
	 */
	@RequestMapping(value = "/article/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message articleDelete(Long[] ids) {
		articleService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	/**
	 * 宣传栏添加
	 */
	@RequestMapping(value = "/article/add", method = RequestMethod.GET)
	public String articleAdd(ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		model.addAttribute("articleCategoryTree", articleCategoryService.find(67L));
		model.addAttribute("tags", tagService.findList(net.wit.entity.Tag.Type.article));
		return "/b2b/member/tenant/article/add";
	}

	/**
	 * 宣传栏保存
	 */
	@RequestMapping(value = "/article/save", method = RequestMethod.POST)
	public String articleSave(Article article,Long areaId, Long articleCategoryId, Long[] tagIds, MultipartFile file, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		if (file != null && !file.isEmpty()) {
			if (!fileService.isValid(FileType.image, file)) {
				addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
				return "redirect:add.jhtml";
			} else {
				ProductImage img = new ProductImage();
				img.setFile(file);
				productImageService.build(img);
				article.setImage(img.getThumbnail());
			}
		}
		article.setArticleCategory(articleCategoryService.find(16l));
		article.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
		article.setArea(areaService.find(areaId));
		if (!isValid(article)) {
			return ERROR_VIEW;
		}
		article.setHits(0L);
		article.setPageNumber(null);
		article.setTenant(tenant);
		articleService.save(article);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 宣传栏编辑
	 */
	@RequestMapping(value = "/article/edit", method = RequestMethod.GET)
	public String articleEdit(Long id, ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		model.addAttribute("articleCategoryTree", articleCategoryService.find(67L));
		model.addAttribute("tags", tagService.findList(net.wit.entity.Tag.Type.article));
		model.addAttribute("article", articleService.find(id));
		return "/b2b/member/tenant/article/edit";
	}

	/**
	 * 宣传栏更新
	 */
	@RequestMapping(value = "/article/update", method = RequestMethod.POST)
	public String articleUpdate(Article article, Long areaId, Long articleCategoryId, Long[] tagIds, MultipartFile file, RedirectAttributes redirectAttributes) {
		if (file != null && !file.isEmpty()) {
			if (!fileService.isValid(FileType.image, file)) {
				addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
				return "redirect:edit.jhtml";
			} else {
				ProductImage img = new ProductImage();
				img.setFile(file);
				productImageService.build(img);
				article.setImage(img.getThumbnail());
			}
		}
		article.setArticleCategory(articleCategoryService.find(16L));
		article.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
		article.setArea(areaService.find(areaId));
		if (!isValid(article)) {
			return ERROR_VIEW;
		}
		Article art = articleService.find(article.getId());
		article.setTenant(art.getTenant());
		articleService.update(article, "hits", "pageNumber");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}
	/**
	 * 广告添加
	 */
	@RequestMapping(value = "/ad/add", method = RequestMethod.GET)
	public String adAdd(ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		model.addAttribute("types", net.wit.entity.Ad.Type.values());
		model.addAttribute("adPositions", adPositionService.find(55L));
		return "/b2b/member/tenant/ad/add";
	}

	/**
	 * 广告保存
	 */
	@RequestMapping(value = "/ad/save", method = RequestMethod.POST)
	public String adSave(Ad ad, Long adPositionId, RedirectAttributes redirectAttributes, MultipartFile file) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		if (file != null && !file.isEmpty()) {
			if (!fileService.isValid(FileType.image, file)) {
				addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
				return "redirect:add.jhtml";
			} else {
				ProductImage img = new ProductImage();
				img.setFile(file);
				productImageService.build(img);
				ad.setPath(img.getSource());
			}
		}
		ad.setAdPosition(adPositionService.find(55L));
		ad.setTenant(tenant);
		if (!isValid(ad)) {
			return ERROR_VIEW;
		}
//		if (ad.getBeginDate() != null && ad.getEndDate() != null && ad.getBeginDate().after(ad.getEndDate())) {
//			return ERROR_VIEW;
//		}
		if (ad.getType() == net.wit.entity.Ad.Type.text) {
			ad.setPath(null);
		} else {
			ad.setContent(null);
		}
		adService.save(ad);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 广告编辑
	 */
	@RequestMapping(value = "/ad/edit", method = RequestMethod.GET)
	public String adEdit(Long id, ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		model.addAttribute("types", net.wit.entity.Ad.Type.values());
		model.addAttribute("ad", adService.find(id));
		model.addAttribute("adPositions", adPositionService.find(55L));
		return "/b2b/member/tenant/ad/edit";
	}

	/**
	 * 广告更新
	 */
	@RequestMapping(value = "/ad/update", method = RequestMethod.POST)
	public String adUpdate(Ad ad, Long adPositionId, RedirectAttributes redirectAttributes, MultipartFile file,String path) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		if (file != null && !file.isEmpty()) {
			if (!fileService.isValid(FileType.image, file)) {
				addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
				return "redirect:edit.jhtml";
			} else {
				ProductImage img = new ProductImage();
				img.setFile(file);
				productImageService.build(img);
				ad.setPath(img.getThumbnail());
			}
		}else {
			ad.setPath(path);
		}
		ad.setAdPosition(adPositionService.find(55L));
		ad.setTenant(tenant);
		if (!isValid(ad)) {
			return ERROR_VIEW;
		}
//		if (ad.getBeginDate() != null && ad.getEndDate() != null && ad.getBeginDate().after(ad.getEndDate())) {
//			return ERROR_VIEW;
//		}
		if (ad.getType() == net.wit.entity.Ad.Type.text) {
			ad.setPath(null);
		} else {
			ad.setContent(null);
		}
		adService.update(ad);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 广告列表
	 */
	@RequestMapping(value = "/ad/list", method = RequestMethod.GET)
	public String adList(Pageable pageable, ModelMap model) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		model.addAttribute("member", member);
		model.addAttribute("page", adService.findMyPage(tenant,pageable));
		return "/b2b/member/tenant/ad/list";
	}

	/**
	 * 广告删除
	 */
	@RequestMapping(value = "/ad/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message adDelete(Long[] ids) {
		adService.delete(ids);
		return SUCCESS_MESSAGE;
	}
	/**
	 * 业务员列表
	 */
	@RequestMapping(value = "/salesMan", method = RequestMethod.GET)
	public String salesMan(ModelMap model) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return ERROR_VIEW;
		}
		Tenant tenant = member.getTenant();
		if (tenant==null) {
			return ERROR_VIEW;
		}
		model.addAttribute("page", memberService.findPage(tenant, null,null));
		model.addAttribute("member", member);
		return "/b2b/member/tenant/salesMan";
	}
	/**
	 * 选择业务员
	 */
	@RequestMapping(value = "/saveSalesman", method = RequestMethod.POST)
	public String saveSalesman(Long salesmanId,ModelMap model) {
		Member salesman =memberService.find(salesmanId);
		Member member = memberService.getCurrent();
		if (member==null) {
			return ERROR_VIEW;
		}
		Tenant tenant = member.getTenant();
		if (tenant==null) {
			return ERROR_VIEW;
		}
		tenantService.save(tenant);
		return "redirect:salesMan.jhtml";
	}
	/**
	 * 移除业务员
	 */
	@RequestMapping(value = "/remove", method = RequestMethod.POST)
	public @ResponseBody
	Message remove(Long id) {
		//Member salesman =memberService.find(id);
		Member member = memberService.getCurrent();;
		Tenant tenant = member.getTenant();
		tenantService.save(tenant);
		return SUCCESS_MESSAGE;
	}
}
