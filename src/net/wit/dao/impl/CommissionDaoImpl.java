package net.wit.dao.impl;


import net.wit.entity.Commission;
import org.springframework.stereotype.Repository;

import net.wit.dao.CommissionDao;
/**
 * 佣金核算
 * 
 * @author thwapp
 *
 */
@Repository("commissionDaoImpl")
public class CommissionDaoImpl extends BaseDaoImpl<Commission, Long> implements CommissionDao {

}
