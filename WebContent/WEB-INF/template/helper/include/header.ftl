<div class="header">
    <div class="bg">
        <div class="container">
            <div class="topNav clearfix">
                <span>欢迎使用${setting.siteName}实体店智能平台</span>
                <div style="float:left;margin-left:200px;line-height:30px;">
                </div>
                <ul>
                [@navigation_list position = "top"]
                    [#list navigations as navigation]
                        <li>
                            <a href="${navigation.url}"[#if navigation.isBlankTarget]
                               target="_blank"[/#if]>${navigation.name}</a>
                            [#if navigation_has_next]|[/#if]
                        </li>
                    [/#list]
                [/@navigation_list]
                    <li>
                        <a href="${base}/helper/member/safe/index.jhtml" id="chagePassword">[个人中心]</a>
                    </li>
                    [#--<li>--]
                        [#--<a href="${base}/helper/member/safe/password.jhtml" id="chagePassword">[修改密码]</a>--]
                    [#--</li>--]
                    <li>
                        <a href="${base}/helper/member/logout.jhtml">[${message("shop.header.logout")}]</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</div>
