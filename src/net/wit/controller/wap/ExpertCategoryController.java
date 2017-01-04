/**
 * @Title：ExpertCategoryController.java 
 * @Package：net.wit.controller.mobile 
 * @Description：
 * @author：Chenlf
 * @date：2015年6月1日 下午10:39:06 
 * @version：V1.0   
 */

package net.wit.controller.wap;

import javax.annotation.Resource;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.ExpertCategory;
import net.wit.service.ExpertCategoryService;
import net.wit.service.ProductChannelService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName：ExpertCategoryController
 * @Description：
 * @author：Chenlf
 * @date：2015年6月1日 下午10:39:06
 */
@Controller("wapExpertCategoryController")
@RequestMapping("/wap/expert_category")
public class ExpertCategoryController extends BaseController {
	@Resource(name = "expertCategoryServiceImpl")
	private ExpertCategoryService expertCategoryService;

	@Resource(name = "productChannelServiceImpl")
	private ProductChannelService productChannelService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list/{productChannelId}", method = RequestMethod.GET)
	public String list(@PathVariable Long productChannelId, Pageable pageable, ModelMap model) {
		model.addAttribute("pageable", pageable);
		model.addAttribute("productChannelId", productChannelId);
		return "/wap/expert_category/list";
	}

	/**
	 * 列表-ajax
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Page<ExpertCategory> listajax(Pageable pageable) {
		// ProductChannel productChannel = productChannelService.find(productChannelId);
		// Set<ExpertCategory> expertCategorys = productChannel.getExpertCategorys();
		Page<ExpertCategory> page = expertCategoryService.findPage(pageable);
		return page;
	}
}
