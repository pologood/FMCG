package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Employee;
import net.wit.entity.ProductChannel;
import net.wit.entity.TenantRules;
import net.wit.entity.TenantRulesRole;

import java.util.List;
/**
 * Service - 商家规则接口
 * @author rsico Team
 * @version 3.0
 */
public interface TenantRulesService extends BaseService<TenantRules, Long> {
    /**
     * 查找顶级规则
     *
     * @return 顶级规则
     */
    List<TenantRules> findRoots();


    /**
     * 查找上级规则
     *
     * @param rules 规则
     * @return 上级规则
     */
    List<TenantRules> findParents(TenantRules tenantRules);


    /**
     * 查找规则树
     *
     * @return 规则树
     */
    List<TenantRules> findTree(int depth);

    Page<TenantRules> openPage(String searchValue, Pageable pageable);

}
