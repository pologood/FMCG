package net.wit.controller.assistant.model;

import net.wit.entity.OrderItem;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/1/5.
 */
public class DataStatisticsModel {
    /* 开始时间 */
    private String startDate;

    /* 结束时间 */
    private String endDate;

    /* 7天时间数组 */
    private ArrayList dateArry;

    /* 付款人数 */
    private Long payMembers;

    /* 平均客单价 */
    private double averageMoney;

    /* 描述 */
    private String description;

    /* 下单数 */
    private Long orders;

    /* 已支付数 */
    private Long pays;

    /* 已发货数 */
    private Long deliverys;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ArrayList getDateArry() {
        return dateArry;
    }

    public void setDateArry(ArrayList dateArry) {
        this.dateArry = dateArry;
    }

    public Long getPayMembers() {
        return payMembers;
    }

    public void setPayMembers(Long payMembers) {
        this.payMembers = payMembers;
    }

    public double getAverageMoney() {
        return averageMoney;
    }

    public void setAverageMoney(double averageMoney) {
        this.averageMoney = averageMoney;
    }

    public Long getOrders() {
        return orders;
    }

    public void setOrders(Long orders) {
        this.orders = orders;
    }

    public Long getPays() {
        return pays;
    }

    public void setPays(Long pays) {
        this.pays = pays;
    }

    public Long getDeliverys() {
        return deliverys;
    }

    public void setDeliverys(Long deliverys) {
        this.deliverys = deliverys;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void copyFrom(String type, Date date, String startDate, String endDate, Long payMembers, Long orders, Long pays, Long deliverys,List<OrderItem> orderItems,Long orderAll){
        this.startDate = startDate;
        this.endDate = endDate;
        this.payMembers= payMembers;
        BigDecimal prize = BigDecimal.ZERO;
        Double d = 0.00;
        this.averageMoney = 0.00;
        if(orderItems.size()>0){
            for (OrderItem orderItem:orderItems){
                BigDecimal p = orderItem.getPrice();
                d = p.add(prize).doubleValue();
                prize = BigDecimal.valueOf(d);
            }
            this.averageMoney = d/orderAll;
        }
        this.orders = orders;
        this.pays = pays;
        this.deliverys = deliverys;
        if("1".equals(type)){
            ArrayList arry = new ArrayList();
            Date now = new Date();
            Date beginDate = null;
            SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
            this.description = sf.format(date);
            String dateString = "";
            for(int time=-6;time<0;time++){
                Calendar cal = Calendar.getInstance();
                cal.setTime(now);
                cal.add(Calendar.DAY_OF_MONTH,time);
                beginDate = cal.getTime();
                dateString = sf.format(beginDate);
                arry.add(dateString);
            }
            arry.add(sf.format(now));
            this.dateArry = arry;
        }
        if("2".equals(type)){
            this.description = "30天汇总";
        }
        if("3".equals(type)){
            this.description = "90天汇总";
        }
    }
}
