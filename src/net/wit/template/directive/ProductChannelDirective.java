package net.wit.template.directive;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import net.wit.entity.ProductChannel;
import net.wit.service.ProductChannelService;
import org.nutz.dao.entity.annotation.Comment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangfuyue on 2016/8/22.
 */
@Component("productChannelDirective")
public class ProductChannelDirective extends  BaseDirective{

    /** 变量名称 */
    private static final String VARIABLE_NAME = "productChannels";

    @Resource(name = "productChannelServiceImpl")
    private ProductChannelService productChannelService;

    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {

        List<ProductChannel> productChannels = productChannelService.findAll();

        setLocalVariable(VARIABLE_NAME, productChannels, env, body);
    }
}
