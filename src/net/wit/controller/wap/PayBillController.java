package net.wit.controller.wap;

import net.wit.controller.app.model.DataBlock;
import net.wit.controller.wap.model.CouponModel;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.SettingUtils;
import net.wit.weixin.main.MenuManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2016/9/6.
 */
@Controller("wapPayBillController")
@RequestMapping("/wap/pay/bill")
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

    @Resource(name = "orderServiceImpl")
    private OrderService orderService;

    @Resource(name = "promotionServiceImpl")
    private PromotionService promotionService;

    @Resource(name = "couponServiceImpl")
    private CouponService couponService;

    /**
     * 立减购买
     */
    @RequestMapping(value = "/buyreduce/{id}", method = RequestMethod.GET)
    public String buyreduce(@PathVariable Long id, BigDecimal amount, @RequestParam(defaultValue = "0") BigDecimal noAmount, ModelMap model) {
        String isActivityTenant = "hidden", isCoupon = "hidden", isBillDiscount = "hidden",activityName="";
        Tenant tenant = tenantService.find(id);
        if (tenant == null) {
            return "/wap/index";
        }

        Promotion promotion = promotionService.getNowPromotion(Promotion.Type.discount, tenant);
        if (promotion != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
            isBillDiscount = "";
            BigDecimal promotionAmount = BigDecimal.ZERO;
            if (amount != null) {
                promotionAmount = amount.subtract(noAmount).multiply(promotion.getAgioRate().divide(BigDecimal.valueOf(100))).add(noAmount);
            }

            model.addAttribute("promotionName", promotion.getName());
            model.addAttribute("promotionAmount", promotionAmount);
            model.addAttribute("promotionTime", sdf.format(promotion.getBeginDate()) + "-" + sdf.format(promotion.getEndDate()));
        } else {
            Set<Coupon> coupons = new HashSet<>();
            for (Coupon coupon : tenant.getCoupons()) {
                if (coupon.getExpired()&&coupon.getType().equals(Coupon.Type.tenantCoupon)) {
                    coupons.add(coupon);
                }
            }
            if (coupons.size() > 0) {
                isCoupon = "";
            }

            model.addAttribute("coupons", CouponModel.bindData(coupons));
        }

        ActivityPlanning activityPlanning = activityPlanningService.getCurrent(tenant, ActivityPlanning.Type.random);
        Set<Coupon> activityCoupons = new HashSet<>();
        if (activityPlanning != null) {
            activityCoupons = activityPlanning.getCoupons();
            isActivityTenant = "";
            activityName = "随机立减";
        }

        if(tenant.getIsUnion()){
            isActivityTenant = "";
            activityName = "平台优惠";
        }
        model.addAttribute("activityName", activityName);
        model.addAttribute("activityCoupons", CouponModel.bindData(activityCoupons));
        model.addAttribute("member", memberService.getCurrent());
        model.addAttribute("amount", amount);
        model.addAttribute("noAmount", noAmount);
        model.addAttribute("tenant", tenant);
        model.addAttribute("id", id);
        model.addAttribute("isActivityTenant", isActivityTenant);
        model.addAttribute("isCoupon", isCoupon);
        model.addAttribute("isBillDiscount", isBillDiscount);
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/tenant/buyreduce/" + id + ".jhtml");
        model.addAttribute("title", tenant.getName());
        model.addAttribute("desc", "买单立减，这里有您想象不到的优惠活动！！！");
        model.addAttribute("link", url);
        model.addAttribute("imgUrl", tenant.getThumbnail());
        model.addAttribute("tscpath", "/wap/tenant/buyreduceRN");
        return "/wap/tenant/buyreduceRN";
    }

    /**
     * 根据经纬度获取实体店地址
     */
    @RequestMapping(value = "/deliver/certer", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock getDeliverCenter(Long id, String lat, String lng) {
        Tenant tenant = tenantService.find(id);
        if (tenant == null) {
            return DataBlock.error("店铺编号不对");
        }
        Member member = memberService.getCurrent();
        if (lat != null && lng != null) {
            Location lcn = new Location();
            lcn.setLat(new BigDecimal(lng));
            lcn.setLng(new BigDecimal(lat));

            if (member != null) {
                member.setLocation(lcn);
                member.setLbsDate(new Date());
                memberService.update(member);
            }

            DeliveryCenter deliveryCenter = deliveryCenterService.findByLocation(tenant, lcn);
            if (deliveryCenter == null) {
                return DataBlock.error("没有找到你所在位置的店铺");
            }

            Map<String, Object> map = new HashMap<>();
            map.put("id", deliveryCenter.getId());
            map.put("address", deliveryCenter.getAddress());
            return DataBlock.success(map, "成功");
        }
        return DataBlock.warn("没有获取到经纬度！");
    }

    /**
     * 根据用户输入的金额，获取平台立减和店铺优惠券
     */
    @RequestMapping(value = "/get/amount", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock getAmount(Long id, BigDecimal amount, @RequestParam(defaultValue = "0") BigDecimal noAmount,HttpServletRequest request) {
        Tenant tenant = tenantService.find(id);
        if (tenant == null) {
            return DataBlock.error("店铺编号不对");
        }

        Member member = memberService.getCurrent();

        // TODO:  商家买单折扣
        Promotion promotions = promotionService.getNowPromotion(Promotion.Type.discount, tenant);
        String couponTime = "-", promotionTime = "-";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String couponName = "您购买的东西暂时不满足优惠券使用范围", promotionName = "";
        Map<String, Object> map = new HashMap<>();

        BigDecimal _amout = amount.subtract(noAmount);       //参与活动的金额

        BigDecimal activityCouponAmount = BigDecimal.ZERO;  //平台活动优惠券金额
        BigDecimal activityMinimumPrice = BigDecimal.ZERO;  //平台活动优惠券最低消费金额

        BigDecimal couponMinimumPrice = BigDecimal.ZERO;    //商家优惠券最低消费金额
        BigDecimal tenantDiscount = BigDecimal.ZERO;          //商家优惠券金额
        PayBill payBill = new PayBill();
        if (promotions != null) {
            BigDecimal zkDiscount = _amout.multiply(promotions.getAgioRate().divide(BigDecimal.valueOf(100)));
            tenantDiscount = _amout.subtract(_amout.multiply(promotions.getAgioRate().divide(BigDecimal.valueOf(100))));
            payBill.setBackDiscount(zkDiscount.multiply(promotions.getBackRate().divide(BigDecimal.valueOf(100))));
            promotionTime = sdf.format(promotions.getBeginDate()) + "-" + sdf.format(promotions.getEndDate());
            promotionName = promotions.getName();
            payBill.setType(PayBill.Type.promotion);
        } else {
            List<Coupon> coupons = new ArrayList<>();
            for (Coupon coupon : tenant.getCoupons()) {
                if (coupon.getExpired()&&coupon.getType().equals(Coupon.Type.tenantCoupon)) {
                    coupons.add(coupon);
                }
            }
            Set<Coupon> cloneCoupon = tenant.getCoupons();
            if (member != null) {
                for (Coupon coupon : cloneCoupon) {
                    if (!coupon.getReceiveTimes().equals(0L)) {
                        Long useCount = couponCodeService.count(coupon, member, true, false, true);

                        Long unUseCount = couponCodeService.count(coupon, member, true, false, false);
                        if (useCount.equals(coupon.getReceiveTimes())) {
                            coupons.remove(coupon);
                        }

                        if (unUseCount > 0) {
                            coupons.add(coupon);
                        }
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
                if ((_amout.compareTo(coupon.getAmount()) >= 0 && _amout.compareTo(coupon.getMinimumPrice()) >= 0)) {
                    tenantDiscount = coupon.getAmount();
                    couponName = coupon.getName();
                    couponTime = sdf.format(coupon.getStartDate()) + "-" + sdf.format(coupon.getEndDate());
                    couponMinimumPrice = coupon.getMinimumPrice();
                    payBill.setType(PayBill.Type.coupon);
                    break;
                }
            }
        }

    	HttpSession session = request.getSession();
        CouponCode couponCode = (CouponCode) session.getAttribute("couponCode");
        if (couponCode!=null) {
        	if ((couponCode.getIsUsed()!=null&&couponCode.getIsUsed()) || !couponCode.isLocked(_amout)) {
        	    couponCode = null;
        	    session.removeAttribute("couponCode");
        	}
        }

        if(couponCode!=null){
            activityCouponAmount = couponCode.getCoupon().getAmount();
            activityMinimumPrice = couponCode.getCoupon().getMinimumPrice();
        }else {
            ActivityPlanning activityPlanning = activityPlanningService.getCurrent(tenant, ActivityPlanning.Type.random);
            if (activityPlanning != null) {
                if (member != null) {
                    if (payBillService.isLimit(member, activityPlanning.getActivityMaximumOrders()) && orderService.isLimit(member, activityPlanning.getActivityMaximumOrders())) {
                        couponCode = activityPlanningService.lockCoupon(activityPlanning, _amout);
                        if (couponCode != null) {
                            activityCouponAmount = couponCode.getCoupon().getAmount();
                            activityMinimumPrice = couponCode.getCoupon().getMinimumPrice();
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
            //Coupon coupon = couponService.find(1L);
            //List<CouponCode> couponCodes=couponCodeService.findCoupon(member,coupon);
            //if(couponCodes!=null&&couponCodes.size()>0){
                couponCode = couponCodeService.findMemberCouponCode(member);
            //}
        }

        payBill.setAmount(amount);
        payBill.setNoAmount(noAmount);
        payBill.setTenantDiscount(tenantDiscount);
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
        map.put("type",payBill.getType());
        map.put("activityName", activityMinimumPrice);
        map.put("promotionName", promotionName);
        map.put("promotionAmount", payBill.getTenantDiscount());
        map.put("promotionTime", promotionTime);
        map.put("activityCouponAmount", payBill.getDiscount());
        map.put("couponAmount", payBill.getTenantDiscount());
        map.put("couponName", couponName);
        map.put("couponTime", couponTime);
        map.put("amount", payBill.getEffectiveAmount());
        map.put("couponMinimumPrice", couponMinimumPrice);
        return DataBlock.success(map, "成功");
    }

}
