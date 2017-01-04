package net.wit.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import net.wit.entity.ProductImage.ImageType;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
/**
 * Entity-商品套餐项
 * @author Administrator
 *
 */
@Entity
@Table(name = "xx_product_item")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_product_item_sequence")
public class ProductItem extends BaseEntity {

	private static final long serialVersionUID = 1430056491771215900L;

	/** 商品 */
	@Expose
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	private Product product;

	/** 数量 */
	@Expose
	@JsonProperty
	@NotNull
	private Integer quantity;
	
	/** 套餐 */
	@Expose
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	private ProductGroup productGroup;
	
	
	/** 获取商品总价 */
	@JsonProperty
	public BigDecimal getTotalPrice() {
		BigDecimal totalPrice = BigDecimal.ZERO;
		totalPrice = totalPrice.multiply(new BigDecimal(quantity));
		return totalPrice.compareTo(BigDecimal.ZERO) > 0 ? totalPrice : BigDecimal.ZERO;
	}
	
	// ======================================getter/setter===========================================//

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public ProductGroup getProductGroup() {
		return productGroup;
	}

	public void setProductGroup(ProductGroup productGroup) {
		this.productGroup = productGroup;
	}

}
