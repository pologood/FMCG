package net.wit.controller.assistant.model;

import net.wit.controller.assistant.model.BaseModel;
import net.wit.controller.assistant.model.SingleModel;
import net.wit.entity.Ad;
import net.wit.entity.Ad.LinkType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdModel extends BaseModel {
	
	/*id*/
	private Long id;
	/*标题*/
	private String title;
	/*图片*/
	private String image;
	/*链接地址*/
	private String url;

	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public void copyFrom(Ad ad) {
		this.id = ad.getId();
		this.image = ad.getPath();
		this.title = ad.getTitle();
		this.url = ad.getUrl();
	}
	
	public static List<AdModel> bindData(Set<Ad> ads) {
		List<AdModel> models = new ArrayList<AdModel>();
		for (Ad ad:ads) {
			AdModel model = new AdModel();
			model.copyFrom(ad);
			models.add(model);
		}
		return models;
	}
}
