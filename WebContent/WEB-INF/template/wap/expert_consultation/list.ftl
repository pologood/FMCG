<!doctype html>
<html>
<head>
   [#include "/wap/include/resource.ftl"]
    <script src="${base}/resources/common/AmazeUI-2.3.0/assets/js/handlebars.min.js"></script>
     <script src="${base}/resources/wap/js/iscrollEvents.js"></script>
    <title>${expertCategory.name}</title>
    <style>
#wrapper{[#if browse_version=="MicroMessenger"]bottom:45px;[/#if]}
</style>
    <script type="text/x-handlebars-template" id="wap-list-item">
        {{#each this}}
        <li class="msry-item am-g am-list-item-desced" style="padding-top: 0.5rem">
                    <div class="am-list-item-text">
                        <img src="{{isNotNull member.headImg}}" class="am-comment-avatar" style="float: none;" width="48" height="48">
                        <span>{{#if member}}{{member.name}}{{else}}游客{{/if}}</span>
                        <strong>发表于</strong>
                        <time >{{formatDate createDate}}
                        </time>
                    </div>
                   <a class="am-link-muted"  href="${base}/wap/expert_consultation/reply.jhtml?id={{id}}"> <span>{{content}}</span></a>
                    <div class="am-btn-group am-btn-group-justify am-margin-top-sm">
                        <a class="am-btn am-btn-default" style="background-color: #fff;" href="javascript:;"><i class="am-icon-paste"></i><span>回复({{replyExpertConsultations.length}})</span></a>
                        <a class="am-btn am-btn-default"  href="${base}/wap/expert_consultation/reply.jhtml?id={{id}}"><i class="am-icon-hand-o-up"></i><span> 帮他解答</span></a>
                    </div>
                </li>
        {{/each}}
    </script>
    <script>
    	
        $(function () {
            var app = new EventsList($("#wrapper"), $('#wap-list-item'), {
                params: {
                	api:"${base}/wap/expert_consultation/list.jhtml",
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
	  <header class="am-topbar am-topbar-fixed-top">
      <header data-am-widget="header" class="am-header am-header-default">
        <div class="am-header-left am-header-nav"><a href="javascript:;" class=""> <span
                class="am-header-nav-title wap-rebk">返回</span> </a></div>
        <h1 class="am-header-title">${expertCategory.name}</h1>

        <div class="am-header-right am-header-nav">
            <a href="${base}/wap/expert_consultation/add.jhtml?expertCategoryId=${expertCategory.id}">
                <span class="am-header-nav-title">提问</span>
            </a>
        </div>
      </header>
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
        [#if browse_version=="MicroMessenger"]
    		<div style="height:49px"></div>
   		[/#if]
    </div>
</div>
[#if browse_version=="MicroMessenger"]
[#include "/wap/include/footer.ftl" /]	
[/#if]	
</body>
</html>