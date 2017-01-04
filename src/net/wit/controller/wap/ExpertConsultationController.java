/**
 * @Title：ExpertConsultationController.java 
 * @Package：net.wit.controller.mobile 
 * @Description：
 * @author：Chenlf
 * @date：2015年6月1日 下午11:05:38 
 * @version：V1.0   
 */

package net.wit.controller.wap;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.entity.Expert;
import net.wit.entity.ExpertCategory;
import net.wit.entity.ExpertConsultation;
import net.wit.service.ExpertCategoryService;
import net.wit.service.ExpertConsultationService;
import net.wit.service.MemberService;
import net.wit.support.MessageModel;
import net.wit.support.PushMessage;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName：ExpertConsultationController
 * @Description：
 * @author：Chenlf
 * @date：2015年6月1日 下午11:05:38
 */
@Controller("wapExpertConsultationController")
@RequestMapping("/wap/expert_consultation")
public class ExpertConsultationController extends BaseController {

	@Resource(name = "expertConsultationServiceImpl")
	private ExpertConsultationService expertConsultationService;

	@Resource(name = "expertCategoryServiceImpl")
	private ExpertCategoryService expertCategoryService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Long expertCategoryId, ModelMap model) {
		ExpertCategory expertCategory = expertCategoryService.find(expertCategoryId);
		model.addAttribute("expertCategory", expertCategory);
		return "/wap/expert_consultation/add";
	}

	/**
	 * reply
	 */
	@RequestMapping(value = "/reply", method = RequestMethod.GET)
	public String reply(Long id, ModelMap model) {
		ExpertConsultation expertConsultation = expertConsultationService.find(id);
		model.addAttribute("expertConsultation", expertConsultation);
		List<ExpertConsultation> replys = expertConsultationService.findListReply(expertConsultation);
		model.addAttribute("replys", replys);
		return "/wap/expert_consultation/reply";
	}

	/**
	 * reply
	 */
	@RequestMapping(value = "/reply", method = RequestMethod.POST)
	public String replySubmit(Long expertConsultationId, ExpertConsultation reply, HttpServletRequest request, ModelMap model) {
		ExpertConsultation expertConsultation = expertConsultationService.find(expertConsultationId);
		reply.setForExpertConsultation(expertConsultation);
		reply.setMember(memberService.getCurrent());
		reply.setExpertCategory(expertConsultation.getExpertCategory());
		reply.setIp(request.getRemoteAddr());
		reply.setIsShow(true);
		if (!isValid(reply)) {
			return ERROR_VIEW;
		}
		expertConsultationService.save(reply);
		expertConsultation.getReplyExpertConsultations().add(reply);
		expertConsultationService.update(expertConsultation);
		/**
		MessageModel mmodel = new MessageModel();
		if (expertConsultation.getMember() != null) {
			
			
			mmodel.setRecver(expertConsultation.getMember().getUsername());
			mmodel.setMsg("您问题有新答案了,\"" + SpringUtils.abbreviate(reply.getContent(), 20, "...") + "\",快去看看吧。");
			Setting setting = SettingUtils.get();
			mmodel.setUrl(setting.getSiteUrl() + "/wap/expert_consultation/reply.jhtml?id=" + expertConsultation.getId().toString());
			PushMessage.jpush(mmodel);
		}
		**/
		return "redirect:reply.jhtml?id=" + expertConsultationId;
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Long expertCategoryId, ExpertConsultation expertConsultation, HttpServletRequest request) {
		ExpertCategory expertCategory = expertCategoryService.find(expertCategoryId);
		expertConsultation.setExpertCategory(expertCategory);
		expertConsultation.setIsShow(true);
		expertConsultation.setIp(request.getRemoteAddr());
		expertConsultation.setMember(memberService.getCurrent());
		if (!isValid(expertConsultation)) {
			return ERROR_VIEW;
		}
		expertConsultationService.save(expertConsultation);
		/**
		Set<Expert> experts = expertCategory.getExperts();
		if (experts != null && !experts.isEmpty()) {
			for (Expert expert : experts) {
				MessageModel model = new MessageModel();
				model.setRecver(expert.getMember().getUsername());
				model.setMsg("您有新的咨询,\"" + SpringUtils.abbreviate(expertConsultation.getContent(), 20, "...") + "\",请尽快解答");
				Setting setting = SettingUtils.get();
				model.setUrl(setting.getSiteUrl() + "/wap/expert_consultation/reply.jhtml?id=" + expertConsultation.getId().toString());
				PushMessage.jpush(model);
			}
		} **/
		return "redirect:list/" + expertCategoryId + ".jhtml";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list/{expertCategoryId}", method = RequestMethod.GET)
	public String list(@PathVariable Long expertCategoryId, Pageable pageable, ModelMap model) {
		model.addAttribute("pageable", pageable);
		ExpertCategory expertCategory = expertCategoryService.find(expertCategoryId);
		model.addAttribute("expertCategory", expertCategory);
		return "/wap/expert_consultation/list";
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public Page<ExpertConsultation> listAjax(Long expertCategoryId, Pageable pageable) {
		ExpertCategory expertCategory = expertCategoryService.find(expertCategoryId);
		Page<ExpertConsultation> page = expertConsultationService.findPage(null, expertCategory, true, pageable);
		return page;
	}
}
