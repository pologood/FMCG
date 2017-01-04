/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.hibernate.validator.constraints.Length;
import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity -
 * @author rsico Team
 * @version 3.0
 */
@Entity
@Table(name = "xx_tenant_rules_role")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_tenant_rules_role_sequence")
public class TenantRulesRole extends BaseEntity {

	private static final long serialVersionUID = 1533130686714725835L;

	@CollectionTable(name = "xx_role")
	@ManyToOne(fetch= FetchType.EAGER)
	private Role role;

	@CollectionTable(name = "xx_tenant_rules")
	@ManyToOne(fetch = FetchType.EAGER)
	private TenantRules  rules;

	/*读权限 0:无权限  1：有权限*/
	@Length(max = 1)
	private boolean readAuthority;
	/*修改权限 0:无权限  1：有权限*/
	@Length(max = 1)
	private boolean updateAuthority;
	/*新增权限 0:无权限  1：有权限*/
	@Length(max = 1)
	private boolean addAuthority;
	/*导出权限 0:无权限  1：有权限*/
	@Length(max = 1)
	private boolean expAuthority;
	/*删除权限 0:无权限  1：有权限*/
	@Length(max = 1)
	private boolean delAuthority;

	/*充值权限 0:无权限  1：有权限*/
	@Length(max = 1)
	private boolean refillAuthority;

	/*提现权限 0:无权限  1：有权限*/
	@Length(max = 1)
	private boolean cashAuthority;

	/*确认受理权限 0:无权限  1：有权限*/
	@Length(max = 1)
	private boolean confirmAuthority;

	/*拒绝受理权限 0:无权限  1：有权限*/
	@Length(max = 1)
	private boolean dismissalAuthority;

	/*调价权限 0:无权限  1：有权限*/
	@Length(max = 1)
	private boolean modifyPriceAuthority;

	/*商品上架权限 0:无权限  1：有权限*/
	@Length(max = 1)
	private boolean upMarketAuthority;

	/*商品下架权限 0:无权限  1：有权限*/
	@Length(max = 1)
	private boolean downMarketAuthority;

	/*打印权限 0:无权限  1：有权限*/
	@Length(max = 1)
	private boolean printAuthority;

	/*缴款权限 0:无权限  1：有权限*/
	@Length(max = 1)
	private boolean paymentAuthority;

	/*审核权限 0:无权限  1：有权限*/
	@Length(max = 1)
	private boolean appliedAuthority;

	/*统计权限 0:无权限  1：有权限*/
	@Length(max = 1)
	private boolean statisticsAuthority;

	/*分享权限 0:无权限  1：有权限*/
	@Length(max = 1)
	private boolean shareAuthority;

	/*管理权限 0:无权限  1：有权限*/
	@Length(max = 1)
	private boolean superviseAuthority;

	/*拒绝退货 0:无权限  1：有权限*/
	@Length(max = 1)
	private boolean refuseReturnAuthority;

	/*同意退货 0:无权限  1：有权限*/
	@Length(max = 1)
	private boolean agreeReturnAuthority;

	/*发货 0:无权限  1：有权限*/
	@Length(max = 1)
	private boolean sendGoodsAuthority;

	/*取消订单 0:无权限  1：有权限*/
	@Length(max = 1)
	private boolean cancelOrderAuthority;

	/*关闭 0:无权限  1：有权限*/
	@Length(max = 1)
	private boolean closeAuthority;

	/*开启 0:无权限  1：有权限*/
	@Length(max = 1)
	private boolean openAuthority;
	/*二维码 0:无权限  1：有权限*/
	@Length(max = 1)
	private boolean qrCodeAuthority;

	//==========================================================================get set=====================================
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public TenantRules getRules() {
		return rules;
	}

	public void setRules(TenantRules rules) {
		this.rules = rules;
	}

	public boolean isReadAuthority() {
		return readAuthority;
	}

	public void setReadAuthority(boolean readAuthority) {
		this.readAuthority = readAuthority;
	}

	public boolean isAddAuthority() {
		return addAuthority;
	}

	public void setAddAuthority(boolean addAuthority) {
		this.addAuthority = addAuthority;
	}

	public boolean isUpdateAuthority() {
		return updateAuthority;
	}

	public void setUpdateAuthority(boolean updateAuthority) {
		this.updateAuthority = updateAuthority;
	}

	public boolean isExpAuthority() {
		return expAuthority;
	}

	public void setExpAuthority(boolean expAuthority) {
		this.expAuthority = expAuthority;
	}

	public boolean isDelAuthority() {
		return delAuthority;
	}

	public void setDelAuthority(boolean delAuthority) {
		this.delAuthority = delAuthority;
	}

	public boolean isRefillAuthority() {
		return refillAuthority;
	}

	public void setRefillAuthority(boolean refillAuthority) {
		this.refillAuthority = refillAuthority;
	}

	public boolean isCashAuthority() {
		return cashAuthority;
	}

	public void setCashAuthority(boolean cashAuthority) {
		this.cashAuthority = cashAuthority;
	}

	public boolean isConfirmAuthority() {
		return confirmAuthority;
	}

	public void setConfirmAuthority(boolean confirmAuthority) {
		this.confirmAuthority = confirmAuthority;
	}

	public boolean isDismissalAuthority() {
		return dismissalAuthority;
	}

	public void setDismissalAuthority(boolean dismissalAuthority) {
		this.dismissalAuthority = dismissalAuthority;
	}

	public boolean isModifyPriceAuthority() {
		return modifyPriceAuthority;
	}

	public void setModifyPriceAuthority(boolean modifyPriceAuthority) {
		this.modifyPriceAuthority = modifyPriceAuthority;
	}

