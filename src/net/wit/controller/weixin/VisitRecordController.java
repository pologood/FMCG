package net.wit.controller.weixin;

import net.wit.controller.weixin.model.DataBlock;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.Tenant;
import net.wit.entity.VisitRecord;
import net.wit.service.MemberService;
import net.wit.service.ProductService;
import net.wit.service.TenantService;
import net.wit.service.VisitRecordService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 访问记录
 * Created by WangChao on 2016/12/15.
 */
@Controller("weixinVisitRecordController")
@RequestMapping("/weixin/visitRecord")
public class VisitRecordController {
    @Resource(name = "memberServiceImpl")
    private MemberService memberService;
    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;
    @Resource(name = "visitRecordServiceImpl")
    private VisitRecordService visitRecordService;
    @Resource(name = "productServiceImpl")
    private ProductService productService;

    /**
     * 添加访问记录
     *
     * @param tenantId    店铺Id
     * @param productId   商品Id
     * @param visitType   访问类型  (pc端web,C端app,购物屏pad,微信weixin)
     * @param machineType 设备类型
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock add(Long tenantId, Long productId, String machineType, VisitRecord.VisitType visitType) {
        visitRecordService.add(memberService.getCurrent(),tenantService.find(tenantId),productService.find(productId),machineType,visitType);
        return DataBlock.success("success", "执行成功");
    }
}
