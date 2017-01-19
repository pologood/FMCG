package net.wit.controller.assistant.model;

import net.wit.entity.Consumer;
import net.wit.entity.Member;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SevenAddListModel extends BaseModel {
	/*ID*/
	private Long id;
	/*名称 */
	private String name;
	/*头像*/
	private String image;
	/*内容*/
	private String content;
	/*会员等级*/
	private String memberRank;
	/*创建时间*/
	private Date createDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMemberRank() {
		return memberRank;
	}

	public void setMemberRank(String memberRank) {
		this.memberRank = memberRank;
	}

	public void copyFrom(Consumer consumer) {
		Member member = consumer.getMember();
		if(member!=null){
        this.id = member.getId();
		this.name =member.getName();
		this.image = member.getHeadImg();
		if(member.getSourceType().equals(Member.SourceType.code)){
			this.content ="通过邀请码新增";
		}else if(member.getSourceType().equals(Member.SourceType.red) ){
			this.content ="通过红包新增";
		}else if(member.getSourceType().equals(Member.SourceType.coupon)){
			this.content ="通过代金券新增";
		}else if(member.getSourceType().equals(Member.SourceType.pad)){
			this.content ="通过购物屏新增";
		}else if(member.getSourceType().equals(Member.SourceType.wifi)){
			this.content ="通过wifi新增";
		}else if(member.getSourceType().equals(Member.SourceType.personal)){
			this.content ="通过个人新增";
		}
		if(member.getMemberRank()!=null){
			this.memberRank = member.getMemberRank().getName();
		}
		this.createDate = member.getCreateDate();

		}
	}
	
	public static  List<SevenAddListModel> bindData(List<Consumer> consumers) {
		List<SevenAddListModel> models = new ArrayList<SevenAddListModel>();
		for (Consumer consumer:consumers) {
			SevenAddListModel model = new SevenAddListModel();
			model.copyFrom(consumer);
			models.add(model);
		}
		return models;
	}
	
}
