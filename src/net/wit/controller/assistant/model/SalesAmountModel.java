package net.wit.controller.assistant.model;

import net.wit.entity.OrderItem;
import net.wit.entity.Promotion;

import java.math.BigDecimal;
import java.util.*;

public class SalesAmountModel extends BaseModel {
	/*等级ID*/
	private Long id;
	/*全名*/
	private String fullName;
	/*名称*/
	private String name;
	/*编码*/
	private String sn;
	/*缩略图*/
	private String thumbnail;
	/*数量*/
	private Integer quantity;
	/*价格*/
	private BigDecimal price;
	/*金额小计*/
	private BigDecimal subTotal;
	/*创建日期*/
	private Date createDate;
	/*订单人*/
	SingleModel singleModel;


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public SingleModel getSingleModel() {
		return singleModel;
	}

	public void setSingleModel(SingleModel singleModel) {
		this.singleModel = singleModel;
	}

	public void copyFrom(OrderItem orderItem) {
		this.id = orderItem.getId();
	    this.quantity = orderItem.getQuantity();
	    this.price = orderItem.getPrice();
	    this.subTotal = orderItem.getSubtotal();
	    this.fullName = orderItem.getFullName();
	    this.thumbnail = orderItem.getThumbnail();
	    this.sn = orderItem.getSn();
	    this.name = orderItem.getName();
		SingleModel model = new SingleModel();
		model.setId(orderItem.getOrder().getMember().getId());
		model.setName(orderItem.getOrder().getMember().getName());
		this.singleModel = model;
		this.createDate=orderItem.getCreateDate();
	}
	
	public static List<SalesAmountModel> bindData(List<OrderItem> orderItems) {
		List<SalesAmountModel> models = new ArrayList<SalesAmountModel>();
		for (OrderItem orderItem:orderItems) {
			SalesAmountModel model = new SalesAmountModel();
			model.copyFrom(orderItem);
			models.add(model);
		}
		return models;
	}
	
}
