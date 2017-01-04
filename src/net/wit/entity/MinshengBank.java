package net.wit.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @ClassName: PayBank
 * @Description: 民生银行
 * @author Administrator
 * @date 2014年10月13日 下午3:02:51
 */
@Entity
@Table(name = "xx_minsheng_bank")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_minsheng_bank_sequence")
public class MinshengBank extends BaseEntity {

	private static final long serialVersionUID = -248735963897951159L;

	/** 客户端日期时间，YYYY-MM-DD HH:MM:SS（★） */
	private String dtClient;
	
	/** 企业客户号（★）*/
	private String clientId;
	
	/** 登录用户号（★）*/
	private String userId;
	
	/** 登录密码（★）*/
	private String userPswd;
	
	/** 希望服务器响应信息使用的语言，目前仅考虑chs(中文简体)，可选*/
	private String sendlanguage;
	
	/** 应用程序编码，目前取nsbdes*/
	private String appId;
	
	/** 应用程序版本nnn，目前取201*/
	private String appVer;
	
	/** 编号 */
	@Column(updatable = false, length = 100)
	private String orderNo;
	
	/**报文体 本次交易的客户端id  */
	private String trnid;
	
	/**报文体 客户端cookie，可选  */
	private String cltcookie;
	
	/**指令ID，一条转帐指令在客户端的唯一标识（★）  */
	private String insId;
	
	/**付款账号（★）  */
	private String acntNo;
	
	/**付款人名称(可选) （★）  */
	private String acntName;
	
	/**收款人账号 （★）  */
	private String acntToNo;
	
	/**收款人名称（★）  */
	private String acntToName;
	
	/**是否跨行,0:同行；1：跨行；（★）  */
	private String externBank;
	
	/**汇路：0:本地；1：异地；2:小额;3大额;4:上海同城; 5:网银互联  */
	private String localFlag;
	
	/**收款人账户类型：1:对公；2:对私；(上海同城汇路必填)
（注：
当选择同行转账时，汇路仅0、1有效，其中0代表民生银行本地、1代表民生银行异地；
当选择跨行转账时，汇路0代表同城票交，目前我行部分地区已关闭；汇路1代表异地汇划汇路，实质为落地的大小额汇路；汇路2代表不落地小额汇路；汇路3代表不落地大额汇路；汇路4：代表上海同城；汇路5： 网银互联（由于网银互联汇路转账是异步处理操作，无法即时得出最终的转账结果,所以需要通过qryXfer接口进行对账得出人行最终转账结果）
注意：0、1异地汇划汇路不需要要求指定行号，仅提供行名即可；2、3大小额汇路必须提供完整的行名行号；5 网银互联汇路必须提供完整的行名行号）
 */
	private String rcvCustType;
	
	/**收款人开户行行号(大/小额,上海同城汇路此项必填)  */
	private String bankCode;
	
	/**收款人开户行名称（★） */
	private String bankName;
	
	/**收款人开户行地址 */
	private String bankAddr;
	
	/**收款行地区编号(可选) */
	private String areaCode;
	
	/** 转账金额 */
	@Column(precision = 15, scale = 2)
	private BigDecimal amount;
	
	/**摘要/用途（★） */
	private String sendexplain;
	
	/**要求的转帐日期 YYYY-MM-DD(上海同城汇路是实时转账，此项无用) */
	private String actDate;
	
	/**
	 * 1--卡 2-- 活折:收款账户类型
	 */
	private String payeeAcctType;
	
	/** 响应 状态码code：0：成功；
    WEC32：当是查询交易表示查询条件错误，一般指起始记录数<=0;
     WEC02:转账的时候出现网络异常，具体转账成败未知；
     其他：失败代码，表示交易失败。
	 */
	private String rescode;

	/**响应 安全信息*/
	private String resseverity;
	
	/**响应 描述信息：ok，交易成功。
         其他：交易描述
	 */
	private String resmessage;
	
	
	/**
	 * 转账状态码 0成功；2失败；3网络问题。
	 */
	private String resstatusCode;
	
	/**
	 * 转账交易返回状态
	 */
	private String resstatusSeverity;
	
	/**
	 * 转账交易返回消息
	 */
	private String resstatusErrMsg;
	
	
	
	/**服务器该笔交易的标识（★）*/
	private String svrId;
	
	/**响应 服务端日期时间，YYYY-MM-DD HH:MM:SS（★）*/
	private String resdtServer;
	
	/**响应 userkey的有效时间(服务器时间)*/
	private String resdtDead;
	
	/**响应 服务器信息使用的语言，目前仅提供chs(中文简体)，可选*/
	private String resLanguage;
	
	/**响应 客户端交易的唯一标志（★）*/
	private String restrnId;

	/**响应 如果客户端发送cookie，同步的历史记录不包括原有的cltcookie（★）*/
	private String rescltcookie;
	
	/**响应 服务器该笔交易的标识（★）*/
	private String ressvrId;
	
	/**响应 指令ID，请求时给出的ID（★）*/
	private String resinsId;
	
	/**响应 余额*/
	@Column(precision = 21, scale = 6)
	private BigDecimal balance;
	
	/** 交易手续费 */
	@Column(precision = 21, scale = 6)
	private BigDecimal serviceFee;

	/** 绑定的会员 */
	@ManyToOne
	private Member member;

