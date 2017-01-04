package net.wit.controller.app.model;

import net.wit.entity.TenantRelation.Status;
import net.wit.entity.Tenant;
import net.wit.entity.TenantRelation;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by thwapp on 2016/2/22.
 */
public class TenantRelationListModel extends BaseModel {
    /** 编号*/
    private  Long id;
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
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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



    public void copyTenant(TenantRelation tenantRelation){
        this.id = tenantRelation.getId();
        this.thumbnail = tenantRelation.getTenant().getThumbnail();
        this.status = tenantRelation.getStatus();
        this.shortName = tenantRelation.getTenant().getShortName();
        if (tenantRelation.getTenant().getTenantCategory()!=null) {
            this.tenantCategoryName = tenantRelation.getTenant().getTenantCategory().getName();
         } else {
            this.tenantCategoryName = "";
         }
         	
         MemberRankModel memberRankModel = new MemberRankModel();
         if (tenantRelation.getMemberRank()!=null) {
            memberRankModel.copyFrom(tenantRelation.getMemberRank());
         } else {
         	memberRankModel.setName("");
         }
         this.memberRank = memberRankModel;
         Tenant tenant = tenantRelation.getTenant();
         if (tenant.getDefaultDeliveryCenter()!=null) {
        	 this.address = tenant.getDefaultDeliveryCenter().getAreaName().concat(tenant.getDefaultDeliveryCenter().getAddress());
         } else {
             this.address = tenant.getArea().getFullName().concat(tenant.getAddress());
         }
    }

    public static List<TenantRelationListModel> bindTenant(List<TenantRelation> tenantRelations){
        List<TenantRelationListModel> tenantRelationModels = new ArrayList<TenantRelationListModel>();
        for(TenantRelation tenantRelation:tenantRelations){
            TenantRelationListModel tenantRelationModel = new TenantRelationListModel();
            tenantRelationModel.copyTenant(tenantRelation);
            tenantRelationModels.add(tenantRelationModel);
        }
        return tenantRelationModels;
    }

    public void copyParent(TenantRelation tenantRelation){
        this.id = tenantRelation.getId();
        this.thumbnail = tenantRelation.getParent().getThumbnail();
        this.status = tenantRelation.getStatus();
        this.shortName = tenantRelation.getParent().getShortName();
        if (tenantRelation.getParent().getTenantCategory()!=null) {
           this.tenantCategoryName = tenantRelation.getParent().getTenantCategory().getName();
        } else {
           this.tenantCategoryName = "";
        }
        	
        MemberRankModel memberRankModel = new MemberRankModel();
        if (tenantRelation.getMemberRank()!=null) {
           memberRankModel.copyFrom(tenantRelation.getMemberRank());
        } else {
        	memberRankModel.setName("");
        }
        this.memberRank = memberRankModel;
        Tenant tenant = tenantRelation.getParent();
        if (tenant.getDefaultDeliveryCenter()!=null) {
       	 this.address = tenant.getDefaultDeliveryCenter().getAreaName().concat(tenant.getDefaultDeliveryCenter().getAddress());
        } else {
            this.address = tenant.getArea().getFullName().concat(tenant.getAddress());
        }
    }

    public static List<TenantRelationListModel> bindParent(List<TenantRelation> tenantRelations){
        List<TenantRelationListModel> tenantRelationModels = new ArrayList<TenantRelationListModel>();
        for(TenantRelation tenantRelation:tenantRelations){
            TenantRelationListModel tenantRelationModel = new TenantRelationListModel();
            tenantRelationModel.copyParent(tenantRelation);
            tenantRelationModels.add(tenantRelationModel);
        }
        return tenantRelationModels;
    }
}
