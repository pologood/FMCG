/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import net.wit.entity.Tenant.Status;
import net.wit.util.MapUtils;

import org.apache.lucene.util.NumericUtils;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Similarity;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.lucene.IKSimilarity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

/**
 * @ClassName: DeliveryCenter
 * @Description: 发货点
 * @author Administrator
 * @date 2014年10月14日 上午9:07:47
 */
@Indexed
@Similarity(impl = IKSimilarity.class)
@Entity
@Table(name = "xx_delivery_center")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_delivery_center_sequence")
public class DeliveryCenter extends BaseEntity {

	private static final long serialVersionUID = 3328996121729039075L;
	/** 编码 */
	@Length(max = 200)
	@JsonProperty
	private String sn;

	/** 名称 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	@JsonProperty
	private String name;

	/** 联系人 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	@JsonProperty
	private String contact;

	/** 地区名称 */
	@Column(nullable = false)
	@JsonProperty
	private String areaName;

	/** 地址 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	@JsonProperty
	private String address;

	/** 邮编 */
	@Length(max = 200)
	@JsonProperty
	private String zipCode;

	/** 电话 */
	@Length(max = 200)
	private String phone;

	/** 手机 */
	@Length(max = 200)
	@JsonProperty
	private String mobile;

	/** 备注 */
	@Length(max = 200)
	private String memo;

	/** 是否默认 */
	@NotNull
	@Column(nullable = false)
	@JsonProperty
	private Boolean isDefault;

	/** 地区 */
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonProperty
	private Area area;

	/** 社区 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Community community;

	/** 地理位置 */
	@Embedded
	@JsonProperty
	private Location location;

	/** 我家店铺 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Tenant tenant;

	/** 我的员工 */
	@OneToMany(mappedBy = "deliveryCenter", fetch = FetchType.LAZY)
	private Set<Employee> employees = new HashSet<Employee>();

	/** 配送评分 */
	@Expose
	@Column(nullable = false, columnDefinition = "long default 0")
	private Long assistant;

	/** 平均评分 */
	@Expose
	@JsonProperty
	@Column(nullable = false, precision = 12, scale = 6, columnDefinition = "integer default 0")
	private Float score;

	/** 总评分 */
	@Expose
	@Column(nullable = false, columnDefinition = "integer default 0")
	private Long totalScore;

	/** 评分数 */
	@Column(nullable = false, columnDefinition = "integer default 0")
	private Long scoreCount;

	/** 周边公里数 */
	public double distatce(Location location) {
		if (location==null || !location.isExists()) {
			return -1;
		}
		if (getLocation() == null) {
			return -1;
		}
		double distatce = MapUtils.getDistatce(location.getLat().doubleValue(), getLocation().getLat().doubleValue(), location.getLng().doubleValue(), getLocation().getLng().doubleValue());
		return distatce;
	}

	/** 持久化前处理 */
	@PrePersist
	public void prePersist() {
		if (getArea() != null) {
			setAreaName(getArea().getFullName());
		}
	}

	/** 更新前处理 */
	@PreUpdate
	public void preUpdate() {
		if (getArea() != null) {
			setAreaName(getArea().getFullName());
		}
	}

