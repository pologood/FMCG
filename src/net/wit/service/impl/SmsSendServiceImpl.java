package net.wit.service.impl;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import net.wit.Template;
import net.wit.dao.SmsSendDao;
import net.wit.entity.Member;
import net.wit.entity.SmsSend;
import net.wit.entity.SmsSend.Status;
import net.wit.entity.SmsSend.Type;
import net.wit.service.MemberService;
import net.wit.service.SmsSendService;
import net.wit.service.TemplateService;
import net.wit.webservice.MwSmsClient;
import net.wit.webservice.SmsClient;

import org.jsoup.helper.StringUtil;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;

@Service("smsSendServiceImpl")
public class SmsSendServiceImpl extends BaseServiceImpl<SmsSend, String> implements SmsSendService {

	@Resource(name = "smsSendDaoImpl")
	private SmsSendDao smsSendDao;

	@Resource(name = "taskExecutor")
	private TaskExecutor taskExecutor;

	@Resource(name = "smsSendDaoImpl")
	public void setBaseDao(SmsSendDao smsSendDao) {
		super.setBaseDao(smsSendDao);
	}

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "templateServiceImpl")
	private TemplateService templateService;

	@Resource(name = "freeMarkerConfigurer")
	private FreeMarkerConfigurer freeMarkerConfigurer;

	private static SmsClient client = null;
	private static MwSmsClient mwClient = null;

	public Long findSendCount(String mobile,SmsSend.Type type){
		return smsSendDao.findSendCount(mobile,type);
	}
	
	public synchronized SmsClient getClient() {
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		if (client == null) {
			try {
				client = new SmsClient(bundle.getString("softwareSerialNo"), bundle.getString("password"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return client;
	}
	
	/**获取梦网平台短信客户端*/
	public synchronized MwSmsClient getMwClient() {
		if (mwClient == null) {
			try {
				mwClient = new MwSmsClient();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mwClient;
	}

	/** 发送验证合法短信 */
	private SmsSend send(SmsSend smsSend) {
		String r = "-1";
		client = getClient();
		try {
			String content = URLEncoder.encode(smsSend.getContent(), "utf8");
			r = client.mdSmsSend_u(smsSend.getMobiles(), content, "", "", "");
		} catch (Exception e) {
			r = "-9999";
		}
		if (r.startsWith("-") || r.equals("")) {
			smsSend.setStatus(Status.Error);
			smsSend.setDescr("短信失败，错误码=" + r);
		} else {
			smsSend.setStatus(Status.send);
			smsSend.setDescr("短信发送成功");
		}
		super.save(smsSend);
		return smsSend;
	}

	/**梦网平台发送短信*/
	private SmsSend mwSend(SmsSend smsSend){
		String r="-1";
		mwClient= getMwClient();
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		String userId=bundle.getString("mwuserId");
		String password=bundle.getString("mwpassword");
		try {
			String content=smsSend.getContent();
			content=content.replace("【找汽配】", "");
			r=mwClient.MongateSendSubmit(userId, password, smsSend.getMobiles(), content, 1, "*", "0");
		} catch (Exception e) {
			r="-999";
		}
		if(r.equals("-1")||r.equals("-12")||r.equals("-14")||r.equals("-999")||
				r.equals("-10001")||r.equals("-10003")||r.equals("-10011")||
				r.equals("-10029")||r.equals("-10030")||r.equals("-10031")||r.equals("-10057")||r.equals("-10056")){
			smsSend.setStatus(Status.Error);
			smsSend.setDescr("短信失败，错误码=" + r);
		}else{
			smsSend.setStatus(Status.send);
			smsSend.setDescr("短信发送成功");
		}
		super.save(smsSend);
		return smsSend;
	}
	
	private void saveIllegalSMS(SmsSend smsSend, String message) {
		if (smsSend == null) {
			return;
		}
		smsSend.setStatus(Status.Error);
		smsSend.setDescr(message);
		super.save(smsSend);
	}

	private void putSendPool(final SmsSend smsSend) {
		try {
			taskExecutor.execute(new Runnable() {
				public void run() {
					smsSend(smsSend);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private SmsSend validate(String mobile, String content, Type type) throws IllegalArgumentException {
		if (StringUtil.isBlank(mobile)) {
			throw new java.lang.IllegalArgumentException("接收号码为空");
		}
		BigDecimal amount = new BigDecimal(1 * 1.00 / 10.00);
		SmsSend smsSend = null;
		switch (type) {
		case member:
			Member member = memberService.getCurrent();
			if (amount.compareTo(member.getBalance()) > 0) {
				throw new java.lang.IllegalArgumentException("账户余额不足");
			}
			smsSend = new SmsSend();
			smsSend.setMember(member);
			smsSend.setType(Type.member);
			smsSend.setFee(amount);
			break;
		case system:
			smsSend = new SmsSend();
			smsSend.setType(Type.system);
			smsSend.setFee(amount);
			break;
		}
		smsSend.setMobiles(mobile);
		smsSend.setContent(content);
		smsSend.setPriority(1);
		smsSend.setCharset("GBK");
		smsSend.setCount(1);
		return smsSend;
	}

	private SmsSend validate(SmsSend smsSend) throws IllegalArgumentException{
		if(smsSend==null){
			throw new java.lang.IllegalArgumentException("短信为null");
		}
		if (StringUtil.isBlank(smsSend.getMobiles())) {
			throw new java.lang.IllegalArgumentException("接收号码为空");
		}
		if (StringUtil.isBlank(smsSend.getContent())) {
			throw new java.lang.IllegalArgumentException("短信内容为空");
		}
		if (smsSend.getType()==null) {
			throw new java.lang.IllegalArgumentException("短信类型为空");
		}
		BigDecimal amount = new BigDecimal(1 * 1.00 / 10.00);
		switch (smsSend.getType()) {
		case member:
			Member member = memberService.getCurrent();
			if (amount.compareTo(member.getBalance()) > 0) {
				throw new java.lang.IllegalArgumentException("账户余额不足");
			}
			smsSend.setFee(amount);
			break;
		case system:
			/**根据需要添加相关验证**/
			smsSend.setFee(amount);
			break;
		case captcha:
			/**根据需要添加相关验证**/
			smsSend.setFee(amount);
			break;
		case service:
			/**根据需要添加相关验证**/
			smsSend.setFee(amount);
			break;
		}
		smsSend.setPriority(1);
		smsSend.setCharset("GBK");
		smsSend.setCount(1);
		return smsSend;
	}
	
	/** 发送会员短信 */
	public String sendByMember(String mobile, String content) {
		SmsSend smsSend = null;
		try {
			smsSend = validate(mobile, content, Type.member);
			send(smsSend);
			return smsSend.getDescr();
		} catch (IllegalArgumentException e) {
			saveIllegalSMS(smsSend, e.getMessage());
			return "error";
		} catch (Exception e) {
			return "error";
		}
	}

	/** 异步线程发送(暂不考虑线程池堆满情况处理) */
	public void putMemberSendPool(final String mobile, final String content) {
		SmsSend smsSend = null;
		try {
			smsSend = validate(mobile, content, Type.member);
			putSendPool(smsSend);
		} catch (IllegalArgumentException e) {
			saveIllegalSMS(smsSend, e.getMessage());
		} catch (Exception e) {
		}
	}

	/** 发送系统短信*/
	public String sysSend(String mobile, String content) {
		SmsSend smsSend = null;
		try {
			smsSend = validate(mobile, content, Type.system);
			send(smsSend);
			return smsSend.getDescr();
		} catch (IllegalArgumentException e) {
			saveIllegalSMS(smsSend, e.getMessage());
			return "error";
		} catch (Exception e) {
			return "error";
		}
	}
	
	/** 异步线程发送(暂不考虑线程池堆满情况处理) */
	public void putSystemSendPool(final String mobile, final String content) {
		SmsSend smsSend = null;
		try {
			smsSend = validate(mobile, content, Type.system);
			putSendPool(smsSend);
		} catch (IllegalArgumentException e) {
			saveIllegalSMS(smsSend, e.getMessage());
		} catch (Exception e) {
		}
	}

	public void sendTemplateNoticePool(String mobile, String tempalte, Map<String, String> model) {
		try {
			Template template = templateService.get(tempalte);
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			freemarker.template.Template templateMarker = configuration.getTemplate(template.getTemplatePath());
			String text = FreeMarkerTemplateUtils.processTemplateIntoString(templateMarker, model);
			putSystemSendPool(mobile, text);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**发送系统短信*/
	private String sysSend(SmsSend smsSend){
		return send(smsSend).getDescr();
	}
	
	/**发送会员短信*/
	private String sendByMember(SmsSend smsSend){
		return send(smsSend).getDescr();
	}
	
	/**发送验证码类短信*/
	private String captchaSend(SmsSend smsSend){
		return mwSend(smsSend).getDescr();
	}
	
	/**发送业务类短信*/
	private String serviceSend(SmsSend smsSend){
		return mwSend(smsSend).getDescr();
	}
	
	/**根据配置选择发送方式*/
	private String selectSend(SmsSend smsSend,String sendMethod){
		String result="";
		if("sysSend".equals(sendMethod)){
			result=sysSend(smsSend);
		}else if("sendByMember".equals(sendMethod)){
			result=sendByMember(smsSend);
		}else if("captchaSend".equals(sendMethod)){
			result=captchaSend(smsSend);
		}else if("serviceSend".equals(sendMethod)){
			result=serviceSend(smsSend);
		}else{
			result=serviceSend(smsSend);
		}
		return result;
	}
	
	@Override
	public String smsSend(SmsSend smsSend) {
		String result="error";
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		String system=bundle.getString("system");
		String member=bundle.getString("member");
		String captcha=bundle.getString("captcha");
		String service=bundle.getString("service");
		SmsSend valSmsSend=null;
		try {
			valSmsSend = validate(smsSend);
			switch (valSmsSend.getType()) {
			case system:
				result=selectSend(valSmsSend, system);
				break;
			case member:
				result=selectSend(valSmsSend, member);
				break;
			case captcha:
				result=selectSend(valSmsSend, captcha);
				break;
			case service:
				result=selectSend(valSmsSend, service);
				break;
			default:
				result=serviceSend(valSmsSend);
			}
			return result;
		} catch (IllegalArgumentException e) {
			saveIllegalSMS(valSmsSend, e.getMessage());
			return result;
		} catch (Exception e) {
			return result;
		}
	}
	
}