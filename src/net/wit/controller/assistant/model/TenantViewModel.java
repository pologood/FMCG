package net.wit.controller.assistant.model;

import net.wit.entity.*;
import net.wit.entity.Tenant.Status;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TenantViewModel extends BaseModel {
	
	/** ID   */
	private Long id;
	/** 企业编码 */
	private String code;
	/** 公司  */
	private String name;
	/** 简称  */
	private String shortName;
	/** logo */
	private String logo;
	/** 门头 */
	private String thumbnail;
	/** 平均评分 */
	private Float grade;
	/** 介绍 */
	private String introduction;
	/** 联系电话 */
	private String telephone;
	/** 店主 */
	private SingleModel member;
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
	/**七天退货*/
	private Boolean noReason;
	/** 微信号 */
	private String weixin;
	/**微信公众号ID*/
	private String appId;
	/** 主营类目 */
	private SingleModel tenantCategory;
	/** 全部商品数 */
	private Integer productCount;
	/**店铺装修*/
	private String shopDecoration;

	public String getShopDecoration() {
		return shopDecoration;
	}

	public void setShopDecoration(String shopDecoration) {
		this.shopDecoration = shopDecoration;
	}

	public Float getGrade() {
		return grade;
	}

	public void setGrade(Float grade) {
		this.grade = grade;
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
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public Integer getProductCount() {
		return productCount;
	}
	public void setProductCount(Integer productCount) {
		this.productCount = productCount;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public void copyFrom(Tenant tenant) {
		this.id = tenant.getId();
		this.name = tenant.getName();
		this.shortName = tenant.getShortName();
		this.grade = tenant.getScore();
		this.code = tenant.getCode();
		this.introduction = tenant.getIntroduction();
		this.logo = tenant.getLogo();
		this.thumbnail = tenant.getThumbnail();
		SingleModel member = new SingleModel();
		member.setId(tenant.getMember().getId());
		member.setName(tenant.getMember().getUsername());
		this.member = member;
		this.tamPo = tenant.getTamPo();
		this.noReason = tenant.getNoReason();
		this.toPay = tenant.getToPay();
		this.telephone = tenant.getTelephone();
		if (this.telephone==null) {
			this.telephone = tenant.getMember().getMobile();
		}
		if(tenant.getTenantWechat()!=null){
			if (tenant.getTenantWechat().getAppSecret()!=null&&tenant.getTenantWechat().getAppId()!=null&&tenant.getTenantWechat().getWeixin_code()!=null) {
				this.weixin = tenant.getTenantWechat().getWeixin_code();
				this.appId = tenant.getTenantWechat().getAppId();
			}
		}

		if (tenant.getTenantCategory()!=null) {
		   SingleModel tenantCategory = new SingleModel();
		   tenantCategory.setId(tenant.getTenantCategory().getId());
		   tenantCategory.setName(tenant.getTenantCategory().getName());
		   this.tenantCategory = tenantCategory;
		}
		this.deliverys = new Long(tenant.getDeliveryCenters().size());
		if (tenant.getFreight()!=null) {
			DecimalFormat df   =new   DecimalFormat("#");
			this.freightDescr = "默认"+df.format(tenant.getFreight().getFirstPrice())+"元";
		} else {
		   this.freightDescr = "未设置";
		}
		this.productCount=tenant.getProducts().size();
		for(Iterator<Product> iterator=tenant.getProducts().iterator();iterator.hasNext();){
			Product product=iterator.next();
			if(!product.getIsMarketable()){
				iterator.remove();
			}
		}
		this.productCount=tenant.getProducts().size();
	}


}
