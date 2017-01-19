package net.wit.controller.assistant.model;

import net.wit.entity.Tenant;
import net.wit.entity.UnionTenant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UnionTenantScreenListModel extends BaseModel {
	/**ID*/
	private Long id;
	/**商家*/
	private String tenantName;
	/** 销售 */
	private BigDecimal sales;

	/** 支出 */
	private BigDecimal pay;

	/** 成交订单数 */
	private BigDecimal volume;

	/** 客单价 */
	private BigDecimal unitPrice;

	public Long getId() {
		return id;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public BigDecimal getPay() {
		return pay;
	}

	public void setPay(BigDecimal pay) {
		this.pay = pay;
	}

	public BigDecimal getSales() {
		return sales;
	}

	public void setSales(BigDecimal sales) {
		this.sales = sales;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void copyFrom(UnionTenant unionTenant) {
        this.id = unionTenant.getId();
		if(unionTenant.getEquipment()!=null){
			this.tenantName = unionTenant.getEquipment().getTenant().getName();
		}

		this.sales = unionTenant.getSales()==null?new BigDecimal(0):unionTenant.getSales();
		this.pay = unionTenant.getPay()==null?new BigDecimal(0):unionTenant.getPay();
		this.volume = unionTenant.getVolume()==null?new BigDecimal(0):unionTenant.getVolume();
		this.unitPrice = unionTenant.getUnitPrice()==null?new BigDecimal(0):unionTenant.getUnitPrice();

	}
	
	public static  List<UnionTenantScreenListModel> bindData(List<UnionTenant> unionTenants) {
		List<UnionTenantScreenListModel> models = new ArrayList<UnionTenantScreenListModel>();
		for (UnionTenant unionTenant:unionTenants) {
			UnionTenantScreenListModel model = new UnionTenantScreenListModel();
			model.copyFrom(unionTenant);
			models.add(model);
		}
		return models;
	}
	
}
