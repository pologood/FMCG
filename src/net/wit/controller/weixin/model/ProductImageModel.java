package net.wit.controller.weixin.model;

import net.wit.entity.ProductImage;

import java.util.ArrayList;
import java.util.List;

public class ProductImageModel extends BaseModel {

	/** 标题 */
	private String title;

	/** 原图片 */
	private String source;

	/** 大图片 */
	private String large;

	/** 中图片 */
	private String medium;

	/** 缩略图 */
	private String thumbnail;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getLarge() {
		return large;
	}

	public void setLarge(String large) {
		this.large = large;
	}

	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public void copyFrom(ProductImage productImage) {
		this.source = productImage.getSource();
		this.title = productImage.getTitle();
		this.large = productImage.getLarge();
		this.medium = productImage.getMedium();
		this.thumbnail = productImage.getThumbnail();
	}
	
	public static List<ProductImageModel> bindData(List<ProductImage> productImages) {
		List<ProductImageModel> models = new ArrayList<>();
		for (ProductImage productImage:productImages) {
			ProductImageModel model = new ProductImageModel();
			model.copyFrom(productImage);
			models.add(model);
		}
		return models;
	}
	
}
