package net.wit.controller.weixin.model;

import net.wit.entity.Cart;
import net.wit.entity.Location;
import net.wit.entity.Member;
import net.wit.entity.Tenant;

public class TenantViewModel extends BaseModel {

    //id
    private Long id;
    //店铺名称
    private String name;
    //缩略图
    private String thumbnail;
    //平均评分
    private Float grade;
    //是否收藏
    private Boolean hasFavorite;
    //粉丝数
    private Integer fansCount;
    //点击数
    private Long hits;
    //营业时间
    private String hours;
    //最近实体店
    private DeliveryCenterModel nearDeliveryCenter;
    //担保交易
    private Boolean tamPo;
    //七天退货
    private Boolean noReason;
    //购物车商品数量
    private Integer cartCount;
    //店主用户名
    private String ownerUserName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Float getGrade() {
        return grade;
    }

    public void setGrade(Float grade) {
        this.grade = grade;
    }

    public Boolean getHasFavorite() {
        return hasFavorite;
    }

    public void setHasFavorite(Boolean hasFavorite) {
        this.hasFavorite = hasFavorite;
    }

    public Integer getFansCount() {
        return fansCount;
    }

    public void setFansCount(Integer fansCount) {
        this.fansCount = fansCount;
    }

    public Long getHits() {
        return hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public DeliveryCenterModel getNearDeliveryCenter() {
        return nearDeliveryCenter;
    }

    public void setNearDeliveryCenter(DeliveryCenterModel nearDeliveryCenter) {
        this.nearDeliveryCenter = nearDeliveryCenter;
    }

    public Boolean getTamPo() {
        return tamPo;
    }

    public void setTamPo(Boolean tamPo) {
        this.tamPo = tamPo;
    }

    public Boolean getNoReason() {
        return noReason;
    }

    public void setNoReason(Boolean noReason) {
        this.noReason = noReason;
    }

    public Integer getCartCount() {
        return cartCount;
    }

    public void setCartCount(Integer cartCount) {
        this.cartCount = cartCount;
    }

    public String getOwnerUserName() {
        return ownerUserName;
    }

    public void setOwnerUserName(String ownerUserName) {
        this.ownerUserName = ownerUserName;
    }

    public void copyFrom(Tenant tenant, Member member, Location location, Cart cart) {
        this.id = tenant.getId();
        this.name = tenant.getName();
        this.thumbnail = tenant.getThumbnail() == null ? tenant.getLogo() : tenant.getThumbnail();
        this.grade = tenant.getScore();
        this.hasFavorite = member != null && member.getFavoriteTenants().contains(tenant);
        this.fansCount = tenant.getFavoriteMembers().size();
        this.hits = tenant.getHits();
        this.hours = tenant.getHours();
        DeliveryCenterModel deliveryCenterModel = new DeliveryCenterModel();
        deliveryCenterModel.copyFrom(tenant.nearDeliveryCenter(location));
        this.nearDeliveryCenter = deliveryCenterModel;
        this.cartCount = cart == null ? 0 : cart.getQuantity();
        this.ownerUserName = tenant.getMember().getUsername();
    }

}
