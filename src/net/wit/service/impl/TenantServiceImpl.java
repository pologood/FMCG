package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.LockModeType;
import javax.servlet.http.HttpServletRequest;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.wit.Filter;
import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Filter.Operator;
import net.wit.dao.AreaDao;
import net.wit.dao.DeliveryCenterDao;
import net.wit.dao.IdcardDao;
import net.wit.dao.MemberDao;
import net.wit.dao.ReceiverDao;
import net.wit.dao.TenantDao;
import net.wit.entity.*;
import net.wit.entity.Enterprise.EnterpriseType;
import net.wit.entity.Tenant.Status;
import net.wit.service.TenantService;
import net.wit.util.MapUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
 * @author liumx
 * @version 1.0
 * @date 2013年7月2日15:46:16
 */
@Service("tenantServiceImpl")
public class TenantServiceImpl extends BaseServiceImpl<Tenant, Long> implements TenantService {

    /**
     * 查看点击数时间
     */
    private long viewHitsTime = System.currentTimeMillis();

    @Resource(name = "ehCacheManager")
    private CacheManager cacheManager;

    @Resource(name = "tenantDaoImpl")
    private TenantDao tenantDao;

    @Resource(name = "memberDaoImpl")
    private MemberDao memberDao;

    @Resource(name = "idcardDaoImpl")
    private IdcardDao idcardDao;

    @Resource(name = "deliveryCenterDaoImpl")
    private DeliveryCenterDao deliveryCenterDao;

    @Resource(name = "receiverDaoImpl")
    private ReceiverDao receiverDao;

    @Resource(name = "tenantDaoImpl")
    public void setBaseDao(TenantDao tenantDao) {
        super.setBaseDao(tenantDao);
    }

    @Transactional(readOnly = true)
    public Tenant findByCode(String code) {
        return tenantDao.findByCode(code);
    }

    @Transactional(readOnly = true)
    public Tenant findByDomain(String domain) {
        return tenantDao.findByDomain(domain);
    }

    @Transactional(readOnly = true)
    public Tenant findByTelephone(String telephone) {
        return tenantDao.findByTelephone(telephone);
    }

    @Transactional(readOnly = true)
    public List<Tenant> findList(Area area, String name, Tag tag, Integer count) {
        return tenantDao.findList(area, name, tag, count);
    }

    @Transactional(readOnly = true)
    public List<Tenant> findList(TenantCategory tenantCategory, List<Tag> tags, Integer count, List<Filter> filters, List<Order> orders) {
        return tenantDao.findList(tenantCategory, tags, count, filters, orders);
    }

    @Transactional(readOnly = true)
    public List<Tenant> findList(TenantCategory tenantCategory, List<Tag> tags, Area area, Community community, Boolean periferal, Integer count, List<Filter> filters, List<Order> orders) {
        return tenantDao.findList(tenantCategory, tags, area, community, periferal, count, filters, orders);
    }

    @Transactional(readOnly = true)
    public List<Tenant> findList(TenantCategory tenantCategory, List<Tag> tags, Integer count, List<Filter> filters, List<Order> orders, String cacheRegion) {
        return tenantDao.findList(tenantCategory, tags, count, filters, orders);
    }

    @Transactional(readOnly = true)
    public List<Tenant> findList(TenantCategory tenantCategory, List<Tag> tags, Area area, Community community, Boolean periferal, Integer count, List<Filter> filters, List<Order> orders, String cacheRegion) {
        return tenantDao.findList(tenantCategory, tags, area, community, periferal, count, filters, orders);
    }

    @Transactional(readOnly = true)
    public List<Tenant> findList(TenantCategory tenantCategory, Date beginDate, Date endDate, Integer first, Integer count) {
        return tenantDao.findList(tenantCategory, beginDate, endDate, first, count);
    }

