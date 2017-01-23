package net.wit.controller.weixin.member;

import net.wit.controller.weixin.model.DataBlock;
import net.wit.controller.weixin.BaseController;
import net.wit.entity.*;
import net.wit.entity.PayBill.Type;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

/**
 * 买单立减
 * Created by Administrator on 2016/9/6.
 */
@Controller("weixinMemberPayBillController")
@RequestMapping("/weixin/member/pay/bill")
public class PayBillController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "payBillServiceImpl")
    private PayBillService payBillService;

    @Resource(name = "snServiceImpl")
    private SnService snService;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "couponServiceImpl")
    private CouponService couponService;

    @Resource(name = "couponCodeServiceImpl")
    private CouponCodeService couponCodeService;

    @Resource(name = "deliveryCenterServiceImpl")
    private DeliveryCenterService deliveryCenterService;

    @Resource(name = "activityPlanningServiceImpl")
    private ActivityPlanningService activityPlanningService;

    @Resource(name = "promotionServiceImpl")
    private PromotionService promotionService;

    @Resource(name = "orderServiceImpl")
    private OrderService orderService;

    /**
     * 买单立减订单提交
     */
    @RequestMapping(value = "/get/amount", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock getAmount(Long id, BigDecimal amount, @RequestParam(defaultValue = "0")BigDecimal noAmount, Long deliveryCenterId,HttpServletRequest request) {
        PayBill payBill = new PayBill();
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error("无效用户");
        }

        Tenant tenant = tenantService.find(id);
        if (tenant == null) {
            return DataBlock.error("无效店铺");
        }

        if(tenant.getEnd()!=null&&tenant.getEnd()){
            return DataBlock.error("商家已打烊");
        }

        BigDecimal _noAmount = amount.subtract(noAmount);   //参与活动的金额

        BigDecimal tenantDiscount = BigDecimal.ZERO;
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal backDiscount = BigDecimal.ZERO;
        Coupon tenantCoupon = null;
        Coupon activityCoupon = null;
        CouponCode couponCode = null;
        CouponCode activityCouponCode = null;
        Promotion promotions = promotionService.getNowPromotion(Promotion.Type.discount, tenant);
        //BigDecimal decimal=amount.subtract(noAmount);
        if (promotions != null) {
            //只有实付金额大于不参与活动金额才能享受折扣返现活动
            if(amount.compareTo(noAmount)>0){
                BigDecimal zkDiscount = _noAmount.multiply(promotions.getAgioRate().divide(BigDecimal.valueOf(100)));
                backDiscount = zkDiscount.multiply(promotions.getBackRate().divide(BigDecimal.valueOf(100)));
                tenantDiscount = _noAmount.subtract(zkDiscount);
                payBill.setType(Type.promotion);
                payBill.setActivityName(promotions.getName());
            }
        } else {
            payBill.setType(Type.coupon);
            List<Coupon> coupons = new ArrayList<>();
            for (Coupon coupon : tenant.getCoupons()) {
                if (coupon.getExpired()&&coupon.getType().equals(Coupon.Type.tenantCoupon)) {
                    if (coupon.getReceiveTimes() != 0) {
                        Long useCount = couponCodeService.count(coupon, member, null, null, true);
                        if (useCount.compareTo(coupon.getReceiveTimes())<0) {
                            coupons.add(coupon);
                        }
                    }else {
                        coupons.add(coupon);
                    }
                }
            }

            Collections.sort(coupons, new Comparator<Coupon>() {
                @Override
                public int compare(Coupon b1, Coupon b2) {
                    return b2.getAmount().compareTo(b1.getAmount());
                }
            });

            for (Coupon coupon : coupons) {
                if ((_noAmount.compareTo(coupon.getAmount()) >= 0 && _noAmount.compareTo(coupon.getMinimumPrice()) >= 0)) {
                    tenantDiscount=coupon.getAmount();
                    tenantCoupon = coupon;
                    break;
                }
            }

            if (tenantCoupon != null) {
                couponCode = couponCodeService.findCouponCodeByCouponAndMember(tenantCoupon, member);
                if (couponCode == null) {
                    couponCode = couponCodeService.build(tenantCoupon, member);
                }
                if (couponCode != null) {
                    tenantCoupon.setSendCount(tenantCoupon.getSendCount() + 1);
                    couponService.update(tenantCoupon);
                }
                tenantDiscount = tenantCoupon.getAmount();
                payBill.setActivityName(tenantCoupon.getName());
            }
        }

        HttpSession session = request.getSession();
        activityCouponCode = (CouponCode) session.getAttribute("couponCode");
        if (activityCouponCode!=null) {
            if (activityCouponCode.getIsUsed() || !activityCouponCode.isLocked(amount)) {
                activityCouponCode = null;
                session.removeAttribute("couponCode");
            }else {
                activityCoupon = activityCouponCode.getCoupon();
                discount = activityCoupon.getAmount();
            }
        }

        if(tenant.getIsUnion()){
            activityCouponCode=couponCodeService.findMemberCouponCode(member);
        }

        payBill.setSn(snService.generate(Sn.Type.paybill));
        payBill.setAmount(amount);
        payBill.setNoAmount(noAmount);
        if (deliveryCenterId != null) {
            payBill.setDeliveryCenter(deliveryCenterService.find(deliveryCenterId));
        } else {
            payBill.setDeliveryCenter(tenant.getDefaultDeliveryCenter());
        }
        payBill.setTenantDiscount(tenantDiscount);
        payBill.setDiscount(discount);
        payBill.setStatus(PayBill.Status.none);
        payBill.setCouponCode(activityCouponCode);
        payBill.setTenantCouponCode(couponCode);
        payBill.setMember(member);
        payBill.setBackDiscount(backDiscount);
        payBill.setTenant(tenantService.find(id));
        payBill.setBrokerage(BigDecimal.ZERO);
        payBill.setGuideBrokerage(BigDecimal.ZERO);
        payBill.setGuideOwnerBrokerage(BigDecimal.ZERO);
        if (payBill.getCouponCode()!=null && payBill.getCouponCode().getCoupon().getType().equals(Coupon.Type.multipleCoupon)) {
            payBill.setBrokerage(payBill.calcBrokerage());
            payBill.setGuideBrokerage(payBill.calcGuideBrokerage());
            payBill.setGuideOwnerBrokerage(payBill.calcGuideOwnerBrokerage());
            payBill.setDiscount(payBill.calcDiscount(payBill.getCouponCode().getBalance()));
        }
        payBillService.save(payBill);
        payBillService.upgrade(payBill);
        Map<String, Object> map = new HashMap<>();
        map.put("amount", payBill.getEffectiveAmount());
        map.put("sn", payBill.getPayment().getSn());
        return DataBlock.success(map, "执行成功");
    }
}
