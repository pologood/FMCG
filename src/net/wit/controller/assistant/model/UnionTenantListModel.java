package net.wit.controller.assistant.model;

import net.wit.entity.*;

import java.math.BigDecimal;
import java.util.*;

public class UnionTenantListModel extends BaseModel {
	/*ID*/
	private Long id;
	/*联盟名称 */
	private String name;
	/*背景图*/
	private String image;
	/*联盟佣金*/
	private BigDecimal brokerage;
	/*我的佣金*/
	private BigDecimal myBrokerage;
	/* 联盟商家数量 */
	private Integer tenantNumber;
	/*是否加入联盟*/
	private Boolean isUnion;
	public Boolean getIsUnion() {
		return isUnion;
	}

	public void setIsUnion(Boolean isUnion) {
		this.isUnion = isUnion;
	}

	public Integer getTenantNumber() {
		return tenantNumber;
	}

	public void setTenantNumber(Integer tenantNumber) {
		this.tenantNumber = tenantNumber;
	}

	public BigDecimal getBrokerage() {
		return brokerage;
	}

	public void setBrokerage(BigDecimal brokerage) {
		this.brokerage = brokerage;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getMyBrokerage() {
		return myBrokerage;
	}

	public void setMyBrokerage(BigDecimal myBrokerage) {
		this.myBrokerage = myBrokerage;
	}

	public void copyFrom(UnionTenant unionTenant, Tenant tenant) {
        this.id = unionTenant.getUnion().getId();
		this.name = unionTenant.getUnion().getName();
		this.brokerage = unionTenant.getUnion().getBrokerage();
		this.image = unionTenant.getUnion().getImage();
		this.tenantNumber = unionTenant.getUnion().getTenantNumber();
		this.myBrokerage = tenant.getAgency();
		if(unionTenant.getTenant() ==tenant){
			this.isUnion = true;
		}else {
			this.isUnion = false;
		}

	}
	
	public static  List<UnionTenantListModel> bindData(List<UnionTenant> unionTenants, Tenant tenant) {
		List<UnionTenantListModel> models = new ArrayList<UnionTenantListModel>();
		for (UnionTenant unionTenant:unionTenants) {
			UnionTenantListModel model = new UnionTenantListModel();
			model.copyFrom(unionTenant,tenant);
			models.add(model);
		}
		return models;
	}
	
}
