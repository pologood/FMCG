var bank = {
    projectUrl:'',
    name: 'bank',
    url: '#bank',
    template: '#tpl_bank',
    fn: null,
    render: function (dataBlock) {
        if (dataBlock.message.type == "success") {
            html = '';
            for (var i = 0; i < dataBlock.data.length; i++) {
                html += '<li><a href="javascript:;" id=' + dataBlock.data[i].id + ' title=' + dataBlock.data[i].bankName + '>' + dataBlock.data[i].bankName + '</a></li>';
            }

            $("#weui_bank").find(".am-list").html(html);

            $("#weui_bank a").bind("click", function () {
                bank.fn($(this).attr("id"), $(this).attr("title"));
                window.history.back();
            });

        } else {
            showDialog2("出错了", dataBlock.message.content);
        }
    },
    load: function (id) {
        url = "";
        url = bank.projectUrl + "/app/member/bank/bank_info/list.jhtml";
        ajaxGet({
            url: url,
            data: id,
            success: function (dataBlock) {
                bank.render(dataBlock);
            }
        })
    }
};

$(function () {
    pageManager.push(bank);
});

/* ==========================================================================
 选择区域入口函数  fn(id,title) {   }
 ============================================================================ */

function findBank(fn) {
    pageManager.go("bank");
    bank.projectUrl=$("#projectUrl").val();
    bank.fn = fn;
    bank.load("");
    $('#weui_bank').show();
}
