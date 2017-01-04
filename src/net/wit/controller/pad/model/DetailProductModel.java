package net.wit.controller.pad.model;

import net.wit.controller.app.model.*;
import net.wit.controller.pad.model.ProductImageModel;
import net.wit.controller.pad.model.PromotionModel;
import net.wit.controller.pad.model.TagModel;
import net.wit.entity.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Administrator on 2016/11/23.
 */
public class DetailProductModel extends BaseModel {
    /*商品ID*/
    private Long id;
    /*商品名称*/
    private String name;
    /*缩略图*/
    private String thumbnail;
    /*销售价*/
    private BigDecimal price;
    /*库存*/
    private Integer stock;
    /*图片*/
    private List<ProductImageModel> productImages;

    /*型号*/
    private String spec;
    /*颜色*/
    private String color;

    public List<SpecificationValueModel> getSpecification() {
        return specification;
    }

    public void setSpecification(List<SpecificationValueModel> specification) {
        this.specification = specification;
    }

    /*规格*/
    private List<SpecificationValueModel> specification;

    /*店铺信息*/
    private DetailTenantModel tenant;
    /*标签*/
    private ArrayList<TagModel> tags;
    /*促销*/
    private Set<PromotionModel> promotions;
    /*单位*/
    private String unit;
    /*评分*/
    private Double score;

    /*图片*/
    private List<ProductsModel> products;

    private ArrayList specs;

    private HashSet colors;
    /** 月销量 */
    private Long monthSales;
    /** 收藏次数 */
    private Long countFavoriteMembers;
    /*商品详情描述*/
    private String introduction;

    /*商品二维码地址*/
    private String qrcodeUrl;

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


    public String getSpec() {
        return spec;
    }


    public void setSpec(String spec) {
        this.spec = spec;
    }


    public String getColor() {
        return color;
    }


    public void setColor(String color) {
        this.color = color;
    }


    public String getThumbnail() {
        return thumbnail;
    }


    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public BigDecimal getPrice() {
        return price;
    }


    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    public List<ProductImageModel> getProductImages() {
        return productImages;
    }


    public void setProductImages(List<ProductImageModel> productImages) {
        this.productImages = productImages;
    }


    public ArrayList<TagModel> getTags() {
        return tags;
    }


    public void setTags(ArrayList<TagModel> tags) {
        this.tags = tags;
    }


    public Set<PromotionModel> getPromotions() {
        return promotions;
    }


    public void setPromotions(Set<PromotionModel> promotions) {
        this.promotions = promotions;
    }


    public String getUnit() {
        return unit;
    }


    public void setUnit(String unit) {
        this.unit = unit;
    }


    public DetailTenantModel getTenant() {
        return tenant;
    }

    public void setTenant(DetailTenantModel tenant) {
        this.tenant = tenant;
    }

    public List<ProductsModel> getProducts() {
        return products;
    }


    public void setProducts(List<ProductsModel> products) {
        this.products = products;
    }


    public Double getScore() {
        return score;
    }


