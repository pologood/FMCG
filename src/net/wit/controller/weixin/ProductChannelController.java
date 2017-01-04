/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.weixin;

import net.wit.controller.weixin.model.DataBlock;
import net.wit.controller.weixin.model.ProductChannelModel;
import net.wit.controller.weixin.model.TenantCategoryModel;
import net.wit.entity.ProductChannel;
import net.wit.service.ProductChannelService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * Controller - 商品频道
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("weixinProductChannelController")
@RequestMapping("/weixin/product_channel")
public class ProductChannelController extends BaseController {

    @Resource(name = "productChannelServiceImpl")
    private ProductChannelService productChannelService;

    /**
     * 获取频道列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list() {
        return DataBlock.success(ProductChannelModel.bindData(productChannelService.findAll()), "执行成功");
    }

    /**
     * 频道下的商家分类
     *
     * @param id 频道id
     */
    @RequestMapping(value = "/tenant_category", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock getTenantCategory(Long id) {
        ProductChannel productChannel = productChannelService.find(id);
        if (productChannel == null) {
            return DataBlock.error("无效频道id");
        }
        return DataBlock.success(TenantCategoryModel.bindData(new ArrayList<>(productChannel.getTenantCategorys())), "执行成功");
    }


}