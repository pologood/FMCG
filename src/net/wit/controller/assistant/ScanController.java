/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.assistant;

import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.ScanModel;
import net.wit.controller.app.model.ScanModel.Type;
import net.wit.entity.Product;
import net.wit.entity.Qrcode;
import net.wit.entity.Tenant;
import net.wit.entity.Trade;
import net.wit.service.ProductService;
import net.wit.service.QrcodeService;
import net.wit.service.TenantService;
import net.wit.service.TradeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Controller - 扫一扫
 * @author rsico Team
 * @version 3.0
 */
@Controller("assistantScanCommonController")
@RequestMapping("/assistant/scan")
public class ScanController {

	@Resource(name = "qrcodeServiceImpl")
	private QrcodeService qrcodeService;
	
	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;
	
	@Resource(name = "tradeServiceImpl")
	private TradeService tradeService;

	@Resource(name = "productServiceImpl")
	private ProductService productService;

	/**
	 * 根据信息
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	public @ResponseBody DataBlock scan(String code,Long tenantId) {
		if (code==null) {
			return DataBlock.error("无效编码");
		}
		Tenant tenant = null;
		if (tenantId!=null) {
			tenant = tenantService.find(tenantId);
		}
		ScanModel model = new ScanModel();
		String flag = code.substring(0,4).toLowerCase();
		if (flag.equals("http") ) {
			Qrcode qrcode = qrcodeService.findbyUrl(code);
			if (qrcode==null) {
				model.setId(null);
				model.setType(Type.webview);
				model.setUrl(code);
			} else {
				model.setId(qrcode.getTenant().getId());
				model.setType(Type.tenant);
				model.setUrl("rzico://tenant?id="+qrcode.getTenant().getId());
				model.setName(qrcode.getTenant().getName());
			}
		} else if (tenant!=null){
			if (flag.equals("bill") ) {
				String sn = code.substring(5);
			    Trade trade = tradeService.findBySn(sn, tenant);
			    if (trade!=null) {
					model.setId(trade.getId());
					model.setType(Type.trade);
					model.setUrl("rzico://bill?sn="+sn); 
					model.setName(null);
			    } else {
					return DataBlock.error("无法识别的二维码/条码");
			    }
			} else  {
				List<Product> products = productService.findByBarcode(tenant, code);
				if (products.size()>0) {
					model.setId(products.get(0).getId());
					model.setType(Type.product);
					model.setUrl("rzico://product?id="+products.get(0).getId());
				} else {
				    Trade trade = tradeService.findBySn(code, tenant);
				    if (trade!=null) {
						model.setId(trade.getId());
						model.setType(Type.trade);
						model.setUrl("rzico://bill?sn="+code);
						model.setName(null);
				    } else {
						return DataBlock.error("无法识别的二维码/条码");
				    }
				}
			}			
		} else {
			return DataBlock.error("无法识别的二维码/条码");
		}
		return DataBlock.success(model, "验证成功");
	}

}