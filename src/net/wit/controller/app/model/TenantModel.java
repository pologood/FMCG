package net.wit.controller.app.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.Column;

import com.sun.org.apache.xpath.internal.operations.Bool;
import net.wit.entity.*;
import net.wit.entity.Tenant.Status;

public class TenantModel extends BaseModel {
	
	/** ID   */
	private Long id;
	/** 企业编码 */
	private String code;
	/** 公司  */
	private String name;
	/** 简称  */
	private String shortName;
	/** 状态  */
	private Status status;
	/** 佣金比率 */
	private BigDecimal brokerage;

	/** logo */
	private String logo;
	/** 门头 */
	private String thumbnail;
	/** 介绍 */
	private String introduction;
	/** 联系电话 */
	private String telephone;
	/** 店主 */
	private SingleModel member;
	
	/** 标签 */
	private Set<TagModel> tags;
	
	/** 平均评分 */
	private Float grade;
	/** 点击数 */
	private Long hits;
	/** 实体店数 */
	private Long deliverys;
	/** 员工人数 */
	private Long employees;
	/** 运费说明 */
	private String freightDescr;
	
	/** 货到付款 */
	private Boolean toPay;
	/** 担保交易 */
	private Boolean tamPo;
	/** 七天退货 */
	private Boolean noReason;
	
	/** 微信号 */
	private String weixin;
	/** 主营类目 */
	private SingleModel tenantCategory;
	/** 活动信息 */
	private Set<PromotionModel> promotions;
	/** 收藏人数 */
	private Integer favoriteMemberCount;
	/** 全部商品数 */
	private Integer productCount;
	/** 最近实体店 */
	private DeliveryCenterModel defaultDeliveryCenter;
	/** 商圈展示图片 */
	private String communityImage;
	/**是否收藏*/
	private Boolean hasFavorite;
	
	/** 营业时间 */
	private String hours;
	/** 距离 */
	private double distance;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public BigDecimal getBrokerage() {
		return brokerage;
	}
	public void setBrokerage(BigDecimal brokerage) {
		this.brokerage = brokerage;
	}

	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public Set<TagModel> getTags() {
		return tags;
	}
	public void setTags(Set<TagModel> tags) {
		this.tags = tags;
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
	
	public SingleModel getMember() {
		return member;
	}
	public void setMember(SingleModel member) {
		this.member = member;
	}
	
	public Long getDeliverys() {
		return deliverys;
	}
	public void setDeliverys(Long deliverys) {
		this.deliverys = deliverys;
	}
	public Long getEmployees() {
		return employees;
	}
	public void setEmployees(Long employees) {
		this.employees = employees;
	}
	
	public String getFreightDescr() {
		return freightDescr;
	}
	public void setFreightDescr(String freightDescr) {
		this.freightDescr = freightDescr;
	}
	public Boolean getToPay() {
		return toPay;
	}
	public void setToPay(Boolean toPay) {
		this.toPay = toPay;
	}
	public Boolean getTamPo() {
		return tamPo;
	}
	public void setTamPo(Boolean tamPo) {
		this.tamPo = tamPo;
	}
	public Boolean getNoReason() {
		return noReason;
	}
	public void setNoReason(Boolean noReason) {
		this.noReason = noReason;
	}	
	
	public String getWeixin() {
		return weixin;
	}
	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}
	public SingleModel getTenantCategory() {
		return tenantCategory;
	}
	public void setTenantCategory(SingleModel tenantCategory) {
		this.tenantCategory = tenantCategory;
	}
	
	public Set<PromotionModel> getPromotions() {
		return promotions;
	}
	public void setPromotions(Set<PromotionModel> promotions) {
		this.promotions = promotions;
	}	
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Integer getFavoriteMemberCount() {
		return favoriteMemberCount;
	}

	public void setFavoriteMemberCount(Integer favoriteMemberCount) {
		this.favoriteMemberCount = favoriteMemberCount;
	}

	public Integer getProductCount() {
		return productCount;
	}

