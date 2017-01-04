package net.wit.controller.app.model;

import net.wit.entity.Feedback;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by WangChao on 2016-7-4.
 */
public class FeedbackModel {
    /*id*/
    private Long id;

    /** 内容 */
    private String content;

    /** 回复内容 */
    private String replyContent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public void copyFrom(Feedback feedback){
        this.id=feedback.getId();
        this.content=feedback.getContent();
        this.replyContent=feedback.getReplyContent();
    }

    public static List<FeedbackModel> bindData(List<Feedback> feedbacks){
        List<FeedbackModel> models=new ArrayList<>();
        for(Feedback feedback:feedbacks){
            FeedbackModel model=new FeedbackModel();
            model.copyFrom(feedback);
            models.add(model);
        }
        return models;
    }
}
