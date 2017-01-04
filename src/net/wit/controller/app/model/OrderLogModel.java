package net.wit.controller.app.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;

import net.wit.entity.OrderLog;
import net.wit.entity.OrderLog.Type;

public class OrderLogModel extends BaseModel {
	
	private Type type;

	/** 操作员 */
	@Column(updatable = false)
	private String operator;

	/** 内容 */
	private String content;
	
	/** 时间 */
	private Date createDate;
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void copyFrom(OrderLog log) {
		this.type = log.getType();
		this.content = log.getContent();
		this.operator = log.getOperator();
		this.createDate = log.getCreateDate();
	}
	
	public static List<OrderLogModel> bindData(Set<OrderLog> logs) {
		List<OrderLogModel> models = new ArrayList<>();
		for (OrderLog orderLog:logs) {
			OrderLogModel model = new OrderLogModel();
			model.copyFrom(orderLog);
			models.add(model);
		}
		return models;
	}
	
}
