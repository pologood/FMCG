<!doctype html>
<html>
<head>
    [#include "/wap/include/resource.ftl"]
    <title>商品评价</title>
     <script src="${base}/resources/common/js/jquery.validate.js"></script>
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
     				content:"评论内容必填！！"
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
        <h1 class="am-header-title">提问</h1>
      </header>
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
[#if browse_version=="MicroMessenger"]
[#include "/wap/include/footer.ftl" /]	
[/#if]	
</body>
</html>