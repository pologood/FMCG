/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.wit.Filter;
import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Filter.Operator;
import net.wit.dao.AreaDao;
import net.wit.dao.ProductDao;
import net.wit.entity.*;
import net.wit.entity.Enterprise.EnterpriseType;
import net.wit.entity.Product.OrderType;
import net.wit.service.ProductService;
import net.wit.service.StaticService;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Service - 商品
 * @author rsico Team
 * @version 3.0
 */
@Service("productServiceImpl")
public class ProductServiceImpl extends BaseServiceImpl<Product, Long> implements ProductService, DisposableBean {

	/** 查看点击数时间 */
	private long viewHitsTime = System.currentTimeMillis();

	@Resource(name = "ehCacheManager")
	private CacheManager cacheManager;

	@Resource(name = "productDaoImpl")
	private ProductDao productDao;

	@Resource(name = "staticServiceImpl")
	private StaticService staticService;
	
	@Resource(name = "areaDaoImpl")
	private AreaDao areaDao;

	@Resource(name = "productDaoImpl")
	public void setBaseDao(ProductDao productDao) {
		super.setBaseDao(productDao);
	}

	@Transactional(readOnly = true)
	public boolean snExists(String sn) {
		return productDao.snExists(sn);
	}

	@Transactional(readOnly = true)
	public boolean snExists(String sn,Tenant tenant) {
		return productDao.snExists(sn,tenant);
	}

	@Transactional(readOnly = true)
	public Product findBySn(String sn) {
		return productDao.findBySn(sn);
	}

	@Transactional(readOnly = true)
	public List<Product> findByBarcode(Tenant tenant, String barcode) {
		return productDao.findByBarcode(tenant, barcode);
	}

	@Transactional(readOnly = true)
	public boolean snUnique(String previousSn, String currentSn) {
		if (StringUtils.equalsIgnoreCase(previousSn, currentSn)) {
			return true;
		} else {
			if (productDao.snExists(currentSn)) {
				return false;
			} else {
				return true;
			}
		}
	}

	@Transactional(readOnly = true)
	public List<Product> search(String keyword, Boolean isGift, Integer count) {
		return productDao.search(keyword, isGift, count);
	}

	@Transactional(readOnly = true)
	public List<Product> findList(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop,
			Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, Integer count, List<Filter> filters, List<Order> orders) {
		return productDao.findList(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, orderType, count, filters, orders);
	}

	@Transactional(readOnly = true)
	public List<Product> findList(Set<ProductCategory> productCategories, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList,
			Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Tenant tenant, Area area, Boolean periferal, OrderType orderType, Integer count, List<Filter> filters, List<Order> orders) {
		return productDao.findList(productCategories, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, community, tenant, area, periferal, orderType, count, filters, orders);
	}

	@Transactional(readOnly = true)
	@Cacheable("product")
	public List<Product> findList(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop,
			Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, Integer count, List<Filter> filters, List<Order> orders, String cacheRegion) {
		return productDao.findList(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, orderType, count, filters, orders);
	}

	@Transactional(readOnly = true)
	@Cacheable("product")
	public List<Product> findList(Set<ProductCategory> productCategories, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList,
			Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Tenant tenant, Area area, Boolean periferal, OrderType orderType, Integer count, List<Filter> filters, List<Order> orders, String cacheRegion) {
		return productDao.findList(productCategories, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, community, tenant, area, periferal, orderType, count, filters, orders);
	}

	@Transactional(readOnly = true)
	public List<Product> findList(ProductCategory productCategory, Date beginDate, Date endDate, Integer first, Integer count) {
		return productDao.findList(productCategory, beginDate, endDate, first, count);
	}

	@Transactional(readOnly = true)
	public List<Object[]> findSalesList(Date beginDate, Date endDate, Integer count) {
		return productDao.findSalesList(beginDate, endDate, count);
	}

