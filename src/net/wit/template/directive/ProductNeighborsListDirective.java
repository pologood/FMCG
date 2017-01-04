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
import net.wit.entity.Product.OrderType;
import net.wit.service.*;
import net.wit.util.FreemarkerUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 模板指令 - 邻家好货
 * 
 * @author rsico Team
 * @version 3.0
 */
@Component("productNeighborsListDirective")
public class ProductNeighborsListDirective extends BaseDirective {

	/** "区域ID"参数名称 */
	private static final String TENANT_ID_PARAMETER_NAME = "tenantId";

	/** "标签ID"参数名称 */
	private static final String TAG_IDS_PARAMETER_NAME = "tagIds";

	/** 变量名称 */
	private static final String VARIABLE_NAME = "products";

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "tenantRelationServiceImpl")
	private TenantRelationService tenantRelationService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
		Long tenantId = FreemarkerUtils.getParameter(TENANT_ID_PARAMETER_NAME, Long.class, params);
		Long[] tagIds = FreemarkerUtils.getParameter(TAG_IDS_PARAMETER_NAME, Long[].class, params);

		Tenant tenant = tenantService.find(tenantId);
		List<Tag> tags = tagService.findList(tagIds);
		Integer count = getCount(params);

		//List<TenantRelation> tenantRelations=tenantRelationService.findRelations(null,null);


		List<Product> products = productService.openList(count,tenant,null,true,true,null,null,tags,null,null,null,null,null,null,null,OrderType.weight);
		setLocalVariable(VARIABLE_NAME, products, env, body);
	}
}