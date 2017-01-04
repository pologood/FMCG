package net.wit.controller.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.entity.Area;
import net.wit.entity.Authenticode;
import net.wit.entity.BindUser;
import net.wit.entity.BindUser.Type;
import net.wit.entity.Location;
import net.wit.entity.Member;
import net.wit.entity.Review;
import net.wit.entity.Tag;
import net.wit.entity.Tenant;
import net.wit.service.AreaService;
import net.wit.service.AuthenticodeService;
import net.wit.service.BindUserService;
import net.wit.service.MemberService;
import net.wit.service.ReviewService;
import net.wit.service.TagService;
import net.wit.service.TenantService;
import net.wit.support.EntitySupport;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - V-Box API
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("apiVBoxController")
@RequestMapping("/api/vbox")
public class VBoxController {

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "bindUserServiceImpl")
	private BindUserService bindUserService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	@Resource(name = "tagServiceImpl")
	private TagService tagService;
	@Resource(name = "reviewServiceImpl")
	private ReviewService reviewService;
	@Resource(name = "authenticodeServiceImpl")
	private AuthenticodeService authenticodeService;
	
	@Value("${vbox.dspServer}")
	private String dspServer;
	
	/**
	 * 通过Code查找安装商
	 */
	@RequestMapping(value = "/getTenantByCode",method = RequestMethod.GET)
	public @ResponseBody Tenant findTenant(String code){
		Tenant tenant = null;
		if (StringUtils.isNotEmpty(code)) {
			tenant = tenantService.findByCode(code);
		}else{
			tenant = EntitySupport.createInitTenant();
		}
		return tenant;
	}
	
	/**
	 * 通过ID查找安装商
	 */
	@RequestMapping(value = "/getTenant",method = RequestMethod.GET)
	public @ResponseBody Tenant findTenantById(Long id){
		Tenant tenant = null;
		if (id != null && id != 0) {
			tenant = tenantService.find(id);
		}else{
			tenant = EntitySupport.createInitTenant();
		}
		return tenant;
	}
	
	/**
	 * 通过名称和区域获取安装商列表
	 */
	@RequestMapping(value = "/getTenantList",method = RequestMethod.GET)
	public @ResponseBody List<Tenant> findTenantList(String name, Long id, Integer count){
		List<Tenant> tenantList = new ArrayList<Tenant>();
		if(id != null && id != 0){
			Area area = new Area();
			area = areaService.find(id);
			Tag tag = new Tag();
			tag = tagService.find((long) 6);
			if(area != null && area.getId() != 0){
				tenantList = tenantService.findList(area, name, tag, count);
			}
		}
		return tenantList;
	}
	
	/**
	 * 获取安装商列表
	 */
	@RequestMapping(value = "/getListToLocation",method = RequestMethod.GET)
	public @ResponseBody List<Tenant> findTenantList(String areaCode, String location_x, String location_y, Integer count){
		List<Tenant> tenantList = new ArrayList<Tenant>();
		if (StringUtils.isNotEmpty(location_x) && StringUtils.isNotEmpty(location_y)) {
			BigDecimal x = new BigDecimal(location_x);
			BigDecimal y = new BigDecimal(location_y);
			Location location = new Location(x,y);
			BigDecimal distatce = new BigDecimal(5000.00);
			Area area = null;
			if(StringUtils.isNotEmpty(areaCode)){
				area = areaService.findByCode(areaCode);
			}
			Tag tag = new Tag();
			tag = tagService.find((long) 6);
			if(count == null){
				count = 99;
			}
		}
		return tenantList;
	}
	
	/**
	 * 获取区域列表
	 */
	@RequestMapping(value = "/getAreaList",method = RequestMethod.GET)
	public @ResponseBody List<Area> findAreaList(Long id, Integer count){
		List<Area> areaList = new ArrayList<Area>();
		if(id == null){
			areaList = areaService.findRoots();
		}else{
			Area area = areaService.find(id);
			Set<Area> set = area.getChildren();
			areaList.addAll(set);
		}
		return areaList;
	}
	
	/**
	 * 校验注册激活码
	 */
	@RequestMapping(value = "/validation",method = RequestMethod.POST)
	public @ResponseBody 
	Message validation(String username, String equipId, String verification){
		
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(equipId) 
				|| StringUtils.isEmpty(verification)) {
			return Message.error("shop.common.invalid");
		}
		
		if(!memberService.usernameExists(username)){
			return Message.error("用户名不存在！");
		}
		
		BindUser user = new BindUser();
		Member member = memberService.findByUsername(username);
		//通过verification验证码获取订单
		Authenticode auth = authenticodeService.findBySn(verification);
		
		if(auth != null){
			
			user.setUsername(equipId);
			user.setPassword(member.getPassword());
			user.setType(Type._vbox);
			user.setMember(member);
			
			auth.setMac(equipId);
			
			memberService.update(member);
			bindUserService.save(user);
			authenticodeService.update(auth);
				
		}else{
			return Message.error("验证码错误！");
		}
		return Message.success("验证成功！");
	}
	
	/**
	 * 修改密码
	 */
	@RequestMapping(value = "/updatePass",method = RequestMethod.POST)
	public @ResponseBody Message updatePass(String username, String oldPass, String newPass){

		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(oldPass) || StringUtils.isEmpty(newPass)) {
			return Message.error("shop.common.invalid");
		}
		
		Member member = null;
		member= memberService.findByUsername(username);
		
		if (member != null && DigestUtils.md5Hex(oldPass).equals(member.getPassword())) {
			member.setPassword(DigestUtils.md5Hex(newPass));
			
			memberService.update(member);
			return Message.success("修改成功");
					
		}else{
			return Message.error("用户名不存在");
		}
	}
	
	/**
	 * 找回密码
	 */
	@RequestMapping(value = "/retrievePass",method = RequestMethod.POST)
	public @ResponseBody Message retrievePass(String username, String newPass){

		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(newPass)) {
			return Message.error("shop.common.invalid");
		}
		
		Member member = null;
		member= memberService.findByUsername(username);
		
		if (member != null) {
			memberService.update(member);
			return Message.success("修改成功");
		}else{
			return Message.error("用户名不存在");
		}
		
	}
	
	/**
	 * 验收
	 */
	@RequestMapping(value = "/accept",method = RequestMethod.GET)
	public @ResponseBody Message accept(Long id, String username, String verification, Integer score){
		
		Tenant tenant = tenantService.find(id);
		Member member = memberService.findByUsername(username);
		
		if(score != null && StringUtils.isNotEmpty(verification)){
			Authenticode auth = authenticodeService.findBySn(verification);
			if(auth != null && (auth.getStatus().equals(Authenticode.Status.install) 
					|| auth.getStatus().equals(Authenticode.Status.accepted))){
				Review review = new Review();
				review.setScore(score);
				review.setFlag(Review.Flag.tenant);
				review.setTenant(tenant);
				review.setMember(member);
				review.setContent("验收成功");
				review.setIp("0.0.0.0");
				review.setIsShow(true);
				review.setProduct(auth.getOrderItem().getProduct());
				reviewService.save(review);
				auth.setStatus(Authenticode.Status.finish);
				authenticodeService.update(auth);
			}else{
				return Message.error("验收失败！");
			}
			return Message.success("验收成功！");
		}else{
			return Message.error("验收失败！");
		}
	}
	
	/**
	 * 查找订单信息
	 */
	@RequestMapping(value = "/getAuth",method = RequestMethod.GET)
	public @ResponseBody Authenticode findAuth(String verification){
		Authenticode auth = new Authenticode();
		if(StringUtils.isNotEmpty(verification)){
			auth = authenticodeService.findBySn(verification);
		}
		return auth;
	}
	
	/**
	 * 委托安装
	 */
	@RequestMapping(value = "/entrust",method = RequestMethod.GET)
	public @ResponseBody Message entrust(String verification, Long id){
		Authenticode auth = new Authenticode();
		if(StringUtils.isNotEmpty(verification) && id != null && id != 0){
			auth = authenticodeService.findBySn(verification);
			Tenant tenant = tenantService.find(id);
			if((auth.getStatus().equals(Authenticode.Status.unshipped) || auth.getStatus().equals(Authenticode.Status.shipped)) 
					&& tenant != null){
				auth.setTenant(tenant);
				authenticodeService.update(auth);
				return Message.success("委托成功！");
			}else{
				return Message.error("委托失败！");
			}
		}else{
			return Message.error("委托失败！");
		}
	}
	
	/**
	 * 取消委托
	 */
	@RequestMapping(value = "/cancelAuth",method = RequestMethod.GET)
	public @ResponseBody Message cancelAuth(String verification){
		Authenticode auth = new Authenticode();
		if(StringUtils.isNotEmpty(verification)){
			auth = authenticodeService.findBySn(verification);
			if((auth.getStatus().equals(Authenticode.Status.unshipped) || auth.getStatus().equals(Authenticode.Status.shipped)) 
					&& auth.getTenant() != null){
				auth.setTenant(null);
				authenticodeService.update(auth);
				return Message.success("取消委托成功！");
			}else{
				return Message.error("取消委托失败！");
			}
		}else{
			return Message.error("验证码不能为空！");
		}
	}
	
	/**
	 * 获取安装商介绍
	 */
	@RequestMapping(value = "/getIntr",method = RequestMethod.GET)
	public @ResponseBody Message getIntr(Long id){
		if (id != null && id != 0) {
			Tenant tenant = tenantService.find(id);
			if(tenant.getIntroduction() != null){
				return Message.success(tenant.getIntroduction());
			}else{
				return Message.success("");
			}
		}else{
			return Message.error("ID不能为空！");
		}
	}
}
