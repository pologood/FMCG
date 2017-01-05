<ul id="first_list"></ul>
<script type="text/javascript">
    var _src = "";
    [@ad_position id=116 areaId=area.id count=1]
        [#if adPosition?has_content]
            [#list adPosition.ads as ads]
                [#if ads.path?has_content]
                _src = '${ads.path}';
                [/#if]
            [/#list]
        [/#if]
    [/@ad_position]
    //全部商品分类
    //initCategoryes();<img style='width:20px;height20px' src='"+first_list.image+"'><i class='icon0" + (i + 1) + "'></i>

    initCategory();
    for (var i = 0; i < categorys.length; i++) {
        var first_list = categorys[i];
        $("#first_list").append("<li><h1><a href='${base}/b2b/product/list/" + first_list.id + ".jhtml'><span><img src='"+first_list.image+"'></span>" + first_list.name + "</a></h1><div class='menuNavList'><div  class='menuNavListCont' id='secend_list" + first_list.id + "'></div><div class='menuNavListImg'><a href='javascript:;'><img src='" + _src + "'/></a></div></div></li>");
        for (var n = 0; n < first_list.childrens.length; n++) {
            var secend_list = first_list.childrens[n];
            var _dl = "<dl><dt><a href='${base}/b2b/product/list/" + secend_list.id + ".jhtml'>";
            if (first_list.childrens.length <= 4) {
                _dl = "<dl style='width: 100%'><dt><a href='${base}/b2b/product/list/" + secend_list.id + ".jhtml'>";
            }
            $("#secend_list" + first_list.id).append(_dl + secend_list.name + "</a></dt><dd id='third_list" + secend_list.id + "'></dd></dl>");
            for (var t = 0; t < secend_list.childrens.length; t++) {
                var third_list = secend_list.childrens[t];
                $("#third_list" + secend_list.id).append("<a href='${base}/b2b/product/list/" + third_list.id + ".jhtml'>" + third_list.name + "</a>");
            }
        }
    }
</script>
