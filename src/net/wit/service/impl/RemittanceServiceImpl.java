package net.wit.service.impl;

import net.wit.dao.RemittanceDao;
import net.wit.entity.Remittance;
import net.wit.service.RemittanceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("remittanceServiceImpl")
public class RemittanceServiceImpl extends BaseServiceImpl<Remittance, Long> implements RemittanceService {

    @Resource(name = "remittanceDaoImpl")
    public void setBaseDao(RemittanceDao remittanceDao) {
        super.setBaseDao(remittanceDao);
    }
}