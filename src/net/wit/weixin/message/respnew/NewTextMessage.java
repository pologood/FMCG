package net.wit.weixin.message.respnew;

/**
 * 文本消息
 * @author Administrator
 */
public class NewTextMessage extends BaseMessage {

	private Content text;

	public Content getText() {
		return text;
	}

	public void setText(Content text) {
		this.text = text;
	}
}
