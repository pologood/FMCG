[#-- 全局导航--]
<nav class="nav G bg-12" data-emptyto="G-nav">
    <ul>
        <li>
            <a class="it ft-bs0" href="${base}/wap/index.jhtml" title="">
                <span>
                    [#if type=='index']
                    <i class="iconfont ft-bs3 clr-red05">&#xe646;</i>
                    [#else]
                    <i class="iconfont ft-bs3 clr-grey02">&#xe621;</i>
                    [/#if]
                </span>
                <span class="[#if type=='index']clr-red05[#else]clr-grey02[/#if]">首页</span>
            </a>
        </li>
        <li>
            <a class="it ft-bs0" href="${base}/wap/productCategory/index.jhtml" title="">
                <span>
                    [#if type=='category']
                    <i class="iconfont ft-bs3 clr-red05">&#xe63e;</i>
                    [#else]
                    <i class="iconfont ft-bs3 clr-grey02">&#xe61d;</i>
                    [/#if]
                </span>
                <span class="[#if type=='category']clr-red05[#else]clr-grey02[/#if]">分类</span>
            </a>
        </li>
        <li>
            <a class="it ft-bs0" href="${base}/wap/tenant/list.jhtml" title="">
                <span>
                    [#if type=='nearby']
                    <i class="iconfont ft-bs3 clr-red05">&#xe649;</i>
                    [#else]
                    <i class="iconfont ft-bs3 clr-grey02">&#xe623;</i>
                    [/#if]
                </span>
                <span class="[#if type=='nearby']clr-red05[#else]clr-grey02[/#if]">附近</span>
            </a>
        </li>
        <li>
            <a class="it ft-bs0" href="${base}/wap/cart/list.jhtml" title="">
                <span>
                    [#if type=='shopcar']
                    <i class="iconfont ft-bs3 clr-red05">&#xe651;</i>
                    [#else]
                    <i class="iconfont ft-bs3 clr-grey02">&#xe628;</i>
                    [/#if]
                </span>
                <span class="[#if type=='shopcar']clr-red05[#else]clr-grey02[/#if]">购物车</span>
            </a>
        </li>
        <li>
            <a class="it ft-bs0" href="${base}/wap/member/index.jhtml" title="">
                <span>
                    [#if type=='mine']
                    <i class="iconfont ft-bs3 clr-red05">&#xe64b;</i>
                    [#else]
                    <i class="iconfont ft-bs3 clr-grey02">&#xe624;</i>
                    [/#if]
                </span>
                <span class="[#if type=='mine']clr-red05[#else]clr-grey02[/#if]">我的</span>
            </a>
        </li>
    </ul>
</nav>