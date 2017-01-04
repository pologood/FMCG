package net.wit.controller.wap;

import com.google.zxing.WriterException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import net.wit.FileInfo;
import net.wit.Page;
import net.wit.controller.app.model.*;
import net.wit.entity.*;
import net.wit.service.*;
import net.wit.util.QRBarCodeUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.*;

/**
 * 社交圈控制层
 * Created by My-PC on 16/03/06.
 */
@Controller("wapSocialCirclesController")
@RequestMapping("/wap/social_circles")
public class SocialCirclesController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "productServiceImpl")
    private ProductService productService;

    @Resource(name = "contactServiceImpl")
    private ContactService contactService;

    @Resource(name = "promotionServiceImpl")
    private PromotionService promotionService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "cartServiceImpl")
    private CartService cartService;

    @Resource(name = "fileServiceImpl")
    private FileService fileService;
    /**
     * 首页
     * @param model
     * @return
     */
    @RequestMapping(value="/index",method= RequestMethod.GET)
    public String index(Model model){
        Member member = memberService.getCurrent();
        if(member ==null){
            return "redirect:/wap/bound/indexNew.jhtml?redirectUrl=sjq";
        }
        model.addAttribute("member",member);
        model.addAttribute("type","index");
        return "/wap/social_circles/index";
    }

    /**
     * 消息页
     * @param model
     * @return
     */
    @RequestMapping(value="/message",method= RequestMethod.GET)
    public String message(Model model){
        model.addAttribute("member",memberService.getCurrent());
        model.addAttribute("type","message");
        return "/wap/social_circles/message";
    }

    /**
     * 发现页
     * @param model
     * @return
     */
    @RequestMapping(value="/found",method= RequestMethod.GET)
    public String found(Model model){
        model.addAttribute("member",memberService.getCurrent());
        model.addAttribute("type","found");
        return "/wap/social_circles/found";
    }

    /**
     * 我的页
     * @param model
     * @return
     */
    @RequestMapping(value="/mine",method= RequestMethod.GET)
    public String mine(Model model){
        MemberListModel memberListModel = new MemberListModel();
        memberListModel.copyFrom(memberService.getCurrent(),null);
        model.addAttribute("member",memberListModel);
        model.addAttribute("type","mine");
        return "/wap/social_circles/mine";
    }

    /**
     * 选择要秀的商品
     * @param model
     * @return
     */
    @RequestMapping(value="/productShow",method= RequestMethod.GET)
    public String productShow(String type,Model model){
        model.addAttribute("member",memberService.getCurrent());
        model.addAttribute("type",type);
        return "/wap/social_circles/productShow";
    }

    /**
     * 选择要秀的商品
     * @param model
     * @return
     */
    @RequestMapping(value="/publish",method= RequestMethod.GET)
    public String publish(String type,Long[] ids,Model model){
        List<Product> product = productService.findList(ids);
        ProductListModel productListModel = new ProductListModel();
        model.addAttribute("member",memberService.getCurrent());
        model.addAttribute("type",type);
        model.addAttribute("products",productListModel.bindData(product));
        model.addAttribute("ids",ids);
        return "/wap/social_circles/publish";
    }

    /**
     * 评论  comment
     */
    @RequestMapping(value="/comment",method= RequestMethod.GET)
    public String comment(Long id,String type,Model model){

        Contact contact = contactService.find(id);
        if (contact == null) {
            return "无效文章编号";
        }
        Long hits = contact.getHits();
        hits += 1;
        contact.setHits(hits);
        contactService.update(contact);

        TenantListModel tenantModel= new TenantListModel();
        tenantModel.copyFrom(contact.getMember().getTenant());

        ContactListModel contactListModel = new ContactListModel();
        contactListModel.copyAll(contact);

        model.addAttribute("member",memberService.getCurrent());
        model.addAttribute("id",id);
        model.addAttribute("type",type);
        model.addAttribute("tenant",tenantModel);
        model.addAttribute("contact",contactListModel);
        return "/wap/social_circles/comment";
    }

    /**
     * 商品详情页面
     */
    @RequestMapping(value = "/content/preview", method = RequestMethod.GET)
    public String productDetails(String sn, String backUrl, Model model, HttpServletRequest request) {
        Page<Promotion> page = promotionService.findPage(null, areaService.getCurrent(), null, null, null, null, null, null, null, null);

        Product product = productService.findBySn(sn);
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (Promotion promotion : page.getContent()) {
            Product promotionDefaultProduct = promotion.getDefaultProduct();
            if (promotionDefaultProduct != null && product.getId().equals(promotionDefaultProduct.getId())) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name", promotion.getName());
                map.put("type", promotion.getType());
                mapList.add(map);
            }
        }
        model.addAttribute("promotions", mapList);
        Cart cart = cartService.getCurrent();
        //Long count = consultationService.count(null, product, true);
        Member member = memberService.getCurrent();
        if (member != null) {
            if (member.getFavoriteProducts().contains(product)) {
                model.addAttribute("hasFavorite", 0);
            } else {
                model.addAttribute("hasFavorite", 1);
            }
        }

        boolean hasSpecication = product.getSpecifications().size()>0;
        model.addAttribute("product", product);
        model.addAttribute("hasSpecication",hasSpecication);
        model.addAttribute("cart", cart != null ? cart.getCartItems().size() : 0);
        model.addAttribute("backUrl", backUrl);

        TenantModel tenantModel = new TenantModel();
        tenantModel.copyFrom(product.getTenant());

        model.addAttribute("tenant", tenantModel);
        model.addAttribute("type", "details");
        return "/wap/product/productDetails";
    }

    /**
     * 查看他/她的秀秀
     */
    @RequestMapping(value="/contact",method= RequestMethod.GET)
    public String contact(Long id,Long memberId,Model model){
        model.addAttribute("id",id);
        MemberListModel memberListModel = new MemberListModel();
        memberListModel.copyFrom(memberService.find(memberId),null);
        model.addAttribute("owner",memberListModel);

        model.addAttribute("member",memberService.getCurrent());
        return "/wap/social_circles/contact";
    }


    /**
     * 通过二维码分享各种秀
     */
    @RequestMapping(value = "/qrcode",method = RequestMethod.GET)
    public void qrcode(Long id,String type,HttpServletRequest request, HttpServletResponse response) {
        try {
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            // 第三方用户唯一凭证
            String url = (bundle.getString("WeiXinSiteUrl") + "/wap/social_circles/comment.jhtml?id="+id+"&type="+type);
            String tempFile = System.getProperty("java.io.tmpdir") + "/upload_" + UUID.randomUUID() + ".jpg";
            response.reset();
            response.setContentType("image/jpeg;charset=utf-8");
            try {
                QRBarCodeUtil.encodeQRCode(url, tempFile, 400, 400);
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
}
