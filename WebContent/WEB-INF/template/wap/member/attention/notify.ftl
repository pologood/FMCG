<!DOCTYPE HTML>
<html lang="en">
<head>
 [#include "/wap/include/resource.ftl"]
<script type="text/javascript">
$().ready(function(){
	if($.isWeiXin()){
		$("#bak_btn").on("click",function(){
			location.href="${base}/wap/index.jhtml";
		});
	}else{
		$("#bak_btn").on("click",function(){
			location.href="vsstoo://appback/?backapp=true";
		});
	}
});
</script>
</head>
<body>
<div class="page">
	  <header class="am-topbar am-topbar-fixed-top">
      <header data-am-widget="header" class="am-header am-header-default">
        <div class="am-header-left am-header-nav"><a href="${base}/wap/index.jhtml" class="">
            <i class="am-icon-home am-icon-md"></i> </a></div>
        <h1 class="am-header-title">温馨提示</h1>
      </header>
    </header>
	
	<div class="am-g">
	<div class="am-text-center am-margin-top-lg "><i style="color: #EC5353" class="am-icon-exclamation-circle am-icon-lg am-icon-btn-sm"></i><p style="font-size: medium;">${notifyMessage}</p></div>
	
	<a href="javascript:;" id="bak_btn" class="am-btn am-btn-default am-center" style="width:100px;">返回</a>
	</div>
</div>	
</body>
</html>
