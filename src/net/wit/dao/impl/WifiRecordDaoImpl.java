package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.WifiRecordDao;
import net.wit.entity.Tenant;
import net.wit.entity.WifiRecord;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * wifi
 * @author Administrator
 *
 */
@Repository("wifiRecordDaoImpl")
public class WifiRecordDaoImpl extends BaseDaoImpl<WifiRecord,Long> implements WifiRecordDao {
}
