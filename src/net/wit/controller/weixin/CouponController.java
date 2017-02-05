package net.wit.controller.weixin;

import net.wit.*;
import net.wit.controller.weixin.BaseController;
import net.wit.controller.weixin.model.CouponModel;
import net.wit.controller.weixin.model.CouponNumberModel;
import net.wit.controller.weixin.model.DataBlock;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.support.EntitySupport;
import net.wit.util.SpringUtils;
import net.wit.weixin.main.MenuManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Controller - 优惠券
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("weixinCouponController")
@RequestMapping("/weixin/coupon")
public class CouponController extends BaseController {
    @Resource(name = "couponServiceImpl")
    private CouponService couponService;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "couponCodeServiceImpl")
    private CouponCodeService couponCodeService;

    @Resource(name = "couponNumberServiceImpl")
    private CouponNumberService couponNumberService;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    @Resource(name = "promotionServiceImpl")
    private PromotionService promotionService;

    /**
     * 首页
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String view(Long id, Long no, Long code, HttpServletRequest request) {
        Long _number = 0L;

        if (no != null) {
            _number = no;
        }
        if (code != null) {
            _number = code;
        }
//        return "redirect:/wap/coupon/view.jhtml?id=" + id + "&no=" + _number;
        return "redirect:multipleCoupon.jhtml?id=" + id + "&no=" + _number;
    }

    /**
     * 平台券领取页面
     * @param extension 推广人
     * @param id 代金券Id
     */
    @RequestMapping(value = "/multipleCoupon", method = RequestMethod.GET)
    public String multipleCoupon(String extension, Long id, Long no, HttpServletRequest request, Model model){
        if (extension != null) {
            Member extensions = memberService.findByUsername(extension);
            if (extensions != null) {
                request.getSession().setAttribute(Member.EXTENSION_ATTRIBUTE_NAME, extensions.getUsername());
            }
        }
        model.addAttribute("id",id);
        model.addAttribute("no",no);
        return "weixin/member/multipleCoupon";
    }

    /**
     * 平台券分享
     * id 平台券Id
     */
    @RequestMapping(value = "/mc_share", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock mcShare(Long id, Long no) {
        Member member = memberService.getCurrent();
        Coupon coupon = couponService.find(id);
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String url = MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/weixin/coupon/multipleCoupon.jhtml?id=" + id + "&no=" + no + "&extension=" + (member != null ? member.getUsername() : "")));
        Map<String, Object> map = new HashMap<>();
        map.put("link", url);
        map.put("title", "快来领取平台券");
        map.put("desc", "您的好友邀您来领取" + coupon.getAmount() + "元平台券！");
        map.put("imgUrl", member == null ? null : member.getHeadImg());
        return DataBlock.success(map, "执行成功");
    }

    /**
     * 店铺可领用优惠券列表
     * tenantId 店铺Id
     * pageSize 页大小
     * pageNumber 页码
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list(Pageable pageable) {
//        Member member = memberService.getCurrent();
//        Tenant tenant = tenantService.find(tenantId);
//        if (tenant == null) {
//            return DataBlock.error(DataBlock.TENANT_INVAILD);
//        }
        couponService.refreshStatus(null);
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("type", Filter.Operator.eq, Coupon.Type.tenantCoupon));
        //filters.add(new Filter("tenant", Filter.Operator.eq, tenant));
        pageable.setFilters(filters);
        Page<Coupon> page = couponService.findPage("canUse", pageable);
        return DataBlock.success(CouponModel.bindData(page.getContent(), null), "执行成功");
    }

    /**
     * 领取优惠券
     *
     * @param id 代金券Id
     */
    @RequestMapping(value = "/pickup", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock pickup(Long id) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error("请登录后领取");
        }
        Coupon coupon = couponService.find(id);
        if (coupon == null) {
            return DataBlock.error("优惠券不存在");
        }
        if (coupon.getEndDate().compareTo(new Date()) < 0) {
            return DataBlock.error("优惠券已过期");
        }
        List<CouponCode> couponCodes = couponCodeService.findCoupon(member, coupon);
        if (couponCodes.size() >= coupon.getReceiveTimes()) {
            return DataBlock.error("您已领完");
        }
        List<CouponCode> couponCode = couponCodeService.build(coupon, member, 1);
        if (couponCode == null) {
            return DataBlock.error("领取失败");
        }
        return DataBlock.success("success", "领取成功");
    }

    /**
     * 领取平台券页面
     * id 优惠券Id
     * no 序号
     */
    @RequestMapping(value = "/view_jsons", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock viewJsons(Long id, Long no) {
        Member member=memberService.getCurrent();
        Coupon coupon = couponService.find(id);
        Map<String, Object> map = new HashMap<>();
        map.put("hasReceived", false);
        List<CouponNumber> couponNumbers = couponNumberService.findList(coupon, null,null, no);
        if (coupon.getType().equals(Coupon.Type.multipleCoupon) && couponNumbers != null && couponNumbers.size() > 0) {
            if(couponNumbers.get(0).getMember()!=null){
                map.put("hasReceived", true);
            }
        }
        map.put("amount", coupon.getAmount());
        CouponCode couponCode = couponCodeService.findCouponCodeByCouponAndMember(coupon, member);
        map.put("balance",0);
        if(couponCode!=null){
            map.put("balance",couponCode.getBalance());
        }
        return DataBlock.success(map, "执行成功");
    }

    /**
     * 领取平台券
     */
    @RequestMapping(value = "/judge", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock judge(Long id, Long no) {
        Coupon coupon = couponService.find(id);
        if (coupon == null) {
            return DataBlock.error("无效的优惠券");
        }
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        if (coupon.getExpired()) {
            if (coupon.getType().equals(Coupon.Type.multipleCoupon)) {
                List<CouponNumber> couponNumbers = couponNumberService.findList(coupon, null,null, no);
                if(couponNumbers!=null&&couponNumbers.size()>0){
                    CouponNumber couponNumber=couponNumbers.get(0);
                    if(couponNumber.getStatus().equals(CouponNumber.Status.bound)){
                        CouponCode couponCode = couponCodeService.build(coupon, member, 1, no, CouponNumber.Status.receive);// 生成优惠码
                        if (couponCode != null) {
                            if(couponCode.getGuideMember()!=null){
                                net.wit.entity.Message message= EntitySupport.createInitMessage(net.wit.entity.Message.Type.message,"您推广的平台券已经被领取了！",null,couponNumber.getGuideMember(),null);
                                message.setWay(net.wit.entity.Message.Way.tenant);
                                messageService.save(message);
                            }
                            return DataBlock.success("success","领取成功");
                        } else {
                            return DataBlock.error("领取失败");
                        }
                    }else {
                        return DataBlock.warn("已经被人领取了");
                    }
                }else {
                    CouponCode couponCode = couponCodeService.build(coupon, member, 1, no, CouponNumber.Status.receive);// 生成优惠码
                    if (couponCode != null) {
                        return DataBlock.success("success","领取成功");
                    } else {
                        return DataBlock.error("领取失败");
                    }
                }
            }else {
                if (couponCodeService.findCouponCodeByCouponAndMember(coupon, member) == null) {
                    List<CouponCode> couponCode = couponCodeService.build(coupon, member, 1);// 生成优惠码
                    if (couponCode != null) {
                        return DataBlock.success("success","领取成功");
                    } else {
                        return DataBlock.error("领取失败");
                    }
                } else {
                    return DataBlock.success("success","已领取");
                }
            }
        }
        return DataBlock.error("没有可用优惠券");
    }




    /**
     * 优惠券分享页数据
     * id 优惠券Id
     */
    @RequestMapping(value = "/view_json", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock viewJson(Long id) {
        Member member = memberService.getCurrent();
        Coupon coupon = couponService.find(id);
        CouponCode couponCode = couponCodeService.findCouponCodeByCouponAndMember(coupon, member);
        Map<String, Object> map = new HashMap<>();
        map.put("hasReceived", couponCode != null);
        map.put("CouponId", coupon.getId());
        map.put("amount", coupon.getAmount());
        map.put("minimumPrice", coupon.getMinimumPrice());
        map.put("restCount", (coupon.getCount() - coupon.getSendCount()) < 0 ? 0 : (coupon.getCount() - coupon.getSendCount()));
        map.put("startDate", new SimpleDateFormat("yyyy-MM-dd").format(coupon.getStartDate()));
        map.put("endDate", new SimpleDateFormat("yyyy-MM-dd").format(coupon.getEndDate()));
        map.put("introduction", coupon.getIntroduction());
        map.put("tenantId", coupon.getTenant().getId());
        map.put("tenantName", coupon.getTenant().getName());
        map.put("tenantGrade", coupon.getTenant().getScore());
        map.put("tenantThumbnail", coupon.getTenant().getThumbnail()==null?coupon.getTenant().getLogo():coupon.getTenant().getThumbnail());
        if (coupon.getTenant().getTenantCategory() != null) {
            map.put("tenantCategoryName", coupon.getTenant().getTenantCategory().getName());
        } else {
            map.put("tenantCategoryName", SpringUtils.abbreviate(coupon.getTenant().getIntroduction(), 20, ".."));
        }
        map.put("address", coupon.getTenant().getAddress());
        return DataBlock.success(map, "执行成功");
    }

    /**
     * 获取优惠信息
     * @param id pomotionId
     */
    @RequestMapping(value = "/promotion", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock promotion(Long id){
        Promotion promotion=promotionService.find(id);
        if(promotion==null){
            return DataBlock.error("无效Id");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("promotionId",promotion.getId());
        map.put("promotionName",promotion.getName());
        map.put("introduction", promotion.getIntroduction());
        map.put("startDate",promotion.getBeginDate()==null?null:new SimpleDateFormat("yyyy-MM-dd").format(promotion.getBeginDate()));
        map.put("endDate",promotion.getEndDate()==null?null:new SimpleDateFormat("yyyy-MM-dd").format(promotion.getEndDate()));
        map.put("tenantId",promotion.getTenant().getId());
        map.put("tenantName",promotion.getTenant().getName());
        map.put("tenantGrade", promotion.getTenant().getScore());
        map.put("tenantThumbnail", promotion.getTenant().getThumbnail()==null?promotion.getTenant().getLogo():promotion.getTenant().getThumbnail());
        map.put("address", promotion.getTenant().getAddress());
        return DataBlock.success(map,"执行成功");
    }

    /**
     * 优惠券分享
     * id 优惠券Id
     */
    @RequestMapping(value = "/share", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock share(Long id){
        Member member = memberService.getCurrent();
        Coupon coupon=couponService.find(id);
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String url = MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/weixin/coupon/bshare.jhtml?id=" + id + "&extension=" + (member != null ? member.getUsername() : "")));
        Map<String, Object> map = new HashMap<>();
        map.put("link", url);
        map.put("title", coupon.getTenant().getName()+"["+coupon.getName()+"]");
        map.put("desc", coupon.getIntroduction());
        map.put("imgUrl", coupon.getTenant().getThumbnail() == null ? coupon.getTenant().getLogo() : coupon.getTenant().getThumbnail());
        return DataBlock.success(map, "执行成功");
    }

    /**
     * 领取代金券页面
     * @param extension 推广人
     * @param id 代金券Id
     */
    @RequestMapping(value = "/bshare", method = RequestMethod.GET)
    public String bshare(String extension, Long id, HttpServletRequest request, Model model){
        if (extension != null) {
            Member extensions = memberService.findByUsername(extension);
            if (extensions != null) {
                request.getSession().setAttribute(Member.EXTENSION_ATTRIBUTE_NAME, extensions.getUsername());
            }
        }
        model.addAttribute("id",id);
        return "weixin/member/Bshare";
    }

    /**
     * 平台券明细
     */
    @RequestMapping(value = "/record", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock record(Long id, Pageable pageable) {

        Coupon coupon = couponService.find(id);
        List<Filter> filters=new ArrayList<>();
        filters.add(new Filter("member", Filter.Operator.eq,memberService.getCurrent()));
        filters.add(new Filter("coupon", Filter.Operator.eq,coupon));
        pageable.setFilters(filters);
        Page<CouponNumber> page=couponNumberService.findPage(pageable);
        return DataBlock.success(CouponNumberModel.bindData(page.getContent()),"执行成功");
    }
}
