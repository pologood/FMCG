$(function(){

		//优惠券
		if($('.m_addps').length > 0){
			$('.m_addps').on('tap',function(){
				
				$(this).toggleClass('m_addpstagz');
				$(this).find('.m_addps_zfxz').toggle();
			});
			$('.m_addps_zfxz ul li').on('tap',function(){
				$(this).siblings().find('.m_radior').removeClass('r_radioup');
				$(this).find('.m_radior').addClass('r_radioup');
				text = $(this).find('em').text();
				$(this).parents('.m_address').find('h1 span').text(text);
			});
		}
		if($('.m_addps_fp h1').length > 0){
			$('.m_addps_fp h1').on('tap',function(){
				$(this).parent().find('.m_radio').toggleClass('r_radioup');
				$(this).parent().find('.m_address_pfp').toggle();
			});
		}
		 //收货地址滚动
		 if($('#m_resscroller_1').length > 0){
		    var resscroller1 = $('#m_resscroller_1'),
				resscroll1 = new iScroll('m_resscroller_1',{onBeforeScrollStart:function(){resscroller1.iScroll('refresh');}});
		 }
		//收货地址选择
		$('.m_receipttc .m_address').on('tap',function(){
			$('.m_address').removeClass('m_receiptdown');
			if($(this).hasClass('m_receiptdown')){
				$(this).removeClass('m_receiptdown');
			}else{
				$(this).addClass('m_receiptdown');
			}
		});

		//收货地址修改
		$('.m_addressc').on('tap',function(){
			$('.m_receipt').show().addClass('rightin').removeClass('leftout').removeClass('rightout');
			$('.m_receiptbg').show().addClass('ddin').removeClass('ddout');	
		});

		//收货地址编辑
		
		$('.m_returnl').on('tap',function(){
			$('.m_receipt').removeClass('rightin').removeClass('leftin').addClass('rightout');
			$('.m_receiptbg').removeClass('ddin').addClass('ddout');
			$.later(function(){
				$('.m_receiptbg').hide();
			}, 800, false);		
		});
		//默认收货地址
		$('.m_radiomore').on('tap',function(){

			$('.m_radiomore').toggleClass('r_radioup');
		});
	    //定位后放大字母
	       $('#m_letter_list').find('a').on('touchstart',function(){
	        $(this).removeClass('m_scale_small').addClass('m_scale_big');
	       })
	       $('#m_letter_list').find('a').on('touchend',function(){
	          $(this).removeClass('m_scale_big').addClass('m_scale_small');
	       });
	    if($('#m_receiptcscroller').length > 0){
	//根据字母定位
		  var receiptrooller = $('#m_receiptcscroller'),
			  receiptrooll = new iScroll('m_receiptcscroller',{onBeforeScrollStart:function(){receiptrooller.iScroll('refresh');}});
			  
	      $('#m_letter_list a').on('tap',function(){
	      	receiptrooller.iScroll('refresh');
			receiptrooll.scrollToElement('#'+$(this).text(),600);
	      });
	  }

	 if($('.m_adddzc_area').length > 0){
	//新增地址区域切换
		$('.m_arear_qy1').on('tap',function(){
			$('.m_arear_dz1').show();
			$('.m_arear_dz2').hide();
			$('.m_arear_dz3').hide();
		});
		$('.m_arear_qy2').on('tap',function(){
			$('.m_arear_dz2').show();
			$('.m_arear_dz1').hide();
			$('.m_arear_dz3').hide();
		});
		$('.m_arear_qy3').on('tap',function(){
			$('.m_arear_dz3').show();
			$('.m_arear_dz2').hide();
			$('.m_arear_dz1').hide();
		});
	}
		
		
})