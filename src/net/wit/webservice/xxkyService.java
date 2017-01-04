package net.wit.webservice;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.wit.entity.Area;
import net.wit.entity.DeliveryCenter;
import net.wit.entity.Order;
import net.wit.entity.OrderItem;
import net.wit.entity.Receiver;
import net.wit.entity.Trade;
import net.wit.webservice.xxkyServiceStub.Add_WEB_ORDERENTERResponse;
import net.wit.webservice.xxkyServiceStub.SelectOrderTraceInfoResponse;

public class xxkyService {
	public Boolean AddOrderenter(Trade trade){
		Boolean isSuccess = Boolean.FALSE;
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		try {
			Order order = trade.getOrder();//订单父表
			   Receiver receiver = trade.getOrder().getReceiver();//收货地址表
			   DeliveryCenter deliveryCenter = trade.getTenant().getDefaultDeliveryCenter();//获取默认发货地址
			   String bsite="";//获取
			   String esite="";
			   if(deliveryCenter.getArea().getParent()!=null && deliveryCenter.getArea().getChildren().size()==0){
				   bsite=deliveryCenter.getArea().getParent().getName();
			   }else if(deliveryCenter.getArea().getParent()!=null && deliveryCenter.getArea().getChildren().size()!=0){
				   bsite=deliveryCenter.getArea().getName();
			   }else if(deliveryCenter.getArea().getParent()==null){
				   bsite=deliveryCenter.getArea().getName();
			   }
			   if(receiver.getArea().getParent()!=null && receiver.getArea().getChildren().size()==0){
				   esite=receiver.getArea().getParent().getName();
			   }else if(receiver.getArea().getParent()!=null && receiver.getArea().getChildren().size()!=0){
				   esite=receiver.getArea().getName();
			   }else if(receiver.getArea().getParent()==null){
				   esite=receiver.getArea().getName();
			   }
			   String products = "";
			   String productsFullName = "";
			   for(OrderItem oderItem: trade.getOrderItems()){
				   products = products+oderItem.getName()+",";
				   productsFullName = productsFullName+oderItem.getFullName()+",";
			   }
			   Calendar calendar = new GregorianCalendar();
				  if(trade.getShippingDate()!=null){
					  calendar.setTime(trade.getShippingDate()); 
					  calendar.add(calendar.DATE,1);
				  }else{
					  calendar.setTime(new Date()); 
					  calendar.add(calendar.DATE,1);
				  }
				  String arrivedate = "";
				  if(trade.getConfirmDueDate()!=null){
					  arrivedate = df.format(trade.getConfirmDueDate());
				  }
			  xxkyServiceStub xxky = new xxkyServiceStub();
			  xxkyServiceStub.Add_WEB_ORDERENTER in = new xxkyServiceStub.Add_WEB_ORDERENTER();
			  in.setBsite(bsite);//始发站(bsite)
			  in.setEsite(esite);// 到达站(esite)
			  in.setOrderno(order.getSn()+"-"+trade.getSn()); // 订单号(ordemo)
			  in.setShipper(deliveryCenter.getContact());//发货人(shipper)
			  in.setShipaddr(deliveryCenter.getAreaName()+deliveryCenter.getAddress());// 卖家地址(shipaddr)
			  in.setShippertel(deliveryCenter.getMobile());// 卖家联系固话(shippertel)
			  in.setShippermb(deliveryCenter.getMobile());// 卖家手机(shippermb)
			  in.setAccount("");// 货款(account)
			  in.setConginee(receiver.getConsignee());// 买家姓名(conginee)
			  in.setConsigneemb(receiver.getPhone()); //买家联系手机(consigneemb)
			  in.setConsigneetel(receiver.getPhone());//买家固话(consigneetel)
			  in.setCongigneeaddr(receiver.getAreaName()+receiver.getAddress()); //买家收货地址(congigneeaddr)
			  in.setProduct(products); //品名(product)
			  in.setProductinfo(productsFullName); //货物详情(productinfo)
			  in.setCompany(trade.getTenant().getName()); //公司名(company)
			  in.setFetchpaper("");//  要求接货证件(fetchpaper)
			  in.setFetchdate(df.format(calendar.getTime()));//要求接货时间(fetchdate)
			  in.setArrivedate(arrivedate);//要求到货时间(arrivedate)
			  Add_WEB_ORDERENTERResponse out = xxky.add_WEB_ORDERENTER(in);
			  String orderenterResult = out.getAdd_WEB_ORDERENTERResult();
			  if(orderenterResult.indexOf("true") != -1) {
				  // TODO
				  isSuccess=true;
			  }
	    	} catch (Exception e) {
	    		isSuccess=false;
	    		e.printStackTrace();
		    }
		   return isSuccess;
	}
	
	public Map<String,Object> GetOrderTraceInfo(String billno){
		ObjectMapper mapper = new ObjectMapper();
		Map<String,Object> productMap=null;
		Map<String,Object> data=null;
		   try {   
			  xxkyServiceStub xxky = new xxkyServiceStub();
			  xxkyServiceStub.SelectOrderTraceInfo in = new xxkyServiceStub.SelectOrderTraceInfo();
			  org.apache.axis2.databinding.types.soapencoding.String instr = new org.apache.axis2.databinding.types.soapencoding.String();
			  in.setBillno(billno);
			  SelectOrderTraceInfoResponse out = xxky.selectOrderTraceInfo(in);
			  //mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
			  try{
				  String getResult = out.getSelectOrderTraceInfoResult();
				  if(getResult.indexOf("正常")!=-1){
					  productMap = mapper.readValue(out.getSelectOrderTraceInfoResult(), Map.class);//转成map
					  if(productMap.get("ErrorCode").equals("正常")){
						  if(getResult.indexOf("签收")!=-1){
							  productMap.put("state", 3);
						  }
					  }else{
						  productMap.put("state", 0);
					  }
				  }
			  }catch(Exception ex){
				  ex.printStackTrace();
				  productMap=null;
			  }
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    		productMap=null;
		    }
		return productMap;
	}

//	public static void main(String[] args) {
//		Order order = null;
//		xxkyService xxkyService = new xxkyService();
//		xxkyService.GetOrderTraceInfo("50010828");
//	}
}
