package net.wit.controller.app.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.wit.entity.Area;
import net.wit.entity.Product;

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

	public void copyFrom(Area area) {
		if (area==null) {
			this.id = 0L;
			this.name = "中国";
			this.fullName = "中国";
			return;
		}
		this.id = area.getId();
		this.name = area.getName();
		this.fullName = area.getFullName();
		this.hasChildren = area.getChildren().size()>0;
		this.hasCommunities = area.getCommunities().size()>0;
	}

	public static List<AreaModel> bindData(List<Area> areas) {
		List<AreaModel> models = new ArrayList<AreaModel>();
		for (Area area:areas) {
			AreaModel model = new AreaModel();
			model.copyFrom(area);
			models.add(model);
		}
		return models;
	}
	public static Set<AreaModel> bindData(Set<Area> areas) {
		Set<AreaModel> models = new HashSet<AreaModel>(areas.size());
		for (Area area:areas) {
			AreaModel model = new AreaModel();
			model.copyFrom(area);
			models.add(model);
		}
		return models;
	}
	
	public static Set<AreaModel> bindAllData(Set<Area> areas) {
		Set<AreaModel> models = new HashSet<AreaModel>(areas.size());
		for (Area area:areas) {
			AreaModel model = new AreaModel();
			model.copyFrom(area);
			model.setCommunities(CommunityModel.bindData(area.getCommunities()));
			models.add(model);
		}
		return models;
	}
	
}