	public boolean isUpMarketAuthority() {
		return upMarketAuthority;
	}

	public void setUpMarketAuthority(boolean upMarketAuthority) {
		this.upMarketAuthority = upMarketAuthority;
	}

	public boolean isDownMarketAuthority() {
		return downMarketAuthority;
	}

	public void setDownMarketAuthority(boolean downMarketAuthority) {
		this.downMarketAuthority = downMarketAuthority;
	}

	public boolean isPrintAuthority() {
		return printAuthority;
	}

	public void setPrintAuthority(boolean printAuthority) {
		this.printAuthority = printAuthority;
	}

	public boolean isPaymentAuthority() {
		return paymentAuthority;
	}

	public void setPaymentAuthority(boolean paymentAuthority) {
		this.paymentAuthority = paymentAuthority;
	}

	public boolean isAppliedAuthority() {
		return appliedAuthority;
	}

	public void setAppliedAuthority(boolean appliedAuthority) {
		this.appliedAuthority = appliedAuthority;
	}

	public boolean isStatisticsAuthority() {
		return statisticsAuthority;
	}

	public void setStatisticsAuthority(boolean statisticsAuthority) {
		this.statisticsAuthority = statisticsAuthority;
	}

	public boolean isShareAuthority() {
		return shareAuthority;
	}

	public void setShareAuthority(boolean shareAuthority) {
		this.shareAuthority = shareAuthority;
	}

	public boolean isSuperviseAuthority() {
		return superviseAuthority;
	}

	public void setSuperviseAuthority(boolean superviseAuthority) {
		this.superviseAuthority = superviseAuthority;
	}

	public boolean isRefuseReturnAuthority() {
		return refuseReturnAuthority;
	}

	public void setRefuseReturnAuthority(boolean refuseReturnAuthority) {
		this.refuseReturnAuthority = refuseReturnAuthority;
	}

	public boolean isAgreeReturnAuthority() {
		return agreeReturnAuthority;
	}

	public void setAgreeReturnAuthority(boolean agreeReturnAuthority) {
		this.agreeReturnAuthority = agreeReturnAuthority;
	}

	public boolean isSendGoodsAuthority() {
		return sendGoodsAuthority;
	}

	public void setSendGoodsAuthority(boolean sendGoodsAuthority) {
		this.sendGoodsAuthority = sendGoodsAuthority;
	}

	public boolean isCancelOrderAuthority() {
		return cancelOrderAuthority;
	}

	public void setCancelOrderAuthority(boolean cancelOrderAuthority) {
		this.cancelOrderAuthority = cancelOrderAuthority;
	}

	public boolean isCloseAuthority() {
		return closeAuthority;
	}

	public void setCloseAuthority(boolean closeAuthority) {
		this.closeAuthority = closeAuthority;
	}

	public boolean isOpenAuthority() {
		return openAuthority;
	}

	public void setOpenAuthority(boolean openAuthority) {
		this.openAuthority = openAuthority;
	}

	public boolean isQrCodeAuthority() {
		return qrCodeAuthority;
	}

	public void setQrCodeAuthority(boolean qrCodeAuthority) {
		this.qrCodeAuthority = qrCodeAuthority;
	}

	//==============================
	public Map<String,Boolean> getAuthority() {
		Map<String,Boolean> mapAuthority=new HashMap<>();
		mapAuthority.put(TenantRules.Type.read.name(),this.isReadAuthority());
		mapAuthority.put(TenantRules.Type.add.name(),this.isAddAuthority());
		mapAuthority.put(TenantRules.Type.update.name(),this.isUpdateAuthority());
		mapAuthority.put(TenantRules.Type.del.name(),this.isDelAuthority());
		mapAuthority.put(TenantRules.Type.exp.name(),this.isExpAuthority());
		mapAuthority.put(TenantRules.Type.refill.name(),this.isRefillAuthority());
		mapAuthority.put(TenantRules.Type.cash.name(),this.isCashAuthority());
		mapAuthority.put(TenantRules.Type.confirm.name(),this.isConfirmAuthority());
		mapAuthority.put(TenantRules.Type.dismissal.name(),this.isDismissalAuthority());
		mapAuthority.put(TenantRules.Type.modifyPrice.name(),this.isModifyPriceAuthority());
		mapAuthority.put(TenantRules.Type.upMarket.name(),this.isUpMarketAuthority());
		mapAuthority.put(TenantRules.Type.downMarket.name(),this.isDownMarketAuthority());
		mapAuthority.put(TenantRules.Type.print.name(),this.isPrintAuthority());
		mapAuthority.put(TenantRules.Type.applied.name(),this.isAppliedAuthority());
		mapAuthority.put(TenantRules.Type.statistics.name(),this.isStatisticsAuthority());
		mapAuthority.put(TenantRules.Type.share.name(),this.isShareAuthority());
		mapAuthority.put(TenantRules.Type.supervise.name(),this.isSuperviseAuthority());
		mapAuthority.put(TenantRules.Type.refuseReturn.name(),this.isRefuseReturnAuthority());
		mapAuthority.put(TenantRules.Type.agreeReturn.name(),this.isAgreeReturnAuthority());
		mapAuthority.put(TenantRules.Type.sendGoods.name(),this.isSendGoodsAuthority());
		mapAuthority.put(TenantRules.Type.cancelOrder.name(),this.isCancelOrderAuthority());
		mapAuthority.put(TenantRules.Type.close.name(),this.isCloseAuthority());
		mapAuthority.put(TenantRules.Type.open.name(),this.isOpenAuthority());
		mapAuthority.put(TenantRules.Type.qrCode.name(),this.isQrCodeAuthority());
		return  mapAuthority;
	}
}