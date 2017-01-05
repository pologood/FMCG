<!doctype html>
<html>
<head>
   [#include "/wap/include/resource.ftl"]
     <script src="${base}/resources/common/js/jquery.validate.js"></script>
    <title>回复</title>
    <style>
#wrapper{[#if browse_version=="MicroMessenger"]bottom:45px;[/#if]}
</style>
    <script >
     	$(function(){
     		$("#reset").on("click",function(){
     			$("textarea[name='content']").val("");
     		});
     		$("#submitForm").validate({
     			rules:{
     				content:"required"
     			},
     			messages:{
     				content:"回复内容必填！！"
     			}
     		});
	setTimeout(function(){
	  init();
  },200);
     	});
     </script>
</head>
<body>
<div class="page">
	  <header class="am-topbar am-topbar-fixed-top">
      <header data-am-widget="header" class="am-header am-header-default">
        <div class="am-header-left am-header-nav"><a href="javascript:;" class=""> <span
                class="am-header-nav-title wap-rebk">返回</span> </a></div>
                <h1 class="am-header-title am-text-truncate">${expertConsultation.content}</h1>
      </header>
    </header>
    <div class="am-panel" id="wrapper">
    	<div>
        <div class="am-padding-left-sm am-padding-right-sm am-padding-bottom-sm">
            <div>
                <img src="${(expertConsultation.member.headImg)!}"
                     class="am-comment-avatar" style="float:none;">
                <span class="am-text-sm am-text-default">[#if expertConsultation.member??]${expertConsultation.member.name}[#else]游客[/#if]</span>
                <time style="float: right;
line-height: 32px;
font-size: 1.4rem;
color: #919191;" >${expertConsultation.createDate?string("yyyy-MM-dd HH:mm:ss")}
                </time>
            </div>
            <div class="am-margin-top-sm" style="background-color: rgb(241, 241, 241);">
                <span>${expertConsultation.content}</span>
            </div>
        </div>
        <hr data-am-widget="divider" style="margin-bottom: 5px;" class="am-divider am-divider-default"
                />
        <div class="am-padding-left-sm am-padding-right-sm">
            <div class="am-padding-bottom-sm">
                <span class="am-fl am-text-sm">回复 </span>
                <span class="am-fr am-text-sm">[#if replys?has_content]共${replys.size()}条评论[/#if]</span>
            </div>
            <div class="am-list-news-bd am-margin-top-sm">
            [#if replys?has_content]
            	[#list replys as reply]
                <ul class="am-list">
                    <li class="am-g am-list-item-desced" style="padding-top: 0.5rem">
                        <div class="am-list-item-text">
                            <img src="${(reply.member.headImg)!}"
                                 alt="" class="am-comment-avatar" style="float: none;" width="48" height="48"/>
                            <span>[#if reply.member??]${reply.member.name}[#else]游客[/#if]</span>
                            <strong>发表于</strong>
                            <time >${reply.createDate?string("yyyy-MM-dd HH:mm:ss")}
                            </time>
                        </div>
                        <span>${reply.content}</span>

                    </li>
            	[/#list]
            [/#if]
                </ul>
            </div>
           <div class="am-margin-top-sm">
                <form id="submitForm" class="am-form" action="reply.jhtml" method="post">
                <input type="hidden" name="expertConsultationId" value=${expertConsultation.id}>
                    <div class="am-form-group">
                        <textarea placeholder="回复内容" name="content" class="" rows="5"></textarea>
                    </div>
                    <button type="submit" class="am-btn am-btn-secondary">回复</button>
                    <a type="submit" class="am-btn am-btn-default" id="reset">重置</a>
                </form>
            </div>
            </div>
        </div>
    </div>
</div>
[#if browse_version=="MicroMessenger"]
[#include "/wap/include/footer.ftl" /]	
[/#if]	
</body>
</html>