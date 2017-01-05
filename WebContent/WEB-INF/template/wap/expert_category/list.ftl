<!doctype html>
<html>
<head>
   [#include "/wap/include/resource.ftl"]
    <script src="${base}/resources/common/AmazeUI-2.3.0/assets/js/handlebars.min.js"></script>
     <script src="${base}/resources/wap/js/iscrollEvents.js"></script><script src="${base}/resources/wap/js/masonry.pkgd.min.js"></script>
    <title>专家小组</title>
    <style>
#wrapper{[#if browse_version=="MicroMessenger"]bottom:45px;[/#if]}
</style>
    <script type="text/x-handlebars-template" id="wap-list-item">
        {{#each this}}
        <li class="msry-item am-g am-list-item-desced am-list-item-thumbed am-list-item-thumb-left">
                        <div class="am-u-sm-4 am-list-thumb">
                            <a href="${base}/wap/expert_consultation/list/{{id}}.jhtml" class="">
                                <img class="lazy" data-original="{{image}}" src="${base}/resources/wap/image/NoPicture.jpg" alt="" />
                            </a>
                        </div>
                        <div class="am-u-sm-8 am-list-main">
                            <h3 class="am-list-item-hd am-text-truncate">
                                <a href="${base}/wap/expert_consultation/list/{{id}}.jhtml" class="">{{name}}</a>
                            </h3>
                            
                            <div class="am-list-item-text">
							                 	{{memo}}                  
                            </div>
                            <small class="am-fr am-text-bottom ">
                                <a class="am-btn-sm" style="text-decoration: underline;cursor: pointer;" href="${base}/wap/expert/list.jhtml?expertCategoryId={{id}}">找专家</a>
                                <a class="am-btn-secondary am-margin-left am-btn-sm" href="${base}/wap/expert_consultation/add.jhtml?expertCategoryId={{id}}">提问</a>
                            </small>
                        </div>
                    </li>
        
        {{/each}}
    </script>
    <script>
        $(function () {
            var app = new EventsList($("#wrapper"), $('#wap-list-item'), {
                params: {
                	api:"${base}/wap/expert_category/list.jhtml",
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
	  <header class="am-topbar am-topbar-fixed-top">
      <header data-am-widget="header" class="am-header am-header-default">
        <div class="am-header-left am-header-nav"><a href="javascript:;" class=""> <span
                class="am-header-nav-title wap-rebk">返回</span> </a></div>
        <h1 class="am-header-title">专家小组</h1>
      </header>
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
    	[#if browse_version=="MicroMessenger"]
    		<div style="height:49px"></div>
    	[/#if]
</div>
[#if browse_version=="MicroMessenger"]
[#include "/wap/include/footer.ftl" /]	
[/#if]	
</body>
</html>