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
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 模板指令 - 顶级商品分类列表
 *
 * @author rsico Team
 * @version 3.0
 */
@Component("productBrandRootListDirective")
public class ProductBrandRootListDirective extends BaseDirective {

    /**
     * 变量名称
     */
    private static final String VARIABLE_NAME = "productBrands";

    @Resource(name = "productCategoryServiceImpl")
    private ProductCategoryService productCategoryService;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        List<ProductCategory> productCategorys= productCategoryService.findRoots();
        setLocalVariable(VARIABLE_NAME, productCategorys, env, body);
    }

}