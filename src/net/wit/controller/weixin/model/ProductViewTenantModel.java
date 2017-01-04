package net.wit.controller.weixin.model;

import net.wit.entity.Tenant;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品详情页的店铺信息
 * Created by WangChao on 2016-10-12.
 */
public class ProductViewTenantModel {
    //店铺ID
    private Long id;
    //店铺名称
    private String name;
    //缩略图
    private String thumbnail;
    //包邮促销方案
    private PromotionModel mailPromotions;
    //货到付款
    private Boolean toPay;
    //担保交易
    private Boolean tamPo;
    //七天退货
    private Boolean noReason;

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

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public PromotionModel getMailPromotions() {
        return mailPromotions;
    }

    public void setMailPromotions(PromotionModel mailPromotions) {
        this.mailPromotions = mailPromotions;
    }

    public Boolean getToPay() {
        return toPay;
    }

    public void setToPay(Boolean toPay) {
        this.toPay = toPay;
    }

    public Boolean getTamPo() {
        return tamPo;
    }

    public void setTamPo(Boolean tamPo) {
        this.tamPo = tamPo;
    }

    public Boolean getNoReason() {
        return noReason;
    }

    public void setNoReason(Boolean noReason) {
        this.noReason = noReason;
    }

    public void copyFrom(Tenant tenant) {
        this.id = tenant.getId();
        this.name = tenant.getName();
        this.thumbnail = tenant.getThumbnail();
        List<PromotionModel> promotionList=PromotionModel.bindData(new ArrayList<>(tenant.getMailPromotions()));
        this.mailPromotions = promotionList.size()>0?promotionList.get(0):null;
        this.toPay = tenant.getToPay();
        this.tamPo = tenant.getTamPo();
        this.noReason = tenant.getNoReason();
    }
}
