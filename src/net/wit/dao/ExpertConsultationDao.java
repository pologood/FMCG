/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import java.util.List;

import net.wit.Filter;
import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Consultation;
import net.wit.entity.ExpertCategory;
import net.wit.entity.ExpertConsultation;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.Consultation.Type;

/**
 * Dao - 咨询
 * @author rsico Team
 * @version 3.0
 */
public interface ExpertConsultationDao extends BaseDao<ExpertConsultation, Long> {

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
	List<ExpertConsultation> findList(Member member, ExpertCategory expertCategory, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders);

	/**
	 * 查找咨询数量
	 * @param member 会员
	 * @param product 商品
	 * @param isShow 是否显示
	 * @return 咨询数量
	 */
	Long count(Member member, ExpertCategory expertCategory, Boolean isShow);

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
	 * 查找咨询分页
	 * @param type
	 * @param member 会员
	 * @param product 商品
	 * @param isShow 是否显示
	 * @param pageable 分页信息
	 * @return 咨询分页
	 */
	Page<ExpertConsultation> findPage(Member member, ExpertCategory expertCategory, Boolean isShow, Pageable pageable);

	/**
	 * @Title：findListReply
	 * @Description：
	 * @param expertConsultation
	 * @return List<ExpertConsultation>
	 */
	List<ExpertConsultation> findListReply(ExpertConsultation expertConsultation);
}