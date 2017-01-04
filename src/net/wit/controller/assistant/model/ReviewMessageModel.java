package net.wit.controller.assistant.model;

import net.wit.controller.app.model.BaseModel;
import net.wit.entity.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewMessageModel extends BaseModel {
	
	/*消息ID*/
	private Long id;
	/*标题*/
	private String title;
	/*内容*/
	private String content;
	/*图片*/
	private String image;
	/*时间*/
	private Date create_date;
	/*订单ID*/
	private Long tradeId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public Long getTradeId() {
		return tradeId;
	}

	public void setTradeId(Long tradeId) {
		this.tradeId = tradeId;
	}

	public void copyFrom(Message message) {
		this.id = message.getId();
		this.title = message.getTitle();
		this.content = message.getContent();
		this.create_date = message.getCreateDate();
		if(message.getTrade()!=null){
			this.image=message.getTrade().getOrder().getMember().getHeadImg();
			this.tradeId = message.getTrade().getId();
		}
	}
	
	public static List<ReviewMessageModel> bindData(List<Message> messages) {
		List<ReviewMessageModel> models = new ArrayList<ReviewMessageModel>();
		for (Message message:messages) {
			ReviewMessageModel model = new ReviewMessageModel();
			model.copyFrom(message);
			models.add(model);
		}
		return models;
	}
	
	
}