	// ===========================================getter/setter===========================================//
	public String getDtClient() {
		return dtClient;
	}

	public void setDtClient(String dtClient) {
		this.dtClient = dtClient;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPswd() {
		return userPswd;
	}

	public void setUserPswd(String userPswd) {
		this.userPswd = userPswd;
	}

	public String getSendLanguage() {
		return sendlanguage;
	}

	public void setSendLanguage(String sendlanguage) {
		this.sendlanguage = sendlanguage;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppVer() {
		return appVer;
	}

	public void setAppVer(String appVer) {
		this.appVer = appVer;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	public String getTrnId() {
		return trnid;
	}

	public void setTrnId(String trnid) {
		this.trnid = trnid;
	}

	public String getCltcookie() {
		return cltcookie;
	}

	public void setCltcookie(String cltcookie) {
		this.cltcookie = cltcookie;
	}

	public String getInsId() {
		return insId;
	}

	public void setInsId(String insId) {
		this.insId = insId;
	}

	public String getAcntNo() {
		return acntNo;
	}

	public void setAcntNo(String acntNo) {
		this.acntNo = acntNo;
	}

	public String getAcntName() {
		return acntName;
	}

	public void setAcntName(String acntName) {
		this.acntName = acntName;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getAcntToNo() {
		return acntToNo;
	}

	public void setAcntToNo(String acntToNo) {
		this.acntToNo = acntToNo;
	}

	public String getAcntToName() {
		return acntToName;
	}

	public void setAcntToName(String acntToName) {
		this.acntToName = acntToName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getExternBank() {
		return externBank;
	}

	public void setExternBank(String externBank) {
		this.externBank = externBank;
	}

	public String getLocalFlag() {
		return localFlag;
	}

	public void setLocalFlag(String localFlag) {
		this.localFlag = localFlag;
	}

	public String getRcvCustType() {
		return rcvCustType;
	}

	public void setRcvCustType(String rcvCustType) {
		this.rcvCustType = rcvCustType;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAddr() {
		return bankAddr;
	}

	public void setBankAddr(String bankAddr) {
		this.bankAddr = bankAddr;
	}

	public String getareaCode() {
		return areaCode;
	}

	public void setareaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getSendExplain() {
		return sendexplain;
	}

	public void setSendExplain(String sendexplain) {
		this.sendexplain = sendexplain;
	}

	public String getActDate() {
		return actDate;
	}

	public void setActDate(String actDate) {
		this.actDate = actDate;
	}
	
	public String getPayeeAcctType() {
		return payeeAcctType;
	}

	public void setPayeeAcctType(String payeeAcctType) {
		this.payeeAcctType = payeeAcctType;
	}
	
	public String getResCode() {
		return rescode;
	}
	
	public void setResCode(String rescode) {
		this.rescode = rescode;
	}

	public String getResseverity() {
		return resseverity;
	}

	public void setResseverity(String resseverity) {
		this.resseverity = resseverity;
	}

	public String getRrsmessage() {
		return resmessage;
	}

	public void setResmessage(String resmessage) {
		this.resmessage = resmessage;
	}


	
	public String getResstatusCode() {
		return resstatusCode;
	}

	public void setResstatusCode(String resstatusCode) {
		this.resstatusCode = resstatusCode;
	}
	
	public String getResstatusSeverity() {
		return resstatusSeverity;
	}

	public void setResstatusSeverity(String resstatusSeverity) {
		this.resstatusSeverity = resstatusSeverity;
	}
	
	public String getResstatusErrMsg() {
		return resstatusErrMsg;
	}

	public void setResstatusErrMsg(String resstatusErrMsg) {
		this.resstatusErrMsg = resstatusErrMsg;
	}
	
	public String getResdtServer() {
		return resdtServer;
	}

	public void setResdtServer(String resdtServer) {
		this.resdtServer = resdtServer;
	}

	public String getResdtDead() {
		return resdtDead;
	}

	public void setResdtDead(String resdtDead) {
		this.resdtDead = resdtDead;
	}

	public String geResLanguage() {
		return resLanguage;
	}

	public void setResLanguage(String resLanguage) {
		this.resLanguage = resLanguage;
	}

	public String getRestrnId() {
		return restrnId;
	}

	public void setRestrnId(String restrnId) {
		this.restrnId = restrnId;
	}

	public String getRescltcookie() {
		return rescltcookie;
	}

	public void setRescltcookie(String rescltcookie) {
		this.rescltcookie = rescltcookie;
	}

	public String getRessvrId() {
		return ressvrId;
	}

	public void setRessvrId(String ressvrId) {
		this.ressvrId = ressvrId;
	}
	
	
	public String getResinsId() {
		return resinsId;
	}

	public void setResinsId(String resinsId) {
		this.resinsId = resinsId;
	}
	
	public String getSvrId() {
		return svrId;
	}

	public void setSvrId(String svrId) {
		this.svrId = svrId;
	}

	public BigDecimal getRalance() {
		return balance;
	}

	public void setRalance(BigDecimal balance) {
		this.balance = balance;
	}
	
	public BigDecimal getServiceFee() {
		return serviceFee;
	}
	
	public void setServiceFee(BigDecimal serviceFee) {
		this.serviceFee = serviceFee;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

}
