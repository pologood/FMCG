/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.template.directive;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Ad;
import net.wit.entity.AdPosition;
import net.wit.entity.Area;
import net.wit.service.AdPositionService;
import net.wit.service.AdService;
import net.wit.service.AreaService;
import net.wit.util.FreemarkerUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * 模板指令 - 广告位
 *
 * @author rsico Team
 * @version 3.0
 */
@Component("adActivityDirective")
public class AdActivityDirective extends BaseDirective {

    /**
     * "城市"参数名称
     */
    private static final String AREA_ID_PARAMETER_NAME = "areaId";

    /**
     * "城市"参数名称
     */
    private static final String LINK_ID_PARAMETER_NAME = "linkId";

    /**
     * 变量名称
     */
    private static final String VARIABLE_NAME = "adPosition";

    @Resource(name = "adPositionServiceImpl")
    private AdPositionService adPositionService;
    @Resource(name = "areaServiceImpl")
    private AreaService areaService;
    @Resource(name = "adServiceImpl")
    private AdService adService;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        Long id = getId(params);
        Long areaId = FreemarkerUtils.getParameter(AREA_ID_PARAMETER_NAME, Long.class, params);
        Long linkId = FreemarkerUtils.getParameter(LINK_ID_PARAMETER_NAME, Long.class, params);
        Area area = areaService.find(areaId);
        Integer count = getCount(params);

        AdPosition adPostion = adPositionService.find(id);
        if (adPostion != null) {
            Pageable pageable = new Pageable();

            List<Order> orders = new ArrayList<>();
            orders.add(Order.asc("order"));
            pageable.setOrders(orders);
            pageable.setPageSize(count);
            pageable.setPageNumber(1);
            Page<Ad> page = adService.findPage(adPostion, area,linkId, pageable);
            setLocalVariable(VARIABLE_NAME, page.getContent(), env, body);

        }
    }
}