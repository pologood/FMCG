<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>${message("admin.promotion.list")} - Powered By rsico</title>
<meta name="author" content="rsico Team" />
<meta name="copyright" content="rsico" />
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<style type="text/css">
	#listTable th{
		text-align: center;
	}
	#listTable td{
		text-align: center;
	}
</style>
<script type="text/javascript" src="${base}/resources/admin/js/jquery.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/common.js"></script>
<script type="text/javascript" src="${base}/resources/admin/js/list.js"></script>
<script type="text/javascript" src="${base}/resources/admin/datePicker/WdatePicker.js"></script>
<script type="text/javascript">
$().ready(function() {
	[@flash_message /]
});
</script>
</head>
<body>
	<div class="path">
		<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo; 联盟列表 <span>(${message("admin.page.total", page.total)})</span>
	</div>
	<form id="listForm" action="tenant_list.jhtml" method="get">
		<input type="hidden" name="id" value="${union.id}">
		<div class="bar">
			<div class="buttonWrap">
				<div class="menuWrap">
					<a href="javascript:;" id="refreshButton" class="iconButton">
						<span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
					</a>
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
			<div class="menuWrap">
				<div class="search">
					<span id="searchPropertySelect" class="arrow">&nbsp;</span>
					<input type="text" name="keyword" value="${keyword}" maxlength="200" />
					<button type="submit">&nbsp;</button>
				</div>
			</div>
			
	        <div class="menuWrap">
	            <input type="text" id="beginDate" name="beginDate" class="text Wdate" value="[#if beginDate??]${beginDate?string("yyyy-MM-dd")}[/#if]"
	                   onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', maxDate: '#F{$dp.$D(\'endDate\')}'});"/>
	            -<input type="text" id="endDate" name="endDate" class="text Wdate" value="[#if endDate??]${endDate?string("yyyy-MM-dd")}[/#if]"
	                    onfocus="WdatePicker({dateFmt: 'yyyy-MM-dd', minDate: '#F{$dp.$D(\'beginDate\')}'});"/>
	            <input type="submit" value="查询"/>
	        </div>
		</div> 
		<table id="listTable" class="list table2excel">
        <tr>
            <th>
                <span>入驻时间</span>
            </th>
            <th>
                <span>所属区域</span>
            </th>
            <th>
                <span>店铺分类</span>
            </th>
            <th>
                <span>店铺名称</span>
            </th>
            <th>
                <span>商品款数</span>
            </th>
            <th>
                <span>地址</span>
            </th>
            <th>
                <span>店主(电话)</span>
            </th>
            <th>
                <span>缩略图</span>
            </th>
            <th>
                <span>审核状态</span>
            </th>

            <th>
                <span>是否推荐</span>
            </th>
            <th>
                <span>是否加盟</span>
            </th>
            <th>
                <span>平台佣金</span>
            </th>
            <th>
                <span>推广分润</span>
            </th>
            <th>
                <span>联盟佣金</span>
            </th>
            <th>
                <span>实名认证</span>
            </th>
            <th>
                <span>绑定银行卡</span>
            </th>
        [#if versionType==0]
            <th>
                <span>二维码</span>
            </th>
        [/#if]
            
        </tr>
    [#list page.content as tenant]
        <tr>
            <td>
            ${tenant.createDate?string("yyyy-MM-dd")}
            </td>
            <td>
            ${(tenant.area.fullName)!}
            </td>
            <td>
            ${(tenant.tenantCategory.name)!}
            </td>
            <td>
                <span title="${tenant.name}">${abbreviate(tenant.name, 20, "...")}</span>
            </td>
            <td>
            ${(tenant.products?size)!}
            </td>
            <td>
            ${(tenant.address)!}
            </td>
            <td>
            ${(tenant.member.displayName)!}&nbsp;(${(tenant.member.mobile)!})
            </td>
            <td>
                [#if tenant.member.thumbnail??]是[#else ]否[/#if]
            </td>
            <td>
            ${message("Tenant.Status."+tenant.status)}
            </td>
            <td>
                [#assign isTag="否"]
                [#list tags as tag]
                [#if tenant.tags?seq_contains(tag)]
                    [#assign isTag="是"]
                    [#break /]
                [/#if]
            [/#list]
                ${isTag}
            </td>
            <td>[#if tenant.isUnion]是[#else ]否[/#if]</td>
            <td>${(tenant.brokerage)!}</td>
            <td>${(tenant.generalize)!}</td>
            <td>${(tenant.agency)!}</td>
            <td>[#if tenant.member.idcard??]是[#else ]否[/#if]</td>
            <td>[#if tenant.member.memberBanks?size>0]是[#else ]否[/#if]</td>
            [#if versionType==0]
                <td><!-- 二维码 -->
                    [#if tenant.qrcodes??&&tenant.qrcodes.size()>0]
                        <a href="javascript:;">
                            <img src="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=${tenant.qrcodes[0].ticket}"
                                 width="30px" height="30px" onclick=" $.showQrcode('${tenant.qrcodes[0].ticket}')"/>
                        </a>
                    [#else]
                        [#if tenant.status=='success']
                            <a href="edit_qrcode.jhtml?id=${tenant.id}">去绑定</a>
                        [/#if]
                    [/#if]
                </td>
            [/#if]
            
        </tr>
    [/#list]
    </table>
		[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
			[#include "/admin/include/pagination.ftl"]
		[/@pagination]
	</form>
</body>
</html>