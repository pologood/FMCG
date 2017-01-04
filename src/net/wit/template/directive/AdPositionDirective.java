/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.template.directive;

import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.util.*;

import javax.annotation.Resource;

import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Ad;
import net.wit.entity.AdPosition;
import net.wit.entity.Area;
import net.wit.entity.Tenant;
import net.wit.service.AdPositionService;
import net.wit.service.AdService;
import net.wit.service.AreaService;
import net.wit.service.TenantService;
import net.wit.util.FreemarkerUtils;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.core.Environment;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 模板指令 - 广告位
 *
 * @author rsico Team
 * @version 3.0
 */
@Component("adPositionDirective")
public class AdPositionDirective extends BaseDirective {

    /**
     * "商家"参数名称
     */
    private static final String TENANT_ID_PARAMETER_NAME = "tenantId";
    /**
     * "城市"参数名称
     */
    private static final String AREA_ID_PARAMETER_NAME = "areaId";
    /**
     * 变量名称
     */
    private static final String VARIABLE_NAME = "adPosition";


    @Resource(name = "freeMarkerConfigurer")
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Resource(name = "adPositionServiceImpl")
    private AdPositionService adPositionService;
    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;
    @Resource(name = "areaServiceImpl")
    private AreaService areaService;
    @Resource(name = "adServiceImpl")
    private AdService adService;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        AdPosition adPosition;
        Long id = getId(params);
        Long tenantId = FreemarkerUtils.getParameter(TENANT_ID_PARAMETER_NAME, Long.class, params);
        Long areaId = FreemarkerUtils.getParameter(AREA_ID_PARAMETER_NAME, Long.class, params);
        Tenant tenant = tenantService.find(tenantId);
        Area area = areaService.find(areaId);
        boolean useCache = useCache(env, params);
        String cacheRegion = getCacheRegion(env, params);
        Integer count = getCount(params);

//        if(id!=null){
            AdPosition adPostion = adPositionService.find(id);
        if(adPostion!=null){
            Pageable pageable = new Pageable();

            List<Order> orders =new ArrayList<>();
            orders.add(Order.asc("order"));
            pageable.setOrders(orders);
            pageable.setPageSize(count);
            pageable.setPageNumber(1);
            AdPosition ap = new AdPosition();
        	ap.setId(adPostion.getId());
            ap.setName(adPostion.getName());
            ap.setTemplate(adPostion.getTemplate());
            ap.setAds(new HashSet<Ad>());
            if (tenant != null) {
                Page<Ad> page = adService.findPage(tenant, adPostion, pageable);
                for (Ad ad : page.getContent()) {
                    ap.getAds().add(ad);
                }
            } else {
                Page<Ad> page = adService.findPage(adPostion, area,null, pageable);
                for (Ad ad : page.getContent()) {
                    ap.getAds().add(ad);
                }
            }

            if (body != null) {
                setLocalVariable(VARIABLE_NAME, ap, env, body);
            } else {
                if (ap != null && ap.getTemplate() != null) {
                    try {
                        Map<String, Object> model = new HashMap<String, Object>();
                        model.put(VARIABLE_NAME, ap);
                        Writer out = env.getOut();
                        new Template("adTemplate", new StringReader(ap.getTemplate()), freeMarkerConfigurer.getConfiguration()).process(model, out);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
//        }

    }

}