package net.wit.controller.store.member;

import com.google.zxing.WriterException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import net.wit.Filter;
import net.wit.Message;
import net.wit.Page;
import net.wit.entity.UnionTenant;
import net.wit.entity.UnionTenant.Status;
import net.wit.Pageable;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.QRBarCodeUtil;
import net.wit.weixin.main.MenuManager;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Administrator on 2016/7/13.
 */
@Controller("storeMemberUnionController")
@RequestMapping("/store/member/union")
public class UnionController extends net.wit.controller.store.BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "unionServiceImpl")
    private UnionService unionService;

    @Resource(name = "unionTenantServiceImpl")
    private UnionTenantService unionTenantService;

    @Resource(name = "equipmentServiceImpl")
    private EquipmentService equipmentService;

    @Resource(name = "productServiceImpl")
    private ProductService productService;

    @Resource(name = "brandSeriesServiceImpl")
    private BrandSeriesService brandSeriesService;

    @Resource(name = "promotionServiceImpl")
    private PromotionService promotionService;

    @Resource(name = "tagServiceImpl")
    private TagService tagService;

    @Resource(name = "brandServiceImpl")
    private BrandService brandService;

    @Resource(name = "productCategoryServiceImpl")
    private ProductCategoryService productCategoryService;

    @Resource(name = "productCategoryTenantServiceImpl")
    private ProductCategoryTenantService productCategoryTenantService;

    @Resource(name = "extendCatalogServiceImpl")
    private ExtendCatalogService extendCatalogService;

    @Resource(name = "paymentServiceImpl")
    private PaymentService paymentService;


    /**
     * 我的商盟
     */
    @RequestMapping(value = "/my_union", method = RequestMethod.GET)
    public String myUnion(ModelMap model) {
        Member member=memberService.getCurrent();
        if (member==null){
            return "redirect:/store/login.jhtml";
        }
        Tenant tenant=member.getTenant();
        List<UnionTenant> unionTenants=new ArrayList<UnionTenant>();
        if(tenant==null){
            return "redirect:/store/login.jhtml";
        }
        List<Filter> filter = new ArrayList<Filter>();
        filter.add(new Filter("type", Filter.Operator.eq, UnionTenant.Type.tenant));
        filter.add(new Filter("status", Filter.Operator.ne, Status.canceled));
        unionTenants=unionTenantService.findUnionTenant(null,tenant,filter);

        if(unionTenants==null){
            return "redirect:all_union.jhtml";
        }else{
            if(unionTenants.size()<1){
                return "redirect:all_union.jhtml";
            }
        }
        model.addAttribute("unionTenants",unionTenants);
        model.addAttribute("menu","my_union");
        return "/store/member/union/my_union";
    }

    /**
     * 平台所有商盟
     */
    @RequestMapping(value = "/all_union", method = RequestMethod.GET)
    public String unionList(ModelMap model,Pageable pageable) {
        Member member= null;
        Tenant tenant= null;
        Page<Union> page=null;
        List<Map<String, Object>> maps=new ArrayList<Map<String, Object>>();
        try {
            member = memberService.getCurrent();
            tenant = member.getTenant();
            page=unionService.findPage(pageable);
            for(Union union:page.getContent()){
                Map<String, Object> map=new HashMap<String,Object>();
                map.put("name", union.getName());
                map.put("brokerage",union.getBrokerage());
                map.put("price", union.getPrice());
                map.put("tenantNumber", union.getTenantNumber());
                map.put("image", union.getImage());
                map.put("id", union.getId());
                map.put("create_date",union.getCreateDate());
                List<UnionTenant> unionTenantList=new ArrayList<UnionTenant>();
                unionTenantList.addAll(union.getUnionTenants());
                if(unionTenantList.size()>0){
                    if(unionTenantList.get(0).getTenant()==tenant && unionTenantList.get(0).getType()== UnionTenant.Type.tenant){
                        if(unionTenantList.get(0).getStatus()==Status.unconfirmed){
                            map.put("judge_status","unconfirmed");
                        }else if(unionTenantList.get(0).getStatus()==Status.confirmed){
                            map.put("judge_status","confirmed");
                        }else if(unionTenantList.get(0).getStatus()==Status.freezed){
                            map.put("judge_status","freezed");
                        }else if(unionTenantList.get(0).getStatus()==Status.canceled){
                            map.put("judge_status","canceled");
                        }
                    }else{
                        map.put("judge_status","canceled");
                    }
                }else{
                    map.put("judge_status","canceled");
                }
                maps.add(map);
            }

        } catch (Exception e) {
            return "/store/member/union/all_union";
        }
        model.addAttribute("maps",maps);
        model.addAttribute("tenant",tenant);
        model.addAttribute("page",page);
        model.addAttribute("menu","my_union");
        return "/store/member/union/all_union";
    }

    /**
     * 商盟店铺列表
     */
    @RequestMapping(value = "/union_tenant_list", method = RequestMethod.GET)
    public String unionTenantList(Long unionId,String keywords,ModelMap model,Pageable pageable) {
        Member member=memberService.getCurrent();
        if (member==null){
            return "redirect:/store/login.jhtml";
        }
        Union union=unionService.find(unionId);
        if(union==null){
            return "redirect:/all_union";
        }
        List<Filter> filter = new ArrayList<Filter>();
        filter.add(new Filter("type", Filter.Operator.eq, UnionTenant.Type.tenant));
        pageable.setFilters(filter);
        Page<UnionTenant> page= unionTenantService.findPage(union,Status.confirmed,pageable);
        model.addAttribute("member",member);
        model.addAttribute("page",page);
        model.addAttribute("menu","my_union");
        model.addAttribute("keywords",keywords);
        model.addAttribute("unionId",unionId);
        return "/store/member/union/union_tenant_list";
    }

    /**
     * 商盟店铺商品列表
     */
    @RequestMapping(value = "/tenant_product_list", method = RequestMethod.GET)
    public String TenantList(Long tenantId, String keywords, Long tagId,ModelMap model, Pageable pageable) {
        Member member=memberService.getCurrent();
        if (member==null){
            return "redirect:/store/login.jhtml";
        }
        Tenant tenant=tenantService.find(tenantId);
        if(tenant==null){
            return "redirect:/all_union";
        }
        List<Tag> tags = tagService.findList(tagId);
        Page<Map<String,Object>> page= productService.findMySharePage(member,tenant,tags,Product.OrderType.salesDesc, keywords,pageable);
        model.addAttribute("member",member);
        model.addAttribute("page",page);
        model.addAttribute("menu","all_union");
        model.addAttribute("keywords",keywords);
        model.addAttribute("tenantId",tenantId);
        return "/store/member/union/tenant_product_list";
    }

    /**
     * 点击申请按钮时，调用该方法
     */
    @RequestMapping(value = "/create_unionTenant", method = RequestMethod.POST)
    @ResponseBody
    public Message createUnionTenant(Long unionId) {
        Member member=memberService.getCurrent();
        if (member==null){
            return Message.error("会员不存在");
        }
        Tenant tenant=member.getTenant();
        if (tenant==null){
            return Message.error("店铺不存在");
        }
        if(member!=tenant.getMember()){
            return Message.error("您不是店主，没有权限操作。");
        }
        Union union=unionService.find(unionId);
        if(union==null){
            return Message.error("系统内部找不到该商盟");
        }
        List<UnionTenant> unionTenants=new ArrayList<UnionTenant>();
        if(tenant.getStatus()==Tenant.Status.success&&union!=null){
            List<Filter> filter = new ArrayList<Filter>();
            filter.add(new Filter("type", Filter.Operator.eq, UnionTenant.Type.tenant));
            unionTenants=unionTenantService.findUnionTenant(union,tenant,filter);
        }
        if(unionTenants.size()>0){//已经加入过，改变状态
            unionTenants.get(0).setStatus(UnionTenant.Status.unconfirmed);
            unionTenantService.update(unionTenants.get(0));
            return Message.success(unionTenants.get(0).getId().toString());
        }

        UnionTenant unionTenant= null;
        try {
            unionTenant = new UnionTenant();
            unionTenant.setUnion(union);
            unionTenant.setTenant(tenant);
            unionTenant.setEquipment(null);
            unionTenant.setPrice(union.getPrice());
            unionTenant.setStatus(UnionTenant.Status.unconfirmed);
            unionTenant.setType(UnionTenant.Type.tenant);
            unionTenantService.save(unionTenant);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("申请失败");
        }
        return Message.success(unionTenant.getId().toString());
    }

    /**
     * 支付完成后调用该方法
     */
    @RequestMapping(value = "/update_unionTenant", method = RequestMethod.POST)
    @ResponseBody
    public Message updateUnionTenant(Long unionTenantId,String sn) {
        Member member=memberService.getCurrent();
        if (member==null){
            return Message.error("会员不存在");
        }
        UnionTenant unionTenant=unionTenantService.find(unionTenantId);
        if (unionTenant==null){
            return Message.error("申请记录不存在");
        }
        Union union=unionTenant.getUnion();
        if (union==null){
            return Message.error("找不到该商盟");
        }
        Tenant tenant=unionTenant.getTenant();
        if (tenant==null){
            return Message.error("店铺不存在");
        }
        Payment payment=paymentService.findBySn(sn);
        if(payment==null){
            return Message.error("支付存在异常");
        }

        Calendar curr = Calendar.getInstance();
        curr.add(Calendar.YEAR, 1);
        Date date=curr.getTime();
        unionTenant.setExpire(date);
        unionTenant.setStatus(UnionTenant.Status.confirmed);
        unionTenant.setPayment(payment);

        union.setTenantNumber(union.getTenantNumber()+1);

        if(tenant.getAgency().compareTo(union.getBrokerage())<0){
            tenant.setAgency(union.getBrokerage());//店铺自己的佣金
        }
        if(tenant.getIsUnion()==false){
            tenant.setIsUnion(true);
        }

        payment.setUnionTenant(unionTenant);
        paymentService.update(payment);
        tenantService.update(tenant);
        unionService.update(union);
        unionTenantService.update(unionTenant);
        return Message.success("加入成功");
    }

    /**
     * 退出商盟
     */
    @RequestMapping(value = "/return_union", method = RequestMethod.POST)
    @ResponseBody
    public Message returnUnion(Long unionId) {
        Member member=memberService.getCurrent();
        if (member==null){
            return Message.error("会员不存在");
        }
        Tenant tenant=member.getTenant();
        if (tenant==null){
            return Message.error("店铺不存在");
        }
        if(member!=tenant.getMember()){
            return Message.error("您不是店主，没有权限操作。");
        }
        Union union=unionService.find(unionId);
        if(union==null){
            return Message.error("系统内部找不到该商盟");
        }
        List<UnionTenant> unionTenants=new ArrayList<UnionTenant>();
        if(tenant.getStatus()==Tenant.Status.success&&union!=null){
            List<Filter> filter = new ArrayList<Filter>();
            filter.add(new Filter("type", Filter.Operator.eq, UnionTenant.Type.tenant));
            unionTenants=unionTenantService.findUnionTenant(union,tenant,filter);
        }
        if(unionTenants.size()>0){
            if(union.getTenantNumber()>0){
                union.setTenantNumber(union.getTenantNumber()-1);
            }
            unionTenants.get(0).setStatus(UnionTenant.Status.canceled);
            unionTenantService.update(unionTenants.get(0));
            unionService.update(union);
        }else{
            return Message.warn("退出失败。");
        }

        return Message.success("退出成功，您可以选择新的联盟加入。");
    }

    /**
     * 查看推广信息
     */
    @RequestMapping(value = "/look_extend", method = RequestMethod.POST)
    @ResponseBody
    public Map<String ,Object> lookExtend(Long tenantId) {
        Map<String,Object> map=new HashMap<String,Object>();
        String type="true";
        String content="success";
        Member member=memberService.getCurrent();
        if (member==null){
            type="false";
            content="当前会员不存在";
        }
        Tenant myTenant=member.getTenant();//我的店铺
        if(myTenant==null){
            type="false";
            content="店铺不存在";
        }
        Tenant tenant=tenantService.find(tenantId);//他的店铺
        if(tenant==null){
            type="false";
            content="找不到该店铺";
        }
        List<Filter> filters=new ArrayList<Filter>();
        filters.add(new Filter("member", Filter.Operator.eq,member));
        filters.add(new Filter("tenant", Filter.Operator.eq,tenant));
        List<ExtendCatalog> extendCatalogs=extendCatalogService.findList(null,filters,null);
        for(ExtendCatalog extendCatalog:extendCatalogs){
            if(extendCatalog==null){
                map.put("i_to_he_valume","0");
                map.put("i_to_he_amount","0");
                map.put("i_to_he_total","0");
            }else{
                map.put("i_to_he_valume",extendCatalog.getVolume());
                map.put("i_to_he_amount",extendCatalog.getAmount());
                map.put("i_to_he_total",extendCatalog.getSalsePrice());
            }
        }


        Set<Member> members=tenant.getMembers();
        Long heVlume=0l;
        BigDecimal heAmount=BigDecimal.ZERO;
        BigDecimal hePrice=BigDecimal.ZERO;
        List<Filter> filter=new ArrayList<Filter>();
        filter.add(new Filter("tenant", Filter.Operator.eq,myTenant));
        if(members.size()>0){
            for(Member meb:members){
                filter.add(new Filter("member", Filter.Operator.eq,meb));
                List<ExtendCatalog> extendCatalogs2=extendCatalogService.findList(null,filter,null);
                for(ExtendCatalog extendCatalog:extendCatalogs2){
                    heAmount=heAmount.add(extendCatalog.getAmount());
                    heVlume=heVlume+extendCatalog.getVolume();
                    hePrice=hePrice.add(extendCatalog.getSalsePrice());
                }
            }
        }
        map.put("he_to_i_valume",heVlume);
        map.put("he_to_i_amount",heAmount);
        map.put("he_to_i_total",hePrice);
        map.put("type",type);
        map.put("content",content);
        return map;
    }

    /**
     * 我收藏的商品
     */
    @RequestMapping(value = "/my_product", method = RequestMethod.GET)
    public String myProdct(ModelMap model,Pageable pageable) {
        Member member=memberService.getCurrent();
        if (member==null){
            return "redirect:/store/login.jhtml";
        }
        List<Filter> filter = new ArrayList<Filter>();
        filter.add(new Filter("member", Filter.Operator.eq, member));
        filter.add(new Filter("type", Filter.Operator.eq, ExtendCatalog.Type.recommended));
        pageable.setFilters(filter);
        Page<ExtendCatalog> page=extendCatalogService.findPage(pageable);
        model.addAttribute("page",page);
        model.addAttribute("menu","my_extend");
        return "/store/member/union/my_product";
    }

    /**
     * 收藏店铺商品到我的推广中
     */
    @RequestMapping(value = "/save_product", method = RequestMethod.POST)
    @ResponseBody
    public Message saveProduct(Long productId) {
        Member member=memberService.getCurrent();
        if (member==null){
            return Message.error("会员不存在");
        }
        Product product=productService.find(productId);
        if(product==null){
            return Message.error("系统内部找不到该商品");
        }
        Tenant tenant=product.getTenant();
        if (tenant==null){
            return Message.error("店铺不存在");
        }
        try {
            ExtendCatalog extendCatalog = extendCatalogService.findExtendCatalog(member,tenant,product);
            if(extendCatalog!=null){
                extendCatalog.setType(ExtendCatalog.Type.recommended);
                extendCatalogService.update(extendCatalog);
            }else{
                extendCatalog =new ExtendCatalog();
                extendCatalog.setProduct(product);
                extendCatalog.setTenant(tenant);
                extendCatalog.setMember(member);
                extendCatalog.setAmount(new BigDecimal(0));
                extendCatalog.setTimes(0l);
                extendCatalog.setVolume(0l);
                extendCatalog.setSalsePrice(new BigDecimal(0));
                extendCatalog.setType(ExtendCatalog.Type.recommended);
                extendCatalogService.save(extendCatalog);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("收藏失败");
        }
        return Message.success("收藏成功。");
    }

    /**
     * 取消收藏店铺商品到我的推广中
     */
    @RequestMapping(value = "/cancel_product", method = RequestMethod.POST)
    @ResponseBody
    public Message cancelledProduct(Long productId) {
        Member member=memberService.getCurrent();
        if (member==null){
            return Message.error("会员不存在");
        }

        Product product=productService.find(productId);
        if(product==null){
            return Message.error("系统内部找不到该商品");
        }
        Tenant tenant=product.getTenant();
        if (tenant==null){
            return Message.error("店铺不存在");
        }
        try {
            ExtendCatalog extendCatalog = extendCatalogService.findExtendCatalog(member,tenant,product);
            extendCatalog.setType(ExtendCatalog.Type.notRecommended);
            extendCatalogService.update(extendCatalog);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("取消失败");
        }
        return Message.success("取消成功");

    }

    /**
     * 分享推广商品
     */
    @RequestMapping(value = "/share_product", method = RequestMethod.POST)
    @ResponseBody
    public Message shareProduct(Long productId) {
        Member member=memberService.getCurrent();
        if (member==null){
            return Message.error("会员不存在");
        }
        Product product=productService.find(productId);
        if(product==null){
            return Message.error("系统内部找不到该商品");
        }
        Tenant tenant=product.getTenant();
        if (tenant==null){
            return Message.error("店铺不存在");
        }
        ExtendCatalog extendCatalog=extendCatalogService.findExtendCatalog(member,tenant,product);
        try {
            if(extendCatalog==null){
                extendCatalog=new ExtendCatalog();
                extendCatalog.setProduct(product);
                extendCatalog.setTenant(tenant);
                extendCatalog.setMember(member);
                extendCatalog.setAmount(new BigDecimal(0));
                extendCatalog.setTimes(1l);
                extendCatalog.setVolume(0l);
                extendCatalog.setSalsePrice(new BigDecimal(0));
                extendCatalog.setType(ExtendCatalog.Type.notRecommended);
                extendCatalogService.save(extendCatalog);
            }else{
                extendCatalog.setTimes(extendCatalog.getTimes()+1l);
                extendCatalogService.update(extendCatalog);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("分享失败");
        }
        return Message.success("分享成功。");
    }

    /**
     * 生成分享推广二维码
     */
    @RequestMapping(value = "/make_qrcode", method = RequestMethod.GET)
    public void qrcodeEmployee(String productId, HttpServletResponse response) {
        Member member = memberService.getCurrent();
        try {
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            // 第三方用户唯一凭证
            String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/product/display/"+productId+".jhtml?extension=" + (member != null ? member.getUsername() : ""));
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
     * 投放我的
     */
    @RequestMapping(value = "/my_device", method = RequestMethod.GET)
    public String myDevice( Pageable pageable, ModelMap model) {
        Member member=memberService.getCurrent();
        if (member==null){
            return "redirect:/store/login.jhtml";
        }
        Tenant tenant=member.getTenant();
        if (tenant==null){
            return "redirect:/store/login.jhtml";
        }

        Equipment equipment = equipmentService.findEquipment(tenant,null);
        List<Filter> filter = new ArrayList<Filter>();
        filter.add(new Filter("type", Filter.Operator.eq, UnionTenant.Type.device));
        filter.add(new Filter("equipment", Filter.Operator.eq, equipment));
        pageable.setFilters(filter);
        Page<UnionTenant> page=unionTenantService.findPage(pageable);

        model.addAttribute("page",page);
        model.addAttribute("status","confirmed");
        model.addAttribute("menu","my_device");
        return "/store/member/union/my_device";
    }

    /**
     * 我投放的
     * @return
     */
    @RequestMapping(value ="invest_device",method =RequestMethod.GET)
    public String investDevice(Pageable pageable,ModelMap model){
        Member member = memberService.getCurrent();
        if(member==null){
            return "redirect:/store/login.jhtml";
        }
        Tenant tenant= member.getTenant();
        if(tenant ==null){
            return "redirect:/store/login.jhtml";
        }

        List<Filter> filter = new ArrayList<Filter>();
        filter.add(new Filter("type", Filter.Operator.eq, UnionTenant.Type.device));
        filter.add(new Filter("tenant", Filter.Operator.eq, tenant));
        pageable.setFilters(filter);
        Page<UnionTenant> page=unionTenantService.findPage(pageable);

        model.addAttribute("statuses", UnionTenant.Status.values());
        model.addAttribute("status","in_device");
        model.addAttribute("menu","my_device");
        model.addAttribute("page",page);
        return "/store/member/union/invest_device";
    }

    /**
     * 投放我的申请
     * @return
     */
    @RequestMapping(value = "my_device_apply",method = RequestMethod.GET)
    public String myDeviceApply(Pageable pageable,String status,ModelMap model){
        Member member=memberService.getCurrent();
        if (member==null){
            return "redirect:/store/login.jhtml";
        }
        Tenant tenant=member.getTenant();
        if (tenant==null){
            return "redirect:/store/login.jhtml";
        }

        Status status1=null;
        if(status==null||status=="unconfirmed"){
            status1=Status.unconfirmed;
        }else if(status=="confirmed"){
            status1=Status.confirmed;
        }else if(status=="freezed"){
            status1=Status.freezed;
        }else if(status=="canceled"){
            status1=Status.canceled;
        }
        Equipment equipment = equipmentService.findEquipment(tenant,null);
        List<Filter> filter = new ArrayList<Filter>();
        filter.add(new Filter("type", Filter.Operator.eq, UnionTenant.Type.device));
        filter.add(new Filter("equipment", Filter.Operator.eq, equipment));
        pageable.setFilters(filter);
        Page<UnionTenant> page=unionTenantService.findPage(null,status1,pageable);
        model.addAttribute("page",page);
        model.addAttribute("status", status);
        model.addAttribute("statuses", UnionTenant.Status.values());
        model.addAttribute("menu","my_device");
        return "/store/member/union/my_device_apply";
    }

    /**
     * 我投放的申请
     * @param pageable
     * @param model
     * @return
     */
    @RequestMapping(value = "invest_device_apply",method = RequestMethod.GET)
    public String investDeviceApply(Pageable pageable,String status,ModelMap model){
        Member member = memberService.getCurrent();
        if(member==null){
            return "redirect:/store/login.jhtml";
        }
        Tenant tenant= member.getTenant();
        if(tenant ==null){
            return "redirect:/store/login.jhtml";
        }
        Status status1=null;
        if(status==null||status=="unconfirmed"){
            status1=Status.unconfirmed;
        }else if(status=="confirmed"){
            status1=Status.confirmed;
        }else if(status=="freezed"){
            status1=Status.freezed;
        }else if(status=="canceled"){
            status1=Status.canceled;
        }

        List<Filter> filter = new ArrayList<Filter>();
        filter.add(new Filter("type", Filter.Operator.eq, UnionTenant.Type.device));
        filter.add(new Filter("tenant", Filter.Operator.eq, tenant));
        pageable.setFilters(filter);
        Page<UnionTenant> page=unionTenantService.findPage(null,status1,pageable);
        model.addAttribute("statuses", UnionTenant.Status.values());
        model.addAttribute("page",page);
        model.addAttribute("menu","my_device");
        model.addAttribute("status",status);
        return "/store/member/union/invest_device_apply";
    }

    /**
     * 申请投放
     */
    @RequestMapping(value = "/applyIn", method = RequestMethod.GET)
    public String applyIn( Pageable pageable, ModelMap model) {
        Member member=memberService.getCurrent();
        if (member==null){
            return "redirect:/store/login.jhtml";
        }
        Tenant tenant=member.getTenant();
        if (tenant==null){
            return "redirect:/store/login.jhtml";
        }
        Page<Union> page=unionService.findPage(pageable);
        model.addAttribute("member",member);
        model.addAttribute("page",page);
        model.addAttribute("menu","my_device");
        return "/store/member/union/apply_in";
    }

    /**
     * 购物屏店铺列表
     */
    @RequestMapping(value = "/equipment_tenant_list", method = RequestMethod.GET)
    public String equipmentTenantList(Long unionId,String keywords,ModelMap model,Pageable pageable) {
        Member member=memberService.getCurrent();
        if (member==null){
            return "redirect:/store/login.jhtml";
        }
        Union union=unionService.find(unionId);
        if(union==null){
            return "redirect:/my_device";
        }
        Tenant tenant=member.getTenant();
        if (tenant==null){
            return "redirect:/store/login.jhtml";
        }
        List<Filter> filter = new ArrayList<Filter>();
        filter.add(new Filter("type", Filter.Operator.eq, UnionTenant.Type.device));
        List<UnionTenant> unionTenants=unionTenantService.findUnionTenant(union,tenant,filter);
        Page<Equipment> page=equipmentService.findPage(unionId,keywords,null,Equipment.Status.enabled,pageable);
        model.addAttribute("union",union);
        model.addAttribute("page",page);
        model.addAttribute("menu","my_device");
        model.addAttribute("keywords",keywords);
        model.addAttribute("unionId",unionId);
        if(unionTenants.size()>0){
            model.addAttribute("unionTenant",unionTenants.get(0));
        }else{
            model.addAttribute("unionTenant",null);
        }
        return "/store/member/union/equipment_tenant_list";
    }

    /**
     * 检查是否加入该购物屏

     * @return
     */
    @RequestMapping(value="check_apply",method = RequestMethod.POST)
    @ResponseBody
    public Message checkApply(Long equipmentId,Long unionId){
        Member member=memberService.getCurrent();
        if (member==null){
            return Message.error("会员不存在");
        }
        Tenant tenant=member.getTenant();
        if (tenant==null){
            return Message.error("店铺不存在");
        }
        Union union = unionService.find(unionId);
        if (union==null){
            return Message.error("联盟不存在");
        }
        Equipment  equipment = equipmentService.find(equipmentId);
        if (equipmentId==null){
            return Message.error("购物屏不存在");
        }
        if(equipment.getTenant()==tenant){
            return Message.error("不能添加自己的屏");
        }

        List<Filter> filter = new ArrayList<Filter>();
        filter.add(new Filter("equipment", Filter.Operator.eq, equipment));
        filter.add(new Filter("type", Filter.Operator.eq, UnionTenant.Type.device));
        List<UnionTenant> unionTenants=unionTenantService.findUnionTenant(union,tenant,filter);
        if(unionTenants.size()>0){
            unionTenants.get(0).setStatus(UnionTenant.Status.unconfirmed);
            unionTenants.get(0).setType(UnionTenant.Type.device);
            unionTenantService.update(unionTenants.get(0));
            return Message.success(unionTenants.get(0).getId().toString());
        }

        UnionTenant unionTenant= null;
        try {
            unionTenant = new UnionTenant();
            unionTenant.setUnion(union);
            unionTenant.setTenant(tenant);
            unionTenant.setEquipment(equipment);
            unionTenant.setPrice(union.getPrice());
            unionTenant.setStatus(UnionTenant.Status.unconfirmed);
            unionTenant.setType(UnionTenant.Type.device);
            unionTenantService.save(unionTenant);
            return Message.success("申请成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("申请失败");
        }
    }

    /**
     * 加入购物屏
     * @param unionId
     * @return
     */
    @RequestMapping(value = "/apply_equipment",method = RequestMethod.POST)
    @ResponseBody
    public Message applyEquipment(Long equipmentId,Long unionId,String sn){
        Member member=memberService.getCurrent();
        if (member==null){
            return Message.error("会员不存在");
        }
        Tenant tenant=member.getTenant();
        if (tenant==null){
            return Message.error("店铺不存在");
        }
        Union union = unionService.find(unionId);
        if (union==null){
            return Message.error("联盟不存在");
        }
        Equipment  equipment = equipmentService.find(equipmentId);
        if (equipmentId==null){
            return Message.error("购物屏不存在");
        }
        if(equipment.getTenant()==tenant){
            return Message.error("不能添加自己的屏");
        }
        Payment payment=paymentService.findBySn(sn);
        List<Filter> filter = new ArrayList<Filter>();
        filter.add(new Filter("equipment", Filter.Operator.eq, equipment));
        filter.add(new Filter("type", Filter.Operator.eq, UnionTenant.Type.device));
        List<UnionTenant> unionTenants=unionTenantService.findUnionTenant(union,tenant,filter);
        if(unionTenants.size()>0){
            unionTenants.get(0).setPayment(payment);
            payment.setUnionTenant(unionTenants.get(0));
            unionTenantService.update(unionTenants.get(0));
            paymentService.update(payment);
            return Message.success(unionTenants.get(0).getId().toString());
        }else{
            return Message.error("加入失败");
        }
    }

    /**
     * 取消申请
     * @param uTenantId
     * @return
     */
    @RequestMapping(value="/cancel_apply",method = RequestMethod.POST)
    @ResponseBody
    public Message cancelApply(Long uTenantId){
        try {
            UnionTenant unionTenant = unionTenantService.find(uTenantId);
            unionTenant.setStatus(Status.canceled);
            unionTenantService.cancel(unionTenant);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("取消失败");
        }
        return Message.success("取消成功");
    }
    /**
     * 确认申请
     * @param uTenantId
     * @return
     */
    @RequestMapping(value="/confirm_apply",method = RequestMethod.POST)
    @ResponseBody
    public Message confirmApply(Long uTenantId){
        try {
            UnionTenant unionTenant = unionTenantService.find(uTenantId);
            Calendar curr = Calendar.getInstance();
            curr.add(Calendar.YEAR, 1);
            Date date=curr.getTime();
            unionTenant.setExpire(date);
            unionTenant.setStatus(Status.confirmed);
            unionTenantService.update(unionTenant);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("确认失败");
        }
        return Message.success("确认成功");
    }
    /**
     * 拒绝申请
     * @param uTenantId
     * @return
     */
    @RequestMapping(value="/refuse_apply",method = RequestMethod.POST)
    @ResponseBody
    public Message refuseApply(Long uTenantId){
        try {
            UnionTenant unionTenant = unionTenantService.find(uTenantId);
            unionTenant.setStatus(Status.canceled);
            unionTenantService.cancel(unionTenant);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("拒绝失败");
        }
        return Message.success("已拒绝");
    }
}
