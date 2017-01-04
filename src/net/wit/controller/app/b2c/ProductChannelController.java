package net.wit.controller.app.b2c;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.ProductCategoryModel;
import net.wit.controller.app.model.ProductListModel;
import net.wit.controller.app.model.TenantCategoryModel;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Controller("appB2cChannelController")
@RequestMapping("/app/b2c/productChannel")
public class ProductChannelController extends BaseController{
	@Resource(name="productChannelServiceImpl")
	private ProductChannelService productChannelService;
	
	@Resource(name="productCategoryServiceImpl")
	private ProductCategoryService productCategoryService;
	
	@Resource(name="productServiceImpl")
	private ProductService productService;
	
	@Resource(name="memberServiceImpl")
	private MemberService memberService;
	
	@Resource(name="communityServiceImpl")
	private CommunityService communityService;

	/**
	 * 频道的商家分类
	 * @param id 频道id
	 * @return 商家分类
     */
	@RequestMapping(value="/tenant_category" ,method=RequestMethod.GET)
	@ResponseBody
	public DataBlock getTenantCategory(Long id){
		ProductChannel productChannel=productChannelService.find(id);
		if (productChannel == null) {
			return DataBlock.error("无效频道id");
		}
		return DataBlock.success(TenantCategoryModel.bindAllData(new ArrayList<>(productChannel.getTenantCategorys())),"执行成功");
	}
	
	/**
	 * 获取频道分类列表
	 * @param channelId
	 * @return
	 */
	@RequestMapping(value="/category" ,method=RequestMethod.GET)
	@ResponseBody
	public DataBlock getChannelCategory(Long channelId){
		ProductChannel productChannel = productChannelService.find(channelId);
		if (productChannel == null) {
			return DataBlock.error("系统内部错误");
		}
		Set<ProductCategory> productCategorys=new HashSet<ProductCategory>();
		productCategorys=productChannel.getProductCategorys();
		
		return DataBlock.success(ProductCategoryModel.bindData(productCategorys),"success");
	}
	
	/**
	 * 根据频道id获取产品
	 * @param channelId
	 * @param pageable
	 * @return
	 */
	@RequestMapping(value="/channel_products" ,method=RequestMethod.GET)
	@ResponseBody
	public DataBlock getChannelProducts(Long channelId,Pageable pageable){
		Member member=memberService.getCurrent();
		if(member==null){
			return DataBlock.error("无法获取当前用户信息");
		}
		Area area=member.getArea();
		Community community=communityService.findbyLocation(member.getLocation());
		ProductChannel productChannel = productChannelService.find(channelId);
		if (productChannel == null) {
			return DataBlock.error("系统内部错误");
		}
		Set<ProductCategory> productCategorys=new HashSet<ProductCategory>();
		productCategorys=productChannel.getProductCategorys();
		Page<Product> page=productService.findPageByChannel(productCategorys, null, null, null, null, null, null, null,
				null, null, null, null, null,community ,area , null, null, null, null, pageable);
		return DataBlock.success(ProductListModel.bindData(page.getContent()),page,"success");
	}
	
	/**
	 * 根据分类id获取商品
	 * @param productCategoryId
	 * @return
	 */
	@RequestMapping(value="/getProduct",method=RequestMethod.GET)
	@ResponseBody
	public DataBlock getProducts(Long productCategoryId,Pageable pageable){
		ProductCategory productCategory=productCategoryService.find(productCategoryId);
		Page <Product> page=productService.findPage(productCategory,null,null,null,null,null,null,
				null,null,null,null,null,null,null,null,null,null,null,null,null,null,pageable);
		return DataBlock.success(ProductListModel.bindData(page.getContent()),page,"cuccess");
	}
}
