package net.wit.controller.assistant.model;

import net.wit.controller.assistant.model.BaseModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BankInfoModel extends BaseModel {
	
	/**ID**/
	private Long id;
	/**银行代码**/
	private String bankCode;
	/**银行名称**/
	private String bankName;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public void copyFrom(net.wit.entity.BankInfo bankInfo) {
		this.id = bankInfo.getId();
		this.bankCode = bankInfo.getBankCode();
		this.bankName = bankInfo.getDepositBank();
	}
	
	public static List<BankInfoModel> bindData(List<net.wit.entity.BankInfo> banks) {
		List<BankInfoModel> models = new ArrayList<BankInfoModel>();
		for (net.wit.entity.BankInfo bankInfo:banks) {
			BankInfoModel model = new BankInfoModel();
			model.copyFrom(bankInfo);
			models.add(model);
		}
		return models;
	}
	public static Set<BankInfoModel> bindData(Set<net.wit.entity.BankInfo> bankInfos) {
		Set<BankInfoModel> models = new HashSet<BankInfoModel>(bankInfos.size());
		for (net.wit.entity.BankInfo bankInfo:bankInfos) {
			BankInfoModel model = new BankInfoModel();
			model.copyFrom(bankInfo);
			models.add(model);
		}
		return models;
	}
	
	
}
