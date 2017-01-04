package net.wit.dao.impl;


import net.wit.Filter;
import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ContactDao;
import net.wit.entity.Area;
import net.wit.entity.Contact;
import net.wit.entity.ContactProduct;
import net.wit.entity.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository("contactDaoImpl")
public class ContactDaoImpl extends BaseDaoImpl<Contact, Long> implements ContactDao {

    public List<Contact> findList(Boolean hasRepaly, Member member, ContactProduct product, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Contact> criteriaQuery = criteriaBuilder.createQuery(Contact.class);
        Root<Contact> root = criteriaQuery.from(Contact.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (hasRepaly != null && hasRepaly) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.get("forContact")));
        }
        if (member != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
        }
        if (product != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("product"), product));
        }
        if (isShow != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isShow"), isShow));
        }
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery, null, count, filters, orders);
    }

    public List<Contact> findList(Member member, ContactProduct product, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Contact> criteriaQuery = criteriaBuilder.createQuery(Contact.class);
        Root<Contact> root = criteriaQuery.from(Contact.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.get("forContact")));
        if (member != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
        }
        if (product != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("product"), product));
        }
        if (isShow != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isShow"), isShow));
        }
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery, null, count, filters, orders);
    }

    public Page<Contact> findPage(Contact.Type type, Member member, ContactProduct product, Boolean isShow, Area area, List<Long> tenants, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Contact> criteriaQuery = criteriaBuilder.createQuery(Contact.class);
        Root<Contact> root = criteriaQuery.from(Contact.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.get("forContact")));
        if (member != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
        }
        //if (product != null) {
        //	restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("product"), product));
        //}
        if (isShow != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isShow"), isShow));
        }
        if (type != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
        }
        if (area != null) {
            restrictions = criteriaBuilder.and(restrictions,
                    criteriaBuilder.or(criteriaBuilder.equal(root.get("member").get("area"), area), criteriaBuilder.like(root.get("member").get("area").<String>get("treePath"), "%" + Area.TREE_PATH_SEPARATOR + area.getId() + Area.TREE_PATH_SEPARATOR + "%")));
        }
        if (tenants != null && tenants.size() > 0) {
            restrictions = criteriaBuilder.and(restrictions, root.get("member").get("tenant").in(tenants));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    public Page<Contact> findMessage(Member member, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Contact> criteriaQuery = criteriaBuilder.createQuery(Contact.class);
        Root<Contact> root = criteriaQuery.from(Contact.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(root.get("forContact")));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
        //restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("forContact").get("member"), member)));
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    public Page<Contact> findMyPage(Member member, ContactProduct product, Boolean isShow, Contact.Type type, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Contact> criteriaQuery = criteriaBuilder.createQuery(Contact.class);
        Root<Contact> root = criteriaQuery.from(Contact.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.get("forContact")));
        if (member != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("product").get("tenant"), member.getTenant()));
        }
        if (product != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("product"), product));
        }
        if (isShow != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isShow"), isShow));
        }
        if (type != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
        }

        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    public Long count(Member member, ContactProduct product, Boolean isShow, Contact.Type type) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Contact> criteriaQuery = criteriaBuilder.createQuery(Contact.class);
        Root<Contact> root = criteriaQuery.from(Contact.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNull(root.get("forContact")));
        if (member != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
        }
        if (product != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("product"), product));
        }
        if (isShow != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isShow"), isShow));
        }
        if (type != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), type));
        }

        criteriaQuery.where(restrictions);
        return super.count(criteriaQuery, null);
    }

//	@Override
//	public List<Contact> findListByArticle(Article article) {
////		if (article == null) {
////			return new ArrayList<Contact>();
////		}
////		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
////		CriteriaQuery<Contact> criteriaQuery = criteriaBuilder.createQuery(Contact.class);
////		Root<Contact> root = criteriaQuery.from(Contact.class);
////		criteriaQuery.select(root);
////		Predicate restrictions = criteriaBuilder.conjunction();
////		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("article"), article));
////		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isShow"), true));
////		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("type"), Type.article));
////		criteriaQuery.where(restrictions);
////		List<Order> orders = new ArrayList<Order>();
////		orders.add(new Order("createDate", net.wit.Order.Direction.desc));
////		return super.findList(criteriaQuery, null, null, null, orders);
//		return null;
//	}
}
