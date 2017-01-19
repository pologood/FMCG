/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.LockModeType;
import javax.servlet.http.HttpServletRequest;

import net.wit.Filter;
import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.DeliveryCenterDao;
import net.wit.dao.MemberDao;
import net.wit.dao.ProductDao;
import net.wit.dao.ReviewDao;
import net.wit.dao.TenantDao;
import net.wit.dao.TradeDao;
import net.wit.entity.*;
import net.wit.entity.Review.Flag;
import net.wit.entity.Review.Type;
import net.wit.service.MessageService;
import net.wit.service.ReviewService;
import net.wit.service.StaticService;

import net.wit.support.EntitySupport;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service - 评论
 *
 * @author rsico Team
 * @version 3.0
 */
@Service("reviewServiceImpl")
public class ReviewServiceImpl extends BaseServiceImpl<Review, Long> implements ReviewService {

    @Resource(name = "reviewDaoImpl")
    private ReviewDao reviewDao;

    @Resource(name = "productDaoImpl")
    private ProductDao productDao;

    @Resource(name = "tenantDaoImpl")
    private TenantDao tenantDao;

    @Resource(name = "memberDaoImpl")
    private MemberDao memberDao;

    @Resource(name = "staticServiceImpl")
    private StaticService staticService;

    @Resource(name = "tradeDaoImpl")
    private TradeDao tradeDao;

    @Resource(name = "deliveryCenterDaoImpl")
    private DeliveryCenterDao deliveryCenterDao;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    @Resource(name = "reviewDaoImpl")
    public void setBaseDao(ReviewDao reviewDao) {
        super.setBaseDao(reviewDao);
    }

    @Transactional(readOnly = true)
    public List<Review> findList(Member member, Product product, Type type, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders) {
        return reviewDao.findList(member, product, type, isShow, count, filters, orders);
    }

    @Transactional(readOnly = true)
    @Cacheable("review")
    public List<Review> findList(Member member, Product product, Type type, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders, String cacheRegion) {
        return reviewDao.findList(member, product, type, isShow, count, filters, orders);
    }

    @Transactional(readOnly = true)
    public Page<Review> findPage(Member member, Product product, Type type, Boolean isShow, Pageable pageable) {
        return reviewDao.findPage(member, product, type, isShow, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Review> findTenantPage(Tenant tenant, Type type, Boolean isShow, Pageable pageable) {
        return reviewDao.findTenantPage(tenant, type, isShow, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Review> findMyTenantPage(String searchValue, Tenant tenant, Type type, Boolean isShow, Pageable pageable) {
        return reviewDao.findMyTenantPage(searchValue, tenant, type, isShow, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Review> findMyPage(Member member, Product product, Type type, Boolean isShow, Pageable pageable) {
        return reviewDao.findMyPage(member, product, type, isShow, pageable);
    }

    @Transactional(readOnly = true)
    public Long count(Member member, Product product, Type type, Boolean isShow) {
        return reviewDao.count(member, product, type, isShow);
    }

    @Transactional(readOnly = true)
    public boolean isReviewed(Member member, Product product) {
        return reviewDao.isReviewed(member, product);
    }

    @Transactional(readOnly = true)
    public boolean hasReviewed(Member member, Trade trade) {
        return reviewDao.hasReviewed(member, trade);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "productCategory", "review", "consultation"}, allEntries = true)
    public void save(Review review) {
        super.save(review);
        if (review.getFlag().equals(Flag.product)) {
            Product product = review.getProduct();
            if (product != null) {
                reviewDao.flush();
                long totalScore = reviewDao.calculateTotalScore(product);
                long scoreCount = reviewDao.calculateScoreCount(product);
                product.setTotalScore(totalScore);
                product.setScoreCount(scoreCount);
                productDao.merge(product);
                reviewDao.flush();
                staticService.build(product);
            }
        } else if (review.getFlag().equals(Flag.tenant)) {
            Tenant tenant = review.getTenant();
            if (tenant != null) {
                reviewDao.flush();
                long totalScore = reviewDao.calculateTotalScore(tenant);
                long scoreCount = reviewDao.calculateScoreCount(tenant);
                tenant.setTotalScore(totalScore);
                tenant.setScoreCount(scoreCount);
                tenantDao.merge(tenant);
                reviewDao.flush();
            }
        } else if (review.getFlag().equals(Flag.trade)) {
            if (review.getOrderItem() != null) { // 订单商品评价
                reviewDao.flush();
                Product product = review.getOrderItem().getProduct();
                product.setTotalScore(product.getTotalScore() + review.getScore());
                product.setScoreCount(product.getScoreCount() + 1);
                productDao.merge(product);
                reviewDao.flush();
                staticService.build(product);
            } else if (review.getMemberTrade() != null) { // 买家订单评价
                Tenant tenant = review.getMemberTrade().getTenant();
                review.setTenant(tenant);

                tenant.setTotalScore(tenant.getTotalScore() + review.getScore());
                if (review.getAssistant() == null) {
                    tenant.setTotalAssistant(tenant.getTotalAssistant() + review.getScore());
                } else {
                    tenant.setTotalAssistant(tenant.getTotalAssistant() + review.getAssistant());
                }
                tenant.setScoreCount(tenant.getScoreCount() + 1);
                tenantDao.merge(tenant);
                reviewDao.flush();
            }
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "productCategory", "review", "consultation"}, allEntries = true)
    public Review update(Review review) {
        Review pReview = super.update(review);
        if (review.getFlag().equals(Flag.product)) {
            Product product = review.getProduct();
            if (product != null) {
                reviewDao.flush();
                long totalScore = reviewDao.calculateTotalScore(product);
                long scoreCount = reviewDao.calculateScoreCount(product);
                product.setTotalScore(totalScore);
                product.setScoreCount(scoreCount);
                productDao.merge(product);
                reviewDao.flush();
                staticService.build(product);
            }
        } else {
            Tenant tenant = review.getTenant();
            if (tenant != null) {
                reviewDao.flush();
                long totalScore = reviewDao.calculateTotalScore(tenant);
                long scoreCount = reviewDao.calculateScoreCount(tenant);
                tenant.setTotalScore(totalScore);
                tenant.setScoreCount(scoreCount);
                tenantDao.merge(tenant);
                reviewDao.flush();
            }
        }
        return pReview;
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "productCategory", "review", "consultation"}, allEntries = true)
    public Review update(Review review, String... ignoreProperties) {
        return super.update(review, ignoreProperties);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "productCategory", "review", "consultation"}, allEntries = true)
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "productCategory", "review", "consultation"}, allEntries = true)
    public void delete(Long... ids) {
        super.delete(ids);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"product", "productCategory", "review", "consultation"}, allEntries = true)
    public void delete(Review review) {
        if (review != null) {
            super.delete(review);
            if (review.getFlag().equals(Flag.product)) {
                Product product = review.getProduct();
                if (product != null) {
                    reviewDao.flush();
                    long totalScore = reviewDao.calculateTotalScore(product);
                    long scoreCount = reviewDao.calculateScoreCount(product);
                    product.setTotalScore(totalScore);
                    product.setScoreCount(scoreCount);
                    productDao.merge(product);
                    reviewDao.flush();
                    staticService.build(product);
                }
            } else {
                Tenant tenant = review.getTenant();
                if (tenant != null) {
                    reviewDao.flush();
                    long totalScore = reviewDao.calculateTotalScore(tenant);
                    long scoreCount = reviewDao.calculateScoreCount(tenant);
                    tenant.setTotalScore(totalScore);
                    tenant.setScoreCount(scoreCount);
                    tenantDao.merge(tenant);
                    reviewDao.flush();
                }
            }
        }
    }

