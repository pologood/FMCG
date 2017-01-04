package net.wit.weixin.message.respnew;

import java.util.List;

public class BaseMessage {
	/** 消息类型 */
	public enum msgType {
		text, voice, image, mpnews
	}

	private List<String> touser;

	private msgType msgtype;

	public List<String> getTouser() {
		return touser;
	}

	public void setTouser(List<String> touser) {
		this.touser = touser;
	}

	public msgType getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(msgType msgtype) {
		this.msgtype = msgtype;
	}

}
