<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN""http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
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
    <link href="${base}/resources/helper/css/tenant.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.placeholder.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/search.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript">
        $().ready(function () {
            //上传图片
            $(".upload_img01").each(function () {
                if ($(this).attr("src") == "") {
                    $(this).hide();
                }
            });
            var $authenIndex = $("#authenIndex");
            var $enterpriseCertification = $("#enterpriseCertification");
            var $certifiedStores = $("#certifiedStores");
            var $manufacturersCertification = $("#manufacturersCertification");

            var $enterpriseAutForm = $("#enterpriseAutForm");
            var $storesAutForm = $("#storesAutForm");
            var $manufacturersAutForm = $("#manufacturersAutForm");
            $enterpriseCertification.hide();
            $certifiedStores.hide();
            $manufacturersCertification.hide();

            //$manufacturersCertification.show();

            $("#enterpriseAut").click(function () {
                $authenIndex.hide();
                $enterpriseCertification.show();
                $certifiedStores.hide();
                $manufacturersCertification.hide();
            });

            $("#storeAut").click(function () {
                $authenIndex.hide();
                $enterpriseCertification.hide();
                $certifiedStores.show();
                $manufacturersCertification.hide();
            });

            $("#manufacturersAut").click(function () {
                $authenIndex.hide();
                $enterpriseCertification.hide();
                $certifiedStores.hide();
                $manufacturersCertification.show();
            });
            //调用图片控件
            function addBrowserImage(url) {
                $("#browserImage").show().attr("src", url);
                $("#licensePhoto").val(url);
                $("#licensePhoto").siblings("label.fieldError").remove();
            }

            function addBrowserStoresImage(url) {
                $("#browserStoresImage").show().attr("src", url);
                $("#storesPhoto").val(url);
                $("#storesPhoto").siblings("label.fieldError").remove();
            }

            function addBrowserManufacturersImage(url) {
                $("#browserManufacturersImage").show().attr("src", url);
                $("#manufacturersPhoto").val(url);
                $("#manufacturersPhoto").siblings("label.fieldError").remove();
            }

            var $browserButton = $("#browserButton");
        [@flash_message /]

            var browserSettings = {
                width: 360,
                height: 360,
                isSubmit: false,
                callback: addBrowserImage
            };
            $browserButton.browser(browserSettings);

            var $browserStoresButton = $("#browserStoresButton");

            var storesSettings = {
                width: 360,
                height: 360,
                isSubmit: false,
                callback: addBrowserStoresImage
            };
            $browserStoresButton.browser(storesSettings);

            var $browserManufacturersButton = $("#browserManufacturersButton");

            var manufacturersSettings = {
                width: 360,
                height: 360,
                isSubmit: false,
                callback: addBrowserManufacturersImage
            };
            $browserManufacturersButton.browser(manufacturersSettings);
            // 企业认证
            $enterpriseAutForm.validate({
                rules: {
                    "name": {
                        required: true
                    },
                    "licenseCode": {
                        required: true
                    },
                    "legalRepr": {
                        required: true
                    },
                    "licensePhoto": {
                        required: true
                    },
                    "areaId_select": {
                        required: true
                    },
                    "address": {
                        required: true
                    }
                },
                messages: {
                    name: "${message("admin.validate.required")}",
                    licenseCode: "${message("admin.validate.required")}",
                    legalRepr: "${message("admin.validate.required")}",
                    licensePhoto: "${message("admin.validate.required")}",
                    areaId_select: "${message("admin.validate.required")}",
                    address: "${message("admin.validate.required")}"
                }
            });
            // 门店认证
            $storesAutForm.validate({
                rules: {
                    "storesPhoto": {
                        required: true
                    }
                },
                messages: {
                    storesPhoto: "${message("admin.validate.required")}"
                }
            });
            // 厂家授权认证
            $manufacturersAutForm.validate({
                rules: {
                    "authorization": {
                        required: true
                    },
                    "manufacturersPhoto": {
                        required: true
                    }
                },
                messages: {
                    authorization: "${message("admin.validate.required")}",
                    manufacturersPhoto: "${message("admin.validate.required")}"
                }
            });
            //获取地区
            $("#areaId").lSelect({
                url: "${base}/common/area.jhtml"
            });
        });
        function authenLayer1() {
            $("#authenIndex").css("display", "block");
            $("#enterpriseCertification").css("display", "none");
            $("#certifiedStores").css("display", "none");
            $("#manufacturersCertification").css("display", "none");
        }
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
                    <img class="js-app_logo app-img" src="${base}/resources/b2b/images/v2.0/shop-data.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">店铺认证</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">认证信息</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="" hideFocus="" href="${base}/helper/member/tenant/edit.jhtml?type=0">基本信息</a></li>
                    <li><a class="" hideFocus="" href="${base}/helper/member/ad/list.jhtml">店铺装修</a></li>
                    <li><a class="" hideFocus="" href="${base}/helper/member/article/list.jhtml">店铺公告</a></li>
                    [#--<li><a class="on" hideFocus="" href="${base}/helper/member/authen/index.jhtml">店铺认证</a></li>--]
                </ul>

            </div>
            <div class="box_con_right">
                <div id="authenIndex" class="authen_div">
                    <!--  <h1>平台认证</h1>
                     <p>
                       1.平台提供多个认证种类供您自主选择，并以对应的徽章最终展示在您的店铺首页中。<br>
                       2.平台认证完成的越多，买家购买时就越放心！
                     </p> -->
                    <table width="100%" border="0" cellspacing="1" cellpadding="0">
                        <tr>
                            <th width="150" scope="col">认证类型</th>
                            <th scope="col">认证内容</th>
                            <th width="100" scope="col">认证状态</th>
                            <th width="95" scope="col">操作</th>
                        </tr>
                        [#--<tr>--]
                            [#--<td>企业认证</td>--]
                            [#--<td class="txt_td">上传您企业真实的营业执照副本的照片，并等待后台的审核通过。<b>企业认证</b>是${setting.siteHelper}平台上开店的必要环节，方便买家关注您以及提高您店铺的信任感！--]
                            [#--</td>--]
                            [#--<td>[#if enterpriseStatus=="fail"]认证失败[#elseif enterpriseStatus=="wait"]--]
                                [#--审核中[#elseif enterpriseStatus=="success"]已认证[#else]未认证[/#if]</td>--]
                            [#--<td><input [#if enterpriseStatus=="success"]disabled="disabled" class="disabled"[/#if]--]
                                       [#--id="enterpriseAut" type="button"--]
                                       [#--value="[#if enterpriseStatus=="wait"]重新认证[#else]我要认证[/#if]"/></td>--]
                        [#--</tr>--]
                        <tr>
                            <td>门店认证</td>
                            <td class="txt_td">上传您实体店铺的外貌全景照片，并等待后台的审核通过。<b>门店认证</b>的通过能够让买方相信您的真实存在！</td>
                            <td>[#if certifiedStatus=="fail"]认证失败[#elseif certifiedStatus=="wait"]
                                审核中[#elseif certifiedStatus=="success"]已认证[#else]未认证[/#if]</td>
                            <td><input [#if certifiedStatus=="success"]disabled="disabled" class="disabled"[/#if]
                                       id="storeAut" type="button"
                                       value="[#if certifiedStatus=="wait"]重新认证[#else]我要认证[/#if]"/></td>
                        </tr>
                        <tr>
                            <td>厂家授权</td>
                            <td class="txt_td">上传您与厂家签订的授权或代理的合同/协议，或者是厂家颁发给您的合法有效的授权证书，并等待后台的审核通过。通过此认证展示您的实力吧！</td>
                            <td>[#if manufacturersStatus=="fail"]认证失败[#elseif manufacturersStatus=="wait"]
                                审核中[#elseif manufacturersStatus=="success"]已认证[#else]未认证[/#if]</td>
                            <td><input [#if manufacturersStatus=="success"]disabled="disabled" class="disabled"[/#if]
                                       id="manufacturersAut" type="button"
                                       value="[#if manufacturersStatus=="wait"]重新认证[#else]我要认证[/#if]"/></td>
                        </tr>
                        <tr>
                            <!--td>在线支付</td>
                            <td class="txt_td">开通-在线支付-并让客户知道您的店铺可以提供多种支付渠道，方便他/她的买卖，便捷您的交易！</td>
                            <td>未认证</td>
                            <td><input id="payAut" class="disabled" type="button" value="关闭"></input></td-->
                        </tr>
                    </table>
                </div>
                <!-- 认证弹出窗 start -->
                <div id="enterpriseCertification" class="authen_busi_div" class="authen_busi_div">
                    <h3 class="busi_tit">企业实名认证</h3>
                    <p class="busi_txt">请按照提示填写企业真实的资料</p>
                    <form id="enterpriseAutForm" action="${base}/helper/member/authen/enterpriseCertification.jhtml"
                          method="post">
                        <input type="hidden" name="id" value="${(tenant.id)!}">
                        <input type="hidden" name="tagId" value="26">
                        <table class="authen_table01">
                            <tr>
                                <th align="right" width="140">用户账号：</th>
                                <td>${tenant.telephone}</td>
                            </tr>
                            <tr>
                                <th align="right"><span>*</span>公司名称：</th>
                                <td><input type="text" name="name" class="text" value="${tenant.name}"/></td>
                            </tr>
                            <tr>
                                <th align="right" valign="top"><span>*</span>营业执照：</th>
                                <td>
                                    <div class="authen_upload">
                                        <p class="up_p01">工商注册号：<input type="text" name="licenseCode" class="text"
                                                                       value="${tenant.licenseCode}"/></p>
                                        <p class="up_p01"><input type="text" id="licensePhoto" name="licensePhoto"
                                                                 class="text" maxlength="200"
                                                                 value="${tenant.licensePhoto}"
                                                                 title="${message("tenant.licensePhoto")}"
                                                                 readonly="true"/>
                                            <input type="button" id="browserButton" class="authen_upload_btn"
                                                   value="上传图片"/>
                                        <div>上传图片请控制在2MB以内，支持格式为bmp\jpg\png</div>
                                        </p>
                                        <img id="browserImage" class="upload_img01"
                                             src="[#if tenant.licensePhoto??]${tenant.licensePhoto}[/#if]">
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <th align="right"><span>*</span>法人代表：</th>
                                <td><input type="text" name="legalRepr" class="text" value="${tenant.legalRepr}"/></td>
                            </tr>
                            <tr>
                                <th align="right"><span>*</span>经营地址：</th>
                                <td><span class="fieldSet">
                          <input type="hidden" id="areaId" name="areaId" value="${(tenant.area.id)!}"
                                 treePath="${(tenant.area.treePath)!}"/>
                        </span>
                                </td>
                            </tr>
                            <tr>
                                <th align="right">街道：</th>
                                <td><input type="text" name="address" class="text" value="${(tenant.address)!}"/>
                                </td>
                            </tr>
                            <tr>
                                <th align="right">&nbsp;</th>
                                <td><input class="button" type="submit" value="确定"/>
                                    <input class="button" type="button" value="取消" onclick="authenLayer1();"/>
                                </td>
                            </tr>
                        </table>
                    </form>
                </div>
                <!--认证窗口-分割线-->
                <div id="certifiedStores" class="authen_busi_div" class="authen_busi_div">
                    <h3 class="busi_tit">门店实名认证</h3>
                    <p class="busi_txt">请上传有清晰门店招牌的照片，等待平台验证通过</p>
                    <form id="storesAutForm" action="${base}/helper/member/authen/certifiedStores.jhtml" method="post">
                        <input type="hidden" name="id" value="${(tenant.id)!}">
                        <input type="hidden" name="tagId" value="27">
                        <table class="authen_table01">
                            <tr>
                                <th align="right" valign="top" width="140"><span>*</span>门店招牌：</th>
                                <td>
                                    <p class="up_p01"><input type="text" id="storesPhoto" name="storesPhoto"
                                                             class="text" maxlength="200" value="${storesPhoto}"
                                                             title="${message("storesPhoto")}" readonly="true"/>
                                        <input type="button" id="browserStoresButton" class="authen_upload_btn"
                                               value="上传图片"/>
                                        上传图片请控制在2MB以内，支持格式为bmp\jpg\png
                                    </p>
                                    <img id="browserStoresImage" class="upload_img01"
                                         src="[#if storesPhoto??]${storesPhoto}[/#if]">
                                </td>
                            </tr>
                            <tr>
                                <th align="right">&nbsp;</th>
                                <td><input class="button" type="submit" value="确定"/><input class="button" type="button"
                                                                                           value="取消"
                                                                                           onclick="authenLayer1();"/>
                                </td>
                            </tr>
                        </table>
                    </form>
                </div>
                <!--认证窗口-分割线-->
                <div id="manufacturersCertification" class="authen_busi_div" class="authen_busi_div">
                    <h3 class="busi_tit">厂家授权认证</h3>
                    <p class="busi_txt">请上传您与厂家签订的授权或代理的合同/协议，或厂家颁发给您的合法有效的授权证书，并等待后台的审核通过。</p>
                    <form id="manufacturersAutForm"
                          action="${base}/helper/member/authen/manufacturersCertification.jhtml" method="post">
                        <input type="hidden" name="id" value="${(tenant.id)!}">
                        <input type="hidden" name="tagId" value="28">
                        <table class="authen_table01">
                            <tr>
                                <th align="right" width="140">厂家授权：</th>
                                <td>
                                    <input name="authorization" type="text" class="text"
                                           value="${tenant.authorization}"/>
                                </td>
                            </tr>
                            <tr>
                                <th align="right" valign="top">&nbsp;</th>
                                <td>
                                    <p class="up_p01"><input type="text" id="manufacturersPhoto"
                                                             name="manufacturersPhoto" class="text"
                                                             value="${manufacturersPhoto}" maxlength="200"
                                                             title="${message("manufacturersPhoto")}" readonly="true"/>
                                        <input type="button" id="browserManufacturersButton" class="authen_upload_btn"
                                               value="上传图片"/>上传图片请控制在2MB以内，支持格式为bmp\jpg\png</p>
                                    <img id="browserManufacturersImage" class="upload_img01"
                                         src="[#if manufacturersPhoto??]${manufacturersPhoto}[/#if]">
                                </td>
                            </tr>
                            <tr>
                                <th align="right">&nbsp;</th>
                                <td><input class="button" type="submit" value="确认授权"/><input onclick="authenLayer1();"
                                                                                             class="button"
                                                                                             type="button"
                                                                                             value="取消授权"/></td>
                            </tr>
                        </table>
                    </form>
                </div>
                <!-- 认证弹出窗 end -->
            </div>
        </div>
    </div>
</div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
