package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 用户信息
 * Created by wangchao on 2016/10/24.
 */
@Entity
@Table(name = "xx_member_parameter")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_member_parameter_sequence")
public class MemberParameter extends BaseEntity{
    /**
     * 会员
     */
    @Expose
    @JsonProperty
    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    /**
     * 身高
     */
    @Expose
    @JsonProperty
    private Double height;

    /**
     * 体重
     */
    @Expose
    @JsonProperty
    private Double weight;

    /**
     * 胸围
     */
    @Expose
    @JsonProperty
    private Double bust;

    /**
     * 腰围
     */
    @Expose
    @JsonProperty
    private Double waistline;

    /**
     * 臀围
     */
    @Expose
    @JsonProperty
    private Double hips;

    /**
     * 肩宽
     */
    @Expose
    @JsonProperty
    private Double shoulderWidth;

    /**
     * 前衣长
     */
    @Expose
    @JsonProperty
    private Double frontClothingLength;

    /**
     * 后衣长
     */
    @Expose
    @JsonProperty
    private Double backClothingLength;

    /**
     * 袖长
     */
    @Expose
    @JsonProperty
    private Double sleeveLength;

    /**
     * 袖口
     */
    @Expose
    @JsonProperty
    private Double cuff;

    /**
     * 下摆围
     */
    @Expose
    @JsonProperty
    private Double hem;

    /**
     * 裤长
     */
    @Expose
    @JsonProperty
    private Double pantsLength;

    /**
     * 大腿围
     */
    @Expose
    @JsonProperty
    private Double thighAround;

    /**
     * 腿口围
     */
    @Expose
    @JsonProperty
    private Double legsAround;

    /**
     * 小腿围
     */
    @Expose
    @JsonProperty
    private Double calfAround;

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getBust() {
        return bust;
    }

    public void setBust(Double bust) {
        this.bust = bust;
    }

    public Double getWaistline() {
        return waistline;
    }

    public void setWaistline(Double waistline) {
        this.waistline = waistline;
    }

    public Double getHips() {
        return hips;
    }

    public void setHips(Double hips) {
        this.hips = hips;
    }

    public Double getShoulderWidth() {
        return shoulderWidth;
    }

    public void setShoulderWidth(Double shoulderWidth) {
        this.shoulderWidth = shoulderWidth;
    }

    public Double getFrontClothingLength() {
        return frontClothingLength;
    }

    public void setFrontClothingLength(Double frontClothingLength) {
        this.frontClothingLength = frontClothingLength;
    }

    public Double getBackClothingLength() {
        return backClothingLength;
    }

    public void setBackClothingLength(Double backClothingLength) {
        this.backClothingLength = backClothingLength;
    }

    public Double getSleeveLength() {
        return sleeveLength;
    }

    public void setSleeveLength(Double sleeveLength) {
        this.sleeveLength = sleeveLength;
    }

    public Double getCuff() {
        return cuff;
    }

    public void setCuff(Double cuff) {
        this.cuff = cuff;
    }

    public Double getHem() {
        return hem;
    }

    public void setHem(Double hem) {
        this.hem = hem;
    }

    public Double getPantsLength() {
        return pantsLength;
    }

    public void setPantsLength(Double pantsLength) {
        this.pantsLength = pantsLength;
    }

    public Double getThighAround() {
        return thighAround;
    }

    public void setThighAround(Double thighAround) {
        this.thighAround = thighAround;
    }

    public Double getLegsAround() {
        return legsAround;
    }

    public void setLegsAround(Double legsAround) {
        this.legsAround = legsAround;
    }

    public Double getCalfAround() {
        return calfAround;
    }

    public void setCalfAround(Double calfAround) {
        this.calfAround = calfAround;
    }
}
