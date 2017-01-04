package net.wit.service.impl;

import net.wit.dao.MemberParameterDao;
import net.wit.entity.MemberParameter;
import net.wit.service.MemberParameterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/10/24.
 */
@Service("memberParameterServiceImpl")
public class MemberParameterServiceImpl extends BaseServiceImpl<MemberParameter, Long> implements MemberParameterService {

    @Resource(name = "memberParameterDaoImpl")
    private MemberParameterDao memberParameterDao;

    @Resource(name = "memberParameterDaoImpl")
    public void setBaseDao(MemberParameterDao memberParameterDao) {
        super.setBaseDao(memberParameterDao);
    }

}
