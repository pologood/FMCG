package net.wit.controller.assistant.model;

import net.wit.controller.app.model.BaseModel;
import net.wit.entity.Message;
import net.wit.entity.Message.Type;
import net.wit.entity.Trade;
import net.wit.support.FinalOrderStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderMessageModel extends BaseModel {
	
	/*消息ID*/
	private Long id;
	/*状态*/
	private FinalOrderStatus finalOrderStatus;
	/*内容*/
	private String content;
	/*单号*/
	private String sn;
	/*订单id*/
	private Long tradeId;
	/*图片*/
	private String image;
	/*类型*/
	private BigDecimal price;
	/*时间*/
	private Date create_date;
	/*是否已读*/
	private Boolean receiverRead;

	public Date getCreate_date() {
		return create_date;
	}

	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	public FinalOrderStatus getFinalOrderStatus() {
		return finalOrderStatus;
	}

	public void setFinalOrderStatus(FinalOrderStatus finalOrderStatus) {
		this.finalOrderStatus = finalOrderStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTradeId() {
		return tradeId;
	}

	public void setTradeId(Long tradeId) {
		this.tradeId = tradeId;
	}

	public Boolean getReceiverRead() {
		return receiverRead;
	}

	public void setReceiverRead(Boolean receiverRead) {
		this.receiverRead = receiverRead;
	}

	public void copyFrom(Message message) {
		this.id = message.getId();
		this.content = message.getContent();
		if (message.getTrade()!=null) {
			this.tradeId = message.getTrade().getId();
			this.finalOrderStatus  = message.getTrade().getFinalOrderStatus().get(0);
			this.sn = message.getTrade().getOrder().getSn();
			this.price = message.getTrade().getAmount();
			this.image=message.getTrade().getOrder().getMember().getHeadImg();
		}
		this.create_date = message.getCreateDate();
		this.receiverRead = message.getReceiverRead();

	}
	
	public static List<OrderMessageModel> bindData(List<Message> messages) {
		List<OrderMessageModel> models = new ArrayList<OrderMessageModel>();
		for (Message message:messages) {
			OrderMessageModel model = new OrderMessageModel();
			model.copyFrom(message);
			models.add(model);
		}
		return models;
	}
	
	
}
