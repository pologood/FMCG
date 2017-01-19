package net.wit.controller.assistant.model;

import net.wit.controller.app.model.BaseModel;
import net.wit.controller.app.model.*;
import net.wit.entity.*;
import net.wit.entity.Tenant.Status;
import net.wit.util.SpringUtils;

import java.math.BigDecimal;
import java.util.*;

public class TenantListModel extends BaseModel {
	
	/** ID   */
	private Long id;
	/** 简称  */
	private String shortName;
	/** 缩影图 */
	private String thumbnail;
	/** 主营类目 */
	private String tenantCategoryName;
	/** 商品数量 */
	private Integer products;
	/** 活动信息 */
	private Set<DescPromotionModel> promotions;
	/**地址*/
	private String address;
	/** 平均评分 */
	private Float grade;
	/**佣金*/
	private BigDecimal brokerage;
	/**联盟佣金*/
	private BigDecimal agency;

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
	public Float getGrade() {
		return grade;
	}
	public void setGrade(Float grade) {
		this.grade = grade;
	}


	public Set<DescPromotionModel> getPromotions() {
		return promotions;
	}
	public void setPromotions(Set<DescPromotionModel> promotions) {
		this.promotions = promotions;
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

	public Integer getProducts() {
		return products;
	}

	public void setProducts(Integer products) {
		this.products = products;
	}

	public BigDecimal getBrokerage() {
		return brokerage;
	}

	public void setBrokerage(BigDecimal brokerage) {
		this.brokerage = brokerage;
	}

	public BigDecimal getAgency() {
		return agency;
	}

	public void setAgency(BigDecimal agency) {
		this.agency = agency;
	}

	public void copyFrom(Tenant tenant) {
		this.id = tenant.getId();
		this.grade = tenant.getScore();
		this.shortName = tenant.getShortName();
		if (tenant.getTenantCategory()!=null) {
			this.tenantCategoryName = tenant.getTenantCategory().getName();
		} else {
			this.tenantCategoryName = SpringUtils.abbreviate(tenant.getIntroduction(),20,"..");
		}
		this.thumbnail = tenant.getThumbnail();
		if (this.thumbnail==null) {
			this.thumbnail = tenant.getLogo();
		}
		this.address = tenant.getAddress();
		this.brokerage = tenant.getBrokerage();
		this.agency = tenant.getAgency();
		this.products = tenant.getProducts().size();
	}

	public void copyFrom(Tenant tenant,Location location) {
		copyFrom(tenant);
	}
	
	public static List<TenantListModel> bindData(List<Tenant> tenants) {
		List<TenantListModel> models = new ArrayList<TenantListModel>();
		for (Tenant tenant:tenants) {
			TenantListModel model = new TenantListModel();
			model.copyFrom(tenant);
			models.add(model);
		}
		return models;
	}
	
	public static List<TenantListModel> bindData(List<Tenant> tenants, Location location) {
		List<TenantListModel> models = new ArrayList<TenantListModel>();
		for (Tenant tenant:tenants) {
			TenantListModel model = new TenantListModel();
			model.copyFrom(tenant);
			model.bindPromoton(tenant.getVaildPromotions());
			models.add(model);
		}
		return models;
	}

	public static Set<TenantListModel> bindData(Set<Tenant> tenants, Location location) {
		Set<TenantListModel> models = new HashSet<TenantListModel>(tenants.size());
		for (Tenant tenant:tenants) {
			TenantListModel model = new TenantListModel();
			model.copyFrom(tenant);
			models.add(model);
		}
		return models;
	}

	
	public static Set<TenantListModel> bindData(Set<Tenant> tenants) {
		Set<TenantListModel> models = new HashSet<TenantListModel>(tenants.size());
		for (Tenant tenant:tenants) {
			TenantListModel model = new TenantListModel();
			model.copyFrom(tenant);
			models.add(model);
		}
		return models;
	}

	public static List<TenantListModel> bindRelations(List<TenantRelation> tenants) {
		List<TenantListModel> models = new ArrayList<TenantListModel>();
		for (TenantRelation relation:tenants) {
			TenantListModel model = new TenantListModel();
			model.copyFrom(relation.getParent());
			MemberRankModel memberRankModel = new MemberRankModel();
			if (relation.getMemberRank()!=null) {
			   memberRankModel.copyFrom(relation.getMemberRank());
			} else {
			  memberRankModel.setName("待审核");
			}
			models.add(model);
		}
		return models;
	}
	
	public void bindProducts(List<Product> prods) {
		Set<ProductListModel> models = new HashSet<ProductListModel>();
		for (Product product:prods) {
			ProductListModel model = new ProductListModel();
			model.copyFrom(product);
			models.add(model);
		}
	}
	
	public void bindPromoton(Set<Promotion> promotions) {
		Set<DescPromotionModel> models = new HashSet<DescPromotionModel>();
		for (Promotion promotion:promotions) {
			DescPromotionModel model = new DescPromotionModel();
			model.copyFrom(promotion);
			models.add(model);
		}
		this.promotions = models;
	}
}
