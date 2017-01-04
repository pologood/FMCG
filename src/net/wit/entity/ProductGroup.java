package net.wit.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import net.wit.BigDecimalNumericFieldBridge;

import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.NumericField;
import org.hibernate.search.annotations.Similarity;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.wltea.analyzer.lucene.IKAnalyzer;
import org.wltea.analyzer.lucene.IKSimilarity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

/**
 * Entity-商品套餐
 * @author Administrator
 *
 */
@Indexed
@Similarity(impl = IKSimilarity.class)
@Entity
@Table(name = "xx_product_group")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_product_group_sequence")
public class ProductGroup extends BaseEntity {

	private static final long serialVersionUID = 7195847619685986666L;

	/** 运费类型 */
	public enum FreightType {
		/** 卖家承运 */
		tenant,
		/** 买家承运 */
		member,
		/** 双方协商 */
		waitDefined
	}

	/** 套餐类型 */
	public enum GroupType {
		/** 积压件 */
		backLog,
		/** 新品套餐 */
		newProduct,
		/** 热门商品套餐 */
		hotProduct,
		/** 促销套餐 */
		promotion
	}

	/** 编号 */
	@Expose
	@JsonProperty
	@Pattern(regexp = "^[0-9a-zA-Z_-]+$")
	@Length(max = 100)
	@Column(nullable = false, unique = true, length = 100)
	private String sn;

	/** 套餐名称 */
	@Expose
	@JsonProperty
	@Field(store = Store.YES, index = Index.TOKENIZED, analyzer = @Analyzer(impl = IKAnalyzer.class))
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	private String name;

	/** 介绍 */
	@Expose
	@JsonProperty
	@Field(store = Store.YES, index = Index.TOKENIZED, analyzer = @Analyzer(impl = IKAnalyzer.class))
	@NotEmpty
	private String introduction;

	/** 套餐类型 */
	@Expose
	@JsonProperty
	@NotEmpty
	private GroupType groupType;

	/** 运费类型 */
	@Expose
	@JsonProperty
	@NotEmpty
	private FreightType freightType;

	/** 运费 */
	@Expose
	@JsonProperty
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	private BigDecimal freight;

	/** 打包价 */
	@Expose
	@JsonProperty
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@NumericField
	@FieldBridge(impl = BigDecimalNumericFieldBridge.class)
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal groupPrice;

	/** 是否上架 */
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	@NotNull
	@Column(nullable = false)
	private Boolean isMarketable;
	
	/** 销量 */
	@Expose
	@JsonProperty
	@Column(nullable = false)
	private Long sales;
	
	/** 区域 */
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	private Area area;
	
	/** 商家 */
	@Expose
	@ManyToOne(fetch = FetchType.LAZY)
	private Tenant tenant;

	/** 商品项 */
	@Expose
	@JsonProperty
	@OneToMany(mappedBy = "productGroup", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<ProductItem> productItems = new HashSet<ProductItem>();

	/** 订单项 */
	@OneToMany(mappedBy = "productGroup", fetch = FetchType.LAZY)
	private Set<OrderItem> orderItems = new HashSet<OrderItem>();

	/** 获取总价 */
	@JsonProperty
	public BigDecimal getTotalPrice() {
		BigDecimal totalPrice = BigDecimal.ZERO;
		if (productItems != null && productItems.size() > 0) {
			for (ProductItem productItem : productItems) {
				totalPrice.add(productItem.getTotalPrice());
			}
		}
		return totalPrice;
	}

	/** 获取商品总数量 */
	@JsonProperty
	public int getTotalQuantity() {
		int totalQuantity = 0;
		if (productItems != null && productItems.size() > 0) {
			for (ProductItem productItem : productItems) {
				totalQuantity += productItem.getQuantity();
			}
		}
		return totalQuantity;
	}

	/** 获取商品种类数量 */
	@JsonProperty
	public int getCount(){
		int count =0;
		if(productItems != null && productItems.size() > 0){
			count = productItems.size();
		}
		return count;
	}
	
	/** 获取折扣 */
	@JsonProperty
	@Field(store = Store.YES, index = Index.UN_TOKENIZED)
	public int getOff() {
		int off = 0;
		if (BigDecimal.ZERO != groupPrice && BigDecimal.ZERO != getTotalPrice()) {
			off = groupPrice.divide(getTotalPrice())
					.multiply(new BigDecimal(10)).intValue();
		}
		if (off < 1) {
			off = 1;
		}
		return off;
	}

	/** 获取缩略图 */
	@JsonProperty
	public String getThumbnail() {
		if (productItems != null && productItems.size() > 0) {
			return new ArrayList<ProductItem>(productItems).get(0).getProduct().getThumbnail();
		}
		return null;
	}
	// ======================================getter/setter===========================================//

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public GroupType getGroupType() {
		return groupType;
	}

	public void setGroupType(GroupType groupType) {
		this.groupType = groupType;
	}

	public FreightType getFreightType() {
		return freightType;
	}

	public void setFreightType(FreightType freightType) {
		this.freightType = freightType;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	public BigDecimal getGroupPrice() {
		return groupPrice;
	}

	public void setGroupPrice(BigDecimal groupPrice) {
		this.groupPrice = groupPrice;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public Set<ProductItem> getProductItems() {
		return productItems;
	}

	public void setProductItems(Set<ProductItem> productItems) {
		this.productItems = productItems;
	}

	public Set<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(Set<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Boolean getIsMarketable() {
		return isMarketable;
	}

	public void setIsMarketable(Boolean isMarketable) {
		this.isMarketable = isMarketable;
	}

	public Long getSales() {
		return sales;
	}

	public void setSales(Long sales) {
		this.sales = sales;
	}

}
