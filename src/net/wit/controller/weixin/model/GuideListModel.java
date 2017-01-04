package net.wit.controller.weixin.model;

import net.wit.entity.Employee;
import net.wit.entity.Member;

import java.util.ArrayList;
import java.util.List;

/**
 * 导购列表
 * Created by WangChao on 2016-6-29.
 */
public class GuideListModel extends BaseModel {
    /**
     * 导购Id
     */
    private Long id;
    /**
     * 员工Id
     */
    private Long employeeId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 头像
     */
    private String headImg;
    /**
     * 店铺名称
     */
    private String tenantName;
    /**
     * 销量
     */
    private Integer quertity;
    /**
     * 粉丝数
     */
    private Integer fansCount;
    /**
     * 是否已收藏
     */
    private boolean hasFavorited;
    /**
     * 电话
     */
    private String phone;

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

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public Integer getQuertity() {
        return quertity;
    }

    public void setQuertity(Integer quertity) {
        this.quertity = quertity;
    }

    public Integer getFansCount() {
        return fansCount;
    }

    public void setFansCount(Integer fansCount) {
        this.fansCount = fansCount;
    }

    public boolean isHasFavorited() {
        return hasFavorited;
    }

    public void setHasFavorited(boolean hasFavorited) {
        this.hasFavorited = hasFavorited;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void copyFrom(Employee employee) {
        this.id = employee.getMember().getId();
        this.employeeId=employee.getId();
        this.name = employee.getMember().getDisplayName();
        this.headImg = employee.getMember().getHeadImg();
        this.tenantName = employee.getTenant().getName();
        this.quertity = employee.getQuertity();
        this.fansCount = 0;
        this.hasFavorited = false;
        this.phone = employee.getMember().getMobile();
    }

    public void copyFrom(Employee employee, Integer fansCount, Member member) {
        copyFrom(employee);
        this.fansCount = fansCount;
        this.hasFavorited = member != null && member.getFavoriteMembers().contains(employee.getMember());
    }

    public static List<GuideListModel> bindData(List<Employee> employees) {
        List<GuideListModel> models = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getMember() != null) {
                GuideListModel model = new GuideListModel();
                model.copyFrom(employee);
                models.add(model);
            }
        }
        return models;
    }

}
