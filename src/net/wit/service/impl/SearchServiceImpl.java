/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ArticleDao;
import net.wit.dao.DeliveryCenterDao;
import net.wit.dao.ProductDao;
import net.wit.dao.TenantDao;
import net.wit.entity.Area;
import net.wit.entity.Article;
import net.wit.entity.Brand;
import net.wit.entity.BrandSeries;
import net.wit.entity.Community;
import net.wit.entity.DeliveryCenter;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.Product.OrderType;
import net.wit.entity.ProductCategory;
import net.wit.entity.ProductCategoryTenant;
import net.wit.entity.Tenant;
import net.wit.entity.TenantCategory;
import net.wit.service.SearchService;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.util.NumericUtils;
import org.apache.lucene.util.Version;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * Service - 搜索
 * @author rsico Team
 * @version 3.0
 */
@Service("searchServiceImpl")
@Transactional
public class SearchServiceImpl implements SearchService {

	/** 模糊查询最小相似度 */
	private static final float FUZZY_QUERY_MINIMUM_SIMILARITY = 0.5F;

	@PersistenceContext
	protected EntityManager entityManager;

	@Resource(name = "articleDaoImpl")
	private ArticleDao articleDao;

	@Resource(name = "productDaoImpl")
	private ProductDao productDao;

	@Resource(name = "tenantDaoImpl")
	private TenantDao tenantDao;

	@Resource(name = "deliveryCenterDaoImpl")
	private DeliveryCenterDao deliveryCenterDao;

	public void index() {
		index(Article.class);
		index(Product.class);
		index(DeliveryCenter.class);
	}

	public void index(Class<?> type) {
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
		if (type == Article.class) {
			for (int i = 0; i < articleDao.count(); i += 20) {
				List<Article> articles = articleDao.findList(i, 20, null, null);
				for (Article article : articles) {
					fullTextEntityManager.index(article);
				}
				fullTextEntityManager.flushToIndexes();
				fullTextEntityManager.clear();
				articleDao.clear();
			}
		} else if (type == Product.class) {
			for (int i = 0; i < productDao.count(); i += 20) {
				List<Product> products = productDao.findList(null, null, null, i, 20);
				for (Product product : products) {
					fullTextEntityManager.index(product);
				}
				fullTextEntityManager.flushToIndexes();
				fullTextEntityManager.clear();
				productDao.clear();
			}
		} else if (type == DeliveryCenter.class) {
			for (int i = 0; i < deliveryCenterDao.count(); i += 20) {
				List<DeliveryCenter> deliveryCenters = deliveryCenterDao.findList(i, 20, null, null);
				for (DeliveryCenter deliveryCenter : deliveryCenters) {
					fullTextEntityManager.index(deliveryCenter);
				}
				fullTextEntityManager.flushToIndexes();
				fullTextEntityManager.clear();
				deliveryCenterDao.clear();
			}
		}
	}

	public void index(Article article) {
		if (article != null) {
			FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
			fullTextEntityManager.index(article);
		}
	}

	public void index(Product product) {
		if (product != null) {
			FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
			fullTextEntityManager.index(product);
		}
	}

	public void index(DeliveryCenter deliveryCenter) {
		if (deliveryCenter != null) {
			FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
			fullTextEntityManager.index(deliveryCenter);
		}
	}

	public void purge() {
		purge(Article.class);
		purge(Product.class);
		purge(DeliveryCenter.class);
	}

	public void purge(Class<?> type) {
		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
		if (type == Article.class) {
			fullTextEntityManager.purgeAll(Article.class);
		} else if (type == Product.class) {
			fullTextEntityManager.purgeAll(Product.class);
		} else if (type == DeliveryCenter.class) {
			fullTextEntityManager.purgeAll(DeliveryCenter.class);
		}
	}

	public void purge(Article article) {
		if (article != null) {
			FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
			fullTextEntityManager.purge(Article.class, article.getId());
		}
	}

	public void purge(Product product) {
		if (product != null) {
			FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
			fullTextEntityManager.purge(Product.class, product.getId());
		}
	}

