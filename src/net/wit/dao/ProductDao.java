/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.wit.Filter;
import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.*;
import net.wit.entity.Product.OrderType;

/**
 * Dao - 商品
 * @author rsico Team
 * @version 3.0
 */
public interface ProductDao extends BaseDao<Product, Long> {

	/**
	 * 商品信息查询
	 * @param productCategory 商品分类
	 * @param brand 品牌
	 * @param promotion 促销
	 * @param tags 标签
	 * @param attributeValue 属性值
	 * @param startPrice 最低价格
	 * @param endPrice 最高价格
	 * @param isMarketable 是否上架
	 * @param isList 是否列出
	 * @param isTop 是否置顶
	 * @param isGift 是否为赠品
	 * @param isOutOfStock 是否缺货
	 * @param isStockAlert 是否库存警告
	 * @param orderType 排序类型
	 * @param community 是否查询周边
	 * @param area 小区
	 * @param periferal 是否库存警告
	 * @param phonetic 商品拼音
	 * @param keyword 商品关键字(id,fullname,sn)
	 * @param orderType 排序类型
	 * @param first
	 * @param count
	 * @param filters
	 * @param orders
	 * @return
	 */
	public List<Product> findList(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop,
			Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, String phonetic, String keyword, OrderType orderType, Integer first, Integer count, List<Filter> filters, List<Order> orders);

