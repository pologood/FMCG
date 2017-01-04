package net.wit.controller.assistant.model;

import net.wit.controller.app.model.BaseModel;
import net.wit.entity.Member;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
//会员model
public class AssociatorModel extends BaseModel {
	
	/*id*/
	private Long id;
	/*姓名*/
	private String name;
	/*昵称*/
	private String nickName;
	/*头像*/
	private String headImg;
	/*时间*/
	private Date createDate;
	/*手机号码*/
	private String phone;
	/*会员等级*/
	private String memberRank;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMemberRank() {
		return memberRank;
	}

	public void setMemberRank(String memberRank) {
		this.memberRank = memberRank;
	}

	public void copyFrom(Member member) {
		this.id = member.getId();
		this.name = member.getName();
		this.nickName = member.getNickName();
		this.headImg = member.getHeadImg();
		this.phone = member.getPhone();
		if(member.getMemberRank()!=null){
			this.memberRank = member.getMemberRank().getName();
		}

		this.createDate = member.getCreateDate();
	}
	
	public static List<AssociatorModel> bindData(List<Member> members) {
		List<AssociatorModel> models = new ArrayList<AssociatorModel>();
		for (Member member:members) {
			AssociatorModel model = new AssociatorModel();
			model.copyFrom(member);
			models.add(model);
		}
		return models;
	}
}
