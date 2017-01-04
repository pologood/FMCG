package net.wit.controller.store.model;

import net.wit.entity.Barcode;
import net.wit.entity.Brand;
import net.wit.entity.ProductCategory;
import net.wit.entity.ProductImage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BarcodeModel  {

    /**
     * 名称
     */
    private String name;

    /**
     * 拼音码
     */
    private String spell;

    /**
     * 条码
     */
    private String barcode;

    /**
     * 单位
     */
    private String unitName;

    /**
     * 参考进价
     */
    private BigDecimal inPrice;

    /**
     * 参考售价
     */
    private BigDecimal outPrice;

    /**
     * 商品分类
     */
    private ProductCategory productCategory;

    /*品牌*/
    private Brand brand;

    /**
     * 图片
     */
    private List<ProductImage> productImages = new ArrayList<ProductImage>();

    /**
     * 商品详情-介绍
     */
    private String introduction;

    /**
     * 商品详情-APP介绍
     */
    private String descriptionapp;
    /*产品行业标签*/
    private  Long expertTagId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BigDecimal getInPrice() {
        return inPrice;
    }

    public void setInPrice(BigDecimal inPrice) {
        this.inPrice = inPrice;
    }

    public BigDecimal getOutPrice() {
        return outPrice;
    }

    public void setOutPrice(BigDecimal outPrice) {
        this.outPrice = outPrice;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getDescriptionapp() {
        return descriptionapp;
    }

    public void setDescriptionapp(String descriptionapp) {
        this.descriptionapp = descriptionapp;
    }

    public Long getExpertTagId() {
        return expertTagId;
    }

    public void setExpertTagId(Long expertTagId) {
        this.expertTagId = expertTagId;
    }

    public void copyFrom(Barcode barcode) {
        if (barcode==null){
            return;
        }
        this.name = barcode.getName();
        this.barcode = barcode.getBarcode();
        this.unitName = barcode.getUnitName();
        this.outPrice = barcode.getOutPrice();
        this.introduction = barcode.getIntroduction();
        this.descriptionapp = barcode.getDescriptionapp();

        this.productImages = barcode.getProductImages();

//        if (barcode.getTag()==null){
            this.expertTagId =barcode.getTag().getId();
//        }
    }

    public static BarcodeModel bindData(Barcode barcode) {
        BarcodeModel model = new BarcodeModel();
        model.copyFrom(barcode);
        return model;
    }

}
