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
    <title>商品评价</title>
     <script src="${base}/resources/common/AmazeUI-2.4.0/assets/js/jquery.min.js"></script>
     <script src="${base}/resources/common/js/jquery.validate.js"></script>
    <script src="${base}/resources/common/AmazeUI-2.4.0/assets/js/amazeui.min.js"></script>
    <script src="${base}/resources/wap/js/fastclick.js"></script>
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
     				content:"评论内容必填！！"
     			}
     		});
     	});
     </script>
</head>
<body>
<div class="page">
    <header data-am-widget="header" class="am-header am-header-default">
        <div class="am-header-left am-header-nav"><a href="${base}/mobile/expert_consultation/list/${expertCategory.id}.jhtml" class=""> <span
                class="am-header-nav-title">返回</span> </a></div>
        <h1 class="am-header-title">提问</h1>
    </header>
    <div class="am-panel ">
        <div class="am-padding-left-sm am-padding-right-sm am-padding-top-sm">
            <form id="submitForm" class="am-form" action="save.jhtml" method="post">
            <input type="hidden" name="expertCategoryId" value="${expertCategory.id}" >
                <div class="am-form-group">
                    <textarea placeholder="你要提问的问题" name="content" class="" rows="5"></textarea>
                </div>
                <button type="submit" class="am-btn am-btn-secondary">提交</button>
                <a href="javascript:;" class="am-btn am-btn-default" id="reset">重置</a>
            </form>
        </div>
    </div>
</div>
</body>
</html>