package net.wit.service.impl;

import net.wit.dao.CooperativePartnerDao;
import net.wit.entity.CooperativePartner;
import net.wit.service.CooperativePartnerService;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/8/22.
 */
@Repository("cooperativePartnerServiceImpl")
public class CooperativePartnerServiceImpl extends BaseServiceImpl<CooperativePartner,Long> implements CooperativePartnerService {

    @Resource(name = "cooperativePartnerDaoImpl")
    private CooperativePartnerDao cooperativePartnerDao;

    @Resource(name = "cooperativePartnerDaoImpl")
    public void setBaseDao(CooperativePartnerDao cooperativePartnerDao) {
        super.setBaseDao(cooperativePartnerDao);
    }
}
