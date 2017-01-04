package net.wit.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.springframework.stereotype.Service;

import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.CreditDao;
import net.wit.dao.MemberDao;
import net.wit.dao.MinshengBankDao;
import net.wit.entity.Credit;
import net.wit.entity.Member;
import net.wit.entity.MinshengBank;
import net.wit.plugin.PaymentPlugin.RequestMethod;
import net.wit.service.MinshengTransService;

@Service("minshengTransServiceImpl")
public class MinshengTransServiceImpl extends
		BaseServiceImpl<MinshengBank, Long> implements MinshengTransService {

	@Resource(name = "creditDaoImpl")
	private CreditDao creditDao;

	@Resource(name = "minshengbankDaoImpl")
	private MinshengBankDao minshengbankDao;

	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;
	
	@Override
	public String getRequestUrl() {
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		return "http://" + bundle.getString("postip") + "/eweb/b2e/connect.do";
	}

	public String getip() {
		InetAddress addr;
		String ip = "1";
		try {
			addr = InetAddress.getLocalHost();
			ip = addr.getHostAddress().toString();// 获得本机IP
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ip;
	}

	@Override
	public RequestMethod getRequestMethod() {
		return RequestMethod.post;
	}

	@Override
	public String getRequestCharset() {
		return "GB2312";
	}

	@Override
	public Page<MinshengBank> findPage(Member member, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 获取单笔费用报销输入参数
	 */
	private String getCostParamterXml(MinshengBank minshengBank) {
		try {
			StringBuilder builder = new StringBuilder();

			builder.append("<?xml version=\"1.0\" encoding=\"GB2312\"?>");
			builder.append("<CMBC header=\"100\" version=\"100\" security=\"none\" lang=\"chs\" trnCode=\"CostReimb\">");
			builder.append("<requestHeader>");
			builder.append("<dtClient>" + minshengBank.getDtClient()
					+ "</dtClient>");
			builder.append("<clientId>" + minshengBank.getClientId()
					+ "</clientId>");
			builder.append("<userId>" + minshengBank.getUserId() + "</userId>");
			builder.append("<userPswd>" + minshengBank.getUserPswd()
					+ "</userPswd>");
			builder.append("<language>chs</language>");
			builder.append("<appId>nsbdes</appId>");
			builder.append("<appVer>201</appVer>");
			builder.append("</requestHeader>");
			builder.append("<xDataBody>");
			builder.append("<trnId>" + minshengBank.getTrnId() + "</trnId>");
			builder.append("<cltcookie></cltcookie>");
			builder.append("<insId>" + minshengBank.getInsId() + "</insId>");
			builder.append("<acntNo>" + minshengBank.getAcntNo() + "</acntNo>");
			builder.append("<acntToNo>" + minshengBank.getAcntToNo()
					+ "</acntToNo>");
			builder.append("<acntToName>" + minshengBank.getAcntToName()
					+ "</acntToName>");// 收款人名称
			builder.append("<payeeAcctType>" + minshengBank.getPayeeAcctType()
					+ "</payeeAcctType>");
			builder.append("<amount>" + minshengBank.getAmount() + "</amount>");
			builder.append("<explain>" + minshengBank.getSendExplain()
					+ "</explain>");
			builder.append("<actDate>" + minshengBank.getActDate()
					+ "</actDate>");
			builder.append("</xDataBody>");
			builder.append("</CMBC>");
			String xml = builder.toString();
			return xml;
		} catch (Exception e) {
			String str = "false";
			return str;
		}
	}

	/**
	 * 获取转账输入参数
	 */
	private String getParamterXml(MinshengBank minshengBank) {
		try {

			StringBuilder builder = new StringBuilder();

			builder.append("<?xml version=\"1.0\" encoding=\"GB2312\"?>");
			builder.append("<CMBC header=\"100\" version=\"100\" security=\"none\" lang=\"chs\" trnCode=\"Xfer\">");
			builder.append("<requestHeader>");
			builder.append("<dtClient>" + minshengBank.getDtClient()
					+ "</dtClient>");
			builder.append("<clientId>" + minshengBank.getClientId()
					+ "</clientId>");
			builder.append("<userId>" + minshengBank.getUserId() + "</userId>");
			builder.append("<userPswd>" + minshengBank.getUserPswd()
					+ "</userPswd>");
			builder.append("<language>chs</language>");
			builder.append("<appId>nsbdes</appId>");
			builder.append("<appVer>201</appVer>");
			builder.append("</requestHeader>");
			builder.append("<xDataBody>");
			builder.append("<trnId>" + minshengBank.getTrnId() + "</trnId>");
			builder.append("<cltcookie></cltcookie>");
			builder.append("<insId>" + minshengBank.getInsId() + "</insId>");
			builder.append("<acntNo>" + minshengBank.getAcntNo() + "</acntNo>");
			builder.append("<acntName>" + minshengBank.getAcntName()
					+ "</acntName>");
			builder.append("<acntToNo>" + minshengBank.getAcntToNo()
					+ "</acntToNo>");
			builder.append("<acntToName>" + minshengBank.getAcntToName()
					+ "</acntToName>");// 收款人名称
			builder.append("<externBank>" + minshengBank.getExternBank()
					+ "</externBank>");// 是否跨行
			builder.append("<localFlag>" + minshengBank.getLocalFlag()
					+ "</localFlag>");// 汇路：0:本地；1：异地；2:小额;3大额;4:上海同城;
										// 5:网银互联
			builder.append("<rcvCustType>" + minshengBank.getRcvCustType()
					+ "</rcvCustType>");
			builder.append("<bankCode>" + minshengBank.getBankCode()
					+ "</bankCode>");
			builder.append("<bankName>" + minshengBank.getBankName()
					+ "</bankName>");// 收款人开户行名称
			builder.append("<bankAddr>" + minshengBank.getBankAddr()
					+ "</bankAddr>");// 收款人开户行地址
			builder.append("<areaCode>" + minshengBank.getareaCode()
					+ "</areaCode>");
			builder.append("<amount>" + minshengBank.getAmount() + "</amount>");
			builder.append("<explain>" + minshengBank.getSendExplain()
					+ "</explain>");
			builder.append("<actDate>" + minshengBank.getActDate()
					+ "</actDate>");
			builder.append("</xDataBody>");
			builder.append("</CMBC>");
			String xml = builder.toString();
			return xml;
		} catch (Exception e) {
			String str = "false";
			return str;
		}
	}

	/**
	 * 获取查询转账参数
	 * 
	 * @param minshengBank
	 * @return
	 */
	private String getParamterQueryXml(MinshengBank minshengBank) {
		StringBuilder builder = new StringBuilder();
		builder.append("<CMBC header=\"100\" version=\"100\" security=\"none\" lang=\"chs\" trnCode=\"qryXfer\">");
		builder.append("<requestHeader>");
		builder.append("<dtClient>" + minshengBank.getDtClient()
				+ "</dtClient>");
		builder.append("<clientId>" + minshengBank.getClientId()
				+ "</clientId>");
		builder.append("<userId>" + minshengBank.getUserId() + "</userId>");
		builder.append("<userPswd>" + minshengBank.getUserPswd()
				+ "</userPswd>");
		builder.append("<language>chs</language>");
		builder.append("<appId>nsbdes</appId>");
		builder.append("<appVer>201</appVer>");
		builder.append("</requestHeader>");
		builder.append("<xDataBody>");
		builder.append("<trnId>" + minshengBank.getTrnId() + "</trnId>");
		builder.append("<cltcookie></cltcookie>");
		builder.append("<insId>" + minshengBank.getInsId() + "</insId>");
		builder.append("<svrId>" + minshengBank.getSvrId() + "</svrId>");
		builder.append("</xDataBody>");
		builder.append("</CMBC>");

		String xml = builder.toString();
		return xml;
	}

	/**
	 * 民生银行转账报文组织
	 */
	@Override
	public Message sendBankmsg(String sn, Credit credit) {
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		Map<String, Object> data = new HashMap<String, Object>();
		String resp = "";

		try {

			// 初始化
			SimpleDateFormat dtClientdate = new SimpleDateFormat(
					"YYYY-MM-DD HH:MM:SS");
			SimpleDateFormat actDate = new SimpleDateFormat("YYYY-MM-DD");
			MinshengBank minshengBank = new MinshengBank();
			DecimalFormat df = new DecimalFormat("#.00");
			BigDecimal transAmount = new BigDecimal(df.format(
					credit.getAmount()).toString());
			BigDecimal limitL = new BigDecimal("50000.00");
			// 对象实现
			minshengBank.setDtClient(dtClientdate.format(new java.util.Date()));// 客户端日期时间，YYYY-MM-DD
																				// HH:MM:SS（★）
			minshengBank.setClientId(new String(Base64.decodeBase64(bundle
					.getString("dtClient")), "UTF-8"));// 企业客户号
			minshengBank.setUserId(new String(Base64.decodeBase64(bundle
					.getString("userId")), "UTF-8"));// 登录用户号
			minshengBank.setUserPswd(new String(Base64.decodeBase64(bundle
					.getString("userPswd")), "UTF-8"));
			minshengBank.setSendLanguage("chs");
			minshengBank.setAppId("nsbdes");
			minshengBank.setAppVer("201");
			minshengBank.setTrnId(getip());
			minshengBank.setCltcookie("");
			// minshengBank.setInsId(UUID.randomUUID().toString());
			minshengBank.setInsId(credit.getSn());
			minshengBank.setOrderNo(credit.getSn());
			minshengBank.setAcntNo(new String(Base64.decodeBase64(bundle
					.getString("acntNo")), "UTF-8"));
			minshengBank.setAcntName(new String(Base64.decodeBase64(bundle
					.getString("acntName")), "UTF-8"));
			minshengBank.setAcntToNo(credit.getAccount());
			minshengBank.setAcntToName(credit.getPayer());
			// 是否跨行 305为民生银行首字母
			String arg = "305";
			if (credit.getBankCode().substring(0, 3).equals(arg)) {
				minshengBank.setExternBank("0");
			} else {
				minshengBank.setExternBank("1");
			}
			// 汇路判断 是否0本地 1异地 5网银互联只支持5W 同行转账只能是0或1
			String arg1 = "08";
			if (minshengBank.getExternBank().equals("0")) {
				if (credit.getBankCode().substring(7, 8).equals(arg)) {
					minshengBank.setLocalFlag("0");
				} else {
					minshengBank.setLocalFlag("1");
				}
			} else {
				minshengBank.setLocalFlag("5");
			}
			// 是否对公对私 1对公 2对私
			if (credit.getAcntToName() == credit.getMember().getTenant()
					.getName()) {
				minshengBank.setRcvCustType("1");
			} else {
				minshengBank.setRcvCustType("2");
			}
			minshengBank.setBankName(credit.getBankName());
			minshengBank.setBankCode(credit.getBankCode());
			minshengBank.setBankAddr(credit.getBankAddr());
			minshengBank.setareaCode("");
			// 交易金额限制5W
			if (transAmount.compareTo(limitL) == -1
					|| transAmount.compareTo(limitL) == 0) {
				minshengBank.setAmount(transAmount);
			} else {
				credit.setStatus(Credit.Status.wait_failure);
				creditDao.merge(credit);
				credit.setMemo("交易失败,提现金额大于50000上限");
				return Message.error("交易失败,提现金额大于50000上限 ");
			}
			minshengBank.setSendExplain("找汽配");
			minshengBank.setActDate(actDate.format(new java.util.Date()));

			minshengbankDao.persist(minshengBank);
			credit.setStatus(Credit.Status.wait_success);
			credit.setMemo("交易正在处理中,未知交易状态，请稍后再次查询. ");
			creditDao.merge(credit);
			return Message.success(getParamterXml(minshengBank));
		} catch (Exception e) {
			credit.setStatus(Credit.Status.wait_success);
			credit.setMemo("交易失败，交易出现未知异常 ，请稍候重试: " + resp);
			creditDao.merge(credit);
			return Message.error("交易出现未知异常，请稍候重试: " + resp);
		}
	}

	/**
	 * 民生银行查询接口
	 */
	@Override
	public Message queryToBank(String sn, Credit credit) {
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		try {
			SimpleDateFormat dtClientdate = new SimpleDateFormat(
					"YYYY-MM-DD HH:MM:SS");
			MinshengBank minshengBank = new MinshengBank();

			minshengBank.setDtClient(dtClientdate.format(new java.util.Date()));// 客户端日期时间，YYYY-MM-DD
																				// HH:MM:SS（★）
			minshengBank.setClientId(new String(Base64.decodeBase64(bundle
					.getString("dtClient")), "UTF-8"));// 企业客户号
			minshengBank.setUserId(new String(Base64.decodeBase64(bundle
					.getString("userId")), "UTF-8"));// 登录用户号
			minshengBank.setUserPswd(new String(Base64.decodeBase64(bundle
					.getString("userPswd")), "UTF-8"));
			minshengBank.setSendLanguage("chs");
			minshengBank.setAppId("nsbdes");
			minshengBank.setAppVer("201");
			minshengBank.setTrnId(getip());
			minshengBank.setCltcookie("");
			minshengBank.setInsId(credit.getSn());
			minshengBank.setSvrId(getip() + credit.getSn());
			return Message.success(getParamterQueryXml(minshengBank));
		} catch (Exception e) {
			credit.setStatus(Credit.Status.wait_success);
			credit.setMemo("交易失败，交易出现未知异常");
			creditDao.merge(credit);
			return Message.error("交易出现未知异常，请稍候重试");
		}
	}

	/**
	 * 民生银行单笔费用报销报文组织
	 */
	@Override
	public Message sendCostReimbmsg(String sn, Credit credit,String sbankcode,String sname) {
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		String resp = "";
		try {
			SimpleDateFormat dtClientdate = new SimpleDateFormat(
					"YYYY-MM-DD HH:MM:SS");
			SimpleDateFormat actDate = new SimpleDateFormat("YYYY-MM-DD");
			MinshengBank minshengBank = new MinshengBank();
			DecimalFormat df = new DecimalFormat("#.00");
			BigDecimal transAmount = new BigDecimal(df.format(
					credit.getAmount()).toString());
			BigDecimal limitL = new BigDecimal("50000.00");
			// 对象实现
			minshengBank.setDtClient(dtClientdate.format(new java.util.Date()));// 客户端日期时间，YYYY-MM-DD
																				// HH:MM:SS（★）
			minshengBank.setClientId(new String(Base64.decodeBase64(bundle
					.getString("dtClient")), "UTF-8"));// 企业客户号
			minshengBank.setUserId(new String(Base64.decodeBase64(bundle
					.getString("userId")), "UTF-8"));// 登录用户号
			minshengBank.setUserPswd(new String(Base64.decodeBase64(bundle
					.getString("userPswd")), "UTF-8"));
			minshengBank.setSendLanguage("chs");
			minshengBank.setAppId("nsbdes");
			minshengBank.setAppVer("201");
			minshengBank.setTrnId(getip());
			// minshengBank.setInsId(UUID.randomUUID().toString());
			minshengBank.setInsId(credit.getSn());
			minshengBank.setOrderNo(credit.getSn());
			minshengBank.setAcntNo(new String(Base64.decodeBase64(bundle
					.getString("acntNo")), "UTF-8"));
			if (sbankcode=="") {
			minshengBank.setAcntToNo(credit.getAccount());
			minshengBank.setAcntToName(credit.getPayer());
			}
			else
			{
				minshengBank.setAcntToNo(sbankcode);
				minshengBank.setAcntToName(sname);
			}
			minshengBank.setPayeeAcctType("1");
			// 交易金额限制5W
			if (transAmount.compareTo(limitL) == -1
					|| transAmount.compareTo(limitL) == 0) {
				minshengBank.setAmount(transAmount);
			} else {
				credit.setStatus(Credit.Status.wait_failure);
				creditDao.merge(credit);
				credit.setMemo("交易失败,提现金额大于50000上限");
				return Message.error("交易失败,提现金额大于50000上限 ");
			}
			minshengBank.setSendExplain("379");
			minshengbankDao.persist(minshengBank);
			credit.setStatus(Credit.Status.wait_success);
			credit.setMemo("交易正在处理中,未知交易状态，请稍后再次查询. ");
			creditDao.merge(credit);
			return Message.success(getCostParamterXml(minshengBank));
		} catch (Exception e) {
			credit.setStatus(Credit.Status.wait_success);
			credit.setMemo("交易失败，交易出现未知异常 ，请稍候重试: " + resp);
			creditDao.merge(credit);
			return Message.error("交易出现未知异常，请稍候重试: " + resp);
		}
	}

	/**
	 * 民生银行回执保存接口
	 */
	@Override
	public Message receiveBank(MinshengBank minshengBank, Credit credit,
			String msgtype, String receivetype) {
		if (msgtype == "error") {
			//creditDao.merge(credit);
			return Message.error("error");
		}
		try {
			if ("W6191".equals(minshengBank.getResCode())) {
				minshengbankDao.merge(minshengBank);
				// 转帐交易失败,可以转账
				return Message.success(minshengBank.getRrsmessage(),
						minshengBank);
			}else if ("W3317".equals(minshengBank.getResCode())){
				credit.setStatus(Credit.Status.wait_failure);
				credit.setMemo("该客户不存在，交易状态未知，请手工检查交易情况");
				creditDao.merge(credit);
				minshengbankDao.merge(minshengBank);
				// 其他状态
				return Message.error("该客户不存在，交易状态未知，请手工检查交易情况 ");
			}
			else if ("E1602".equals(minshengBank.getResCode())) {
				minshengbankDao.merge(minshengBank);
				//if (receivetype=="pay"){
				credit.setStatus(Credit.Status.wait);
				credit.setMemo("银行未查询到该笔交易,请人工处理:"
						+ minshengBank.getRrsmessage());
				//}
				//else{
				//	credit.setStatus(Credit.Status.wait_failure);
				//	credit.setMemo("银行账户不符:"
				//			+ minshengBank.getRrsmessage());
				//}
				creditDao.merge(credit);
				return Message.success("E1602", minshengBank.getRrsmessage());
			} else if ("0".equals(minshengBank.getResCode())) {
				try {
					if ("0".equals(minshengBank.getResstatusCode())) {
						minshengbankDao.merge(minshengBank);
						credit.setStatus(Credit.Status.success);
						credit.setMemo("转账成功:" + minshengBank.getRrsmessage());
						creditDao.merge(credit);

						return Message.error("0",
								minshengBank.getResstatusErrMsg());
					} else if ("2".equals(minshengBank.getResstatusCode())) {

						credit.setStatus(Credit.Status.wait_failure);
						credit.setMemo("交易失败待退款->"
								+ minshengBank.getResstatusSeverity()
								+ minshengBank.getResstatusErrMsg());
						creditDao.merge(credit);
						minshengbankDao.merge(minshengBank);
						// 其他状态
						return Message.error("查询交易失败，请稍候重试或联系银行: "
								+ minshengBank.getResstatusErrMsg());
					} else if ("3".equals(minshengBank.getResstatusCode())) {

						credit.setStatus(Credit.Status.wait_success);
						credit.setMemo("转账因网络原因失败，交易状态未知，请稍后再试："
								+ minshengBank.getResstatusSeverity()
								+ minshengBank.getResstatusErrMsg());
						creditDao.merge(credit);
						minshengbankDao.merge(minshengBank);
						// 其他状态
						return Message.error("转账因网络原因失败，交易状态未知，请稍后再试: "
								+ minshengBank.getResstatusErrMsg());
					} else {

						credit.setStatus(Credit.Status.wait_success);
						credit.setMemo("原交易处理中,请稍后再检查交易状态："
								+ minshengBank.getResstatusSeverity()
								+ minshengBank.getResstatusErrMsg());
						creditDao.merge(credit);
						minshengbankDao.merge(minshengBank);
						// 其他状态
						return Message.error("原交易处理中,请稍后再检查交易状态: "
								+ minshengBank.getResstatusErrMsg());
					}
				} catch (Exception e) {
					credit.setStatus(Credit.Status.wait_success);
					credit.setMemo("原交易处理中,请稍后再检查交易状态："
							+ minshengBank.getResseverity()
							+ minshengBank.getRrsmessage());
					creditDao.merge(credit);
					minshengbankDao.merge(minshengBank);
					// 其他状态
					return Message.error("原交易处理中,请稍后再检查交易状态: "
							+ minshengBank.getRrsmessage());
				}
			} else if ("WYHL01".equals(minshengBank.getResCode())) {
				minshengbankDao.merge(minshengBank);
				credit.setStatus(Credit.Status.wait_success);
				credit.setMemo("银行交易状态暂未提示到账情况,请稍后手动检查到账情况:"
						+ minshengBank.getRrsmessage());
				creditDao.merge(credit);
				return Message.success("WYHL01", minshengBank.getRrsmessage());
			} else {
				credit.setStatus(Credit.Status.wait_failure);
				credit.setMemo("交易失败待退款" + minshengBank.getRrsmessage());
				creditDao.merge(credit);
				minshengbankDao.merge(minshengBank);
				// 其他状态
				return Message.error("查询交易失败，请稍候重试或联系银行: "
						+ minshengBank.getRrsmessage());
			}
		} catch (Exception e) {
			credit.setStatus(Credit.Status.wait_success);
			credit.setMemo("交易失败，交易出现未知异常");
			creditDao.merge(credit);
			return Message.error("交易出现未知异常，请稍候重试: "
					+ minshengBank.getRrsmessage());
		}
	}

	/**
	 * 根据订单号查询实体
	 */
	@Override
	public MinshengBank findMinshengbyNo(String sn) {
		try {
			MinshengBank minshengBank = minshengbankDao.findbyMinshengPayNo(sn);
			return minshengBank;
		} catch (Exception e) {
			return null;
		}
	}

	// /**
	// * 民生银行转账接口
	// */
	// @Override
	// public Message payToBank(String sn, Credit credit) {
	// ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
	// String resp = "";
	// try {
	//
	// // String baseString = Base64.encodeBase64String(bundle.getString(
	// // "acntNo").getBytes("UTF-8"));
	// // String baseString1 = Base64.encodeBase64String(bundle.getString(
	// // "acntName").getBytes("UTF-8"));
	// // String baseString2 = Base64.encodeBase64String(bundle.getString(
	// // "dtClient").getBytes("UTF-8"));
	// // String baseString3 = Base64.encodeBase64String(bundle.getString(
	// // "userId").getBytes("UTF-8"));
	// // String baseString4 = Base64.encodeBase64String(bundle.getString(
	// // "userPswd").getBytes("UTF-8"));
	// //
	// // System.out.println("" + bundle.getString("acntNo") + ""
	// // + "的Base64编码为:" + baseString);
	// // System.out.println("" + bundle.getString("acntName") + ""
	// // + "的Base64编码为:" + baseString1);
	// // System.out.println("" + bundle.getString("dtClient") + ""
	// // + "的Base64编码为:" + baseString2);
	// // System.out.println("" + bundle.getString("userId") + ""
	// // + "的Base64编码为:" + baseString3);
	// // System.out.println("" + bundle.getString("userPswd") + ""
	// // + "的Base64编码为:" + baseString4);
	//
	// // String base64Str1 = bundle.getString("acntNo");
	// // byte[] bytes1 = Base64.decodeBase64(base64Str1);
	// // System.out.println("解码后:" + new String(bytes1, "UTF-8"));
	//
	// // Message msg = findbyMinshengPayNo(credit.getSn());
	// // 查询数据库内是否付过款项目
	// // if (msg.getType().equals(Message.Type.success)) {
	//
	// // 初始化
	// SimpleDateFormat dtClientdate = new SimpleDateFormat(
	// "YYYY-MM-DD HH:MM:SS");
	// SimpleDateFormat actDate = new SimpleDateFormat("YYYY-MM-DD");
	// MinshengBank minshengBank = new MinshengBank();
	// DecimalFormat df = new DecimalFormat("#.00");
	// BigDecimal transAmount = new BigDecimal(df.format(
	// credit.getAmount()).toString());
	// BigDecimal limitL = new BigDecimal("50000.00");
	// // 对象实现
	// minshengBank.setDtClient(dtClientdate.format(new java.util.Date()));//
	// 客户端日期时间，YYYY-MM-DD
	// // HH:MM:SS（★）
	// minshengBank.setClientId(new String(Base64.decodeBase64(bundle
	// .getString("dtClient")), "UTF-8"));// 企业客户号
	// minshengBank.setUserId(new String(Base64.decodeBase64(bundle
	// .getString("userId")), "UTF-8"));// 登录用户号
	// minshengBank.setUserPswd(new String(Base64.decodeBase64(bundle
	// .getString("userPswd")), "UTF-8"));
	// minshengBank.setSendLanguage("chs");
	// minshengBank.setAppId("nsbdes");
	// minshengBank.setAppVer("201");
	// minshengBank.setTrnId(getip());
	// minshengBank.setCltcookie("");
	// // minshengBank.setInsId(UUID.randomUUID().toString());
	// minshengBank.setInsId(credit.getSn());
	// minshengBank.setOrderNo(credit.getSn());
	// minshengBank.setAcntNo(new String(Base64.decodeBase64(bundle
	// .getString("acntNo")), "UTF-8"));
	// minshengBank.setAcntName(new String(Base64.decodeBase64(bundle
	// .getString("acntName")), "UTF-8"));
	// minshengBank.setAcntToNo(credit.getAccount());
	// minshengBank.setAcntToName(credit.getPayer());
	// // 是否跨行 305为民生银行首字母
	// String arg = "305";
	// if (credit.getBankCode().substring(0, 3).equals(arg)) {
	// minshengBank.setExternBank("0");
	// } else {
	// minshengBank.setExternBank("1");
	// }
	// // 汇路判断 是否0本地 1异地 5网银互联只支持5W 同行转账只能是0或1
	// String arg1 = "08";
	// if (minshengBank.getExternBank().equals("0")) {
	// if (credit.getBankCode().substring(7, 8).equals(arg)) {
	// minshengBank.setLocalFlag("0");
	// } else {
	// minshengBank.setLocalFlag("1");
	// }
	// } else {
	// minshengBank.setLocalFlag("5");
	// }
	// // 是否对公对私 1对公 2对私
	// if (credit.getAcntToName() == credit.getMember().getTenant()
	// .getName()) {
	// minshengBank.setRcvCustType("1");
	// } else {
	// minshengBank.setRcvCustType("2");
	// }
	// minshengBank.setBankName(credit.getBankName());
	// minshengBank.setBankCode(credit.getBankCode());
	// minshengBank.setBankAddr(credit.getBankAddr());
	// minshengBank.setareaCode("");
	// // 交易金额限制5W
	// if (transAmount.compareTo(limitL) == -1
	// || transAmount.compareTo(limitL) == 0) {
	// minshengBank.setAmount(transAmount);
	// } else {
	// credit.setStatus(Credit.Status.wait_failure);
	// creditDao.merge(credit);
	// credit.setMemo("交易失败,提现金额大于50000上限");
	// return Message.error("交易失败,提现金额大于50000上限 ");
	// }
	// minshengBank.setSendExplain("找汽配提现");
	// minshengBank.setActDate(actDate.format(new java.util.Date()));
	// resp = sendPostMessage(getParamterXml(minshengBank), "GB2312",
	// getRequestUrl());
	// System.out.println("-->>" + resp);
	// if (resp != null) {
	// Document doxml = XmlUtil.getDocumentHelper(resp);
	// minshengBank.setResCode(doxml.getRootElement()
	// .element("responseHeader").element("status")
	// .elementText("code"));
	// minshengBank.setResseverity(doxml.getRootElement()
	// .element("responseHeader").element("status")
	// .elementText("severity"));
	// minshengBank.setResmessage(doxml.getRootElement()
	// .element("responseHeader").element("status")
	// .elementText("message"));
	// minshengBank.setRessvrId(doxml.getRootElement()
	// .element("responseHeader").element("status")
	// .elementText("svrId"));
	// minshengBank.setRestrnId(doxml.getRootElement()
	// .element("responseHeader").element("status")
	// .elementText("trnId"));
	// minshengBank.setResinsId(doxml.getRootElement()
	// .element("responseHeader").element("status")
	// .elementText("insId"));
	// minshengBank.setResdtDead(doxml.getRootElement()
	// .element("responseHeader").element("status")
	// .elementText("dtDead"));
	// minshengBank.setRescltcookie(doxml.getRootElement()
	// .element("responseHeader").element("status")
	// .elementText("insId"));
	// minshengBank.setResdtServer(doxml.getRootElement()
	// .element("responseHeader").element("status")
	// .elementText("dtServer"));
	// minshengBank.setResLanguage(doxml.getRootElement()
	// .element("responseHeader").element("status")
	// .elementText("language"));
	// credit.setStatus(Credit.Status.failure);
	// minshengbankDao.persist(minshengBank);
	// if ("0".equals(minshengBank.getResCode())) {
	// credit.setCreditDate(new Date());
	// credit.setStatus(Credit.Status.success);
	// credit.setMemo("交易成功" + minshengBank.getRrsmessage());
	// creditDao.merge(credit);
	// return Message.success(resp);
	// } else if ("WYHL01".equals(minshengBank.getResCode())) {
	// minshengbankDao.merge(minshengBank);
	// credit.setStatus(Credit.Status.wait_success);
	// credit.setMemo("银行交易状态暂未提示到账情况,请稍后手动检查到账情况:"
	// + minshengBank.getRrsmessage());
	// creditDao.merge(credit);
	// return Message.success("WYHL01", resp);
	// } else {
	// creditDao.merge(credit);
	// credit.setMemo("交易失败" + minshengBank.getRrsmessage());
	// return Message.error("交易失败，请稍候重试: "
	// + minshengBank.getResCode()
	// + minshengBank.getRrsmessage());
	// }
	// } else {
	// credit.setStatus(Credit.Status.wait_success);
	// credit.setMemo("交易失败，交易出现未知异常");
	// creditDao.merge(credit);
	// return Message.error("交易出现未知异常，请稍候重试: ");
	// }
	// // } else {
	// // credit.setStatus(Credit.Status.wait_success);
	// // credit.setMemo("交易失败： " + msg.getContent());
	// // creditDao.merge(credit);
	// // return Message.error(msg.getContent());
	// // }
	// } catch (Exception e) {
	// credit.setStatus(Credit.Status.wait_success);
	// credit.setMemo("交易失败，交易出现未知异常 ，请稍候重试: " + resp);
	// creditDao.merge(credit);
	// return Message.error("交易出现未知异常，请稍候重试: " + resp);
	// }
	// }

	// /**
	// * 民生银行单笔费用报销接口
	// */
	// @Override
	// public Message CostReimbPay(String sn, Credit credit) {
	// ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
	// String resp = "";
	// try {
	// Message msg = findbyMinshengPayNo(credit.getSn());
	// // 查询数据库内是否付过款项目
	// if (msg.getType().equals(Message.Type.success)) {
	//
	// // 初始化
	// SimpleDateFormat dtClientdate = new SimpleDateFormat(
	// "YYYY-MM-DD HH:MM:SS");
	// SimpleDateFormat actDate = new SimpleDateFormat("YYYY-MM-DD");
	// MinshengBank minshengBank = new MinshengBank();
	// DecimalFormat df = new DecimalFormat("#.00");
	// BigDecimal transAmount = new BigDecimal(df.format(
	// credit.getAmount()).toString());
	// BigDecimal limitL = new BigDecimal("50000.00");
	// // 对象实现
	// minshengBank.setDtClient(dtClientdate
	// .format(new java.util.Date()));// 客户端日期时间，YYYY-MM-DD
	// // HH:MM:SS（★）
	// minshengBank.setClientId(new String(Base64.decodeBase64(bundle
	// .getString("dtClient")), "UTF-8"));// 企业客户号
	// minshengBank.setUserId(new String(Base64.decodeBase64(bundle
	// .getString("userId")), "UTF-8"));// 登录用户号
	// minshengBank.setUserPswd(new String(Base64.decodeBase64(bundle
	// .getString("userPswd")), "UTF-8"));
	// minshengBank.setSendLanguage("chs");
	// minshengBank.setAppId("nsbdes");
	// minshengBank.setAppVer("201");
	// minshengBank.setTrnId(getip());
	// // minshengBank.setInsId(UUID.randomUUID().toString());
	// minshengBank.setInsId(credit.getSn());
	// minshengBank.setOrderNo(credit.getSn());
	// minshengBank.setAcntNo(new String(Base64.decodeBase64(bundle
	// .getString("acntNo")), "UTF-8"));
	// minshengBank.setAcntToNo(credit.getAccount());
	// minshengBank.setAcntToName(credit.getPayer());
	// minshengBank.setPayeeAcctType(sn);
	// // 交易金额限制5W
	// if (transAmount.compareTo(limitL) == -1
	// || transAmount.compareTo(limitL) == 0) {
	// minshengBank.setAmount(transAmount);
	// } else {
	// credit.setStatus(Credit.Status.wait_failure);
	// creditDao.merge(credit);
	// credit.setMemo("交易失败,提现金额大于50000上限");
	// return Message.error("交易失败,提现金额大于50000上限 ");
	// }
	//
	// minshengBank.setSendExplain("379");
	// resp = sendPostMessage(getCostParamterXml(minshengBank),
	// "GB2312", getRequestUrl());
	// System.out.println("-->>" + resp);
	// if (resp != null) {
	// Document doxml = XmlUtil.getDocumentHelper(resp);
	// minshengBank.setResCode(doxml.getRootElement()
	// .element("responseHeader").element("status")
	// .elementText("code"));
	// minshengBank.setResseverity(doxml.getRootElement()
	// .element("responseHeader").element("status")
	// .elementText("severity"));
	// minshengBank.setResmessage(doxml.getRootElement()
	// .element("responseHeader").element("status")
	// .elementText("message"));
	// minshengBank.setRessvrId(doxml.getRootElement()
	// .element("responseHeader").element("status")
	// .elementText("svrId"));
	// minshengBank.setRestrnId(doxml.getRootElement()
	// .element("responseHeader").element("status")
	// .elementText("trnId"));
	// minshengBank.setResinsId(doxml.getRootElement()
	// .element("responseHeader").element("status")
	// .elementText("insId"));
	// minshengBank.setResdtDead(doxml.getRootElement()
	// .element("responseHeader").element("status")
	// .elementText("dtDead"));
	// minshengBank.setRescltcookie(doxml.getRootElement()
	// .element("responseHeader").element("status")
	// .elementText("insId"));
	// minshengBank.setResdtServer(doxml.getRootElement()
	// .element("responseHeader").element("status")
	// .elementText("dtServer"));
	// minshengBank.setResLanguage(doxml.getRootElement()
	// .element("responseHeader").element("status")
	// .elementText("language"));
	// credit.setStatus(Credit.Status.failure);
	// minshengbankDao.persist(minshengBank);
	// if ("0".equals(minshengBank.getResCode())) {
	// credit.setCreditDate(new Date());
	// credit.setStatus(Credit.Status.success);
	// credit.setMemo("交易成功" + minshengBank.getRrsmessage());
	// creditDao.merge(credit);
	// return Message.success(resp);
	// } else if ("WYHL01".equals(minshengBank.getResCode())) {
	// minshengbankDao.merge(minshengBank);
	// credit.setStatus(Credit.Status.wait_success);
	// credit.setMemo("银行交易状态暂未提示到账情况,请稍后手动检查到账情况:"
	// + minshengBank.getRrsmessage());
	// creditDao.merge(credit);
	// return Message.success("WYHL01", resp);
	// } else {
	// creditDao.merge(credit);
	// credit.setMemo("交易失败" + minshengBank.getRrsmessage());
	// return Message.error("交易失败，请稍候重试: "
	// + minshengBank.getResCode()
	// + minshengBank.getRrsmessage());
	// }
	// } else {
	// credit.setStatus(Credit.Status.wait_success);
	// credit.setMemo("交易失败，交易出现未知异常");
	// creditDao.merge(credit);
	// return Message.error("交易出现未知异常，请稍候重试: ");
	// }
	// } else {
	// credit.setStatus(Credit.Status.wait_success);
	// credit.setMemo("交易失败： " + msg.getContent());
	// creditDao.merge(credit);
	// return Message.error(msg.getContent());
	// }
	// } catch (Exception e) {
	//
	// credit.setStatus(Credit.Status.wait_success);
	// credit.setMemo("交易失败，交易出现未知异常");
	// creditDao.merge(credit);
	// return Message.error("交易出现未知异常，请稍候重试: " + resp);
	// }
	// }

	// /**
	// * 民生银行查询接口
	// */
	// @Override
	// public Message queryToBank(String sn, Credit credit) {
	// ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
	// String resp = "";
	// try {
	// SimpleDateFormat dtClientdate = new SimpleDateFormat(
	// "YYYY-MM-DD HH:MM:SS");
	// MinshengBank minshengBank = new MinshengBank();
	//
	// minshengBank.setDtClient(dtClientdate.format(new java.util.Date()));//
	// 客户端日期时间，YYYY-MM-DD
	// // HH:MM:SS（★）
	// minshengBank.setClientId(new String(Base64.decodeBase64(bundle
	// .getString("dtClient")), "UTF-8"));// 企业客户号
	// minshengBank.setUserId(new String(Base64.decodeBase64(bundle
	// .getString("userId")), "UTF-8"));// 登录用户号
	// minshengBank.setUserPswd(new String(Base64.decodeBase64(bundle
	// .getString("userPswd")), "UTF-8"));
	// minshengBank.setSendLanguage("chs");
	// minshengBank.setAppId("nsbdes");
	// minshengBank.setAppVer("201");
	// minshengBank.setTrnId(getip());
	// minshengBank.setCltcookie("");
	// minshengBank.setInsId(credit.getSn());
	// minshengBank.setSvrId(getip() + credit.getSn());
	// resp = sendPostMessage(getParamterQueryXml(minshengBank), "GB2312",
	// getRequestUrl());
	// System.out.println("-->>" + resp);
	// if (resp != null) {
	// Document doxml = XmlUtil.getDocumentHelper(resp);
	// minshengBank.setResCode(doxml.getRootElement()
	// .element("responseHeader").element("status")
	// .elementText("code"));
	// minshengBank.setResseverity(doxml.getRootElement()
	// .element("responseHeader").element("status")
	// .elementText("severity"));
	// minshengBank.setResmessage(doxml.getRootElement()
	// .element("responseHeader").element("status")
	// .elementText("message"));
	// minshengBank.setRessvrId(doxml.getRootElement()
	// .element("responseHeader").element("status")
	// .elementText("svrId"));
	// minshengBank.setRestrnId(doxml.getRootElement()
	// .element("responseHeader").element("status")
	// .elementText("trnId"));
	// minshengBank.setResinsId(doxml.getRootElement()
	// .element("responseHeader").element("status")
	// .elementText("insId"));
	// minshengBank.setResstatusCode(doxml.getRootElement()
	// .element("xDataBody").element("statusId")
	// .elementText("statusCode"));
	// minshengBank.setResstatusSeverity(doxml.getRootElement()
	// .element("xDataBody").element("statusId")
	// .elementText("statusSeverity"));
	// minshengBank.setResstatusErrMsg(doxml.getRootElement()
	// .element("xDataBody").element("statusId")
	// .elementText("statusErrMsg"));
	//
	// if ("W6191".equals(minshengBank.getResCode())) {
	// minshengbankDao.merge(minshengBank);
	// // 转帐交易失败,可以转账
	// return Message.success(resp, minshengBank);
	// } else if ("E1602".equals(minshengBank.getResCode())) {
	// minshengbankDao.merge(minshengBank);
	// credit.setStatus(Credit.Status.wait);
	// credit.setMemo("银行未查询到该笔交易,请人工处理:"
	// + minshengBank.getRrsmessage());
	// creditDao.merge(credit);
	// // minshengbankDao.merge(minshengBank);
	// // 此流水号不存在,请查证
	// return Message.success("E1602", resp);
	// } else if ("0".equals(minshengBank.getResCode())) {
	//
	// if ("0".equals(minshengBank.getResstatusCode())) {
	// minshengbankDao.merge(minshengBank);
	// credit.setStatus(Credit.Status.success);
	// credit.setMemo("转账成功:" + minshengBank.getRrsmessage());
	// creditDao.merge(credit);
	// // minshengbankDao.merge(minshengBank);
	// // 此流水号不存在,请查证
	// return Message.error("0", resp);
	// } else if ("2".equals(minshengBank.getResstatusCode())) {
	//
	// credit.setStatus(Credit.Status.failure);
	// credit.setMemo("交易失败"
	// + minshengBank.getResstatusSeverity()
	// + minshengBank.getResstatusErrMsg());
	// creditDao.merge(credit);
	// minshengbankDao.merge(minshengBank);
	// // 其他状态
	// return Message.error("查询交易失败，请稍候重试或联系银行: " + resp);
	// } else if ("3".equals(minshengBank.getResstatusCode())) {
	//
	// credit.setStatus(Credit.Status.wait_success);
	// credit.setMemo("转账因网络原因失败，交易状态未知，请稍后再试："
	// + minshengBank.getResstatusSeverity()
	// + minshengBank.getResstatusErrMsg());
	// creditDao.merge(credit);
	// minshengbankDao.merge(minshengBank);
	// // 其他状态
	// return Message.error("转账因网络原因失败，交易状态未知，请稍后再试: " + resp);
	// } else {
	//
	// credit.setStatus(Credit.Status.wait_success);
	// credit.setMemo("原交易处理中,请稍后再检查交易状态："
	// + minshengBank.getResstatusSeverity()
	// + minshengBank.getResstatusErrMsg());
	// creditDao.merge(credit);
	// minshengbankDao.merge(minshengBank);
	// // 其他状态
	// return Message.error("原交易处理中,请稍后再检查交易状态: " + resp);
	// }
	// } else if ("WYHL01".equals(minshengBank.getResCode())) {
	// minshengbankDao.merge(minshengBank);
	// credit.setStatus(Credit.Status.wait_success);
	// credit.setMemo("银行交易状态暂未提示到账情况,请稍后手动检查到账情况:"
	// + minshengBank.getRrsmessage());
	// creditDao.merge(credit);
	// return Message.success("WYHL01", resp);
	// } else {
	// credit.setStatus(Credit.Status.failure);
	// credit.setMemo("交易失败" + minshengBank.getRrsmessage());
	// creditDao.merge(credit);
	// minshengbankDao.merge(minshengBank);
	// // 其他状态
	// return Message.error("查询交易失败，请稍候重试或联系银行: " + resp);
	// }
	// } else {
	// credit.setStatus(Credit.Status.wait_success);
	// credit.setMemo("已提交银行,但未反馈成功信息" + minshengBank.getRrsmessage());
	// creditDao.merge(credit);
	// return Message.error("网络超时，请稍候重试: " + resp);
	// }
	// } catch (Exception e) {
	// credit.setStatus(Credit.Status.wait_success);
	// credit.setMemo("交易失败，交易出现未知异常");
	// creditDao.merge(credit);
	// return Message.error("交易出现未知异常，请稍候重试: " + resp);
	// }
	// }

	/**
	 * 保存与更新民生银行记录 输入：订单号
	 */
	public Message findbyMinshengPayNo(String sn) {
		try {
			MinshengBank minshengBank = minshengbankDao.findbyMinshengPayNo(sn);
			if (null == minshengBank) {
				return Message.success("未付款");
			}
			if (minshengBank.getResCode() == "0") {
				return Message.error("已付款");
			} else {
				return Message.success("未成功款");
			}
		} catch (Exception e) {
			return Message.error("付款状态查询失败");
		}
	}

	/**
	 * Http post方法
	 * 
	 * @param xmlString
	 * @param encode
	 * @param sendurl
	 * @return
	 */
	public static String sendPostMessage(String xmlString, String encode,
			String sendurl) {
		// 关闭
		System.setProperty("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime",
				"true");
		System.setProperty(
				"org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient",
				"stdout");
		System.out.println(sendurl + "-->>" + xmlString);
		// 创建httpclient工具对象
		HttpClient client = new HttpClient();
		// 创建post请求方法
		PostMethod myPost = new PostMethod(sendurl);
		// 设置请求超时时间
		client.setConnectionTimeout(300 * 1000);
		String responseString = null;
		try {
			// 设置请求头部类型
			myPost.setRequestHeader("Content-Type", "text/xml");
			myPost.setRequestHeader("charset", encode);

			// 设置请求体，即xml文本内容，注：这里写了两种方式，一种是直接获取xml内容字符串，一种是读取xml文件以流的形式
			myPost.setRequestBody(xmlString);
			// InputStream
			// body=this.getClass().getResourceAsStream("/"+xmlFileName);
			// myPost.setRequestBody(body);
			myPost.setRequestEntity(new StringRequestEntity(xmlString,
					"text/xml", encode));
			int statusCode = client.executeMethod(myPost);
			if (statusCode == HttpStatus.SC_OK) {
				BufferedInputStream bis = new BufferedInputStream(
						myPost.getResponseBodyAsStream());
				byte[] bytes = new byte[1024];
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				int count = 0;
				while ((count = bis.read(bytes)) != -1) {
					bos.write(bytes, 0, count);
				}
				byte[] strByte = bos.toByteArray();
				responseString = new String(strByte, 0, strByte.length, encode);
				bos.close();
				bis.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		myPost.releaseConnection();
		return responseString;

	}

	/**
	 * 用传统的URI类进行请求
	 * 
	 * @param urlStr
	 */
	public void testPost(String urlStr, String xmlInfo, String encode) {
		try {
			URL url = new URL(urlStr);
			URLConnection con = url.openConnection();
			con.setDoOutput(true);
			con.setRequestProperty("Pragma:", "no-cache");
			con.setRequestProperty("Cache-Control", "no-cache");
			con.setRequestProperty("Content-Type", "text/xml");

			OutputStreamWriter out = new OutputStreamWriter(
					con.getOutputStream());
			// System.out.println("urlStr=" + urlStr);
			// System.out.println("xmlInfo=" + xmlInfo);
			out.write(new String(xmlInfo.getBytes(encode)));
			out.flush();
			out.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String line = "";
			for (line = br.readLine(); line != null; line = br.readLine()) {
				// System.out.println(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
