/**
 * @Title：ProductVo.java 
 * @Package：net.wit.controller.displayvo 
 * @Description：
 * @author：Chenlf
 * @date：2015年5月16日 上午11:19:58 
 * @version：V1.0   
 */

package net.wit.display.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName：ProductVo
 * @Description：
 * @author：Chenlf
 * @date：2015年5月16日 上午11:19:58
 */
public class ProductVo extends BaseVo {

	private String fullName;

	private String medium;

	private BigDecimal price;

	private BigDecimal marketPrice;

	private List<String> specification_value;
	
	private String unit;
	private Integer stock;
	private Long sales;
	private Long hits;
	
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	/**
	 * @return the specification_value
	 */
	public List<String> getSpecification_value() {
		return specification_value;
	}

	/**
	 * @param specification_value the specification_value to set
	 */
	public void setSpecification_value(List<String> specification_value) {
		this.specification_value = specification_value;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Long getSales() {
		return sales;
	}

	public void setSales(Long sales) {
		this.sales = sales;
	}

	public Long getHits() {
		return hits;
	}

	public void setHits(Long hits) {
		this.hits = hits;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	
}
