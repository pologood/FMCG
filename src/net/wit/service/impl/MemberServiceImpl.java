/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.persistence.LockModeType;
import javax.servlet.http.HttpServletRequest;

import net.wit.Filter;
import net.wit.Filter.Operator;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Principal;
import net.wit.Setting;
import net.wit.dao.AreaDao;
import net.wit.dao.DeliveryCenterDao;
import net.wit.dao.DepositDao;
import net.wit.dao.MemberDao;
import net.wit.dao.MemberRankDao;
import net.wit.dao.MessageDao;
import net.wit.dao.TenantDao;
import net.wit.dao.BindUserDao;
import net.wit.dao.VideoDemoDao;
import net.wit.entity.Admin;
import net.wit.entity.Area;
import net.wit.entity.BindUser;
import net.wit.entity.Deposit;
import net.wit.entity.Enterprise;
import net.wit.entity.Enterprise.EnterpriseType;
import net.wit.entity.Idcard.AuthStatus;
import net.wit.entity.Location;
import net.wit.entity.Member;
import net.wit.entity.Message;
import net.wit.entity.Tenant;
import net.wit.entity.BindUser.Type;
import net.wit.entity.Tenant.TenantType;
import net.wit.exception.BalanceNotEnoughException;
import net.wit.service.MemberService;
import net.wit.service.MessageService;
import net.wit.service.RSAService;
import net.wit.service.TenantCategoryService;
import net.wit.support.EntitySupport;
import net.wit.support.PushMessage;
import net.wit.util.SettingUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Service - 会员
 * @author rsico Team
 * @version 3.0
 */
@Service("memberServiceImpl")
public class MemberServiceImpl extends BaseServiceImpl<Member, Long> implements MemberService {

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;

	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;

	@Resource(name = "tenantDaoImpl")
	private TenantDao tenantDao;

	@Resource(name = "bindUserDaoImpl")
	private BindUserDao bindUserDao;

	@Resource(name = "areaDaoImpl")
	private AreaDao areaDao;

	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;

	@Resource(name = "videoDemoDaoImpl")
	private VideoDemoDao videoDemoDao;

	@Resource(name = "deliveryCenterDaoImpl")
	private DeliveryCenterDao deliveryCenterDao;

	@Resource(name = "messageServiceImpl")
	private MessageService messageService;
	
	@Resource(name = "memberRankDaoImpl")
	private MemberRankDao memberRankDao;

	@Resource(name = "tenantCategoryServiceImpl")
	private TenantCategoryService tenantCategoryService;

	@Resource(name = "memberDaoImpl")
	public void setBaseDao(MemberDao memberDao) {
		super.setBaseDao(memberDao);
	}

	@Transactional(readOnly = true)
	public boolean usernameExists(String username) {
		return memberDao.usernameExists(username);
	}

	@Transactional(readOnly = true)
	public boolean usernameDisabled(String username) {
		Assert.hasText(username);
		Setting setting = SettingUtils.get();
		if (setting.getDisabledUsernames() != null) {
			for (String disabledUsername : setting.getDisabledUsernames()) {
				if (StringUtils.containsIgnoreCase(username, disabledUsername)) {
					return true;
				}
			}
		}
		return false;
	}

	@Transactional(readOnly = true)
	public boolean emailExists(String email) {
		return memberDao.emailExists(email);
	}

	@Transactional(readOnly = true)
	public boolean mobileExists(String mobile) {
		return memberDao.mobileExists(mobile);
	}

	@Transactional(readOnly = true)
	public boolean emailUnique(String previousEmail, String currentEmail) {
		if (StringUtils.equalsIgnoreCase(previousEmail, currentEmail)) {
			return true;
		} else {
			if (memberDao.emailExists(currentEmail)) {
				return false;
			} else {
				return true;
			}
		}
	}

	@Override
	public void save(Member member) {
		Assert.notNull(member);
		memberDao.persist(member);
	}

	public void save(Member member, Admin operator) {
		Assert.notNull(member);
		memberDao.persist(member);
	}

