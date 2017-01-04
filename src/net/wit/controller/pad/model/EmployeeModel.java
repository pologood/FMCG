package net.wit.controller.pad.model;

/**
 * 员工列表
 * Created by ruanx on 2016/12/20.
 */
public class EmployeeModel {
    /** id */
    private Long id;

    /** 姓名 */
    private String name;

    /** 头像 */
    private String image;

    /** 评分 */
    private Long score;

    /** 发展会员 */
    private Integer promotingMembers;

    /** 月销量 */
    private Integer monthSales;

    /**描述*/
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getScore() {
        return score;
    }

    public void setScore(Long score) {
        this.score = score;
    }

    public Integer getPromotingMembers() {
        return promotingMembers;
    }

    public void setPromotingMembers(Integer promotingMembers) {
        this.promotingMembers = promotingMembers;
    }

    public Integer getMonthSales() {
        return monthSales;
    }

    public void setMonthSales(Integer monthSales) {
        this.monthSales = monthSales;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
