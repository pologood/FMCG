package net.wit.service.impl;

import net.wit.dao.SubsidiesItemDao;
import net.wit.entity.Subsidies;
import net.wit.entity.SubsidiesItem;
import net.wit.service.SubsidiesItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("subsidiesItemServiceImpl")
public class SubsidiesItemServiceImpl extends BaseServiceImpl<SubsidiesItem, Long> implements SubsidiesItemService {
	@Resource(name="subsidiesItemDaoImpl")
	private SubsidiesItemDao subsidiesItemDao;

	@Resource(name = "subsidiesItemDaoImpl")
	public void setBaseDao(SubsidiesItemDao subsidiesItemDao) {
		super.setBaseDao(subsidiesItemDao);
	}
//
//
//	@Override
//	public Page<Account> findByTenant(Tenant tenant,Date start_date,Date end_date,Tenant seller,Pageable pageable){
//		return accountDao.findByTenant(tenant,start_date,end_date,seller,pageable);
//	}
//
//	@Override
//	public List<Account> findByTenant(Tenant tenant,Date start_date,Date end_date,Tenant seller,Account.Status status){
//		return accountDao.findByTenant(tenant,start_date,end_date,seller,status);
//	}
//
//	@Override
//	public List<Account> withdrawSettle(Tenant tenant,Date start_date,Date end_date,Account.Status status){
//		return accountDao.withdrawSettle(tenant,start_date,end_date,status);
//	}
//
//	@Override
//	public List<Account> findTenants(Tenant tenant){
//		return accountDao.findTenants(tenant);
//	}
}
