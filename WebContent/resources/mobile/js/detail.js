$(function(){
	//图片大小
	$('.m_roombanner').css("height",window.innerWidth - 40+'px');

	$('.m_collect').on('tap',function(){
		if($('.m_collect').hasClass('m_down')){
			$('.m_collect').removeClass('p_turnin');
			$('.m_collect').removeClass('m_down').addClass('p_turnout');
		}else{
			$('.m_collect').removeClass('p_turnout');
			$('.m_collect').addClass('m_down').addClass('p_turnin');
		}
	});
	$('.m_shopsc i').on('tap',function(){
		if($('.m_shopsc i').hasClass('m_down')){
			$('.m_shopsc i').removeClass('p_turnin');
			$('.m_shopsc i').removeClass('m_down').addClass('p_turnout');
		}else{
			$('.m_shopsc i').removeClass('p_turnout');
			$('.m_shopsc i').addClass('m_down').addClass('p_turnin');
		}
	});
})
