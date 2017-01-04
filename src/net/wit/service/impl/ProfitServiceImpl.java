package net.wit.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.DepositDao;
import net.wit.dao.MemberDao;
import net.wit.dao.ProfitDao;
import net.wit.entity.Profit;
import net.wit.entity.Profit.Status;
import net.wit.service.ProfitService;

/**
 * 货郎联盟
 * @author Administrator
 *
 */
@Service("profitServiceImpl")
public class ProfitServiceImpl extends BaseServiceImpl<Profit,Long> implements ProfitService{

	@Resource(name = "profitDaoImpl")
	private ProfitDao profitDao;

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;
	
	@Resource(name = "depositDaoImpl")
	private DepositDao depositDao;

	@Resource(name = "profitDaoImpl")
	public void setBaseDao(ProfitDao profitDao) {
		super.setBaseDao(profitDao);
	}
	public int share(Date startDate,Date endDate,Integer level) {
		
	  return 0;
	}
	
	public Page<Profit> findPage(Date beginDate, Date endDate,Status status,String keyword,Pageable pageable)  {
		return profitDao.findPage( beginDate, endDate, status, keyword, pageable);
	}
}
