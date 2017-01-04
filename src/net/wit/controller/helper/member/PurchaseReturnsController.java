package net.wit.controller.helper.member;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.app.model.ProductModel;
import net.wit.controller.helper.model.DataBlock;
import net.wit.entity.*;
import net.wit.entity.PurchaseReturns.Type;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 采购单控制层
 * Created by My-PC on 16/06/03.
 */
@Controller("helperMemberPurchaseReturnsController")
@RequestMapping("/helper/member/purchase/returns")
public class PurchaseReturnsController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "purchaseReturnsServiceImpl")
    private PurchaseReturnsService purchaseReturnsService;

    @Resource(name = "tenantRelationServiceImpl")
    private TenantRelationService tenantRelationService;

    @Resource(name = "productServiceImpl")
    private ProductService productService;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "snServiceImpl")
    private SnService snService;

    @Resource(name = "monthlyServiceImpl")
    private MonthlyService monthlyService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Pageable pageable, ModelMap model, Date beginDate, Date endDate, String keywords) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return "redirect:" + ERROR_VIEW;
        }
        Page<PurchaseReturns> page = purchaseReturnsService.openPage(pageable,member.getTenant(), null, beginDate, endDate, null,null, keywords );
        String isMonthly="false";
        if(monthlyService.isMonthly(member,null)){
            isMonthly="true";
        }
        model.addAttribute("isMonthly",isMonthly);
        model.addAttribute("page", page);
        model.addAttribute("beginDate", beginDate);
        model.addAttribute("endDate", endDate);
