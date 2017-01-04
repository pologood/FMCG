/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import javax.persistence.*;

/**
 * Entity - 会员常用产品类目
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_product_category_member")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_product_category_member_sequence")
public class ProductCategoryMember extends BaseEntity {

	private static final long serialVersionUID = 1533130686714725835L;

	@ManyToOne(fetch = FetchType.LAZY)
	private ProductCategory productCategory;

	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	public ProductCategory getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
}