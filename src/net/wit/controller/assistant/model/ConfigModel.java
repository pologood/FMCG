package net.wit.controller.assistant.model;

import net.wit.controller.assistant.model.BaseModel;
import net.wit.entity.Host;

public class ConfigModel extends BaseModel {
	/*连接主机名*/
	private String host;
	/*连接数据库ID*/
	private Long dbid;
	/*连接端口*/
	private Long port;
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Long getDbid() {
		return dbid;
	}
	public void setDbid(Long dbid) {
		this.dbid = dbid;
	}
	public Long getPort() {
		return port;
	}
	public void setPort(Long port) {
		this.port = port;
	}
	
	public void copyFrom(Host host) {
		this.host = host.getHost();
		this.port = host.getPort();
		this.dbid = host.getDbid();
	}
	
}
