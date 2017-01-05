$(function(){

	// $('body').css('height',$(document).width() + 'px');
	//$('.p_recommend_list img').addClass('bottomind');
	$('.p_login').addClass('ddin');
	$('.p_loginenter').addClass('rightin');
	$('.p_loginlg').addClass('leftin');
	$.later(function(){
		$('.p_copyright').addClass('bottominl').show();
		//$('.p_recommend_list img').removeClass('bottomind');
	}, 1000, false);

	// $.later(function(){
	// 	$('.p_loginenter').removeClass('rightin');
	// 	$('.p_loginlg').removeClass('leftin');
	// 	$('.p_login').addClass('ddin');
	// 	$('.p_copyright').removeClass('bottomin');
	// }, 2000, false);
	
	if($('.p_login').length > 0){
		//忘记密码
		$('.p_nopw').on('tap',function(){
			$('.p_nopw_wind').show().removeClass('rightout').addClass('leftin');
			$('.p_windowbg').show().removeClass('ddout');
			$('.p_login').addClass('ddout').removeClass('ddin');
			$('.p_tips').addClass('ddout').removeClass('ddin');
		})
		//忘记密码 关闭按钮
		$('.p_nclose').on('tap',function(){
			$('.p_nopw_wind').removeClass('leftin').addClass('rightout');
			$.later(function(){
				$('.p_windowbg').addClass('ddout').hide();
				$('.p_login').removeClass('ddout').addClass('ddin');
			}, 500, false);
			$('.p_tips').addClass('ddout').removeClass('ddin');
		})
		
		//登录提示
		// $('.p_loginbtn').on('tap',function(){
		// 	$('.p_tips').show().addClass('ddin').removeClass('ddout');
		// 	$('.p_tips p').text('您输入的账号或密码错误，请重新输入！');
		// })
		//登录提示 关闭
		$('.p_tipsclose').on('tap',function(){
			$('.p_tips').addClass('ddout').removeClass('ddin');
		})
		//密码发送 提示
		// $('.p_nopwbtn').on('tap',function(){
		// 	$('.p_tips').show().addClass('ddin').removeClass('ddout');
		// 	$('.p_tips p').text('您输入的手机号错误，请重新输入！');
		// })
	}


})
