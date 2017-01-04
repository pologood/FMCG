package net.wit.service;


import java.util.Date;
import java.util.List;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Account;
import net.wit.entity.Tenant;

public interface AccountService extends BaseService<Account, Long> {
	Page<Account> findByTenant(Tenant tenant,Date start_date,Date end_date,Tenant seller,Pageable pageable);
	List<Account> findByTenant(Tenant tenant,Date start_date,Date end_date,Tenant seller,Account.Status status);
	List<Account> withdrawSettle(Tenant tenant,Date start_date,Date end_date,Account.Status status);
	List<Account> findTenants(Tenant tenant);
}
