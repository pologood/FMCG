package net.wit.controller.weixin.model;

import net.wit.entity.Review;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductReviewModel extends BaseModel {
    //评论内容
    private String content;
    //评价时间
    private String createDate;
    /*昵称*/
    private String nickName;
    /*头像*/
    private String headImg;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public void copyFrom(Review review) {
        this.content = review.getContent();
        this.createDate = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").format(review.getCreateDate());
        if(review.getMember()!=null){
            this.nickName=review.getMember().getDisplayName();
            this.headImg=review.getMember().getHeadImg();
        }
    }

    public static List<ProductReviewModel> bindData(List<Review> reviews) {
        List<ProductReviewModel> models = new ArrayList<>();
        for (Review review : reviews) {
            ProductReviewModel model = new ProductReviewModel();
            model.copyFrom(review);
            models.add(model);
        }
        return models;
    }

}
