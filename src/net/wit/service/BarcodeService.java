/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Barcode;

/**
 * Service - 货品
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface BarcodeService extends BaseService<Barcode, Long> {

	public Barcode findByBarcode(String barcode) ;
	Page<Barcode> openPage(String keyword,Pageable pageable);
}