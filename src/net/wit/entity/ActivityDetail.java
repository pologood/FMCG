/**
 * ====================================================
 * 文件名称: Union.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年5月9日			Administrator(创建:创建文件)
 * ====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 */
package net.wit.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * @ClassName: 活动明细
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014年5月9日 下午3:26:44
 */
@Entity
@Table(name = "xx_activity_detail")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_activity_detail_sequence")
public class ActivityDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 活动描述 */
    @ManyToOne(fetch = FetchType.LAZY)
    private ActivityRules activityRules;

    /** 赠送积分 */
    @Min(0)
    @Column()
    private Long point;

    /** 奖励金额 */
    @Min(0)
    @Digits(integer = 12, fraction = 3)
    @Column(nullable = false, precision = 21, scale = 6)
    private BigDecimal amount;

    /** 所属店铺 */
    @ManyToOne(fetch = FetchType.LAZY)
    private Tenant tenant;

    /** 所属会员 */
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    /** 完成时间 */
    private Date finishTime;

    public ActivityRules getActivityRules() {
        return activityRules;
    }

    public void setActivityRules(ActivityRules activityRules) {
        this.activityRules = activityRules;
    }

    public Long getPoint() {
        return point;
    }

    public void setPoint(Long point) {
        this.point = point;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }
}
