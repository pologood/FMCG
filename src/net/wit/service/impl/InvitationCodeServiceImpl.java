package net.wit.service.impl;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.InvitationCodeDao;
import net.wit.entity.Admin;
import net.wit.entity.Enterprise;
import net.wit.entity.InvitationCode;
import net.wit.service.InvitationCodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by WangChao on 2016-1-11.
 */
@Service("invitationCodeServiceImpl")
public class InvitationCodeServiceImpl extends BaseServiceImpl<InvitationCode,Long> implements InvitationCodeService{

    @Resource(name = "invitationCodeDaoImpl")
    InvitationCodeDao invitationCodeDao;
    @Resource(name = "invitationCodeDaoImpl")
    public void setBaseDao(InvitationCodeDao invitationCodeDao){
        super.setBaseDao(invitationCodeDao);
    }

    @Override
    public Page<InvitationCode> findPage(Admin admin, Pageable pageable) {
        List<Filter> filters = pageable.getFilters();
        filters.add(new Filter("admin", Filter.Operator.eq, admin));
        Page<InvitationCode> page=invitationCodeDao.findPage(pageable);
        return page;
    }

    public InvitationCode findByCode(String code) {
    	return invitationCodeDao.findByCode(code);
    }
}
