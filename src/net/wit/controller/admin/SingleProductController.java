/*
 * Copyright 2005-2013 rsico.net. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;
import net.wit.entity.*;
import net.wit.service.*;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller - 广告
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminSingleProductController")
@RequestMapping("/admin/single/product")
public class SingleProductController extends BaseController {

    @Resource(name = "singleProductServiceImpl")
    private SingleProductService singleProductService;

    @Resource(name = "singleProductPositionServiceImpl")
    private SingleProductPositionService singleProductPositionService;

    @Resource(name = "productServiceImpl")
    private ProductService productService;

    /**
     * 添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Long singleProductPositionId, ModelMap model) {
        model.addAttribute("singleProductPosition", singleProductPositionService.find(singleProductPositionId));
        model.addAttribute("singleProductPositionId", singleProductPositionId);
        return "/admin/single_product/add";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(SingleProduct singleProduct, Long singleProductPositionId,Long linkId, RedirectAttributes redirectAttributes) {
        try {
            singleProduct.setOrder(singleProductService.findOrderMax(singleProductPositionId)+1);
            singleProduct.setProduct(productService.find(linkId));
            singleProduct.setSingleProductPosition(singleProductPositionService.find(singleProductPositionId));
            if (!isValid(singleProduct)) {
                return ERROR_VIEW;
            }
            singleProductService.save(singleProduct);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:list.jhtml";
        }
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model, RedirectAttributes redirectAttributes) {
        SingleProduct singleProduct = singleProductService.find(id);

        if (singleProduct == null) {
            addFlashMessage(redirectAttributes, Message.error("无效的广告编号"));
            return "redirect:list.jhtml";
        }
        model.addAttribute("singleProduct", singleProduct);
        return "/admin/single_product/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Long id, String title,String content,Long linkId, RedirectAttributes redirectAttributes) {
        SingleProduct singleProduct = singleProductService.find(id);
        singleProduct.setProduct(productService.find(linkId));
        singleProduct.setTitle(title);
        singleProduct.setContent(content);
        if (!isValid(singleProduct)) {
            return ERROR_VIEW;
        }
        singleProductService.update(singleProduct);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Long singleProductPositionId, Pageable pageable, ModelMap model) {
        pageable.setSearchProperty("title");
        if (singleProductPositionId != null) {
            SingleProductPosition singleProductPosition = singleProductPositionService.find(singleProductPositionId);
            model.addAttribute("page", singleProductService.findPage(singleProductPosition, pageable));
        } else {
            model.addAttribute("page", singleProductService.findPage(pageable));
        }
        model.addAttribute("singleProductPositionId", singleProductPositionId);
        return "/admin/single_product/list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Message delete(Long[] ids) {
        singleProductService.delete(ids);
        return SUCCESS_MESSAGE;
    }


    /**
     * 根据店铺获取商品列表
     */
    @RequestMapping(value = "/getProductOrTenant", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> getProduct(String searchValue,Long id) {
        List<Map<String, Object>> mapList = new ArrayList<>();

        if(searchValue!=null&&!"".equals(searchValue)){
            List<Product> products = productService.openList(null, null, null, true, true, null, null, null, searchValue, null, null, null, null, null, null, Product.OrderType.dateDesc);
            for (Product product : products) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", product.getId());
                map.put("name", product.getFullName());
                mapList.add(map);
            }
        }


        if(id!=null){
            Product product = productService.find(id);
            Map<String, Object> map = new HashMap<>();
            map.put("id", product.getId());
            map.put("name", product.getFullName());
            mapList.add(map);
        }
        return mapList;
    }
    @Test
    public void test(){
        System.out.println(singleProductService.findOrderMax(16l));
    }

}