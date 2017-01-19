<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>门店列表 - Powered By rsico</title>
<meta name="author" content="rsico Team" />
<meta name="copyright" content="rsico" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />

</style>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
<script type="text/javascript">
$().ready(function() {

	[@flash_message /]

});
</script>
</head>
<body>
	<div class="path">
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 门店列表 <span>(${message("admin.page.total", page.total)})</span>
	</div>
	<form id="listForm" action="deliveryCenter_list.jhtml" method="get">
		<input type="hidden" name="id" value="${communityId}">
		<div class="bar">
			<a href="deliveryCenter_add.jhtml?communityId=${communityId}" class="iconButton">
				<span class="addIcon">&nbsp;</span>选择待添加门店
			</a>
			<div class="buttonWrap">
				<a href="javascript:;" id="addButton" class="iconButton disabled" onclick="deleted_delivery_center()">
					<span class="deleteIcon">&nbsp;</span>移除社区
				</a>
				<a href="javascript:;" id="refreshButton" class="iconButton">
					<span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
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
			<div class="buttonWrap">
				<input type="text" class="text" name="keyword" value="${keyword}" placeholder="搜索：门店名称,店铺名称,联系人,电话" style="width:230px;" />
				<input type="button" class="button" value="查询" onclick="set_page_number()"/>
			</div>
		</div>
		<table id="listTable" class="list">
			<tr>
				<th class="check"><input type="checkbox" id="selectAll" /></th>
				<th><span>创建时间</span></th>
				<th><span>门店名称</span></th>
				<th><span>所属店铺</span></th>
				<th><span>地区</span></th>
				<th><span>地址</span></th>
				<th><span>联系人</span></th>
				<th><span>电话</span></th>
				<th><span>是否默认</span></th>
			</tr>
			[#list page.content as deliveryCenter]
				<tr>
					<td><input type="checkbox" name="ids" value="${deliveryCenter.id}" /></td>
					<td><span title="${deliveryCenter.createDate?string("yyyy-MM-dd HH:mm:ss")}">${deliveryCenter.createDate}</span></td>
					<td>${deliveryCenter.name}</td>
					<td>[#if deliveryCenter.tenant?has_content]${deliveryCenter.tenant.name}[#else]--[/#if]</td>
					<td>${deliveryCenter.areaName}</td>
					<td>${deliveryCenter.address}</td>
					<td>${deliveryCenter.contact}</td>
					<td>${(deliveryCenter.phone)!"--"}</td>
					<td>[#if deliveryCenter.isDefault=='true']是[#else]否[/#if]</td>
				</tr>
			[/#list]
		</table>
		[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
			[#include "/admin/include/pagination.ftl"]
		[/@pagination]
	</form>
	<script type="text/javascript">
    function set_page_number(){
        $("#pageNumber").val("1");
        $("#listForm").submit();
    }
    function deleted_delivery_center(){
    	if($("#listTable input[name='ids']:checked").size()>0){
    		var ids=[];
    		$.each($("#listTable input[name='ids']:checked"),function(i,obj){
    			ids.push($(obj).val());
    		});
    		if(ids.length<=0){
    			return;
    		}
    		$.ajax({
	            url: "${base}/admin/community/deliveryCenter_deleted.jhtml",
	            type: "post",
	            data: {
	            	ids:ids,
	            	communityId:"${communityId}"
	            },
	            traditional: true,
	            dataType: "json",
	            success: function (message) {
	            	$.message(message);
	                if(message.type=="success"){
	                	location.href="${base}/admin/community/deliveryCenter_list.jhtml?id=${communityId}";
	                }
	            }
	        });
    	}else{
    		$.message("warn","请先选择门店")
    	}
    }
</script>
</body>
</html>