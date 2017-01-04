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

}
