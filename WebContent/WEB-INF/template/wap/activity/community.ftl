<!DOCTYPE html>
<html>
<head>
[#include "/wap/include/resource-3.0-part1.ftl"/]
    <title>${community}</title>
    <style type="text/css">
        .bg_act_924 {
            background-color: #e9e9e9;
        }

        .list_item.ACT828 .items {
            padding: 0;
        }

        .item .img {
            position: relative;
        }

        .item .img .img_label {
            position: absolute;
            bottom: 0;
            left: 0;
            padding: 0 2vw;
            background-color: rgba(0, 0, 0, 0.4);
            color: #fff;
            width: 100%;
            font-size: 2.5vw;
        }

        .item .shangquan_title {
            background-color: #fff;
            font-size: 4vw;
            padding: 2vw;
            display: flex;
           

        }

        .item .shangquan_title span {
            flex: 1;
            color: #333;
        }

        .item .shangquan_title a {
            color: #eb3341;
            font-weight: 600;
        }
    </style>
</head>

<body class="bg_act_924" >
<!--活动-内容-->
<div class="content">
    <div class="list_item ACT828">
        <div class="items">
        [#list tenants as tenant]
            <div class="item">
                <div class="img">
                    <img src="${base}/resources/wap/activity/${tenant.id}.png" width="100%"/>
                    [#--<div class="img_label">满100减10，全场8.8折</div>--]
                </div>
                <div class="shangquan_title">
                    <span>${tenant.name}</span>
                    <a href="${base}/wap/tenant/index/${tenant.id}.jhtml">${tenant.activityName}</a>
                </div>
            </div>
        [/#list]
        </div>
    </div>
</div>

</body>

</html>
