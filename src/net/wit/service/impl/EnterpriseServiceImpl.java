/**
 *====================================================
 * 文件名称: EnterpriseServiceImpl.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年10月22日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 */
package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.LockModeType;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Filter.Operator;
import net.wit.dao.AreaDao;
import net.wit.dao.EnterpriseDao;
import net.wit.dao.SnDepositDao;
import net.wit.entity.Admin;
import net.wit.entity.Area;
import net.wit.entity.Enterprise;
import net.wit.entity.Enterprise.EnterpriseType;
import net.wit.entity.SnDeposit;
import net.wit.entity.SnDeposit.Type;
import net.wit.exception.BalanceNotEnoughException;
import net.wit.service.EnterpriseService;

import org.springframework.stereotype.Service;

/**
 * @ClassName: EnterpriseServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014年10月22日 上午9:24:31
 */
@Service("enterpriseServiceImpl")
public class EnterpriseServiceImpl extends BaseServiceImpl<Enterprise, Long> implements EnterpriseService {

	@Resource(name = "enterpriseDaoImpl")
	private EnterpriseDao enterpriseDao;
	
	@Resource(name = "areaDaoImpl")
	private AreaDao areaDao;
	
	@Resource(name = "snDepositDaoImpl")
	private SnDepositDao snDepositDao;

	@Resource(name = "enterpriseDaoImpl")
	public void setBaseDao(EnterpriseDao enterpriseDao) {
		super.setBaseDao(enterpriseDao);
	}
	
	public Page<Enterprise> findPage(EnterpriseType enterpriseType,Area area,Pageable pageable){
		return enterpriseDao.findPage(enterpriseType, area, pageable);
	}
	
	public Page<Enterprise> findPage(EnterpriseType enterpriseType,Pageable pageable){
		return enterpriseDao.findPage(enterpriseType,pageable);
	}
	
	public Page<Enterprise> findPage(Admin admin,Pageable pageable){
		if(!admin.getUsername().equals("admin")){
			EnterpriseType enterpriseType=admin.getEnterprise().getEnterprisetype();
			List<Filter> filters = pageable.getFilters();
			Area area=null;
			if(enterpriseType==EnterpriseType.provinceproxy){
				area=admin.getEnterprise().getArea();
				List<Area> areas = areaDao.findChildren(area, null);
				filters.add(new Filter("area", Operator.in, areas));
			}else if(enterpriseType==EnterpriseType.cityproxy){
				area=admin.getEnterprise().getArea().getParent();
				List<Area> areas = areaDao.findChildren(area, null);
				filters.add(new Filter("area", Operator.in, areas));
			}else if(enterpriseType==EnterpriseType.countyproxy){
				area=admin.getEnterprise().getArea().getParent().getParent();
				filters.add(new Filter("area", Operator.eq, area));
			}
			List<EnterpriseType> enterpriseTypes= new ArrayList<Enterprise.EnterpriseType>();
			for(int i=enterpriseType.ordinal()+1;i<EnterpriseType.values().length;i++){
				enterpriseTypes.add(EnterpriseType.values()[i]);
			}
			filters.add(new Filter("enterprisetype", Operator.in, enterpriseTypes));
//			pageable.setOrderProperty("enterprisetype");
		}
		return enterpriseDao.findPage(pageable);
	}

	@Override
	public void charge(Enterprise currentEnterprise,Enterprise chargeEnterprise, int snNumber,String operator) throws Exception {
		enterpriseDao.lock(currentEnterprise, LockModeType.PESSIMISTIC_WRITE);
		enterpriseDao.lock(chargeEnterprise, LockModeType.PESSIMISTIC_WRITE);
		if(currentEnterprise.getSnNumber()==null||currentEnterprise.getSnNumber().compareTo(snNumber)<0){
			throw new BalanceNotEnoughException("snNumber.not.enough");
		}
		//更新当前企业许可数
		currentEnterprise.setSnNumber(currentEnterprise.getSnNumber()-snNumber);
		enterpriseDao.merge(currentEnterprise);
		//生成支出流水
		SnDeposit snDeposit=new SnDeposit();
		snDeposit.setCreateDate(new Date());
		snDeposit.setBalance(currentEnterprise.getSnNumber());
		snDeposit.setCredit(0);
		snDeposit.setDebit(new BigDecimal(snNumber));
		snDeposit.setOperator(operator);
		snDeposit.setType(Type.outcome);
		snDeposit.setEnterprise(currentEnterprise);
		snDepositDao.persist(snDeposit);
		//更新充值企业许可数
		chargeEnterprise.setSnNumber(chargeEnterprise.getSnNumber()+snNumber);
		enterpriseDao.merge(chargeEnterprise);
		//生成收入流水
		snDeposit=new SnDeposit();
		snDeposit.setCreateDate(new Date());
		snDeposit.setBalance(chargeEnterprise.getSnNumber());
		snDeposit.setCredit(snNumber);
		snDeposit.setDebit(new BigDecimal(0));
		snDeposit.setOperator(operator);
		snDeposit.setType(Type.income);
		snDeposit.setEnterprise(chargeEnterprise);
		snDepositDao.persist(snDeposit);
	}
}
