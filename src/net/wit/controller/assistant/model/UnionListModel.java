package net.wit.controller.assistant.model;

import net.wit.entity.Tenant;
import net.wit.entity.Union;
import net.wit.entity.UnionTenant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UnionListModel extends BaseModel {
	/*ID*/
	private Long id;
	/*联盟名称 */
	private String name;
	/*背景图*/
	private String image;
	/*联盟佣金*/
	private BigDecimal brokerage;

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


	public void copyFrom(Union union) {
        this.id = union.getId();
		this.name = union.getName();
		this.brokerage = union.getBrokerage();
		this.image = union.getImage();


	}
	
	public static  List<UnionListModel> bindData(List<Union> unions) {
		List<UnionListModel> models = new ArrayList<UnionListModel>();
		for (Union union:unions) {
			UnionListModel model = new UnionListModel();
			model.copyFrom(union);
			models.add(model);
		}
		return models;
	}
	
}
