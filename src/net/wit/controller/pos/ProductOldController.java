/**
 *====================================================
 * 文件名称: OrderController.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年6月5日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.controller.pos;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.WriterException;
//sun公司仅提供了jpg图片文件的编码api  
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import net.wit.Message;
import net.wit.Setting;
import net.wit.controller.ajax.model.ProductModel;
import net.wit.entity.Goods;
import net.wit.entity.Product;
import net.wit.entity.Product.OrderType;
import net.wit.entity.ProductCategoryTenant;
import net.wit.entity.ProductImage;
import net.wit.entity.Sn.Type;
import net.wit.entity.Tenant;
import net.wit.service.BrandSeriesService;
import net.wit.service.FileService;
import net.wit.service.GoodsService;
import net.wit.service.ProductCategoryService;
import net.wit.service.ProductCategoryTenantService;
import net.wit.service.ProductImageService;
import net.wit.service.ProductService;
import net.wit.service.SnService;
import net.wit.service.TenantService;
import net.wit.util.JsonUtils;
import net.wit.util.QRBarCodeUtil;
import net.wit.util.SettingUtils; 

@Controller("posProductController")
@RequestMapping("/pos/product")
public class ProductOldController extends BaseController {

	@Resource(name = "productServiceImpl")
	private ProductService productService;
	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;
	@Resource(name = "productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	@Resource(name = "productCategoryTenantServiceImpl")
	private ProductCategoryTenantService productCategoryTenantService;
	@Resource(name = "fileServiceImpl")
	private FileService fileService;
	@Resource(name = "goodsServiceImpl")
	private GoodsService goodsService;
	@Resource(name = "snServiceImpl")
	private SnService snService;
	@Resource(name = "brandSeriesServiceImpl")
	private BrandSeriesService brandSeriesService;
	@Resource(name = "productImageServiceImpl")
	private ProductImageService productImageService;
	
	/** 商品分类 */
	@RequestMapping(value = "/productCategoryTenant", method = RequestMethod.GET)
	public @ResponseBody
	Map<Long, String> productCategory(Long tenantId,Long parentId) {
		if (tenantId == null) {
			return new HashMap<Long,String>();
		}
		
		Tenant tenant = tenantService.find(tenantId);
		List<ProductCategoryTenant> productCategorys = productCategoryTenantService.findTree(tenant);
		Map<Long, String> options = new HashMap<Long, String>();
		for (ProductCategoryTenant productCategory : productCategorys) {
			String p = "";
			for (int i = 0;i<(productCategory.getGrade()-1);i++){ 
				p = p + "   ";
			}
			options.put(productCategory.getId(),p + productCategory.getName());
		}
		return options;
	}
	
	/** 列表 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Message list(Long tenantId,Long productCategoryId) {
		if (tenantId == null) {
			return Message.error("商家信息标识为空!");
		}
		try {
			Tenant tenant = tenantService.find(tenantId);
			ProductCategoryTenant productCategoryTenant = productCategoryTenantService.find(productCategoryId);
			List<Product> products = productService.findMyList(tenant, null, productCategoryTenant, null, null, null, null, null, null, true, null, null, null, null, null,null, OrderType.dateDesc);
			ArrayList lists = new ArrayList();
			for (Product product:products) {
				ProductModel model = new ProductModel();
				if (product.getBarcode() == null) {
					model.setBarcode("");
				} else {
					model.setBarcode(product.getBarcode());
				}
				model.setFullName(product.getFullName());
				model.setId(product.getId());
				model.setMarketPrice(product.getMarketPrice());
				model.setPrice(product.getPrice());
				model.setProductCategory(product.getProductCategory().getName());
				model.setSn(product.getSn());
				model.setUnit(product.getUnit());
				model.setName(product.getName());
				model.setSpec(product.getSpec());
				model.setMadeIn(product.getMadeIn());
				lists.add(model);
			}
			String json = JsonUtils.toJson(lists);
			return Message.success(json);
		} catch (Exception e) {
			return Message.error("出错了");
		}
	}

	/** 库存同步 */
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	@ResponseBody
	public Message update(Long tenantId,String sn,BigDecimal stock,BigDecimal price,BigDecimal marketPrice) {
		if (tenantId == null) {
			return Message.error("商家信息标识为空!");
		}
		try {
			Tenant tenant = tenantService.find(tenantId);
			Product product = productService.findBySn(sn);
			if (product!=null && product.getTenant().getId().equals(tenant.getId())) {
			   if  (stock!=null) {
				   product.setStock(stock.intValue());
			   }
			   if (price!=null) {
			      product.setPrice(price);
			   }
    		   if (price!=null && (price.compareTo(product.getMarketPrice())>0)) {
			      product.setMarketPrice(price);
			   }
			   productService.save(product);
			   return Message.success("success");
			} else {
			   return Message.success("no data");
			}
		} catch (Exception e) {
			return Message.error("出错了");
		}
	}
	
	/** 上架 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public Message upload(MultipartFile file,HttpServletRequest request) {
		String filename = System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".tmp";
		File tempFile = new File(filename);
		if (!tempFile.getParentFile().exists()) {
			tempFile.getParentFile().mkdirs();
		}
		try {
			file.transferTo(tempFile);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Message.error(filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Message.error(filename);
		}
		return Message.success(filename);
	}
	/** 上架 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	@ResponseBody
	public Message add(Long tenantId,String name,String spec,String color,Long productCategoryId,Long productCategoryTenantId,String barcode,String[] images,Long [] brandSerieses,String unitName,BigDecimal marketPrice,BigDecimal price,BigDecimal wholePrice,BigDecimal cost,BigDecimal stock,BigDecimal weight,HttpServletRequest request ) {
		if (tenantId == null) {
			return Message.error("商家信息标识为空!");
		}
		Tenant tenant = tenantService.find(tenantId);
		try {
			String sn = snService.generate(Type.product);
			Product product = new Product();
			product.setProductCategory(productCategoryService.find(productCategoryId));
			product.setTenant(tenant);
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
			product.setIsMarketable(true);
			product.setSn(sn);
			product.setIsGift(false);
			product.setIsList(true);
			product.setIsLimit(false);
			product.setIsTop(false);
			product.setPoint(0L);
			product.setPriority(0L);
			
			if (productCategoryTenantId != null) {
				product.setProductCategoryTenant(productCategoryTenantService.find(productCategoryTenantId));
			}
			
			product.setName(name);
			product.setFullName(name);
			product.setSpec(spec);
			product.setMadeIn(color);
			product.setUnit(unitName);
			product.setBarcode(barcode);
			product.setPrice(price);
			product.setWholePrice(wholePrice);
			product.setStock(stock.intValue());
			product.setCost(cost);
			product.setMarketPrice(marketPrice);
            if (images!=null) {
			   for (int j = 0; j<images.length; j++){
				  File file = new File(images[j]);
				  ProductImage productImage = new ProductImage();
				  productImage.setLocalFile(file);
				  productImageService.build(productImage);
				  product.getProductImages().add(productImage);
			   }
			   Collections.sort(product.getProductImages());
			   if (product.getImage() == null && product.getThumbnail() != null) {
				  product.setImage(product.getThumbnail());
			   }
            }
			Goods goods = new Goods();
			product.setGoods(goods);
			List<Product> products = new ArrayList<Product>();
			products.add(product);
			goods.getProducts().addAll(products);
			goodsService.save(goods);
			
		    return Message.success(sn);
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("出错了");
		}
	}
	
	
	/**
	 * 根据
	 */
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> detail(String sn) {
		Map<String, Object> data = new HashMap<String, Object>();
		Product product = productService.findBySn(sn);
		if (product == null) {
			data.put("message",Message.error("该商品不存在！"));
		} else {
			data.put("message",Message.success("获取商品成功！"));
			data.put("image", product.getLarge());
			data.put("price", product.getPrice());
			data.put("width", 800);
			data.put("height", 800);
		}
		return data;
	}
	
	/**
	 * 根据
	 */
	@RequestMapping(value = "/qrcode", method = RequestMethod.GET)
	public void qrcode(String sn,HttpServletRequest request, HttpServletResponse response) {
		Product product = productService.findBySn(sn);
		try {
			Setting setting = SettingUtils.get();
			String url = setting.getSiteUrl()+"/mobile/product/content/"+product.getId()+".jhtml";
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
}
