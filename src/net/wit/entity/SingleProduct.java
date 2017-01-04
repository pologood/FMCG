package net.wit.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Administrator on 16/11/30.
 */
@Entity
@Table(name = "xx_single_product")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_single_product_sequence")
public class SingleProduct extends BaseEntity {

    private static final long serialVersionUID = -1307743303786909390L;


    /** 标题 */
    @NotEmpty
    @Length(max = 200)
    @Column(nullable = false)
    private String title;

    /** 内容 */
    @Lob
    private String content;

    /** 关联商品 */
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    /** 单品所属类别 */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private SingleProductPosition singleProductPosition;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public SingleProductPosition getSingleProductPosition() {
        return singleProductPosition;
    }

    public void setSingleProductPosition(SingleProductPosition singleProductPosition) {
        this.singleProductPosition = singleProductPosition;
    }
}
