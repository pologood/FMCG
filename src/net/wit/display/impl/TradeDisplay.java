/**
 * @Title：TradeDisplay.java 
 * @Package：net.wit.display.impl 
 * @Description：
 * @author：Chenlf
 * @date：2015年5月16日 下午12:48:43 
 * @version：V1.0   
 */

package net.wit.display.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import net.wit.display.DisplayEngine;
import net.wit.display.vo.OrderItemVo;
import net.wit.display.vo.OrderVo;
import net.wit.display.vo.TenantVo;
import net.wit.display.vo.TradeVo;
import net.wit.entity.Order;
import net.wit.entity.OrderItem;
import net.wit.entity.Tenant;
import net.wit.entity.Trade;

/**
 * @ClassName：TradeDisplay
 * @Description：
 * @author：Chenlf
 * @date：2015年5月16日 下午12:48:43
 */
@Component("tradeDisplay")
public class TradeDisplay implements DisplayEngine<Trade, TradeVo> {

	@Resource(name = "orderDisplay")
	private DisplayEngine<Order, OrderVo> orderDisplay;

	@Resource(name = "tenantDisplay")
	private DisplayEngine<Tenant, TenantVo> tenantDisplay;

	@Resource(name = "orderItemDisplay")
	private DisplayEngine<OrderItem, OrderItemVo> orderItemDisplay;

	/*
	 * (non-Javadoc) <p>Title: convertList</p> <p>Description: </p>
	 * @param list
	 * @return
	 * @see net.wit.display.DisplayEngine#convertList(java.util.List)
	 */

	@Override
	public List<TradeVo> convertList(List<Trade> list) {
		List<TradeVo> trades = new ArrayList<TradeVo>();
		for (Trade td : list) {
			trades.add(convertEntity(td));
		}
		return trades;
	}

	/*
	 * (non-Javadoc) <p>Title: convertEntity</p> <p>Description: </p>
	 * @param entity
	 * @return
	 * @see net.wit.display.DisplayEngine#convertEntity(java.lang.Object)
	 */

	@Override
	public TradeVo convertEntity(Trade d) {
		TradeVo vo = new TradeVo();
		vo.setAmount(d.getAmount());
		vo.setCreateDate(d.getCreateDate());
		vo.setFinalOrderStatus(d.getFinalOrderStatus());
		vo.setId(d.getId());
		vo.setOrder(orderDisplay.convertEntity(d.getOrder()));
		vo.setPrice(d.getPrice());
		vo.setQuantity(d.getQuantity());
		vo.setSn(d.getOrder().getSn());
		vo.setReceiver(d.getOrder().getConsignee());
		vo.setTenant(tenantDisplay.convertEntity(d.getTenant()));
		vo.setOrderItems(orderItemDisplay.convertList(d.getOrderItems()));
		return vo;
	}

}
