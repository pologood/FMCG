package net.wit.controller.app.model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.wit.entity.MemberRank;

public class MemberPriceModel extends BaseModel {
	/*等级*/
	private MemberRankModel memberRank;
	/*价格*/
	private BigDecimal price;
	
	
	public MemberRankModel getMemberRank() {
		return memberRank;
	}

	public void setMemberRank(MemberRankModel memberRank) {
		this.memberRank = memberRank;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void copyFrom(MemberRank memberRank,BigDecimal price) {
		MemberRankModel rank = new MemberRankModel();
		rank.copyFrom(memberRank);
		this.memberRank = rank;
		this.price = price;
	}
	
	public static Set<MemberPriceModel> bindData(Map<MemberRank, BigDecimal> prices) {
		Set<MemberPriceModel> models = new HashSet<MemberPriceModel>();
		for (MemberRank memberRank:prices.keySet()) {
			MemberPriceModel model = new MemberPriceModel();
			model.copyFrom(memberRank,prices.get(memberRank));
			models.add(model);
		}
		return models;
	}
	
}
