package net.wit.controller.app.model;

import net.wit.entity.ProductImage;
import net.wit.entity.Tenant;
import net.wit.entity.TenantRelation;
import net.wit.entity.TenantRelation.Status;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by thwapp on 2016/2/22.
 */
public class TenantRelationModel extends BaseModel {
    /** 编号*/
    private  Long id;
    /** 商铺编号*/
    private  Long tenantId;
    /** 店主*/
    private MemberListModel member;
    /** 缩影图 */
    private String thumbnail;
    /** 店铺状态  */
    private Status status;
    /** 简称  */
    private String shortName;
    /** 主营类目 */
    private String tenantCategoryName;
    /** 我的等级*/
    private MemberRankModel memberRank;
    /** 地址*/
    private String address;

    /** 简介*/
    private String introduction;

    /** logo*/
    private String logo;

    /** 展示图片*/
    private List tenantImages;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public MemberListModel getMember() {
        return member;
    }

    public void setMember(MemberListModel member) {
        this.member = member;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getTenantCategoryName() {
        return tenantCategoryName;
    }

    public void setTenantCategoryName(String tenantCategoryName) {
        this.tenantCategoryName = tenantCategoryName;
    }

    public MemberRankModel getMemberRank() {
        return memberRank;
    }

    public void setMemberRank(MemberRankModel memberRank) {
        this.memberRank = memberRank;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List getTenantImages() {
        return tenantImages;
    }

    public void setTenantImages(List tenantImages) {
        this.tenantImages = tenantImages;
    }


    public void copyFrom(Tenant tenant){
        this.tenantId = tenant.getId();
        this.thumbnail = tenant.getThumbnail();
        MemberListModel memberListModel = new MemberListModel();
        memberListModel.copyFrom(tenant.getMember(),null);
        this.member = memberListModel;
        //this.status = tenant.getStatus();
        this.shortName = tenant.getShortName();
        this.tenantCategoryName = tenant.getTenantCategory().getName();
//        MemberRankModel memberRankModel = new MemberRankModel();
//        memberRankModel.copyFrom(tenantRelation.getMemberRank());
//        this.memberRank = memberRankModel;
        if (tenant.getDefaultDeliveryCenter()!=null) {
          	 this.address = tenant.getDefaultDeliveryCenter().getAreaName().concat(tenant.getDefaultDeliveryCenter().getAddress());
           } else {
               this.address = tenant.getArea().getFullName().concat(tenant.getAddress());
           }
        this.introduction = tenant.getIntroduction();
        this.logo = tenant.getLogo();
        //this.tenantImages = tenantRelation.getTenant().getTenantImages();
        List image = new ArrayList();
        List<ProductImage> productImages = tenant.getTenantImages();
        for (ProductImage productImage:productImages){
            //image.add(productImage.getMedium());
            image.add(productImage.getLarge());
        }
        this.tenantImages = image;
    }


}
