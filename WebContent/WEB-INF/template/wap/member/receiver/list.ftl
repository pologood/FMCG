<!DOCTYPE html>
<html lang="en">
<head>
    <title>我的收货地址</title>
[#include "/wap/include/resource-2.0.ftl"]
    <script src="${base}/resources/wap/2.0/js/area.js"></script>
[#include "/wap/component/area.ftl"]
    <script type="text/javascript" src="${base}/resources/wap/2.0/js/receiver.js"></script>
    <link rel="stylesheet" href="${base}/resources/wap/2.0/css/wap.css"/>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script src="${base}/resources/wap/2.0/js/iscroll-probe.js"></script>
    <style type="text/css">
        a{
            color: #888888;
        }
        a:hover{
            color: #888888;
        }
        .checkbox-deft_addr{

        }
        .checkbox-deft_addr:before{
            font-size: 20px!important;
        }
        .weui_cells_checkbox .weui_check:checked+.checkbox-deft_addr:before{
            color:#ff6d06;
        }
        .twowords{
            letter-spacing: 1em;
        }
    </style>
    <script  type="text/html"  id="tpl_addReceiver">
        <div class="page papering" id="weui_addReceiver" style="display: none;">
            <form id="form" action="${base}/wap/member/receiver/save.jhtml" method="post">
                <input type="hidden" name="isDefault" value="false">
                <div class="weui_cells weui_cells_form">
                    <div class="weui_cell">
                        <div class="weui_cell_hd"><label>收货人：</label></div>
                        <div class="weui_cell_bd weui_cell_primary">
                            <input name="consignee" id="consignee" class="weui_input" type="text" placeholder="请输入收货人"
                                   value="">
                        </div>
                        <div class="weui_cell_ft">
                            <i class="weui_icon_warn"></i>
                        </div>
                    </div>
                    <div class="weui_cell">
                        <div class="weui_cell_hd"><label>联系方式：</label></div>
                        <div class="weui_cell_bd weui_cell_primary">
                            <input id="phone" name="phone" class="weui_input" type="text" placeholder="请输入联系方式"
                                   value="">
                        </div>
                        <div class="weui_cell_ft">
                            <i class="weui_icon_warn"></i>
                        </div>
                    </div>
                </div>
                <div class="weui_cells weui_cells_access">
                    <a class="weui_cell" href="javascript:;" id="area">
                        <div class="weui_cell_bd">
                            <p>所在地区：</p>
                        </div>
                        <div class="weui_cell_ft weui_cell_primary" id="areaName">
                            请选择
                        </div>
                    </a>
                </div>
                <div class="weui_cells weui_cells_form">
                    <div class="weui_cell">
                        <div class="weui_cell_hd"><label>详细地址：</label></div>
                        <div class="weui_cell_bd weui_cell_primary">
                            <input name="address" id="address" class="weui_input" type="text" placeholder="请输入详细地址"
                                   value="">
                        </div>
                        <div class="weui_cell_ft">
                            <i class="weui_icon_warn"></i>
                        </div>
                    </div>
                </div>
                <div style="padding:30px 10px 0 10px;">
                    <a href="javascript:;" class="weui_btn weui_btn_warn3" id="submit">确认</a>
                </div>
            </form>
        </div>
    </script>
    <script  type="text/html"  id="tpl_editReceiver">
        <div class="page papering" id="weui_editReceiver" style="display: none;">
            <form id="form" action="update.jhtml" method="post">
                <input type="hidden" name="id" value="" id="id">
                <input type="hidden" name="isDefault" id="isDefault" value="false">
                <div class="weui_cells weui_cells_form">
                    <div class="weui_cell">
                        <div class="weui_cell_hd"><label>收货人：</label></div>
                        <div class="weui_cell_bd weui_cell_primary">
                            <input name="consignee" id="consignee" class="weui_input" type="text" placeholder="请输入收货人"
                                   value="${(receiver.consignee)!}">
                        </div>
                        <div class="weui_cell_ft">
                            <i class="weui_icon_warn"></i>
                        </div>
                    </div>
                    <div class="weui_cell">
                        <div class="weui_cell_hd"><label>联系方式：</label></div>
                        <div class="weui_cell_bd weui_cell_primary">
                            <input id="phone" name="phone" class="weui_input" type="text" placeholder="请输入联系方式"
                                   value="${(receiver.phone)!}">
                        </div>
                        <div class="weui_cell_ft">
                            <i class="weui_icon_warn"></i>
                        </div>
                    </div>
                </div>
                <div class="weui_cells weui_cells_access">
                    <a class="weui_cell" href="javascript:;" id="province">
                        <div class="weui_cell_bd weui_cell_primary">
                            <p>所在地区：</p>
                        </div>
                        <div class="weui_cell_ft" id="areaName" areaId="${(receiver.area.id)!}">
                        ${(receiver.area.fullName)!}
                        </div>
                    </a>
                </div>
                <div class="weui_cells weui_cells_form">
                    <div class="weui_cell">
                        <div class="weui_cell_hd"><label>详细地址：</label></div>
                        <div class="weui_cell_bd weui_cell_primary">
                            <input name="address" id="address" class="weui_input" type="text" placeholder="请输入详细地址"
                                   value="${(receiver.address)!}">
                        </div>
                        <div class="weui_cell_ft">
                            <i class="weui_icon_warn"></i>
                        </div>
                    </div>
                </div>
                <div style="padding:30px 10px 0 10px;">
                    <a href="javascript:;" class="weui_btn weui_btn_warn3" id="submit">确认</a>
                </div>
            </form>
        </div>
    </script>
    <script type="text/x-handlebars-template" id="list-item">
        {{#each this}}
        <div class="weui_cells weui_cells_checkbox marginTop10">

            <a class="weui_cell" [#if fromCart??]href="${base}/wap/member/order/orderPay.jhtml?receiverId={{id}}"[#else]href="javascript:;"[/#if]>
                <div class="weui_cell_bd weui_cell_primary">
                    <p style="color:#333;">{{consignee}}&nbsp;&nbsp;&nbsp;{{phone}}</p>
                    <p style="color:#888;font-size: small;padding-top: 5px;">{{area.fullName}}{{address}}</p>
                </div>
            </a>

            <label class="weui_cell weui_check_label" for="{{id}}"
                   style="color:#888;font-size: small;padding-right: 20px;">
                <div class="weui_cell_hd">
                    <input type="checkbox" class="weui_check" name="checkbox1" id="{{id}}"
                           {{#if isDefault}} checked="checked" disabled {{/if}} onclick="setDefault(this)">
                    <i class="weui_icon_checked checkbox-deft_addr"></i>
                </div>
                <div class="weui_cell_bd weui_cell_primary">
                    <p>设为默认</p>
                </div>

                <a href="javascript:edit_receiver({{id}},'${fromCart!}');">
                    <i class="iconfont">&#xe613;</i>
                    <span>编辑</span>
                </a>&nbsp;&nbsp;&nbsp;&nbsp;
                <a href="javascript:deleteAddress({{id}}); "  >
                    <i class="iconfont">&#xe610;</i>
                    <span>删除</span>
                </a>
            </label>

        </div>
        {{/each}}

        <div style="padding:30px 10px;">
            <a href="javascript:add_receiver('${fromCart!}','${backUrl}','${addReceiver}');" class="weui_btn weui_btn_warn3">新增收货地址</a>
        </div>
    </script>
</head>
<body>
[#include "/wap/include/static_resource.ftl"]
<div class="container">

</div>

<script type="text/html" id="tpl_wraper">

    <div class="page papering" id="weui_wraper">

    </div>
</script>

<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>

<script>
    $(function () {
        init();
        loadReceiver();
    });
    var $url = '${backUrl}';
    var $addReceiver = '${addReceiver}';
    var receiverId = "${receiverId}";
    var compiler = Handlebars.compile($("#list-item").html());
    function loadReceiver() {
        ajaxGet({
            url: "${base}/app/member/receiver/getReceivers.jhtml",
            success: function (dataBlock) {
                $(".page").html(compiler(dataBlock.data));

                if (receiverId == "" ) {
                    add_receiver('${fromCart!}','${backUrl}','${addReceiver}');
                }

                if(dataBlock.data.length==1&&$addReceiver!=''){
                    location.href = '${base}'+$url;
                }
            }
        });
    }

    function setDefault(b) {
        $(b).prop("checked", !$(b).prop("checked"));
        showDialog1("提示", "是否设为默认？", function () {
            ajaxPost({
                url: "setDefault.jhtml?id=" + $(b).attr("id"),
                data: '',
                success: function (data) {
                    if (data.type == "success") {
                        showToast();
                        setTimeout(function () {
                            if($url!=null&&$url!=''){
                                location.href = '${base}'+$url;
                            }else{
                                location.reload(true);
                            }
                        }, 600);
                    }
                }
            });
        });
    }

    function deleteAddress(id) {
        showDialog1("提示", "你确定删除这个地址吗？", function () {
            ajaxPost({
                url: 'delete.jhtml?idsStr=' + id,
                data: '',
                success: function (data) {
                    showToast();
                    setTimeout(function () {
                        location.reload(true);
                    }, 600);
                }
            });
        });
    }

</script>

</body>
</html>