    @Transactional
    public void reviewTrade(String type, Member member, Trade trade, OrderItem orderItem, Review review) {
        // Assert.isNull(trade);
        // Assert.isNull(member);
        // Assert.isNull(review);
        if (review.getFlag().equals(Flag.trade)&&trade!=null) {
            //对订单评价
            tradeDao.lock(trade, LockModeType.PESSIMISTIC_WRITE);
            if (hasReviewed(member, trade)) {
                return;
            }
            if (review.getTenant() == null) {
                review.setTenant(trade.getTenant());
            }
            review.setMemberTrade(trade);
            if(review.getContent()==null){
                review.setContent("");
            }
            reviewDao.persist(review);
            //更新店铺评分
            Tenant tenant = review.getTenant();
            tenant.setTotalScore(tenant.getTotalScore() + review.getScore());
            tenant.setScoreCount(tenant.getScoreCount() + 1);
            if (tenant.getScoreCount() != 0 && tenant.getTotalScore() != null) {
                tenant.setScore((float) (tenant.getTotalScore() / tenant.getScoreCount()));
            }
            tenant.setTotalAssistant(review.getAssistant() + tenant.getTotalAssistant());
            tenantDao.merge(tenant);

            if(type.equals("weixin")||type.equals("b2c")){
                for (OrderItem item : trade.getOrderItems()) {
                    if(item.getReview()!=null){
                        continue;
                    }
                    //对商品评价
                    Product product = productDao.findBySn(item.getSn());
                    if (product != null) {
                        Review productReview = new Review();
                        productReview.setAssistant(review.getAssistant());
                        productReview.setContent(review.getContent());
                        productReview.setFlag(Flag.product);
                        productReview.setIp(review.getIp());
                        productReview.setIsShow(review.getIsShow());
                        productReview.setOrderItem(item);
                        productReview.setMember(member);
                        productReview.setProduct(product);
                        productReview.setScore(review.getScore());
                        productReview.setTenant(product.getTenant());
                        productReview.setType(review.getType());

                        List<ProductImage> productImages=new ArrayList<ProductImage>();
                        for(ProductImage productImage:review.getImages()){
                            productImages.add(productImage);
                        }

                        productReview.setImages(productImages);
                        reviewDao.persist(productReview);
                        // 更新商品评分
                        Long scoreCount = product.getScoreCount();
                        if (scoreCount == null) {
                            product.setScoreCount(1l);
                        } else {
                            product.setScoreCount(scoreCount + 1);
                        }
                        Long totalScore = product.getTotalScore();
                        if (totalScore == null) {
                            product.setTotalScore(Long.valueOf(review.getScore()));
                            product.setScore(0f);
                        } else {
                            product.setTotalScore(totalScore + review.getScore());
                            product.setScore((float) totalScore / product.getScoreCount());
                        }
                        product.getReviews().add(productReview);
                        productDao.merge(product);
                    }
                }
            }
            Message message= EntitySupport.createInitMessage(Message.Type.review,review.getContent(),trade.getOrder().getSn(),trade.getTenant().getMember(),null);
            message.setTrade(review.getMemberTrade());
            message.setWay(Message.Way.tenant);
            message.setTitle(member.getDisplayName() + "对你进行新的评价啦！");
            messageService.save(message);

        }
        if(review.getFlag().equals(Flag.product)&&orderItem!=null){
            //对商品评价
            if(orderItem.getReview()!=null){
                return;
            }
            Product product = productDao.findBySn(orderItem.getSn());
            review.setOrderItem(orderItem);
            review.setProduct(product);
            review.setTenant(product.getTenant());
            reviewDao.persist(review);
            if (product!=null) {
                //更新商品评分
                Long scoreCount = product.getScoreCount();
                if (scoreCount == null) {
                    product.setScoreCount(1l);
                } else {
                    product.setScoreCount(scoreCount + 1);
                }
                Long totalScore = product.getTotalScore();
                if (totalScore == null) {
                    product.setTotalScore(Long.valueOf(review.getScore()));
                    product.setScore(0f);
                } else {
                    product.setTotalScore(totalScore + review.getScore());
                    product.setScore((float) totalScore / product.getScoreCount());
                }
                product.getReviews().add(review);
                productDao.merge(product);
            }
            Message message= EntitySupport.createInitMessage(Message.Type.review,review.getContent(),orderItem.getOrder().getSn(),orderItem.getTrade().getTenant().getMember(),null);
            message.setTrade(orderItem.getTrade());
            message.setWay(Message.Way.tenant);
            message.setTitle(member.getDisplayName() + "对你进行新的评价啦！");
            messageService.save(message);

        }

        // 对商家发货点评价
//        for (Shipping shipping : trade.getShippings()) {
//
//            if(shipping.getDeliveryCenter()!=null){
//                DeliveryCenter deliveryCenter = shipping.getDeliveryCenter();
//                if (deliveryCenter != null) {//修改判断是否为空
//                    Long count = deliveryCenter.getScoreCount();
//                    if (count == null) {
//                        deliveryCenter.setScoreCount(1l);
//                    } else {
//                        deliveryCenter.setScoreCount(deliveryCenter.getScoreCount() + 1);
//
//                    }
//                    if (deliveryCenter.getTotalScore() == null) {
//                        deliveryCenter.setTotalScore(Long.valueOf(review.getAssistant()));
//                    } else {
//                        deliveryCenter.setTotalScore(deliveryCenter.getTotalScore() + review.getAssistant());
//                    }
//                    if (deliveryCenter.getScoreCount() != 0 && deliveryCenter.getTotalScore() != null) {
//                        deliveryCenter.setScore((float) (deliveryCenter.getTotalScore() / deliveryCenter.getScoreCount()));
//                    }
//                    deliveryCenterDao.merge(deliveryCenter);
//                }
//            }
//
//        }

//        for (OrderItem orderItem : trade.getOrderItems()) {
//            // 对商品评价
//            Product product = productDao.findBySn(orderItem.getSn());
//            if (product != null) {
//                Review productReview = new Review();
//                productReview.setAssistant(review.getAssistant());
//                productReview.setContent(review.getContent());
//                productReview.setFlag(Flag.product);
//                productReview.setIp(request.getRemoteAddr());
//                productReview.setIsShow(review.getIsShow());
//                productReview.setOrderItem(orderItem);
//                productReview.setMember(member);
//                productReview.setProduct(product);
//                productReview.setScore(review.getScore());
//                productReview.setTenant(product.getTenant());
//                productReview.setType(review.getType());
//
////                List<ProductImage> productImages = null;
//                List<ProductImage> productImages=new ArrayList<ProductImage>();
//                for(ProductImage productImage:review.getImages()){
//                    productImages.add(productImage);
//                }
//
//                productReview.setImages(productImages);
//                reviewDao.persist(productReview);
//                // 更新商品评分
//                Long scoreCount = product.getScoreCount();
//                if (scoreCount == null) {
//                    product.setScoreCount(1l);
//                } else {
//                    product.setScoreCount(scoreCount + 1);
//                }
//                Long totalScore = product.getTotalScore();
//                if (totalScore == null) {
//                    product.setTotalScore(Long.valueOf(review.getScore()));
//                    product.setScore(0f);
//                } else {
//                    product.setTotalScore(totalScore + review.getScore());
//                    product.setScore((float) totalScore / product.getScoreCount());
//                }
//                product.getReviews().add(productReview);
//                productDao.merge(product);
//
//            }
//
//        }

    }
}