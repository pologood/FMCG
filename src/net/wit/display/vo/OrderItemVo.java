/**
 * @Title：OrderItemVo.java 
 * @Package：net.wit.controller.displayvo 
 * @Description：
 * @author：Chenlf
 * @date：2015年5月16日 上午11:43:37 
 * @version：V1.0   
 */

package net.wit.display.vo;

import net.wit.entity.Trade;

/**
 * @ClassName：OrderItemVo
 * @Description：
 * @author：Chenlf
 * @date：2015年5月16日 上午11:43:37
 */
public class OrderItemVo extends BaseVo {

	private String thumbnail;

	private String fullName;


	private ProductVo product;

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public ProductVo getProduct() {
		return product;
	}

	public void setProduct(ProductVo product) {
		this.product = product;
	}

}
