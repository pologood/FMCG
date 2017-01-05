<header class="am-topbar am-topbar-fixed-bottom bg-default">
    <div data-am-widget="navbar" class="am-navbar am-cf am-navbar-default am-no-layout"
         style="border-top: 1px solid #ddd;" id="footer_b">
        <ul class="am-navbar-nav am-cf am-avg-sm-5 ">
            <li>
                <a href="${base}/wap/social_circles/index.jhtml" class="[#if type=='index']color-lired2[#else]color-default[/#if]">
                    <span class="icon iconfont we-icon-bottom font-large-1">[#if type=='index']&#xe646;[#else]&#xe621;[/#if]</span>
                    <span class="am-navbar-label">首页</span>
                </a>
            </li>
            <li>
                <a href="${base}/wap/social_circles/message.jhtml" class="[#if type=='message']color-lired2[#else]color-default[/#if]">
                    <span class="icon iconfont we-icon-bottom font-large-1">[#if type=='message']&#xe64d;[#else]&#xe625;[/#if]</span>
                    <span class="am-navbar-label">消息</span>
                </a>
            </li>
            <li>
                <a href="javascript:showDetailDialog($('#dialogSocialCircles'));">
                    <span class="icon iconfont we-icon-bottom color-lired2 font-large-1" style="font-size: 35px;margin-top: 12px;">&#xe648;</span>
                </a>
            </li>
            <li>
                <a href="${base}/wap/social_circles/found.jhtml" class="[#if type=='found']color-lired2[#else]color-default[/#if]">
                    <span class="icon iconfont we-icon-bottom font-large-1">[#if type=='found']&#xe642;[#else]&#xe61f;[/#if]</span>
                    <span class="am-navbar-label">发现</span>
                </a>
            </li>
            <li>
                <a href="${base}/wap/social_circles/mine.jhtml" class="[#if type=='mine']color-lired2[#else]color-default[/#if]">
                    <span class="icon iconfont we-icon-bottom font-large-1">[#if type=='mine']&#xe64b;[#else]&#xe624;[/#if]</span>
                    <span class="am-navbar-label">我的</span>
                </a>
            </li>
        </ul>
    </div>
</header>

<!-- BEGIN empty div for fixed ele -->
<div class="empty-for-fixedbottom_tab"></div>
<!-- END empty div for fixed ele -->