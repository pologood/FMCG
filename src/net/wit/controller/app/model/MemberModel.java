package net.wit.controller.app.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.wit.entity.Idcard.AuthStatus;
import net.wit.entity.Consumer;
import net.wit.entity.Member;
import net.wit.entity.Member.BindStatus;
import net.wit.entity.Member.Gender;
import net.wit.util.StringUtils;

public class MemberModel extends BaseModel {
	/*会员ID*/
	private Long id;
	/*会员名*/
	private String name;
	/*用户名*/
	private String username;
	/*昵称*/
	private String nickName;
	/*性别*/
	private Gender gender;
	/*是否设置支付密码*/
	private Boolean hasPaymentPassword;
	/*所在区域*/
	private AreaModel area;
	/*手机号*/
	private String mobile;
	/*星级*/
	private Float grade;
	/*地址*/
	private String address;
	/*头像*/
	private String headImg;
	/*等级*/
	private MemberRankModel memberRank;
	/*余额*/
	private BigDecimal balance;
	/*冷结余额*/
	private BigDecimal freezeBalance;
	/*是否实名*/
	private AuthStatus authStatus;
	/*拒绝原因*/
	private String auth_descr;
	/*身份证号*/
	private String idNo;
	/*绑定手机*/
	private BindStatus bindMobile;
	/* 店铺  */
	private SingleModel tenant;
	/* 是否店主  */
	private Boolean owner;
	/* 店主  */
	private String role;
	/* 购物车商品数量  */
	private Integer cartItemCount;
	
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
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public AreaModel getArea() {
		return area;
	}
	public void setArea(AreaModel area) {
		this.area = area;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getFreezeBalance() {
		return freezeBalance;
	}
	public void setFreezeBalance(BigDecimal freezeBalance) {
		this.freezeBalance = freezeBalance;
	}
	public AuthStatus getAuthStatus() {
		return authStatus;
	}
	public void setAuthStatus(AuthStatus authStatus) {
		this.authStatus = authStatus;
	}
	public BindStatus getBindMobile() {
		return bindMobile;
	}
	public void setBindMobile(BindStatus bindMobile) {
		this.bindMobile = bindMobile;
	}
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Float getGrade() {
		return grade;
	}
	public void setGrade(Float grade) {
		this.grade = grade;
	}	
	public SingleModel getTenant() {
		return tenant;
	}
	public void setTenant(SingleModel tenant) {
		this.tenant = tenant;
	}
	
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Boolean getHasPaymentPassword() {
		return hasPaymentPassword;
	}

	public void setHasPaymentPassword(Boolean hasPaymentPassword) {
		this.hasPaymentPassword = hasPaymentPassword;
	}

	public String getAuth_descr() {
		return auth_descr;
	}
	public void setAuth_descr(String auth_descr) {
		this.auth_descr = auth_descr;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	
	public Boolean getOwner() {
		return owner;
	}
	public void setOwner(Boolean owner) {
		this.owner = owner;
	}
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

	public Integer getCartItemCount() {
		return cartItemCount;
	}

	public void setCartItemCount(Integer cartItemCount) {
		this.cartItemCount = cartItemCount;
	}

	public void copyFrom(Member member) {
		this.id = member.getId();
		this.name = member.getName();
		this.username = member.getUsername();
		
		if (this.name == null) {
			this.nickName = member.getDisplayName();
		} else {
			this.nickName = member.getName();
		}

		this.address = member.getAddress();
		AreaModel areaModel = new AreaModel();
	    areaModel.copyFrom(member.getArea());
		this.area = areaModel;
		this.headImg = member.getHeadImg();
		MemberRankModel memberRankModel = new MemberRankModel();
		memberRankModel.copyFrom(member.getMemberRank());
		this.memberRank = memberRankModel;
		this.balance = member.getBalance();
		this.freezeBalance = member.getFreezeBalance();
		this.bindMobile = member.getBindMobile();
		this.authStatus = member.getAuthStatus();
		if (this.authStatus.equals(AuthStatus.fail)) {
			this.auth_descr = member.getIdcard().getMemo();
		}
		if (this.authStatus.equals(AuthStatus.success)) {
			String no = member.getIdcard().getNo();
		    this.idNo = StringUtils.mosaic(no,3,"~~");
		}
		this.grade = member.getScore();
		this.mobile = member.getMobile();
		this.gender = member.getGender();
		this.hasPaymentPassword= org.apache.commons.lang.StringUtils.isNotBlank(member.getPaymentPassword());
		SingleModel tenant = new SingleModel();
		if (member.getTenant()!=null) {
		   tenant.setId(member.getTenant().getId());
		   tenant.setName(member.getTenant().getName());
		   if (member.getTenant().getMember().equals(member)) {
			   this.owner = true;
		   } else {
			   this.owner = false;
		   }
		} else {
		   tenant = null;
		   this.owner = false;
		}
		this.tenant = tenant;

		if(member.getCart()!=null){
			this.cartItemCount=member.getCart().getQuantity();
		}else {
			this.cartItemCount=0;
		}

	}
	
	public void copyConsumer(Consumer consumer) {
		this.copyFrom(consumer.getMember());
		MemberRankModel  rank = new MemberRankModel();
		rank.copyFrom(consumer.getMemberRank());
		this.setMemberRank(rank);
	}
	
	public static List<MemberModel> bindData(List<Member> praises) {
		List<MemberModel> models = new ArrayList<MemberModel>();
		for (Member praise:praises) {
			MemberModel model = new MemberModel();
			model.copyFrom(praise);
			models.add(model);
		}
		return models;
	}

	public static List<MemberModel> bindConsumerData(List<Consumer> consumers) {
		List<MemberModel> models = new ArrayList<MemberModel>();
		for (Consumer consumer:consumers) {
			MemberModel model = new MemberModel();
			model.copyConsumer(consumer);
			models.add(model);
		}
		return models;
	}
	
}
