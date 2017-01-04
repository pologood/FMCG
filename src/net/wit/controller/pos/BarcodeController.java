/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.pos;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import net.wit.Message;
import net.wit.entity.Barcode;
import net.wit.service.BarcodeService;
import net.wit.util.JsonUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 条码
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("barcodeController")
@RequestMapping("/barcode")
public class BarcodeController extends BaseController {

	@Resource(name = "barcodeServiceImpl")
	private BarcodeService barcodeService;

	/**
	 * 扫码 
	 */
	@RequestMapping(value = "/get/{barcode}", method = RequestMethod.GET)
	public @ResponseBody
	Message get(@PathVariable String barcode) {
		Barcode entity = barcodeService.findByBarcode(barcode);
		if (entity == null) {
			return Message.error("404");
		}
		String jsonString = JsonUtils.toJson(entity);
		return Message.success(jsonString);
	
	}

	/**
	 * 上传条码
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public @ResponseBody
	Message create(String name, String spell, String barcode, String unitName, BigDecimal inPrice, BigDecimal outPrice, Long barcodeCategoryId) throws UnsupportedEncodingException {

	   //BarcodeCategory category = barcodeCategoryService.find(barcodeCategoryId);
//	   if (category == null) {
//		   return Message.warn("100");
//	   }
//	   Barcode entity = barcodeService.findByBarcode(barcode);
//	   if (entity != null) {
//		   return Message.warn("000");
//	   };
//	   entity = new Barcode();
//	   entity.setBarcode(barcode);
//	   entity.setName(name);
//	   entity.setSpell(spell);
//	   entity.setUnitName(unitName);
//	   entity.setInPrice(inPrice);
//	   entity.setOutPrice(outPrice);
//	   entity.setProductCategory(category);
//
//	   barcodeService.save(entity);
	   
	   return Message.success("000");
	}

	/**
	 * 获取分类
	 */
//	@RequestMapping(value = "/getCategorys", method = RequestMethod.GET)
//	public @ResponseBody
//	Message getCategory() {
//		List<BarcodeCategory> list = barcodeCategoryService.findAll();
//		if (list == null) {
//			return Message.error("404");
//		}
//		String jsonString = JsonUtils.toJson(list);
//		return Message.success(jsonString);
//	}
}