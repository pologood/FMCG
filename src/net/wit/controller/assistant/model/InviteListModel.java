package net.wit.controller.assistant.model;

import net.wit.entity.Consumer;
import net.wit.entity.Location;
import net.wit.entity.Member;
import net.wit.util.MapUtils;

import java.util.*;

public class InviteListModel extends BaseModel {
	/*会员ID*/
	private Long id;
	/*会员名*/
	private String nickName;
	private String username;
	/*头像*/
	private String headImg;
	/*更新时间*/
	private Date modify_date;
	/*创建时间*/
	private Date create_date;
	/*手机号*/
	private String mobile;
	/*地址*/
	private String address;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}


	public Date getModify_date() {
		return modify_date;
	}

	public void setModify_date(Date modify_date) {
		this.modify_date = modify_date;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void copyFrom(Member member) {
		this.id = member.getId();
		this.nickName = member.getDisplayName();
		this.username = member.getUsername();
		this.headImg = member.getHeadImg();
		this.modify_date = member.getLbsDate();
		this.create_date = member.getCreateDate();
		this.mobile = member.getMobile();
		this.address = member.getAddress();
	}

	public static List<InviteListModel> bindData(List<Member> members) {
		List<InviteListModel> models = new ArrayList<InviteListModel>();
		for (Member member:members) {
			InviteListModel model = new InviteListModel();
			model.copyFrom(member);
			models.add(model);
		}
		return models;
	}
}
