<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.productCategory.list")} - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript">
        $().ready(function () {

            var $delete = $("#listTable a.delete");
            var $popu = $("#listTable a.popu");

        [@flash_message /]

            // 删除
            $delete.click(function () {
                var $this = $(this);
                $.dialog({
                    type: "warn",
                    content: "${message("admin.dialog.deleteConfirm")}",
                    onOk: function () {
                        $.ajax({
                            url: "delete.jhtml",
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
            // 推广链接
            $popu.click(function () {
                var productChannelId = $(this).attr("val");
                $.dialog({
                    title: "获取推广链接",
                [@compress single_line = true]
                    content: '<div>' +
                    '<table style= "height: 141px;width:480px;">' +
                    '<tr>' +
                    '<th>选择推广的类型<\/th>' +
                    '<td>' +
                    '<select id = "urlType" name ="urlType">[#list urlTypes as urlType]<option value = "${urlType}">${message("Ad.UrlType."+urlType)}<\/option >[/#list]<\/select>' +
                    '<\/td><\/tr>' +
                    '<tr>' +
                    '<th>链接地址<\/th>' +
                    '<td><input type = "text" id = "url_result" class="text" style = "width:250px"\/><\/td><\/tr><tr><td><\/td><td>' +
                    '<a href = "javascript:;" id = "getUrl" class="button">获取<\/a><\/td><\/tr><\/table><\/div>',
                [/@compress]
                    width: 480,
                    modal: true,
                    cancel: "${message("admin.dialog.cancel")}",
                    onShow: function () {
                        $("#getUrl").click(function () {
                            $.ajax({
                                url: "${base}/ajax/common/getUrl.jhtml",
                                data: {
                                    urlType: $("#urlType").val(),
                                    id: productChannelId,
                                    businessType: 'product_channel'
                                },
                                dataType: "json",
                                type: "get",
                                success: function (message) {
                                    if (message.type == 'success') {
                                        $("#url_result").val(message.content);
                                    } else {
                                        $.message("获取失败！");
                                    }
                                }
                            });
                        });
                    }
                })
                ;
            });

        });
    </script>
</head>
<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 分类频道列表
</div>
<div class="bar">
    <a href="add.jhtml" class="iconButton">
        <span class="addIcon">&nbsp;</span>${message("admin.common.add")}
    </a>
    <a href="javascript:;" id="refreshButton" class="iconButton">
        <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
    </a>
</div>
<table id="listTable" class="list">
    <tr>
        <th width="15%">
            <span>名称</span>
        </th>
        <th width="45%">
            <span>描述</span>
        </th>
        <th width="15%">
            <span>采用模版</span>
        </th>
        <th width="10%">
            <span>排序</span>
        </th>
        <th width="12%">
            <span>管理</span>
        </th>
    </tr>
[#list productChannels as productChannel]
    <tr>
        <td>
        ${productChannel.name}
        </td>
        <td>
        ${productChannel.description}
        </td>
        <td>
        ${(productChannel.template.name)!}
        </td>
        <td>
        ${productChannel.order}
        </td>
        <td>
            <a href="edit.jhtml?id=${productChannel.id}">[${message("admin.common.edit")}]</a>
            <a href="javascript:;" class="delete" val="${productChannel.id}">[${message("admin.common.delete")}]</a>
            <a href="javascript:;" class="popu" val="${productChannel.id}">[链接]</a>
        </td>
    </tr>
[/#list]
</table>
</body>
</html>