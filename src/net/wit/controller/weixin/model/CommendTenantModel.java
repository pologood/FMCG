package net.wit.controller.weixin.model;

import net.wit.entity.Location;
import net.wit.entity.Tenant;
import net.wit.util.SpringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 推荐好店
 * Created by WangChao on 2016-10-11.
 */
public class CommendTenantModel {
    //店铺ID
    private Long id;
    //店铺名称
    private String name;
    //缩略图
    private String thumbnail;
    //主营类目
    private String tenantCategoryName;
    //地址
    private String address;
    //距离
    private double distance;
    //店铺活动
    private List<TenantPromotionListModel> promotions;

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

    public String getTenantCategoryName() {
        return tenantCategoryName;
    }

    public void setTenantCategoryName(String tenantCategoryName) {
        this.tenantCategoryName = tenantCategoryName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public List<TenantPromotionListModel> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<TenantPromotionListModel> promotions) {
        this.promotions = promotions;
    }

    public void copyFrom(Tenant tenant, Location location) {
        this.id = tenant.getId();
        this.name = tenant.getName();
        this.thumbnail = tenant.getThumbnail() == null ? tenant.getLogo() : tenant.getThumbnail();
        if (tenant.getTenantCategory() != null) {
            this.tenantCategoryName = tenant.getTenantCategory().getName();
        } else {
            this.tenantCategoryName = SpringUtils.abbreviate(tenant.getIntroduction(), 20, "..");
        }
        this.address = tenant.getAddress();
        this.distance = tenant.distatce(location);
        this.promotions = TenantPromotionListModel.bindData(new ArrayList<>(tenant.getVaildPromotions()));
    }

    public static List<CommendTenantModel> bindData(List<Tenant> tenants, Location location) {
        List<CommendTenantModel> models = new ArrayList<>();
        for (Tenant tenant : tenants) {
            CommendTenantModel model = new CommendTenantModel();
            model.copyFrom(tenant, location);
            models.add(model);
        }
        return models;
    }
}
