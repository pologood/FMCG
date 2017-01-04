/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.MemberDao;
import net.wit.entity.*;
import net.wit.entity.Idcard.AuthStatus;
import net.wit.entity.Order;
import net.wit.entity.Order.OrderStatus;
import net.wit.entity.Order.PaymentStatus;
import net.wit.util.MapUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * Dao - 会员
 *
 * @author rsico Team
 * @version 3.0
 */
@Repository("memberDaoImpl")
public class MemberDaoImpl extends BaseDaoImpl<Member, Long> implements MemberDao {

    public boolean usernameExists(String username) {
        if (username == null) {
            return false;
        }
        String jpql = "select count(members) from Member members where lower(members.username) = lower(:username)";
        Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("username", username).getSingleResult();
        return count > 0;
    }

    public Member findByWechatId(String wechatId) {
        if (wechatId == null) {
            return null;
        }
        try {
            String jpql = "select members from Member members where lower(members.wechatId) = lower(:wechatId)";
            return entityManager.createQuery(jpql, Member.class).setParameter("wechatId", wechatId).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public boolean emailExists(String email) {
        if (email == null) {
            return false;
        }
        String jpql = "select count(members) from Member members where lower(members.email) = lower(:email)";
        Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("email", email).getSingleResult();
        return count > 0;
    }

    public boolean mobileExists(String mobile) {
        if (mobile == null) {
            return false;
        }
        String jpql = "select count(members) from Member members where members.mobile = :mobile and members.bindMobile = 1 ";
        Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("mobile", mobile).getSingleResult();
        return count > 0;
    }

    public Member findByUsername(String username) {
        if (username == null) {
            return null;
        }
        try {
            String jpql = "select members from Member members where lower(members.username) = lower(:username)";
            return entityManager.createQuery(jpql, Member.class).setFlushMode(FlushModeType.COMMIT).setParameter("username", username).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Member> findListByEmail(String email) {
        if (email == null) {
            return Collections.<Member>emptyList();
        }
        String jpql = "select members from Member members where lower(members.email) = lower(:email)";
        return entityManager.createQuery(jpql, Member.class).setFlushMode(FlushModeType.COMMIT).setParameter("email", email).getResultList();
    }

    public List<Object[]> findPurchaseList(Date beginDate, Date endDate, Integer count) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Member> member = criteriaQuery.from(Member.class);
        Join<Product, Order> orders = member.join("orders");
        criteriaQuery.multiselect(member.get("id"), member.get("username"), member.get("email"), member.get("point"), member.get("amount"), member.get("balance"), criteriaBuilder.sum(orders.<BigDecimal>get("amountPaid")));
        Predicate restrictions = criteriaBuilder.conjunction();
        if (beginDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(orders.<Date>get("createDate"), beginDate));
        }
        if (endDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(orders.<Date>get("createDate"), endDate));
        }
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(orders.get("orderStatus"), OrderStatus.completed), criteriaBuilder.equal(orders.get("paymentStatus"), PaymentStatus.paid));
        criteriaQuery.where(restrictions);
        criteriaQuery.groupBy(member.get("id"), member.get("username"), member.get("email"), member.get("point"), member.get("amount"), member.get("balance"));
        criteriaQuery.orderBy(criteriaBuilder.desc(criteriaBuilder.sum(orders.<BigDecimal>get("amountPaid"))));
        TypedQuery<Object[]> query = entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
        if (count != null && count >= 0) {
            query.setMaxResults(count);
        }
        return query.getResultList();
    }

