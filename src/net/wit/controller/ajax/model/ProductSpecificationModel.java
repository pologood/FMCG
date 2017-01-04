/**
 *====================================================
 * 文件名称: BankModel.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014-9-5			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.controller.ajax.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import net.wit.entity.Specification;
import net.wit.entity.SpecificationValue;

/**
 * @ClassName: BankModel
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014-9-5 下午1:58:15
 */
public class ProductSpecificationModel extends BaseModel{

	/** 销售价 */
	private BigDecimal price;
	/** 库存 */
	private Integer stock;

	private Set<Specification> specifications = new HashSet<Specification>();
	private Set<SpecificationValue> specificationValues = new HashSet<SpecificationValue>();
	
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	public Set<Specification> getSpecifications() {
		return specifications;
	}
	public void setSpecifications(Set<Specification> specifications) {
		this.specifications = specifications;
	}
	public Set<SpecificationValue> getSpecificationValues() {
		return specificationValues;
	}
	public void setSpecificationValues(Set<SpecificationValue> specificationValues) {
		this.specificationValues = specificationValues;
	}
	
	
}
