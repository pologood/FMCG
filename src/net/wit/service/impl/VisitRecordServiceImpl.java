package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.BaseDao;
import net.wit.dao.ProductDao;
import net.wit.dao.TenantDao;
import net.wit.dao.VisitRecordDao;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.Tenant;
import net.wit.entity.VisitRecord;
import net.wit.service.VisitRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 访问记录
 *
 * @author Administrator
 */
@Service("visitRecordServiceImpl")
public class VisitRecordServiceImpl extends BaseServiceImpl<VisitRecord, Long> implements VisitRecordService {

    @Resource(name = "visitRecordDaoImpl")
    public void setBaseDao(VisitRecordDao visitRecordDao) {
        super.setBaseDao(visitRecordDao);
    }

    @Resource(name = "visitRecordDaoImpl")
    private VisitRecordDao visitRecordDao;

    @Resource(name = "productDaoImpl")
    private ProductDao productDao;

    @Resource(name = "tenantDaoImpl")
    private TenantDao tenantDao;

    @Override
    public void setBaseDao(BaseDao<VisitRecord, Long> baseDao) {
        super.setBaseDao(baseDao);
    }

    @Override
    public Page<VisitRecord> findByVisitRecordPage(Tenant tenant, Date beginDate, Date endDate, Pageable pageable) {
        return visitRecordDao.findByVisitRecordPage(tenant, beginDate, endDate, pageable);
    }

    @Override
    public List<VisitRecord> findByVisitRecordList(Tenant tenant, Date beginDate, Date endDate) {
        return visitRecordDao.findByVisitRecordList(tenant, beginDate, endDate);
    }

    public void add(Member member, Tenant tenant, Product product, String machineType, VisitRecord.VisitType visitType) {
        if (product != null) {
            if (product.getHits() == null) {
                product.setHits(0L);
            }
            product.setHits(product.getHits() + 1);
            productDao.merge(product);
        } else if (tenant != null) {
            if (tenant.getHits() == null) {
                tenant.setHits(0L);
            }
            tenant.setHits(tenant.getHits() + 1);
            tenantDao.merge(tenant);
        }
        if (member == null) return;
        VisitRecord visitRecord = new VisitRecord();
        visitRecord.setMachineType(machineType);
        visitRecord.setVisitType(visitType);
        visitRecord.setContent("通过" + visitType + "访问了你的" + (product == null ? "店铺" : "商品"));
        visitRecord.setMember(member);
        if (product != null) {
            visitRecord.setProduct(product);
        } else if (tenant != null) {
            visitRecord.setTenant(tenant);
        } else {
            return;
        }
        visitRecordDao.persist(visitRecord);
    }
}
