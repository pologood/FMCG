<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta name="format-detection" content="telephone=no">
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <link rel="stylesheet" type="text/css" href="${base}/resources/common/AmazeUI-2.4.0/assets/css/amazeui.min.css">
    <link rel="stylesheet" type="text/css" href="${base}/resources/wap/css/common.css">
    <link rel="alternate icon" type="image/png" href="${base}/resources/common/AmazeUI-2.4.0/assets/i/favicon.png">
    <script src="${base}/resources/common/AmazeUI-2.4.0/assets/js/jquery.min.js"></script>
    <script src="${base}/resources/common/AmazeUI-2.4.0/assets/js/amazeui.min.js"></script>
    <script src="${base}/resources/common/AmazeUI-2.4.0/assets/js/amazeui.lazyload.min.js"></script>
    <script src="${base}/resources/wap/js/iscroll.js"></script>
    <script src="${base}/resources/wap/js/common.js"></script>
    <script src="${base}/resources/wap/js/fastclick.js"></script>
    <title>资讯</title>
    <style>
    	#wrapper{top:49px;}
    </style>
    <script>
    $(function(){
      wapScroll("wrapper");    
    })
    
    </script>
</head>
<body>
<div class="page">
    <header data-am-widget="header" class="am-header am-header-default">
        <div class="am-header-left am-header-nav"><a href="javascript:window.history.back();" class=""> <span
                class="am-header-nav-title">返回</span> </a></div>
        <div class="am-header-right am-header-nav">
            <a href="">
                <small>${article.consultations.size()}跟帖</small>
            </a>
        </div>
    </header>
    <div id="wrapper" >
    <div>
    <div class="am-panel am-margin-top-sm">
        <div class="am-article">
            <div class="am-article-hd am-margin-left-sm am-margin-right-sm">
                <p class="am-article-title" style="font-size: 2rem;
margin-bottom: 1px;">${article.title}</p>
                <small class="am-article-meta">${article.createDate?string("yyyy-MM-dd hh:MM:ss")}</small>
            </div>
            <div class="am-article-bd am-margin-left-sm am-margin-right-sm">
                <img class="lazy" data-original="${article.image}" class="am-img-responsive">

                <div>
                    ${article.content}
                </div>
            </div>

        </div>
    </div>
    <div class="am-padding-sm" style="border: 1px solid;
    border-color: rgba(192, 192, 192, 0.59);padding-top: 0px;">
        <span class="am-badge">最新回复</span>
        <ul class="am-comments-list">
        [#list consultations as consultation]
            <li class="am-comment ">
                <a href="javascript:;">
                    <img [#if consultation.member??]src="${(consultation.member.headImg)!}" class="am-comment-avatar" [#else] class="am-comment-avatar am-icon-user" [/#if] 
                         alt=""  width="48" height="48"/>
                </a>
                <div class="am-comment-main">
                    <header class="am-comment-hd">
                        <div class="am-comment-meta">
                            <a href="javascript:;" class="am-comment-author">[#if consultation.member??]${consultation.member.name}[#else]游客[/#if]</a>
                            评论于
                            <time >${consultation.createDate?string("yyyy-MM-dd HH:mm:ss")}
                            </time>
                        </div>
                    </header>

                    <div class="am-comment-bd">
                        ${consultation.content}
                    </div>
                    <footer class="am-comment-footer" style="background-color: #DBDBDB;">
                        <div class="am-comment-actions" ><a href="${base}/mobile/article/review/${article.id}.jhtml" style="color: rgb(39, 39, 39);
text-decoration: underline">回复</a></div>
                    </footer>
            </li>
        [/#list]
        </ul>
        <a class="am-btn am-btn-secondary" href="${base}/mobile/article/review/${article.id}.jhtml">评论</a>
    </div>
    </div>
    </div>
</div>
</body>
</html>