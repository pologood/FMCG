<!--底部start-->
<div class="footer">
    <div class="container">
    [@article_category_root_list count = 10]
        [#list articleCategories as category]
            [#if category.id==59]
                [#list category.children as articleCategory]
                    [#if articleCategory_index == 5]
                        [#break /]
                    [/#if]
                    <dl>
                        <dt>
                        ${articleCategory.name}
                        </dt>
                        <dd>
                            [@article_list articleCategoryId = articleCategory.id count = 6 orderBy="hits desc"]
                                [#list articles as article]
                                    <div><a href="${base}/helper/article/content/${article.id}.jhtml"
                                            title="${article.title}">${abbreviate(article.title, 15)}</a></div>
                                [/#list]
                            [/@article_list]
                        </dd>

                    </dl>
                [/#list]
            [/#if]
        [/#list]
    [/@article_category_root_list]
        <dl>
            <dt>商家入驻</dt>
            <dd>
                <div><a href="${base}/b2c/supplier/login.jhtml" target="_blank">供应商入口</a></div>
                <div><a href="${base}/helper/index.jhtml" target="_blank">服务站入口</a></div>
            </dd>
        </dl>
        <div class="quickMark">
            <div><img src="${base}/upload/images/jdh_qr.jpg" style="width:105px;height:105px;margin-top:5px;"></div>
            <span>微信扫描二维码</span>
            <span>关注${setting.siteName}官方微信</span>
        </div>
    </div>
    <div class="container">
        <div class="copyright">Copyright©&nbsp;2010-2016&nbsp;${setting.siteName}&nbsp;All&nbsp;Rights&nbsp;Reserved&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;皖ICP备15024112&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;技术支持：安徽威思通电子商务有限公司</div>
        <div class="authentication">
            <a rel="nofollow" target="_blank" href="http://www.hd315.gov.cn/beian/view.asp?bianhao=">
                <img alt="经营性网站备案中心" src="${base}/resources/b2c/images/54b8871eNa9a7067e.png" height="32" width="103">
            </a>

            <a rel="nofollow" target="_blank" id="urlknet" tabindex="-1" href="javascript:;">
                <img alt="可信网站" src="${base}/resources/b2c/images/54b8872dNe37a9860.png" border="true" height="32" width="103">
            </a>
            <a rel="nofollow" target="_blank" href="http://www.bj.cyberpolice.cn/index.do">
                <img alt="网络警察" src="${base}/resources/b2c/images/56a89b8fNfbaade9a.jpg" height="32" width="103">
            </a>
            <a rel="nofollow" target="_blank" href="javascript:;">
                <img alt="诚信网站" src="${base}/resources/b2c/images/54b8875fNad1e0c4c.png" height="32" width="103">
            </a>
            <a rel="nofollow" target="_blank" href="http://www.12377.cn">
                <img alt="中国互联网举报中心" src="${base}/resources/b2c/images/5698dc03N23f2e3b8.jpg" height="32" width="103">
            </a>
            <a rel="nofollow" target="_blank" href="http://www.12377.cn/node_548446.htm">
                <img alt="网络举报APP下载" src="${base}/resources/b2c/images/5698dc16Nb2ab99df.jpg" height="32" width="103">
            </a>
        </div>
    </div>
</div>
<!--底部end-->