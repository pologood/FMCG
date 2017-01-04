package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.dao.ApplicationDao;
import net.wit.dao.CommissionDao;
import net.wit.dao.DepositDao;
import net.wit.dao.EnterpriseDao;
import net.wit.dao.MemberDao;
import net.wit.dao.PaymentDao;
import net.wit.dao.SnDao;
import net.wit.entity.*;
import net.wit.entity.Enterprise.EnterpriseType;
import net.wit.entity.Application.Status;
import net.wit.entity.Application.Type;
import net.wit.entity.Payment.Method;
import net.wit.service.ApplicationService;
import net.wit.service.MessageService;
import net.wit.service.SnService;
import net.wit.support.EntitySupport;
import net.wit.util.SettingUtils;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 应用
 * 
 * @author rsico Team
 * @version 3.0
 */
@Service("applicationServiceImpl")
public class ApplicationServiceImpl extends BaseServiceImpl<Application, Long> implements ApplicationService {
	
	@Resource(name = "applicationDaoImpl")
	private ApplicationDao applicationDao;
	
	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;
	
	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;
	
	@Resource(name = "snDaoImpl")
	private SnDao snDao;
	
	@Resource(name = "paymentDaoImpl")
	private PaymentDao paymentDao;
	
	@Resource(name = "enterpriseDaoImpl")
	private EnterpriseDao enterpriseDao;
	
	@Resource(name = "commissionDaoImpl")
	private CommissionDao commissionDao;

	@Resource(name = "messageServiceImpl")
	private MessageService messageService;
	
	public Application findByCode(Member member,String code){
		return applicationDao.findByCode(member, code);
	}
	
	
	/**
	 * 根据编号查找应用
	 * 
	 * @param sn
	 *            应用编号(忽略大小写)
	 * @return 应用，若不存在则返回null
	 */
	public Application findApplication(Tenant tenant,String code,Type type) {
		return applicationDao.findApplication(tenant, code, type);
	};
	public List<Application> findByMember(Member member){
		return applicationDao.findByMember(member);
	}
	public List<Application> findList(Tenant tenant, Type type){
		return applicationDao.findList(tenant,type);
	}

	@Resource(name = "applicationDaoImpl")
	public void setBaseDao(ApplicationDao applicationDao) {
		super.setBaseDao(applicationDao);
	}
	
	@Override
	@Transactional
	public void save(Application application) {
		super.save(application);
	}


	@Override
	@Transactional
	public Application update(Application application) {
		return super.update(application);
	}
	