	/**
	 * 营业开始时间
	 */
	private Date startTime;
	/**
	 * 营业结束时间
	 */
	private Date endTime;

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	// ===========================================getter/setter===========================================//
	/**
	 * 获取编码
	 * @return 编码
	 */
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}


	/**
	 * 获取名称
	 * @return 名称
	 */
	@JsonProperty
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * @param name 名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取联系人
	 * @return 联系人
	 */
	public String getContact() {
		return contact;
	}

	/**
	 * 设置联系人
	 * @param contact 联系人
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}

	/**
	 * 获取地区名称
	 * @return 地区名称
	 */
	public String getAreaName() {
		return areaName;
	}

	/**
	 * 设置地区名称
	 * @param areaName 地区名称
	 */
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	/**
	 * 获取地址
	 * @return 地址
	 */
	@JsonProperty
	public String getAddress() {
		return address;
	}

	/**
	 * 设置地址
	 * @param address 地址
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 获取邮编
	 * @return 邮编
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * 设置邮编
	 * @param zipCode 邮编
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * 获取电话
	 * @return 电话
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 设置电话
	 * @param phone 电话
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 获取手机
	 * @return 手机
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * 设置手机
	 * @param mobile 手机
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * 获取备注
	 * @return 备注
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * 设置备注
	 * @param memo 备注
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * 获取是否默认
	 * @return 是否默认
	 */
	public Boolean getIsDefault() {
		return isDefault;
	}

	/**
	 * 设置是否默认
	 * @param isDefault 是否默认
	 */
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * 获取地区
	 * @return 地区
	 */
	public Area getArea() {
		return area;
	}

	/**
	 * 设置地区
	 * @param area 地区
	 */
	public void setArea(Area area) {
		this.area = area;
	}

	/**
	 * 所属社区
	 * @return 社区
	 */
	public Community getCommunity() {
		return community;
	}

	/**
	 * 设置社区
	 * @param community 社区
	 */
	public void setCommunity(Community community) {
		this.community = community;
	}

	
	/**
	 * 获取企业
	 * @return 所属企业
	 */
	@JsonProperty
	public Tenant getTenant() {
		return tenant;
	}

	/**
	 * 设置企业
	 * @param tenant 所属企业
	 */
	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	/**
	 * 获取经纬度
	 * @return 经纬度
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * 设置经纬度
	 * @param location 经纬度
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	public Long getAssistant() {
		return assistant;
	}

	public void setAssistant(Long assistant) {
		this.assistant = assistant;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public Long getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(Long totalScore) {
		this.totalScore = totalScore;
	}

	public Long getScoreCount() {
		return scoreCount;
	}

	public void setScoreCount(Long scoreCount) {
		this.scoreCount = scoreCount;
	}
	

	public Set<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}
	
	//以下部份关于全文搜索使用
	
	//搜索全名
	@Transient
	@Field(store = Store.YES, index = Index.TOKENIZED, analyzer = @Analyzer(impl = IKAnalyzer.class))
	public String getFullName() {
		return getTenant().getName();
	}
	
	//经营范围
	@Transient
	@Field(store = Store.YES, index = Index.TOKENIZED, analyzer = @Analyzer(impl = IKAnalyzer.class))
	public String getDescr() {
		String descr = "";
		if (getTenant().getTenantCategory()!=null) {
			descr = descr+getTenant().getTenantCategory().getName();
		}
		if (getTenant().getIntroduction()!=null) {
			descr = descr + getTenant().getIntroduction();
		}
		if (getTenant().getScopeOfBusiness()!=null) {
			descr = descr + getTenant().getScopeOfBusiness();
		}
		return descr;
	}

	//所属商圈
	@Transient
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	public String getCommunityId() {
		if (getCommunity()!=null) {
		   return NumericUtils.longToPrefixCoded(getCommunity().getId());
		} else {
		   return NumericUtils.longToPrefixCoded(0L);
		}
	}
	
	//所属城市
	@Transient
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	public String getCityId() {
		Area a = getArea().getCity();
		if (a==null) {
			return "";
		} else {
			return NumericUtils.longToPrefixCoded(a.getId());
		}
	}
	
	//所在地区
	@Transient
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	public String getAreaId() {
		return NumericUtils.longToPrefixCoded(getArea().getId());
	}

	//商家主营分类	
	@Transient
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	public String getTenantCategory() {
		if (getTenant().getTenantCategory()!=null) {
			return getTenant().getTenantCategory().getTreePath()+getTenant().getTenantCategory().getId()+TenantCategory.TREE_PATH_SEPARATOR;
		} else {
			return "";
		}
	}

	//开通状态
	@Transient
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	public String getStatus() {
		return getTenant().getStatus().toString();
	}
		
	//浏览量	
	@Transient
	@Field(store = Store.YES, index = Index.NO)
	public Long getHits() {
		return getTenant().getHits();
	}

	//商家评分
	@Transient
	@Field(store = Store.YES, index = Index.NO)
	public Float getAvgScore() {
		return getTenant().getScore();
	}

	//纬度
	@Transient
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	public String getlat() {
		if (getLocation()!=null && getLocation().getLat()!=null) {
		   return NumericUtils.doubleToPrefixCoded(getLocation().getLat().doubleValue());
		} else {
		   return NumericUtils.doubleToPrefixCoded(0);
		}
	}
	//经度
	@Transient
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	public String getlng() {
		if (getLocation()!=null && getLocation().getLng()!=null) {
			   return NumericUtils.doubleToPrefixCoded(getLocation().getLng().doubleValue());
			} else {
			   return NumericUtils.doubleToPrefixCoded(0);
			}
	}
	
}