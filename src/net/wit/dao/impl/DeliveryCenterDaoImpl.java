/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import net.wit.Filter;
import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.DeliveryCenterDao;
import net.wit.entity.Area;
import net.wit.entity.Community;
import net.wit.entity.DeliveryCenter;
import net.wit.entity.Location;
import net.wit.entity.Member;
import net.wit.entity.Tag;
import net.wit.entity.Tenant;
import net.wit.entity.Tenant.Status;
import net.wit.entity.TenantCategory;
import net.wit.support.DeliveryComparatorByDistance;
import net.wit.support.EntitySupport;
import net.wit.util.MapUtils;

import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * Dao - 发货点
 * @author rsico Team
 * @version 3.0
 */
@Repository("deliveryCenterDaoImpl")
public class DeliveryCenterDaoImpl extends BaseDaoImpl<DeliveryCenter, Long> implements DeliveryCenterDao {

	public DeliveryCenter findDefault() {
		try {
			String jpql = "select deliveryCenter from DeliveryCenter deliveryCenter where deliveryCenter.isDefault = true and tenant=:tenant";
			Tenant tenant = EntitySupport.createInitTenant();
			tenant.setId(1L);
			return entityManager.createQuery(jpql, DeliveryCenter.class).setParameter("tenant", tenant).setFlushMode(FlushModeType.COMMIT).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public DeliveryCenter findDefault(Tenant tenant) {
		try {
			String jpql = "select deliveryCenter from DeliveryCenter deliveryCenter where deliveryCenter.isDefault = true and tenant=:tenant";
			return entityManager.createQuery(jpql, DeliveryCenter.class).setParameter("tenant", tenant).setFlushMode(FlushModeType.COMMIT).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * 查找发货点
	 * @param code 实体店编号
	 * @return
	 */
	public DeliveryCenter findByCode(Tenant tenant, String code) {
		try {
			String jpql = "select deliveryCenter from DeliveryCenter deliveryCenter where tenant=:tenant and sn=:sn";
			return entityManager.createQuery(jpql, DeliveryCenter.class).setParameter("tenant", tenant).setParameter("sn", code).setFlushMode(FlushModeType.COMMIT).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * 处理默认并保存
	 * @param deliveryCenter 发货点
	 */
	@Override
	public void persist(DeliveryCenter deliveryCenter) {
		Assert.notNull(deliveryCenter);
		if (deliveryCenter.getIsDefault()) {
			String jpql = "update DeliveryCenter deliveryCenter set deliveryCenter.isDefault = false where deliveryCenter.isDefault = true and deliveryCenter.tenant = :tenant";
			entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", deliveryCenter.getTenant()).executeUpdate();
		}
		super.persist(deliveryCenter);
	}

	/**
	 * 处理默认并更新
	 * @param deliveryCenter 发货点
	 * @return 发货点
	 */
	@Override
	public DeliveryCenter merge(DeliveryCenter deliveryCenter) {
		Assert.notNull(deliveryCenter);
		if (deliveryCenter.getIsDefault()) {
			String jpql = "update DeliveryCenter deliveryCenter set deliveryCenter.isDefault = false where deliveryCenter.isDefault = true and deliveryCenter.tenant = :tenant and deliveryCenter != :deliveryCenter";
			entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", deliveryCenter.getTenant()).setParameter("deliveryCenter", deliveryCenter).executeUpdate();
		}
		return super.merge(deliveryCenter);
	}

	public List<DeliveryCenter> findMyAll(Member member) {
		try {
			String jpql = "select deliveryCenter from DeliveryCenter deliveryCenter where tenant=:tenant";
			return entityManager.createQuery(jpql, DeliveryCenter.class).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", member.getTenant()).getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<DeliveryCenter> findList(Area area, Community community) {
		if ((area == null) && (community == null)) {
			return new ArrayList<DeliveryCenter>();
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<DeliveryCenter> criteriaQuery = criteriaBuilder.createQuery(DeliveryCenter.class);
		Root<DeliveryCenter> root = criteriaQuery.from(DeliveryCenter.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (area != null) {
			restrictions = criteriaBuilder.and(restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("area"), area), criteriaBuilder.like(root.get("area").<String> get("treePath"), "%" + Area.TREE_PATH_SEPARATOR + area.getId() + Area.TREE_PATH_SEPARATOR + "%")));
		}
		if (community != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("community"), community));
		}
		criteriaQuery.where(restrictions);
		return entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getResultList();
	}

	public List<DeliveryCenter> findList(Area area, Location location, BigDecimal distatce) {
		if (location == null || !location.isExists()) {
			return new ArrayList<DeliveryCenter>();
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<DeliveryCenter> criteriaQuery = criteriaBuilder.createQuery(DeliveryCenter.class);
		Root<DeliveryCenter> root = criteriaQuery.from(DeliveryCenter.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (area != null) {
			restrictions = criteriaBuilder.and(restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("area"), area), criteriaBuilder.like(root.get("area").<String> get("treePath"), "%" + Area.TREE_PATH_SEPARATOR + area.getId() + Area.TREE_PATH_SEPARATOR + "%")));
		}
		criteriaQuery.where(restrictions);
		List<DeliveryCenter> dvcs = entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getResultList();
		List<DeliveryCenter> data = new ArrayList<DeliveryCenter>();

		DeliveryComparatorByDistance comparatorByDistance = new DeliveryComparatorByDistance();
		comparatorByDistance.setLocation(location);
		Collections.sort(data, comparatorByDistance);
		return data;
	}

	public Page<DeliveryCenter> findPage(Member member, Location location, Pageable pageable) {
		if (member == null) {
			return new Page<DeliveryCenter>(Collections.<DeliveryCenter> emptyList(), 0, pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<DeliveryCenter> criteriaQuery = criteriaBuilder.createQuery(DeliveryCenter.class);
		Root<DeliveryCenter> root = criteriaQuery.from(DeliveryCenter.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), member.getTenant()));
		criteriaQuery.where(criteriaBuilder.equal(root.get("tenant"), member.getTenant()));
		if (location != null && location.isExists()) {
			List<DeliveryCenter> dvcs = entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getResultList();
			DeliveryComparatorByDistance comparatorByDistance = new DeliveryComparatorByDistance();
			comparatorByDistance.setLocation(location);
			Collections.sort(dvcs, comparatorByDistance);
			int fromindex = (pageable.getPageNumber() - 1) * pageable.getPageSize();
			int endindex = fromindex + pageable.getPageSize();
			if (endindex > dvcs.size()) {
				endindex = dvcs.size();
			}
			if (endindex <= fromindex) {
				return new Page<DeliveryCenter>(new ArrayList<DeliveryCenter>(), 0, pageable);
			}
			return new Page<DeliveryCenter>(new ArrayList<DeliveryCenter>(dvcs.subList(fromindex, endindex)), dvcs.size(), pageable);
		} else {
			return super.findPage(criteriaQuery, pageable);
		}
	}

	public List<DeliveryCenter> findListByLocation(Location location, Area area, Community community, BigDecimal distance) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<DeliveryCenter> criteriaQuery = criteriaBuilder.createQuery(DeliveryCenter.class);
		Root<DeliveryCenter> root = criteriaQuery.from(DeliveryCenter.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (area != null) {
			restrictions = criteriaBuilder.and(restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("area"), area), criteriaBuilder.like(root.get("area").<String> get("treePath"), "%" + Area.TREE_PATH_SEPARATOR + area.getId() + Area.TREE_PATH_SEPARATOR + "%")));
		}
		if (community != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("community"), community));
		}
		if (distance != null && location != null && location.isExists()) {
			MapUtils mapUtils = new MapUtils(distance.doubleValue());
			mapUtils.rectangle4Point(location.getLat().doubleValue(), location.getLng().doubleValue());
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.get("location").<BigDecimal> get("lat"), mapUtils.getLeft_bottom().getLat()));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.get("location").<BigDecimal> get("lat"), mapUtils.getLeft_top().getLat()));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.get("location").<BigDecimal> get("lng"), mapUtils.getLeft_top().getLng()));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.get("location").<BigDecimal> get("lng"), mapUtils.getRight_bottom().getLng()));
		}

		criteriaQuery.where(restrictions);

		return entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getResultList();

	}

	public Page<DeliveryCenter> findPage(Set<TenantCategory> tenantCategories, Area area, Community community, Location location, Double distance, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<DeliveryCenter> criteriaQuery = criteriaBuilder.createQuery(DeliveryCenter.class);
		Root<DeliveryCenter> root = criteriaQuery.from(DeliveryCenter.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (area != null) {
			restrictions = criteriaBuilder.and(restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("area"), area), criteriaBuilder.like(root.get("area").<String> get("treePath"), "%" + Area.TREE_PATH_SEPARATOR + area.getId() + Area.TREE_PATH_SEPARATOR + "%")));
		}
		if (community != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("community"), community));
		}
		// if (distance != null && location != null && location.isExists()) { // 检查过的
		// MapUtils mapUtils = new MapUtils(distance.doubleValue());
		// mapUtils.rectangle4Point(location.getLat().doubleValue(), location.getLng().doubleValue());
		// restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.get("location").<BigDecimal> get("lat"),
		// mapUtils.getLeft_bottom().getLat()));
		// restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.get("location").<BigDecimal> get("lat"), mapUtils.getLeft_top().getLat()));
		// restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.get("location").<BigDecimal> get("lng"), mapUtils.getLeft_top().getLng()));
		// restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.get("location").<BigDecimal> get("lng"),
		// mapUtils.getRight_bottom().getLng()));
		// }
		if (tenantCategories != null && tenantCategories.size() > 0) {
			Predicate categoryPredicate = criteriaBuilder.conjunction();
			Predicate categoryPredicate1 = null;
			for (TenantCategory tenantCategory : tenantCategories) {
				String categoryTreePath = TenantCategory.TREE_PATH_SEPARATOR + tenantCategory.getId() + TenantCategory.TREE_PATH_SEPARATOR;
				if (categoryPredicate1 == null) {
					categoryPredicate = criteriaBuilder.or(criteriaBuilder.equal(root.get("tenant").get("tenantCategory"), tenantCategory), criteriaBuilder.like(root.get("tenant").get("tenantCategory").<String> get("treePath"), categoryTreePath + "%"));
				} else {
					categoryPredicate = criteriaBuilder.or(categoryPredicate,
							criteriaBuilder.or(criteriaBuilder.equal(root.get("tenant").get("tenantCategory"), tenantCategory), criteriaBuilder.like(root.get("tenant").get("tenantCategory").<String> get("treePath"), categoryTreePath + "%")));
				}
				categoryPredicate1 = criteriaBuilder.or(categoryPredicate1, categoryPredicate);
			}
			restrictions = criteriaBuilder.and(restrictions, categoryPredicate);
		} else {
			return new Page(new ArrayList<DeliveryCenter>(), 0, pageable);
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.<Status> get("tenant").get("status"), Status.success));
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	/*
	 * (non-Javadoc) <p>Title: findList</p> <p>Description: </p>
	 * @param tenantCategories
	 * @param area
	 * @param community
	 * @param location
	 * @param distance
	 * @return
	 * @see net.wit.dao.DeliveryCenterDao#findList(java.util.Set, net.wit.entity.Area, net.wit.entity.Community, net.wit.entity.Location, java.lang.Double)
	 */

	@Override
	public List<DeliveryCenter> findList(Set<TenantCategory> tenantCategories, Area area, Community community, Location location, Double distance) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<DeliveryCenter> criteriaQuery = criteriaBuilder.createQuery(DeliveryCenter.class);
		Root<DeliveryCenter> root = criteriaQuery.from(DeliveryCenter.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (area != null) {
			restrictions = criteriaBuilder.and(restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("area"), area), criteriaBuilder.like(root.get("area").<String> get("treePath"), "%" + Area.TREE_PATH_SEPARATOR + area.getId() + Area.TREE_PATH_SEPARATOR + "%")));
		}
		if (community != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("community"), community));
		}
		// if (distance != null && location != null && location.isExists()) {
		// MapUtils mapUtils = new MapUtils(distance.doubleValue());
		// mapUtils.rectangle4Point(location.getLat().doubleValue(), location.getLng().doubleValue());
		// restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.get("location").<BigDecimal> get("lat"),
		// mapUtils.getLeft_bottom().getLat()));
		// restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.get("location").<BigDecimal> get("lat"), mapUtils.getLeft_top().getLat()));
		// restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.get("location").<BigDecimal> get("lng"), mapUtils.getLeft_top().getLng()));
		// restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.get("location").<BigDecimal> get("lng"),
		// mapUtils.getRight_bottom().getLng()));
		// }
		if (tenantCategories != null && tenantCategories.size() > 0) {
			Predicate categoryPredicate = criteriaBuilder.conjunction();
			Predicate categoryPredicate1 = null;
			for (TenantCategory tenantCategory : tenantCategories) {
				String categoryTreePath = TenantCategory.TREE_PATH_SEPARATOR + tenantCategory.getId() + TenantCategory.TREE_PATH_SEPARATOR;
				if (categoryPredicate1 == null) {
					categoryPredicate = criteriaBuilder.or(criteriaBuilder.equal(root.get("tenant").get("tenantCategory"), tenantCategory), criteriaBuilder.like(root.get("tenant").get("tenantCategory").<String> get("treePath"), categoryTreePath + "%"));
				} else {
					categoryPredicate = criteriaBuilder.or(categoryPredicate,
							criteriaBuilder.or(criteriaBuilder.equal(root.get("tenant").get("tenantCategory"), tenantCategory), criteriaBuilder.like(root.get("tenant").get("tenantCategory").<String> get("treePath"), categoryTreePath + "%")));
				}
				categoryPredicate1 = criteriaBuilder.or(categoryPredicate1, categoryPredicate);
			}
			restrictions = criteriaBuilder.and(restrictions, categoryPredicate);
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.<Status> get("tenant").get("status"), Status.success));
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, null);
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
	 * @see net.wit.dao.DeliveryCenterDao#findList(net.wit.entity.TenantCategory, java.util.List, net.wit.entity.Area, net.wit.entity.Community,
	 * java.lang.Boolean, java.lang.Integer, java.util.List, java.util.List)
	 */

	@Override
	public List<DeliveryCenter> findList(TenantCategory tenantCategory, List<Tag> tags, Area area, Boolean isDefault, Community community, Boolean periferal, Integer count, List<Filter> filters, List<Order> orders) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<DeliveryCenter> criteriaQuery = criteriaBuilder.createQuery(DeliveryCenter.class);
		Root<DeliveryCenter> root = criteriaQuery.from(DeliveryCenter.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (area != null) {
			restrictions = criteriaBuilder.and(restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("area"), area), criteriaBuilder.like(root.get("area").<String> get("treePath"), "%" + Area.TREE_PATH_SEPARATOR + area.getId() + Area.TREE_PATH_SEPARATOR + "%")));
		}
		if (community != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("community"), community));
		}
		if (tenantCategory != null) {
			restrictions = criteriaBuilder.and(
					restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("tenant").get("tenantCategory"), tenantCategory),
							criteriaBuilder.like(root.get("tenant").get("tenantCategory").<String> get("treePath"), "%" + TenantCategory.TREE_PATH_SEPARATOR + tenantCategory.getId() + TenantCategory.TREE_PATH_SEPARATOR + "%")));
		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.<Status> get("tenant").get("status"), Status.success));
		if (tags != null && !tags.isEmpty()) {
			Subquery<Tenant> subquery = criteriaQuery.subquery(Tenant.class);
			Root<Tenant> subqueryRoot = subquery.from(Tenant.class);
			subquery.select(subqueryRoot);
			subquery.where(criteriaBuilder.equal(subqueryRoot, root.get("tenant")), subqueryRoot.join("tags").in(tags));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
		}
		if (isDefault != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isDefault"), isDefault));
		}
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, count, filters, orders);
	}

	/*
	 * (non-Javadoc) <p>Title: findList</p> <p>Description: </p>
	 * @param tenantCategorys
	 * @param tenantTags
	 * @param area
	 * @param community
	 * @param count
	 * @return
	 * @see net.wit.dao.DeliveryCenterDao#findList(java.util.Set, java.util.List, net.wit.entity.Area, net.wit.entity.Community, java.lang.Integer)
	 */

	@Override
	public List<DeliveryCenter> findList(Set<TenantCategory> tenantCategorys, List<Tag> tenantTags, Area area, Community community, Integer count) {

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<DeliveryCenter> criteriaQuery = criteriaBuilder.createQuery(DeliveryCenter.class);
		Root<DeliveryCenter> root = criteriaQuery.from(DeliveryCenter.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (area != null) {
			restrictions = criteriaBuilder.and(restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("area"), area), criteriaBuilder.like(root.get("area").<String> get("treePath"), "%" + Area.TREE_PATH_SEPARATOR + area.getId() + Area.TREE_PATH_SEPARATOR + "%")));
		}
		if (community != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("community"), community));
		}
		if (tenantCategorys != null && tenantCategorys.size() > 0) {
			Predicate categoryPredicate = criteriaBuilder.conjunction();
			Predicate categoryPredicate1 = null;
			for (TenantCategory tenantCategory : tenantCategorys) {
				String categoryTreePath = TenantCategory.TREE_PATH_SEPARATOR + tenantCategory.getId() + TenantCategory.TREE_PATH_SEPARATOR;
				if (categoryPredicate1 == null) {
					categoryPredicate = criteriaBuilder.or(criteriaBuilder.equal(root.get("tenant").get("tenantCategory"), tenantCategory), criteriaBuilder.like(root.get("tenant").get("tenantCategory").<String> get("treePath"), categoryTreePath + "%"));
				} else {
					categoryPredicate = criteriaBuilder.or(categoryPredicate,
							criteriaBuilder.or(criteriaBuilder.equal(root.get("tenant").get("tenantCategory"), tenantCategory), criteriaBuilder.like(root.get("tenant").get("tenantCategory").<String> get("treePath"), categoryTreePath + "%")));
				}
				categoryPredicate1 = criteriaBuilder.or(categoryPredicate1, categoryPredicate);
			}
			restrictions = criteriaBuilder.and(restrictions, categoryPredicate);
		} else {
			return new ArrayList<DeliveryCenter>();
		}

		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.<Status> get("tenant").get("status"), Status.success));
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isDefault"), Boolean.TRUE));
		if (tenantTags != null && !tenantTags.isEmpty()) {
			Subquery<Tenant> subquery = criteriaQuery.subquery(Tenant.class);
			Root<Tenant> subqueryRoot = subquery.from(Tenant.class);
			subquery.select(subqueryRoot);
			subquery.where(criteriaBuilder.equal(subqueryRoot, root.get("tenant")), subqueryRoot.join("tags").in(tenantTags));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
		}
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, count, null, null);
	}

	@Override
	public List<DeliveryCenter> findList(TenantCategory tenantCategory, Date beginDate, Date endDate, Integer first, Integer count) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<DeliveryCenter> criteriaQuery = criteriaBuilder.createQuery(DeliveryCenter.class);
		Root<DeliveryCenter> root = criteriaQuery.from(DeliveryCenter.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (tenantCategory != null) {
			restrictions = criteriaBuilder.and(
					restrictions,
					criteriaBuilder.or(criteriaBuilder.equal(root.get("tenant").get("tenantCategory"), tenantCategory),
							criteriaBuilder.like(root.get("tenant").get("tenantCategory").<String> get("treePath"), "%" + TenantCategory.TREE_PATH_SEPARATOR + tenantCategory.getId() + TenantCategory.TREE_PATH_SEPARATOR + "%")));
		}
		if (beginDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), beginDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
		}
		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, first, count, null, null);
	}
	public List<DeliveryCenter>findourStoreList(Tenant tenant){
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<DeliveryCenter> criteriaQuery = criteriaBuilder.createQuery(DeliveryCenter.class);
		Root<DeliveryCenter> root = criteriaQuery.from(DeliveryCenter.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();

		if (tenant != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
		}else{
			return null;
		}

		criteriaQuery.where(restrictions);
		return super.findList(criteriaQuery, null, null, null, null);
	}
}