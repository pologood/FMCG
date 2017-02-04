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
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            var mpOpType =
            {
        [#list types as type]
        ${type}:
            '${message("helper.role."+type)}',
        [/#list]
        } ;
            var roleId=null;
            $("a[name='roleName']").mouseout(function () {
                $("#dialogRoleRules").hide();

            });
            $("a[name='roleName']").mouseover(function () {
                $("#dialogRoleRules").show();
                if ($(this).attr("id") != ""&&roleId!=$(this).attr("id")) {
                    roleId=$(this).attr("id");
                    $("#dialogRoleRules").empty();
                    $("#dialogRoleRules").append(" <strong>加载中。。。</strong>");
                    $.ajax({
                        url: "${base}/helper/member/role/getRules.jhtml",
                        type: "post",
                        data: {id: $(this).attr("id")},
                        dataType: "json",
                        success: function (message) {
                            if (message.data != null && message.message.type == "success") {
                                var strHtml = "";
                                strHtml += " <div>";
                                for (var i = 0; i < message.data.length; i++) {
                                    if (message.data[i].rules.children.length == 0) {
                                        strHtml += " <div>";
                                        strHtml += "<strong>" + message.data[i].rules.name + "：</strong>  ";
                                        $.each(message.data[i].mapAuthority, function (key, values) {
                                            if (values == true) {
                                                strHtml += mpOpType[key] + "、 ";
                                            }
                                        });
                                        strHtml += " </div>";
                                    }

                                }

                                $("#dialogRoleRules").empty();
                                $("#dialogRoleRules").append(strHtml);
                            }
                        }
                    });
                }
            });
        });
    </script>

</head>
<body>

<!--BEGIN dialog1-->
<div style=
             "position: absolute; background-color:ghostwhite; width: 250px;
        z-index: 401;;padding: 20px;
    top: 50%;
    left: 50%; display: none;
    transform: translate(-50%, -50%);"
     id="dialogRoleRules">
    <div class="js-app-header title-wrap" id="app_0000000844">
      <strong>加载中。。。</strong>
    </div>
</div>
<!--END dialog1-->
[#include "/helper/include/header.ftl" /]
[#include "/helper/member/include/navigation.ftl" /]
<div class="desktop">
    <div class="container bg_fff">

    [#include "/helper/member/include/border.ftl" /]

    [#include "/helper/member/include/menu.ftl" /]

        <div class="wrapper" id="wrapper">

            <!-- <div class="con-con"> -->
            <div class="page-nav page-nav-app vip-guide-nav" id="app_head_nav">
                <div class="js-app-header title-wrap" id="app_0000000844">
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/shop-data.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">角色管理</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">角色与权限设置</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                [@helperRole url="helper/member/tenant/employee/list.jhtml"]
                    [#if helperRole.retUrl!="0"]
                        <li><a class="" hideFocus="" href="${base}/helper/member/tenant/employee/list.jhtml">员工管理</a>
                        </li>
                    [/#if]
                [/@helperRole]
                [@helperRole url="helper/member/role/list.jhtml"]
                    [#if helperRole.retUrl!="0"]
                        <li><a class="on" hideFocus="" href="${base}/helper/member/role/list.jhtml">角色管理</a></li>
                    [/#if]
                [/@helperRole]
                [#--<li><a class="" hideFocus=""  href="${base}/helper/member/authen/index.jhtml">店铺认证</a></li>--]

                </ul>
            </div>
            <form id="listForm" action="list.jhtml" method="get">
                <div class="bar">
                    <div class="buttonWrap">
                    [@helperRole url="helper/member/role/list.jhtml" type="add"]
                        [#if helperRole.retOper!="0"]
                            <a href="add.jhtml" class="iconButton">
                                <span class="addIcon">&nbsp;</span>${message("admin.common.add")}
                            </a>
                        [/#if]
                    [/@helperRole]

                    [@helperRole url="helper/member/role/list.jhtml" type="del"]
                        [#if helperRole.retOper!="0"]
                            <a href="javascript:;" id="deleteButton" class="iconButton disabled">
                                <span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
                            </a>
                        [/#if]
                    [/@helperRole]
                        <a href="javascript:;" id="refreshButton" class="iconButton">
                            <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
                        </a>
                        <div class="menuWrap">
                            <a href="javascript:;" id="pageSizeSelect" class="button">
                            ${message("admin.page.pageSize")}<span class="arrow">&nbsp;</span>
                            </a>
                            <div class="popupMenu">
                                <ul id="pageSizeOption">
                                    <li>
                                        <a href="javascript:;"[#if page.pageSize == 10] class="current"[/#if] val="10">10</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;"[#if page.pageSize == 20] class="current"[/#if] val="20">20</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;"[#if page.pageSize == 50] class="current"[/#if] val="50">50</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;"[#if page.pageSize == 100] class="current"[/#if]
                                           val="100">100</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>

                    <div class="menuWrap">
                        <div class="search">
                            <input type="text" name="keyWord" value="${keyWord}"
                                   maxlength="200" placeholder="搜索角色名"/>
                            <button type="submit">&nbsp;</button>
                        </div>
                    </div>
                </div>
                <div class="list">
                    <table class="list" id="listTable">
                        <tr>
                            <th class="check">
                                <input type="checkbox" id="selectAll">
                            </th>
                            <th>角色名称</th>
                            <th><span>操作</span></th>
                        </tr>
                    [#list page.content as role ]
                        <tr>
                            <td>
                                <input type="checkbox" name="ids" value="${role.id}"/>
                            </td>
                            <td>
                                <a name="roleName" id="${(role.id)}"> ${(role.name)}</a>
                            </td>
                            <td>
                                [@helperRole url="helper/member/role/list.jhtml" type="update"]
                                    [#if helperRole.retOper!="0"]
                                        <a href="javascript:;"
                                           onclick="roleRoot('edit.jhtml?id=${role.id}','owner')">编辑</a>
                                    [/#if]
                                [/@helperRole]

                                [#if role.isSystem]<label style="color: red">[系统内置角色,请勿删除]</label>[/#if]
                            </td>
                        </tr>
                    [/#list]
                    </table>
                [#if !page.content?has_content]
                    <p class="nothing">${message("helper.member.noResult")}</p>
                [/#if]
                </div>
            [@pagination pageNumber = page.pageNumber totalPages = page.totalPages pattern = "?pageNumber={pageNumber}"]
                [#include "/helper/include/pagination.ftl"]
            [/@pagination]
        </div>
        </form>
    </div>
</div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
