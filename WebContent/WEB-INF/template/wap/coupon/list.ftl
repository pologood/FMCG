<!DOCTYPE html>
<html lang="en">
<head>
    <title>优惠券</title>
[#include "/wap/include/resource-2.0.ftl"]

    <link rel="stylesheet" href="${base}/resources/wap/3.0/css/coupon.css">
    <style type="text/css">
        .weui_cell_ft:after {
            display: none !important;
        }

    </style>
    <script src="${base}/resources/wap/2.0/js/handlebars.min.js"></script>
    <script type="text/x-handlebars-template" id="coupon_list">
        <div class="am-cf">
            {{#each this}}
            {{#compare type 'multipleCoupon'}}
            <a href="${base}/wap/coupon/balance.jhtml" class="platform weui-cell">
                <div class="weui-cell__hd"><div class="bg"><span>￥{{amount}}</span></div></div>
                <div class="weui-cell__bd">平台券</div>
            </a>
            {{/compare}}
            {{#if hasExpired}}

            {{else}}
            <div href="javascript:;" class="am-cf simcell-youhuiquan">
                <a href="javascript:;" class="am-fl store-avatar">
                    <div class="store-avatar-rap">
                        <img class="lazy" src="${base}/resources/wap/2.0/images/AccountBitmap-tenant.png"
                             data-original="{{thumbnail}}" alt="icon"></div>
                </a>
                <div class="am-fl ctn">
                    <p class="title1">{{#abbreviate tenantName 5 "..."}}{{/abbreviate}}</p>
                    <p class="title2">
                        使用日期 {{#formatDate startDate}}{{/formatDate}}-{{#formatDate endDate}}{{/formatDate}}
                    </p>
                </div>
                <div class="am-fl youhuiquan">
                    <div class="up">
                        <div class="in-table"><p class="in-cell">￥{{amount}}</p></div>
                    </div>
                    <div class="down">
                        <div class="in-table"><p class="in-cell">满{{minimumPrice}}可用</p></div>
                    </div>
                    {{#if goingExpired}}
                    <div class="expiredsign"></div>
                    {{/if}}
                </div>
            </div>
            {{/if}}
            {{/each}}
        </div>
    </script>
</head>
<body>
[#include "/wap/include/static_resource.ftl"]

<div class="container">

</div>

<script type="text/html" id="tpl_wraper">

    <div class="page">

    </div>
</script>
[#include "/wap/include/resource-3.0-part2.ftl"]
<script src="${base}/resources/wap/2.0/js/weui-extend.js"></script>
<script type="text/javascript">
    /**
     *receive remote data here first
     * 后台刷入数据使用前先存储到_TH_
     */
    var _TH_ = {};
    /**
     * 基准路径base
     */
    _TH_.base = "${base}"; //${base}>
    $(function () {
        init();

        Handlebars.registerHelper("formatDate", function (value) {
            if (value == null || value == "") {
                return "";
            } else {
                var date = new Date(value);
                return date.getFullYear() + "." + (date.getMonth() + 1) + "." + date.getDate();
            }
        });
        Handlebars.registerHelper("abbreviate", function (value, count, str) {
            if (value == null || value == "") {
                return "";
            } else {
                var length = value.length;
                if (length <= count) {
                    return value;
                }
                var result = value.substring(0, count);
                if (str) {
                    result += str;
                }
                return result;
            }
        });
        var compiler = Handlebars.compile($("#coupon_list").html());

        ajaxGet({
            url: '${base}/wap/coupon/coupon_list.jhtml',
            success: function (data) {
                if (data.length == 0) {
                    $(".page").html("<div style='text-align: center;line-height: 50px;'>暂无优惠券<div>");
                    return;
                }
                $(".page").html(compiler(data));
                $(".lazy").picLazyLoad({
                    threshold: 100,
                    placeholder: '${base}/resources/wap/2.0/images/AccountBitmap-tenant.png'
                });
            }
        });
    });

</script>

</body>
</html>