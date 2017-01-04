package net.wit.controller.assistant.model;

import net.wit.controller.assistant.model.BaseModel;
import net.wit.controller.assistant.model.DeliveryCenterModel;
import net.wit.controller.assistant.model.SingleModel;
import net.wit.controller.assistant.model.TagModel;
import net.wit.entity.Employee;
import net.wit.entity.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by WangChao on 2016-6-29.
 */
public class GuideModel extends BaseModel {
    /*导购Id*/
    private Long id;
    /*员工Id*/
    private Long employeeId;
    /*姓名*/
    private String name;
    /*昵称*/
    private String nickName;
    /*头像*/
    private String headImg;
    /*标签*/
    private Set<TagModel> tags;
    /*店铺*/
    private SingleModel tenant;
    /*手机*/
    private String mobile;
    /*销量*/
    private Integer quertity;
    /*介绍*/
    private String description;
    /*粉丝数*/
    private Integer fansCount;
    /**门店*/
    private DeliveryCenterModel deliveryCenter;
    /**是否收藏*/
    private Boolean hasFavorited;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Set<TagModel> getTags() {
        return tags;
    }

    public void setTags(Set<TagModel> tags) {
        this.tags = tags;
    }

    public SingleModel getTenant() {
        return tenant;
    }

    public void setTenant(SingleModel tenant) {
        this.tenant = tenant;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getQuertity() {
        return quertity;
    }

    public void setQuertity(Integer quertity) {
        this.quertity = quertity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getFansCount() {
        return fansCount;
    }

    public void setFansCount(Integer fansCount) {
        this.fansCount = fansCount;
    }

    public DeliveryCenterModel getDeliveryCenter() {
        return deliveryCenter;
    }

    public void setDeliveryCenter(DeliveryCenterModel deliveryCenter) {
        this.deliveryCenter = deliveryCenter;
    }

    public Boolean getHasFavorited() {
        return hasFavorited;
    }

    public void setHasFavorited(Boolean hasFavorited) {
        this.hasFavorited = hasFavorited;
    }

    public void copyFrom(Employee employee) {
        this.id=employee.getMember().getId();
        this.employeeId=employee.getId();
        this.name=employee.getMember().getName();
        if (this.name == null) {
            this.nickName = employee.getMember().getDisplayName();
        } else {
            this.nickName = employee.getMember().getName();
        }
        this.headImg = employee.getMember().getHeadImg();
        this.tags = TagModel.bindData(employee.getTags());
        SingleModel tenant = new SingleModel();
        tenant.setId(employee.getTenant().getId());
        tenant.setName(employee.getTenant().getName());
        this.tenant=tenant;
        this.mobile=employee.getMember().getMobile();
        this.quertity=employee.getQuertity();
        this.description=employee.getDescription();
        DeliveryCenterModel deliveryCenterModel=null;
        if(employee.getDeliveryCenter()!=null){
            deliveryCenterModel=new DeliveryCenterModel();
            deliveryCenterModel.copyFrom(employee.getDeliveryCenter());
        }
        this.deliveryCenter=deliveryCenterModel;
    }

    public void copyFrom(Employee employee, Integer fansCount,Member member) {
        copyFrom(employee);
        this.fansCount=fansCount;
        this.hasFavorited=false;
        if(member!=null){
            this.hasFavorited=member.getFavoriteMembers().contains(employee.getMember());
        }
    }

    public static List<GuideModel> bindData(List<Employee> employees) {
        List<GuideModel> models = new ArrayList<>();
        for (Employee employee:employees){
            if(employee.getMember()!=null){
                GuideModel model=new GuideModel();
                model.copyFrom(employee);
                models.add(model);
            }
        }
        return models;
    }

}
