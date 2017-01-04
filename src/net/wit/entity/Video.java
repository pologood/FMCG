package net.wit.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

/**
 * Entity - 视频广告
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_video")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_video_sequence")
public class Video extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	/** 类型 */
	public enum MediaType {
		mp4,
		wav,
		avi,
		rm,
		rmvb,
		swf,
		flv
	}

	/** 标题 */
	@Length(max = 255)
	private String title;

	/** 源文件 */
	@Length(max = 255)
	private String url;

	/** 源文件 */
	@Length(max = 255)
	private String equipId;

	/** 各分店铺 */
	@ManyToOne(fetch = FetchType.LAZY)
	private DeliveryCenter deliveryCenter;

	/** 店铺 */
	@JsonProperty
	@ManyToOne(fetch = FetchType.LAZY)
	private Tenant tenant;

	/** 类型 */
	@Length(max = 255)
	private MediaType mediaType;
	
	/** 费用 */
	@Column(nullable = false, precision = 21, scale = 6)
	private BigDecimal amount;

	/** 开始日期 */
	@Length(max = 255)
	private Date startDate;
	private Date beginDate;

	/** 结束日期 */
	private Date endDate;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public DeliveryCenter getDeliveryCenter() {
		return deliveryCenter;
	}

	public String getEquipId() {
		return equipId;
	}

	public void setEquipId(String equipId) {
		this.equipId = equipId;
	}

	public void setDeliveryCenter(DeliveryCenter deliveryCenter) {
		this.deliveryCenter = deliveryCenter;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}
}