package net.wit.controller.weixin.model;

import net.wit.entity.Cart;

import java.math.BigDecimal;
import java.util.List;

public class CartModel extends BaseModel {
    /**
     * ID
     */
    private Long id;
    /**
     * 有效金额总数
     */
    private BigDecimal effectivePrice;
    /**
     * 店铺
     */
    private List<CartTenantModel> tenants;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getEffectivePrice() {
        return effectivePrice;
    }

    public void setEffectivePrice(BigDecimal effectivePrice) {
        this.effectivePrice = effectivePrice;
    }

    public List<CartTenantModel> getTenants() {
        return tenants;
    }

    public void setTenants(List<CartTenantModel> tenants) {
        this.tenants = tenants;
    }

    public void copyFrom(Cart cart) {
        this.id = cart.getId();
        this.effectivePrice = cart.getEffectivePrice();
        if (cart.getTenants() != null) {
            this.tenants = CartTenantModel.bindData(cart);
        }
    }

}
