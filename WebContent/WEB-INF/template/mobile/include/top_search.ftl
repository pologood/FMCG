<script type="text/javascript">
//搜索
$().ready(function(){
	$("#search").on('tap',function(){
		 var keyword = $("#keyword").val();
		 if(keyword==''){
			 mtips("请输入搜索关键字!");
			 return false;
		 }
		 location.href="${base}/mobile/product/search.jhtml?keyword="+keyword;
	});
});
</script>

<div class="m_search m_search_n m_search_style">
	<div class="m_searchon">
		<div class="p_search_icon"></div>
		<span>请输入搜索词</span>
	</div>
	<div class="m_searchclass">
		商品
	</div>
	<input type="text" placeholder="请输入搜索词" id="keyword" value="${productKeyword}">
	<div class="m_searchbtn" id="search">搜索</div>
</div>