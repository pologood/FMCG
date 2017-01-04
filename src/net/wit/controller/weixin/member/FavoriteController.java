/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.weixin.member;

import net.wit.*;
import net.wit.controller.weixin.BaseController;
import net.wit.controller.weixin.model.*;
import net.wit.entity.*;
import net.wit.entity.Message;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller - 会员中心 - 商品收藏
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("weixinMemberFavoriteController")
@RequestMapping("/weixin/member/favorite")
public class FavoriteController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;
    @Resource(name = "productServiceImpl")
    private ProductService productService;
    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;
    @Resource(name = "memberRankServiceImpl")
    private MemberRankService memberRankService;
    @Resource(name = "consumerServiceImpl")
    private ConsumerService consumerService;
    @Resource(name = "employeeServiceImpl")
    private EmployeeService employeeService;
    @Resource(name = "reviewServiceImpl")
    private ReviewService reviewService;

    /**
     * 我收藏的商品
     */
    @RequestMapping(value = "/product/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list(Pageable pageable) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Page<Product> page = productService.findPage(member, pageable);
        List<ProductListModel> models = new ArrayList<>();
        for (Product product : page.getContent()) {
            ProductListModel model = new ProductListModel();
            model.copyFrom(product);
            Long positiveCount = reviewService.count(null, product, Review.Type.positive, null);
            if (product.getReviews().size() > 0) {
                model.setPositivePercent((positiveCount * 1.0 / product.getReviews().size())*100);
            }
            models.add(model);
        }
        return DataBlock.success(models, page, "执行成功");
    }

    /**
     * 添加收藏商品
     * params id 商品
     */
    @RequestMapping(value = "/product/add", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock add(Long id) {
        Member member = memberService.getCurrent();
        if (member == null) {
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
        return DataBlock.success("success", "执行成功");
    }

    /**
     * 删除收藏的商品
     * params id 商品
     */
    @RequestMapping(value = "/product/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock delete(Long id) {
        Member member = memberService.getCurrent();
        if (member == null) {
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
        return DataBlock.success("success", "执行成功");
    }

    /**
     * 我关注的商家
     * lat lng 经伟度
     */
    @RequestMapping(value = "/tenant/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock tenantList(Pageable pageable, Location location) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Page<Tenant> page = tenantService.findPage(member, pageable);

        return DataBlock.success(FavoriteTenantListModel.bindData(page.getContent(), location), page, "执行成功");
    }

    /**
     * 取消关注商家
     * params id 商家
     */
    @RequestMapping(value = "/tenant/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock tenantDelete(Long id) {
        Member member = memberService.getCurrent();
        if (member == null) {
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
        return DataBlock.success("success", "取消收藏成功");
    }

    /**
     * 添加收藏店铺
     * params id 店铺
     */
    @RequestMapping(value = "/tenant/add", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock addTenant(Long id) {
        Member member = memberService.getCurrent();
        if (member == null) {
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
        consumer.setStatus(Consumer.Status.none);
        consumer.setTenant(tenant);
        consumer.setMemberRank(memberRankService.findDefault());
        consumerService.save(consumer);
        return DataBlock.success(member.getFavoriteTenants().size(), "加入收藏成功");
    }

    /**
     * 我收藏的导购
     */
    @RequestMapping(value = "/guide/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock GuideList(Pageable pageable) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Page<Employee> page = new Page<>();
        if (member.getFavoriteMembers().size() > 0) {
            List<Filter> filters = new ArrayList<>();
            filters.add(new Filter("member", Filter.Operator.in, member.getFavoriteMembers()));
            pageable.setFilters(filters);
            page = employeeService.findPage(pageable);
        }
        return DataBlock.success(FavoriteGuideListModel.bindData(page.getContent()), "执行成功");
    }

    /**
     * 添加收藏导购
     *
     * @param id 员工ID
     */
    @RequestMapping(value = "/guide/add", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock addGuide(Long id) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Employee employee = employeeService.find(id);
        if (employee == null || employee.getMember() == null) {
            return DataBlock.error("该导购不存在");
        }
        if (member.getFavoriteMembers().contains(employee.getMember())) {
            return DataBlock.error("该导购已收藏");
        }
        if (Member.MAX_FAVORITE_COUNT != null && member.getFavoriteProducts().size() >= Member.MAX_FAVORITE_COUNT) {
            return DataBlock.warn("最多允许收藏" + Member.MAX_FAVORITE_COUNT + "个导购");
        }
        member.getFavoriteMembers().add(employee.getMember());
        memberService.update(member);
        return DataBlock.success(member.getFavoriteMembers().size(), "执行成功");
    }

    /**
     * 删除收藏的导购
     * params id 员工ID
     */
    @RequestMapping(value = "/guide/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock guideDelete(Long id) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Employee employee = employeeService.find(id);
        if (employee == null || employee.getMember() == null) {
            return DataBlock.error("该导购不存在");
        }
        if (!member.getFavoriteMembers().contains(employee.getMember())) {
            return DataBlock.error("已经取消收藏了");
        }
        member.getFavoriteMembers().remove(employee.getMember());
        memberService.update(member);
        return DataBlock.success(member.getFavoriteMembers().size(), "执行成功");
    }

}