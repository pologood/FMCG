package net.wit.controller.pad;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.SellCatalog;
import net.wit.entity.Tenant;
import net.wit.service.MemberService;
import net.wit.service.ProductService;
import net.wit.service.SellCatalogService;
import net.wit.service.TenantService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/17.
 */
@Controller("padSellCatalogController")
@RequestMapping("/pad/sellcatalog")
public class SellCatalogController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "productServiceImpl")
    private ProductService productService;

    @Resource(name = "sellCatalogServiceImpl")
    private SellCatalogService sellCatalogService;
    //明星导购推荐商品
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list(Long id, Pageable pageable) {
        Page<Map<String,Object>> page= sellCatalogService.findMyRecommendProduct(id,pageable);
        List<Map<String, Object>> maps=new ArrayList<Map<String, Object>>();
        for(Map<String, Object> m:page.getContent()){
            Map<String, Object> map=new HashMap<String,Object>();
            map.put("id", m.get("id"));
            map.put("thumbnail",m.get("image"));
            map.put("fullName", m.get("full_name"));
            map.put("price", m.get("price"));
            map.put("monthSales", m.get("month_sales"));
            maps.add(map);
        }
        Map pmap = new HashMap();
        pmap.put("products",maps);
        return DataBlock.success(pmap,page, "执行成功");
    }
}
