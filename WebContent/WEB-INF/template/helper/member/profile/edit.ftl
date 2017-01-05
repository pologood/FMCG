<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("shop.member.profile.edit")}[#if systemShowPowered][/#if]</title>
    <link href="${base}/resources/helper/css/common.css" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" href="${base}/resources/helper/css/product.css">


    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/search.js"></script>
    <script type="text/javascript" src="${base}/resources/common/jcrop/js/jquery.Jcrop.min.js"></script>
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {

            var $inputForm = $("#inputForm");
            var $areaId = $("#areaId");
            var $browserButton = $("#browserButton");
            var $shortName = $("#shortName");
            var $submit = $(":submit");

        [@flash_message /]

            function addImage(url, local) {
                $("#unload_img").attr("src", url);
                $("#headImg").val(url);
            }

            var settings = {
                width: 360,
                height: 360,
                callback: addImage
            }

            $browserButton.browser(settings);

            // 地区选择
            $areaId.lSelect({
                url: "${base}/common/area.jhtml"
            });
            //清除火狐浏览器刷新多出拉下框
            $("select[name='memberAttribute_4_select']").each(function () {
                if ($(this).val() == "") {
                    $(this).nextAll("select").remove();
                    return false;
                }
                ;
            });
            ;

            //店铺名称唯一验证
            $shortName.blur(function () {
                var shortName = $(this).val();
                if (shortName.trim() != "") {
                    $.ajax({
                        url: "${base}/helper/member/profile/checkShortName.jhtml",
                        type: "get",
                        data: {"shortName": shortName},
                        dataType: "json",
                        success: function (data) {
                            if (data) {
                                $("#checkMsg").text("(店铺名称已存在！)");
                                $submit.prop("disabled", true);
                            } else {
                                $("#checkMsg").text("");
                                $submit.prop("disabled", false);
                            }
                        }
                    });
                } else {
                    $("#checkMsg").text("");
                    $submit.prop("disabled", false);
                }
            });

            // 表单验证
            $inputForm.validate({
                rules: {
                    email: {
                        required: false
                    }
                [#list memberAttributes as memberAttribute]
                    [#if memberAttribute.isRequired]
                        , memberAttribute_${memberAttribute.id}: {
                        required: true
                    }
                    [/#if]
                    [#if memberAttribute.type == "phone"]
                        [#if !memberAttribute.isRequired]
                            , memberAttribute_${memberAttribute.id}: {
                            pattern: /^((\d{3,4}-\d{7,8})|(1[23456798]\d{9}))$/
                        }
                        [/#if]
                    [/#if]
                    [#if memberAttribute.type == "zipCode"]
                        [#if !memberAttribute.isRequired]
                            , memberAttribute_${memberAttribute.id}: {
                            pattern: /^\d{3,6}$/
                        }
                        [/#if]
                    [/#if]
                [/#list]
                },
                messages: {
                    email: ''
                [#list memberAttributes as memberAttribute]
                    [#if memberAttribute.isRequired]
                        , memberAttribute_${memberAttribute.id}: {
                        required: '必填'
                    }
                    [/#if]
                    [#if memberAttribute.type == "phone"]
                        [#if !memberAttribute.isRequired]
                            , memberAttribute_${memberAttribute.id}: {
                            pattern: "请填写正确的号码"
                        }
                        [/#if]
                    [/#if]
                    [#if memberAttribute.type == "zipCode"]
                        [#if !memberAttribute.isRequired]
                            , memberAttribute_${memberAttribute.id}: {
                            pattern: "请填写正确的邮编号码"
                        }
                        [/#if]
                    [/#if]
                [/#list]
                }
            });

        });
    </script>
</head>
<body>
[#include "/helper/include/header.ftl" /]
[#include "/helper/member/include/navigation.ftl" /]
<div class="desktop">
    <div class="container bg_fff">

    [#include "/helper/member/include/border.ftl" /]

    [#include "/helper/member/include/menu.ftl" /]

        <div class="wrapper" id="wrapper">


        [#assign current = "profileEdit" /]
            <div class="page-nav page-nav-app vip-guide-nav" id="app_head_nav">
                <div class="js-app-header title-wrap" id="app_0000000844">
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/payment-details2.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">账户信息</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">查看、修改账户基本信息</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="on" hideFocus="" href="${base}/helper/member/profile/edit.jhtml">账户信息</a></li>
                </ul>

            </div>
            <div class="list" style="margin-top:10px;border: solid 1px #d8deea;">
                <form id="inputForm" action="update.jhtml" method="post">
                    <div class="payment_info_div payment_info_div3">
						<span class="fieldSet">
							<img class="unload_img" id="unload_img" src="${member.headImg}"/>
							<input type="hidden" name="headImg" id="headImg" class="text" value="${member.headImg}"
                                   maxlength="200" title="${message("admin.product.imageTitle")}"/>
							<input type="button" class="browserUpLoad" id="browserButton" value="上传头像"/>
						</span>
                    </div>
                    <div class="payment_info_div">
                        账户名称：<span>${mosaic(member.username,4,"***")}</span>
                    </div>
                    <div class="payment_info_div">
                        店铺名称：<input type="text" id="shortName" name="tenantShortName" class="text"
                                    value="${(tenant.shortName)!}" maxlength="20"/>
                        <span id="checkMsg" style="color:red"></span>
                        <span class="mar_lef">*建议使用圈子里熟悉的称呼，方便你的老朋友找到你</span>
                    </div>
                    <div class="payment_info_div">
                    [#assign authStatus=member.authStatus /]
                    [#if member.authStatus == "success"]
                        真实姓名：${(member.name)!}<span class="authen_btn">【已认证】</span>
                        <input type="hidden" class="text" name="name" value="${(member.name)!}"/>
                    [#elseif    member.authStatus == "wait"]
                        真实姓名：${(member.name)!}<span class="authen_btn">【认证中】</span>
                        <input type="hidden" class="text" name="name" value="${(member.name)!}"/>
                    [#else]
                        真实姓名：<input type="text" class="text" name="name" value="${(member.name)!}"/><span
                            class="authen_btn">【未认证】</span><span>*未进行实名制的用户不能在平台提现哦~</span><a class="authen_btn2"
                                                                                              href="${base}/helper/member/safe/idcard.jhtml?urlid=1">去认证</a>
                    [/#if]
                    </div>
                    <div class="payment_info_div">
                    [#if tenant??]
                        [#if tenant.status == "success"]
                            公司名称：${(tenant.name)!}<span class="authen_btn">【已认证】</span>
                            <input type="hidden" class="text" name="tenantName" value="${(tenant.name)!}"/>
                        [#elseif tenant.status == "wait"]
                            公司名称：${(tenant.name)!}<span class="authen_btn">【已认证】</span>
                            <input type="hidden" class="text" name="tenantName" value="${(tenant.name)!}"/>
                        [#else]
                            公司名称：<input type="text" class="text" name="tenantName" value="${(tenant.name)!}"/><span
                                class="authen_btn" href="#">【未认证】</span><span>*只有通过企业验证的买家才能获得VIP超低折扣哦~</span><a
                                class="authen_btn2" href="${base}/helper/member/tenant/add.jhtml?pageActive=1">去认证</a>
                        [/#if]
                    [#else]
                        公司名称：<input type="text" class="text" name="tenantName" value="${(tenant.name)!}"/><span
                            class="authen_btn" href="#">【未认证】</span><span>*只有通过企业验证的买家才能获得VIP超低折扣哦~</span><a
                            class="authen_btn2" href="${base}/helper/member/tenant/add.jhtml?pageActive=1">去认证</a>
                    [/#if]
                    </div>
                [#list memberAttributes as memberAttribute]
                    <div class="payment_info_div">
                        [#if memberAttribute.type != "mobile"]
                            [#if memberAttribute.isRequired]
                            [/#if]
                        ${memberAttribute.name}：
                        [/#if]
                        [#if memberAttribute.type == "name"]
                            [#if authStatus == "success"]
                                <input type="text" name="memberAttribute_${memberAttribute.id}" class="text"
                                       value="${member.name}" maxlength="200" readonly="readonly"/>
                            [#else]
                                <input type="text" name="memberAttribute_${memberAttribute.id}" class="text"
                                       value="${member.name}" maxlength="200"/>
                            [/#if]
                        [#elseif memberAttribute.type == "gender"]
                            <span class="fieldSet">[#list genders as gender]<label><input type="radio"
                                                                                          name="memberAttribute_${memberAttribute.id}"
                                                                                          value="${gender}"[#if gender == member.gender]
                                                                                          checked="checked"[/#if]/>${message("Member.Gender." + gender)}
                            </label>[/#list]</span>
                        [#elseif memberAttribute.type == "birth"]
                            <input type="text" name="memberAttribute_${memberAttribute.id}" class="text"
                                   value="${member.birth}" onfocus="WdatePicker();"/>
                        [#elseif memberAttribute.type == "area"]
                            <span class="fieldSet"><input type="hidden" id="areaId"
                                                          name="memberAttribute_${memberAttribute.id}"
                                                          value="${(member.area.id)!}"
                                                          treePath="${(member.area.treePath)!}"/></span>
                        [#elseif memberAttribute.type == "address"]
                            <input type="text" name="memberAttribute_${memberAttribute.id}" class="text"
                                   value="${member.address}" maxlength="200"/>
                        [#elseif memberAttribute.type == "zipCode"]
                            <input type="text" name="memberAttribute_${memberAttribute.id}" class="text"
                                   value="${member.zipCode}" maxlength="200"/>
                        [#elseif memberAttribute.type == "phone"]
                            <input type="text" name="memberAttribute_${memberAttribute.id}" class="text"
                                   value="${member.phone}" maxlength="200"/>
                        [#elseif memberAttribute.type == "mobile"]
                        [#elseif memberAttribute.type == "text"]
                            <input type="text" name="memberAttribute_${memberAttribute.id}" class="text"
                                   value="${member.getAttributeValue(memberAttribute)}" maxlength="200"/>
                        [#elseif memberAttribute.type == "select"]
                            <select name="memberAttribute_${memberAttribute.id}">
                                <option value="">${message("shop.common.choose")}</option>
                                [#list memberAttribute.options as option]
                                    <option value="${option}"[#if option == member.getAttributeValue(memberAttribute)]
                                            selected="selected"[/#if]>
                                    ${option}
                                    </option>
                                [/#list]
                            </select>
                        [#elseif memberAttribute.type == "checkbox"]
                            <span class="fieldSet">
                                [#list memberAttribute.options as option]
                                    <label>
                                        <input type="checkbox" name="memberAttribute_${memberAttribute.id}"
                                               value="${option}"[#if (member.getAttributeValue(memberAttribute)?seq_contains(option))!]
                                               checked="checked"[/#if]/>${option}
                                    </label>
                                [/#list]
							</span>
                        [/#if]
                    </div>
                [/#list]
                    <div class="payment_info_div payment_info_div2">
                        <input type="submit" class="button" value="${message("shop.member.submit")}"/>
                        <input type="button" class="button" value="${message("shop.member.back")}"
                               onclick="javascript:history.back();"/>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
