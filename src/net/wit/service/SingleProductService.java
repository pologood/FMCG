package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.SingleProduct;
import net.wit.entity.SingleProductPosition;

/**
 * Created by Administrator on 16/11/30.
 */
public interface SingleProductService extends BaseService<SingleProduct,Long> {

    Page<SingleProduct> findPage(SingleProductPosition productPosition, Pageable pageable);
}
