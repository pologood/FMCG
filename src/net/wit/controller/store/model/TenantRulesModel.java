package net.wit.controller.store.model;

import net.wit.entity.TenantRules;

import java.util.ArrayList;
import java.util.List;

public class TenantRulesModel {

    /**
     * 名称
     */
    private String name;
    /**
     * 子节点
     **/
    private List<TenantRulesModel> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TenantRulesModel> getChildren() {
        return children;
    }

    public void setChildren(List<TenantRulesModel> children) {
        this.children = children;
    }

    public void copyFrom(TenantRules tenantRules) {
        if (tenantRules == null) {
            return;
        }
        this.name = tenantRules.getName();
        List<TenantRulesModel> tenantRulesModels = new ArrayList<>();
        for (TenantRules item : tenantRules.getChildren()) {
            TenantRulesModel model = new TenantRulesModel();
            model.copyFrom(item);
            tenantRulesModels.add(model);
        }
        this.children = tenantRulesModels;
    }

    public static List<TenantRulesModel> bindData(List<TenantRules> tenantRuless) {
        List<TenantRulesModel> tenantRulesModels = new ArrayList<>();
        for (TenantRules tenantRules : tenantRuless) {
            TenantRulesModel model = new TenantRulesModel();
            model.copyFrom(tenantRules);
            tenantRulesModels.add(model);
        }
        return tenantRulesModels;
    }

    public static TenantRulesModel bindData(TenantRules tenantRules) {
            TenantRulesModel model = new TenantRulesModel();
            model.copyFrom(tenantRules);
        return model;
    }

}
