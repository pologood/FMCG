package net.wit.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Similarity;
import org.hibernate.search.annotations.Store;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.lucene.IKSimilarity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;


/**
 * <p>
 * Title: 店铺认证
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: www.rsico.cn
 * </p>
 * <p>
 * Company: www.rsico.cn
 * </p>
 * @author shenjc
 * @version 1.0
 * @2015-9-3
 */

@Indexed
@Similarity(impl = IKSimilarity.class)
@Entity
@Table(name = "xx_authen")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_authen_sequence")
public class Authen extends BaseEntity{
	private static final long serialVersionUID = -341769727343851441L;
	
	/** 认证类型 */
	public enum AuthenType {
		/** 企业认证 */
		enterprise,
		/** 门店认证 */
		certified,
		/** 厂家认证 */
		manufacturers
		/** 在线支付 */
	}
	
	
	/** 认证类型 */
	@Expose
	@JsonProperty
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	private AuthenType authenType;
	
	/** 认证内容 */
	@Expose
	private String authenContent;
	
	
	/** 认证状态 */
	public enum AuthenStatus {
		/**未认证 */
		none,
		/**待认证 */
		wait,
		/**已认证*/
		success,
		/**已驳回*/
		fail
	}
	
	/** 认证状态 */
	@Expose
	@NotNull
	@JsonProperty
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	private AuthenStatus authenStatus;
	
	/** 店主*/
	@NotNull
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Tenant tenant;
	
	
	
	/** 企业名称*/
	@Expose
	private String name;
	
	/** 区域 */
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	private Area area;
	
	/** 证件号 */
	@Expose
	private String no;

	/** 发证日期 */
	@Expose
	private Date beginDate;

	/** 有效日期 */
	@Expose
	private Date endDate;

	/** 法人代表 */
	@Expose
	@JsonProperty
	@Column(length = 255)
	private String legalRepr;
	
	/** 地址 */
	@Expose
	@JsonProperty
	@Column(length = 255)
	@Field(store = Store.YES, index = Index.TOKENIZED, analyzer = @Analyzer(impl = IKAnalyzer.class))
	private String address;

	/** 正面图片 */
	@Expose
	private String pathFront;

	/** 反面图片 */
	@Expose
	private String pathBack;

	/** 正面图片 */
	@Transient
	private ProductImage imageFront;

	/** 反面图片 */
	@Transient
	private ProductImage imageBack;
	
	/** 标签 */
	@OneToOne(fetch = FetchType.LAZY)
	private Tag tag;
	
	/** 备注 */
	@Expose
	private String memo;

	public Authen() {
		super();
	}

	public Authen(long l) {
		super();
		setId(l);
	}

	public AuthenType getAuthenType() {
		return authenType;
	}

	public void setAuthenType(AuthenType authenType) {
		this.authenType = authenType;
	}

	public String getAuthenContent() {
		return authenContent;
	}

	public void setAuthenContent(String authenContent) {
		this.authenContent = authenContent;
	}


	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getLegalRepr() {
		return legalRepr;
	}

	public void setLegalRepr(String legalRepr) {
		this.legalRepr = legalRepr;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPathFront() {
		return pathFront;
	}

	public void setPathFront(String pathFront) {
		this.pathFront = pathFront;
	}

	public String getPathBack() {
		return pathBack;
	}

	public void setPathBack(String pathBack) {
		this.pathBack = pathBack;
	}

	public ProductImage getImageFront() {
		return imageFront;
	}

	public void setImageFront(ProductImage imageFront) {
		this.imageFront = imageFront;
	}

	public ProductImage getImageBack() {
		return imageBack;
	}

	public void setImageBack(ProductImage imageBack) {
		this.imageBack = imageBack;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public AuthenStatus getAuthenStatus() {
		return authenStatus;
	}

	public void setAuthenStatus(AuthenStatus authenStatus) {
		this.authenStatus = authenStatus;
	}


}
