/**
 * ====================================================
 * 文件名称: EntitySupport.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年8月18日			Administrator(创建:创建文件)
 * ====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 */
package net.wit.support;

import java.math.BigDecimal;
import java.util.Date;

import net.wit.Setting;
import net.wit.entity.*;
import net.wit.entity.Member.BindStatus;
import net.wit.entity.Order.OrderSource;
import net.wit.entity.Order.OrderStatus;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.Order.ShippingStatus;
import net.wit.entity.Tenant.Status;
import net.wit.entity.Tenant.TenantType;
import net.wit.util.SettingUtils;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @ClassName: EntitySupport
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014年8月18日 上午11:21:04
 */
public class EntitySupport {

    public static DeliveryCenter createDeliveryCenter() {
        DeliveryCenter deliveryCenter = new DeliveryCenter();
        deliveryCenter.setScore(0F);
        deliveryCenter.setTotalScore(0L);
        deliveryCenter.setScoreCount(0L);
        return deliveryCenter;
    }

    public static Order createInitOrder() {
        Order order = new Order();
        order.setOrderStatus(OrderStatus.unconfirmed);
        order.setPaymentStatus(PaymentStatus.unpaid);
        order.setShippingStatus(ShippingStatus.unshipped);
        order.setFee(BigDecimal.ZERO);
        order.setFreight(BigDecimal.ZERO);
        order.setPromotionDiscount(BigDecimal.ZERO);
        order.setCouponDiscount(BigDecimal.ZERO);
        order.setOffsetAmount(BigDecimal.ZERO);
        order.setPoint(0L);
        order.setOrderSource(OrderSource.web);
        order.setIsProfitSettlement(false);
        return order;
    }

    public static Message createInitMessage(Message.Type type, String content, String sn, Member receiver, Member sender) {
        Message msg = new Message();
        msg.setType(type);
        msg.setIsDraft(false);
        msg.setContent(content);
        msg.setReceiver(receiver);
        msg.setReceiverDelete(false);
        msg.setReceiverRead(false);
        msg.setSenderDelete(false);
        msg.setSenderRead(false);
        msg.setSender(sender);
        msg.setSn(sn);
        msg.setOrderStatus("");
        msg.setIp("0.0.0.0");
        msg.setTemplete(Message.Templete.none);
        switch (type) {
            case order:
                msg.setTitle("订单提醒");
                break;
            case account:
                msg.setTitle("账单提醒");
                break;
            case notice:
                msg.setTitle("系统公告");
                break;
            case message:
                msg.setTitle("系统消息");
                break;
            case consultation:
                msg.setTitle("咨询回复");
                break;
            case contact:
                msg.setTitle("社交圈");
                break;
            case redPacket:
                msg.setTitle("卡券库存提醒");
                break;
            default:
                msg.setTitle("系统消息");
        }
        return msg;
    }

    public static Trade createInitTrade() {
        Trade trade = new Trade();
        trade.setShippingStatus(ShippingStatus.unshipped);
        trade.setTax(BigDecimal.ZERO);
        trade.setFreight(BigDecimal.ZERO);
        trade.setOffsetAmount(BigDecimal.ZERO);
        trade.setCouponDiscount(BigDecimal.ZERO);
        trade.setClearing(false);
        trade.setClearingDate(null);
        trade.setSuppliered(false);
        trade.setSupplierDate(null);
        trade.setFee(BigDecimal.ZERO);
        trade.setDiscount(BigDecimal.ZERO);
        trade.setAgencyAmount(BigDecimal.ZERO);
        return trade;
    }

    public static Member createInitMember() {
        Member member = new Member();
        member.setCreateDate(new Date());
        member.setModifyDate(new Date());
        member.setAmount(BigDecimal.ZERO);
        member.setBalance(BigDecimal.ZERO);
        member.setClearBalance(BigDecimal.ZERO);
        member.setFreezeBalance(BigDecimal.ZERO);
        member.setPrivilege(0);
        member.setTotalScore(0L);
        member.setIsEnabled(true);
        member.setIsLocked(Member.LockType.none);
        member.setLoginFailureCount(0);
        member.setPoint(0l);
        member.setSourceType(Member.SourceType.none);
        member.setJmessage(false);
        member.setPassword(DigestUtils.md5Hex("123456"));
        member.setPaymentPassword(DigestUtils.md5Hex("123456"));
        member.setEmail("@");
        member.setRegisterIp("0.0.0.0");
        member.setBindMobile(BindStatus.none);
        member.setBindEmail(BindStatus.none);
        member.setRebateAmount(BigDecimal.ZERO);
        member.setProfitAmount(BigDecimal.ZERO);
        member.setScore(0F);
        member.setFans(0);
        member.setScoreCount(0L);
        member.setTotalScore(0L);
        member.setLoginCount(0);
        member.setFreezeCashBalance(BigDecimal.ZERO);
        return member;
    }

    public static Tenant createInitTenant() {
        Tenant tenant  = new Tenant();
        tenant.setCode("1");
        tenant.setScore(0F);
        tenant.setTotalScore(0L);
        tenant.setScoreCount(0L);
        tenant.setHits(0L);
        tenant.setWeekHits(0L);
        tenant.setMonthHits(0L);
        tenant.setMonthSales(BigDecimal.ZERO);
        tenant.setSales(BigDecimal.ZERO);
        tenant.setWeekSales(BigDecimal.ZERO);
        tenant.setTotalAssistant(0L);
        tenant.setTenantType(TenantType.tenant);
        tenant.setAgency(BigDecimal.ZERO);
        tenant.setBrokerage(BigDecimal.ZERO);
        tenant.setBalance(BigDecimal.ZERO);
        tenant.setFreezeBalance(BigDecimal.ZERO);
        Freight freight = new Freight();
        freight.setFreightType(Freight.Type.weight);
        freight.setFirstPrice(BigDecimal.ZERO);
        freight.setContinuePrice(BigDecimal.ZERO);
        freight.setFirstWeight(0);
        freight.setContinueWeight(0);
        tenant.setFreight(freight);
        tenant.setTotalAssistant(0L);
        tenant.setTamPo(false);
        tenant.setToPay(true);
        tenant.setNoReason(false);
        tenant.setIsUnion(false);
        tenant.setSelf(false);
        tenant.setWeekHitsDate(new Date());
        tenant.setMonthHitsDate(new Date());
        tenant.setWifi(false);
        tenant.setCloudTenant(false);
        tenant.setEquipment(false);

        Setting setting = SettingUtils.get();
        tenant.setBrokerage(setting.getBrokerage());
        tenant.setGeneralize(BigDecimal.ZERO);
        tenant.setStatus(Status.none);
        Software software = new Software();
        software.setIndustry(Software.Industry.MKT);
        software.setVersion(Software.Version.NET);
        tenant.setSoftware(software);
        return tenant;
    }

    public static Promotion createInitPromtion(){
        Promotion promotion = new Promotion();
        promotion.setMinimumPrice(null);
        promotion.setMaximumPrice(null);
        promotion.setMaximumQuantity(null);
        promotion.setMinimumQuantity(null);
        promotion.setAgioRate(BigDecimal.ZERO);
        promotion.setBackRate(BigDecimal.ZERO);
        return  promotion;
    }

    public static Authen createInitAuthen() {
        Authen authen = new Authen();
        return authen;
    }

    public static Refunds createInitRefunds() {
        Refunds refunds = new Refunds();
        return refunds;
    }

}
