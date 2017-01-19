/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.entity;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import net.wit.constant.SettingConstant;
import net.wit.entity.Idcard.AuthStatus;
import net.wit.entity.MemberAttribute.Type;
import net.wit.util.JsonUtils;
import net.wit.util.SettingUtils;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.Expose;

/**
 * @author Administrator
 * @ClassName: Member
 * @Description: 会员
 * @date 2014年10月14日 上午9:08:38
 */
@Entity
@Table(name = "xx_member")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_member_sequence")
public class Member extends BaseEntity {

    private static final long serialVersionUID = 1533130686714725835L;

    /**
     * 注册类型
     */
    public enum RegType {
        /**
         * 手机
         */
        mobile,
        /**
         * 邮箱
         */
        email,
        /**
         * 账号
         */
        account
    }

    /**
     * 性别
     */
    public enum Gender {
        /**
         * 男
         */
        male,
        /**
         * 女
         */
        female
    }

    /**
     * 绑定状态
     */
    public enum BindStatus {
        /**
         * 没绑定
         */
        none,
        /**
         * 已绑定
         */
        binded,
        /**
         * 已解绑
         */
        unbind
    }

    /**
     * 用户角色
     */
    public enum Role {
        /**
         * 普通会员
         */
        ordinaryMember,
        /**
         * 安装商
         */
        installer,
        /**
         * 销售商
         */
        vendors,
        /**
         * 经销商
         */
        dealers
    }

    /**
     * 锁定类型
     */
    public enum LockType {
        /**
         * 无锁定
         */
        none,
        /**
         * 锁定
         */
        locked,
        /**
         * 冻结
         */
        freezed
    }
    /**
     * 会员来源类型
     */
    public enum SourceType {
        /**
         * 未知
         */
        none,
        /**
         * 邀请码
         */
        code,
        /**
         * 红包
         */
        red,
        /**
         * 代金券
         */
        coupon,
        /**
         * 购物屏
         */
        pad,
        /**
         * wifi
         */
        wifi,
        /**
         * 个人
         */
        personal
    }

    /**
     * "用户会话"参数名称
     */
    public static final String SESSION_ATTRIBUTE_NAME = "MEMBER.SESSION";

    /**
     * 微信openid
     */
    public static final String WEIXIN_OPENT_ID = "WEIXIN.OPENID";

    /**
     * "推广员"参数名称
     */
    public static final String EXTENSION_ATTRIBUTE_NAME = "MEMBER.EXTENSION";

    /**
     * "身份信息"参数名称
     */
    public static final String PRINCIPAL_ATTRIBUTE_NAME = "MEMBER.PRINCIPAL";

    /**
     * "所属区域"参数名称
     */
    public static final String AREA_ATTRIBUTE_NAME = "MEMBER.AREA";

    /**
     * "校证码"参数名称
     */
    public static final String CHALLEGE_ATTRIBUTE_NAME = "MEMBER.CAPTCHA";

    /** "接口权限"参数名称 */
    //public static final String INTERFACE_ATTRIBUTE_NAME = "MEMBER.INTERFACE";

    /**
     * "用户名"Cookie名称
     */
    public static final String USERNAME_COOKIE_NAME = "username";

    /**
     * "令牌"Cookie名称
     */
    public static final String TOKEN_COOKIE_NAME = "token";

    /**
     * 会员注册项值属性个数
     */
    public static final int ATTRIBUTE_VALUE_PROPERTY_COUNT = 10;

    /**
     * 会员注册项值属性名称前缀
     */
    public static final String ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX = "attributeValue";

    /**
     * 最大收藏商品数
     */
    public static final Integer MAX_FAVORITE_COUNT = 50;

    /**
     * 用户名
     */
    @Expose
    @JsonProperty
    @NotEmpty(groups = Save.class)
    @Pattern(regexp = "^[0-9a-z_A-Z\\u4e00-\\u9fa5]+$|^(\\w+)([\\-+.\\'][\\w]+)*@(\\w[\\-\\w]*\\.){1,5}([A-Za-z]){2,6}$")
    @Column(nullable = false, unique = true, length = 100)
    private String username;

    /**
     * 密码
     */
    @Expose
    @NotEmpty(groups = Save.class)
    @Pattern(regexp = "^[^\\s&\"<>]+$")
    @Column(nullable = false)
    private String password;

    /**
     * 支付密码
     */
    @NotEmpty(groups = Save.class)
    @Pattern(regexp = "^[^\\s&\"<>]+$")
    //@Column(nullable = false)
    private String paymentPassword;

    /**
     * E-mail
     */
    @Email
    @Length(max = 200)
    //@Column(nullable = false)
    private String email;

    /**
     * E-mail绑定
     */
    @Expose
    //@Column(nullable = false)
    private BindStatus bindEmail;

    /**
     * 积分
     */
    @Min(0)
    private Long point;

    /**
     * 评分
     */
    @Min(0)
    @Column(nullable = false, precision = 12, scale = 6)
    private Float score;

