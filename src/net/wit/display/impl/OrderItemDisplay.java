/**
 * @Title：OrderItemDisplay.java 
 * @Package：net.wit.display.impl 
 * @Description：
 * @author：Chenlf
 * @date：2015年5月16日 下午12:40:52 
 * @version：V1.0   
 */

package net.wit.display.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import net.wit.display.DisplayEngine;
import net.wit.display.vo.OrderItemVo;
import net.wit.display.vo.ProductVo;
import net.wit.display.vo.TradeVo;
import net.wit.entity.OrderItem;
import net.wit.entity.Product;
import net.wit.entity.Trade;

/**
 * @ClassName：OrderItemDisplay
 * @Description：
 * @author：Chenlf
 * @date：2015年5月16日 下午12:40:52
 */
@Component("orderItemDisplay")
public class OrderItemDisplay implements DisplayEngine<OrderItem, OrderItemVo> {

	@Resource(name = "productDisplay")
	private DisplayEngine<Product, ProductVo> productDisplay;

	@Resource(name = "tradeDisplay")
	private DisplayEngine<Trade, TradeVo> tradeDisplay;

	/*
	 * (non-Javadoc) <p>Title: convertList</p> <p>Description: </p>
	 * @param list
	 * @return
	 * @see net.wit.display.DisplayEngine#convertList(java.util.List)
	 */

	@Override
	public List<OrderItemVo> convertList(List<OrderItem> list) {
		List<OrderItemVo> items = new ArrayList<OrderItemVo>();
		for (OrderItem item : list) {
			items.add(convertEntity(item));
		}
		return items;
	}

	/*
	 * (non-Javadoc) <p>Title: convertEntity</p> <p>Description: </p>
	 * @param entity
	 * @return
	 * @see net.wit.display.DisplayEngine#convertEntity(java.lang.Object)
	 */

	@Override
	public OrderItemVo convertEntity(OrderItem d) {
		OrderItemVo item = new OrderItemVo();
		item.setCreateDate(d.getCreateDate());
		item.setId(d.getId());
		item.setFullName(d.getFullName());
		if (d.getProduct()!=null) {
		   item.setProduct(productDisplay.convertEntity(d.getProduct()));
		}  else {
			ProductVo vo = new ProductVo();
			vo.setId(d.getId());
			vo.setFullName(d.getFullName());
			vo.setCreateDate(d.getCreateDate());
			vo.setMarketPrice(d.getPrice());
			vo.setMedium(d.getThumbnail());
			vo.setSpecification_value(null);
			vo.setPrice(d.getPrice());
			vo.setStock(0);
			vo.setSales(0L);
			vo.setHits(0L);
			vo.setUnit(d.getPackagUnitName());
			item.setProduct(vo);
		}
		item.setThumbnail(d.getThumbnail());
		return item;
	}

}
