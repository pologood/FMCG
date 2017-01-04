/**
 * @Title：TradeVo.java 
 * @Package：net.wit.controller.displayvo 
 * @Description：
 * @author：Chenlf
 * @date：2015年5月16日 上午11:42:04 
 * @version：V1.0   
 */

package net.wit.display.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import net.wit.support.FinalOrderStatus;

/**
 * @ClassName：TradeVo
 * @Description：
 * @author：Chenlf
 * @date：2015年5月16日 上午11:42:04
 */
public class TradeVo extends BaseVo {

	private Date createDate;

	private OrderVo order;

	private BigDecimal price;

	private Integer quantity;

	private TenantVo tenant;

	private String sn;
	
	private String receiver;

	private BigDecimal amount;

	private List<FinalOrderStatus> finalOrderStatus;
	
	private List<OrderItemVo> orderItems;

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public OrderVo getOrder() {
		return order;
	}

	public void setOrder(OrderVo order) {
		this.order = order;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public TenantVo getTenant() {
		return tenant;
	}

	public void setTenant(TenantVo tenant) {
		this.tenant = tenant;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public List<FinalOrderStatus> getFinalOrderStatus() {
		return finalOrderStatus;
	}

	public void setFinalOrderStatus(List<FinalOrderStatus> finalOrderStatus) {
		this.finalOrderStatus = finalOrderStatus;
	}

	/**
	 * @return the orderItems
	 */
	public List<OrderItemVo> getOrderItems() {
		return orderItems;
	}

	/**
	 * @param orderItems the orderItems to set
	 */
	public void setOrderItems(List<OrderItemVo> orderItems) {
		this.orderItems = orderItems;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	
}
