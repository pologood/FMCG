package net.wit.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 实体类 - 短信发送 ============================================================================ 版权所有 2008-2010 rsico.com,并保留所有权利。
 * ---------------------------------------------------------------------------- 提示：在未取得rsico商业授权之前,您不能将本软件应用于商业用途,否则rsico将保留追究的权力。
 * ---------------------------------------------------------------------------- 官方网站：http://www.rsico.com
 * ---------------------------------------------------------------------------- KEY: SHOPUNION5CAA6FDAF2A5662FADB5F15AD00B2070
 * ============================================================================
 */

@Entity
@Table(name = "xx_smssend")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_smssend_sequence")
public class SmsSend extends BaseEntity {

	private static final long serialVersionUID = -8541323033439515141L;

	public static final int SMS_CONTENT_MAX_LENGTH = 255;// 短信最大长度

	public static final int SMS_MOBILES_MAX_LENGTH = 200;// 一次发送的最大手机号数

	public enum Status {
		wait, send, Error
	}

	/** 类型 */
	public enum Type {
		/** 系统 */
		system,
		/** 会员 */
		member,
		/**验证码类短信*/
		captcha,
		/**业务类短信*/
		service
	}

	/** 类型 */
	@Column(nullable = false, updatable = false)
	private Type type;

	/** 最机号，最多200个 */
	@Lob
	@Column(nullable = false)
	private String mobiles;

	/** 发送内容，最长255字符，127个汉字 */
	@Column(nullable = false)
	private String content;

	/** ip */
	private String ip;
	
	/** 发送优先级 1-5 */
	@Column(nullable = false)
	private Integer priority;

	/** 字符集 GBK */
	@Column(nullable = false)
	private String charset;

	/** 短信总数 */
	@Column(nullable = true)
	private Integer count;

	/** 发送费用 */
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal fee;

	/** 发送状态 0待发送 1发送成功，其他失败 */
	@Column(nullable = false)
	private Status status;

	/** 发送状态说明 */
	@Column(nullable = true)
	private String descr;

	/** 会员 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;
	
	// ===========================================getter/setter===========================================//
	public String getMobiles() {
		return mobiles;
	}

	public void setMobiles(String mobiles) {
		this.mobiles = mobiles;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public BigDecimal getFee() {
		return fee;
	}

	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	/**
	 * 获取类型
	 * @return 类型
	 */
	public Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * @param type 类型
	 */
	public void setType(Type type) {
		this.type = type;
	}

}