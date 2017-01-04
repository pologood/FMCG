/**
 *====================================================
 * 文件名称: MemberController.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2015年2月1日			Chenlf(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.controller.wap.member;

import net.wit.FileInfo.FileType;
import net.wit.Filter;
import net.wit.Message;
import net.wit.Pageable;
import net.wit.controller.app.model.MemberBankModel;
import net.wit.controller.wap.BaseController;
import net.wit.controller.wap.model.MemberListModel;
import net.wit.entity.*;
import net.wit.entity.Idcard.AuthStatus;
import net.wit.entity.Member.Gender;
import net.wit.service.*;
import net.wit.uic.api.UICService;
import net.wit.util.DateUtil;
import net.wit.util.JsonUtils;
import net.wit.weixin.main.MenuManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.imageio.IIOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @ClassName: MemberController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Chenlf
 * @date 2015年2月1日 下午10:27:34
 */
@Controller("wapMemberController")
@RequestMapping("/wap/member")
public class MemberController extends BaseController {
	public static final String CAPTCHA_SECURITYCODE_SESSION = "captcha_safe_key";
	public static final String CAPTCHA_CONTENT_SESSION = "captcha_code";

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	@Resource(name = "idcardServiceImpl")
	private IdcardService idcardService;

	@Resource(name = "fileServiceImpl")
	private FileService fileService;

	@Resource(name = "rsaServiceImpl")
	private RSAService rsaService;

	@Resource(name = "uicService")
	private UICService uicService;

	@Resource(name = "productImageServiceImpl")
	private ProductImageService productImageService;

	@Resource(name = "cartServiceImpl")
	private CartService cartService;
	
	@Resource(name = "tradeServiceImpl")
	private TradeService tradeService;

	@Resource(name = "memberBankServiceImpl")
	private MemberBankService memberBankService;
	
	public static final String REGISTER_SECURITYCODE_SESSION = "register_securityCode_session";

	public static final String REGISTER_CONTENT_SESSION = "register_content_session";

	@Resource(name = "couponCodeServiceImpl")
	private CouponCodeService couponCodeService;

