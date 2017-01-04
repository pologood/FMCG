/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.Principal;
import net.wit.dao.AreaDao;
import net.wit.dao.MemberDao;
import net.wit.entity.Area;
import net.wit.entity.Member;
import net.wit.service.AreaService;

import net.wit.util.LBSUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Service - 地区
 * @author rsico Team
 * @version 3.0
 */
@Service("areaServiceImpl")
public class AreaServiceImpl extends BaseServiceImpl<Area, Long> implements AreaService {

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;

	@Resource(name = "areaDaoImpl")
	private AreaDao areaDao;

	@Resource(name = "areaDaoImpl")
	public void setBaseDao(AreaDao areaDao) {
		super.setBaseDao(areaDao);
	}

	@Transactional(readOnly = true)
	public List<Area> findRoots() {
		return areaDao.findRoots(null);
	}

	@Transactional(readOnly = true)
	public List<Area> findRoots(Integer count) {
		return areaDao.findRoots(count);
	}

	@Override
	@Transactional
	@CacheEvict(value = "area", allEntries = true)
	public void save(Area area) {
		super.save(area);
	}

	@Override
	@Transactional
	@CacheEvict(value = "area", allEntries = true)
	public Area update(Area area) {
		return super.update(area);
	}

	@Override
	@Transactional
	@CacheEvict(value = "area", allEntries = true)
	public Area update(Area area, String... ignoreProperties) {
		return super.update(area, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = "area", allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = "area", allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = "area", allEntries = true)
	public void delete(Area area) {
		super.delete(area);
	}

	public Area findByCode(String code) {
		return areaDao.findByCode(code);
	}

	public Area findByTelCode(String code) {
		return areaDao.findByTelCode(code);
	}

	@Transactional(readOnly = true)
	public Area getCurrent() {
		try {
			RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
			if (requestAttributes != null) {
				HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
				Principal principal = (Principal) request.getSession().getAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME);
				if (principal != null) {
					Member member = memberDao.find(principal.getId());
					if (member.getLbsCity()!=null) {
						return member.getLbsCity();
					}
				}
				Area area = (Area) request.getSession().getAttribute(Member.AREA_ATTRIBUTE_NAME);
				if (area == null) {
					ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
					area = areaDao.find(Long.parseLong(bundle.getString("localArea")));
				}
				return area;
			} else {
				ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
				Area area = areaDao.find(Long.parseLong(bundle.getString("localArea")));
				return area;
			}
		} catch (Exception e) {
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
			return areaDao.find(Long.parseLong(bundle.getString("localArea")));
		}
	}

	@Transactional(readOnly = true)
	public Area findByZipCode(String zipCode) {
		return areaDao.findByZipCode(zipCode);
	}

	@Transactional(readOnly = true)
	public Area findByAreaName(String areaName) {
		return areaDao.findByAreaName(areaName);
	}

	@Transactional(readOnly = true)
	public List<Area> findOpenList() {
		return areaDao.findOpenList();
	}
	
	@Transactional(readOnly = true)
	public Area findByLbs(double lat,double lng) {
        String adCode = LBSUtil.getAreaFromLocation(String.valueOf(lat),String.valueOf(lng));
        String code = adCode.substring(0,4).concat("00");
		Area area = areaDao.findByCode(code);
		if (!area.getStatus().equals(Area.Status.enabled)) {
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
			area = areaDao.find(Long.parseLong(bundle.getString("localArea")));
		}
		return area;
	}

	@Transactional(readOnly = true)
	public List<Area> findChildren(Area area, Integer count){
		return areaDao.findChildren(area, count);
	}
}