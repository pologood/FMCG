/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONObject;
import net.wit.Filter;
import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.DeliveryCenterDao;
import net.wit.dao.DeviceDao;
import net.wit.dao.MemberDao;
import net.wit.entity.Area;
import net.wit.entity.Community;
import net.wit.entity.DeliveryCenter;
import net.wit.entity.Location;
import net.wit.entity.Member;
import net.wit.entity.Tag;
import net.wit.entity.Tenant;
import net.wit.entity.Tenant.OrderType;
import net.wit.entity.TenantCategory;
import net.wit.service.DeliveryCenterService;
import net.wit.support.DeliveryComparatorByDistance;
import net.wit.util.Constants;
import net.wit.util.HttpRequestProxy;
import net.wit.util.MapUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 广告
 * @author rsico Team
 * @version 3.0
 */
@Service("deliveryCenterServiceImpl")
public class DeliveryCenterServiceImpl extends BaseServiceImpl<DeliveryCenter, Long> implements DeliveryCenterService {

	@Resource(name = "deliveryCenterDaoImpl")
	private DeliveryCenterDao deliveryCenterDao;

	@Resource(name = "deviceDaoImpl")
	private DeviceDao deviceDao;

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;

	@Resource(name = "deliveryCenterDaoImpl")
	public void setBaseDao(DeliveryCenterDao DeliveryCenterDao) {
		super.setBaseDao(DeliveryCenterDao);
	}

	@Transactional(readOnly = true)
	public DeliveryCenter findDefault() {
		return deliveryCenterDao.findDefault();
	}

	@Transactional(readOnly = true)
	public DeliveryCenter findDefault(Tenant tennat) {
		return deliveryCenterDao.findDefault(tennat);
	}

	@Transactional(readOnly = true)
	public List<DeliveryCenter> findMyAll(Member member) {
		return deliveryCenterDao.findMyAll(member);
	}

	public Page<DeliveryCenter> findPage(Member member, Location location, Pageable pageable) {
		return deliveryCenterDao.findPage(member, location, pageable);
	}

	/**
	 * 查找最近发货点
	 * @param location 查找最近发货点
	 * @return
	 */
	public DeliveryCenter findByLocation(Tenant tenant, Location location) {
		List<DeliveryCenter> lists = deliveryCenterDao.findMyAll(tenant.getMember());
		DeliveryCenter current = null;
		if (location!=null) {
		double distance = 9999999;
		for (DeliveryCenter center : lists) {
			if (center.getLocation() != null) {
				double dis = MapUtils.getDistatce(location.getLat().doubleValue(), center.getLocation().getLat().doubleValue(), location.getLng().doubleValue(), center.getLocation().getLng().doubleValue());
				if (dis < distance) {
					distance = dis;
					current = center;
				}
			}
		} }
		if (current == null) {
			current = findDefault(tenant);
		}
		if (current==null && lists.size()>0) {
			current = lists.get(0);
		}
		return current;
	}

