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
    <title>资讯</title>
    <script type="text/x-handlebars-template" id="wap-list-item">
        {{#each this}}
        <li>
                <div class="am-gallery-item">
                        <img class="lazy" data-original="{{image}}"/>
                        <h3 class="am-gallery-title  am-text-center">{{member.name}}</h3>
                         <div class="am-gallery-desc am-margin-bottom-xs">
                          {{content}}
                        </div>
                        <a class="am-btn am-btn-secondary am-padding-xs am-center" username="{{member.username}}" nickName="{{member.name}}" headImg="{{member.headImg}}" >咨询</a>
                </div>
            	</li>
        
        {{/each}}
    </script>
    <script>
    $(function(){
    	var app = new EventsList($("#wrapper"), $('#wap-list-item'), {
                params: {
                	api:"${base}/mobile/expert/ajax-list.jhtml",
                    pageNumber: ${pageable.pageNumber},
                    pageSize: ${pageable.pageSize},
                    expertCategoryId:${expertCategoryId}
                }
            });
          app.addEvents=function(){
          	$("ul li a").on("click",function(){
    	var $this =$(this);
    		location.href="chat://open?sender="+encodeURI("${(member.username)!}")
	 	+"&sender-nickName="+encodeURI("${(member.name)!}")
	 	+"&sender-image="+encodeURI("${(member.headImg)!}")
	 	+"&recver="+encodeURI($this.attr("username"))
	 	+"&recver-nickName="+encodeURI($this.attr("nickName"))
	 	+"&recver-image="+encodeURI($this.attr("headImg"))
	 	+"&descr=&descr-image=";
	 	return false;
    	})
            }  
            app.init();
    })
    </script>
</head>
<body>
<div class="page">
    <header data-am-widget="header" class="am-header am-header-default">
        <div class="am-header-left am-header-nav"><a href="javascript:;window.history.back();" class=""> <span
                class="am-header-nav-title">返回</span> </a></div>
        <h1 class="am-header-title">专家咨询</h1>
    </header>
        <div id="wrapper">
        <div>
        <div class="pull-action loading" id="pull-down">
                	<span class="am-icon-arrow-down pull-label" id="pull-down-label">下拉刷新</span>
                	<span class="am-icon-spinner am-icon-spin"></span>
            	</div>
        <ul id="events-list" data-am-widget="gallery" class="am-gallery am-avg-sm-2
  am-avg-md-4 am-avg-lg-5 am-gallery-bordered " data-am-gallery="">
 				 <li>
                        <div class="am-gallery-item">
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