/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.helper;

import net.wit.controller.helper.model.BarcodeModel;
import net.wit.controller.weixin.model.DataBlock;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Controller - 商品
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("helperBarcodeController")
@RequestMapping("/helper/barcode")
public class BarcodeController extends BaseController {

    @Resource(name = "barcodeServiceImpl")
    private BarcodeService barcodeService;

    /**
     * 获取条码信息
     */
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock get(String barcode) {

        Barcode _barcode = barcodeService.findByBarcode(barcode);
        if (_barcode == null) {
            DataBlock.error("条码无效");
        }

        return DataBlock.success(BarcodeModel.bindData(_barcode), "执行成功");
    }
}