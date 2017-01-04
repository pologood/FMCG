package net.wit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import net.wit.util.PhoneticZhCNUtil;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "xx_report_file")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_report_file_sequence")
public class ReportFile  extends BaseEntity{
	
	private static final long serialVersionUID = 2673739467029665976L;

	/** 名称 */
	@JsonProperty
	@NotEmpty
	@Column(nullable = false)
	private String name;
	
	/** 标题 */
	@JsonProperty
	@NotEmpty
	@Column(nullable = false)
	private String title;
	
	/** 路径 */
	@JsonProperty
	@NotEmpty
	@Column(nullable = false)
	private String source;
	
	/** 路径 */
	@JsonProperty
	private String url;
	
	/** 描述 */
	@Length(max = 200)
	private String description;
	
	/** 操作员 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false, updatable = false)
	private Admin admin;

	public String getPhonetic() {
		return PhoneticZhCNUtil.getZhCNFirstSpell(this.title).substring(0, 1).toUpperCase();
	}
	// ===========================================getter/setter===========================================//
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
}