	/**
	 * @see ProductDao#findList(ProductCategory, Brand, Promotion, List, Map, BigDecimal, BigDecimal, Boolean, Boolean, Boolean, Boolean, Boolean, Boolean,
	 *      Community, Area, Boolean, String, String, OrderType, Integer, Integer, List, List)
	 */
	public Page<Product> findPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop,
			Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, OrderType orderType, String phonetic, String keyword, Pageable pageable);

	public Page<Product> findPageByChannel(Set<ProductCategory> productCategorys, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList,
			Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, OrderType orderType, String phonetic, String keyword, Pageable pageable);

	/**
	 * 判断商品编号是否存在
	 * @param sn 商品编号(忽略大小写)
	 * @return 商品编号是否存在
	 */
	boolean snExists(String sn);

	boolean snExists(String sn,Tenant tenant);

	/**
	 * 根据商品编号查找商品
	 * @param sn 商品编号(忽略大小写)
	 * @return 商品，若不存在则返回null
	 */
	Product findBySn(String sn);

	/**
	 * 根据商品编号查找商品
	 * @param sn 商品编号(忽略大小写)
	 * @return 商品，若不存在则返回null
	 */
	List<Product> findByBarcode(Tenant tenant, String barcode);

	/**
	 * 通过ID、编号、全称查找商品
	 * @param keyword 关键词
	 * @param isGift 是否为赠品
	 * @param count 数量
	 * @return 商品
	 */
	List<Product> search(String keyword, Boolean isGift, Integer count);

	/**
	 * 查找商品
	 * @param productCategory 商品分类
	 * @param brand 品牌
	 * @param promotion 促销
	 * @param tags 标签
	 * @param attributeValue 属性值
	 * @param startPrice 最低价格
	 * @param endPrice 最高价格
	 * @param isMarketable 是否上架
	 * @param isList 是否列出
	 * @param isTop 是否置顶
	 * @param isGift 是否为赠品
	 * @param isOutOfStock 是否缺货
	 * @param isStockAlert 是否库存警告
	 * @param orderType 排序类型
	 * @param count 数量
	 * @param filters 筛选
	 * @param orders 排序
	 * @return 商品
	 */
	List<Product> findList(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop,
			Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, Integer count, List<Filter> filters, List<Order> orders);

	/**
	 * 查找商品
	 * @param productCategories 商品分类
	 * @param brand 品牌
	 * @param promotion 促销
	 * @param tags 标签
	 * @param attributeValue 属性值
	 * @param startPrice 最低价格
	 * @param endPrice 最高价格
	 * @param isMarketable 是否上架
	 * @param isList 是否列出
	 * @param isTop 是否置顶
	 * @param isGift 是否为赠品
	 * @param isOutOfStock 是否缺货
	 * @param isStockAlert 所属地区
	 * @param area 小区
	 * @param tenant 企业
	 * @param community 是否查询周边
	 * @param periferal 是否库存警告
	 * @param orderType 排序类型
	 * @param count 数量
	 * @param filters 筛选
	 * @param orders 排序
	 * @return 商品
	 */
	List<Product> findList(Set<ProductCategory> productCategories, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop,
			Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Tenant tenant, Area area, Boolean periferal, OrderType orderType, Integer count, List<Filter> filters, List<Order> orders);

	/**
	 * 查找已上架商品
	 * @param productCategory 商品分类
	 * @param beginDate 起始日期
	 * @param endDate 结束日期
	 * @param first 起始记录
	 * @param count 数量
	 * @return 已上架商品
	 */
	List<Product> findList(ProductCategory productCategory, Date beginDate, Date endDate, Integer first, Integer count);

	/**
	 * 查找商品
	 * @param goods 货品
	 * @param excludes 排除商品
	 * @return 商品
	 */
	List<Product> findList(Goods goods, Set<Product> excludes);

	/**
	 * 查找商品销售信息
	 * @param beginDate 起始日期
	 * @param endDate 结束日期
	 * @param count 数量
	 * @return 商品销售信息
	 */
	List<Object[]> findSalesList(Date beginDate, Date endDate, Integer count);

	/**
	 * 查找商品分页
	 * @param productCategory 商品分类
	 * @param brand 品牌
	 * @param promotion 促销
	 * @param tags 标签
	 * @param attributeValue 属性值
	 * @param startPrice 最低价格
	 * @param endPrice 最高价格
	 * @param isMarketable 是否上架
	 * @param isList 是否列出
	 * @param isTop 是否置顶
	 * @param isGift 是否为赠品
	 * @param isOutOfStock 是否缺货
	 * @param isStockAlert 是否库存警告
	 * @param orderType 排序类型
	 * @param pageable 分页信息
	 * @return 商品分页
	 */
	Page<Product> findPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop,
			Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, Pageable pageable);

	/**
	 * 查找商品分页
	 * @param productCategory 商品分类
	 * @param brand 品牌
	 * @param promotion 促销
	 * @param tags 标签
	 * @param attributeValue 属性值
	 * @param startPrice 最低价格
	 * @param endPrice 最高价格
	 * @param isMarketable 是否上架
	 * @param isList 是否列出
	 * @param isTop 是否置顶
	 * @param isGift 是否为赠品
	 * @param isOutOfStock 是否缺货
	 * @param isStockAlert 所属地区
	 * @param area 小区
	 * @param community 是否查询周边
	 * @param periferal 是否库存警告
	 * @param orderType 排序类型
	 * @param pageable 分页信息
	 * @return 商品分页
	 */
	Page<Product> findPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop,
			Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, Location location, BigDecimal distatce, OrderType orderType, Pageable pageable);

	/**
	 * 查找收藏商品分页
	 * @param member 会员
	 * @param pageable 分页信息
	 * @return 收藏商品分页
	 */
	Page<Product> findPage(Member member, Pageable pageable);

	/**
	 * 查找收藏商品分页(过滤条件:分类，品牌)
	 * @param member 会员
	 * @param pageable 分页信息
	 * @return 收藏商品分页
	 */
	Page<Product> findPage(Member member, ProductCategory productCategory, Brand brand, Pageable pageable);

	/**
	 * 查找我经营的商品
	 * @param member 会员
	 * @param productCategoryTenant 商铺商品分类
	 * @param keyword
	 * @param pageable 分页信息
	 * @return 收藏商品分页
	 */

	Page<Product> findMyPage(Tenant tenant, ProductCategory productCategory, ProductCategoryTenant productCategoryTenant, String keyword, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice,
			BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, String phonetic, OrderType orderType, Pageable pageable);

	/**
	 * 查询商品数量
	 * @param favoriteMember 收藏会员
	 * @param isMarketable 是否上架
	 * @param isList 是否列出
	 * @param isTop 是否置顶
	 * @param isGift 是否为赠品
	 * @param isOutOfStock 是否缺货
	 * @param isStockAlert 是否库存警告
	 * @return 商品数量
	 */
	Long count(Member favoriteMember, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert);

	/**
	 * 判断会员是否已购买该商品
	 * @param member 会员
	 * @param product 商品
	 * @return 是否已购买该商品
	 */
	boolean isPurchased(Member member, Product product);

	/**
	 * 会员近一段时间购买的商品
	 * @param member 会员
	 * @param days 天数
	 * @param pageable 分页信息
	 * @return 是否已购买该商品
	 */
	Page<Product> findPage(Member member, Integer days, ProductCategory productCategory, Pageable pageable);

	Page<Product> search(String keyword,String phonetic,Member member, OrderType orderType, Pageable pageable);
	
	Object findByUnion(Tenant tenant, Union union, OrderType datedesc, Pageable pageable);

	public Page<Product> search(String keyword, String phonetic, BigDecimal startPrice, BigDecimal endPrice, OrderType orderType, Pageable pageable);

	public Page<Product> mobileFindPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList,
			Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, Location location, BigDecimal distatce, OrderType orderType, Pageable pageable);

	public Page<Product> findPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop,
			Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, OrderType orderType, String phonetic, String keyword, Location location, BigDecimal distatce, Pageable pageable);

	public Page<Product> mobileFindPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList,
			Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, OrderType orderType, String phonetic, String keyword, Location location, BigDecimal distatce, Pageable pageable);

	public List<Product> findList(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop,
			Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Tenant tenant, Area area, Boolean periferal, OrderType orderType, Integer count, List<Filter> filters, List<Order> orders);

	public Page<Product> findPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, List<Tag> unionTags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable,
			Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, OrderType orderType, String phonetic, String keyword, Pageable pageable);

	public Page<Product> findUnionPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, List<Tag> unionTags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable,
			Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, Location location, BigDecimal distatce, String phonetic, String keyword, OrderType orderType,
			Pageable pageable);

	public List<Product> productTenantSelect(String q, Long tenantId, Boolean b, int i);

	public List<Product> findMyList(Tenant tenant, ProductCategory productCategory, ProductCategoryTenant productCategoryTenant, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice,
			BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert,Integer count, OrderType orderType);

	// 每日好货
	public List<Product> findListByTag(Area area, List<Tag> tags, Integer count, List<Order> orders);

	/** 每日新货*/
	List<Product> findListByTag(Area area, List<Tag> tags, Integer count, List<Order> orders,Date beginDate,Date endDate);

	Page<Product> findListByTag(Area area, List<Tag> tags,Date beginDate,Date endDate, Pageable pageable);

	Page<Product> findListByKeyword(Area area, String keyword, Pageable pageable);
	/**添加了起止时间筛选*/
	Page<Product> findPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice,Date beginDate, Date endDate, Boolean isMarketable, Boolean isList, Boolean isTop,
			Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, Location location, BigDecimal distatce, OrderType orderType, Pageable pageable);
	
	/**添加了areaList参数筛选*/
	Page<Product> findPage(ProductCategory productCategory, Brand brand, Promotion promotion, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice,Date beginDate, Date endDate, Boolean isMarketable, Boolean isList, Boolean isTop,
			Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Community community, Area area, Boolean periferal, Location location, BigDecimal distatce, OrderType orderType,List<Area> areaList, Pageable pageable);



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
	Page<Product> openPage(Pageable pageable,                            //分页查询和条件过滤(isGift,isOutOfStock,isStockAlert,isTop)
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
	);

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
	Page<Product> openPage(Pageable pageable,                            //分页查询和条件过滤(isGift,isOutOfStock,isStockAlert,isTop)
			               Area area,                                   //所属区域
						   Tenant tenant,                                //所属商家
                           Date beginDate, Date endDate,         //时间
						   Set<ProductCategory> productCategories,       //所属分类
						   Boolean isMarketable,                         //是否上架
						   Boolean isStockAlert,
						   Boolean isOutOfStock,
						   Brand brand,                                  //所属品牌
						   Promotion promotion,                          //促销活动
						   List<Tag> tags,                               //标签 (推荐、新品、促销)
						   String keyword,                               //根据关键字或者拼音码查询
						   OrderType orderType                           //排序
	);

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
	Page<Product> openPage(Pageable pageable,                           //分页查询和条件过滤(isGift,isOutOfStock,isStockAlert,isTop)
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
	);


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
	List<Product> openList(Integer count,                               //查询条数
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
	);

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
	List<Product> openList(Integer count,                               //查询条数
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
	);

	Page<Product> findSupplierPage(Tenant tenant,Date start_date,Date end_date,Tenant seller,String search_content,Pageable pageable);

	Map<String,BigDecimal> getStockAmount(Long tenantid);
	public Page<Product> findSellCatalogPage(List<SellCatalog> sellCatalogs, Pageable pageable);
	/**
	 * 查找我要分享的商品
	 *
	 * @param tenant                商铺
	 * @param keyword
	 * @param pageable              分页信息
	 * @return 收藏商品分页
	 */
	Page<Map<String,Object>> findMySharePage(Member member,Tenant tenant, List<Tag> tags, OrderType orderType, String keyword, Pageable pageable);

	Page<Map<String,Object>> searchPage(String keyword, Pageable pageable);

	Long findByProduct(Long id);
}