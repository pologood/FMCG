package net.wit.dao;

import java.util.Date;
import java.util.List;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Profit;
import net.wit.entity.Profit.Status;

public interface ProfitDao extends BaseDao<Profit,Long>{

	public List<Profit> findList(Date startDate,Date endDate,Status status,Integer level,Integer count);
	
	public Page<Profit> findPage(Date beginDate, Date endDate,Status status,String keyword,Pageable pageable);
	
}
