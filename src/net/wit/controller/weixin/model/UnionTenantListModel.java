package net.wit.controller.weixin.model;

import net.wit.entity.Location;
import net.wit.entity.Tenant;
import net.wit.entity.TenantRelation;
import net.wit.entity.UnionTenant;

import java.util.ArrayList;
import java.util.List;

/**
 * 联盟商家
 * Created by WangChao on 2016-10-11.
 */
public class UnionTenantListModel {
    //店铺ID
    private Long id;
    //缩略图
    private String thumbnail;
    //店铺名称
    private String name;
    //地址
    private String address;
    //营业时间
    private String hours;
    //担保交易
    private Boolean tamPo;
    //七天退货
    private Boolean noReason;
    //距离
    private double distance;
    //电话
    private String telephone;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void copyFrom(Tenant tenant, Location location) {
        this.id = tenant.getId();
        this.thumbnail = tenant.getThumbnail() == null ? tenant.getLogo() : tenant.getThumbnail();
        this.name = tenant.getName();
        this.address = tenant.nearDeliveryCenter(location).getAddress();
        this.hours = tenant.getHours();
        this.tamPo = tenant.getTamPo();
        this.noReason = tenant.getNoReason();
        this.distance = tenant.distatce(location);
        this.telephone = tenant.getTelephone();
    }

    public static List<UnionTenantListModel> bindData(List<UnionTenant> tenantRelations, Location location) {
        List<UnionTenantListModel> models = new ArrayList<>();
        for (UnionTenant tenantRelation : tenantRelations) {
            if(tenantRelation.getTenant()!=null){
                UnionTenantListModel model = new UnionTenantListModel();
                model.copyFrom(tenantRelation.getTenant(), location);
                models.add(model);
            }
        }
        return models;
    }
}
