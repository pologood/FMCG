/**
 * @Title：CoreController.java 
 * @Package：net.wit.controller.weixin 
 * @Description：
 * @author：Chenlf
 * @date：2015年2月17日 上午9:34:09 
 * @version：V1.0   
 */

package net.wit.controller.weixin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.wit.Setting;
import net.wit.entity.BindUser;
import net.wit.entity.Member;
import net.wit.entity.Member.BindStatus;
import net.wit.entity.Qrcode;
import net.wit.entity.Tenant;
import net.wit.entity.BindUser.Type;
import net.wit.service.AreaService;
import net.wit.service.BindUserService;
import net.wit.service.MemberRankService;
import net.wit.service.MemberService;
import net.wit.service.QrcodeService;
import net.wit.util.SettingUtils;
import net.wit.weixin.main.MenuManager;
import net.wit.weixin.message.resp.Article;
import net.wit.weixin.message.resp.NewsMessage;
import net.wit.weixin.util.MessageUtil;
import net.wit.weixin.util.SignUtil;

/**
 * @ClassName：CoreController
 * @Description：
 * @author：Chenlf
 * @date：2015年2月17日 上午9:34:09
 */
@Controller("weiXinCoreController")
@RequestMapping("/weixin/core")
public class CoreController extends BaseController {

