package net.wit.controller.app.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.wit.entity.DeliveryCenter;
import net.wit.entity.Location;
import net.wit.entity.Product;
import net.wit.entity.Promotion;
import net.wit.entity.Tenant;
import net.wit.entity.Tenant.Status;
import net.wit.entity.TenantRelation;
import net.wit.util.MapUtils;
import net.wit.util.SpringUtils;

public class TenantListModel extends BaseModel {
	
	/** ID   */
	private Long id;
	/** 简称  */
	private String shortName;
	/** 店主 */
	private MemberListModel member;
	/** 缩影图 */
	private String thumbnail;
	/** 主营类目 */
	private String tenantCategoryName;
	/*我的等级*/
	private MemberRankModel memberRank;
	/** 推荐商品 */
	private Set<ProductListModel> products;
	/** 活动信息 */
	private Set<PromotionModel> promotions;
	/*距离*/
	private double distance;
	/**地址*/
	private String address;
	/**商圈*/
	private String communityName;
	/** 平均评分 */
	private Float grade;
	/** 点击数 */
	private Long hits;
	/** 月销售额 */
	private BigDecimal monthSales;
	/** 状态 */
	private  Status status;
	/** 最近实体店 */
	private DeliveryCenterModel defaultDeliveryCenter;
	
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
	
	public String getCommunityName() {
		return communityName;
	}
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
	public MemberListModel getMember() {
		return member;
	}
	public void setMember(MemberListModel member) {
		this.member = member;
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
	public Long getHits() {
		return hits;
	}
	public void setHits(Long hits) {
		this.hits = hits;
	}
	
	public MemberRankModel getMemberRank() {
		return memberRank;
	}
	public void setMemberRank(MemberRankModel memberRank) {
		this.memberRank = memberRank;
	}
	public Set<ProductListModel> getProducts() {
		return products;
	}
	public void setProducts(Set<ProductListModel> products) {
		this.products = products;
	}

	public Set<PromotionModel> getPromotions() {
		return promotions;
	}
	public void setPromotions(Set<PromotionModel> promotions) {
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
		
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}

	public BigDecimal getMonthSales() {
		return monthSales;
	}

	public void setMonthSales(BigDecimal monthSales) {
		this.monthSales = monthSales;
	}

	public DeliveryCenterModel getDefaultDeliveryCenter() {
		return defaultDeliveryCenter;
	}

	public void setDefaultDeliveryCenter(DeliveryCenterModel defaultDeliveryCenter) {
		this.defaultDeliveryCenter = defaultDeliveryCenter;
	}

	public void copyFrom(Tenant tenant) {
		this.id = tenant.getId();
		this.hits = tenant.getHits();
		this.grade = tenant.getScore();
		this.shortName = tenant.getShortName();
		//this.status = tenant.getStatus();
		MemberListModel member = new MemberListModel();
		member.copyFrom(tenant.getMember(),null);
		this.member = member;
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
		this.communityName = "";
		this.monthSales=tenant.getMonthSales();
		this.status = tenant.getStatus();
		DeliveryCenterModel deliveryCenterModel=new DeliveryCenterModel();
		if (tenant.getDefaultDeliveryCenter()!=null) {
		    deliveryCenterModel.copyFrom(tenant.getDefaultDeliveryCenter());
		}
		this.defaultDeliveryCenter=deliveryCenterModel;
	}

	public void copyFrom(Tenant tenant,Location location) {
		copyFrom(tenant);
		DeliveryCenter delivery = tenant.nearDeliveryCenter(location);
		if(delivery!=null){
			DeliveryCenterModel deliveryCenterModel=new DeliveryCenterModel();
			deliveryCenterModel.copyFrom(delivery);
			this.defaultDeliveryCenter=deliveryCenterModel;
		}
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
	
	public static List<TenantListModel> bindData(List<Tenant> tenants,Location location) {
		List<TenantListModel> models = new ArrayList<TenantListModel>();
		for (Tenant tenant:tenants) {
			TenantListModel model = new TenantListModel();
			model.copyFrom(tenant);
			model.setDistance(tenant.distatce(location));
			DeliveryCenter delivery = tenant.nearDeliveryCenter(location);
			if (delivery!=null) {
				model.setAddress(delivery.getAddress());
				if (delivery.getCommunity()!=null) {
				    model.setCommunityName(delivery.getCommunity().getName());
				}
			}
			model.bindPromoton(tenant.getVaildPromotions());
			models.add(model);
		}
		return models;
	}

	public static Set<TenantListModel> bindData(Set<Tenant> tenants,Location location) {
		Set<TenantListModel> models = new HashSet<TenantListModel>(tenants.size());
		for (Tenant tenant:tenants) {
			TenantListModel model = new TenantListModel();
			model.copyFrom(tenant);
			model.setDistance(tenant.distatce(location));
			DeliveryCenter delivery = tenant.nearDeliveryCenter(location);
			if (delivery!=null) {
				model.setAddress(delivery.getAddress());
				if (delivery.getCommunity()!=null) {
				    model.setCommunityName(delivery.getCommunity().getName());
				}
			}
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
			model.memberRank = memberRankModel;
			models.add(model);
		}
		return models;
	}
	
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public void bindProducts(List<Product> prods) {
		Set<ProductListModel> models = new HashSet<ProductListModel>();
		for (Product product:prods) {
			ProductListModel model = new ProductListModel();
			model.copyFrom(product);
			models.add(model);
		}
		this.products = models;
	}
	
	public void bindPromoton(Set<Promotion> promotions) {
		Set<PromotionModel> models = new HashSet<PromotionModel>();
		for (Promotion promotion:promotions) {
			PromotionModel model = new PromotionModel();
			model.copyFrom(promotion);
			models.add(model);
		}
		this.promotions = models;
	}
}
