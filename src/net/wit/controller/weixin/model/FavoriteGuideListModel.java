package net.wit.controller.weixin.model;

import net.wit.entity.Employee;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏的导购列表
 * Created by WangChao on 2016-6-29.
 */
public class FavoriteGuideListModel extends BaseModel {
    /**
     * 导购Id
     */
    private Long id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 头像
     */
    private String headImg;
    /**
     * 店铺名
     */
    private String tenantName;
    /**
     * 标签
     */
    private List<TagModel> tags;
    /**
     * 销量
     */
    private Integer quertity;

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

    public List<TagModel> getTags() {
        return tags;
    }

    public void setTags(List<TagModel> tags) {
        this.tags = tags;
    }

    public Integer getQuertity() {
        return quertity;
    }

    public void setQuertity(Integer quertity) {
        this.quertity = quertity;
    }

    public void copyFrom(Employee employee) {
        this.id = employee.getMember().getId();
        this.name = employee.getMember().getDisplayName();
        this.headImg = employee.getMember().getHeadImg();
        this.tenantName = employee.getTenant().getName();
        this.tags = TagModel.bindData(new ArrayList<>(employee.getTags()));
        this.quertity = employee.getQuertity();
    }

    public static List<FavoriteGuideListModel> bindData(List<Employee> employees) {
        List<FavoriteGuideListModel> models = new ArrayList<>();
        for (Employee employee : employees) {
            FavoriteGuideListModel model = new FavoriteGuideListModel();
            model.copyFrom(employee);
            models.add(model);
        }
        return models;
    }

}
