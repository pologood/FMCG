package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.SingleProductDao;
import net.wit.entity.SingleProduct;
import net.wit.entity.SingleProductPosition;
import net.wit.service.SingleProductService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 16/11/30.
 */
@Service("singleProductServiceImpl")
public class SingleProductServiceImpl extends BaseServiceImpl<SingleProduct,Long> implements SingleProductService {

    @Resource(name = "singleProductDaoImpl")
    private SingleProductDao singleProductDao;

    @Resource(name = "singleProductDaoImpl")
    public void setBaseDao(SingleProductDao singleProductDao) {
        super.setBaseDao(singleProductDao);
    }


    public Page<SingleProduct> findPage(SingleProductPosition productPosition, Pageable pageable){
        return singleProductDao.findPage(productPosition,pageable);
    }
}
