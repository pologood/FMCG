package net.wit.service;

import net.wit.Filter;
import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Area;
import net.wit.entity.Contact;
import net.wit.entity.ContactProduct;
import net.wit.entity.Member;

import java.util.List;

public interface ContactService extends BaseService<Contact, Long> {

    /**
     * 查找咨询
     * @param member 会员
     * @param product 商品
     * @param isShow 是否显示
     * @param count 数量
     * @param filters 筛选
     * @param orders 排序
     * @return 咨询,不包含咨询回复
     */
    List<Contact> findList(Member member, ContactProduct product, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders);

    /**
     * 查找咨询
     * @param member 会员
     * @param product 商品
     * @param isShow 是否显示
     * @param count 数量
     * @param filters 筛选
     * @param orders 排序
     * @return 咨询
     */
    List<Contact> findList(Boolean hasRepaly, Member member, ContactProduct product, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders);

    /**
     * 查找咨询(缓存)
     * @param member 会员
     * @param product 商品
     * @param isShow 是否显示
     * @param count 数量
     * @param filters 筛选
     * @param orders 排序
     * @param cacheRegion 缓存区域
     * @return 咨询(缓存),不包含咨询回复
     */
    List<Contact> findList(Member member, ContactProduct product, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders, String cacheRegion);

    /**
     * 查找咨询分页
     * @param type
     * @param member 会员
     * @param product 商品
     * @param isShow 是否显示
     * @param pageable 分页信息
     * @return 不包含咨询回复
     */
    Page<Contact> findPage(Contact.Type type, Member member, ContactProduct product, Boolean isShow, Area area, List<Long> tenants, Pageable pageable);

    /**
     * 查找我发表的帖子和我参与回复的帖子
     * @param member 会员
     * @param pageable 分页信息
     * @return 不包含咨询回复
     */
    Page<Contact> findMessage( Member member, Pageable pageable);

    /**
     * 查找咨询分页
     * @param member 会员
     * @param product 商品
     * @param isShow 是否显示
     * @param pageable 分页信息
     * @return 不包含咨询回复
     */
    Page<Contact> findMyPage(Member member, ContactProduct product, Boolean isShow, Contact.Type type, Pageable pageable);

    /**
     * 查找咨询数量
     * @param member 会员
     * @param product 商品
     * @param isShow 是否显示
     * @return 咨询数量
     */
    Long count(Member member, ContactProduct product, Boolean isShow,Contact.Type type);

    /**
     * 咨询回复
     * @param contact 咨询
     * @param replyContact 回复咨询
     */
    void reply(Contact contact, Contact replyContact);

//    /**
//     * @Title：findListByArticle
//     * @Description：
//     * @param article
//     * @return List<Consultation>
//     */
//    List<Consultation> findListByArticle(Article article);


}
