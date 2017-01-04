package net.wit.controller.app.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.wit.entity.MemberBank;
import net.wit.entity.MemberBank.Type;

public class MemberBankModel extends BaseModel {
	
	/**ID**/
	private Long id;
	/** 类型 */
	private Type type;
	/** 卡号 */
	private String cardNo;
	/** 开户行 */
	private String bankName;
	/** 银行LOGO */
	private String logo;
	/** 开户名 */
	private String account;
	/** 背景颜色 */
	private String bkgColor;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getBkgColor() {
		return bkgColor;
	}

	public void setBkgColor(String bkgColor) {
		this.bkgColor = bkgColor;
	}

	public void copyFrom(MemberBank memberBank) {
		this.id = memberBank.getId();
		this.type = memberBank.getType();
		String no = memberBank.getCardNo();
		this.cardNo = no.substring(no.length()-4,no.length());
		this.bankName = memberBank.getDepositBank();
		this.account = memberBank.getDepositUser();
		if(memberBank.getBankInfo()!=null){
			this.logo = memberBank.getBankInfo().getLogo();
			this.bkgColor=memberBank.getBankInfo().getBkgColor();
		}

	}
	
	public static List<MemberBankModel> bindData(List<MemberBank> memberBanks) {
		List<MemberBankModel> models = new ArrayList<MemberBankModel>();
		for (MemberBank memberBank:memberBanks) {
			MemberBankModel model = new MemberBankModel();
			model.copyFrom(memberBank);
			models.add(model);
		}
		return models;
	}
	public static Set<MemberBankModel> bindData(Set<MemberBank> memberBanks) {
		Set<MemberBankModel> models = new HashSet<MemberBankModel>(memberBanks.size());
		for (MemberBank memberBank:memberBanks) {
			MemberBankModel model = new MemberBankModel();
			model.copyFrom(memberBank);
			models.add(model);
		}
		return models;
	}
	
	
}
