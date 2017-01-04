package net.wit.controller.app;

import net.wit.controller.app.model.DataBlock;
import net.wit.controller.wap.BaseController;
import net.wit.controller.wap.model.CouponModel;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.weixin.main.MenuManager;
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
@Controller("appPayBillController")
@RequestMapping("/app/pay/bill")
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
     * 立减购买
     */
    @RequestMapping(value = "/buyreduce/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    DataBlock buyreduce(@PathVariable Long id) {
        Tenant tenant = tenantService.find(id);
        if (tenant == null) {
            return DataBlock.error(DataBlock.TENANT_INVAILD);
        }

        Promotion promotion = promotionService.getNowPromotion(Promotion.Type.discount,tenant);//买单折扣活动
        ActivityPlanning activityPlanning = activityPlanningService.getCurrent(tenant, ActivityPlanning.Type.random);//随机立减活动

        boolean isActivityTenant=false;//是否启用随机立减活动
        boolean isCoupon=false;//是否区配店铺优惠券
        boolean isBillDiscount=false;//是否启用买单折扣
        String promotionName="";//买单折扣名称
        BigDecimal promotionAmount = BigDecimal.ZERO;//折扣金额
        Date promotionStartDate=null;//买单折扣活动开始日期
        Date promotionEndDate=null;//买单折扣活动结束日期
        Set<CouponModel> coupons=new HashSet<>();//店铺优惠券
        Set<CouponModel> activityCoupons=new HashSet<>();//随机减优惠券

        if(promotion!=null){
            isBillDiscount=true;
            promotionName=promotion.getName();
            promotionStartDate=promotion.getBeginDate();
            promotionEndDate=promotion.getEndDate();
        }else{
            Set<Coupon> couponSet = new HashSet<>();
            for (Coupon coupon : tenant.getCoupons()) {
                if (coupon.getExpired()&&coupon.getType().equals(Coupon.Type.tenantCoupon)) {
                    couponSet.add(coupon);
                }
            }
            coupons=CouponModel.bindData(couponSet);
            if(coupons.size()>0){
                isCoupon=true;
            }
        }

        if(activityPlanning!=null){
            isActivityTenant=true;
            activityCoupons=CouponModel.bindData(activityPlanning.getCoupons());
        }

        Map<String, Object> map = new HashMap<>();
        map.put("isActivityTenant",true);
        map.put("isCoupon",isCoupon);
        map.put("isBillDiscount",isBillDiscount);
        map.put("promotionName",promotionName);
        map.put("promotionAmount",promotionAmount);
        map.put("promotionStartDate",promotionStartDate);
        map.put("promotionEndDate",promotionEndDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String effectiveDate = null;
        if (promotionStartDate != null && promotionEndDate != null) {
            effectiveDate = sdf.format(promotionStartDate) + "-" + sdf.format(promotionEndDate);
        }
        map.put("effectiveDate", effectiveDate);
        map.put("coupons", coupons);
        map.put("activityCoupons", activityCoupons);
        //分享数据
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/pay/bill/buyreduce/" + id + ".jhtml");
        map.put("title", tenant.getName());
        map.put("desc", "买单立减，这里有您想象不到的优惠活动！！！");
        map.put("link", url);
        String imgUrl=tenant.getThumbnail();
        if(imgUrl==null){
            imgUrl=tenant.getLogo();
        }
        map.put("imgUrl", imgUrl);
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

    /**
     * 根据用户输入的金额，获取平台立减和店铺优惠券
     */
    @RequestMapping(value = "/get/amount", method = RequestMethod.POST)
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

        BigDecimal _amout = amount.subtract(noAmount);       //参与活动的金额

        BigDecimal decimal = BigDecimal.ZERO;               //应付金额
        BigDecimal promotionAmount = BigDecimal.ZERO;       //折扣金额

        BigDecimal couponAmount = BigDecimal.ZERO;          //商家优惠券金额

        BigDecimal activityCouponAmount = BigDecimal.ZERO;  //平台活动优惠券金额

        CouponModel couponModel = null;                     //自动区配到的商家优惠券
        CouponModel activityCouponModel = null;             //随机区配的平台优惠券
        PayBill payBill = new PayBill();
        if(promotion!=null){//买单折扣
            BigDecimal zkDiscount = _amout.multiply(promotion.getAgioRate().divide(BigDecimal.valueOf(100)));
            promotionAmount = _amout.subtract(_amout.multiply(promotion.getAgioRate().divide(BigDecimal.valueOf(100))));
            payBill.setBackDiscount(zkDiscount.multiply(promotion.getBackRate().divide(BigDecimal.valueOf(100))));
            payBill.setType(PayBill.Type.promotion);

        }else{//区配店铺优惠券
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
                    couponModel.copyFrom(coupon);
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
            activityCouponModel = new CouponModel();
            activityCouponModel.copyFrom(couponCode.getCoupon());
            activityCouponAmount = couponCode.getCoupon().getAmount();
        }else {
            if (activityPlanning != null) {
                if (member != null) {
                    if (payBillService.isLimit(member, activityPlanning.getActivityMaximumOrders())) {
                        couponCode = activityPlanningService.lockCoupon(activityPlanning, amount.subtract(noAmount));
                        if (couponCode != null) {
                            activityCouponModel = new CouponModel();
                            activityCouponModel.copyFrom(couponCode.getCoupon());
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
            activityCouponModel=new CouponModel();
            activityCouponModel.setAmount(payBill.getDiscount());
        }

        Map<String, Object> map = new HashMap<>();
        map.put("decimal", payBill.getEffectiveAmount());
        map.put("promotionAmount",promotionAmount);
        map.put("coupon", couponModel);
        map.put("activityCoupon", activityCouponModel);

        return DataBlock.success(map, "执行成功");


    }
}
