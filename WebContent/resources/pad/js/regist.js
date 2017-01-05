$(function(){
	//区域滚动
	 var rescroller = $('#p_rescroller'),
	     rescroll = new iScroll('p_rescroller',{onBeforeScrollStart:function(){rescroller.iScroll('refresh');}}),
		 rescroller_1 = $('#p_rescroller_1'),
	     rescroll_1 = new iScroll('p_rescroller_1',{onBeforeScrollStart:function(){rescroller_1.iScroll('refresh');}}),
	     rescroller_2 = $('#p_rescroller_2'),
	     rescroll_2 = new iScroll('p_rescroller_2',{onBeforeScrollStart:function(){rescroller_2.iScroll('refresh');}});

	 if($('#p_atextscrooll').length > 0){
	  var  atextscrooller = $('#p_atextscrooll'),
	       atextscrooll = new iScroll('p_atextscrooll',{onBeforeScrollStart:function(){atextscrooller.iScroll('refresh');}});	
	 }
	 if($('#p_rescroller_4').length > 0){
	  var rescroller_4 = $('#p_rescroller_4'),
	      rescroll_4 = new iScroll('p_rescroller_4',{onBeforeScrollStart:function(){rescroller_4.iScroll('refresh');}}),
	      rescroller_5 = $('#p_rescroller_5'),
	      rescroll_5 = new iScroll('p_rescroller_5',{onBeforeScrollStart:function(){rescroller_5.iScroll('refresh');}}),
	      rescroller_6 = $('#p_rescroller_6'),
	      rescroll_6 = new iScroll('p_rescroller_6',{onBeforeScrollStart:function(){rescroller_6.iScroll('refresh');}});		
	 }		
	  //区域切换
	  $('.p_areap_1').on('tap',function(){
			$('.p_areaselect_1').toggle();
			$('.p_areaselect_2').hide();
			$('.p_areaselect_3').hide();
			$('.p_areaselect_4').hide();
			$('.p_areaselect_5').hide();
			$('.p_areaselect_6').hide();
		});
	  $('.p_areap_2').on('tap',function(){
	  		$('.p_areaselect_2').toggle();
	  		$('.p_areaselect_1').hide();
			$('.p_areaselect_3').hide();
			$('.p_areaselect_4').hide();
			$('.p_areaselect_5').hide();
			$('.p_areaselect_6').hide();
		});
	  $('.p_areap_3').on('tap',function(){
			$('.p_areaselect_3').toggle();
			$('.p_areaselect_2').hide();
			$('.p_areaselect_1').hide();
			$('.p_areaselect_4').hide();
			$('.p_areaselect_5').hide();
			$('.p_areaselect_6').hide();
		});
	  $('.p_areap_4').on('tap',function(){
			$('.p_areaselect_4').toggle();
			$('.p_areaselect_2').hide();
			$('.p_areaselect_1').hide();
			$('.p_areaselect_3').hide();
			$('.p_areaselect_5').hide();
			$('.p_areaselect_6').hide();
		});
	  $('.p_areap_5').on('tap',function(){
			$('.p_areaselect_5').toggle();
			$('.p_areaselect_2').hide();
			$('.p_areaselect_1').hide();
			$('.p_areaselect_4').hide();
			$('.p_areaselect_3').hide();
			$('.p_areaselect_6').hide();
		});
	  $('.p_areap_6').on('tap',function(){
			$('.p_areaselect_6').toggle();
			$('.p_areaselect_2').hide();
			$('.p_areaselect_1').hide();
			$('.p_areaselect_4').hide();
			$('.p_areaselect_5').hide();
			$('.p_areaselect_3').hide();
		});
	  $('article').on('touchmove',function(e){
			areselect();
	    });
	 //注册切换
	 if($('.p_registtab_1').length > 0){
		 $('.p_registtab_1').on('tap',function(){
		 		$('.p_userrg_wind_1').show().addClass('rightin').removeClass('rightout');
		 		$('.p_registtab_1').addClass('p_detailfunup');
		 		if($('.p_registtab_2').hasClass('p_detailfunup')){
					$('.p_userrg_wind_2').addClass('rightout').removeClass('rightin');
					$('.p_registtab_2').removeClass('p_detailfunup');
				}
				if($('.p_registtab_3').hasClass('p_detailfunup')){
					$('.p_userrg_wind_3').addClass('rightout').removeClass('rightin');
					$('.p_registtab_3').removeClass('p_detailfunup');
				}
				areselect();

		});
		 $('.p_registtab_2').on('tap',function(){
	 			$('.p_userrg_wind_2').show().addClass('rightin').removeClass('rightout');
		 		$('.p_registtab_2').addClass('p_detailfunup');
		 		if($('.p_registtab_1').hasClass('p_detailfunup')){
					$('.p_userrg_wind_1').addClass('rightout').removeClass('rightin');
					$('.p_registtab_1').removeClass('p_detailfunup');
				}
				if($('.p_registtab_3').hasClass('p_detailfunup')){
					$('.p_userrg_wind_3').addClass('rightout').removeClass('rightin');
					$('.p_registtab_3').removeClass('p_detailfunup');
				}
				areselect();
		 });
		 $('.p_registtab_3').on('tap',function(){
	 			$('.p_userrg_wind_3').show().addClass('rightin').removeClass('rightout');
		 		$('.p_registtab_3').addClass('p_detailfunup');
		 		if($('.p_registtab_2').hasClass('p_detailfunup')){
					$('.p_userrg_wind_2').addClass('rightout').removeClass('rightin');
					$('.p_registtab_2').removeClass('p_detailfunup');
				}
				if($('.p_registtab_1').hasClass('p_detailfunup')){
					$('.p_userrg_wind_1').addClass('rightout').removeClass('rightin');
					$('.p_registtab_1').removeClass('p_detailfunup');
				}
				areselect();
		 });
	 }
	 function areselect(){
			$('.p_areaselect_1').hide();
			$('.p_areaselect_2').hide();
			$('.p_areaselect_3').hide();
			$('.p_areaselect_4').hide();
			$('.p_areaselect_5').hide();
			$('.p_areaselect_6').hide();
		}
	//用户协议效果
	$('.p_agreement').on('tap',function(){
		$('.p_agreementtext').show().addClass('topin').removeClass('topout');
		$('.p_registclose').show().addClass('topin').removeClass('topout');
	 });
	$('.p_registclose').on('tap',function(){
		$('.p_agreementtext').addClass('topout').removeClass('topin');
		$('.p_registclose').addClass('topout').removeClass('topin');
	 });
	if($('.p_userrg_wind').length > 0){
	  var  nactscrooler1 = $('#p_nactscrooler_1'),
	       nactscrooll1 = new iScroll('p_nactscrooler_1',{onBeforeScrollStart:function(){nactscrooler1.iScroll('refresh');}}),
      	   nactscrooler2 = $('#p_nactscrooler_2'),
           nactscrooll2 = new iScroll('p_nactscrooler_2',{onBeforeScrollStart:function(){nactscrooler2.iScroll('refresh');}}),
           nactscrooler3 = $('#p_nactscrooler_3'),
           nactscrooll3 = new iScroll('p_nactscrooler_3',{onBeforeScrollStart:function(){nactscrooler3.iScroll('refresh');}});
           $('.p_userrg_wind').css("max-height",window.innerHeight -200 +'px');			
	 }
	 if($('.p_nactscrooler_4').length > 0){
	  var  nactscrooler4 = $('#p_nactscrooler_4'),
           nactscrooll4 = new iScroll('p_nactscrooler_4',{onBeforeScrollStart:function(){nactscrooler4.iScroll('refresh');}});
           $('.p_userrg_wind').css("max-height",window.innerHeight -139 +'px');			
	 }
})
