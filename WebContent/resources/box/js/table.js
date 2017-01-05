
$(document).ready(function(e) {
    $(".table_list tr:not(tr:first)").mouseover(function(e) {
        $(this).addClass("tr_hover_bg").attr("title", "双击鼠标查看详情");
    }).mouseout(function(e) {
        $(this).removeClass("tr_hover_bg");
    });
    
    $(".table_list2 tr:not(tr:first)").mouseover(function(e) {
        $(this).addClass("tr_hover_bg");
    }).mouseout(function(e) {
        $(this).removeClass("tr_hover_bg");
    });
});