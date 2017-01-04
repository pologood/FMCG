/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.template.directive;

import freemarker.core.Environment;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.FreemarkerUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.util.*;

/**
 * 模板指令 - 商家助手
 *
 * @author rsico Team
 * @version 3.0
 */
@Component("helperRoleDirective")
public class HelperRoleDirective extends BaseDirective {

    /** 变量名称 */
    private static final String VARIABLE_NAME = "helperRole";


    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "cartServiceImpl")
    private CartService cartService;

    @Resource(name = "employeeServiceImpl")
    private EmployeeService employeeService;

    @Resource(name = "tenantRulesRoleServiceImpl")
    private TenantRulesRoleService tenantRulesRoleService;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
       //获取参数
        Long role=FreemarkerUtils.getParameter("role",Long.class,params);
        String type=FreemarkerUtils.getParameter("type",String.class,params);
        String url=FreemarkerUtils.getParameter("url",String.class,params);
        String urlsStr=FreemarkerUtils.getParameter("urls",String.class,params);
        String[] urls=null;
        if (urlsStr!=null)
        {
            urls=urlsStr.split(";");
            if (url==null&&urls.length>0){
                url=urls[0];
            }else if (url==null&&urlsStr!=null){
                url=urlsStr;
            }
        }
        /*返回URL:0：无权限访问；url:有权限访问（一级和二级）*/
        String retUrl="0";
        /*返回URL:0：无权限访问；1:有权限访问（检查操作，如增删改查）*/
        String retOper="0";
        /*无权限访问时的样式*/
        String noAuthorityStypeColor="  color: #ADADAD;  ";
        Map<String,Object> map=new HashMap<>();

        Member member = memberService.getCurrent();
        if (member != null) {
            Tenant tenant = member.getTenant();
            if (tenant != null) {
                Member owner = tenant.getMember();
                String rolesStr = "";
                //屏蔽，暂时屏蔽权限功能
                if (member!=owner) {
                    Pageable pageable = new Pageable();
                    List<Filter> filters = new ArrayList<Filter>();
                    filters.add(new Filter("tenant", Filter.Operator.eq, tenant));
                    filters.add(new Filter("member", Filter.Operator.eq, member));
                    pageable.setFilters(filters);
                    Page<Employee> emps = employeeService.findPage(pageable);
                    if (!emps.getContent().isEmpty()) {
                        rolesStr = emps.getContent().get(0).getRole();
                        String[] roleIds = rolesStr.split(Employee.RoleSplit);
                        for (String id : roleIds) {
                            if(!"".equals(id)){
                                try {
                                    if (urls!=null){
                                        //多个入口中寻找有权限的入口
                                        int i=0;
                                        for (;i<urls.length;i++ )
                                        {
                                            List<TenantRulesRole> tenantRulesRoles = tenantRulesRoleService.findByRoleId(Long.valueOf(id), urls[i], type);
                                            if (tenantRulesRoles.size() > 0) {
                                                //通过
                                                retUrl = urls[i];
                                                retOper = "1";
                                                noAuthorityStypeColor = "";
                                                break;
                                            }
                                        }
                                        if (retUrl == urls[i]){
                                            break;
                                        }
                                    }else  {
                                        //单入口
                                        List<TenantRulesRole> tenantRulesRoles = tenantRulesRoleService.findByRoleId(Long.valueOf(id), url, type);
                                        if (tenantRulesRoles.size() > 0) {

                                            TenantRules tenantRules = tenantRulesRoles.get(0).getRules();

                                            if (type != null && tenantRules.getOper().indexOf(type) != -1) {
                                                retOper = "1";
                                            }

                                            //通过
                                            retUrl = url;
                                            noAuthorityStypeColor = "";
                                            break;
                                        }
                                    }
                                } catch (Exception e) {
                                    continue;
                                }
                            }

                        }
                    }
                } else {
                    //店长-默认拥有所有权限
                    retUrl=url;
                    retOper="1";
                    noAuthorityStypeColor="";
                }
            }

        }

        String noAuthorityStype=" style=\""+noAuthorityStypeColor+"\" ";
        map.put("noAuthorityStypeColor",noAuthorityStypeColor);
        map.put("noAuthorityStype",noAuthorityStype);
        map.put("retUrl",retUrl);
        map.put("retOper",retOper);
        //0：无权  1：有权
        setLocalVariable(VARIABLE_NAME,map,env,body);
    }
}