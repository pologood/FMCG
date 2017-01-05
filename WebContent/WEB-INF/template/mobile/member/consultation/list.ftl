<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8"/>
<meta http-equiv="Cache-Control" content="no-transform " />
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0"  media="(device-height:768px)"/>
<meta name="apple-mobile-web-app-capable" content="yes" />
<title>最新咨询</title>
<link rel="stylesheet" href="${base}/resources/mobile/css/library.css" />
<link rel="stylesheet" href="${base}/resources/mobile/css/iconfont.css" />
<link rel="stylesheet" href="${base}/resources/mobile/css/common.css" />
<link rel="stylesheet" href="${base}/resources/mobile/css/myorder.css" />
<script type="text/javascript" src="${base}/resources/mobile/js/tts.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/js/extend.js"></script>
<script type="text/javascript" src="${base}/resources/mobile/js/common.js"></script>
<script type="text/javascript">
$().ready(function(){
	//返回按钮事件
	 $("#backapp").on("tap",function(){
		location.href="vsstoo://appback/?backapp=true";
	 });
	 var $answer = $("a[name='answer']");
	 $answer.on('tap',function(){
		var $this = $(this);
		var id = $this.attr("forConsultationId");
		location.href="${base}/mobile/member/consultation/reply.jhtml?id="+id;
	});
})
</script>
</head>
<body>
<section class="m_section">
  <header class="m_header">
    <div class="m_headercont_1">
      <div class="m_return" id="backapp"><a href="javascript:;" alt="返回">
        <div class="p_datag" >返回</div>
        </a></div>
      <div class="m_title" alt="选择日期">最新咨询</div>
    </div>
  </header>
  <div class="reference_tab">
    <ul>
      <li class="current_screen">收到的咨询</li>
      <li>发出的咨询</li>
    </ul>
  </div>
  <article class="reference_list" id="r_scrooler0">
    <div class="reference_list_content">
	  	[#if consultations_receive?? && consultations_receive?has_content]
	      <ul id="consultationList0">
		      	[#list consultations_receive as consultation]
		        <li> <a href="#" class="r_user"> <img src="${consultation.member.headImg}"/>
		          <div class="r_user_info">
		            <p class="r_user_name">${consultation.member.username}</p>
		            <p class="r_user_time">${consultation.createDate?string("yyyy-MM-dd HH:mm:ss")}</p>
		          </div>
		          </a>
		          <div class="r_content">
		            ${consultation.content}
		          </div>
		          <div class="m_clear"> </div>
		          <div class="r_goods"> <a href="${base}/mobile/product/content/${consultation.product.id}.jhtml" class="r_goods_c">
		            <div class="order_goods_img"> <img src="${consultation.product.thumbnail}"/> </div>
		            <div class="o_goods_c">
		              <p class="goods_name">${consultation.product.name}</p>
		              <p class="goods_style">[#if consultation.product.specification_value?has_content]${consultation.product.specification_value}[/#if]</p>
		            </div>
		            <div class="order_goods_r">
		              <p class="goods_price">${currency(consultation.product.price,true)}</p>
		            </div>
		            </a> </div>
		          <div class="m_clear"> </div>
		        [#list consultation.replyConsultations as replyConsultation]
		        <li> <a href="#" class="r_user"> <img src="[#if replyConsultation.member??]${replyConsultation.member.headImg}[/#if]"/>
		          <div class="r_user_info">
		            <p class="r_user_name">[#if replyConsultation.member??]${replyConsultation.member.username}[/#if]</p>
		            <p class="r_user_time">${replyConsultation.createDate?string("yyyy-MM-dd HH:mm:ss")}</p>
		          </div>
		          </a>
		          <div class="r_content_h">
		            ${replyConsultation.content}
		          </div>
		          <div class="m_clear"> </div>
		        </li>
		        [/#list]
		          <div class="r_opreate">
		            <div class="r_opreate_btn"> <a class="o_button" href="javascript:void(0);" name="answer" forConsultationId="${consultation.id}">回复</a> </div>
		          </div>
		          <div class="m_clear"> </div>
		        </li>
		       [/#list]
	      </ul>
	      [#else]
	      	 <div class="m_more"><span>你还没有收到咨询</span></div>
	      [/#if] 
    </div>
  </article>
  <article class="reference_list" id="r_scrooler1" style="display:none;">
    <div class="reference_list_content">
	[#if consultations_send??&&consultations_send?has_content]
      <ul id="consultationList1">
      	[#list consultations_send as consultation]
        <li> <a href="#" class="r_user"> <img src="${member.headImg}"/>
          <div class="r_user_info">
            <p class="r_user_name">${member.username}</p>
            <p class="r_user_time">${consultation.createDate?string("yyyy-MM-dd HH:mm:ss")}</p>
          </div>
          </a>
          <div class="r_content">
            ${consultation.content}
          </div>
          <div class="m_clear"> </div>
          <div class="r_goods"> <a href="${base}/mobile/product/content/${consultation.product.id}.jhtml" class="r_goods_c">
            <div class="order_goods_img"> <img src="${consultation.product.thumbnail}"/> </div>
            <div class="o_goods_c">
              <p class="goods_name">${consultation.product.name}</p>
              <p class="goods_style">[#if consultation.product.specification_value?has_content]${consultation.product.specification_value}[/#if]</p>
            </div>
            <div class="order_goods_r">
              <p class="goods_price">${currency(consultation.product.price,true)}</p>
            </div>
            </a> </div>
          <div class="m_clear"> </div>
        </li>
        [#if consultation.replyConsultations??&&consultation.replyConsultations?has_content]
			[#list consultation.replyConsultations as replyConsultation]
        <li> 
        	<a href="#" class="r_user"> 
	        	<img src="[#if replyConsultation.member??]${replyConsultation.member.headImg}[/#if]"/>
		        <div class="r_user_info">
		          <p class="r_user_name">[#if replyConsultation.member??]${replyConsultation.member.username}[/#if]</p>
		          <p class="r_user_time">${replyConsultation.createDate?string("yyyy-MM-dd HH:mm:ss")}</p>
		        </div>
	        </a>
	        <div class="m_clear"> </div>
	        <div class="r_content_h">
	          ${replyConsultation.content}
	        </div>
	        <div class="m_clear"> </div>
	    </li>
	    	[/#list]
	    [/#if]	 
      [/#list]
      </ul>
      [#else]
      		<div class="m_more"><span>你还没有发过咨询</span></div>
      [/#if]
    </div>
  </article>
</section>
</body>
</html>
