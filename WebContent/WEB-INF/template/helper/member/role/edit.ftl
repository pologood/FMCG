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
            var _isRoleName="";
        [@flash_message /]
            $inputForm.validate({
                rules: {
                    name: "required"
                },
                messages: {
                    name: "必填"
                },
                submitHandler:function(form){

                    if(_isRoleName!=""){
                        $.message("warn",_isRoleName);
                        return false;
                    }
                    form.submit();
                }
            });
            $.isRoleName=function(name){
                if(name==null||name==""){
                    $.message("warn","活动名称不可以为空！");
                    return false;
                }
                $.ajax({
                    url:"${base}/helper/member/role/isRoleName.jhtml",
                    data:{
                        name:name
                    },
                    type:'post',
                    dataType:'json',
                    success:function(dataBlock){
                        if (dataBlock.type=="error"){
                            $.message(dataBlock.type,dataBlock.content);
                            _isRoleName=dataBlock.content;
                        }else{
                            _isRoleName="";
                        }
                    }
                });
            }
        });
    </script>
[#--选项卡内样式--]
    <style type="text/css">

        #node .app dl {
            margin: 10px 0;
            border: 1px solid #dcdcdc;
            height: auto;
            overflow: hidden;
        }

        #node .app dl dt {
            display: block;
            height: 36px;
            line-height: 36px;
            background: #e6e6fa;
            text-indent: 10px; /*文字缩进*/
        }

        #node .app dl dt strong {
            font-size: 16px;
            color: #61a1fa;
        }

        #node .app dl dd {
            padding: 10px;
            float: left;
        }

    </style>
[#--选项卡-样式--]
    <style>
        * {
            margin: 0;
            padding: 0;
            list-style: none;
        }

        body {
            font: 12px/1.5 Tahoma;
        }

        #outer {
            width: 100%;
            margin: 0px auto;
        }

        #tab {
            overflow: hidden;
            zoom: 1;
            background: #333333;
            border: 1px solid #333333;
        }

        #tab li {
            float: left;
            color: #fff;
            height: 30px;
            cursor: pointer;
            line-height: 30px;
            padding: 0 20px;
        }

        #tab li.current {
            color: #333333;
            background: #ccc;
        }

        #content {
            border: 1px solid #333333;
            border-top-width: 0;
        }

        #content ul {
            line-height: 25px;
            display: none;
            margin: 0 30px;
            padding: 10px 0;
        }
    </style>
    <script>
        //选项卡
        $(function () {
            window.onload = function () {
                var $li = $('#tab li');
                var $ul = $('#content ul');

                $li.mouseover(function () {
                    var $this = $(this);
                    var $t = $this.index();
                    $li.removeClass();
                    $this.addClass('current');
                    $ul.css('display', 'none');
                    $ul.eq($t).css('display', 'block');
                })

                $(function () {
                    $('input[level=1]').click(function () {
                        var inputs = $(this).parents('.app').find("input[level=2]");
                        if ($(this).attr('checked')) {
                            inputs.removeAttr('disabled');
                        } else {
                            inputs.removeAttr('checked');
                            inputs.attr('disabled', 'disabled');
                        }
                    });
                    //必须先选择read才可选择其他
                    $('input[level=2]').click(function () {
                        if ($(this).attr('checked') && $(this).attr('name') != "readRulesIds") {

                            var inputs = $(this).parents('.app  dt').find("input[name='readRulesIds']");
                            inputs.attr('checked', 'checked');
//                          alert("请先授权查看权限！");
//                            $(this).removeAttr('checked');

                        }
                    });
                    //2级功能全部选择
                    $('input[name="selectLv2All"]').click(function () {

                        var inputsRead = $(this).parents('.app  dt').find("input[name='readRulesIds']");
                        if (inputsRead.attr('disabled') && $(this).attr('checked')) {
                            alert("请先选择一级权限！");
                            $(this).removeAttr('checked')
                        } else {

                            var inputs = $(this).parents('.app  dt').find("input[level=2]");
                            $(this).attr('checked') ? inputs.attr('checked', 'checked') : inputs.removeAttr('checked');
                        }

                    });
                });
            }
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
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/shop-data.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">角色管理</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">角色与权限设置</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="on" hideFocus="" href="${base}/helper/member/role/list.jhtml">角色编辑</a></li>
                </ul>
            </div>
            <form id="inputForm" action="${base}/helper/member/role/update.jhtml" method="post">

                <input type="hidden" name="id" value="[#if role??]${role.id}[/#if]"/>
            [#--<input type="hidden" name="tenant" value="[#if role??]${role.tenant}[/#if]"/>--]
                <input type="hidden" name="roleType" value="[#if role??]${role.roleType}[/#if]"/>
                <table class="input">

                    <tr>
                        <td>
                            名称:
                        </td>
                        <td>
                            <input type="text" name="name" value="[#if role??]${role.name}[/#if]"  [#if role.isSystem]readonly[/#if] onblur= "$.isRoleName(this.value)">
                        </td>

                    </tr>


                    <tr>
                        <td>
                            描述:
                        </td>
                        <td>
                            <input type="text" name="description" value="[#if role??]${role.description}[/#if]">
                        </td>

                    </tr>
                </table>

                <div id="node">
                    <div id="outer">
                        <ul id="tab">

                        [#list ruless as rules]
                            <li [#if rules_index=0] class="current"[/#if]>${rules.name}</li>
                        [/#list]
                        </ul>
                        <div id="content">
                        [#list ruless as rules]
                            <ul  [#if rules_index=0]  style="display:block;[/#if]">
                                <div class="app">
                                    <p>
                                        [#list rules.oper?split(',') as operation]
                                            [#list types as type]
                                                [#if operation==type]
                                                    <input type="checkbox" id="${operation}RulesIds${rules.id}"
                                                           name="${operation}RulesIds" level='1'
                                                           value="${rules.id}"
                                                        [#if havsRulesIds.get(operation)??&&havsRulesIds.get(operation)?seq_contains(rules.id)]
                                                           checked="checked"
                                                        [/#if]/>

                                                ${message("helper.role."+operation)}
                                                [/#if]
                                            [/#list]
                                        [/#list]
                                        <strong>${rules.name}</strong>
                                    </p>
                                    [#list rules.children as crules]
                                        <dl>
                                            <dt>
                                                <input type="checkbox"
                                                       name="selectLv2All"/>
                                                <strong>${crules.name}</strong>
                                                [#list crules.oper?split(',') as operation]
                                                    [#list types as type]
                                                        [#if operation==type]
                                                            <input type="checkbox" id="${operation}RulesIds${crules.id}"
                                                                   name="${operation}RulesIds" level='2'
                                                                   value="${crules.id}"
                                                                [#if havsRulesIds.get(operation)??&&havsRulesIds.get(operation)?seq_contains(crules.id)]
                                                                   checked="checked"
                                                                [/#if ]
                                                                [#if !(havsRulesIds.get("read")??&&havsRulesIds.get("read")?seq_contains(rules.id))]
                                                                   disabled="disabled"
                                                                [/#if]/>
                                                        ${message("helper.role."+operation)}
                                                        [/#if]
                                                    [/#list]
                                                [/#list]
                                            </dt>

                                        </dl>
                                    [/#list]
                                </div>
                            </ul>
                        [/#list]
                        </div>
                    </div>
                </div>
                <input type="submit" class="button" value="${message("admin.common.submit")}"/>
                <input type="button" class="button" value="${message("admin.common.back")}"
                       onclick="history.go(-1)"/>
            </form>
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
