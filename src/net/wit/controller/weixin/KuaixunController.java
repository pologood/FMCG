package net.wit.controller.weixin;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.weixin.model.ArticleModel;
import net.wit.controller.weixin.model.DataBlock;
import net.wit.entity.Article;
import net.wit.service.ArticleService;
import net.wit.service.EmployeeService;
import net.wit.service.MemberService;
import net.wit.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 挑货快讯
 * Created by WangChao on 2017/1/10.
 */
@Controller("weixinKuaixunController")
@RequestMapping("/weixin/kuaixun")
public class KuaixunController {
    @Resource(name = "employeeServiceImpl")
    private EmployeeService employeeService;
    @Resource(name = "tagServiceImpl")
    private TagService tagService;
    @Resource(name = "memberServiceImpl")
    private MemberService memberService;
    @Resource(name = "articleServiceImpl")
    private ArticleService articleService;


    /**
     * list
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list(Pageable pageable) {
        Page<Article> page = articleService.findPage(null, null, null, pageable);
        return DataBlock.success(ArticleModel.bindData(page.getContent()), page, "执行成功");
    }

    /**
     * view
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock view(Long id) {
        Article article = articleService.find(id);
        ArticleModel model = new ArticleModel();
        model.copyFrom(article);
        return DataBlock.success(model, "执行成功");
    }

}
