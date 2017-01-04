/**
 * @Title：OrderDisplay.java 
 * @Package：net.wit.display.impl 
 * @Description：
 * @author：Chenlf
 * @date：2015年5月16日 下午12:46:01 
 * @version：V1.0   
 */

package net.wit.display.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import net.wit.display.DisplayEngine;
import net.wit.display.vo.OrderVo;
import net.wit.entity.Order;

/**
 * @ClassName：OrderDisplay
 * @Description：
 * @author：Chenlf
 * @date：2015年5月16日 下午12:46:01
 */
@Component("orderDisplay")
public class OrderDisplay implements DisplayEngine<Order, OrderVo> {

	/*
	 * (non-Javadoc) <p>Title: convertList</p> <p>Description: </p>
	 * @param list
	 * @return
	 * @see net.wit.display.DisplayEngine#convertList(java.util.List)
	 */

	@Override
	public List<OrderVo> convertList(List<Order> list) {
		List<OrderVo> orders = new ArrayList<OrderVo>();
		for (Order o : list) {
			orders.add(convertEntity(o));
		}
		return orders;
	}

	/*
	 * (non-Javadoc) <p>Title: convertEntity</p> <p>Description: </p>
	 * @param entity
	 * @return
	 * @see net.wit.display.DisplayEngine#convertEntity(java.lang.Object)
	 */

	@Override
	public OrderVo convertEntity(Order d) {
		OrderVo vo = new OrderVo();
		vo.setCreateDate(d.getCreateDate());
		vo.setId(d.getId());
		vo.setSn(d.getSn());
		return vo;
	}

}
