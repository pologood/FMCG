<!doctype html>
<html>
<head>
    [#include "/wap/include/resource.ftl"]
    <script src="${base}/resources/common/js/jquery.validate.js"></script>
<script src="${base}/resources/common/js/jquery-form.js"></script>
    <title>商品评价</title>
    <script>
        $(function () {
	setTimeout(function(){
	  init();
  },200);
            $("#reset").on("click",function(){
     			$("textarea[name='content']").val("");
     		});
     		$("#submitForm").validate({
     			rules:{
     				content:"required"
     			},
     			messages:{
     				content:"内容必填！！"
     			},
     			submitHandler:function(){
     				var xs=$(".w_satrlist_1").find("i.w_down").length;
     				var score=$(".w_satrlist_2").find("i.w_down").length;
					var assistant=$(".w_satrlist_3").find("i.w_down").length;
					var type;
					if(xs<=1){
						type="negative";
					}else if(xs<=3){
						type="moderate";
					}else if(xs<=5){
						type="positive";
					}
					$("#type").val(type);
					$("#score").val(score);
					$("#assistant").val(assistant);
     				$("#submitForm").ajaxSubmit(function(message){
     					invokTips(message.type,message.content);
						if(message.type=='success'){
							setTimeout(function(){
								location.href="${base}/wap/member/order/tradeView.jhtml?sn=${trade.sn}";
							},1500);
						}
     				})
     			}
     		});
        });
    </script>
    <style>
    </style>
</head>
<body>
<div class="page">
	  <header class="am-topbar am-topbar-fixed-top">
      <header data-am-widget="header" class="am-header am-header-default">
        <div class="am-header-left am-header-nav"><a href="javascript:;" class=""> <span
                class="am-header-nav-title wap-rebk">返回</span> </a></div>
        <h1 class="am-header-title">我要评价</h1>
      </header>
    </header>
    <div class="content" id="wrapper">
        <div>
            <div class="am-g am-padding-sm">
                <div class="am-cf">
                    <p class="am-align-left">
                        <img width="80"
                          data-original="${trade.tenant.thumbnail}"  src="${base}/resources/wap/image/NoPicture.jpg"
                             class="am-img-thumbnail lazy"/>
                    </p>

                    <p style="margin:1px 2px;">${trade.tenant.name}</p>

                    <p style="margin:1px 2px; line-height: 22px;height: 22px;">订单号:${trade.order.sn}</p>
                    <small style="line-height: 22px; height: 22px;">下单时间:${trade.createDate?string('yyyy-MM-dd hh:mm:SS')}</small>
                </div>
            </div>
            <div class="am-g">
                <ul class="wap-trade-review">
                    <li>
                        <div class="w_satrlist_1"><span>相识度</span>
                            <i class="am-icon-star w_down" name="type"></i>
                            <i class="am-icon-star w_down" name="type"></i>
                            <i class="am-icon-star w_down" name="type"></i>
                            <i class="am-icon-star w_down" name="type"></i>
                            <i class="am-icon-star " name="type"></i>
                        </div>
                    </li>
                    <li>
                        <div class="w_satrlist_2"><span>服务</span>
                            <i class="am-icon-star w_down" name="score"></i>
                            <i class="am-icon-star w_down" name="score"></i>
                            <i class="am-icon-star w_down" name="score"></i>
                            <i class="am-icon-star w_down" name="score"></i>
                            <i class="am-icon-star" name="score"></i>
                        </div>
                    </li>
                    <li>
                        <div class="w_satrlist_3"><span>物流</span>
                            <i class="am-icon-star w_down" name="assistant"></i>
                            <i class="am-icon-star w_down" name="assistant"></i>
                            <i class="am-icon-star w_down" name="assistant"></i>
                            <i class="am-icon-star w_down" name="assistant"></i>
                            <i class="am-icon-star" name="assistant"></i>
                        </div>
                    </li>
                </ul>
            </div>
            <div class="am-g">
                <form id="submitForm" class="am-form" action="${base}/mobile/member/review/save.jhtml" method="post">
                	<input type="hidden"  name="type" id="type">
                	<input type="hidden"  name="score" id="score">
                	<input type="hidden"  name="assistant" id="assistant">
                	<input type="hidden" name="id" value="${trade.id}">
                    <div class="am-form-group">
                        <textarea placeholder="输入你要评价内容。。" name="content" rows="5"></textarea>
                    </div>
                    <button type="submit" class="am-btn am-btn-secondary">提交</button>
                    <button  id="reset" class="am-btn am-btn-default">重置</button>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>