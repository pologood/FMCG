package net.wit.controller.weixin.model;

import net.wit.entity.Location;
import net.wit.entity.Tenant;
import net.wit.entity.UnionTenant;
import net.wit.util.SpringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 商家列表
 * Created by WangChao on 2016-10-11.
 */
public class TenantListModel {
    //店铺ID
    private Long id;
    //缩略图
    private String thumbnail;
    //店铺名称
    private String name;
    //平均评分
    private Float grade;
    //月销售额
    private BigDecimal monthSales;
    //主营类目
    private String tenantCategoryName;
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

    public Float getGrade() {
        return grade;
    }

    public void setGrade(Float grade) {
        this.grade = grade;
    }

    public BigDecimal getMonthSales() {
        return monthSales;
    }

    public void setMonthSales(BigDecimal monthSales) {
        this.monthSales = monthSales;
    }

    public String getTenantCategoryName() {
        return tenantCategoryName;
    }

    public void setTenantCategoryName(String tenantCategoryName) {
        this.tenantCategoryName = tenantCategoryName;
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

    public void copyFrom(Tenant tenant) {
        this.id = tenant.getId();
        this.thumbnail = tenant.getThumbnail();
        this.name = tenant.getName();
        this.grade = tenant.getScore();
        this.monthSales = tenant.getMonthSales();
        if (tenant.getTenantCategory() != null) {
            this.tenantCategoryName = tenant.getTenantCategory().getName();
        } else {
            this.tenantCategoryName = SpringUtils.abbreviate(tenant.getIntroduction(), 20, "..");
        }
        this.promotions = TenantPromotionListModel.bindData(new ArrayList<>(tenant.getVaildPromotions()));
    }

    public static List<TenantListModel> bindData(List<Tenant> tenants) {
        List<TenantListModel> models = new ArrayList<>();
        for (Tenant tenant : tenants) {
            TenantListModel model = new TenantListModel();
            model.copyFrom(tenant);
            models.add(model);
        }
        return models;
    }

    public void copyFrom(Tenant tenant, Location location) {
        this.id = tenant.getId();
        this.thumbnail = tenant.getThumbnail();
        this.name = tenant.getName();
        this.grade = tenant.getScore();
        this.monthSales = tenant.getMonthSales();
        if (tenant.getTenantCategory() != null) {
            this.tenantCategoryName = tenant.getTenantCategory().getName();
        } else {
            this.tenantCategoryName = SpringUtils.abbreviate(tenant.getIntroduction(), 20, "..");
        }
        this.distance = tenant.distatce(location);
        if (this.distance!=-1) {
            this.distance = Double.parseDouble(String.format("%.2f",this.distance / 1000));
        }
        this.promotions = TenantPromotionListModel.bindData(new ArrayList<>(tenant.getVaildPromotions()));
    }

    public static List<TenantListModel> bindData(List<Tenant> tenants, Location location) {
        List<TenantListModel> models = new ArrayList<>();
        for (Tenant tenant : tenants) {
            TenantListModel model = new TenantListModel();
            model.copyFrom(tenant, location);
            models.add(model);
        }
        return models;
    }

    public static List<TenantListModel> bindUnionTenant(List<UnionTenant> tenantRelations, Location location) {
        List<TenantListModel> models = new ArrayList<>();
        for (UnionTenant tenantRelation : tenantRelations) {
            if(tenantRelation.getTenant()!=null){
                TenantListModel model = new TenantListModel();
                model.copyFrom(tenantRelation.getTenant(), location);
                models.add(model);
            }
        }
        return models;
    }
}
