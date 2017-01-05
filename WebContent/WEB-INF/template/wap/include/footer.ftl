<footer class="btcopyright">
 <!--   <a href="${base}/wap/html/techsupport.html"> -->
    <a>
        <div class="techsupport">
        </div>
    </a>
</footer>
<header class="am-topbar am-topbar-fixed-bottom bg-default">
    <div data-am-widget="navbar" class="am-navbar am-cf am-navbar-default am-no-layout"
         style="border-top: 1px solid #ddd;" id="footer_b">
        <ul class="am-navbar-nav am-cf am-avg-sm-5 ">
            <li>
                <a href="${base}/wap/index.jhtml" class="[#if type=='index']color-lired2[#else]color-default[/#if]">
                    <span class="icon iconfont we-icon-bottom font-large-1">[#if type=='index']&#xe646;[#else]
                        &#xe621;[/#if]</span>
                    <span class="am-navbar-label">首页</span>
                </a>
            </li>
            <li>
                <a href="${base}/wap/productCategory/index.jhtml"
                   class="[#if type=='category']color-lired2[#else]color-default[/#if]">
                    <span class="icon iconfont we-icon-bottom font-large-1">[#if type=='category']&#xe63e;[#else]
                        &#xe61d;[/#if]</span>
                    <span class="am-navbar-label">分类</span>
                </a>
            </li>
        [#if versionType==1]
            <li>
                <a href="${base}/wap/social_circles/index.jhtml"
                   class="[#if type=='circles']color-lired2[#else]color-default[/#if]">
                    <span class="icon iconfont we-icon-bottom font-large-1">&#xe62a;</span>
                    <span class="am-navbar-label">社交圈</span>
                </a>
            </li>
        [/#if]
            <li>
                <a href="${base}/wap/tenant/list.jhtml"
                   class="[#if type=='nearby']color-lired2[#else]color-default[/#if]">
                    <span class="icon iconfont we-icon-bottom font-large-1">[#if type=='nearby']&#xe649;[#else]
                        &#xe623;[/#if]</span>
                    <span class="am-navbar-label">附近</span>
                </a>
            </li>
            <li>
                <a href="${base}/wap/cart/list.jhtml"
                   class="[#if type=='shopcar']color-lired2[#else]color-default[/#if]">
                    <span class="icon iconfont we-icon-bottom font-large-1">[#if type=='shopcar']&#xe651;[#else]
                        &#xe628;[/#if]</span>
                    <span class="am-navbar-label">购物车</span>
                </a>
            </li>
            <li>
                <a href="${base}/wap/member/index.jhtml"
                   class="[#if type=='mine']color-lired2[#else]color-default[/#if]">
                    <span class="icon iconfont we-icon-bottom font-large-1">[#if type=='mine']&#xe64b;[#else]
                        &#xe624;[/#if]</span>
                    <span class="am-navbar-label">我的</span>
                </a>
            </li>
        </ul>
    </div>
</header>
<!-- BEGIN empty div for fixed ele -->
<div class="empty-for-fixedbottom_tab"></div>
<!-- END empty div for fixed ele -->