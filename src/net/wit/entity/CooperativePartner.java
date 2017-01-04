package net.wit.entity;

import com.google.gson.annotations.Expose;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Created by Administrator on 2016/8/22.
 */
@Entity
@Table(name = "xx_cooperative_partner")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_cooperative_partner_sequence")
public class CooperativePartner extends OrderEntity {

    private static final long serialVersionUID = 3177144171916587643L;

    /** 名称 */
    @Column(nullable = false, length = 255)
    private String name;

    /** 图片 **/
    private String licensePhoto;

    /** 图片 **/
    private String licensePhoto1;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicensePhoto() {
        return licensePhoto;
    }

    public void setLicensePhoto(String licensePhoto) {
        this.licensePhoto = licensePhoto;
    }

    public String getLicensePhoto1() {
        return licensePhoto1;
    }

    public void setLicensePhoto1(String licensePhoto1) {
        this.licensePhoto1 = licensePhoto1;
    }
}
