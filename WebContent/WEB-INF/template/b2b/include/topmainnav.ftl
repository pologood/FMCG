<div class="mainNav">
    <div class="container ">
        <div class="left allCategorys">
            <div class="title positionR">
                <i></i>
                <a href="javascript:;">所有商品分类</a>
            </div>
            <div class="[#if versionType==0]menuNavTiaohuo[#elseif versionType==1]menuNav[/#if] display">
            [#include "/b2b/include/category.ftl"]
            </div>
        </div>
        <div class="content navItems">
            <ul>
            [@navigation_list position = "middle"]
                [#if navigations?size>0]
                [#list navigations as navigation]
                    [#if navigation.type=="b2b"]
                        <li>
                            <a href="${navigation.url}">${navigation.name}</a>
                        </li>
                    [/#if]
                [/#list]
                [/#if]
            [/@navigation_list]
            </ul>
        </div>

        [#--<div class="my-links bgfff">--]
            [#--<h3>友情链接</h3>--]
            [#--<p class="clear"></p>--]
            [#--<ul>--]
                [#--<li><a target="blank" href="http://mobile.51cto.com/android/">Android频道</a></li>--]
                [#--<li><a target="blank" href="http://www.enet.com.cn/eschool/">eNet学院</a></li>--]
                [#--<li><a target="blank" href="http://www.51aspx.com/">51Aspx.com</a></li>--]
                [#--<li><a target="blank" href="http://www.cnw.com.cn/software/">网界网软件</a></li>--]
                [#--<li><a target="blank" href="http://www.chinaz.com/news/">站长资讯</a></li>--]
                [#--<li><a target="blank" href="http://www.mscto.com/">软件开发网</a></li>--]
                [#--<li><a target="blank" href="http://www.236z.com/">站长技术</a></li>--]
                [#--<li><a target="blank" href="http://www.aspjzy.com/">ASP集中营</a></li>--]
                [#--<li><a target="blank" href="http://www.downcodes.com/">源码网</a></li>--]
                [#--<li><a target="blank" href="http://www.diybl.com">飞诺网</a></li>--]
                [#--<li><a target="blank" href="http://idc.zol.com.cn/">IDC中国</a></li>--]
                [#--<li><a target="blank" href="http://mobile.51cto.com/">移动开发</a></li>--]
                [#--<li><a target="blank" href="http://os.51cto.com/linux/">Linux专区</a></li>--]
                [#--<li><a target="blank" href="http://www.chnfuture.com.cn/">华夏未来</a></li>--]
                [#--<li><a target="blank" href="http://www.51testing.com/">51软件测试网</a></li>--]
                [#--<li><a target="blank" href="http://www.cstc.org.cn">中国软件评测中心</a></li>--]
                [#--<li><a href="#" target="blank">Windows频道</a></li>--]
                [#--<li><a target="blank" href="http://www.uplooking.com/">嵌入式培训</a></li>--]
                [#--<li><a target="blank" href="http://www.mhtml5.com/">HTML 5研究小组</a></li>--]
                [#--<li><a target="blank" href="http://www.jb51.net/">脚本之家</a></li>--]
                [#--<li><a target="blank" href="http://www.8844.com/">连邦IT服务</a></li>--]
                [#--<li><a target="blank" href="http://www.knowsky.com">动态网站制作</a></li>--]
                [#--<li><a target="blank" href="http://www.html5cn.org/">HTML 5中国</a></li>--]
            [#--</ul>--]
        [#--</div>--]

    </div>
</div>