package net.wit.controller.weixin.model;

import net.wit.entity.Cart;
import net.wit.entity.Member;
import net.wit.entity.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品详情
 * Created by WangChao on 2016-10-12.
 */
public class ProductViewModel {
    /**
     * 商品ID
     */
    private Long id;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 商品全名
     */
    private String fullName;
    /**
     * 商品图片
     */
    private List<ProductImageModel> productImages;
    /**
     * 销售价
     */
    private BigDecimal price;
    /**
     * 市场价
     */
    private BigDecimal marketPrice;
    /**
     * 点击数
     */
    private Long hits;
    /**
     * 月销量
     */
    private Long monthSales;
    /**
     * 活动
     */
    private List<PromotionModel> promotions;
    /**
     * 包邮活动
     */
    private PromotionModel mailPromotion;
    /**
     * 店铺Id
     */
    private Long tenantId;
    /**
     * 店铺名称
     */
    private String tenantName;
    /**
     * 店铺缩略图
     */
    private String tenantThumbnail;
    /**
     * 最近门店地址
     */
    private String address;
    /**
     * 全部商品数
     */
    private Integer productCount;
    /**
     * 店铺收藏人数
     */
    private Integer tenantFavoriteCount;
    /**
     * 店铺星级
     */
    private Float tenantGrade;
    /**
     * 联系电话
     */
    private String phone;
    /**
     * 七天退货
     */
    private Boolean noReason;
    /**
     * 担保交易
     */
    private Boolean tamPo;
    /**
     * 略图
     */
    private String thumbnail;
    /**
     * 可用库存
     */
    private Integer availableStock;
    /**
     * 当前规格
     */
    private List<SpecificationValueModel> specification;
    /**
     * 所有规格
     */
    private List<SpecificationModel> specifications;
    /**
     * 所有商品规格
     */
    private List<ProductSpecificationModel> productSpecifications;
    /**
     * 好评度
     */
    private Double positivePercent;
    /**
     * 商品首条评价
     */
    private ProductReviewModel review;
    /**
     * 商品属性
     */
    private List<ProductAttributeModel> attributes;
    /**
     * 商品详情描述 HTML格式
     */
    private String introduction;
    /**
     * 是否收藏
     */
    private Boolean hasFavorite;
    /**
     * 购物车数量
     */
    private Integer cartCount;

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<ProductImageModel> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImageModel> productImages) {
        this.productImages = productImages;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Long getHits() {
        return hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

    public Long getMonthSales() {
        return monthSales;
    }

    public void setMonthSales(Long monthSales) {
        this.monthSales = monthSales;
    }

    public List<PromotionModel> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<PromotionModel> promotions) {
        this.promotions = promotions;
    }

    public PromotionModel getMailPromotion() {
        return mailPromotion;
    }

    public void setMailPromotion(PromotionModel mailPromotion) {
        this.mailPromotion = mailPromotion;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantThumbnail() {
        return tenantThumbnail;
    }

    public void setTenantThumbnail(String tenantThumbnail) {
        this.tenantThumbnail = tenantThumbnail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public Integer getTenantFavoriteCount() {
        return tenantFavoriteCount;
    }

    public void setTenantFavoriteCount(Integer tenantFavoriteCount) {
        this.tenantFavoriteCount = tenantFavoriteCount;
    }

    public Float getTenantGrade() {
        return tenantGrade;
    }

    public void setTenantGrade(Float tenantGrade) {
        this.tenantGrade = tenantGrade;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getNoReason() {
        return noReason;
    }

    public void setNoReason(Boolean noReason) {
        this.noReason = noReason;
    }

    public Boolean getTamPo() {
        return tamPo;
    }

    public void setTamPo(Boolean tamPo) {
        this.tamPo = tamPo;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Integer getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(Integer availableStock) {
        this.availableStock = availableStock;
    }

    public List<SpecificationValueModel> getSpecification() {
        return specification;
    }

    public void setSpecification(List<SpecificationValueModel> specification) {
        this.specification = specification;
    }

    public List<SpecificationModel> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(List<SpecificationModel> specifications) {
        this.specifications = specifications;
    }

    public List<ProductSpecificationModel> getProductSpecifications() {
        return productSpecifications;
    }

    public void setProductSpecifications(List<ProductSpecificationModel> productSpecifications) {
        this.productSpecifications = productSpecifications;
    }

    public Double getPositivePercent() {
        return positivePercent;
    }

    public void setPositivePercent(Double positivePercent) {
        this.positivePercent = positivePercent;
    }

    public ProductReviewModel getReview() {
        return review;
    }

    public void setReview(ProductReviewModel review) {
        this.review = review;
    }

    public List<ProductAttributeModel> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<ProductAttributeModel> attributes) {
        this.attributes = attributes;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Boolean getHasFavorite() {
        return hasFavorite;
    }

    public void setHasFavorite(Boolean hasFavorite) {
        this.hasFavorite = hasFavorite;
    }

    public Integer getCartCount() {
        return cartCount;
    }

    public void setCartCount(Integer cartCount) {
        this.cartCount = cartCount;
    }

    public void copyFrom(Product product, Member member, Cart cart) {
        this.id = product.getId();
        this.name = product.getName();
        this.fullName = product.getFullName();
        this.productImages = ProductImageModel.bindData(product.getProductImages());
        this.price = product.calcEffectivePrice(null);
        this.marketPrice = product.getMarketPrice();
        this.hits = product.getHits();
        this.monthSales = product.getMonthSales();
        this.promotions = PromotionModel.bindData(new ArrayList<>(product.getValidPromotions()));
        if (product.getTenant().getMailPromotions().size() != 0) {
            PromotionModel promotionModel = new PromotionModel();
            promotionModel.copyFrom(product.getTenant().getMailPromotions().iterator().next());
            this.mailPromotion = promotionModel;
        }
        this.tenantId = product.getTenant().getId();
        this.tenantName = product.getTenant().getName();
        this.tenantThumbnail = product.getTenant().getThumbnail() == null ? product.getTenant().getLogo() : product.getTenant().getThumbnail();
        this.address = product.getTenant().getDefaultDeliveryCenter().getAddress();
        this.productCount = product.getGoods().getProducts().size();
        this.tenantFavoriteCount = product.getTenant().getFavoriteMembers().size();
        this.tenantGrade = product.getTenant().getScore();
        this.phone = product.getTenant().getDefaultDeliveryCenter().getPhone();
        this.noReason = product.getTenant().getNoReason();
        this.tamPo = product.getTenant().getTamPo();
        this.thumbnail = product.getThumbnail();
        this.availableStock = product.getAvailableStock();
        this.specification = SpecificationValueModel.bindData(product.getSpecificationValues());
        for (SpecificationValueModel specification : this.specification) {
            if (specification.getId().equals(1L) && product.getGoods().getSpecification1Title() != null) {
                specification.setTitle(product.getGoods().getSpecification1Title());
            }
            if (specification.getId().equals(2L) && product.getGoods().getSpecification2Title() != null) {
                specification.setTitle(product.getGoods().getSpecification2Title());
            }
        }
        this.positivePercent = 98D;
        if (product.getReviews().size() > 0) {
            ProductReviewModel productReviewListModel = new ProductReviewModel();
            productReviewListModel.copyFrom(product.getReviews().iterator().next());
            this.review = productReviewListModel;
        }
        this.attributes = ProductAttributeModel.bindData(product);
        this.introduction = product.getIntroduction();
        this.hasFavorite = member != null && member.getFavoriteProducts().contains(product);
        this.cartCount = cart == null ? 0 : cart.getQuantity();
    }

    public void bindSpecifications(Product product) {
        this.specifications = SpecificationModel.bindData(product);
        this.productSpecifications = ProductSpecificationModel.bindData(product.getGoods().getProducts());
    }

}
