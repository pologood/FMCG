<div class="am-g header-bg">
    <div class="am-container am-center">
        <div class="am-fl">
            <a href="${base}/helper/member/index.jhtml">
                <img src="${base}/upload/images/PC-login_00.png" alt="店家助手" class="header-login"> 
            </a>
        </div>
        <ul class="am-fr nav-entrance">
        [@navigation_list position = "loginMiddle"]
            [#list navigations as navigation]
                <li>
                    <a href="${navigation.url}"
                       [#if navigation.isBlankTarget]target="_blank"[/#if]>${navigation.name}</a>
                </li>
            [/#list]
        [/@navigation_list]
        </ul>
    </div>
</div>
