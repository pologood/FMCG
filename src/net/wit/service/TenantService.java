package net.wit.service;

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
 * Title:接口类 - 企业
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
 * @version 1.0
 * @date 2013年7月2日15:46:16
 */

public interface TenantService extends BaseService<Tenant, Long> {

	/**
	 * 查找企业
	 * @param code 企业编码
	 * @return 企业
	 */
	Tenant findByCode(String code);

	/**
	 * 查找企业
	 * @param domain 企业编码
	 * @return 企业
	 */
	Tenant findByDomain(String domain);

	Tenant findByTelephone(String telephone);

	Tenant getCurrent();

	/**
	 * 查找企业
	 * @param area 城市
	 * @param name 企业名称
	 * @param tags 标签
	 * @param count 数量
	 * @return 企业
	 */
	List<Tenant> findList(Area area, String name, Tag tag, Integer count);

	/**
	 * 查找企业
	 * @param area 城市
	 * @param tags 标签
	 * @param pageable 分页数量
	 * @return 企业
	 */
	Page<Tenant> findPage(Area area, List<Tag> tags, Pageable pageable);

	/**
	 * 查找文章
	 * @param tenantCategory 文章分类
	 * @param tags 标签
	 * @param count 数量
	 * @param filters 筛选
	 * @param orders 排序
	 * @return 仅包含已发布文章
	 */
	List<Tenant> findList(TenantCategory tenantCategory, List<Tag> tags, Integer count, List<Filter> filters, List<Order> orders);

	/**
	 * 查找文章
	 * @param tenantCategory 文章分类
	 * @param tags 标签 所属地区
	 * @param area 小区
	 * @param community 是否查询周边
	 * @param periferal
	 * @param count 数量
	 * @param filters 筛选
	 * @param orders 排序
	 * @return 仅包含已发布文章
	 */
	List<Tenant> findList(TenantCategory tenantCategory, List<Tag> tags, Area area, Community community, Boolean periferal, Integer count, List<Filter> filters, List<Order> orders);

	/**
	 * 查找文章(缓存)
	 * @param tenantCategory 文章分类
	 * @param tags 标签 所属地区
	 * @param area 小区
	 * @param community 是否查询周边
	 * @param periferal
	 * @param count 数量
	 * @param filters 筛选
	 * @param orders 排序
	 * @param cacheRegion 缓存区域
	 * @return 仅包含已发布文章
	 */
	List<Tenant> findList(TenantCategory tenantCategory, List<Tag> tags, Area area, Community community, Boolean periferal, Integer count, List<Filter> filters, List<Order> orders, String cacheRegion);

	/**
	 * 查找文章(缓存)
	 * @param tenantCategory 文章分类
	 * @param tags 标签
	 * @param count 数量
	 * @param filters 筛选
	 * @param orders 排序
	 * @param cacheRegion 缓存区域
	 * @return 仅包含已发布文章
	 */
	List<Tenant> findList(TenantCategory tenantCategory, List<Tag> tags, Integer count, List<Filter> filters, List<Order> orders, String cacheRegion);

	/**
	 * 查找文章
	 * @param tenantCategory 文章分类
	 * @param beginDate 起始日期
	 * @param endDate 结束日期
	 * @param first 起始记录
	 * @param count 数量
	 * @return 仅包含已发布文章
	 */
	List<Tenant> findList(TenantCategory tenantCategory, Date beginDate, Date endDate, Integer first, Integer count);

	/**
	 * 查找文章分页
	 * @param tenantCategory 文章分类
	 * @param tags 标签
	 * @param pageable 分页信息
	 * @return 仅包含已发布文章
	 */
	Page<Tenant> findPage(TenantCategory tenantCategory, List<Tag> tags, Pageable pageable);
	
	Page<Tenant> findPage(TenantCategory tenantCategory, Area area,Boolean isPromotion, Pageable pageable);

	/**
	 * 查找文章分页
	 * @param tenantCategory 文章分类
	 * @param tags 标签 所属地区
	 * @param area 小区
	 * @param community 是否查询周边
	 * @param periferal
	 * @param pageable 分页信息
	 * @return 仅包含已发布文章
	 */
	Page<Tenant> findPage(Set<TenantCategory> tenantCategorys, List<Tag> tags, Area area, Community community, Boolean periferal, Location location, BigDecimal distatce, Pageable pageable);