	/**
	 * dsp用户登录
	 * @param username
	 * @param password
	 * @return
	 */
	private boolean dspLogin(String username, String password) {
		final String url = Constants.Video.loginUrl;
		Map<String, String> map = new HashMap<String, String>();
		map.put("userName", username);
		map.put("clubLoginPwd", password);
		map.put("productType", "V");
		String json_result = HttpRequestProxy.doGet(url, map);
		if (StringUtils.isNotEmpty(json_result)) {
			JSONObject jsonObject = JSONObject.fromObject(json_result);
			String result = jsonObject.getString("result");
			if ("1".equals(result)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 查找发货点
	 * @param code 实体店编号
	 * @return
	 */
	public DeliveryCenter findByCode(Tenant tenant, String code) {
		return deliveryCenterDao.findByCode(tenant, code);
	}

	public Page<DeliveryCenter> findPage(Set<TenantCategory> tenantCategories, Area area, Community community, Location location, Double distance, OrderType orderType, Pageable pageable) {
		List<DeliveryCenter> all = deliveryCenterDao.findList(tenantCategories, area, community, location, distance);
		DeliveryComparatorByDistance comparatorByDistance = new DeliveryComparatorByDistance(orderType);
		comparatorByDistance.setLocation(location);
		Collections.sort(all, comparatorByDistance);
		int fromindex = (pageable.getPageNumber() - 1) * pageable.getPageSize();
		int endindex = fromindex + pageable.getPageSize();
		if (endindex > all.size()) {
			endindex = all.size();
		}
		if (endindex <= fromindex) {
			return new Page<DeliveryCenter>(new ArrayList<DeliveryCenter>(), 0, pageable);
		}
		return new Page<DeliveryCenter>(new ArrayList<DeliveryCenter>(all.subList(fromindex, endindex)), all.size(), pageable);
	}

	/*
	 * (non-Javadoc) <p>Title: findList</p> <p>Description: </p>
	 * @param tenantCategory
	 * @param tags
	 * @param area
	 * @param community
	 * @param periferal
	 * @param count
	 * @param filters
	 * @param orders
	 * @return
	 * @see net.wit.service.DeliveryCenterService#findList(net.wit.entity.TenantCategory, java.util.List, net.wit.entity.Area, net.wit.entity.Community,
	 * java.lang.Boolean, java.lang.Integer, java.util.List, java.util.List)
	 */

	@Override
	public List<DeliveryCenter> findList(TenantCategory tenantCategory, List<Tag> tags, Area area, Boolean isDefault, Community community, Boolean periferal, Integer count, List<Filter> filters, List<Order> orders) {
		return deliveryCenterDao.findList(tenantCategory, tags, area, isDefault, community, periferal, count, filters, orders);
	}

	/*
	 * (non-Javadoc) <p>Title: findList</p> <p>Description: </p>
	 * @param tenantCategory
	 * @param tags
	 * @param area
	 * @param community
	 * @param periferal
	 * @param count
	 * @param filters
	 * @param orders
	 * @param cacheRegion
	 * @return
	 * @see net.wit.service.DeliveryCenterService#findList(net.wit.entity.TenantCategory, java.util.List, net.wit.entity.Area, net.wit.entity.Community,
	 * java.lang.Boolean, java.lang.Integer, java.util.List, java.util.List, java.lang.String)
	 */
	@Cacheable("deliverys")
	public List<DeliveryCenter> findList(TenantCategory tenantCategory, List<Tag> tags, Area area, Boolean isDefault, Community community, Boolean periferal, Integer count, List<Filter> filters, List<Order> orders, String cacheRegion) {
		return deliveryCenterDao.findList(tenantCategory, tags, area, isDefault, community, periferal, count, filters, orders);
	}

	/*
	 * (non-Javadoc) <p>Title: findList</p> <p>Description: </p>
	 * @param tenantCategorys
	 * @param tenantTags
	 * @param current
	 * @param community
	 * @param count
	 * @return
	 * @see net.wit.service.DeliveryCenterService#findList(java.util.Set, java.util.ArrayList, net.wit.entity.Area, net.wit.entity.Community, java.lang.Integer)
	 */

	public List<DeliveryCenter> findList(Set<TenantCategory> tenantCategorys, List<Tag> tenantTags, Area area, Community community, Integer count) {
		return deliveryCenterDao.findList(tenantCategorys, tenantTags, area, community, count);
	}

	public List<DeliveryCenter> findList(TenantCategory tenantCategory, Date beginDate, Date endDate, Integer first, Integer count) {
		return deliveryCenterDao.findList(tenantCategory, beginDate, endDate, first, count);
	}
	public List<DeliveryCenter> findourStoreList(Tenant tenant) {
		return deliveryCenterDao.findourStoreList(tenant);
	}
	public Page<DeliveryCenter> findPage(String keyword, Pageable pageable){
		return deliveryCenterDao.findPage(keyword, pageable);
	}
}