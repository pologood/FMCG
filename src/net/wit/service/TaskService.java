package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;
import net.wit.entity.Task;
import net.wit.entity.Tenant;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


/**
 * Created by ruanx on 2016/11/3.
 */
public interface TaskService extends BaseService<Task, Long> {
    /**根据会员id查询任务*/
    Task findByMember(Member member, Long month);

    /**根据店铺id查询该店铺所有任务统计*/
    Map<String, Object> findByTenant(Member member,Tenant tenant, Long month);

    /**根据店铺id和排序输出*/
    Page<Task> findPage(Tenant tenant, Long month, String type, Pageable pageable, String order);

    /**查询推广数统计*/
    BigDecimal sum(String sumParameter, Member member, Tenant tenant, Long month);
}
