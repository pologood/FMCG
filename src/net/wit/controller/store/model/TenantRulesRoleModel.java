package net.wit.controller.store.model;

import net.wit.entity.TenantRulesRole;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TenantRulesRoleModel {

    private RoleModel role;

    private TenantRulesModel rules;

    /*权限 0:无权限  1：有权限*/
    private Map<String,Boolean> mapAuthority;
//=====================================================getset

    public RoleModel getRole() {
        return role;
    }

    public void setRole(RoleModel role) {
        this.role = role;
    }

    public TenantRulesModel getRules() {
        return rules;
    }

    public void setRules(TenantRulesModel rules) {
        this.rules = rules;
    }

    public Map<String, Boolean> getMapAuthority() {
        return mapAuthority;
    }

    public void setMapAuthority(Map<String, Boolean> mapAuthority) {
        this.mapAuthority = mapAuthority;
    }

    //=====================================================getset
    public void copyFrom(TenantRulesRole tenantRulesRole) {
        if (tenantRulesRole == null) {
            return;
        }
        this.role = RoleModel.bindData(tenantRulesRole.getRole());
        this.rules = TenantRulesModel.bindData(tenantRulesRole.getRules());
            this.mapAuthority = tenantRulesRole.getAuthority();
    }

    public static List<TenantRulesRoleModel> bindData(List<TenantRulesRole> tenantRulesRoles) {
        List<TenantRulesRoleModel> tenantRulesModels = new ArrayList<>();
        for (TenantRulesRole tenantRulesRole : tenantRulesRoles) {
            TenantRulesRoleModel model = new TenantRulesRoleModel();
            model.copyFrom(tenantRulesRole);
            tenantRulesModels.add(model);
        }
        return tenantRulesModels;
    }


    public static TenantRulesRoleModel bindData(TenantRulesRole tenantRulesRole) {
            TenantRulesRoleModel model = new TenantRulesRoleModel();
            model.copyFrom(tenantRulesRole);
        return model;
    }

}
