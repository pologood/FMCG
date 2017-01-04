/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Barcode;

/**
 * Dao - 条码库
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface BarcodeDao extends BaseDao<Barcode, Long> {

	public Barcode findByBarcode(String barcode) ;
	public Page<Barcode> openPage(String keyword,Pageable pageable);
	
}