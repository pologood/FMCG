package net.wit.dao.impl;

import net.wit.dao.BaseDao;
import net.wit.dao.MemberParameterDao;
import net.wit.entity.MemberParameter;
import org.springframework.stereotype.Repository;

/**
 *
 * Created by Administrator on 2016/10/24.
 */
@Repository("memberParameterDaoImpl")
public class MemberParameterDaoImpl extends BaseDaoImpl<MemberParameter,Long> implements MemberParameterDao {

}