    /**
     * 总评分
     */
    @Min(0)
    @Column(nullable = false, columnDefinition = "bigint default 0")
    private Long totalScore;

    /**
     * 评分数
     */
    @Min(0)
    @Column(nullable = false, columnDefinition = "bigint default 0")
    private Long scoreCount;

    /**
     * 消费金额
     */
    @Expose
    @JsonProperty
    @Column(nullable = false, precision = 27, scale = 12)
    private BigDecimal amount;

    /**
     * 消费返利
     */
    @Expose
    @Column(nullable = false, precision = 27, scale = 12)
    private BigDecimal profitAmount;

    /**
     * 代理收益
     */
    @Expose
    @Column(nullable = false, precision = 27, scale = 12)
    private BigDecimal rebateAmount;

    /**
     * 冻结余额
     */
    @Min(0)
    @Column(nullable = false, precision = 27, scale = 12, columnDefinition = "decimal default 0")
    private BigDecimal freezeBalance;

    /**
     * 特权数
     */
    @Expose
    @JsonProperty
    @Column(nullable = false)
    private Integer privilege;

    /**
     * 余额
     */
    @Expose
    @JsonProperty
    @NotNull(groups = Save.class)
    @Min(0)
    @Column(nullable = false, precision = 27, scale = 12)
    private BigDecimal balance;

    /**
     * 冻结提现余额
     */
    @Expose
    @JsonProperty
    @NotNull(groups = Save.class)
    @Min(0)
    @Column(nullable = false, precision = 27, scale = 12)
    private BigDecimal freezeCashBalance;
    
    /**
     * 结算余额
     */
    @Expose
    @JsonProperty
    @NotNull(groups = Save.class)
    @Min(0)
    @Column(nullable = false, precision = 27, scale = 12)
    private BigDecimal clearBalance;

    /**
     * 是否启用
     */
    @Expose
    @NotNull
    @Column(nullable = false)
    private Boolean isEnabled;

    /**
     * 是否锁定
     */
    @Expose
    @Column(nullable = false)
    private LockType isLocked;

    /**
     * 连续登录失败次数
     */
    @Expose
    @Column(nullable = false)
    private Integer loginFailureCount;

    /**
     * 登录成功次数
     */
    @Expose
    @Column(columnDefinition = "decimal default 0")
    private Integer loginCount;


    /**
     * 粉丝数
     */
    @Expose
    @Column(nullable = false, columnDefinition = "decimal default 0")
    private Integer fans;

    /**
     * 锁定日期
     */
    @Expose
    private Date lockedDate;

    /**
     * 注册IP
     */
    @Expose
    @Column(nullable = false, updatable = false)
    private String registerIp;
    /**
     * 会员来源
     */
    @Expose
    @Column(nullable = false)
    private SourceType sourceType;

    /**
     * 最后登录IP
     */
    @Expose
    private String loginIp;

    /**
     * 最后登录日期
     */
    @Expose
    private Date loginDate;

    /**
     * 姓名
     */
    @Expose
    @JsonProperty
    @Length(max = 200)
    private String name;

    /**
     * 昵称
     */
    @Expose
    @JsonProperty
    @Length(max = 200)
    private String nickName;

    /**
     * 头像
     */
    @Expose
    @JsonProperty
    @Length(max = 255)
    private String headImg;

    /**
     * 性别
     */
    @Expose
    @JsonProperty
    private Gender gender;

    /**
     * 出生日期
     */
    @Expose
    @JsonProperty
    private Date birth;

    /**
     * 地址
     */
    @Expose
    @JsonProperty
    @Length(max = 200)
    private String address;

    /**
     * 邮编
     */
    @Expose
    @JsonProperty
    @Length(max = 200)
    private String zipCode;

    /**
     * 电话
     */
    @Expose
    @JsonProperty
    @Length(max = 200)
    private String phone;

    /**
     * 手机
     */
    @Length(max = 200)
    private String mobile;

    /**
     * 提现手续费
     */
    @Column(precision = 27, scale = 12)
    private BigDecimal withdrawCashScale;

    /**
     * 手机 绑定
     */
    @Expose
    @Column(nullable = false)
    private BindStatus bindMobile;

    /**
     * 会员注册项值0
     */
    @Length(max = 200)
    private String attributeValue0;

    /**
     * 会员注册项值1
     */
    @Length(max = 200)
    private String attributeValue1;

    /**
     * 会员注册项值2
     */
    @Length(max = 200)
    private String attributeValue2;

    /**
     * 会员注册项值3
     */
    @Length(max = 200)
    private String attributeValue3;

    /**
     * 会员注册项值4
     */
    @Length(max = 200)
    private String attributeValue4;

    /**
     * 会员注册项值5
     */
    @Length(max = 200)
    private String attributeValue5;

    /**
     * 会员注册项值6
     */
    @Length(max = 200)
    private String attributeValue6;

    /**
     * 会员注册项值7
     */
    @Length(max = 200)
    private String attributeValue7;

