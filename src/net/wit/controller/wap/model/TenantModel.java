package net.wit.controller.wap.model;

import java.math.BigDecimal;
import java.util.Set;

import net.wit.controller.app.model.BaseModel;
import net.wit.controller.app.model.SingleModel;
import net.wit.controller.app.model.TagModel;
import net.wit.entity.ProductCategoryTenant;
import net.wit.entity.Promotion;
import net.wit.entity.Tenant;
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
	/** 店铺地址 */
	private String address;
	/** 联系电话 */
	private String telephone;
	
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}


	/**优惠券*/
	private Set<CouponModel> coupons;
	
	public Set<CouponModel> getCoupons() {
		return coupons;
	}
	public void setCoupons(Set<CouponModel> coupons) {
		this.coupons = coupons;
	}
	

	/** 商品*/
	private Set<ProductModel> products;

	public Set<ProductModel> getProducts() {
		return products;
	}
	public void setProducts(Set<ProductModel> products) {
		this.products = products;
	}
	/**店铺产品分类*/
	private Set<ProductCategoryTenantModel> productCategoryTenants;
	
	public Set<ProductCategoryTenantModel> getProductCategoryTenants() {
		return productCategoryTenants;
	}
	public void setProductCategoryTenants(Set<ProductCategoryTenantModel> productCategoryTenants) {
		this.productCategoryTenants = productCategoryTenants;
	}


	/**促销*/
	private Set<PromotionModel> promotions;
	
	public Set<PromotionModel> getPromotions() {
		return promotions;
	}
	public void setPromotions(Set<PromotionModel> promotions) {
		this.promotions = promotions;
	}

	/**关注会员*/
	private Long favoriteNum;
	
	public Long getFavoriteNum() {
		return favoriteNum;
	}
	public void setFavoriteNum(Long favoriteNum) {
		this.favoriteNum = favoriteNum;
	}
	
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
		this.products=ProductModel.bindData(tenant.getProducts());//店铺商品
		this.promotions=PromotionModel.bindData(tenant.getPromotions());//促销
		this.coupons=CouponModel.bindData(tenant.getCoupons());//优惠券
		this.productCategoryTenants=ProductCategoryTenantModel.bindData
				(tenant.getProductCategoryTenants());//店铺商品分类
		this.telephone=tenant.getTelephone();
		this.hits = tenant.getHits();
		this.grade = tenant.getScore();
		SingleModel member = new SingleModel();
		member.setId(tenant.getMember().getId());
		member.setName(tenant.getMember().getUsername());
		this.member = member;
		this.tamPo = tenant.getTamPo();
		this.noReason = tenant.getNoReason();
		this.toPay = tenant.getToPay();
		this.address=tenant.getAddress();
		
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
		this.favoriteNum=new Long(tenant.getFavoriteMembers().size());//关注会员人数
		if (tenant.getFreight()!=null) {
		   this.freightDescr = "已设置";
		} else {
		   this.freightDescr = "未设置";
		}
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
}
