package net.wit.controller.pad;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.controller.pad.model.TenantModel;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import net.wit.entity.Tenant.OrderType;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商盟下企业
 * Created by ruanx on 2016/11/10.
 */
@Controller("padUnionTenantController")
@RequestMapping("/pad/union")
public class UnionTenantController {
    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "unionServiceImpl")
    private UnionService unionService;

    @Resource(name = "unionTenantServiceImpl")
    private UnionTenantService unionTenantService;

    @Resource(name = "couponServiceImpl")
    private CouponService couponService;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "promotionServiceImpl")
    private PromotionService promotionService;

    @Resource(name = "equipmentServiceImpl")
    private EquipmentService equipmentService;

    //商盟内店铺列表
    @RequestMapping(value = "/tenant/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock tenantList(long id, Pageable pageable,OrderType orderType) {
        Page<Map<String,Object>> page = null;
        Union union = unionService.find(id);
        if (union == null) {
            return DataBlock.error("无效商盟id");
        }
        //商家list
        page = unionTenantService.findTenant(union,pageable,orderType);
        List<Map<String, Object>> maps=new ArrayList<Map<String, Object>>();
        for(Map<String, Object> m:page.getContent()){
            Map<String, Object> map=new HashMap<String,Object>();
            //优惠券
            List<Coupon> couponList = couponService.findEnabledCouponList(tenantService.find(Long.parseLong(m.get("id").toString())));
            //平台活动
            List<Promotion> promotionList = promotionService.findEnabledPromotionService(tenantService.find(Long.parseLong(m.get("id").toString())));
            //拼接标签
            Map<String, Object> map1=new HashMap<String,Object>();
            List list= new ArrayList();
            if(couponList.size()>0){
                map1.put("name","代金券");
                map1.put("type","coupon");
                list.add(map1);
            }
            if(promotionList.size()>0){
                for(Promotion promotion:promotionList){
                    Map<String, Object> map2=new HashMap<String,Object>();
                    if(Promotion.Type.buyfree.equals(promotion.getType())){
                        map2.put("name","买赠");
                        map2.put("type", Promotion.Type.buyfree);
                        list.add(map2);
                    }
                    if(Promotion.Type.seckill.equals(promotion.getType())){
                        map2.put("name","限时抢购");
                        map2.put("type",Promotion.Type.seckill);
                        list.add(map2);
                    }
                    if(Promotion.Type.discount.equals(promotion.getType())){
                        map2.put("name","折扣");
                        map2.put("type",Promotion.Type.discount);
                        list.add(map2);
                    }
                    if(Promotion.Type.mail.equals(promotion.getType())){
                        map2.put("name","包邮");
                        map2.put("type",Promotion.Type.mail);
                        list.add(map2);
                    }
                    if(Promotion.Type.points.equals(promotion.getType())){
                        map2.put("name","积分");
                        map2.put("type",Promotion.Type.points);
                        list.add(map2);
                    }
                    if(Promotion.Type.coupon.equals(promotion.getType())){
                        map2.put("name","代金券");
                        map2.put("type",Promotion.Type.coupon);
                        list.add(map2);
                    }
                    if(Promotion.Type.activity.equals(promotion.getType())){
                        map2.put("name","平台立减");
                        map2.put("type",Promotion.Type.activity);
                        list.add(map2);
                    }
                }
            }
            map.put("tags", list);
            map.put("id", m.get("id"));
            map.put("thumbnail",m.get("image"));
            map.put("name", m.get("name"));
            map.put("grade", m.get("score"));
            maps.add(map);
        }
        return DataBlock.success(maps,page, "执行成功");
    }

    //屏联盟内店铺列表
    @RequestMapping(value = "/pad/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock padList(String uuid, Pageable pageable,OrderType orderType) {
        Page<Map<String,Object>> page = null;
        Equipment equipment = equipmentService.findByUUID(uuid);
        Tenant tenant = equipment.getTenant();
        if (tenant==null) {
            DataBlock.error("企业ID无效");
        }
        //商家list
        page = unionTenantService.findTenantByPad(equipment,pageable,orderType);
        List<Map<String, Object>> maps=new ArrayList<Map<String, Object>>();
        for(Map<String, Object> m:page.getContent()){
            Map<String, Object> map=new HashMap<String,Object>();
            //代金券
            List<Coupon> couponList = couponService.findEnabledCouponList(tenantService.find(Long.parseLong(m.get("id").toString())));
            //平台活动
            List<Promotion> promotionList = promotionService.findEnabledPromotionService(tenantService.find(Long.parseLong(m.get("id").toString())));
            //拼接标签
            Map<String, Object> map1=new HashMap<String,Object>();
            List list= new ArrayList();
            if(couponList.size()>0){
                map1.put("name","代金券");
                map1.put("type","coupon");
                list.add(map1);
            }
            if(promotionList.size()>0){
                for(Promotion promotion:promotionList){
                    Map<String, Object> map2=new HashMap<String,Object>();
                    if(Promotion.Type.buyfree.equals(promotion.getType())){
                        map2.put("name","买赠");
                        map2.put("type", Promotion.Type.buyfree);
                        list.add(map2);
                    }
                    if(Promotion.Type.seckill.equals(promotion.getType())){
                        map2.put("name","限时抢购");
                        map2.put("type",Promotion.Type.seckill);
                        list.add(map2);
                    }
                    if(Promotion.Type.discount.equals(promotion.getType())){
                        map2.put("name","折扣");
                        map2.put("type",Promotion.Type.discount);
                        list.add(map2);
                    }
                    if(Promotion.Type.mail.equals(promotion.getType())){
                        map2.put("name","包邮");
                        map2.put("type",Promotion.Type.mail);
                        list.add(map2);
                    }
                    if(Promotion.Type.points.equals(promotion.getType())){
                        map2.put("name","积分");
                        map2.put("type",Promotion.Type.points);
                        list.add(map2);
                    }
                    if(Promotion.Type.coupon.equals(promotion.getType())){
                        map2.put("name","代金券");
                        map2.put("type",Promotion.Type.coupon);
                        list.add(map2);
                    }
                    if(Promotion.Type.activity.equals(promotion.getType())){
                        map2.put("name","平台立减");
                        map2.put("type",Promotion.Type.activity);
                        list.add(map2);
                    }
                }
            }
            map.put("tags", list);
            map.put("id", m.get("id"));
            map.put("thumbnail",m.get("thumbnail"));
            map.put("name", m.get("name"));
            map.put("grade", m.get("score"));
            maps.add(map);
        }
        return DataBlock.success(maps,page, "执行成功");
    }
}
