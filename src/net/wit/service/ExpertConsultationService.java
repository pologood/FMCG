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
import net.wit.entity.ExpertCategory;
import net.wit.entity.ExpertConsultation;
import net.wit.entity.Member;

/**
 * Service - 咨询
 * @author rsico Team
 * @version 3.0
 */
public interface ExpertConsultationService extends BaseService<ExpertConsultation, Long> {

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
	List<ExpertConsultation> findList(Member member, ExpertCategory expertCategory, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders);

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
	List<ExpertConsultation> findList(Boolean hasRepaly, Member member, ExpertCategory expertCategory, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders);

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
	List<ExpertConsultation> findList(Member member, ExpertCategory expertCategory, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders, String cacheRegion);

	/**
	 * 查找咨询分页
	 * @param type
	 * @param member 会员
	 * @param product 商品
	 * @param isShow 是否显示
	 * @param pageable 分页信息
	 * @return 不包含咨询回复
	 */
	Page<ExpertConsultation> findPage(Member member, ExpertCategory expertCategory, Boolean isShow, Pageable pageable);

	/**
	 * 查找咨询数量
	 * @param member 会员
	 * @param product 商品
	 * @param isShow 是否显示
	 * @return 咨询数量
	 */
	Long count(Member member, ExpertCategory expertCategory, Boolean isShow);

	/**
	 * 咨询回复
	 * @param consultation 咨询
	 * @param replyConsultation 回复咨询
	 */
	void reply(ExpertConsultation expertConsultation, ExpertConsultation replyExpertConsultation);

	/**
	 * @Title：findListReply
	 * @Description：
	 * @param expertConsultation
	 * @return List<ExpertConsultation>
	 */
	List<ExpertConsultation> findListReply(ExpertConsultation expertConsultation);

}