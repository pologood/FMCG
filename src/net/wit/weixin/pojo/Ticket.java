/**
 * @Title：Ticket.java 
 * @Package：net.wit.weixin.pojo 
 * @Description：
 * @author：Chenlf
 * @date：2015年3月15日 下午9:39:43 
 * @version：V1.0   
 */

package net.wit.weixin.pojo;

import java.util.Date;

/**
 * @ClassName：Ticket
 * @Description：
 * @author：Chenlf
 * @date：2015年3月15日 下午9:39:43
 */
public class Ticket {

	private Integer errcode;

	private String errmsg;

	private String ticket;

	private Integer expires_in;

	private Date expire;

	public Integer getErrcode() {
		return errcode;
	}

	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Integer getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(Integer expires_in) {
		this.expires_in = expires_in;
	}

	public Date getExpire() {
		return expire;
	}

	public void setExpire(Date expire) {
		this.expire = expire;
	}

}
