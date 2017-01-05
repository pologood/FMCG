<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>${message("admin.productCategory.list")} - Powered By rsico</title>
    <meta name="author" content="rsico Team"/>
    <meta name="copyright" content="rsico"/>
    <link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css"/>
    <link href="${base}/resources/admin/css/TreeGrid.css" rel="stylesheet" type="text/css"/>

    <script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/TreeGrid.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
    <script type="text/javascript">
        $().ready(function () {

            var $delete = $(".delete");
            //$(".example").treeTable();
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

        });
    </script>
</head>
<body>
<div class="path">
    <a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.productCategory.list")}
</div>
<div class="bar">
    <a href="add.jhtml" class="iconButton">
        <span class="addIcon">&nbsp;</span>${message("admin.common.add")}
    </a>
    <a href="javascript:;" id="refreshButton" class="iconButton">
        <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
    </a>

    <input type="button" class="button" value="关闭所有节点" onclick="expandAll('N')">
    <input type="button" class="button" value="展开所有节点" onclick="expandAll('Y')">

</div>

<div id="div1"></div>

<script language="javascript">
    var config = {
        id: "tg1",
        width: "100%",
        renderTo: "div1",
        headerAlign: "left",
        headerHeight: "30",
        dataAlign: "left",
        indentation: "20",
        folderOpenIcon: "${base}/resources/admin/images/folderOpen.gif",
        folderCloseIcon: "${base}/resources/admin/images/folderClose.gif",
        defaultLeafIcon: "",
        hoverRowBackground: "false",
        folderColumnIndex: "0",
        itemClick: "itemClickEvent",
        columns: [
            // {headerText: "", headerAlign: "center", dataAlign: "center", width: "20", handler: "customCheckBox"},
            {
                headerText: "${message('ProductCategory.name')}",
                dataField: "name",
                headerAlign: "center",
                handler: "customOrgName"
            },
            {headerText: "图片", dataField: "image", headerAlign: "center", dataAlign: "center", width: "100"},
            {headerText: "编号", dataField: "code", headerAlign: "center", dataAlign: "center", width: "100"},
            {
                headerText: "${message('admin.common.order')}",
                dataField: "assignee",
                headerAlign: "center",
                dataAlign: "center",
                width: "100"
            },
            {
                headerText: "${message('admin.common.handle')}",
                headerAlign: "center",
                dataAlign: "center",
                width: "150",
                handler: "customLook"
            }
        ],
        data: ${(productCategoryTree)!}
    };

    /*
        单击数据行后触发该事件
        id：行的id
        index：行的索引。
        data：json格式的行数据对象。
    */
    function itemClickEvent(id, index, data) {
        jQuery("#currentRow").val(id + ", " + index + ", " + TreeGrid.json2str(data));
    }

    /*
        通过指定的方法来自定义栏数据
    */
    function customCheckBox(row, col) {
        return "<input type='checkbox'>";
    }

    function customOrgName(row, col) {
        var name = row[col.dataField] || "";
        return name;
    }

    function customLook(row, col) {
        return '<a href="${base}/b2c/product/list/' + row.code + '.jhtml" target="_blank">[${message("admin.common.view")}]</a>' +
                '&nbsp;&nbsp;<a href="edit.jhtml?id=' + row.code + '">[${message("admin.common.edit")}]</a>' +
                '&nbsp;&nbsp;<a href="javascript:;" class="delete" val="' + row.code + '">[${message("admin.common.delete")}]</a>';
    }

    //创建一个组件对象
    var treeGrid = new TreeGrid(config);
    treeGrid.show();

    /*
        展开、关闭所有节点。
        isOpen=Y表示展开，isOpen=N表示关闭
    */
    function expandAll(isOpen) {
        treeGrid.expandAll(isOpen);
    }

    /*
        取得当前选中的行，方法返回TreeGridItem对象
    */
    function selectedItem() {
        var treeGridItem = treeGrid.getSelectedItem();
        if (treeGridItem != null) {
            //获取数据行属性值
            //alert(treeGridItem.id + ", " + treeGridItem.index + ", " + treeGridItem.data.name);

            //获取父数据行
            var parent = treeGridItem.getParent();
            if (parent != null) {
                //jQuery("#currentRow").val(parent.data.name);
            }

            //获取子数据行集
            var children = treeGridItem.getChildren();
            if (children != null && children.length > 0) {
                jQuery("#currentRow").val(children[0].data.name);
            }
        }
    }
</script>
</body>
</html>