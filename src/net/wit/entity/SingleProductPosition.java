package net.wit.entity;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 16/11/30.
 */
@Entity
@Table(name = "xx_single_product_position")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_single_product_position_sequence")
public class SingleProductPosition extends BaseEntity{
    private static final long serialVersionUID = -7849848867030199578L;

    public enum Type {
        /** 单品 */
        single
    }

    /** 广告位类型 */
    private Type type;

    /** 名称 */
    @NotEmpty
    @Length(max = 200)
    @Column(nullable = false)
    private String name;

    /** 描述 */
    @Length(max = 200)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private ActivityPlanning activityPlanning;

    /** 单品 */
    @OneToMany(mappedBy = "singleProductPosition", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @OrderBy("order asc")
    private Set<SingleProduct> singleProducts = new HashSet<SingleProduct>();

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ActivityPlanning getActivityPlanning() {
        return activityPlanning;
    }

    public void setActivityPlanning(ActivityPlanning activityPlanning) {
        this.activityPlanning = activityPlanning;
    }

    public Set<SingleProduct> getSingleProducts() {
        return singleProducts;
    }

    public void setSingleProducts(Set<SingleProduct> singleProducts) {
        this.singleProducts = singleProducts;
    }
}
