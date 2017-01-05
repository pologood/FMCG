<div class="m_Areas">
	<div class="m_Areas_cont">
		<h1><strong id="currentProvince"></strong> <span class="m_switch" areaId="${currentArea.id}">切换省份</span></h1>
		<div class="m_Areas_scrooler" id="m_Areas_scrooler_1">
			<ul id="chooseCity">
			</ul>
		</div>
	</div>
	<div class="m_Areas_cont">
		<div class="m_Areas_scrooler m_Areas_scrooler_s" id="m_Areas_scrooler_2">
			<ul id="chooseProvince">
			</ul>
		</div>
	</div>
	<div class="p_tipsclose"></div>
</div>
<script type="text/javascript">
	var $currentProvince=$("#currentProvince");
	var $chooseCity=$("#chooseCity");
	var $chooseProvince=$("#chooseProvince");
	
	$('.m_city').on('tap',function(){
		$('.m_Areas_bg').show().addClass('ddin').removeClass('ddout');
		$.later(function() {
			$('.m_Areas').show().addClass('bottomin').removeClass('ddout');
		}, 500, false);
		renderCity($(this).attr("areaId"));
	});
	$('.p_tipsclose').on('tap',function(){
		$('.m_Areas_bg').removeClass('ddin').addClass('ddout');
		$('.m_Areas').removeClass('bottomin').addClass('ddout');
		$.later(function() {
			$('.m_Areas').hide();
			$('.m_Areas_bg').hide();
			$('.m_Areas').find('.m_Areas_cont').eq(0).show();
			$('.m_Areas').find('.m_Areas_cont').eq(1).hide();
		}, 500, false);
	});
	$('.m_Areas_bg').on('tap',function(){
		$('.m_Areas_bg').removeClass('ddin').addClass('ddout');
		$('.m_Areas').removeClass('bottomin').addClass('ddout');
		$.later(function() {
			$('.m_Areas').hide();
			$('.m_Areas_bg').hide();
			$('.m_Areas').find('.m_Areas_cont').eq(0).show();
			$('.m_Areas').find('.m_Areas_cont').eq(1).hide();
		}, 500, false);
	});
	$('.m_switch').on('click',function(){
		$('.m_Areas').find('.m_Areas_cont').eq(0).hide();
		$('.m_Areas').find('.m_Areas_cont').eq(1).show();
		renderProvince($(this).attr("areaId"));
	});
	$('.m_Areas_scrooler li').on('tap',function(){
		$(this).parents('.m_Areas_scrooler').find('li').removeClass('down');
		$(this).addClass('down');
	});
	
	function renderProvince(id){
		$.ajax({
			url:"${base}/common/parent.jhtml",
			type:"get",
			dataType:"json",
			data:{
				id:id
			},
			success:function(data){
				if(data!=null){
					var id=data.id;
					$.ajax({
						url:"${base}/common/sibling.jhtml",
						type:"get",
						dataType:"json",
						data:{id:id},
						success:function(map){
							$chooseProvince.empty();
							for(var key in map){
								if(key==id){
									$chooseProvince.append("<li class='down' areaId="+key+">"+map[key]+"</li>");
								}else{
									$chooseProvince.append("<li areaId="+key+">"+map[key]+"</li>");
								}
							}
							$chooseProvince.find("li").on("tap",function(){
								$('.m_Areas').find('.m_Areas_cont').eq(0).show();
								$('.m_Areas').find('.m_Areas_cont').eq(1).hide();
								$currentProvince.text($(this).text());
								$chooseCity.empty();
								$.ajax({
									url:"${base}/common/area.jhtml",
									type:"get",
									dataType:"json",
									data:{
										parentId:$(this).attr("areaId")
									},
									success:function(map){
										for(var key in map){
											$chooseCity.append("<li areaId="+key+">"+map[key]+"</li>");
										}
										$chooseCity.find("li").on("tap",function(){
											setCity($(this).attr("areaId"));
										});
									}
								});
							});
						}
					});
				}
			}
		});
	}
	
	
	function renderCity(areaId){
		$.ajax({
			url:"${base}/common/parent.jhtml",
			type:"get",
			dataType:"json",
			data:{
				id:areaId
			},
			success:function(data){
				if(data!=null){
					$currentProvince.text(data.name);
				}
			}
		});	
		$.ajax({
			url:"${base}/common/sibling.jhtml",
			type:"get",
			dataType:"json",
			data:{
				id:areaId
			},
			success:function(map){
				$chooseCity.empty();
				for(var key in map){
					if(key==areaId){
						$chooseCity.append("<li class='down' areaId="+key+">"+map[key]+"</li>");
					}else{
						$chooseCity.append("<li areaId="+key+">"+map[key]+"</li>");
					}
				}
				$chooseCity.find("li").on("tap",function(){
					setCity($(this).attr("areaId"));
				});
			}
		});
	}
	
	//选择城市切换
	function setCity(id){
		$.ajax({
			url:"${base}/common/switchCity.jhtml",
			type:"get",
			dataType:"json",
			data:{
				id:id
			},
			success:function(message){
				if(message.type=='success'){
					mtips(message.content);
					setTimeout(function(){location.reload();},1000);
				}
			}
		});
	}
	
</script>