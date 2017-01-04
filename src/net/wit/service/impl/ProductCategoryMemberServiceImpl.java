package net.wit.service.impl;

import net.wit.dao.ProductCategoryMemberDao;
import net.wit.entity.ProductCategoryMember;
import net.wit.service.ProductCategoryMemberService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * ..
 * Created by WangChao on 2016-5-19.
 */
@Service("productCategoryMemberServiceImpl")
public class ProductCategoryMemberServiceImpl extends BaseServiceImpl<ProductCategoryMember, Long> implements ProductCategoryMemberService{
    @Resource(name = "productCategoryMemberDaoImpl")
    private ProductCategoryMemberDao productCategoryMemberDao;

    @Resource(name = "productCategoryMemberDaoImpl")
    public void setBaseDao(ProductCategoryMemberDao productCategoryMemberDao) {
        super.setBaseDao(productCategoryMemberDao);
    }

}