	public Deposit payment(Member member, Integer modifyPoint,BigDecimal modifyClearBalance, BigDecimal modifyBalance, String depositMemo, Admin operator) throws Exception {
		Assert.notNull(member);
		memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
		Deposit deposit = null;
		if (modifyBalance != null && modifyBalance.compareTo(new BigDecimal(0)) != 0) {
			if (member.getBalance().subtract(modifyBalance).compareTo(new BigDecimal(0)) < 0) {
				throw new BalanceNotEnoughException("balance.not.enough");
			}
			if (member.getClearBalance().subtract(modifyClearBalance).compareTo(new BigDecimal(0)) < 0) {
				throw new BalanceNotEnoughException("balance.not.enough");
			}
			member.setClearBalance(member.getClearBalance().subtract(modifyClearBalance));
			member.setBalance(member.getBalance().subtract(modifyBalance));
			deposit = new Deposit();
			deposit.setType(Deposit.Type.payment);
			deposit.setStatus(Deposit.Status.complete);
			deposit.setCredit(new BigDecimal(0));
			deposit.setDebit(modifyBalance.abs());
			deposit.setBalance(member.getBalance());
			deposit.setOperator(operator != null ? operator.getUsername() : null);
			deposit.setMemo(depositMemo);
			deposit.setMember(member);
			depositDao.persist(deposit);
			Setting setting = SettingUtils.get();
			
			Message msg = EntitySupport.createInitMessage(Message.Type.account,
					"您的账户" + depositMemo + setting.setScale(modifyBalance).toString()+"元.",
					"",member,null);
			msg.setDeposit(deposit);
			messageService.save(msg);
			
		}
		memberDao.merge(member);
		return deposit;
	}

	public void Recharge(Member member, Integer modifyPoint, BigDecimal modifyBalance, String depositMemo, Admin operator) throws Exception {
		Assert.notNull(member);

		memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);

		if (modifyPoint != null && modifyPoint != 0 && member.getPoint() + modifyPoint >= 0) {
			member.setPoint(member.getPoint() + modifyPoint);
		}

