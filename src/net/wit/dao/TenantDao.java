package net.wit.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import net.wit.Filter;
import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.*;
import net.wit.entity.Tenant.Status;

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
 * @author liumx
 * @version 3.0 2013年7月1日16:37:52
 */

public interface TenantDao extends BaseDao<Tenant, Long> {
	/**
	 * 查找企业
	 * @param code 企业编码
	 * @return 企业
	 */
	Tenant findByCode(String code);

	/**
	 * 查找企业
	 * @param domain 企业绑定域名
	 * @return 企业
	 */
	Tenant findByDomain(String domain);

	Tenant findByTelephone(String telephone);

	/**
	 * 查找企业
	 * @param area 城市
	 * @param name 企业名称
	 * @param tag 标签
	 * @param count 数量
	 * @return 企业
	 */
	List<Tenant> findList(Area area, String name, Tag tag, Integer count);

	/**
	 * 查找企业
	 * @param tenantCategory 企业分类
	 * @param tags 标签
	 * @param count 数量
	 * @param filters 筛选
	 * @param orders 排序
	 * @return 企业
	 */
	List<Tenant> findList(TenantCategory tenantCategory, List<Tag> tags, Integer count, List<Filter> filters, List<Order> orders);

	/**
	 * 查找企业
	 * @param tenantCategory 企业分类
	 * @param tags 所属地区
	 * @param area 小区
	 * @param community 是否查询周边
	 * @param periferal 标签
	 * @param count 数量
	 * @param filters 筛选
	 * @param orders 排序
	 * @return 企业
	 */
	List<Tenant> findList(TenantCategory tenantCategory, List<Tag> tags, Area area, Community community, Boolean periferal, Integer count, List<Filter> filters, List<Order> orders);

	/**
	 * 查找企业
	 * @param tenantCategory 企业分类
	 * @param beginDate 起始日期
	 * @param endDate 结束日期
	 * @param first 起始记录
	 * @param count 数量
	 * @return 企业
	 */
	List<Tenant> findList(TenantCategory tenantCategory, Date beginDate, Date endDate, Integer first, Integer count);

	/**
	 * 查找企业分页
	 * @param tenantCategory 企业分类
	 * @param tags 所属地区
	 * @param area 小区
	 * @param community 是否查询周边
	 * @param periferal 标签
	 * @param pageable 分页信息
	 * @return 企业分页
	 */
	Page<Tenant> findPage(Set<TenantCategory> tenantCategorys, List<Tag> tags, Area area, Community community, Boolean periferal, Location location, BigDecimal distance, Pageable pageable);

	/**
	 * 查找企业分页
	 * @param tenantCategory 企业分类
	 * @param tags 标签
	 * @param pageable 分页信息
	 * @return 企业分页
	 */
	Page<Tenant> findPage(TenantCategory tenantCategory, List<Tag> tags, Pageable pageable);

    Page<Tenant> findPage(TenantCategory tenantCategory, Area area,Boolean isPromotion, Pageable pageable);
	/**
	 * 查找企业
	 * @param area 城市
	 * @param name 企业名称
	 * @param tags 标签
	 * @param count 数量
	 * @return 企业
	 */
	Page<Tenant> findPage(Area area, List<Tag> tags, Pageable pageable);

	Page<Tenant> findAgency(Member member, Status status, Pageable pageable);

	long count(Member member, Date startTime, Date endTime, Status status);

	List<ProductCategoryTenant> findRoots(Tenant tenant, Integer count);

	Page<Tenant> mobileFindPage(Set<TenantCategory> tenantCategorys, List<Tag> tags, Area area, Community community, Boolean periferal, Location location, BigDecimal distatce, Pageable pageable);

	Page<Tenant> findPage(Set<TenantCategory> tenantCategorys, List<Tag> tags, Area area, Community community, Boolean periferal, Pageable pageable);

	Page<Tenant> findPage(String keyword, TenantCategory tenantCategory, List<Tag> tags, Area area, ProductCategory productCategory, Brand brand, BrandSeries brandSeries, Pageable pageable);

	List<Tenant> findList(Set<TenantCategory> tenantCategorys, List<Tag> tags, Area area, Community community, Integer count);

	List<Tenant> tenantSelect(String q, Boolean b, int i);

	List<Tenant> findNewest(List<Tag> tags, Integer count);

	Page<Tenant> findPage(Member member, Pageable pageable);

	Page<Tenant> findPage(Status status, List<Tag> tags, Pageable pageable);

	/**
	 * @Title：findMemberFavorite
	 * @Description：会员收藏商铺
	 * @param member
	 * @param keyword
	 * @param count
	 * @return List<Tenant>
	 */
	List<Tenant> findMemberFavorite(Member member, String keyword, Integer count, List<Order> orders);
	
	/**
	 * @Title：统计关注我的会员
	 */
	Long countMyFavorite(Tenant tenant);
	
	public List<Tenant> findListByAreas(List<Area> areas);
	
	/**
	 * @Title：domainExists
	 * @Description：判断域名是否存在
	 * @param member
	 * @return boolean
	 */
	public boolean domainExists(String domain);
	
	/**
	 * 检测店铺名称的唯一性
	 * @param shortName 店铺名称
	 * @return boolean
	 */
	public boolean checkShortName(String shortName);

	boolean isOwner(Member member);
	Page<Tenant> findPage(Status status, List<Tag> tags,List<Area> areas,Date beginDate, Date endDate, Pageable pageable);
	List<Tenant> findList(Status status, List<Tag> tags, Date beginDate, Date endDate);
	/**
     * 分页查询商家-管理端
     *
     * @param pageable        用于分页、排序、过滤和查询关键字
     * @param area            区域
     * @param beginDate, endDate,         //时间
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
								 String marketableSize                 //商品上架数量,分隔符'%',例子：1%3 代表数量在1到3之间
    );
    
	/**
	 * 分页查询商家
	 * @param pageable 			用于分页、排序、过滤和查询关键字
	 * @param area				区域
	 * @param tenantCategorys	商家分类
	 * @param tags				标签筛选
	 * @param keyword			查询关键字
	 * @param location			定位坐标
	 * @param distatce			定位后根据距离搜索商家
	 * @param orderType			排序
	 * @return  商家分页
	 */
	Page<Tenant> openPage(Pageable pageable,                    //用于分页、排序、过滤和查询关键字
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
	);

	/**
	 * 返回固定条数的商家
	 * @param count				查询的条数
	 * @param area				区域
	 * @param tenantCategorys	商家分类
	 * @param tags				标签筛选
	 * @param keyword			查询关键字
	 * @param location			定位坐标
	 * @param distatce			定位后根据距离搜索商家
	 * @param filters	   		过滤
	 * @param orderType   		排序
	 * @return  商家集合
	 */
	List<Tenant> openList(Integer count,						//查询的条数
						  Area area,							//区域
						  Set<TenantCategory> tenantCategorys,	//商家分类
						  List<Tag> tags,						//标签筛选
						  String keyword,						//查询关键字
						  Location location,					//定位坐标
						  BigDecimal distatce,					//定位后根据距离搜索商家
						  List<Filter> filters,					//过滤
						  Tenant.OrderType orderType			//排序
	);
}
