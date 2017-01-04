/**
 * @Title：ExpertController.java 
 * @Package：net.wit.controller.mobile 
 * @Description：
 * @author：Chenlf
 * @date：2015年6月1日 下午10:49:05 
 * @version：V1.0   
 */

package net.wit.controller.wap;

import javax.annotation.Resource;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Expert;
import net.wit.entity.ExpertCategory;
import net.wit.entity.Member;
import net.wit.service.ExpertCategoryService;
import net.wit.service.ExpertService;
import net.wit.service.MemberService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName：ExpertController
 * @Description：专家分类
 * @author：Chenlf
 * @date：2015年6月1日 下午10:49:05
 */
@Controller("wapExpertController")
@RequestMapping("/wap/expert")
public class ExpertController extends BaseController {
	@Resource(name = "expertServiceImpl")
	private ExpertService expertService;

	@Resource(name = "expertCategoryServiceImpl")
	private ExpertCategoryService expertCategoryService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Long expertCategoryId, Pageable pageable, ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		model.addAttribute("pageable", pageable);
		model.addAttribute("expertCategoryId", expertCategoryId);
		return "/wap/expert/list";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/ajax-list", method = RequestMethod.GET)
	@ResponseBody
	public Page<Expert> listAjax(Long expertCategoryId, Pageable pageable) {
		ExpertCategory expertCategory = expertCategoryService.find(expertCategoryId);
		Page<Expert> page = expertService.findPage(expertCategory, pageable);
		return page;
	}

}
