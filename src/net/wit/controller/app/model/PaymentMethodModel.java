package net.wit.controller.app.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.wit.entity.PaymentMethod;
import net.wit.entity.PaymentMethod.Method;

public class PaymentMethodModel extends BaseModel {
	
	/*ID*/
	private Long id;
	/*名称*/
	private String name;
	/** 方式 */
	private Method method;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
	
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public void copyFrom(PaymentMethod paymentMethod) {
		this.id = paymentMethod.getId();
		this.name = paymentMethod.getName();
		this.method = paymentMethod.getMethod();
	}
	
	public static List<PaymentMethodModel> bindData(List<PaymentMethod> PaymentMethods) {
		List<PaymentMethodModel> models = new ArrayList<PaymentMethodModel>();
		for (PaymentMethod paymentMethod:PaymentMethods) {
			PaymentMethodModel model = new PaymentMethodModel();
			model.copyFrom(paymentMethod);
			models.add(model);
		}
		return models;
	}
	public static Set<PaymentMethodModel> bindData(Set<PaymentMethod> PaymentMethods) {
		Set<PaymentMethodModel> models = new HashSet<PaymentMethodModel>(PaymentMethods.size());
		for (PaymentMethod paymentMethod:PaymentMethods) {
			PaymentMethodModel model = new PaymentMethodModel();
			model.copyFrom(paymentMethod);
			models.add(model);
		}
		return models;
	}
	
	
}
