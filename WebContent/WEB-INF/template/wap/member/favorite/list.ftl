<!doctype html>
<html lang="en">
<head>
     [#include "/wap/include/resource.ftl"]
    <style>
        
    </style>
    <script>
        $(function () {
            $(".wap-favorite-tab").find("li").on("click",function(){
                $(".wap-favorite-tab").find("li").removeClass("m_down");
                $(this).addClass("m_down");
                var index=$(this).index("li");
                $("#wrapper").find(".wap-tabs-item").addClass("am-hide");
                $("#wrapper").find(".wap-tabs-item").eq(index).removeClass("am-hide");
            });
        })
    </script>
    <title>我的收藏</title>
</head>
<body>
<div class="page">
	  <header class="am-topbar am-topbar-fixed-top">
      <header data-am-widget="header" class="am-header am-header-default">
        <div class="am-header-left am-header-nav"><a href="javascript:;" class=""> <span
                class="am-header-nav-title wap-rebk">返回</span> </a></div>

        <div class="am-header-title wap-favorite" >
            <ul class="am-list wap-favorite-tab" style="">
                <li  class="m_down">收藏商品</li>
                <li class="">关注商家</li>
            </ul>
        </div>
      </header>
    </header>
    <div id="wrapper" data-am-widget="list_news"
         class="am-list-news am-list-news-default">
        <div>
            <div class="am-list-news-bd  wap-tabs-item">
                <ul class="am-list am-gallery am-avg-sm-2 am-gallery-default" data-am-gallery="{}" id="events-list"
                    data-am-widget="gallery">
                   [#if page.content?has_content]
					[#list page.content as product]
                    <li>
                        <div class="am-gallery-item">
                            <a href="${base}/wap/product/content/${product.id}.jhtml"
                               class="am-block am-link-muted">
                                <img src="${product.thumbnail}" />

                                <h3 class="am-gallery-title" style="margin-top:1px;">${product.fullName}</h3>
                                <div><strong class="am-text-danger"><i class=" am-icon-cny"></i>${product.price}</strong>
                                    <del class="am-text-default">
                                        <small><i class="am-icon-cny"></i>${product.wholePrice}</small>
                                    </del>
                                </div>
                            </a>
                        </div>
                    </li>
                    [/#list]
                    [#else]
                    <p>您还没有收藏过任何商品喔!</p>
					[/#if]
                </ul>
            </div>
            <div class="am-list-news-bd am-hide wap-tabs-item">
                <ul class="am-list am-gallery am-avg-sm-1 am-gallery-default" data-am-gallery="{}" id="events-list"
                    data-am-widget="gallery">
                    [#if pageTenant.content?has_content]
                    	[#list pageTenant.content as tenant]
							[#if tenant.defaultDeliveryCenter??]
								<li>
                        <div class="am-gallery-item">
                        <a href="${base}/wap/delivery/${tenant.defaultDeliveryCenter.id}/index.jhtml">
                            <img style="width: 80px;"
                                 src="${tenant.thumbnail}" data-original="${tenant.thumbnail}"
                                 class="am-img-thumbnail am-u-sm-4 am-margin-right-sm"></a>
                            <p style="margin: 1px auto;">${tenant.name}<i class="wap-icon-start-${tenant.score} wap-icon-right"></i>
                            <p style="margin: 1px auto;">
									主营:[#list tenant.productCategoryTenants as productCategoryTenant]
						     			  [#if productCategoryTenant_index<3]
										 	${productCategoryTenant.name}
										 	[#if productCategoryTenant_has_next]/[/#if]
					                  [/#if]
									[/#list]
</p>
                            <small class="am-text-bottom"><i class="am-icon-map-marker"></i>&nbsp;${tenant.address}</small>
                        </div>
                    </li>
							[/#if]
						[/#list]
					[#else]
					 <p>您还没有收藏过任何店铺喔!</p>
					[/#if]
                </ul>
            </div>
        </div>
    </div>
</div>
</body>
</html>