package net.wit.controller.app.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.wit.entity.Ad;
import net.wit.entity.Ad.LinkType;
import net.wit.entity.Area;
import net.wit.entity.Product;

public class AdModel extends BaseModel {
	
	/*id*/
	private Long id;
	/*标题*/
	private String title;
	/*图片*/
	private String image;
	/*链接地址*/
	private String url;
	/*广各位*/
	private SingleModel adPosition;
	private LinkType linkType;
	private Long linkId;
	
	
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
	public SingleModel getAdPosition() {
		return adPosition;
	}
	public void setAdPosition(SingleModel adPosition) {
		this.adPosition = adPosition;
	}
	
	public void copyFrom(Ad ad) {
		this.id = ad.getId();
		this.image = ad.getPath();
		this.title = ad.getTitle();
		this.url = ad.getUrl();
		this.linkId = ad.getLinkId();
		this.linkType = ad.getLinkType();
	}
	
	public static List<AdModel> bindData(List<Ad> ads) {
		List<AdModel> models = new ArrayList<AdModel>();
		for (Ad ad:ads) {
			AdModel model = new AdModel();
			model.copyFrom(ad);
			models.add(model);
		}
		return models;
	}
	public static Set<AdModel> bindData(Set<Ad> ads) {
		Set<AdModel> models = new HashSet<AdModel>(ads.size());
		for (Ad ad:ads) {
			AdModel model = new AdModel();
			model.copyFrom(ad);
			models.add(model);
		}
		return models;
	}
	public LinkType getLinkType() {
		return linkType;
	}
	public void setLinkType(LinkType linkType) {
		this.linkType = linkType;
	}
	public Long getLinkId() {
		return linkId;
	}
	public void setLinkId(Long linkId) {
		this.linkId = linkId;
	}
	
	
}
