package net.wit.controller.weixin.model;

import net.wit.entity.Article;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArticleModel extends BaseModel {
    /**
     * ID
     */
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 作者
     */
    private String author;
    /**
     * 内容
     */
    private String content;
    /**
     * 点击数
     */
    private Long looks;
    /**
     * 点赞数
     */
    private Long praise;
    /**
     * 展示图片
     */
    private String image;
    /**
     * 创建日期
     */
    private Date createDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getLooks() {
        return looks;
    }

    public void setLooks(Long looks) {
        this.looks = looks;
    }

    public Long getPraise() {
        return praise;
    }

    public void setPraise(Long praise) {
        this.praise = praise;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void copyFrom(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.author = article.getAuthor();
        this.content = article.getContent();
        this.looks = article.getHits();
        this.praise= article.getHits();
        this.image = article.getImage();
        this.createDate = article.getCreateDate();
    }

    public static List<ArticleModel> bindData(List<Article> articles) {
        List<ArticleModel> models = new ArrayList<>();
        for (Article article : articles) {
            ArticleModel model = new ArticleModel();
            model.copyFrom(article);
            models.add(model);
        }
        return models;
    }
}
