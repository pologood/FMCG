package net.wit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.wit.Filter;
import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ProductGroupDao;
import net.wit.entity.ProductGroup;
import net.wit.service.ProductGroupService;

/**
 * Service-商品套餐
 * @author Administrator
 *
 */
@Service("productGroupServiceImpl")
public class ProductGroupServiceImpl extends BaseServiceImpl<ProductGroup, Long> implements ProductGroupService {

	@Resource(name = "productGroupDaoImpl")
	private ProductGroupDao productGroupDao;
	
	@Resource(name = "productGroupDaoImpl")
	public void setBaseDao(ProductGroupDao productGroupDao) {
		super.setBaseDao(productGroupDao);
	}

}
