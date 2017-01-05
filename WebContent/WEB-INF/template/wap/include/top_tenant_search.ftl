<div class="am-popup" id="search-modal">
    <div class="am-popup-inner">
        <div class="am-popup-hd"><h4 class="am-popup-title">搜索</h4>
         <span data-am-modal-close="" class="am-close" ><i class="am-icon-chevron-left"></i></span>
        </div>
        <div class="am-popup-bd" style="background:#fff;padding: 0px 10px 0px;">
        	<div data-am-widget="tabs" class="am-tabs am-tabs-d2" style="margin:0px;">
  				<ul class="am-tabs-nav am-cf" >
    				<li class="am-active">
      					<a style="line-height: 30px;" href="[data-tab-panel-0]">商家</a>
    				</li>
   					 <li class="" >
      					<a style="line-height: 30px;" href="[data-tab-panel-1]">商品</a>
    				</li>
  				</ul>
  				<div class="am-tabs-bd" style="border-left: none;border-right: none;">
  					<div data-tab-panel-0 class="am-tab-panel  am-active" style="border-left: none;border-right: none;">
   						<form  method="GET" action="${base}/wap/delivery/search.jhtml" >
               			 <div class="am-input-group am-input-group-primary">
                			<input type="text" name="keyword" class="am-form-field"  placeholder="输入关键词进行搜索" >
                			<span class="am-input-group-btn">
               				 <button class="am-btn am-btn-primary" type="submit"><span class="am-icon-search"></span></button>
               	 			</span>
                			</div>
              			</form>
                		<div class="am-margin-top-sm am-text-left"><strong>热门搜索:</strong>
                		[#list setting.hotTenantSearch?split(",") as hot]
                				<a href="${base}/wap/delivery/search.jhtml?keyword=${hot}" >${hot}</a>&nbsp;
                			[/#list]
                 		</div>
   					</div>
    				<div data-tab-panel-1 class="am-tab-panel">
    					<form action="${base}/wap/product/search.jhtml" method="GET">
               			 <div class="am-input-group am-input-group-primary">
                			<input type="text" name="keyword" class="am-form-field"  placeholder="输入关键词进行搜索" >
                			<span class="am-input-group-btn">
               				 <button class="am-btn am-btn-primary" type="submit"><span class="am-icon-search"></span></button>
               	 			</span>
                			</div>
              			</form>
                		<div class="am-margin-top-sm am-text-left"><strong>热门搜索:</strong>
               	 			[#list setting.hotSearch?split(",") as hot]
                				<a href="${base}/wap/product/search.jhtml?keyword=${hot}" >${hot}</a>&nbsp;
                			[/#list]
                 		</div>
    				</div>
				</div>
			</div>
        </div>
    </div>
</div>