	public Payment create(Tenant tenant,BigDecimal price,Admin admin,Member member) {
        List<Application> apps = findList(tenant,Type.erp);
        BigDecimal totalPrice = BigDecimal.ZERO;
    	for (Application app:apps) {
    		if (app.getStatus().equals(Status.none)) { 
    			totalPrice = totalPrice.add(price);
    		}
      	}
    	Payment payment = new Payment();
		payment.setSn(snDao.generate(Sn.Type.payment));
		payment.setType(Payment.Type.function);
		payment.setMethod(Method.online);
		payment.setStatus(Payment.Status.wait);
		payment.setPaymentMethod("");
		payment.setFee(BigDecimal.ZERO);
		payment.setAmount(totalPrice);
		payment.setMemo("店家助手一年使用费");
		payment.setPaymentPluginId(null);
		payment.setExpire(DateUtils.addMinutes(new Date(),3600));
		payment.setMember(member);
		paymentDao.persist(payment);
		Setting setting = SettingUtils.get();
		
    	for (Application app:apps) {
    		if (app.getStatus().equals(Status.none)) {
    			Commission commission = new Commission();
    			commission.setAdmin(admin);
    			commission.setMember(member);
    			commission.setAmount(price);
     			commission.setBrokerage(setting.setScale(price.multiply(new BigDecimal("0.25")) ));
    			commission.setArea(app.getTenant().getArea());
    			if (admin!=null) {
    	   			commission.setType(Commission.Type.admin);
    	   			commission.setAdmin(admin);
        			commission.setEnterprise(admin.getEnterprise());
        			commission.setArea(admin.getEnterprise().getArea());
    	   		}  else if (member!=null) {
    	   		    commission.setType(Commission.Type.member);
    	   		    commission.setMember(member);
    	   		    commission.setArea(member.getArea());
    	   		    Enterprise enterprise = null;
    	   		    if (commission.getArea()!=null) {
          	   	   		 enterprise = enterpriseDao.findEnterPrise(EnterpriseType.countyproxy,commission.getArea());
               	   		 if (enterprise==null) {
               	   			 Area area = commission.getArea();
               	   			 if (area.getParent()!=null) {
               	   	   	        enterprise = enterpriseDao.findEnterPrise(EnterpriseType.cityproxy,commission.getArea().getParent());
               	   			 } else {
                	   	   	    enterprise = enterpriseDao.findEnterPrise(EnterpriseType.cityproxy,commission.getArea());
               	   			 }
               	   		 }
                  	   	 if (enterprise==null) {
               	   			 Area area = commission.getArea();
               	   			 if (area.getParent()!=null && area.getParent().getParent()!=null) {
               	   	   	        enterprise = enterpriseDao.findEnterPrise(EnterpriseType.provinceproxy,commission.getArea().getParent().getParent());
               	   			 } else if (area.getParent()!=null) {
                	   	   	        enterprise = enterpriseDao.findEnterPrise(EnterpriseType.provinceproxy,commission.getArea().getParent());
               	   			 }
               	   		 }
           	   		 }
          	   		 if (enterprise==null) {
      	   	   	        enterprise = enterpriseDao.findEnterPrise(EnterpriseType.proxy,null);
          	   		 }
         			commission.setEnterprise(enterprise);
   	   		    } else {
    	   		    commission.setType(Commission.Type.none);
    	   		    commission.setMember(null);
    	   		    Enterprise enterprise = enterpriseDao.findEnterPrise(EnterpriseType.proxy,null);
         			commission.setEnterprise(enterprise);
    	   		}
    			commission.setStatus(Commission.Status.unpay);    
    			commission.setPayment(payment);
    			commission.setApplication(app);
    			commissionDao.persist(commission);
    		}
      	}
    	
    	return payment;
    	
	}
	public void payment(Payment payment, Member operator) {
		if (payment.getMethod() == Payment.Method.deposit) {
			Member member = payment.getMember();
			memberDao.lock(member, LockModeType.PESSIMISTIC_WRITE);
			if (member.getClearBalance().compareTo(member.getBalance()) > 0) {
				member.setClearBalance(member.getBalance());
			}

			if (member.getBalance().compareTo(payment.getAmount()) >= 0) {
				member.setBalance(member.getBalance().subtract(payment.getAmount()));
				if (member.getClearBalance().compareTo(payment.getAmount())>0) {
				   BigDecimal rechargeBalance = member.getBalance().subtract(member.getClearBalance());
				   payment.setClearAmount(payment.getAmount().subtract(rechargeBalance));
				   member.setClearBalance(member.getClearBalance().subtract(payment.getAmount()));
				} else {
				   payment.setClearAmount(member.getClearBalance());
				   member.setClearBalance(BigDecimal.ZERO);
				}
	            if (member.getFreezeCashBalance().compareTo(payment.getAmount())<0) {
	                member.setFreezeCashBalance(BigDecimal.ZERO);
	            } else {
	                member.setFreezeCashBalance(member.getFreezeCashBalance().subtract(payment.getAmount()));
	            }
				memberDao.merge(member);
				
				Deposit deposit = new Deposit();
				deposit.setType(Deposit.Type.payment);
				deposit.setStatus(Deposit.Status.complete);
				deposit.setCredit(BigDecimal.ZERO);
				deposit.setDebit(payment.getAmount());
				deposit.setBalance(member.getBalance());
				deposit.setOperator(operator != null ? operator.getUsername() : null);
				deposit.setMember(member);
				deposit.setMemo("购买店家助手 单号:" + payment.getSn());
				deposit.setOrder(null);
				deposit.setStatus(Deposit.Status.none);
				depositDao.persist(deposit);
				payment.setStatus(Payment.Status.success);

				Setting setting = SettingUtils.get();
				Message message = EntitySupport.createInitMessage(Message.Type.account,
						"您的账户，支付费用" + setting.setScale(payment.getAmount()).toString() + "元，付款号:" + payment.getSn() + "",
						null,member,null);
				message.setDeposit(deposit);
				message.setWay(Message.Way.tenant);
				messageService.save(message);

			} else {
				payment.setAmount(BigDecimal.ZERO);
				return;
			}
		}
		
		for (Commission commission:payment.getCommissions()) {
			commission.setStatus(Commission.Status.paid);
		    Application app = commission.getApplication();
			Calendar   calendar   =   new GregorianCalendar(); 
		    calendar.setTime(new Date()); 
		    calendar.add(calendar.YEAR,1);
		    app.setValidityDate(calendar.getTime());
		    app.setStatus(Status.opened);
		    applicationDao.merge(app);
		    commissionDao.merge(commission);
		}
		
	}


	public Page<Application> openPage(String keyword, Pageable pageable,Status statu){
		return applicationDao.openPage(keyword,pageable,statu);
	}
	
}