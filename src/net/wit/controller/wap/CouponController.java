package net.wit.controller.wap;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.wap.model.CouponCodeModel;
import net.wit.entity.*;
import net.wit.entity.BindUser.Type;
import net.wit.entity.Member.BindStatus;
import net.wit.service.*;
import net.wit.support.EntitySupport;
import net.wit.support.PushMessage;
import net.wit.util.BrowseUtil;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;
import net.wit.util.WebUtils;
import net.wit.weixin.main.MenuManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.Map.Entry;


/**
 * Controller - 优惠券
 *
 * @author rsico Team
 * @version 3.0
 */
@Controller("couponController")
@RequestMapping("/wap/coupon")
public class CouponController extends BaseController {
    public static final String REGISTER_SECURITYCODE_SESSION = "register_safe_key";
    public static final String REGISTER_CONTENT_SESSION = "register_mobile";

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "cartServiceImpl")
    private CartService cartService;

    @Resource(name = "smsSendServiceImpl")
    private SmsSendService smsSendService;

    @Resource(name = "bindUserServiceImpl")
    private BindUserService bindUserService;

    @Resource(name = "couponServiceImpl")
    private CouponService couponService;

    @Resource(name = "consumerServiceImpl")
    private ConsumerService consumerService;

    @Resource(name = "memberRankServiceImpl")
    private MemberRankService memberRankService;

    @Resource(name = "couponCodeServiceImpl")
    private CouponCodeService couponCodeService;

    @Resource(name = "couponNumberServiceImpl")
    private CouponNumberService couponNumberService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    /**
     * 首页
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String view(Long id, Long no, ModelMap model, String extension, HttpServletRequest request) {

        if (extension != null) {
            request.getSession().setAttribute(Member.EXTENSION_ATTRIBUTE_NAME, extension);
        }
        Coupon coupon = couponService.find(id);
        if (coupon == null) {
            return "redirect:/wap/index.jhtml";
        }
        Member member = memberService.getCurrent();
        List<CouponNumber> couponNumbers = couponNumberService.findList(coupon, null,null, no);
        if (coupon.getType().equals(Coupon.Type.multipleCoupon) && couponNumbers != null && couponNumbers.size() > 0) {
            if(couponNumbers.get(0).getMember()!=null){
                return "redirect:/wap/coupon/receive/" + id + ".jhtml?no="+no;
            }
        }
        CouponCode couponCode = couponCodeService.findCouponCodeByCouponAndMember(coupon, member);
        model.addAttribute("couponCode", couponCode);
        model.addAttribute("coupon", coupon);
        model.addAttribute("no", no);
        model.addAttribute("extension", extension);
        return "/wap/coupon/view";
    }

    /**
     * 根据会员是否存在弹出弹框
     */
    @RequestMapping(value = "/judge", method = RequestMethod.POST)
    @ResponseBody
    public Message judge(Long id, Long no) {
        Coupon coupon = couponService.find(id);
        if (coupon == null) {
            return Message.error("无效的优惠券");
        }
        Member member = memberService.getCurrent();
        if (member == null) {
            return Message.error("用户不存在");
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
                            return Message.success("领取成功");
                        } else {
                            return Message.error("领取失败");
                        }
                    }else {
                        return Message.warn("已经被人领取了");
                    }
                }else {
                    CouponCode couponCode = couponCodeService.build(coupon, member, 1, no, CouponNumber.Status.receive);// 生成优惠码
                    if (couponCode != null) {
                        return Message.success("领取成功");
                    } else {
                        return Message.error("领取失败");
                    }
                }
            }else {
                if (couponCodeService.findCouponCodeByCouponAndMember(coupon, member) == null) {
                    List<CouponCode> couponCode = couponCodeService.build(coupon, member, 1);// 生成优惠码
                    if (couponCode != null) {
                        return Message.success("领取成功");
                    } else {
                        return Message.error("领取失败");
                    }
                } else {
                    return Message.success("已领取");
                }
            }
        }
        return Message.error("没有可用优惠券");
    }

    /**
     * 发送手机获取验证码
     */
    @RequestMapping(value = "/send_mobile", method = RequestMethod.POST)
    @ResponseBody
    public Message sendMobile(String mobile, HttpServletRequest request) {
        if (mobile == null) {
            return Message.error("手机号码不能为空");
        } else {
            if (mobile.length() != 11) {
                return Message.error("手机号码格式不对");
            }
            HttpSession session = request.getSession();
            Setting setting = SettingUtils.get();
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            int challege = SpringUtils.getIdentifyingCode();
            String securityCode = String.valueOf(challege);
            SafeKey tmp = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
            if (tmp != null && !tmp.hasExpired()) {
                securityCode = tmp.getValue();
            }
            SafeKey safeKey = new SafeKey();
            safeKey.setValue(securityCode);
            safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0
                    ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()) : null);
            session.setAttribute(REGISTER_SECURITYCODE_SESSION, safeKey);
            session.setAttribute(REGISTER_CONTENT_SESSION, mobile);

            SmsSend smsSend = new SmsSend();
            smsSend.setMobiles(mobile);
            smsSend.setContent("验证码 :" + securityCode + ",为了您的账户安全请不要转发他人.【" + bundle.getString("signature") + "】");
            smsSend.setType(net.wit.entity.SmsSend.Type.captcha);
            smsSendService.smsSend(smsSend);
            return Message.success("消息发送成功");
        }
    }

    /**
     * 注册或者登录
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Message takeCoupon(Long id, ModelMap model, String mobile, String captcha, HttpServletResponse response,
                              HttpServletRequest request) {
        String extension = (String) request.getSession().getAttribute(Member.EXTENSION_ATTRIBUTE_NAME);
        Coupon coupon = couponService.find(id);
        Tenant tenant = coupon.getTenant();
        /*
		 * 检查验证码
		 */
        HttpSession session = request.getSession();
        SafeKey safeKey = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
        session.removeAttribute(REGISTER_SECURITYCODE_SESSION);
        session.removeAttribute(REGISTER_CONTENT_SESSION);
        if (safeKey == null) {
            return Message.error("请获取验证码");
        }
        if (safeKey.hasExpired()) {
            return Message.error("验证码过期");
        }
        if (!safeKey.getValue().equals(captcha)) {
            return Message.error("验证码不正确");
        }
		/*
		 * 注册用户
		 */
        Member member = null;
        String password = mobile.substring(mobile.length() - 6, mobile.length());// 帮用户设置默认密码
        Member current = memberService.findByUsername(extension);//这个member是推荐人
        member = memberService.findByUsername(mobile);
        if (member==null) {
            member = memberService.findByBindTel(mobile);
        }
        if (member==null) {
            Setting setting = SettingUtils.get();
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            if (!setting.getIsRegisterEnabled()) {
                return Message.error("不能注册");
            }
            member = EntitySupport.createInitMember();

            Area areaId = current.getArea();//获取推荐人的地区，设置为被推荐人 的地区
            member.setArea(areaId);
            member.setMember(current);
            member.setUsername(mobile);
            member.setPassword(DigestUtils.md5Hex(password));
            member.setPoint(setting.getRegisterPoint());
            member.setAmount(new BigDecimal(0));
            member.setBalance(new BigDecimal(0));
            member.setIsEnabled(true);
            member.setIsLocked(Member.LockType.none);
            member.setLoginFailureCount(0);
            member.setLockedDate(null);
            member.setRegisterIp(request.getRemoteAddr());
            member.setLoginIp(request.getRemoteAddr());
            member.setLoginDate(new Date());
            member.setSafeKey(null);
            member.setBindEmail(Member.BindStatus.none);
            member.setBindMobile(Member.BindStatus.none);
            member.setPaymentPassword(DigestUtils.md5Hex(password));
            member.setRebateAmount(new BigDecimal(0));
            member.setProfitAmount(new BigDecimal(0));
            member.setMemberRank(memberRankService.findDefault());
            member.setFavoriteProducts(null);
            member.setFreezeBalance(new BigDecimal(0));
            member.setPrivilege(0);
            member.setTotalScore((long) 0);
            member.setMobile(mobile);
            member.setEmail("@");
            member.setMobile(mobile);
            member.setBindMobile(BindStatus.binded);
            memberService.save(member);
			/*
			 * 发送短信
			 */
            SmsSend smsSend = new SmsSend();
            smsSend.setMobiles(member.getUsername());
            smsSend.setContent("注册成功,账号:" + member.getUsername() + " 默认密码:" + password + "【"
                    + bundle.getString("signature") + "】");
            smsSend.setType(net.wit.entity.SmsSend.Type.captcha);
            smsSendService.smsSend(smsSend);
        } else {
//            member = memberService.findByUsername(mobile);
			/*
			 * 登录
			 */
            Map<String, Object> attributes = new HashMap<String, Object>();
            member.setLoginIp(request.getRemoteAddr());
            member.setLoginDate(new Date());
            member.setLoginFailureCount(0);
            if (member.getJmessage() == null || !member.getJmessage()) {
                if (PushMessage.jpush_register(member.getUsername(), "rzico@2015")) {
                    member.setJmessage(true);
                }
            }
            if (member.getEmessage() == null || !member.getEmessage()) {
                if (PushMessage.ease_register(member.getId().toString(), "rzico@2015", member.getDisplayName())) {
                    member.setEmessage(true);
                }
            }
            Enumeration<?> keys = session.getAttributeNames();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                attributes.put(key, session.getAttribute(key));
            }
            session.invalidate();
            session = request.getSession();
            for (Entry<String, Object> entry : attributes.entrySet()) {
                session.setAttribute(entry.getKey(), entry.getValue());
            }
            Cart cart = cartService.getCurrent();
            Principal principal = new Principal(member.getId(), member.getUsername());
            request.getSession().setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, principal);
            WebUtils.addCookie(request, response, Member.USERNAME_COOKIE_NAME, member.getUsername());
            if (cart != null) {
                if (cart.getMember() == null) {
                    cartService.merge(member, cart);
                    WebUtils.removeCookie(request, response, Cart.ID_COOKIE_NAME);
                    WebUtils.removeCookie(request, response, Cart.KEY_COOKIE_NAME);
                }
            }
			/*
			 * 判断是否生成优惠码
			 */
            if (member != null && coupon != null && couponCodeService.findCouponCodeByCouponAndMember(coupon, member) == null) {
                List<CouponCode> couponCode = couponCodeService.build(coupon, member, 1);// 生成优惠码
                if (couponCode != null) {
                    coupon.setSendCount(coupon.getSendCount() + 1);
                    couponService.update(coupon);
                    return Message.success("领取成功");
                } else {
                    return Message.error("领取失败");
                }
            } else if (member != null && coupon != null && couponCodeService.findCouponCodeByCouponAndMember(coupon, member) != null) {
                return Message.success("已领取");
            }
        }
		/*
		 * 注册店铺的会员
		 */
        if (tenant != null) {
            Consumer consumer = new Consumer();
            consumer.setMember(member);
            consumer.setStatus(net.wit.entity.Consumer.Status.enable);
            consumer.setMemberRank(memberRankService.findDefault());
            consumer.setTenant(current.getTenant());
            consumerService.save(consumer);
        }
		/*
		 * 绑定微信
		 */
        String openId = (String) request.getSession().getAttribute(Member.WEIXIN_OPENT_ID);
        if (openId != null) {
            BindUser user = bindUserService.findByUsername(openId, Type._wx);

            if (user == null) {
                user = new BindUser();
                user.setUsername(openId);
                user.setPassword(password);
                user.setMember(member);
                user.setType(Type._wx);
                bindUserService.save(user);
            } else {
                user.setUsername(openId);
                user.setPassword(password);
                user.setMember(member);
                user.setType(Type._wx);
                bindUserService.update(user);
            }
        }
		/*
		 * 登录
		 */
        Map<String, Object> attributes = new HashMap<String, Object>();
        member.setLoginIp(request.getRemoteAddr());
        member.setLoginDate(new Date());
        member.setLoginFailureCount(0);
        if (member.getJmessage() == null || !member.getJmessage()) {
            if (PushMessage.jpush_register(member.getUsername(), "rzico@2015")) {
                member.setJmessage(true);
            }
        }
        if (member.getEmessage() == null || !member.getEmessage()) {
            if (PushMessage.ease_register(member.getId().toString(), "rzico@2015", member.getDisplayName())) {
                member.setEmessage(true);
            }
        }
        Enumeration<?> keys = session.getAttributeNames();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            attributes.put(key, session.getAttribute(key));
        }
        session.invalidate();
        session = request.getSession();
        for (Entry<String, Object> entry : attributes.entrySet()) {
            session.setAttribute(entry.getKey(), entry.getValue());
        }
        Cart cart = cartService.getCurrent();
        Principal principal = new Principal(member.getId(), member.getUsername());
        request.getSession().setAttribute(Member.PRINCIPAL_ATTRIBUTE_NAME, principal);
        WebUtils.addCookie(request, response, Member.USERNAME_COOKIE_NAME, member.getUsername());
        if (cart != null) {
            if (cart.getMember() == null) {
                cartService.merge(member, cart);
                WebUtils.removeCookie(request, response, Cart.ID_COOKIE_NAME);
                WebUtils.removeCookie(request, response, Cart.KEY_COOKIE_NAME);
            }
        }
		/*
		 * 判断是否生成优惠码
		 */
        if (coupon != null && member != null && couponCodeService.findCouponCodeByCouponAndMember(coupon, member) == null) {
            List<CouponCode> couponCode = couponCodeService.build(coupon, member, 1);// 生成优惠码
            if (couponCode != null) {
                return Message.success("领取成功");
            }
        }

        return Message.success("注册成功");
    }

    /**
     * 注册或登录后，第一次获取优惠码的页面
     */
    @RequestMapping(value = "/inform", method = RequestMethod.GET)
    public String inform(Long id, ModelMap model) {
        Coupon coupon = couponService.find(id);
        Tenant tenant = coupon.getTenant();
        DeliveryCenter delivery = tenant.getDefaultDeliveryCenter();
        model.addAttribute("delivery", delivery);
        model.addAttribute("member", memberService.getCurrent());
        model.addAttribute("coupon", coupon);
        return "/wap/coupon/inform";
    }

    /**
     * 点击获取优惠码后页面的头像跳转到的页面
     */
    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public String success(HttpServletRequest request, Long id, ModelMap model) {
        Coupon coupon = couponService.find(id);
        Member member = memberService.getCurrent();
        Tenant tenant = coupon.getTenant();
        DeliveryCenter delivery = tenant.getDefaultDeliveryCenter();
        model.addAttribute("delivery", delivery);
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String header = request.getHeader("User-Agent");
        String browseVersion = BrowseUtil.checkBrowse(header);
        if (BrowseUtil.WEIXIN.equalsIgnoreCase(browseVersion)) {
            model.addAttribute("sharedUrl", MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/wap/coupon/view.jhtml?id=" + coupon.getId() + "&extension=" + (member != null ? member.getUsername() : ""))));
        } else {
            model.addAttribute("sharedUrl", URLEncoder.encode(MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/coupon/view.jhtml?id=" + coupon.getId() + "&extension=" + (member != null ? member.getUsername() : ""))));
        }
        model.addAttribute("member", member);
        model.addAttribute("coupon", coupon);
        return "/wap/coupon/success";
    }

    /**
     * 点击详细按钮跳转到的页面
     */
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public String datail(Long id, ModelMap model) {
        Coupon coupon = couponService.find(id);
        Member member = memberService.getCurrent();
        model.addAttribute("member", member);
        model.addAttribute("coupon", coupon);
        return "/wap/coupon/detail";
    }

    /**
     * 我的优惠券
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list() {
        return "/wap/coupon/list";
    }

    /**
     * 套券领取状态
     *
     * @return
     */
    @RequestMapping(value = "/receive/{id}", method = RequestMethod.GET)
    public String receive(@PathVariable Long id, Long no,String type, ModelMap model) {
        Coupon coupon = couponService.find(id);
        if (coupon == null) {
            return "redirect:/wap/index.jhtml";
        }
        Member member = memberService.getCurrent();
        if (member == null) {
            return "redirect:/wap/index.jhtml";
        }
        String success = "none", fail = "none", received = "none";

        if("success".equals(type)){
            success = "";
        }else {
            List<CouponNumber> couponNumbers = couponNumberService.findList(coupon, null,null, no);
            if (couponNumbers != null && couponNumbers.size() > 0) {
                for (CouponNumber couponNumber : couponNumbers) {
                    if (couponNumber.getMember().equals(member)) {
                        received = "";
                    }
                }
            }
        }

        if("none".equals(success)&&"none".equals(received)){
            fail="";
        }

        boolean isCoupon = couponNumberService.findList(coupon, member,null, null).size()>0;
        model.addAttribute("isCoupon", isCoupon);
        model.addAttribute("success", success);
        model.addAttribute("fail", fail);
        model.addAttribute("received", received);
        model.addAttribute("coupon", coupon);
        model.addAttribute("areaId", areaService.getCurrent().getId());
        return "/wap/coupon/receive";
    }

    /**
     * 获取我的优惠券
     *
     * @return
     */
    @RequestMapping(value = "/coupon_list", method = RequestMethod.GET)
    public
    @ResponseBody
    List<CouponCodeModel> couponList() {
        List<CouponCode> couponCodes = couponCodeService.findCoupon(memberService.getCurrent(),couponService.find(0L));
        return CouponCodeModel.bindData(couponCodes);
    }

    /**
     * 获取我的优惠券
     *
     * @return
     */
    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public String balance(ModelMap model) {
        CouponCode couponCode = couponCodeService.findMemberCouponCode(memberService.getCurrent());
        model.addAttribute("amount",couponCode==null? BigDecimal.ZERO:couponCode.getBalance());
        model.addAttribute("areaId", areaService.getCurrent().getId());
        return "/wap/coupon/balance";
    }
    /**
     * 获取我的优惠券
     *
     * @return
     */
    @RequestMapping(value = "/record", method = RequestMethod.GET)
    public String record(ModelMap model) {
        List<CouponNumber> couponNumbers = couponNumberService.findList(null,memberService.getCurrent(),null,null);
        model.addAttribute("couponNumbers",couponNumbers);
        model.addAttribute("areaId", areaService.getCurrent().getId());
        return "/wap/coupon/record";
    }

}