//        model.addAttribute("supplierName", supplierName);
//        model.addAttribute("barcode", barcode);
        return "/helper/member/purchase_returns/list";
    }
    
    @RequestMapping(value = "/export_list", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> exportList(Pageable pageable, ModelMap model, Date beginDate, Date endDate, String keywords) {
    	List<Map<String, Object>> maps=new ArrayList<Map<String,Object>>();
        Member member = memberService.getCurrent();
        List<PurchaseReturns> purchaseReturns = purchaseReturnsService.exportOpenPage(member.getTenant(),null, beginDate, endDate,keywords);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(PurchaseReturns purchaseReturn:purchaseReturns){
        	List<Map<String, Object>> maps2=new ArrayList<Map<String,Object>>();
        	Map<String,Object> map=new HashMap<String , Object>();
        	map.put("sn", purchaseReturn.getSn());
        	map.put("date", sdf.format(purchaseReturn.getCreateDate()));
        	map.put("supplier", purchaseReturn.getSupplier().getName());
        	if(purchaseReturn.getType()==Type.applied){
        		map.put("type", "已申请");
        	}else{
        		map.put("type", "已出库");
        	}
        	for(PurchaseReturnsItem purchaseItem:purchaseReturn.getPurchaseItems()){
        		Map<String,Object> map2=new HashMap<String , Object>();
        		map2.put("name", purchaseItem.getName());
        		map2.put("pSn", purchaseItem.getSn());
        		map2.put("price", purchaseItem.getPrice());
        		map2.put("quantity", purchaseItem.getQuantity());
        		maps2.add(map2);
        	}
        	map.put("purchaseItem", maps2);
        	maps.add(map);
        }
        return maps;
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(ModelMap model) {
        Member member = memberService.getCurrent();

        List<Filter> filters = new ArrayList<Filter>();
        filters.add(new Filter("tenant", Filter.Operator.eq, member.getTenant()));
        filters.add(new Filter("status", Filter.Operator.eq, TenantRelation.Status.success));
        List<TenantRelation> relations = tenantRelationService.findList(null, filters, null);

        Date nowTime = new Date();
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        model.addAttribute("time", time.format(nowTime));

        String code = generatePurchaseRetures();
        model.addAttribute("code", code);
        model.addAttribute("suppliers", relations);
        return "/helper/member/purchase_returns/add";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, PurchaseReturns.Type type,ModelMap model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return ERROR_VIEW;
        }

        PurchaseReturns purchaseReturns = purchaseReturnsService.find(id);
        model.addAttribute("purchaseReturns",purchaseReturns);
        model.addAttribute("type",type);
        return "/helper/member/purchase_returns/edit";
    }

    @RequestMapping(value = "/print", method = RequestMethod.GET)
    public String print(Long id,ModelMap model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return ERROR_VIEW;
        }

        PurchaseReturns purchaseReturns = purchaseReturnsService.find(id);
        model.addAttribute("purchaseReturns",purchaseReturns);
        model.addAttribute("member",member);
        return "/helper/member/purchase_returns/print_purchase_returns";
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock update(@PathVariable Long id,Long[] sns,Integer[] quartitys ) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }

        PurchaseReturns purchaseReturns = purchaseReturnsService.find(id);
        if (purchaseReturns == null) {
            return DataBlock.error("无效的退货单");
        }

        for(int i=0;i<sns.length;i++){
            boolean b = productService.snExists(sns[i].toString(),member.getTenant());
            Product product = productService.findBySn(sns[i].toString());
            product.setStock(product.getStock()-quartitys[i]);
            product.setTenant(member.getTenant());
            product.setSupplier(purchaseReturns.getSupplier());
            if(b){
                productService.update(product);
            }else {
                productService.delete(product);
            }
        }
        purchaseReturns.setType(PurchaseReturns.Type.outStorage);
        purchaseReturnsService.update(purchaseReturns);
        return DataBlock.success("success", "删除成功");
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock delete(@PathVariable Long id, HttpServletRequest request) {
        return DataBlock.success("success", "删除成功");
    }

    @RequestMapping(value = "/supplier", method = RequestMethod.GET)
    public String supplier(Long id, String keyword, Pageable pageable,ModelMap model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return ERROR_VIEW;
        }
        Tenant supplier = tenantService.find(id);
        if (supplier == null) {
            return ERROR_VIEW;
        }
        Page<Product> productPage = productService.findSupplierPage(supplier,null,null, member.getTenant(),keyword,pageable);
        model.addAttribute("page",productPage);
        model.addAttribute("supplierId",id);
        model.addAttribute("keywords",keyword);
        return "/helper/member/purchase_returns/supplier_list";
    }

    /**
     * 提交采购单
     *
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock save(String sn, Long supplierId, String purchaseDate,Long[] ids,Integer[] quartitys ,String[] memos,RedirectAttributes redirectAttributes) {
        Member member = memberService.getCurrent();
        Tenant tenant = tenantService.find(supplierId);
        if (tenant == null) {
            return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
        PurchaseReturns purchaseReturns = new PurchaseReturns();
        purchaseReturns.setSn(sn);
        purchaseReturns.setSupplier(tenant);

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = time.parse(purchaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        purchaseReturns.setTenant(member.getTenant());
        purchaseReturns.setPurchaseDate(date);
        purchaseReturns.setOperator(member.getDisplayName());

        List<PurchaseReturnsItem> purchaseReturnsItems = new ArrayList<>();
        for(int i=0;i<ids.length;i++){
            Product product = productService.find(ids[i]);
            PurchaseReturnsItem purchaseReturnsItem = new PurchaseReturnsItem();
            purchaseReturnsItem.setSn(product.getSn());
            purchaseReturnsItem.setName(product.getFullName());
            purchaseReturnsItem.setBarcode(product.getBarcode());
            purchaseReturnsItem.setPrice(product.getCost());
            for (SpecificationValue specificationValue:product.getSpecificationValues()) {
                if (specificationValue.getSpecification().getId().equals(1L)) {
                    purchaseReturnsItem.setSpec(specificationValue.getName());
                }
                if (specificationValue.getSpecification().getId().equals(2L)) {
                    purchaseReturnsItem.setModel(specificationValue.getName());
                }
            }
            purchaseReturnsItem.setQuantity(quartitys[i]);
            purchaseReturnsItem.setPurchaseReturns(purchaseReturns);

            if(memos.length>0){
                purchaseReturnsItem.setMemo(memos[i]);
            }

            purchaseReturnsItems.add(purchaseReturnsItem);
        }

        purchaseReturns.setType(PurchaseReturns.Type.applied);
        purchaseReturns.setPurchaseItems(purchaseReturnsItems);
        purchaseReturnsService.save(purchaseReturns);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return DataBlock.success("success", "执行成功");
    }

    @RequestMapping(value = "/generate/purchase", method = RequestMethod.GET)
    public String generatePurchaseRetures() {
        String code = snService.generate(Sn.Type.purchaseReturns);
        return "CGTHD-" +code;
    }
}
