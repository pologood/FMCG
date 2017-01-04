/**
 * @Title：ProductDisplay.java 
 * @Package：net.wit.controller.displayvo 
 * @Description：
 * @author：Chenlf
 * @date：2015年5月16日 下午12:03:59 
 * @version：V1.0   
 */

package net.wit.display.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import net.wit.display.DisplayEngine;
import net.wit.display.vo.ProductVo;
import net.wit.entity.Product;

/**
 * @ClassName：ProductDisplay
 * @Description： 商品
 * @author：Chenlf
 * @date：2015年5月16日 下午12:03:59
 */
@Component("productDisplay")
public class ProductDisplay implements DisplayEngine<Product, ProductVo> {

	/*
	 * (non-Javadoc) <p>Title: convert</p> <p>Description: </p>
	 * @param list
	 * @return
	 * @see net.wit.controller.displayvo.DisplayEngine#convert(java.util.List)
	 */

	@Override
	public List<ProductVo> convertList(List<Product> list) {
		ArrayList<ProductVo> ps = new ArrayList<ProductVo>();
		if (list != null) {
			for (Product d : list) {
				ps.add(convertEntity(d));
			}
		}
		return ps;
	}

	/*
	 * (non-Javadoc) <p>Title: convertEntity</p> <p>Description: </p>
	 * @param entity
	 * @return
	 * @see net.wit.display.DisplayEngine#convertEntity(java.lang.Object)
	 */

	@Override
	public ProductVo convertEntity(Product d) {
		ProductVo vo = new ProductVo();
		vo.setId(d.getId());
		vo.setFullName(d.getFullName());
		vo.setCreateDate(d.getCreateDate());
		vo.setMarketPrice(d.getMarketPrice());
		vo.setMedium(d.getThumbnail());
		vo.setSpecification_value(d.getSpecification_value());
		vo.setPrice(d.getPrice());
		vo.setStock(d.getStock());
		vo.setSales(d.getSales());
		vo.setHits(d.getHits());
		vo.setUnit(d.getUnit());
		return vo;
	}

}
