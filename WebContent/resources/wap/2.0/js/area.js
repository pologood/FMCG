var area = {
    projectUrl:'',
    name: 'area',
    url: '#area',
    template: '#tpl_area',
    fn: null,
    render: function (dataBlock) {
        if (dataBlock.message.type == "success") {
            html = '';
            for (var i = 0; i < dataBlock.data.length; i++) {
                var areaclaname="";

                if(dataBlock.data[i].fullName=="中国"){
                    continue;
                }

                if(dataBlock.data[i].fullName.length==2){
                    areaclaname="twowords";
                }
                html += '<li><a href="javascript:;" class="'+areaclaname+'" id=' + dataBlock.data[i].id + ' child=' + dataBlock.data[i].hasChildren + ' title=' + dataBlock.data[i].fullName + '>' + dataBlock.data[i].fullName + '</a></li>';
            }

            $("#weui_area").find(".am-list").html(html);

            $("#weui_area a").bind("click", function () {
                if ($(this).attr("child") == "true") {
                    area.load($(this).attr("id"));
                } else {
                    area.fn($(this).attr("id"), $(this).attr("title"));
                    window.history.back();
                }
            });

        } else {
            showDialog2("出错了", dataBlock.message.content);
        }
    },
    load: function (id) {
        url = "";
        if (id == "") {
            url = area.projectUrl + "/app/area/roots.jhtml";
        } else {
            url = area.projectUrl + "/app/area/childrens.jhtml?areaId=" + id;
        }
        ajaxGet({
            url: url,
            data: id,
            success: function (dataBlock) {
                area.render(dataBlock);
            }
        })
    }
};

$(function () {
    pageManager.push(area);
});

/* ==========================================================================
 选择区域入口函数  fn(id,title) {   }
 ============================================================================ */

function findArea(fn) {
    pageManager.go("area");
    area.projectUrl=$("#projectUrl").val();
    area.fn = fn;
    area.load("");
    $('#weui_area').show();
}
