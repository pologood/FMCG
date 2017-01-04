package net.wit.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 服务实体
 * Created by My-PC on 16/05/31.
 */
@Entity
@Table(name = "xx_services")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_services_sequence")
public class Services extends BaseEntity {
    private static final long serialVersionUID = -540706734359671149L;

    /**
     * 服务类型
     */
    public enum Status{
        /** 购物屏*/
        shoppingScreen,
        /** 云看店*/
        cloudSeeShop
    }

    /**
     * 跟踪状态
     */
    public enum Type{
        /** 无*/
        none,
        /** 已申请未开通 */
        wait,
        /** 暂不开通*/
        refuse,
        /** 成功开通*/
        success
    }

    /**
     * 状态
     */
    public enum State{
        /** 未跟踪*/
        none,
        /** 已跟踪*/
        success
    }

    /** 商铺 */
    @ManyToOne(fetch = FetchType.LAZY)
    private Tenant tenant;

    /** 服务类型 */
    @NotNull
    @Column(nullable = false)
    private Status status;

    /** 跟踪状态 */
    @NotNull
    @Column(nullable = false)
    private Type type;

    /** 状态 */
    @NotNull
    @Column(nullable = false)
    private State state;

    /** 备注*/
    @Lob
    private String content;

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
