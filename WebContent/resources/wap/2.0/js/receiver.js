/**
 * Created by WangChao on 2016-3-15.
 */
var addReceiver = {
    name: 'addReceiver',
    url: '#addReceiver',
    template: '#tpl_addReceiver'
};

var editReceiver = {
    name: 'editReceiver',
    url: '#editReceiver',
    template: '#tpl_editReceiver'
};

$(function () {
    pageManager.push(addReceiver);
    pageManager.push(editReceiver);
});

function add_receiver(fromCart,backUrl,addReceiver) {
    pageManager.go("addReceiver");

    var $form = $("#form");
    var $areaName=$("#areaName");

    $("#area").click(function () {
        findArea(function (id, title) {
            $areaName.attr("areaId", id);
            $areaName.text(title);
        });
    });

    validate.require("#consignee", "必填");
    validate.require("#phone", "必填");
    validate.require("#address", "必填");

    $("#submit").click(function () {

        var pattern=/[^\u4E00-\u9FA5]/g;

        if ($form.find(".weui_cell_warn").size() != 0) {
            showDialog2('友情提醒', '请正确填写带红色标志的数据。');
            return;
        }
        var areaId = $areaName.attr("areaId");
        if($("#consignee").val().trim()==""){
            showDialog2("友情提醒","请输入收货人！");
            return;
        }

        if(pattern.test($("#consignee").val().trim())){
            showDialog2("友情提醒","请输入您的真实姓名！");
            return;
        }

        if($("#phone").val().trim()==""){
            showDialog2("友情提醒","请输入联系方式！");
            return;
        }

        if(!/^(0|86|17951)?(13[0-9]|15[012356789]|18[0-9]|14[57]|17[0-9])[0-9]{8}$/.test($("#phone").val().trim())){
            showDialog2("友情提醒","您输入的联系方式不对，请重新输入！");
            return;
        }
        if(areaId==null){
            showDialog2("友情提醒","请选择所在地区！");
            return;
        }
        if($("#address").val().trim()==""){
            showDialog2("友情提醒","请输入详细地址！");
            return;
        }

        //if(pattern.test($("#address").val().trim())){
        //    showDialog2("友情提醒","您输入的地址不合法，请重新输入！");
        //    return;
        //}

        ajaxPost({
            url: $form.attr("action"),
            data: $form.serialize() + "&areaId=" + areaId,
            success: function (data) {
                if (data.type == "success") {
                    showToast();
                    setTimeout(function () {
                        location.href = "list.jhtml?fromCart="+fromCart+"&backUrl="+backUrl+"&addReceiver="+addReceiver;
                    }, 600);
                } else {
                    showDialog2(data.content);
                }
            }
        });
    });

    $('#weui_addReceiver').show();
}

function edit_receiver(id,fromCart) {
    pageManager.go("editReceiver");

    var $form = $("#form");
    var $areaName=$("#areaName");

    $("#province").click(function () {
        findArea(function (id, title) {
            $areaName.attr("areaId", id);
            $areaName.text(title);
        });
    });

    validate.require("#consignee", "必填");
    validate.require("#phone", "必填");
    validate.require("#address", "必填");

    $("#submit").click(function () {
        if ($form.find(".weui_cell_warn").size() != 0) {
            showDialog2('友情提醒', '请正确填写带红色标志的数据。');
            return;
        }
        var areaId = $areaName.attr("areaId");
        if(areaId==null){
            showDialog2("友情提醒","请选择所在地区！");
            return;
        }
        ajaxPost({
            url: $form.attr("action"),
            data: $form.serialize() + "&areaId=" + areaId,
            success: function (data) {
                if (data.type == "success") {
                    showToast();
                    setTimeout(function () {
                        location.href = "list.jhtml?fromCart="+fromCart;
                    }, 600);
                } else {
                    showDialog2(data.content);
                }
            }
        });
    });

    ajaxGet({
        url:'getReceiver.jhtml',
        data:{id:id},
        success:function(data){
            $("#id").val(data.id);
            $("#isDefault").val(data.isDefault);
            $("#consignee").val(data.consignee);
            $("#phone").val(data.phone);
            $areaName.attr("areaId",data.area.id);
            $areaName.text(data.area.fullName);
            $("#address").val(data.address);
        }
    });

    $('#weui_editReceiver').show();
}

