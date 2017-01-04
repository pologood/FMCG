package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;

/**
 * @Description: 我推广的产品目录
 */
@Entity
@Table(name = "xx_extend_catalog")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_extend_catalog_sequence")
public class ExtendCatalog extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 是否推荐状态
	 */
	public enum  Type{
		/**未推荐*/
		notRecommended,
		/**推荐*/
		recommended
	}
	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	/** 商家 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Tenant tenant;

	/** 商品 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;

	/**推广商品赚取的金额*/
	@Min(0)
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal amount;

	/**推广商品销售的金额*/
	@Min(0)
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal salsePrice;

	/** 推广商品的次数*/
	@Min(0)
	@Column(nullable = false, precision = 21, scale = 6)
	private Long times;

	/** 推广商品的成交笔数*/
	@Min(0)
	@Column(nullable = false, precision = 21, scale = 6)
	private Long volume;

	/** 推荐理由 */
	private String descr;

	@Expose
	@JsonProperty
	@Column(nullable = false)
	/**是否推荐*/
	private Type type;

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Long getTimes() {
		return times;
	}

	public void setTimes(Long times) {
		this.times = times;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Long getVolume() {
		return volume;
	}

	public void setVolume(Long volume) {
		this.volume = volume;
	}

	public BigDecimal getSalsePrice() {
		return salsePrice;
	}

	public void setSalsePrice(BigDecimal salsePrice) {
		this.salsePrice = salsePrice;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
