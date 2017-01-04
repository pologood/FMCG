package net.wit.controller.app.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.wit.entity.Consumer;
import net.wit.entity.Location;
import net.wit.entity.Member;
import net.wit.util.MapUtils;
import net.wit.util.StringUtils;

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
	/*距离*/
	private double distance;
	/*更新时间*/
	private Date modify_date;
	/*创建时间*/
	private Date create_date;
	/*手机号*/
	private String mobile;
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

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
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

	public void copyFrom(Member member,Location location) {
		this.id = member.getId();
		this.nickName = member.getDisplayName();
		this.username = member.getUsername();
		this.headImg = member.getHeadImg();
		MemberRankModel memberRankModel = new MemberRankModel();
		memberRankModel.copyFrom(member.getMemberRank());
		this.memberRank = memberRankModel;
		this.modify_date = member.getLbsDate();
		if ((location!=null && location.getLng()!=null && location.getLat()!=null) && (member.getLocation()!=null && member.getLocation().getLat()!=null && member.getLocation().getLng()!=null)) {
			this.distance = MapUtils.getDistatce(location.getLat().doubleValue(),member.getLocation().getLat().doubleValue(),
					location.getLng().doubleValue(), member.getLocation().getLng().doubleValue());
		} else {
			this.distance = -1;
		}
		this.create_date = member.getCreateDate();
		this.mobile = member.getMobile();
		this.grade = member.getScore();
		this.address = member.getAddress();
	}
	
	public static List<MemberListModel> bindData(List<Member> members,Location location) {
		List<MemberListModel> models = new ArrayList<MemberListModel>();
		for (Member member:members) {
			MemberListModel model = new MemberListModel();
			model.copyFrom(member,location);
			models.add(model);
		}
		return models;
	}
	public static List<MemberListModel> bindConsumer(List<Consumer> consumers,Location location) {
		List<MemberListModel> models = new ArrayList<MemberListModel>();
		for (Consumer consumer:consumers) {
			MemberListModel model = new MemberListModel();
			model.copyFrom(consumer.getMember(),location);
			MemberRankModel  rank = new MemberRankModel();
			rank.copyFrom(consumer.getMemberRank());
			model.setMemberRank(rank);
			models.add(model);
		}
		return models;
	}
	public static Set<MemberListModel> bindData(Set<Member> members,Location location) {
		Set<MemberListModel> models = new HashSet<MemberListModel>(members.size());
		for (Member member:members) {
			MemberListModel model = new MemberListModel();
			model.copyFrom(member,location);
			models.add(model);
		}
		return models;
	}
}
