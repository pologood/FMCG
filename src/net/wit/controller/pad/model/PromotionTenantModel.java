package net.wit.controller.pad.model;

import net.wit.controller.assistant.model.SingleModel;
import net.wit.controller.assistant.model.TagModel;
import net.wit.entity.Cart;
import net.wit.entity.CartItem;
import net.wit.entity.Product;
import net.wit.entity.Promotion;
import net.wit.entity.model.BaseModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 促销
 * Created by ruanx on 2016/12/17.
 */
public class PromotionTenantModel extends BaseModel {
    /*商品ID*/
    private Long id;

    /*全名描述*/
    private String fullName;

    /*缩略图*/
    private String thumbnail;

    /*销售价*/
    private BigDecimal price;

    /*标签*/
    private Set<TagModel> tags;

    /** 月销量 */
    private Long monthSales;

    /** 是否已被添加到购物车 */
    private boolean selected;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Set<TagModel> getTags() {
        return tags;
    }

    public void setTags(Set<TagModel> tags) {
        this.tags = tags;
    }

    public Long getMonthSales() {
        return monthSales;
    }

    public void setMonthSales(Long monthSales) {
        this.monthSales = monthSales;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public static List<PromotionTenantModel> bindData(List<Promotion> promotions, Cart cart) {
        List<PromotionTenantModel> models = new ArrayList<PromotionTenantModel>(promotions.size());
        for (Promotion promotion:promotions) {
            if(promotion.getType()== Promotion.Type.buyfree||promotion.getType()== Promotion.Type.seckill){
                PromotionTenantModel model = new PromotionTenantModel();
                model.copyFrom(promotion.getDefaultProduct(),cart,promotion);
                models.add(model);
            }

        }
        return models;
    }

    public void copyFrom(Product product, Cart cart,Promotion promotion) {
        boolean has = false;
        if(cart!=null){
            for(CartItem cartitem:cart.getCartItems()){
                if(cartitem.getProduct().getId().equals(product.getId())){
                    has = true;
                }
            }
        }
        this.id = product.getId();
        this.fullName = product.getFullName();
        this.price = product.getPrice();
        SingleModel tenant = new SingleModel();
        tenant.setId(product.getTenant().getId());
        tenant.setName(product.getTenant().getName());
        this.tags = TagModel.bindData(product.getTags());
        this.thumbnail = product.getThumbnail();
        this.monthSales=product.getMonthSales();
        this.selected = has;
        if (product!=null) {
            if (promotion.getType().equals(Promotion.Type.buyfree)) {
                this.price = product.calcEffectivePrice(null); //product.getPrice();
            } else if (promotion.getType().equals(Promotion.Type.seckill)) {
                this.price = new BigDecimal(promotion.getPriceExpression());
            }
        }
    }
}
