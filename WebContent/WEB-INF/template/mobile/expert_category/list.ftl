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
    <title>专家小组</title>
    <script type="text/x-handlebars-template" id="wap-list-item">
        {{#each this}}
        <li class="am-g am-list-item-desced am-list-item-thumbed am-list-item-thumb-left">
                        <div class="am-u-sm-4 am-list-thumb">
                            <a href="${base}/mobile/expert_consultation/list/{{id}}.jhtml" class="">
                                <img class="lazy" data-original="{{image}}" alt="" />
                            </a>
                        </div>
                        <div class="am-u-sm-8 am-list-main">
                            <h3 class="am-list-item-hd am-text-truncate">
                                <a href="${base}/mobile/expert_consultation/list/{{id}}.jhtml" class="">{{name}}</a>
                            </h3>
                            
                            <div class="am-list-item-text">
							                 	{{memo}}                  
                            </div>
                            <small class="am-fr am-text-bottom ">
                                <a class="am-btn-sm" style="text-decoration: underline;cursor: pointer;" href="${base}/mobile/expert/list.jhtml?expertCategoryId={{id}}">找专家</a>
                                <a class="am-btn-secondary am-margin-left am-btn-sm" href="${base}/mobile/expert_consultation/add.jhtml?expertCategoryId={{id}}">提问</a>
                            </small>
                        </div>
                    </li>
        
        {{/each}}
    </script>
    <script>
        $(function () {
            var app = new EventsList($("#wrapper"), $('#wap-list-item'), {
                params: {
                	api:"${base}/mobile/expert_category/list.jhtml",
                    pageNumber: ${pageable.pageNumber},
                    pageSize: ${pageable.pageSize}
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
        <h1 class="am-header-title">专家小组</h1>
    </header>
        <div id="wrapper" data-am-widget="list_news" class="am-list-news am-list-news-default">
            <div  class="am-list-news-bd">
            	<div class="pull-action loading" id="pull-down">
                	<span class="am-icon-arrow-down pull-label" id="pull-down-label">下拉刷新</span>
                	<span class="am-icon-spinner am-icon-spin"></span>
            	</div>
                <ul class="am-list" id="events-list" >
  					<li class="am-g am-list-item-desced am-list-item-thumbed am-list-item-thumb-left">
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
</body>
</html>