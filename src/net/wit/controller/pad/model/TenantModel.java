package net.wit.controller.pad.model;

import net.wit.controller.app.model.MemberListModel;
import net.wit.controller.pad.model.PromotionModel;
import net.wit.entity.Coupon;
import net.wit.entity.Promotion;
import net.wit.entity.Tenant;
import net.wit.entity.Union;
import net.wit.service.CouponService;
import net.wit.util.SpringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by Administrator on 2016/11/10.
 */
public class TenantModel {
    /*ID*/
    private Long id;

    /*name*/
    private String name;

    /*name*/
    private String image;

    /** 活动信息 */
    private Set<PromotionModel> promotions;

    /** 平均评分 */
    private Float grade;

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

    public Set<PromotionModel> getPromotions() {
        return promotions;
    }

    public void setPromotions(Set<PromotionModel> promotions) {
        this.promotions = promotions;
    }

    public Float getGrade() {
        return grade;
    }

    public void setGrade(Float grade) {
        this.grade = grade;
    }

    public  void copyFrom(Tenant q) {
        this.id = q.getId();
        this.name = q.getName();
    }

    public static List<TenantModel> bindData(List<Tenant> tenants) {
//        List<TenantModel> models = new ArrayList<TenantModel>();
//        for (Tenant u:us) {
//            TenantModel model = new TenantModel();
//            model.copyFrom(u);
//            models.add(model);
//        }
//        return models;
        List<TenantModel> models = new ArrayList<TenantModel>();
        for (Tenant tenant:tenants) {
            TenantModel model = new TenantModel();
            model.copyFroms(tenant);
            model.bindPromoton(tenant.getVaildPromotions());
            models.add(model);
        }
        return models;
    }
    public void bindPromoton(Set<Promotion> promotions) {
        Set<PromotionModel> models = new HashSet<PromotionModel>();
        for (Promotion promotion:promotions) {
            PromotionModel model = new PromotionModel();
            model.copyFrom(promotion);
            models.add(model);
        }
        this.promotions = models;
    }

    public void copyFroms(Tenant tenant) {
        this.id = tenant.getId();
        this.grade = tenant.getScore();
        this.name = tenant.getName();
        //this.status = tenant.getStatus();
        MemberListModel member = new MemberListModel();
        member.copyFrom(tenant.getMember(),null);
        this.image = tenant.getThumbnail();
        if (this.image==null) {
            this.image = tenant.getLogo();
        }
    }

    public static List<Map<String, Object>> bindDatas(List<Map<String, Object>> tenants) {
        List<Map<String, Object>> maps=new ArrayList<Map<String, Object>>();
        for(Map<String, Object> m:tenants){
            Map<String, Object> map=new HashMap<String,Object>();
            Map<String, Object> map1=new HashMap<String,Object>();
            Map<String, Object> map2=new HashMap<String,Object>();
            List list= new ArrayList();
            map1.put("id",1);
            map1.put("type","discount");
            map2.put("id",2);
            map2.put("type","deckill");
            list.add(map1);
            list.add(map2);
            map.put("tags", list);
            map.put("id", m.get("id"));
            map.put("thumbnail",m.get("image"));
            map.put("name", m.get("name"));
            map.put("grade", m.get("score"));
            maps.add(map);
        }
        return maps;
    }
}
