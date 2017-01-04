package net.wit.controller.weixin.model;

import net.wit.entity.Review;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ReviewModel extends BaseModel {

    /**
     * 姓名
     */
    private String name;
    /**
     * 头像
     */
    private String headImg;
    /**
     * 评分(商品评分/商家诚信服务评分/会员诚信评分)
     */
    private Integer score;
    /**
     * 内容
     */
    private String content;
    /**
     * 评价日期
     */
    private String createDate;
    /**
     * 图片
     */
    private List<ProductImageModel> images;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

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

    public List<ProductImageModel> getImages() {
        return images;
    }

    public void setImages(List<ProductImageModel> images) {
        this.images = images;
    }

    public void copyFrom(Review review) {
        this.name = review.getMember().getDisplayName();
        this.headImg = review.getMember().getHeadImg();
        this.score = review.getScore();
        this.content = review.getContent();
        this.createDate = review.getCreateDate() == null ? null : new SimpleDateFormat("yyyy-MM-dd").format(review.getCreateDate());
        this.images = ProductImageModel.bindData(review.getImages());

    }

    public static List<ReviewModel> bindData(List<Review> reviews) {
        List<ReviewModel> models = new ArrayList<>();
        for (Review review : reviews) {
            ReviewModel model = new ReviewModel();
            model.copyFrom(review);
            models.add(model);
        }
        return models;
    }

}