    /**
     * 会员注册项值8
     */
    @Length(max = 200)
    private String attributeValue8;

    /**
     * 会员注册项值9
     */
    @Length(max = 200)
    private String attributeValue9;

    /**
     * 安全密匙
     */
    @Embedded
    private SafeKey safeKey;

    /**
     * 地区
     */
    @Expose
    @JsonProperty
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Area area;

    /**
     * 地理位置
     */
    @Embedded
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    private Area lbsCity;
    /**
     * 更新日期
     */
    @Expose
    private Date lbsDate;

    /**
     * 会员等级
     */
    @Expose
    @JsonProperty
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private MemberRank memberRank;

    /**
     * 实名认证
     */
    @Expose
    @JsonProperty
    @ManyToOne(fetch = FetchType.LAZY)
    private Idcard idcard;

    /**
     * 我家店铺
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Tenant tenant;

    /**
     * 所属代理
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    /**
     * 发展店家
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Tenant shareOwner;

    /**
     * 购物车
     */
    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Cart cart;

    /**
     * 订单
     */
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Order> orders = new HashSet<Order>();

    /**
     * 预存款
     */
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Deposit> deposits = new HashSet<Deposit>();

    /**
     * 收款单
     */
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Payment> payments = new HashSet<Payment>();

    /**
     * 优惠码
     */
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<CouponCode> couponCodes = new HashSet<CouponCode>();

    /**
     * 收货地址
     */
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OrderBy("isDefault desc, createDate desc")
    private Set<Receiver> receivers = new HashSet<Receiver>();

    /**
     * 评论
     */

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OrderBy("createDate desc")
    private Set<Review> reviews = new HashSet<Review>();

    /**
     * 咨询
     */
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OrderBy("createDate desc")
    private Set<Consultation> consultations = new HashSet<Consultation>();

    /**
     * 收藏商品
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "xx_member_favorite_product")
    @OrderBy("createDate desc")
    private Set<Product> favoriteProducts = new HashSet<Product>();

    /**
     * 我的好友
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "xx_member_favorite_member")
    @OrderBy("createDate desc")
    private Set<Member> favoriteMembers = new HashSet<Member>();

    /**
     * 关注商家
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "xx_member_favorite_tenant")
    @OrderBy("createDate desc")
    private Set<Tenant> favoriteTenants = new HashSet<Tenant>();

    /**
     * 到货通知
     */
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<ProductNotify> productNotifies = new HashSet<ProductNotify>();

    /**
     * 接收的消息
     */
    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Message> inMessages = new HashSet<Message>();

    /**
     * 发送的消息
     */
    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Message> outMessages = new HashSet<Message>();

    /**
     * 绑定银行卡
     */
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<MemberBank> memberBanks = new HashSet<MemberBank>();

    /**
     * 申请促销方案
     */
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Promotion> promotions = new HashSet<Promotion>();

    /**
     * 参与促销
     */
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<PromotionMember> promotionMembers = new HashSet<PromotionMember>();

    /**
     * 登录角色ID
     */
    private String login_id;

    /**
     * 微信号
     */
    private String wechatId;

    /**
     * 极光IM
     */
    private Boolean jmessage;

    /**
     * 环信IM
     */
    private Boolean emessage;

