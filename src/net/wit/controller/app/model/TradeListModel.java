package net.wit.controller.app.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.wit.entity.Trade;
import net.wit.support.FinalOrderStatus;

public class TradeListModel extends BaseModel {
	
	/**等级ID**/
	private Long id;
	/**订单号**/
	private String sn;
	/**制单日期**/
	private Date create_date;
	/**缩略图**/
	private String thumbnail;
	/**收货人**/
	private String consignee;
	/**运费**/
	private BigDecimal freight;
	/**订单总价**/
	private BigDecimal amount;
	/**订单件数**/
	private int quantity;
	/**订单状态**/
	private FinalOrderStatus finalOrderStatus;
	/**第一个商品**/
	private OrderItemModel orderItem;
	/**订单商品**/
	private List<OrderItemModel> orderItems;
	
	/**商家名称 **/
	private TenantListModel tenant;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public FinalOrderStatus getFinalOrderStatus() {
		return finalOrderStatus;
	}

	public void setFinalOrderStatus(FinalOrderStatus finalOrderStatus) {
		this.finalOrderStatus = finalOrderStatus;
	}

	public OrderItemModel getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(OrderItemModel orderItem) {
		this.orderItem = orderItem;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public TenantListModel getTenant() {
		return tenant;
	}

	public void setTenant(TenantListModel tenant) {
		this.tenant = tenant;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	public List<OrderItemModel> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItemModel> orderItems) {
		this.orderItems = orderItems;
	}

	public void copyFrom(Trade trade) {
		this.id = trade.getId();
		this.consignee = trade.getOrder().getConsignee();
		this.create_date = trade.getCreateDate();
		this.sn = trade.getOrder().getSn();
		this.thumbnail = trade.getOrderItems().get(0).getThumbnail();
		this.amount = trade.getAmount();
	    this.freight = trade.getFreight();
		this.quantity = trade.getQuantity();
		this.finalOrderStatus = trade.getFinalOrderStatus().get(0);
		OrderItemModel model = new OrderItemModel();
		model.copyFrom(trade.getOrderItems(false).get(0));	
		this.orderItem = model;
		TenantListModel tenant = new TenantListModel();
		tenant.copyFrom(trade.getTenant());
		this.tenant = tenant;
		this.orderItems = OrderItemModel.bindData(trade.getOrderItems());
	}
		
	public static List<TradeListModel> bindData(List<Trade> trades) {
		List<TradeListModel> models = new ArrayList<TradeListModel>();
		for (Trade trade:trades) {
			TradeListModel model = new TradeListModel();
			model.copyFrom(trade);
			models.add(model);
		}
		return models;
	}
	
	
}
