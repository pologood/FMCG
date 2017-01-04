package net.wit.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

/**
 * Entity - 视频广告
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_qrcode")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_qrcode_sequence")
public class Qrcode extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	/** 类型 */
	public enum QrcodeType {
		tenant
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(updatable = true)
	private Tenant tenant;

	/**
	 * 会员
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	/**
	 * 锁定到期时间
	 */
	private Date lockExpire;


	/** 源文件 */
	@Length(max = 255)
	private String url;
	
	/** 源文件 */
	@Length(max = 255)
	private String ticket;
	
	/** 类型 */
	@Length(max = 255)
	private QrcodeType qrcodeType;

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public QrcodeType getQrcodeType() {
		return qrcodeType;
	}

	public void setQrcodeType(QrcodeType qrcodeType) {
		this.qrcodeType = qrcodeType;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Date getLockExpire() {
		return lockExpire;
	}

	public void setLockExpire(Date lockExpire) {
		this.lockExpire = lockExpire;
	}

    /**
     * 判断是否已锁定
     * @param member 操作员
     * @return 是否已锁定
     */
    public boolean isLocked(Member member) {
        return getLockExpire() != null && new Date().before(getLockExpire()) && ((member != null && !member.equals(getMember())) || (member == null && getMember() != null));
    }
}