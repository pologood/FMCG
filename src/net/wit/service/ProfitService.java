package net.wit.service;

import java.util.Date;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Profit;
import net.wit.entity.Profit.Status;
/**
 * 货郎联盟
 * @author Administrator
 *
 */
public interface ProfitService extends BaseService<Profit,Long>{
	int share(Date startDate,Date endDate,Integer level);
	Page<Profit> findPage(Date beginDate, Date endDate,Status status,String keyword,Pageable pageable);
}
