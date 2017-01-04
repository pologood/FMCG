package net.wit.support;

import java.util.Date;

import net.wit.entity.Message.Type;

public class MessageModel {

    /*消息类型*/
	public Type type;
    /*消息id*/
	public Long id;
    /*内容*/
	public String msg;
     /*标题*/
	public String title;
    /*单号*/
	public Long sid;
	/*时间*/
	private Date create_date;
	
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getTitle() {
		if (title==null) {
			return "系统消息";
		}
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getSid() {
		return sid;
	}
	public void setSid(Long sid) {
		this.sid = sid;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	
}

