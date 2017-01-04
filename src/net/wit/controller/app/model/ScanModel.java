package net.wit.controller.app.model;

public class ScanModel extends BaseModel {

	/** 类型 */
	public enum Type {
		/** 用 webview 打开 */
		webview,
		/** 指向商品 */
		product,
		/** 指向商家 */
		tenant,
		/** 指向会员 */
	    member,
		/** 指向订单 */
	    trade
    }
	
	
	private Type type;
	private Long id;
	private String url;
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
}
