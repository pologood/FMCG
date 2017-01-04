/**
 * @Title：BaseVo.java 
 * @Package：net.wit.controller.displayvo 
 * @Description：
 * @author：Chenlf
 * @date：2015年5月16日 上午10:18:44 
 * @version：V1.0   
 */

package net.wit.display.vo;

import java.util.Date;

/**
 * @ClassName：BaseVo
 * @Description：
 * @author：Chenlf
 * @date：2015年5月16日 上午10:18:44
 */
public class BaseVo {

	private Long id;
	private Date createDate;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
