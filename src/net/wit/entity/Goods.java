/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

/**
 * @ClassName: Goods
 * @Description: 货品
 * @author Administrator
 * @date 2014年10月14日 上午10:20:14
 */
@Entity
@Table(name = "xx_goods")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_goods_sequence")
public class Goods extends BaseEntity {

	private static final long serialVersionUID = -6977025562650112419L;

	/** 商品 */
	@OneToMany(mappedBy = "goods", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("order asc")
	private Set<Product> products = new HashSet<Product>();
	
	/** 规格1标题**/
	@Length(max = 200)
	private String specification1Title;
	
	/** 规格2标题**/
	@Length(max = 200)
	private String specification2Title;
	

	/** 获取规格值 */
	public Set<SpecificationValue> getSpecificationValues() {
		Set<SpecificationValue> specificationValues = new HashSet<SpecificationValue>();
		if (getProducts() != null) {
			for (Product product : getProducts()) {
				specificationValues.addAll(product.getSpecificationValues());
			}
		}
		return specificationValues;
	}

	// ===========================================getter/setter===========================================//
	/**
	 * 获取商品
	 * @return 商品
	 */

	public Set<Product> getProducts() {
		return products;
	}

	/**
	 * 获取商品
	 * @return 商品
	 */

	public List<Product> getSortProducts() {
		List<Product> products = new ArrayList<Product>();
		for (Product product:getProducts()) {
			products.add(product);
		}
		Collections.sort(products,new Comparator<Product>() {
			public int compare(Product o1, Product o2) {
			   return o1.compareTo(o2);
			}
		}
        );
		return products;
	}
	
	/**
	 * 设置商品
	 * @param products 商品
	 */
	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	public String getSpecification1Title() {
		return specification1Title;
	}

	public void setSpecification1Title(String specification1Title) {
		this.specification1Title = specification1Title;
	}

	public String getSpecification2Title() {
		return specification2Title;
	}

	public void setSpecification2Title(String specification2Title) {
		this.specification2Title = specification2Title;
	}

	public Integer  getStock() {
		Integer stock = 0;
		for (Product product:getProducts()) {
			if (product.getStock()!=null) {
			    stock = stock + (product.getStock() < 0 ? 0 : product.getStock());
			}
		}
		return stock;
	}

	public Integer  getAllocatedStock() {
		Integer stock = 0;
		for (Product product:getProducts()) {
			if (product.getAllocatedStock()!=null) {
			   stock = stock + (product.getAllocatedStock() < 0 ? 0 : product.getAllocatedStock());
			}
		}
		return stock;
	}
	

	public Long  getHits() {
		Long stock = 0L;
		for (Product product:getProducts()) {
			stock = stock + product.getHits();
		}
		return stock;
	}
	

	public Long  getMonthSales() {
		Long stock = 0L;
		for (Product product:getProducts()) {
			stock = stock + product.getMonthSales();
		}
		return stock;
	}
	
}