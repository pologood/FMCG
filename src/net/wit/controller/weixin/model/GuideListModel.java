package net.wit.controller.weixin.model;

import net.wit.entity.Employee;
import net.wit.entity.Location;
import net.wit.entity.Member;
import net.wit.entity.MemberAttribute;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * 导购列表
 * Created by WangChao on 2016-6-29.
 */
public class GuideListModel extends BaseModel {
    /**
     * 导购Id
     */
    private Long id;
    /**
     * 员工Id
     */
    private Long employeeId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 头像
     */
    private String headImg;
    /**
     * 店铺名称
     */
    private String tenantName;
    /**
     * 销量
     */
    private Integer quertity;
    /**
     * 粉丝数
     */
    private Integer fansCount;
    /**
     * 是否已收藏
     */
    private boolean hasFavorited;
    /**
     * 电话
     */
    private String phone;
    /**
     * 星座
     */
    private String constellation;
    /**
     * 标签
     */
    private List<TagModel> tags;
    /**
     * 活跃时间
     */
    private String activeTime;
    /**
     * 区域名
     */
    private String areaName;
    /**
     * 常驻区域
     */
    private String permanentArea;
    /**
     * 距离
     */
    private double distance;
    /**
     * 是否去过他/她的店
     */
    private boolean tenantVisited;
    /**
     * 会员vip等级
     */
    private MemberRankModel memberRank;
    /**
     * 个性签名
     */
    private String signature;

    private List<String> personalitytags;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public Integer getQuertity() {
        return quertity;
    }

    public void setQuertity(Integer quertity) {
        this.quertity = quertity;
    }

    public Integer getFansCount() {
        return fansCount;
    }

    public void setFansCount(Integer fansCount) {
        this.fansCount = fansCount;
    }

    public boolean isHasFavorited() {
        return hasFavorited;
    }

    public void setHasFavorited(boolean hasFavorited) {
        this.hasFavorited = hasFavorited;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public List<TagModel> getTags() {
        return tags;
    }

    public void setTags(List<TagModel> tags) {
        this.tags = tags;
    }

    public String getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(String activeTime) {
        this.activeTime = activeTime;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getPermanentArea() {
        return permanentArea;
    }

    public void setPermanentArea(String permanentArea) {
        this.permanentArea = permanentArea;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public boolean isTenantVisited() {
        return tenantVisited;
    }

    public void setTenantVisited(boolean tenantVisited) {
        this.tenantVisited = tenantVisited;
    }

    public MemberRankModel getMemberRank() {
        return memberRank;
    }

    public void setMemberRank(MemberRankModel memberRank) {
        this.memberRank = memberRank;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public List<String> getPersonalitytags() {
        return personalitytags;
    }

    public void setPersonalitytags(List<String> personalitytags) {
        this.personalitytags = personalitytags;
    }

    public void copyFrom(Employee employee) {
        this.id = employee.getMember().getId();
        this.employeeId = employee.getId();
        this.name = employee.getMember().getDisplayName();
        this.age = 22;
        this.headImg = employee.getMember().getHeadImg();
        this.tenantName = employee.getTenant().getName();
        this.quertity = employee.getQuertity();
        this.fansCount = 0;
        this.hasFavorited = false;
        this.phone = employee.getMember().getMobile();
        this.constellation = "双鱼座";
        this.tags = TagModel.bindData(new ArrayList<>(employee.getTags()));
        this.activeTime = "15";
        this.permanentArea = employee.getMember().getArea() != null ? employee.getMember().getArea().getName() : null;
        MemberRankModel memberRankModel = new MemberRankModel();
        memberRankModel.copyFrom(employee.getMember().getMemberRank());
        this.memberRank = memberRankModel;
    }

    public void copyFrom(Employee employee, Integer fansCount, Long visitCount, Member member, Location location, Member guideMember, MemberAttribute signatureMemberAttribute, MemberAttribute personalitytagMemberAttribute) {
        copyFrom(employee, fansCount, visitCount, member, location);
        this.signature= (String) guideMember.getAttributeValue(signatureMemberAttribute);
        this.personalitytags= (List<String>) guideMember.getAttributeValue(personalitytagMemberAttribute);
    }

    public void copyFrom(Employee employee, Integer fansCount, Long visitCount, Member member, Location location) {
        copyFrom(employee);
        this.fansCount = fansCount;
        this.hasFavorited = member != null && member.getFavoriteMembers().contains(employee.getMember());
        if (employee.getTenant() != null) {
            this.areaName = employee.getTenant().nearDeliveryCenter(location).getArea().getName();
            this.distance = employee.getTenant().distatce(location);
            if (this.distance != -1) {
                this.distance = Double.parseDouble(String.format("%.2f", this.distance / 1000));
            }
        }
        if (visitCount > 0) {
            tenantVisited = true;
        }
    }

    public void copyFrom(Employee employee, Integer fansCount, Member member) {
        copyFrom(employee);
        this.fansCount = fansCount;
        this.hasFavorited = member != null && member.getFavoriteMembers().contains(employee.getMember());
    }

    public static List<GuideListModel> bindData(List<Employee> employees) {
        List<GuideListModel> models = new ArrayList<>();
        for (Employee employee : employees) {
            if (employee.getMember() != null) {
                GuideListModel model = new GuideListModel();
                model.copyFrom(employee);
                models.add(model);
            }
        }
        return models;
    }

}
