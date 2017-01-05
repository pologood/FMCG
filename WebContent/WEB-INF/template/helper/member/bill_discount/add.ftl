<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.consultation.edit")}</title>
    <meta name="baidu-site-verification" content="7EKp4TWRZT"/>
[@seo type = "index"]
    <title> [#if seo.title??][@seo.title?interpret /][#else]首页[/#if]</title>
    [#if seo.keywords??]
        <meta name="keywords" content="[@seo.keywords?interpret /]"/>
    [/#if]
    [#if seo.description??]
        <meta name="description" content="[@seo.description?interpret /]"/>
    [/#if]
[/@seo]
    <link href="${base}/resources/helper/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/helper/font/iconfont.css" type="text/css" rel="stylesheet"/>

    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/input.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/datePicker/WdatePicker.js"></script>
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            var $inputForm = $("#inputForm");
            var $delete = $("#inputForm a.delete");
        [@flash_message /]
            // 删除
            $delete.click(function () {
                var $this = $(this);
                $.dialog({
                    type: "warn",
                    content: "${message("admin.dialog.deleteConfirm")}",
                    onOk: function () {
                        $.ajax({
                            url: "delete_reply.jhtml",
                            type: "POST",
                            data: {id: $this.attr("val")},
                            dataType: "json",
                            cache: false,
                            success: function (message) {
                                $.message(message);
                                if (message.type == "success") {
                                    $this.closest("tr").remove();
                                }
                            }
                        });
                    }
                });
                return false;
            });


            // 表单验证
            $inputForm.validate({
                rules: {
                    "name": {
                        required: true
                    },
                    "agioRate": {
                        required: true,
                        number: true,
                        min: 0,
                        max:100
                    },
                    "backRate": {
                        required: true,
                        integer: true,
                        min: 0,
                        max:100
                    },
                    "beginDate": {
                        required: true
                    },
                    "endDate": {
                        required: true
                    }
                },
                submitHandler: function (form) {
                    var $amount = $("input[name='amount']").val();
                    var $minimumPrice = $("input[name='minimumPrice']").val();

                    if (parseFloat($minimumPrice) < parseFloat($amount)) {
                        $.message("warn", "最低消费必须大于券的金额！");
                        return;
                    }
                    form.submit();
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

            <div class="page-nav page-nav-app vip-guide-nav" id="app_head_nav">
                <div class="js-app-header title-wrap" id="app_0000000844">
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/coupon.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">买单折扣</dt>
                        <dd class="app-status" id="app_add_status"></dd>
                        <dd class="app-intro" id="app_desc">快去添加买单折扣券，对您的销量有很大的帮助。</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="on"
                           href="${base}/helper/member/bill/discount/list.jhtml?status=${(status)!}">买单折扣</a>
                    </li>
                </ul>
            </div>
            <form id="inputForm" action="save.jhtml" method="post">
                <table class="input">
                    <tr>
                        <th>
                            <span class="requiredField">*</span>名称:
                        </th>
                        <td colspan="3">
                            <input type="text" id="name" name="name" class="text" value="${(promotion.name)!}"  />
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <span class="requiredField">*</span>折扣比例:
                        </th>
                        <td colspan="3">
                            <input type="number" id="agioRate" name="agioRate" class="text" value="${(promotion.agioRate)!}"/>% &nbsp;&nbsp;
                            <label style="color: red">0-100,设置买单折扣的比例  如：80%</label>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <span class="requiredField">*</span>返现比例:
                        </th>
                        <td colspan="3">
                            <input type="number" id="backRate" name="backRate" class="text" value="${(promotion.backRate)!}"/>%&nbsp;&nbsp;
                            <label style="color: red">根据用户实际支付的金额按此比例返现</label>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <span class="requiredField">*</span>开始时间:
                        </th>
                        <td colspan="3">
                            <input type="text" id="beginDate" name="beginDate" class="text Wdate"
                                   onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:00:00', maxDate: '#F{$dp.$D(\'beginDate\')}'});"/>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            <span class="requiredField">*</span>结束时间:
                        </th>
                        <td colspan="3">
                            <input type="text" id="endDate" name="endDate" class="text Wdate"
                                   onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd HH:00:00', minDate: '#F{$dp.$D(\'endDate\')}'});"/>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            活动规则:
                        </th>
                        <td colspan="3">
                <textarea id="introduction" name="introduction" style="width: 60%;height: 80px;"
                           maxlength="50">${(promotion.introduction?default(""))!}</textarea>
                        </td>
                    </tr>
                    <tr>
                        <th>
                            &nbsp;
                        </th>
                        <td colspan="3">
                            <input type="submit" class="button" value="${message("admin.common.submit")}"/>
                            <input type="button" class="button" value="${message("admin.common.back")}"
                                   onclick="history.go(-1)"/>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
