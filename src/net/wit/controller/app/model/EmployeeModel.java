package net.wit.controller.app.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.wit.entity.Employee;
import net.wit.entity.Location;
import net.wit.entity.Member;
import net.wit.entity.Member.Gender;

public class EmployeeModel extends BaseModel {
	/*ID*/
	private Long id;
	/** 员工的member编号*/
	private Long  memberId;
	/*会员名*/
	private String nickName;
	/*性别*/
	private Gender gender;
	/*头像*/
	private String headImg;
	/*更新时间*/
	private Date modify_date;
	/*电话*/
	private String mobile;
	/*地址*/
	private String address;
	/*所属门店*/
	private SingleModel deliveryCenter;
	/*所属企业*/
	private SingleModel tenant;
	/*角色*/
	private String role;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
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

	public Date getModify_date() {
		return modify_date;
	}

	public void setModify_date(Date modify_date) {
		this.modify_date = modify_date;
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

	public SingleModel getDeliveryCenter() {
		return deliveryCenter;
	}

	public void setDeliveryCenter(SingleModel deliveryCenter) {
		this.deliveryCenter = deliveryCenter;
	}

	public SingleModel getTenant() {
		return tenant;
	}

	public void setTenant(SingleModel tenant) {
		this.tenant = tenant;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void copyFrom(Employee employee,Location location) {
		this.id = employee.getId();
		Member member = employee.getMember();
		this.nickName = member.getDisplayName();
		this.memberId=member.getId();
		this.headImg = member.getHeadImg();
		MemberRankModel memberRankModel = new MemberRankModel();
		memberRankModel.copyFrom(member.getMemberRank());
		SingleModel tenant = new SingleModel();
		if (employee.getTenant()!=null) {
		   tenant.setId(employee.getTenant().getId());
		   tenant.setName(employee.getTenant().getName());
		}
		this.tenant = tenant;
		SingleModel deliveryCenter = new SingleModel();
		if (employee.getDeliveryCenter()!=null && employee.getDeliveryCenter().getId()!=null) {
		   deliveryCenter.setId(employee.getDeliveryCenter().getId());
		   deliveryCenter.setName(employee.getDeliveryCenter().getName());
		}
		this.deliveryCenter = deliveryCenter;
		this.modify_date = member.getLbsDate();
		this.address = member.getAddress();
		this.gender = member.getGender();
		this.role = employee.getRole();
	}
	
	public static List<EmployeeModel> bindData(List<Employee> emps,Location location) {
		List<EmployeeModel> models = new ArrayList<EmployeeModel>();
		for (Employee emp:emps) {
				EmployeeModel model = new EmployeeModel();
				model.copyFrom(emp,location);
				models.add(model);
		}
		return models;
	}

	public static List<EmployeeModel> bindDatas(List<Employee> emps,Member memberBoss,Location location) {
		List<EmployeeModel> models = new ArrayList<EmployeeModel>();
		//员工列表新增店主
		EmployeeModel modelBoss = new EmployeeModel();
		modelBoss.id = memberBoss.getId();
		modelBoss.memberId = memberBoss.getId();
		modelBoss.nickName = memberBoss.getDisplayName();
		SingleModel deliveryCenterBoss = new SingleModel();
		deliveryCenterBoss.setId(0L);
		deliveryCenterBoss.setName("店主");
		modelBoss.deliveryCenter = deliveryCenterBoss;
		SingleModel tenant = new SingleModel();
			tenant.setId(memberBoss.getTenant().getId());
			tenant.setName(memberBoss.getTenant().getName());
		modelBoss.tenant = tenant;
		models.add(modelBoss);
		for (Employee emp:emps) {
			if(!memberBoss.getId().equals(emp.getMember().getId())){
				EmployeeModel model = new EmployeeModel();
				model.copyFrom(emp,location);
				models.add(model);
			}
		}
		return models;
	}
}
