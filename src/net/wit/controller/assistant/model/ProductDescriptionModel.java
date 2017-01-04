package net.wit.controller.assistant.model;

import net.wit.controller.app.model.AttributeModel;
import net.wit.controller.app.model.BaseModel;
import net.wit.controller.app.model.ParameterValueModel;
import net.wit.entity.Product;

import java.util.Set;

public class ProductDescriptionModel extends BaseModel {
	
	/**描述**/
	private String description;
	
	/**参数**/
	private Set<ParameterValueModel> parameters;
	/**属性**/
	private Set<AttributeModel> attributes;
	
	/** 商品属性值0 */
	private String attributeValue0;

	/** 商品属性值1 */
	private String attributeValue1;

	/** 商品属性值2 */
	private String attributeValue2;

	/** 商品属性值3 */
	private String attributeValue3;

	/** 商品属性值4 */
	private String attributeValue4;

	/** 商品属性值5 */
	private String attributeValue5;

	/** 商品属性值6 */
	private String attributeValue6;

	/** 商品属性值7 */
	private String attributeValue7;

	/** 商品属性值8 */
	private String attributeValue8;

	/** 商品属性值9 */
	private String attributeValue9;

	/** 商品属性值10 */
	private String attributeValue10;

	/** 商品属性值11 */
	private String attributeValue11;

	/** 商品属性值12 */
	private String attributeValue12;

	/** 商品属性值13 */
	private String attributeValue13;

	/** 商品属性值14 */
	private String attributeValue14;

	/** 商品属性值15 */
	private String attributeValue15;

	/** 商品属性值16 */
	private String attributeValue16;

	/** 商品属性值17 */
	private String attributeValue17;

	/** 商品属性值18 */
	private String attributeValue18;

	/** 商品属性值19 */
	private String attributeValue19;
	
	
	
	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public Set<ParameterValueModel> getParameters() {
		return parameters;
	}



	public void setParameters(Set<ParameterValueModel> parameters) {
		this.parameters = parameters;
	}



	public Set<AttributeModel> getAttributes() {
		return attributes;
	}



	public void setAttributes(Set<AttributeModel> attributes) {
		this.attributes = attributes;
	}



	public String getAttributeValue0() {
		return attributeValue0;
	}



	public void setAttributeValue0(String attributeValue0) {
		this.attributeValue0 = attributeValue0;
	}



	public String getAttributeValue1() {
		return attributeValue1;
	}



	public void setAttributeValue1(String attributeValue1) {
		this.attributeValue1 = attributeValue1;
	}



	public String getAttributeValue2() {
		return attributeValue2;
	}



	public void setAttributeValue2(String attributeValue2) {
		this.attributeValue2 = attributeValue2;
	}



	public String getAttributeValue3() {
		return attributeValue3;
	}



	public void setAttributeValue3(String attributeValue3) {
		this.attributeValue3 = attributeValue3;
	}



	public String getAttributeValue4() {
		return attributeValue4;
	}



	public void setAttributeValue4(String attributeValue4) {
		this.attributeValue4 = attributeValue4;
	}



	public String getAttributeValue5() {
		return attributeValue5;
	}



	public void setAttributeValue5(String attributeValue5) {
		this.attributeValue5 = attributeValue5;
	}



	public String getAttributeValue6() {
		return attributeValue6;
	}



	public void setAttributeValue6(String attributeValue6) {
		this.attributeValue6 = attributeValue6;
	}



	public String getAttributeValue7() {
		return attributeValue7;
	}



	public void setAttributeValue7(String attributeValue7) {
		this.attributeValue7 = attributeValue7;
	}



	public String getAttributeValue8() {
		return attributeValue8;
	}



	public void setAttributeValue8(String attributeValue8) {
		this.attributeValue8 = attributeValue8;
	}



	public String getAttributeValue9() {
		return attributeValue9;
	}



	public void setAttributeValue9(String attributeValue9) {
		this.attributeValue9 = attributeValue9;
	}



	public String getAttributeValue10() {
		return attributeValue10;
	}



	public void setAttributeValue10(String attributeValue10) {
		this.attributeValue10 = attributeValue10;
	}



	public String getAttributeValue11() {
		return attributeValue11;
	}



	public void setAttributeValue11(String attributeValue11) {
		this.attributeValue11 = attributeValue11;
	}



	public String getAttributeValue12() {
		return attributeValue12;
	}



	public void setAttributeValue12(String attributeValue12) {
		this.attributeValue12 = attributeValue12;
	}



	public String getAttributeValue13() {
		return attributeValue13;
	}



	public void setAttributeValue13(String attributeValue13) {
		this.attributeValue13 = attributeValue13;
	}



	public String getAttributeValue14() {
		return attributeValue14;
	}



	public void setAttributeValue14(String attributeValue14) {
		this.attributeValue14 = attributeValue14;
	}



	public String getAttributeValue15() {
		return attributeValue15;
	}



	public void setAttributeValue15(String attributeValue15) {
		this.attributeValue15 = attributeValue15;
	}



	public String getAttributeValue16() {
		return attributeValue16;
	}



	public void setAttributeValue16(String attributeValue16) {
		this.attributeValue16 = attributeValue16;
	}



	public String getAttributeValue17() {
		return attributeValue17;
	}



	public void setAttributeValue17(String attributeValue17) {
		this.attributeValue17 = attributeValue17;
	}



	public String getAttributeValue18() {
		return attributeValue18;
	}



	public void setAttributeValue18(String attributeValue18) {
		this.attributeValue18 = attributeValue18;
	}



	public String getAttributeValue19() {
		return attributeValue19;
	}



	public void setAttributeValue19(String attributeValue19) {
		this.attributeValue19 = attributeValue19;
	}



	public void copyFrom(Product product) {
		this.parameters = ParameterValueModel.bindData(product.getParameterValue());
		this.attributes = AttributeModel.bindData(product.getProductCategory().getAttributes());
		this.attributeValue0 = product.getAttributeValue0();
		this.attributeValue1 = product.getAttributeValue1();
		this.attributeValue2 = product.getAttributeValue2();
		this.attributeValue3 = product.getAttributeValue3();
		this.attributeValue4 = product.getAttributeValue4();
		this.attributeValue5 = product.getAttributeValue5();
		this.attributeValue6 = product.getAttributeValue6();
		this.attributeValue7 = product.getAttributeValue7();
		this.attributeValue8 = product.getAttributeValue8();
		this.attributeValue9 = product.getAttributeValue9();
		this.attributeValue10 = product.getAttributeValue10();
		this.attributeValue11 = product.getAttributeValue11();
		this.attributeValue12 = product.getAttributeValue12();
		this.attributeValue13 = product.getAttributeValue13();
		this.attributeValue14 = product.getAttributeValue14();
		this.attributeValue15 = product.getAttributeValue15();
		this.attributeValue16 = product.getAttributeValue16();
		this.attributeValue17 = product.getAttributeValue17();
		this.attributeValue18 = product.getAttributeValue18();
		this.attributeValue19 = product.getAttributeValue19();
	}
		
}
