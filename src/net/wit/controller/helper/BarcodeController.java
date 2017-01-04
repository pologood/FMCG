/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.helper;

import net.wit.Pageable;
import net.wit.ResourceNotFoundException;
import net.wit.controller.app.model.AdModel;
import net.wit.controller.helper.model.BarcodeModel;
import net.wit.controller.app.model.DataBlock;
import net.wit.entity.*;
import net.wit.entity.Product.OrderType;
import net.wit.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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