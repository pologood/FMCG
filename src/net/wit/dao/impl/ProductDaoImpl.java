/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import net.wit.Filter;
import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.dao.DeliveryCenterDao;
import net.wit.dao.GoodsDao;
import net.wit.dao.ProductDao;
import net.wit.dao.SnDao;
import net.wit.entity.*;
import net.wit.entity.Order.OrderStatus;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.Product.OrderType;
import net.wit.entity.Sn.Type;
import net.wit.entity.Tenant.Status;
import net.wit.util.DateUtil;
import net.wit.util.MapUtils;
import net.wit.util.SettingUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * Dao - 商品
 *
 * @author rsico Team
 * @version 3.0
 */
@Repository("productDaoImpl")
public class ProductDaoImpl extends BaseDaoImpl<Product, Long> implements ProductDao {

    private static final Pattern pattern = Pattern.compile("\\d*");

    @Resource(name = "goodsDaoImpl")
    private GoodsDao goodsDao;

    @Resource(name = "snDaoImpl")
    private SnDao snDao;

    @Resource(name = "deliveryCenterDaoImpl")
    private DeliveryCenterDao deliveryCenterDao;

    private CriteriaQuery<Product> criteriaQuery(Set<ProductCategory> productCategorys, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable,
                                                 Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, String phonetic, String keyword) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (productCategorys != null && productCategorys.size() > 0) { // 商品分类
            Predicate categoryPredicate = criteriaBuilder.conjunction();
            Predicate categoryPredicate1 = null;
            for (ProductCategory productCategory : productCategorys) {
                String categoryTreePath = ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR;
                if (categoryPredicate1 == null) {
                    categoryPredicate = criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory), criteriaBuilder.like(root.get("productCategory").<String>get("treePath"), "%" + categoryTreePath + "%"));
                    categoryPredicate1 = criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory), criteriaBuilder.like(root.get("productCategory").<String>get("treePath"), "%" + categoryTreePath + "%"));
                } else {
                    categoryPredicate = criteriaBuilder.or(categoryPredicate,
                            criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory), criteriaBuilder.like(root.get("productCategory").<String>get("treePath"), "%" + categoryTreePath + "%")));
                }
            }
            restrictions = criteriaBuilder.and(restrictions, categoryPredicate);
        }
        if (brand != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("brand"), brand));
        }
        if (promotion != null) { // 促销信息
            Subquery<Product> promotionProductSubquery = criteriaQuery.subquery(Product.class);
            Root<Product> promotionProductSubqueryRoot = promotionProductSubquery.from(Product.class);
            promotionProductSubquery.select(promotionProductSubqueryRoot);
            promotionProductSubquery.where(criteriaBuilder.equal(promotionProductSubqueryRoot, root), criteriaBuilder.equal(promotionProductSubqueryRoot.join("promotionProducts").get("promotion"), promotion));

            Subquery<Product> productCategorySubquery = criteriaQuery.subquery(Product.class);
            Root<Product> productCategorySubqueryRoot = productCategorySubquery.from(Product.class);
            productCategorySubquery.select(productCategorySubqueryRoot);
            productCategorySubquery.where(criteriaBuilder.equal(productCategorySubqueryRoot, root), criteriaBuilder.equal(productCategorySubqueryRoot.join("productCategory").join("promotions"), promotion));

            Subquery<Product> brandSubQuery = criteriaQuery.subquery(Product.class);
            Root<Product> brandSubQueryRoot = brandSubQuery.from(Product.class);
            brandSubQuery.select(brandSubQueryRoot);
            brandSubQuery.where(criteriaBuilder.equal(brandSubQueryRoot, root), criteriaBuilder.equal(brandSubQueryRoot.join("brand").join("promotions"), promotion));

            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.exists(promotionProductSubquery), criteriaBuilder.exists(productCategorySubquery), criteriaBuilder.exists(brandSubQuery)));
        }
        if (tags != null && !tags.isEmpty()) { // 标签
            Subquery<Product> tagsSubquery = criteriaQuery.subquery(Product.class);
            Root<Product> tagsSubqueryRoot = tagsSubquery.from(Product.class);
            tagsSubquery.select(tagsSubqueryRoot);
            tagsSubquery.where(criteriaBuilder.equal(tagsSubqueryRoot, root), tagsSubqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(tagsSubquery));
        }
        if (attributeValue != null) { // 属性
            for (Entry<Attribute, String> entry : attributeValue.entrySet()) {
                String propertyName = Product.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + entry.getKey().getPropertyIndex();
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get(propertyName), entry.getValue()));
            }
        }
        if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
            BigDecimal temp = startPrice;
            startPrice = endPrice;
            endPrice = temp;
        }
        if (startPrice != null && startPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.<Number>get("price"), startPrice));
        }
        if (endPrice != null && endPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Number>get("price"), endPrice));
        }
        if (isMarketable != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
        }
        if (isList != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), isList));
        }
        if (isTop != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isTop"), isTop));
        }
        if (isGift != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isGift"), isGift));
        }
        Path<Integer> stock = root.get("stock");
        Path<Integer> allocatedStock = root.get("allocatedStock");
        if (isOutOfStock != null) {
            if (isOutOfStock) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, allocatedStock));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, allocatedStock)));
            }
        }
        if (isStockAlert != null) {
            Setting setting = SettingUtils.get();
            if (isStockAlert) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount()))));
            }
        }

        List<Tenant> tenants = new ArrayList<Tenant>();
        List<DeliveryCenter> dlvs = new ArrayList<DeliveryCenter>();
        if ((periferal != null) && (periferal)) {
            dlvs = deliveryCenterDao.findList(area, community.getLocation(), new BigDecimal(6));
        } else {
            dlvs = deliveryCenterDao.findList(area, community);
        }
        for (Iterator<DeliveryCenter> it = dlvs.iterator(); it.hasNext(); ) {
            DeliveryCenter dc = it.next();
            tenants.add(dc.getTenant());
        }
        if (tenants.size() == 0) {
            return null;
        }
        restrictions = criteriaBuilder.and(restrictions, root.<Tenant>get("tenant").in(tenants));

        if (StringUtils.isNotBlank(phonetic)) { // 拼音条件
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.<String>get("phonetic"), "%" + phonetic + "%"));
        }
        if (StringUtils.isNotBlank(keyword)) { // 关键字
            Predicate snPredicate = criteriaBuilder.like(root.<String>get("sn"), "%" + keyword + "%");
            Predicate fullNamePredicate = criteriaBuilder.like(root.<String>get("fullName"), "%" + keyword + "%");
            if (pattern.matcher(keyword).matches()) {
                Predicate idPredicate = criteriaBuilder.equal(root.get("id"), Long.valueOf(keyword));
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(idPredicate, snPredicate, fullNamePredicate));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(snPredicate, fullNamePredicate));
            }
        }
        return criteriaQuery.where(restrictions);
    }

    private CriteriaQuery<Product> criteriaQuery(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList,
                                                 Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, String phonetic, String keyword) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (productCategory != null) { // 商品分类
            String categoryTreePath = ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR;
            Predicate categoryPredicate = criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory), criteriaBuilder.like(root.get("productCategory").<String>get("treePath"), "%" + categoryTreePath + "%"));
            restrictions = criteriaBuilder.and(restrictions, categoryPredicate);
        }
        if (brand != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("brand"), brand));
        }
        if (promotion != null) { // 促销信息
            Subquery<Product> promotionProductSubquery = criteriaQuery.subquery(Product.class);
            Root<Product> promotionProductSubqueryRoot = promotionProductSubquery.from(Product.class);
            promotionProductSubquery.select(promotionProductSubqueryRoot);
            promotionProductSubquery.where(criteriaBuilder.equal(promotionProductSubqueryRoot, root), criteriaBuilder.equal(promotionProductSubqueryRoot.join("promotionProducts").get("promotion"), promotion));

            Subquery<Product> productCategorySubquery = criteriaQuery.subquery(Product.class);
            Root<Product> productCategorySubqueryRoot = productCategorySubquery.from(Product.class);
            productCategorySubquery.select(productCategorySubqueryRoot);
            productCategorySubquery.where(criteriaBuilder.equal(productCategorySubqueryRoot, root), criteriaBuilder.equal(productCategorySubqueryRoot.join("productCategory").join("promotions"), promotion));

            Subquery<Product> brandSubQuery = criteriaQuery.subquery(Product.class);
            Root<Product> brandSubQueryRoot = brandSubQuery.from(Product.class);
            brandSubQuery.select(brandSubQueryRoot);
            brandSubQuery.where(criteriaBuilder.equal(brandSubQueryRoot, root), criteriaBuilder.equal(brandSubQueryRoot.join("brand").join("promotions"), promotion));

            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.exists(promotionProductSubquery), criteriaBuilder.exists(productCategorySubquery), criteriaBuilder.exists(brandSubQuery)));
        }
        if (tags != null && !tags.isEmpty()) { // 标签
            Subquery<Product> tagsSubquery = criteriaQuery.subquery(Product.class);
            Root<Product> tagsSubqueryRoot = tagsSubquery.from(Product.class);
            tagsSubquery.select(tagsSubqueryRoot);
            tagsSubquery.where(criteriaBuilder.equal(tagsSubqueryRoot, root), tagsSubqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(tagsSubquery));
        }
        if (attributeValue != null) { // 属性
            for (Entry<Attribute, String> entry : attributeValue.entrySet()) {
                String propertyName = Product.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + entry.getKey().getPropertyIndex();
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get(propertyName), entry.getValue()));
            }
        }
        if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
            BigDecimal temp = startPrice;
            startPrice = endPrice;
            endPrice = temp;
        }
        if (startPrice != null && startPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.<Number>get("price"), startPrice));
        }
        if (endPrice != null && endPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Number>get("price"), endPrice));
        }
        if (isMarketable != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
        }
        if (isList != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), isList));
        }
        if (isTop != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isTop"), isTop));
        }
        if (isGift != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isGift"), isGift));
        }
        Path<Integer> stock = root.get("stock");
        Path<Integer> allocatedStock = root.get("allocatedStock");
        if (isOutOfStock != null) {
            if (isOutOfStock) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, allocatedStock));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, allocatedStock)));
            }
        }
        if (isStockAlert != null) {
            Setting setting = SettingUtils.get();
            if (isStockAlert) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount()))));
            }
        }

        if (community != null) { // 寻找周边商家
            List<Tenant> tenants = new ArrayList<Tenant>();
            List<DeliveryCenter> dlvs = new ArrayList<DeliveryCenter>();
            if ((periferal != null) && (periferal)) {
                dlvs = deliveryCenterDao.findList(area, community.getLocation(), new BigDecimal(6));
            } else {
                dlvs = deliveryCenterDao.findList(area, community);
            }
            for (Iterator<DeliveryCenter> it = dlvs.iterator(); it.hasNext(); ) {
                DeliveryCenter dc = it.next();
                tenants.add(dc.getTenant());
            }
            if (tenants.size() == 0) {
                return null;
            }
            restrictions = criteriaBuilder.and(restrictions, root.<Tenant>get("tenant").in(tenants));
        } else if (area != null) {
            Subquery<Product> areaSubquery = criteriaQuery.subquery(Product.class);
            Root<Product> tagsSubqueryRoot = areaSubquery.from(Product.class);
            areaSubquery.select(tagsSubqueryRoot);
            areaSubquery.where(criteriaBuilder.equal(tagsSubqueryRoot, root), criteriaBuilder.equal(tagsSubqueryRoot.join("tenant").join("deliveryCenters").get("area"), area));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(areaSubquery));
        }

        if (StringUtils.isNotBlank(phonetic)) { // 拼音条件
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.<String>get("phonetic"), "%" + phonetic + "%"));
        }
        if (StringUtils.isNotBlank(keyword)) { // 关键字
            Predicate snPredicate = criteriaBuilder.like(root.<String>get("sn"), "%" + keyword + "%");
            Predicate fullNamePredicate = criteriaBuilder.like(root.<String>get("name"), "%" + keyword + "%");
            if (pattern.matcher(keyword).matches()) {
                Predicate idPredicate = criteriaBuilder.equal(root.get("id"), Long.valueOf(keyword));
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(idPredicate, snPredicate, fullNamePredicate));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(snPredicate, fullNamePredicate));
            }
        }
        return criteriaQuery.where(restrictions);
    }

    public boolean snExists(String sn) {
        if (sn == null) {
            return false;
        }
        String jpql = "select count(product) from Product product where lower(product.sn) = lower(:sn)";
        Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).getSingleResult();
        return count > 0;
    }

    public boolean snExists(String sn,Tenant tenant) {
        if (sn == null) {
            return false;
        }
        String jpql = "select count(product) from Product product where lower(product.sn) = lower(:sn) and product.tenant=:tenant";
        Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).setParameter("tenant", tenant).getSingleResult();
        return count > 0;
    }

    public Product findBySn(String sn) {
        if (sn == null) {
            return null;
        }
        String jpql = "select product from Product product where lower(product.sn) = lower(:sn)";
        try {
            return entityManager.createQuery(jpql, Product.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Product> findByBarcode(Tenant tenant, String barcode) {
        if (barcode == null) {
            return new ArrayList<Product>();
        }
        try {
            if (tenant != null) {
                String jpql = "select product from Product product where product.tenant=:tenant and product.barcode = :barcode";
                return entityManager.createQuery(jpql, Product.class).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant).setParameter("barcode", barcode).getResultList();
            } else {
                String jpql = "select product from Product product where product.barcode = :barcode";
                return entityManager.createQuery(jpql, Product.class).setFlushMode(FlushModeType.COMMIT).setParameter("barcode", barcode).getResultList();
            }
        } catch (NoResultException e) {
            return null;
        }
    }

    public Page<Product> findPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop,
                                  Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, OrderType orderType, String phonetic, String keyword, Pageable pageable) {
        CriteriaQuery<Product> criteriaQuery = criteriaQuery(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, community, area, periferal, phonetic, keyword);
        if (criteriaQuery == null) {
            return new Page<Product>(new ArrayList<Product>(), 0, pageable);
        }
        List<Order> orders = pageable.getOrders();
        if (orderType == OrderType.priceAsc) {
            orders.add(Order.asc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.weight) {
            orders.add(Order.desc("priority"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.priceDesc) {
            orders.add(Order.desc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.salesDesc) {
            orders.add(Order.desc("sales"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.scoreDesc) {
            orders.add(Order.desc("score"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.dateDesc) {
            orders.add(Order.desc("createDate"));
        } else {
            orders.add(Order.desc("isTop"));
            orders.add(Order.desc("modifyDate"));
        }
        return super.findPage(criteriaQuery, pageable);
    }

    public Page<Product> findPageByChannel(Set<ProductCategory> productCategorys, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList,
                                           Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, OrderType orderType, String phonetic, String keyword, Pageable pageable) {
        if (productCategorys == null || productCategorys.size() <= 0) {
            return new Page<Product>();
        }
        CriteriaQuery<Product> criteriaQuery = criteriaQuery(productCategorys, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, community, area, periferal, phonetic, keyword);
        if (criteriaQuery == null) {
            return new Page<Product>(new ArrayList<Product>(), 0, pageable);
        }

        List<Order> orders = pageable.getOrders();
        if (orderType == OrderType.priceAsc) {
            orders.add(Order.asc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.weight) {
            orders.add(Order.desc("priority"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.priceDesc) {
            orders.add(Order.desc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.salesDesc) {
            orders.add(Order.desc("sales"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.scoreDesc) {
            orders.add(Order.desc("score"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.dateDesc) {
            orders.add(Order.desc("createDate"));
        } else {
            orders.add(Order.desc("isTop"));
            orders.add(Order.desc("modifyDate"));
        }
        try {
            return super.findPage(criteriaQuery, pageable);
        } catch (Exception e) {
            e.printStackTrace();
            return new Page<Product>();
        }
    }

    public List<Product> findList(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop,
                                  Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, String phonetic, String keyword, OrderType orderType, Integer first, Integer count, List<Filter> filters, List<Order> orders) {
        CriteriaQuery<Product> criteriaQuery = criteriaQuery(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, community, area, periferal, phonetic, keyword);
        if (criteriaQuery == null) {
            return new ArrayList<Product>();
        }
        if (orderType == OrderType.priceAsc) {
            orders.add(Order.asc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.weight) {
            orders.add(Order.desc("priority"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.priceDesc) {
            orders.add(Order.desc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.salesDesc) {
            orders.add(Order.desc("sales"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.scoreDesc) {
            orders.add(Order.desc("score"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.dateDesc) {
            orders.add(Order.desc("createDate"));
        } else {
            orders.add(Order.desc("isTop"));
            orders.add(Order.desc("modifyDate"));
        }
        return super.findList(criteriaQuery, first, count, filters, orders);
    }

    public List<Product> search(String keyword, Boolean isGift, Integer count) {
        if (StringUtils.isEmpty(keyword)) {
            return Collections.<Product>emptyList();
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (pattern.matcher(keyword).matches()) {
            restrictions = criteriaBuilder.and(restrictions,
                    criteriaBuilder.or(criteriaBuilder.equal(root.get("id"), Long.valueOf(keyword)), criteriaBuilder.like(root.<String>get("sn"), "%" + keyword + "%"), criteriaBuilder.like(root.<String>get("fullName"), "%" + keyword + "%")));
        } else {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.like(root.<String>get("sn"), "%" + keyword + "%"), criteriaBuilder.like(root.<String>get("fullName"), "%" + keyword + "%")));
        }
        if (isGift != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isGift"), isGift));
        }
        criteriaQuery.where(restrictions);
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("isTop")));
        return super.findList(criteriaQuery, null, count, null, null);
    }

    public List<Product> findList(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop,
                                  Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, Integer count, List<Filter> filters, List<Order> orders) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (productCategory != null) {
            restrictions = criteriaBuilder.and(
                    restrictions,
                    criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory),
                            criteriaBuilder.like(root.get("productCategory").<String>get("treePath"), "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%")));
        }
        if (brand != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("brand"), brand));
        }
        if (promotion != null) {
            Subquery<Product> subquery1 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot1 = subquery1.from(Product.class);
            subquery1.select(subqueryRoot1);
            subquery1.where(criteriaBuilder.equal(subqueryRoot1, root), criteriaBuilder.equal(subqueryRoot1.join("promotions"), promotion));

            Subquery<Product> subquery2 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot2 = subquery2.from(Product.class);
            subquery2.select(subqueryRoot2);
            subquery2.where(criteriaBuilder.equal(subqueryRoot2, root), criteriaBuilder.equal(subqueryRoot2.join("productCategory").join("promotions"), promotion));

            Subquery<Product> subquery3 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot3 = subquery3.from(Product.class);
            subquery3.select(subqueryRoot3);
            subquery3.where(criteriaBuilder.equal(subqueryRoot3, root), criteriaBuilder.equal(subqueryRoot3.join("brand").join("promotions"), promotion));

            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.exists(subquery1), criteriaBuilder.exists(subquery2), criteriaBuilder.exists(subquery3)));
        }
        if (tags != null && !tags.isEmpty()) {
            Subquery<Product> subquery = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot = subquery.from(Product.class);
            subquery.select(subqueryRoot);
            subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        }
        if (attributeValue != null) {
            for (Entry<Attribute, String> entry : attributeValue.entrySet()) {
                String propertyName = Product.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + entry.getKey().getPropertyIndex();
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get(propertyName), entry.getValue()));
            }
        }
        if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
            BigDecimal temp = startPrice;
            startPrice = endPrice;
            endPrice = temp;
        }
        if (startPrice != null && startPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.<Number>get("price"), startPrice));
        }
        if (endPrice != null && endPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Number>get("price"), endPrice));
        }
        if (isMarketable != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
        }
        if (isList != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), isList));
        }
        if (isTop != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isTop"), isTop));
        }
        if (isGift != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isGift"), isGift));
        }
        Path<Integer> stock = root.get("stock");
        Path<Integer> allocatedStock = root.get("allocatedStock");
        if (isOutOfStock != null) {
            if (isOutOfStock) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, allocatedStock));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, allocatedStock)));
            }
        }
        if (isStockAlert != null) {
            Setting setting = SettingUtils.get();
            if (isStockAlert) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount()))));
            }
        }
        criteriaQuery.where(restrictions);
        if (orderType == OrderType.priceAsc) {
            orders.add(Order.asc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.priceDesc) {
            orders.add(Order.desc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.salesDesc) {
            orders.add(Order.desc("sales"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.scoreDesc) {
            orders.add(Order.desc("score"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.dateDesc) {
            orders.add(Order.desc("createDate"));
        } else {
            orders.add(Order.desc("isTop"));
            orders.add(Order.desc("modifyDate"));
        }
        return super.findList(criteriaQuery, null, count, filters, orders);
    }

    public List<Product> findList(Set<ProductCategory> productCategories, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList,
                                  Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Tenant tenant, Area area, Boolean periferal, OrderType orderType, Integer count, List<Filter> filters, List<Order> orders) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (productCategories != null && productCategories.size() > 0) { // 商品分类
            Predicate categoryPredicate = criteriaBuilder.conjunction();
            Predicate categoryPredicate1 = null;
            for (ProductCategory productCategory : productCategories) {
                String categoryTreePath = ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR;
                if (categoryPredicate1 == null) {
                    categoryPredicate = criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory), criteriaBuilder.like(root.get("productCategory").<String>get("treePath"), "%" + categoryTreePath + "%"));
                    categoryPredicate1 = criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory), criteriaBuilder.like(root.get("productCategory").<String>get("treePath"), "%" + categoryTreePath + "%"));
                } else {
                    categoryPredicate = criteriaBuilder.or(categoryPredicate,
                            criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory), criteriaBuilder.like(root.get("productCategory").<String>get("treePath"), "%" + categoryTreePath + "%")));
                }
            }
            restrictions = criteriaBuilder.and(restrictions, categoryPredicate);
        }

        if (brand != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("brand"), brand));
        }
        if (promotion != null) {
            Subquery<Product> subquery1 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot1 = subquery1.from(Product.class);
            subquery1.select(subqueryRoot1);
            subquery1.where(criteriaBuilder.equal(subqueryRoot1, root), criteriaBuilder.equal(subqueryRoot1.join("promotionProducts").get("promotion"), promotion));

            Subquery<Product> subquery2 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot2 = subquery2.from(Product.class);
            subquery2.select(subqueryRoot2);
            subquery2.where(criteriaBuilder.equal(subqueryRoot2, root), criteriaBuilder.equal(subqueryRoot2.join("productCategory").join("promotions"), promotion));

            Subquery<Product> subquery3 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot3 = subquery3.from(Product.class);
            subquery3.select(subqueryRoot3);
            subquery3.where(criteriaBuilder.equal(subqueryRoot3, root), criteriaBuilder.equal(subqueryRoot3.join("brand").join("promotions"), promotion));

            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.exists(subquery1), criteriaBuilder.exists(subquery2), criteriaBuilder.exists(subquery3)));
        }
        if (tags != null && !tags.isEmpty()) {
            Subquery<Product> subquery = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot = subquery.from(Product.class);
            subquery.select(subqueryRoot);
            subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        }
        if (attributeValue != null) {
            for (Entry<Attribute, String> entry : attributeValue.entrySet()) {
                String propertyName = Product.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + entry.getKey().getPropertyIndex();
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get(propertyName), entry.getValue()));
            }
        }
        if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
            BigDecimal temp = startPrice;
            startPrice = endPrice;
            endPrice = temp;
        }
        if (startPrice != null && startPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.<Number>get("price"), startPrice));
        }
        if (endPrice != null && endPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Number>get("price"), endPrice));
        }
        if (isMarketable != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
        }
        if (isList != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), isList));
        }
        if (isTop != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isTop"), isTop));
        }
        if (isGift != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isGift"), isGift));
        }
        Path<Integer> stock = root.get("stock");
        Path<Integer> allocatedStock = root.get("allocatedStock");
        if (isOutOfStock != null) {
            if (isOutOfStock) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, allocatedStock));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, allocatedStock)));
            }
        }
        if (isStockAlert != null) {
            Setting setting = SettingUtils.get();
            if (isStockAlert) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount()))));
            }
        }

        List<Tenant> tenants = new ArrayList<Tenant>();
        if (tenant == null) {
            List<DeliveryCenter> dlvs = new ArrayList<DeliveryCenter>();
            if ((periferal != null) && (periferal)) {
                dlvs = deliveryCenterDao.findList(area, community.getLocation(), new BigDecimal(6));
            } else {
                dlvs = deliveryCenterDao.findList(area, community);
            }

            for (Iterator<DeliveryCenter> it = dlvs.iterator(); it.hasNext(); ) {
                DeliveryCenter dc = it.next();
                tenants.add(dc.getTenant());
            }
        } else {
            tenants.add(tenant);
        }
        if (tenants.size() == 0) {
            return new ArrayList<Product>(Collections.<Product>emptyList());
        }
        restrictions = criteriaBuilder.and(restrictions, root.get("tenant").in(tenants));

        criteriaQuery.where(restrictions);
        if (orderType == OrderType.priceAsc) {
            orders.add(Order.asc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.priceDesc) {
            orders.add(Order.desc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.salesDesc) {
            orders.add(Order.desc("sales"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.scoreDesc) {
            orders.add(Order.desc("score"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.dateDesc) {
            orders.add(Order.desc("createDate"));
        } else {
            orders.add(Order.desc("isTop"));
            orders.add(Order.desc("modifyDate"));
        }
        return super.findList(criteriaQuery, null, count, filters, orders);
    }

    public List<Product> findList(ProductCategory productCategory, Date beginDate, Date endDate, Integer first, Integer count) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), true));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant").get("status"), Tenant.Status.success));
        if (productCategory != null) {
            restrictions = criteriaBuilder.and(
                    restrictions,
                    criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory),
                            criteriaBuilder.like(root.get("productCategory").<String>get("treePath"), "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%")));
        }
        if (beginDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createDate"), beginDate));
        }
        if (endDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createDate"), endDate));
        }
        criteriaQuery.where(restrictions);
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("isTop")));
        return super.findList(criteriaQuery, first, count, null, null);
    }

    public List<Product> findList(Goods goods, Set<Product> excludes) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (goods != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("goods"), goods));
        }
        if (excludes != null && !excludes.isEmpty()) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(root.in(excludes)));
        }
        criteriaQuery.where(restrictions);
        return entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getResultList();
    }

    public List<Object[]> findSalesList(Date beginDate, Date endDate, Integer count) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Product> product = criteriaQuery.from(Product.class);
        Join<Product, OrderItem> orderItems = product.join("orderItems");
        Join<Product, net.wit.entity.Order> order = orderItems.join("order");
        criteriaQuery.multiselect(product.get("id"), product.get("sn"), product.get("name"), product.get("fullName"), product.get("price"), criteriaBuilder.sum(orderItems.<Integer>get("quantity")),
                criteriaBuilder.sum(criteriaBuilder.prod(orderItems.<Integer>get("quantity"), orderItems.<BigDecimal>get("price"))));
        Predicate restrictions = criteriaBuilder.conjunction();
        if (beginDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(order.<Date>get("createDate"), beginDate));
        }
        if (endDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(order.<Date>get("createDate"), endDate));
        }
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(order.get("orderStatus"), OrderStatus.completed), criteriaBuilder.equal(order.get("paymentStatus"), PaymentStatus.paid));
        criteriaQuery.where(restrictions);
        criteriaQuery.groupBy(product.get("id"), product.get("sn"), product.get("name"), product.get("fullName"), product.get("price"));
        criteriaQuery.orderBy(criteriaBuilder.desc(criteriaBuilder.sum(criteriaBuilder.prod(orderItems.<Integer>get("quantity"), orderItems.<BigDecimal>get("price")))));
        TypedQuery<Object[]> query = entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
        if (count != null && count >= 0) {
            query.setMaxResults(count);
        }
        return query.getResultList();
    }

    public Page<Product> findPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop,
                                  Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        List<Order> orders = pageable.getOrders();
        if (orderType == OrderType.priceAsc) {
            orders.add(Order.asc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.weight) {
            orders.add(Order.desc("priority"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.priceDesc) {
            orders.add(Order.desc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.salesDesc) {
            orders.add(Order.desc("sales"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.scoreDesc) {
            orders.add(Order.desc("score"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.dateDesc) {
            orders.add(Order.desc("createDate"));
        } else {
            orders.add(Order.desc("isTop"));
            orders.add(Order.desc("modifyDate"));
        }

        if (productCategory != null) {
            restrictions = criteriaBuilder.and(
                    restrictions,
                    criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory),
                            criteriaBuilder.like(root.get("productCategory").<String>get("treePath"), "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%")));
        }
        if (brand != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("brand"), brand));
        }
        if (promotion != null) {
            Subquery<Product> subquery1 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot1 = subquery1.from(Product.class);
            subquery1.select(subqueryRoot1);
            subquery1.where(criteriaBuilder.equal(subqueryRoot1, root), criteriaBuilder.equal(subqueryRoot1.join("promotions"), promotion));

            Subquery<Product> subquery2 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot2 = subquery2.from(Product.class);
            subquery2.select(subqueryRoot2);
            subquery2.where(criteriaBuilder.equal(subqueryRoot2, root), criteriaBuilder.equal(subqueryRoot2.join("productCategory").join("promotions"), promotion));

            Subquery<Product> subquery3 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot3 = subquery3.from(Product.class);
            subquery3.select(subqueryRoot3);
            subquery3.where(criteriaBuilder.equal(subqueryRoot3, root), criteriaBuilder.equal(subqueryRoot3.join("brand").join("promotions"), promotion));

            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.exists(subquery1), criteriaBuilder.exists(subquery2), criteriaBuilder.exists(subquery3)));
        }
        if (tags != null && !tags.isEmpty()) {
            Subquery<Product> subquery = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot = subquery.from(Product.class);
            subquery.select(subqueryRoot);
            subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        }
        if (attributeValue != null) {
            for (Entry<Attribute, String> entry : attributeValue.entrySet()) {
                String propertyName = Product.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + entry.getKey().getPropertyIndex();
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get(propertyName), entry.getValue()));
            }
        }
        if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
            BigDecimal temp = startPrice;
            startPrice = endPrice;
            endPrice = temp;
        }
        if (startPrice != null && startPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.<Number>get("price"), startPrice));
        }
        if (endPrice != null && endPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Number>get("price"), endPrice));
        }
        if (isMarketable != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
        }
        if (isList != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), isList));
        }
        if (isTop != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isTop"), isTop));
        }
        if (isGift != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isGift"), isGift));
        }
        Path<Integer> stock = root.get("stock");
        Path<Integer> allocatedStock = root.get("allocatedStock");
        if (isOutOfStock != null) {
            if (isOutOfStock) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, allocatedStock));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, allocatedStock)));
            }
        }
        if (isStockAlert != null) {
            Setting setting = SettingUtils.get();
            if (isStockAlert) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount()))));
            }
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    public Page<Product> findPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop,
                                  Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, Location location, BigDecimal distatce, OrderType orderType, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (productCategory != null) {
            restrictions = criteriaBuilder.and(
                    restrictions,
                    criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory),
                            criteriaBuilder.like(root.get("productCategory").<String>get("treePath"), "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%")));
        }
        if (brand != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("brand"), brand));
        }
        if (promotion != null) {
            Subquery<Product> subquery1 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot1 = subquery1.from(Product.class);
            subquery1.select(subqueryRoot1);
            subquery1.where(criteriaBuilder.equal(subqueryRoot1, root), criteriaBuilder.equal(subqueryRoot1.join("promotionProducts").get("promotion"), promotion));

            Subquery<Product> subquery2 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot2 = subquery2.from(Product.class);
            subquery2.select(subqueryRoot2);
            subquery2.where(criteriaBuilder.equal(subqueryRoot2, root), criteriaBuilder.equal(subqueryRoot2.join("productCategory").join("promotions"), promotion));

            Subquery<Product> subquery3 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot3 = subquery3.from(Product.class);
            subquery3.select(subqueryRoot3);
            subquery3.where(criteriaBuilder.equal(subqueryRoot3, root), criteriaBuilder.equal(subqueryRoot3.join("brand").join("promotions"), promotion));

            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.exists(subquery1), criteriaBuilder.exists(subquery2), criteriaBuilder.exists(subquery3)));
        }
        if (tags != null && !tags.isEmpty()) {
            Subquery<Product> subquery = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot = subquery.from(Product.class);
            subquery.select(subqueryRoot);
            subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        }
        if (attributeValue != null) {
            for (Entry<Attribute, String> entry : attributeValue.entrySet()) {
                String propertyName = Product.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + entry.getKey().getPropertyIndex();
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get(propertyName), entry.getValue()));
            }
        }
        if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
            BigDecimal temp = startPrice;
            startPrice = endPrice;
            endPrice = temp;
        }
        if (startPrice != null && startPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.<Number>get("price"), startPrice));
        }
        if (endPrice != null && endPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Number>get("price"), endPrice));
        }
        if (isMarketable != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
        }
        if (isList != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), isList));
        }
        if (isTop != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isTop"), isTop));
        }
        if (isGift != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isGift"), isGift));
        }
        Path<Integer> stock = root.get("stock");
        Path<Integer> allocatedStock = root.get("allocatedStock");
        if (isOutOfStock != null) {
            if (isOutOfStock) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, allocatedStock));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, allocatedStock)));
            }
        }
        if (isStockAlert != null) {
            Setting setting = SettingUtils.get();
            if (isStockAlert) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount()))));
            }
        }

        List<Tenant> tenants = new ArrayList<Tenant>();
        List<DeliveryCenter> dlvs = new ArrayList<DeliveryCenter>();
        if ((periferal != null) && periferal) {
            if ((location == null || !location.isExists()) && community != null) {
                dlvs = deliveryCenterDao.findList(area, community.getLocation(), new BigDecimal(6));
            } else if (location != null) {
                if (distatce == null) {
                    distatce = new BigDecimal(6);
                }
                dlvs = deliveryCenterDao.findList(area, location, distatce);
            }
        } else {
            dlvs = deliveryCenterDao.findList(area, community);
        }

        for (Iterator<DeliveryCenter> it = dlvs.iterator(); it.hasNext(); ) {
            DeliveryCenter dc = it.next();
            tenants.add(dc.getTenant());
        }
        if (tenants.size() > 0) {
            restrictions = criteriaBuilder.and(restrictions, root.<Tenant>get("tenant").in(tenants));
        }
