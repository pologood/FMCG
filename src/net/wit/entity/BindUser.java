package net.wit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: BindUser
 * @Description: 第三方账号绑定
 * @author Administrator
 * @date 2014年10月13日 下午2:18:09
 */
@Entity
@Table(name = "xx_bind_user")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_bind_user_sequence")
public class BindUser extends BaseEntity {

	private static final long serialVersionUID = -2735035962597250149L;

	/** 类型 */
	public enum Type {
		/** 新商盟 */
		_xsm,
		/** 19e */
		_19e,
		/** 微信 */
		_wx,
		/** V-BOX */
		_vbox,
		/** 设备号 */
		_mac,
		/** 摄像头 */
		dahua
	}

	/** 用户名 */
	@NotNull
	@Column(nullable = false)
	private String username;

	/** 密码 */
	@NotNull
	@Column(nullable = false)
	private String password;

	/** 类型 */
	@NotNull
	@Column(nullable = false)
	private Type type;

	/** 绑定的会员 */
	@NotNull
	@ManyToOne
	@JoinColumn(nullable = false)
	private Member member;

	// ===========================================getter/setter===========================================//
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

}
