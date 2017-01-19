package net.wit.controller.weixin.member;

import com.google.zxing.WriterException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.controller.weixin.BaseController;
import net.wit.controller.weixin.model.CommendTenantModel;
import net.wit.controller.weixin.model.DataBlock;
import net.wit.controller.weixin.model.RedPacketListModel;
import net.wit.controller.weixin.model.TenantRedPacketListModel;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.support.EntitySupport;
import net.wit.util.QRBarCodeUtil;
import net.wit.util.SettingUtils;
import net.wit.weixin.main.MenuManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 红包
 * Created by WangChao on 2016-10-11.
 */
@Controller("weixinMemberRedPacketController")
@RequestMapping("/weixin/member/redPacket")
public class RedPacketController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "couponServiceImpl")
    private CouponService couponService;

    @Resource(name = "couponCodeServiceImpl")
    private CouponCodeService couponCodeService;

    @Resource(name = "tenantCategoryServiceImpl")
    private TenantCategoryService tenantCategoryService;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "communityServiceImpl")
    private CommunityService communityService;

    @Resource(name = "taskServiceImpl")
    private TaskService taskService;

    @Resource(name = "promotionServiceImpl")
    private PromotionService promotionService;

    @Resource(name = "deliveryCenterServiceImpl")
    private DeliveryCenterService deliveryCenterService;

    /**
     * 优惠雨
     */
    @RequestMapping(value = "/promotions", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock promotions(Long communityId){
        Community community=null;
        if (communityId != null) {
            community = communityService.find(communityId);
            if(community==null){
                return DataBlock.error("无效商圈Id");
            }
        }
        List<Coupon> coupons = couponService.findList(null, community, null, false, null, null, null, true, null,null,null,null);
        List<Filter> filters=new ArrayList<>();
        List<Promotion.Type> list=new ArrayList<>();
        list.add(Promotion.Type.buyfree);
        list.add(Promotion.Type.seckill);
        list.add(Promotion.Type.mail);
        filters.add(new Filter("type", Filter.Operator.in,list));
        List<Promotion> promotions = promotionService.findByCommunity(null, community,false,null,null,filters,null);
        Set set = new HashSet();
        for (Coupon coupon : coupons) {
            Map<String,Object> map = new HashMap<>();
            map.put("id", coupon.getId());
            map.put("type", coupon.getType());
            map.put("amount", coupon.getAmount());
            map.put("minimumPrice", coupon.getMinimumPrice());
            map.put("tenantName", coupon.getTenant().getName());
            set.add(map);
        }
        for (Promotion promotion : promotions) {
            Map<String,Object> map = new HashMap<>();
            map.put("id", promotion.getId());
            map.put("type", promotion.getType());
            map.put("amount", null);
            map.put("minimumPrice", promotion.getMinimumPrice());
            map.put("tenantName", promotion.getTenant().getName());
            set.add(map);
        }
        return DataBlock.success(set,"执行成功");
    }

    /**
     * 商家红包页面
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String list(String extension,HttpServletRequest request){
        if (extension != null) {
            Member extensions = memberService.findByUsername(extension);
            if (extensions != null) {
                request.getSession().setAttribute(Member.EXTENSION_ATTRIBUTE_NAME, extensions.getUsername());
            }
        }
        return "weixin/member/redPacket/index";
    }

    /**
     * 优惠券分享
     * id 优惠券Id
     */
    @RequestMapping(value = "/share", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock share(){
        Member member = memberService.getCurrent();
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String url = MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/weixin/member/redPacket/index.jhtml?extension=" + (member != null ? member.getUsername() : "")));
        Map<String, Object> map = new HashMap<>();
        map.put("link", url);
        map.put("title", "快来领取店铺红包");
        map.put("desc", "您的好友邀请您来领取店铺红包！");
        map.put("imgUrl", member==null?null:member.getHeadImg());
        return DataBlock.success(map, "执行成功");
    }

    /**
     * 我的红包页面
     */
    @RequestMapping(value = "/red", method = RequestMethod.GET)
    public String red(){
        return "weixin/member/redPacket/red";
    }

    /**
     * 商家红包列表
     * @param areaId 区域Id
     * @param categoryId 分类Id
     * @param orderType 排序类型（distance 按距离排序;amountSize 按红包大小排序）
     * @param location  地理经纬度（lng 经度，lat 纬度）
     * @param distance  距离
     * @param pageable  分页参数
     */
    @RequestMapping(value = "/tenant/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock tenantList(Long areaId, Long communityId, Long categoryId, String orderType, Location location, BigDecimal distance, Pageable pageable) {
        Member member = memberService.getCurrent();
        Object data = null;
        Page<Coupon> page = new Page<>();
        try {
            Area area= areaService.find(areaId);
            if(areaId!=null&&area==null){
                return DataBlock.error("无效区域id");
            }
            TenantCategory tenantCategory = tenantCategoryService.find(categoryId);
            Community community=communityService.find(communityId);
            page = couponService.findPage(area, community, tenantCategory, false, location, distance, orderType, false, pageable);
            data = TenantRedPacketListModel.bindData(page.getContent(), location, member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DataBlock.success(data, page, "执行成功");
    }

    /**
     * 我的红包
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list(Pageable pageable,Location location) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Page<CouponCode> page = couponCodeService.findPage(member, null, false, Coupon.Type.tenantBonus, pageable);
        return DataBlock.success(RedPacketListModel.bindData(page.getContent(),location),page, "执行成功");
    }

    /**
     * 领取红包
     */
    @RequestMapping(value = "/get", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock get(Long id, HttpServletRequest request){
        Member member=memberService.getCurrent();
        if(member==null){
            return DataBlock.error("请登录后领取");
        }
        Coupon coupon=couponService.find(id);
        if(coupon==null){
            return DataBlock.error("红包不存在");
        }
        if(coupon.getStatus()== Coupon.Status.unconfirmed){
            return DataBlock.error("无效红包");
        }
        if(coupon.getStatus()== Coupon.Status.cancelled){
            return DataBlock.error("红包已取消");
        }
        if(coupon.getEndDate().before(new Date())){
            return DataBlock.error("红包已过期");
        }
        if(coupon.getSendCount().compareTo(coupon.getCount())>=0){
            return DataBlock.error("红包已被抢完");
        }
        List<CouponCode> couponCodeList=couponCodeService.findCoupon(member,coupon);
        if(couponCodeList.size()>0){
            return DataBlock.error("您已经领取过了");
        }
        List<CouponCode> list=couponCodeService.findRedCouponCode(member,null,false,false);
        if(list.size()>=5){
            return DataBlock.error("您已有5个红包，不能再领取");
        }
        Member extention = null;//推广人
        String ext = (String) request.getSession().getAttribute(Member.EXTENSION_ATTRIBUTE_NAME);
        if (StringUtils.isNotBlank(ext)) {
            extention = memberService.findByUsername(ext);
        }
        List<CouponCode> couponCodes = couponCodeService.buildRed(coupon, member, 1, extention);// 生成红包优惠码
        if (couponCodes == null || couponCodes.size() == 0) {
            return DataBlock.error("领取失败");
        }
        if(extention!=null){
            Long month = Long.parseLong(new SimpleDateFormat("yyyyMM").format(new Date()));
            Task task=taskService.findByMember(extention,month);
            if(task!=null){
                if(task.getDoCoupon()==null){
                    task.setDoCoupon(0L);
                }
                task.setDoCoupon(task.getDoCoupon()+1L);
                taskService.update(task);
            }
            Message message= EntitySupport.createInitMessage(Message.Type.message,"您推广的红包已经被领取了！",null,extention,null);
            message.setWay(Message.Way.tenant);
            messageService.save(message);
        }
        coupon=couponService.find(coupon.getId());
        if(coupon.getRemindQuantity()!=null&&(coupon.getCount()-coupon.getSendCount()<=coupon.getRemindQuantity())){
            try {
                Setting setting = SettingUtils.get();
                Message message= EntitySupport.createInitMessage(Message.Type.redPacket,"红包库存即将不足，已低于你设置的"+coupon.getRemindQuantity()+"个，请尽快前往商家后台（"+setting.getSiteName()+"商家版app、"+setting.getSiteUrl()+"）发放。",null, coupon.getTenant().getMember(),null);
                message.setCoupon(coupon);
                message.setTemplete(Message.Templete.coupon);
                message.setWay(Message.Way.tenant);
                messageService.save(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Map<String,Object> map=new HashMap<>();
        CouponCode couponCode=couponCodeService.find(couponCodes.get(0).getId());
        map.put("amount",couponCode.getCoupon().getAmount());
        return DataBlock.success(map,"领取成功");
    }


    /**
     * 退红包(删除红包)
     * @param code 红包码
     */
    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock refund(String code){
        CouponCode couponCode=couponCodeService.findByCode(code);
        if(couponCode==null){
            return DataBlock.error("无效红包码");
        }
        if(couponCode.getIsUsed()){
            return DataBlock.error("红包已使用，不能删除");
        }
        Coupon coupon=couponCode.getCoupon();
        couponCodeService.delete(couponCode);
        coupon.setSendCount(coupon.getSendCount()-1);
        couponService.update(coupon);
        return DataBlock.success("success","操作成功");
    }

    /**
     * 使用红包
     *
     */
    @RequestMapping(value = "/use", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock use(String code){
        CouponCode couponCode=couponCodeService.findByCode(code);
        if(couponCode==null){
            return DataBlock.error("无效红包");
        }
        if(couponCode.getCoupon().getEndDate().compareTo(new Date())<0){
            return DataBlock.error("红包已过期");
        }
        Map<String,Object> map=new HashMap<>();
        map.put("tenantName",couponCode.getCoupon().getTenant().getName());
        return DataBlock.success(map,"执行成功");
    }

    /**
     * 使用红包展示的二维码
     */
    @RequestMapping(value = "/qrcode", method = RequestMethod.GET)
    public void qrcode(String code,HttpServletResponse response) {
        try {
            String tempFile = System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".jpg";
            response.reset();
            response.setContentType("image/jpeg;charset=utf-8");
            try {
                QRBarCodeUtil.encodeQRCode(code, tempFile, 200, 200);
            } catch (WriterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            ServletOutputStream output = response.getOutputStream();// 得到输出流
            InputStream imageIn = new FileInputStream(new File(tempFile));
            // 得到输入的编码器，将文件流进行jpg格式编码
            JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(imageIn);
            // 得到编码后的图片对象
            BufferedImage image = decoder.decodeAsBufferedImage();
            // 得到输出的编码器
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);
            encoder.encode(image);// 对图片进行输出编码
            imageIn.close();// 关闭文件流
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 推荐好店
     * @param areaId   区域Id
     * @param location  坐标（lng经度,lat纬度）
     * @param pageable  分页参数（pageSize每页记录数，pageNumber页码）
     */
    @RequestMapping(value = "/tenant/recommend", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock recommendTenant(Long areaId,Location location,Pageable pageable){
        Area area = areaService.find(areaId);
        if (area == null) {
            area = areaService.getCurrent();
        }
        Page<Tenant> page = tenantService.openPage(pageable, area, null, null, null, location, null, null, null, null,null,null);
        return DataBlock.success(CommendTenantModel.bindData(page.getContent(),location),"执行成功");
    }

}
