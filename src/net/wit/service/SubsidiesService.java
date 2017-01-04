package net.wit.service;


import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Account;
import net.wit.entity.Member;
import net.wit.entity.Subsidies;
import net.wit.entity.Tenant;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SubsidiesService extends BaseService<Subsidies, Long> {
    List<Map<String,String>> readExl(MultipartFile file);//读取excel
    Map<String,Object> recharge(Subsidies subsidies);//给会员充值
}
