package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;
import net.wit.entity.Task;
import net.wit.entity.Tenant;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by ruanx on 2016/11/4.
 */
public interface TaskDao extends BaseDao<Task, Long> {

  Task findByMember(Member member, Long month);

  Map<String, Object> findByTenant(Member member,Tenant tenant, Long month);

  Page<Task> findPage(Tenant tenant, Long month, String type, Pageable pageable, String order);
  /**根据会员查询推广数统计*/
  BigDecimal sum(String sumParameter, Member member, Tenant tenant, Long month);
}
