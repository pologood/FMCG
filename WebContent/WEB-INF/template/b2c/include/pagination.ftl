[@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
<input type="hidden" id="pageSize" name="pageSize" value="${page.pageSize}" />
[#if totalPages > 1]
<div class="my-order-footer clearfix" style="margin-left:160px;" >
    <span class="p-num">
        [#if isFirst]
            <a class="pn-first" href="javascript:;">&lt;&lt;</a>
        [#else]
            <a class="pn-first" href="javascript:$.pageSkip(${firstPageNumber});">&lt;&lt;</a>
        [/#if]
        [#if hasPrevious]
            <a class="pn-prev" href="javascript:$.pageSkip(${previousPageNumber});">&lt;</a>
        [#else]
            <a class="pn-prev" href="javascript:;">&lt;</a>
        [/#if]
        [#list segment as segmentPageNumber]
            [#if segmentPageNumber_index == 0 && segmentPageNumber > firstPageNumber + 1]
                <b class="pn-break">…</b>
            [/#if]
            [#if segmentPageNumber != pageNumber]
                <a href="javascript:$.pageSkip(${segmentPageNumber});">${segmentPageNumber}</a>
            [#else]
                <a href="javascript:;" class="curr">${segmentPageNumber}</a>
            [/#if]
            [#if !segmentPageNumber_has_next && segmentPageNumber < lastPageNumber - 1]
                <b class="pn-break">…</b>
            [/#if]
        [/#list]
        [#if hasNext]
            <a class="pn-next" href="javascript:$.pageSkip(${nextPageNumber});">&gt;</a>
        [#else]
            <a class="pn-next" href="javascript:;">&gt;</a>
        [/#if]
        [#if isLast]
            <a class="pn-last" href="javascript:;">&gt;&gt;</a>
        [#else]
            <a class="pn-last" href="javascript:$.pageSkip(${lastPageNumber});">&gt;&gt;</a>
        [/#if]
    </span>
    <span class="p-skip">
        <em>共<b>${totalPages}</b>页&nbsp;&nbsp;到第</em>
        <input class="input-txt" id="pageNumber" name="pageNumber" maxlength="9" value="${pageNumber}" onpaste="return false;">
        <em>页</em>
        <a href="javascript:$.pageSubmit();" class="btn btn-default pageSubmit">确定</a>
    </span>
</div>

<script>
    $(function(){
        var $listForm = $("#listForm");
        var $searchPropertyOption = $("#searchPropertyOption a");
        var $searchValue = $("#searchValue");
        var $searchProperty = $("#searchProperty");
        var $pageNumber = $("#pageNumber");

        // 页码输入
        $pageNumber.keypress(function(event) {
            var key = event.keyCode ? event.keyCode : event.which;
            if ((key == 13 && $(this).val().length > 0) || (key >= 48 && key <= 57)) {
                return true;
            } else {
                return false;
            }
        });
        // 表单提交
        $listForm.submit(function() {
            if (!/^\d*[1-9]\d*$/.test($pageNumber.val())) {
                $pageNumber.val("1");
            }
            if ($searchValue.size() > 0 && $searchValue.val() != "" && $searchProperty.val() == "") {
                $searchProperty.val($searchPropertyOption.eq(0).attr("val"));
            }
        });

        // 页码跳转
        $.pageSkip = function(pageNumber) {
            $pageNumber.val(pageNumber);
            $listForm.submit();
            return false;
        };

        $.pageSubmit = function(a){
            $listForm.submit();
        };
    });
</script>
[/#if]
[/@pagination]