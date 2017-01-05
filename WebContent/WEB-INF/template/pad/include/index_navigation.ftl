<script type="text/javascript">
$().ready(function(){
	var $p_tap1 = $("#p_navscrooler .p_tap1li .p_navli a");
	var $p_Root = $("#p_navscrooler .p_tap1li .p_navli div.p_listinRoot");
	var $p_tap2 = $("#p_tap2ul");
	var $p_tap3 = $("#p_tap3ul");
	var $p_tap2_a = $("#p_tap2ul .p_navli a");
	var $p_tap3_a = $("#p_tap3ul .p_navli a");
	var $p_tap2_div = $("#p_tap2ul .p_navli div.p_listinTWO");
	//
	$p_tap1.on("tap",function(){
		var $this = $(this);
		location.href="${base}/pad/product/list.jhtml?productCategoryId="+$this.attr("productCategoryId");
		return false;
	});
	//
	$p_tap2_a.live("tap",function(){
		var $this = $(this);
		location.href="${base}/pad/product/list.jhtml?productCategoryId="+$this.attr("productCategoryId");
		return false;
	});
	//
	$p_tap3_a.live("tap",function(){
		var $this = $(this);
		location.href="${base}/pad/product/list.jhtml?productCategoryId="+$this.attr("productCategoryId");
		return false;
	});
	//
	$p_tap2_div.live("tap",function(){
		var $this = $(this);
		if($this.hasClass("flag")){
			$.ajax({
				url :"${base}/pad/productCategory/children.jhtml",
				data:{parentId:$this.attr("productCategoryId")},
				dataType:"json",
				type:"get",
				success:function(data){
					$p_tap3.empty();	
					for(var i in data){
						$p_tap3.append("<li><div class='p_navli'><a href='javascript:void();' productCategoryId="
										+data[i].id
										+">"
										+data[i].name+"</a></li>");
					}
				}
			});
			$('.p_tap2li ul li .p_listinTWO').addClass('flag');
			$this.removeClass("flag");
			$('.p_tap2li ul li').removeClass('p_navhove');
			$(this).parent().parent().addClass('p_navhove');
			$('.p_classf3').animate({left:500 + "px"},1000).addClass('leftind').addClass('p_block').removeClass('leftoutd');
			$('.p_tap2li ul li .p_tag').removeClass('p_deg180');
			$(this).parent().find('.p_tag').addClass('p_deg180');
			$("#p_classf3scrooler").iScroll('refresh');
		}else{
			$('.p_tap2li ul li .p_listinTWO').addClass('flag');
			$('.p_tap2li ul li').removeClass('p_navhove');
			$('.p_classf3').animate({left:0 + "px"},1000).addClass('leftoutd').removeClass('leftind');
			$('.p_tap2li ul li .p_tag').removeClass('p_deg180');
		}
	});
	//
	$p_Root.on("tap",function(){
		var $this = $(this);
		if($this.hasClass("flag")){
			$.ajax({
				url :"${base}/pad/productCategory/children.jhtml",
				data:{parentId:$this.attr("productCategoryId")},
				dataType:"json",
				type:"get",
				success:function(data){
					$p_tap2.empty();	
					for(var i in data){
						$p_tap2.append("<li><div class='p_navli'><a href='javascript:void()' productCategoryId="
										+data[i].id
										+">"
										+data[i].name
										+"</a><div class='p_tag'></div><div class='p_listin p_listin2 p_listinTWO flag' productCategoryId="
										+data[i].id
										+"></div></div></li>");
					}
				}
			});
			$this.removeClass("flag");
		}else{
			$this.addClass("flag");
		}
		return false;
	});
});

</script>
<aside class="p_aside">
	<div class="p_aside">
		<div class="p_navheader">
			<div class="p_logo">
				<h1>${area.name}</h1>
				<span>[ 切换城市 ]</span>
			</div>
			<div class="p_search">
				<div class="p_search_icon"></div>
				<input type="text" placeholder="请输入搜索内容"/>
			</div>
		</div>
		<div class="p_navbody" id="p_navscrooler">
			<div class="bodycont p_tap1li">
				<ul>
					[@product_category_root_list count = 10]
					 [#list productCategories as rootProductCategory]
					    <li>
							<div class="p_navli">
								<a href="javascript:void();" productCategoryId="${rootProductCategory.id}">
								<h1>${rootProductCategory.name}</h1>
								[@product_category_children_list count=1 productCategoryId=rootProductCategory.id]
									 [#list productCategories as productCategory]
									  	<p>${productCategory.name}</p>
									 [/#list]
								 [/@product_category_children_list]
								</a>
								<div class="p_tag" ></div>
								<div class="p_listin p_listin1 flag p_listinRoot"  productCategoryId="${rootProductCategory.id}"></div>
							</div>
					    </li>
					  [/#list]
					 [/@product_category_root_list]
				   </ul>
				<div class="p_h30"></div>
				</div>
			</div>
		</div>
	</aside>
	<div class="p_classf2" id="p_classf2scrooler">
		<div class="bodycont p_tap2li">
			<ul id="p_tap2ul">
			</ul>
		</div>
	</div>
	<div class="p_classf3" id="p_classf3scrooler">
		<div class="bodycont p_tap3li">
			<ul id="p_tap3ul">
			</ul>
		</div>
	</div>