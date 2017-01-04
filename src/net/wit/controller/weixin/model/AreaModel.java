package net.wit.controller.weixin.model;

import net.wit.entity.Area;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AreaModel extends BaseModel {

    /*区域ID*/
    private Long id;
    /*区域名*/
    private String name;
    /*区域全名*/
    private String fullName;
    /*是否有下级*/
    private Boolean hasChildren;
    /*商圈*/
    private Set<CommunityModel> communities;
    /*是否有商圈*/
    private Boolean hasCommunities;
    /*区域子级*/
    private List<AreaModel> childrens;


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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Boolean getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(Boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public Set<CommunityModel> getCommunities() {
        return communities;
    }

    public void setCommunities(Set<CommunityModel> communities) {
        this.communities = communities;
    }

    public Boolean getHasCommunities() {
        return hasCommunities;
    }

    public void setHasCommunities(Boolean hasCommunities) {
        this.hasCommunities = hasCommunities;
    }

    public List<AreaModel> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<AreaModel> childrens) {
        this.childrens = childrens;
    }

    public void copyFrom(Area area) {
        if (area == null) {
            this.id = 0L;
            this.name = "中国";
            this.fullName = "中国";
            return;
        }
        this.id = area.getId();
        this.name = area.getName();
        this.fullName = area.getFullName();
        this.hasChildren = area.getChildren().size() > 0;
        this.hasCommunities = area.getCommunities().size() > 0;
    }

    public static List<AreaModel> bindData(List<Area> areas) {
        List<AreaModel> models = new ArrayList<>();
        for (Area area : areas) {
            AreaModel model = new AreaModel();
            model.copyFrom(area);
            models.add(model);
        }
        return models;
    }

    public static List<AreaModel> bindAllData(List<Area> areas) {
        List<AreaModel> models = new ArrayList<>();
        for (Area area : areas) {
            AreaModel model = new AreaModel();
            model.copyFrom(area);
            if(area.getCommunities().size()>0){
                model.setCommunities(CommunityModel.bindData(area.getCommunities()));
            }
            if(area.getChildren().size()>0){
                model.setChildrens(AreaModel.bindAllData(new ArrayList<>(area.getChildren())));
            }
            models.add(model);
        }
        return models;
    }

}
