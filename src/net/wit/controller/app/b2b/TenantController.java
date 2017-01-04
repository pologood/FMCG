/**
 *====================================================
 * 文件名称: TenantController.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014-9-11			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.controller.app.b2b;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wit.controller.app.model.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.zxing.WriterException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.app.BaseController;
import net.wit.entity.Area;
import net.wit.entity.DeliveryCenter;
import net.wit.entity.Location;
import net.wit.entity.Member;
import net.wit.entity.ProductCategoryTenant;
import net.wit.entity.Tag;
import net.wit.entity.Tenant;
import net.wit.entity.TenantCategory;
import net.wit.entity.TenantWechat;
import net.wit.entity.Tenant.OrderType;
import net.wit.support.EntitySupport;
import net.wit.util.QRBarCodeUtil;
import net.wit.weixin.main.MenuManager;

/**
 * @ClassName: TenantController
 * @Description: TODO(店铺管理)
 * @author Administrator
 * @date 2014-9-11 上午11:31:34
 */
@Controller("appB2bTenantController")
@RequestMapping("/app/b2b/tenant")
public class TenantController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;
	@Resource(name = "areaServiceImpl")
	private AreaService areaService;
	
	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;
	
	@Resource(name = "tenantCategoryServiceImpl")
	private TenantCategoryService tenantCategoryService;

	@Resource(name = "deliveryCenterServiceImpl")
	private DeliveryCenterService deliveryCenterService;

    @Resource(name = "tagServiceImpl")
    private TagService tagService;

	/**
	 * 查看
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock view(Long id,Boolean promotion) {
		Tenant tenant = tenantService.find(id);
		if (tenant == null) {
			return DataBlock.error("无效店铺id");
			
		}
		TenantModel model = new TenantModel();
		model.copyFrom(tenant);
		if (promotion!=null) {
			if (promotion) {
				   model.bindPromoton(tenant.getVaildPromotions());
				}
		}
		return DataBlock.success(model,"执行成功");
	}
	
	/**
	 * 商家列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock list(Long tenantCategoryId,Long areaId,String keyword,Long[] tagIds,Pageable pageable,HttpServletRequest request) {
	    Area area = areaService.find(areaId);
		//if(area==null){
		//	area = areaService.getCurrent();
		//}
 	    TenantCategory tenantCategory = tenantCategoryService.find(tenantCategoryId);
        List<Tag> tags = tagService.findList(tagIds);
	    Page<Tenant> page = tenantService.findPage(keyword, tenantCategory, tags, area, null, null, null, pageable);
	    return DataBlock.success(TenantListModel.bindData(page.getContent()),"执行成功");
	}

	
	/**
	 * 活动商家   
	 */
	@RequestMapping(value = "/promotion", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock promotion(Long areaId,Pageable pageable,HttpServletRequest request) {
	    Area area = null;// areaService.getCurrent();
	    Page page = tenantService.findPage(null,area, true, pageable);
	    List<TenantListModel> models = TenantListModel.bindData(page.getContent());
	    for (TenantListModel model:models) {
			Tenant tenant = tenantService.find(model.getId());
			model.bindPromoton(tenant.getVaildPromotions());
		}
	    return DataBlock.success(models,"执行成功");
	}

	/**
	 * 微信端附近商家列表
	 */
	@RequestMapping(value = "/nearby", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock nearby(String keyword,Pageable pageable) {
		//Page<DeliveryCenter> page = deliveryCenterService.findPage(null, areaService.getCurrent(), null, null, null, null, pageable);
		Page<Tenant> page = tenantService.findPage(keyword, null, null, areaService.getCurrent(), null, null, null, pageable);
		List<TenantListModel> models = TenantListModel.bindData(page.getContent());
		for (TenantListModel model:models) {
			Tenant tenant = tenantService.find(model.getId());
			model.bindPromoton(tenant.getVaildPromotions());
		}
		return DataBlock.success(models,"执行成功");
	}

}
