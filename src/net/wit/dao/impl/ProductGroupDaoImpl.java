package net.wit.dao.impl;

import java.util.List;

import javax.persistence.LockModeType;

import org.springframework.stereotype.Repository;

import net.wit.Filter;
import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ProductGroupDao;
import net.wit.entity.ProductGroup;
/**
 * Dao-商品套餐
 * @author Administrator
 *
 */
@Repository("productGroupDaoImpl")
public class ProductGroupDaoImpl extends BaseDaoImpl<ProductGroup, Long> implements ProductGroupDao {

	
}
