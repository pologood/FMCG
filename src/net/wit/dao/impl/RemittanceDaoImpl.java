package net.wit.dao.impl;

import net.wit.dao.RemittanceDao;
import net.wit.entity.Remittance;
import org.springframework.stereotype.Repository;

/**
 * Created by hujun on 16/7/5.
 */
@Repository("remittanceDaoImpl")
public class RemittanceDaoImpl extends BaseDaoImpl<Remittance,Long> implements RemittanceDao {
}
