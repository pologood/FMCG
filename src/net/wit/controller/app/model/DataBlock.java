package net.wit.controller.app.model;

import java.util.List;

import net.wit.Message;
import net.wit.Page;

public class DataBlock {
	
	/** "无效会员 */
	public static final String SESSION_INVAILD = "session.invaild";
	/** "无效店铺 */
	public static final String TENANT_INVAILD = "tenant.invaild";
	
	/*协议头*/
	private Message message;
	/*数据*/
	private Object data;
	/*数据*/
	private Object PageModel;
	
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
	
	public Object getPageModel() {
		return PageModel;
	}
	public void setPageModel(Object pageModel) {
		PageModel = pageModel;
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
	 * 返回成功消息
	 * 
	 * @param content
	 *            内容
	 * @param args
	 *            参数
	 * @return 成功消息
	 */
	public static DataBlock success(Object data,Page page, String content, Object... args) {
		DataBlock dataBlock = new DataBlock(Message.success(content, args));
		dataBlock.setData(data);
		PageModel model = new PageModel();
		model.copyFrom(page);
		dataBlock.setPageModel(model);
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
