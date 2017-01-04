package net.wit.controller.assistant.model;

import net.wit.controller.assistant.model.BaseModel;
import net.wit.entity.Freight;
import net.wit.entity.Freight.Type;

import java.math.BigDecimal;

public class FreightModel extends BaseModel {
	/** 类型 */
	private DescModel descModel;
	
	/** 重量,距离，件数 */
	private Integer firstWeight;

	/** 每增加 */
	private Integer continueWeight;

	/** 运费 */
	private BigDecimal firstPrice;

	/** 运费加 */
	private BigDecimal continuePrice;


	public DescModel getDescModel() {
		return descModel;
	}

	public void setDescModel(DescModel descModel) {
		this.descModel = descModel;
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
		DescModel model = new  DescModel();
		if(freight.getFreightType().equals(Type.distance)){
			model.setStatus("distance");
			model.setDesc("计距离");
		}else if(freight.getFreightType().equals(Type.weight)){
			model.setStatus("weight");
			model.setDesc("计重");
		}else if(freight.getFreightType().equals(Type.piece)){
			model.setStatus("piece");
			model.setDesc("计件");
		}
		this.descModel = model;

	}
	
}
