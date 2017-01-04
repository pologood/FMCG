package net.wit.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import net.wit.Filter;
import net.wit.Order;
import net.wit.Order.Direction;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.DeliveryCenterDao;
import net.wit.dao.TenantDao;
import net.wit.entity.*;
import net.wit.entity.Tenant.OrderType;
import net.wit.entity.Tenant.Status;
import net.wit.support.DeliveryComparatorByDistance;
import net.wit.support.TenantComparatorByDistance;
import net.wit.support.TenantDefaultComparatorByDistance;
import net.wit.util.MapUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: www.insuper.com
 * </p>
 * <p>
 * Company: www.insuper.com
 * </p>
 *
 * @author chenqifu
 * @version 1.0 Apr 3, 2013
 */

@Repository("tenantDaoImpl")
public class TenantDaoImpl extends BaseDaoImpl<Tenant, Long> implements TenantDao {
    @Resource(name = "deliveryCenterDaoImpl")
    private DeliveryCenterDao deliveryCenterDao;

    private static final Pattern pattern = Pattern.compile("\\d*");

    public Tenant findByCode(String code) {
        if (code == null) {
            return null;
        }
        String jpql = "select tenant from Tenant tenant where tenant.code = :code";
        try {
            return entityManager.createQuery(jpql, Tenant.class).setFlushMode(FlushModeType.COMMIT).setParameter("code", code).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Tenant findByDomain(String domain) {
        if (domain == null) {
            return null;
        }
        String jpql = "select tenant from Tenant tenant where tenant.domain = :domain";
        try {
            return entityManager.createQuery(jpql, Tenant.class).setFlushMode(FlushModeType.COMMIT).setParameter("domain", domain).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Tenant findByTelephone(String telephone) {
        if (telephone == null) {
            return null;
        }
        String jpql = "select tenant from Tenant tenant where tenant.telephone = :telephone and tenant.status=2";
        try {
            return entityManager.createQuery(jpql, Tenant.class).setFlushMode(FlushModeType.COMMIT).setParameter("telephone", telephone).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Tenant> findList(Area area, String name, Tag tag, Integer count) {
        // String jpql =
        // "select tenant from Tenant tenant,IN(tenant.tags) tags where tags=:tag and tenant.area=:area and tenant.name like :name order by tenant.score desc";
        String sql = "select * from xx_tenant t1 left join xx_tenant_tag t2 on t1.id = t2.tenants where 1=1";
        if (StringUtils.isNotEmpty(name)) {
            sql = sql + " and t1.name like '%" + name + "%'";
        }
        if (area != null) {
            sql = sql + " and t1.area='" + area.getId() + "'";
        }
        if (tag != null) {
            sql = sql + " and t2.tags = " + tag.getId() + " and t1.status = 2";
        }
        try {
            Query query = entityManager.createNativeQuery(sql, Tenant.class);
            if (count != null) {
                query.setMaxResults(count);
            }
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Tenant> findList(TenantCategory tenantCategory, List<Tag> tags, Integer count, List<Filter> filters, List<Order> orders) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tenant> criteriaQuery = criteriaBuilder.createQuery(Tenant.class);
        Root<Tenant> root = criteriaQuery.from(Tenant.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (tenantCategory != null) {
            restrictions = criteriaBuilder.and(
                    restrictions,
                    criteriaBuilder.or(criteriaBuilder.equal(root.get("tenantCategory"), tenantCategory),
                            criteriaBuilder.like(root.get("tenantCategory").<String>get("treePath"), "%" + TenantCategory.TREE_PATH_SEPARATOR + tenantCategory.getId() + TenantCategory.TREE_PATH_SEPARATOR + "%")));
        }
        if (tags != null && !tags.isEmpty()) {
            Subquery<Tenant> subquery = criteriaQuery.subquery(Tenant.class);
            Root<Tenant> subqueryRoot = subquery.from(Tenant.class);
            subquery.select(subqueryRoot);
            subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        }
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery, null, count, filters, orders);
    }

    public List<Tenant> findList(TenantCategory tenantCategory, List<Tag> tags, Area area, Community community, Boolean periferal, Integer count, List<Filter> filters, List<Order> orders) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tenant> criteriaQuery = criteriaBuilder.createQuery(Tenant.class);
        Root<Tenant> root = criteriaQuery.from(Tenant.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (tenantCategory != null) {
            restrictions = criteriaBuilder.and(
                    restrictions,
                    criteriaBuilder.or(criteriaBuilder.equal(root.get("tenantCategory"), tenantCategory),
                            criteriaBuilder.like(root.get("tenantCategory").<String>get("treePath"), "%" + TenantCategory.TREE_PATH_SEPARATOR + tenantCategory.getId() + TenantCategory.TREE_PATH_SEPARATOR + "%")));
        }
        if (tags != null && !tags.isEmpty()) {
            Subquery<Tenant> subquery = criteriaQuery.subquery(Tenant.class);
            Root<Tenant> subqueryRoot = subquery.from(Tenant.class);
            subquery.select(subqueryRoot);
            subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        }
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.<Status>get("status"), Status.success));
        List<Long> tenants = new ArrayList<Long>();
        List<DeliveryCenter> dlvs = new ArrayList<DeliveryCenter>();
        if ((periferal != null) && (periferal)) {
            dlvs = deliveryCenterDao.findList(area, community.getLocation(), new BigDecimal(6));
        } else {
            dlvs = deliveryCenterDao.findList(area, community);
        }
        for (Iterator<DeliveryCenter> it = dlvs.iterator(); it.hasNext(); ) {
            DeliveryCenter dc = it.next();
            tenants.add(dc.getTenant().getId());
        }
        if (tenants.size() > 0) {
            restrictions = criteriaBuilder.and(restrictions, root.get("id").in(tenants));
        } else {
            return new ArrayList<Tenant>();
        }
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery, null, count, filters, orders);
    }

    public List<Tenant> findList(TenantCategory tenantCategory, Date beginDate, Date endDate, Integer first, Integer count) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tenant> criteriaQuery = criteriaBuilder.createQuery(Tenant.class);
        Root<Tenant> root = criteriaQuery.from(Tenant.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (tenantCategory != null) {
            restrictions = criteriaBuilder.and(
                    restrictions,
                    criteriaBuilder.or(criteriaBuilder.equal(root.get("tenantCategory"), tenantCategory),
                            criteriaBuilder.like(root.get("tenantCategory").<String>get("treePath"), "%" + TenantCategory.TREE_PATH_SEPARATOR + tenantCategory.getId() + TenantCategory.TREE_PATH_SEPARATOR + "%")));
        }
        if (beginDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createDate"), beginDate));
        }
        if (endDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createDate"), endDate));
        }
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery, first, count, null, null);
    }

    public Page<Tenant> findPage(TenantCategory tenantCategory, List<Tag> tags, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tenant> criteriaQuery = criteriaBuilder.createQuery(Tenant.class);
        Root<Tenant> root = criteriaQuery.from(Tenant.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (tenantCategory != null) {
            restrictions = criteriaBuilder.and(
                    restrictions,
                    criteriaBuilder.or(criteriaBuilder.equal(root.get("tenantCategory"), tenantCategory),
                            criteriaBuilder.like(root.get("tenantCategory").<String>get("treePath"), "%" + TenantCategory.TREE_PATH_SEPARATOR + tenantCategory.getId() + TenantCategory.TREE_PATH_SEPARATOR + "%")));
        }
        if (tags != null && !tags.isEmpty()) {
            Subquery<Tenant> subquery = criteriaQuery.subquery(Tenant.class);
            Root<Tenant> subqueryRoot = subquery.from(Tenant.class);
            subquery.select(subqueryRoot);
            subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    public Page<Tenant> findPage(TenantCategory tenantCategory, Area area, Boolean isPromotion, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tenant> criteriaQuery = criteriaBuilder.createQuery(Tenant.class);
        Root<Tenant> root = criteriaQuery.from(Tenant.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (tenantCategory != null) {
            restrictions = criteriaBuilder.and(
                    restrictions,
                    criteriaBuilder.or(criteriaBuilder.equal(root.get("tenantCategory"), tenantCategory),
                            criteriaBuilder.like(root.get("tenantCategory").<String>get("treePath"), "%" + TenantCategory.TREE_PATH_SEPARATOR + tenantCategory.getId() + TenantCategory.TREE_PATH_SEPARATOR + "%")));
        }
        if (area != null) {
            Subquery<DeliveryCenter> subquery = criteriaQuery.subquery(DeliveryCenter.class);
            Root<DeliveryCenter> subqueryRoot = subquery.from(DeliveryCenter.class);
            subquery.select(subqueryRoot);
            subquery.where(criteriaBuilder.equal(subqueryRoot.get("tenant"), root), criteriaBuilder.equal(subqueryRoot.get("area"), area));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        }
        if (isPromotion != null) {
            Subquery<Promotion> subquery = criteriaQuery.subquery(Promotion.class);
            Root<Promotion> subqueryRoot = subquery.from(Promotion.class);
            subquery.select(subqueryRoot);
            Promotion.Type[] types = {Promotion.Type.buyfree, Promotion.Type.seckill};
            subquery.where(criteriaBuilder.equal(subqueryRoot.get("tenant"), root), subqueryRoot.get("type").in(types));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    public Page<Tenant> findPage(Set<TenantCategory> tenantCategorys, List<Tag> tags, Area area, Community community, Boolean periferal, Location location, BigDecimal distatce, Pageable pageable) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tenant> criteriaQuery = criteriaBuilder.createQuery(Tenant.class);
            Root<Tenant> root = criteriaQuery.from(Tenant.class);
            criteriaQuery.select(root);
            Predicate restrictions = criteriaBuilder.conjunction();
            if (tenantCategorys != null && tenantCategorys.size() > 0) {
                Predicate categoryPredicate = criteriaBuilder.conjunction();
                Predicate categoryPredicate1 = null;
                for (TenantCategory tenantCategory : tenantCategorys) {
                    String categoryTreePath = TenantCategory.TREE_PATH_SEPARATOR + tenantCategory.getId() + TenantCategory.TREE_PATH_SEPARATOR;
                    if (categoryPredicate1 == null) {
                        categoryPredicate = criteriaBuilder.or(criteriaBuilder.equal(root.get("tenantCategory"), tenantCategory), criteriaBuilder.like(root.get("tenantCategory").<String>get("treePath"), "%" + categoryTreePath + "%"));
                    } else {
                        categoryPredicate = criteriaBuilder.or(categoryPredicate1,
                                criteriaBuilder.or(criteriaBuilder.equal(root.get("tenantCategory"), tenantCategory), criteriaBuilder.like(root.get("tenantCategory").<String>get("treePath"), "%" + categoryTreePath + "%")));
                    }
                    categoryPredicate1 = criteriaBuilder.or(criteriaBuilder.equal(root.get("tenantCategory"), tenantCategory), criteriaBuilder.like(root.get("tenantCategory").<String>get("treePath"), "%" + categoryTreePath + "%"));
                }
                restrictions = criteriaBuilder.and(restrictions, categoryPredicate);
            }
            if (tags != null && !tags.isEmpty()) {
                Subquery<Tenant> subquery = criteriaQuery.subquery(Tenant.class);
                Root<Tenant> subqueryRoot = subquery.from(Tenant.class);
                subquery.select(subqueryRoot);
                subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
            }
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.<Status>get("status"), Status.success));
            List<Long> tenants = new ArrayList<Long>();
            List<DeliveryCenter> dlvs = new ArrayList<DeliveryCenter>();
            if ((periferal != null) && periferal) {
                if (location == null && community != null) {
                    dlvs = deliveryCenterDao.findList(community.getArea(), community.getLocation(), new BigDecimal(6));
                } else if (location.isExists()) {
                    if (distatce == null) {
                        distatce = new BigDecimal(6);
                    } else {

                    }
                    dlvs = deliveryCenterDao.findList(area, location, distatce);
                }
            } else {
                dlvs = deliveryCenterDao.findList(area, community);
            }

            for (Iterator<DeliveryCenter> it = dlvs.iterator(); it.hasNext(); ) {
                DeliveryCenter dc = it.next();
                tenants.add(dc.getTenant().getId());
            }
            if (tenants.size() > 0) {
                restrictions = criteriaBuilder.and(restrictions, root.get("id").in(tenants));
            } else {
                return new Page<Tenant>(Collections.<Tenant>emptyList(), 0, pageable);
            }
            criteriaQuery.where(restrictions);
            return super.findPage(criteriaQuery, pageable);
        } catch (Exception e) {
            e.printStackTrace();
            return new Page<Tenant>();
        }
    }

    public Page<Tenant> findPage(Area area, List<Tag> tags, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tenant> criteriaQuery = criteriaBuilder.createQuery(Tenant.class);
        Root<Tenant> root = criteriaQuery.from(Tenant.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (area != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.<Area>get("area"), area));
        }
        if (tags != null && !tags.isEmpty()) {
            Subquery<Tenant> subquery = criteriaQuery.subquery(Tenant.class);
            Root<Tenant> subqueryRoot = subquery.from(Tenant.class);
            subquery.select(subqueryRoot);
            subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    public Page<Tenant> findAgency(Member member, Status status, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tenant> criteriaQuery = criteriaBuilder.createQuery(Tenant.class);
        Root<Tenant> root = criteriaQuery.from(Tenant.class);
        Predicate restriction = criteriaBuilder.conjunction();
        restriction = criteriaBuilder.and(restriction, criteriaBuilder.equal(root.get("member").get("member"), member));
        if (status != null) {
            restriction = criteriaBuilder.and(restriction, criteriaBuilder.equal(root.get("status"), status));
        }
        criteriaQuery.select(root);
        criteriaQuery.where(restriction);
        return super.findPage(criteriaQuery, pageable);
    }

    public long count(Member member, Date startTime, Date endTime, Status status) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tenant> criteriaQuery = criteriaBuilder.createQuery(Tenant.class);
        Root<Tenant> root = criteriaQuery.from(Tenant.class);
        Predicate restriction = criteriaBuilder.conjunction();
        restriction = criteriaBuilder.and(restriction, criteriaBuilder.equal(root.get("member").get("member"), member));
        if (status != null) {
            restriction = criteriaBuilder.and(restriction, criteriaBuilder.equal(root.get("status"), status));
        }
        if (startTime != null) {
            restriction = criteriaBuilder.and(restriction, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createDate"), startTime));
        }
        if (endTime != null) {
            restriction = criteriaBuilder.and(restriction, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createDate"), endTime));
        }
        criteriaQuery.select(root);
        criteriaQuery.where(restriction);
        try {
            return super.count(criteriaQuery, null);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<ProductCategoryTenant> findRoots(Tenant tenant, Integer count) {
        String jpql = "select productCategoryTenant from ProductCategoryTenant productCategoryTenant where productCategoryTenant.parent is null and productCategoryTenant.tenant= (:tenant) order by productCategoryTenant.order asc";
        TypedQuery<ProductCategoryTenant> query = entityManager.createQuery(jpql, ProductCategoryTenant.class).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant);
        if (count != null) {
            query.setMaxResults(count);
        }
        return query.getResultList();
    }

    public Page<Tenant> mobileFindPage(Set<TenantCategory> tenantCategorys, List<Tag> tags, Area area, Community community, Boolean periferal, Location location, BigDecimal distatce, Pageable pageable) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tenant> criteriaQuery = criteriaBuilder.createQuery(Tenant.class);
            Root<Tenant> root = criteriaQuery.from(Tenant.class);
            criteriaQuery.select(root);
            Predicate restrictions = criteriaBuilder.conjunction();
            if (tenantCategorys != null && tenantCategorys.size() > 0) {
                Predicate categoryPredicate = criteriaBuilder.conjunction();
                Predicate categoryPredicate1 = null;
                for (TenantCategory tenantCategory : tenantCategorys) {
                    String categoryTreePath = TenantCategory.TREE_PATH_SEPARATOR + tenantCategory.getId() + TenantCategory.TREE_PATH_SEPARATOR;
                    if (categoryPredicate1 == null) {
                        categoryPredicate = criteriaBuilder.or(criteriaBuilder.equal(root.get("tenantCategory"), tenantCategory), criteriaBuilder.like(root.get("tenantCategory").<String>get("treePath"), "%" + categoryTreePath + "%"));
                    } else {
                        categoryPredicate = criteriaBuilder.or(categoryPredicate1,
                                criteriaBuilder.or(criteriaBuilder.equal(root.get("tenantCategory"), tenantCategory), criteriaBuilder.like(root.get("tenantCategory").<String>get("treePath"), "%" + categoryTreePath + "%")));
                    }
                    categoryPredicate1 = criteriaBuilder.or(criteriaBuilder.equal(root.get("tenantCategory"), tenantCategory), criteriaBuilder.like(root.get("tenantCategory").<String>get("treePath"), "%" + categoryTreePath + "%"));
                }
                restrictions = criteriaBuilder.and(restrictions, categoryPredicate);
            }
            if (tags != null && !tags.isEmpty()) {
                Subquery<Tenant> subquery = criteriaQuery.subquery(Tenant.class);
                Root<Tenant> subqueryRoot = subquery.from(Tenant.class);
                subquery.select(subqueryRoot);
                subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
            }
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("status"), Status.success),
                    criteriaBuilder.equal(root.get("status"), Status.confirm)));
            List<DeliveryCenter> dlvs = deliveryCenterDao.findListByLocation(location, area, community, distatce);

            List<Long> tenants = new ArrayList<Long>();
            for (DeliveryCenter deliveryCenter : dlvs) {
                tenants.add(deliveryCenter.getTenant().getId());
            }
            if (tenants.size() > 0) {
                restrictions = criteriaBuilder.and(restrictions, root.get("id").in(tenants));
            } else {
                return new Page<Tenant>(Collections.<Tenant>emptyList(), 0, pageable);
            }
            criteriaQuery.where(restrictions);
            return super.findPage(criteriaQuery, pageable);
        } catch (Exception e) {
            e.printStackTrace();
            return new Page<Tenant>();
        }
    }

    public Page<Tenant> findPage(Set<TenantCategory> tenantCategorys, List<Tag> tags, Area area, Community community, Boolean periferal, Pageable pageable) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tenant> criteriaQuery = criteriaBuilder.createQuery(Tenant.class);
            Root<Tenant> root = criteriaQuery.from(Tenant.class);
            criteriaQuery.select(root);
            Predicate restrictions = criteriaBuilder.conjunction();
            if (tenantCategorys != null && tenantCategorys.size() > 0) {
                Predicate categoryPredicate = criteriaBuilder.conjunction();
                Predicate categoryPredicate1 = null;
                for (TenantCategory tenantCategory : tenantCategorys) {
                    String categoryTreePath = TenantCategory.TREE_PATH_SEPARATOR + tenantCategory.getId() + TenantCategory.TREE_PATH_SEPARATOR;
                    if (categoryPredicate1 == null) {
                        categoryPredicate = criteriaBuilder.or(criteriaBuilder.equal(root.get("tenantCategory"), tenantCategory), criteriaBuilder.like(root.get("tenantCategory").<String>get("treePath"), "%" + categoryTreePath + "%"));
                    } else {
                        categoryPredicate = criteriaBuilder.or(categoryPredicate1,
                                criteriaBuilder.or(criteriaBuilder.equal(root.get("tenantCategory"), tenantCategory), criteriaBuilder.like(root.get("tenantCategory").<String>get("treePath"), "%" + categoryTreePath + "%")));
                    }
                    categoryPredicate1 = criteriaBuilder.or(criteriaBuilder.equal(root.get("tenantCategory"), tenantCategory), criteriaBuilder.like(root.get("tenantCategory").<String>get("treePath"), "%" + categoryTreePath + "%"));
                }
                restrictions = criteriaBuilder.and(restrictions, categoryPredicate);
            }
            if (tags != null && !tags.isEmpty()) {
                Subquery<Tenant> subquery = criteriaQuery.subquery(Tenant.class);
                Root<Tenant> subqueryRoot = subquery.from(Tenant.class);
                subquery.select(subqueryRoot);
                subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
            }
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.<Status>get("status"), Status.success));
            List<Long> tenants = new ArrayList<Long>();
            List<DeliveryCenter> dlvs = new ArrayList<DeliveryCenter>();
            if ((periferal != null) && periferal && community != null) {
                dlvs = deliveryCenterDao.findList(community.getArea(), community.getLocation(), new BigDecimal(6));
            } else {
                dlvs = deliveryCenterDao.findList(area, community);
            }

            for (Iterator<DeliveryCenter> it = dlvs.iterator(); it.hasNext(); ) {
                DeliveryCenter dc = it.next();
                tenants.add(dc.getTenant().getId());
            }
            if (tenants.size() > 0) {
                restrictions = criteriaBuilder.and(restrictions, root.get("id").in(tenants));
            } else {
                return new Page<Tenant>(Collections.<Tenant>emptyList(), 0, pageable);
            }
            criteriaQuery.where(restrictions);
            return super.findPage(criteriaQuery, pageable);
        } catch (Exception e) {
            e.printStackTrace();
            return new Page<Tenant>();
        }
    }

    public List<Tenant> findList(Set<TenantCategory> tenantCategorys, List<Tag> tags, Area area, Community community, Integer count) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tenant> criteriaQuery = criteriaBuilder.createQuery(Tenant.class);
            Root<Tenant> root = criteriaQuery.from(Tenant.class);
            criteriaQuery.select(root);
            Predicate restrictions = criteriaBuilder.conjunction();
            if (tenantCategorys != null && tenantCategorys.size() > 0) {
                Predicate categoryPredicate = criteriaBuilder.conjunction();
                Predicate categoryPredicate1 = null;
                for (TenantCategory tenantCategory : tenantCategorys) {
                    String categoryTreePath = TenantCategory.TREE_PATH_SEPARATOR + tenantCategory.getId() + TenantCategory.TREE_PATH_SEPARATOR;
                    if (categoryPredicate1 == null) {
                        categoryPredicate = criteriaBuilder.or(criteriaBuilder.equal(root.get("tenantCategory"), tenantCategory), criteriaBuilder.like(root.get("tenantCategory").<String>get("treePath"), "%" + categoryTreePath + "%"));
                    } else {
                        categoryPredicate = criteriaBuilder.or(categoryPredicate1,
                                criteriaBuilder.or(criteriaBuilder.equal(root.get("tenantCategory"), tenantCategory), criteriaBuilder.like(root.get("tenantCategory").<String>get("treePath"), "%" + categoryTreePath + "%")));
                    }
                    categoryPredicate1 = criteriaBuilder.or(criteriaBuilder.equal(root.get("tenantCategory"), tenantCategory), criteriaBuilder.like(root.get("tenantCategory").<String>get("treePath"), "%" + categoryTreePath + "%"));
                }
                restrictions = criteriaBuilder.and(restrictions, categoryPredicate);
            }
            if (tags != null && !tags.isEmpty()) {
                Subquery<Tenant> subquery = criteriaQuery.subquery(Tenant.class);
                Root<Tenant> subqueryRoot = subquery.from(Tenant.class);
                subquery.select(subqueryRoot);
                subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
            }
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("status"), Status.success),
                    criteriaBuilder.equal(root.get("status"), Status.confirm)));
            List<Long> tenants = new ArrayList<Long>();
            List<DeliveryCenter> dlvs = new ArrayList<DeliveryCenter>();

            dlvs = deliveryCenterDao.findList(area, community);

            for (Iterator<DeliveryCenter> it = dlvs.iterator(); it.hasNext(); ) {
                DeliveryCenter dc = it.next();
                tenants.add(dc.getTenant().getId());
            }
            if (tenants.size() > 0) {
                restrictions = criteriaBuilder.and(restrictions, root.get("id").in(tenants));
            } else {
                return new ArrayList<Tenant>();
            }
            criteriaQuery.where(restrictions);
            return super.findList(criteriaQuery, null, count, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Tenant>();
        }
    }

    public List<Tenant> tenantSelect(String keyword, Boolean isGift, int count) {
        if (StringUtils.isEmpty(keyword)) {
            return Collections.<Tenant>emptyList();
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tenant> criteriaQuery = criteriaBuilder.createQuery(Tenant.class);
        Root<Tenant> root = criteriaQuery.from(Tenant.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (pattern.matcher(keyword).matches()) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.like(root.<String>get("name"), "%" + keyword + "%"), criteriaBuilder.like(root.<String>get("shortName"), "%" + keyword + "%")));
        } else {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.like(root.<String>get("name"), "%" + keyword + "%"), criteriaBuilder.like(root.<String>get("shortName"), "%" + keyword + "%")));
        }
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery, null, count, null, null);
    }

    public List<Tenant> findNewest(List<Tag> tags, Integer count) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tenant> criteriaQuery = criteriaBuilder.createQuery(Tenant.class);
        Root<Tenant> root = criteriaQuery.from(Tenant.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (tags != null) {
            Subquery<Tenant> subquery = criteriaQuery.subquery(Tenant.class);
            Root<Tenant> subqueryRoot = subquery.from(Tenant.class);
            subquery.select(subqueryRoot);
            subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("unionTags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        }
        criteriaQuery.where(restrictions);
        List<Order> orders = new ArrayList<Order>();
        orders.add(new Order("modifyDate", Direction.desc));
        return super.findList(criteriaQuery, null, count, null, orders);
    }

    public Page<Tenant> findPage(Member member, Pageable pageable) {
        if (member == null) {
            return new Page<Tenant>(Collections.<Tenant>emptyList(), 0, pageable);
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tenant> criteriaQuery = criteriaBuilder.createQuery(Tenant.class);
        Root<Tenant> root = criteriaQuery.from(Tenant.class);
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(root.join("favoriteMembers"), member));

        return super.findPage(criteriaQuery, pageable);
    }

    public Page<Tenant> findPage(Status status, List<Tag> tags, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tenant> criteriaQuery = criteriaBuilder.createQuery(Tenant.class);
        Root<Tenant> root = criteriaQuery.from(Tenant.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (status != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
        }
        if (tags != null && tags.size() > 0) {
            Subquery<Tenant> subquery = criteriaQuery.subquery(Tenant.class);
            Root<Tenant> subqueryRoot = subquery.from(Tenant.class);
            subquery.select(subqueryRoot);
            subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        }

        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    /**
     * 支持商品找商家
     */
    public Page<Tenant> findPage(String keyword, TenantCategory tenantCategory, List<Tag> tags, Area area, ProductCategory productCategory, Brand brand, BrandSeries brandSeries, Pageable pageable) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Tenant> criteriaQuery = criteriaBuilder.createQuery(Tenant.class);
            Root<Tenant> root = criteriaQuery.from(Tenant.class);
            criteriaQuery.select(root);
            Predicate restrictions = criteriaBuilder.conjunction();
            if (keyword != null) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(
                        criteriaBuilder.like(root.get("area").<String>get("fullName"), "%" + keyword + "%"),
                        criteriaBuilder.like(root.<String>get("name"), "%" + keyword + "%"),
                        criteriaBuilder.like(root.<String>get("shortName"), "%" + keyword + "%"),
                        criteriaBuilder.like(root.<String>get("scopeOfBusiness"), "%" + keyword + "%")
                        )
                );
            }
            if (tenantCategory != null) {
                restrictions = criteriaBuilder.and(
                        restrictions,
                        criteriaBuilder.or(criteriaBuilder.equal(root.get("tenantCategory"), tenantCategory),
                                criteriaBuilder.like(root.get("tenantCategory").<String>get("treePath"), "%" + TenantCategory.TREE_PATH_SEPARATOR + tenantCategory.getId() + TenantCategory.TREE_PATH_SEPARATOR + "%")));
            }
            if (tags != null && !tags.isEmpty()) {
                Subquery<Tenant> subquery = criteriaQuery.subquery(Tenant.class);
                Root<Tenant> subqueryRoot = subquery.from(Tenant.class);
                subquery.select(subqueryRoot);
                subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
            }
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("status"), Status.success),
                    criteriaBuilder.equal(root.get("status"), Status.confirm)));

            if (area != null) {
                restrictions = criteriaBuilder.and(restrictions,
                        criteriaBuilder.or(criteriaBuilder.equal(root.get("area"), area), criteriaBuilder.like(root.get("area").<String>get("treePath"), "%" + Area.TREE_PATH_SEPARATOR + area.getId() + Area.TREE_PATH_SEPARATOR + "%")));
            }

            if (productCategory != null || brand != null || brandSeries != null) {
                Subquery<Product> productquery = criteriaQuery.subquery(Product.class);
                Root<Product> productqueryRoot = productquery.from(Product.class);
                productquery.select(productqueryRoot);
                Predicate subRestrictions = criteriaBuilder.conjunction();

                //if (keyword != null) {
                //	subRestrictions = criteriaBuilder.and(subRestrictions,
                //			criteriaBuilder.or(criteriaBuilder.like(productqueryRoot.<String> get("fullName"), "%" + keyword + "%"), criteriaBuilder.like(productqueryRoot.get("productCategory").<String> get("fullName"), "%" + keyword + "%")));
                //}

                if (brand != null) {
                    subRestrictions = criteriaBuilder.and(subRestrictions, criteriaBuilder.equal(productqueryRoot.get("brand"), brand));
                }

                if (brandSeries != null) {
                    subRestrictions = criteriaBuilder.and(subRestrictions, criteriaBuilder.like(productqueryRoot.join("brandSeries").<String>get("treePath"), "%" + BrandSeries.TREE_PATH_SEPARATOR + brandSeries.getId() + BrandSeries.TREE_PATH_SEPARATOR + "%"));
                }

                productquery.where(criteriaBuilder.equal(productqueryRoot.get("tenant"), root), subRestrictions);
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(productquery));
            }

            criteriaQuery.where(restrictions);
            return super.findPage(criteriaQuery, pageable);
        } catch (Exception e) {
            e.printStackTrace();
            return new Page<Tenant>();
        }
    }

    public List<Tenant> findMemberFavorite(Member member, String keyword, Integer count, List<Order> orders) {
        if (member == null) {
            return new ArrayList<Tenant>();
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tenant> criteriaQuery = criteriaBuilder.createQuery(Tenant.class);
        Root<Tenant> root = criteriaQuery.from(Tenant.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.join("favoriteMembers"), member));
        if (StringUtils.isNotBlank(keyword)) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.like(root.<String>get("name"), "%" + keyword + "%"), criteriaBuilder.like(root.<String>get("shortName"), "%" + keyword + "%")));
        }
        criteriaQuery.where(restrictions);
        if (orders != null) {
            orders.add(new Order("modifyDate", Direction.desc));
        }
        return super.findList(criteriaQuery, null, count, null, orders);
    }

    /**
     * @Title：统计关注我的会员
     */
    public Long countMyFavorite(Tenant tenant) {
        if (tenant == null) {
            return 0L;
        }
        String jpql = "select count(favorite) from Tenant.favoriteTenants favorite where favorite.tenant= :tenant";
        try {
            Long sumCount = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant).getSingleResult();
            return sumCount;
        } catch (Exception E) {
            E.printStackTrace();
            return 0L;
        }
    }

    @Override
    public List<Tenant> findListByAreas(List<Area> areas) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tenant> criteriaQuery = criteriaBuilder.createQuery(Tenant.class);
        Root<Tenant> root = criteriaQuery.from(Tenant.class);
        criteriaQuery.select(root);
        if (areas != null && areas.size() > 0) {
            Predicate restrictions = criteriaBuilder.conjunction();
            restrictions = criteriaBuilder.and(restrictions, root.get("area").in(areas));
            criteriaQuery.where(restrictions);
        }
        TypedQuery<Tenant> query = entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
        return query.getResultList();
    }

    public boolean domainExists(String domain) {
        if (domain == null) {
            return false;
        }
        String jpql = "select count(tenant) from Tenant tenant where lower(tenant.domain) = lower(:domain)";
        Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("domain", domain).getSingleResult();
        return count > 0;
    }

    public boolean checkShortName(String shortName) {
        if (shortName == null || shortName.trim().length() == 0) {
            return false;
        }
        String jpql = "select count(tenant) from Tenant tenant where tenant.shortName=:shortName";
        Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("shortName", shortName).getSingleResult();
        return count > 0;
    }


    public boolean isOwner(Member member) {
        if (member == null) {
            return false;
        }
        String jpql = "select count(tenant) from Tenant tenant where tenant.member=:member and tenant.status=:status";
        Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("member", member).setParameter("status", Status.success).getSingleResult();
        return count > 0;
    }

    @Override
    public Page<Tenant> findPage(Status status, List<Tag> tags,
                                 List<Area> areas, Date beginDate, Date endDate, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tenant> criteriaQuery = criteriaBuilder.createQuery(Tenant.class);
        Root<Tenant> root = criteriaQuery.from(Tenant.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (status != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
        }
        if (beginDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createDate"), beginDate));
        }
        if (endDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createDate"), endDate));
        }
        if (tags != null && tags.size() > 0) {
            Subquery<Tenant> subquery = criteriaQuery.subquery(Tenant.class);
            Root<Tenant> subqueryRoot = subquery.from(Tenant.class);
            subquery.select(subqueryRoot);
            subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        }
        if (areas != null && areas.size() > 0) {
            restrictions = criteriaBuilder.and(restrictions, root.get("area").in(areas));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    public List<Tenant> findList(Status status, List<Tag> tags, Date beginDate, Date endDate) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tenant> criteriaQuery = criteriaBuilder.createQuery(Tenant.class);
        Root<Tenant> root = criteriaQuery.from(Tenant.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (status != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
        }
        if (beginDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createDate"), beginDate));
        }
        if (endDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createDate"), endDate));
        }
        if (tags != null && tags.size() > 0) {
            Subquery<Tenant> subquery = criteriaQuery.subquery(Tenant.class);
            Root<Tenant> subqueryRoot = subquery.from(Tenant.class);
            subquery.select(subqueryRoot);
            subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        }
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery, null, null, null, null);
    }


    /**
     * 分页查询商家-管理端
     *
     * @param pageable        用于分页、排序、过滤和查询关键字
     * @param area            区域
     * @param beginDate,      endDate,         //时间
     * @param tenantCategorys 商家分类
     * @param tags            标签筛选
     * @param keyword         查询关键字
     * @param status          状态
     * @param orderType       排序
     * @return 商家分页
     */
    public Page<Tenant> openPage(Pageable pageable,                   //用于分页、排序、过滤和查询关键字
                                 Area area,                            //区域
                                 Date beginDate, Date endDate,         //时间
                                 Set<TenantCategory> tenantCategorys,  //商家分类
                                 List<Tag> tags,                       //标签筛选
                                 String keyword,                       //查询关键字
                                 Status status,                        //状态
                                 Tenant.OrderType orderType,            //排序
                                 String qrCodeStatus,                   // 是否有二维码 1：有0：没有
                                 String marketableSize                 //商品上架数量,分隔符',',例子：1,3 代表数量在1到3之间
    ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tenant> criteriaQuery = criteriaBuilder.createQuery(Tenant.class);
        Root<Tenant> root = criteriaQuery.from(Tenant.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (area != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("area"), area), criteriaBuilder.like(root.get("area").<String>get("treePath"), "%" + Area.TREE_PATH_SEPARATOR + area.getId() + Area.TREE_PATH_SEPARATOR + "%")));
        }

        if (tenantCategorys != null && tenantCategorys.size() > 0) {
            Predicate categoryPredicate = criteriaBuilder.conjunction();
            int idx = 0;
            for (TenantCategory tenantCategory : tenantCategorys) {
                String categoryTreePath = TenantCategory.TREE_PATH_SEPARATOR + tenantCategory.getId() + TenantCategory.TREE_PATH_SEPARATOR;
                if (idx == 0) {
                    categoryPredicate = criteriaBuilder.or(criteriaBuilder.equal(root.get("tenantCategory"), tenantCategory), criteriaBuilder.like(root.get("tenantCategory").<String>get("treePath"), "%" + categoryTreePath + "%"));
                } else {
                    categoryPredicate = criteriaBuilder.or(categoryPredicate,
                            criteriaBuilder.or(criteriaBuilder.equal(root.get("tenantCategory"), tenantCategory), criteriaBuilder.like(root.get("tenantCategory").<String>get("treePath"), "%" + categoryTreePath + "%")));
                }
                idx = idx + 1;
            }
            restrictions = criteriaBuilder.and(restrictions, categoryPredicate);
        }

        if (beginDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createDate"), beginDate));
        }
        if (endDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createDate"), endDate));
        }

        if (tags != null && tags.size() > 0) {
            Subquery<Tenant> subquery = criteriaQuery.subquery(Tenant.class);
            Root<Tenant> subqueryRoot = subquery.from(Tenant.class);
            subquery.select(subqueryRoot);
            subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        }

        if (keyword != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(
                    criteriaBuilder.like(root.<String>get("name"), "%" + keyword + "%"),
                    criteriaBuilder.like(root.<String>get("shortName"), "%" + keyword + "%"),
                    criteriaBuilder.like(root.<String>get("telephone"), "%" + keyword + "%"),
                    criteriaBuilder.like(root.get("member").<String>get("mobile"), "%" + keyword + "%"),
                    criteriaBuilder.like(root.get("member").<String>get("username"), "%" + keyword + "%"),
                    criteriaBuilder.like(root.get("member").<String>get("name"), "%" + keyword + "%")
                    )
            );
        }

        if (status != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("status"), status));
        }
        //是否有二维码
        if (qrCodeStatus != null) {
            if (qrCodeStatus.equals("1")) {
                Subquery<Tenant> subquery = criteriaQuery.subquery(Tenant.class);
                Root<Tenant> subqueryRoot = subquery.from(Tenant.class);
                subquery.select(subqueryRoot);
                subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("qrcodes").isNotNull());
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
            } else {
                Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
                Root<Tenant> subqueryRoot = subquery.from(Tenant.class);
                subquery.select(criteriaBuilder.count(subqueryRoot));
                subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("qrcodes").isNotNull());
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(subquery, 0));
            }
        }
        if (marketableSize != null) {
            String[] marketableSizes = marketableSize.split(",");
            if (marketableSizes.length >= 1) {
                Long _min = Long.valueOf(marketableSizes[0]);
                Subquery<Long> subquery = criteriaQuery.subquery(Long.class);
                Root<Tenant> subqueryRoot = subquery.from(Tenant.class);
                subquery.select(criteriaBuilder.count(subqueryRoot));
                subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("products").isNotNull());
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
                if (marketableSizes.length >= 2) {
                    Long _max = Long.valueOf(marketableSizes[1]);
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.between(subquery, _min, _max));
                } else if (marketableSizes.length == 1) {
                    //>=
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.gt(subquery, _min));
                }
            }
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    /**
     * 分页查询商家
     *
     * @param pageable        用于分页、排序、过滤和查询关键字
     * @param area            区域
     * @param tenantCategorys 商家分类
     * @param tags            标签筛选
     * @param keyword         查询关键字
     * @param location        定位坐标
     * @param distatce        定位后根据距离搜索商家
     * @param orderType       排序
     * @return 商家分页
     */
    public Page<Tenant> openPage(Pageable pageable,                    //用于分页、排序、过滤和查询关键字
                                 Area area,                            //区域
                                 Set<TenantCategory> tenantCategorys,  //商家分类
                                 List<Tag> tags,                       //标签筛选
                                 String keyword,                       //查询关键字
                                 Location location,                    //定位坐标
                                 BigDecimal distatce,                  //定位后根据距离搜索商家
                                 Community community,                  //商圈
                                 Tenant.OrderType orderType,            //排序
                                 Boolean isPromotion,
                                 Boolean isUnion,
                                 Union union
    ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tenant> criteriaQuery = criteriaBuilder.createQuery(Tenant.class);
        Root<Tenant> root = criteriaQuery.from(Tenant.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        //if (area != null) {
        //    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("area"), area), criteriaBuilder.like(root.get("area").<String>get("treePath"), "%" + Area.TREE_PATH_SEPARATOR + area.getId() + Area.TREE_PATH_SEPARATOR + "%")));
        //}

        if (tenantCategorys != null && tenantCategorys.size() > 0) {
            Predicate categoryPredicate = criteriaBuilder.conjunction();
            int idx = 0;
            for (TenantCategory tenantCategory : tenantCategorys) {
                String categoryTreePath = TenantCategory.TREE_PATH_SEPARATOR + tenantCategory.getId() + TenantCategory.TREE_PATH_SEPARATOR;
                if (idx == 0) {
                    categoryPredicate = criteriaBuilder.or(criteriaBuilder.equal(root.get("tenantCategory"), tenantCategory), criteriaBuilder.like(root.get("tenantCategory").<String>get("treePath"), "%" + categoryTreePath + "%"));
                } else {
                    categoryPredicate = criteriaBuilder.or(categoryPredicate,
                            criteriaBuilder.or(criteriaBuilder.equal(root.get("tenantCategory"), tenantCategory), criteriaBuilder.like(root.get("tenantCategory").<String>get("treePath"), "%" + categoryTreePath + "%")));
                }
                idx = idx + 1;
            }
            restrictions = criteriaBuilder.and(restrictions, categoryPredicate);
        }

        if (tags != null && tags.size() > 0) {
            Subquery<Tenant> subquery = criteriaQuery.subquery(Tenant.class);
            Root<Tenant> subqueryRoot = subquery.from(Tenant.class);
            subquery.select(subqueryRoot);
            subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        }

        if (keyword != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(
                    criteriaBuilder.like(root.<String>get("name"), "%" + keyword + "%"),
                    criteriaBuilder.like(root.<String>get("shortName"), "%" + keyword + "%"),
                    criteriaBuilder.like(root.<String>get("scopeOfBusiness"), "%" + keyword + "%"),
                    criteriaBuilder.like(root.get("member").<String>get("mobile"), "%" + keyword + "%"),
                    criteriaBuilder.like(root.get("member").<String>get("username"), "%" + keyword + "%"),
                    criteriaBuilder.like(root.get("member").<String>get("name"), "%" + keyword + "%")
                    )
            );
        }

            Subquery<DeliveryCenter> subquery = criteriaQuery.subquery(DeliveryCenter.class);
            Root<DeliveryCenter> subqueryRoot = subquery.from(DeliveryCenter.class);
            subquery.select(subqueryRoot);
            Predicate subRestrictions = criteriaBuilder.conjunction();
            subRestrictions = criteriaBuilder.and(criteriaBuilder.equal(subqueryRoot.get("tenant"), root));
            if (area != null) {
                subRestrictions = criteriaBuilder.and(subRestrictions,
                        criteriaBuilder.or(criteriaBuilder.equal(subqueryRoot.get("area"), area), criteriaBuilder.like(subqueryRoot.get("area").<String>get("treePath"), "%" + Area.TREE_PATH_SEPARATOR + area.getId() + Area.TREE_PATH_SEPARATOR + "%")));
            }
            if (community != null) {
                subRestrictions = criteriaBuilder.and(subRestrictions,
                        criteriaBuilder.equal(subqueryRoot.get("community"), community));
            }
            if (location!=null && location.isExists()  && distatce != null) {
                MapUtils mapUtils = new MapUtils(distatce.doubleValue());
                mapUtils.rectangle4Point(location.getLat().doubleValue(), location.getLng().doubleValue());
                subRestrictions = criteriaBuilder.and(subRestrictions, criteriaBuilder.ge(subqueryRoot.get("location").<BigDecimal>get("lat"), mapUtils.getLeft_bottom().getLat()));
                subRestrictions = criteriaBuilder.and(subRestrictions, criteriaBuilder.le(subqueryRoot.get("location").<BigDecimal>get("lat"), mapUtils.getLeft_top().getLat()));
                subRestrictions = criteriaBuilder.and(subRestrictions, criteriaBuilder.ge(subqueryRoot.get("location").<BigDecimal>get("lng"), mapUtils.getLeft_top().getLng()));
                subRestrictions = criteriaBuilder.and(subRestrictions, criteriaBuilder.le(subqueryRoot.get("location").<BigDecimal>get("lng"), mapUtils.getRight_bottom().getLng()));
            }
            subquery.where(subRestrictions);
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));

        if (isPromotion == null) {
            restrictions = criteriaBuilder.and(restrictions,
                    criteriaBuilder.equal(root.get("status"), Status.success));
        } else if (isPromotion) {
            restrictions = criteriaBuilder.and(restrictions,
                    criteriaBuilder.equal(root.get("status"), Status.success));
        } else {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(
                    criteriaBuilder.equal(root.get("status"), Status.success),
                    criteriaBuilder.equal(root.get("status"), Status.confirm)));
        }

        if(isUnion!=null){
            restrictions = criteriaBuilder.and(restrictions,
                    criteriaBuilder.equal(root.get("isUnion"), isUnion));
        } if(union!=null){
            restrictions = criteriaBuilder.and(restrictions,
                    criteriaBuilder.equal(root.get("unions"), union));
        }


        criteriaQuery.where(restrictions);
        if (orderType != null) {
            if (orderType.equals(OrderType.weight)) {
                List<Tenant> tenants = super.findList(criteriaQuery, 0, null, null, null);
                TenantDefaultComparatorByDistance comparatorByDistance = new TenantDefaultComparatorByDistance();
                comparatorByDistance.setLocation(location);
                Collections.sort(tenants, comparatorByDistance);
                int fromindex = (pageable.getPageNumber() - 1) * pageable.getPageSize();
                int endindex = fromindex + pageable.getPageSize();
                if (endindex > tenants.size()) {
                    endindex = tenants.size();
                }
                if (endindex <= fromindex) {
                    return new Page<Tenant>(new ArrayList<Tenant>(), 0, pageable);
                }
                return new Page<Tenant>(new ArrayList<Tenant>(tenants.subList(fromindex, endindex)), tenants.size(), pageable);
            } else if (orderType.equals(OrderType.distance)) {
                List<Tenant> tenants = super.findList(criteriaQuery, 0, null, null, null);
                TenantComparatorByDistance comparatorByDistance = new TenantComparatorByDistance();
                comparatorByDistance.setLocation(location);
                Collections.sort(tenants, comparatorByDistance);
                int fromindex = (pageable.getPageNumber() - 1) * pageable.getPageSize();
                int endindex = fromindex + pageable.getPageSize();
                if (endindex > tenants.size()) {
                    endindex = tenants.size();
                }
                if (endindex <= fromindex) {
                    return new Page<Tenant>(new ArrayList<Tenant>(), 0, pageable);
                }
                return new Page<Tenant>(new ArrayList<Tenant>(tenants.subList(fromindex, endindex)), tenants.size(), pageable);
            } else {
                List<Order> orders = pageable.getOrders();
                orders.clear();
                if (orderType == OrderType.dateDesc) {
                    orders.add(Order.desc("createDate"));
                } else if (orderType == OrderType.hitsDesc) {
                    orders.add(Order.asc("hits"));
                    orders.add(Order.desc("createDate"));
                } else if (orderType == OrderType.scoreDesc) {
                    orders.add(Order.desc("score"));
                } else {
                    orders.add(Order.desc("priority"));
                }
                return super.findPage(criteriaQuery, pageable);
            }
        } else {
            return super.findPage(criteriaQuery, pageable);
        }
    }

    /**
     * 返回固定条数的商家
     *
     * @param count           查询的条数
     * @param area            区域
     * @param tenantCategorys 商家分类
     * @param tags            标签筛选
     * @param keyword         查询关键字
     * @param location        定位坐标
     * @param distatce        定位后根据距离搜索商家
     * @param filters         过滤
     * @param orderType       排序
     * @return 商家集合
     */
    public List<Tenant> openList(Integer count,                        //查询的条数
                                 Area area,                            //区域
                                 Set<TenantCategory> tenantCategorys,  //商家分类
                                 List<Tag> tags,                       //标签筛选
                                 String keyword,                       //查询关键字
                                 Location location,                    //定位坐标
                                 BigDecimal distatce,                  //定位后根据距离搜索商家
                                 List<Filter> filters,                 //过滤
                                 Tenant.OrderType orderType            //排序
    ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tenant> criteriaQuery = criteriaBuilder.createQuery(Tenant.class);
        Root<Tenant> root = criteriaQuery.from(Tenant.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        //if (area != null) {
       //     restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("area"), area), criteriaBuilder.like(root.get("area").<String>get("treePath"), "%" + Area.TREE_PATH_SEPARATOR + area.getId() + Area.TREE_PATH_SEPARATOR + "%")));
        //}

        if (tenantCategorys != null && tenantCategorys.size() > 0) {
            Predicate categoryPredicate = criteriaBuilder.conjunction();
            int idx = 0;
            for (TenantCategory tenantCategory : tenantCategorys) {
                String categoryTreePath = TenantCategory.TREE_PATH_SEPARATOR + tenantCategory.getId() + TenantCategory.TREE_PATH_SEPARATOR;
                if (idx == 0) {
                    categoryPredicate = criteriaBuilder.or(criteriaBuilder.equal(root.get("tenantCategory"), tenantCategory), criteriaBuilder.like(root.get("tenantCategory").<String>get("treePath"), "%" + categoryTreePath + "%"));
                } else {
                    categoryPredicate = criteriaBuilder.or(categoryPredicate,
                            criteriaBuilder.or(criteriaBuilder.equal(root.get("tenantCategory"), tenantCategory), criteriaBuilder.like(root.get("tenantCategory").<String>get("treePath"), "%" + categoryTreePath + "%")));
                }
                idx = idx + 1;
            }
            restrictions = criteriaBuilder.and(restrictions, categoryPredicate);
        }

        if (tags != null && tags.size() > 0) {
            Subquery<Tenant> subquery = criteriaQuery.subquery(Tenant.class);
            Root<Tenant> subqueryRoot = subquery.from(Tenant.class);
            subquery.select(subqueryRoot);
            subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        }

        if (keyword != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(
                    criteriaBuilder.like(root.<String>get("name"), "%" + keyword + "%"),
                    criteriaBuilder.like(root.<String>get("shortName"), "%" + keyword + "%"),
                    criteriaBuilder.like(root.<String>get("scopeOfBusiness"), "%" + keyword + "%"),
                    criteriaBuilder.like(root.get("member").<String>get("mobile"), "%" + keyword + "%"),
                    criteriaBuilder.like(root.get("member").<String>get("username"), "%" + keyword + "%"),
                    criteriaBuilder.like(root.get("member").<String>get("name"), "%" + keyword + "%")
                    )
            );
        }

        Subquery<DeliveryCenter> subquery = criteriaQuery.subquery(DeliveryCenter.class);
        Root<DeliveryCenter> subqueryRoot = subquery.from(DeliveryCenter.class);
        subquery.select(subqueryRoot);
        Predicate subRestrictions = criteriaBuilder.conjunction();
        subRestrictions = criteriaBuilder.and(criteriaBuilder.equal(subqueryRoot.get("tenant"), root));
        if (area != null) {
            subRestrictions = criteriaBuilder.and(subRestrictions,
                    criteriaBuilder.or(criteriaBuilder.equal(subqueryRoot.get("area"), area), criteriaBuilder.like(subqueryRoot.get("area").<String>get("treePath"), "%" + Area.TREE_PATH_SEPARATOR + area.getId() + Area.TREE_PATH_SEPARATOR + "%")));
        }
         if (location!=null && location.isExists()  && distatce != null) {
            MapUtils mapUtils = new MapUtils(distatce.doubleValue());
            mapUtils.rectangle4Point(location.getLat().doubleValue(), location.getLng().doubleValue());
            subRestrictions = criteriaBuilder.and(subRestrictions, criteriaBuilder.ge(subqueryRoot.get("location").<BigDecimal>get("lat"), mapUtils.getLeft_bottom().getLat()));
            subRestrictions = criteriaBuilder.and(subRestrictions, criteriaBuilder.le(subqueryRoot.get("location").<BigDecimal>get("lat"), mapUtils.getLeft_top().getLat()));
            subRestrictions = criteriaBuilder.and(subRestrictions, criteriaBuilder.ge(subqueryRoot.get("location").<BigDecimal>get("lng"), mapUtils.getLeft_top().getLng()));
            subRestrictions = criteriaBuilder.and(subRestrictions, criteriaBuilder.le(subqueryRoot.get("location").<BigDecimal>get("lng"), mapUtils.getRight_bottom().getLng()));
        }
        subquery.where(subRestrictions);
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));

        restrictions = criteriaBuilder.and(restrictions,
                criteriaBuilder.equal(root.get("status"), Status.success));
        //restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(
        //        criteriaBuilder.equal(root.get("status"), Status.success),
        //        criteriaBuilder.equal(root.get("status"), Status.confirm)));

        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery, null, count, filters, getOrder(orderType));
    }


    private List<Order> getOrder(Tenant.OrderType orderType) {
        List<Order> orders = new ArrayList<>();
        if (orderType == OrderType.dateDesc) {
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.hitsDesc) {
            orders.add(Order.desc("hits"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.scoreDesc) {
            orders.add(Order.desc("score"));
        } else {
            orders.add(Order.desc("priority"));
        }
        return orders;
    }

}
