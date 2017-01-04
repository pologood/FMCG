/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.CommonAttributes;
import net.wit.Filter;
import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.Filter.Operator;
import net.wit.controller.app.model.DataBlock;
import net.wit.entity.*;
import net.wit.entity.BaseEntity.Save;
import net.wit.entity.Member.BindStatus;
import net.wit.entity.Member.Gender;
import net.wit.entity.MemberAttribute.Type;
import net.wit.service.AdminService;
import net.wit.service.AreaService;
import net.wit.service.MemberAttributeService;
import net.wit.service.MemberRankService;
import net.wit.service.MemberService;
import net.wit.service.TenantService;
import net.wit.util.SettingUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller - 会员
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminMemberController")
@RequestMapping("/admin/member")
public class MemberController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;

	@Resource(name = "memberAttributeServiceImpl")
	private MemberAttributeService memberAttributeService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "adminServiceImpl")
	private AdminService adminService;

	/**
	 * 检查用户名是否被禁用或已存在
	 */
	@RequestMapping(value = "/check_username", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkUsername(String username) {
		if (StringUtils.isEmpty(username)) {
			return false;
		}
		if (memberService.usernameDisabled(username) || memberService.usernameExists(username)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 检查E-mail是否唯一
	 */
	@RequestMapping(value = "/check_email", method = RequestMethod.GET)
	public @ResponseBody
	boolean checkEmail(String previousEmail, String email) {
		if (StringUtils.isEmpty(email)) {
			return false;
		}
		if (memberService.emailUnique(previousEmail, email)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 查看
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(Long id, ModelMap model) {
		model.addAttribute("genders", Gender.values());
		model.addAttribute("memberAttributes", memberAttributeService.findList());
		model.addAttribute("member", memberService.find(id));
		return "/admin/member/view";
	}

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap model) {
		model.addAttribute("genders", Gender.values());
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("memberAttributes", memberAttributeService.findList());
		return "/admin/member/add";
	}

	/** 保存 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Member member, Long memberRankId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		member.setMemberRank(memberRankService.find(memberRankId));
		member.removeAttributeValue();
		for (MemberAttribute memberAttribute : memberAttributeService.findList()) {
			String parameter = request.getParameter("memberAttribute_" + memberAttribute.getId());
			if (memberAttribute.getType() == Type.name || memberAttribute.getType() == Type.address || memberAttribute.getType() == Type.zipCode || memberAttribute.getType() == Type.phone || memberAttribute.getType() == Type.mobile || memberAttribute.getType() == Type.text || memberAttribute.getType() == Type.select) {
				if (memberAttribute.getIsRequired() && StringUtils.isEmpty(parameter)) {
					return ERROR_VIEW;
				}
				member.setAttributeValue(memberAttribute, parameter);
			} else if (memberAttribute.getType() == Type.gender) {
				Gender gender = StringUtils.isNotEmpty(parameter) ? Gender.valueOf(parameter) : null;
				if (memberAttribute.getIsRequired() && gender == null) {
					return ERROR_VIEW;
				}
				member.setGender(gender);
			} else if (memberAttribute.getType() == Type.birth) {
				try {
					Date birth = StringUtils.isNotEmpty(parameter) ? DateUtils.parseDate(parameter, CommonAttributes.DATE_PATTERNS) : null;
					if (memberAttribute.getIsRequired() && birth == null) {
						return ERROR_VIEW;
					}
					member.setBirth(birth);
				} catch (ParseException e) {
					return ERROR_VIEW;
				}
			} else if (memberAttribute.getType() == Type.area) {
				Area area = StringUtils.isNotEmpty(parameter) ? areaService.find(Long.valueOf(parameter)) : null;
				if (area != null) {
					member.setArea(area);
				} else if (memberAttribute.getIsRequired()) {
					return ERROR_VIEW;
				}
			} else if (memberAttribute.getType() == Type.checkbox) {
				String[] parameterValues = request.getParameterValues("memberAttribute_" + memberAttribute.getId());
				List<String> options = parameterValues != null ? Arrays.asList(parameterValues) : null;
				if (memberAttribute.getIsRequired() && (options == null || options.isEmpty())) {
					return ERROR_VIEW;
				}
				member.setAttributeValue(memberAttribute, options);
			}
		}
		Setting setting = SettingUtils.get();
		if (member.getUsername().length() < setting.getUsernameMinLength() || member.getUsername().length() > setting.getUsernameMaxLength()) {
			return ERROR_VIEW;
		}
		if (member.getPassword().length() < setting.getPasswordMinLength() || member.getPassword().length() > setting.getPasswordMaxLength()) {
			return ERROR_VIEW;
		}
		if (memberService.usernameDisabled(member.getUsername()) || memberService.usernameExists(member.getUsername())) {
			return ERROR_VIEW;
		}
		if (!setting.getIsDuplicateEmail() && memberService.emailExists(member.getEmail())) {
			return ERROR_VIEW;
		}

		member.setUsername(member.getUsername().toLowerCase());
		member.setPassword(DigestUtils.md5Hex(member.getPassword()));
		member.setTenant(null);
		member.setAmount(new BigDecimal(0));
		member.setClearBalance(new BigDecimal(0));
		member.setIsLocked(Member.LockType.none);
		member.setLoginFailureCount(0);
		member.setLockedDate(null);
		member.setRegisterIp(request.getRemoteAddr());
		member.setLoginIp(null);
		member.setLoginDate(null);
		member.setSafeKey(null);
		member.setCart(null);
		member.setOrders(null);
		member.setDeposits(null);
		member.setPayments(null);
		if (member.getPaymentPassword() == null) {
			member.setPaymentPassword(member.getPassword());
		}
		member.setCouponCodes(null);
		member.setReceivers(null);
		member.setReviews(null);
		member.setConsultations(null);
		member.setFavoriteProducts(null);
		member.setProductNotifies(null);
		member.setInMessages(null);
		member.setOutMessages(null);
		if (member.getBindEmail() == null) {
			member.setBindEmail(BindStatus.none);
		}
		if (member.getBindMobile() == null) {
			member.setBindMobile(BindStatus.none);
		}
		member.setProfitAmount(BigDecimal.ZERO);
		member.setRebateAmount(BigDecimal.ZERO);
		member.setFreezeBalance(BigDecimal.ZERO);
		member.setPrivilege(0);
		member.setTotalScore(0L);
		member.setScore(0f);
		member.setScoreCount(0L);
		if (!isValid(member, Save.class)) {
			return ERROR_VIEW;
		}
		memberService.save(member, adminService.getCurrent());
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Long id, ModelMap model) {
		model.addAttribute("genders", Gender.values());
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("memberAttributes", memberAttributeService.findList());
		model.addAttribute("member", memberService.find(id));
		return "/admin/member/edit";
	}

	/**
	 * 更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Member member, Long memberRankId, Integer modifyPoint, BigDecimal modifyBalance, String depositMemo, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		member.setMemberRank(memberRankService.find(memberRankId));
		Setting setting = SettingUtils.get();
		if (member.getPassword() != null && (member.getPassword().length() < setting.getPasswordMinLength() || member.getPassword().length() > setting.getPasswordMaxLength())) {
			return ERROR_VIEW;
		}
		Member pMember = memberService.find(member.getId());
		if (pMember == null) {
			return ERROR_VIEW;
		}
		member.removeAttributeValue();
		for (MemberAttribute memberAttribute : memberAttributeService.findList()) {
			String parameter = request.getParameter("memberAttribute_" + memberAttribute.getId());
			if (memberAttribute.getType() == Type.name || memberAttribute.getType() == Type.address || memberAttribute.getType() == Type.zipCode || memberAttribute.getType() == Type.phone || memberAttribute.getType() == Type.text || memberAttribute.getType() == Type.select) {
				member.setAttributeValue(memberAttribute, parameter);
			} else if (memberAttribute.getType() == Type.gender) {
				Gender gender = StringUtils.isNotEmpty(parameter) ? Gender.valueOf(parameter) : null;
				if (memberAttribute.getIsRequired() && gender == null) {
					return ERROR_VIEW;
				}
				member.setGender(gender);
			} else if (memberAttribute.getType() == Type.birth) {
				try {
					Date birth = StringUtils.isNotEmpty(parameter) ? DateUtils.parseDate(parameter, CommonAttributes.DATE_PATTERNS) : null;
					if (memberAttribute.getIsRequired() && birth == null) {
						return ERROR_VIEW;
					}
					member.setBirth(birth);
				} catch (ParseException e) {
					return ERROR_VIEW;
				}
			} else if (memberAttribute.getType() == Type.area) {
				Area area = StringUtils.isNotEmpty(parameter) ? areaService.find(Long.valueOf(parameter)) : null;
				if (area != null) {
					member.setArea(area);
				} else if (memberAttribute.getIsRequired()) {
					return ERROR_VIEW;
				}
			} else if (memberAttribute.getType() == Type.checkbox) {
				String[] parameterValues = request.getParameterValues("memberAttribute_" + memberAttribute.getId());
				List<String> options = parameterValues != null ? Arrays.asList(parameterValues) : null;
				if (memberAttribute.getIsRequired() && (options == null || options.isEmpty())) {
					return ERROR_VIEW;
				}
				member.setAttributeValue(memberAttribute, options);
			}
		}
		if (StringUtils.isEmpty(member.getPassword())) {
			member.setPassword(pMember.getPassword());
		} else {
			member.setPassword(DigestUtils.md5Hex(member.getPassword()));
		}
		if (member.getIsLocked()==null || member.getIsLocked().equals(Member.LockType.none)) {
			member.setIsLocked(Member.LockType.none);
			member.setLoginFailureCount(0);
			member.setLockedDate(null);
		} else {
			member.setIsLocked(member.getIsLocked());
			if(member.getIsLocked().equals(Member.LockType.locked)){
				member.setLoginFailureCount(5);
			}else{
				member.setLoginFailureCount(0);
			}
			member.setLockedDate(new Date());
		}
		member.setScore(pMember.getScore());
		member.setScoreCount(pMember.getScoreCount());
		member.setTenant(pMember.getTenant());
		BeanUtils.copyProperties(member, pMember, new String[] { "username", "member", "bindEmail", "bindMobile", "mobile", "email", "idcard", "point", "amount", "profitAmount", "rebateAmount", "balance", "clearBalance", "registerIp", "loginIp", "loginDate", "safeKey", "cart", "orders", "deposits", "payments", "couponCodes", "receivers", "reviews", "consultations",
				"favoriteProducts","favoriteTenants", "productNotifies", "inMessages", "outMessages", "totalScore", "freezeBalance", "privilege" });
		try {
			memberService.update(pMember);
		} catch (Exception e) {
			return ERROR_VIEW;
		}
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Date beginDate, Date endDate,Pageable pageable, ModelMap model) {
		model.addAttribute("page", memberService.findPage(beginDate,endDate,pageable));
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("memberAttributes", memberAttributeService.findAll());
		return "/admin/member/list";
	}

	/**
	 * 列表导出
	 */
	@RequestMapping(value = "/list_export", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock listExport(Date beginDate, Date endDate, String keywords) {
		List<Map<String ,Object>> maps=new ArrayList<Map<String ,Object>>();
		try {
			List<Member> members= memberService.memberListExport(beginDate,endDate,keywords);
			if(members.size()>=2000){
				return DataBlock.warn("数据太多请进行筛选处理！");
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(Member member:members){
                Map<String ,Object> map=new HashMap<String ,Object>();
                map.put("create_date",sdf.format(member.getCreateDate()));
                map.put("name",member.getDisplayName());
				map.put("username",member.getUsername());
                map.put("mobile",member.getMobile());
                map.put("member_rank",member.getMemberRank().getName());
                map.put("is_auther",member.getIdcard()!=null?
                        (member.getIdcard().getAuthStatus()!= Idcard.AuthStatus.success?"已认证":"未认证"):"未认证");
                map.put("point",member.getPoint());
                map.put("balance",member.getBalance());
                map.put("freeze_balance",member.getFreezeBalance());
                map.put("clearing_balance",member.getClearBalance());
                map.put("order_amount",member.getOrderAmount());
                map.put("order_count",member.getOrderCount());
                map.put("login_count",member.getLoginCount());
                map.put("login_fail_count",member.getLoginFailureCount());
                if(!member.getIsEnabled()){
                    map.put("status","禁用");
                }else if(member.getIsLocked()== Member.LockType.locked){
                    map.put("status","锁定");
                }else if(member.getIsLocked()== Member.LockType.freezed){
                    map.put("status","冻结");
                }else{
                    map.put("status","正常");
                }
                map.put("register_ip",member.getRegisterIp());
                map.put("last_login_date",sdf.format(member.getLoginDate()));
                map.put("last_login_ip",member.getLoginIp());
                map.put("review_count",member.getReviews().size());
				map.put("save_product_count",member.getFavoriteProducts().size());
                map.put("consultation_count",member.getConsultations().size());
                map.put("area_name",member.getArea()!=null?member.getArea().getName():"--");
                map.put("gender",member.getGender()==Gender.female?"女":"男");
                map.put("extension_name",member.getMember()!=null?member.getMember().getName():"--");
                if(member.getMember()!=null){
                    if(member.getMember().getTenant()!=null){
                        map.put("extension_tenant",member.getMember().getTenant().getName());
                    }else {
                        map.put("extension_tenant","--");
                    }
                }else{
                    map.put("extension_tenant","--");
                }
				maps.add(map);
            }
		} catch (Exception e) {
			e.printStackTrace();
			return DataBlock.error("导出失败");
		}
		return DataBlock.success(maps,"导出成功");
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody
	Message delete(Long[] ids) {
		if (ids != null) {
			for (Long id : ids) {
				Member member = memberService.find(id);
				if (member != null && member.getBalance().compareTo(new BigDecimal(0)) > 0) {
					return Message.error("admin.member.deleteExistDepositNotAllowed", member.getUsername());
				}
			}
			memberService.delete(ids);
		}
		return SUCCESS_MESSAGE;
	}

	/** 自动注册商家 */
	@RequestMapping(value = "/upgrade", method = RequestMethod.GET)
	public String upgrade(Long id) {
		Member member = memberService.find(id);
		memberService.upgrade(member);
		return "redirect:list.jhtml";
	}
	
	
	/**
	 * 超级查看
	 */
	@RequestMapping(value = "/superview", method = RequestMethod.GET)
	public String superview(Long id, ModelMap model) {
		Admin admin = adminService.getCurrent();
		model.addAttribute("genders", Gender.values());
		model.addAttribute("memberAttributes", memberAttributeService.findList());
		model.addAttribute("member", memberService.find(id));
		model.addAttribute("administrator",admin);
		return "/admin/member/superview";
	}
	
	/**
	 * 编辑
	 */
	@RequestMapping(value = "/superedit", method = RequestMethod.GET)
	public String superedit(Long id, ModelMap model) {
		Admin admin = adminService.getCurrent();
		model.addAttribute("genders", Gender.values());
		model.addAttribute("memberRanks", memberRankService.findAll());
		model.addAttribute("memberAttributes", memberAttributeService.findList());
		model.addAttribute("member", memberService.find(id));
		model.addAttribute("administrator",admin);
		return "/admin/member/superedit";
	}

}