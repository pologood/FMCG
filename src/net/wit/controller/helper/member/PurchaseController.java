package net.wit.controller.helper.member;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import cfca.org.bouncycastle.jce.provider.M;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.app.model.ProductModel;
import net.wit.controller.helper.model.DataBlock;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.Purchase;
import net.wit.entity.Purchase.Type;
import net.wit.entity.PurchaseItem;
import net.wit.entity.Sn;
import net.wit.entity.SpecificationValue;
import net.wit.entity.Tenant;
import net.wit.entity.TenantRelation;

/**
 * 采购单控制层
 * Created by My-PC on 16/06/03.
 */
@Controller("helperMemberPurchaseController")
@RequestMapping("/helper/member/purchase")
public class PurchaseController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "purchaseServiceImpl")
    private PurchaseService purchaseService;

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
    public String list(Pageable pageable, ModelMap model,Date beginDate,Date endDate,String keywords) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return "redirect:" + ERROR_VIEW;
        }
        Page<Purchase> purchases = purchaseService.openPage(pageable,member.getTenant(),null, beginDate, endDate, null,null,keywords);
        String isMonthly="false";
        if(monthlyService.isMonthly(member,null)){
            isMonthly="true";
        }
        model.addAttribute("isMonthly",isMonthly);
        model.addAttribute("page", purchases);
        model.addAttribute("beginDate",beginDate);
        model.addAttribute("endDate",endDate);
        model.addAttribute("keywords",keywords);
        return "/helper/member/purchase/list";
    }
    
    @RequestMapping(value = "/export_list", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> exportList(Pageable pageable, ModelMap model,Date beginDate,Date endDate,String keywords) {
        List<Map<String, Object>> maps=new ArrayList<Map<String,Object>>();
        Member member = memberService.getCurrent();
        List<Purchase> purchases = purchaseService.exportOpenPage(pageable,member.getTenant(),null, beginDate, endDate, null,null,keywords);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(Purchase purchase:purchases){
        	List<Map<String, Object>> maps2=new ArrayList<Map<String,Object>>();
        	Map<String,Object> map=new HashMap<String , Object>();
        	map.put("sn", purchase.getSn());
        	map.put("date", sdf.format(purchase.getCreateDate()));
        	map.put("supplier", purchase.getSupplier().getName());
        	if(purchase.getType()==Type.applied){
        		map.put("type", "已申请");
        	}else{
        		map.put("type", "已入库");
        	}
        	for(PurchaseItem purchaseItem:purchase.getPurchaseItems()){
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

        String code = generatePurchase();
        model.addAttribute("code", code);
        model.addAttribute("suppliers", relations);
        return "/helper/member/purchase/add";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, Purchase.Type type,ModelMap model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return ERROR_VIEW;
        }

        Purchase purchase = purchaseService.find(id);
        model.addAttribute("purchase",purchase);
        model.addAttribute("type",type);
        return "/helper/member/purchase/edit";
    }

    @RequestMapping(value = "/print", method = RequestMethod.GET)
    public String print(Long id,ModelMap model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return ERROR_VIEW;
        }

        Purchase purchase = purchaseService.find(id);
        model.addAttribute("purchase",purchase);
        model.addAttribute("member",member);
        return "/helper/member/purchase/print_purchase";
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock update(@PathVariable Long id,Long[] sns,Integer[] quartitys ) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }

        Purchase purchase = purchaseService.find(id);
        if (purchase == null) {
            return DataBlock.error("采购单号无效");
        }

        for(int i=0;i<sns.length;i++){
            boolean b = productService.snExists(sns[i].toString(),member.getTenant());
            Product product = productService.findBySn(sns[i].toString());
            product.setStock(product.getStock()+quartitys[i]);
            product.setTenant(member.getTenant());
            product.setSupplier(purchase.getSupplier());
            if(b){
                productService.update(product);
            }else {
                productService.save(product);
            }
        }
        purchase.setType(Purchase.Type.inStorage);
        purchaseService.update(purchase);
        return DataBlock.success("success", "删除成功");
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock delete(@PathVariable Long id, HttpServletRequest request) {
        return DataBlock.success("success", "删除成功");
    }

    @RequestMapping(value = "/supplier", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock supplier(Long id, String keyword, Pageable pageable) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Tenant supplier = tenantService.find(id);
        if (supplier == null) {
            return DataBlock.error(DataBlock.TENANT_INVAILD);
        }
        Page<Product> productPage = productService.findSupplierPage(supplier,null,null, member.getTenant(),keyword,pageable);
        return DataBlock.success(ProductModel.bindData(productPage.getContent()),"执行成功", pageable.getPageNumber());
    }
    
    @RequestMapping(value = "/supplier_list", method = RequestMethod.GET)
    public String supplierList(Long id, String keywords, Pageable pageable,ModelMap model) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return ERROR_VIEW;
        }
        Tenant supplier = tenantService.find(id);
        if (supplier == null) {
            return ERROR_VIEW;
        }
        Page<Product> page = productService.findSupplierPage(supplier,null,null, member.getTenant(),keywords,pageable);
        model.addAttribute("page",page);
        model.addAttribute("supplierId",id);
        model.addAttribute("keywords",keywords);
        return "/helper/member/purchase/supplier_list";
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
        Purchase purchase = new Purchase();
        purchase.setSn(sn);
        purchase.setSupplier(tenant);

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = time.parse(purchaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        purchase.setTenant(member.getTenant());
        purchase.setPurchaseDate(date);
        purchase.setOperator(member.getDisplayName());

        List<PurchaseItem> purchaseItems = new ArrayList<>();
        for(int i=0;i<ids.length;i++){
            Product product = productService.find(ids[i]);
            PurchaseItem purchaseItem = new PurchaseItem();
            purchaseItem.setSn(product.getSn());
            purchaseItem.setName(product.getFullName());
            purchaseItem.setPrice(product.getCost());
            purchaseItem.setBarcode(product.getBarcode());
            for (SpecificationValue specificationValue:product.getSpecificationValues()) {
                if (specificationValue.getSpecification().getId().equals(1L)) {
                    purchaseItem.setSpec(specificationValue.getName());
                }
                if (specificationValue.getSpecification().getId().equals(2L)) {
                    purchaseItem.setModel(specificationValue.getName());
                }
            }
            purchaseItem.setQuantity(quartitys[i]);
            purchaseItem.setPurchase(purchase);
            if(memos.length>0){
                purchaseItem.setMemo(memos[i]);
            }

            purchaseItems.add(purchaseItem);
        }

        purchase.setType(Purchase.Type.applied);
        purchase.setPurchaseItems(purchaseItems);
        purchaseService.save(purchase);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return DataBlock.success("success", "执行成功");
    }

    @RequestMapping(value = "/generate/purchase", method = RequestMethod.GET)
    public String generatePurchase() {
        String code = snService.generate(Sn.Type.purchase);
        return "CGD-" +code;
    }
}
