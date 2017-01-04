package net.wit.controller.wap.model;

import net.wit.controller.app.model.BaseModel;
import net.wit.controller.app.model.MemberRankModel;
import net.wit.entity.Consumer;
import net.wit.entity.Location;
import net.wit.entity.Member;
import net.wit.util.MapUtils;

import java.util.*;

public class MemberListModel extends BaseModel {
	/*会员ID*/
	private Long id;
	/*会员名*/
	private String nickName;
	private String username;
	/*头像*/
	private String headImg;
	/*等级*/
	private MemberRankModel memberRank;
	/*更新时间*/
	private Date modify_date;
	/*星级*/
	private float grade;
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

	public MemberRankModel getMemberRank() {
		return memberRank;
	}

	public void setMemberRank(MemberRankModel memberRank) {
		this.memberRank = memberRank;
	}

	public Date getModify_date() {
		return modify_date;
	}

	public void setModify_date(Date modify_date) {
		this.modify_date = modify_date;
	}

	public float getGrade() {
		return grade;
	}

	public void setGrade(float grade) {
		this.grade = grade;
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
		MemberRankModel memberRankModel = new MemberRankModel();
		memberRankModel.copyFrom(member.getMemberRank());
		this.memberRank = memberRankModel;
		this.modify_date = member.getCreateDate();
		this.grade = member.getScore();
		this.address = member.getAddress();
	}
	
	public static List<MemberListModel> bindData(List<Member> members) {
		List<MemberListModel> models = new ArrayList<MemberListModel>();
		for (Member member:members) {
			MemberListModel model = new MemberListModel();
			model.copyFrom(member);
			models.add(model);
		}
		return models;
	}


}
