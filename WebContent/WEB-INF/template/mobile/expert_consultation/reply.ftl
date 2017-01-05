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
     <script src="${base}/resources/common/js/jquery.validate.js"></script>
    <script src="${base}/resources/common/AmazeUI-2.4.0/assets/js/amazeui.min.js"></script>
    <script src="${base}/resources/common/AmazeUI-2.4.0/assets/js/amazeui.lazyload.min.js"></script>
    <script src="${base}/resources/wap/js/common.js"></script>
    <script src="${base}/resources/wap/js/fastclick.js"></script>
    <title>回复</title>
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
        wapScroll("wrapper");    
     	});
     </script>
</head>
<body>
<div class="page">
    <header data-am-widget="header" class="am-header am-header-default">
        <div class="am-header-left am-header-nav"><a href="${base}/mobile/expert_consultation/list/${expertConsultation.expertCategory.id}.jhtml" class=""> <span
                class="am-header-nav-title">返回</span> </a></div>
                <h1 class="am-header-title am-text-truncate">${expertConsultation.content}</h1>
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
</body>
</html>