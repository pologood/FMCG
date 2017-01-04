package net.wit.controller.assistant.model;

import net.wit.entity.Member;
import net.wit.entity.Task;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by ruanx on 2016/11/5.
 */
public class TaskModel extends BaseModel {
    /*ID*/
    private Long id;

    /*会员名*/
    private String nickName;

    /** 销售金额-计划 */
    private BigDecimal sale;

    /** 发展会员-计划 */
    private Long invite;

    /** 推广红包-计划 */
    private Long coupon;

    /** 分享链接-计划 */
    private Long share;

    /** 销售金额-执行 */
    private BigDecimal doSale;

    /** 发展会员-执行 */
    private Long doInvite;

    /** 推广红包-执行 */
    private Long doCoupon;

    /** 分享链接-执行 */
    private Long doShare;

    /** 销售金额-占比 */
    private String proSale;

    /** 发展会员-占比 */
    private String proInvite;

    /** 推广红包-占比 */
    private String proCoupon;

    /** 分享链接-占比 */
    private String proShare;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSale() {
        return sale;
    }

    public void setSale(BigDecimal sale) {
        this.sale = sale;
    }

    public Long getInvite() {
        return invite;
    }

    public void setInvite(Long invite) {
        this.invite = invite;
    }

    public Long getCoupon() {
        return coupon;
    }

    public void setCoupon(Long coupon) {
        this.coupon = coupon;
    }

    public Long getShare() {
        return share;
    }

    public void setShare(Long share) {
        this.share = share;
    }

    public BigDecimal getDoSale() {
        return doSale;
    }

    public void setDoSale(BigDecimal doSale) {
        this.doSale = doSale;
    }

    public Long getDoInvite() {
        return doInvite;
    }

    public void setDoInvite(Long doInvite) {
        this.doInvite = doInvite;
    }

    public Long getDoCoupon() {
        return doCoupon;
    }

    public void setDoCoupon(Long doCoupon) {
        this.doCoupon = doCoupon;
    }

    public Long getDoShare() {
        return doShare;
    }

    public void setDoShare(Long doShare) {
        this.doShare = doShare;
    }

    public String getProSale() {
        return proSale;
    }

    public void setProSale(String proSale) {
        this.proSale = proSale;
    }

    public String getProInvite() {
        return proInvite;
    }

    public void setProInvite(String proInvite) {
        this.proInvite = proInvite;
    }

    public String getProCoupon() {
        return proCoupon;
    }

    public void setProCoupon(String proCoupon) {
        this.proCoupon = proCoupon;
    }

    public String getProShare() {
        return proShare;
    }

    public void setProShare(String proShare) {
        this.proShare = proShare;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public  void copyFrom(Task task) {
        this.id = task.getId();
        DecimalFormat df = new DecimalFormat("#");
        Member member = task.getMember();
        if (member.getName() == null) {
            this.nickName = member.getDisplayName();
        } else {
            this.nickName = member.getName();
        }
        this.sale = task.getSale();
        this.doSale = task.getDoSale();
        this.proSale = df.format(task.getDoSale().divide(task.getSale(),2,BigDecimal.ROUND_HALF_UP).doubleValue()*100);
        this.share = task.getShare();
        this.doShare = task.getDoShare();
        this.proShare = df.format((double)task.getDoShare()/(double)task.getShare()*100);
        this.invite = task.getInvite();
        this.doInvite = task.getDoInvite();
        this.proInvite = df.format((double)task.getDoInvite()/(double)task.getInvite()*100);
        this.coupon = task.getCoupon();
        this.doCoupon = task.getDoCoupon();
        this.proCoupon = df.format((double)task.getDoCoupon()/(double)task.getCoupon()*100);
    }

    public static List<TaskModel> bindData(List<Task> tasks) {
        List<TaskModel> models = new ArrayList<TaskModel>();
        for (Task task:tasks) {
            TaskModel model = new TaskModel();
            model.copyFrom(task);
            models.add(model);
        }
        return models;
    }


}
