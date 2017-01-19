package net.wit.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: TenantFreightTemplate
 * @Description: 运费模版
 * @author Administrator
 * @date 2017年1月9日 下午4:09:56
 */
@Entity
@Table(name = "xx_tenant_freight_template")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_tenant_freight_template_sequence")
public class TenantFreightTemplate extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** 名称 */
    @Expose
    @JsonProperty
    @Length(max = 200)
    private String name;

    /** 是否默认模版 */
    @Column(nullable = false)
    private Boolean isDefault;

    /** 店铺 */
    @NotNull
    @JsonProperty
    @ManyToOne(fetch = FetchType.LAZY)
    private Tenant tenant;

    /** 区域 */
    @Expose
    @Valid
    @NotEmpty
    @OneToMany(mappedBy = "tenantFreightTemplate", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TenantFreight> freights = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public List<TenantFreight> getFreights() {
        return freights;
    }

    public void setFreights(List<TenantFreight> freights) {
        this.freights = freights;
    }
}
