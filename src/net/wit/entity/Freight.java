/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Min;

import net.wit.Setting;
import net.wit.util.SettingUtils;

/**
 * @ClassName: Location
 * @Description: 地理信息经纬度
 * @author Administrator
 * @date 2014年10月14日 上午10:20:28
 */
@Embeddable
public class Freight implements Serializable {
	private static final long serialVersionUID = 123L;
	
	/** 类型 */
	public enum Type {
		/**计距离*/
		distance,
		/** 计重 */
		weight,
		/** 计件 */
		piece
	}

	/** 类型 */
	@Column(nullable = false)
	private Type freightType;
	
	/** 首重量 */
	@Column(nullable = false)
	private Integer firstWeight;

	/** 续重量 */
	@Column(nullable = false)
	@Min(1)
	private Integer continueWeight;

	/** 首重价格 */
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal firstPrice;

	/** 续重价格 */
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal continuePrice;
 
	public Type getFreightType() {
		return freightType;
	}


	public void setFreightType(Type freightType) {
		this.freightType = freightType;
	}


	public Integer getFirstWeight() {
		return firstWeight;
	}


	public void setFirstWeight(Integer firstWeight) {
		this.firstWeight = firstWeight;
	}


	public Integer getContinueWeight() {
		return continueWeight;
	}


	public void setContinueWeight(Integer continueWeight) {
		this.continueWeight = continueWeight;
	}


	public BigDecimal getFirstPrice() {
		return firstPrice;
	}


	public void setFirstPrice(BigDecimal firstPrice) {
		this.firstPrice = firstPrice;
	}


	public BigDecimal getContinuePrice() {
		return continuePrice;
	}


	public void setContinuePrice(BigDecimal continuePrice) {
		this.continuePrice = continuePrice;
	}


	/**
	 * 计算运费
	 * @param weight 重量
	 * @return 运费
	 */
	public BigDecimal calculateFreight(Integer weight) {
		Setting setting = SettingUtils.get();
		BigDecimal freight = new BigDecimal(0);
		if (weight != null) {
			if (weight <= getFirstWeight() || getContinuePrice().compareTo(new BigDecimal(0)) == 0) {
				freight = getFirstPrice();
			} else {
				double contiuneWeightCount = Math.ceil((weight - getFirstWeight()) / (double) getContinueWeight());
				freight = getFirstPrice().add(getContinuePrice().multiply(new BigDecimal(contiuneWeightCount)));
			}
		}
		return setting.setScale(freight);
	}

	
	public String getDescr() {
		BigDecimal freight = calculateFreight(1);
		if (freight.compareTo(BigDecimal.ZERO)==0) {
			return "免运费";
		} else {
			return freight.toString() +"元运费";
		}
	}
}