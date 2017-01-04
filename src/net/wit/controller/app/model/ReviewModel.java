package net.wit.controller.app.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import net.wit.entity.*;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

import net.wit.entity.Review.Type;

public class ReviewModel extends BaseModel {
	
	private Type type;

	/** 评分(商品评分/商家诚信服务评分/会员诚信评分) */
	private Integer score;

	/** 内容 */
	private String content;
	
	private Date createDate;

	private MemberListModel member;
	/* 图片 */
	private List<ProductImageModel> productImages;

	/**	评论的商品*/
	private OrderItemModel orderItem;

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

	public MemberListModel getMember() {
		return member;
	}

	public void setMember(MemberListModel member) {
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

	public OrderItemModel getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(OrderItemModel orderItem) {
		this.orderItem = orderItem;
	}

	public void copyFrom(Review review) {
		this.type = review.getType();
		this.content = review.getContent();
		this.score = review.getScore();
		this.productImages = ProductImageModel.bindData(review.getImages());
		this.createDate = review.getCreateDate();
		MemberListModel model = new MemberListModel();
		model.copyFrom(review.getMember(),null);
		this.member = model;
		if (review.getOrderItem() != null) {
			OrderItemModel orderItemModel = new OrderItemModel();
			orderItemModel.copyFrom(review.getOrderItem());
			this.orderItem=orderItemModel;
		}
	}
	
	public static List<ReviewModel> bindData(List<Review> reviews) {
		List<ReviewModel> models = new ArrayList<ReviewModel>();
		for (Review review:reviews) {
			ReviewModel model = new ReviewModel();
			model.copyFrom(review);
			models.add(model);
		}
		return models;
	}
	
}
