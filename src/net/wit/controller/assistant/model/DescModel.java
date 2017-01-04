package net.wit.controller.assistant.model;

import net.wit.entity.Ad;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DescModel extends BaseModel {

	private String status;// 状态

	private String desc;// 描述


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
