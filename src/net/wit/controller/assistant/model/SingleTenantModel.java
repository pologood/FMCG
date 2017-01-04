/**
 *====================================================
 * 文件名称: TenantController.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014-9-11			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.controller.assistant.model;

import net.wit.controller.assistant.BaseController;
import net.wit.entity.Member;
import net.wit.entity.Tenant;

public class SingleTenantModel extends BaseController {

	/**id*/
	private Long id;
	/** 名称 */
	private String name;
	/**头像*/
	private String logo;

	public void copyFrom(Tenant tenant) {
		this.id = tenant.getId();
		this.name = tenant.getName();
		this.logo = tenant.getLogo();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
}
