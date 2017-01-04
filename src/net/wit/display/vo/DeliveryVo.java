/**
 * @Title：DeliveryVo.java 
 * @Package：net.wit.controller.displayvo 
 * @Description：
 * @author：Chenlf
 * @date：2015年5月16日 上午10:18:24 
 * @version：V1.0   
 */

package net.wit.display.vo;

import java.math.BigDecimal;

/**
 * @ClassName：DeliveryVo
 * @Description：
 * @author：Chenlf
 * @date：2015年5月16日 上午10:18:24
 */
public class DeliveryVo extends BaseVo {

	private TenantVo tenant;

	private String address;
	
	private String name;
	
	private BigDecimal distance;
	private String areaName;

	/**
	 * @return the tenant
	 */
	public TenantVo getTenant() {
		return tenant;
	}

	/**
	 * @param tenant the tenant to set
	 */
	public void setTenant(TenantVo tenant) {
		this.tenant = tenant;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the distance
	 */
	public BigDecimal getDistance() {
		return distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(BigDecimal distance) {
		this.distance = distance;
	}

	/**
	 * @return the areaName
	 */
	public String getAreaName() {
		return areaName;
	}

	/**
	 * @param areaName the areaName to set
	 */
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

}
