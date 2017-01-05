[#-- @ftlroot "../../../" --]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>我的网店</title>
    <meta name="baidu-site-verification" content="7EKp4TWRZT"/>
[@seo type = "index"]
    <title> [#if seo.title??][@seo.title?interpret /][#else]首页[/#if]</title>
    [#if seo.keywords??]
        <meta name="keywords" content="[@seo.keywords?interpret /]"/>
    [/#if]
    [#if seo.description??]
        <meta name="description" content="[@seo.description?interpret /]"/>
    [/#if]
[/@seo]
    <link href="${base}/resources/helper/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/helper/font/iconfont.css" type="text/css" rel="stylesheet"/>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/input.js"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=Gs8IEsy5WZ5qL6KFK8bnbsVT"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {

            var $inputForm = $("#inputForm");
            var $areaId = $("#areaId");
            var $communityId = $("#communityId");
            var timeout;
        [@flash_message /]

            function getCommunity() {
                if ($areaId.val() > 0) {
                    $.ajax({
                        url: "get_community.jhtml",
                        type: "GET",
                        data: {areaId: $areaId.val()},
                        dataType: "json",
                        cache: false,
                        success: function (data) {
                            var opt = '<option>--请选择--</option>';
                            $.each(data,function(i,v){
                                opt += '<option value="' + v.id + '" lat="'+v.location.lat+'" lng="'+v.location.lng+'">' + v.name + '</option>';
                            });
                            $communityId.html(opt);
                        }
                    });
                }

            }

            // 地区选择
            $areaId.lSelect({
                url: "${base}/common/area.jhtml",
                fn: getCommunity
            });

            //清除火狐浏览器刷新多出拉下框
            $("select[name='areaId_select']").each(function () {
                if ($(this).val() == "") {
                    $(this).nextAll("select").remove();
                    return false;
                }
            });

            // 表单验证
            $inputForm.validate({
                rules: {
                    sn: "required",
                    name: "required",
                    contact: "required",
                    areaId: "required",
                    address: "required",
                    zipCode: {
                        pattern: /^\d{3,8}$/
                    },
                    phone: {
                        pattern: /^((\d{3,4}-\d{7,8})|(1[23456798]\d{9}))$/
                    },
                    mobile: {
                        pattern: /^\d{8,11}$/
                    }
                },
                messages: {
                    sn: "${message("admin.validate.required")}",
                    name: "${message("admin.validate.required")}",
                    contact: "${message("admin.validate.required")}",
                    areaId: "${message("admin.validate.required")}",
                    address: "${message("admin.validate.required")}",
                    zipCode: {
                        pattern: "请填写正确的邮编号码"
                    },
                    phone: {
                        pattern: "请填写正确的号码"
                    },
                    mobile: {
                        pattern: "请填写正确的号码"
                    }
                }
            });

            getCommunity();
        });
    </script>

</head>
<body>


[#include "/helper/include/header.ftl" /]
[#include "/helper/member/include/navigation.ftl" /]
<div class="desktop">
    <div class="container bg_fff">

    [#include "/helper/member/include/border.ftl" /]

    [#include "/helper/member/include/menu.ftl" /]

        <div class="wrapper" id="wrapper">

            <div class="page-nav page-nav-app vip-guide-nav" id="app_head_nav">
                <div class="js-app-header title-wrap" id="app_0000000844">
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/address-manage.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">发货地址</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">维护和添加的店铺的发货地址，包括区域、社区、地址信息位置等。</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="on" hideFocus="" href="javascript:;">修改地址</a></li>
                    <li><a class="" hideFocus="" href="${base}/helper/member/delivery_center/list.jhtml">地址列表</a></li>
                </ul>
            </div>
            <div class="list" style="margin-top: 0px;border-right: 1px solid #d8deea;">
                <div id="map" style="position:absolute;margin-top:110px;margin-left:380px;display: none;">
                    <div style="width:520px;height:340px;border:1px solid gray" id="mapform"></div>
                    <p><input type="button" onclick="closeMap()" value="关闭"/></p>
                </div>
                <form id="inputForm" action="update.jhtml" method="post">
                    <input type="hidden" name="id" value="${deliveryCenter.id}"/>
                    <table class="input">
                        <tr>
                            <th>
                                <span class="requiredField">*</span>编码:
                            </th>
                            <td>
                                <select name="sn" class="text" onchange="get(this.value)">
                                    <option value="${deliveryCenter.sn}">${deliveryCenter.sn}</option>
                                </select>
                                <!-- <input type="text" name="sn" class="text" value="${deliveryCenter.sn}" maxlength="20" />  -->
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <span class="requiredField">*</span>${message("DeliveryCenter.name")}:
                            </th>
                            <td>
                                <input type="text" name="name" class="text" value="${deliveryCenter.name}"
                                       maxlength="20"/>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <span class="requiredField">*</span>${message("DeliveryCenter.contact")}:
                            </th>
                            <td>
                                <input type="text" name="contact" class="text" value="${deliveryCenter.contact}"
                                       maxlength="20"/>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <span class="requiredField">*</span>${message("DeliveryCenter.area")}:
                            </th>
                            <td>
									<span class="fieldSet">
										<input type="hidden" id="areaId" name="areaId"
                                               value="${(deliveryCenter.area.id)!}"
                                               treePath="${(deliveryCenter.area.treePath)!}"/>
									</span>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                所属商圈:
                            </th>
                            <td>
									<span class="fieldSet">
								   	<select id="communityId" name="communityId">
                                        <option value="">--请选择--</option>
                                    </select>
									</span>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <span class="requiredField">*</span>${message("DeliveryCenter.address")}:
                            </th>
                            <td>
                                <input type="text" id="address" name="address" class="text"
                                       value="${deliveryCenter.address}" maxlength="30"/>
                            </td>
                        </tr>

                        <tr>
                            <th>
                                <span class="requiredField">*</span>地理位置:
                            </th>
                            <td>
                                <input type="hidden" name="lat" id="lat" class="text"
                                       value="[#if deliveryCenter.location??]${deliveryCenter.location.lat}[/#if]"/>
                                <input type="hidden" name="lng" id="lng" class="text"
                                       value="[#if deliveryCenter.location??]${deliveryCenter.location.lng}[/#if]"/>
                                <span id="location">[#if deliveryCenter.location??](lat:${deliveryCenter.location.lat},lng:${deliveryCenter.location.lng})[/#if]</span><input
                                    type="button" class="button" onclick="openMap();" value="获取"/>
                            </td>
                        </tr>

                        <tr>
                            <th>
                            ${message("DeliveryCenter.mobile")}:
                            </th>
                            <td>
                                <input type="text" name="mobile" class="text" value="${deliveryCenter.mobile}"
                                       maxlength="12"/>
                            </td>
                        </tr>
                        <tr>
                            <th>
                            ${message("DeliveryCenter.isDefault")}:
                            </th>
                            <td>
                                <input type="checkbox" name="isDefault"[#if deliveryCenter.isDefault]
                                       checked="checked"[/#if]/>
                                <input type="hidden" name="_isDefault" value="false"/>
                            </td>
                        </tr>

                        <tr>
                            <th>
                                &nbsp;
                            </th>
                            <td>
                                <input type="submit" class="button" value="${message("admin.common.submit")}"/>
                                <input type="button" class="button" value="${message("admin.common.back")}"
                                       onclick="location.href='list.jhtml'"/>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
<script type="text/javascript">
    var map = new BMap.Map("mapform");                        // 创建Map实例
    map.centerAndZoom("厦门市", 18);
    map.addControl(new BMap.NavigationControl());
    map.addControl(new BMap.ScaleControl());
    map.addControl(new BMap.OverviewMapControl());
    map.addEventListener("click", function (e) {   //单击地图，形成折线覆盖物
        var newpoint = new BMap.Point(e.point.lng, e.point.lat);
        var gcj02=bd09togcj02(e.point.lng, e.point.lat);
        $("#lat").val(gcj02[1]);
        $("#lng").val(gcj02[0]);
        $("#location").html("(lat:" + gcj02[1] + ",lng:" + gcj02[0] + ")");
        map.clearOverlays();
        var mkr = new BMap.Marker(newpoint);
        map.addOverlay(mkr);
    });
    function closeMap() {
        $("#map").hide();
    }
    function openMap() {
        $.ajax({
            url: "${base}/common/area_key.jhtml",
            type: "GET",
            data: {id: $("#areaId").val()},
            dataType: "json",
            cache: false,
            success: function (area) {
                var myGeo = new BMap.Geocoder();
                var address=$("#address").val().trim();
                myGeo.getPoint(area.fullName + address, function (point) {
                    var $option=$("#communityId option:selected");
                    var lat = $option.attr("lat");
                    var lng = $option.attr("lng");
                    if(address==""&&lat&&lng){
                        var location=new BMap.Point(lng,lat);
                        var convertor = new BMap.Convertor();
                        var pointArr = [];
                        pointArr.push(location);
                        convertor.translate(pointArr, 3, 5, function(data){
                            if(data.status===0){
                                $("#lat").val(location.lat);
                                $("#lng").val(location.lng);
                                $("#location").html("(lat:" + location.lat + ",lng:" + location.lng + ")");
                                map.centerAndZoom(data.points[0], 16);
                                map.clearOverlays();
                                map.addOverlay(new BMap.Marker(data.points[0]));
                            }
                        });
                    }else if (point) {
                        var gcj02=bd09togcj02(point.lng,point.lat);
                        $("#lat").val(gcj02[1]);
                        $("#lng").val(gcj02[0]);
                        $("#location").html("(lat:" + gcj02[1] + ",lng:" + gcj02[0] + ")");
                        map.centerAndZoom(point, 16);
                        map.clearOverlays();
                        map.addOverlay(new BMap.Marker(point));
                    }
                }, "北京市");
            }
        });
        $("#map").show();
    }
    function get(obj) {
        alert(obj);
    }

    /**
     * 百度坐标系 (BD-09) 与 火星坐标系 (GCJ-02)的转换
     * 即 百度 转 谷歌、高德
     * @param bd_lon
     * @param bd_lat
     * @returns {*[]}
     */
    function bd09togcj02(bd_lon, bd_lat) {
        var x_pi = 3.14159265358979324 * 3000.0 / 180.0;
        var x = bd_lon - 0.0065;
        var y = bd_lat - 0.006;
        var z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        var theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        var gg_lng = z * Math.cos(theta);
        var gg_lat = z * Math.sin(theta);
        return [gg_lng, gg_lat]
    }

</script>
