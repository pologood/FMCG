package net.wit.controller.helper.member;

import java.util.HashSet;

import javax.annotation.Resource;
import net.wit.Message;
import net.wit.Pageable;
import net.wit.entity.Article;
import net.wit.entity.Member;
import net.wit.entity.Tag;
import net.wit.entity.Tenant;
import net.wit.service.AreaService;
import net.wit.service.ArticleCategoryService;
import net.wit.service.ArticleService;
import net.wit.service.FileService;
import net.wit.service.MemberService;
import net.wit.service.RSAService;
import net.wit.service.TagService;
import net.wit.service.TenantService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller("helperMemberArticleController")
@RequestMapping("/helper/member/article")
public class ArticleController extends BaseController {
	/** 每页记录数 */

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;

	@Resource(name = "fileServiceImpl")
	private FileService fileService;

	@Resource(name = "articleServiceImpl")
	private ArticleService articleService;

	@Resource(name = "articleCategoryServiceImpl")
	private ArticleCategoryService articleCategoryService;

	@Resource(name = "tagServiceImpl")
	private TagService tagService;

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	/**
	 * 宣传栏列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String articleList(Pageable pageable, ModelMap model) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		model.addAttribute("page", articleService.findMyPage(tenant, pageable));
		model.addAttribute("pageActive",2);
		model.addAttribute("member", member);
		return "/helper/member/article/list";
	}
	
	/**
	 * 宣传栏列表
	 */
	@RequestMapping(value = "/listadmin", method = RequestMethod.GET)
	public String articleadminList(Pageable pageable, ModelMap model) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		model.addAttribute("page", articleService.findMyPage(tenant, pageable));
		model.addAttribute("pageActive",2);
		model.addAttribute("member", member);
		return "/helper/member/article/list";
	}

	/**
	 * 宣传栏删除
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Message articleDelete(Long[] ids) {
		articleService.delete(ids);
		return SUCCESS_MESSAGE;
	}

	/**
	 * 宣传栏添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String articleAdd(ModelMap model) {
		model.addAttribute("articleCategoryTree", articleCategoryService.find(69L));
		model.addAttribute("tags", tagService.findList(net.wit.entity.Tag.Type.article));
		model.addAttribute("pageActive",2);
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		return "/helper/member/article/add";
	}

	/**
	 * 宣传栏保存
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String articleSave(Article article, Long areaId, Long articleCategoryId, Long[] tagIds, MultipartFile file, RedirectAttributes redirectAttributes) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		article.setArticleCategory(articleCategoryService.find(69l));
		article.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
		article.setArea(areaService.find(areaId));
		article.setHits(0L);
		article.setPageNumber(null);
		article.setTenant(tenant);
		article.setArticleCategory(articleCategoryService.find(16L));//设置文章分类：通知公告
		if (!isValid(article)) {
			return ERROR_VIEW;
		}
		articleService.save(article);
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

	/**
	 * 宣传栏编辑
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String articleEdit(Long id, ModelMap model) {
		model.addAttribute("articleCategoryTree", articleCategoryService.find(69l));
		model.addAttribute("tags", tagService.findList(net.wit.entity.Tag.Type.article));
		model.addAttribute("article", articleService.find(id));
		model.addAttribute("pageActive",2);
		return "/helper/member/article/edit";
	}

	/**
	 * 宣传栏更新
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String articleUpdate(Article article, Long areaId, Long articleCategoryId, Long[] tagIds, MultipartFile file, RedirectAttributes redirectAttributes, String image) {
		article.setArticleCategory(articleCategoryService.find(69l));
		article.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
		article.setArea(areaService.find(areaId));
		article.setArticleCategory(articleCategoryService.find(16L));//设置文章分类：通知公告
		if (!isValid(article)) {
			return ERROR_VIEW;
		}
		Article art = articleService.find(article.getId());
		article.setTenant(art.getTenant());
		articleService.update(article, "hits", "pageNumber");
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:list.jhtml";
	}

}
