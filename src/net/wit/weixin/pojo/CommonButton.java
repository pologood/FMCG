package net.wit.weixin.pojo;

/**
 * 普通按钮（子按钮）
 * 
 * @author Administrator
 *
 */
public class CommonButton extends Button {
	/**
	 * 按钮类型（click或view）
	 */
	private String type;  
	/**
	 * key值（当type=click使用）
	 */
    private String key;
    /**
     * url地址（当type=view时使用）
     */
    private String url;  
        
    public String getType() {  
        return type;  
    }  
        
    public void setType(String type) {  
        this.type = type;  
    }  
        
    public String getKey() {  
        return key;  
    }  
        
    public void setKey(String key) {  
        this.key = key;  
    }

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}  
}
