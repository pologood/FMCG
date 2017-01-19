<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>奖励补贴列表 - Powered By rsico</title>
<meta name="author" content="rsico Team" />
<meta name="copyright" content="rsico" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<style type="text/css">
	#listForm th,#listForm td{
		text-align: center;
	}
</style>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>
<script type="text/javascript" src="${base}/resources/common/js/jquery.validate.js"></script>
<script type="text/javascript">
$().ready(function() {
	[@flash_message /]
	$("#subsidy_import").click(function(){
		$.dialog({
            title: "奖励补贴",
        	[@compress single_line = true]
            content:'<form id="form_data" enctype="multipart/form-data" action="confirm_subsidy.jhtml" method="post"><table class="input">'+
            			'<tr>'+
            				'<th>上传文件:</th>'+
            				'<td><input type="file" name="file" multiple="multiple" id="import_file"/></td>'+
            			'</tr>'+
            			'<tr>'+
            				'<th>类型:</th>'+
            				'<td>'+
            					'<select style="width:100px;" name="type" id="import_type" onchange="judge_type(this)">'+
            						'<option value="recharge">充值</option>'+
            						'<option value="profit">分润</option>'+
            						'<option value="receipts">货款</option>'+
            						'<option value="other">其他</option>'+
            					'</select>'+
            				'</td>'+
            			'</tr>'+
            			'<tr>'+
            				'<th>模版信息:</th>'+
            				'<td><textarea style="width:260px;height:60px;" name="message" id="import_message" '+
            				'placeholder="例：亲爱的用户name你好，平台已给您充值money元，请注意查收！"></textarea></td>'+
            			'</tr>'+
            			'<tr>'+
            				'<th>备注:</th>'+
            				'<td><textarea style="width:260px;height:60px;" name="remark" id="import_remark" placeholder="例：平台充值"></textarea></td>'+
            			'</tr>'+
            		'</table></form>',
        	[/@compress]
            width: 500,
            modal: true,
            ok: "确认",
            cancel: "取消",
            onOk: function () {
            	var formData = new FormData($('#form_data')[0]);
            	if($('#import_file').val()==''){
            		$.message("warn","请选择文件");	
            		return;
            	}
            	if($('#import_type').val()==''){
            		$.message("warn","请选择类型");	
            		return;
            	}
            	if($('#import_message').val()==''){
            		$.message("warn","请输入模版信息");	
            		return;
            	}
            	if($('#import_remark').val()==''){
            		$.message("warn","请输入备注");	
            		return;
            	}
                $.ajax({
                    url: "confirm_subsidy.jhtml",
                    type: "POST",
                    data: formData,
                    cache: false,
                    processData: false,
    				contentType: false,
                    success: function (message) {
                    	$.message(message.message);
                       	location.reload();
                    }
                });
            }
        });
	});
	
});
function confirm_recharge(id){
	$.ajax({
        url: "confirm_recharge.jhtml",
        type: "POST",
        data: {id:id},
        cache: false,
        dataType:"json",
        success: function (message) {
        	if(message.data.successCount==0&&message.data.errorCount==0){
        		$.message("warn","没有可充值的数据");
        		return;
        	}
        	alert(message.message.content+"! "+" ||成功数量："+message.data.successCount+"||失败数量："+message.data.errorCount);
        }
    });
}
function judge_type(obj){
	if($(obj).val()=="recharge"){
		$("#import_message").attr("placeholder","例：亲爱的用户name你好，平台已给您充值money元，请注意查收！");
		$("#import_remark").attr("placeholder","例：平台充值");
	}else if($(obj).val()=="profit"){
		$("#import_message").attr("placeholder","例：亲爱的用户name，您已收到平台分润money元，请注意查收！");
		$("#import_remark").attr("placeholder","例：平台分润");
	}else if($(obj).val()=="receipts"){
		$("#import_message").attr("placeholder","例：亲爱的用户name，您已收到平台货款money元，请注意查收！");
		$("#import_remark").attr("placeholder","例：平台货款");
	}else{
		$("#import_message").attr("placeholder","例：亲爱的用户name你好，平台已给您返回money元，请注意查收！");
		$("#import_remark").attr("placeholder","例：其他收入");
	}
	
}
</script>
</head>
<body>
	<div class="path">
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 奖励补贴 <span>(${message("admin.page.total", page.total)})</span>
	</div>
	<form id="listForm" action="subsidy_list.jhtml" method="get">
		<div class="bar">
	        <div class="buttonWrap">
	            <a href="javascript:;" id="subsidy_import" class="button">导入</a>
	            <!-- <a href="javascript:;" id="export_ss" class="button">导出</a> -->
	            <a href="javascript:;" id="refreshButton" class="iconButton">
	                <span class="refreshIcon">&nbsp;</span>刷新
	            </a>
	            <div class="menuWrap">
	                <a href="javascript:;" id="pageSizeSelect" class="button">
	                ${message("admin.page.pageSize")}<span class="arrow">&nbsp;</span>
	                </a>
	                <div class="popupMenu">
	                    <ul id="pageSizeOption">
	                        <li>
	                            <a href="javascript:;"[#if page.pageSize == 10] class="current"[/#if] val="10">10</a>
	                        </li>
	                        <li>
	                            <a href="javascript:;"[#if page.pageSize == 20] class="current"[/#if] val="20">20</a>
	                        </li>
	                        <li>
	                            <a href="javascript:;"[#if page.pageSize == 50] class="current"[/#if] val="50">50</a>
	                        </li>
	                        <li>
	                            <a href="javascript:;"[#if page.pageSize == 100] class="current"[/#if] val="100">100</a>
	                        </li>
	                    </ul>
	                </div>
	            </div>
	        </div>
	        <!-- <div class="menuWrap">
	            <div class="search">
	                <input type="text" id="keyword" name="keyword" placeholder="导购、商家" value="" maxlength="200"/>
	                <button type="submit">&nbsp;</button>
	            </div>
	        </div> -->
	    </div>
		<input type="hidden" name="id" value="" />
		<table id="listTable" class="list" >
			<tr>
				<th>奖励时间</th>
				<th>奖励金额</th>
				<th>奖励人数</th>
				<th>奖励类型</th>
				<th>备注</th>
				<th>操作</th>
			</tr>
			[#list page.content as subsidies]
				<tr>
					<td>${subsidies.createDate?string('yyyy-MM-dd HH:mm:ss')}</td>
					<td>${subsidies.amount}</td>
					<td>${subsidies.count}</td>
					<td>
						[#if subsidies.type=="recharge"]充值
						[#elseif subsidies.type=="receipts"]货款
						[#elseif subsidies.type=="profit"]分润
						[#elseif subsidies.type=="other"]其他[/#if]
					</td>
					<td>${subsidies.remark}</td>
					<td>
						<a href="${base}/admin/subsidy/child_subsidy_list.jhtml?id=${subsidies.id}">查看</a>
						<a href="javascript:;" onclick="confirm_recharge(${subsidies.id})">审核</a>
					</td>
				</tr>
			[/#list]
		</table>
		[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
			[#include "/admin/include/pagination.ftl"]
		[/@pagination]
	</form>
	<div id="trade_wrap"></div>
</body>
</html>