package net.wit.controller.pad.model;

import net.wit.controller.app.model.BaseModel;
import net.wit.entity.Cart;
import net.wit.entity.Tenant;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by ruanx on 2016/12/6.
 */
public class CartModel extends BaseModel {
    /*购物车总价（不包含运费）*/
    private BigDecimal totalPrize;

    /*运费*/
    private BigDecimal freight;

    /*已选总数*/
    private int totalQuantity;

    /*店铺条目*/
    private List<CartTenantModel> tenants;

    /*token_key*/
    private String token_key;

    /*确认订单二维码地址*/
    private String qurl;

    public BigDecimal getTotalPrize() {
        return totalPrize;
    }

    public void setTotalPrize(BigDecimal totalPrize) {
        this.totalPrize = totalPrize;
    }

    public BigDecimal getFreight() {
        return freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public List<CartTenantModel> getTenants() {
        return tenants;
    }

    public void setTenants(List<CartTenantModel> tenants) {
        this.tenants = tenants;
    }

    public String getToken_key() {
        return token_key;
    }

    public void setToken_key(String token_key) {
        this.token_key = token_key;
    }

    public String getQurl() {
        return qurl;
    }

    public void setQurl(String qurl) {
        this.qurl = qurl;
    }

    public void copyFrom(Cart cart, String type, String coupons){
        this.totalPrize = cart.getEffectivePrice();
        this.freight = cart.getFreight();
        this.totalQuantity = cart.getEffectiveQuantity();
        List<CartTenantModel> tenants=new ArrayList<>();
        for(Tenant tenant:cart.getTenants()){
            CartTenantModel cartTenantModel = new CartTenantModel();
            cartTenantModel.copyFrom(tenant,cart,type,coupons);
            tenants.add(cartTenantModel);
        }
        this.tenants = tenants;
        if("order".equals(type)){
            String uuid = UUID.randomUUID().toString() + DigestUtils.md5Hex(RandomStringUtils.randomAlphabetic(30));
            this.token_key = uuid;
            if(coupons==null){
                coupons="";
            }
            this.qurl = "order/addAndPay.jhtml?cart_key="+cart.getId()+"&coupons="+coupons+"&token_key="+uuid;
        }
    }
}