	/**
	 * 我的-个人信息首页
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/set_info", method = RequestMethod.GET)
	public String setInformation( ModelMap model) {
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		// 第三方用户唯一凭证
		String url = bundle.getString("WeiXinSiteUrl") + "/wap/share/index.jhtml?extension=" + (memberService.getCurrent() != null ? memberService.getCurrent().getUsername() : "");
		model.addAttribute("url",url);
		return "/wap/member/set_info";
	}
	
	/**
	 * 上传头像
	 */
	@RequestMapping(value = "/setPhoto", method = RequestMethod.POST)
	public String setPhoto(MultipartFile headImg, HttpServletRequest request, RedirectAttributes redirectAttributes, HttpServletResponse response) throws IIOException{
		try {
			Member member = memberService.getCurrent();
			if (!fileService.isValid(FileType.image, headImg)) {
				addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
				return "redirect:/wap/member/set_info.jhtml";
			}
			ProductImage img = new ProductImage();
			img.setFile(headImg);
			productImageService.build(img);
			String imgUrl = img.getThumbnail();
			member.setHeadImg(imgUrl);
			memberService.update(member);
			Message msg = uicService.updateUserInfo(null, null, null, null, null, null, null, imgUrl, request);
			if (msg.getType().equals(Message.Type.error)) {
				addFlashMessage(redirectAttributes, msg);
				return "redirect:/wap/member/set_info.jhtml";
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:/wap/member/set_info.jhtml";
	}
	/**
	 * 修改姓名页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/set_name", method = RequestMethod.GET)
	public String setName(HttpServletRequest request, ModelMap model) {
		model.addAttribute("member", memberService.getCurrent());
		return "wap/member/set_name";
	}
	/**
	 * 修改姓名
	 * @param request
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/update_name", method = RequestMethod.POST)
	@ResponseBody
	public Message updateName(HttpServletRequest request,String name) {
		Member member=memberService.getCurrent();
		if (StringUtils.isEmpty(name)){
			return Message.error("姓名不能为空");
		}
		if (member.getIdcard() != null && member.getIdcard().getAuthStatus().equals(Idcard.AuthStatus.success)){
			return Message.error("实名认证通过，不能修改姓名");
		}
		member.setName(name);
		memberService.update(member);
		return Message.success("操作成功");
	}
	/**
	 * 设置性别页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/set_gender", method = RequestMethod.GET)
	public String setGender(HttpServletRequest request, ModelMap model) {
		model.addAttribute("member", memberService.getCurrent());
		return "wap/member/set_gender";
	}
	/**
	 * 修改性别
	 * @param request
	 * @param sex
	 */
	@RequestMapping(value = "/update_gender", method = RequestMethod.POST)
	@ResponseBody
	public Message update(HttpServletRequest request,String sex) {
		Member member=memberService.getCurrent();
		if("male".equals(sex)){
			member.setGender(Gender.male);
		}else{
			member.setGender(Gender.female);
		}
		memberService.update(member);
		return Message.success("操作成功");
	}
	
	@RequestMapping(value = "/set_auth_condition", method = RequestMethod.GET)
	public String setAuth(HttpServletRequest request, ModelMap model) {
		model.addAttribute("member", memberService.getCurrent());
		return "wap/member/set_auth_condition";
	}
	
	/**
	 * 修改登录密码
	 * @param securityCode
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	@ResponseBody
	public Message updateLoginPass(String securityCode,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			Message.error("您还没有登录或者注册");
		}
		
		HttpSession session = request.getSession();
		SafeKey safeKey = (SafeKey) session.getAttribute(CAPTCHA_SECURITYCODE_SESSION);
		session.removeAttribute(CAPTCHA_SECURITYCODE_SESSION);
		session.removeAttribute(CAPTCHA_CONTENT_SESSION);
		if (safeKey == null) {
			return Message.error("验证码过期了");
		}
		if (safeKey.hasExpired()) {
			return Message.error("验证码过期了");
		}
		if (!safeKey.getValue().equals(securityCode)) {
			return Message.error("验证码不正确");
		}
		
		String newLoginPassword = rsaService.decryptParameter("newLoginPassword", request);	
		String reNewLoginPassword = rsaService.decryptParameter("reNewLoginPassword", request);
		if(newLoginPassword!=null){
			if(!newLoginPassword.equals(reNewLoginPassword)){
				return Message.error("两次输入密码不一致");
			}
			member.setPassword(DigestUtils.md5Hex(newLoginPassword));
		}else{
			return Message.error("登录密码不能为空");
		} 
		memberService.update(member);
		return Message.success("success","修改成功");
	}
	/**
	 * 修改支付密码
	 * @param securityCode
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/updatePaymentPassword", method = RequestMethod.POST)
	@ResponseBody
	public Message updatePaymentPass(String securityCode,HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (member==null) {
			Message.error("您还没有登录或者注册");
		}
		
		HttpSession session = request.getSession();
		SafeKey safeKey = (SafeKey) session.getAttribute(CAPTCHA_SECURITYCODE_SESSION);
		
		session.removeAttribute(CAPTCHA_SECURITYCODE_SESSION);
		session.removeAttribute(CAPTCHA_CONTENT_SESSION);
		if (safeKey == null) {
			return Message.error("验证码过期了");
		}
		if (safeKey.hasExpired()) {
			return Message.error("验证码过期了");
		}
		if (!safeKey.getValue().equals(securityCode)) {
			return Message.error("验证码不正确");
		}
		
		
		String newPaymentPassword = rsaService.decryptParameter("newPaymentPassword", request);
		String reNewPaymentPassword = rsaService.decryptParameter("reNewPaymentPassword", request);

		if(newPaymentPassword!=null){
			if(!newPaymentPassword.equals(reNewPaymentPassword)){
				return Message.error("两次输入密码不一致");
			}
			member.setPaymentPassword(DigestUtils.md5Hex(newPaymentPassword));
		}else{
			return Message.error("支付密码不能为空");
		}
		
		memberService.update(member);
		return Message.success("success","修改成功");
	}
	/**
	 * 设置登录密码页面
	 */
	@RequestMapping(value = "/set_login_code", method = RequestMethod.GET)
	public String setLoginCode(HttpServletRequest request, ModelMap model) {
		model.addAttribute("member", memberService.getCurrent());
		return "wap/member/set_login_code";
	}
	/**
	 * 设置支付密码页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/set_payment_code", method = RequestMethod.GET)
	public String setPaymentCode(HttpServletRequest request, ModelMap model) {
		model.addAttribute("member", memberService.getCurrent());
		return "wap/member/set_payment_code";
	}



	/**
	 * 主页面
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String indexNew(HttpServletRequest request, ModelMap model, Pageable pageable) {
		Member member = memberService.getCurrent();
		if(member==null){
			return ERROR_VIEW;
		}
		model.addAttribute("member", member);
		
		Cart cart = cartService.getCurrent();
		if(cart == null){
			model.addAttribute("carts", 0);
		}else{
			model.addAttribute("carts", cart.getQuantity());
		}

		Long unpaid1 = tradeService.findWaitPayCount(member, false);
		Long unshiped1 = tradeService.findWaitShippingCount(member, null);
		Long shipped1 = tradeService.findWaitSignCount(member, null);
		Long reviewed1 = tradeService.findWaitReviewCount(member, null);

		List<Filter> filters=new ArrayList<Filter>();
		filters.add(new Filter("member",Filter.Operator.eq,member));
		filters.add(new Filter("isUsed",Filter.Operator.eq,false));
		List<CouponCode> couponCodes=couponCodeService.findList(null,filters,null);

		Integer _size = 0;
		for(CouponCode couponCode:couponCodes){
			if(!couponCode.hasExpired()){
				_size++;
			}
		}

		model.addAttribute("couponCodes",_size);

		model.addAttribute("unpaid", unpaid1);//待发货
		model.addAttribute("shipped", shipped1);//已发货
		model.addAttribute("unreview", reviewed1);//待评价
		model.addAttribute("unshiped", unshiped1);//待支付
		model.addAttribute("refunded", "0");
		model.addAttribute("type","mine");
		//model.addAttribute("favoriteProduct",favoriteProduct);
		return "wap/member/index";
	}
	
	
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public String information(HttpServletRequest request, ModelMap model) {
		model.addAttribute("member", memberService.getCurrent());
		return "wap/member/info";
	}
	
	/**
	 * 上传头像
	 */
	@RequestMapping(value = "/uploadPhoto", method = RequestMethod.POST)
	public String uploadPhoto(MultipartFile headImg, HttpServletRequest request, RedirectAttributes redirectAttributes, HttpServletResponse response) {
		try {
			Member member = memberService.getCurrent();
			if (!fileService.isValid(FileType.image, headImg)) {
				addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
				return "redirect:/wap/member/info.jhtml";
			}
			ProductImage img = new ProductImage();
			img.setFile(headImg);
			productImageService.build(img);
			String imgUrl = img.getThumbnail();
			member.setHeadImg(imgUrl);
			memberService.save(member);
			Message msg = uicService.updateUserInfo(null, null, null, null, null, null, null, imgUrl, request);
			if (msg.getType().equals(Message.Type.error)) {
				addFlashMessage(redirectAttributes, msg);
				return "redirect:/wap/member/info.jhtml";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
		return "redirect:/wap/member/info.jhtml";
	}

	/**
	 * 上传证件照
	 */
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public void uploadFile(MultipartFile pathFront, MultipartFile pathBack, HttpServletRequest request, RedirectAttributes redirectAttributes, HttpServletResponse response) {
		try {
			String imgUrl = "";
			if (pathFront != null && !pathFront.isEmpty()) {
				if (!fileService.isValid(FileType.image, pathFront)) {
					addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
				} else {
					ProductImage img = new ProductImage();
					img.setFile(pathFront);
					productImageService.build(img);
					imgUrl = img.getThumbnail();
				}
			}
			if (pathBack != null && !pathBack.isEmpty()) {
				if (!fileService.isValid(FileType.image, pathBack)) {
					addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
				} else {
					ProductImage img = new ProductImage();
					img.setFile(pathBack);
					productImageService.build(img);
					imgUrl = img.getThumbnail();
				}
			}
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Cache-Control", "no-store");
			response.setDateHeader("Expires", 0);
			response.setContentType("text/html;charset=UTF-8");
			System.out.println("调用上传图片");
			JsonUtils.writeValue(response.getWriter(), imgUrl);
		} catch (Exception e) {
			addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
		}
	}

	/**
	 * 编辑
	 * @Title：edit
	 * @Description：
	 * @param type
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(String type, Long areaId, HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap model) {
		Member member = memberService.getCurrent();
		model.addAttribute("member", member);
		if ("name".equals(type)) {
			return "wap/member/modify_name";
		} else if ("sex".equals(type)) {
			return "wap/member/modify_sex";
		} else if ("phone".equals(type)) {
			return "wap/member/modify_phone";
		} else if ("email".equals(type)) {
			return "wap/member/modify_email";
		} else if ("real_name".equals(type)) {
			return "wap/member/real_name";
		} else if ("area".equals(type)) {
			Area area = areaService.find(areaId);
			if (area != null) {
				model.addAttribute("cityList", area.getParent().getParent().getChildren());
				model.addAttribute("districtList", area.getParent().getChildren());
			}
			List<Area> areaList = areaService.findRoots();
			model.addAttribute("area", area);
			model.addAttribute("areaList", areaList);
			return "wap/member/modify_area";
		} else if ("password".equals(type)) {
			return "wap/member/modify_password";
		} else if ("paymentPass".equals(type)) {
			return "wap/member/modify_paymentPass";
		} else if ("findPassword".equals(type)) {
			return "wap/member/find_password";
		}
		return "wap/member/info";
	}

	/**
	 * 修改用户信息
	 */
	@RequestMapping(value = "/updateInfo", method = RequestMethod.POST)
	public @ResponseBody Message editInfo(String type, String email, String name, String birth, String address, String mobile, String phone, String zipCode, String sex, Long areaId, HttpServletRequest request, HttpServletResponse response) {
		try {
			Member member = memberService.getCurrent();
			if ("name".equals(type)) {
				if (StringUtils.isEmpty(name))
					return Message.error("姓名不能为空");
				if (member.getIdcard() != null && member.getIdcard().getAuthStatus().equals(Idcard.AuthStatus.success))
					return Message.error("实名认证通过，不能修改姓名");
				member.setName(name);
				memberService.update(member);
				return uicService.updateUserInfo(name, null, null, null, null, null, null, null, request);
			} else if ("sex".equals(type)) {
				if ("0".equals(sex)) {
					member.setGender(Gender.male);
				} else {
					member.setGender(Gender.female);
				}
				memberService.update(member);
				return uicService.updateUserInfo(null, null, null, null, null, sex, null, null, request);
			} else if ("area".equals(type)) {
				Area area = areaService.find(areaId);
				if (area == null) {
					return Message.error("区域错误");
				}
				member.setArea(area);
				memberService.update(member);
				return uicService.updateUserInfo(null, null, null, null, null, null, areaId.toString(), null, request);
			} else if ("birth".equals(type)) {
				member.setBirth(DateUtil.parseDate(birth));
				memberService.update(member);
				return uicService.updateUserInfo(null, birth, null, null, null, null, null, null, request);
			} else if ("address".equals(type)) {
				member.setAddress(address);
				memberService.update(member);
				return uicService.updateUserInfo(null, null, address, null, null, null, null, null, request);
			} else if ("phone".equals(type)) {
				member.setPhone(phone);
				memberService.update(member);
				return uicService.updateUserInfo(null, null, null, phone, null, null, null, null, request);
			} else if ("zipCode".equals(type)) {
				member.setZipCode(zipCode);
				memberService.update(member);
				return uicService.updateUserInfo(null, null, null, null, zipCode, null, null, null, request);
			}
			return Message.error("不支持的参数");
		} catch (Exception e) {
			e.printStackTrace();
			return Message.error("修改失败");
		}
	}

	/**
	 * 实名认证
	 */
	@RequestMapping(value = "/apply", method = RequestMethod.POST)
	
	public String apply(String name, String idCard, MultipartFile pathFront, MultipartFile pathBack,  HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes) {
		try {
			String frontUrl=new String();
			String backUrl=new String();
			try {
				if (pathFront != null && !pathFront.isEmpty()) {
					if (!fileService.isValid(FileType.image, pathFront)) {
						addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
					} else {
						frontUrl = fileService.upload(null, pathFront, false);
					}
				}
				if (pathBack != null && !pathBack.isEmpty()) {
					if (!fileService.isValid(FileType.image, pathBack)) {
						addFlashMessage(redirectAttributes, Message.error("无效的文件类型"));
					} else {
						 backUrl = fileService.upload(null, pathBack, false);
					}
				}
				response.setHeader("Pragma", "no-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setHeader("Cache-Control", "no-store");
				response.setDateHeader("Expires", 0);
				response.setContentType("text/html;charset=UTF-8");
				
			} catch (Exception e) {
				return "redirect:/wap/member/set_auth_condition.jhtml";
			}
			Member member = memberService.getCurrent();
			Idcard idcard = member.getIdcard();
			if (idcard == null) {
				idcard = new Idcard();
			}
			idcard.setPathFront(frontUrl);
			idcard.setPathBack(backUrl);
			idcard.setNo(idCard);
			idcard.setAuthStatus(AuthStatus.wait);
			idcard.setName(name);
			idcardService.save(idcard);
			member.setIdcard(idcard);
//			member.setName(name);
			memberService.save(member);

//			Message msg = uicService.idcardSave(name, idCard, frontUrl, backUrl, request);
			return "redirect:/wap/member/set_info.jhtml";
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/wap/member/set_auth_condition.jhtml";
		}
	}

	/**
	 * 修改密码
	 */
	@RequestMapping(value = "/updatePass", method = RequestMethod.POST)
	@ResponseBody
	public Message updatePass(HttpServletRequest request) {
		String newPass = rsaService.decryptParameter("newPass", request);
		String oldPass = rsaService.decryptParameter("oldPass", request);
		rsaService.removePrivateKey(request);
		return uicService.updatePass(newPass, oldPass, request);
	}

	/**
	 * 修改密码
	 */
	@RequestMapping(value = "/updatePaymentPass", method = RequestMethod.POST)
	@ResponseBody
	public Message updatePaymentPass(HttpServletRequest request) {
		String newPass = rsaService.decryptParameter("newPass", request);
		String oldPass = rsaService.decryptParameter("oldPass", request);
		rsaService.removePrivateKey(request);
		return uicService.updatePaymentPass(newPass, oldPass, request);
	}

	/**
	 * 发送找回密码验证码
	 */
	@RequestMapping(value = "/send_captcha", method = RequestMethod.POST)
	@ResponseBody
	public Message send_captcha(HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (!member.getBindMobile().equals(Member.BindStatus.binded)) {
			return Message.error("未绑定手机");
		}
		return uicService.sendAuthcodeByTel(member.getMobile(), member.getUsername(), request);
	}

	/**
	 * 重置密码提交(通过手机/邮箱)
	 */
	@RequestMapping(value = "/retrievePass", method = RequestMethod.POST)
	@ResponseBody
	public Message retrievePass(String securityCode, HttpServletRequest request) {
		String newpassword = rsaService.decryptParameter("newPass", request);
		Member current = memberService.getCurrent();
		Message msg = uicService.retrievePass(current.getUsername(), newpassword, securityCode, request);
		if (msg.getType().equals(Message.Type.success)) {
			current.setSafeKey(null);
			current.setPaymentPassword(DigestUtils.md5Hex(newpassword));
			memberService.update(current);
			return Message.success("密码重置成功");
		} else {
			return Message.error("密码重置失败");
		}
	}

	/**
	 * 发送绑定验证码
	 */
	@RequestMapping(value = "/bind_send_captcha", method = RequestMethod.POST)
	@ResponseBody
	public Message bindSendCaptcha(String bindNo, String type, HttpServletRequest request) {
		Member member = memberService.getCurrent();
		if (type.equals("mobile")) {
			return uicService.sendAuthcodeByTel(bindNo, member.getUsername(), request);
		} else {
			return uicService.sendAuthcodeByMail(bindNo, member.getUsername(), request);
		}
	}

	/**
	 * 绑定手机/邮箱
	 */
	@RequestMapping(value = "/bind_save", method = RequestMethod.POST)
	@ResponseBody
	public Message bind_save(String bindNo, String type, String securityCode, HttpServletRequest request) {
		return uicService.bindUpdate(securityCode, request);
	}

	/**
	 * 钱包首页
	 * @return
	 */
	@RequestMapping(value = "/purse/index", method = RequestMethod.GET)
	public String purse(){
		return "wap/member/purse/index";
	}

	/**
	 * 可提现金额
	 * @return
	 */
	@RequestMapping(value = "/purse/withdraw", method = RequestMethod.GET)
	public String withdraw(){
		return "wap/member/purse/withdraw";
	}

	/**
	 * 充值
	 * @return
	 */
	@RequestMapping(value = "/purse/charge", method = RequestMethod.GET)
	public String charge(){
		return "wap/member/purse/charge";
	}

	/**
	 * 提现
	 * @return
	 */
	@RequestMapping(value = "/purse/cash", method = RequestMethod.GET)
	public String cash(Long bankId,ModelMap model){
		if(bankId!=null){
			MemberBank memberBank=memberBankService.find(bankId);
			MemberBankModel memberBankModel = new MemberBankModel();
			memberBankModel.copyFrom(memberBank);
			model.addAttribute("bank",memberBankModel);
		}
		return "wap/member/purse/cash";
	}

	/**
	 * 银行卡列表
	 * @return
	 */
	@RequestMapping(value = "/purse/bank", method = RequestMethod.GET)
	public String bank(String choose_bank,ModelMap model){
		if(StringUtils.isNotBlank(choose_bank)){
			model.addAttribute("choose_bank",choose_bank);
		}
		Member member=memberService.getCurrent();
		model.addAttribute("idcard",member.getIdcard());
		return "wap/member/purse/bank";
	}

	/**
	 * 添加银行卡
	 * @return
	 */
	@RequestMapping(value = "/purse/addBank", method = RequestMethod.GET)
	public String addBank(String choose_bank,ModelMap model){
		if(StringUtils.isNotBlank(choose_bank)){
			model.addAttribute("choose_bank",choose_bank);
		}
		return "wap/member/purse/add_bank";
	}

	/**
	 * 确认支付
	 * @param amount
	 * @param model
     * @return
     */
	@RequestMapping(value = "/purse/payment", method = RequestMethod.GET)
	public String payment(BigDecimal amount, ModelMap model){
		model.addAttribute("amount",amount);
		return "wap/member/purse/payment";
	}

	/**
	 * 收款
	 * @return
     */
	@RequestMapping(value = "/purse/checkout", method = RequestMethod.GET)
	public String checkout(){
		return "wap/member/purse/checkout";
	}

	/**
	 * 扫码付款
	 * @param amount
	 * @param model
     * @return
     */
	@RequestMapping(value = "/purse/scanner", method = RequestMethod.GET)
	public String scanner(BigDecimal amount,ModelMap model){
		model.addAttribute("amount",amount);
		return "wap/member/purse/scanner";
	}

	/**
	 * 收款成功
     * @return
     */
	@RequestMapping(value = "/purse/success", method = RequestMethod.GET)
	public String success(String sn,ModelMap model){
		model.addAttribute("sn",sn);
		return "wap/member/purse/success";
	}

	/**
	 * 我的推广
	 * @return
	 */
	@RequestMapping(value = "/promoting/index", method = RequestMethod.GET)
	public String promoting(ModelMap model){
		Member member = memberService.getCurrent();
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		Member promotingMember  = member.getMember();
		model.addAttribute("promotingMembers",memberService.findList(member).size());
		model.addAttribute("promotingMember",promotingMember==null?"":promotingMember.getDisplayName());
		model.addAttribute("rebateAmount",member.getRebateAmount());

		String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/share/promoting/intro.jhtml?extension=" + (member != null ? member.getUsername() : ""));

		String _title="好货多多,您的好友喊您来挑货啦!";

		if("101".equals(bundle.getString("wxMenu"))){
			_title="好货多多,您的好友喊您来直通邦啦!";
		}

		model.addAttribute("link", url);
		model.addAttribute("title",_title);
		model.addAttribute("desc", "推广新用户,注册有惊喜。发展会员,享受永久分润。会员越多收入越多。");
		model.addAttribute("imgUrl", member.getHeadImg());

		return "wap/member/promoting/index";
	}

	/**
	 * 我推广的会员列表
	 * @return
	 */
	@RequestMapping(value = "/promoting/list", method = RequestMethod.GET)
	public String promotingMember(ModelMap model){
		Member member = memberService.getCurrent();
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		model.addAttribute("promotingMembers", MemberListModel.bindData(memberService.findList(member)));
		model.addAttribute("rebateAmount",member.getRebateAmount());

		String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/share/promoting/intro.jhtml?extension=" + (member != null ? member.getUsername() : ""));

		String _title="好货多多,您的好友喊您来挑货啦!";

		if("101".equals(bundle.getString("wxMenu"))){
			_title="好货多多,您的好友喊您来直通邦啦!";
		}
		model.addAttribute("link", url);
		model.addAttribute("title",_title);
		model.addAttribute("desc", "推广新用户,注册有惊喜。发展会员,享受永久分润。会员越多收入越多。");
		model.addAttribute("imgUrl", member.getHeadImg());

		return "wap/member/promoting/list";
	}

	/**
	 * 我的咨询
	 */

	@RequestMapping(value = "/consulting/list", method = RequestMethod.GET)
	public String consulting(ModelMap model){
//		Member member = memberService.getCurrent();
//		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
//		model.addAttribute("promotingMembers", MemberListModel.bindData(memberService.findList(member)));
//		model.addAttribute("rebateAmount",member.getRebateAmount());
//
//		String url = MenuManager.codeUrlO2(bundle.getString("WeiXinSiteUrl") + "/wap/share/promoting/intro.jhtml?extension=" + (member != null ? member.getUsername() : ""));
//		model.addAttribute("link", url);
//		model.addAttribute("title","好货多多,您的好友喊您来挑货啦!");
//		model.addAttribute("desc", "推广新用户,注册有惊喜。发展会员,享受永久分润。会员越多收入越多。");
//		model.addAttribute("imgUrl", member.getHeadImg());

		return "wap/member/consulting/list";
	}
}
