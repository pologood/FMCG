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
    <script src="${base}/resources/common/AmazeUI-2.4.0/assets/js/handlebars.min.js"></script>
    <script src="${base}/resources/wap/js/common.js"></script>
    <script src="${base}/resources/wap/js/iscrollEvents.js"></script>
    <script src="${base}/resources/wap/js/fastclick.js"></script>
    <title>${expertCategory.name}</title>
    <script type="text/x-handlebars-template" id="wap-list-item">
        {{#each this}}
        <li class="am-g am-list-item-desced" style="padding-top: 0.5rem">
                    <div class="am-list-item-text">
                        <img src="{{isNotNull member.headImg}}" class="am-comment-avatar" style="float: none;" width="48" height="48">
                        <span>{{#if member}}{{member.name}}{{else}}游客{{/if}}</span>
                        <strong>发表于</strong>
                        <time >{{formatDate createDate}}
                        </time>
                    </div>
                   <a class="am-link-muted"  href="${base}/mobile/expert_consultation/reply.jhtml?id={{id}}"> <span>{{content}}</span></a>
                    <div class="am-btn-group am-btn-group-justify am-margin-top-sm">
                        <a class="am-btn am-btn-default" style="background-color: #fff;" href="javascript:;"><i class="am-icon-paste"></i><span>回复({{replyExpertConsultations.length}})</span></a>
                        <a class="am-btn am-btn-default"  href="${base}/mobile/expert_consultation/reply.jhtml?id={{id}}"><i class="am-icon-hand-o-up"></i><span> 帮他解答</span></a>
                    </div>
                </li>
        {{/each}}
    </script>
    <script>
    	
        $(function () {
            var app = new EventsList($("#wrapper"), $('#wap-list-item'), {
                params: {
                	api:"${base}/mobile/expert_consultation/list.jhtml",
                    pageNumber: ${pageable.pageNumber},
                    pageSize: ${pageable.pageSize},
                    expertCategoryId:${expertCategory.id}
                }
            });
            app.init();

        });
    </script>
</head>
<body>
<div class="page">
    <header data-am-widget="header" class="am-header am-header-default">
        <div class="am-header-left am-header-nav"><a href="${base}/mobile/index.jhtml" class=""> <span
                class="am-header-nav-title">返回</span> </a></div>
        <h1 class="am-header-title">${expertCategory.name}</h1>

        <div class="am-header-right am-header-nav">
            <a href="${base}/mobile/expert_consultation/add.jhtml?expertCategoryId=${expertCategory.id}">
                <span class="am-header-nav-title">提问</span>
            </a>
        </div>
    </header>
    <div id="wrapper" data-am-widget="list_news" class="am-list-news am-list-news-default">
        <div class="am-list-news-bd">
        	<div class="pull-action loading" id="pull-down">
                	<span class="am-icon-arrow-down pull-label" id="pull-down-label">下拉刷新</span>
                	<span class="am-icon-spinner am-icon-spin"></span>
            	</div>
            <ul class="am-list" id="events-list">
            	<li class="am-g am-list-item-desced">
                        <div class="am-list-item-text">
                        正在加载内容...
                    </div>
                    </li>
            </ul>
            <div class="pull-action" id="pull-up">
                	<span class="am-icon-arrow-down pull-label" id="pull-up-label"> 上拉加载更多</span>
                	<span class="am-icon-spinner am-icon-spin"></span>
           		</div>
        </div>
    </div>
</div>
</body>
</html>