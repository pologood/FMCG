/**
 * @Title：TenantDisplay.java 
 * @Package：net.wit.display.impl 
 * @Description：
 * @author：Chenlf
 * @date：2015年5月16日 下午12:23:54 
 * @version：V1.0   
 */

package net.wit.display.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import net.wit.display.DisplayEngine;
import net.wit.display.vo.ProductCategoryTenantVo;
import net.wit.display.vo.TenantVo;
import net.wit.entity.ProductCategoryTenant;
import net.wit.entity.Tenant;

/**
 * @ClassName：TenantDisplay
 * @Description：
 * @author：Chenlf
 * @date：2015年5月16日 下午12:23:54
 */
@Component("tenantDisplay")
public class TenantDisplay implements DisplayEngine<Tenant, TenantVo> {

	/*
	 * (non-Javadoc) <p>Title: convertList</p> <p>Description: </p>
	 * @param list
	 * @return
	 * @see net.wit.display.DisplayEngine#convertList(java.util.List)
	 */

	@Override
	public List<TenantVo> convertList(List<Tenant> list) {

		List<TenantVo> tenants = new ArrayList<TenantVo>();
		for (Tenant t : list) {
			tenants.add(convertEntity(t));
		}
		return tenants;
	}

	/*
	 * (non-Javadoc) <p>Title: convertEntity</p> <p>Description: </p>
	 * @param entity
	 * @return
	 * @see net.wit.display.DisplayEngine#convertEntity(java.lang.Object)
	 */

	@Override
	public TenantVo convertEntity(Tenant t) {
		TenantVo vo = new TenantVo();
		vo.setName(t.getName());
		vo.setId(t.getId());
		vo.setCreateDate(t.getCreateDate());
		vo.setLogo(t.getLogo());
		vo.setScore(t.getScore());
		vo.setTelephone(t.getTelephone());
		vo.setThumbnail(t.getThumbnail());

		Set<ProductCategoryTenantVo> pcts = new HashSet<ProductCategoryTenantVo>();
		for (ProductCategoryTenant pct : t.getProductCategoryTenants()) {
			ProductCategoryTenantVo ptcvo = new ProductCategoryTenantVo();
			ptcvo.setId(pct.getId());
			ptcvo.setName(pct.getName());
			ptcvo.setCreateDate(pct.getCreateDate());
			pcts.add(ptcvo);
		}
		vo.setProductCategoryTenants(pcts);
		return vo;
	}

}
