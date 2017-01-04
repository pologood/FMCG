package net.wit.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

/**
 * Entity - 专家
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_expert")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_expert_sequence")
public class Expert extends BaseEntity {

	private static final long serialVersionUID = -5132653159151649662L;
	
	/** 工作照 */
	@NotEmpty
	@JsonProperty
	private String image;
	
	/** 专家简介 */
	@NotEmpty
	@Lob
	@JsonProperty
	private String content;

	/** 绑定的会员 */
	@NotNull
	@ManyToOne
	@JoinColumn(nullable = false)
	@JsonProperty
	private Member member;
	
	/** 文章分类 */
	@Expose
	@JsonProperty
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private ExpertCategory expertCategory;

	/** 标签 */
	@JsonProperty
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "xx_expert_tag")
	@OrderBy("order asc")
	private Set<Tag> tags = new HashSet<Tag>();

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public ExpertCategory getExpertCategory() {
		return expertCategory;
	}

	public void setExpertCategory(ExpertCategory expertCategory) {
		this.expertCategory = expertCategory;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}


}
