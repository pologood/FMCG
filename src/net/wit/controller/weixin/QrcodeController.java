/**
 * @Title：CoreController.java 
 * @Package：net.wit.controller.weixin 
 * @Description：
 * @author：Chenlf
 * @date：2015年2月17日 上午9:34:09 
 * @version：V1.0   
 */

package net.wit.controller.weixin;

import net.wit.Setting;
import net.wit.entity.BindUser;
import net.wit.entity.BindUser.Type;
import net.wit.entity.Member;
import net.wit.entity.Member.BindStatus;
import net.wit.entity.Qrcode;
import net.wit.entity.Tenant;
import net.wit.service.AreaService;
import net.wit.service.BindUserService;
import net.wit.service.TenantService;
import net.wit.service.QrcodeService;
import net.wit.util.SettingUtils;
import net.wit.weixin.main.MenuManager;
import net.wit.weixin.message.resp.Article;
import net.wit.weixin.message.resp.NewsMessage;
import net.wit.weixin.util.MessageUtil;
import net.wit.weixin.util.SignUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @ClassName：QrcodeController
 * @Description：
 * @author：Chenlf
 * @date：2015年2月17日 上午9:34:09
 */
@Controller("weiXinQrcodeController")
@RequestMapping("/weixin/qrcode")
public class QrcodeController extends BaseController {

	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;


	@RequestMapping(value = "/go",method = RequestMethod.GET)
	public String go(String type,String no,HttpServletRequest request, HttpServletResponse response, ModelMap model) {		// 调用核心业务类接收消息、处理消息
       Tenant tenant = tenantService.findByCode(no);
		if (tenant==null) {
			return "redirect:/weixin/index.jhtml";
		}
		if ("paybill".equals(type)) {
			return "redirect:/weixin/tenant/offerToPay.jhtml?id=" + tenant.getId();
		} else {
			return "redirect:/weixin/index.jhtml";
		}
	}

}
