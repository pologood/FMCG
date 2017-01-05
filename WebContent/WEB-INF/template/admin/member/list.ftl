<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("admin.member.list")} - Powered By rsico</title>
<meta name="author" content="rsico Team" />
<meta name="copyright" content="rsico" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
<script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>
<script type="text/javascript">
$().ready(function() {
	[@flash_message /]
	var $listForm = $("#listForm");
	var $filterSelect = $("#filterSelect");
	var $filterOption = $("#filterOption a");
	// 订单筛选
	$filterSelect.mouseover(function() {
		var $this = $(this);
		var offset = $this.offset();
		var $menuWrap = $this.closest("div.menuWrap");
		var $popupMenu = $menuWrap.children("div.popupMenu");
		$popupMenu.css({left: offset.left, top: offset.top + $this.height() + 2}).show();
		$menuWrap.mouseleave(function() {
			$popupMenu.hide();
		});
	});
	
	// 筛选选项
	$filterOption.click(function() {
		var $this = $(this);
		var $dest = $("#" + $this.attr("name"));
		if ($this.hasClass("checked")) {
			$dest.val("");
		} else {
			$dest.val($this.attr("val"));
		}
		$listForm.submit();
		return false;
	});
	//导出
	$("#export_ss").click(function () {
        if(confirm("导出是当前页面数据导出，如想导出多条数据，可选择每页显示数")){
        	$.message("success","正在帮您导出，请稍后");
            //导出数据到excel
	        $(".table2excel").table2excel({
	          	exclude: ".noExl",
	          	name: "会员列表",
	          	filename: "会员列表",
	          	fileext: ".xls", 
	          	exclude_img: true,
	          	exclude_links: false,
	          	exclude_inputs: true
	        });
        }
        
    });
});
</script>
</head>
<body>
	<div class="path">
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; ${message("admin.member.list")} <span>(${message("admin.page.total", page.total)})</span>
	</div>
	<form id="listForm" action="list.jhtml" method="get">
		<input type="hidden" name="authStatus" value="${authStatus}" id="authStatus" />
		<div class="bar">
			<a href="add.jhtml" class="iconButton">
				<span class="addIcon">&nbsp;</span>${message("admin.common.add")}
			</a>
			<div class="buttonWrap">
				<!--<a href="javascript:;" id="deleteButton" class="iconButton disabled">
					<span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
				</a>-->
				<a href="javascript:;" id="export_ss" class="iconButton">导出</a>
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
							<li>
	                            <a href="javascript:;"[#if page.pageSize == 500] class="current"[/#if] val="500">500</a>
	                        </li>
	                        <li>
	                            <a href="javascript:;"[#if page.pageSize == 1000] class="current"[/#if] val="1000">1000</a>
	                        </li>
						</ul>
					</div>
				</div>
			</div>
			<div class="menuWrap">
				<div class="search">
					<input type="text" id="searchValue" name="searchValue" value="${page.searchValue}" maxlength="200" placeholder="名称、手机号"/>
					<button type="submit">&nbsp;</button>
				</div>
			</div>
			<div class="menuWrap">
				<input type="text" id="beginDate" name="beginDate" class="text Wdate" value="[#if beginDate??]${beginDate?string("yyyy-MM-dd")}[/#if]" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', maxDate: '#F{$dp.$D(\'endDate\')}'});" placeholder="注册开始日期"/>
				&nbsp;-&nbsp;
				<input type="text" id="endDate" name="endDate" class="text Wdate" value="[#if endDate??]${endDate?string("yyyy-MM-dd")}[/#if]" onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '#F{$dp.$D(\'beginDate\')}'});" placeholder="注册结束日期"/>
			</div>
			<input type="submit" value="查询" class="bar buttonWrap button">
		</div>
		<table id="listTable" class="list table2excel">
			<tr>
				<!-- <th class="check">
					<input type="checkbox" id="selectAll" />
				</th> -->
				<th>
					<a href="javascript:;" class="sort" name="createDate">注册日期</a>
				</th>
				<th>
					<a href="javascript:;">名称</a>
				</th>
				<th>
					<a href="javascript:;">用户名</a>
				</th>
				<th>
					<a href="javascript:;">号码</a>
				</th>
				<th>
					<a href="javascript:;" class="sort" name="memberRank">会员等级</a>
				</th>
				<th>
					<a href="javascript:;">实名认证</a>
				</th>
				<th>
					<a href="javascript:;">发展者</a>
				</th>
				<th>
					<a href="javascript:;">发展者店主</a>
				</th>
                <th>
                    <a href="javascript:;" class="sort" name="memberRank">余额</a>
                </th>
                <th>
                    <a href="javascript:;" class="sort" name="memberRank">冻结余额</a>
                </th>
				<!-- <th>
					<a href="javascript:;" class="sort" name="email">${message("Member.email")}</a>
				</th> -->
				<th>
					<a href="javascript:;">交易金额</a>
				</th>
				<th>
					<a href="javascript:;">交易次数</a>
				</th>
				<th>
					<a href="javascript:;">登录次数</a>
				</th>
				<th>
					<span>${message("admin.member.status")}</span>
				</th>
				<th class="noExl">
					<span>${message("admin.common.handle")}</span>
				</th>
			</tr>
			[#list page.content as member]
			[#if member??]
				<tr>
					<!-- <td>
						<input type="checkbox" name="ids" value="${member.id}" />
					</td> -->
					<td>
						<span title="${member.createDate?string("yyyy-MM-dd HH:mm:ss")}">${member.createDate}</span>
					</td>
					<td>${(member.displayName)!'--'}</td>
					<td>${(member.username)!'--'}</td>
					<td>${(member.mobile)!'--'}</td>
					<td>${member.memberRank.name}</td>
					<td>
						[#if member.idCard??]
							[#if member.idCard=="success"]已认证
							[#else]未认证[/#if]
						[#else]未认证[/#if]
					</td>
                    <td>[#if member.member??]${member.member.name}[#else]--[/#if]</td>
                    <td>[#if member.member??][#if member.member.tenant??]${member.member.tenant.name}[#else]--[/#if][#else]--[/#if]</td>
					<td>${member.balance}</td>
					<td>${member.freezeBalance}</td>
					<td>${member.orderAmount}</td>
					<td>${member.orderCount}</td>
					<td>${(member.loginCount)!0}</td>
					<td>
						[#if !member.isEnabled]
							<span class="red">${message("admin.member.disabled")}</span>
						[#elseif member.isLocked!='none']
							<span class="red"> ${message("admin.member."+member.isLocked)} </span>
						[#else]
							<span class="green">${message("admin.member.normal")}</span>
						[/#if]
						[#if member.AuthStatus=="success"]
							<span class="red">[实名]</span>
						[/#if]
					</td>
					<td class="noExl">
						<a href="view.jhtml?id=${member.id}">[${message("admin.common.view")}]</a>
						<!-- a href="edit.jhtml?id=${member.id}">[${message("admin.common.edit")}]</a -->
					</td>
				</tr>
			[/#if]
			[/#list]
		</table>
		[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
			[#include "/admin/include/pagination.ftl"]
		[/@pagination]
	</form>
	<div id="trade_wrap"></div>
</body>
</html>