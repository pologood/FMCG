package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.TaskDao;
import net.wit.entity.Member;
import net.wit.entity.Task;
import net.wit.entity.Tenant;
import net.wit.service.TaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by ruanx on 2016/11/3.
 */
@Service("taskServiceImpl")
public class TaskServiceImpl extends BaseServiceImpl<Task, Long> implements TaskService {
    @Resource(name = "taskDaoImpl")
    private TaskDao taskDao;

    @Resource(name = "taskDaoImpl")
    public void setBaseDao(TaskDao taskDao) {
        super.setBaseDao(taskDao);
    }

    @Transactional(readOnly = true)
    public Task findByMember(Member member, Long month){
        return taskDao.findByMember(member,month);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> findByTenant(Member member,Tenant tenant, Long month){
        return taskDao.findByTenant(member,tenant,month);
    }

    @Transactional(readOnly = true)
    public Page<Task> findPage(Tenant tenant, Long month, String type, Pageable pageable, String order){
        return taskDao.findPage(tenant,month,type,pageable,order);
    }

    @Override
    public BigDecimal sum(String sumParameter, Member member, Tenant tenant, Long month) {
        return taskDao.sum(sumParameter,member,tenant,month);
    }
}