	@Transactional(readOnly = true)
	public Page<Product> findPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop,
			Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, Location location, BigDecimal distatce, OrderType orderType, Pageable pageable) {
		return productDao.findPage(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, community, area, periferal, location, distatce, orderType, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Product> findPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop,
			Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, OrderType orderType, String phonetic, String keyword, Pageable pageable) {
		return productDao.findPage(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, community, area, periferal, orderType, phonetic, keyword, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Product> searchPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop,
			Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, OrderType orderType, String phonetic, String keyword, Pageable pageable) {
		return productDao.findPage(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, community, area, periferal, orderType, phonetic, keyword, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Product> findPageByChannel(Set<ProductCategory> productCategorys, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList,
			Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, OrderType orderType, String phonetic, String keyword, Pageable pageable) {
		return productDao
				.findPageByChannel(productCategorys, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, community, area, periferal, orderType, phonetic, keyword, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Product> findPage(Member member, Pageable pageable) {
		return productDao.findPage(member, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Product> findPage(Member member, ProductCategory productCategory, Brand brand, Pageable pageable) {
		return productDao.findPage(member, productCategory, brand, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Product> findPage(Member member, Integer days, ProductCategory productCategory, Pageable pageable) {
		return productDao.findPage(member, days, productCategory, pageable);
	}

	@Transactional(readOnly = true)
	public Page<Product> findMyPage(Tenant tenant,String keyword,ProductCategory productCategory, ProductCategoryTenant productCategoryTenant, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue,
			BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, Pageable pageable) {
		return productDao.findMyPage(tenant, productCategory, productCategoryTenant, keyword, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, null, orderType, pageable);
	}

	@Transactional(readOnly = true)
	public List<Product> findMyList(Tenant tenant, ProductCategory productCategory, ProductCategoryTenant productCategoryTenant, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice,
			BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert,Integer count, OrderType orderType) {
		return productDao.findMyList(tenant, productCategory, productCategoryTenant, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert,count, orderType);
	}

	@Transactional(readOnly = true)
	public Long count(Member favoriteMember, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert) {
		return productDao.count(favoriteMember, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert);
	}

	@Transactional(readOnly = true)
	public boolean isPurchased(Member member, Product product) {
		return productDao.isPurchased(member, product);
	}

	public long viewHits(Long id) {
		Ehcache cache = cacheManager.getEhcache(Product.HITS_CACHE_NAME);
		Element element = cache.get(id);
		Long hits;
		if (element != null) {
			hits = (Long) element.getObjectValue();
		} else {
			Product product = productDao.find(id);
			if (product == null) {
				return 0L;
			}
			hits = product.getHits();
		}
		hits++;
		cache.put(new Element(id, hits));
		long time = System.currentTimeMillis();
		if (time > viewHitsTime + Product.HITS_CACHE_INTERVAL) {
			viewHitsTime = time;
			updateHits();
			cache.removeAll();
		}
		return hits;
	}

	public void destroy() throws Exception {
		updateHits();
	}

	/**
	 * 更新点击数
	 */
	@SuppressWarnings("unchecked")
	private void updateHits() {
		Ehcache cache = cacheManager.getEhcache(Product.HITS_CACHE_NAME);
		List<Long> ids = cache.getKeys();
		for (Long id : ids) {
			Product product = productDao.find(id);
			if (product != null) {
				productDao.lock(product, LockModeType.PESSIMISTIC_WRITE);
				Element element = cache.get(id);
				long hits = (Long) element.getObjectValue();
				long increment = hits - product.getHits();
				Calendar nowCalendar = Calendar.getInstance();
				Calendar weekHitsCalendar = DateUtils.toCalendar(product.getWeekHitsDate());
				Calendar monthHitsCalendar = DateUtils.toCalendar(product.getMonthHitsDate());
				if (nowCalendar.get(Calendar.YEAR) != weekHitsCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.WEEK_OF_YEAR) > weekHitsCalendar.get(Calendar.WEEK_OF_YEAR)) {
					product.setWeekHits(increment);
				} else {
					product.setWeekHits(product.getWeekHits() + increment);
				}
				if (nowCalendar.get(Calendar.YEAR) != monthHitsCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.MONTH) > monthHitsCalendar.get(Calendar.MONTH)) {
					product.setMonthHits(increment);
				} else {
					product.setMonthHits(product.getMonthHits() + increment);
				}
				product.setHits(hits);
				product.setWeekHitsDate(new Date());
				product.setMonthHitsDate(new Date());
				productDao.merge(product);
			}
		}
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review", "consultation" }, allEntries = true)
	public void save(Product product) {
		Assert.notNull(product);

		super.save(product);
		productDao.flush();
		staticService.build(product);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review", "consultation" }, allEntries = true)
	public Product update(Product product) {
		Assert.notNull(product);

		Product pProduct = super.update(product);
		productDao.flush();
		staticService.build(pProduct);
		return pProduct;
	}

	@Override
	@Transactional
	@CacheEvict(value = { "product", "productCategory", "review", "consultation" }, allEntries = true)
	public Product update(Product product, String... ignoreProperties) {
		return super.update(product, ignoreProperties);
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
	public void delete(Product product) {
		if (product != null) {
			staticService.delete(product);
		}
		super.delete(product);
	}

	public Object findByUnion(Tenant tenant, Union union, OrderType datedesc, Pageable pageable) {
		return productDao.findByUnion(tenant, union, datedesc, pageable);
	}

	public Page<Product> search(String keyword, String phonetic, BigDecimal startPrice, BigDecimal endPrice, OrderType orderType, Pageable pageable) {
		return productDao.search(keyword, phonetic, startPrice, endPrice, orderType, pageable);
	}
	
	public Page<Product> search(String keyword,String phonetic, Member member, OrderType orderType, Pageable pageable) {
		return productDao.search(keyword,phonetic, member, orderType, pageable);
	}

	public Page<Product> mobileFindPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList,
			Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, Location location, BigDecimal distatce, OrderType orderType, Pageable pageable) {
		return productDao.mobileFindPage(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, community, area, periferal, location, distatce, orderType, pageable);
	}

	public Page<Product> searchPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop,
			Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, OrderType orderType, String phonetic, String keyword, Location location, BigDecimal distatce, Pageable pageable) {
		return productDao.findPage(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, community, area, periferal, orderType, phonetic, keyword, location,
				distatce, pageable);
	}

	public Page<Product> mobileSearchPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList,
			Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, OrderType orderType, String phonetic, String keyword, Location location, BigDecimal distatce, Pageable pageable) {
		return productDao.mobileFindPage(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, community, area, periferal, orderType, phonetic, keyword, location,
				distatce, pageable);
	}

	public List<Product> findList(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop,
			Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Tenant tenant, Area area, Boolean periferal, OrderType orderType, Integer count, List<Filter> filters, List<Order> orders) {
		return productDao.findList(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, community, tenant, area, periferal, orderType, count, filters, orders);
	}

	public Page<Product> findPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop,
			Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, Pageable pageable) {
		return productDao.findPage(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, true, true, null, false, null, null, null, null, null, null, null, orderType, pageable);
	}

	public Page<Product> searchPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, List<Tag> unionTags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable,
			Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, OrderType orderType, String phonetic, String keyword, Pageable pageable) {
		return productDao.findPage(productCategory, brand, promotion, tags, unionTags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, community, area, periferal, orderType, phonetic, keyword,
				pageable);
	}

	public Page<Product> findUnionPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, List<Tag> unionTags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable,
			Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, Location location, BigDecimal distatce, String phonetic, String keyword, OrderType orderType,
			Pageable pageable) {
		return productDao.findUnionPage(productCategory, brand, promotion, tags, unionTags, attributeValue, startPrice, endPrice, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, community, area, periferal, location, distatce, phonetic,
				keyword, orderType, pageable);
	}

	@Transactional(readOnly = true)
	public List<Product> productTenantSelect(String q, Long tenantId, Boolean b, int i) {
		return productDao.productTenantSelect(q, tenantId, b, i);
	}

	@Transactional(readOnly = true)
	public List<Product> findListByTag(Area area, List<Tag> tags, Integer count, List<Order> orders) {
		return productDao.findListByTag(area, tags, count, orders);
	}

	@Transactional(readOnly = true)
	public List<Product> findListByTag(Area area, List<Tag> tags, Integer count, List<Order> orders,Date beginDate,Date endDate) {
		return productDao.findListByTag(area, tags, count, orders,beginDate,endDate);
	}

	@Transactional(readOnly = true)
	public Page<Product> findListByTag(Area area, List<Tag> tags, Date beginDate,Date endDate,Pageable pageable) {
		return productDao.findListByTag(area, tags, beginDate,endDate,pageable);
	}

	@Transactional(readOnly = true)
	public Page<Product> findListByKeyword(Area area, String keyword,Pageable pageable) {
		return productDao.findListByKeyword(area, keyword,pageable);
	}

	@Transactional(readOnly = true)
	@Override
	public Page<Product> findPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Date beginDate, Date endDate,
			Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, Location location, BigDecimal distatce, OrderType orderType, Pageable pageable) {
		return productDao.findPage(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice,beginDate,endDate, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, community, area, periferal, location, distatce, orderType, pageable);
	}
	
	@Transactional(readOnly = true)
	@Override
	public Page<Product> findPage(Admin admin,Area areaSelect,ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Date beginDate, Date endDate,
			Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, Location location, BigDecimal distatce, OrderType orderType, Pageable pageable) {
		Enterprise enterprise=admin.getEnterprise();
		List<Product> products=new ArrayList<Product>();
		Page<Product> page=new Page<Product>(products,0,pageable);
		if(enterprise!=null){
			EnterpriseType enterprisetype=enterprise.getEnterprisetype();
			Area enterpriseArea=enterprise.getArea();
			List<Area> areasproxy=new ArrayList<Area>();
			this.findAllChildren(enterpriseArea,areasproxy);
			List<Area> areas=new ArrayList<Area>();
			this.findAllChildren(areaSelect,areas);
			List<Area> list=new ArrayList<Area>();
			if(areas!=null&&areas.size()>0){
				for (Area area2 : areas) {
					if(areasproxy.contains(area2)){
						list.add(area2);
					}
				}
			}else{
				list=areasproxy;
			}
			
			if(enterprisetype==EnterpriseType.proxy){
				List<Area> areaList=new ArrayList<Area>();
				this.findAllChildren(areaSelect,areaList);
				page=productDao.findPage(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, beginDate, endDate, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, community, area, periferal, location, distatce, orderType, areaList, pageable);
			}else if(enterprisetype==EnterpriseType.provinceproxy){
				if(list!=null&&list.size()>0){
					page=productDao.findPage(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, beginDate, endDate, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, community, area, periferal, location, distatce, orderType, list, pageable);
				}
			}else if(enterprisetype==EnterpriseType.cityproxy){
				if(list!=null&&list.size()>0){
					page=productDao.findPage(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, beginDate, endDate, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, community, area, periferal, location, distatce, orderType, list, pageable);
				}
			}else if(enterprisetype==EnterpriseType.countyproxy){
				if(list!=null&&list.size()>0){
					page=productDao.findPage(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, beginDate, endDate, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, community, area, periferal, location, distatce, orderType, list, pageable);
				}
			}else if(enterprisetype==EnterpriseType.personproxy){
				List<Tenant> tenantList=new ArrayList<Tenant>(enterprise.getTenants());
				if(tenantList!=null&&tenantList.size()>0){
					List<Filter> filters = pageable.getFilters();
					filters.add(new Filter("tenant", Operator.in, tenantList));
					page=productDao.findPage(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, beginDate, endDate, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, community, area, periferal, location, distatce, orderType, areas, pageable);
				}
			}else{
				return page;
			}
		}else{
			if(admin.getUsername().equals("admin")){
				List<Area> areaList=new ArrayList<Area>();
				//this.findAllChildren(areaSelect,areaList);
				if(areaSelect!=null){
					areaList=areaDao.findChildren(areaSelect, null);
				}
				page=productDao.findPage(productCategory, brand, promotion, tags, attributeValue, startPrice, endPrice, beginDate, endDate, isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, community, area, periferal, location, distatce, orderType, areaList, pageable);
			}else{
				return page;
			}
		}
		return page;
	}

	/**查找下属所有区域*/
	private void findAllChildren(Area area,List<Area> areas){
		if(area!=null){
			areas.add(area);
			List<Area> children=new ArrayList<Area>(area.getChildren());
			if(children!=null&&children.size()>0){
				for (Area area2 : children) {
					//areas.add(area2);
					findAllChildren(area2,areas);
				}
			}
		}
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
		return productDao.openPage(pageable, area, tenant, beginDate, endDate, productCategories, isMarketable,isStockAlert,isOutOfStock, brand, promotion, tags, keyword, orderType);
	}

	/**
	 * 查找商家内的商品分页
	 * @param pageable              分页查询和条件过滤(isGift,isOutOfStock,isStockAlert,isTop)
	 * @param tenant                所属商家
	 * @param productCategories     所属分类
	 * @param isMarketable          是否上架
	 * @param isList                是否展出
	 * @param brand                 所属品牌
	 * @param promotion             促销活动
	 * @param tags                  标签 (推荐、新品、促销)
	 * @param keyword               根据关键字或者拼音码查询
	 * @param startPrice            起始价格
	 * @param endPrice              结束价格
	 * @param beginDate             开始时间
	 * @param endDate               结束时间
	 * @param attributeValue        商品属性
	 * @param orderType             排序
	 * @return
	 */
	@Transactional(readOnly = true)
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
	){return productDao.openPage(pageable,tenant,productCategories,isMarketable,isList,brand,promotion,tags,keyword,startPrice,endPrice,beginDate,endDate,attributeValue,orderType);}

	/**
	 * 查找区域内的商品分页
	 * @param pageable              分页查询和条件过滤(isGift,isOutOfStock,isStockAlert,isTop)
	 * @param area                  所属区域
	 * @param productCategories     所属分类
	 * @param brand                 所属品牌
	 * @param promotion             促销活动
	 * @param tags                  标签 (推荐、新品、促销)
	 * @param keyword               根据关键字或者拼音码查询
	 * @param startPrice            起始价格
	 * @param endPrice              结束价格
	 * @param beginDate             开始时间
	 * @param endDate               结束时间
	 * @param attributeValue        商品属性
	 * @param orderType             排序
	 * @return
	 */
	@Transactional(readOnly = true)
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
								   Community community,                         //所属区域
						   OrderType orderType                          //排序
	){return productDao.openPage(pageable,area,productCategories,brand,promotion,tags,keyword,startPrice,endPrice,beginDate,endDate,attributeValue,community,orderType);}


	/**
	 * 查找商家内的商品
	 * @param count                 查询条数
	 * @param tenant                所属商家
	 * @param productCategories     所属分类
	 * @param isMarketable          是否上架
	 * @param isList                是否展出
	 * @param brand                 所属品牌
	 * @param promotion             促销活动
	 * @param tags                  标签 (推荐、新品、促销)
	 * @param keyword               根据关键字或者拼音码查询
	 * @param startPrice            起始价格
	 * @param endPrice              结束价格
	 * @param beginDate             开始时间
	 * @param endDate               结束时间
	 * @param attributeValue        商品属性
	 * @param filters               过滤条件(isGift,isOutOfStock,isStockAlert,isTop)
	 * @param orderType             排序
	 * @return
	 */
	@Transactional(readOnly = true)
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
	){
		return productDao.openList(count,tenant,productCategories,isMarketable,isList,brand,promotion,tags,keyword,startPrice,endPrice,beginDate,endDate,attributeValue,filters,orderType);
	}

	/**
	 * 查找区域内的商品
	 * @param count                 查询条数
	 * @param area                  所属商家
	 * @param productCategories     所属分类
	 * @param brand                 所属品牌
	 * @param promotion             促销活动
	 * @param tags                  标签 (推荐、新品、促销)
	 * @param keyword               根据关键字或者拼音码查询
	 * @param startPrice            起始价格
	 * @param endPrice              结束价格
	 * @param beginDate             开始时间
	 * @param endDate               结束时间
	 * @param attributeValue        商品属性
	 * @param filters               过滤条件(isGift,isOutOfStock,isStockAlert,isTop)
	 * @param orderType             排序
	 * @return
	 */
	@Transactional(readOnly = true)
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
								  Community community,                         //所属区域
						   OrderType orderType                          //排序
	){
		return productDao.openList(count,area,productCategories,brand,promotion,tags,keyword,startPrice,endPrice,beginDate,endDate,attributeValue,filters,community,orderType);
	}
	
	public Page<Product> findSupplierPage(Tenant tenant,Date start_date,Date end_date,Tenant seller,String search_content,Pageable pageable) {
		return productDao.findSupplierPage(tenant, start_date, end_date,seller,search_content, pageable);
	}


	public Map<String,BigDecimal> getStockAmount(Long tenantid){
		return productDao.getStockAmount(tenantid);
	};

	@Override
	public Page<Map<String,Object>> findMySharePage(Member member,Tenant tenant, List<Tag> tags, OrderType orderType, String keyword, Pageable pageable) {
		return productDao.findMySharePage(member,tenant,tags,orderType,keyword,pageable);
	}
	@Transactional(readOnly = true)
	public Page<Product> findSellCatalogPage(List<SellCatalog> sellCatalogs, Pageable pageable){
		return productDao.findSellCatalogPage(sellCatalogs,pageable);
	}

	public Page<Map<String,Object>> searchPage(String keyword, Pageable pageable){
		return productDao.searchPage(keyword,pageable);
	};

	public Long findByProduct(Long id){
		return productDao.findByProduct(id);
	}
}