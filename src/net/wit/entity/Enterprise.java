/**
 *====================================================
 * 文件名称: Enterprise.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年10月17日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 */
package net.wit.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.google.gson.annotations.Expose;

/**
 * @ClassName: Enterprise
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014年10月17日 上午9:18:00
 */
@Entity
@Table(name = "xx_enterprise")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_enterprise_sequence")
public class Enterprise extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9177144171916587643L;

	/** 企业类型 */
	public enum EnterpriseType {
		/** 运营商 */
		proxy,
		/** 省级代理 */
		provinceproxy,
		/** 市级代理 */
		cityproxy,
		/** 区县代理 */
		countyproxy,
		/** 个人代理 */
		personproxy
	}

	/** 企业名称 */
	@Column(nullable = false, length = 255)
	private String name;

	/** 企业简称 */
	@Expose
	private String shortName;

	/** 企业类型 */
	@Expose
	private EnterpriseType enterprisetype;

	/** 经营许可证 */
	@Column(length = 255)
	private String licenseCode;

	/** 营业执照 **/
	private String licensePhoto;

	/** 拼音码 */
	@Column(length = 255)
	private String spell;

	/** 法人代表 */
	@Expose
	@Column(length = 255)
	private String legalRepr;

	/** 联系人 */
	@Expose
	@Column(length = 255)
	private String linkman;

	/** 联系电话 */
	@Column(length = 255)
	private String telephone;

	/** 传真 */
	@Column(length = 255)
	private String faxes;

	/** 地址 */
	@Column(length = 255)
	private String address;

	/** 邮编 */
	@Column(length = 255)
	private String zipcode;

	/** 结余许可数 */
	private Integer snNumber;
	
	/** 绑定会员 */
	@NotNull
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Member member;
	
	/** 下属店铺*/
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_enterprise_tenant")
	private Set<Tenant> tenants = new HashSet<Tenant>();

	/** 区域 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Area area;

	/** 商圈信息 */
	@OneToMany(mappedBy = "enterprise", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<Community> communities = new HashSet<Community>();

	/** 部门信息 */
	@OneToMany(mappedBy = "enterprise", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<Department> departments = new HashSet<Department>();

	/** 管理员信息 */
	@OneToMany(mappedBy = "enterprise", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<Admin> admins = new HashSet<Admin>();

	/** 角色信息 */
	@OneToMany(mappedBy = "enterprise", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private Set<Role> roles = new HashSet<Role>();

	// ===========================================getter/setter===========================================//
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public EnterpriseType getEnterprisetype() {
		return enterprisetype;
	}

	public void setEnterprisetype(EnterpriseType enterprisetype) {
		this.enterprisetype = enterprisetype;
	}

	public String getLicenseCode() {
		return licenseCode;
	}

	public void setLicenseCode(String licenseCode) {
		this.licenseCode = licenseCode;
	}

	public String getLicensePhoto() {
		return licensePhoto;
	}

	public void setLicensePhoto(String licensePhoto) {
		this.licensePhoto = licensePhoto;
	}

	public String getSpell() {
		return spell;
	}

	public void setSpell(String spell) {
		this.spell = spell;
	}

	public String getLegalRepr() {
		return legalRepr;
	}

	public void setLegalRepr(String legalRepr) {
		this.legalRepr = legalRepr;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getFaxes() {
		return faxes;
	}

	public void setFaxes(String faxes) {
		this.faxes = faxes;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public void setTenants(Set<Tenant> tenants) {
		this.tenants = tenants;
	}

	public Set<Tenant> getTenants() {
		return tenants;
	}
	
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Set<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(Set<Department> departments) {
		this.departments = departments;
	}

	public Set<Admin> getAdmins() {
		return admins;
	}

	public void setAdmins(Set<Admin> admins) {
		this.admins = admins;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<Community> getCommunities() {
		return communities;
	}

	public void setCommunities(Set<Community> communities) {
		this.communities = communities;
	}

	public Integer getSnNumber() {
		return snNumber;
	}

	public void setSnNumber(Integer snNumber) {
		this.snNumber = snNumber;
	}

}
