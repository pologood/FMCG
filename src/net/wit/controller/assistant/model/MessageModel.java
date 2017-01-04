package net.wit.controller.assistant.model;

import net.wit.controller.app.model.BaseModel;
import net.wit.entity.Message;
import net.wit.entity.Message.Type;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageModel extends BaseModel {
	
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
	/*订单*/
	private Long sid;

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

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

	public void copyFrom(Message message) {
		this.id = message.getId();
		this.title = message.getTitle();
		this.content = message.getContent();
		this.create_date = message.getCreateDate();
		this.image=message.getImage();

	}
	
	public static List<MessageModel> bindData(List<Message> messages) {
		List<MessageModel> models = new ArrayList<MessageModel>();
		for (Message message:messages) {
			MessageModel model = new MessageModel();
			model.copyFrom(message);
			models.add(model);
		}
		return models;
	}
	
	
}
