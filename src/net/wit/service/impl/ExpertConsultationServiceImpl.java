/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import net.wit.Filter;
import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ExpertConsultationDao;
import net.wit.entity.ExpertCategory;
import net.wit.entity.ExpertConsultation;
import net.wit.entity.Member;
import net.wit.service.ExpertConsultationService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 咨询
 * @author rsico Team
 * @version 3.0
 */
@Service("expertConsultationServiceImpl")
public class ExpertConsultationServiceImpl extends BaseServiceImpl<ExpertConsultation, Long> implements ExpertConsultationService {

	@Resource(name = "expertConsultationDaoImpl")
	private ExpertConsultationDao expertConsultationDao;

	@Resource(name = "expertConsultationDaoImpl")
	public void setBaseDao(ExpertConsultationDao expertConsultationDao) {
		super.setBaseDao(expertConsultationDao);
	}

	@Transactional(readOnly = true)
	public List<ExpertConsultation> findList(Member member, ExpertCategory expertCategory, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders) {
		return expertConsultationDao.findList(false, member, expertCategory, isShow, count, filters, orders);
	}

	@Transactional(readOnly = true)
	public List<ExpertConsultation> findList(Boolean hasRepaly, Member member, ExpertCategory expertCategory, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders) {
		return expertConsultationDao.findList(hasRepaly, member, expertCategory, isShow, count, filters, orders);
	}

	@Transactional(readOnly = true)
	@Cacheable("expertConsultation")
	public List<ExpertConsultation> findList(Member member, ExpertCategory expertCategory, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders, String cacheRegion) {
		return expertConsultationDao.findList(false, member, expertCategory, isShow, count, filters, orders);
	}

	@Transactional(readOnly = true)
	public Page<ExpertConsultation> findPage(Member member, ExpertCategory expertCategory, Boolean isShow, Pageable pageable) {
		return expertConsultationDao.findPage(member, expertCategory, isShow, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Member member, ExpertCategory expertCategory, Boolean isShow) {
		return expertConsultationDao.count(member, expertCategory, isShow);
	}

	@CacheEvict(value = { "expert", "expertCategory", "expertConsultation" }, allEntries = true)
	@Transactional
	public void reply(ExpertConsultation expertConsultation, ExpertConsultation replyExpertConsultation) {
		if (expertConsultation == null || replyExpertConsultation == null) {
			return;
		}
		replyExpertConsultation.setIsShow(true);
		replyExpertConsultation.setExpertCategory(expertConsultation.getExpertCategory());
		replyExpertConsultation.setForExpertConsultation(expertConsultation);
		expertConsultationDao.persist(replyExpertConsultation);

		expertConsultation.getReplyExpertConsultations().add(replyExpertConsultation);
		expertConsultation.setIsShow(true);
		expertConsultationDao.merge(expertConsultation);

		// Product product = consultation.getProduct();
		// if (product != null) {
		// consultationDao.flush();
		// staticService.build(product);
		// }
	}

	@Override
	@Transactional
	@CacheEvict(value = { "expert", "expertCategory", "expertConsultation" }, allEntries = true)
	public void save(ExpertConsultation expertConsultation) {
		super.save(expertConsultation);
		ExpertCategory expertCategory = expertConsultation.getExpertCategory();
		if (expertCategory != null) {
			expertConsultationDao.flush();
		}
	}

	@Override
	@Transactional
	@CacheEvict(value = { "expert", "expertCategory", "expertConsultation" }, allEntries = true)
	public ExpertConsultation update(ExpertConsultation expertConsultation) {
		ExpertConsultation pExpertConsultation = super.update(expertConsultation);
		ExpertCategory expertCategory = pExpertConsultation.getExpertCategory();
		if (expertCategory != null) {
			expertConsultationDao.flush();
		}
		return pExpertConsultation;
	}

	@Override
	@Transactional
	@CacheEvict(value = { "expert", "expertCategory", "expertConsultation" }, allEntries = true)
	public ExpertConsultation update(ExpertConsultation expertConsultation, String... ignoreProperties) {
		return super.update(expertConsultation, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "expert", "expertCategory", "expertConsultation" }, allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "expert", "expertCategory", "expertConsultation" }, allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "expert", "expertCategory", "expertConsultation" }, allEntries = true)
	public void delete(ExpertConsultation expertConsultation) {
		if (expertConsultation != null) {
			super.delete(expertConsultation);
			ExpertCategory expertCategory = expertConsultation.getExpertCategory();
			if (expertCategory != null) {
				expertConsultationDao.flush();
			}
		}
	}

	@Transactional(readOnly = true)
	public List<ExpertConsultation> findListReply(ExpertConsultation expertConsultation) {
		return expertConsultationDao.findListReply(expertConsultation);
	}

}