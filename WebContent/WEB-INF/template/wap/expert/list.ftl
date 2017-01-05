<!doctype html>
<html>
<head>
[#include "/wap/include/resource.ftl"]
    <script src="${base}/resources/common/AmazeUI-2.4.0/assets/js/handlebars.min.js"></script>
     <script src="${base}/resources/wap/js/iscrollEvents.js"></script>
    <title>资讯</title>
    <style>
#wrapper{[#if browse_version=="MicroMessenger"]bottom:45px;[/#if]}
</style>
    <script type="text/x-handlebars-template" id="wap-list-item">
        {{#each this}}
        <li class="msry-item">
                <div class="am-gallery-item">
                        <img class="lazy" data-original="{{image}}" src="${base}/resources/wap/image/NoPicture.jpg"/>
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
                	api:"${base}/wap/expert/ajax-list.jhtml",
                    pageNumber: ${pageable.pageNumber},
                    pageSize: ${pageable.pageSize},
                    expertCategoryId:${expertCategoryId}
                }
            });
          app.addEvents=function(){
          	[#if browse_version=="MicroMessenger"]
          	$("ul li a").on("click",function(){
				if(confirm("找专家，请下载手机客户端!\n是否为您跳转下载页?")){
					location.href="${base}/www/app-index.html";
					return fasle;
				}	
							
    	})
    	[#else]
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
    	[/#if]
            }  
            app.init();
    })
    </script>
</head>
<body>
<div class="page">
	  <header class="am-topbar am-topbar-fixed-top">
      <header data-am-widget="header" class="am-header am-header-default">
        <div class="am-header-left am-header-nav"><a href="javascript:;" class=""> <span
                class="am-header-nav-title wap-rebk">返回</span> </a></div>
        <h1 class="am-header-title">专家咨询</h1>
      </header>
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