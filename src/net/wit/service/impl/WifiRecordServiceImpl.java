package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.BaseDao;
import net.wit.dao.WifiRecordDao;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.Tenant;
import net.wit.entity.WifiRecord;
import net.wit.service.WifiRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 访问记录
 * @author Administrator
 *
 */
@Service("wifiRecordServiceImpl")
public class WifiRecordServiceImpl extends BaseServiceImpl<WifiRecord,Long> implements WifiRecordService {

	@Resource(name = "wifiRecordDaoImpl")
	public void setBaseDao(WifiRecordDao wifiRecordDao) {
		super.setBaseDao(wifiRecordDao);
	}

	@Resource(name = "wifiRecordDaoImpl")
	private WifiRecordDao wifiRecordDao;
	@Override
	public List<Map<String,Object>> findList(String datetime,  Tenant tenant) {
		return wifiRecordDao.findList(datetime,tenant);
	}

	@Override
	public List<Map<String, Object>> findMemberList(String datetime, Tenant tenant) {
		return wifiRecordDao.findMemberList(datetime,tenant);
	}

	@Override
	public Page<Map<String, Object>> findSummaryPage(Date start_time, Date end_time, Tenant tenant,Pageable pageable) {
		return wifiRecordDao.findSummaryPage(start_time,end_time,tenant,pageable);
	}
}
