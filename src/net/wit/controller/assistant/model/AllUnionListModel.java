package net.wit.controller.assistant.model;

import net.wit.entity.Tenant;
import net.wit.entity.Union;
import net.wit.entity.UnionTenant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AllUnionListModel extends BaseModel {
	/*ID*/
	private Long id;
	/*联盟名称 */
	private String name;
	/*背景图*/
	private String image;
	/*联盟佣金*/
	private BigDecimal brokerage;
	/* 联盟商家数量 */
	private Integer tenantNumber;
	/*是否加入联盟*/
	private Boolean isUnion;
	public Boolean getIsUnion() {
		return isUnion;
	}

	public void setIsUnion(Boolean isUnion) {
		this.isUnion = isUnion;
	}

	public Integer getTenantNumber() {
		return tenantNumber;
	}

	public void setTenantNumber(Integer tenantNumber) {
		this.tenantNumber = tenantNumber;
	}

	public BigDecimal getBrokerage() {
		return brokerage;
	}

	public void setBrokerage(BigDecimal brokerage) {
		this.brokerage = brokerage;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void copyFrom(Union union, Tenant tenant) {
        this.id = union.getId();
		this.name = union.getName();
		this.brokerage = union.getBrokerage();
		this.image =union.getImage();
		this.tenantNumber = union.getTenantNumber();
		if(union.getUnionTenants().size()>0){
			for(UnionTenant unionTenant:union.getUnionTenants()){
				if(unionTenant.getTenant() == tenant && unionTenant.getStatus().equals(UnionTenant.Status.confirmed) && unionTenant.getType().equals(UnionTenant.Type.tenant)){
					this.isUnion = true;
					break;
				}else {
					this.isUnion = false;
				}

			}

		}else{
			this.isUnion = false;
		}


	}
	//根据status状态是否加入联盟
	public static  List<AllUnionListModel> bindData(List<Union> unions, Tenant tenant,String status) {
		List<AllUnionListModel> models = new ArrayList<AllUnionListModel>();
		List<Union> newUnions  = new ArrayList<Union>();
		for (Union union:unions) {
			AllUnionListModel model = new AllUnionListModel();
			if(status.equals("joined")){
				if(union.getUnionTenants()!=null){
					for(UnionTenant unionTenant:union.getUnionTenants()){
						if(unionTenant.getTenant()  == tenant && unionTenant.getStatus().equals(UnionTenant.Status.confirmed) && unionTenant.getType().equals(UnionTenant.Type.tenant)){
							newUnions.add(union);
							break;
						}
					}

				}
			}else if(status.equals("notJoined")){
				newUnions.add(union);
				if(union.getUnionTenants()!=null){
					for(UnionTenant unionTenant:union.getUnionTenants()){
						if(unionTenant.getTenant() == tenant && unionTenant.getStatus().equals(UnionTenant.Status.confirmed) && unionTenant.getType().equals(UnionTenant.Type.tenant)){
							newUnions.remove(union);
							break;
						}
					}

				}
			}else{
				newUnions.add(union);
			}
		}
		for (Union union:newUnions) {
			AllUnionListModel model = new AllUnionListModel();
			model.copyFrom(union, tenant);
			models.add(model);
		}
		return models;
	}
	
}
