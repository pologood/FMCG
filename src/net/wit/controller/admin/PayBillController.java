package net.wit.controller.admin;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.PayBill;
import net.wit.entity.Payment;
import net.wit.entity.Refunds;
import net.wit.service.PayBillService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Controller - 优惠买单
 * 2016/12/30
 * @author ruanx
 * @version 3.0
 */
@Controller("adminPayBillController")
@RequestMapping("/admin/paybill")
public class PayBillController extends BaseController {
    @Resource(name = "payBillServiceImpl")
    private PayBillService payBillService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(String tenantName, String username, String paymentMethod, Date beginDate, Date endDate, Pageable pageable, ModelMap model) {
        if(beginDate!=null&&endDate!=null){
            Long time=endDate.getTime();
            Long end=time+24*60*60*1000-1;
            endDate=new Date(end);
        }
        Page page = payBillService.findPage(tenantName,username,paymentMethod,beginDate,endDate,pageable);
        model.addAttribute("page", page);
        model.addAttribute("beginDate", beginDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("paymentMethod", paymentMethod);
        model.addAttribute("tenantName", tenantName);
        model.addAttribute("username", username);
        return "/admin/paybill/list";
    }

    /**
     *订单导出
     */
    @RequestMapping(value = "/payBill_export", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String , Object >> payBillExport(String tenantName, String username, String paymentMethod, Date beginDate, Date endDate, Pageable pageable) {
        List<Map<String,Object>> maps=new ArrayList<Map<String , Object >>();
        if(beginDate!=null&&endDate!=null){
            Long time=endDate.getTime();
            Long end=time+24*60*60*1000-1;
            endDate=new Date(end);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //最大可导出数
        pageable.setPageSize(1000);
        Page page = payBillService.findPage(tenantName,username,paymentMethod,beginDate,endDate,pageable);
        List<PayBill> payBills=page.getContent();
        if(payBills!=null){
            for(PayBill payBill:payBills){
                Map<String,Object> map=new HashMap<String,Object>();
                map.put("createDate",sdf.format(payBill.getCreateDate()));
                map.put("paymentDate",payBill.getPayment()==null?"-":(payBill.getPayment().getPaymentDate()==null?"-":sdf.format(payBill.getPayment().getPaymentDate())));
                map.put("order",payBill.getPayment()==null?"-":(payBill.getPayment().getOrder()==null?"-":payBill.getPayment().getOrder().getId()));
                map.put("sn",payBill.getSn());
                map.put("amount",payBill.getAmount());
                map.put("username",payBill.getMember().getUsername());
                map.put("tenantName",payBill.getTenant().getName());
                map.put("paymentMethod",payBill.getPayment()==null?"-":payBill.getPayment().getPaymentMethod());
                if(payBill.getStatus()== PayBill.Status.none){
                    map.put("status", "等待支付");
                }else if(payBill.getStatus()== PayBill.Status.success){
                    map.put("status", "支付成功");
                }else if(payBill.getStatus()== PayBill.Status.failure){
                    map.put("status", "支付失败");
                }
                if(payBill.getType()== PayBill.Type.coupon){
                    map.put("type", "代金券");
                }else if(payBill.getType()== PayBill.Type.promotion){
                    map.put("type", "活动优惠");
                }else if(payBill.getType()== PayBill.Type.cashier){
                    map.put("type", "收银台");
                }
                if(payBill.getPayment()!=null&&payBill.getPayment().getMethod()== Payment.Method.online){
                    map.put("method", "在线支付");
                }else if(payBill.getPayment()!=null&&payBill.getPayment().getMethod()== Payment.Method.offline){
                    map.put("method", "线下支付");
                }else if(payBill.getPayment()!=null&&payBill.getPayment().getMethod()== Payment.Method.deposit){
                    map.put("method", "账户支付");
                }
                maps.add(map);
            }
        }
        return maps;
    }

    /**
     * 查看
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String view(Long id, ModelMap model) {
        model.addAttribute("paybill", payBillService.find(id));
        return "/admin/paybill/view";
    }
}