    public void setScore(Double score) {
        this.score = score;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Long getMonthSales() {
        return monthSales;
    }

    public void setMonthSales(Long monthSales) {
        this.monthSales = monthSales;
    }

    public Long getCountFavoriteMembers() {
        return countFavoriteMembers;
    }

    public void setCountFavoriteMembers(Long countFavoriteMembers) {
        this.countFavoriteMembers = countFavoriteMembers;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl;
    }

    public ArrayList getSpecs() {
        return specs;
    }

    public void setSpecs(ArrayList specs) {
        this.specs = specs;
    }

    public HashSet getColors() {
        return colors;
    }

    public void setColors(HashSet colors) {
        this.colors = colors;
    }

    public void copyFrom(Product product,Tenant tenant1) {
        this.id = product.getId();
        this.name = product.getFullName();
        this.price = product.getPrice();
        this.stock = product.getAvailableStock();
        this.qrcodeUrl = "tenant/product/display/"+product.getId()+".jhtml";
        if(product.getIntroduction()!=null&&!"".equals(product.getIntroduction())){
            this.introduction = "tenant/product/introduction.jhtml?id="+product.getId();
        }else{
            this.introduction = "";
        }
        ProductCategoryModel productCategory = new ProductCategoryModel();
        productCategory.copyFrom(product.getProductCategory());

        this.productImages = ProductImageModel.bindData(product.getProductImages());
        this.spec = "";
        this.color = "";

        for (SpecificationValue specificationValue:product.getSpecificationValues()) {
            if (specificationValue.getSpecification().getId().equals(1L)) {
                this.spec = specificationValue.getName();
            }
            if (specificationValue.getSpecification().getId().equals(2L)) {
                this.color = specificationValue.getName();
            }
        }

        DetailTenantModel tenant = new DetailTenantModel();
        tenant.setId(product.getTenant().getId());
        tenant.setName(product.getTenant().getName());
        tenant.setNoReason(product.getTenant().getNoReason());
        tenant.setTamPo(product.getTenant().getTamPo());
        tenant.setToPay(product.getTenant().getToPay());
        this.tenant = tenant;
        this.tags = TagModel.bindData(product.getTags());
        this.score = Double.parseDouble(String.valueOf(product.getScore()));

        Set<PromotionModel> promotions = new HashSet<PromotionModel>();
        for (Promotion promotion:product.getPromotions()) {
            PromotionModel model = new PromotionModel();
            model.copyFrom(promotion);
            promotions.add(model);
            if (product!=null) {
                if (promotion.getType().equals(Promotion.Type.buyfree)) {
                    this.price = product.calcEffectivePrice(null); //product.getPrice();
                } else if (promotion.getType().equals(Promotion.Type.seckill)) {
                    this.price = new BigDecimal(promotion.getPriceExpression());
                }
            }
        }
        for (Promotion promotion:tenant1.getMailPromotions()) {
            PromotionModel model = new PromotionModel();
            model.copyFrom(promotion);
            promotions.add(model);
        }

        this.promotions = promotions;
        Set<MemberModel> favoriteMembers = new HashSet<MemberModel>();
        for (Member member:product.getFavoriteMembers()) {
            MemberModel model = new MemberModel();
            model.copyFrom(member);
            favoriteMembers.add(model);
        }
        this.countFavoriteMembers=Long.parseLong(favoriteMembers.size()+"");
        this.thumbnail = product.getThumbnail();
        this.unit = product.getUnit();

        this.specification =	SpecificationValueModel.bindData(product.getSpecificationValues());
        for (SpecificationValueModel specification:this.specification) {
            if (specification.getId().equals(1L) && product.getGoods().getSpecification1Title()!=null) {
                specification.setTitle(product.getGoods().getSpecification1Title());
            }
            if (specification.getId().equals(2L) && product.getGoods().getSpecification2Title()!=null) {
                specification.setTitle(product.getGoods().getSpecification2Title());
            }
        }
        this.monthSales=product.getMonthSales();
    }

    public void bind(Goods goods) {
        ArrayList<ProductsModel> models = new ArrayList<ProductsModel>();
        ArrayList specs = new ArrayList();
        HashSet colors = new HashSet();
        HashMap maps = new HashMap();
        for (Product product:goods.getSortProducts()) {
//            Long id1 = 0L;
//            Long id2 = 0L;
//            for (SpecificationValue specificationValue:product.getSpecificationValues()) {
//                if (specificationValue.getSpecification().getId().equals(1L)) {
//                    if(specificationValue.getName().equals(this.spec)){
//                        id1 = product.getId();
//                    }
//                }
//                if (specificationValue.getSpecification().getId().equals(2L)) {
//                    if(specificationValue.getName().equals(this.color)){
//                        id2 = product.getId();
//                    }
//                }
//            }
            //去重拼装尺码，颜色数组
            for (SpecificationValue specificationValue:product.getSpecificationValues()) {
                if (specificationValue.getSpecification().getId().equals(1L)) {
                        maps.put(specificationValue.getOrder(),specificationValue.getName());
                }
                if (specificationValue.getSpecification().getId().equals(2L)) {
                    colors.add(specificationValue.getName());
                }
            }
                ProductsModel model = new ProductsModel();
                model.copyFrom(product);
                models.add(model);
        }
        //去重拼装尺码，颜色数组
        Iterator iter = maps.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object val = entry.getValue();
            specs.add(val);
            }
        this.specs = specs;
        this.colors = colors;
        this.products = models;
    }

    public static List<ProductsModel> bindData(List<Product> products) {
        List<ProductsModel> models = new ArrayList<ProductsModel>();
        for (Product product:products) {
            ProductsModel model = new ProductsModel();
            model.copyFrom(product);
            models.add(model);
        }
        return models;
    }

}
