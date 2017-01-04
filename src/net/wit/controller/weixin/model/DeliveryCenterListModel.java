package net.wit.controller.weixin.model;

import net.wit.entity.DeliveryCenter;
import net.wit.entity.Location;

import java.util.ArrayList;
import java.util.List;

public class DeliveryCenterListModel extends BaseModel {

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

    public void copyFrom(DeliveryCenter deliveryCenter) {
        this.id = deliveryCenter.getId();
        this.name = deliveryCenter.getName();
        this.areaName = deliveryCenter.getArea().getFullName();
        this.address = deliveryCenter.getAddress();
    }

    public static List<DeliveryCenterListModel> bindData(List<DeliveryCenter> deliveryCenters) {
        List<DeliveryCenterListModel> models = new ArrayList<>();
        for (DeliveryCenter deliveryCenter : deliveryCenters) {
            DeliveryCenterListModel model = new DeliveryCenterListModel();
            model.copyFrom(deliveryCenter);
            models.add(model);
        }
        return models;
    }
}
