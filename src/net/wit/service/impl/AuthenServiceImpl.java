package net.wit.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.service.AuthenService;
import net.wit.service.StaticService;
import net.wit.Filter.Operator;
import net.wit.dao.AuthenDao;
import net.wit.entity.Admin;
import net.wit.entity.Area;
import net.wit.entity.Authen;
import net.wit.entity.Authen.AuthenStatus;
import net.wit.entity.Enterprise;
import net.wit.entity.Enterprise.EnterpriseType;
import net.wit.entity.Tenant;
import net.wit.entity.Authen.AuthenType;

@Service("authenServiceImpl")
public class AuthenServiceImpl extends BaseServiceImpl<Authen, Long> implements AuthenService{

	@Resource(name = "authenDaoImpl")
	private AuthenDao authenDao;
	
	@Resource(name = "staticServiceImpl")
	private StaticService staticService;
	
	@Resource(name = "authenDaoImpl")
	public void setBaseDao(AuthenDao authenDao) {
		super.setBaseDao(authenDao);
	}
	
	/* (non-Javadoc)
	 * @see net.wit.service.impl.AuthenService#findByType(net.wit.entity.Tenant, java.lang.String)
	 */
	@Override
	@Transactional(readOnly = true)
	public Authen findByType(Tenant tenant,AuthenType enterprise) {
		return authenDao.findByType(tenant, enterprise);
	}
	
	/* (non-Javadoc)
	 * @see net.wit.service.impl.AuthenService#save(net.wit.entity.Authen)
	 */
	@Override
	@Transactional
	public void save(Authen authen) {
		Assert.notNull(authen);

		super.save(authen);
		authenDao.flush();
		staticService.build(authen);
	}

	/* (non-Javadoc)
	 * @see net.wit.service.impl.AuthenService#update(net.wit.entity.Authen)
	 */
	@Override
	@Transactional
	public Authen update(Authen authen) {
		Assert.notNull(authen);

		Authen aAuthen = super.update(authen);
		authenDao.flush();
		staticService.build(aAuthen);
		return aAuthen;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Authen> findPage(Area area, AuthenStatus authenStatus,Date beginDate, Date endDate,
			Pageable pageable) {
       return authenDao.findPage(area, authenStatus, beginDate, endDate, pageable);
	}
	
}