	public void purge(Tenant tenant) {
		if (tenant != null) {
			FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
			fullTextEntityManager.purge(Tenant.class, tenant.getId());
		}
	}

	public void purge(DeliveryCenter deliveryCenter) {
		if (deliveryCenter != null) {
			FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
			fullTextEntityManager.purge(DeliveryCenter.class, deliveryCenter.getId());
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Page<Article> search(String keyword, Pageable pageable) {
		if (StringUtils.isEmpty(keyword)) {
			return new Page<Article>();
		}
		if (pageable == null) {
			pageable = new Pageable();
		}
		try {
			String text = QueryParser.escape(keyword);
			QueryParser titleParser = new QueryParser(Version.LUCENE_35, "title", new IKAnalyzer());
			titleParser.setDefaultOperator(QueryParser.AND_OPERATOR);
			Query titleQuery = titleParser.parse(text);
			FuzzyQuery titleFuzzyQuery = new FuzzyQuery(new Term("title", text), FUZZY_QUERY_MINIMUM_SIMILARITY);
			Query contentQuery = new TermQuery(new Term("content", text));
			Query isPublicationQuery = new TermQuery(new Term("isPublication", "true"));
			BooleanQuery textQuery = new BooleanQuery();
			BooleanQuery query = new BooleanQuery();
			textQuery.add(titleQuery, Occur.SHOULD);
			textQuery.add(titleFuzzyQuery, Occur.SHOULD);
			textQuery.add(contentQuery, Occur.SHOULD);
			query.add(isPublicationQuery, Occur.MUST);
			query.add(textQuery, Occur.MUST);
			FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
			FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(query, Article.class);
			fullTextQuery.setSort(new Sort(new SortField[] { new SortField("isTop", SortField.STRING, true), new SortField(null, SortField.SCORE), new SortField("createDate", SortField.LONG, true) }));
			fullTextQuery.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
			fullTextQuery.setMaxResults(pageable.getPageSize());
			return new Page<Article>(fullTextQuery.getResultList(), fullTextQuery.getResultSize(), pageable);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Page<Article>();
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Page<DeliveryCenter> search(String keyword, TenantCategory tenantCategory, Area area, Community community, Tenant.OrderType orderType,Tenant.Status status, Pageable pageable) {
		if (StringUtils.isEmpty(keyword)) {
			return new Page<DeliveryCenter>();
		}
		if (pageable == null) {
			pageable = new Pageable();
		}
		try {
			BooleanQuery query = new BooleanQuery();
			if (!StringUtils.isEmpty(keyword)){
				String text = QueryParser.escape(keyword);

				QueryParser descrParser = new QueryParser(Version.LUCENE_35, "descr", new IKAnalyzer());
				descrParser.setDefaultOperator(QueryParser.AND_OPERATOR);
				Query descrQuery = descrParser.parse(text);
				
				QueryParser nameParser = new QueryParser(Version.LUCENE_35, "fullname", new IKAnalyzer());
				nameParser.setDefaultOperator(QueryParser.AND_OPERATOR);
				Query nameQuery = nameParser.parse(text);
				FuzzyQuery nameFuzzyQuery = new FuzzyQuery(new Term("fullName", text), FUZZY_QUERY_MINIMUM_SIMILARITY);
				
				BooleanQuery textQuery = new BooleanQuery();
				
				textQuery.add(nameFuzzyQuery, Occur.SHOULD);
				textQuery.add(descrQuery, Occur.SHOULD);
				textQuery.add(nameQuery, Occur.SHOULD);
				query.add(textQuery, Occur.MUST);
			}
            if (status==null) {
			    TermQuery statusQuery = new TermQuery(new Term("status", "success"));
				query.add(statusQuery, Occur.MUST);
            } else
            {
			    TermQuery statusQuery = new TermQuery(new Term("status", status.toString()));
				query.add(statusQuery, Occur.MUST);
            }

			if (tenantCategory != null) {
				Term term = new Term("tenantCategory", TenantCategory.TREE_PATH_SEPARATOR + tenantCategory.getId() + TenantCategory.TREE_PATH_SEPARATOR);
				WildcardQuery wildcardquery = new WildcardQuery(term);
				query.add(wildcardquery, Occur.MUST);
			}
			if (area != null) {
			    TermQuery statusQuery = new TermQuery(new Term("areaId",NumericUtils.longToPrefixCoded(area.getId())));
				query.add(statusQuery, Occur.MUST);
			}
			if (community != null) {
			    TermQuery communityQuery = new TermQuery(new Term("communityId",NumericUtils.longToPrefixCoded(community.getId())));
				query.add(communityQuery, Occur.MUST);
			}

			FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
			FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(query, DeliveryCenter.class);
			SortField[] sortFields = null;

			if (orderType == Tenant.OrderType.hitsDesc) {
				sortFields = new SortField[] { new SortField("hits", SortField.INT, true), new SortField("createDate", SortField.LONG, true) };
			} else if (orderType == Tenant.OrderType.scoreDesc) {
				sortFields = new SortField[] { new SortField("avgScore", SortField.INT, true), new SortField("createDate", SortField.LONG, true) };
			} else if (orderType == Tenant.OrderType.dateDesc) {
				sortFields = new SortField[] { new SortField("createDate", SortField.LONG, true) };
			} else {
				sortFields = new SortField[] { new SortField(null, SortField.SCORE), new SortField("createDate", SortField.LONG, true) };
			}
			fullTextQuery.setSort(new Sort(sortFields));
			fullTextQuery.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
			fullTextQuery.setMaxResults(pageable.getPageSize());
			Page<DeliveryCenter> page = new Page<DeliveryCenter>(fullTextQuery.getResultList(), fullTextQuery.getResultSize(), pageable);
			return page;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Page<DeliveryCenter>();
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Page<Product> search(String keyword, ProductCategory productCategory, Brand brand, Area area, Community community, BigDecimal startPrice, BigDecimal endPrice, OrderType orderType, Pageable pageable) {
		if (pageable == null) {
			pageable = new Pageable();
		}
		try {
			BooleanQuery query = new BooleanQuery();
			BooleanQuery textQuery = new BooleanQuery();
			if (!StringUtils.isEmpty(keyword)) {
				
				String text = QueryParser.escape(keyword);
				
				TermQuery snQuery = new TermQuery(new Term("sn", text));
				TermQuery barcodeQuery = new TermQuery(new Term("barcode", text));
				
				QueryParser nameParser = new QueryParser(Version.LUCENE_35, "fullName", new IKAnalyzer());
				nameParser.setDefaultOperator(QueryParser.AND_OPERATOR);
				Query nameQuery = nameParser.parse(text);
				FuzzyQuery nameFuzzyQuery = new FuzzyQuery(new Term("fullName", text), FUZZY_QUERY_MINIMUM_SIMILARITY);
				
				Query keywordQuery = new QueryParser(Version.LUCENE_35, "keyword", new IKAnalyzer()).parse(text);
				
				textQuery.add(snQuery, Occur.SHOULD);
				textQuery.add(keywordQuery, Occur.SHOULD);
				textQuery.add(nameQuery, Occur.SHOULD);
				textQuery.add(nameFuzzyQuery, Occur.SHOULD);
				textQuery.add(barcodeQuery, Occur.SHOULD);
				query.add(textQuery, Occur.MUST);
			}
			TermQuery tenantStatus = new TermQuery(new Term("status", "success"));
			TermQuery isMarketableQuery = new TermQuery(new Term("isMarketable", "true"));
			TermQuery isListQuery = new TermQuery(new Term("isList", "true"));
			TermQuery isGiftQuery = new TermQuery(new Term("isGift", "false"));
			query.add(tenantStatus,Occur.MUST);
			query.add(isMarketableQuery, Occur.MUST);
			query.add(isListQuery, Occur.MUST);
			query.add(isGiftQuery, Occur.MUST);

			if (brand != null) {
				TermQuery brandQuery = new TermQuery(new Term("brandId", NumericUtils.longToPrefixCoded(brand.getId())));
				query.add(brandQuery, Occur.MUST);
			}

			if (productCategory != null) {
				Term term = new Term("productCategoryIds", ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR);
				WildcardQuery wildcardquery = new WildcardQuery(term);
				query.add(wildcardquery, Occur.MUST);
			}

			if (area != null) {
				Area city = area.getCity();
				if (city!=null) {
					Term term = new Term("cityIds", "," + city.getId() + ",");
					WildcardQuery wildcardquery = new WildcardQuery(term);
					query.add(wildcardquery, Occur.MUST);
					if (!area.equals(city) ) {
						Term areaTerm = new Term("areaIds", "," + area.getId() + ",");
						WildcardQuery areaWildcardquery = new WildcardQuery(areaTerm);
						query.add(areaWildcardquery, Occur.MUST);
					}
				}
			}

			if (community != null) {
				Term term = new Term("communityIds", "," + community.getId().toString() + ",");
				WildcardQuery wildcardquery = new WildcardQuery(term);
				query.add(wildcardquery, Occur.MUST);
			}

			if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
				BigDecimal temp = startPrice;
				startPrice = endPrice;
				endPrice = temp;
			}
			if (startPrice != null && startPrice.compareTo(new BigDecimal(0)) >= 0 && endPrice != null && endPrice.compareTo(new BigDecimal(0)) >= 0) {
				NumericRangeQuery<Double> numericRangeQuery = NumericRangeQuery.newDoubleRange("price", startPrice.doubleValue(), endPrice.doubleValue(), true, true);
				query.add(numericRangeQuery, Occur.MUST);
			} else if (startPrice != null && startPrice.compareTo(new BigDecimal(0)) >= 0) {
				NumericRangeQuery<Double> numericRangeQuery = NumericRangeQuery.newDoubleRange("price", startPrice.doubleValue(), null, true, false);
				query.add(numericRangeQuery, Occur.MUST);
			} else if (endPrice != null && endPrice.compareTo(new BigDecimal(0)) >= 0) {
				NumericRangeQuery<Double> numericRangeQuery = NumericRangeQuery.newDoubleRange("price", null, endPrice.doubleValue(), false, true);
				query.add(numericRangeQuery, Occur.MUST);
			}
			FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
			FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(query, Product.class);
			SortField[] sortFields = null;
			if (orderType == OrderType.priceAsc) {
				sortFields = new SortField[] { new SortField("price", SortField.DOUBLE, false),new SortField("priority", SortField.LONG, true), new SortField("createDate", SortField.LONG, true) };
			} else if (orderType == OrderType.priceDesc) {
				sortFields = new SortField[] {new SortField("price", SortField.DOUBLE, true),new SortField("priority", SortField.LONG, true), new SortField("createDate", SortField.LONG, true) };
			} else if (orderType == OrderType.salesDesc) {
				sortFields = new SortField[] { new SortField("sales", SortField.INT, true),new SortField("priority", SortField.LONG, true), new SortField("createDate", SortField.LONG, true) };
			} else if (orderType == OrderType.scoreDesc) {
				sortFields = new SortField[] { new SortField("score", SortField.INT, true),new SortField("priority", SortField.LONG, true), new SortField("createDate", SortField.LONG, true) };
			} else if (orderType == OrderType.dateDesc) {
				sortFields = new SortField[] { new SortField("createDate", SortField.LONG, true),new SortField("priority", SortField.LONG, true) };
			} else {
				sortFields = new SortField[] {new SortField(null, SortField.SCORE),new SortField("priority", SortField.LONG, true), new SortField("isTop", SortField.STRING, true), new SortField(null, SortField.SCORE), new SortField("modifyDate", SortField.LONG, true) };
			}
			fullTextQuery.setSort(new Sort(sortFields));
			fullTextQuery.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
			fullTextQuery.setMaxResults(pageable.getPageSize());
			return new Page<Product>(fullTextQuery.getResultList(), fullTextQuery.getResultSize(), pageable);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Page<Product>();
	}

}