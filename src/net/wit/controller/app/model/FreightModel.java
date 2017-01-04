package net.wit.controller.app.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.validation.constraints.Min;

import net.wit.entity.Parameter;
import net.wit.entity.Freight;
import net.wit.entity.Freight.Type;

public class FreightModel extends BaseModel {
	/** 首重量 */
	private Type freightType;
	
	/** 首重量 */
	private Integer firstWeight;

	/** 续重量 */
	private Integer continueWeight;

	/** 首重价格 */
	private BigDecimal firstPrice;

	/** 续重价格 */
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

	public void copyFrom(Freight freight) {
		this.firstPrice = freight.getFirstPrice();
		this.continuePrice = freight.getContinuePrice();
		this.continueWeight = freight.getContinueWeight();
		this.firstWeight = freight.getFirstWeight();
		this.freightType = freight.getFreightType();
	}
	
}
