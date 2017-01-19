package net.wit.controller.assistant.model;

import net.wit.controller.app.model.BaseModel;
import net.wit.controller.app.model.MemberListModel;
import net.wit.controller.app.model.ProductImageModel;
import net.wit.entity.Member;
import net.wit.entity.Review;
import net.wit.entity.Review.Type;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewProductModel extends BaseModel {

	private Type type;

	/** 评分(商品评分/商家诚信服务评分/会员诚信评分) */
	private Integer score;

	/** 内容 */
	private String content;

	private Date createDate;

	private SingleModel member;
	/* 图片 */
	private List<ProductImageModel> productImages;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public SingleModel getMember() {
		return member;
	}

	public void setMember(SingleModel member) {
		this.member = member;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public List<ProductImageModel> getProductImages() {
		return productImages;
	}

	public void setProductImages(List<ProductImageModel> productImages) {
		this.productImages = productImages;
	}

	public void copyFrom(Review review) {
		this.type = review.getType();
		this.content = review.getContent();
		this.score = review.getScore();
		this.productImages = ProductImageModel.bindData(review.getImages());
		this.createDate = review.getCreateDate();
		SingleModel model = new SingleModel();
		Member member = review.getMember();
		if(member!=null){
			model.setId(member.getId());
			model.setName(member.getName());
		}
		this.member = model;
		this.member = model;

	}
	
	public static List<ReviewProductModel> bindData(List<Review> reviews) {
		List<ReviewProductModel> models = new ArrayList<ReviewProductModel>();
		for (Review review:reviews) {
			ReviewProductModel model = new ReviewProductModel();
			model.copyFrom(review);
			models.add(model);
		}
		return models;
	}
	
}
