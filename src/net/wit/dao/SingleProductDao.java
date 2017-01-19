package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.SingleProduct;
import net.wit.entity.SingleProductPosition;

/**
 * Created by Administrator on 16/11/30.
 */
public interface SingleProductDao extends BaseDao<SingleProduct,Long> {

    Page<SingleProduct> findPage(SingleProductPosition productPosition, Pageable pageable);

    Integer findOrderMax(Long id);
}
