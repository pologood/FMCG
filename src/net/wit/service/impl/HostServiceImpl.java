package net.wit.service.impl;

import javax.annotation.Resource;

import net.wit.dao.HostDao;
import net.wit.entity.Host;
import net.wit.service.HostService;

import org.springframework.stereotype.Service;

/**
 * 货郎联盟
 * @author Administrator
 *
 */
@Service("hostServiceImpl")
public class HostServiceImpl extends BaseServiceImpl<Host,Long> implements HostService{

	@Resource(name = "hostDaoImpl")
	public void setBaseDao(HostDao hostDao) {
		super.setBaseDao(hostDao);
	}
}
