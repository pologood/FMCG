package net.wit.controller.helper.member;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.app.model.DataBlock;
import net.wit.entity.*;
import net.wit.entity.Member.BindStatus;

import net.wit.service.*;
import net.wit.weixin.main.MenuManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.zxing.WriterException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import net.wit.constant.Constant;
import net.wit.constant.Constant.Cookies;
import net.wit.entity.Order.PaymentStatus;
import net.wit.entity.Tenant.TenantType;
import net.wit.entity.Order.OrderStatus;
import net.wit.entity.Order.ShippingStatus;
import net.wit.support.EntitySupport;
import net.wit.uic.api.UICService;
import net.wit.util.DESUtil;
import net.wit.util.JsonUtils;
import net.wit.util.QRBarCodeUtil;
import net.wit.util.SettingUtils;
import net.wit.util.SpringUtils;
import net.wit.util.WebUtils;

@Controller("helperMemberTenantController")
@RequestMapping("/helper/member/tenant")
public class TenantController extends BaseController {
    public static final String REGISTER_SECURITYCODE_SESSION = "register_safe_key";
    public static final String REGISTER_CONTENT_SESSION = "register_mobile";
    /**
     * 每页记录数
     */
    private static final int PAGE_SIZE = 10;//vane添加

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "rsaServiceImpl")
    private RSAService rsaService;

    @Resource(name = "memberRankServiceImpl")
    private MemberRankService memberRankService;

    @Resource(name = "smsSendServiceImpl")
    private SmsSendService smsSendService;

    @Resource(name = "tenantCategoryServiceImpl")
    private TenantCategoryService tenantCategoryService;

    @Resource(name = "tradeServiceImpl")
    private TradeService tradeService;

    @Resource(name = "productImageServiceImpl")
    private ProductImageService productImageService;

    @Resource(name = "hostServiceImpl")
    private HostService hostService;

    @Resource(name = "uicService")
    private UICService uicService;

    @Resource(name = "tenantRelationServiceImpl")
    private TenantRelationService tenantRelationService;

    @Resource(name = "deliveryCenterServiceImpl")
    private DeliveryCenterService deliveryCenterService;

    @Resource(name = "employeeServiceImpl")
    private EmployeeService employeeService;

    @Resource(name = "tagServiceImpl")
    private TagService tagService;

    @Resource(name = "activityDetailServiceImpl")
    private ActivityDetailService activityDetailService;

    @Resource(name = "activityRulesServiceImpl")
    private ActivityRulesService activityRulesService;


    @Resource(name = "roleServiceImpl")
    private RoleService roleService;

    /**
     * 商家分类
     */
    @RequestMapping(value = "/tenantCategory", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<Long, String> category(Long parentId) {
        List<TenantCategory> categorys = new ArrayList<TenantCategory>();
        TenantCategory parent = tenantCategoryService.find(parentId);
        if (parent != null) {
            categorys = new ArrayList<TenantCategory>(parent.getChildren());
        } else {
            categorys = tenantCategoryService.findRoots();
        }
        Map<Long, String> options = new HashMap<Long, String>();
        for (TenantCategory category : categorys) {
            options.put(category.getId(), category.getName());
        }
        return options;
    }

    /**
     * 检查用户名是否存在
     */
    @RequestMapping(value = "/check_domain", method = RequestMethod.GET)
    public
    @ResponseBody
    boolean checkDomain(String domain) {
        if (StringUtils.isEmpty(domain)) {
            return false;
        }
        Setting setting = SettingUtils.get();
        String host = setting.getSiteUrl();
        String[] hosts = host.split("\\.");
        String b2b = host;
        if (hosts.length > 2) {
            b2b = hosts[hosts.length - 2] + "." + hosts[hosts.length - 1];
        }
        System.out.println(b2b);
        if (domain.equals(setting.getSiteUrl())) {
            return false;
        }
        if (tenantService.domainExists(domain)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Long pageActive, ModelMap model) {
        Member member = memberService.getCurrent();
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            tenant = EntitySupport.createInitTenant();
            if (member.getArea() != null) {
                tenant.setArea(member.getArea());
            }
            tenant.setTenantType(Tenant.TenantType.tenant);
            tenant.setAddress(member.getAddress());
            tenant.setLinkman(member.getName());
            tenant.setTelephone(member.getMobile());
        }
        model.addAttribute("tenantCategoryTree", tenantCategoryService.findTree());
        model.addAttribute("tenant", tenant);
        if (pageActive == null) {
            pageActive = 2L;
        }
        model.addAttribute("pageActive", pageActive);
        model.addAttribute("member", member);
        return "/helper/member/tenant/add";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public
    @ResponseBody
    Message save(Long id, String name, String address, Long tenantCategoryId, String licensePhoto, String linkman, String telephone, Long areaId, RedirectAttributes redirectAttributes) {
        Member member = memberService.getCurrent();
        Tenant saveTenant = member.getTenant();
        if (saveTenant == null) {
            saveTenant = EntitySupport.createInitTenant();
            if (member.getArea() != null) {
                saveTenant.setArea(member.getArea());
            }
            saveTenant.setTenantType(Tenant.TenantType.tenant);
            saveTenant.setAddress(member.getAddress());
            saveTenant.setLinkman(member.getName());
            saveTenant.setTelephone(member.getMobile());
        }

        if (saveTenant.getCode() == null) {
            saveTenant.setCode("1");
        }

        saveTenant.setTenantCategory(tenantCategoryService.find(tenantCategoryId));
        saveTenant.setArea(areaService.find(areaId));
        saveTenant.setName(name);
        if (saveTenant.getShortName() == null) {
            saveTenant.setShortName(name);
        }
        saveTenant.setAddress(address);
        saveTenant.setLinkman(linkman);
        saveTenant.setTelephone(telephone);
        saveTenant.setLicensePhoto(licensePhoto);
        saveTenant.setTenantType(TenantType.tenant);
        //saveTenant.setStatus(Tenant.Status.confirm);
        tenantService.save(saveTenant, member, null);
        //// TODO: 2016/7/21 注册账号，并成功设置店铺名称，即完成任务
        activityDetailService.addPoint(null, saveTenant, activityRulesService.find(1L));

        return Message.success("申请成功");
    }

    /**
     * 基本信息
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(HttpServletRequest request, ModelMap model) {
        Member member = memberService.getCurrent();
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            tenant = EntitySupport.createInitTenant();
            if (member.getArea() != null) {
                tenant.setArea(member.getArea());
            }
            tenant.setTenantType(Tenant.TenantType.tenant);
            tenant.setAddress(member.getAddress());
            tenant.setLinkman(member.getName());
            tenant.setTelephone(member.getMobile());
        }

        String host = request.getServerName();
        String[] hosts = host.split("\\.");
        String b2b = host;
        if (hosts.length > 2) {
            b2b = tenant.getId() + "." + hosts[hosts.length - 2] + "." + hosts[hosts.length - 1];
        }

        model.addAttribute("tenantUrl", b2b);
        model.addAttribute("tenant", tenant);
        model.addAttribute("pageActive", 2);

        model.addAttribute("member", member);

        return "/helper/member/tenant/edit";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Long tenantCategoryId, long areaId, Tenant tenant, RedirectAttributes redirectAttributes) {
        Tenant saveTenant = null;
        Member member = memberService.getCurrent();
        if (tenant.getId() != null) {
            saveTenant = tenantService.find(tenant.getId());
        }
        if (saveTenant == null) {
            saveTenant = EntitySupport.createInitTenant();
            saveTenant.setCode("1");
            saveTenant.setScore(0F);
            saveTenant.setTotalScore(0L);
            saveTenant.setScoreCount(0L);
            saveTenant.setHits(0L);
            saveTenant.setWeekHits(0L);
            saveTenant.setMonthHits(0L);
            saveTenant.setNoReason(tenant.getNoReason());
            saveTenant.setTamPo(tenant.getTamPo());
            saveTenant.setToPay(tenant.getToPay());
            Host host = hostService.find(new Long(1));
            saveTenant.setHost(host);
        }
        saveTenant.setIntroduction(tenant.getIntroduction());
        saveTenant.setAuthorization(tenant.getAuthorization());
        saveTenant.setLogo(tenant.getLogo());
        saveTenant.setThumbnail(tenant.getThumbnail());
        saveTenant.setTenantType(tenant.getTenantType());
        //saveTenant.setName(tenant.getName());
        saveTenant.setAddress(tenant.getAddress());
        if (saveTenant.getDomain() == null) {
            saveTenant.setDomain(tenant.getDomain());
        }
        saveTenant.setLinkman(tenant.getLinkman());
        saveTenant.setTelephone(tenant.getTelephone());
        saveTenant.setScopeOfBusiness(tenant.getScopeOfBusiness());
        saveTenant.setLicenseCode(tenant.getLicenseCode());
        saveTenant.setArea(areaService.find(areaId));
        saveTenant.setShortName(tenant.getShortName());
        saveTenant.setName(tenant.getShortName());//vane
        saveTenant.setQq(tenant.getQq());
        saveTenant.setRangeInfo(tenant.getRangeInfo());
        saveTenant.setTenantCategory(tenantCategoryService.find(tenantCategoryId));
        saveTenant.setNoReason(tenant.getNoReason());
        saveTenant.setTamPo(tenant.getTamPo());
        saveTenant.setToPay(tenant.getToPay());
//        tenantService.save(saveTenant, member, null);
        tenantService.update(saveTenant);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);

        if (tenant.getAddress() != null) {
            //// TODO: 2016/7/21 我的店铺>店铺资料 录入店铺地址
            activityDetailService.addPoint(null, saveTenant, activityRulesService.find(2L));
        }

        if (tenant.getLogo() != null) {
            //// TODO: 2016/7/21 我的店铺>店铺资料 上传头像
            activityDetailService.addPoint(null, saveTenant, activityRulesService.find(3L));
        }

        if (tenant.getThumbnail() != null) {
            //// TODO: 2016/7/21 我的店铺>店铺资料 上传店招图片
            activityDetailService.addPoint(null, saveTenant, activityRulesService.find(4L));
        }

        return "redirect:edit.jhtml";
    }

    /**
     * 上传图片
     */
    @RequestMapping(value = "/addImage", method = RequestMethod.POST)
    public String addImage(Tenant tenant, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        Member member = memberService.getCurrent();
        Tenant saveTenant = member.getTenant();
        for (Iterator<ProductImage> iterator = tenant.getTenantImages().iterator(); iterator.hasNext(); ) {
            ProductImage productImage = iterator.next();
            if (productImage == null || productImage.isEmpty()) {
                iterator.remove();
                continue;
            }
        }
        for (ProductImage productImage : tenant.getTenantImages()) {
            if (productImage.getLocal() != null) {
                productImage.setLocalFile(new File(productImage.getLocal()));
                productImageService.build(productImage);
            }
        }

        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        saveTenant.setTenantImages(tenant.getTenantImages());
        tenantService.save(saveTenant);
        return "redirect:/helper/member/ad/list.jhtml?";
    }

    /**
     * 状态
     */
    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public String status(ModelMap model) {
        Member member = memberService.getCurrent();
        model.addAttribute("member", member);
        model.addAttribute("tenant", member.getTenant());
        model.addAttribute("pageActive", 2);
        return "/helper/member/tenant/status";
    }

    /**
     * 关注商家
     */
    @RequestMapping(value = "/attenTenant", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    public
    @ResponseBody
    Message attenTenant(Long memberId, HttpServletRequest request) {
        Member member = null;
        String uctoken = WebUtils.getCookie(request, Cookies.UC_TOKEN);
        if (StringUtils.isNotBlank(uctoken)) {
            uctoken = DESUtil.decrypt(uctoken, Constant.generateKey);
            Principal principal = JsonUtils.toObject(uctoken, Principal.class);
            member = memberService.findByUsername(principal.getUsername());
        }
        Member agent = memberService.find(memberId);
        if (member == null) {
            return Message.error("关注失败");
        }
        if (member.getMember() == null) {
            member.setMember(agent);
        }
        member.getFavoriteTenants().add(agent.getTenant());
        memberService.update(member);
        return Message.success("关注成功");
    }

    /**
     * 点击数
     */
    @RequestMapping(value = "/hits/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Long hits(@PathVariable Long id) {
        return tenantService.viewHits(id);
    }

    /**
     * 根据
     */
    @RequestMapping(value = "/qrcode", method = RequestMethod.GET)
    public void qrcode(HttpServletRequest request, HttpServletResponse response) {
        Member member = memberService.getCurrent();
        try {
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            // 第三方用户唯一凭证
            String appId = bundle.getString("APPID");// 睿商圈
            Tenant tenant = member.getTenant();
            String redirectUri = URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/wap/delivery/" + tenant.getDefaultDeliveryCenter().getId() + "/index.jhtml");
            String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri=" + redirectUri + "&response_type=code&scope=snsapi_base&state=123&from=singlemessage&isappinstalled=1#wechat_redirect";

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
     * 根据
     */
    @RequestMapping(value = "/qrcode/employee", method = RequestMethod.GET)
    public void qrcodeEmployee(String mobile, HttpServletResponse response) {
        Member member = memberService.findByBindTel(mobile);
        try {
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            // 第三方用户唯一凭证
            String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/share/index.jhtml?extension=" + (member != null ? member.getUsername() : ""));
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
     * 主页面
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(ModelMap model) {
        Member member = memberService.getCurrent();
        Tenant tenant = member.getTenant();
        model.addAttribute("member", member);
        if (tenant == null) {
            return "redirect:status.jhtml";
        }
        if (tenant.equals(Tenant.Status.fail) || tenant.equals(Tenant.Status.none)) {
            return "redirect:status.jhtml";
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
        BigDecimal sales = tradeService.countSales(tenant, df.format(new Date()));
        tenant.setMonthSales(sales);
        model.addAttribute("tenant", tenant);
        Long success = 0L;
        Long wait = 0L;
        Long fail = 0L;
        Map<MemberRank, Long> data = new HashMap<MemberRank, Long>();
        Page<TenantRelation> page = tenantRelationService.findPage(tenant, null, new Pageable());
        for (TenantRelation relation : page.getContent()) {
            if (relation.getStatus().equals(TenantRelation.Status.success)) {
                success++;
                if (data.containsKey(relation.getMemberRank())) {
                    Long tmp = data.get(relation.getMemberRank());
                    tmp++;
                    data.put(relation.getMemberRank(), tmp);
                } else {
                    data.put(relation.getMemberRank(), 1L);
                }
            } else if (relation.getStatus().equals(TenantRelation.Status.none)) {
                wait++;
            } else if (relation.getStatus().equals(TenantRelation.Status.fail)) {
                fail++;
            }

        }
        //model.addAttribute("FavoriteCount", tenant.getFavoriteMembers().size());
        model.addAttribute("FavoriteCount", success);
        model.addAttribute("MemberWaits", wait);
        model.addAttribute("Memberfails", fail);
        model.addAttribute("MemberCounts", data);
        model.addAttribute("MemberRanks", memberRankService.findAll());

        Long waitShipping = tradeService.count(member.getTenant(), OrderStatus.confirmed, null, ShippingStatus.unshipped, null, null);
        Long waitSigned = tradeService.count(member.getTenant(), OrderStatus.confirmed, null, ShippingStatus.shipped, null, null);
        Long waitPayment = tradeService.count(member.getTenant(), null, PaymentStatus.unpaid, null, null, false);
        Long waitReturn = tradeService.count(member.getTenant(), OrderStatus.confirmed, null, null, true, null);
        Long competedCount = tradeService.count(member.getTenant(), OrderStatus.completed, null, null, null, false);
        model.addAttribute("waitShipping", waitShipping);
        model.addAttribute("waitSigned", waitSigned);
        model.addAttribute("waitPayment", waitPayment);
        model.addAttribute("waitReturn", waitReturn);
        model.addAttribute("notCompleted", waitShipping + waitSigned + waitPayment + waitReturn);
        model.addAttribute("allCount", waitShipping + waitSigned + waitPayment + waitReturn + competedCount);
        model.addAttribute("pageActive", 2);
        model.addAttribute("member", member);
        return "/helper/member/tenant/index";
    }

    /**
     * 会员列表
     */
    @RequestMapping(value = "/member/list", method = RequestMethod.GET)
    public String memberList(Integer pageNumber, ModelMap model) {
        Member agent = memberService.getCurrent();
        if (agent == null) {
            return ERROR_VIEW;
        }
        Tenant tenant = agent.getTenant();
        if (tenant == null) {
            return "redirect:/box/register/add.jhtml";
        }
        Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
        model.addAttribute("page", memberService.findPageMyMember(agent, pageable));
        model.addAttribute("agent", agent);
        model.addAttribute("member", agent);
        return "/helper/member/constomer/list";
    }

    /**
     * 注册会员页面
     */
    @RequestMapping(value = "/member/add", method = RequestMethod.GET)
    public String memberRegister(HttpServletRequest request, HttpServletResponse response, ModelMap model, RedirectAttributes redirectAttributes) {
        Member agent = memberService.getCurrent();
        model.addAttribute("agent", agent);
        model.addAttribute("genders", Member.Gender.values());
        model.addAttribute("captchaId", UUID.randomUUID().toString());
        Tenant tenant = agent.getTenant();
        if (tenant == null) {
            addFlashMessage(redirectAttributes, Message.error("您的店铺没有通过认证，不能添加会员"));
            return "redirect:/box/index.jhtml";
        }
        if (tenant.getStatus() != Tenant.Status.success) {
            addFlashMessage(redirectAttributes, Message.error("您的店铺没有通过认证，不能添加会员"));
        }
        model.addAttribute("members", memberService.findListEmployee(agent));
        model.addAttribute("member", agent);
        return "/helper/member/constomer/add";
    }

    /**
     * 注册提交
     */
    @RequestMapping(value = "/member/save", method = RequestMethod.POST)
    public
    @ResponseBody
    Message addMember(String mobile, String securityCode, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        Member agent = memberService.getCurrent();
        if (agent == null) {
            return Message.error("您的会话已经失效，请重新登录");
        }

        String password = rsaService.decryptParameter("enPassword", request);
        rsaService.removePrivateKey(request);

        if (password == null) {
            password = "123456";
        }
        Member.RegType regType = Member.RegType.mobile;

        Message msg = uicService.addMember(mobile, password, securityCode, agent.getArea().getId(), regType, agent.getUsername(), request, response);
        if (msg.getType().equals(Message.Type.success)) {
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            SmsSend smsSend = new SmsSend();
            smsSend.setMobiles(mobile);
            smsSend.setContent("您的账号注册成功,账号:" + mobile + ",初始密码:" + password + ",请登录修改密码。【" + bundle.getString("signature") + "】");
            smsSend.setType(SmsSend.Type.captcha);
            smsSendService.smsSend(smsSend);
        }
        return msg;
    }

    /**
     * 员工列表
     */
    @RequestMapping(value = "/employee/list", method = RequestMethod.GET)
    public String list(ModelMap model, Pageable pageable, String keyWord) {
        Member member = memberService.getCurrent();
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return "redirect:/helper/register/add.jhtml";
        }
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("tenant", Filter.Operator.eq, tenant));
        pageable.setFilters(filters);
        Page<Employee> page = employeeService.findPage(pageable,null, keyWord);
        model.addAttribute("keyWord", keyWord);
        model.addAttribute("page", page);
        //角色
        pageable=new Pageable();
        filters = new ArrayList<>();
        if (tenant==null){
            filters.add(new Filter("isSystem", Filter.Operator.eq, true));
        }else{
            filters.add(new Filter("tenant", Filter.Operator.eq, tenant));
        }
        pageable.setFilters(filters);
        Page<Role> _page = roleService.findPage(Role.RoleType.helper, pageable);
        model.addAttribute("roles", _page.getContent());
        return "/helper/member/tenant/employee/list";
    }

    /**
     * 员工移除
     */
    @RequestMapping(value = "/employee/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Message delete(Long[] ids) {
        Member member = memberService.getCurrent();
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return Message.error("还没有开通店铺，快去申请吧。");
        }
        for (Long id : ids) {
            Employee employee = employeeService.find(id);
            if (employee.getMember().equals(tenant.getMember())) {
                return Message.error("店主不能删除");
            }
            if (employee.getMember().equals(member)) {
                return Message.error("不能删除自己,");
            }
            employee.getMember().setTenant(null);
            memberService.update(employee.getMember());
            employeeService.delete(employee);
        }
        return Message.success("删除成功");
    }

    /**
     * 员工添加
     */
    @RequestMapping(value = "/employee/add", method = RequestMethod.GET)
    public String add(ModelMap model) {
        Member member = memberService.getCurrent();
        List<DeliveryCenter> deliveryCenterList = deliveryCenterService.findMyAll(member);

        //角色
        Tenant tenant=member.getTenant();
        Pageable pageable=new Pageable();
        List<Filter> filters = new ArrayList<>();
        if (tenant==null){
            filters.add(new Filter("isSystem", Filter.Operator.eq, true));
        }else{
            filters.add(new Filter("tenant", Filter.Operator.eq, tenant));
        }
        pageable.setFilters(filters);
        Page<Role> page = roleService.findPage(Role.RoleType.helper, pageable);
        model.addAttribute("roles", page.getContent());

        model.addAttribute("deliveryCenterList", deliveryCenterList);
        model.addAttribute("types", TenantRules.Type.values());
        return "/helper/member/tenant/employee/add";
    }

    /**
     * 添加会员
     */
    @RequestMapping(value = "/employee/add", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock add(String usernumber,String newPassword,String mobile, Long deliveryCenterId, Long[] roles, String username, Member.Gender gender, String address, String captcha, HttpServletRequest request, HttpServletResponse response) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return DataBlock.error("还没有开通店铺，快去申请吧。");
        }
        DeliveryCenter deliveryCenter = deliveryCenterService.find(deliveryCenterId);
        if (deliveryCenter == null) {
            return DataBlock.error("无效门店id。");
        }

        //手机验证码
        if(mobile!=null) {
            HttpSession session = request.getSession();
            SafeKey safeKey = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
            session.removeAttribute(REGISTER_SECURITYCODE_SESSION);
            session.removeAttribute(REGISTER_CONTENT_SESSION);
            if (safeKey == null) {
                return DataBlock.error("验证码过期了");
            }
            if (safeKey.hasExpired()) {
                return DataBlock.error("验证码过期了");
            }
            if (!safeKey.getValue().equals(captcha)) {
                return DataBlock.error("验证码不正确");
            }
        }
        boolean isExists=false;//是否存在
        String _mobileTemp="";//手机号或者工号
        if (mobile!=null){
            isExists= memberService.usernameExists(mobile) || memberService.mobileExists(mobile);
            _mobileTemp=mobile;
        }else if (usernumber!=null){
            isExists= memberService.usernameExists(usernumber);
            _mobileTemp=usernumber;
        }

        Member employee = null;
        if (!isExists) {//检查手机是否已经注册
            String password =newPassword;
            if (mobile!=null) {
                password = mobile.substring(mobile.length() - 6, mobile.length());
            }

            Setting setting = SettingUtils.get();
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            if (!setting.getIsRegisterEnabled()) {
                return DataBlock.error("系统关闭注册");
            }
            employee = EntitySupport.createInitMember();

            employee.setArea(deliveryCenter.getArea());
            employee.setUsername(_mobileTemp);
            employee.setPassword(DigestUtils.md5Hex(password));
            employee.setPoint(setting.getRegisterPoint());
            employee.setAmount(new BigDecimal(0));
            employee.setBalance(new BigDecimal(0));
            employee.setIsEnabled(true);
            employee.setIsLocked(Member.LockType.none);
            employee.setLoginFailureCount(0);
            employee.setLockedDate(null);
            employee.setRegisterIp(request.getRemoteAddr());
            employee.setLoginIp(request.getRemoteAddr());
            employee.setLoginDate(new Date());
            employee.setSafeKey(null);
            employee.setBindEmail(Member.BindStatus.none);
            employee.setBindMobile(Member.BindStatus.none);
            employee.setPaymentPassword(DigestUtils.md5Hex(password));
            employee.setRebateAmount(new BigDecimal(0));
            employee.setProfitAmount(new BigDecimal(0));
            employee.setMemberRank(memberRankService.findDefault());
            employee.setFavoriteProducts(null);
            employee.setFreezeBalance(new BigDecimal(0));
            employee.setPrivilege(0);
            employee.setTotalScore((long) 0);

            employee.setMember(member);
            if (mobile!=null) {
                employee.setMobile(mobile);
            }else{
                employee.setMobile("");
            }
            employee.setEmail("@");
            if (mobile!=null) {
                employee.setMobile(mobile);
            }else{
                employee.setMobile("");
            }
            employee.setBindMobile(BindStatus.binded);

            //手机注册，发送密码
            if (mobile!=null) {
                SmsSend smsSend = new SmsSend();
                smsSend.setMobiles(employee.getUsername());
                smsSend.setContent("注册成功,账号:" + employee.getUsername() + " 默认密码:" + password + "【" + bundle.getString("signature") + "】");
                smsSend.setType(SmsSend.Type.captcha);
                smsSendService.smsSend(smsSend);
            }
        } else {
            if (mobile!=null){
                employee = memberService.findByUsername(mobile);
                if (employee==null) {
                	employee = memberService.findByBindTel(mobile);
                }
            }else if (usernumber!=null){
                employee = memberService.findByUsername(usernumber);
            }
            if (employee.getTenant() != null && employee.getTenant().getStatus().equals(Tenant.Status.success)) {
                return DataBlock.error("此会员正在开店中");
            }
            List<Filter> filters = new ArrayList<Filter>();
            filters.add(new Filter("tenant", Filter.Operator.eq, tenant));
            filters.add(new Filter("member", Filter.Operator.eq, employee));
            Pageable pageable = new Pageable();
            pageable.setFilters(filters);
            Page<Employee> page = employeeService.findPage(pageable);
            if (page.getContent().size() > 0) {
                return DataBlock.error("已经是你的员工了");
            }
        }
        employee.setTenant(tenant);
        if (username != null) employee.setName(username);
        if (address != null) employee.setAddress(address);
        //if (gender != null && "male".equals(gender))
            employee.setGender(gender);
       // else employee.setGender(Member.Gender.female);
        memberService.save(employee);


        Employee emp = new Employee();
        emp.setDeliveryCenter(tenant.getDefaultDeliveryCenter());
        emp.setMember(employee);
        emp.setTenant(tenant);

//        Pageable pageable = new Pageable();
//        List<Filter> filters = new ArrayList<Filter>();
//        filters.add(new Filter("tenant", Filter.Operator.eq, tenant));
//        pageable.setFilters(filters);
//        Page<Role> roleList=roleService.findPage(Role.RoleType.guide,pageable);
//        if (roleList.getContent().size()<0){
//            filters.clear();
//            filters.add(new Filter("roleType", Filter.Operator.eq, 1));
//            pageable.setFilters(filters);
//            roleList=roleService.findPage(Role.RoleType.guide,pageable);
//        }
//        if(roleList.getContent().size()>0)
//        {
//            emp.setRole(","+roleList.getContent().get(0).getId());
//        }else{
//            return DataBlock.error("导购权限未设置,请先设置角色权限");
//        }

        emp.setQuertity(0);

        if (roles != null) {
            String role = "";
            for (Long rl : roles) {
                role = role.concat(Employee.RoleSplit.concat(rl.toString()));
            }
            emp.setRole(role);
        }
        employeeService.save(emp);

        if (member.getTenant() != null) {
            activityDetailService.addPoint(null, member.getTenant(), activityRulesService.find(7L));
            activityDetailService.addPoint(null, member.getTenant(), activityRulesService.find(8L));
        }

        return DataBlock.success("success", "注册成功");
    }

    /**
     * 编辑员工页
     *
     * @return
     */
    @RequestMapping(value = "/employee/edit", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        Member member = memberService.getCurrent();
        Employee employee = employeeService.find(id);
        List<DeliveryCenter> deliveryCenterList = deliveryCenterService.findMyAll(member);
        model.addAttribute("deliveryCenterList", deliveryCenterList);
        model.addAttribute("employee", employee);
        //角色
        Tenant tenant=member.getTenant();
        Pageable pageable=new Pageable();
        List<Filter> filters = new ArrayList<>();
        if (tenant==null){
            filters.add(new Filter("isSystem", Filter.Operator.eq, true));
        }else{
            filters.add(new Filter("tenant", Filter.Operator.eq, tenant));
        }
        pageable.setFilters(filters);
        Page<Role> page = roleService.findPage(Role.RoleType.helper, pageable);
        model.addAttribute("roles", page.getContent());

        model.addAttribute("tags", tagService.findList(Tag.Type.guide));
        model.addAttribute("types", TenantRules.Type.values());
        return "helper/member/tenant/employee/edit";
    }

    /**
     * 获取验证码
     */
    @RequestMapping(value = "/employee/send_mobile", method = RequestMethod.POST)
    @ResponseBody
    public Message getCheckCode(String username, HttpServletRequest request) {
        if (StringUtils.isEmpty(username)) {
            return Message.error("用户名不能空");
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
        safeKey.setExpire(setting.getSafeKeyExpiryTime() != 0 ? DateUtils.addMinutes(new Date(), setting.getSafeKeyExpiryTime()) : null);
        session.setAttribute(REGISTER_SECURITYCODE_SESSION, safeKey);
        session.setAttribute(REGISTER_CONTENT_SESSION, username);

        SmsSend smsSend = new SmsSend();
        smsSend.setMobiles(username);
        smsSend.setContent("验证码 :" + securityCode + ",为了您的账户安全请不要转发他人.【" + bundle.getString("signature") + "】");
        smsSend.setType(net.wit.entity.SmsSend.Type.captcha);
        smsSendService.smsSend(smsSend);
        return Message.success("消息发送成功");
    }

    /**
     * 注册提交
     */
    //手机号注册了直接添加（除了自己不能添加自己），没注册直接帮他注册
    @RequestMapping(value = "/employee/save", method = RequestMethod.POST)
    public
    @ResponseBody
    Message submit(String mobile, String securityCode, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        Member agent = memberService.getCurrent();//获取当前会员
        if (agent == null) {
            return Message.error("您的会话已经失效，请重新登录");
        }
        Tenant tenant = agent.getTenant();//获取当前会员d
        if (tenant == null) {
            return Message.error("您的店铺没有通过认证，不能添加员工");
        }
        if (tenant.getStatus() != Tenant.Status.success) {
            return Message.error("您的店铺没有通过认证，不能添加员工");
        }

//		HttpSession session = request.getSession();
        SafeKey safeKey = (SafeKey) session.getAttribute(REGISTER_SECURITYCODE_SESSION);
        session.removeAttribute(REGISTER_SECURITYCODE_SESSION);
        session.removeAttribute(REGISTER_CONTENT_SESSION);
        if (safeKey == null) {
            return Message.error("请获取验证码");
        }
        if (safeKey.hasExpired()) {
            return Message.error("验证码过期");
        }
        if (!safeKey.getValue().equals(securityCode)) {
            return Message.error("验证码不正确");
        }

        Member member = memberService.findByUsername(mobile);
        if (member==null) {
        	member = memberService.findByBindTel(mobile);
        }
        
        if (member == null) {
            //帮用户设置默认密码
            String password = mobile.substring(mobile.length() - 6, mobile.length());
            //这个member是推荐人
            if (!memberService.usernameExists(mobile) && !memberService.mobileExists(mobile)) {
                Setting setting = SettingUtils.get();
                ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
                if (!setting.getIsRegisterEnabled()) {
                    return Message.error("不能注册");
                }
                member = EntitySupport.createInitMember();
                //获取推荐人的地区，设置为被推荐人 的地区
                Area areaId = agent.getArea();
                member.setArea(areaId);

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
                member.setMember(agent);
                member.setMobile(mobile);
                member.setEmail("@");
                member.setMobile(mobile);
                member.setBindMobile(BindStatus.binded);
                memberService.save(member);

                SmsSend smsSend = new SmsSend();
                smsSend.setMobiles(member.getUsername());
                smsSend.setContent("注册成功,账号:" + member.getUsername() + " 默认密码:" + password + "【" + bundle.getString("signature") + "】");
                smsSend.setType(net.wit.entity.SmsSend.Type.captcha);
                smsSendService.smsSend(smsSend);
            }
        }
        if (member.equals(agent)) {
            return Message.error("不能自已为员工");
        }

        Tenant tenant2 = member.getTenant();
        if (tenant2 == null || tenant2 != null && tenant2.getStatus() != Tenant.Status.success) {
            member.setTenant(tenant);
            memberService.save(member);

            DeliveryCenter deliveryCenter = deliveryCenterService.findDefault(tenant);
            if (deliveryCenter == null) {
                deliveryCenter = new DeliveryCenter();
            }
            Employee employee = new Employee();
            employee.setMember(member);
            employee.setDeliveryCenter(deliveryCenter);
            employee.setTenant(deliveryCenter.getTenant());
            employee.setRole(",guide");
            employee.setQuertity(0);
            employeeService.save(employee);
            return Message.success("添加员工成功");
        }
        if (tenant2 != null && tenant2.getStatus() == Tenant.Status.success) {
            return Message.error("当前账号已有店铺，不能添加成您的员工");
        }
        return Message.error("添加员失败");
    }


    @RequestMapping(value = "/isOwner", method = RequestMethod.POST)
    @ResponseBody
    public Message isOwner(String username) {
        Member member = memberService.findByUsername(username);
        if(member==null){
            return Message.warn("当前用户不存在");
        }

        boolean isOwner = tenantService.isOwner(member);

        if (isOwner) {
            return Message.error("已经是【"+member.getTenant().getName()+"】店主");
        }
        return Message.success("success");
    }
}