		if (modifyBalance != null && modifyBalance.compareTo(new BigDecimal(0)) != 0) {
			if (member.getBalance().add(modifyBalance).compareTo(new BigDecimal(0)) < 0) {
				throw new BalanceNotEnoughException("balance.not.enough");
			}
			member.setBalance(member.getBalance().add(modifyBalance));
			Deposit deposit = new Deposit();
			deposit.setType(Deposit.Type.recharge);
			deposit.setStatus(Deposit.Status.complete);
			deposit.setCredit(modifyBalance);
			deposit.setDebit(new BigDecimal(0));
			deposit.setBalance(member.getBalance());
			deposit.setOperator(operator != null ? operator.getUsername() : null);
			deposit.setMemo(depositMemo);
			deposit.setMember(member);
			depositDao.persist(deposit);
			Setting setting = SettingUtils.get();
			Message msg = EntitySupport.createInitMessage(Message.Type.account,
					"您的账户" + depositMemo + setting.setScale(modifyBalance).toString()+"元.",
					"",member,null);
			msg.setDeposit(deposit);
			messageService.save(msg);

		}
		memberDao.merge(member);
	}

	public void Cashier(Member member, Integer modifyPoint, BigDecimal modifyBalance, String depositMemo, Admin operator) throws Exception {
		Assert.notNull(member);

		memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);

		if (modifyPoint != null && modifyPoint != 0 && member.getPoint() + modifyPoint >= 0) {
			member.setPoint(member.getPoint() + modifyPoint);
		}

		if (modifyBalance != null && modifyBalance.compareTo(new BigDecimal(0)) != 0) {
			if (member.getBalance().add(modifyBalance).compareTo(new BigDecimal(0)) < 0) {
				throw new BalanceNotEnoughException("balance.not.enough");
			}
			member.setBalance(member.getBalance().add(modifyBalance));
			Deposit deposit = new Deposit();
			deposit.setType(Deposit.Type.cashier);
			deposit.setStatus(Deposit.Status.complete);
			deposit.setCredit(modifyBalance);
			deposit.setDebit(new BigDecimal(0));
			deposit.setBalance(member.getBalance());
			deposit.setOperator(operator != null ? operator.getUsername() : null);
			deposit.setMemo(depositMemo);
			deposit.setMember(member);
			depositDao.persist(deposit);
			Setting setting = SettingUtils.get();
			Message msg = EntitySupport.createInitMessage(Message.Type.account,
					"您的账户" + depositMemo + setting.setScale(modifyBalance).toString()+"元.",
					"",member,null);
			msg.setDeposit(deposit);
			messageService.save(msg);
		}
		memberDao.merge(member);
	}
	
	public void withdraw(Member member, Integer modifyPoint, BigDecimal modifyClearBalance, BigDecimal modifyBalance, String depositMemo, Admin operator) throws Exception {
		Assert.notNull(member);

		memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);

		if (modifyPoint != null && modifyPoint != 0 && member.getPoint() + modifyPoint >= 0) {
			member.setPoint(member.getPoint() + modifyPoint);
		}

		if (modifyBalance != null && modifyBalance.compareTo(new BigDecimal(0)) != 0) {
			if (member.getBalance().subtract(modifyBalance).compareTo(new BigDecimal(0)) < 0) {
				throw new BalanceNotEnoughException("balance.not.enough");
			}
			member.setClearBalance(member.getClearBalance().subtract(modifyClearBalance));
			member.setBalance(member.getBalance().subtract(modifyBalance));
			memberDao.merge(member);
			Deposit deposit = new Deposit();
			deposit.setType(Deposit.Type.withdraw);
			deposit.setStatus(Deposit.Status.complete);
			deposit.setCredit(new BigDecimal(0));
			deposit.setDebit(modifyBalance);
			deposit.setBalance(member.getBalance());
			deposit.setOperator(operator != null ? operator.getUsername() : null);
			deposit.setMemo(depositMemo);
			deposit.setMember(member);
			depositDao.persist(deposit);
			Setting setting = SettingUtils.get();
			Message msg = EntitySupport.createInitMessage(Message.Type.account,
					"您的账户" + depositMemo + setting.setScale(modifyBalance).toString()+"元.",
					"",member,null);
			msg.setDeposit(deposit);
			messageService.save(msg);
		}
	}

	@Transactional(readOnly = true)
	public Member findByUsername(String username) {
		return memberDao.findByUsername(username);
	}

	@Transactional(readOnly = true)
	public List<Member> findListByEmail(String email) {
		return memberDao.findListByEmail(email);
	}

	@Transactional(readOnly = true)
	public List<Object[]> findPurchaseList(Date beginDate, Date endDate, Integer count) {
		return memberDao.findPurchaseList(beginDate, endDate, count);
	}

	@Transactional(readOnly = true)
	public boolean isAuthenticated() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		if (requestAttributes != null) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			Principal principal = (Principal) request.getSession().getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
			if (principal != null) {
				return true;
			}
		}
		return false;
	}

	@Transactional(readOnly = true)
	public Member getCurrent() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		if (requestAttributes != null) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			Principal principal = (Principal) request.getSession().getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
			if (principal != null) {
				return memberDao.find(principal.getId());
			}
		}
		return null;
	}

	@Transactional(readOnly = true)
	public Area getCurrentArea() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		if (requestAttributes != null) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			Area area = (Area) request.getSession().getAttribute(Member.AREA_ATTRIBUTE_NAME);
			if (area != null) {
				return areaDao.find(area.getId());
			}
		}
		return null;
	}

	@Transactional(readOnly = true)
	public String getCurrentUsername() {
		RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		if (requestAttributes != null) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			Principal principal = (Principal) request.getSession().getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
			if (principal != null) {
				return principal.getUsername();
			}
		}
		return null;
	}

	public String getToken(Member member) {
		Tenant tenant = member.getTenant();
		if (tenant == null) {
			return "error";
		}

		String memberName = member.getName();
		if (memberName == null) {
			memberName = member.getUsername();
		}
		String memberAddress = member.getAddress();
		if (memberAddress == null) {
			memberAddress = "无";
		}
		String memberMobile = member.getMobile();
		if (memberMobile == null) {
			memberMobile = "无";
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String shopAddress = tenant.getAddress();
		String shopArea = null;
		if (tenant.getArea() != null) {
			if (tenant.getArea().getCode() == null) {
				shopArea = tenant.getArea().getId().toString();
			} else {
				shopArea = tenant.getArea().getCode();
			}
			shopAddress = tenant.getArea().getFullName() + shopAddress;
		} else {
			shopArea = "#";
		}

		if (tenant.getLicenseCode() == null) {
			tenant.setLicenseCode("无");
		}

		if (shopAddress == null) {
			shopAddress = "无";
		}

		if (tenant.getLinkman() == null) {
			tenant.setLinkman("无");
		}

		if (tenant.getTelephone() == null) {
			tenant.setTelephone("无");
		}
		
		String code = tenant.getCode();
		String shopId = "1" + String.format("%08d", tenant.getId())+"0001";
		if (code!=null && code.length()==9) {
			shopId = code + "0001";
		}
		
		String token = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<token code=\"0000\" msg=\"success\"> <userInfo>" + "<userId>" + String.format("%036d", member.getId()) + "</userId>" + "<account>" + member.getUsername() + "</account>" + "<username>"
				+ memberName + "</username>" + "<tenantId>"+ "1" + String.format("%08d", tenant.getId()) + "</tenantId>" + "<code>"+ tenant.getCode() + "</code>" + "<tenantName>" + tenant.getName() + "</tenantName>" + "<shopId>" + shopId+ "</shopId>"
				+ "<shopName>" + tenant.getShortName() + "</shopName>" + "<address>" + shopAddress + "</address>" + "<xsmCode>" + member.getUsername() + "</xsmCode>" + "<xsmAlias>" + member.getUsername() + "</xsmAlias>" + "<xsmPWD>" + member.getPassword()
				+ "</xsmPWD>" + "<licenseCode>" + tenant.getLicenseCode() + "</licenseCode>" + "<legal>" + tenant.getLinkman() + "</legal>" + "<idCard>无</idCard>" + "<mobile>" + tenant.getTelephone() + "</mobile>" + "<regionId>" + shopArea + "</regionId>"
				+ "<lDate>" + sdf.format(new java.util.Date()) + "</lDate>" + "<online>1</online>" + "</userInfo></token>";
		return token;
		
	}

	public Page<Member> findPage(Member member, Pageable pageable) {
		return memberDao.findPage(member, pageable);
	}
	
	/**
	 * 查找附近的人
	 * @param location 位置
	 * @param pageable 分页信息
	 * @return 收藏商家分页
	 */
	public Page<Member> findNearBy(Location location, Pageable pageable) {
		return memberDao.findNearBy(location, pageable);
	}

	public List<Member> findList(Member member) {
		return memberDao.findList(member);
	}

	public List<Member> findListEmployee(Member member) {
		return memberDao.findListEmployee(member);
	}

	public List<Member> findList(Tenant tenant) {
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("tenant", Operator.eq, tenant));
		return memberDao.findList(0, null, filters, null);
	}

	public Page<Member> findPage(Tenant tenant,String keyword ,Pageable pageable) {
		return memberDao.findPage(tenant,keyword, pageable);
	}

	public Page<Member> findPageMyMember(Member member, Pageable pageable) {
		return memberDao.findPageMyMember(member, pageable);
	}

	public Member findByTel(String mobile) {

		return memberDao.findByTel(mobile);
	}

	public Member findByEmail(String email) {
		return memberDao.findByEmail(email);
	}
	
	/**
	 * 创建会员
	 */
	public Member createAndBind(String openId,Area area,Member ext, String ipAddr) {
        Setting setting = SettingUtils.get();
        Member member =  this.findByUsername(openId);
		if (member == null) {
			member = EntitySupport.createInitMember();
		}
        member.setArea(area);
        member.setUsername(openId);
        member.setPassword(DigestUtils.md5Hex(openId));
        member.setPoint(setting.getRegisterPoint());
        member.setAmount(new BigDecimal(0));
        member.setBalance(new BigDecimal(0));
        member.setIsEnabled(true);
        member.setIsLocked(Member.LockType.none);
        member.setLoginFailureCount(0);
        member.setLockedDate(null);
        member.setRegisterIp(ipAddr);
        member.setLoginIp(ipAddr);
        member.setLoginDate(new Date());
        member.setSafeKey(null);
        member.setBindEmail(Member.BindStatus.none);
        member.setBindMobile(Member.BindStatus.none);
        member.setPaymentPassword(null);
        member.setRebateAmount(new BigDecimal(0));
        member.setProfitAmount(new BigDecimal(0));
        member.setMemberRank(memberRankDao.findDefault());
        member.setFavoriteProducts(null);
        member.setFreezeBalance(new BigDecimal(0));
        member.setPrivilege(0);
        member.setTotalScore((long) 0);
        member.setMobile(null);
        member.setEmail(null);
        member.setBindMobile(Member.BindStatus.none);
        if (ext!=null) {
                member.setMember(ext);
                member.setShareOwner(ext.getTenant());
        }
        member.setLoginIp(ipAddr);
        member.setLoginDate(new Date());
        member.setLoginFailureCount(0);
        memberDao.persist(member);
        
        BindUser user = bindUserDao.findByUsername(openId, Type._wx);

        if(user==null){
            user = new BindUser();
            user.setUsername(openId);
            user.setPassword(member.getPassword());
            user.setMember(member);
            user.setType(Type._wx);
            bindUserDao.persist(user);
        }else {
            user.setUsername(openId);
            user.setPassword(member.getPassword());
            user.setMember(member);
            user.setType(Type._wx);
            bindUserDao.merge(user);
        }
        return member;
	}


	@Transactional
	public void upgrade(Member member) {
		Tenant tenant = EntitySupport.createInitTenant();
		tenant.setMember(member);
		tenant.setCode(member.getUsername());
		tenant.setName((member.getName() == null ? member.getUsername() : member.getName()) + "的店铺");
		tenant.setShortName(member.getUsername());
		tenant.setTenantType(TenantType.tenant);
		tenant.setTenantCategory(tenantCategoryService.findFirst());
		tenant.setScore(0f);
		tenant.setTotalScore(0L);
		tenant.setScoreCount(0L);
		tenant.setHits(0L);
		tenant.setWeekHits(0L);
		tenant.setMonthHits(0L);
		tenant.setArea(member.getArea());
		tenantDao.persist(tenant);
		member.setTenant(tenant);
		memberDao.persist(member);
	}

	@Transactional(readOnly = true)
	public boolean emailExistsWithoutUser(String email, Member member) {
		return memberDao.emailExistsWithoutUser(email, member);
	}

	@Transactional(readOnly = true)
	public boolean mobileExistsWithoutUser(String mobile, Member member) {
		return memberDao.mobileExistsWithoutUser(mobile, member);
	}

	public Member findByBindTel(String mobile) {

		return memberDao.findByBindTel(mobile);
	}

	public Member findByBindEmail(String email) {
		return memberDao.findByBindEmail(email);
	}

	@Transactional(readOnly = true)
	public Page<Member> findFavoritePage(Member member, Pageable pageable) {
		return memberDao.findFavoritePage(member, pageable);
	}
	@Transactional(readOnly = true)
	public List<Member> findFavoriteList(Member member) {
		return memberDao.findFavoriteList(member);
	}
	@Transactional(readOnly = true)
	public Member findByWechatId(String wechatId) {
		return memberDao.findByWechatId(wechatId);
	}

	@Transactional(readOnly = true)
	public Page<Member> findPage(AuthStatus authStatus, Pageable pageable) {
		return memberDao.findRealnameMemberPage(authStatus, pageable);
	}
	
	/**  我发展会员的累计销售
	 */
	public BigDecimal sumExtAmount(Member member) {
		return memberDao.sumExtAmount(member);
	}
	
	/**  我发展会员的累计返利
	 */
	public BigDecimal sumExtProfit(Member member) {
		return memberDao.sumExtProfit(member);
	}

	@Override
	public Page<Member> findPage(Admin admin, Pageable pageable) {
		Enterprise enterprise=admin.getEnterprise();
		List<Member> members=new ArrayList<Member>();
		Page<Member> page=new Page<Member>(members, 0, pageable);
		if(enterprise!=null){
			EnterpriseType enterprisetype=enterprise.getEnterprisetype();
			
			Area area=enterprise.getArea();
			List<Area> areaList=new ArrayList<Area>();
			areaList.add(area);
			List<Area> list=findAllChildren(area, areaList);
			/*分类查找*/
			if(enterprisetype==EnterpriseType.proxy){
				page= super.findPage(pageable);
			}else if(enterprisetype==EnterpriseType.provinceproxy){
				pageable.getFilters().add(new Filter("area", Operator.in, list));
				page= super.findPage(pageable);
			}else if(enterprisetype==EnterpriseType.cityproxy){
				pageable.getFilters().add(new Filter("area", Operator.in, list));
				page= super.findPage(pageable);
			}else if(enterprisetype==EnterpriseType.countyproxy){
				pageable.getFilters().add(new Filter("area", Operator.in, list));
				page= super.findPage(pageable);
			}else if(enterprisetype==EnterpriseType.personproxy){
				List<Tenant> tenants=new ArrayList<Tenant>(enterprise.getTenants());
				pageable.getFilters().add(new Filter("tenant", Operator.in, tenants));
				page= super.findPage(pageable);
			}else{
				return page;
			}
		}else{
			if(admin.getUsername().equals("admin")){
				page= super.findPage(pageable);
			}else{
				return page;
			}
		}
		return page;
	}
	
	@Override
	public Page<Member> findPage(Date beginDate, Date endDate, Pageable pageable) {
		return memberDao.findPage(beginDate,endDate,pageable);
	}

	/**区域代理查找下属所有区域*/
	private List<Area> findAllChildren(Area area,List<Area> areaList){
		if(area!=null){
			List<Area> children=new ArrayList<Area>(area.getChildren());;
			if(children!=null&&children.size()>0){
				for (Area area2 : children) {
					areaList.add(area2);
					findAllChildren(area2,areaList);
				}
			}
		}
		return areaList;
	}

	@Override
	public List<Member> findByCondition(Long areaId, String userName) {
		if(areaId == null && (userName == null || "".equals(userName))) {
			return memberDao.findByCondition(new ArrayList<Area>(), userName);
		}
		List<Area> areas = new ArrayList<Area>();
		if (areaId != null) {
			Area area = areaDao.find(areaId);
			areas = areaDao.findChildren(area, null);
			areas.add(area);
		}
		if(areaId != null && userName != null && !"".equals(userName)) {
			return memberDao.findByCondition(areas, userName);
		} else if (userName == null || "".equals(userName)) {
			return memberDao.findByArea(areas);
		} else if (areaId == null) {
			return memberDao.findByLikeUserName(userName);
		}
		return new ArrayList<Member>();
	}

	public List<Member> findFans(Member member){
		return memberDao.findFans(member);
	}
	public Page<Member> findFanPage(Member member,Pageable pageable){
		return memberDao.findFanPage(member,pageable);
	}
	public Long findGuiderStar(Tenant tenant){
		return memberDao.findGuiderStar(tenant);
	}

	@Override
	public Page<Member> findByAddPage(Member member, Date beginDate, Date endDate, Pageable pageable) {
		return memberDao.findByAddPage(member,beginDate,endDate,pageable);
	}

	@Override
	public List<Member> findByAddList(Member member, Date beginDate, Date endDate) {
		return memberDao.findByAddList(member,beginDate,endDate);
	}

	public List<Member> memberListExport(Date beginDate, Date endDate,String keywords) {
		return memberDao.memberListExport(beginDate,endDate,keywords);
	}
}