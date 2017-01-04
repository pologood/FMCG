package net.wit.controller.wap.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.wit.controller.app.model.BaseModel;
import net.wit.controller.app.model.MemberPriceModel;
import net.wit.controller.app.model.ParameterValueModel;
import net.wit.controller.app.model.ProductCategoryModel;
import net.wit.controller.app.model.ProductCategoryTenantModel;
import net.wit.controller.app.model.ProductImageModel;
import net.wit.controller.app.model.PromotionModel;
import net.wit.controller.app.model.TagModel;
import net.wit.entity.Goods;
import net.wit.entity.Product;
import net.wit.entity.Promotion;
import net.wit.entity.SpecificationValue;
import net.wit.entity.Tag;

public class ProductModel extends BaseModel {
	/*商品ID*/
	private Long id;
	/*商品SN*/
	private String sn;
	/*条码码*/
	private String barcode;
	/*商品名称*/
	private String name;
	/*全名描述*/
	private String fullName;
	/*缩略图*/
	private String thumbnail;
	/*销售价*/
	private BigDecimal price;
	/*批发价*/
	private BigDecimal wholePrice;
	/*市场价*/
	private BigDecimal marketPrice;
	/*分润金额*/
	private BigDecimal fee;
	/*分类*/
	private ProductCategoryModel productCategory;
	/*店内分类*/
	private ProductCategoryTenantModel productCategoryTenant;
	/*图片*/
	private List<ProductImageModel> productImages;
	
	/*型号*/
	private String spec;
	/*颜色*/
	private String color;
	
	/*会员价*/
	private Set<MemberPriceModel> memberPrices;
	/*标签*/
	private Set<TagModel> tags;
	/*促销*/
	private Set<PromotionModel> promotions;
	/*参数*/
	private Set<ParameterValueModel> parameterValues;
	/*单位*/
	private String unit;
	/*库存*/
	private Integer stock;
	/*销量*/
	private Long sales;
	/*点击数*/
	private Long hits;
	/*评分*/
	private Float score;
	/*描述*/
	private String descriptionapp;
		
	/*图片*/
	private List<ProductModel> products;
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getSn() {
		return sn;
	}


	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getBarcode() {
		return barcode;
	}


	public void setBarcode(String barcode) {
		this.barcode = barcode;
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


	public BigDecimal getWholePrice() {
		return wholePrice;
	}


	public void setWholePrice(BigDecimal wholePrice) {
		this.wholePrice = wholePrice;
	}


	public BigDecimal getMarketPrice() {
		return marketPrice;
	}


	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}


	public BigDecimal getFee() {
		return fee;
	}


	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}


	public ProductCategoryModel getProductCategory() {
		return productCategory;
	}


	public void setProductCategory(ProductCategoryModel productCategory) {
		this.productCategory = productCategory;
	}


	public ProductCategoryTenantModel getProductCategoryTenant() {
		return productCategoryTenant;
	}


	public void setProductCategoryTenant(ProductCategoryTenantModel productCategoryTenant) {
		this.productCategoryTenant = productCategoryTenant;
	}


	public List<ProductImageModel> getProductImages() {
		return productImages;
	}


	public void setProductImages(List<ProductImageModel> productImages) {
		this.productImages = productImages;
	}


	public Set<TagModel> getTags() {
		return tags;
	}


	public void setTags(Set<TagModel> tags) {
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


	public Integer getStock() {
		return stock;
	}


	public void setStock(Integer stock) {
		this.stock = stock;
	}


	public Long getSales() {
		return sales;
	}


	public void setSales(Long sales) {
		this.sales = sales;
	}


	public Long getHits() {
		return hits;
	}


	public void setHits(Long hits) {
		this.hits = hits;
	}


	public Set<MemberPriceModel> getMemberPrices() {
		return memberPrices;
	}


	public void setMemberPrices(Set<MemberPriceModel> memberPrices) {
		this.memberPrices = memberPrices;
	}


	public List<ProductModel> getProducts() {
		return products;
	}


	public void setProducts(List<ProductModel> products) {
		this.products = products;
	}


	public String getDescriptionapp() {
		return descriptionapp;
	}


	public void setDescriptionapp(String descriptionapp) {
		this.descriptionapp = descriptionapp;
	}


	public Set<ParameterValueModel> getParameterValues() {
		return parameterValues;
	}


	public void setParameterValues(Set<ParameterValueModel> parameterValues) {
		this.parameterValues = parameterValues;
	}


	public Float getScore() {
		return score;
	}


	public void setScore(Float score) {
		this.score = score;
	}


	public void copyFrom(Product product) {
		this.id = product.getId();
		this.sn = product.getSn();
		this.name = product.getName();
		this.fee = product.getFee();
		this.fullName = product.getName();//商品全称改为商品名称
		this.marketPrice = product.getMarketPrice();
		this.price = product.calcEffectivePrice(null);
		
		ProductCategoryModel productCategory = new ProductCategoryModel();
		productCategory.copyFrom(product.getProductCategory());
		this.productCategory = productCategory;
		ProductCategoryTenantModel productCategoryTenant = new ProductCategoryTenantModel();
		if (product.getProductCategoryTenant()!=null) {
		   productCategoryTenant.copyFrom(product.getProductCategoryTenant());
		}
		
		this.productCategoryTenant = productCategoryTenant;
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
		
		this.sales = product.getSales();
		this.hits = product.getHits();
		this.stock = product.getAvailableStock();
		this.tags = TagModel.bindData(product.getTags());
		this.score = product.getScore();
		
		Set<PromotionModel> promotions = new HashSet<PromotionModel>();
		for (Promotion promotion:product.getPromotions()) {
			PromotionModel model = new PromotionModel();
			model.copyFrom(promotion);
			promotions.add(model);
		}
		this.promotions = promotions;
		this.thumbnail = product.getThumbnail();
		this.unit = product.getUnit();
		this.wholePrice = product.getWholePrice();
		this.barcode = product.getBarcode();
		this.memberPrices = MemberPriceModel.bindData(product.getMemberPrice());
		this.descriptionapp = product.getDescriptionapp();
		
		this.parameterValues = ParameterValueModel.bindData(product.getParameterValue());
	}
	
	public void bind(Goods goods) {
		ArrayList<ProductModel> models = new ArrayList<ProductModel>();
		for (Product product:goods.getProducts()) {
			if (!product.getId().equals(this.id)) {
				ProductModel model = new ProductModel();
				model.copyFrom(product);
				models.add(model);
			}
		}
		this.products = models;
	}		
	
	public static List<ProductModel> bindData(List<Product> products) {
		List<ProductModel> models = new ArrayList<ProductModel>();
		for (Product product:products) {
			ProductModel model = new ProductModel();
			model.copyFrom(product);
			models.add(model);
		}
		return models;
	}
	public static Set<ProductModel> bindData(Set<Product> products) {
		Set<ProductModel> models = new HashSet<ProductModel>(products.size());
		for (Product product:products) {
			ProductModel model=new ProductModel();
			model.copyFrom(product);
			models.add(model);
		}
		return models;
	}
}
