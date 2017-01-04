package net.wit.controller.assistant.model;

import net.wit.entity.Tenant;
import net.wit.entity.UnionTenant;
import net.wit.entity.VisitRecord;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VisitRecordListModel extends BaseModel {
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

	public void copyFrom(VisitRecord visitRecord) {
        this.id = visitRecord.getId();
		this.name =visitRecord.getMember().getName();
		this.image = visitRecord.getMember().getHeadImg();
		this.content = visitRecord.getContent();
		if(visitRecord.getMember().getMemberRank()!=null){
			this.memberRank = visitRecord.getMember().getMemberRank().getName();
		}

		this.createDate = visitRecord.getCreateDate();

	}
	
	public static  List<VisitRecordListModel> bindData(List<VisitRecord> visitRecords) {
		List<VisitRecordListModel> models = new ArrayList<VisitRecordListModel>();
		for (VisitRecord visitRecord:visitRecords) {
			VisitRecordListModel model = new VisitRecordListModel();
			model.copyFrom(visitRecord);
			models.add(model);
		}
		return models;
	}
	
}
