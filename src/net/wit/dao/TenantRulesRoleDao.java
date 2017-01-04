package net.wit.dao;

import com.fr.base.Inter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.*;

import java.util.List;

public interface TenantRulesRoleDao extends BaseDao<TenantRulesRole, Long> {
    Page<TenantRulesRole> openPage(Pageable pageable, String keyWord);

    List<TenantRulesRole> openList(Inter count, Role role, TenantRules tenantRules);

    List<TenantRulesRole> findByRoleId(Long id, String url,String type);
    List<TenantRulesRole> findByRulesId(Long id);
    TenantRulesRole find(Role role, TenantRules tenantRules);

    List<Long> findRulesIdsByRole(Role role, String type);
}
