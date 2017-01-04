package net.wit.dao.impl;

import net.wit.dao.HostDao;
import net.wit.entity.Host;

import org.springframework.stereotype.Repository;
/**
 * 货郎联盟
 * @author Administrator
 *
 */
@Repository("hostDaoImpl")
public class HostDaoImpl extends BaseDaoImpl<Host,Long> implements HostDao{

}