	public void setProductCount(Integer productCount) {
		this.productCount = productCount;
	}

	public DeliveryCenterModel getDefaultDeliveryCenter() {
		return defaultDeliveryCenter;
	}
	public void setDefaultDeliveryCenter(DeliveryCenterModel defaultDeliveryCenter) {
		this.defaultDeliveryCenter = defaultDeliveryCenter;
	}

	public String getCommunityImage() {
		return communityImage;
	}

	public void setCommunityImage(String communityImage) {
		this.communityImage = communityImage;
	}

	public Boolean getHasFavorite() {
		return hasFavorite;
	}

	public void setHasFavorite(Boolean hasFavorite) {
		this.hasFavorite = hasFavorite;
	}

	public String getHours() {
		return hours;
	}
	public void setHours(String hours) {
		this.hours = hours;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public void copyFrom(Tenant tenant) {
		this.id = tenant.getId();
		this.name = tenant.getName();
		this.shortName = tenant.getShortName();
		this.status = tenant.getStatus();
		this.code = tenant.getCode();
		this.brokerage = tenant.getBrokerage();
		this.introduction = tenant.getIntroduction();
		this.logo = tenant.getLogo();
		this.thumbnail = tenant.getThumbnail();
		this.tags = TagModel.bindData(tenant.getTags());
		this.hits = tenant.getHits();
		this.grade = tenant.getScore();
		SingleModel member = new SingleModel();
		member.setId(tenant.getMember().getId());
		member.setName(tenant.getMember().getUsername());
		this.member = member;
		this.tamPo = tenant.getTamPo();
		this.noReason = tenant.getNoReason();
		this.toPay = tenant.getToPay();
		this.telephone = tenant.getTelephone();
		this.hours = tenant.getHours();
		if (this.telephone==null) {
			this.telephone = tenant.getMember().getMobile();
		}
		if (tenant.getTenantWechat()!=null) {
		   this.weixin = tenant.getTenantWechat().getWeixin_code();
		}
		if (tenant.getTenantCategory()!=null) {
		   SingleModel tenantCategory = new SingleModel();
		   tenantCategory.setId(tenant.getTenantCategory().getId());
		   tenantCategory.setName(tenant.getTenantCategory().getName());
		   this.tenantCategory = tenantCategory;
		}
		this.deliverys = new Long(tenant.getDeliveryCenters().size());
		this.employees = new Long(tenant.getMembers().size());
		if (tenant.getFreight()!=null) {
		   this.freightDescr = "已设置";
		} else {
		   this.freightDescr = "未设置";
		}
		this.favoriteMemberCount=tenant.getFavoriteMembers().size();
		this.productCount=tenant.getProducts().size();
		for(Iterator<Product> iterator=tenant.getProducts().iterator();iterator.hasNext();){
			Product product=iterator.next();
			if(!product.getIsMarketable()){
				iterator.remove();
			}
		}
		if (tenant.getDefaultDeliveryCenter()!=null) {
		   DeliveryCenterModel deliveryCenter = new DeliveryCenterModel();
		   deliveryCenter.copyFrom(tenant.getDefaultDeliveryCenter());
		   this.defaultDeliveryCenter = deliveryCenter;
		}
		this.productCount=tenant.getProducts().size();
		this.hasFavorite=false;
	}

	public void copyFrom(Tenant tenant, Location location){
		copyFrom(tenant);
		DeliveryCenterModel deliveryCenterModel = new DeliveryCenterModel();
		DeliveryCenter near=tenant.nearDeliveryCenter(location);
		deliveryCenterModel.copyFrom(near);
		this.defaultDeliveryCenter = deliveryCenterModel;
		if(near.getCommunity()!=null){
			this.communityImage=near.getCommunity().getImage();
		}
		this.distance=tenant.distatce(location);
	}

	public void copyFrom(Tenant tenant, Location location,Member member) {
		copyFrom(tenant,location);
		if(member!=null&&member.getFavoriteTenants().contains(tenant)){
			this.hasFavorite=true;
		}
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
