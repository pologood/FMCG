/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import java.util.List;

import net.wit.Filter;
import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Article;
import net.wit.entity.Consultation;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.Tag.Type;

/**
 * Service - 咨询
 * @author rsico Team
 * @version 3.0
 */
public interface ConsultationService extends BaseService<Consultation, Long> {

	/**
	 * 查找咨询
	 * @param member 会员
	 * @param product 商品
	 * @param isShow 是否显示
	 * @param count 数量
	 * @param filters 筛选
	 * @param orders 排序
	 * @return 咨询,不包含咨询回复
	 */
	List<Consultation> findList(Member member, Product product, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders);

	/**
	 * 查找咨询
	 * @param member 会员
	 * @param product 商品
	 * @param isShow 是否显示
	 * @param count 数量
	 * @param filters 筛选
	 * @param orders 排序
	 * @return 咨询
	 */
	List<Consultation> findList(Boolean hasRepaly, Member member, Product product, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders);

	/**
	 * 查找咨询(缓存)
	 * @param member 会员
	 * @param product 商品
	 * @param isShow 是否显示
	 * @param count 数量
	 * @param filters 筛选
	 * @param orders 排序
	 * @param cacheRegion 缓存区域
	 * @return 咨询(缓存),不包含咨询回复
	 */
	List<Consultation> findList(Member member, Product product, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders, String cacheRegion);

	/**
	 * 查找咨询分页
	 * @param type
	 * @param member 会员
	 * @param product 商品
	 * @param isShow 是否显示
	 * @param pageable 分页信息
	 * @return 不包含咨询回复
	 */
	Page<Consultation> findPage(net.wit.entity.Consultation.Type type, Member member, Product product, Boolean isShow, Pageable pageable);

	/**
	 * 查找咨询分页
	 * @param member 会员
	 * @param product 商品
	 * @param isShow 是否显示
	 * @param pageable 分页信息
	 * @return 不包含咨询回复
	 */
	Page<Consultation> findMyPage(Member member, Product product, Boolean isShow, Pageable pageable);

	/**
	 * 查找咨询数量
	 * @param member 会员
	 * @param product 商品
	 * @param isShow 是否显示
	 * @return 咨询数量
	 */
	Long count(Member member, Product product, Boolean isShow);

	/**
	 * 咨询回复
	 * @param consultation 咨询
	 * @param replyConsultation 回复咨询
	 */
	void reply(Consultation consultation, Consultation replyConsultation);

	/**
	 * @Title：findListByArticle
	 * @Description：
	 * @param article
	 * @return List<Consultation>
	 */
	List<Consultation> findListByArticle(Article article);

}