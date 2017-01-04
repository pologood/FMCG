package net.wit.controller.weixin.model;

import net.wit.entity.Idcard.AuthStatus;
import net.wit.entity.Member;
import net.wit.entity.Member.Gender;

public class MemberModel extends BaseModel {
    /**
     * 会员ID
     */
    private Long id;
    /**
     * 头像
     */
    private String headImg;
    /**
     * 姓名
     */
    private String name;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 性别
     */
    private Gender gender;
    /**
     * 实名状态
     */
    private AuthStatus authStatus;
    /**
     * 绑定手机
     */
    private Member.BindStatus bindMobile;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public AuthStatus getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(AuthStatus authStatus) {
        this.authStatus = authStatus;
    }

    public Member.BindStatus getBindMobile() {
        return bindMobile;
    }

    public void setBindMobile(Member.BindStatus bindMobile) {
        this.bindMobile = bindMobile;
    }

    public void copyFrom(Member member) {
        this.id = member.getId();
        this.headImg = member.getHeadImg();
        this.name = member.getName();
        this.nickName = member.getDisplayName();
        this.gender = member.getGender();
        this.authStatus = member.getAuthStatus();
        this.bindMobile = member.getBindMobile();
    }

}