	@Resource(name = "qrcodeServiceImpl")
	private QrcodeService qrcodeService;
	
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "bindUserServiceImpl")
	private BindUserService bindUserService;

	/**
	 * 确认请求来自微信服务器
	 */
	@RequestMapping(value = "/notify",method = RequestMethod.GET)
	public String doGet(HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");

		// 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
		if (SignUtil.checkSignature(signature, timestamp, nonce)) {
			model.addAttribute("notifyMessage", echostr);
		} else {
			model.addAttribute("notifyMessage", "error");
		}
		return "/weixin/common/notify";
	}

	/**
	 * 处理微信服务器发来的消息
	 */

	@RequestMapping(value = "/notify",method = RequestMethod.POST)
	public String doPost(HttpServletRequest request, HttpServletResponse response, ModelMap model) {		// 调用核心业务类接收消息、处理消息
		try {
			String respMessage = null;
			// 默认返回的文本消息内容
			String respContent = "请求处理异常，请稍候尝试！";
			Map<String, String> requestMap = MessageUtil.parseXml(request);
			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");

			Member member = null;
			Tenant tenant = null;
			
			// 事件推送
		    if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
				NewsMessage newsMessage = new NewsMessage();

				newsMessage.setToUserName(fromUserName);
				newsMessage.setFromUserName(toUserName);
				newsMessage.setCreateTime(new Date().getTime());
				newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
				newsMessage.setFuncFlag(0);

				// 事件类型
				String eventType = requestMap.get("Event");
				Setting setting = SettingUtils.get();
	            //  System.out.println(eventType);
				// 订阅
				if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
					try {
					    ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
						String eventKey = null;
						if (requestMap.containsKey("EventKey")) {
						    eventKey = requestMap.get("EventKey");
						}
						List<Article> articles = new ArrayList<Article>();
						Article article = new Article();
						article.setDescription("聚焦同城好店，这座城市，你想要的，我来实现。");
						article.setPicUrl(bundle.getString("WeiXinSiteUrl")+"/upload/welcome.png");
						article.setTitle("欢迎关注"+setting.getSiteName());
						article.setUrl(bundle.getString("welcome"));
						articles.add(article);
                        if (eventKey!=null && !eventKey.isEmpty() && !"".equals(eventKey)) {
							if(eventKey.indexOf("qrscene")!=-1){
								String idStr = eventKey.substring(8);
								Long id = Long.parseLong(idStr);
								Qrcode qrcode = qrcodeService.find(id);
								if (qrcode!=null && qrcode.getTenant()!=null) {

									String description = "";
									if(!"".equals(qrcode.getTenant().getIntroduction())){
										description=qrcode.getTenant().getIntroduction();
									}
                                    tenant = qrcode.getTenant();
									Article art = new Article();
									art.setDescription(description);
									art.setPicUrl(tenant.getThumbnail());
									art.setTitle("立即进入"+tenant.getName());
									art.setUrl(MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl")+"/wap/tenant/index/"+tenant.getId()+".jhtml?extension="+tenant.getMember().getUsername()));
									articles.clear();
									articles.add(art);
								}
							}
                        }
                        
                        BindUser bindUser = bindUserService.findByUsername(fromUserName,Type._wx);
            		    if (bindUser==null) {
            		    	member = memberService.createAndBind(fromUserName,areaService.find(0L),tenant==null?null:tenant.getMember(),"127.0.0.1");
            		    } else {
            		    	member = bindUser.getMember();
            		    }
            		    
                        if (member!=null && !member.getBindMobile().equals(BindStatus.binded)) {
							Article art = new Article();
							art.setDescription(member.getNickName()+"欢迎你。");
							art.setPicUrl(member.getHeadImg());
							art.setTitle("点击立即绑定手机");
							art.setUrl(MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl")+"//wap/bound/indexNew.jhtml"));
							articles.add(art);
                        }
                        newsMessage.setArticles(articles);
						newsMessage.setArticleCount(articles.size());
						respMessage = MessageUtil.newsMessageToXml(newsMessage);
						model.addAttribute("notifyMessage", respMessage);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				// 取消订阅
				else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {
					// Member member = memberService.findByWechatId(fromUserName);
					// if (member != null) {
					// member.setIsEnabled(false);
					// memberService.save(member);
					// }
				}
				// 自定义菜单点击事件
				else if (eventType.equals(MessageUtil.EVENT_TYPE_VIEW)) {
					// String eventKey = requestMap.get("EventKey");
					// if (eventKey.equals("MENU_TO_ORDER")) {
					//
					// }
					// 扫描二维码
				} else if (eventType.equals(MessageUtil.SCAN)) {
				    ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
					List<Article> articles = new ArrayList<Article>();
					Article article = new Article();
					article.setDescription("聚焦同城好店，这座城市，你想要的，我来实现。");
					article.setPicUrl(bundle.getString("WeiXinSiteUrl")+"/upload/welcome.png");
					article.setTitle("欢迎关注"+setting.getSiteName());
					article.setUrl(bundle.getString("welcome"));
					String eventKey = null;
					if (requestMap.containsKey("EventKey")) {
					    eventKey = requestMap.get("EventKey");
					}
					articles.add(article);
		            //System.out.println(eventKey);
                    if (eventKey!=null && !eventKey.isEmpty() && !"".equals(eventKey)) {
                    	String idStr = eventKey;
                    	Long id = Long.parseLong(idStr);
                    	Qrcode qrcode = qrcodeService.find(id);
                    	if (qrcode!=null && qrcode.getTenant()!=null) {
    						Article art = new Article();

							String description = "";
							if(!"".equals(qrcode.getTenant().getIntroduction())){
								description=qrcode.getTenant().getIntroduction();
							}

                            tenant = qrcode.getTenant();
    						art.setDescription(description);
    						art.setPicUrl(qrcode.getTenant().getThumbnail());
    						art.setTitle("立即进入"+qrcode.getTenant().getName());
    						art.setUrl(MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl")+"/wap/tenant/index/"+qrcode.getTenant().getId()+".jhtml?extension="+qrcode.getTenant().getMember().getUsername()));
    						articles.clear();
    						articles.add(art);
                    	}
                    }
                    BindUser bindUser = bindUserService.findByUsername(fromUserName,Type._wx);
        		    if (bindUser==null) {
						member = memberService.createAndBind(fromUserName, areaService.find(0L),tenant==null?null:tenant.getMember(), "127.0.0.1");
					} else {
        		    	member = bindUser.getMember();
        		    }
                    if (member!=null && !member.getBindMobile().equals(BindStatus.binded)) {
						Article art = new Article();
						art.setDescription(member.getNickName()+"欢迎你。");
						art.setPicUrl(member.getHeadImg());
						art.setTitle("点击立即绑定手机");
						art.setUrl(MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl")+"//wap/bound/indexNew.jhtml"));
						articles.add(art);
                    }
                    newsMessage.setArticles(articles);
					newsMessage.setArticleCount(articles.size());
					respMessage = MessageUtil.newsMessageToXml(newsMessage);
					model.addAttribute("notifyMessage", respMessage);
				}
			}

			model.addAttribute("notifyMessage", respMessage);
			return "weixin/common/notify";
		} catch (Exception e) {
			e.printStackTrace();
			return ERROR_VIEW;
		}
	}

}
