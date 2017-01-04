/*
P * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @ClassName: Software
 * @Description: 安全密钥
 * @author Administrator
 * @date 2014年10月14日 上午9:11:26
 */
@Embeddable
public class Software implements Serializable {

	private static final long serialVersionUID = -8536541568286987548L;
	
	/** 版本类型 */
	public enum Version {
		/** 单机版 */
		LCL,
		/** 网络版 */
		NET,
		/** 在线版 */
		ONL
	}
	
	/** 行业类型 */
	public enum Industry {
		/** 商超版 */
		MKT,
		/** 服装版 */
		FIG
	}
	

	/** 版本类型 */
	@Column(name = "version")
	private Version version;

	/** 行业类型 */
	@Column(name = "industry")
	private Industry industry;

	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	public Industry getIndustry() {
		return industry;
	}

	public void setIndustry(Industry industry) {
		this.industry = industry;
	}

}