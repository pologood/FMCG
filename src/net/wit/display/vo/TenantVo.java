/**
 * @Title：TenantVo.java 
 * @Package：net.wit.controller.displayvo 
 * @Description：
 * @author：Chenlf
 * @date：2015年5月16日 上午10:20:04 
 * @version：V1.0   
 */

package net.wit.display.vo;

import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName：TenantVo
 * @Description：
 * @author：Chenlf
 * @date：2015年5月16日 上午10:20:04
 */
public class TenantVo extends BaseVo {

	private String name;

	private String logo;
	
	private String thumbnail;

	private Float score;
	
	private String telephone;

	private Set<ProductCategoryTenantVo> productCategoryTenants = new HashSet<ProductCategoryTenantVo>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Float getScore() {
		return score;
	}

	public void setScore(Float score) {
		this.score = score;
	}

	public Set<ProductCategoryTenantVo> getProductCategoryTenants() {
		return productCategoryTenants;
	}

	public void setProductCategoryTenants(Set<ProductCategoryTenantVo> productCategoryTenants) {
		this.productCategoryTenants = productCategoryTenants;
	}

	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * @param telephone the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * @return the thumbnail
	 */
	public String getThumbnail() {
		return thumbnail;
	}

	/**
	 * @param thumbnail the thumbnail to set
	 */
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

}
