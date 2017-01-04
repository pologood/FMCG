package net.wit.controller.weixin.model;

import net.wit.entity.OrderLog;
import net.wit.entity.OrderLog.Type;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OrderLogModel extends BaseModel {

    /**
     * 类型
     */
    private Type type;

    /**
     * 操作员
     */
    private String operator;

    /**
     * 内容
     */
    private String content;

    /**
     * 时间
     */
    private String createDate;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void copyFrom(OrderLog log) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.type = log.getType();
        this.content = log.getContent();
        this.operator = log.getOperator();
        this.createDate = log.getCreateDate() != null ? sdf.format(log.getCreateDate()) : null;
    }

    public static List<OrderLogModel> bindData(Set<OrderLog> logs) {
        List<OrderLogModel> models = new ArrayList<>();
        for (OrderLog orderLog : logs) {
            OrderLogModel model = new OrderLogModel();
            model.copyFrom(orderLog);
            models.add(model);
        }
        return models;
    }

}
