/**
 *====================================================
 * 文件名称: MemberInfoModel.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014-9-11			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.controller.ajax.model;

import java.math.BigDecimal;
import java.util.Date;

import net.wit.entity.Tenant;


/**
 * @ClassName: MemberInfoModel
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014-9-11 上午11:38:58
 */
public class MemberInfoModel {

	/** 用户名 */
	private String username;

	/** 余额 */
	private BigDecimal balance;

	/** 会员等级 */
	private String memberRankName;
	
	/** 积分 */
	private Long point;

	/** 优惠券 */
	private Long couponCodes;
	
	/** 收藏数 */
	private Long favorite;
	
	/** 推广数 */
	private Long extension;
	
	/** 企业号 */
	private Long tenantId;

	/** 实体店 */
	private Long deliveryId;
	
	/** 企业名 */
	private String tenantName;
	
	/** 店铺LOGO */
	private String logo;
	
	Tenant.Status status;
	
	/** 主机地址 */
	private String hostname;

	/** 主机端口 */
	private Long port;

	/** 数据库 */
	private Long dbid;
	
	private BigDecimal fee;
	
	private Date availablePeriod;
	
	private String appId;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getMemberRankName() {
		return memberRankName;
	}

	public void setMemberRankName(String memberRankName) {
		this.memberRankName = memberRankName;
	}

	public Long getPoint() {
		return point;
	}

	public void setPoint(Long point) {
		this.point = point;
	}

	public Long getCouponCodes() {
		return couponCodes;
	}

	public void setCouponCodes(Long couponCodes) {
		this.couponCodes = couponCodes;
	}

	public Long getFavorite() {
		return favorite;
	}

	public void setFavorite(Long favorite) {
		this.favorite = favorite;
	}

	public Long getExtension() {
		return extension;
	}

	public void setExtension(Long extension) {
		this.extension = extension;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public Long getPort() {
		return port;
	}

	public void setPort(Long port) {
		this.port = port;
	}

	public Long getDbid() {
		return dbid;
	}

	public void setDbid(Long dbid) {
		this.dbid = dbid;
	}

	public Date getAvailablePeriod() {
		return availablePeriod;
	}

	public void setAvailablePeriod(Date availablePeriod) {
		this.availablePeriod = availablePeriod;
	}

	public Tenant.Status getStatus() {
		return status;
	}

	public void setStatus(Tenant.Status status) {
		this.status = status;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Long getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(Long deliveryId) {
		this.deliveryId = deliveryId;
	}

		
}
