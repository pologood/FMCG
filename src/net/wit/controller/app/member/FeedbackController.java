package net.wit.controller.app.member;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.FeedbackModel;
import net.wit.entity.Feedback;
import net.wit.entity.ProductImage;
import net.wit.service.FeedbackService;
import net.wit.service.ProductImageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.File;

/**
 * 意见反馈
 * Created by WangChao on 2016-7-4.
 */
@Controller("appMemberFeedbackController")
@RequestMapping("/app/member/feedback")
public class FeedbackController extends BaseController {
    @Resource(name = "feedbackServiceImpl")
    FeedbackService feedbackService;
    @Resource(name = "productImageServiceImpl")
    private ProductImageService productImageService;

    /**
     * 意见反馈列表
     * @param pageable 分页信息
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list(Pageable pageable){
        Page<Feedback> page = feedbackService.findPage(pageable);
        return DataBlock.success(FeedbackModel.bindData(page.getContent()),"执行成功");
    }

    /**
     * 详情页
     * @param id 反馈意见id
     */
    @RequestMapping(value = "/view",method = RequestMethod.GET)
    @ResponseBody
    public DataBlock view(Long id){
        Feedback feedback=feedbackService.find(id);
        FeedbackModel model=new FeedbackModel();
        model.copyFrom(feedback);
        return DataBlock.success(model,"执行成功");
    }

    /**
     * 意见反馈提交
     * content  内容
     * ip       本机Ip地址
     * images   图片
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ResponseBody
    public DataBlock save(Feedback feedback){
        for (ProductImage productImage : feedback.getImages()) {
            if (productImage.getLocal() != null) {
                productImage.setLocalFile(new File(productImage.getLocal()));
                productImageService.build(productImage);
            }
        }
        feedbackService.save(feedback);
        return DataBlock.success("success","执行成功");
    }

}
