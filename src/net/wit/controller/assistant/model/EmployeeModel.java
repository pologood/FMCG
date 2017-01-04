package net.wit.controller.assistant.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.wit.controller.app.model.*;
import net.wit.controller.app.model.SingleModel;
import net.wit.entity.Employee;
import net.wit.entity.Location;
import net.wit.entity.Member;

import java.util.*;

/**
 * Created by Administrator on 2016/11/10.
 */
public class EmployeeModel {
    /*ID*/
    private Long id;
    /*会员名*/
    private String nickName;
    /*所属门店*/
    private String deliveryCenter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDeliveryCenter() {
        return deliveryCenter;
    }

    public void setDeliveryCenter(String deliveryCenter) {
        this.deliveryCenter = deliveryCenter;
    }

    public static List<JSONObject> bindData(List<Employee> emps,Member memberBoss, Location location) {
        List<JSONObject> models = new ArrayList<JSONObject>();
        HashMap map = new HashMap();
            for (Employee emp:emps) {
            if (emp.getDeliveryCenter()!=null && emp.getDeliveryCenter().getId()!=null) {
                map.put(emp.getDeliveryCenter().getId(),emp.getDeliveryCenter().getName());
            }
        }
        // 店主加入员工列表
        JSONObject jsonBoss = new JSONObject();
        JSONArray employeeBoss = new JSONArray();
        jsonBoss.put("deliveryCenter","店主");
        JSONObject emplBoss = new JSONObject();
        emplBoss.put("id",memberBoss.getId());
        emplBoss.put("nickName",memberBoss.getDisplayName());
        employeeBoss.add(emplBoss);
        jsonBoss.put("employee",employeeBoss);
        models.add(jsonBoss);
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            JSONObject jsonObject = new JSONObject();
            JSONObject empl = null;
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            jsonObject.put("deliveryCenter",val.toString());
            int i = 0;
            JSONArray employee = new JSONArray();
            for (Employee emp:emps) {
                if(emp.getDeliveryCenter().getId().equals(Long.valueOf(key.toString()))&&!emp.getMember().getId().equals(memberBoss.getId())){
                    empl = new JSONObject();
                    empl.put("id",emp.getId());
                    Member member = emp.getMember();
                    empl.put("nickName",member.getDisplayName());
                    employee.add(i,empl);
                    i++;
                }
            }
            jsonObject.put("employee",employee);
            models.add(jsonObject);
        }
        return models;
    }

}
