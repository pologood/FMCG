package net.wit.controller.helper.model;

import net.wit.Message;

public class DataBlock {
	
	/** "无效会员 */
	public static final String SESSION_INVAILD = "session.invaild";
	/** "无效店铺 */
	public static final String TENANT_INVAILD = "tenant.invaild";
	
	/*协议头*/
	private Message message;
	/*数据*/
	private Object data;
	
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	public DataBlock(Message message) {
		this.message = message;
		this.data = null;
	}
	
	/**
	 * 返回成功消息
	 * 
	 * @param content
	 *            内容
	 * @param args
	 *            参数
	 * @return 成功消息
	 */
	public static DataBlock success(Object data, String content, Object... args) {
		DataBlock dataBlock = new DataBlock(Message.success(content, args));
		dataBlock.setData(data);
		return dataBlock;
	}
	
	/**
	 * 返回失败消息
	 * 
	 * @param content
	 *            内容
	 * @param args
	 *            参数
	 * @return 成功消息
	 */
	public static DataBlock error(String content, Object... args) {
		return new DataBlock(Message.error(content, args));
	}
	
	/**
	 * 返回警告消息
	 * 
	 * @param content
	 *            内容
	 * @param args
	 *            参数
	 * @return 成功消息
	 */
	public static DataBlock warn(String content, Object... args) {
		return new DataBlock(Message.warn(content, args));
	}

}
