package net.wit.service;

import com.fr.base.Inter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Employee;
import net.wit.entity.Role;
import net.wit.entity.TenantRules;
import net.wit.entity.TenantRulesRole;

import java.util.List;

/**
 * Service - 商家角色规则
 * @author rsico Team
 * @version 3.0
 */
public interface TenantRulesRoleService extends BaseService<TenantRulesRole, Long> {

    /**
     * * 分页查找
     */
    Page<TenantRulesRole> openPage(Pageable pageable, String keyWord);

    /**
     * 按条数查找
     * @param count
     * @param role
     * @param tenantRules
     * @return
     */
    List<TenantRulesRole> openList(Inter count,Role role,TenantRules tenantRules);

    /**
     *
     * @param role
     * @param tenantRules
     * @return
     */
    TenantRulesRole find(Role role,TenantRules tenantRules);

    List<TenantRulesRole> findByRoleId(Long id);
    List<TenantRulesRole> findByRoleId(Long id,String url);
    List<TenantRulesRole> findByRulesId(Long id);
    List<TenantRulesRole> findByRoleId(Long id,String url,String type);

    List<Long> findRulesIdsByRole(Role role, String type);


}
