package net.wit.controller.store.model;

import net.wit.entity.Role;

import java.util.ArrayList;
import java.util.List;

public class RoleModel {

    /**
     * 名称
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void copyFrom(Role role) {
        if (role == null) {
            return;
        }
        this.name = role.getName();
    }

    public static List<RoleModel> bindData(List<Role> roles) {
        List<RoleModel> tenantRulesModels = new ArrayList<>();
        for (Role role : roles) {
            RoleModel model = new RoleModel();
            model.copyFrom(role);
            tenantRulesModels.add(model);
        }
        return tenantRulesModels;
    }

    public static RoleModel bindData(Role role) {
        RoleModel model = new RoleModel();
        model.copyFrom(role);
        return model;
    }

}
