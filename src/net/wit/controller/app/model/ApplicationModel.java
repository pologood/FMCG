package net.wit.controller.app.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.wit.entity.Application;
import net.wit.entity.Application.Status;

public class ApplicationModel extends BaseModel {
	
	/** 代码 */
	private String code;

	/** 名称 */
	private String name;

	/** 单价 */
	private BigDecimal price;

	private Date createDate;
	private Date validityDate;
	
	private Status status;
	private String descr;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Date getValidityDate() {
		return validityDate;
	}

	public void setValidityDate(Date validityDate) {
		this.validityDate = validityDate;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void copyFrom(Application app) {
		this.code = app.getCode();
		this.name = app.getName();
		this.price = app.getPrice();
		this.validityDate = app.getValidityDate();
		this.createDate = app.getCreateDate();
		this.status = app.getStatus();
		this.descr = "店家助手一年服务费用";
	}
	
	public static List<ApplicationModel> bindData(List<Application> apps,Date validityDate,BigDecimal agioPrice) {
		List<ApplicationModel> models = new ArrayList<ApplicationModel>();
		for (Application app:apps) {
			ApplicationModel model = new ApplicationModel();
			model.copyFrom(app);
			model.price = model.calc(validityDate, agioPrice);
			models.add(model);
		}
		return models;
	}
	
	public static List<ApplicationModel> bindData(List<Application> apps) {
		List<ApplicationModel> models = new ArrayList<ApplicationModel>();
		for (Application app:apps) {
			ApplicationModel model = new ApplicationModel();
			model.copyFrom(app);
			models.add(model);
		}
		return models;
	}

	public BigDecimal calc(Date validityDate,BigDecimal agioPrice) {
		if (this.status.equals(Status.none)) {
			BigDecimal price = agioPrice;	
			return price;
		} else {
			return BigDecimal.ZERO;
		}
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}
	
}
