/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @ClassName: ModelYear
 * @Description: 年款
 * @author Administrator
 * @date 2014年10月14日 上午9:09:06
 */
@Entity
@Table(name = "xx_model_year")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_model_year_sequence")
public class ModelYear extends OrderEntity {

	private static final long serialVersionUID = -6109592619136943240L;

	/** 年款 */
	@NotEmpty
	@JsonProperty
	@Length(max = 200)
	@Column(nullable = false)
	private String name;

	// ===========================================getter/setter===========================================//

	/**
	 * 获取名称
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 * @param name 名称
	 */
	public void setName(String name) {
		this.name = name;
	}

}