    @Transactional(readOnly = true)
    public Page<Tenant> findPage(TenantCategory tenantCategory, List<Tag> tags, Pageable pageable) {
        return tenantDao.findPage(tenantCategory, tags, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Tenant> findPage(Set<TenantCategory> tenantCategorys, List<Tag> tags, Area area, Community community, Boolean periferal, Location location, BigDecimal distatce, Pageable pageable) {
        return tenantDao.findPage(tenantCategorys, tags, area, community, periferal, location, distatce, pageable);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"tenant", "tenantCategory"}, allEntries = true)
    public void save(Tenant tenant) {
        Assert.notNull(tenant);

        super.save(tenant);
        tenantDao.flush();
    }

    @Override
    @Transactional
    @CacheEvict(value = {"tenant", "tenantCategory"}, allEntries = true)
    public Tenant update(Tenant tenant) {
        Assert.notNull(tenant);

        Tenant pTenant = super.update(tenant);
        tenantDao.flush();
        return pTenant;
    }

    @Override
    @Transactional
    @CacheEvict(value = {"tenant", "tenantCategory"}, allEntries = true)
    public Tenant update(Tenant tenant, String... ignoreProperties) {
        return super.update(tenant, ignoreProperties);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"tenant", "tenantCategory"}, allEntries = true)
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"tenant", "tenantCategory"}, allEntries = true)
    public void delete(Long... ids) {
        super.delete(ids);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"tenant", "tenantCategory"}, allEntries = true)
    public void delete(Tenant tenant) {
        super.delete(tenant);
    }

    @Transactional
    @CacheEvict(value = {"tenant", "tenantCategory", "deliverys"}, allEntries = true)
    public void save(Tenant tenant, Member member, Location location) {
        if (member.getIdcard() != null) {
            idcardDao.persist(member.getIdcard());
        }
        tenant.setMember(member);
        member.setTenant(tenant);
        tenantDao.persist(tenant);
        tenantDao.flush();
        memberDao.merge(member);
        DeliveryCenter deliveryCenter = deliveryCenterDao.findDefault(tenant);
        if (deliveryCenter == null && tenant.getArea() != null) {
            // 新增默认发货地址
            deliveryCenter = new DeliveryCenter();
            Set<DeliveryCenter> deliveryCenters = new HashSet<DeliveryCenter>();
            deliveryCenter.setName(tenant.getName());
            if (member.getName() != null) {
                deliveryCenter.setContact(member.getName());
            } else {
                deliveryCenter.setContact(member.getDisplayName());
            }

            deliveryCenter.setAreaName(tenant.getArea().getFullName());
            if (tenant.getAddress() == null) {
                deliveryCenter.setAddress(" ");
            } else {
                deliveryCenter.setAddress(tenant.getAddress());
            }
            if (tenant.getZipcode() == null) {
                deliveryCenter.setZipCode("000000");
            } else {
                deliveryCenter.setZipCode(tenant.getZipcode());
            }
            Long sn = tenant.getId() + 100000000;
            deliveryCenter.setSn(sn.toString() + "0001");
            deliveryCenter.setPhone(tenant.getTelephone());
            deliveryCenter.setMobile(tenant.getMember().getMobile());
            deliveryCenter.setIsDefault(true);
            deliveryCenter.setArea(tenant.getArea());
            deliveryCenter.setTenant(tenant);
            deliveryCenter.setScore(new Float(0));
            deliveryCenter.setScoreCount(new Long(0));
            deliveryCenter.setTotalScore(new Long(0));
            deliveryCenter.setLocation(location);
            deliveryCenters.add(deliveryCenter);

            tenant.setDeliveryCenters(deliveryCenters);
            deliveryCenterDao.persist(deliveryCenter);
            tenantDao.merge(tenant);
        }

        if ((member.getReceivers() == null || member.getReceivers().isEmpty()) && tenant.getAddress() != null) {
            Receiver receiver = new Receiver();
            receiver.setAddress(deliveryCenter.getAddress());
            receiver.setArea(deliveryCenter.getArea());
            receiver.setAreaName(deliveryCenter.getAreaName());
            receiver.setConsignee(tenant.getName());
            receiver.setIsDefault(true);
            receiver.setMember(member);
            receiver.setPhone(deliveryCenter.getPhone());
            receiver.setZipCode(deliveryCenter.getZipCode() == null ? deliveryCenter.getArea().getZipCode() : deliveryCenter.getZipCode());
            if (receiver.getZipCode() == null) {
                receiver.setZipCode("000000");
            }
            receiverDao.persist(receiver);
        }
    }

    @Transactional(readOnly = true)
    public Page<Tenant> findPage(Area area, List<Tag> tags, Pageable pageable) {
        return tenantDao.findPage(area, tags, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Tenant> findAgency(Member member, Status status, Pageable pageable) {
        return tenantDao.findAgency(member, status, pageable);
    }

    @Transactional(readOnly = true)
    public long count(Member member, Date startTime, Date endTime, Status status) {
        return tenantDao.count(member, startTime, endTime, status);
    }

    public List<ProductCategoryTenant> findRoots(Tenant tenant, Integer count) {
        return tenantDao.findRoots(tenant, count);
    }

    /**
     * 计算商家距离经纬度的距离
     *
     * @param tenant
     * @param location
     * @return
     */
    public BigDecimal calculateDistance(Tenant tenant, Location location) {
        DeliveryCenter deliveryCenter = deliveryCenterDao.findDefault(tenant);
        try {
            if (deliveryCenter != null && deliveryCenter.getLocation() != null) {
                double distance = MapUtils.getDistatce(location.getLat().doubleValue(), deliveryCenter.getLocation().getLat().doubleValue(), location.getLng().doubleValue(), deliveryCenter.getLocation().getLng().doubleValue());
                return new BigDecimal(distance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    public static double GetDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    public Page<Tenant> mobileFindPage(Set<TenantCategory> tenantCategorys, List<Tag> tags, Area area, Community community, Location location, BigDecimal distatce, Pageable pageable) {
        if (community == null) {
            return tenantDao.mobileFindPage(tenantCategorys, tags, area, community, false, location, distatce, pageable);
        } else {
            return tenantDao.mobileFindPage(tenantCategorys, tags, area, community, true, location, distatce, pageable);
        }
    }

    public Page<Tenant> findPage(Set<TenantCategory> tenantCategorys, List<Tag> tags, Area area, Community community, Boolean periferal, Pageable pageable) {
        return tenantDao.findPage(tenantCategorys, tags, area, community, periferal, pageable);
    }

    public List<Tenant> findList(Set<TenantCategory> tenantCategorys, List<Tag> tags, Area area, Community community, Integer count) {
        return tenantDao.findList(tenantCategorys, tags, area, community, count);
    }

    @Transactional(readOnly = true)
    public List<Tenant> tenantSelect(String q, Boolean b, int i) {
        return tenantDao.tenantSelect(q, b, i);
    }

    public List<Tenant> findNewest(List<Tag> tags, Integer count) {
        return tenantDao.findNewest(tags, count);
    }

    @Transactional(readOnly = true)
    public Page<Tenant> findPage(Member member, Pageable pageable) {
        return tenantDao.findPage(member, pageable);
    }

    /**
     * 获取当前商铺
     *
     * @return 获取当前商铺
     */
    public Tenant getCurrent() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String host = request.getServerName();
        String[] hosts = host.split("\\.");
        String b2b = host;
        Tenant tenant = null;
        if (hosts.length > 2) {
            b2b = hosts[hosts.length - 2] + "." + hosts[hosts.length - 1];
            String code = hosts[0];
            if (StringUtils.isNumeric(code)) {
                tenant = find(new Long(code));
            } else {
                tenant = findByDomain(b2b);
            }
        }
        return tenant;
    }

    @Transactional(readOnly = true)
    public Page<Tenant> findPage(Status status, List<Tag> tags, Pageable pageable) {
        return tenantDao.findPage(status, tags, pageable);
    }

    /**
     * 支持商品找商家
     */
    public Page<Tenant> findPage(String keyword, TenantCategory tenantCategory, List<Tag> tags, Area area, ProductCategory productCategory, Brand brand, BrandSeries brandSeries, Pageable pageable) {
        return tenantDao.findPage(keyword, tenantCategory, tags, area, productCategory, brand, brandSeries, pageable);

    }

    public List<Tenant> findMemberFavorite(Member member, String keyword, Integer count, List<Order> orders) {
        return tenantDao.findMemberFavorite(member, keyword, count, orders);
    }

    /**
     * @Title：统计关注我的会员
     */
    public Long countMyFavorite(Tenant tenant) {
        return tenantDao.countMyFavorite(tenant);
    }

    public long viewHits(Long id) {
        Ehcache cache = cacheManager.getEhcache(Tenant.HITS_CACHE_NAME);
        Element element = cache.get(id);
        Long hits;
        if (element != null) {
            hits = (Long) element.getObjectValue();
        } else {
            Tenant tenant = tenantDao.find(id);
            if (tenant == null) {
                return 0L;
            }
            hits = tenant.getHits();
        }
        hits++;
        cache.put(new Element(id, hits));
        long time = System.currentTimeMillis();
        if (time > viewHitsTime + Tenant.HITS_CACHE_INTERVAL) {
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
        Ehcache cache = cacheManager.getEhcache(Tenant.HITS_CACHE_NAME);
        List<Long> ids = cache.getKeys();
        for (Long id : ids) {
            Tenant tenant = tenantDao.find(id);
            if (tenant != null) {
                tenantDao.lock(tenant, LockModeType.PESSIMISTIC_WRITE);
                Element element = cache.get(id);
                long hits = (Long) element.getObjectValue();
                long increment = hits - tenant.getHits();
                Calendar nowCalendar = Calendar.getInstance();
                Calendar weekHitsCalendar = DateUtils.toCalendar(tenant.getWeekHitsDate());
                Calendar monthHitsCalendar = DateUtils.toCalendar(tenant.getMonthHitsDate());
                if (nowCalendar.get(Calendar.YEAR) != weekHitsCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.WEEK_OF_YEAR) > weekHitsCalendar.get(Calendar.WEEK_OF_YEAR)) {
                    tenant.setWeekHits(increment);
                } else {
                    tenant.setWeekHits(tenant.getWeekHits() + increment);
                }
                if (nowCalendar.get(Calendar.YEAR) != monthHitsCalendar.get(Calendar.YEAR) || nowCalendar.get(Calendar.MONTH) > monthHitsCalendar.get(Calendar.MONTH)) {
                    tenant.setMonthHits(increment);
                } else {
                    tenant.setMonthHits(tenant.getMonthHits() + increment);
                }
                tenant.setHits(hits);
                tenant.setWeekHitsDate(new Date());
                tenant.setMonthHitsDate(new Date());
                tenantDao.merge(tenant);
            }
        }
    }

    /**
     * 根据登陆用户查找商家
     */
    @Transactional(readOnly = true)
    @Override
    public Page<Tenant> findPage(Admin admin, Status status, Pageable pageable) {
        List<Filter> filters = pageable.getFilters();
        Enterprise enterprise = admin.getEnterprise();
        List<Tenant> tenantList = new ArrayList<Tenant>();
        Page<Tenant> page = new Page<Tenant>(tenantList, 0, pageable);
        if (enterprise != null) {
            EnterpriseType enterprisetype = enterprise.getEnterprisetype();

            Area area = enterprise.getArea();
            List<Area> areaList = new ArrayList<Area>();
            areaList.add(area);
            List<Area> list = findAllChildren(area, areaList);
            /*分类查找*/
            if (enterprisetype == EnterpriseType.proxy) {
                page = super.findPage(pageable);
            } else if (enterprisetype == EnterpriseType.provinceproxy) {
                filters.add(new Filter("area", Operator.in, list));
                page = super.findPage(pageable);
            } else if (enterprisetype == EnterpriseType.cityproxy) {
                filters.add(new Filter("area", Operator.in, list));
                page = super.findPage(pageable);
            } else if (enterprisetype == EnterpriseType.countyproxy) {
                filters.add(new Filter("area", Operator.in, list));
                page = super.findPage(pageable);
            } else if (enterprisetype == EnterpriseType.personproxy) {
                List<Tenant> tenants = new ArrayList<Tenant>(enterprise.getTenants());
                //System.out.println(tenants.size());
                if (tenants != null && tenants.size() > 0) {
                    List<String> listName = new ArrayList<String>();
                    List<String> listLinkman = new ArrayList<String>();
                    for (Tenant tenant : tenants) {
                        listName.add(tenant.getName());
                        listLinkman.add(tenant.getLinkman());
                    }
                    filters.add(new Filter("name", Operator.in, listName));
                    filters.add(new Filter("linkman", Operator.in, listLinkman));
                    page = super.findPage(pageable);
                }
            } else {

            }
            return page;
        } else {
            if (admin.getUsername().equals("admin")) {
                return super.findPage(pageable);
            } else {
                List<Tenant> tenants = new ArrayList<Tenant>();
                return new Page<Tenant>(tenants, 0, pageable);
            }
        }

    }

    /**
     * 区域代理查找下属所有区域
     */
    private List<Area> findAllChildren(Area area, List<Area> areaList) {
        if (area != null) {
            List<Area> children = new ArrayList<Area>(area.getChildren());
            ;
            if (children != null && children.size() > 0) {
                for (Area area2 : children) {
                    areaList.add(area2);
                    findAllChildren(area2, areaList);
                }
            }
        }
        return areaList;
    }

    public Page<Tenant> findPage(TenantCategory tenantCategory, Area area, Boolean isPromotion, Pageable pageable) {
        return tenantDao.findPage(tenantCategory, area, isPromotion, pageable);
    }

    /**
     * 根据登陆用户查找商家
     */
    public Page<Tenant> findPage(Admin admin, Status status, List<Tag> tags,
                                 Pageable pageable) {
        List<Tenant> tenantList = new ArrayList<Tenant>();
        Page<Tenant> page = new Page<Tenant>(tenantList, 0, pageable);

        List<Filter> filters = pageable.getFilters();
        Enterprise enterprise = admin.getEnterprise();
        if (enterprise != null) {
            EnterpriseType enterprisetype = enterprise.getEnterprisetype();

            Area area = enterprise.getArea();
            List<Area> areaList = new ArrayList<Area>();
            areaList.add(area);
            List<Area> list = findAllChildren(area, areaList);
            /*分类查找*/
            if (enterprisetype == EnterpriseType.proxy) {
                page = tenantDao.findPage(status, tags, pageable);
            } else if (enterprisetype == EnterpriseType.provinceproxy) {
                filters.add(new Filter("area", Operator.in, list));
                page = tenantDao.findPage(status, tags, pageable);
            } else if (enterprisetype == EnterpriseType.cityproxy) {
                filters.add(new Filter("area", Operator.in, list));
                page = tenantDao.findPage(status, tags, pageable);
            } else if (enterprisetype == EnterpriseType.countyproxy) {
                filters.add(new Filter("area", Operator.in, list));
                page = tenantDao.findPage(status, tags, pageable);
            } else if (enterprisetype == EnterpriseType.personproxy) {
                List<Tenant> tenants = new ArrayList<Tenant>(enterprise.getTenants());
                List<Member> ListMember = new ArrayList<Member>();
                List<String> listName = new ArrayList<String>();
                for (Tenant tenant : tenants) {
                    ListMember.add(tenant.getMember());
                    listName.add(tenant.getName());
                }
                pageable.getFilters().add(new Filter("name", Operator.in, listName));
                pageable.getFilters().add(new Filter("member", Operator.in, ListMember));
                page = tenantDao.findPage(status, tags, pageable);
            } else {
                return page;
            }
            return page;
        } else {
            if (admin.getUsername().equals("admin")) {
                return tenantDao.findPage(status, tags, pageable);
            } else {
                List<Tenant> tenants = new ArrayList<Tenant>();
                return new Page<Tenant>(tenants, 0, pageable);
            }
        }
    }

    public boolean domainExists(String domain) {
        return tenantDao.domainExists(domain);
    }

    @Override
    public List<Tenant> findTenants(Area area) {
        List<Area> areaList = new ArrayList<Area>();
        areaList.add(area);
        List<Area> list = findAllChildren(area, areaList);
        return tenantDao.findListByAreas(list);
    }

    @Override
    public Page<Tenant> findPage(Admin admin, Status status, List<Tag> tags,
                                 Area area, Date beginDate, Date endDate, Pageable pageable) {
        List<Tenant> tenantList = new ArrayList<Tenant>();
        Page<Tenant> page = new Page<Tenant>(tenantList, 0, pageable);

        List<Area> areaSelect = new ArrayList<Area>();
        List<Area> areas = new ArrayList<Area>();
        areas.add(area);
        List<Area> selectList = findAllChildren(area, areas);

        List<Filter> filters = pageable.getFilters();
        Enterprise enterprise = admin.getEnterprise();
        if (enterprise != null) {
            EnterpriseType enterprisetype = enterprise.getEnterprisetype();

            Area areaEnterprise = enterprise.getArea();
            List<Area> areaList = new ArrayList<Area>();
            areaList.add(areaEnterprise);
            List<Area> list = findAllChildren(areaEnterprise, areaList);

            for (Area area2 : list) {
                if (selectList.contains(area2)) {
                    areaSelect.add(area2);
                }
            }

			/*分类查找*/
            if (enterprisetype == EnterpriseType.proxy) {
                filters.add(new Filter("area", Operator.in, selectList));
                page = tenantDao.findPage(status, tags, null, beginDate, endDate, pageable);
            } else if (enterprisetype == EnterpriseType.provinceproxy) {
                if (areaSelect != null && areaSelect.size() > 0) {
                    filters.add(new Filter("area", Operator.in, areaSelect));
                    page = tenantDao.findPage(status, tags, null, beginDate, endDate, pageable);
                }
            } else if (enterprisetype == EnterpriseType.cityproxy) {
                if (areaSelect != null && areaSelect.size() > 0) {
                    filters.add(new Filter("area", Operator.in, areaSelect));
                    page = tenantDao.findPage(status, tags, null, beginDate, endDate, pageable);
                }
            } else if (enterprisetype == EnterpriseType.countyproxy) {
                if (areaSelect != null && areaSelect.size() > 0) {
                    filters.add(new Filter("area", Operator.in, areaSelect));
                    page = tenantDao.findPage(status, tags, null, beginDate, endDate, pageable);
                }
            } else if (enterprisetype == EnterpriseType.personproxy) {
                List<Tenant> tenants = new ArrayList<Tenant>(enterprise.getTenants());
                List<Member> ListMember = new ArrayList<Member>();
                List<String> listName = new ArrayList<String>();
                if (tenants != null && tenants.size() > 0) {
                    for (Tenant tenant : tenants) {
                        ListMember.add(tenant.getMember());
                        listName.add(tenant.getName());
                    }
                }
                pageable.getFilters().add(new Filter("name", Operator.in, listName));
                pageable.getFilters().add(new Filter("member", Operator.in, ListMember));
                page = tenantDao.findPage(status, tags, selectList, beginDate, endDate, pageable);
            } else {
                return page;
            }
            return page;
        } else {
            if (admin.getUsername().equals("admin")) {
                filters.add(new Filter("area", Operator.in, selectList));
                return tenantDao.findPage(status, tags, null, beginDate, endDate, pageable);
            } else {
                List<Tenant> tenants = new ArrayList<Tenant>();
                return new Page<Tenant>(tenants, 0, pageable);
            }
        }
    }

    @Override
    public Page<Tenant> findPage(Admin admin, Status status, List<Tag> tags,
                                 Date beginDate, Date endDate, Pageable pageable) {
        List<Tenant> tenantList = new ArrayList<Tenant>();
        Page<Tenant> page = new Page<Tenant>(tenantList, 0, pageable);

        List<Filter> filters = pageable.getFilters();
        Enterprise enterprise = admin.getEnterprise();
        if (enterprise != null) {
            EnterpriseType enterprisetype = enterprise.getEnterprisetype();

            Area area = enterprise.getArea();
            List<Area> areaList = new ArrayList<Area>();
            areaList.add(area);
            List<Area> list = findAllChildren(area, areaList);
            /*分类查找*/
            if (enterprisetype == EnterpriseType.proxy) {
                page = tenantDao.findPage(status, tags, null, beginDate, endDate, pageable);
            } else if (enterprisetype == EnterpriseType.provinceproxy) {
                filters.add(new Filter("area", Operator.in, list));
                page = tenantDao.findPage(status, tags, null, beginDate, endDate, pageable);
            } else if (enterprisetype == EnterpriseType.cityproxy) {
                filters.add(new Filter("area", Operator.in, list));
                page = tenantDao.findPage(status, tags, null, beginDate, endDate, pageable);
            } else if (enterprisetype == EnterpriseType.countyproxy) {
                filters.add(new Filter("area", Operator.in, list));
                page = tenantDao.findPage(status, tags, null, beginDate, endDate, pageable);
            } else if (enterprisetype == EnterpriseType.personproxy) {
                List<Tenant> tenants = new ArrayList<Tenant>(enterprise.getTenants());
                List<Member> ListMember = new ArrayList<Member>();
                List<String> listName = new ArrayList<String>();
                for (Tenant tenant : tenants) {
                    ListMember.add(tenant.getMember());
                    listName.add(tenant.getName());
                }
                pageable.getFilters().add(new Filter("name", Operator.in, listName));
                pageable.getFilters().add(new Filter("member", Operator.in, ListMember));
                page = tenantDao.findPage(status, tags, null, beginDate, endDate, pageable);
            } else {
                return page;
            }
            return page;
        } else {
            if (admin.getUsername().equals("admin")) {
                return tenantDao.findPage(status, tags, null, beginDate, endDate, pageable);
            } else {
                List<Tenant> tenants = new ArrayList<Tenant>();
                return new Page<Tenant>(tenants, 0, pageable);
            }
        }
    }

    public boolean checkShortName(String shortName) {
        return tenantDao.checkShortName(shortName);
    }
    public boolean isOwner(Member member) {
        return tenantDao.isOwner(member);
    }
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
                                 String marketableSize                 //String marketableSize:商品上架数量,分隔符'%',例子：1%3 代表数量在1到3之间
    )  {
    	return tenantDao.openPage(pageable, area, beginDate, endDate, tenantCategorys, tags, keyword, status, orderType,qrCodeStatus,marketableSize);
    }
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
                                 Tenant.OrderType orderType            //排序
    )  {
        return tenantDao.openPage(pageable, area, beginDate, endDate, tenantCategorys, tags, keyword, status, orderType,null,null);
    }

    public Page<Tenant> openPage(Pageable pageable,                    //用于分页、排序、过滤和查询关键字
                                 Area area,                            //区域
                                 Set<TenantCategory> tenantCategorys,  //商家分类
                                 List<Tag> tags,                       //标签筛选
                                 String keyword,                       //查询关键字
                                 Location location,                    //定位坐标
                                 BigDecimal distatce,                  //定位后根据距离搜索商家
       						     Community community,                  //商圈
                                 Tenant.OrderType orderType            //排序
    ) {
        return tenantDao.openPage(pageable,area,tenantCategorys,tags,keyword,location,distatce,community,orderType,null,null,null);
    }

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
        return tenantDao.openPage(pageable,area,tenantCategorys,tags,keyword,location,distatce,community,orderType,isPromotion,isUnion,union);
    }

    public List<Tenant> openList(Integer count,                        //查询的条数
                                 Area area,                            //区域
                                 Set<TenantCategory> tenantCategorys,    //商家分类
                                 List<Tag> tags,                        //标签筛选
                                 String keyword,                        //查询关键字
                                 Location location,                    //定位坐标
                                 BigDecimal distatce,                    //定位后根据距离搜索商家
                                 List<Filter> filters,                    //过滤
                                 Tenant.OrderType orderType            //排序
    ) {
        return tenantDao.openList(count,area,tenantCategorys,tags,keyword,location,distatce,filters,orderType);
    }

    public List<Tenant> findList(Status status, List<Tag> tags, Date beginDate, Date endDate) {
        return tenantDao.findList(status,tags,beginDate,endDate);
    }
}
