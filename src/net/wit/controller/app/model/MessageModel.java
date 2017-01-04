package net.wit.controller.app.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.wit.entity.Message;
import net.wit.entity.Message.Type;
import org.apache.lucene.search.FieldCache;

public class MessageModel extends BaseModel {
	
	/*消息ID*/
	private Long id;
	/*标题*/
	private String title;
	/*内容*/
	private String content;
	/*单号*/
	private Long sid;
	/*图片*/
	private String image;
	/*类型*/
	private Type type;
	/*标离*/
	private Boolean read;
	/*时间*/
	private Date create_date;

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

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Boolean getRead() {
		return read;
	}

	public void setRead(Boolean read) {
		this.read = read;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public void copyFrom(Message message) {
		this.id = message.getId();
		this.title = message.getTitle();
		this.content = message.getContent();
		if (message.getTrade()!=null) {
			this.sid = message.getTrade().getId();
		}
		this.type = message.getType();
		this.read = message.getReceiverRead();
		this.create_date = message.getCreateDate();
		if(message.getType()==Type.contact&&message.getSn()!=null){
			this.sid=Long.parseLong(message.getSn());
		}
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
