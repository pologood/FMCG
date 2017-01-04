/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.weixin.member;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.weixin.BaseController;
import net.wit.controller.weixin.model.CouponCodeModel;
import net.wit.controller.weixin.model.DataBlock;
import net.wit.entity.CouponCode;
import net.wit.entity.Member;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Controller - 我的优惠券
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("weixinMemberCouponController")
@RequestMapping("/weixin/member/coupon")
public class CouponController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "couponCodeServiceImpl")
    private CouponCodeService couponCodeService;

    /**
     * 我的优惠券
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list(Pageable pageable) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Page<CouponCode> page = couponCodeService.findPage(member, null, false, pageable);
        return DataBlock.success(CouponCodeModel.bindData(page.getContent()), page, "执行成功");
    }


}