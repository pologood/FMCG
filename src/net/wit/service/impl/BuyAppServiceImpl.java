/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ApplicationDao;
import net.wit.dao.BuyAppDao;
import net.wit.entity.Application;
import net.wit.entity.BuyApp;
import net.wit.entity.Member;
import net.wit.service.BuyAppService;
import net.wit.service.MemberService;

import org.springframework.stereotype.Service;

/**
 * Service - 购买应用
 * 
 * @author rsico Team
 * @version 3.0
 */
@Service("buyAppServiceImpl")
public class BuyAppServiceImpl extends BaseServiceImpl<BuyApp, Long> implements BuyAppService {

	@Resource(name = "buyAppDaoImpl")
	private BuyAppDao buyAppDao;
	@Resource(name = "applicationDaoImpl")
	private ApplicationDao applicationDao;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
	public void saveAndNew(BuyApp entity) {
	  Application application = applicationDao.findByCode(entity.getMember(),entity.getCode());
	  if (application==null) {
		  application = new Application();
		  application.setValidityDate(new Date());
		  application.setStatus(Application.Status.closed);
		  
	  }
	  application.setCode(entity.getCode());
	  application.setMember(entity.getMember());
	  application.setName(entity.getName());
	  application.setPrice(entity.getAmount().divide(entity.getQuantity()).setScale(1));
	  applicationDao.merge(application);
	  entity.setApplication(applicationDao.findByCode(entity.getMember(),entity.getCode()) );
	  buyAppDao.merge(entity);
	}
	public BuyApp findBySn(String sn) {
	  return buyAppDao.findBySn(sn);
	}
	public void notify(BuyApp buyApp) throws Exception {
	  if (buyApp.getStatus().equals(BuyApp.Status.wait)) {
      memberService.payment(buyApp.getMember(),null,BigDecimal.ZERO, buyApp.getAmount(),buyApp.getName(),null);
	  buyApp.setStatus(BuyApp.Status.success);
	  buyAppDao.merge(buyApp);
	  Application application = buyApp.getApplication();
	  application.setStatus(Application.Status.opened);
	  Calendar cd = Calendar.getInstance();   
      cd.setTime(application.getValidityDate());
      
	  if (buyApp.getType().equals(BuyApp.Type.year)) {
        cd.add(Calendar.YEAR,buyApp.getQuantity().intValue());
	  } else {
	    cd.add(Calendar.MONTH,buyApp.getQuantity().intValue());
	  }
		  
	  application.setValidityDate(cd.getTime());
	  applicationDao.merge(application);
	  }
	}
	
	public Page<BuyApp> findPage(Member member, Pageable pageable){
		return buyAppDao.findPage(member, pageable);
	}
}