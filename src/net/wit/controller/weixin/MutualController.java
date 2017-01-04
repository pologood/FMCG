/**
 * @Title：MutualController.java
 * @Package：net.wit.controller.wap.member
 * @Description：
 * @author：Chenlf
 * @date：2015年3月15日 下午9:03:54
 * @version：V1.0
 */

package net.wit.controller.weixin;

import net.wit.Setting;
import net.wit.controller.weixin.model.DataBlock;
import net.wit.entity.Member;
import net.wit.service.MemberService;
import net.wit.util.SettingUtils;
import net.wit.util.Sha1Util;
import net.wit.weixin.main.MenuManager;
import net.wit.weixin.pojo.Ticket;
import net.wit.weixin.util.WeiXinUtils;
import net.wit.weixin.util.WeixinUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @ClassName：MutualController
 * @Description：
 * @author：Chenlf
 * @date：2015年3月15日 下午9:03:54
 */

@Controller("weixinMutualController")
@RequestMapping("/weixin/mutual")
public class MutualController extends BaseController {
    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    public String getSha1Sign(HashMap<String, Object> params) {
        try {
            String str1 = WeiXinUtils.FormatBizQueryParaMap(params, false);
            return Sha1Util.encode(str1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取微信配置参数
     *
     * @param url
     * @return
     */
    @RequestMapping(value = "/get_config", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock getConfig(String url) {
        String noncestr = WeiXinUtils.CreateNoncestr();
        String timeStamp = WeiXinUtils.getTimeStamp();
        HashMap<String, Object> map = new HashMap<>();
        map.put("noncestr", noncestr);
        Ticket ticket = WeixinUtil.getTicket();
        if (ticket == null) {
            return DataBlock.error("ticket 获取失败");
        }
        map.put("jsapi_ticket", ticket.getTicket());
        map.put("timestamp", timeStamp);
        map.put("url", url);
        String sha1Sign = getSha1Sign(map);
        HashMap<String, Object> config = new HashMap<>();
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        Setting setting = SettingUtils.get();
        config.put("appId", bundle.getString("APPID"));
        config.put("timestamp", timeStamp);
        config.put("nonceStr", noncestr);
        config.put("signature", sha1Sign);
        Member member=memberService.getCurrent();
        config.put("sharetitle", "欢迎关注" + setting.getSiteName());
        config.put("sharedesc", "聚焦同城好店，这座城市，你想要的，我来实现。");
        config.put("sharelink", MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/weixin/index.jhtml?extension=" + (member != null ? member.getUsername() : ""))));
        config.put("shareimage", bundle.getString("WeiXinSiteUrl") + "/upload/welcome.png");
        return DataBlock.success(config, "执行成功");
    }
}
