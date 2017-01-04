/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.app.member;

import javax.annotation.Resource;

import net.wit.Filter;
import net.wit.controller.app.model.*;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.app.BaseController;
import net.wit.entity.Consumer.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller - 会员中心 - 商品收藏
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("appMemberFavoriteController")
@RequestMapping("/app/member/favorite")
public class FavoriteController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;
	
	@Resource(name = "consumerServiceImpl")
	private ConsumerService consumerService;

	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;

	@Resource(name = "employeeServiceImpl")
	private EmployeeService employeeService;

	/**
	 * 添加收藏商品
	 * params id 商品 
	 */
	@RequestMapping(value = "/product/add", method = RequestMethod.POST)
	public @ResponseBody DataBlock add(Long id) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Product product = productService.find(id);
		if (product == null) {
			return DataBlock.error("商品ID无效");
		}
		if (member.getFavoriteProducts().contains(product)) {
			return DataBlock.warn("shop.member.favorite.exist");
		}
		if (Member.MAX_FAVORITE_COUNT != null && member.getFavoriteProducts().size() >= Member.MAX_FAVORITE_COUNT) {
			return DataBlock.warn("shop.member.favorite.addCountNotAllowed", Member.MAX_FAVORITE_COUNT);
		}
		member.getFavoriteProducts().add(product);
		memberService.update(member);
		return DataBlock.success(member.getFavoriteProducts().size(),"执行成功");
	}

	/**
	 * 我收藏的商品
	 */
	@RequestMapping(value = "/product/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(Integer pageNumber) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		Page<Product> page = productService.findPage(member, pageable);
		return DataBlock.success(ProductListModel.bindData(page.getContent()),"执行成功");
	}

	/**
	 * 删除收藏的商品
	 * params id 商品 
	 */
	@RequestMapping(value = "/product/delete", method = RequestMethod.POST)
	public @ResponseBody
	DataBlock delete(Long id) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Product product = productService.find(id);
		if (product == null) {
			return DataBlock.error("商品id无效");
		}
		if (!member.getFavoriteProducts().contains(product)) {
			return DataBlock.error("已经取消关注了");
		}
		member.getFavoriteProducts().remove(product);
		memberService.update(member);
		return DataBlock.success(member.getFavoriteProducts().size(),"执行成功");
	}

	/**
	 * 添加收藏店铺
	 * params id 店铺
	 */
	@RequestMapping(value = "/tenant/add", method = RequestMethod.POST)
	public @ResponseBody DataBlock addTenant(Long id) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Tenant tenant = tenantService.find(id);
		if (tenant == null) {
			return DataBlock.error("商家ID无效");
		}
		if (member.getFavoriteTenants().contains(tenant)) {
			return DataBlock.warn("该店铺已收藏");
		}
		if (Member.MAX_FAVORITE_COUNT != null && member.getFavoriteTenants().size() >= Member.MAX_FAVORITE_COUNT) {
			return DataBlock.warn("shop.member.favorite.addTenantCountNotAllowed", Member.MAX_FAVORITE_COUNT);
		}
		member.getFavoriteTenants().add(tenant);
		memberService.update(member);
	    Consumer consumer = new Consumer();
		consumer.setMember(member);
		consumer.setStatus(Status.none);
		consumer.setTenant(tenant);
		consumer.setMemberRank(memberRankService.findDefault());
		consumerService.save(consumer);
		return DataBlock.success(member.getFavoriteTenants().size(),"加入收藏成功");
	}

	/**
	 * 我关注的商家
	 * lat lng 经伟度
	 */
	@RequestMapping(value = "/tenant/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock tenantList(Location location,Integer pageNumber) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		Page<Tenant> page = tenantService.findPage(member, pageable);
		
		return DataBlock.success(TenantListModel.bindData(page.getContent(),location),"执行成功");
	}

	/**
	 * 取消关注商家
	 * params id 商家
	 */
	@RequestMapping(value = "/tenant/delete", method = RequestMethod.POST)
	public @ResponseBody
	DataBlock tenantDelete(Long id) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Tenant tenant = tenantService.find(id);
		if (tenant == null) {
			return DataBlock.error("商家id无效");
		}
		if (!member.getFavoriteTenants().contains(tenant)) {
			return DataBlock.error("已经取消关注了");
		}
		member.getFavoriteTenants().remove(tenant);
		memberService.update(member);
		return DataBlock.success(member.getFavoriteTenants().size(),"取消收藏成功");
	}

	/**
	 * 添加收藏导购
	 * @param id 员工ID
	 * @return
     */
	@RequestMapping(value = "/guide/add", method = RequestMethod.POST)
	public @ResponseBody DataBlock addGuide(Long id) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Employee employee = employeeService.find(id);
		if(employee==null||employee.getMember()==null){
			return DataBlock.error("该导购不存在");
		}
		if(member.getFavoriteMembers().contains(employee.getMember())){
			return DataBlock.error("该导购已收藏");
		}
		if (Member.MAX_FAVORITE_COUNT != null && member.getFavoriteProducts().size() >= Member.MAX_FAVORITE_COUNT) {
			return DataBlock.warn("最多允许收藏"+Member.MAX_FAVORITE_COUNT+"个导购");
		}
		member.getFavoriteMembers().add(employee.getMember());
		memberService.update(member);
		return DataBlock.success(member.getFavoriteMembers().size(),"执行成功");
	}


	/**
	 * 我收藏的导购
	 * @param pageNumber 页码
	 * @return
	 */
	@RequestMapping(value = "/guide/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock GuideList(Integer pageNumber) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		Page<Employee> page=new Page<>();
		if(member.getFavoriteMembers()!=null&&member.getFavoriteMembers().size()>0){
			List<Filter> filters=new ArrayList<>();
			filters.add(new Filter("member", Filter.Operator.in,member.getFavoriteMembers()));
			pageable.setFilters(filters);
			page= employeeService.findPage(pageable);
		}
		return DataBlock.success(GuideModel.bindData(page.getContent()),"执行成功");
	}

	/**
	 * 删除收藏的导购
	 * params id 员工ID
	 */
	@RequestMapping(value = "/guide/delete", method = RequestMethod.POST)
	public @ResponseBody
	DataBlock guideDelete(Long id) {
		Member member = memberService.getCurrent();
		if (member==null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Employee employee = employeeService.find(id);
		if (employee == null||employee.getMember()==null) {
			return DataBlock.error("该导购不存在");
		}
		if (!member.getFavoriteMembers().contains(employee.getMember())) {
			return DataBlock.error("已经取消收藏了");
		}
		member.getFavoriteMembers().remove(employee.getMember());
		memberService.update(member);
		return DataBlock.success(member.getFavoriteMembers().size(),"执行成功");
	}
	
}