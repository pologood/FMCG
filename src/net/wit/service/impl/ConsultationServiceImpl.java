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
import net.wit.dao.ConsultationDao;
import net.wit.entity.Article;
import net.wit.entity.Consultation;
import net.wit.entity.Consultation.Type;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.service.ConsultationService;
import net.wit.service.StaticService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 咨询
 * @author rsico Team
 * @version 3.0
 */
@Service("consultationServiceImpl")
public class ConsultationServiceImpl extends BaseServiceImpl<Consultation, Long> implements ConsultationService {

	@Resource(name = "consultationDaoImpl")
	private ConsultationDao consultationDao;

	@Resource(name = "staticServiceImpl")
	private StaticService staticService;

	@Resource(name = "consultationDaoImpl")
	public void setBaseDao(ConsultationDao consultationDao) {
		super.setBaseDao(consultationDao);
	}

	@Transactional(readOnly = true)
	public List<Consultation> findList(Member member, Product product, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders) {
		return consultationDao.findList(false, member, product, isShow, count, filters, orders);
	}

	@Transactional(readOnly = true)
	public List<Consultation> findList(Boolean hasRepaly, Member member, Product product, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders) {
		return consultationDao.findList(hasRepaly, member, product, isShow, count, filters, orders);
	}

	@Transactional(readOnly = true)
	@Cacheable("consultation")
	public List<Consultation> findList(Member member, Product product, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders, String cacheRegion) {
		return consultationDao.findList(false, member, product, isShow, count, filters, orders);
	}

	@Transactional(readOnly = true)
	public Page<Consultation> findPage(Type type, Member member, Product product, Boolean isShow, Pageable pageable) {
		return consultationDao.findPage(type, member, product, isShow, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Consultation> findMyPage(Member member, Product product, Boolean isShow, Pageable pageable) {
		return consultationDao.findMyPage(member, product, isShow, pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Member member, Product product, Boolean isShow) {
		return consultationDao.count(member, product, isShow);
	}

	@CacheEvict(value = { "product", "productCategory", "review", "consultation" }, allEntries = true)
	@Transactional
	public void reply(Consultation consultation, Consultation replyConsultation) {
		if (consultation == null || replyConsultation == null) {
			return;
		}
		replyConsultation.setIsShow(true);
		replyConsultation.setProduct(consultation.getProduct());
		replyConsultation.setForConsultation(consultation);
		consultationDao.persist(replyConsultation);

		consultation.getReplyConsultations().add(replyConsultation);
		consultation.setIsShow(true);
		consultationDao.merge(consultation);

		// Product product = consultation.getProduct();
		// if (product != null) {
		// consultationDao.flush();
		// staticService.build(product);
		// }
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review", "consultation" }, allEntries = true)
	public void save(Consultation consultation) {
		super.save(consultation);
		Product product = consultation.getProduct();
		if (product != null) {
			consultationDao.flush();
			staticService.build(product);
		}
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review", "consultation" }, allEntries = true)
	public Consultation update(Consultation consultation) {
		Consultation pConsultation = super.update(consultation);
		Product product = pConsultation.getProduct();
		if (product != null) {
			consultationDao.flush();
			staticService.build(product);
		}
		return pConsultation;
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review", "consultation" }, allEntries = true)
	public Consultation update(Consultation consultation, String... ignoreProperties) {
		return super.update(consultation, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review", "consultation" }, allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review", "consultation" }, allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review", "consultation" }, allEntries = true)
	public void delete(Consultation consultation) {
		if (consultation != null) {
			super.delete(consultation);
			Product product = consultation.getProduct();
			if (product != null) {
				consultationDao.flush();
				staticService.build(product);
			}
		}
	}
	@Transactional(readOnly = true)
	public List<Consultation> findListByArticle(Article article) {
		return consultationDao.findListByArticle(article);
	}

}