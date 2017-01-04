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
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * @ClassName: 活动规则
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014年5月9日 下午3:26:44
 */
@Entity
@Table(name = "xx_activity_rules")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_activity_rules_sequence")
public class ActivityRules extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	/** 状态 */
	public enum Status {
		/** 启动 */
		enabled,
		/** 禁用 */
		disabled
	}

	/**
	 * 任务类型
	 */
	public enum Type{
		/** 成长任务 */
		growth,
		/** 每日任务 */
		daily,
		/** 活动任务 */
		activity
	}

	/** 活动简称 */
	private String title;

	/** 活动描述 */
	private String description;

	/** 备注 */
	private String remarks;

	/** 赠送积分 */
	@Min(0)
	@Column()
	private Long point;
	
	/** 奖励金额 */
	@Min(0)
	@Digits(integer = 12, fraction = 3)
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal amount;
	
	/** 状态 */
	private Status status;

	/** 任务类型*/
	private Type type;

	/** 活动链接*/
	private String url;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getPoint() {
		return point;
	}

	public void setPoint(Long point) {
		this.point = point;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
