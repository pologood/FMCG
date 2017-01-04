package net.wit.controller.app.model;

import net.wit.entity.DeliveryCenter;
import net.wit.entity.Location;
import net.wit.entity.Tenant;

import java.util.ArrayList;
import java.util.List;

public class SupplierListModel extends BaseModel {

    /** 商家ID */
    private Long id;
    /** 简称 */
    private String shortName;
    /** 缩影图 */
    private String thumbnail;
    /* 距离 */
    private double distance;
    /** 区域 */
    private AreaModel area;
    /** 地址 */
    private String address;
    /** 担保交易 */
    private Boolean tamPo;
    /** 七天退货 */
    private Boolean noReason;
    /** 联系电话 */
    private String telephone;
    /** 营业时间 */
    private String hours;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public AreaModel getArea() {
        return area;
    }

    public void setArea(AreaModel area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public void copyFrom(Tenant tenant, Location location) {
        this.id = tenant.getId();
        this.shortName = tenant.getShortName();
        this.thumbnail=this.thumbnail==null?tenant.getLogo():tenant.getThumbnail();
        this.tamPo = tenant.getTamPo();
        this.noReason = tenant.getNoReason();
        this.telephone = tenant.getTelephone();
        this.distance = tenant.distatce(location);
        this.hours=tenant.getHours();
        DeliveryCenter deliveryCenter=tenant.nearDeliveryCenter(location);
        if(deliveryCenter!=null){
            AreaModel areaModel=new AreaModel();
            areaModel.copyFrom(deliveryCenter.getArea());
            this.area=areaModel;
            this.address=deliveryCenter.getAddress();
        }
    }

    public static List<SupplierListModel> bindData(List<Tenant> tenants, Location location) {
        List<SupplierListModel> models = new ArrayList<>();
        for (Tenant tenant : tenants) {
            SupplierListModel model=new SupplierListModel();
            model.copyFrom(tenant,location);
            models.add(model);
        }
        return models;
    }

}
