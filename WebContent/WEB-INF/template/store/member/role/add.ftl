<!DOCTYPE html>
<html>

    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>角色管理</title>
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport"> [#include "/store/member/include/bootstrap_css.ftl"]
        <link rel="stylesheet" href="${base}/resources/store/css/style.css">
        <link href="${base}/resources/store/css/common.css" type="text/css" rel="stylesheet"/>
    </head>

    <body class="hold-transition skin-blue sidebar-mini">
        <div class="wrapper">
            [#include "/store/member/include/header.ftl"]
            <!-- Left side column. contains the logo and sidebar -->
            [#include "/store/member/include/menu.ftl"]
            <!-- Content Wrapper. Contains page content -->
            <div class="content-wrapper">
                <!-- Content Header (Page header) -->
                <section class="content-header">
                    <h1>我的店铺<small>角色与权限设置</small>  </h1>
                    <ol class="breadcrumb">
                        <li><a href="${base}/store/member/index.jhtml"><i class="fa fa-tv"></i>首页</a></li>
                        <li><a href="${base}/store/member/tenant/edit.jhtml">我的店铺</a></li>
                        <li><a href="${base}/store/member/role/list.jhtml">角色管理</a></li>
                        <li class="active">添加</li>
                    </ol>
                </section>
                <!-- Main content -->
                <section class="content">
                    <div class="row">
                        <div class="col-md-12">
                            <div class="nav-tabs-custom">
                                <ul class="nav nav-tabs pull-right">
                                    <li class="pull-left header"><i class="fa fa-user-secret"></i>角色编辑</li>
                                </ul>
                                <div class="tab-content">
                                    <form class="form-horizontal" role="form" id="inputForm" action="${base}/store/member/role/save.jhtml" method="post">
                                        <input type="hidden" name="id"/>
                                        <input type="hidden" name="redirectUrl" value="${redirectUrl}"/>
                                        <div class="form-group">
                                            <label class="col-sm-1 control-label">名称:</label>
                                            <div class="col-sm-8">
                                                <input type="text" class="form-control" name="name" value="[#if role??]${role.name}[/#if]">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-1 control-label">描述:</label>
                                            <div class="col-sm-8">
                                                <input type="text" class="form-control" name="description" value="[#if role??]${role.description}[/#if]">
                                            </div>
                                        </div>
                                        <div class="tab">
                                        <!-- Nav tabs -->
                                        <ul class="nav nav-tabs" role="tablist">
                                            [#list ruless as rule]
                                                <li [#if rule_index=0] class="active"[/#if]>
                                                    <a href="#${rule_index}" role="tab" data-toggle="tab">${rule.name}</a>
                                                </li>
                                            [/#list]
                                        </ul>

                                        <!-- Tab panes -->
                                        <div class="tab-content">
                                            [#list ruless as rules]
                                            <div role="tabpanel" class="tab-pane [#if rules_index=0]active[/#if]" id="${rules_index}">
                                                <div class="box-header">
                                                    <h3 class="box-title">
                                                        <strong>${rules.name}</strong>
                                                        [#list rules.oper?split(',') as operation]
                                                            [#list types as type]
                                                                [#if operation==type]
                                                                    <input type="checkbox" 
                                                                        id="${operation}RulesIds${rules.id}"
                                                                        name="${operation}RulesIds" 
                                                                        level='1'
                                                                        value="${rules.id}"/>
                                                                    ${message("helper.role."+operation)}
                                                                [/#if]
                                                            [/#list]
                                                        [/#list]
                                                    </h3>
                                                </div>
                                                <!-- /.box-header -->
                                                <div class="box-body no-padding borders">
                                                    <table class="table table-striped set">
                                                        <tbody>
                                                            [#list rules.children as crules]
                                                            <tr>
                                                              <td>
                                                                <strong>
                                                                  <input type="checkbox" name="selectLv2All" style="margin-right:5px;" />${crules.name}
                                                                </strong>
                                                                <div class="form-group checkbox-list">
                                                                [#list crules.oper?split(',') as operation]
                                                                [#list types as type]
                                                                [#if operation==type]
                                                                    <div class="checkbox" style="margin-top: -10px;">
                                                                        <label>
                                                                          <input type="checkbox" 
                                                                              name="${operation}RulesIds" level='2'
                                                                              value="${crules.id}"
                                                                              disabled="disabled"/>
                                                                          ${message("helper.role."+operation)}
                                                                        </label>
                                                                    </div>
                                                                [/#if]
                                                                [/#list]
                                                                [/#list]
                                                                </div>
                                                              </td>
                                                            </tr>
                                                            [/#list]
                                                        </tbody>
                                                    </table>
                                                </div>
                                                <!-- /.box-body -->
                                                <div class="box-footer mt10" style="border-top:none;">
                                                    <div class="col-sm-offset-0 col-sm-2">
                                                        <button type="submit" class="btn btn-block btn-primary">提交</button>
                                                    </div>
                                                    <div class="col-sm-offset-0 col-sm-2">
                                                        <button type="button" class="btn btn-block btn-default" onclick="history.go(-1)">返回</button>
                                                    </div>
                                                </div>
                                            </div>
                                            [/#list]
                                        </div>
                                        </div>
                                    </form>
                                </div>

                            </div>
                        </div>
                    </div>

                </section>
                <!-- /.content -->
            </div>
            <!-- /.content-wrapper -->
            [#include "/store/member/include/footer.ftl"]
        </div>
        [#include "/store/member/include/bootstrap_js.ftl"]
        <script type="text/javascript" src="${base}/resources/store/js/common.js"></script>
        <script type="text/javascript" src="${base}/resources/store/js/input.js"></script>
        <script type="text/javascript" src="${base}/resources/store/js/jquery.validate.js"></script>
        <script>
            $(function () {
                //表单验证
                var $inputForm = $("#inputForm");
                $inputForm.validate({
                    rules: {
                        name: "required"
                    },
                    messages: {
                        name: "必填"
                    }
                });

                //选项卡
                $('input[level=1]').click(function () {
                    var inputs = $(this).parents('.box-header').next().find("input[level=2]");
                    if ($(this).prop('checked')) {
                        inputs.removeAttr('disabled');
                    } else {
                        inputs.removeAttr('checked');
                        inputs.attr('disabled', 'disabled');
                    }
                });
                //必须先选择read才可选择其他
                $('input[level=2]').click(function () {
                    if ($(this).prop('checked') && $(this).attr('name') != "readRulesIds") {
                        var inputs = $(this).parents('td').find("input[name='readRulesIds']");
                        inputs.prop('checked', 'true');
                    }
                });
                //2级功能全部选择
                $('input[name="selectLv2All"]').click(function () {
                    var inputsRead = $(this).parents("div[role='tabpanel']").find("input[name='readRulesIds']");
                    if (!inputsRead.prop('checked') && $(this).prop('checked')) {
                        alert("请先选择一级权限！");
                        $(this).removeAttr('checked')
                    } else {
                        var inputs = $(this).parents('td').find("input[level=2]");
                        $(this).prop('checked') ? inputs.prop('checked', 'true') : inputs.removeAttr('checked');
                    }
                });
                    
            });
            
        </script>
    </body>
</html>