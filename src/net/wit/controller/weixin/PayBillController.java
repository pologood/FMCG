package net.wit.controller.weixin;

import net.wit.controller.weixin.model.DataBlock;
import net.wit.controller.weixin.model.CouponModel;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 买单立减活动页
 * Created by Administrator on 2016/9/6.
 */
@Controller("weixinPayBillController")
@RequestMapping("/weixin/pay/bill")
public class PayBillController extends BaseController {

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "deliveryCenterServiceImpl")
    private DeliveryCenterService deliveryCenterService;

    @Resource(name = "activityPlanningServiceImpl")
    private ActivityPlanningService activityPlanningService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "couponCodeServiceImpl")
    private CouponCodeService couponCodeService;

    @Resource(name = "payBillServiceImpl")
    private PayBillService payBillService;

    @Resource(name = "promotionServiceImpl")
    private PromotionService promotionService;

    /**
     * 根据用户输入的金额，获取平台立减和店铺优惠券
     */
    @RequestMapping(value = "/get/amount")
    public
    @ResponseBody
    DataBlock getAmount(Long id, BigDecimal amount,@RequestParam(defaultValue = "0")BigDecimal noAmount,HttpServletRequest request) {
        Member member = memberService.getCurrent();
        Tenant tenant = tenantService.find(id);
        if (tenant == null) {
            return DataBlock.error(DataBlock.TENANT_INVAILD);
        }

        Promotion promotion = promotionService.getNowPromotion(Promotion.Type.discount,tenant);//买单折扣活动
        ActivityPlanning activityPlanning = activityPlanningService.getCurrent(tenant, ActivityPlanning.Type.random);//随机减活动
        BigDecimal _amout = amount.subtract(noAmount);      //参与活动的金额
        String promotionName="";                            //买单折扣名称
        BigDecimal promotionAmount = BigDecimal.ZERO;       //折扣金额
        String promotionDate="";                            //折扣活动有效期
        BigDecimal activityCouponAmount = BigDecimal.ZERO;  //平台活动优惠券金额
        CouponModel couponModel = null;                     //自动区配到的商家优惠券
        PayBill payBill = new PayBill();
        if(promotion!=null){
            //买单折扣
            promotionName = promotion.getName();
            BigDecimal zkDiscount = _amout.multiply(promotion.getAgioRate().divide(BigDecimal.valueOf(100)));
            promotionAmount = _amout.subtract(_amout.multiply(promotion.getAgioRate().divide(BigDecimal.valueOf(100))));
            if (promotion.getBeginDate() != null && promotion.getEndDate() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                promotionDate = sdf.format(promotion.getBeginDate()) + "-" + sdf.format(promotion.getEndDate());
            }
            payBill.setBackDiscount(zkDiscount.multiply(promotion.getBackRate().divide(BigDecimal.valueOf(100))));
            payBill.setType(PayBill.Type.promotion);

        }else{
            //区配店铺优惠券
            List<Coupon> coupons = new ArrayList<>();
            for (Coupon coupon : tenant.getCoupons()) {
                if (coupon.getExpired()&&coupon.getType().equals(Coupon.Type.tenantCoupon)) {
                    if(member!=null){

                        if (coupon.getReceiveTimes() != 0) {
                            Long useCount = couponCodeService.count(coupon, member, null, null, true);
                            if (useCount.compareTo(coupon.getReceiveTimes())<0) {
                                coupons.add(coupon);
                            }
                        }
                        else {
                            coupons.add(coupon);
                        }

                    } else {
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
                if ((amount.subtract(noAmount)).compareTo(coupon.getAmount()) >= 0 && (amount.subtract(noAmount)).compareTo(coupon.getMinimumPrice()) >= 0) {
                    promotionAmount = coupon.getAmount();
                    couponModel = new CouponModel();
                    couponModel.copyFrom(coupon,null);
                    payBill.setType(PayBill.Type.coupon);
                    break;
                }
            }
        }

        HttpSession session = request.getSession();
        CouponCode couponCode = (CouponCode) session.getAttribute("couponCode");
        if (couponCode!=null) {
            if (couponCode.getIsUsed() || !couponCode.isLocked(_amout)) {
                couponCode = null;
                session.removeAttribute("couponCode");
            }
        }

        if(couponCode!=null){
            activityCouponAmount = couponCode.getCoupon().getAmount();
        }else {
            if (activityPlanning != null) {
                if (member != null) {
                    if (payBillService.isLimit(member, activityPlanning.getActivityMaximumOrders())) {
                        couponCode = activityPlanningService.lockCoupon(activityPlanning, amount.subtract(noAmount));
                        if (couponCode != null) {
                            activityCouponAmount = couponCode.getCoupon().getAmount();
                        }
                    }
                }
            }
        }
        if (couponCode!=null) {
            couponCode.locked(member);
            couponCodeService.update(couponCode);
            session.setAttribute("couponCode", couponCode);
        }

        if(tenant.getIsUnion()){
            couponCode = couponCodeService.findMemberCouponCode(member);
        }
        payBill.setAmount(amount);
        payBill.setNoAmount(noAmount);
        payBill.setTenantDiscount(promotionAmount);
        payBill.setDiscount(activityCouponAmount);
        payBill.setStatus(PayBill.Status.none);
        payBill.setCouponCode(couponCode);
        payBill.setMember(member);
        payBill.setTenant(tenantService.find(id));
        payBill.setBrokerage(BigDecimal.ZERO);
        if (payBill.getCouponCode()!=null && payBill.getCouponCode().getCoupon().getType().equals(Coupon.Type.multipleCoupon)) {
            payBill.setBrokerage(payBill.calcBrokerage());
            payBill.setDiscount(payBill.calcDiscount(payBill.getCouponCode().getBalance()));
        }

        Map<String, Object> map = new HashMap<>();
        map.put("decimal", payBill.getEffectiveAmount());
        map.put("hasPromotion",promotion!=null);
        map.put("promotionName",promotionName);
        map.put("promotionAmount",promotionAmount);
        map.put("promotionDate",promotionDate);
        map.put("coupon", couponModel);
        map.put("platformDiscount", payBill.getDiscount());
        return DataBlock.success(map, "执行成功");


    }

    /**
     * 根据经纬度获取实体店地址
     */
    @RequestMapping(value = "/deliver/center", method = RequestMethod.GET)
    public
    @ResponseBody
    DataBlock getDeliverCenter(Long id, Location location) {
        Tenant tenant = tenantService.find(id);
        if (tenant == null) {
            return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
        DeliveryCenter deliveryCenter = deliveryCenterService.findByLocation(tenant, location);
        if (deliveryCenter == null) {
            return DataBlock.error("没有找到你所在位置的店铺");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id", deliveryCenter.getId());
        map.put("name",deliveryCenter.getName());
        map.put("address", deliveryCenter.getAddress());
        String tenantThumbnail=deliveryCenter.getTenant().getThumbnail();
        if(tenantThumbnail==null){
            tenantThumbnail=deliveryCenter.getTenant().getLogo();
        }
        map.put("tenantThumbnail",tenantThumbnail);
        return DataBlock.success(map, "执行成功");
    }
}
