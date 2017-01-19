package net.wit.controller.assistant.member;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.*;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.DateUtil;
import net.wit.util.SettingUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller("assistantMemberMyController")
@RequestMapping("/assistant/member/my")
/**
 * 我的
 */
public class MyController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;
    @Resource(name = "orderServiceImpl")
    private OrderService orderService;
    @Resource(name ="couponCodeServiceImpl")
    private CouponCodeService couponCodeService;
    @Resource(name="rebateServiceImpl")
    private RebateService rebateService;
    @Resource(name="taskServiceImpl")
    private TaskService taskService;
    @Resource(name = "tradeServiceImpl")
    private TradeService tradeService;
    @Resource(name = "orderItemServiceImpl")
    private OrderItemService orderItemService;
    @Resource(name = "extendCatalogServiceImpl")
    private ExtendCatalogService extendCatalogService;
    @Resource(name = "productServiceImpl")
    private ProductService productService;
    @Resource(name = "unionTenantServiceImpl")
    private  UnionTenantService unionTenantService;
    @Resource(name ="tenantServiceImpl")
    private  TenantService tenantService;


    /**
     *   我的列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list() {
        Member member = memberService.getCurrent();
        if (member==null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Map<String,Object> data = new HashMap<String,Object>();
        DecimalFormat bdf = new DecimalFormat("#0.00");
        //销售金额
        data.put("salesTotal", taskService.sum("doSale",member,member.getTenant(),null));
        //分享商品
        data.put("shareTotal",taskService.sum("doShare",member,member.getTenant(),null));
        data.put("shareAccountTotal",bdf.format(rebateService.sumBrokerage(member,Rebate.Type.sale,Rebate.OrderType.order)));
        //推广会员
        data.put("inviteTotal",taskService.sum("doInvite",member,member.getTenant(),null));
        data.put("inviteAccountTotal",bdf.format(rebateService.sumBrokerage(member,Rebate.Type.extension,Rebate.OrderType.order)));
        //推广红包
        data.put("couponTotal",taskService.sum("doCoupon",member,member.getTenant(),null));
        data.put("couponAccountTotal",bdf.format(rebateService.sumBrokerage(member,Rebate.Type.extension,Rebate.OrderType.coupon)));

        //当前余额
        data.put("balance",member.getBalance());
        SingelMemberModel memberModel = new SingelMemberModel();
        memberModel.copyFrom(member);
        data.put("member",memberModel);

        return DataBlock.success(data, "执行成功");
    }
    /**
     *  推广的会员列表
     */
  @RequestMapping(value = "/invite", method = RequestMethod.GET)
  @ResponseBody
  public DataBlock invite( Pageable pageable) {
      Member member = memberService.getCurrent();
      if (member == null) {
          return DataBlock.error(DataBlock.SESSION_INVAILD);
      }
      Page<Member> page = memberService.findPage(member,pageable);

      return DataBlock.success(InviteListModel.bindData(page.getContent()),page, "执行成功");
  }
    /**
     *  销售金额列表
     */

    @RequestMapping(value = "/salesAmount", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock salesAmount(Pageable pageable) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
       Page<Map<String, Object>> page= extendCatalogService.findExtendCatalog(member, pageable);

        return DataBlock.success(page.getContent(),page ,"执行成功");
    }
    /**
     *  分享的商品
     */
    @RequestMapping(value = "/shareProduct", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock shareProduct(long tenantId,Pageable pageable) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = tenantService.find(tenantId);
        if (tenant == null) {
            return DataBlock.error("店铺ID不存在");
        }
        Setting setting = SettingUtils.get();
        BigDecimal guidePercent  = setting.getGuidePercent();
        Page<Map<String,Object>> page= productService.findMySharePage(member,tenant,null, null,  null,  pageable);
        if(page.getContent().size()>0){
           for(Map<String,Object> map:page.getContent()){
               DecimalFormat bdf = new DecimalFormat("#0.00");
             BigDecimal bigDecimal =   new BigDecimal(String.valueOf(map.get("rate"))).multiply(guidePercent).multiply  (new BigDecimal(100));
               map.put("rate",bdf.format(bigDecimal));
           }
        }
        return DataBlock.success(page.getContent(),page, "执行成功");
    }

    /**
     * 取消推荐
     */
    @RequestMapping(value = "/cancelAttention",method = RequestMethod.POST)
    @ResponseBody
    public DataBlock cancelAttention(Long puductId,long tenantId) {
        Member member = memberService.getCurrent();
        if (member==null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = tenantService.find(tenantId);
        if (tenant == null) {
            return DataBlock.error("店铺ID不存在");
        }
        Product product = productService.find(puductId);
        if (product==null){
            return DataBlock.error("商品id不存在");
        }
        try {
            ExtendCatalog extendCatalog = extendCatalogService.findExtendCatalog(member,tenant,product);
            extendCatalog.setType(ExtendCatalog.Type.notRecommended);
            extendCatalogService.update(extendCatalog);
        } catch (Exception e) {
            e.printStackTrace();
            return DataBlock.error("取消失败");
        }
        return DataBlock.success("success", "取消成功");
    }

    /**
     * 添加推荐
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public DataBlock add(Long puductId,long tenantId) {
        Member member = memberService.getCurrent();
        if (member==null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = tenantService.find(tenantId);
        if (tenant == null) {
            return DataBlock.error("店铺ID不存在");
        }
        Product product = productService.find(puductId);
        if (product==null){
            return DataBlock.error("商品id不存在");
        }
        try {
            ExtendCatalog exCatalog = extendCatalogService.findExtendCatalog(member,tenant,product);
            if(exCatalog ==null){
                ExtendCatalog extendCatalog =new ExtendCatalog();
                extendCatalog.setProduct(product);
                extendCatalog.setTenant(tenant);
                extendCatalog.setMember(member);
                extendCatalog.setAmount(new BigDecimal(0));
                extendCatalog.setTimes(0l);
                extendCatalog.setVolume(0l);
                extendCatalog.setSalsePrice(new BigDecimal(0));
                extendCatalog.setType(ExtendCatalog.Type.recommended);
                extendCatalog.setCreateDate(new Date());
                extendCatalog.setModifyDate(new Date());
                extendCatalogService.save(extendCatalog);
            }else{
                if(exCatalog.getType()== ExtendCatalog.Type.recommended){
                    return DataBlock.error("该商品已经关注");
                }else{
                    exCatalog.setTimes(exCatalog.getTimes()+1);
                    exCatalog.setType(ExtendCatalog.Type.recommended);
                    exCatalog.setModifyDate(new Date());
                    extendCatalogService.update(exCatalog);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            return DataBlock.error("添加失败");
        }
        return DataBlock.success("success", "添加成功");
    }

    /**
     * 分享赚钱
     */
    @RequestMapping(value = "/shareMakeMoney",method = RequestMethod.POST)
    @ResponseBody
    public DataBlock shareMakeMoney(Long puductId,long tenantId) {
        Member member = memberService.getCurrent();
        if (member==null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant tenant = tenantService.find(tenantId);
        if (tenant == null) {
            return DataBlock.error("店铺ID不存在");
        }
        Product product = productService.find(puductId);
        if (product==null){
            return DataBlock.error("商品id不存在");
        }
        try {

            ExtendCatalog exCatalog = extendCatalogService.findExtendCatalog(member,tenant,product);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
            Task task = taskService.findByMember(member,Long.parseLong(sdf.format(new Date())));
            if(task!=null){
                task.setDoShare(task.getDoShare()+1);
                taskService.update(task);
            }

            if(exCatalog ==null){
                ExtendCatalog extendCatalog =new ExtendCatalog();
                extendCatalog.setProduct(product);
                extendCatalog.setTenant(tenant);
                extendCatalog.setMember(member);
                extendCatalog.setAmount(new BigDecimal(0));
                extendCatalog.setTimes(1l);
                extendCatalog.setVolume(0l);
                extendCatalog.setCreateDate(new Date());
                extendCatalog.setModifyDate(new Date());
                extendCatalog.setSalsePrice(new BigDecimal(0));
                extendCatalog.setType(ExtendCatalog.Type.notRecommended);
                extendCatalogService.save(extendCatalog);
            }else{
                exCatalog.setTimes(exCatalog.getTimes()+1);
                extendCatalogService.update(exCatalog);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DataBlock.error("分享失败");
        }
        return DataBlock.success("success", "分享成功");
    }

}