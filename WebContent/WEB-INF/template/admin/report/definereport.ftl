<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>      
<head>      
<title>自定义浏览页面</title>      
<meta http-equiv="Content-Type" content="text/html; charset=GBK">  
<link href="${base}/resources/admin/css/common.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${base}/ReportServer?op=resource&resource=/com/fr/web/core/page.css"></link>      
<link rel="stylesheet" type="text/css" href="${base}/ReportServer?op=resource&resource=/com/fr/web/load.css"></link>   
<script type="text/javascript" src="${base}/ReportServer?op=resource&resource=/com/fr/web/load.js"></script>
<script type="text/javascript">       
$(function() {      
	var $toolbar = $("#toolbar");      
	var $reportPane = $("#reportPane").PagePane();      
	// 取出实际的PagePane对象，PagePane用全局变量      
	pagePane = $reportPane.data("PagePane");// james:get object of the pagepane      
	pagePane.load("${base}/${reportFile.source}");      
     
	// 页码功能      
	pagePane.on("afterload", function(){      
	    // currentPageIndex是从0开始的      
	    var cPageIndex = pagePane.currentPageIndex + 1;     
	    var pv = "第" + cPageIndex + "页/共" + pagePane.reportTotalPage + "页";   
	    $("#pnum").val(pv);   
	    var $obj = $("#contentDIV").children("div");
	    $("#contentDIV").css({height:($(".x-table").height()+100)+"px",width:($("#contentDIV").width()+50)+"px"});
	    $obj.eq(0).css({height:($(".x-table").height()+100)+"px"});
	    $obj.eq(1).css({height:($(".x-table").height()+100)+"px",overflow:"visible",paddingRight:"15px"});
	    $obj.eq(2).css({top:($(".x-table").height()+100)+"px"});
	    //$("#contentDIV").css({height:($(document.body).height()-100)+"px",overflow:"auto"});
	});      
     
	// 布局      
	var $container = $("<div>").appendTo($("body")).css({height:"93%", width:"100%"}).__border__([{region:"center",el:$reportPane}]);
	$container.doLayout();      
	$(window).resize(function() {      
		$container.doLayout();      
	});    
});      
</script>  
</head>      
<body style=" overflow:visible;"> 
<div class="path">
	<a href="${base}/admin/common/index.jhtml">${message("admin.path.index")}</a> &raquo;<a href="${base}/admin/IWReport/list.jhtml">报表管理</a>&raquo;${reportFile.name}
</div> 
<div id="toolbar">
<input type="button" class="button" value="${message("admin.common.back")}" onclick="history.go(-1)" />
<input type="button" class="button" value="首页" onclick="pagePane.gotoFirstPage()" />
<input type="button" class="button" value="上一页" onclick="pagePane.gotoPreviousPage()" />
<input id="pnum" type="text" readonly="true" style="width:80px">
<input type="button" class="button" value="下一页" onclick="pagePane.gotoNextPage()" />
<input type="button" class="button" value="末页" onclick="pagePane.gotoLastPage()" />
<input type="button" class="button" value="导出[PDF]" onclick="pagePane.exportReportToPDF()" />
<input type="button" class="button" value="导出[Excel]" onclick="pagePane.exportReportToExcel()" />
<input type="button" class="button" value="导出[Word]" onclick="pagePane.exportReportToWord()" />
<input type="button" class="button" value="页面设置" onclick="pagePane.pageSetup()" />
<!--<input type="button" class="button" value="客户端PDF打印" onclick="pagePane.pdfPrint()" />
<input type="button" class="button" value="客户端FLASH打印" onclick="pagePane.flashPrint()" />
<input type="button" class="button" value="邮件" onclick="pagePane.emailReport()" />-->
</div>

<hr style="color:#B0D1BF"/>
<div id="reportPane"></div>

<script>
	setTimeout(function(){
		$(".x-form-text").css({height:"20px"});
		$(".x-form-text").each(function(){
			$(this).width(($(this).width()-10)+"px");
		});
		$(".contentPane").height(($(".contentPane").height()-20)+"px");
		$(".x-trigger-icon").css({height:"22px"});
		$(".x-btn-text").css({background:"#E1EDFA",width:"60px",height:"23px"});
	},300);
</script>

</body> 
</html>
<style>
.x-form-text { margin-bottom:5px; background:none;}
</style>