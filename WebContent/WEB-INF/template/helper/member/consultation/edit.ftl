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
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {
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
        <img class="js-app_logo app-img" src="${base}/resources/helper/images/message-manage.png"/>
        <dl class="app-info">
            <dt class="app-title" id="app_name">咨询回复</dt>
            <dd class="app-status" id="app_add_status"></dd>
            <dd class="app-intro" id="app_desc">快去回复粉丝们咨询的问题，对您的诚信度有很大的帮助。</dd>
        </dl>
    </div>
    <ul class="links" id="mod_menus">
        <li><a class="" hideFocus="" href="${base}/helper/member/consultation/manager.jhtml">咨询列表</a></li>
        <li><a class="on" hideFocus="">咨询详情</a></li>
    </ul>
</div>
<form id="inputForm" action="update.jhtml" method="post">
    <input type="hidden" name="id" value="${consultation.id}"/>
    <table class="input">
        <tr>
            <th>
            ${message("Consultation.product")}:
            </th>
            <td colspan="3">
                <a href="${base}/helper/product/content/${consultation.product.id}.jhtml" target="_blank">${consultation.product.name}</a>
            </td>
        </tr>
        <tr>
            <th>
            ${message("Consultation.member")}:
            </th>
            <td colspan="3">
            [#if consultation.member??]
                <a href="../member/view.jhtml?id=${consultation.member.id}">${consultation.member.username}</a>
            [#else]
            ${message("admin.consultation.anonymous")}
            [/#if]
            </td>
        </tr>
        <tr>
            <th>
            ${message("Consultation.content")}:
            </th>
            <td colspan="3">
            ${consultation.content}
            </td>
        </tr>
        <tr>
            <th>
            ${message("Consultation.ip")}:
            </th>
            <td colspan="3">
            ${consultation.ip}
            </td>
        </tr>
    [#if consultation.replyConsultations?has_content]
        <tr class="title">
            <td colspan="4">
                &nbsp;
            </td>
        </tr>
        [#list consultation.replyConsultations as replyConsultation]
            <tr>
                <th>
                    &nbsp;
                </th>
                <td>
                ${replyConsultation.content}
                </td>
                <td width="80">
                    <span title="${replyConsultation.createDate?string("yyyy-MM-dd HH:mm:ss")}">${replyConsultation.createDate}</span>
                </td>
                <td width="80">
                    <a href="javascript:;" class="delete"
                       val="${replyConsultation.id}">[${message("admin.common.delete")}]</a>
                </td>
            </tr>
        [/#list]
    [/#if]
        <tr>
            <th>
            ${message("Consultation.isShow")}:
            </th>
            <td colspan="3">
                <input type="checkbox" name="isShow" value="true"[#if consultation.isShow] checked="checked"[/#if]/>
            </td>
        </tr>
        <tr>
            <th>
                &nbsp;
            </th>
            <td colspan="3">
                <input type="submit" class="button" value="${message("admin.common.submit")}"/>
                <input type="button" class="button" value="${message("admin.common.back")}"
                       onclick="location.href='manager.jhtml'"/>
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
