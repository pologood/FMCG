/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.admin;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wit.FileInfo.FileType;
import net.wit.Filter.Operator;
import net.wit.Filter;
import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.entity.*;
import net.wit.entity.Ad.LinkType;
import net.wit.entity.Tag.Type;
import net.wit.entity.Tenant.TenantType;
import net.wit.service.*;
import net.wit.util.QRBarCodeUtil;
import net.wit.util.SettingUtils;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.zxing.WriterException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * Controller - 商品
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("adminProductController")
@RequestMapping("/admin/product")
public class ProductController extends BaseController {

    @Resource(name = "productServiceImpl")
    private ProductService productService;

    @Resource(name = "productCategoryServiceImpl")
    private ProductCategoryService productCategoryService;

    @Resource(name = "goodsServiceImpl")
    private GoodsService goodsService;

    @Resource(name = "brandServiceImpl")
    private BrandService brandService;

    @Resource(name = "promotionServiceImpl")
    private PromotionService promotionService;

    @Resource(name = "tagServiceImpl")
    private TagService tagService;

    @Resource(name = "memberRankServiceImpl")
    private MemberRankService memberRankService;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "productImageServiceImpl")
    private ProductImageService productImageService;

    @Resource(name = "specificationServiceImpl")
    private SpecificationService specificationService;

    @Resource(name = "specificationValueServiceImpl")
    private SpecificationValueService specificationValueService;

    @Resource(name = "fileServiceImpl")
    private FileService fileService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "barcodeServiceImpl")
    private BarcodeService barcodeService;

    @Resource(name = "activityDetailServiceImpl")
    private ActivityDetailService activityDetailService;

    @Resource(name = "activityRulesServiceImpl")
    private ActivityRulesService activityRulesService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;
    /**
     * 检查编号是否唯一
     */
    @RequestMapping(value = "/check_sn", method = RequestMethod.GET)
    public
    @ResponseBody
    boolean checkSn(String previousSn, String sn) {
        if (StringUtils.isEmpty(sn)) {
            return false;
        }
        if (productService.snUnique(previousSn, sn)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取参数组
     */
    @RequestMapping(value = "/parameter_groups", method = RequestMethod.GET)
    public
    @ResponseBody
    Set<ParameterGroup> parameterGroups(Long id) {
        ProductCategory productCategory = productCategoryService.find(id);
        return productCategory.getParameterGroups();
    }

    /**
     * 获取属性
     */
    @RequestMapping(value = "/attributes", method = RequestMethod.GET)
    public
    @ResponseBody
    Set<Attribute> attributes(Long id) {
        ProductCategory productCategory = productCategoryService.find(id);
        return productCategory.getAttributes();
    }

    /**
     * 添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(ModelMap model) {
        model.addAttribute("productCategoryTree", productCategoryService.findTree());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("tags", tagService.findList(Type.product));
        model.addAttribute("memberRanks", memberRankService.findAll());
        model.addAttribute("specifications", specificationService.findAll());
        return "/admin/product/add";
    }


    /*
   * 上架
   */
    @RequestMapping(value = "/upMarketable", method = RequestMethod.GET)
    @ResponseBody
    public Message upMarketable(Long id) {
        Product product = productService.find(id);
        product.setIsMarketable(true);
        productService.update(product);
        return Message.success("上架成功");
    }
    /*
    * 下架
    */
    @RequestMapping(value = "/downMarketable", method = RequestMethod.GET)
    @ResponseBody
    public Message downMarketable(Long id) {
        Product product = productService.find(id);
        product.setIsMarketable(false);
        productService.update(product);
        //TODO 进入“商品”，下架商品
        if(!activityDetailService.isActivity(null,memberService.getCurrent().getTenant(),activityRulesService.find(19L))){
            activityDetailService.addPoint(null,memberService.getCurrent().getTenant(),activityRulesService.find(19L));
        }
        return Message.success("下架成功");
    }
    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Product product, Long productCategoryId, Long brandId, Long[] tagIds, Long[] specificationIds, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        for (Iterator<ProductImage> iterator = product.getProductImages().iterator(); iterator.hasNext(); ) {
            ProductImage productImage = iterator.next();
            if (productImage == null || productImage.isEmpty()) {
                iterator.remove();
                continue;
            }
            if (productImage.getFile() != null && !productImage.getFile().isEmpty()) {
                if (!fileService.isValid(FileType.image, productImage.getFile())) {
                    addFlashMessage(redirectAttributes, Message.error("admin.upload.invalid"));
                    return "redirect:add.jhtml";
                }
            }
        }
        product.setProductCategory(productCategoryService.find(productCategoryId));
        product.setBrand(brandService.find(brandId));
        product.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
        if (product.getTenant()==null) {
            product.setTenant(tenantService.find(Long.parseLong("1")));
        }
//        if (!isValid(product)) {
//            return ERROR_VIEW;
//        }
        if (StringUtils.isNotEmpty(product.getSn()) && productService.snExists(product.getSn())) {
            return ERROR_VIEW;
        }
        if (product.getMarketPrice() == null) {
            BigDecimal defaultMarketPrice = calculateDefaultMarketPrice(product.getPrice());
            product.setMarketPrice(defaultMarketPrice);
        }
        if (product.getPoint() == null) {
            long point = calculateDefaultPoint(product.getPrice());
            product.setPoint(point);
        }
        product.setFullName(null);
        product.setAllocatedStock(0);
        product.setScore(0F);
        product.setTotalScore(0L);
        product.setScoreCount(0L);
        product.setPriority(0L);
        product.setHits(0L);
        product.setWeekHits(0L);
        product.setMonthHits(0L);
        product.setSales(0L);
        product.setWeekSales(0L);
        product.setMonthSales(0L);
        product.setWeekHitsDate(new Date());
        product.setMonthHitsDate(new Date());
        product.setWeekSalesDate(new Date());
        product.setMonthSalesDate(new Date());
        product.setReviews(null);
        product.setConsultations(null);
        product.setFavoriteMembers(null);
        product.setPromotions(null);
        product.setCartItems(null);
        product.setOrderItems(null);
        product.setGiftItems(null);
        product.setProductNotifies(null);

        for (PackagUnit p : product.getPackagUnits()) {
            p.setProduct(product);
        }

        for (MemberRank memberRank : memberRankService.findAll()) {
            String price = request.getParameter("memberPrice_" + memberRank.getId());
            if (StringUtils.isNotEmpty(price) && new BigDecimal(price).compareTo(new BigDecimal(0)) >= 0) {
                product.getMemberPrice().put(memberRank, new BigDecimal(price));
            } else {
                product.getMemberPrice().remove(memberRank);
            }
        }

        for (ProductImage productImage : product.getProductImages()) {
            productImageService.build(productImage);
        }

        Collections.sort(product.getProductImages());
        if (product.getImage() == null && product.getThumbnail() != null) {
            product.setImage(product.getThumbnail());
        }

        for (ParameterGroup parameterGroup : product.getProductCategory().getParameterGroups()) {
            for (Parameter parameter : parameterGroup.getParameters()) {
                String parameterValue = request.getParameter("parameter_" + parameter.getId());
                if (StringUtils.isNotEmpty(parameterValue)) {
                    product.getParameterValue().put(parameter, parameterValue);
                } else {
                    product.getParameterValue().remove(parameter);
                }
            }
        }

        for (Attribute attribute : product.getProductCategory().getAttributes()) {
            String attributeValue = request.getParameter("attribute_" + attribute.getId());
            if (StringUtils.isNotEmpty(attributeValue)) {
                product.setAttributeValue(attribute, attributeValue);
            } else {
                product.setAttributeValue(attribute, null);
            }
        }

        Goods goods = new Goods();
        List<Product> products = new ArrayList<Product>();
        if (specificationIds != null && specificationIds.length > 0) {
            for (int i = 0; i < specificationIds.length; i++) {
                Specification specification = specificationService.find(specificationIds[i]);
                String[] specificationValueIds = request.getParameterValues("specification_" + specification.getId());
                if (specificationValueIds != null && specificationValueIds.length > 0) {
                    for (int j = 0; j < specificationValueIds.length; j++) {
                        if (i == 0) {
                            if (j == 0) {
                                product.setGoods(goods);
                                product.setSpecifications(new HashSet<Specification>());
                                product.setSpecificationValues(new HashSet<SpecificationValue>());
                                products.add(product);
                            } else {
                                Product specificationProduct = new Product();
                                BeanUtils.copyProperties(product, specificationProduct);
                                specificationProduct.setId(null);
                                specificationProduct.setCreateDate(null);
                                specificationProduct.setModifyDate(null);
                                specificationProduct.setSn(null);
                                specificationProduct.setFullName(null);
                                specificationProduct.setAllocatedStock(0);
                                specificationProduct.setIsList(false);
                                specificationProduct.setScore(0F);
                                specificationProduct.setPriority(0L);
                                specificationProduct.setTotalScore(0L);
                                specificationProduct.setScoreCount(0L);
                                specificationProduct.setHits(0L);
                                specificationProduct.setWeekHits(0L);
                                specificationProduct.setMonthHits(0L);
                                specificationProduct.setSales(0L);
                                specificationProduct.setWeekSales(0L);
                                specificationProduct.setMonthSales(0L);
                                specificationProduct.setWeekHitsDate(new Date());
                                specificationProduct.setMonthHitsDate(new Date());
                                specificationProduct.setWeekSalesDate(new Date());
                                specificationProduct.setMonthSalesDate(new Date());
                                specificationProduct.setGoods(goods);
                                specificationProduct.setReviews(null);
                                specificationProduct.setConsultations(null);
                                specificationProduct.setFavoriteMembers(null);
                                specificationProduct.setSpecifications(new HashSet<Specification>());
                                specificationProduct.setSpecificationValues(new HashSet<SpecificationValue>());
                                specificationProduct.setPromotions(null);
                                specificationProduct.setCartItems(null);
                                specificationProduct.setOrderItems(null);
                                specificationProduct.setGiftItems(null);
                                specificationProduct.setProductNotifies(null);
                                product.setSpecifications(new HashSet<Specification>());
                                product.setSpecificationValues(new HashSet<SpecificationValue>());
                                products.add(specificationProduct);
                            }
                        }
                        Product specificationProduct = products.get(j);
                        SpecificationValue specificationValue = specificationValueService.find(Long.valueOf(specificationValueIds[j]));
                        specificationProduct.getSpecifications().add(specification);
                        specificationProduct.getSpecificationValues().add(specificationValue);
                    }
                }
            }
        } else {
            product.setGoods(goods);
            product.setSpecifications(null);
            product.setSpecificationValues(null);
            products.add(product);
        }
        goods.getProducts().clear();
        goods.getProducts().addAll(products);
        goodsService.save(goods);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        model.addAttribute("productCategoryTree", productCategoryService.findTree());
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("tags", tagService.findList(Type.product));
        model.addAttribute("memberRanks", memberRankService.findAll());
        model.addAttribute("specifications", specificationService.findAll());
        model.addAttribute("product", productService.find(id));
        return "/admin/product/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Product product, Long productCategoryId, Long[] tagIds, Long brandId, RedirectAttributes redirectAttributes) {
        try {
            Product p_product = productService.find(product.getId());

            Brand brand = brandService.find(brandId);
            Goods goods = p_product.getGoods();
            for (Product product_t : goods.getProducts()) {
                product_t.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
                product_t.setIsMarketable(product.getIsMarketable());
                if (product.equals(product_t)) {
                    product_t.setIsList(product.getIsList());
                }
                product_t.setIsGift(product.getIsGift());
                product_t.setIsLimit(product.getIsLimit());
                product_t.setProductCategory(productCategoryService.find(productCategoryId));
                product_t.setKeyword(product.getKeyword());
                product_t.setSeoKeywords(product.getSeoKeywords());
                product_t.setSeoTitle(product.getSeoTitle());

                product_t.setBrand(brand);
            }
            goodsService.update(goods);
            addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
            return "redirect:list.jhtml";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR_VIEW;
        }
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Long tenantId, Long productCategoryId, Long brandId, Long promotionId, Long tagId, Long areaId, Long supplierId, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, Location location,
                       BigDecimal distance, Date beginDate, Date endDate, String searchValue, Pageable pageable, ModelMap model, HttpServletRequest request) {
        Tenant tenant = tenantService.find(tenantId);
        Tenant supplier = tenantService.find(supplierId);
//        if (beginDate != null) {
//            //beginDate=new Date();
//            Long time = beginDate.getTime();
//            Long begin = time - 24 * 60 * 60 * 1000 * 7;
//            beginDate = new Date(begin);
//        }
//        if (endDate != null) {
//            Long time = endDate.getTime();
//            Long end = time + 24 * 60 * 60 * 1000 - 1;
//            endDate = new Date(end);
//        }

        Admin admin = adminService.getCurrent();
        Area area = null;
        Area area2 = null;
        if (admin != null && admin.getEnterprise() != null) {
            area2 = admin.getEnterprise().getArea();
            if(admin.getEnterprise().getEnterprisetype()== Enterprise.EnterpriseType.proxy){
                model.addAttribute("enterprise", "proxy");
                area=areaService.find(areaId);
			}else if(admin.getEnterprise().getEnterprisetype()== Enterprise.EnterpriseType.provinceproxy){
                model.addAttribute("enterprise", "province_proxy");
                area=areaService.find(areaId);
			}else if(admin.getEnterprise().getEnterprisetype()== Enterprise.EnterpriseType.cityproxy){
                model.addAttribute("enterprise", "city_proxy");
                area=areaService.find(areaId);
			}else if(admin.getEnterprise().getEnterprisetype()== Enterprise.EnterpriseType.countyproxy){
                model.addAttribute("enterprise", "area_proxy");
                area=area2;
            }else{
                model.addAttribute("enterprise", "personal_proxy");
            }

        }
        ProductCategory productCategory = productCategoryService.find(productCategoryId);
        Set<ProductCategory> productCategories = new HashSet<ProductCategory>();
        if (productCategory != null) {
            productCategories.add(productCategory);
        }
        Brand brand = brandService.find(brandId);
        Promotion promotion = promotionService.find(promotionId);
        List<Tag> tags = tagService.findList(tagId);
//        String keyword = pageable.getSearchValue();
//        pageable.setSearchProperty(null);
        //pageable.setSearchValue(null);
        List<Filter> filter = new ArrayList<Filter>();
        if (supplier != null) {
            filter.add(new Filter("supplier", Operator.eq, supplier));
        }
        if (tenant != null) {
            filter.add(new Filter("tenant", Operator.eq, tenant));
        }

//        filter.add(new Filter("isMarketable", Operator.eq, isMarketable));
        filter.add(new Filter("isList", Operator.eq, isList));
        filter.add(new Filter("isTop", Operator.eq, isTop));
        filter.add(new Filter("isGift", Operator.eq, isGift));
        //filter.add(new Filter("isOutOfStock", Operator.eq, isOutOfStock));
        //filter.add(new Filter("isStockAlert", Operator.eq, isStockAlert));

        //filter.add(new Filter("isStockAlert", Operator, isStockAlert));

        pageable.setFilters(filter);
        Page<Product> page = productService.openPage(pageable, area, tenant, beginDate, endDate, productCategories, isMarketable,isOutOfStock,isStockAlert, brand, promotion, tags, searchValue, Product.OrderType.dateDesc);
//        Page<Product> page1 = productService.findPage(productCategory, brand, promotion, tags, null, null, null,
//                isMarketable, isList, isTop, isGift, isOutOfStock, isStockAlert, null, area, null, location, null, null, pageable);
        model.addAttribute("productCategoryTree", productCategoryService.findTree());
        model.addAttribute("page", page);
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("promotions", promotionService.findAll());
        model.addAttribute("tags", tagService.findList(Type.product));
        model.addAttribute("productCategoryId", productCategoryId);
        model.addAttribute("brandId", brandId);
        model.addAttribute("promotionId", promotionId);
        model.addAttribute("tagId", tagId);
        model.addAttribute("isMarketable", isMarketable);
        model.addAttribute("isList", isList);
        model.addAttribute("isTop", isTop);
        model.addAttribute("isGift", isGift);
        model.addAttribute("isOutOfStock", isOutOfStock);
        model.addAttribute("isStockAlert", isStockAlert);
        model.addAttribute("linkTypes", LinkType.values());
        model.addAttribute("tenantId", tenantId);
        model.addAttribute("supplierId", supplierId);
        model.addAttribute("beginDate", beginDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("area", area2);
        model.addAttribute("areaId", areaId);
        List<Filter> filters2 = new ArrayList<Filter>();
        filters2.add(new Filter("tenantType", Filter.Operator.eq, TenantType.suppier));
        filters2.add(new Filter("status", Operator.eq, Tenant.Status.success));
        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("tenantType", Filter.Operator.eq, TenantType.tenant));
        filters.add(new Filter("status", Operator.eq, Tenant.Status.success));
        model.addAttribute("tenan", tenant);
        model.addAttribute("tenants", tenantService.openList(null, area, null, null, null, null, null, filters, Tenant.OrderType.scoreDesc));
        model.addAttribute("suppliers", tenantService.openList(null, area, null, null, null, null, null, filters2, Tenant.OrderType.scoreDesc));
        return "/admin/product/list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Message delete(Long[] ids) {
        productService.delete(ids);
        return SUCCESS_MESSAGE;
    }

    /**
     * 计算默认市场价
     *
     * @param price 价格
     */
    private BigDecimal calculateDefaultMarketPrice(BigDecimal price) {
        Setting setting = SettingUtils.get();
        Double defaultMarketPriceScale = setting.getDefaultMarketPriceScale();
        return setting.setScale(price.multiply(new BigDecimal(defaultMarketPriceScale.toString())));
    }

    /**
     * 计算默认积分
     *
     * @param price 价格
     */
    private long calculateDefaultPoint(BigDecimal price) {
        Setting setting = SettingUtils.get();
        Double defaultPointScale = setting.getDefaultPointScale();
        return price.multiply(new BigDecimal(defaultPointScale.toString())).longValue();
    }

    /**
     * 根据
     */
    @RequestMapping(value = "/qrcode", method = RequestMethod.GET)
    public void qrcode(Long id, HttpServletRequest request, HttpServletResponse response) {
        try {
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            // 第三方用户唯一凭证
            String appId = bundle.getString("APPID");// 睿商圈
            String redirectUri = URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/wap/product/content/" + id.toString() + "/product.jhtml");
            String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri=" + redirectUri + "&response_type=code&scope=snsapi_base&state=123&from=singlemessage&isappinstalled=1#wechat_redirect";

            String tempFile = System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".jpg";
            response.reset();
            response.setContentType("image/jpeg;charset=utf-8");
            try {
                QRBarCodeUtil.encodeQRCode(url, tempFile, 200, 200);
            } catch (WriterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ServletOutputStream output = response.getOutputStream();// 得到输出流
            InputStream imageIn = new FileInputStream(new File(tempFile));
            // 得到输入的编码器，将文件流进行jpg格式编码
            JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(imageIn);
            // 得到编码后的图片对象
            BufferedImage image = decoder.decodeAsBufferedImage();
            // 得到输出的编码器
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);
            encoder.encode(image);// 对图片进行输出编码
            imageIn.close();// 关闭文件流
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 一键添加添加页面
     */
    @RequestMapping(value = "/oneKeyAdd", method = RequestMethod.GET)
    public String oneKeyAdd(Long tenantId, ModelMap model) {
        model.addAttribute("expertTags", tagService.openPage(null, Tag.Type.expert).getContent());
        model.addAttribute("tenant", tenantService.find(tenantId));
        return "/admin/product/oneKeyAdd";
    }

    /**
     * 一键保存
     * Long tenantId 商家Id
     * Long expertTagId  专家标签ID
     */
    @RequestMapping(value = "/oneKeySave", method = RequestMethod.POST)
    public String oneKeySave(Tenant tenant, Long expertTagId,
                             Pageable pageable, RedirectAttributes redirectAttributes,
                             HttpServletRequest request) {
        try {
            Long tenantId = tenant.getId();
            //获取该标签下所有商品的标签信息
            List<Filter> filter = new ArrayList<>();
            filter.add(new Filter("tag", Filter.Operator.eq, expertTagId));
            List<Barcode> barcodes = barcodeService.findList(null,filter,null);
            //遍历商品并且进行上架操作
            if (barcodes == null) {
                addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
                return "redirect:/admin/tenant/list.jhtml";
            }
            for (Barcode item : barcodes) {
                Product product = new Product();
                //条码
                product.setBarcode(item.getBarcode());
                //                            名称
                product.setName(item.getName());
//                            单位
                product.setUnit(item.getUnitName());
//                            市场价
                if (item.getOutPrice() == null) {
                    product.setPrice(BigDecimal.valueOf(0));
                } else {
                    product.setPrice(item.getOutPrice());
                }
//                            商品详情
                product.setIntroduction(item.getIntroduction());
                product.setDescriptionapp(item.getDescriptionapp());
//                            图片
                List<ProductImage> productImages = new ArrayList<>();
                for (ProductImage itemImag : item.getProductImages()) {
                    ProductImage productImg = new ProductImage();
                    productImg.setLocal(itemImag.getSource());
                    productImg.setSource(itemImag.getSource());
                    productImg.setLarge(itemImag.getSource());
                    productImg.setMedium(itemImag.getSource());
                    productImg.setThumbnail(itemImag.getSource());
                    productImages.add(productImg);
                }
                product.setProductImages(productImages);
//                店铺
                product.setTenant(tenantService.find(tenantId));
                //====================================
//                库存
                product.setStock(0);
//                已分配库存
                product.setAllocatedStock(0);
//                是否上架
                product.setIsMarketable(true);
//                批发价
                if (item.getOutPrice() == null) {
                    product.setWholePrice(BigDecimal.valueOf(0));
                } else {
                    product.setWholePrice(item.getOutPrice());
                }
//                销售价
                if (item.getOutPrice() == null) {
                    product.setPrice(BigDecimal.valueOf(0));
                } else {
                    product.setPrice(item.getOutPrice());
                }
//                市场价
                if (item.getOutPrice() == null) {
                    product.setMarketPrice(BigDecimal.valueOf(0));
                } else {
                    product.setMarketPrice(item.getOutPrice());
                }
//                成本价
                if (item.getInPrice() == null) {
                    product.setCost(BigDecimal.valueOf(0));
                } else {
                    product.setCost(item.getInPrice());
                }
//                是否为赠品
                product.setIsGift(false);
//                是否置顶
                product.setIsTop(false);
//                是否列出
                product.setIsList(true);
                save(product, item.getProductCategory().getId(), null, null, null, request, redirectAttributes);

            }

            addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
            return "redirect:/admin/tenant/list.jhtml";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR_VIEW;
        }
    }

}