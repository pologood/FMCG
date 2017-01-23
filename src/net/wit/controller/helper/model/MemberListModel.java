package net.wit.controller.helper.model;

import net.wit.controller.weixin.model.BaseModel;
import net.wit.controller.weixin.model.MemberRankModel;
import net.wit.entity.Consumer;
import net.wit.entity.Location;
import net.wit.entity.Member;
import net.wit.util.MapUtils;
import net.wit.util.StringUtils;

import java.util.*;

public class MemberListModel extends BaseModel {
	/*会员ID*/
	private long id;
	/*ConsumerID*/
	private long consumerId;
	/*会员名*/
	private String nickName;
	/*头像*/
	private String headImg;
	/*等级*/
	private MemberRankModel memberRank;
	/*距离*/
	private double distance;
	/*更新时间*/
	private Date modify_date;
	/*星级*/
	private float grade;
	/*地址*/
	private String address;
	/*联系电话*/
	private String mobile;
	/*发展者*/
	private Member developer;
	/*发展者角色*/
	private String role;

	/*会员性别*/
	private String gender;

	/*會員狀態*/
	private Consumer.Status status;
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getConsumerId() {
		return consumerId;
	}

	public void setConsumerId(long consumerId) {
		this.consumerId = consumerId;
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

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Member getDeveloper() {
		return developer;
	}

	public void setDeveloper(Member developer) {
		this.developer = developer;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Consumer.Status getStatus() {
		return status;
	}

	public void setStatus(Consumer.Status status) {
		this.status = status;
	}

	public void copyFrom(Member member, Location location) {
		this.id = member.getId();
		if (member.getName()!=null) {
			this.nickName = member.getName();
		} else if (member.getNickName()!=null) {
			this.nickName = member.getNickName();
		} else {
		    this.nickName = StringUtils.mosaic(member.getUsername(),3,"~~");
		}
		this.headImg = member.getHeadImg();
		MemberRankModel memberRankModel = new MemberRankModel();
		memberRankModel.copyFrom(member.getMemberRank());
		this.memberRank = memberRankModel;
		this.modify_date = member.getLbsDate();
		if ((location!=null && location.getLng()!=null && location.getLat()!=null) && (member.getLocation()!=null && member.getLocation().getLat()!=null && member.getLocation().getLng()!=null)) {
			this.distance = MapUtils.getDistatce(location.getLat().doubleValue(),member.getLocation().getLat().doubleValue(),
					location.getLng().doubleValue(), member.getLocation().getLng().doubleValue());
		} else {
			this.distance = 0;
		}
		this.grade = member.getScore();
		this.address = member.getAddress();
		this.mobile=member.getMobile();
		this.developer=member.getMember();
		this.gender =member.getGender()==null?"":member.getGender().toString();
	}

	public void copyFrom(Long consumerId, Member member, Location location) {
		this.consumerId = consumerId;
		copyFrom(member, location);
	}
	
	public static List<MemberListModel> bindData(List<Member> members, Location location) {
		List<MemberListModel> models = new ArrayList<MemberListModel>();
		for (Member member:members) {
			MemberListModel model = new MemberListModel();
			model.copyFrom(member,location);
			models.add(model);
		}
		return models;
	}
	public static List<MemberListModel> bindConsumer(List<Consumer> consumers, Location location) {
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
	public static Set<MemberListModel> bindData(Set<Member> members, Location location) {
		Set<MemberListModel> models = new HashSet<MemberListModel>(members.size());
		for (Member member:members) {
			MemberListModel model = new MemberListModel();
			model.copyFrom(member,location);
			models.add(model);
		}
		return models;
	}
}
