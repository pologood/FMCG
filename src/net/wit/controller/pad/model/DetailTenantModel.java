package net.wit.controller.pad.model;

/**
 * Created by Administrator on 2016/11/28.
 */
public class DetailTenantModel {
    /*ID*/
    private Long id;

    /*name*/
    private String name;

    /** 货到付款 */
    private Boolean toPay;

    /** 担保交易 */
    private Boolean tamPo;

    /** 七天退货 */
    private Boolean noReason;

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

    public Boolean getToPay() {
        return toPay;
    }

    public void setToPay(Boolean toPay) {
        this.toPay = toPay;
    }

    public Boolean getTamPo() {
        return tamPo;
    }

    public void setTamPo(Boolean tamPo) {
        this.tamPo = tamPo;
    }

    public Boolean getNoReason() {
        return noReason;
    }

    public void setNoReason(Boolean noReason) {
        this.noReason = noReason;
    }
}
