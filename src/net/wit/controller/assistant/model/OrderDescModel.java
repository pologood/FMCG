package net.wit.controller.assistant.model;

import net.wit.support.FinalOrderStatus;

public class OrderDescModel extends BaseModel {

	private FinalOrderStatus.Status status;// 状态

	private String desc;// 描述


	public FinalOrderStatus.Status getStatus() {
		return status;
	}

	public void setStatus(FinalOrderStatus.Status status) {
		this.status = status;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
