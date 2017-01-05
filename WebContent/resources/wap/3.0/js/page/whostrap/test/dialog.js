/**
 * page js
 * @for page/whostrap/test/tip
 */
if (_TH_.tscpath == "page/whostrap/test/dialog") {
    $(function() {
        //初始化对话框 neterror
        $(".neterror.WHT").dialog({
            dialogtype: "alert",
            tpldatas: {
                ctns: {
                    txt_ok: "确定",
                    txt_cancel: "取消",
                    txt_title: "错误信息",
                    txt_body: "网络异常，请检查网络连接是否正常"
                },
                hooks: {
                    wrapperid: "neterror"
                },
                Ctrls: {
                    tplname: "alert"
                }
            }
        });
        //初始化对话框 switchtocity
        $(".switchtocity.WHT").dialog({
            dialogtype: "confirm",
            tpldatas: {
                ctns: {
                    txt_ok: "确定",
                    txt_cancel: "取消",
                    txt_title: "提示信息",
                    txt_body: "是否切换到当前城市?"
                },
                hooks: {
                    wrapperid: "switchtocity"
                },
                Ctrls: {
                    tplname: "confirm"
                }
            }
        });

        $(".btns").on('click', '.opendialog1', function(event) {
            event.preventDefault();
            $(".neterror.WHT").dialog("toggle");
        });
        $(".neterror.WHT").on('opened:WHT:dialog', function(event) {
            console.log("对话框" + $(this).attr("class") + "已开启");
        });
        $(".neterror.WHT").on('closed:WHT:dialog', function(event) {
            console.log("对话框" + $(this).attr("class") + "已关闭");
        });
        $(".btns").on('click', '.opendialog2', function(event) {
            event.preventDefault();
            $(".switchtocity.WHT").dialog("toggle");
        });
        //监听插件格式化事件
        $(".switchtocity.WHT").on('determined:WHT:dialog:confirm', function(event) {
            console.log("你点了确定按钮");
            //城市切换逻辑
        });
        $(".switchtocity.WHT").on('opened:WHT:dialog', function(event) {
            console.log("对话框" + $(this).attr("class") + "已开启");
        });
        $(".switchtocity.WHT").on('closed:WHT:dialog', function(event) {
            console.log("对话框" + $(this).attr("class") + "已关闭");
        });
    });
}
