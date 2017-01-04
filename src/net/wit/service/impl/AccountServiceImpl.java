package net.wit.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.AccountDao;
import net.wit.entity.Account;
import net.wit.entity.Tenant;
import net.wit.service.AccountService;
@Service("accountServiceImpl")
public class AccountServiceImpl extends BaseServiceImpl<Account, Long> implements AccountService{
	@Resource(name="accountDaoImpl")
	private AccountDao accountDao;
	
	@Resource(name = "accountDaoImpl")
	public void setBaseDao(AccountDao accountDao) {
		super.setBaseDao(accountDao);
	}

	
	@Override
	public Page<Account> findByTenant(Tenant tenant,Date start_date,Date end_date,Tenant seller,Pageable pageable){
		return accountDao.findByTenant(tenant,start_date,end_date,seller,pageable);
	}
	
	@Override
	public List<Account> findByTenant(Tenant tenant,Date start_date,Date end_date,Tenant seller,Account.Status status){
		return accountDao.findByTenant(tenant,start_date,end_date,seller,status);
	}
	
	@Override
	public List<Account> withdrawSettle(Tenant tenant,Date start_date,Date end_date,Account.Status status){
		return accountDao.withdrawSettle(tenant,start_date,end_date,status);
	} 
	
	@Override
	public List<Account> findTenants(Tenant tenant){
		return accountDao.findTenants(tenant);
	} 
}
