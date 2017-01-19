<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("admin.index.build")} - Powered By rsico</title>
<meta name="author" content="rsico Team" />
<meta name="copyright" content="rsico" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.tools.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.validate.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/input.js"></script>
<script type="text/javascript">
$().ready(function() {

	var $inputForm = $("#inputForm");
	var $count = $("#count");
	var $statusTr = $("#statusTr");
	var $status = $("#status");
	var $submit = $(":submit");
	
	var first;
	var buildCount;
	var buildTime;
	var count;
	var successData=[];
	var errorData=[];
	var abnormalData=[];
	
	// 表单验证
	$inputForm.validate({
		rules: {
			count: {
				required: true,
				integer: true,
				min: 1
			}
		},
		submitHandler: function(form) {
			first = 0;
			buildCount = 0;
			buildTime = 0;
			count = parseInt($count.val());
			// $buildType.prop("disabled", true);
			$count.prop("disabled", true);
			$submit.prop("disabled", true);
			$statusTr.show();
            $status.text("${message("admin.index.building")} [" + first + " - " + (first + count) + "]");
            build();
		}
	});
	
	function build() {
		$.ajax({
			url: "${base}/admin/compare/compare.jhtml",
			type: "POST",
			data: {
				first: first,
				count: count
			},
			dataType: "json",
			traditional: true,
			cache: false,
			success: function(data) {
				buildCount += data.buildCount;
				buildTime += data.buildTime;
				$.each(data.successData,function(i,obj){
					successData.push(obj);
				});
				$.each(data.errorData,function(i,obj){
					errorData.push(obj);
				});
				$.each(data.abnormalData,function(i,obj){
					abnormalData.push(obj);
				});
				console.log("error:"+errorData.length+"=====success:"+successData.length+"=====abnormal:"+abnormalData.length);
				
				if (!data.isCompleted) {
					first = data.first;
					$status.text("${message("admin.index.building")} [" + first + " - " + (first + count) + "]");
					build();
				} else {
					var html=
					'<table class="list">'+
		                '<thead>'+
		                  '<tr>'+
		                    '<th>创建日期</th>'+
		                    '<th>订单单号</th>'+
		                    '<th>支付单号</th>'+
		                    '<th>付款金额</th>'+
		                    '<th>会员</th>'+
		                    '<th>店铺</th>'+
		                    '<th>状态</th>'+
		                    '<th>方式</th>'+
		                    '<th>支付方式</th>'+
		                  '</tr>'+
		                '</thead>'+
		              '<tbody>';
		            $.each(abnormalData,function(i,obj){
		                html+=
		                  '<tr>'+
		                    '<td>'+obj.create_date+'</td>'+
		                    '<td>'+obj.order_sn+'</td>'+
		                    '<td>'+obj.payment_sn+'</td>'+
		                    '<td>'+obj.amount+'</td>'+
		                    '<td>'+obj.member_name+'</td>'+
		                    '<td>'+obj.tenant_name+'</td>'+
		                    '<td>'+obj.status+'</td>'+
		                    '<td>'+obj.method+'</td>'+
		                    '<td>'+obj.payment_method+'</td>'+
		                  '</tr>';
		            });
		            html+='</tbody></table>';
		            if(abnormalData.length>0){
		            	$("#error_data").show();
		            }
	                $("#error_data").html(html);
					$count.prop("disabled", false);
					$submit.prop("disabled", false);
					$statusTr.hide();
					$status.empty(); 
					var time;
					if (buildTime < 60000) {
						time = (buildTime / 1000).toFixed(2) + "秒";
					} else {
						time = (buildTime / 60000).toFixed(2) + "分钟";
					}
					$("#compare_time").text(time);//耗时
					$("#compare_count").text(buildCount);//比较总数
					$("#compare_sus_count").text(successData.length);//处理成功数
					$("#compare_err_count").text(errorData.length);//处理失败数
					$("#compare_abnam_count").text(abnormalData.length);//处理失败数
					$.message("success", "对账完成,后台正在统计数据，请稍候");
					$.ajax({
						url: "${base}/admin/statistics/create_date.jhtml",
						type: "get",
						dataType: "json",
						success: function(message) {
							$.message(message);
							if(message.type=="success"){
								$("#begin_compare").prop("disabled", true);
								$("#against_compare").prop("disabled", false);
							}
							// window.location.reload();
							
						}
					});
					
				}
			}
		});
	}

});
function aginst_capital(){
	$.ajax({
		url: "${base}/admin/statistics/delete_today_capital.jhtml",
		type: "get",
		dataType: "json",
		success: function(message) {
			$.message(message);
			window.location.reload();
		}
	});
}
</script>
</head>
<body>
	<div class="path">
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 对账单
	</div>
	<form id="inputForm" action="compare.jhtml" method="post">
		<table class="input">
			<tr>
				<th>
					<span class="requiredField">*</span>每次对账数目:
				</th>
				<td>
					<input type="text" id="count" name="count" class="text" value="50" maxlength="9" />
				</td>
			</tr>
			<tr id="statusTr" class="hidden">
				<th>
					&nbsp;
				</th>
				<td>
					<span class="loadingBar">&nbsp;</span>
					<div id="status"></div>
				</td>
			</tr>
			<tr>
				<th>耗时:</th>
				<td id="compare_time"></td>
			</tr>
			<tr>
				<th>对账总数量:</th>
				<td id="compare_count"></td>
			</tr>
			<tr>
				<th>对账成功数量:</th>
				<td id="compare_sus_count"></td>
			</tr>
			<tr>
				<th>对账失败数量:</th>
				<td id="compare_err_count"></td>
			</tr>
			<tr>
				<th>处理异常数量:</th>
				<td id="compare_abnam_count"></td>
			</tr>
			<tr>
				<th>
					操作:
				</th>
				<td>
					
					<input type="submit" class="button" value="开始对账" [#if is_capital=="true"]disabled="true"[/#if] id="begin_compare"/>
					
					<input type="button" class="button" value="反结账" [#if is_capital=='true']onclick="aginst_capital()"[#else]disabled="true"[/#if] id="against_compare"/>
					<input type="button" class="button" value="${message("admin.common.back")}" onclick="location.href='../common/index.jhtml'" />
					<input type="button" class="button" value="刷新" onclick="location.reload();" id="refresh"/>
					<input type="button" class="button red" value="查看平台资金流水" onclick="location.href='${base}/admin/statistics/platform_capital_total.jhtml'"/>
					<input type="button" class="button red" value="查看会员资金流水" onclick="location.href='${base}/admin/statistics/member_capital_total.jhtml'" />
				</td>
			</tr>
		</table>
	</form>
	<div id="error_data" style="display:none;"></div>
</body>
</html>