/**
 *====================================================
 * 文件名称: MemberInfoController.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014-9-11			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.controller.ajax;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.wit.Filter;
import net.wit.Message;
import net.wit.Setting;
import net.wit.Filter.Operator;
import net.wit.controller.ajax.BaseController;
import net.wit.controller.ajax.model.MemberInfoModel;
import net.wit.entity.Host;
import net.wit.entity.Member;
import net.wit.entity.Tenant;
import net.wit.service.MemberRankService;
import net.wit.service.MemberService;
import net.wit.util.JsonUtils;
import net.wit.util.QRBarCodeUtil;
import net.wit.util.SettingUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.zxing.WriterException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * @ClassName: QrcodeController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014-9-11 上午11:31:34
 */
@Controller("ajaxQrCodeController")
@RequestMapping("/ajax/qrcode")
public class QrcodeController extends BaseController {
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	/**
	 * 根据
	 */
	@RequestMapping(method = RequestMethod.GET)
	public void qrcode(HttpServletRequest request, HttpServletResponse response) {
		try {
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
			// 第三方用户唯一凭证
			String appId = bundle.getString("APPID");// 睿商圈
			String redirectUri = URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/wap/bound/index.jhtml");
			String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId+"&redirect_uri="+redirectUri+"&response_type=code&scope=snsapi_base&state=123&from=singlemessage&isappinstalled=1#wechat_redirect";

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
