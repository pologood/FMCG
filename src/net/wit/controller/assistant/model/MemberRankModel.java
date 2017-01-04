package net.wit.controller.assistant.model;

import net.wit.controller.assistant.model.BaseModel;
import net.wit.entity.MemberRank;

import java.util.ArrayList;
import java.util.List;

public class MemberRankModel extends BaseModel {
	/*等级ID*/
	private Long id;
	/*等级名*/
	private String name;
	
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
	
	public void copyFrom(MemberRank memberRank) {
		this.id = memberRank.getId();
		this.name = memberRank.getName();
	}
	
	public static List<MemberRankModel> bindData(List<MemberRank> memberRanks) {
		List<MemberRankModel> models = new ArrayList<MemberRankModel>();
		for (MemberRank memberRank:memberRanks) {
			MemberRankModel model = new MemberRankModel();
			model.copyFrom(memberRank);
			models.add(model);
		}
		return models;
	}
	
}
