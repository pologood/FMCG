package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Product;
import net.wit.entity.Tenant;
import net.wit.entity.Union;
import net.wit.entity.VisitRecord;

import java.util.Date;
import java.util.List;

public interface VisitRecordDao extends BaseDao<VisitRecord,Long>{
    /**
     * 根据时间查询访问记录
     */
    public Page<VisitRecord> findByVisitRecordPage(Tenant tenant, Date beginDate, Date endDate, Pageable pageable);
    public List<VisitRecord> findByVisitRecordList(Tenant tenant, Date beginDate, Date endDate);

    /**
     * 根据时间统计访问量
     */
    public Long count(Tenant tenant, Date beginDate, Date endDate,VisitRecord.VisitType visitType);
    public Long uvCount(Tenant tenant, Date beginDate, Date endDate,VisitRecord.VisitType visitType);
}
