package net.wit.template.directive;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.wit.*;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.FreemarkerUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/24.
 */
@Component("adProductChannelDirective")
public class AdProductChannelDirective extends BaseDirective {

    /** 频道编号*/
    private static final String PRODUCT_CHANNEL_ID_PARAMETER_NAME = "productChannelId";
    /** 所属区域编号*/
    private static final String AREA_ID_PARAMETER_NAME = "areaId";
    /** 变量名称 */
    private static final String VARIABLE_NAME = "adProductChannels";

    @Resource(name = "adPositionServiceImpl")
    private AdPositionService adPositionService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "adServiceImpl")
    private AdService adService;

    @Resource(name = "productChannelServiceImpl")
    private ProductChannelService productChannelService;

    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        Long id = getId(params);
        Long productChannelId = FreemarkerUtils.getParameter(PRODUCT_CHANNEL_ID_PARAMETER_NAME, Long.class, params);
        Long areaId=FreemarkerUtils.getParameter(AREA_ID_PARAMETER_NAME, Long.class, params);

        Integer count = getCount(params);

        Pageable pageable = new Pageable();

        List<net.wit.Order> orders =new ArrayList<>();
        orders.add(net.wit.Order.asc("order"));
        pageable.setOrders(orders);
        pageable.setPageSize(count);
        pageable.setPageNumber(1);

        Area area = areaService.find(areaId);
        AdPosition adPostion = adPositionService.find(id);
        ProductChannel productChannel = productChannelService.find(productChannelId);
        Page<Ad> page = adService.openPage(null,adPostion,area,productChannel,pageable);

        setLocalVariable(VARIABLE_NAME, page.getContent(), env, body);

    }
}