//        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.<Tenant>get("tenant").get("status"), Status.success));
        List<Order> orders = pageable.getOrders();
        if (orderType == OrderType.priceAsc) {
            orders.add(Order.asc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.priceDesc) {
            orders.add(Order.desc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.salesDesc) {
            orders.add(Order.desc("sales"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.scoreDesc) {
            orders.add(Order.desc("score"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.dateDesc) {
            orders.add(Order.desc("createDate"));
        } else {
            orders.add(Order.desc("isTop"));
            orders.add(Order.desc("modifyDate"));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    public Page<Product> findPage(Member member, Pageable pageable) {
        if (member == null) {
            return new Page<Product>(Collections.<Product>emptyList(), 0, pageable);
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(root.join("favoriteMembers"), member));

        return super.findPage(criteriaQuery, pageable);
    }

    public Page<Product> findPage(Member member, ProductCategory productCategory, Brand brand, Pageable pageable) {
        if (member == null) {
            return new Page<Product>(Collections.<Product>emptyList(), 0, pageable);
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        // criteriaQuery.where(criteriaBuilder.equal(root.join("favoriteMembers"),
        // member));
        Subquery<Product> subquery = criteriaQuery.subquery(Product.class);
        Root<Product> subqueryRoot = subquery.from(Product.class);
        subquery.select(subqueryRoot);
        subquery.where(criteriaBuilder.equal(subqueryRoot, root), criteriaBuilder.equal(subqueryRoot.join("favoriteMembers"), member));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.exists(subquery)));
        if (productCategory != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("productCategory"), productCategory));
        }
        if (brand != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("brand"), brand));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    public Page<Product> findPage(Member member, Integer days, ProductCategory productCategory, Pageable pageable) {
        if (member == null && days > 0) {
            return new Page<Product>(Collections.<Product>emptyList(), 0, pageable);
        }
        Date latestDate = DateUtil.transpositionDate(new Date(), Calendar.DATE, days);
        String hql = "from Product p where exists(from OrderItem oi where oi.product = p and oi.order.createDate > ? and oi.order.member = ?)";
        if (productCategory != null) {
            hql += " and (p.productCategory.id = " + productCategory.getId() + " or p.productCategory.treePath like '" + productCategory.getTreePath() + productCategory.getId() + "%')";
        }
        Session session = (Session) entityManager.getDelegate();
        Query query = session.createQuery(hql);
        query.setParameter(0, latestDate);
        query.setParameter(1, member);
        query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize()).setMaxResults(pageable.getPageSize());
        @SuppressWarnings("unchecked")
        List<Product> products = query.list();
        query = session.createQuery("select count(*) " + hql);
        query.setParameter(0, latestDate);
        query.setParameter(1, member);
        Long count = (Long) query.list().get(0);
        return new Page<Product>(products, count, pageable);
    }

    public Page<Product> findMyPage(Tenant tenant, ProductCategory productCategory, ProductCategoryTenant productCategoryTenant, String keyword, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue,
                                    BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, String phonetic, OrderType orderType, Pageable pageable) {
        if (tenant == null) {
            return new Page<Product>(Collections.<Product>emptyList(), 0, pageable);
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (productCategory != null) {
            restrictions = criteriaBuilder.and(
                    restrictions,
                    criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory),
                            criteriaBuilder.like(root.get("productCategory").<String>get("treePath"), "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%")));
        }
        if (productCategoryTenant != null) {
            restrictions = criteriaBuilder.and(
                    restrictions,
                    criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategoryTenant"), productCategoryTenant),
                            criteriaBuilder.like(root.get("productCategoryTenant").<String>get("treePath"), "%" + ProductCategoryTenant.TREE_PATH_SEPARATOR + productCategoryTenant.getId() + ProductCategoryTenant.TREE_PATH_SEPARATOR + "%")));
        }

        if (brand != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("brand"), brand));
        }
        if (StringUtils.isNotBlank(keyword)) { // 关键字
            restrictions = criteriaBuilder
                    .and(restrictions,
                            criteriaBuilder.or(criteriaBuilder.like(root.<String>get("fullName"), "%" + keyword + "%"), criteriaBuilder.like(root.<String>get("sn"), "%" + keyword + "%"),
                                    criteriaBuilder.like(root.<String>get("barcode"), "%" + keyword + "%")));
        }
        if (StringUtils.isNotBlank(phonetic)) { // 拼音条件
            restrictions = criteriaBuilder.and(
                    restrictions,
                    criteriaBuilder.or(criteriaBuilder.like(root.<String>get("phonetic"), "%" + phonetic + "%"), criteriaBuilder.like(root.<String>get("fullName"), "%" + phonetic + "%"),
                            criteriaBuilder.like(root.<String>get("sn"), "%" + phonetic + "%")));
        }
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));

        if (promotion != null) {
            Subquery<Product> subquery1 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot1 = subquery1.from(Product.class);
            subquery1.select(subqueryRoot1);
            subquery1.where(criteriaBuilder.equal(subqueryRoot1, root), criteriaBuilder.equal(subqueryRoot1.join("promotionProducts").get("promotion"), promotion));

            Subquery<Product> subquery2 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot2 = subquery2.from(Product.class);
            subquery2.select(subqueryRoot2);
            subquery2.where(criteriaBuilder.equal(subqueryRoot2, root), criteriaBuilder.equal(subqueryRoot2.join("productCategory").join("promotions"), promotion));

            Subquery<Product> subquery3 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot3 = subquery3.from(Product.class);
            subquery3.select(subqueryRoot3);
            subquery3.where(criteriaBuilder.equal(subqueryRoot3, root), criteriaBuilder.equal(subqueryRoot3.join("brand").join("promotions"), promotion));

            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.exists(subquery1), criteriaBuilder.exists(subquery2), criteriaBuilder.exists(subquery3)));
        }
        if (tags != null && !tags.isEmpty()) {
            Subquery<Product> subquery = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot = subquery.from(Product.class);
            subquery.select(subqueryRoot);
            subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        }
        if (attributeValue != null) {
            for (Entry<Attribute, String> entry : attributeValue.entrySet()) {
                String propertyName = Product.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + entry.getKey().getPropertyIndex();
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get(propertyName), entry.getValue()));
            }
        }
        if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
            BigDecimal temp = startPrice;
            startPrice = endPrice;
            endPrice = temp;
        }
        if (startPrice != null && startPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.<Number>get("wholePrice"), startPrice));
        }
        if (endPrice != null && endPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Number>get("wholePrice"), endPrice));
        }
        if (isMarketable != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
        }
        if (isList != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), isList));
        }
        if (isTop != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isTop"), isTop));
        }
        if (isGift != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isGift"), isGift));
        }
        Path<Integer> stock = root.get("stock");
        Path<Integer> allocatedStock = root.get("allocatedStock");
        if (isOutOfStock != null) {
            if (isOutOfStock) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, allocatedStock));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, allocatedStock)));
            }
        }
        if (isStockAlert != null) {
            Setting setting = SettingUtils.get();
            if (isStockAlert) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount()))));
            }
        }
        criteriaQuery.where(restrictions);
        List<Order> orders = pageable.getOrders();
        if (orderType == OrderType.priceAsc) {
            orders.add(Order.asc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.priceDesc) {
            orders.add(Order.desc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.salesDesc) {
            orders.add(Order.desc("sales"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.scoreDesc) {
            orders.add(Order.desc("score"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.dateDesc) {
            orders.add(Order.desc("createDate"));
        } else {
            orders.add(Order.desc("isTop"));
            orders.add(Order.desc("modifyDate"));
        }
        return super.findPage(criteriaQuery, pageable);
    }

    public List<Product> findMyList(Tenant tenant, ProductCategory productCategory, ProductCategoryTenant productCategoryTenant, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice,
                                    BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Integer count, OrderType orderType) {
        if (tenant == null) {
            return new ArrayList<Product>();
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (productCategory != null) {
            restrictions = criteriaBuilder.and(
                    restrictions,
                    criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory),
                            criteriaBuilder.like(root.get("productCategory").<String>get("treePath"), "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%")));
        }
        if (productCategoryTenant != null) {
            restrictions = criteriaBuilder.and(
                    restrictions,
                    criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategoryTenant"), productCategoryTenant),
                            criteriaBuilder.like(root.get("productCategoryTenant").<String>get("treePath"), "%" + ProductCategoryTenant.TREE_PATH_SEPARATOR + productCategoryTenant.getId() + ProductCategoryTenant.TREE_PATH_SEPARATOR + "%")));
        }
        /**
         * try { if (productCategoryTenant != null) { Subquery<Product> subquery1 = criteriaQuery.subquery(Product.class); Root<Product> subqueryRoot1 =
         * subquery1.from(Product.class); subquery1.select(subqueryRoot1); subquery1.where( criteriaBuilder.equal(subqueryRoot1, root),
         * criteriaBuilder.or(criteriaBuilder.equal(subqueryRoot1.join("productCategoryTenants"), productCategoryTenant),
         * criteriaBuilder.equal(subqueryRoot1.join("productCategoryTenants").<String> get("treePath"), "%" + ProductCategory.TREE_PATH_SEPARATOR +
         * productCategoryTenant.getId() + productCategoryTenant.TREE_PATH_SEPARATOR + "%"))); restrictions = criteriaBuilder.and(restrictions,
         * criteriaBuilder.exists(subquery1)); } } catch (Exception e) { e.printStackTrace(); }
         **/
        if (brand != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("brand"), brand));
        }
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
        if (promotion != null) {
            Subquery<Product> subquery1 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot1 = subquery1.from(Product.class);
            subquery1.select(subqueryRoot1);
            subquery1.where(criteriaBuilder.equal(subqueryRoot1, root), criteriaBuilder.equal(subqueryRoot1.join("promotions"), promotion));

            Subquery<Product> subquery2 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot2 = subquery2.from(Product.class);
            subquery2.select(subqueryRoot2);
            subquery2.where(criteriaBuilder.equal(subqueryRoot2, root), criteriaBuilder.equal(subqueryRoot2.join("productCategory").join("promotions"), promotion));

            Subquery<Product> subquery3 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot3 = subquery3.from(Product.class);
            subquery3.select(subqueryRoot3);
            subquery3.where(criteriaBuilder.equal(subqueryRoot3, root), criteriaBuilder.equal(subqueryRoot3.join("brand").join("promotions"), promotion));

            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.exists(subquery1), criteriaBuilder.exists(subquery2), criteriaBuilder.exists(subquery3)));
        }
        if (tags != null && !tags.isEmpty()) {
            Subquery<Product> subquery = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot = subquery.from(Product.class);
            subquery.select(subqueryRoot);
            subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        }
        if (attributeValue != null) {
            for (Entry<Attribute, String> entry : attributeValue.entrySet()) {
                String propertyName = Product.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + entry.getKey().getPropertyIndex();
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get(propertyName), entry.getValue()));
            }
        }
        if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
            BigDecimal temp = startPrice;
            startPrice = endPrice;
            endPrice = temp;
        }
        if (startPrice != null && startPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.<Number>get("wholePrice"), startPrice));
        }
        if (endPrice != null && endPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Number>get("wholePrice"), endPrice));
        }
        if (isMarketable != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
        }
        if (isList != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), isList));
        }
        if (isTop != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isTop"), isTop));
        }
        if (isGift != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isGift"), isGift));
        }
        Path<Integer> stock = root.get("stock");
        Path<Integer> allocatedStock = root.get("allocatedStock");
        if (isOutOfStock != null) {
            if (isOutOfStock) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, allocatedStock));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, allocatedStock)));
            }
        }
        if (isStockAlert != null) {
            Setting setting = SettingUtils.get();
            if (isStockAlert) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount()))));
            }
        }
        criteriaQuery.where(restrictions);
        List<Order> orders = new ArrayList<Order>();
        if (orderType == OrderType.priceAsc) {
            orders.add(Order.asc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.priceDesc) {
            orders.add(Order.desc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.salesDesc) {
            orders.add(Order.desc("sales"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.scoreDesc) {
            orders.add(Order.desc("score"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.dateDesc) {
            orders.add(Order.desc("createDate"));
        } else {
            orders.add(Order.desc("isTop"));
            orders.add(Order.desc("modifyDate"));
        }
        return super.findList(criteriaQuery, null, count, null, orders);
    }

    public Long count(Member favoriteMember, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (favoriteMember != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.join("favoriteMembers"), favoriteMember));
        }
        if (isMarketable != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
        }
        if (isList != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), isList));
        }
        if (isTop != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isTop"), isTop));
        }
        if (isGift != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isGift"), isGift));
        }
        Path<Integer> stock = root.get("stock");
        Path<Integer> allocatedStock = root.get("allocatedStock");
        if (isOutOfStock != null) {
            if (isOutOfStock) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, allocatedStock));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, allocatedStock)));
            }
        }
        if (isStockAlert != null) {
            Setting setting = SettingUtils.get();
            if (isStockAlert) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount()))));
            }
        }
        criteriaQuery.where(restrictions);
        return super.count(criteriaQuery, null);
    }

    public boolean isPurchased(Member member, Product product) {
        if (member == null || product == null) {
            return false;
        }
        String jqpl = "select count(orderItem) from OrderItem orderItem where orderItem.product = :product and orderItem.order.member = :member and orderItem.order.orderStatus = :orderStatus";
        Long count = entityManager.createQuery(jqpl, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("product", product).setParameter("member", member).setParameter("orderStatus", OrderStatus.completed).getSingleResult();
        return count > 0;
    }

    /**
     * 设置值并保存
     *
     * @param product 商品
     */
    @Override
    public void persist(Product product) {
        Assert.notNull(product);

        setValue(product);
        super.persist(product);
    }

    /**
     * 设置值并更新
     *
     * @param product 商品
     * @return 商品
     */
    @Override
    public Product merge(Product product) {
        Assert.notNull(product);

//        if (!product.getIsGift()) {
//            String jpql = "delete from GiftItem giftItem where giftItem.gift = :product";
//            entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("product", product).executeUpdate();
//        }
//        if (!product.getIsMarketable() || product.getIsGift()) {
//            String jpql = "delete from CartItem cartItem where cartItem.product = :product";
//            entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("product", product).executeUpdate();
//        }
        setValue(product);   //设置FullName
        return super.merge(product);
    }

    @Override
    public void remove(Product product) {
        if (product != null) {
            Goods goods = product.getGoods();
            if (goods != null && goods.getProducts() != null) {
                goods.getProducts().remove(product);
                if (goods.getProducts().isEmpty()) {
                    goodsDao.remove(goods);
                }
            }
        }
        super.remove(product);
    }

    /**
     * 设置值
     *
     * @param product 商品
     */
    private void setValue(Product product) {
        if (product == null) {
            return;
        }
        if (StringUtils.isEmpty(product.getSn())) {
            String sn;
            do {
                sn = snDao.generate(Type.product);
            } while (snExists(sn));
            product.setSn(sn);
        }
        String getFullName = null;
        try {
            getFullName = product.getFullName();
        } catch (Exception ex) {
            getFullName = "";
        }
        if (getFullName == null || getFullName.equals("")) {
            StringBuffer fullName = new StringBuffer(product.getName());
            if (product.getSpecificationValues() != null && !product.getSpecificationValues().isEmpty()) {
                List<SpecificationValue> specificationValues = new ArrayList<SpecificationValue>(product.getSpecificationValues());
                Collections.sort(specificationValues, new Comparator<SpecificationValue>() {
                    public int compare(SpecificationValue a1, SpecificationValue a2) {
                        return new CompareToBuilder().append(a1.getSpecification(), a2.getSpecification()).toComparison();
                    }
                });
                fullName.append(Product.FULL_NAME_SPECIFICATION_PREFIX);
                int i = 0;
                for (Iterator<SpecificationValue> iterator = specificationValues.iterator(); iterator.hasNext(); i++) {
                    if (i != 0) {
                        fullName.append(Product.FULL_NAME_SPECIFICATION_SEPARATOR);
                    }
                    fullName.append(iterator.next().getName());
                }
                fullName.append(Product.FULL_NAME_SPECIFICATION_SUFFIX);
            }
            product.setFullName(fullName.toString());
        }
    }

    public Object findByUnion(Tenant tenant, Union union, OrderType datedesc, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThan(criteriaBuilder.size(root.<Set<Union>>get("unions")), 0));
        if (tenant != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
        }
        if (union != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.join("unions"), union));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    @SuppressWarnings("unchecked")
    public Page<Product> search(String keyword, String phonetic, BigDecimal startPrice, BigDecimal endPrice, OrderType orderType, Pageable pageable) {
        if (StringUtils.isEmpty(keyword)) {
            return (Page<Product>) Collections.<Product>emptyList();
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (pattern.matcher(keyword).matches()) {
            restrictions = criteriaBuilder.and(restrictions,
                    criteriaBuilder.or(criteriaBuilder.equal(root.get("id"), Long.valueOf(keyword)), criteriaBuilder.like(root.<String>get("sn"), "%" + keyword + "%"), criteriaBuilder.like(root.<String>get("fullName"), "%" + keyword + "%")),
                    criteriaBuilder.like(root.get("productCategory").<String>get("name"), "%" + keyword + "%"));
        } else {
            restrictions = criteriaBuilder.and(
                    restrictions,
                    criteriaBuilder.or(criteriaBuilder.like(root.<String>get("sn"), "%" + keyword + "%"), criteriaBuilder.like(root.<String>get("fullName"), "%" + keyword + "%"),
                            criteriaBuilder.like(root.get("productCategory").<String>get("name"), "%" + keyword + "%")));
        }
        if (StringUtils.isNotBlank(phonetic)) { // 拼音条件
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.<String>get("phonetic"), "%" + phonetic + "%"));
        }
        if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
            BigDecimal temp = startPrice;
            startPrice = endPrice;
            endPrice = temp;
        }
        if (startPrice != null && startPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.<Number>get("price"), startPrice));
        }
        if (endPrice != null && endPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Number>get("price"), endPrice));
        }
        List<Order> orders = pageable.getOrders();
        if (orderType == OrderType.priceAsc) {
            orders.add(Order.asc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.weight) {
            orders.add(Order.desc("priority"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.priceDesc) {
            orders.add(Order.desc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.salesDesc) {
            orders.add(Order.desc("sales"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.scoreDesc) {
            orders.add(Order.desc("score"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.dateDesc) {
            orders.add(Order.desc("createDate"));
        } else {
            orders.add(Order.desc("isTop"));
            orders.add(Order.desc("modifyDate"));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    //shenjc
    @SuppressWarnings("unchecked")
    public Page<Product> search(String keyword, String phonetic, Member member, OrderType orderType, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        if (StringUtils.isEmpty(keyword)) {
            criteriaQuery.where(criteriaBuilder.equal(root.join("favoriteMembers"), member));
            return super.findPage(criteriaQuery, pageable);
        }
        Predicate restrictions = criteriaBuilder.conjunction();
        if (member != null) {
            Subquery<Product> subquery1 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot1 = subquery1.from(Product.class);
            subquery1.select(subqueryRoot1);
            subquery1.where(criteriaBuilder.equal(subqueryRoot1, root), criteriaBuilder.equal(subqueryRoot1.join("favoriteMembers"), member));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.exists(subquery1)));
        }
        if (pattern.matcher(keyword).matches()) {
            restrictions = criteriaBuilder.and(restrictions,
                    criteriaBuilder.or(criteriaBuilder.equal(root.get("id"), Long.valueOf(keyword)), criteriaBuilder.like(root.<String>get("sn"), "%" + keyword + "%"), criteriaBuilder.like(root.<String>get("fullName"), "%" + keyword + "%")));
        } else {
            restrictions = criteriaBuilder.and(
                    restrictions,
                    criteriaBuilder.or(criteriaBuilder.like(root.<String>get("sn"), "%" + keyword + "%"), criteriaBuilder.like(root.<String>get("fullName"), "%" + keyword + "%")));
        }
        if (StringUtils.isNotBlank(phonetic)) { // 拼音条件
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.<String>get("phonetic"), "%" + phonetic + "%"));
        }
        criteriaQuery.where(restrictions);
        List<Order> orders = pageable.getOrders();
        if (orderType == OrderType.priceAsc) {
            orders.add(Order.asc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.weight) {
            orders.add(Order.desc("priority"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.priceDesc) {
            orders.add(Order.desc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.salesDesc) {
            orders.add(Order.desc("sales"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.scoreDesc) {
            orders.add(Order.desc("score"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.dateDesc) {
            orders.add(Order.desc("createDate"));
        } else {
            orders.add(Order.desc("isTop"));
            orders.add(Order.desc("modifyDate"));
        }
        return super.findPage(criteriaQuery, pageable);
    }


    public Page<Product> mobileFindPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList,
                                        Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, Location location, BigDecimal distatce, OrderType orderType, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (productCategory != null) {
            restrictions = criteriaBuilder.and(
                    restrictions,
                    criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory),
                            criteriaBuilder.like(root.get("productCategory").<String>get("treePath"), "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%")));
        }
        if (brand != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("brand"), brand));
        }
        if (promotion != null) {
            Subquery<Product> subquery1 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot1 = subquery1.from(Product.class);
            subquery1.select(subqueryRoot1);
            subquery1.where(criteriaBuilder.equal(subqueryRoot1, root), criteriaBuilder.equal(subqueryRoot1.join("promotions"), promotion));

            Subquery<Product> subquery2 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot2 = subquery2.from(Product.class);
            subquery2.select(subqueryRoot2);
            subquery2.where(criteriaBuilder.equal(subqueryRoot2, root), criteriaBuilder.equal(subqueryRoot2.join("productCategory").join("promotions"), promotion));

            Subquery<Product> subquery3 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot3 = subquery3.from(Product.class);
            subquery3.select(subqueryRoot3);
            subquery3.where(criteriaBuilder.equal(subqueryRoot3, root), criteriaBuilder.equal(subqueryRoot3.join("brand").join("promotions"), promotion));

            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.exists(subquery1), criteriaBuilder.exists(subquery2), criteriaBuilder.exists(subquery3)));
        }
        if (tags != null && !tags.isEmpty()) {
            Subquery<Product> subquery = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot = subquery.from(Product.class);
            subquery.select(subqueryRoot);
            subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        }
        if (attributeValue != null) {
            for (Entry<Attribute, String> entry : attributeValue.entrySet()) {
                String propertyName = Product.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + entry.getKey().getPropertyIndex();
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get(propertyName), entry.getValue()));
            }
        }
        if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
            BigDecimal temp = startPrice;
            startPrice = endPrice;
            endPrice = temp;
        }
        if (startPrice != null && startPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.<Number>get("price"), startPrice));
        }
        if (endPrice != null && endPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Number>get("price"), endPrice));
        }
        if (isMarketable != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
        }
        if (isList != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), isList));
        }
        if (isTop != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isTop"), isTop));
        }
        if (isGift != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isGift"), isGift));
        }
        Path<Integer> stock = root.get("stock");
        Path<Integer> allocatedStock = root.get("allocatedStock");
        if (isOutOfStock != null) {
            if (isOutOfStock) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, allocatedStock));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, allocatedStock)));
            }
        }
        if (isStockAlert != null) {
            Setting setting = SettingUtils.get();
            if (isStockAlert) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount()))));
            }
        }

        List<Tenant> tenants = new ArrayList<Tenant>();
        List<DeliveryCenter> dlvs = new ArrayList<DeliveryCenter>();
        if ((periferal != null) && periferal) {
            if ((location == null || !location.isExists()) && community != null) {
                dlvs = deliveryCenterDao.findList(area, community.getLocation(), new BigDecimal(6));
            } else if (location.isExists()) {
                if (distatce == null) {
                    distatce = new BigDecimal(6);
                }
                dlvs = deliveryCenterDao.findList(area, location, distatce);
            }
        } else {
            dlvs = deliveryCenterDao.findList(area, community);
        }

        for (Iterator<DeliveryCenter> it = dlvs.iterator(); it.hasNext(); ) {
            DeliveryCenter dc = it.next();
            tenants.add(dc.getTenant());
        }
        if (tenants.size() == 0) {
            return new Page<Product>(Collections.<Product>emptyList(), 0, pageable);
        }
        restrictions = criteriaBuilder.and(restrictions, root.<Tenant>get("tenant").in(tenants));

        criteriaQuery.where(restrictions);
        List<Order> orders = pageable.getOrders();
        if (orderType == OrderType.priceAsc) {
            orders.add(Order.asc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.weight) {
            orders.add(Order.desc("priority"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.priceDesc) {
            orders.add(Order.desc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.salesDesc) {
            orders.add(Order.desc("sales"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.scoreDesc) {
            orders.add(Order.desc("score"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.dateDesc) {
            orders.add(Order.desc("createDate"));
        } else {
            orders.add(Order.desc("isTop"));
            orders.add(Order.desc("modifyDate"));
        }
        return super.findPage(criteriaQuery, pageable);
    }

    public Page<Product> findPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop,
                                  Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, OrderType orderType, String phonetic, String keyword, Location location, BigDecimal distatce, Pageable pageable) {
        CriteriaQuery<Product> criteriaQuery = criteriaQuery(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, community, area, periferal, phonetic, keyword,
                location, distatce);
        List<Order> orders = pageable.getOrders();
        if (orderType == OrderType.priceAsc) {
            orders.add(Order.asc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.weight) {
            orders.add(Order.desc("priority"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.priceDesc) {
            orders.add(Order.desc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.salesDesc) {
            orders.add(Order.desc("sales"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.scoreDesc) {
            orders.add(Order.desc("score"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.dateDesc) {
            orders.add(Order.desc("createDate"));
        } else {
            orders.add(Order.desc("isTop"));
            orders.add(Order.desc("modifyDate"));
        }
        return super.findPage(criteriaQuery, pageable);
    }

    private CriteriaQuery<Product> criteriaQuery(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList,
                                                 Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, String phonetic, String keyword, Location location, BigDecimal distatce) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (productCategory != null) { // 商品分类
            String categoryTreePath = ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR;
            Predicate categoryPredicate = criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory), criteriaBuilder.like(root.get("productCategory").<String>get("treePath"), "%" + categoryTreePath + "%"));
            restrictions = criteriaBuilder.and(restrictions, categoryPredicate);
        }
        if (brand != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("brand"), brand));
        }
        if (promotion != null) { // 促销信息
            Subquery<Product> promotionProductSubquery = criteriaQuery.subquery(Product.class);
            Root<Product> promotionProductSubqueryRoot = promotionProductSubquery.from(Product.class);
            promotionProductSubquery.select(promotionProductSubqueryRoot);
            promotionProductSubquery.where(criteriaBuilder.equal(promotionProductSubqueryRoot, root), criteriaBuilder.equal(promotionProductSubqueryRoot.join("promotionProducts").get("promotion"), promotion));

            Subquery<Product> productCategorySubquery = criteriaQuery.subquery(Product.class);
            Root<Product> productCategorySubqueryRoot = productCategorySubquery.from(Product.class);
            productCategorySubquery.select(productCategorySubqueryRoot);
            productCategorySubquery.where(criteriaBuilder.equal(productCategorySubqueryRoot, root), criteriaBuilder.equal(productCategorySubqueryRoot.join("productCategory").join("promotions"), promotion));

            Subquery<Product> brandSubQuery = criteriaQuery.subquery(Product.class);
            Root<Product> brandSubQueryRoot = brandSubQuery.from(Product.class);
            brandSubQuery.select(brandSubQueryRoot);
            brandSubQuery.where(criteriaBuilder.equal(brandSubQueryRoot, root), criteriaBuilder.equal(brandSubQueryRoot.join("brand").join("promotions"), promotion));

            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.exists(promotionProductSubquery), criteriaBuilder.exists(productCategorySubquery), criteriaBuilder.exists(brandSubQuery)));
        }
        if (tags != null && !tags.isEmpty()) { // 标签
            Subquery<Product> tagsSubquery = criteriaQuery.subquery(Product.class);
            Root<Product> tagsSubqueryRoot = tagsSubquery.from(Product.class);
            tagsSubquery.select(tagsSubqueryRoot);
            tagsSubquery.where(criteriaBuilder.equal(tagsSubqueryRoot, root), tagsSubqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(tagsSubquery));
        }
        if (attributeValue != null) { // 属性
            for (Entry<Attribute, String> entry : attributeValue.entrySet()) {
                String propertyName = Product.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + entry.getKey().getPropertyIndex();
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get(propertyName), entry.getValue()));
            }
        }
        if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
            BigDecimal temp = startPrice;
            startPrice = endPrice;
            endPrice = temp;
        }
        if (startPrice != null && startPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.<Number>get("price"), startPrice));
        }
        if (endPrice != null && endPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Number>get("price"), endPrice));
        }
        if (isMarketable != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
        }
        if (isList != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), isList));
        }
        if (isTop != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isTop"), isTop));
        }
        if (isGift != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isGift"), isGift));
        }
        Path<Integer> stock = root.get("stock");
        Path<Integer> allocatedStock = root.get("allocatedStock");
        if (isOutOfStock != null) {
            if (isOutOfStock) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, allocatedStock));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, allocatedStock)));
            }
        }
        if (isStockAlert != null) {
            Setting setting = SettingUtils.get();
            if (isStockAlert) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount()))));
            }
        }

        List<Tenant> tenants = new ArrayList<Tenant>();
        List<DeliveryCenter> dlvs = new ArrayList<DeliveryCenter>();
        if (location != null && location.isExists()) {
            dlvs = deliveryCenterDao.findList(area, location, distatce);
        } else {
            dlvs = deliveryCenterDao.findList(area, community);
        }

        for (Iterator<DeliveryCenter> it = dlvs.iterator(); it.hasNext(); ) {
            DeliveryCenter dc = it.next();
            tenants.add(dc.getTenant());
        }
        if (tenants.size() > 0) {
            restrictions = criteriaBuilder.and(restrictions, root.<Tenant>get("tenant").in(tenants));
        }

        if (StringUtils.isNotBlank(phonetic)) { // 拼音条件
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.<String>get("phonetic"), "%" + phonetic + "%"));
        }
        if (StringUtils.isNotBlank(keyword)) { // 关键字
            Predicate snPredicate = criteriaBuilder.like(root.<String>get("sn"), "%" + keyword + "%");
            Predicate fullNamePredicate = criteriaBuilder.like(root.<String>get("fullName"), "%" + keyword + "%");
            if (pattern.matcher(keyword).matches()) {
                Predicate idPredicate = criteriaBuilder.equal(root.get("id"), Long.valueOf(keyword));
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(idPredicate, snPredicate, fullNamePredicate));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(snPredicate, fullNamePredicate));
            }
        }
        return criteriaQuery.where(restrictions);
    }

    public Page<Product> mobileFindPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList,
                                        Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, OrderType orderType, String phonetic, String keyword, Location location, BigDecimal distatce, Pageable pageable) {
        CriteriaQuery<Product> criteriaQuery = criteriaQueryMobile(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, community, area, periferal, phonetic,
                keyword, location, distatce);
        if (criteriaQuery == null) {
            return new Page<Product>(Collections.<Product>emptyList(), 0, pageable);
        }
        List<Order> orders = pageable.getOrders();
        if (orderType == OrderType.priceAsc) {
            orders.add(Order.asc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.weight) {
            orders.add(Order.desc("priority"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.priceDesc) {
            orders.add(Order.desc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.salesDesc) {
            orders.add(Order.desc("sales"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.scoreDesc) {
            orders.add(Order.desc("score"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.dateDesc) {
            orders.add(Order.desc("createDate"));
        } else {
            orders.add(Order.desc("isTop"));
            orders.add(Order.desc("modifyDate"));
        }
        return super.findPage(criteriaQuery, pageable);
    }

    private CriteriaQuery<Product> criteriaQueryMobile(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable,
                                                       Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, String phonetic, String keyword, Location location, BigDecimal distatce) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (productCategory != null) { // 商品分类
            String categoryTreePath = ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR;
            Predicate categoryPredicate = criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory), criteriaBuilder.like(root.get("productCategory").<String>get("treePath"), "%" + categoryTreePath + "%"));
            restrictions = criteriaBuilder.and(restrictions, categoryPredicate);
        }
        if (brand != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("brand"), brand));
        }
        if (promotion != null) { // 促销信息
            Subquery<Product> promotionProductSubquery = criteriaQuery.subquery(Product.class);
            Root<Product> promotionProductSubqueryRoot = promotionProductSubquery.from(Product.class);
            promotionProductSubquery.select(promotionProductSubqueryRoot);
            promotionProductSubquery.where(criteriaBuilder.equal(promotionProductSubqueryRoot, root), criteriaBuilder.equal(promotionProductSubqueryRoot.join("promotionProducts").get("promotion"), promotion));

            Subquery<Product> productCategorySubquery = criteriaQuery.subquery(Product.class);
            Root<Product> productCategorySubqueryRoot = productCategorySubquery.from(Product.class);
            productCategorySubquery.select(productCategorySubqueryRoot);
            productCategorySubquery.where(criteriaBuilder.equal(productCategorySubqueryRoot, root), criteriaBuilder.equal(productCategorySubqueryRoot.join("productCategory").join("promotions"), promotion));

            Subquery<Product> brandSubQuery = criteriaQuery.subquery(Product.class);
            Root<Product> brandSubQueryRoot = brandSubQuery.from(Product.class);
            brandSubQuery.select(brandSubQueryRoot);
            brandSubQuery.where(criteriaBuilder.equal(brandSubQueryRoot, root), criteriaBuilder.equal(brandSubQueryRoot.join("brand").join("promotions"), promotion));

            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.exists(promotionProductSubquery), criteriaBuilder.exists(productCategorySubquery), criteriaBuilder.exists(brandSubQuery)));
        }
        if (tags != null && !tags.isEmpty()) { // 标签
            Subquery<Product> tagsSubquery = criteriaQuery.subquery(Product.class);
            Root<Product> tagsSubqueryRoot = tagsSubquery.from(Product.class);
            tagsSubquery.select(tagsSubqueryRoot);
            tagsSubquery.where(criteriaBuilder.equal(tagsSubqueryRoot, root), tagsSubqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(tagsSubquery));
        }
        if (attributeValue != null) { // 属性
            for (Entry<Attribute, String> entry : attributeValue.entrySet()) {
                String propertyName = Product.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + entry.getKey().getPropertyIndex();
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get(propertyName), entry.getValue()));
            }
        }
        if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
            BigDecimal temp = startPrice;
            startPrice = endPrice;
            endPrice = temp;
        }
        if (startPrice != null && startPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.<Number>get("price"), startPrice));
        }
        if (endPrice != null && endPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Number>get("price"), endPrice));
        }
        if (isMarketable != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
        }
        if (isList != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), isList));
        }
        if (isTop != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isTop"), isTop));
        }
        if (isGift != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isGift"), isGift));
        }
        Path<Integer> stock = root.get("stock");
        Path<Integer> allocatedStock = root.get("allocatedStock");
        if (isOutOfStock != null) {
            if (isOutOfStock) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, allocatedStock));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, allocatedStock)));
            }
        }
        if (isStockAlert != null) {
            Setting setting = SettingUtils.get();
            if (isStockAlert) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount()))));
            }
        }

        List<Tenant> tenants = new ArrayList<Tenant>();
        List<DeliveryCenter> dlvs = new ArrayList<DeliveryCenter>();
        if ((periferal != null) && periferal) {
            if ((location == null || !location.isExists()) && community != null) {
                dlvs = deliveryCenterDao.findList(area, community.getLocation(), new BigDecimal(6));
            } else if (location.isExists()) {
                if (distatce == null) {
                    distatce = new BigDecimal(6);
                }
                dlvs = deliveryCenterDao.findList(area, location, distatce);
            }
        } else {
            dlvs = deliveryCenterDao.findList(area, community);
        }

        for (Iterator<DeliveryCenter> it = dlvs.iterator(); it.hasNext(); ) {
            DeliveryCenter dc = it.next();
            tenants.add(dc.getTenant());
        }
        if (tenants.size() == 0) {
            return null;
        }

        if (StringUtils.isNotBlank(phonetic)) { // 拼音条件
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.<String>get("phonetic"), "%" + phonetic + "%"));
        }
        if (StringUtils.isNotBlank(keyword)) { // 关键字
            Predicate snPredicate = criteriaBuilder.like(root.<String>get("sn"), "%" + keyword + "%");
            Predicate fullNamePredicate = criteriaBuilder.like(root.<String>get("fullName"), "%" + keyword + "%");
            if (pattern.matcher(keyword).matches()) {
                Predicate idPredicate = criteriaBuilder.equal(root.get("id"), Long.valueOf(keyword));
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(idPredicate, snPredicate, fullNamePredicate));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(snPredicate, fullNamePredicate));
            }
        }
        return criteriaQuery.where(restrictions);
    }

    public List<Product> findList(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop,
                                  Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Tenant tenant, Area area, Boolean periferal, OrderType orderType, Integer count, List<Filter> filters, List<Order> orders) {
        CriteriaQuery<Product> criteriaQuery = criteriaQuery(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, community, area, periferal, null, null);
        if (orderType == OrderType.priceAsc) {
            orders.add(Order.asc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.weight) {
            orders.add(Order.desc("priority"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.priceDesc) {
            orders.add(Order.desc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.salesDesc) {
            orders.add(Order.desc("sales"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.scoreDesc) {
            orders.add(Order.desc("score"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.dateDesc) {
            orders.add(Order.desc("createDate"));
        } else {
            orders.add(Order.desc("isTop"));
            orders.add(Order.desc("modifyDate"));
        }
        return super.findList(criteriaQuery, 0, count, filters, orders);
    }

    public Page<Product> findPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, List<Tag> unionTags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable,
                                  Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, OrderType orderType, String phonetic, String keyword, Pageable pageable) {
        CriteriaQuery<Product> criteriaQuery = criteriaQuery(productCategory, brand, promotion, tags, unionTags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, community, area, periferal, phonetic,
                keyword);
        List<Order> orders = pageable.getOrders();
        if (orderType == OrderType.priceAsc) {
            orders.add(Order.asc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.weight) {
            orders.add(Order.desc("priority"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.priceDesc) {
            orders.add(Order.desc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.salesDesc) {
            orders.add(Order.desc("sales"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.scoreDesc) {
            orders.add(Order.desc("score"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.dateDesc) {
            orders.add(Order.desc("createDate"));
        } else {
            orders.add(Order.desc("isTop"));
            orders.add(Order.desc("modifyDate"));
        }
        return super.findPage(criteriaQuery, pageable);
    }

    private CriteriaQuery<Product> criteriaQuery(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, List<Tag> unionTags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice,
                                                 Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, String phonetic, String keyword) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (productCategory != null) { // 鍟嗗搧鍒嗙被
            String categoryTreePath = ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR;
            Predicate categoryPredicate = criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory), criteriaBuilder.like(root.get("productCategory").<String>get("treePath"), "%" + categoryTreePath + "%"));
            restrictions = criteriaBuilder.and(restrictions, categoryPredicate);
        }
        if (brand != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("brand"), brand));
        }
        if (promotion != null) { // 淇冮攢淇℃伅
            Subquery<Product> promotionProductSubquery = criteriaQuery.subquery(Product.class);
            Root<Product> promotionProductSubqueryRoot = promotionProductSubquery.from(Product.class);
            promotionProductSubquery.select(promotionProductSubqueryRoot);
            promotionProductSubquery.where(criteriaBuilder.equal(promotionProductSubqueryRoot, root), criteriaBuilder.equal(promotionProductSubqueryRoot.join("promotionProducts").get("promotion"), promotion));

            Subquery<Product> productCategorySubquery = criteriaQuery.subquery(Product.class);
            Root<Product> productCategorySubqueryRoot = productCategorySubquery.from(Product.class);
            productCategorySubquery.select(productCategorySubqueryRoot);
            productCategorySubquery.where(criteriaBuilder.equal(productCategorySubqueryRoot, root), criteriaBuilder.equal(productCategorySubqueryRoot.join("productCategory").join("promotions"), promotion));

            Subquery<Product> brandSubQuery = criteriaQuery.subquery(Product.class);
            Root<Product> brandSubQueryRoot = brandSubQuery.from(Product.class);
            brandSubQuery.select(brandSubQueryRoot);
            brandSubQuery.where(criteriaBuilder.equal(brandSubQueryRoot, root), criteriaBuilder.equal(brandSubQueryRoot.join("brand").join("promotions"), promotion));

            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.exists(promotionProductSubquery), criteriaBuilder.exists(productCategorySubquery), criteriaBuilder.exists(brandSubQuery)));
        }
        if (tags != null && !tags.isEmpty()) { // 鏍囩
            Subquery<Product> tagsSubquery = criteriaQuery.subquery(Product.class);
            Root<Product> tagsSubqueryRoot = tagsSubquery.from(Product.class);
            tagsSubquery.select(tagsSubqueryRoot);
            tagsSubquery.where(criteriaBuilder.equal(tagsSubqueryRoot, root), tagsSubqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(tagsSubquery));
        }
        if (unionTags != null && !unionTags.isEmpty()) { // 鏍囩
            Subquery<Tenant> tagsSubquery = criteriaQuery.subquery(Tenant.class);
            Root<Tenant> tagsSubqueryRoot = tagsSubquery.from(Tenant.class);
            tagsSubquery.select(tagsSubqueryRoot);
            tagsSubquery.where(criteriaBuilder.equal(tagsSubqueryRoot, root.get("tenant")), tagsSubqueryRoot.join("unionTags").in(unionTags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(tagsSubquery));
        } else {
            Subquery<Tenant> subquery = criteriaQuery.subquery(Tenant.class);
            Root<Tenant> subqueryRoot = subquery.from(Tenant.class);
            subquery.select(subqueryRoot);
            subquery.where(criteriaBuilder.and(criteriaBuilder.equal(subqueryRoot, root.get("tenant")), criteriaBuilder.isNotEmpty(subqueryRoot.<Set<Tag>>get("unionTags"))));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        }
        if (attributeValue != null) { // 灞炴�
            for (Entry<Attribute, String> entry : attributeValue.entrySet()) {
                String propertyName = Product.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + entry.getKey().getPropertyIndex();
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get(propertyName), entry.getValue()));
            }
        }
        if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
            BigDecimal temp = startPrice;
            startPrice = endPrice;
            endPrice = temp;
        }
        if (startPrice != null && startPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.<Number>get("price"), startPrice));
        }
        if (endPrice != null && endPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Number>get("price"), endPrice));
        }
        if (isMarketable != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
        }
        if (isList != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), isList));
        }
        if (isTop != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isTop"), isTop));
        }
        if (isGift != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isGift"), isGift));
        }
        Path<Integer> stock = root.get("stock");
        Path<Integer> allocatedStock = root.get("allocatedStock");
        if (isOutOfStock != null) {
            if (isOutOfStock) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, allocatedStock));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, allocatedStock)));
            }
        }
        if (isStockAlert != null) {
            Setting setting = SettingUtils.get();
            if (isStockAlert) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount()))));
            }
        }

        if (community != null) { // 瀵绘壘鍛ㄨ竟鍟嗗
            List<Tenant> tenants = new ArrayList<Tenant>();
            List<DeliveryCenter> dlvs = new ArrayList<DeliveryCenter>();
            if ((periferal != null) && (periferal)) {
                dlvs = deliveryCenterDao.findList(area, community.getLocation(), new BigDecimal(6));
            } else {
                dlvs = deliveryCenterDao.findList(area, community);
            }
            for (Iterator<DeliveryCenter> it = dlvs.iterator(); it.hasNext(); ) {
                DeliveryCenter dc = it.next();
                tenants.add(dc.getTenant());
            }
            if (tenants.size() == 0) {
                return null;
            }
            restrictions = criteriaBuilder.and(restrictions, root.<Tenant>get("tenant").in(tenants));
        } else if (area != null) {
            Subquery<Product> areaSubquery = criteriaQuery.subquery(Product.class);
            Root<Product> tagsSubqueryRoot = areaSubquery.from(Product.class);
            areaSubquery.select(tagsSubqueryRoot);

            areaSubquery.where(criteriaBuilder.equal(tagsSubqueryRoot, root), criteriaBuilder.like(tagsSubqueryRoot.join("tenant").join("deliveryCenters").get("area").<String>get("treePath"), "%" + area.getTreePath() + "%"));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(areaSubquery));
        }

        if (StringUtils.isNotBlank(phonetic)) { // 鎷奸煶鏉′欢
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.<String>get("phonetic"), "%" + phonetic + "%"));
        }
        if (StringUtils.isNotBlank(keyword)) { // 鍏抽敭瀛�
            Predicate snPredicate = criteriaBuilder.like(root.<String>get("sn"), "%" + keyword + "%");
            Predicate fullNamePredicate = criteriaBuilder.like(root.<String>get("fullName"), "%" + keyword + "%");
            if (pattern.matcher(keyword).matches()) {
                Predicate idPredicate = criteriaBuilder.equal(root.get("id"), Long.valueOf(keyword));
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(idPredicate, snPredicate, fullNamePredicate));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(snPredicate, fullNamePredicate));
            }
        }
        return criteriaQuery.where(restrictions);
    }

    public Page<Product> findUnionPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, List<Tag> unionTags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable,
                                       Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, Location location, BigDecimal distatce, String phonetic, String keyword, OrderType orderType,
                                       Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (productCategory != null) {
            restrictions = criteriaBuilder.and(
                    restrictions,
                    criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory),
                            criteriaBuilder.like(root.get("productCategory").<String>get("treePath"), "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%")));
        }
        if (brand != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("brand"), brand));
        }
        if (promotion != null) {
            Subquery<Product> subquery1 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot1 = subquery1.from(Product.class);
            subquery1.select(subqueryRoot1);
            subquery1.where(criteriaBuilder.equal(subqueryRoot1, root), criteriaBuilder.equal(subqueryRoot1.join("promotions"), promotion));

            Subquery<Product> subquery2 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot2 = subquery2.from(Product.class);
            subquery2.select(subqueryRoot2);
            subquery2.where(criteriaBuilder.equal(subqueryRoot2, root), criteriaBuilder.equal(subqueryRoot2.join("productCategory").join("promotions"), promotion));

            Subquery<Product> subquery3 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot3 = subquery3.from(Product.class);
            subquery3.select(subqueryRoot3);
            subquery3.where(criteriaBuilder.equal(subqueryRoot3, root), criteriaBuilder.equal(subqueryRoot3.join("brand").join("promotions"), promotion));

            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.exists(subquery1), criteriaBuilder.exists(subquery2), criteriaBuilder.exists(subquery3)));
        }
        if (tags != null && !tags.isEmpty()) {
            Subquery<Product> subquery = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot = subquery.from(Product.class);
            subquery.select(subqueryRoot);
            subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        }
        if (unionTags != null && !unionTags.isEmpty()) {
            Subquery<Tenant> subquery = criteriaQuery.subquery(Tenant.class);
            Root<Tenant> subqueryRoot = subquery.from(Tenant.class);
            subquery.select(subqueryRoot);
            subquery.where(criteriaBuilder.equal(subqueryRoot, root.get("tenant")), subqueryRoot.join("unionTags").in(unionTags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        } else {
            Subquery<Tenant> subquery = criteriaQuery.subquery(Tenant.class);
            Root<Tenant> subqueryRoot = subquery.from(Tenant.class);
            subquery.select(subqueryRoot);
            subquery.where(criteriaBuilder.and(criteriaBuilder.equal(subqueryRoot, root.get("tenant")), criteriaBuilder.isNotEmpty(subqueryRoot.<Set<Tag>>get("unionTags"))));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        }
        if (attributeValue != null) {
            for (Entry<Attribute, String> entry : attributeValue.entrySet()) {
                String propertyName = Product.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + entry.getKey().getPropertyIndex();
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get(propertyName), entry.getValue()));
            }
        }
        if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
            BigDecimal temp = startPrice;
            startPrice = endPrice;
            endPrice = temp;
        }
        if (startPrice != null && startPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.<Number>get("price"), startPrice));
        }
        if (endPrice != null && endPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Number>get("price"), endPrice));
        }
        if (isMarketable != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
        }
        if (isList != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), isList));
        }
        if (isTop != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isTop"), isTop));
        }
        if (isGift != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isGift"), isGift));
        }
        Path<Integer> stock = root.get("stock");
        Path<Integer> allocatedStock = root.get("allocatedStock");
        if (isOutOfStock != null) {
            if (isOutOfStock) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, allocatedStock));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, allocatedStock)));
            }
        }
        if (isStockAlert != null) {
            Setting setting = SettingUtils.get();
            if (isStockAlert) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount()))));
            }
        }

        List<Tenant> tenants = new ArrayList<Tenant>();
        List<DeliveryCenter> dlvs = new ArrayList<DeliveryCenter>();
        if ((periferal != null) && periferal) {
            if ((location == null || !location.isExists()) && community != null) {
                dlvs = deliveryCenterDao.findList(area, community.getLocation(), new BigDecimal(6));
            } else if (location.isExists()) {
                if (distatce == null) {
                    distatce = new BigDecimal(6);
                }
                dlvs = deliveryCenterDao.findList(area, location, distatce);
            }
        } else {
            dlvs = deliveryCenterDao.findList(area, community);
        }

        for (Iterator<DeliveryCenter> it = dlvs.iterator(); it.hasNext(); ) {
            DeliveryCenter dc = it.next();
            tenants.add(dc.getTenant());
        }
        if (tenants.size() > 0) {
            restrictions = criteriaBuilder.and(restrictions, root.<Tenant>get("tenant").in(tenants));
        }
        if (StringUtils.isNotBlank(phonetic)) { // 拼音条件
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.<String>get("phonetic"), "%" + phonetic + "%"));
        }
        if (StringUtils.isNotBlank(keyword)) { // 关键字
            Predicate snPredicate = criteriaBuilder.like(root.<String>get("sn"), "%" + keyword + "%");
            Predicate fullNamePredicate = criteriaBuilder.like(root.<String>get("fullName"), "%" + keyword + "%");
            if (pattern.matcher(keyword).matches()) {
                Predicate idPredicate = criteriaBuilder.equal(root.get("id"), Long.valueOf(keyword));
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(idPredicate, snPredicate, fullNamePredicate));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(snPredicate, fullNamePredicate));
            }
        }
        criteriaQuery.where(restrictions);
        List<Order> orders = pageable.getOrders();
        if (orderType == OrderType.priceAsc) {
            orders.add(Order.asc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.weight) {
            orders.add(Order.desc("priority"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.priceDesc) {
            orders.add(Order.desc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.salesDesc) {
            orders.add(Order.desc("sales"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.scoreDesc) {
            orders.add(Order.desc("score"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.dateDesc) {
            orders.add(Order.desc("createDate"));
        } else {
            orders.add(Order.desc("isTop"));
            orders.add(Order.desc("modifyDate"));
        }
        return super.findPage(criteriaQuery, pageable);
    }

    public List<Product> productTenantSelect(String keyword, Long tenantId, Boolean isGift, int count) {
        if (StringUtils.isEmpty(keyword)) {
            return Collections.<Product>emptyList();
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenantId));
        if (pattern.matcher(keyword).matches()) {
            restrictions = criteriaBuilder.and(restrictions,
                    criteriaBuilder.or(criteriaBuilder.equal(root.get("id"), Long.valueOf(keyword)), criteriaBuilder.like(root.<String>get("sn"), "%" + keyword + "%"), criteriaBuilder.like(root.<String>get("fullName"), "%" + keyword + "%")));
        } else {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.like(root.<String>get("sn"), "%" + keyword + "%"), criteriaBuilder.like(root.<String>get("fullName"), "%" + keyword + "%")));
        }
        if (isGift != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isGift"), isGift));
        }
        criteriaQuery.where(restrictions);
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("isTop")));
        return super.findList(criteriaQuery, null, count, null, null);
    }

    public List<Product> findListByTag(Area area, List<Tag> tags, Integer count, List<Order> orders) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);

        Predicate restrictions = criteriaBuilder.conjunction();
        if (tags != null && !tags.isEmpty()) { // 标签
            Subquery<Product> tagsSubquery = criteriaQuery.subquery(Product.class);
            Root<Product> tagsSubqueryRoot = tagsSubquery.from(Product.class);
            tagsSubquery.select(tagsSubqueryRoot);
            tagsSubquery.where(criteriaBuilder.equal(tagsSubqueryRoot, root), tagsSubqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(tagsSubquery));
        }
        if (area != null) {

            Subquery<Product> areaSubquery = criteriaQuery.subquery(Product.class);
            Root<Product> tagsSubqueryRoot = areaSubquery.from(Product.class);
            areaSubquery.select(tagsSubqueryRoot);
            areaSubquery.where(
                    criteriaBuilder.equal(tagsSubqueryRoot, root),
                    criteriaBuilder.or(criteriaBuilder.equal(tagsSubqueryRoot.join("tenant").join("deliveryCenters").get("area"), area),
                            criteriaBuilder.like(tagsSubqueryRoot.join("tenant").join("deliveryCenters").get("area").<String>get("treePath"), "%" + Area.TREE_PATH_SEPARATOR + area.getId() + Area.TREE_PATH_SEPARATOR + "%")));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(areaSubquery));
        }

        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), true));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), true));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isGift"), false));
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery, null, count, null, orders);
    }

    public Page<Product> findListByTag(Area area, List<Tag> tags, Date beginDate, Date endDate, Pageable pageable) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);

        Predicate restrictions = criteriaBuilder.conjunction();
        if (tags != null && !tags.isEmpty()) { // 标签
            Subquery<Product> tagsSubquery = criteriaQuery.subquery(Product.class);
            Root<Product> tagsSubqueryRoot = tagsSubquery.from(Product.class);
            tagsSubquery.select(tagsSubqueryRoot);
            tagsSubquery.where(criteriaBuilder.equal(tagsSubqueryRoot, root), tagsSubqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(tagsSubquery));
        }
        if (area != null) {

            Subquery<Product> areaSubquery = criteriaQuery.subquery(Product.class);
            Root<Product> tagsSubqueryRoot = areaSubquery.from(Product.class);
            areaSubquery.select(tagsSubqueryRoot);
            areaSubquery.where(
                    criteriaBuilder.equal(tagsSubqueryRoot, root),
                    criteriaBuilder.or(criteriaBuilder.equal(tagsSubqueryRoot.join("tenant").join("deliveryCenters").get("area"), area),
                            criteriaBuilder.like(tagsSubqueryRoot.join("tenant").join("deliveryCenters").get("area").<String>get("treePath"), "%" + Area.TREE_PATH_SEPARATOR + area.getId() + Area.TREE_PATH_SEPARATOR + "%")));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(areaSubquery));
        }
        if (beginDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createDate"), beginDate));
        }
        if (endDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createDate"), endDate));
        }
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), true));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), true));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isGift"), false));
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    public Page<Product> findListByKeyword(Area area, String keyword, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);

        Predicate restrictions = criteriaBuilder.conjunction();
        if (area != null) {

            Subquery<Product> areaSubquery = criteriaQuery.subquery(Product.class);
            Root<Product> tagsSubqueryRoot = areaSubquery.from(Product.class);
            areaSubquery.select(tagsSubqueryRoot);
            areaSubquery.where(
                    criteriaBuilder.equal(tagsSubqueryRoot, root),
                    criteriaBuilder.or(criteriaBuilder.equal(tagsSubqueryRoot.join("tenant").join("deliveryCenters").get("area"), area),
                            criteriaBuilder.like(tagsSubqueryRoot.join("tenant").join("deliveryCenters").get("area").<String>get("treePath"), "%" + Area.TREE_PATH_SEPARATOR + area.getId() + Area.TREE_PATH_SEPARATOR + "%")));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(areaSubquery));
        }

        if (StringUtils.isNotBlank(keyword)) { // 关键字
            Predicate snPredicate = criteriaBuilder.like(root.<String>get("sn"), "%" + keyword + "%");
            Predicate fullNamePredicate = criteriaBuilder.like(root.<String>get("fullName"), "%" + keyword + "%");
            if (pattern.matcher(keyword).matches()) {
                Predicate idPredicate = criteriaBuilder.equal(root.get("id"), Long.valueOf(keyword));
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(idPredicate, snPredicate, fullNamePredicate));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(snPredicate, fullNamePredicate));
            }
        }

        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), true));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), true));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isGift"), false));
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    public List<Product> findListByTag(Area area, List<Tag> tags, Integer count, List<Order> orders, Date beginDate, Date endDate) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);

        Predicate restrictions = criteriaBuilder.conjunction();
        if (tags != null && !tags.isEmpty()) { // 标签
            Subquery<Product> tagsSubquery = criteriaQuery.subquery(Product.class);
            Root<Product> tagsSubqueryRoot = tagsSubquery.from(Product.class);
            tagsSubquery.select(tagsSubqueryRoot);
            tagsSubquery.where(criteriaBuilder.equal(tagsSubqueryRoot, root), tagsSubqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(tagsSubquery));
        }
        if (area != null) {

            Subquery<Product> areaSubquery = criteriaQuery.subquery(Product.class);
            Root<Product> tagsSubqueryRoot = areaSubquery.from(Product.class);
            areaSubquery.select(tagsSubqueryRoot);
            areaSubquery.where(
                    criteriaBuilder.equal(tagsSubqueryRoot, root),
                    criteriaBuilder.or(criteriaBuilder.equal(tagsSubqueryRoot.join("tenant").join("deliveryCenters").get("area"), area),
                            criteriaBuilder.like(tagsSubqueryRoot.join("tenant").join("deliveryCenters").get("area").<String>get("treePath"), "%" + Area.TREE_PATH_SEPARATOR + area.getId() + Area.TREE_PATH_SEPARATOR + "%")));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(areaSubquery));
        }

        if (beginDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createDate"), beginDate));
        }
        if (endDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createDate"), endDate));
        }
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), true));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), true));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isGift"), false));
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery, null, count, null, orders);
    }

    public Page<Product> findPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Date beginDate, Date endDate, Boolean isMarketable, Boolean isList, Boolean isTop,
                                  Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, Location location, BigDecimal distatce, OrderType orderType, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (productCategory != null) {
            restrictions = criteriaBuilder.and(
                    restrictions,
                    criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory),
                            criteriaBuilder.like(root.get("productCategory").<String>get("treePath"), "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%")));
        }
        if (brand != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("brand"), brand));
        }
        if (promotion != null) {
            Subquery<Product> subquery1 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot1 = subquery1.from(Product.class);
            subquery1.select(subqueryRoot1);
            subquery1.where(criteriaBuilder.equal(subqueryRoot1, root), criteriaBuilder.equal(subqueryRoot1.join("promotions"), promotion));

            Subquery<Product> subquery2 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot2 = subquery2.from(Product.class);
            subquery2.select(subqueryRoot2);
            subquery2.where(criteriaBuilder.equal(subqueryRoot2, root), criteriaBuilder.equal(subqueryRoot2.join("productCategory").join("promotions"), promotion));

            Subquery<Product> subquery3 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot3 = subquery3.from(Product.class);
            subquery3.select(subqueryRoot3);
            subquery3.where(criteriaBuilder.equal(subqueryRoot3, root), criteriaBuilder.equal(subqueryRoot3.join("brand").join("promotions"), promotion));

            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.exists(subquery1), criteriaBuilder.exists(subquery2), criteriaBuilder.exists(subquery3)));
        }
        if (tags != null && !tags.isEmpty()) {
            Subquery<Product> subquery = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot = subquery.from(Product.class);
            subquery.select(subqueryRoot);
            subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        }
        if (attributeValue != null) {
            for (Entry<Attribute, String> entry : attributeValue.entrySet()) {
                String propertyName = Product.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + entry.getKey().getPropertyIndex();
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get(propertyName), entry.getValue()));
            }
        }
        if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
            BigDecimal temp = startPrice;
            startPrice = endPrice;
            endPrice = temp;
        }
        if (startPrice != null && startPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.<Number>get("price"), startPrice));
        }
        if (endPrice != null && endPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Number>get("price"), endPrice));
        }
        if (beginDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createDate"), beginDate));
        }
        if (endDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createDate"), endDate));
        }
        if (isMarketable != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
        }
        if (isList != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), isList));
        }
        if (isTop != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isTop"), isTop));
        }
        if (isGift != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isGift"), isGift));
        }
        Path<Integer> stock = root.get("stock");
        Path<Integer> allocatedStock = root.get("allocatedStock");
        if (isOutOfStock != null) {
            if (isOutOfStock) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, allocatedStock));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, allocatedStock)));
            }
        }
        if (isStockAlert != null) {
            Setting setting = SettingUtils.get();
            if (isStockAlert) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount()))));
            }
        }

        List<Tenant> tenants = new ArrayList<Tenant>();
        List<DeliveryCenter> dlvs = new ArrayList<DeliveryCenter>();
        if ((periferal != null) && periferal) {
            if ((location == null || !location.isExists()) && community != null) {
                dlvs = deliveryCenterDao.findList(area, community.getLocation(), new BigDecimal(6));
            } else if (location != null) {
                if (distatce == null) {
                    distatce = new BigDecimal(6);
                }
                dlvs = deliveryCenterDao.findList(area, location, distatce);
            }
        } else {
            dlvs = deliveryCenterDao.findList(area, community);
        }

        for (Iterator<DeliveryCenter> it = dlvs.iterator(); it.hasNext(); ) {
            DeliveryCenter dc = it.next();
            tenants.add(dc.getTenant());
        }
        if (tenants.size() > 0) {
            restrictions = criteriaBuilder.and(restrictions, root.<Tenant>get("tenant").in(tenants));
        }
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.<Tenant>get("tenant").get("status"), Status.success));
        List<Order> orders = pageable.getOrders();
        if (orderType == OrderType.priceAsc) {
            orders.add(Order.asc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.priceDesc) {
            orders.add(Order.desc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.salesDesc) {
            orders.add(Order.desc("sales"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.scoreDesc) {
            orders.add(Order.desc("score"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.dateDesc) {
            orders.add(Order.desc("createDate"));
        } else {
            orders.add(Order.desc("isTop"));
            orders.add(Order.desc("modifyDate"));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    public Page<Product> findPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Date beginDate, Date endDate, Boolean isMarketable, Boolean isList, Boolean isTop,
                                  Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, Location location, BigDecimal distatce, OrderType orderType, List<Area> areaList, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (productCategory != null) {
            restrictions = criteriaBuilder.and(
                    restrictions,
                    criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory),
                            criteriaBuilder.like(root.get("productCategory").<String>get("treePath"), "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%")));
        }
        if (brand != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("brand"), brand));
        }
        if (areaList != null && areaList.size() > 0) {
            restrictions = criteriaBuilder.and(restrictions, root.get("tenant").get("area").in(areaList));
        }
        if (promotion != null) {
            Subquery<Product> subquery1 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot1 = subquery1.from(Product.class);
            subquery1.select(subqueryRoot1);
            subquery1.where(criteriaBuilder.equal(subqueryRoot1, root), criteriaBuilder.equal(subqueryRoot1.join("promotions"), promotion));

            Subquery<Product> subquery2 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot2 = subquery2.from(Product.class);
            subquery2.select(subqueryRoot2);
            subquery2.where(criteriaBuilder.equal(subqueryRoot2, root), criteriaBuilder.equal(subqueryRoot2.join("productCategory").join("promotions"), promotion));

            Subquery<Product> subquery3 = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot3 = subquery3.from(Product.class);
            subquery3.select(subqueryRoot3);
            subquery3.where(criteriaBuilder.equal(subqueryRoot3, root), criteriaBuilder.equal(subqueryRoot3.join("brand").join("promotions"), promotion));

            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.exists(subquery1), criteriaBuilder.exists(subquery2), criteriaBuilder.exists(subquery3)));
        }
        if (tags != null && !tags.isEmpty()) {
            Subquery<Product> subquery = criteriaQuery.subquery(Product.class);
            Root<Product> subqueryRoot = subquery.from(Product.class);
            subquery.select(subqueryRoot);
            subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        }
        if (attributeValue != null) {
            for (Entry<Attribute, String> entry : attributeValue.entrySet()) {
                String propertyName = Product.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + entry.getKey().getPropertyIndex();
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get(propertyName), entry.getValue()));
            }
        }
        if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
            BigDecimal temp = startPrice;
            startPrice = endPrice;
            endPrice = temp;
        }
        if (startPrice != null && startPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.<Number>get("price"), startPrice));
        }
        if (endPrice != null && endPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Number>get("price"), endPrice));
        }
        if (beginDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createDate"), beginDate));
        }
        if (endDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createDate"), endDate));
        }
        if (isMarketable != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
        }
        if (isList != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), isList));
        }
        if (isTop != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isTop"), isTop));
        }
        if (isGift != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isGift"), isGift));
        }
        Path<Integer> stock = root.get("stock");
        Path<Integer> allocatedStock = root.get("allocatedStock");
        if (isOutOfStock != null) {
            if (isOutOfStock) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, allocatedStock));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, allocatedStock)));
            }
        }
        if (isStockAlert != null) {
            Setting setting = SettingUtils.get();
            if (isStockAlert) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount()))));
            }
        }

        List<Tenant> tenants = new ArrayList<Tenant>();
        List<DeliveryCenter> dlvs = new ArrayList<DeliveryCenter>();
        if ((periferal != null) && periferal) {
            if ((location == null || !location.isExists()) && community != null) {
                dlvs = deliveryCenterDao.findList(area, community.getLocation(), new BigDecimal(6));
            } else if (location != null) {
                if (distatce == null) {
                    distatce = new BigDecimal(6);
                }
                dlvs = deliveryCenterDao.findList(area, location, distatce);
            }
        } else {
            dlvs = deliveryCenterDao.findList(area, community);
        }

        for (Iterator<DeliveryCenter> it = dlvs.iterator(); it.hasNext(); ) {
            DeliveryCenter dc = it.next();
            tenants.add(dc.getTenant());
        }
        if (tenants.size() > 0) {
            restrictions = criteriaBuilder.and(restrictions, root.<Tenant>get("tenant").in(tenants));
        }
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.<Tenant>get("tenant").get("status"), Status.success));
        List<Order> orders = pageable.getOrders();
        if (orderType == OrderType.priceAsc) {
            orders.add(Order.asc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.priceDesc) {
            orders.add(Order.desc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.salesDesc) {
            orders.add(Order.desc("sales"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.scoreDesc) {
            orders.add(Order.desc("score"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.dateDesc) {
            orders.add(Order.desc("createDate"));
        } else {
            orders.add(Order.desc("isTop"));
            orders.add(Order.desc("modifyDate"));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

	/**
	 * 查找商家内的商品分页--管理端
	 * @param pageable              分页查询和条件过滤(isGift,isOutOfStock,isStockAlert,isTop)
	 * @param area                  所属区域
	 * @param tenant                所属商家
	 * @param beginDate             开始时间
	 * @param endDate               结束时间
	 * @param productCategories     所属分类
	 * @param isMarketable          是否上架
	 * @param brand                 所属品牌
	 * @param promotion             促销活动
	 * @param tags                  标签 (推荐、新品、促销)
	 * @param keyword               根据关键字或者拼音码查询
	 * @param orderType             排序
	 * @return
	 */
	public Page<Product> openPage(Pageable pageable,                            //分页查询和条件过滤(isGift,isOutOfStock,isStockAlert,isTop)
			               Area area,                                    //所属区域
						   Tenant tenant,                                //所属商家
                           Date beginDate, Date endDate,                 //时间
						   Set<ProductCategory> productCategories,       //所属分类
						   Boolean isMarketable,                         //是否上架
                           Boolean isStockAlert,
                           Boolean isOutOfStock,
						   Brand brand,                                  //所属品牌
						   Promotion promotion,                          //促销活动
						   List<Tag> tags,                               //标签 (推荐、新品、促销)
						   String keyword,                               //根据关键字或者拼音码查询
						   OrderType orderType                           //排序
	) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (productCategories != null && productCategories.size() > 0) { // 商品分类
            Predicate categoryPredicate = criteriaBuilder.conjunction();
            Predicate categoryPredicate1 = null;
            for (ProductCategory productCategory : productCategories) {
                String categoryTreePath = ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR;
                if (categoryPredicate1 == null) {
                    categoryPredicate = criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory), criteriaBuilder.like(root.get("productCategory").<String>get("treePath"), "%" + categoryTreePath + "%"));
                    categoryPredicate1 = criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory), criteriaBuilder.like(root.get("productCategory").<String>get("treePath"), "%" + categoryTreePath + "%"));
                } else {
                    categoryPredicate = criteriaBuilder.or(categoryPredicate,
                            criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory), criteriaBuilder.like(root.get("productCategory").<String>get("treePath"), "%" + categoryTreePath + "%")));
                }
            }
            restrictions = criteriaBuilder.and(restrictions, categoryPredicate);
        }
        if (brand != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("brand"), brand));
        }
        if (promotion != null) { // 促销信息
            Subquery<Product> promotionProductSubquery = criteriaQuery.subquery(Product.class);
            Root<Product> promotionProductSubqueryRoot = promotionProductSubquery.from(Product.class);
            promotionProductSubquery.select(promotionProductSubqueryRoot);
            promotionProductSubquery.where(criteriaBuilder.equal(promotionProductSubqueryRoot, root), criteriaBuilder.equal(promotionProductSubqueryRoot.join("promotionProducts").get("promotion"), promotion));

            Subquery<Product> productCategorySubquery = criteriaQuery.subquery(Product.class);
            Root<Product> productCategorySubqueryRoot = productCategorySubquery.from(Product.class);
            productCategorySubquery.select(productCategorySubqueryRoot);
            productCategorySubquery.where(criteriaBuilder.equal(productCategorySubqueryRoot, root), criteriaBuilder.equal(productCategorySubqueryRoot.join("productCategory").join("promotions"), promotion));

            Subquery<Product> brandSubQuery = criteriaQuery.subquery(Product.class);
            Root<Product> brandSubQueryRoot = brandSubQuery.from(Product.class);
            brandSubQuery.select(brandSubQueryRoot);
            brandSubQuery.where(criteriaBuilder.equal(brandSubQueryRoot, root), criteriaBuilder.equal(brandSubQueryRoot.join("brand").join("promotions"), promotion));

            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.exists(promotionProductSubquery), criteriaBuilder.exists(productCategorySubquery), criteriaBuilder.exists(brandSubQuery)));
        }
        if (tags != null && !tags.isEmpty()) { // 标签
            Subquery<Product> tagsSubquery = criteriaQuery.subquery(Product.class);
            Root<Product> tagsSubqueryRoot = tagsSubquery.from(Product.class);
            tagsSubquery.select(tagsSubqueryRoot);
            tagsSubquery.where(criteriaBuilder.equal(tagsSubqueryRoot, root), tagsSubqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(tagsSubquery));
        }

        if (beginDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createDate"), beginDate));
        }
        if (endDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createDate"), endDate));
        }

        if (isMarketable != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
        }
        
        if (tenant!=null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
        } else {
        	if (area!=null) {
    			restrictions = criteriaBuilder.and(restrictions,
    					criteriaBuilder.or(criteriaBuilder.equal(root.get("tenant").get("area"), area), criteriaBuilder.like(root.get("tenant").get("area").<String> get("treePath"), "%" + Area.TREE_PATH_SEPARATOR + area.getId() + Area.TREE_PATH_SEPARATOR + "%")));
        	}
        }

        if (StringUtils.isNotBlank(keyword)) {
            restrictions = criteriaBuilder.and(
                    restrictions,
                    criteriaBuilder.or(
                            criteriaBuilder.like(root.<String>get("name"), "%" + keyword + "%"),
                            criteriaBuilder.like(root.<String>get("sn"), "%" + keyword + "%")
                    )
            );
        }


        Path<Integer> stock = root.get("stock");
        Path<Integer> allocatedStock = root.get("allocatedStock");
        if (isOutOfStock != null) {
            if (isOutOfStock) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, allocatedStock));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, allocatedStock)));
            }
        }
        if (isStockAlert != null) {
            Setting setting = SettingUtils.get();
            if (isStockAlert) {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount()))));
            }
        }

        criteriaQuery.where(restrictions);

        List<Order> orders = pageable.getOrders();
        if (orderType == OrderType.priceAsc) {
            orders.add(Order.asc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.weight) {
            orders.add(Order.desc("priority"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.priceDesc) {
            orders.add(Order.desc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.salesDesc) {
            orders.add(Order.desc("sales"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.scoreDesc) {
            orders.add(Order.desc("score"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.dateDesc) {
            orders.add(Order.desc("createDate"));
        } else {
            orders.add(Order.desc("isTop"));
            orders.add(Order.desc("modifyDate"));
        }
        return super.findPage(criteriaQuery,pageable);
		
	}

    /**
     * 查找商家内的商品分页
     *
     * @param pageable          分页查询和条件过滤(isGift,isOutOfStock,isStockAlert,isTop)
     * @param tenant            所属商家
     * @param productCategories 所属分类
     * @param isMarketable      是否上架
     * @param isList            是否展出
     * @param brand             所属品牌
     * @param promotion         促销活动
     * @param tags              标签 (推荐、新品、促销)
     * @param keyword           根据关键字或者拼音码查询
     * @param startPrice        起始价格
     * @param endPrice          结束价格
     * @param beginDate         开始时间
     * @param endDate           结束时间
     * @param attributeValue    商品属性
     * @param orderType         排序
     * @return
     */
    public Page<Product> openPage(Pageable pageable,                            //分页查询和条件过滤(isGift,isOutOfStock,isStockAlert,isTop)
                                  Tenant tenant,                                //所属商家
                                  Set<ProductCategory> productCategories,       //所属分类
                                  Boolean isMarketable,                         //是否上架
                                  Boolean isList,                               //是否展出
                                  Brand brand,                                  //所属品牌
                                  Promotion promotion,                          //促销活动
                                  List<Tag> tags,                               //标签 (推荐、新品、促销)
                                  String keyword,                               //根据关键字或者拼音码查询
                                  BigDecimal startPrice,                        //起始价格
                                  BigDecimal endPrice,                          //结束价格
                                  Date beginDate,                               //开始时间
                                  Date endDate,                                 //结束时间
                                  Map<Attribute, String> attributeValue,        //商品属性
                                  OrderType orderType                           //排序
    ) {
        CriteriaQuery<Product> criteriaQuery = criteriaQuery(null,tenant,productCategories,isMarketable,isList,brand,promotion,tags,keyword,startPrice,endPrice,beginDate,endDate,attributeValue,null);

        List<Order> orders = pageable.getOrders();
        if (orderType == OrderType.priceAsc) {
            orders.add(Order.asc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.weight) {
            orders.add(Order.desc("priority"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.priceDesc) {
            orders.add(Order.desc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.salesDesc) {
            orders.add(Order.desc("sales"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.scoreDesc) {
            orders.add(Order.desc("score"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.dateDesc) {
            orders.add(Order.desc("createDate"));
        } else {
            orders.add(Order.desc("isTop"));
            orders.add(Order.desc("modifyDate"));
        }
        return super.findPage(criteriaQuery,pageable);
    }

    /**
     * 查找区域内的商品分页
     *
     * @param pageable          分页查询和条件过滤(isGift,isOutOfStock,isStockAlert,isTop)
     * @param area              所属区域
     * @param productCategories 所属分类
     * @param brand             所属品牌
     * @param promotion         促销活动
     * @param tags              标签 (推荐、新品、促销)
     * @param keyword           根据关键字或者拼音码查询
     * @param startPrice        起始价格
     * @param endPrice          结束价格
     * @param beginDate         开始时间
     * @param endDate           结束时间
     * @param attributeValue    商品属性
     * @param orderType         排序
     * @return
     */
    public Page<Product> openPage(Pageable pageable,                           //分页查询和条件过滤(isGift,isOutOfStock,isStockAlert,isTop)
                                  Area area,                                   //所属区域
                                  Set<ProductCategory> productCategories,      //所属分类
                                  Brand brand,                                 //所属品牌
                                  Promotion promotion,                         //促销活动
                                  List<Tag> tags,                              //标签 (推荐、新品、促销)
                                  String keyword,                              //根据关键字或者拼音码查询
                                  BigDecimal startPrice,                       //起始价格
                                  BigDecimal endPrice,                         //结束价格
                                  Date beginDate,                              //开始时间
                                  Date endDate,                                //结束时间
                                  Map<Attribute, String> attributeValue,       //商品属性
                                  Community community,
                                  OrderType orderType                          //排序
    ) {
        CriteriaQuery<Product> criteriaQuery = criteriaQuery(area,null,productCategories,true,true,brand,promotion,tags,keyword,startPrice,endPrice,beginDate,endDate,attributeValue,community);
        List<Order> orders = pageable.getOrders();
        if (orderType == OrderType.priceAsc) {
            orders.add(Order.asc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.weight) {
            orders.add(Order.desc("priority"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.priceDesc) {

            orders.add(Order.desc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.salesDesc) {
            orders.add(Order.desc("sales"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.scoreDesc) {
            orders.add(Order.desc("score"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.dateDesc) {
            orders.add(Order.desc("createDate"));
        } else {
            orders.add(Order.desc("isTop"));
            orders.add(Order.desc("modifyDate"));
        }
        return super.findPage(criteriaQuery,pageable);
    }


    /**
     * 查找商家内的商品
     *
     * @param count             查询条数
     * @param tenant            所属商家
     * @param productCategories 所属分类
     * @param isMarketable      是否上架
     * @param isList            是否展出
     * @param brand             所属品牌
     * @param promotion         促销活动
     * @param tags              标签 (推荐、新品、促销)
     * @param keyword           根据关键字或者拼音码查询
     * @param startPrice        起始价格
     * @param endPrice          结束价格
     * @param beginDate         开始时间
     * @param endDate           结束时间
     * @param attributeValue    商品属性
     * @param filters           过滤条件(isGift,isOutOfStock,isStockAlert,isTop)
     * @param orderType         排序
     * @return
     */
    public List<Product> openList(Integer count,                               //查询条数
                                  Tenant tenant,                               //所属商家
                                  Set<ProductCategory> productCategories,      //所属分类
                                  Boolean isMarketable,                        //是否上架
                                  Boolean isList,                              //是否展出
                                  Brand brand,                                 //所属品牌
                                  Promotion promotion,                         //促销活动
                                  List<Tag> tags,                              //标签 (推荐、新品、促销)
                                  String keyword,                              //根据关键字或者拼音码查询
                                  BigDecimal startPrice,                       //起始价格
                                  BigDecimal endPrice,                         //结束价格
                                  Date beginDate,                              //开始时间
                                  Date endDate,                                //结束时间
                                  Map<Attribute, String> attributeValue,       //商品属性
                                  List<Filter> filters,                        //过滤条件(isGift,isOutOfStock,isStockAlert,isTop)
                                  OrderType orderType                          //排序
    ) {
        CriteriaQuery<Product> criteriaQuery = criteriaQuery(null,tenant,productCategories,isMarketable,isList,brand,promotion,tags,keyword,startPrice,endPrice,beginDate,endDate,attributeValue,null);
        return super.findList(criteriaQuery,null,count,filters,getOrder(orderType));
    }

    /**
     * 查找区域内的商品
     *
     * @param count             查询条数
     * @param area              所属商家
     * @param productCategories 所属分类
     * @param brand             所属品牌
     * @param promotion         促销活动
     * @param tags              标签 (推荐、新品、促销)
     * @param keyword           根据关键字或者拼音码查询
     * @param startPrice        起始价格
     * @param endPrice          结束价格
     * @param beginDate         开始时间
     * @param endDate           结束时间
     * @param attributeValue    商品属性
     * @param filters           过滤条件(isGift,isOutOfStock,isStockAlert,isTop)
     * @param orderType         排序
     * @return
     */
    public List<Product> openList(Integer count,                               //查询条数
                                  Area area,                                   //所属区域
                                  Set<ProductCategory> productCategories,      //所属分类
                                  Brand brand,                                 //所属品牌
                                  Promotion promotion,                         //促销活动
                                  List<Tag> tags,                              //标签 (推荐、新品、促销)
                                  String keyword,                              //根据关键字或者拼音码查询
                                  BigDecimal startPrice,                       //起始价格
                                  BigDecimal endPrice,                         //结束价格
                                  Date beginDate,                              //开始时间
                                  Date endDate,                                //结束时间
                                  Map<Attribute, String> attributeValue,       //商品属性
                                  List<Filter> filters,                        //过滤条件(isGift,isOutOfStock,isStockAlert,isTop)
                                  Community community,
                                  OrderType orderType                          //排序
    ) {
        CriteriaQuery<Product> criteriaQuery = criteriaQuery(area,null,productCategories,true,true,brand,promotion,tags,keyword,startPrice,endPrice,beginDate,endDate,attributeValue,community);
        return super.findList(criteriaQuery,null,count,filters,getOrder(orderType));
    }


    private CriteriaQuery<Product> criteriaQuery(Area area,
                                                 Tenant tenant,
                                                 Set<ProductCategory> productCategorys,
                                                 Boolean isMarketable,                        //是否上架
                                                 Boolean isList,                              //是否展出
                                                 Brand brand,
                                                 Promotion promotion,
                                                 List<Tag> tags,
                                                 String keyword,
                                                 BigDecimal startPrice,
                                                 BigDecimal endPrice,
                                                 Date beginDate,                              //开始时间
                                                 Date endDate,                              //结束时间
                                                 Map<Attribute, String> attributeValue,
                                                 Community community

    ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (productCategorys != null && productCategorys.size() > 0) { // 商品分类
            Predicate categoryPredicate = criteriaBuilder.conjunction();
            int idx = 0;
            for (ProductCategory productCategory : productCategorys) {
                String categoryTreePath = ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR;
                if (idx == 0) {
                    categoryPredicate = criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory), criteriaBuilder.like(root.get("productCategory").<String>get("treePath"), "%" + categoryTreePath + "%"));
                } else {
                    categoryPredicate = criteriaBuilder.or(categoryPredicate,
                            criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory), criteriaBuilder.like(root.get("productCategory").<String>get("treePath"), "%" + categoryTreePath + "%")));
                }
                idx = idx +1;
            }
            restrictions = criteriaBuilder.and(restrictions, categoryPredicate);
        }
        if (brand != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("brand"), brand));
        }
        if (promotion != null) { // 促销信息
            Subquery<Product> promotionProductSubquery = criteriaQuery.subquery(Product.class);
            Root<Product> promotionProductSubqueryRoot = promotionProductSubquery.from(Product.class);
            promotionProductSubquery.select(promotionProductSubqueryRoot);
            promotionProductSubquery.where(criteriaBuilder.equal(promotionProductSubqueryRoot, root), criteriaBuilder.equal(promotionProductSubqueryRoot.join("promotionProducts").get("promotion"), promotion));

            Subquery<Product> productCategorySubquery = criteriaQuery.subquery(Product.class);
            Root<Product> productCategorySubqueryRoot = productCategorySubquery.from(Product.class);
            productCategorySubquery.select(productCategorySubqueryRoot);
            productCategorySubquery.where(criteriaBuilder.equal(productCategorySubqueryRoot, root), criteriaBuilder.equal(productCategorySubqueryRoot.join("productCategory").join("promotions"), promotion));

            Subquery<Product> brandSubQuery = criteriaQuery.subquery(Product.class);
            Root<Product> brandSubQueryRoot = brandSubQuery.from(Product.class);
            brandSubQuery.select(brandSubQueryRoot);
            brandSubQuery.where(criteriaBuilder.equal(brandSubQueryRoot, root), criteriaBuilder.equal(brandSubQueryRoot.join("brand").join("promotions"), promotion));

            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.exists(promotionProductSubquery), criteriaBuilder.exists(productCategorySubquery), criteriaBuilder.exists(brandSubQuery)));
        }
        if (tags != null && !tags.isEmpty()) { // 标签
            Subquery<Product> tagsSubquery = criteriaQuery.subquery(Product.class);
            Root<Product> tagsSubqueryRoot = tagsSubquery.from(Product.class);
            tagsSubquery.select(tagsSubqueryRoot);
            tagsSubquery.where(criteriaBuilder.equal(tagsSubqueryRoot, root), tagsSubqueryRoot.join("tags").in(tags));
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(tagsSubquery));
        }
        if (attributeValue != null) { // 属性
            for (Entry<Attribute, String> entry : attributeValue.entrySet()) {
                String propertyName = Product.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + entry.getKey().getPropertyIndex();
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get(propertyName), entry.getValue()));
            }
        }
        if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
            BigDecimal temp = startPrice;
            startPrice = endPrice;
            endPrice = temp;
        }
        if (startPrice != null && startPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.<Number>get("price"), startPrice));
        }
        if (endPrice != null && endPrice.compareTo(new BigDecimal(0)) >= 0) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Number>get("price"), endPrice));
        }

        if (beginDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createDate"), beginDate));
        }
        if (endDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createDate"), endDate));
        }

        if (isMarketable != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
        }
        if (isList != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), isList));
        }
        
        
        if (tenant != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
        } else {
                Subquery<Tenant> subquery = criteriaQuery.subquery(Tenant.class);
                Root<Tenant> subqueryRoot = subquery.from(Tenant.class);
                subquery.select(subqueryRoot);
                
                Predicate subRestrictions = criteriaBuilder.conjunction();
                subRestrictions = criteriaBuilder.and(subRestrictions, criteriaBuilder.equal(subqueryRoot.get("status"),Status.success));

                if (area != null) {
            		Path<Area> areaQuary = subqueryRoot.join("deliveryCenters").get("area");
                    subRestrictions = criteriaBuilder.and(subRestrictions,
                    criteriaBuilder.or(criteriaBuilder.equal(areaQuary, area),
                                 criteriaBuilder.like(areaQuary.<String>get("treePath"), "%" + Area.TREE_PATH_SEPARATOR + area.getId() + Area.TREE_PATH_SEPARATOR + "%")));
                }
            if (community != null) {
                Path<Community> communityQuary = subqueryRoot.join("deliveryCenters").get("community");
                subRestrictions = criteriaBuilder.and(subRestrictions,
                        criteriaBuilder.equal(communityQuary, community));
            }
                subquery.where(criteriaBuilder.equal(subqueryRoot, root.get("tenant")), subRestrictions);
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
        }
        if (StringUtils.isNotBlank(keyword)) {
            restrictions = criteriaBuilder.and(
                    restrictions,
                    criteriaBuilder.or(
                            criteriaBuilder.like(root.<String>get("name"), "%" + keyword + "%"),
                            criteriaBuilder.like(root.<String>get("fullName"), "%" + keyword + "%"),
                            criteriaBuilder.like(root.<String>get("barcode"), "%" + keyword + "%"),
                            criteriaBuilder.like(root.<String>get("sn"), "%" + keyword + "%"),
                            criteriaBuilder.like(root.<String>get("seoKeywords"), "%" + keyword + "%")
                            
                    )
            );
        }

       // if (area != null) {
       //     Subquery<Product> areaSubquery = criteriaQuery.subquery(Product.class);
       //     Root<Product> tagsSubqueryRoot = areaSubquery.from(Product.class);
       //     areaSubquery.select(tagsSubqueryRoot);
       //     areaSubquery.where(
       //             criteriaBuilder.equal(tagsSubqueryRoot, root),
        //            criteriaBuilder.or(criteriaBuilder.equal(tagsSubqueryRoot.join("tenant").join("deliveryCenters").get("area"), area),
       //                     criteriaBuilder.like(tagsSubqueryRoot.join("tenant").join("deliveryCenters").get("area").<String>get("treePath"), "%" + Area.TREE_PATH_SEPARATOR + area.getId() + Area.TREE_PATH_SEPARATOR + "%")));
       //     restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(areaSubquery));
       // }

       // restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.<Tenant>get("tenant").get("status"), Status.success));
        return criteriaQuery.where(restrictions);
    }


    private  List<Order> getOrder(OrderType orderType){
        List<Order> orders =new ArrayList<>();
        if (orderType == OrderType.priceAsc) {
            orders.add(Order.asc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.weight) {
            orders.add(Order.desc("priority"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.priceDesc) {
            orders.add(Order.desc("price"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.salesDesc) {
            orders.add(Order.desc("sales"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.scoreDesc) {
            orders.add(Order.desc("score"));
            orders.add(Order.desc("createDate"));
        } else if (orderType == OrderType.dateDesc) {
            orders.add(Order.desc("createDate"));
        } else {
            orders.add(Order.desc("isTop"));
            orders.add(Order.desc("modifyDate"));
        }
        return orders;
    }
    
    public Page<Product> findSupplierPage(Tenant tenant,Date start_date,Date end_date,Tenant seller,String search_content,Pageable pageable){
    	CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if(start_date!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"),start_date));
		}
		if(end_date!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"),end_date));
		}
		if(seller!=null){
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"),seller));
		}
		if (StringUtils.isNotBlank(search_content)) { // 关键字
            restrictions = criteriaBuilder.and(restrictions,
                            criteriaBuilder.or(
                                    criteriaBuilder.like(root.<String>get("name"), "%" + search_content + "%"),
                                    criteriaBuilder.like(root.<String>get("fullName"), "%" + search_content + "%"),
                                    criteriaBuilder.like(root.<String>get("sn"), "%" + search_content + "%"),
                                    criteriaBuilder.like(root.<String>get("seoKeywords"), "%" + search_content + "%"),
                                    criteriaBuilder.like(root.<String>get("barcode"), "%" + search_content + "%")));
        }
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("supplier"),tenant));
		criteriaQuery.where(restrictions);
    	return super.findPage(criteriaQuery,pageable);
    }

    /**
     * 获取库存金额
     */
    public Map<String,BigDecimal> getStockAmount(Long tenantid){
        try {
            StringBuffer hsql = new StringBuffer("SELECT sum(product.cost*product.stock) from xx_product product WHERE product.tenant = :tenant");
            javax.persistence.Query query = entityManager.createNativeQuery(hsql.toString());
            query.setFlushMode(FlushModeType.COMMIT);
            query.setParameter("tenant", tenantid);
            List<?> data =  query.getResultList();
            Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
            if(data.size()>0){
                for(Object obj : data){
                    map.put("amount",(BigDecimal)(obj==null?BigDecimal.ZERO:obj));
                }
            }else {
                map.put("amount",BigDecimal.ZERO);
            }

            StringBuffer hsql1 = new StringBuffer("SELECT sum(product.cost*product.stock) from xx_product product WHERE product.supplier = :tenant");
            javax.persistence.Query query1 = entityManager.createNativeQuery(hsql1.toString());
            query1.setFlushMode(FlushModeType.COMMIT);
            query1.setParameter("tenant", tenantid);
            List<?> data1 =  query1.getResultList();
            if(data1.size()>0){
                for(Object obj : data1){
                    map.put("SuppilerAmount",(BigDecimal)(obj==null?BigDecimal.ZERO:obj));
                }
            }else {
                map.put("SuppilerAmount",BigDecimal.ZERO);
            }
            return map;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Page<Map<String,Object>> findMySharePage(Member member ,Tenant tenant, List<Tag> tags, OrderType orderType, String keyword, Pageable pageable) {
       if(keyword==null){
           keyword="";
       }
        String sql ="SELECT a.id,a.name,a.full_name,a.image,a.price,w.agency,d.volume,d.amount,d.times,d.ex_id,d.ex_type,d.salse_price  FROM xx_product a " +
                " LEFT JOIN " +
                " (SELECT b.id as ex_id,b.descr,b.member as ex_member,b.tenant as ex_tenant ,b.amount,b.times,b.volume,b.product,b.type as ex_type,b.salse_price from xx_extend_catalog b where b.member =:member and b.tenant=:tenant) d on a.id = d.product" +
                " left join xx_product_tag e on e.products = a.id" +
                " RIGHT JOIN xx_tag f on f.id = e.tags" +
                " LEFT JOIN  xx_tenant w on w.id = a.tenant"+
                " where f.name in('热销','新品','推荐') and a.tenant =:tenant and a.is_marketable = 1 and a.is_list = 1 and a.name LIKE :keyword GROUP  BY a.id";
        String totalSql = "SELECT COUNT(1) FROM (SELECT count(1) FROM xx_product a " +
                " LEFT JOIN " +
                " (SELECT b.id as ex_id,b.descr,b.member as ex_member,b.tenant as ex_tenant ,b.amount,b.times,b.volume,b.product,b.type as ex_type,b.salse_price from xx_extend_catalog b where b.member =:member and b.tenant=:tenant) d on a.id = d.product" +
                " left join xx_product_tag e on e.products = a.id" +
                " RIGHT JOIN xx_tag f on f.id = e.tags" +
                " LEFT JOIN  xx_tenant w on w.id = a.tenant"+
                " where f.name in('热销','新品','推荐') and a.tenant =:tenant and a.is_marketable = 1 and a.is_list = 1 and a.name LIKE :keyword GROUP  BY a.id) s";
        javax.persistence.Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant.getId()).setParameter("member",member.getId()).setParameter("keyword","%"+keyword+"%");
        javax.persistence.Query totalQuery = entityManager.createNativeQuery(totalSql).setFlushMode(FlushModeType.COMMIT).setParameter("tenant", tenant.getId()).setParameter("member",member.getId()).setParameter("keyword","%"+keyword+"%");
        query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        List list = new ArrayList();
        Long total = 0l;
        try {
            list = query.getResultList();
            total = totalQuery.getSingleResult().equals(0)?0:Long.parseLong(totalQuery.getSingleResult().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
        if (totalPages < pageable.getPageNumber()) {
            return new Page<Map<String, Object>>(Collections.<Map<String, Object>>emptyList(), total, pageable);
        }
        List<Map<String, Object>> maps = new ArrayList<>();
        if (list.size() > 0) {
            for (Object obj : list) {
                Map<String, Object> map = new HashMap<>();
                Object[] row = (Object[]) obj;
                map.put("id", row[0]);
                map.put("name", row[1]);
                map.put("fullName", row[2]);
                map.put("thumbnail", row[3]);
                map.put("price", row[4]);
                map.put("rate", row[5]);
                map.put("volume", row[6]);
                map.put("earnedAmount", row[7]);
                map.put("shareTimes", row[8]);
                map.put("shareId", row[9]);
                map.put("isRecommended", row[10]);
                map.put("salsePrice", row[11]);
                maps.add(map);
            }
        }
        return new Page<Map<String, Object>>(maps, total, pageable);
    }

    private CriteriaQuery<Product> criteriaQuery(List<SellCatalog> sellCatalogs){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        criteriaQuery.select(root);
        Predicate  sellCatalogPredicate =null ;
        if (sellCatalogs != null && !sellCatalogs.isEmpty()) { // 标签
            for (SellCatalog sellCatalog : sellCatalogs) {
                sellCatalogPredicate = criteriaBuilder.and(criteriaBuilder.equal(root.get("id"), sellCatalog.getProduct()));
                return criteriaQuery.where(sellCatalogPredicate);
            }
        }
        return criteriaQuery.where(sellCatalogPredicate);
    }
    public Page<Product> findSellCatalogPage(List<SellCatalog> sellCatalogs, Pageable pageable){
        CriteriaQuery<Product> criteriaQuery = criteriaQuery(sellCatalogs);
        return super.findPage(criteriaQuery, pageable);
    }

    public Page<Map<String,Object>> searchPage(String keyword, Pageable pageable){
        String sql ="SELECT p.id,p.full_name,p.price,p.month_sales,i.thumbnail from xx_tenant t,xx_union u,xx_union_tenant s,xx_product p,xx_product_product_image i  where t.unions = u.id and u.type=0 and t.id=s.tenant and s.status=2 and p.id=i.product and t.id=p.tenant and p.is_list=1 and p.is_marketable=1 and p.is_gift=0 and p.name like '%"+keyword+"%' group by p.id";
        javax.persistence.Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT);
        javax.persistence.Query query1 = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT);
        query.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        List list = new ArrayList();
        List list1 = new ArrayList();
        Long total = 0l;
        try {
            list = query.getResultList();
            list1 = query1.getResultList();
            total = (long)list1.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int totalPages = (int) Math.ceil((double) total / (double) pageable.getPageSize());
        if (totalPages < pageable.getPageNumber()) {
            return new Page<Map<String, Object>>(Collections.<Map<String, Object>>emptyList(), total, pageable);
        }
        List<Map<String, Object>> maps = new ArrayList<>();
        if (list.size() > 0) {
            for (Object obj : list) {
                Map<String, Object> map = new HashMap<>();
                Object[] row = (Object[]) obj;
                map.put("id", row[0]);
                map.put("fullName", row[1]);
                map.put("price", row[2]);
                map.put("monthSales", row[3]);
                map.put("thumbnail", row[4]);
                maps.add(map);
            }
        }
        return new Page<Map<String, Object>>(maps, total, pageable);
    }

    public Long findByProduct(Long id){
        if (id == null) {
            return null;
        }
        String jpql = "select p.tenant from xx_product p where p.id=:id";
        try {
            Object tid = entityManager.createNativeQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("id", id).getSingleResult();
            return Long.parseLong(tid.toString());
        } catch (NoResultException e) {
            return null;
        }
    }
}