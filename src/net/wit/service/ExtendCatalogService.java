/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.ExtendCatalog;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.Tenant;

import java.awt.*;
import java.util.Map;

/**
 * Service - 分享商品
 * 
 * @author rsico Team
 * @version 3.0
 */
public interface ExtendCatalogService extends BaseService<ExtendCatalog, Long> {

    /**
     * 查询分享商品
     */
    ExtendCatalog findExtendCatalog(Member member,Tenant tenant,Product product);
    Page<ExtendCatalog> findPage(Product product,Pageable pageable);

    /**
     * 查询销售金额
     */
    Page<Map<String, Object>> findExtendCatalog(Member member, Pageable pageable);
}