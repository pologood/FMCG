<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
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
    <script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            var $inputForm = $("#inputForm");
        [@flash_message /]
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
            <dt class="app-title" id="app_name">我的供应商</dt>
            <dd class="app-status" id="app_add_status">
            </dd>
            <dd class="app-intro" id="app_desc">管理我的供应商。</dd>
        </dl>
    </div>
    <ul class="links" id="mod_menus">
        <li><a class="on" hideFocus="" href="${base}/helper/member/relation/parent.jhtml">我的供应商</a></li>
    </ul>

</div>
<form id="inputForm" action="editParent.jhtml" method="post">
    <input type="hidden" name="id" value="${tenantRelation.id}"/>
    <table class="input">
        <tr>
            <th>
                供应商名称:
            </th>
            <td>
            ${tenantRelation.parent.name}
            </td>
        </tr>
        <tr>
            <th>
                联系人:
            </th>
            <td>
            	<input type="text" name="linkman" value="${tenantRelation.parent.linkman}">
            </td>
        </tr>
        <tr>
            <th>
                手机号码:
            </th>
            <td>
            ${tenantRelation.parent.telephone}
            </td>
        </tr>
        <tr>
            <th>
                地址:
            </th>
            <td>
            	<input type="text" name="address" value="${tenantRelation.parent.address}">
            </td>
        </tr>
        <tr>
            <th>
                等级:
            </th>
            <td>
                <select name="memberRankId">
                [#list memberRanks as memberRank]
                    <option value="${memberRank.id}"[#if memberRank.id == tenantRelation.parent.member.memberRank.id]
                            selected="selected"[/#if]>
                    ${memberRank.name}
                    </option>
                [/#list]
                </select>
            </td>
        </tr>
        <tr>
            <th>
                状态:
            </th>
            <td>
                <select name="status">
                    <option value="none" [#if ("none" == (tenantRelation.status)!)] selected[/#if]>未审核</option>
                    <option value="success" [#if ("success" == (tenantRelation.status)!)] selected[/#if]>已审核</option>
                    <option value="fail" [#if ("fail" == (tenantRelation.status)!)] selected[/#if]>已驳回</option>
                </select>
            </td>
        </tr>
        <tr>
            <th>
                账户余额:
            </th>
            <td>
                ${(tenantRelation.parent.balance)!}元
            </td>
        </tr>
        <tr>
            <th>
                冻结金额:
            </th>
            <td>
                ${(tenantRelation.parent.freezeBalance)!}元
            </td>
        </tr>
        <tr>
            <th>
                库存金额:
            </th>
            <td>
                [#assign stock=0]
                [#list products as product]
                    [#if product.supplier??&&product.supplier.id==tenantRelation.parent.id]
                    [#assign stock=stock+product.stock*product.cost]
                    [/#if]
                [/#list]
                ${stock}元
            </td>
        </tr>
        <tr>
            <th>
                &nbsp;
            </th>
            <td>
                <input type="submit" class="button" value="${message("admin.common.submit")}"/>
                <input type="button" class="button" value="${message("admin.common.back")}"
                       onclick="location.href='parent.jhtml'"/>
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
