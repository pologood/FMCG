package net.wit.controller.assistant.model;

import net.wit.entity.*;

import java.math.BigDecimal;
import java.util.*;

public class ScreenEquipmentListModel extends BaseModel {
	/**
	 * ID
	 */
	private Long id;
	/**
	 * 简称
	 */
	private String shortName;
	/**
	 * 缩影图
	 */
	private String thumbnail;
	/**
	 * 主营类目
	 */
	private String tenantCategoryName;
	/**
	 * 地址
	 */
	private String address;
	/**
	 * 平均评分
	 */
	private Float grade;
	/**
	 * 佣金
	 */
	private BigDecimal brokerage;
	/**
	 * 是否加入
	 */
	private Boolean isJoin;
	/**
	 * 是否推荐
	 */
	private Boolean isRecommended;
	/**
	 * 投放状态
	 */
	private UnionTenant.Status status;
	/**
	 * 状态描述
	 */
	private String desc;

	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 更新时间
	 */
	private Date modifyeDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getTenantCategoryName() {
		return tenantCategoryName;
	}

	public void setTenantCategoryName(String tenantCategoryName) {
		this.tenantCategoryName = tenantCategoryName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Float getGrade() {
		return grade;
	}

	public void setGrade(Float grade) {
		this.grade = grade;
	}

	public BigDecimal getBrokerage() {
		return brokerage;
	}

	public void setBrokerage(BigDecimal brokerage) {
		this.brokerage = brokerage;
	}

	public Boolean getIsJoin() {
		return isJoin;
	}

	public void setIsJoin(Boolean isJoin) {
		this.isJoin = isJoin;
	}

	public UnionTenant.Status getStatus() {
		return status;
	}

	public void setStatus(UnionTenant.Status status) {
		this.status = status;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifyeDate() {
		return modifyeDate;
	}

	public void setModifyeDate(Date modifyeDate) {
		this.modifyeDate = modifyeDate;
	}

	public Boolean getIsRecommended() {
		return isRecommended;
	}

	public void setIsRecommended(Boolean isRecommended) {
		this.isRecommended = isRecommended;
	}

	public void copyFrom(Equipment equipment) {
		this.id = equipment.getId();
		Tenant tenant = equipment.getTenant();
		if (tenant != null) {
			this.shortName = tenant.getShortName();
			this.thumbnail = tenant.getThumbnail();
			if (this.thumbnail == null) {
				this.thumbnail = tenant.getLogo();
			}
			this.tenantCategoryName = tenant.getTenantCategory().getName();
			this.grade = tenant.getScore();
			this.brokerage = tenant.getAgency();
			this.isRecommended = false;
			if (tenant.getTags() != null) {
				for (Tag tag : tenant.getTags()) {
					if (tag.getType() == Tag.Type.tenant) {
						this.isRecommended = true;
						break;
					}
				}
			}
		}
		if (equipment.getStore() != null) {
			this.address = equipment.getStore().getAddress();
		}
		this.createDate = equipment.getCreateDate();
		this.modifyeDate = equipment.getModifyDate();


	}

	public static List<ScreenEquipmentListModel> bindData(List<Equipment> equipments) {
		List<ScreenEquipmentListModel> models = new ArrayList<ScreenEquipmentListModel>();
		for (Equipment equipment : equipments) {
			ScreenEquipmentListModel model = new ScreenEquipmentListModel();
			model.copyFrom(equipment);
			models.add(model);
		}
		return models;
	}

	public void copyFromUnionTenant(UnionTenant unionTenant, String type) {
		this.id = unionTenant.getId();
		Equipment equipment = unionTenant.getEquipment();
		if (equipment != null) {
			Tenant tenant = equipment.getTenant();
			if (tenant != null) {
				this.shortName = tenant.getShortName();
				this.thumbnail = tenant.getThumbnail();
				if (this.thumbnail == null) {
					this.thumbnail = tenant.getLogo();
				}
				this.tenantCategoryName = tenant.getTenantCategory().getName();
				this.grade = tenant.getScore();
				this.brokerage = tenant.getAgency();
			}
			if (equipment.getStore() != null) {
				this.address = equipment.getStore().getAddress();
			}
		}
		this.status = unionTenant.getStatus();
		if (unionTenant.getStatus() == UnionTenant.Status.freezed) {
			if (type.equals("0")) {
				this.desc = "待同意";
			} else {
				this.desc = "新申请";
			}

		}
		if (unionTenant.getStatus() == UnionTenant.Status.confirmed) {
			this.desc = "已同意";
		}
		if (unionTenant.getStatus() == UnionTenant.Status.canceled) {
			this.desc = "已关闭";
		}
		this.createDate = unionTenant.getCreateDate();
		this.modifyeDate = unionTenant.getModifyDate();

	}

	public static List<ScreenEquipmentListModel> bindScreenData(List<UnionTenant> unionTenants, String type) {
		List<ScreenEquipmentListModel> models = new ArrayList<ScreenEquipmentListModel>();
		for (UnionTenant unionTenant : unionTenants) {
			ScreenEquipmentListModel model = new ScreenEquipmentListModel();
			model.copyFromUnionTenant(unionTenant, type);
			models.add(model);
		}
		return models;
	}

}