    /**
     * 会员信息
     */
    @Expose
    @JsonProperty
    @OneToOne(fetch = FetchType.LAZY)
    private MemberParameter memberParameter;
    /**
     * 获取会员注册项值
     *
     * @param memberAttribute 会员注册项
     * @return 会员注册项值
     */
    public Object getAttributeValue(MemberAttribute memberAttribute) {
        if (memberAttribute != null) {
            if (memberAttribute.getType() == Type.name) {
                return getName();
            } else if (memberAttribute.getType() == Type.gender) {
                return getGender();
            } else if (memberAttribute.getType() == Type.birth) {
                return getBirth();
            } else if (memberAttribute.getType() == Type.area) {
                return getArea();
            } else if (memberAttribute.getType() == Type.address) {
                return getAddress();
            } else if (memberAttribute.getType() == Type.zipCode) {
                return getZipCode();
            } else if (memberAttribute.getType() == Type.phone) {
                return getPhone();
            } else if (memberAttribute.getType() == Type.mobile) {
                return getMobile();
            } else if (memberAttribute.getType() == Type.checkbox) {
                if (memberAttribute.getPropertyIndex() != null) {
                    try {
                        String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + memberAttribute.getPropertyIndex();
                        String propertyValue = (String) PropertyUtils.getProperty(this, propertyName);
                        if (propertyValue != null) {
                            return JsonUtils.toObject(propertyValue, List.class);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (memberAttribute.getPropertyIndex() != null) {
                    try {
                        String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + memberAttribute.getPropertyIndex();
                        return (String) PropertyUtils.getProperty(this, propertyName);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    /**
     * 设置会员注册项值
     *
     * @param memberAttribute 会员注册项
     * @param attributeValue  会员注册项值
     */
    public void setAttributeValue(MemberAttribute memberAttribute, Object attributeValue) {
        if (memberAttribute != null) {
            if (attributeValue instanceof String && StringUtils.isEmpty((String) attributeValue)) {
                attributeValue = null;
            }
            if (memberAttribute.getType() == Type.name && (attributeValue instanceof String || attributeValue == null)) {
                setName((String) attributeValue);
            } else if (memberAttribute.getType() == Type.gender && (attributeValue instanceof Gender || attributeValue == null)) {
                setGender((Gender) attributeValue);
            } else if (memberAttribute.getType() == Type.birth && (attributeValue instanceof Date || attributeValue == null)) {
                setBirth((Date) attributeValue);
            } else if (memberAttribute.getType() == Type.area && (attributeValue instanceof Area || attributeValue == null)) {
                setArea((Area) attributeValue);
            } else if (memberAttribute.getType() == Type.address && (attributeValue instanceof String || attributeValue == null)) {
                setAddress((String) attributeValue);
            } else if (memberAttribute.getType() == Type.zipCode && (attributeValue instanceof String || attributeValue == null)) {
                setZipCode((String) attributeValue);
            } else if (memberAttribute.getType() == Type.phone && (attributeValue instanceof String || attributeValue == null)) {
                setPhone((String) attributeValue);
            } else if (memberAttribute.getType() == Type.mobile && (attributeValue instanceof String || attributeValue == null)) {
                setMobile((String) attributeValue);
            } else if (memberAttribute.getType() == Type.checkbox && (attributeValue instanceof List || attributeValue == null)) {
                if (memberAttribute.getPropertyIndex() != null) {
                    if (attributeValue == null || (memberAttribute.getOptions() != null && memberAttribute.getOptions().containsAll((List<?>) attributeValue))) {
                        try {
                            String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + memberAttribute.getPropertyIndex();
                            PropertyUtils.setProperty(this, propertyName, JsonUtils.toJson(attributeValue));
                        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                if (memberAttribute.getPropertyIndex() != null) {
                    try {
                        String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + memberAttribute.getPropertyIndex();
                        PropertyUtils.setProperty(this, propertyName, attributeValue);
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public AuthStatus getAuthStatus() {
        Idcard idcard = this.getIdcard();
        if (idcard == null) {
            return Idcard.AuthStatus.none;
        } else {
            return idcard.getAuthStatus();
        }
    }

    /**
     * 移除所有会员注册项值
     */
    public void removeAttributeValue() {
        setName(null);
        setGender(null);
        setBirth(null);
        setArea(null);
        setAddress(null);
        setZipCode(null);
        setPhone(null);
        for (int i = 0; i < ATTRIBUTE_VALUE_PROPERTY_COUNT; i++) {
            String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + i;
            try {
                PropertyUtils.setProperty(this, propertyName, null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public Receiver getDefaultReceiver() {
        if (getReceivers() == null || getReceivers().size() <= 0) {
            return null;
        }
        for (Receiver receiver : getReceivers()) {
            if (receiver.getIsDefault()) {
                return receiver;
            }
        }
        return null;
    }

    /**
     * 获取体现手续费
     */
    public BigDecimal getBaseWithdrawCashScale() {
        return withdrawCashScale == null ? SettingUtils.get().getWithdrawCashScale() : this.withdrawCashScale;
    }

    /**
     * 诚信积分等级换算
     */
    public String getCreditLevel() {
        Long score = getTotalScore();
        if (score == null || score <= 0) {
            return "000";
        }
        BigDecimal big = new BigDecimal(score).subtract(new BigDecimal(Math.max(0, -score + 2)));
        int level = new BigDecimal(Math.sqrt(big.add(SettingConstant.scoreParams20).divide(SettingConstant.scoreParams5).doubleValue())).subtract(SettingConstant.scoreParams1).intValue();
        if (level >= 125) {
            return "500";
        }
        int[] group = new int[]{0, 0, 0};
        group[0] = level / 25;
        group[1] = level % 25;
        if (group[1] / 5 > 1) {
            group[1] = group[1] / 5;
            group[2] = group[1] % 5;
        } else {
            group[2] = group[1];
            group[1] = 0;
        }
        return group[0] + "" + group[1] + "" + group[2];
    }

    /**
     * 获取会员卡号码
     */
    @JsonProperty
    public String getMemberCardNo() {
        if (getId() == null) {
            return "";
        }
        return getId().toString();
    }

    // ===========================================getter/setter===========================================//

    /**
     * 获取登录角色
     *
     * @return
     */
    public String getLogin_id() {
        return login_id;
    }

    public void setLogin_id(String login_id) {
        this.login_id = login_id;
    }

    /**
     * 获取用户名
     *
     * @return 用户名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username 用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取密码
     *
     * @return 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取支付密码
     *
     * @return 支付密码
     */
    public String getPaymentPassword() {
        return paymentPassword;
    }


    public Boolean getJmessage() {
        return jmessage;
    }

    public void setJmessage(Boolean jmessage) {
        this.jmessage = jmessage;
    }

    /**
     * 设置支付密码
     *
     * @param paymentPassword 支付密码
     */
    public void setPaymentPassword(String paymentPassword) {
        this.paymentPassword = paymentPassword;
    }

    /**
     * 获取E-mail
     *
     * @return E-mail
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置E-mail
     *
     * @param email E-mail
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public BindStatus getBindEmail() {
        return bindEmail;
    }

    public void setBindEmail(BindStatus bindEmail) {
        this.bindEmail = bindEmail;
    }

    /**
     * 获取积分
     *
     * @return 积分
     */
    public Long getPoint() {
        return point;
    }

    /**
     * 设置积分
     *
     * @param point 积分
     */
    public void setPoint(Long point) {
        this.point = point;
    }

    /**
     * 获取总评分
     *
     * @return 总评分
     */
    public Long getTotalScore() {
        return totalScore;
    }

    /**
     * 设置总评分
     *
     * @param totalScore 总评分
     */
    public void setTotalScore(Long totalScore) {
        this.totalScore = totalScore;
    }

    /**
     * 获取消费金额
     *
     * @return 消费金额
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置消费金额
     *
     * @param amount 消费金额
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 获取消费返利
     *
     * @return 消费返利
     */
    public BigDecimal getProfitAmount() {
        return profitAmount;
    }

    /**
     * 设置消费返利
     *
     * @param amount 消费返利
     */
    public void setProfitAmount(BigDecimal profitAmount) {
        this.profitAmount = profitAmount;
    }

    /**
     * 获取消推广收益
     *
     * @return 推广收益
     */
    public BigDecimal getRebateAmount() {
        return rebateAmount;
    }

    /**
     * 设置推广收益
     *
     * @param amount 推广收益
     */
    public void setRebateAmount(BigDecimal rebateAmount) {
        this.rebateAmount = rebateAmount;
    }

    /**
     * 冻结余额
     *
     * @return 冻结余额
     */
    public BigDecimal getFreezeBalance() {
        return freezeBalance;
    }

    /**
     * 冻结余额
     *
     * @param freezeBalance 冻结余额
     */
    public void setFreezeBalance(BigDecimal freezeBalance) {
        this.freezeBalance = freezeBalance;
    }

    /**
     * 特权数
     *
     * @return 特权数
     */
    public Integer getPrivilege() {
        return privilege;
    }

    public void setPrivilege(Integer privilege) {
        this.privilege = privilege;
    }

    /**
     * 获取余额
     *
     * @return 余额
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * 设置余额
     *
     * @param balance 余额
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * 获取结算余额
     *
     * @return 结算余额
     */
    public BigDecimal getClearBalance() {
        return clearBalance;
    }

    /**
     * 设置结算余额
     *
     * @param balance 结算余额
     */
    public void setClearBalance(BigDecimal clearBalance) {
        this.clearBalance = clearBalance;
    }

    public BigDecimal getWithdrawCashScale() {
        return withdrawCashScale;
    }

    public void setWithdrawCashScale(BigDecimal withdrawCashScale) {
        this.withdrawCashScale = withdrawCashScale;
    }
    
    public BigDecimal getFreezeCashBalance() {
		return freezeCashBalance;
	}

	public void setFreezeCashBalance(BigDecimal freezeCashBalance) {
		this.freezeCashBalance = freezeCashBalance;
	}

	public Integer getFans() {
        return fans;
    }

    public void setFans(Integer fans) {
        this.fans = fans;
    }

    /**
     * 获取是否启用
     *
     * @return 是否启用
     */
    public Boolean getIsEnabled() {
        return isEnabled;
    }

    /**
     * 设置是否启用
     *
     * @param isEnabled 是否启用
     */
    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    /**
     * 获取是否锁定
     *
     * @return 是否锁定
     */
    public LockType getIsLocked() {
        return isLocked;
    }

    /**
     * 设置是否锁定
     *
     * @param isLocked 是否锁定
     */
    public void setIsLocked(LockType isLocked) {
        this.isLocked = isLocked;
    }

    /**
     * 获取连续登录失败次数
     *
     * @return 连续登录失败次数
     */
    public Integer getLoginFailureCount() {
        return loginFailureCount;
    }

    /**
     * 设置连续登录失败次数
     *
     * @param loginFailureCount 连续登录失败次数
     */
    public void setLoginFailureCount(Integer loginFailureCount) {
        this.loginFailureCount = loginFailureCount;
    }

    /**
     * 获取锁定日期
     *
     * @return 锁定日期
     */
    public Date getLockedDate() {
        return lockedDate;
    }

    /**
     * 设置锁定日期
     *
     * @param lockedDate 锁定日期
     */
    public void setLockedDate(Date lockedDate) {
        this.lockedDate = lockedDate;
    }

    /**
     * 获取注册IP
     *
     * @return 注册IP
     */
    public String getRegisterIp() {
        return registerIp;
    }

    /**
     * 设置注册IP
     *
     * @param registerIp 注册IP
     */
    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    /**
     * 获取最后登录IP
     *
     * @return 最后登录IP
     */
    public String getLoginIp() {
        return loginIp;
    }

    /**
     * 设置最后登录IP
     *
     * @param loginIp 最后登录IP
     */
    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    /**
     * 获取最后登录日期
     *
     * @return 最后登录日期
     */
    public Date getLoginDate() {
        return loginDate;
    }

    /**
     * 设置最后登录日期
     *
     * @param loginDate 最后登录日期
     */
    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    /**
     * 获取姓名
     *
     * @return 姓名
     */
    @JsonProperty
    public String getName() {
        return name;
    }

    /**
     * 设置姓名
     *
     * @param name 姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取头像
     *
     * @return 头像
     */
    @JsonProperty
    public String getHeadImg() {
        return headImg;
    }

    /**
     * 设置头像
     *
     * @param headImg 头像
     */
    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    /**
     * 获取性别
     *
     * @return 性别
     */

    public Gender getGender() {
        return gender;
    }

    /**
     * 设置性别
     *
     * @param gender 性别
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * 获取出生日期
     *
     * @return 出生日期
     */

    public Date getBirth() {
        return birth;
    }

    /**
     * 设置出生日期
     *
     * @param birth 出生日期
     */
    public void setBirth(Date birth) {
        this.birth = birth;
    }

    /**
     * 获取地址
     *
     * @return 地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置地址
     *
     * @param address 地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取邮编
     *
     * @return 邮编
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * 设置邮编
     *
     * @param zipCode 邮编
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * 获取电话
     *
     * @return 电话
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置电话
     *
     * @param phone 电话
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取手机
     *
     * @return 手机
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机
     *
     * @param mobile 手机
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public BindStatus getBindMobile() {
        return bindMobile;
    }

    public void setBindMobile(BindStatus bindMobile) {
        this.bindMobile = bindMobile;
    }

    /**
     * 获取会员注册项值0
     *
     * @return 会员注册项值0
     */
    public String getAttributeValue0() {
        return attributeValue0;
    }

    /**
     * 设置会员注册项值0
     *
     * @param attributeValue0 会员注册项值0
     */
    public void setAttributeValue0(String attributeValue0) {
        this.attributeValue0 = attributeValue0;
    }

    /**
     * 获取会员注册项值1
     *
     * @return 会员注册项值1
     */
    public String getAttributeValue1() {
        return attributeValue1;
    }

    /**
     * 设置会员注册项值1
     *
     * @param attributeValue1 会员注册项值1
     */
    public void setAttributeValue1(String attributeValue1) {
        this.attributeValue1 = attributeValue1;
    }

    /**
     * 获取会员注册项值2
     *
     * @return 会员注册项值2
     */
    public String getAttributeValue2() {
        return attributeValue2;
    }

    /**
     * 设置会员注册项值2
     *
     * @param attributeValue2 会员注册项值2
     */
    public void setAttributeValue2(String attributeValue2) {
        this.attributeValue2 = attributeValue2;
    }

    /**
     * 获取会员注册项值3
     *
     * @return 会员注册项值3
     */
    public String getAttributeValue3() {
        return attributeValue3;
    }

    /**
     * 设置会员注册项值3
     *
     * @param attributeValue3 会员注册项值3
     */
    public void setAttributeValue3(String attributeValue3) {
        this.attributeValue3 = attributeValue3;
    }

    /**
     * 获取会员注册项值4
     *
     * @return 会员注册项值4
     */
    public String getAttributeValue4() {
        return attributeValue4;
    }

    /**
     * 设置会员注册项值4
     *
     * @param attributeValue4 会员注册项值4
     */
    public void setAttributeValue4(String attributeValue4) {
        this.attributeValue4 = attributeValue4;
    }

    /**
     * 获取会员注册项值5
     *
     * @return 会员注册项值5
     */
    public String getAttributeValue5() {
        return attributeValue5;
    }

    /**
     * 设置会员注册项值5
     *
     * @param attributeValue5 会员注册项值5
     */
    public void setAttributeValue5(String attributeValue5) {
        this.attributeValue5 = attributeValue5;
    }

    /**
     * 获取会员注册项值6
     *
     * @return 会员注册项值6
     */
    public String getAttributeValue6() {
        return attributeValue6;
    }

    /**
     * 设置会员注册项值6
     *
     * @param attributeValue6 会员注册项值6
     */
    public void setAttributeValue6(String attributeValue6) {
        this.attributeValue6 = attributeValue6;
    }

    /**
     * 获取会员注册项值7
     *
     * @return 会员注册项值7
     */
    public String getAttributeValue7() {
        return attributeValue7;
    }

    /**
     * 设置会员注册项值7
     *
     * @param attributeValue7 会员注册项值7
     */
    public void setAttributeValue7(String attributeValue7) {
        this.attributeValue7 = attributeValue7;
    }

    /**
     * 获取会员注册项值8
     *
     * @return 会员注册项值8
     */
    public String getAttributeValue8() {
        return attributeValue8;
    }

    /**
     * 设置会员注册项值8
     *
     * @param attributeValue8 会员注册项值8
     */
    public void setAttributeValue8(String attributeValue8) {
        this.attributeValue8 = attributeValue8;
    }

    /**
     * 获取会员注册项值9
     *
     * @return 会员注册项值9
     */
    public String getAttributeValue9() {
        return attributeValue9;
    }

    /**
     * 设置会员注册项值9
     *
     * @param attributeValue9 会员注册项值9
     */
    public void setAttributeValue9(String attributeValue9) {
        this.attributeValue9 = attributeValue9;
    }

    /**
     * 获取安全密匙
     *
     * @return 安全密匙
     */
    public SafeKey getSafeKey() {
        return safeKey;
    }

    /**
     * 设置安全密匙
     *
     * @param safeKey 安全密匙
     */
    public void setSafeKey(SafeKey safeKey) {
        this.safeKey = safeKey;
    }

    /**
     * 获取地区
     *
     * @return 地区
     */
    public Area getArea() {
        return area;
    }

    /**
     * 设置地区
     *
     * @param area 地区
     */
    public void setArea(Area area) {
        this.area = area;
    }

    /**
     * 获取经纬度
     *
     * @return 经纬度
     */
    public Location getLocation() {
        return location;
    }

    /**
     * 设置经纬度
     *
     * @param location 经纬度
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * 获取会员等级
     *
     * @return 会员等级
     */
    public MemberRank getMemberRank() {
        return memberRank;
    }

    /**
     * 设置会员等级
     *
     * @param memberRank 会员等级
     */
    public void setMemberRank(MemberRank memberRank) {
        this.memberRank = memberRank;
    }

    /**
     * 获取实名认证
     *
     * @return 实名认证
     */
    public Idcard getIdcard() {
        return idcard;
    }

    /**
     * 设置实名认证
     *
     * @param idcard 实名认证
     */
    public void setIdcard(Idcard idcard) {
        this.idcard = idcard;
    }

    /**
     * 获取企业
     *
     * @return 所属企业
     */
    public Tenant getTenant() {
        return tenant;
    }

    /**
     * 设置企业
     *
     * @param tenant 所属企业
     */
    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    /**
     * 所属代理
     *
     * @return 代理
     */
    public Member getMember() {
        return member;
    }

    /**
     * 设置代理
     *
     * @param member 代理
     */
    public void setMember(Member member) {
        this.member = member;
    }

    public Tenant getShareOwner() {
        return shareOwner;
    }

    public void setShareOwner(Tenant shareOwner) {
        this.shareOwner = shareOwner;
    }

    /**
     * 获取购物车
     *
     * @return 购物车
     */
    public Cart getCart() {
        return cart;
    }

    /**
     * 设置购物车
     *
     * @param cart 购物车
     */
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    /**
     * 获取订单
     *
     * @return 订单
     */
    public Set<Order> getOrders() {
        return orders;
    }

    /**
     * 设置订单
     *
     * @param orders 订单
     */
    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    /**
     * 获取预存款
     *
     * @return 预存款
     */
    public Set<Deposit> getDeposits() {
        return deposits;
    }

    /**
     * 设置预存款
     *
     * @param deposits 预存款
     */
    public void setDeposits(Set<Deposit> deposits) {
        this.deposits = deposits;
    }

    /**
     * 获取收款单
     *
     * @return 收款单
     */
    public Set<Payment> getPayments() {
        return payments;
    }

    /**
     * 设置收款单
     *
     * @param payments 收款单
     */
    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }

    /**
     * 获取优惠码
     *
     * @return 优惠码
     */
    public Set<CouponCode> getCouponCodes() {
        return couponCodes;
    }

    /**
     * 设置优惠码
     *
     * @param couponCodes 优惠码
     */
    public void setCouponCodes(Set<CouponCode> couponCodes) {
        this.couponCodes = couponCodes;
    }

    /**
     * 获取收货地址
     *
     * @return 收货地址
     */
    public Set<Receiver> getReceivers() {
        return receivers;
    }

    /**
     * 设置收货地址
     *
     * @param receivers 收货地址
     */
    public void setReceivers(Set<Receiver> receivers) {
        this.receivers = receivers;
    }

    /**
     * 获取评论
     *
     * @return 评论
     */
    public Set<Review> getReviews() {
        return reviews;
    }

    /**
     * 设置评论
     *
     * @param reviews 评论
     */
    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    /**
     * 获取咨询
     *
     * @return 咨询
     */
    public Set<Consultation> getConsultations() {
        return consultations;
    }

    /**
     * 设置咨询
     *
     * @param consultations 咨询
     */
    public void setConsultations(Set<Consultation> consultations) {
        this.consultations = consultations;
    }

    /**
     * 获取收藏商品
     *
     * @return 收藏商品
     */
    public Set<Product> getFavoriteProducts() {
        return favoriteProducts;
    }

    /**
     * 设置收藏商品
     *
     * @param favoriteProducts 收藏商品
     */
    public void setFavoriteProducts(Set<Product> favoriteProducts) {
        this.favoriteProducts = favoriteProducts;
    }

    /**
     * 获取关注商家
     *
     * @return 关注商家
     */
    public Set<Tenant> getFavoriteTenants() {
        return favoriteTenants;
    }

    /**
     * 设置关注商家
     *
     * @param favoriteProducts 关注商家
     */
    public void setFavoriteTenants(Set<Tenant> favoriteTenants) {
        this.favoriteTenants = favoriteTenants;
    }


    public Set<Member> getFavoriteMembers() {
        return favoriteMembers;
    }

    public void setFavoriteMembers(Set<Member> favoriteMembers) {
        this.favoriteMembers = favoriteMembers;
    }

    public Set<Promotion> getPromotions() {
        return promotions;
    }

    /**
     * 设置促销方案
     *
     * @param promotions 促销方案
     */
    public void setPromotions(Set<Promotion> promotions) {
        this.promotions = promotions;
    }

    public Set<PromotionMember> getPromotionMembers() {
        return promotionMembers;
    }

    public void setPromotionMembers(Set<PromotionMember> promotionMembers) {
        this.promotionMembers = promotionMembers;
    }

    /**
     * 获取到货通知
     *
     * @return 到货通知
     */

    public Set<ProductNotify> getProductNotifies() {
        return productNotifies;
    }

    /**
     * 设置到货通知
     *
     * @param productNotifies 到货通知
     */
    public void setProductNotifies(Set<ProductNotify> productNotifies) {
        this.productNotifies = productNotifies;
    }

    /**
     * 获取接收的消息
     *
     * @return 接收的消息
     */

    public Set<Message> getInMessages() {
        Set<Message> messages = new HashSet<Message>();
        for (Message message : inMessages) {
            if (message.getReceiverDelete() == false) {
                messages.add(message);
            }
        }
        return messages;
    }

    /**
     * 设置接收的消息
     *
     * @param inMessages 接收的消息
     */
    public void setInMessages(Set<Message> inMessages) {
        this.inMessages = inMessages;
    }

    /**
     * 获取发送的消息
     *
     * @return 发送的消息
     */

    public Set<Message> getOutMessages() {
        return outMessages;
    }

    /**
     * 设置发送的消息
     *
     * @param outMessages 发送的消息
     */
    public void setOutMessages(Set<Message> outMessages) {
        this.outMessages = outMessages;
    }

    /**
     * 获取银行卡
     *
     * @return 银行卡
     */

    public Set<MemberBank> getMemberBanks() {
        return memberBanks;
    }

    /**
     * 设置银行卡
     *
     * @param outMessages 银行卡
     */
    public void setMemberBanks(Set<MemberBank> memberBanks) {
        this.memberBanks = memberBanks;
    }

    public MemberParameter getMemberParameter() {
        return memberParameter;
    }

    public void setMemberParameter(MemberParameter memberParameter) {
        this.memberParameter = memberParameter;
    }

    public Date getLbsDate() {
        return lbsDate;
    }

    public void setLbsDate(Date lbsDate) {
        this.lbsDate = lbsDate;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Long getScoreCount() {
        return scoreCount;
    }

    public void setScoreCount(Long scoreCount) {
        this.scoreCount = scoreCount;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Area getLbsCity() {
        return lbsCity;
    }

    public void setLbsCity(Area lbsCity) {
        this.lbsCity = lbsCity;
    }

    public Boolean getEmessage() {
        return emessage;
    }

    public void setEmessage(Boolean emessage) {
        this.emessage = emessage;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public String getDisplayName() {
        if (getName() != null) {
            return getName();
        } else if (getNickName() != null) {
            return getNickName();
        } else {
            return net.wit.util.StringUtils.mosaic(getUsername(), 3, "~~");
        }
    }

    /**
     * 获取订单交易额
     * 2016-12-20 add by yw
     * @return
     */
    public BigDecimal getOrderAmount(){
        BigDecimal orderAmount=BigDecimal.ZERO;
        if(getOrders()!=null){
            for(Order order:getOrders()){
                if(order.getShippingStatus()!= Order.ShippingStatus.unshipped
                  &&(order.getOrderStatus()== Order.OrderStatus.completed||order.getOrderStatus()== Order.OrderStatus.confirmed)){
                    orderAmount=orderAmount.add(order.getAmount());
                }
            }
        }
        return orderAmount;
    }

    /**
     * 获取订单交易量
     * 2016-12-20 add by yw
     * @return
     */
    public Integer getOrderCount(){
        Integer orderCount=0;
        if(getOrders()!=null){
            for(Order order:getOrders()){
                if(order.getShippingStatus()!= Order.ShippingStatus.unshipped
                        &&(order.getOrderStatus()== Order.OrderStatus.completed||order.getOrderStatus()== Order.OrderStatus.confirmed)){
                    orderCount=orderCount+1;
                }
            }
        }
        return orderCount;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }
}