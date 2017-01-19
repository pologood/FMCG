package net.wit.controller.weixin.member;

import net.wit.controller.weixin.BaseController;
import net.wit.controller.weixin.model.DataBlock;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 社交圈
 */
@Controller("weixinMemberContactController")
@RequestMapping("/weixin/member/contact")
public class ContactController extends BaseController {
    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "productServiceImpl")
    private ProductService productService;

    @Resource(name = "contactServiceImpl")
    private ContactService contactService;

    @Resource(name = "memberAttributeServiceImpl")
    private MemberAttributeService memberAttributeService;

    @Resource(name = "contactProductServiceImpl")
    private ContactProductService contactProductService;

    @Resource(name = "productImageServiceImpl")
    private ProductImageService productImageService;

    @Resource(name = "consumerServiceImpl")
    private ConsumerService consumerService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "tagServiceImpl")
    private TagService tagService;

    @Resource(name = "fileServiceImpl")
    private FileService fileService;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    /**
     * 发表说说
     * content  内容
     * type  类型{订单秀order,魔拍秀camera,任性秀wayward}
     * isShow   是否显示(true,false)
     * images[{file 文件}]  图片集合
     * ids  商品id（数组）
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock save(Contact contact, Long[] ids, HttpServletRequest request) {
        Member member = memberService.getCurrent();
        if (member == null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        if (contact == null) {
            return DataBlock.error("无效内容");
        }
        for (Iterator<ProductImage> iterator = contact.getImages().iterator(); iterator.hasNext(); ) {
            ProductImage productImage = iterator.next();
            if (productImage == null || productImage.isEmpty()) {
                iterator.remove();
            }
        }
        for (ProductImage productImage : contact.getImages()) {
            productImageService.build(productImage);
        }
        List<Product> products = productService.findList(ids);
        List<ContactProduct> contactProducts = new ArrayList<>();
        for (Product product : products) {
            ContactProduct cp = new ContactProduct();
            cp.setSn(product.getSn());
            cp.setName(product.getName());
            cp.setFullName(product.getFullName());
            cp.setPrice(product.getPrice());
            cp.setMarketPrice(product.getMarketPrice());
            cp.setLarge(product.getLarge());
            cp.setMedium(product.getMedium());
            cp.setThumbnail(product.getThumbnail());
            cp.setIntroduction(product.getIntroduction());
            contactProducts.add(cp);
        }
        contact.setProducts(contactProducts);
        contact.setHits(0L);
        contact.setMember(member);
        contact.setIp(request.getRemoteAddr());
        contactService.save(contact);

        Map<String, Object> data = new HashMap<>();
        data.put("id", contact.getId());
        return DataBlock.success(data, "执行成功");
    }

    /**
     * 修改个性签名
     * signature  个性签名
     */
    @RequestMapping(value = "/updateSignature", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock updateSignature(String signature) {
        Member member = memberService.getCurrent();
        MemberAttribute memberAttribute = memberAttributeService.find(MemberAttribute.GUIDE_SIGNATUREID);
        member.setAttributeValue(memberAttribute, signature);
        return DataBlock.success("success", "执行成功");
    }

    /**
     * 修改常驻区域
     */
    @RequestMapping(value = "/updateArea", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock updateArea(Long areaId) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Area area = areaService.find(areaId);
        if (area == null) {
            return DataBlock.error("无效区域");
        }
        member.setArea(area);
        memberService.update(member);
        return DataBlock.success("success", "执行成功");
    }

    /**
     * 设置个性标签
     */
    @RequestMapping(value = "/updatePersonalityTag", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock updatePersonalityTag(String[] tags) {
        Member member = memberService.getCurrent();
        MemberAttribute memberAttribute = memberAttributeService.find(MemberAttribute.GUIDE_PERSONALITYTAGID);
        List<String> options = tags != null ? Arrays.asList(tags) : null;
        member.setAttributeValue(memberAttribute, options);
        memberService.update(member);
        return DataBlock.success("success", "执行成功");
    }

}
