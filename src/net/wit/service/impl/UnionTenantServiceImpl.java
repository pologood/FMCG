package net.wit.service.impl;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.dao.TenantDao;
import net.wit.dao.UnionDao;
import net.wit.dao.PaymentDao;
import net.wit.dao.SnDao;
import net.wit.dao.UnionTenantDao;
import net.wit.dao.impl.BaseDaoImpl;
import net.wit.entity.*;
import net.wit.service.UnionTenantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import net.wit.entity.Tenant.OrderType;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/14.
 */
@Service("unionTenantServiceImpl")
public class UnionTenantServiceImpl extends BaseServiceImpl<UnionTenant,Long> implements UnionTenantService{
    @Resource(name = "unionTenantDaoImpl")
    private UnionTenantDao unionTenantDao;
    @Resource(name = "snDaoImpl")
    private SnDao snDao;
    @Resource(name = "paymentDaoImpl")
    private PaymentDao paymentDao;
    @Resource(name = "tenantDaoImpl")
    private TenantDao tenantDao;
    @Resource(name = "unionDaoImpl")
    private UnionDao unionDao;

    @Resource(name = "unionTenantDaoImpl")
    public void setBaseDao(UnionTenantDao unionTenantDao) {
        super.setBaseDao(unionTenantDao);
    }

    @Transactional(readOnly = true)
    public Page<Map<String,Object>> findTenant(Union union, Pageable pageable, OrderType orderType){
        return unionTenantDao.findTenant(union,pageable,orderType);
    }

    @Transactional(readOnly = true)
    public Page<Map<String,Object>> findTenantByPad(Equipment equipment, Pageable pageable, OrderType orderType){
        return unionTenantDao.findTenantByPad(equipment,pageable,orderType);
    }

    @Transactional(readOnly = true)
    public List<UnionTenant> findUnionTenant(Union unioin, Tenant tenant, List<Filter> filters){
        return unionTenantDao.findUnionTenant(unioin, tenant, filters);
    }

    public Page<Map<String,Object>> findPage(Tenant tenant,String status,Pageable pageable){
        return unionTenantDao.findPage( tenant,status,pageable);
    }

    public Page<Map<String,Object>> findPage(Equipment equipment,String status,Pageable pageable){
        return unionTenantDao.findPage( equipment,status,pageable);
    }

    public Long findUnionTenant(Equipment equipment,Tenant tenant){
        return unionTenantDao.findUnionTenant(equipment,tenant);
    }

    public Page<UnionTenant> findPage(Union union, UnionTenant.Status status, Pageable pageable) {
        return unionTenantDao.findPage(union,status,pageable);
    }

    @Override
    public Long count(Equipment equipment, Tenant tenant, UnionTenant.Status status) {
        return unionTenantDao.count(equipment,tenant,status);
    }

    @Override
    public Page<UnionTenant> findUnionTenantPage(Equipment equipment, Tenant tenant, UnionTenant.Status status,Union union ,Pageable pageable) {
        return unionTenantDao.findUnionTenantPage(equipment,tenant,status,union, pageable);
    }

    @Override
    public List<UnionTenant> findUnionTenantList(Equipment equipment, Tenant tenant, UnionTenant.Status status,Union union) {
        return unionTenantDao.findUnionTenantList(equipment,tenant,status,union);
    }

    @Override
    public void pay(UnionTenant unionTenant, Payment payment) {
        if(unionTenant.getId()!=null){
            unionTenant.setPayment(payment);
            unionTenantDao.merge(unionTenant);
            payment.setUnionTenant(unionTenant);
            paymentDao.persist(payment);
        }else{
            paymentDao.persist(payment);
            unionTenant.setPayment(payment);
            unionTenantDao.persist(unionTenant);
            payment.setUnionTenant(unionTenant);
            paymentDao.merge(payment);

        }

    }
    public void payment(Payment payment, Member operator) {
        UnionTenant unionTenant = payment.getUnionTenant();
        if (unionTenant.getStatus().equals(UnionTenant.Status.confirmed)) {
            return;
        };
        if(unionTenant.getType() ==UnionTenant.Type.device){
            unionTenant.setStatus(UnionTenant.Status.freezed);
        }else{
            unionTenant.setStatus(UnionTenant.Status.confirmed);
        }

        Union union=unionTenant.getUnion();
        Tenant tenant=unionTenant.getTenant();
        if(tenant.getIsUnion()==false){
            tenant.setIsUnion(true);
        }
        union.setTenantNumber(union.getTenantNumber()+1);
        Calendar curr = Calendar.getInstance();
        curr.add(Calendar.YEAR, 1);
        Date date=curr.getTime();
        unionTenant.setExpire(date);
        if(tenant.getAgency().compareTo(BigDecimal.ZERO)==0){
            tenant.setAgency(union.getBrokerage());//店铺的联盟佣金
        }
        tenant.setUnion(union);
        tenantDao.merge(tenant);
        unionDao.merge(union);
        unionTenantDao.merge(unionTenant);
     }

    @Override
    public void cancel(UnionTenant unionTenant) {
        unionTenantDao.merge(unionTenant);
    }

}
