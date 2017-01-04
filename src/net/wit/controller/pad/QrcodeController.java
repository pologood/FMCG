package net.wit.controller.pad;

import com.fr.base.Inter;
import com.google.zxing.WriterException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.entity.Member;
import net.wit.entity.Qrcode;
import net.wit.entity.Tenant;
import net.wit.service.MemberService;
import net.wit.service.ProductService;
import net.wit.service.QrcodeService;
import net.wit.service.TenantService;
import net.wit.util.QRBarCodeUtil;
import net.wit.weixin.main.MenuManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 二维码
 * Created by Administrator on 2016/11/16.
 */
@Controller("padQrcodeController")
@RequestMapping("/pad/qrcode")
public class QrcodeController extends BaseController {
    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "qrcodeServiceImpl")
    private QrcodeService qrcodeService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "productServiceImpl")
    private ProductService productService;


    //店铺二维码
    @RequestMapping(value = "/tenant", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock getQrcode(Long id) {
        Tenant tenant = tenantService.find(id);
        String url = "";
        Qrcode qrcode = qrcodeService.findbyTenant(tenant);
        if (tenant == null) {
            return DataBlock.error("还没有开通店铺，快去申请吧。");
        }
        if (qrcode != null) {
            url = qrcode.getUrl();
        }
        Map map = new HashMap<>();
        map.put("id",id);
        map.put("image",tenant.getThumbnail());
        map.put("url",url);
        return DataBlock.success(map, "执行成功");
    }

    /**
     * 员工二维码
     */
    @RequestMapping(value = "/employee", method = RequestMethod.GET)
    public void qrcodeEmployee(Long id, HttpServletResponse response) {
        Member member = memberService.find(id);
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
     * 生成收藏二维码
     */
    @RequestMapping(value = "/favorite", method = RequestMethod.GET)
    public void favorite(String ids, HttpServletResponse response) {
        try {
            ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
            // 第三方用户唯一凭证
            String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/member/favorite/addAll.jhtml?ids=" + ids);
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

}
