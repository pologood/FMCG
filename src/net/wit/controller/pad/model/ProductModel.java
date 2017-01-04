package net.wit.controller.pad.model;

import net.wit.controller.assistant.model.SingleModel;
import net.wit.controller.assistant.model.TagModel;
import net.wit.entity.Cart;
import net.wit.entity.CartItem;
import net.wit.entity.Product;
import net.wit.entity.model.BaseModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ruanx on 2016/11/15.
 */
public class ProductModel extends BaseModel {
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

    public static List<ProductModel> bindData(List<Product> products,Cart cart) {
        List<ProductModel> models = new ArrayList<ProductModel>();
        for (Product product:products) {
            ProductModel model = new ProductModel();
            model.copyFrom(product,cart);
            models.add(model);
        }
        return models;
    }
    public void copyFrom(Product product,Cart cart) {
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
    }

    public static List<ProductModel> bindDatas(List<Map<String, Object>> products, Cart cart) {
        List<ProductModel> models = new ArrayList<ProductModel>();
        for (Map<String, Object> product:products) {
            ProductModel model = new ProductModel();
            model.copyFroms(product,cart);
            models.add(model);
        }
        return models;
    }

    public void copyFroms(Map<String, Object> product,Cart cart) {
        boolean has = false;
        Long id = Long.parseLong(product.get("id").toString());
        if(cart!=null){
            for(CartItem cartitem:cart.getCartItems()){
                if(cartitem.getProduct().getId().equals(id)){
                    has = true;
                }
            }
        }
        this.id = id;
        this.fullName = product.get("fullName").toString();
        this.price = new BigDecimal(Double.parseDouble((product.get("price").toString())));//BigDecimal.valueOf(Long.parseLong(product.get("price").toString()));
        this.thumbnail = product.get("thumbnail").toString();
        this.monthSales=Long.parseLong(product.get("monthSales").toString());
        this.selected = has;
    }
}
