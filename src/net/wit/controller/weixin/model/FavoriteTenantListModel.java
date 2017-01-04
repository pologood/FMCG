package net.wit.controller.weixin.model;

import net.wit.entity.Location;
import net.wit.entity.Tenant;
import net.wit.util.SpringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏的商家列表
 * Created by WangChao on 2016-10-11.
 */
public class FavoriteTenantListModel {
    //店铺ID
    private Long id;
    //缩略图
    private String thumbnail;
    //店铺名称
    private String name;
    //主营类目
    private String tenantCategoryName;
    //地址
    private String address;
    //距离
    private double distance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void copyFrom(Tenant tenant, Location location) {
        this.id = tenant.getId();
        this.thumbnail = tenant.getThumbnail() == null ? tenant.getThumbnail() : tenant.getLogo();
        this.name = tenant.getName();
        if (tenant.getTenantCategory() != null) {
            this.tenantCategoryName = tenant.getTenantCategory().getName();
        } else {
            this.tenantCategoryName = SpringUtils.abbreviate(tenant.getIntroduction(), 20, "..");
        }
        this.address = tenant.nearDeliveryCenter(location).getAddress();
        this.distance = tenant.distatce(location);
        if (this.distance!=-1) {
            this.distance = Double.parseDouble(String.format("%.2f",this.distance / 1000));
        }
    }

    public static List<FavoriteTenantListModel> bindData(List<Tenant> tenants, Location location) {
        List<FavoriteTenantListModel> models = new ArrayList<>();
        for (Tenant tenant : tenants) {
            FavoriteTenantListModel model = new FavoriteTenantListModel();
            model.copyFrom(tenant, location);
            models.add(model);
        }
        return models;
    }
}
