package net.wit.controller.assistant.model;

import net.wit.entity.Article;
import net.wit.entity.Member;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArticleModel extends BaseModel {
	/*ID*/
	private Long id;
	/*标题 */
	private String title;
	/*作者 */
	private String author;
	/*内容 */
	private String content;
	/*点击数 */
	private Long hits;
	/*展示图片 */
	private String image;
	/*创建日期 */
	private Date createDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Long getHits() {
		return hits;
	}

	public void setHits(Long hits) {
		this.hits = hits;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void copyFrom(Article article) {
		this.id = article.getId();
		this.title = article.getTitle();
		this.author = article.getAuthor();
		this.content = article.getContent();
		this.hits = article.getHits();
		this.image = article.getImage();
		this.createDate = article.getCreateDate();
	}
}
