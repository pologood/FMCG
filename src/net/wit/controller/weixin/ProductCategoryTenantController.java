/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.weixin;

import net.wit.controller.weixin.model.DataBlock;
import net.wit.controller.weixin.model.ProductCategoryTenantModel;
import net.wit.entity.ProductCategoryTenant;
import net.wit.entity.Tenant;
import net.wit.service.ProductCategoryTenantService;
import net.wit.service.TenantService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Controller - 商品分类
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("weixinProductCategoryTenantController")
@RequestMapping("/weixin/productCategoryTenant")
public class ProductCategoryTenantController extends BaseController {

    @Resource(name = "productCategoryTenantServiceImpl")
    private ProductCategoryTenantService productCategoryTenantService;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    /**
     * 获取商家商品分类
     * @param tenantId 店铺Id
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock roots(Long tenantId) {
        Tenant tenant = tenantService.find(tenantId);
        if (tenant == null) {
            return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
        List<ProductCategoryTenant> productCategories;
        productCategories = productCategoryTenantService.findRoots(tenant);
        return DataBlock.success(ProductCategoryTenantModel.bindAllData(productCategories), "执行成功");
    }

}