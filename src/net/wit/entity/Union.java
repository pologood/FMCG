/**
 *====================================================
 * 文件名称: Union.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年5月9日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.google.gson.annotations.Expose;

/**
 * @ClassName: Union
 * @Description: 商家联盟
 * @author Administrator
 * @date 2014年5月9日 下午3:26:44
 */
@Entity
@Table(name = "xx_union")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_union_sequence")
public class Union extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/** 类型 */
	public enum Type {
		/** 商家联盟 */
		tenant,
		/** 购物屏联盟  */
		device
	}

	/** 联盟类型 */
	@NotNull
	private Type type;
	
	/** 联盟名称 */
	@NotNull
	@Length(max = 100)
	private String name;

	/** 联盟描述 */
	@Length(max = 100)
	private String description;

	/** 背景图 */
	@NotNull
	@Length(max = 255)
	private String image;

	/** 联盟佣金 */
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal brokerage;

	/** 加盟费用 */
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(precision = 21, scale = 6)
	private BigDecimal price;

	/** 联盟商家数量 */
	@Min(0)
	@Column(nullable = false)
	private Integer tenantNumber;

	/** 联盟商家列表 */
	@OneToMany(mappedBy = "unions", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<UnionTenant> unionTenants = new HashSet<UnionTenant>();


	// ===========================================getter/setter===========================================//
	
	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getBrokerage() {
		return brokerage;
	}

	public void setBrokerage(BigDecimal brokerage) {
		this.brokerage = brokerage;
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}


	public Integer getTenantNumber() {
		return tenantNumber;
	}

	public void setTenantNumber(Integer tenantNumber) {
		this.tenantNumber = tenantNumber;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Set<UnionTenant> getUnionTenants() {
		return unionTenants;
	}

	public void setUnionTenants(Set<UnionTenant> unionTenants) {
		this.unionTenants = unionTenants;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
