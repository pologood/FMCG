package net.wit.controller.pad.model;

import net.wit.entity.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ruanx on 2016/12/6.
 */
public class CartTenantModel {
    /*ID*/
    private Long id;

    /*name*/
    private String name;

    /*商品下所有商品是否全被选中*/
    private boolean selectAll;

    /*商铺红包*/
    private List<CouponModel> coupons;

    /*商品*/
    private Set<CartItemModel> products;

    /*店铺被选中商品数*/
    private int tenantQuantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }

    public List<CouponModel> getCoupons() {
        return coupons;
    }

    public void setCoupon(List<CouponModel> coupons) {
        this.coupons = coupons;
    }

    public Set<CartItemModel> getProducts() {
        return products;
    }

    public int getTenantQuantity() {
        return tenantQuantity;
    }

    public void setTenantQuantity(int tenantQuantity) {
        this.tenantQuantity = tenantQuantity;
    }

    public void setProducts(Set<CartItemModel> products) {
        this.products = products;
    }
    public void copyFrom(Tenant tenant,Cart cart,String type,String couponings){
        this.id = tenant.getId();
        this.name = tenant.getName();
        List<CouponModel> coupons = new ArrayList<CouponModel>();
        for(Coupon coupon:tenant.getCoupons()){
            if(coupon.getIsEnabled()&&coupon.getType().equals(Coupon.Type.tenantCoupon)&&coupon.getExpired()) {
                //确认订单，查看展示选中的红包
                if("order".equals(type)){
                    if(!"".equals(couponings)&&couponings!=null){
                        String[] str = couponings.split(",");
                        for (int i = 0; i < str.length; i++) {
                            Long couponing = Long.valueOf(str[i]);
                            if(coupon.getId().equals(couponing)){
                                CouponModel model = new CouponModel();
                                model.copyFrom(coupon);
                                coupons.add(model);
                            }
                        }
                    }
                }else{
                    CouponModel model = new CouponModel();
                    model.copyFrom(coupon);
                    coupons.add(model);
                }
            }
        }
        this.coupons = coupons;
        Set<CartItemModel> CartItems = new HashSet<CartItemModel>();
        boolean s = true;
        int q = 0;
        for(CartItem cartItem:cart.getCartItems()){
            if(cartItem.getProduct().getTenant().getId().equals(tenant.getId())){
                if(!cartItem.getSelected()){
                    s = false;
                }else{
                    q = q + cartItem.getQuantity();
                }
                CartItemModel model = new CartItemModel();
                model.copyFrom(cartItem,tenant);
                CartItems.add(model);
            }
        }
        this.selectAll = s;
        this.products = CartItems;
        this.tenantQuantity = q;
    }

}
