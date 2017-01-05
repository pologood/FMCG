$(function(){

		$('.p_radio').on('tap',function(){
			$('.p_radio').removeClass('r_radioup');
			if($(this).hasClass('r_radioup')){
				$(this).removeClass('r_radioup');
			}else{
				$(this).addClass('r_radioup');
			}
		});
		$('.p_action').on('tap',function(){
			$('.p_radior').removeClass('r_radioup');
			if($(this).find('.p_radior').hasClass('r_radioup')){
				$(this).find('.p_radior').removeClass('r_radioup');
			}else{
				$(this).find('.p_radior').addClass('r_radioup');
			}
		});
		$('.p_address_show').on('tap',function(){
			if($(this).find('.p_radiol').hasClass('r_radioup')){
				$(this).find('.p_radiol').removeClass('r_radioup');
				$('.p_invoice1').addClass('ddout').removeClass('ddin');
			}else{
				$(this).find('.p_radiol').addClass('r_radioup');
				$('.p_invoice1').addClass('ddin').removeClass('ddout').show();
			}
		});

		 //收货地址滚动
		 if($('#p_resscroller_1').length > 0){
	    var resscroller1 = $('#p_resscroller_1'),
			resscroll1 = new iScroll('p_resscroller_1',{onBeforeScrollStart:function(){resscroller1.iScroll('refresh');}});
		}
		//收货地址选择
		$('.p_receipttc .p_address').on('tap',function(){
			$('.p_address').removeClass('p_receiptdown');
			if($(this).hasClass('p_receiptdown')){
				$(this).removeClass('p_receiptdown');
			}else{
				$(this).addClass('p_receiptdown');
			}
		});

		//收货地址修改
		$('.p_addressc').on('tap',function(){
			$('.p_receipt').show().addClass('rightin').removeClass('leftout').removeClass('rightout');
			$('.p_receiptbg').show().addClass('ddin').removeClass('ddout');	
		});

		//收货地址编辑p_rfunpen
		
		$('.p_returnl').on('tap',function(){
			$('.p_receipt').removeClass('rightin').removeClass('leftin').addClass('rightout');
			$('.p_receiptbg').removeClass('ddin').addClass('ddout');
			$.later(function(){
				$('.p_receiptbg').hide();
			}, 800, false);		
		});
		//默认收货地址
		$('.p_radiomore').on('tap',function(){

			$('.p_radiomore').toggleClass('r_radioup');
		});
	    //定位后放大字母
	       $('#p_letter_list').find('a').on('touchstart',function(){
	        $(this).removeClass('p_scale_small').addClass('p_scale_big');
	       })
	       $('#p_letter_list').find('a').on('touchend',function(){
	          $(this).removeClass('p_scale_big').addClass('p_scale_small');
	       });
	      //区域滚动
	if($('#p_infoscroller_1').length > 0){
	 var infoscroller_1 = $('#p_infoscroller_1'),
	     infoscroll_1 = new iScroll('p_infoscroller_1',{onBeforeScrollStart:function(){infoscroller_1.iScroll('refresh');}}),
		 infoscroller_2 = $('#p_infoscroller_2'),
	     infoscroll_2 = new iScroll('p_infoscroller_2',{onBeforeScrollStart:function(){infoscroller_2.iScroll('refresh');}}),
	     infoscroller_3 = $('#p_infoscroller_3'),
	     infoscroll_3 = new iScroll('p_infoscroller_3',{onBeforeScrollStart:function(){infoscroller_3.iScroll('refresh');}});
	  //区域切换
	  $('.p_inareap_1').on('tap',function(){
			$('.p_infoselect_1').toggle();
			$('.p_infoselect_2').hide();
			$('.p_infoselect_3').hide();
		});
	  $('.p_inareap_2').on('tap',function(){
	  		$('.p_infoselect_2').toggle();
	  		$('.p_infoselect_1').hide();
			$('.p_infoselect_3').hide();
		});
	  $('.p_inareap_3').on('tap',function(){
			$('.p_infoselect_3').toggle();
			$('.p_infoselect_2').hide();
			$('.p_infoselect_1').hide();
		});
	  function infoselect(){
			$('.p_infoselect_3').hide();
			$('.p_infoselect_2').hide();
			$('.p_infoselect_1').hide();
		}
		$('article').on('touchmove',function(){
			infoselect();
	    });
	}
		
		
})