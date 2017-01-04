package net.wit.controller.store.model;

import net.wit.controller.app.model.BaseModel;
import net.wit.entity.MemberRank;

import java.util.ArrayList;
import java.util.List;

public class MemberRankModel extends BaseModel {
	/*等级ID*/
	private long id;
	/*等级名*/
	private String name;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
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
