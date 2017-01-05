<!DOCTYPE html>
<html lang="en">
<head>
[#include "/wap/include/resource-2.0.ftl"/]
    <title>选择要秀的商品</title>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script type="text/x-handlebars-template" id="wap-list-item">
        {{#each data}}
        <div class="weui_cell" href="javascript:;">
        [#if type=='order']
            <div class="weui_cell_hd">
                <input style="zoom:150%;" type="checkbox" name="check_product" value="{{orderItem.productId}}">
            </div>
        [/#if]
            <div class="weui_cell_hd">
                <img src="${base}/resources/wap/2.0/images/AccountBitmap-product.png"
                     data-original="{{orderItem.thumbnail}}" class="lazy" alt="icon"
                     style="width:80px;margin-right:5px;display:block">
            </div>
            <div class="weui_cell_bd weui_cell_primary">
                <p style="font-size:15px;">{{orderItem.fullName}}</p>
                <p style="font-size:15px;color:#E6C99C">￥{{orderItem.price}}
                    {{#if orderItem.promotions}}
                    {{#each orderItem.promotions}}
                    <span class="order-discount order-prefer" style="margin-left: 2rem;font-size: 12px;">{{name}}</span>
                    {{/each}}
                    {{/if}}
                </p>
            </div>
        [#if type=='camera']
            <div style="text-align:right;">
            <span class="order-discount order-prefer" style="margin-left: 5rem;" data-id="0"
                  onclick="getProduct($(this),{{orderItem.productId}})">
                        关联
                </span>
            </div>
        [/#if]
        </div>
        {{/each}}
    </script>
</head>
<body>
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>
<script type="text/html" id="tpl_wraper">
    <div class="weui_cells weui_cells_access" style="margin-top: 0.8rem;">
        <div id="order_show_list"></div>
    </div>
    <header class="am-topbar am-topbar-fixed-bottom bg-lightslategray">
        <div class="weui_cell">
            <a href="javascript:getProductId();" class="weui_btn weui_btn_warn" style="width:80%;" id="save">下一步</a>
        </div>
    </header>
    <!-- BEGIN empty div for fixed ele -->
    <div class="empty-for-fixedbottom_tab"></div>
</script>
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script>
    $(function () {
        init();
        var compiler = Handlebars.compile($("#wap-list-item").html());
        ajaxGet({
            url: "${base}/app/member/order/list.jhtml",
            success: function (data) {
                $("#order_show_list").html(compiler(data));
                $(".lazy").picLazyLoad({
                    threshold: 100,
                    placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-product.png'
                });
            }
        });
    });

    var _product = [];
    function getProduct(o, productId) {
        var dataId = o.attr('data-id');
        if (dataId == '0') {
            o.removeClass("order-discount");
            o.addClass("order-gift1");
            o.attr('data-id', '1');
            _product.push(productId);
        } else {
            o.removeClass("order-gift1");
            o.addClass("order-discount");
            o.attr('data-id', '0');
            _product.remove(productId);
        }
    }

    function getProductId() {
        var obj = document.getElementsByName("check_product");
        var checked_obj = '';
        for (var i = 0; i < obj.length; i++) {
            if (obj[i].checked == true) {
                checked_obj += "&ids=" + obj[i].value;
            }
        }
        if (_product.length > 0) {
            checked_obj = '';
            for (var j = 0; j < _product.length; j++) {
                checked_obj += "&ids=" + _product[j];
            }
        }

        if (checked_obj == "") {
            var message = {content: '请选择商品'};
            showToast2(message);
        } else {
            location.href = "${base}/wap/social_circles/publish.jhtml?type=${type}" + checked_obj;
        }
    }

    Array.prototype.indexOf = function (val) {
        for (var i = 0; i < this.length; i++) {
            if (this[i] == val) return i;
        }
        return -1;
    };

    Array.prototype.remove = function (val) {
        var index = this.indexOf(val);
        if (index > -1) {
            this.splice(index, 1);
        }
    };
</script>
</body>
</html>