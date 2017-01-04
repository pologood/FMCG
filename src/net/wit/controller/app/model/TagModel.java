package net.wit.controller.app.model;

import java.util.HashSet;
import java.util.Set;

import net.wit.entity.SpecificationValue;
import net.wit.entity.Tag;

public class TagModel extends BaseModel {
	/*ID*/
	private Long id;
	/*标签名称*/
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


	public void copyFrom(Tag tag) {
		this.id = tag.getId();
		this.name = tag.getName();
	}
	
	public static Set<TagModel> bindData(Set<Tag> tags) {
		Set<TagModel> models = new HashSet<TagModel>(tags.size());
		for (Tag tag:tags) {
			TagModel model = new TagModel();
			model.copyFrom(tag);
			models.add(model);
		}
		return models;
	}
			
}