	/**
	 * 支持商品找商家
	 */
	Page<Tenant> findPage(String keyword, TenantCategory tenantCategory, List<Tag> tags, Area area, ProductCategory productCategory, Brand brand, BrandSeries brandSeries, Pageable pageable);

	Page<Tenant> mobileFindPage(Set<TenantCategory> tenantCategorys, List<Tag> tags, Area area, Community community, Location location, BigDecimal distatce, Pageable pageable);

	/**
	 * 保存企业，更新member
	 * @param name
	 * @return
	 */
	public void save(Tenant tenant, Member member, Location location);

	/**
	 * 查找代理企业
	 * @param member 销售员
	 * @param status 状态
	 * @param pageable 分页
	 * @return 企业
	 */
	public Page<Tenant> findAgency(Member member, Status status, Pageable pageable);

	public long count(Member member, Date startTime, Date endTime, Status status);

	/**
	 * 查找顶级商家商品分类
	 * @return 顶级商家商品分类
	 */
	public List<ProductCategoryTenant> findRoots(Tenant tenant, Integer count);

	/**
	 * 计算商家距离经纬度的距离
	 * @param tenant
	 * @param location
	 * @return
	 */
	public BigDecimal calculateDistance(Tenant tenant, Location location);

	Page<Tenant> findPage(Set<TenantCategory> tenantCategorys, List<Tag> tags, Area area, Community community, Boolean periferal, Pageable pageable);

	List<Tenant> findList(Set<TenantCategory> tenantCategorys, List<Tag> tags, Area area, Community community, Integer count);

	List<Tenant> tenantSelect(String q, Boolean b, int i);

	List<Tenant> findNewest(List<Tag> tags, Integer count);

	/**
	 * 查找关注商家分页
	 * @param member 会员
	 * @param pageable 分页信息
	 * @return 收藏商品分页
	 */
	Page<Tenant> findPage(Member member, Pageable pageable);

	/**
	 * @Title：findPage
	 * @Description：
	 * @param status
	 * @param tag
	 * @param pageable
	 * @return Object
	 */
	Page<Tenant> findPage(Status status, List<Tag> tags, Pageable pageable);

	/**
	 * @Title：findMemberFavorite
	 * @Description：会员收藏的商铺
	 * @param member
	 * @param keyword
	 * @param object
	 * @return List<Tenant>
	 */
	List<Tenant> findMemberFavorite(Member member, String keyword, Integer count, List<Order> orders);

	/**
	 * @Title：统计关注我的会员
	 */
	Long countMyFavorite(Tenant tenant);
	
	/**
	 * @Title：viewHits
	 * @Description：获取点击数
	 * @param id
	 * @return long
	 */
	long viewHits(Long id);
	/**
	 * 根据登陆用户查找商家
	 * @param admin 登陆用户
	 * @param status 开通状态
	 * @param pageable 分页信息
	 * @return 商家分页
	 */
	Page<Tenant> findPage(Admin admin,Status status, Pageable pageable);
	/**
	 * 根据登陆用户查找商家
	 * @param admin 登陆用户
	 * @param status 开通状态
	 * @param tags 标签
	 * @param pageable 分页信息
	 * @return 商家分页
	 */
	Page<Tenant> findPage(Admin admin,Status status, List<Tag> tags, Pageable pageable);
	
	/**
	 * @Title：domainExists
	 * @Description：判断域名是否存在
	 * @param member
	 * @return boolean
	 */
	boolean domainExists(String domain);
	
	List<Tenant> findTenants(Area area);
	Page<Tenant> findPage(Admin admin,Status status, List<Tag> tags,Area area,Date beginDate, Date endDate, Pageable pageable);
	Page<Tenant> findPage(Admin admin,Status status, List<Tag> tags,Date beginDate, Date endDate, Pageable pageable);
	
	/**
	 * 检测店铺名称的唯一性
	 * @param shortName 店铺名称
	 * @return boolean
	 */
	public boolean checkShortName(String shortName);

	boolean isOwner(Member member);

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
								 String marketableSize                 //商品上架数量,分隔符',',例子：1,3 代表数量在1到3之间
    );
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
								 Tenant.OrderType orderType           //排序
	);

	/**
	 * 分页查询商家-前端
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
						  Tenant.OrderType orderType            //排序
	);

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
	 * 返回固定条数的商家-前端
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

	List<Tenant> findList(Status status, List<Tag> tags, Date beginDate, Date endDate);
}
