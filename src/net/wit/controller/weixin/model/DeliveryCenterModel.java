package net.wit.controller.weixin.model;

import net.wit.entity.DeliveryCenter;
import net.wit.entity.Location;

public class DeliveryCenterModel extends BaseModel {

    /**
     * ID
     **/
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 地区名称
     */
    private String areaName;
    /**
     * 地址
     */
    private String address;
    /**
     * 缩略图
     */
    private String thumbnail;
    /**
     * 经纬度
     */
    private Location location;

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

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void copyFrom(DeliveryCenter deliveryCenter) {
        this.id = deliveryCenter.getId();
        this.name = deliveryCenter.getName();
        this.areaName = deliveryCenter.getArea().getName();
        this.address = deliveryCenter.getAddress();
        this.thumbnail = deliveryCenter.getTenant().getThumbnail() == null ? deliveryCenter.getTenant().getLogo() : deliveryCenter.getTenant().getThumbnail();
        this.location = deliveryCenter.getLocation();
    }

}
