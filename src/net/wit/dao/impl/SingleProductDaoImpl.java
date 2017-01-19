package net.wit.dao.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.SingleProductDao;
import net.wit.entity.Ad;
import net.wit.entity.Payment;
import net.wit.entity.SingleProduct;
import net.wit.entity.SingleProductPosition;
import org.springframework.stereotype.Repository;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
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

    public Integer findOrderMax(Long singleProductPositionId){
        String jpql = "select max(s.orders) from xx_single_product s where s.single_product_position=:id";
        try {
            Object tid = entityManager.createNativeQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("id", singleProductPositionId).getSingleResult();
            return Integer.valueOf(tid.toString());
        } catch (Exception e) {
            System.out.println(e);
            return 0;
        }
    }
}
