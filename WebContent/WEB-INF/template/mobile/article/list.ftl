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
    <title>${productChannel.name}</title>
    <style>
    	#wrapper{top:95px;}
    </style>
     <script type="text/x-handlebars-template" id="wap-list-item">
        {{#each this}}
        <li class="am-g am-list-item-desced {{#if image}}am-list-item-thumbed am-list-item-thumb-left{{/if}}">
                                 {{#if image}}
                                    <div class="am-u-sm-4 am-list-thumb">
                                        <a href="${base}/mobile/article/detail/{{id}}.jhtml?productChannelId=${productChannel.id}" class="">
                                            <img class="lazy" data-original="{{image}}" alt="{{seoDescription}}">
                                        </a>
                                    </div>
                                 {{/if}}   
                                    <div class="{{#if image}} am-u-sm-8{{/if}} am-list-main">
                                        <h3 class="am-list-item-hd am-text-truncate">
                                            <a href="${base}/mobile/article/detail/{{id}}.jhtml?productChannelId=${productChannel.id}" class="">{{title}}</a>
                                        </h3>
                                        <div class="am-list-item-text">
                                          {{text}}
                                        </div>
                                        <small class="am-fr am-text-bottom wap-text-block">{{formatDate createDate}}</small>
                                    </div>
                                </li>
        
        {{/each}}
    </script>
    <script>
     $(function(){
    	var app = new EventsList($("#wrapper"), $('#wap-list-item'), {
                params: {
                	api:"${base}/mobile/article/ajax-list.jhtml",
                    pageNumber: ${pageable.pageNumber},
                    pageSize: ${pageable.pageSize},
                    productChannelId:${productChannel.id},
                    articleCategoryId:${articleCategory.id}
                }
            });
            app.init();
    })
    </script>
</head>
<body>
<div class="page">
    <header data-am-widget="header" class="am-header am-header-default">
        <div class="am-header-left am-header-nav"><a href="javascript:window.history.back();" class=""> <span
                class="am-header-nav-title">返回</span> </a></div>
        <h1 class="am-header-title">${productChannel.name}</h1>
    </header>
    
    	<div data-am-widget="titlebar" class="am-titlebar am-titlebar-cols" style="margin-top:0px;">
  <nav class="am-titlebar-nav">
  	[#list articleCategories as articleCategoryTemp]
        <a [#if articleCategoryTemp==articleCategory]style="color: #3c3c3c; border-bottom: 2px solid rgba(155, 147, 147, 0); border-bottom-color: #0e90d2;" [/#if] href="${base}/mobile/article/list/${productChannel.id}.jhtml?articleCategoryId=${articleCategoryTemp.id}">${articleCategoryTemp.name}</a>
    [/#list]
  </nav>
</div>
	<div id="wrapper">
    	<div>
    				<div data-am-widget="list_news" class="am-list-news am-list-news-default">
                        <div class="am-list-news-bd">
                        	<div class="pull-action loading" id="pull-down">
                	<span class="am-icon-arrow-down pull-label" id="pull-down-label">下拉刷新</span>
                	<span class="am-icon-spinner am-icon-spin"></span>
            	</div>
            	<div class="am-slider am-slider-default" data-am-flexslider>
                        <ul class="am-slides am-gallery-overlay">
                        	[@article_list productChannelId=productChannel.id articleCategoryId=(articleCategory.id)! tagIds=4  count=5]
                        		[#list articles as article]
                        		[#if article.image??]
                            <li class="am-gallery-item" style="padding: 0px;">
                            	 <a href="${base}/mobile/article/detail/${article.id}.jhtml">
                                <img src="${article.image}" 
                                     alt="${article.title}"/>
                           		</a>
                            </li>
                            [/#if]
                        		[/#list]
                        	[/@article_list]
                        </ul>
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
    </div>
</div>
</div>
</body>
</html>