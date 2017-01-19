/**
 * ====================================================
 * 文件名称: MemberInfoController.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014-9-11			Administrator(创建:创建文件)
 * ====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 */
package net.wit.controller.weixin.member;

import com.google.zxing.WriterException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import net.wit.Filter;
import net.wit.Principal;
import net.wit.Setting;
import net.wit.controller.weixin.BaseController;
import net.wit.controller.weixin.model.BalanceModel;
import net.wit.controller.weixin.model.DataBlock;
import net.wit.controller.weixin.model.MemberModel;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.*;
import net.wit.weixin.main.MenuManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
 * @author Administrator
 * @ClassName: MemberInfoController
 * @Description: 会员资料 密码等
 * @date 2014-9-11 上午11:31:34
 */
@Controller("weixinMemberController")
@RequestMapping("/weixin/member")
public class MemberController extends BaseController {

    public static final String CAPTCHA_SECURITYCODE_SESSION = "captcha_safe_key";
    public static final String CAPTCHA_CONTENT_SESSION = "captcha_code";

    public static final String REGISTER_SECURITYCODE_SESSION = "register_securityCode_session";
    public static final String REGISTER_CONTENT_SESSION = "register_mobile";

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "smsSendServiceImpl")
    private SmsSendService smsSendService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "idcardServiceImpl")
    private IdcardService idcardService;

    @Resource(name = "activityDetailServiceImpl")
    private ActivityDetailService activityDetailService;

    @Resource(name = "activityRulesServiceImpl")
    private ActivityRulesService activityRulesService;

    @Resource(name = "creditServiceImpl")
    private CreditService creditService;

    @Resource(name="memberParameterServiceImpl")
    private MemberParameterService memberParameterService;

    @Resource(name = "cartServiceImpl")
    private CartService cartService;

    @Resource(name = "tradeServiceImpl")
    private TradeService tradeService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "consumerServiceImpl")
    private ConsumerService consumerService;

    @Resource(name = "memberRankServiceImpl")
    private MemberRankService memberRankService;

    @Resource(name = "qrcodeServiceImpl")
    private QrcodeService qrcodeService;
    @Resource(name = "taskServiceImpl")
    private TaskService taskService;

    /**
     * 钱包页面
     */
    @RequestMapping(value = "/purse/index", method = RequestMethod.GET)
    public String purse(String extension,HttpServletRequest request){
        if (extension != null) {
            Member extensions = memberService.findByUsername(extension);
            if (extensions != null) {
                request.getSession().setAttribute(Member.EXTENSION_ATTRIBUTE_NAME, extensions.getUsername());
            }
        }
        return "weixin/member/purse/index";
    }


    /**
     * 会员中心首页
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(String extension,HttpServletRequest request){
        if (extension != null) {
            Member extensions = memberService.findByUsername(extension);
            if (extensions != null) {
                request.getSession().setAttribute(Member.EXTENSION_ATTRIBUTE_NAME, extensions.getUsername());
            }
        }
        return "weixin/member/index";
    }

    /**
     * 绑定手机页面
     */
    @RequestMapping(value = "/bindmobile", method = RequestMethod.GET)
    public String bindmobile(String extension,HttpServletRequest request){
        if (extension != null) {
            Member extensions = memberService.findByUsername(extension);
            if (extensions != null) {
                request.getSession().setAttribute(Member.EXTENSION_ATTRIBUTE_NAME, extensions.getUsername());
            }
        }
        return "weixin/member/bindmobile";
    }

    /**
     * 会员中心首页分享
     */
    @RequestMapping(value = "/share", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock share(){
        Member member=memberService.getCurrent();
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String url = MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/weixin/member/index.jhtml?extension=" + (member != null ? member.getUsername() : "")));
        Map<String,Object> map=new HashMap<>();
        map.put("link", url);
        return DataBlock.success(map,"执行成功");
    }

    /**
     * 会员中心首页数据
     */
    @RequestMapping(value = "/indexView", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock indexView(){
        Member member = memberService.getCurrent();
        if(member==null){
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Map<String,Object> map=new HashMap<>();
        map.put("headImg",member.getHeadImg());//头像
        map.put("username",member.getDisplayName());//显示名
        map.put("memberRank",member.getMemberRank().getName());//会员等级
        map.put("authStatus",member.getAuthStatus());//实名认证状态
        map.put("unshipped",tradeService.count(null,member, Order.QueryStatus.unshipped,null));//待发货
        map.put("unpaid",tradeService.count(null,member, Order.QueryStatus.unpaid,null));//待支付
        map.put("unreciver",tradeService.count(null,member, Order.QueryStatus.shipped,null));//待收货
        map.put("unreview",tradeService.count(null,member, Order.QueryStatus.unreview,null));//待评价
        return DataBlock.success(map,"执行成功");
    }

    /**
     * 读取会员资料
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public
    @ResponseBody
    DataBlock view(Long id) {
        Member member;
        if(id==null){
            member = memberService.getCurrent();
        }else{
            member=memberService.find(id);
        }
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        MemberModel model = new MemberModel();
        model.copyFrom(member);
        return DataBlock.success(model, "执行成功");
    }

    /**
     * 会员二维码页面
     * @return
     */
    @RequestMapping(value = "/becomevip", method = RequestMethod.GET)
    public String becomevip(String extension, HttpServletRequest request){
        Member member=memberService.getCurrent();
        Member extensions=null;//推广人
        Tenant tenant = null;//推广人的店铺
        if (extension != null) {
            extensions = memberService.findByUsername(extension);
            if (extensions != null) {
                request.getSession().setAttribute(Member.EXTENSION_ATTRIBUTE_NAME, extensions.getUsername());
                tenant = extensions.getTenant();
            }
        }else{
            String ext = (String) request.getSession().getAttribute(Member.EXTENSION_ATTRIBUTE_NAME);
            if (StringUtils.isNotBlank(ext)) {
                extensions = memberService.findByUsername(ext);
            }
        }
        if (extensions == null||member==null) {
            return "redirect:/weixin/index.jhtml";
        }
        if(tenant!=null&&!consumerService.consumerExists(member.getId(),tenant.getId())){
            Consumer consumer = new Consumer();
            consumer.setMember(member);
            consumer.setStatus(Consumer.Status.enable);
            consumer.setTenant(tenant);
            consumer.setMemberRank(memberRankService.findDefault());
            consumerService.save(consumer);
        }
        if(member.getMember()==null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            Task task = taskService.findByMember(member,Long.parseLong(sdf.format(new Date())));
            if(task!=null){
                task.setDoInvite(task.getDoInvite()+1);
                taskService.update(task);
            }
            member.setMember(extensions);
            memberService.update(member);
        }
        return "weixin/member/becomevip";
    }


    @RequestMapping(value = "/attention", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock attention(HttpServletRequest request){
        Member extention = null;//推广人
        String ext = (String) request.getSession().getAttribute(Member.EXTENSION_ATTRIBUTE_NAME);
        if (StringUtils.isNotBlank(ext)) {
            extention = memberService.findByUsername(ext);
        }
        Map<String, Object> map = new HashMap<>();
        Setting setting = SettingUtils.get();
        Tenant tenant = null;
        if (extention != null) {
            tenant = extention.getTenant();
        }
        String ticket = "";
        String name = setting.getSiteName();
        if (tenant != null) {
            Qrcode qrcode = qrcodeService.findbyTenant(tenant);
            if (qrcode != null) {
                name = tenant.getName();
                ticket = qrcode.getTicket();
            }
        }
        map.put("hasTenant",tenant!=null);
        map.put("name", name);
        map.put("url", StringUtils.isBlank(ticket) ? null : ("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket));
        return DataBlock.success(map, "执行成功");
    }

    /**
     * 会员二维码
     * @param response
     */
    @RequestMapping(value = "/qrcode", method = RequestMethod.GET)
    public void qrcode(HttpServletResponse response) {
        try {
            Member member=memberService.getCurrent();
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            String url= MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/weixin/member/becomevip.jhtml?extension=" + member.getUsername()));
            String tempFile = System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".jpg";
            response.reset();
            response.setContentType("image/jpeg;charset=utf-8");
            try {
                QRBarCodeUtil.encodeQRCode(url, tempFile, 200, 200);
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
     * 读取会员余额资料
     */
    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public
    @ResponseBody
    DataBlock balance() {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        BalanceModel model = new BalanceModel();
        model.copyFrom(member);
        return DataBlock.success(model, "执行成功");
    }

    /**
     * 进行中未到帐金额
     */
    @RequestMapping(value = "/sumer", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock sumer(){
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        List<Filter> filters = new ArrayList<>();
        List<net.wit.entity.Credit.Status> status=new ArrayList<net.wit.entity.Credit.Status>();
        status.add(net.wit.entity.Credit.Status.wait);
        status.add(net.wit.entity.Credit.Status.wait_success);
        filters.add(new Filter("status", Filter.Operator.in, status));
        filters.add(new Filter("member", Filter.Operator.eq, member));
        List<Credit> list=creditService.findList(null, filters,null);
        BigDecimal amount=new BigDecimal(0);
        for (Credit credit : list) {
            amount=amount.add(credit.getAmount());
        }
        return DataBlock.success(amount,"执行成功");
    }

    /**
     * 修改用户信息
     * params name 姓名
     * params nickName 昵称
     * params birth 生日 2015-05-01
     * params address 详细地址
     * params phone 电话
     * params zipCode 邮政编码
     * params sex 性别  0 男  1 是女
     * params areaId 区域地址
     * params headImg 头像的 URL
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody DataBlock update(MemberParameter memberParameter, String name, String nickName, String birth, String address,String phone, String zipCode, String sex, String areaId, String headImg) {
        Member member = memberService.getCurrent();
        if (member==null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        if (!StringUtils.isEmpty(name)) {
            if (member.getName() != null && !member.getName().equals(name)) {
                if (member.getIdcard() != null && member.getIdcard().getAuthStatus().equals(Idcard.AuthStatus.success)) {
                    return DataBlock.error("实名认证通过，不能修改姓名");
                }
            }
            member.setName(name);

            if(member.getTenant()!=null){
                if(!activityDetailService.isActivity(null,member.getTenant(), activityRulesService.find(12L))){
                    activityDetailService.addPoint(null,member.getTenant(),activityRulesService.find(12L));
                }
            }

        }

        if (!StringUtils.isEmpty(nickName) && nickName != null) {
            member.setNickName(nickName);
        }
        if (!StringUtils.isEmpty(sex) && sex != null) {
            if ("0".equals(sex)) {
                member.setGender(Member.Gender.male);
            } else
            if ("1".equals(sex)){
                member.setGender(Member.Gender.female);
            } else
            if ("male".equals(sex)) {
                member.setGender(Member.Gender.male);
            } else {
                member.setGender(Member.Gender.female);
            }
        }
        if (areaId != null) {
            Area area = areaService.find(Long.valueOf(areaId));
            member.setArea(area);
        }
        if (!StringUtils.isEmpty(birth) && birth != null) {
            member.setBirth(DateUtil.parseDate(birth));
        }
        if (!StringUtils.isEmpty(address) && address != null) {
            member.setAddress(address);
        }
        if (!StringUtils.isEmpty(phone) && phone != null) {
            member.setPhone(phone);
        }
        if (!StringUtils.isEmpty(zipCode) && zipCode != null) {
            member.setZipCode(zipCode);
        }
        if (!StringUtils.isEmpty(headImg) && headImg != null) {
            member.setHeadImg(headImg);

            if(member.getTenant()!=null){
                if(!activityDetailService.isActivity(null,member.getTenant(), activityRulesService.find(11L))){
                    activityDetailService.addPoint(null,member.getTenant(),activityRulesService.find(11L));
                }
            }
        }
        MemberParameter mp=member.getMemberParameter();
        if(mp==null){
            mp=new MemberParameter();
        }
        mp.setMember(member);
        mp.setHeight(memberParameter.getHeight());
        mp.setWeight(memberParameter.getWeight());
        mp.setBust(memberParameter.getBust());
        mp.setWaistline(memberParameter.getWaistline());
        mp.setHips(memberParameter.getHips());
        mp.setShoulderWidth(memberParameter.getShoulderWidth());
        mp.setFrontClothingLength(memberParameter.getFrontClothingLength());
        mp.setBackClothingLength(memberParameter.getBackClothingLength());
        mp.setSleeveLength(memberParameter.getSleeveLength());
        mp.setCuff(memberParameter.getCuff());
        mp.setHem(memberParameter.getHem());
        mp.setPantsLength(memberParameter.getPantsLength());
        mp.setThighAround(memberParameter.getThighAround());
        mp.setLegsAround(memberParameter.getLegsAround());
        mp.setCalfAround(memberParameter.getCalfAround());
        memberParameterService.save(mp);
        member.setMemberParameter(mp);
        memberService.save(member);
        return DataBlock.success("success","修改成功");
    }

    /**
     * 提交实名认证
     * params name 姓名
     * params idcard 身份证号
     * params pathFront 身份证正面拍照  url
     * params pathBack 身份证反面提照  url
     */
    @RequestMapping(value = "/idcard/save", method = RequestMethod.POST)
    public @ResponseBody DataBlock idcardSave(String name, String idCard, String pathFront, String pathBack) {
        Member member = memberService.getCurrent();
        if (member==null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Idcard card = member.getIdcard();
        if (pathFront == null || "".equals(pathFront)) {
            return DataBlock.error("无拍照图片");
        }
        if (pathBack == null || "".equals(pathBack)) {
            return DataBlock.error("无拍照图片");
        }
        if (name == null || "".equals(name)) {
            return DataBlock.error("请正确输入姓名");
        }
        if (card == null) {
            card = new Idcard();
        }
        card.setPathFront(pathFront);
        card.setPathBack(pathBack);
        card.setNo(idCard);
        card.setAddress("#");
        card.setBeginDate(new Date());
        card.setEndDate(new Date());
        card.setAuthStatus(Idcard.AuthStatus.wait);
        card.setName(name);
        idcardService.save(card);
        member.setIdcard(card);
        memberService.save(member);
        return DataBlock.success("success","提交成功");
    }

    /**
     * 检测邀请码是否合法
     */
    @RequestMapping(value = "/invite_code", method = RequestMethod.GET)
    public @ResponseBody DataBlock InviteCode(Long code) {
        if(code==null){
            return DataBlock.error("无效邀请码");
        }
        Long id = code-100000L;
        Member member = memberService.find(id);
        if (member==null) {
            return DataBlock.error("无效邀请码");
        }
        return DataBlock.success(id,"执行成功");
    }

    /**
     * 绑定手机获取验证码
     *
     * @param mobile 手机号
     */
    @RequestMapping(value = "/sendCode", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock sendCode(String mobile, HttpServletRequest request) {
        if (StringUtils.isEmpty(mobile)) {
            return DataBlock.error("手机号为空");
        }
        HttpSession session = request.getSession();
        Setting setting = SettingUtils.get();
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        int challege = SpringUtils.getIdentifyingCode();
        String securityCode = String.valueOf(challege);
        SafeKey tmp = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
        if (tmp != null && !tmp.hasExpired()) {
            securityCode = tmp.getValue();
            if (!tmp.canReset()) {
                return DataBlock.error("系统忙，稍等几秒重试");
            }
        }
        SafeKey safeKey = new SafeKey();
        safeKey.setValue(securityCode);
        safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()) : null);
        session.setAttribute(REGISTER_SECURITYCODE_SESSION, safeKey);
        session.setAttribute(REGISTER_CONTENT_SESSION, mobile);
        SmsSend smsSend = new SmsSend();
        smsSend.setMobiles(mobile);
        smsSend.setContent("验证码 :" + securityCode + ",为了您的账户安全请不要转发他人.【" + bundle.getString("signature") + "】");
        smsSend.setType(net.wit.entity.SmsSend.Type.captcha);
        smsSendService.smsSend(smsSend);
        return DataBlock.success("success","消息发送成功");
    }

    /**
     * 绑定手机
     * @param mobile 手机号
     * @param captcha 验证码
     * @param inviteCode 邀请码
     */
    @RequestMapping(value = "/bind_mobile", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock bindMobile(String mobile, String captcha, String inviteCode, HttpServletRequest request) {
        String extension = (String) request.getSession().getAttribute(Member.EXTENSION_ATTRIBUTE_NAME);
        HttpSession session = request.getSession();
        SafeKey safeKey = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
        if (safeKey == null) {
            return DataBlock.error("验证码过期了");
        }
        if (safeKey.hasExpired()) {
            return DataBlock.error("验证码过期了");
        }
        if (!safeKey.getValue().equals(captcha)) {
            return DataBlock.error("验证码不正确");
        }
        Member current;
        if (inviteCode != null && !"".equals(inviteCode)) {
            if (inviteCode.length() != 6) {
                return DataBlock.error("邀请码不正确");
            }
            Long _inviteCode = Long.valueOf(inviteCode) - 100000;
            current = memberService.find(_inviteCode);
            if (current == null) {
                return DataBlock.error("您输入的邀请码不是导购或店主邀请码");
            }
        } else {
            current = memberService.findByUsername(extension);
        }
        session.removeAttribute(REGISTER_SECURITYCODE_SESSION);
        session.removeAttribute(REGISTER_CONTENT_SESSION);
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error("当前会话无效");
        }
        if (member.getBindMobile().equals(Member.BindStatus.binded)) {
            return DataBlock.error("已经绑定，不能重复操作");
        }
        if (memberService.findByBindTel(mobile) != null) {
            return DataBlock.error("当前手机已经被占用，不能重复绑定");
        }
        if (current != null) {
            member.setMember(current);
            member.setShareOwner(current.getTenant());
        }
        member.setUsername(mobile);
        member.setMobile(mobile);
        member.setBindMobile(Member.BindStatus.binded);
        memberService.save(member);
        return DataBlock.success("success","绑定手机成功");
    }

    /**
     * 找回密码发送验证码
     */
    @RequestMapping(value = "/send_mobile", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock sendMobile(HttpServletRequest request) {
        Member member = memberService.getCurrent();
        if (member==null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        HttpSession session = request.getSession();
        Setting setting = SettingUtils.get();
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        int challege = SpringUtils.getIdentifyingCode();
        String securityCode = String.valueOf(challege);
        SafeKey tmp = (SafeKey) session.getAttribute(CAPTCHA_SECURITYCODE_SESSION);
        if (tmp!=null && !tmp.hasExpired()) {
            securityCode = tmp.getValue();
            if (!tmp.canReset()) {
                return DataBlock.error("系统忙，稍等几秒重试");
            }
        }
        if (!member.getBindMobile().equals(Member.BindStatus.binded) ) {
            return DataBlock.error("当前用户没有绑定手机号");
        }
        String mobile = member.getMobile();
        SafeKey safeKey = new SafeKey();
        safeKey.setValue(securityCode);
        safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()) : null);
        session.setAttribute(CAPTCHA_SECURITYCODE_SESSION, safeKey);
        session.setAttribute(CAPTCHA_CONTENT_SESSION, mobile);

        SmsSend smsSend=new SmsSend();
        smsSend.setMobiles(mobile);
        smsSend.setContent("验证码 :" + securityCode + ",为了您的账户安全请不要转发他人.【" + bundle.getString("signature") + "】");
        smsSend.setType(SmsSend.Type.captcha);
        smsSendService.smsSend(smsSend);
        return DataBlock.success("success","发送成功");
    }

    /**
     * 找回登录密码
     * captcha 验证码
     * newPass 新密码（加密后）
     */
    @RequestMapping(value = "/password/retrieve", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock retrievePassword(String captcha, String newPass, HttpServletRequest request) {
        Member member = memberService.getCurrent();
        if (member==null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        HttpSession session = request.getSession();
        SafeKey safeKey = (SafeKey) session.getAttribute(CAPTCHA_SECURITYCODE_SESSION);
        String code = (String) session.getAttribute(CAPTCHA_CONTENT_SESSION);
        session.removeAttribute(CAPTCHA_SECURITYCODE_SESSION);
        session.removeAttribute(CAPTCHA_CONTENT_SESSION);
        String newPwd = rsaService.decryptParameter("newPass",request);
        if (safeKey == null) {
            return DataBlock.error("验证码过期了");
        }
        if (safeKey.hasExpired()) {
            return DataBlock.error("验证码过期了");
        }
        if (!safeKey.getValue().equals(captcha)) {
            return DataBlock.error("验证码不正确");
        }
        if (!member.getMobile().equals(code) || member.getBindMobile() != Member.BindStatus.binded) {
            return DataBlock.error("该用户手机未绑定");
        }
        member.setPassword(DigestUtils.md5Hex(newPwd));
        member.setLoginFailureCount(0);
        member.setIsLocked(Member.LockType.none);
        member.setLockedDate(null);
        memberService.update(member);
        return DataBlock.success("success", "修改成功");
    }

    /**
     * 找回支付密码
     * params captcha  手机发送时收到的验证码
     * params newPass 新密码 （需要加密）
     */
    @RequestMapping(value = "/payPassword/retrieve", method = RequestMethod.POST)
    public @ResponseBody DataBlock retrievePayPassword(String captcha, String newPass, HttpServletRequest request) {
        Member member = memberService.getCurrent();
        if (member==null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        HttpSession session = request.getSession();
        SafeKey safeKey = (SafeKey) session.getAttribute(CAPTCHA_SECURITYCODE_SESSION);
        String code = (String) session.getAttribute(CAPTCHA_CONTENT_SESSION);
        session.removeAttribute(CAPTCHA_SECURITYCODE_SESSION);
        session.removeAttribute(CAPTCHA_CONTENT_SESSION);
        String newPwd = rsaService.decryptParameter("newPass",request);
        if (safeKey == null) {
            return DataBlock.error("验证码过期了");
        }
        if (safeKey.hasExpired()) {
            return DataBlock.error("验证码过期了");
        }
        if (!safeKey.getValue().equals(captcha)) {
            return DataBlock.error("验证码不正确");
        }
        if (!member.getMobile().equals(code) || !member.getBindMobile().equals(Member.BindStatus.binded) ) {
            return DataBlock.error("该用户手机未绑定");
        }
        member.setPaymentPassword(DigestUtils.md5Hex(newPwd));
        memberService.update(member);
        return DataBlock.success("success","修改成功");
    }

    /**
     * 我的推广分享页
     * @return
     */
    @RequestMapping(value = "/promoting", method = RequestMethod.GET)
    public String promoting(String extension, HttpServletRequest request) {
        if (extension != null) {
            Member extensions = memberService.findByUsername(extension);
            if (extensions != null) {
                request.getSession().setAttribute(Member.EXTENSION_ATTRIBUTE_NAME, extensions.getUsername());
            }
        }
        return "weixin/member/promoting";
    }

    /**
     * 我的推广
     * rebate 推广分润金额
     * count 推广人数
     */
    @RequestMapping(value = "/extension", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock extension(){
        Member member=memberService.getCurrent();
        if(member==null){
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Map<String,Object> map=new HashMap<>();
        map.put("rebate",member.getRebateAmount());//分润
        map.put("count",memberService.findList(member).size());//邀请的人数
        map.put("username",member.getMember()!=null?member.getMember().getDisplayName():"");//我的推荐人
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        String url = MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/weixin/member/promoting.jhtml?extension=" + member.getUsername()));
        map.put("link", url);
        map.put("title","好货多多,您的好友喊您来挑货啦!");
        map.put("desc", "推广新用户,注册有惊喜。发展会员,享受永久分润。会员越多收入越多。");
        map.put("imgUrl", member.getHeadImg());
        return DataBlock.success(map,"执行成功");
    }

    @RequestMapping(value = "/lockQrcode", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock lock() {
        Member member = memberService.getCurrent();
        List<Qrcode> qrcodeList = qrcodeService.findUnLockList(10, member);
        Qrcode qrcode = null;
        for (Qrcode qr : qrcodeList) {
            if (!qr.isLocked(member)) {
                qrcode = qr;
                break;
            }
        }
        return DataBlock.success(qrcode, "登录成功");
    }


    /**
     * 用户登录(测试)
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock login(Long id,String username, String mobile,HttpServletRequest request,HttpServletResponse response){
        Member member=null;
        if(id!=null){
            member=memberService.find(id);
        }else if(username!=null){
            member=memberService.findByUsername(username);
        }else if (mobile!=null){
            member=memberService.findByTel(mobile);
        }
        if(member==null){
            return DataBlock.error("用户不存在");
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
        return DataBlock.success("success","登录成功");
    }

}
