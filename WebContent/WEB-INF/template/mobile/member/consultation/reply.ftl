<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8"/>
<meta http-equiv="Cache-Control" content="no-transform " />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"  media="(device-height:768px)"/>
<meta name="apple-mobile-web-app-capable" content="yes" />
<title>回复咨询</title>
<link rel="stylesheet" href="${base}/resources/mobile/css/library.css" />
<link rel="stylesheet" href="${base}/resources/mobile/css/iconfont.css" />
<link rel="stylesheet" href="${base}/resources/mobile/css/common.css" />
<link rel="stylesheet" href="${base}/resources/mobile/css/myorder.css" />
<script type="text/javascript" src="${base}/resources/mobile/js/tts.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/js/extend.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/js/common.js"></script>
<script type="text/javascript">
$().ready(function(){
	var $submit = $("#submit");
	var $consultationForm = $("#consultationForm");
	var $content = $("#content");
	$submit.on('tap',function(){
		if($content.val().trim()==''){
			mtips("请输入内容");
			return false;
		}
		$consultationForm.submit();
		
	});
	 $("#backapp").on("tap",function(){
		 location.href="vsstoo://appback/?backapp=true";
	 })
	
});
</script>
</head>

<body>
<section class="m_section">
  <header class="m_header">
    <div class="m_headercont_1">
      <div class="m_return" id="backapp"><a href="javascript:;" alt="返回">
        <div class="p_datag" >返回</div>
        </a></div>
      
      <div class="m_title" alt="选择日期">回复咨询</div>
      <!-- <div class="m_member"><a href="member.html" alt="会员中心"><i class="iconfont">&#xe601</i></a></div> --> 
    </div>
  </header>
  <article class="r_reply" >
  <form id="consultationForm" action="${base}/mobile/member/consultation/reply.jhtml" method="post">
  	 <input type="hidden" name="id" value="${consultation.id}">
     <div class="r_reply_content">
        <textarea maxlength="200" name="content" id="content"></textarea>
     </div>
     <div class="m_clear"></div>
      <div class="reply_opreate">
          <div class="r_opreate_btn"> <a class="o_button" href="javascript:void(0);" id="submit">回复</a> </div>
      </div>
   </form>   
   </article>
</section>
</body>
</html>
