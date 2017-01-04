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

/**
 * @ClassName: BankModel
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014-9-5 下午1:58:15
 */
public class ProductModel extends BaseModel{

	/** 全名 */
	private String fullName;
	/** 分类 */
	private String productCategory;
	/** 单位 */
	private String unit;
	/** 单位 */
	private String barcode;
	/** 货号 */
	private String sn;
	/** 销售价 */
	private BigDecimal price;
	/** 市场价 */
	private BigDecimal marketPrice;
	
	private String name;
	private String spec;
	private String madeIn;
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getProductCategory() {
		return productCategory;
	}
	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getMadeIn() {
		return madeIn;
	}
	public void setMadeIn(String madeIn) {
		this.madeIn = madeIn;
	}

}
