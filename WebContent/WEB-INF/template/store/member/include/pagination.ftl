<input type="hidden" id="pageSize" name="pageSize" value="${page.pageSize}" />
<input type="hidden" id="searchProperty" name="searchProperty" value="${page.searchProperty}" />
<input type="hidden" id="orderProperty" name="orderProperty" value="${page.orderProperty}" />
<input type="hidden" id="orderDirection" name="orderDirection" value="${page.orderDirection}" />
[#if totalPages > 1]
	<ul class="pagination">
		[#if isFirst]
			<li class="paginate_button previous disabled">
				<a class="">第一页</a>
			</li>
		[#else]
			<li class="paginate_button previous">
				<a class="" href="javascript: $.pageSkip(${firstPageNumber});">第一页</a>
			</li>
		[/#if]
		
		[#if hasPrevious]
			<li class="paginate_button previous">
				<a class="" href="javascript: $.pageSkip(${previousPageNumber});">上一页</a>
			</li>
		[#else]
			<li class="paginate_button previous disabled">
				<a class="">上一页</a>
			</li>
		[/#if]

		[#list segment as segmentPageNumber]
			[#if segmentPageNumber_index == 0 && segmentPageNumber > firstPageNumber + 1]
				<li class="paginate_button ">
					<a class="">...</a>
				</li>
			[/#if]
			[#if segmentPageNumber != pageNumber]
				<li class="paginate_button ">
					<a href="javascript: $.pageSkip(${segmentPageNumber});">${segmentPageNumber}</a>
				</li>
			[#else]
				<li class="paginate_button active">
					<a class="">${segmentPageNumber}</a>
				</li>
			[/#if]
			[#if !segmentPageNumber_has_next && segmentPageNumber < lastPageNumber - 1]
				<li class="paginate_button">
					<a class="">...</a>
				</li>
			[/#if]
		[/#list]
		
		[#if hasNext]
			<li class="paginate_button next">
				<a class="" href="javascript: $.pageSkip(${nextPageNumber});">下一页</a>
			</li>
		[#else]
			<li class="paginate_button next disabled">
				<a class="">下一页</a>
			</li>
		[/#if]
		
		[#if isLast]
			<li class="paginate_button next disabled">
				<a class="">最后一页</a>
			</li>
		[#else]
			<li class="paginate_button next">
				<a class="" href="javascript: $.pageSkip(${lastPageNumber});">最后一页</a>
			</li>
		[/#if]

		<div class="" style="display:inline;line-height: 28px;margin-left: 10px;color: #777;">
			${message("admin.page.totalPages", totalPages)}
			${message("admin.page.pageNumber",
			 '<input id="pageNumber" name="pageNumber" value="' + pageNumber + '" maxlength="9" onpaste="return false;" style="width:40px;border: 1px solid #d2d6de;
    margin: 0 6px;height: 30px;text-align: center" />')}
		</div>
	</ul>
[/#if]