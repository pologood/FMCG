package net.wit.controller.weixin;

import net.wit.controller.weixin.model.DataBlock;
import net.wit.controller.weixin.model.TagModel;
import net.wit.entity.Tag;
import net.wit.service.TagService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * 标签
 * Created by wangchao on 2016/10/22.
 */
@Controller("weixinTagController")
@RequestMapping("/weixin/tag")
public class TagController {

    @Resource(name = "tagServiceImpl")
    private TagService tagService;

    /**
     * 获取标签列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list(Tag.Type type) {
        List<Tag> tags = tagService.findList(type);
        return DataBlock.success(TagModel.bindData(tags), "执行成功");
    }

}
