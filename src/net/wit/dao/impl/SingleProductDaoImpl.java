package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.SingleProductDao;
import net.wit.entity.Ad;
import net.wit.entity.SingleProduct;
import net.wit.entity.SingleProductPosition;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by Administrator on 16/11/30.
 */
@Repository("singleProductDaoImpl")
public class SingleProductDaoImpl extends BaseDaoImpl<SingleProduct,Long> implements SingleProductDao {


    public Page<SingleProduct> findPage(SingleProductPosition singleProductPosition, Pageable pageable){

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<SingleProduct> criteriaQuery = criteriaBuilder.createQuery(SingleProduct.class);
        Root<SingleProduct> root = criteriaQuery.from(SingleProduct.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("singleProductPosition"), singleProductPosition));
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);

    }
}
