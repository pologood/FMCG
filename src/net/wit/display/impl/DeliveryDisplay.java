/**
 * @Title：DeliveryDisplay.java 
 * @Package：net.wit.controller.displayvo 
 * @Description：
 * @author：Chenlf
 * @date：2015年5月16日 上午11:58:40 
 * @version：V1.0   
 */

package net.wit.display.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import net.wit.display.DisplayEngine;
import net.wit.display.vo.DeliveryVo;
import net.wit.display.vo.ProductCategoryTenantVo;
import net.wit.display.vo.TenantVo;
import net.wit.entity.DeliveryCenter;
import net.wit.entity.ProductCategoryTenant;
import net.wit.entity.Tenant;

/**
 * @ClassName：DeliveryDisplay
 * @Description：商家
 * @author：Chenlf
 * @date：2015年5月16日 上午11:58:40
 */
@Component("deliveryDisplay")
public class DeliveryDisplay implements DisplayEngine<DeliveryCenter, DeliveryVo> {

	@Resource(name = "tenantDisplay")
	private DisplayEngine<Tenant, TenantVo> tenantDisplay;

	/*
	 * (non-Javadoc) <p>Title: convert</p> <p>Description: </p>
	 * @param list
	 * @return
	 * @see net.wit.controller.displayvo.DisplayEngine#convert(java.util.List)
	 */

	@Override
	public List<DeliveryVo> convertList(List<DeliveryCenter> list) {
		ArrayList<DeliveryVo> ls = new ArrayList<DeliveryVo>();
		if (list != null) {
			for (DeliveryCenter d : list) {
				ls.add(convertEntity(d));
			}
		}
		return ls;
	}

	/*
	 * (non-Javadoc) <p>Title: convertEntity</p> <p>Description: </p>
	 * @param entity
	 * @return
	 * @see net.wit.display.DisplayEngine#convertEntity(java.lang.Object)
	 */

	@Override
	public DeliveryVo convertEntity(DeliveryCenter d) {
		DeliveryVo vo = new DeliveryVo();
		vo.setId(d.getId());
		vo.setCreateDate(d.getCreateDate());
		vo.setName(d.getName());
		vo.setAddress(d.getAddress());
		vo.setDistance(new BigDecimal(-1));
		vo.setAreaName(d.getAreaName());
		TenantVo t = tenantDisplay.convertEntity(d.getTenant());

		vo.setTenant(t);
		return vo;
	}

}