    public Page<Member> findPage(Member member, Pageable pageable) {
        if (member == null) {
            return new Page<Member>(Collections.<Member>emptyList(), 0, pageable);
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
        Root<Member> root = criteriaQuery.from(Member.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), member.getTenant()));
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    public Page<Member> findPageMyMember(Member member, Pageable pageable) {
        if (member == null) {
            return new Page<Member>(Collections.<Member>emptyList(), 0, pageable);
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
        Root<Member> root = criteriaQuery.from(Member.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    public List<Member> findList(Member member) {
        if (member == null) {
            return new ArrayList<Member>(Collections.<Member>emptyList());
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
        Root<Member> root = criteriaQuery.from(Member.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery, null, null, null, null);
    }

    public List<Member> findListEmployee(Member member) {
        if (member == null) {
            return new ArrayList<Member>(Collections.<Member>emptyList());
        }
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return new ArrayList<Member>(Collections.<Member>emptyList());
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
        Root<Member> root = criteriaQuery.from(Member.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), member.getTenant()));
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery, null, null, null, null);

    }

    public Page<Member> findPage(Tenant tenant,String keyword ,Pageable pageable) {
        if (tenant == null) {
            return new Page<Member>(Collections.<Member>emptyList(), 0, pageable);
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
        Root<Member> root = criteriaQuery.from(Member.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("tenant"), tenant));
        if (StringUtils.isNotBlank(keyword)) { // 拼音条件
            restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.or(criteriaBuilder.like(root.<String> get("name"), "%" + keyword + "%")));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    public Page<Member> findRealnameMemberPage(Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
        Root<Member> root = criteriaQuery.from(Member.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(root.get("idcard")), criteriaBuilder.equal(root.get("idcard").get("authStatus"), Idcard.AuthStatus.wait));
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    public Member findByTel(String mobile) {
        if (mobile == null) {
            return null;
        }
        try {
            String jpql = "select members from Member members where lower(members.mobile) = lower(:mobile)";
            return entityManager.createQuery(jpql, Member.class).setFlushMode(FlushModeType.COMMIT).setParameter("mobile", mobile).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Member findByEmail(String email) {
        if (email == null) {
            return null;
        }
        try {
            String jpql = "select members from Member members where lower(members.email) = lower(:email)";
            return entityManager.createQuery(jpql, Member.class).setFlushMode(FlushModeType.COMMIT).setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public boolean emailExistsWithoutUser(String email, Member member) {
        if (email == null) {
            return false;
        }
        String jpql = "select count(members) from Member members where lower(members.email) = lower(:email) and members.id <> :id";
        Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("email", email).setParameter("id", member.getId()).getSingleResult();
        return count > 0;
    }

    public boolean mobileExistsWithoutUser(String mobile, Member member) {
        if (mobile == null) {
            return false;
        }
        String jpql = "select count(members) from Member members where members.mobile = :mobile and members.id <> :id";
        Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("mobile", mobile).setParameter("id", member.getId()).getSingleResult();
        return count > 0;
    }

    public Member findByBindTel(String mobile) {
        if (mobile == null) {
            return null;
        }
        try {
            String jpql = "select members from Member members where lower(members.mobile) = lower(:mobile) and members.bindMobile = 1";
            return entityManager.createQuery(jpql, Member.class).setFlushMode(FlushModeType.COMMIT).setParameter("mobile", mobile).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Member findByBindEmail(String email) {
        if (email == null) {
            return null;
        }
        try {
            String jpql = "select members from Member members where lower(members.email) = lower(:email) and members.bindEmail = 1";
            return entityManager.createQuery(jpql, Member.class).setFlushMode(FlushModeType.COMMIT).setParameter("email", email).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Page<Member> findFavoritePage(Member member, Pageable pageable) {
        if (member == null) {
            return new Page<Member>(Collections.<Member>emptyList(), 0, pageable);
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
        Root<Member> root = criteriaQuery.from(Member.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.join("favoriteTenants"), member.getTenant()));
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }
    public List<Member> findFavoriteList(Member member) {
        if (member == null) {
            return new ArrayList<Member>(Collections.<Member>emptyList());
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
        Root<Member> root = criteriaQuery.from(Member.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.join("favoriteTenants"), member.getTenant()));
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery, null, null, null, null);
    }
    /**
     * 查找附近的人
     *
     * @param location 位置
     * @param pageable 分页信息
     * @return 收藏商家分页
     */
    public Page<Member> findNearBy(Location location, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
        Root<Member> root = criteriaQuery.from(Member.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        MapUtils mapUtils = new MapUtils(3000);
        mapUtils.rectangle4Point(location.getLat().doubleValue(), location.getLng().doubleValue());
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.get("location").<BigDecimal>get("lat"), mapUtils.getLeft_bottom().getLat()));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.get("location").<BigDecimal>get("lat"), mapUtils.getLeft_top().getLat()));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.get("location").<BigDecimal>get("lng"), mapUtils.getLeft_top().getLng()));
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.get("location").<BigDecimal>get("lng"), mapUtils.getRight_bottom().getLng()));

        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    @Override
    public Page<Member> findRealnameMemberPage(AuthStatus authStatus, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
        Root<Member> root = criteriaQuery.from(Member.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(root.get("idcard")));
        if (authStatus != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("idcard").get("authStatus"), authStatus));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    /**
     * 我发展会员的累计销售
     */
    public BigDecimal sumExtAmount(Member member) {
        if (member == null) {
            return BigDecimal.ZERO;
        }
        String jpql = "select sum(member.amount) from Member member where member.member = :member";
        try {
            BigDecimal sumAmount = entityManager.createQuery(jpql, BigDecimal.class).setFlushMode(FlushModeType.COMMIT).setParameter("member", member).getSingleResult();
            return sumAmount;
        } catch (Exception E) {
            return BigDecimal.ZERO;
        }
    }

    /**
     * 我发展会员的累计佣金返利
     */
    public BigDecimal sumExtProfit(Member member) {
        if (member == null) {
            return BigDecimal.ZERO;
        }
        String jpql = "select sum(selfAmount) from Profit profit where member.member = :member";
        try {
            BigDecimal sumAmount = entityManager.createQuery(jpql, BigDecimal.class).setFlushMode(FlushModeType.COMMIT).setParameter("member", member).getSingleResult();
            return sumAmount;
        } catch (Exception E) {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public Page<Member> findPage(AuthStatus authStatus, Date beginDate,
                                 Date endDate, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
        Root<Member> root = criteriaQuery.from(Member.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(root.get("idcard")));
        if (authStatus != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("idcard").get("authStatus"), authStatus));
        }
        if (beginDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createDate"), beginDate));
        }
        if (endDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createDate"), endDate));
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    @Override
    public Page<Member> findPage(Date beginDate, Date endDate, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
        Root<Member> root = criteriaQuery.from(Member.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (beginDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("createDate"), beginDate));
        }
        if (endDate != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date>get("createDate"), endDate));
        }
        if (pageable.getSearchValue() != null) {
            restrictions =  criteriaBuilder.and(restrictions,criteriaBuilder.or(
                    criteriaBuilder.like(root.<String>get("username"), "%" + pageable.getSearchValue() + "%"),
                    criteriaBuilder.like(root.<String>get("mobile"), "%" + pageable.getSearchValue() + "%"),
                    criteriaBuilder.like(root.<String>get("name"), "%" + pageable.getSearchValue() + "%")));
        }

        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    @Override
    public List<Member> findByArea(List<Area> area) {
        CriteriaBuilder critBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Member> criteriaQuery = critBuilder.createQuery(Member.class);
        Root<Member> root = criteriaQuery.from(Member.class);
        criteriaQuery.select(root);
        if (area.size() != 0) {
            criteriaQuery.where(root.get("area").in(area));
        }
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Member> findByLikeUserName(String userName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
        Root<Member> root = criteriaQuery.from(Member.class);
        criteriaQuery.select(root);
        if (userName != null && !"".equals(userName)) {
            criteriaQuery.where(criteriaBuilder.like(root.<String>get("username"), "%" + userName + "%"));
        }
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Member> findByCondition(List<Area> area, String userName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
        Root<Member> root = criteriaQuery.from(Member.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (area.size() != 0) {
            restrictions = criteriaBuilder.and(restrictions, root.get("area").in(area));
        }
        if (userName != null && !"".equals(userName)) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.<String>get("username"), "%" + userName + "%"));
        }
        criteriaQuery.where(restrictions);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    public List<Member> findFans(Member member) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
        Root<Member> root = criteriaQuery.from(Member.class);
        criteriaQuery.select(root);
        Subquery<Member> subquery = criteriaQuery.subquery(Member.class);
        Root<Member> subqueryRoot = subquery.from(Member.class);
        subquery.select(subqueryRoot);
        subquery.where(criteriaBuilder.equal(subqueryRoot, root), criteriaBuilder.equal(subqueryRoot.join("favoriteMembers"), member));
        criteriaQuery.where(criteriaBuilder.exists(subquery));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
    public Page<Member> findFanPage(Member member,Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
        Root<Member> root = criteriaQuery.from(Member.class);
        criteriaQuery.select(root);
        Subquery<Member> subquery = criteriaQuery.subquery(Member.class);
        Root<Member> subqueryRoot = subquery.from(Member.class);
        subquery.select(subqueryRoot);
        subquery.where(criteriaBuilder.equal(subqueryRoot, root), criteriaBuilder.equal(subqueryRoot.join("favoriteMembers"), member));
        criteriaQuery.where(criteriaBuilder.exists(subquery));
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("username")));
        return super.findPage(criteriaQuery,pageable);
    }
    public Long findGuiderStar(Tenant tenant){
        String sql ="SELECT e.member FROM xx_employee e inner join xx_employee_tag t" +
                    " on e.id = t.xx_employee" +
                    " where e.tenant=:id and t.tags='33' order by rand() limit 1";
        Query query = entityManager.createNativeQuery(sql).setFlushMode(FlushModeType.COMMIT).setParameter("id", tenant.getId());
        return Long.parseLong(query.getSingleResult().toString());
    }

    @Override
    public Page<Member> findByAddPage(Member member, Date beginDate, Date endDate, Pageable pageable) {
        if (member == null) {
            return new Page<Member>(Collections.<Member>emptyList(), 0, pageable);
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
        Root<Member> root = criteriaQuery.from(Member.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
        if(beginDate!=null){
        restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"),beginDate));
        }
        if(endDate!=null){
            restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"),endDate));
        }
        criteriaQuery.where(restrictions);
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));
        return super.findPage(criteriaQuery, pageable);
    }

    @Override
    public List<Member> findByAddList(Member member, Date beginDate, Date endDate) {
        if (member == null) {
            return new ArrayList<Member>(Collections.<Member>emptyList());
        }
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
        Root<Member> root = criteriaQuery.from(Member.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
        if(beginDate!=null){
            restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"),beginDate));
        }
        if(endDate!=null){
            restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"),endDate));
        }
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery, null, null, null, null);
    }

    public List<Member> memberListExport(Date beginDate, Date endDate,String keywords) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
        Root<Member> root = criteriaQuery.from(Member.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if(beginDate!=null){
            restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"),beginDate));
        }
        if(endDate!=null){
            restrictions = criteriaBuilder.and(restrictions,criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"),endDate));
        }
        if (keywords != null) {
            restrictions =  criteriaBuilder.and(restrictions,criteriaBuilder.or(
                    criteriaBuilder.like(root.<String>get("username"), "%" + keywords + "%"),
                    criteriaBuilder.like(root.<String>get("mobile"), "%" + keywords + "%"),
                    criteriaBuilder.like(root.<String>get("name"), "%" + keywords + "%")));
        }
        criteriaQuery.where(restrictions);
        return super.findList(criteriaQuery, null, null, null, null);
    }
}