package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.Tenant;
import net.wit.entity.VisitRecord;

import java.util.Date;
import java.util.List;

/**
 * 访问记录
 * @author Administrator
 *
 */
public interface VisitRecordService extends BaseService<VisitRecord,Long>{

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

	void add(Member member, Tenant tenant, Product product, String machineType, VisitRecord.VisitType visitType);
}
