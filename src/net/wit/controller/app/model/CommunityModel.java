package net.wit.controller.app.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.wit.entity.Community;
import net.wit.entity.Contact;
import net.wit.entity.Contact.Type;

public class CommunityModel extends BaseModel {
	/*编号*/
	private Long id;
	/*名称*/
	private String name;
	
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

	public void copyFrom(Community community) {
		this.id = community.getId();
		this.name = community.getName();
	}
	
	public static List<CommunityModel> bindData(List<Community> communities){
		List<CommunityModel> models = new ArrayList<CommunityModel>();
		for(Community community:communities){
			CommunityModel model = new CommunityModel();
			model.copyFrom(community);
			models.add(model);
		}
		return models;
	}
	
	public static Set<CommunityModel> bindData(Set<Community> communities){
		Set<CommunityModel> models = new HashSet<CommunityModel>();
		for(Community community:communities){
			CommunityModel model = new CommunityModel();
			model.copyFrom(community);
			models.add(model);
		}
		return models;
	}
}
