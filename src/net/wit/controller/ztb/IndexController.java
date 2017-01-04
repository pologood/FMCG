package net.wit.controller.ztb;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Article;
import net.wit.entity.ArticleCategory;
import net.wit.service.ArticleCategoryService;
import net.wit.service.ArticleService;
import net.wit.service.CooperativePartnerService;
import org.hibernate.validator.constraints.Range;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2016/8/19.
 */

@Controller("ztbIndexController")
@RequestMapping("/ztb/index")
public class IndexController extends BaseController {

    @Resource(name = "cooperativePartnerServiceImpl")
    private CooperativePartnerService cooperativePartnerService;


    @Resource(name = "articleServiceImpl")
    private ArticleService articleService;

    @Resource(name = "articleCategoryServiceImpl")
    private ArticleCategoryService articleCategoryService;
    /**
     * 主页面
     */
    @RequestMapping(method = RequestMethod.GET)
    public String index(ModelMap model, Pageable pageable) {
        ArticleCategory industryNews = articleCategoryService.find(67L);//行业新闻
        ArticleCategory companyDynamics = articleCategoryService.find(68L);//公司动态
        Set<ArticleCategory> industryNewsArticleCategories = new HashSet<ArticleCategory>();
        if(industryNews!=null){
            industryNewsArticleCategories.add(industryNews);
        }

        Set<ArticleCategory> companyDynamicsArticleCategories = new HashSet<ArticleCategory>();

        if(companyDynamics!=null){
            companyDynamicsArticleCategories.add(companyDynamics);
        }

        model.addAttribute("industryNews", articleService.findPage(industryNewsArticleCategories, null, null, pageable));
        model.addAttribute("companyDynamics", articleService.findPage(companyDynamicsArticleCategories, null, null, pageable));
        model.addAttribute("cooperativePartners", cooperativePartnerService.findAll());

        return "/ztb/index";
    }

    /**
     * 搜索
     */
    @RequestMapping(value = "/title/{id}", method = RequestMethod.GET)
    public String content(@PathVariable Long id, ModelMap model) {
        Article article = articleService.find(id);
        if (article == null) {
            return ERROR_VIEW;
        }
        model.addAttribute("article", article);
        model.addAttribute("articleCategory", article.getArticleCategory());
        return "/ztb/title";
    }

    /**
     * 获取更多
     */
    @RequestMapping(value = "/get/more/{id}", method = RequestMethod.GET)
    public @ResponseBody
    List<Map<String,Object>> getMore(@PathVariable Long id, Pageable pageable) {
        List<Map<String,Object>> mapList = new ArrayList<>();

        ArticleCategory articleCategory = articleCategoryService.find(id);
        if (articleCategory == null) {
            return null;
        }
        Set<ArticleCategory> articleCategories = new HashSet<ArticleCategory>();
        if(articleCategory!=null){
            articleCategories.add(articleCategory);
        }
        Page<Article> page =  articleService.findPage(articleCategories, null, null, pageable);

        for (Article article:page.getContent()){
            SimpleDateFormat simpleDateFormatDD = new SimpleDateFormat("dd");
            SimpleDateFormat simpleDateFormatYear = new SimpleDateFormat("yyyy-MM");
            Map<String,Object> map=new HashMap<>();
            map.put("title",article.getTitle());
            map.put("desc","详情");
            map.put("date",simpleDateFormatDD.format(article.getCreateDate()));
            map.put("year",simpleDateFormatYear.format(article.getCreateDate()));
            mapList.add(map);
        }


        return mapList;
    }
}
