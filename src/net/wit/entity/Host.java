/**
 *====================================================
 * 文件名称: HServer.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年5月9日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.entity;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * @ClassName: HServer
 * @Description: POS(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014年5月9日 下午3:26:44
 */
@Entity
@Table(name = "xx_host")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_host_sequence")
public class Host extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/** 主机名称 */
	@NotNull
	@Length(max = 100)
	private String name;
	
	/** 主机地址 */
	@NotNull
	@Length(max = 100)
	private String hostname;

	/** 主机端口 */
	@NotNull
	private Long port;

	/** 数据库 */
	@NotNull
	private Long dbid;

	/** 主站地址 */
	@NotNull
	@Length(max = 100)
	private String host;
	
	// ===========================================getter/setter===========================================//
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public Long getPort() {
		return port;
	}

	public void setPort(Long port) {
		this.port = port;
	}

	public Long getDbid() {
		return dbid;
	}

	public void setDbid(Long dbid) {
		this.dbid = dbid;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

}
