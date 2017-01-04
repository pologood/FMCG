package net.wit.controller.assistant.model;

import net.wit.controller.assistant.model.BaseModel;
import net.wit.controller.assistant.model.SpReturnItemModel;
import net.wit.controller.assistant.model.TenantModel;
import net.wit.entity.SpReturns;
import net.wit.entity.SpReturns.ReturnStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SpReturnListModel extends BaseModel {

	/** ID */
	private Long id;
	/** 店铺 */
	private	TenantModel tenant;
	/** 退货状态 */
	private ReturnStatus returnStatus;
	/** 退货项 */
	private List<SpReturnItemModel> returnsItems;
	/** 结算金额，含运费 */
	private BigDecimal amount;
	/** 物流费用 */
	private BigDecimal freight;
	/** 总数量 */
	private int quantity;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TenantModel getTenant() {
		return tenant;
	}

	public void setTenant(TenantModel tenant) {
		this.tenant = tenant;
	}

	public ReturnStatus getReturnStatus() {
		return returnStatus;
	}

	public void setReturnStatus(ReturnStatus returnStatus) {
		this.returnStatus = returnStatus;
	}

	public List<SpReturnItemModel> getReturnsItems() {
		return returnsItems;
	}

	public void setReturnsItems(List<SpReturnItemModel> returnsItems) {
		this.returnsItems = returnsItems;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void copyFrom(SpReturns spReturns){
		this.id=spReturns.getId();
		TenantModel tenant=new TenantModel();
		tenant.copyFrom(spReturns.getTrade().getTenant());
		this.tenant=tenant;
		this.returnStatus=spReturns.getReturnStatus();
		this.returnsItems=SpReturnItemModel.bindData(spReturns.getReturnsItems());
		this.amount=spReturns.getAmount();
		this.freight=spReturns.getFreight();
		this.quantity=spReturns.getQuantity();
	}

	public static List<SpReturnListModel> bindData(List<SpReturns> spReturnses){
		List<SpReturnListModel> models=new ArrayList<>();
		for(SpReturns spReturns : spReturnses){
			SpReturnListModel model=new SpReturnListModel();
			model.copyFrom(spReturns);
			models.add(model);
		}
		return models;
	}
}
