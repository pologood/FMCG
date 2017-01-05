$(function(){
	//detail 滚动
	var contscrooler1  = $('#p_contscrooler_1'),
		contscrooll1 = new iScroll('p_contscrooler_1',{onBeforeScrollStart:function(){contscrooler1.iScroll('refresh');}});
	var contscrooler2  = $('#p_contscrooler_2'),
		contscrooll2 = new iScroll('p_contscrooler_2',{onBeforeScrollStart:function(){contscrooler2.iScroll('refresh');}});
	var contscrooler3  = $('#p_contscrooler_3'),
		contscrooll3 = new iScroll('p_contscrooler_3',{onBeforeScrollStart:function(){contscrooler3.iScroll('refresh');}});
	var contscrooler4  = $('#p_contscrooler_4'),
		contscrooll4 = new iScroll('p_contscrooler_4',{onBeforeScrollStart:function(){contscrooler4.iScroll('refresh');}});
	
	$('.p_darticle0').addClass('bottomin');
	$('.p_detailfun0').addClass('p_detailfunup');

	$('.p_detailfun0').on('tap',function(){
		if($('.p_darticle0').hasClass('bottomin')){
				$('.p_darticle0').addClass('bottomout').removeClass('bottomin');
		}
		if($('.p_darticle1').hasClass('rightin')){
			$('.p_darticle1').addClass('rightout').removeClass('rightin');
		}
		if($('.p_darticle2').hasClass('rightin')){
			$('.p_darticle2').addClass('rightout').removeClass('rightin');
		}
		if($('.p_darticle3').hasClass('rightin')){
			$('.p_darticle3').addClass('rightout').removeClass('rightin');
		}
		if($('.p_darticle4').hasClass('rightin')){
			$('.p_darticle4').addClass('rightout').removeClass('rightin');
		}
		if($('.p_darticle5').hasClass('rightin')){
			$('.p_darticle5').addClass('rightout').removeClass('rightin');
		}
		$('.p_detailfun1').removeClass('p_detailfunup');
		$('.p_detailfun4').removeClass('p_detailfunup');
		$('.p_detailfun2').removeClass('p_detailfunup');
		$('.p_detailfun3').removeClass('p_detailfunup');
		$('.p_detailfun5').removeClass('p_detailfunup');
		$('.p_detailfun0').addClass('p_detailfunup');
		$('.p_darticle0').addClass('bottomin').removeClass('bottomout').addClass('p_left0');
	});

	$('.p_detailfun1').on('tap',function(){
		if($('.p_detailfun1').hasClass('p_detailfunup')){
				$('.p_detailfun1').removeClass('p_detailfunup');
				$('.p_darticle1').removeClass('rightin').addClass('rightout');
				$('.p_detailfun0').addClass('p_detailfunup');
				$('.p_darticle0').addClass('bottomin').removeClass('bottomout');
		}else{
			if($('.p_darticle0').hasClass('bottomin')){
				$('.p_darticle0').addClass('bottomout').removeClass('bottomin');
			}
			if($('.p_darticle2').hasClass('rightin')){
				$('.p_darticle2').addClass('rightout').removeClass('rightin');
			}
			if($('.p_darticle3').hasClass('rightin')){
				$('.p_darticle3').addClass('rightout').removeClass('rightin');
			}
			if($('.p_darticle4').hasClass('rightin')){
				$('.p_darticle4').addClass('rightout').removeClass('rightin');
			}
			if($('.p_darticle5').hasClass('rightin')){
				$('.p_darticle5').addClass('rightout').removeClass('rightin');
			}
			$('.p_detailfun0').removeClass('p_detailfunup');
			$('.p_detailfun4').removeClass('p_detailfunup');
			$('.p_detailfun2').removeClass('p_detailfunup');
			$('.p_detailfun3').removeClass('p_detailfunup');
			$('.p_detailfun5').removeClass('p_detailfunup');
			$('.p_detailfun1').addClass('p_detailfunup');
			$('.p_darticle1').addClass('rightin').removeClass('rightout').addClass('p_left0');
		}
	});

	$('.p_detailfun2').on('tap',function(){
		if($('.p_detailfun2').hasClass('p_detailfunup')){
				$('.p_detailfun2').removeClass('p_detailfunup');
				$('.p_darticle2').removeClass('rightin').addClass('rightout');
				$('.p_detailfun0').addClass('p_detailfunup');
				$('.p_darticle0').addClass('bottomin').removeClass('bottomout');
		}else{
			if($('.p_darticle0').hasClass('bottomin')){
				$('.p_darticle0').addClass('bottomout').removeClass('bottomin');
			}
			if($('.p_darticle1').hasClass('rightin')){
				$('.p_darticle1').addClass('rightout').removeClass('rightin');
			}
			if($('.p_darticle3').hasClass('rightin')){
				$('.p_darticle3').addClass('rightout').removeClass('rightin');
			}
			if($('.p_darticle4').hasClass('rightin')){
				$('.p_darticle4').addClass('rightout').removeClass('rightin');
			}
			if($('.p_darticle5').hasClass('rightin')){
				$('.p_darticle5').addClass('rightout').removeClass('rightin');
			}
			$('.p_detailfun0').removeClass('p_detailfunup');
			$('.p_detailfun1').removeClass('p_detailfunup');
			$('.p_detailfun4').removeClass('p_detailfunup');
			$('.p_detailfun3').removeClass('p_detailfunup');
			$('.p_detailfun5').removeClass('p_detailfunup');
			$('.p_detailfun2').addClass('p_detailfunup');
			$('.p_darticle2').addClass('rightin').removeClass('rightout').addClass('p_left0');
		}
	});

	$('.p_detailfun3').on('tap',function(){
		if($('.p_detailfun3').hasClass('p_detailfunup')){
				$('.p_detailfun3').removeClass('p_detailfunup');
				$('.p_darticle3').removeClass('rightin').addClass('rightout');
				$('.p_detailfun0').addClass('p_detailfunup');
				$('.p_darticle0').addClass('bottomin').removeClass('bottomout');
		}else{
			if($('.p_darticle0').hasClass('bottomin')){
				$('.p_darticle0').addClass('bottomout').removeClass('bottomin');
			}
			if($('.p_darticle2').hasClass('rightin')){
				$('.p_darticle2').addClass('rightout').removeClass('rightin');
			}
			if($('.p_darticle1').hasClass('rightin')){
				$('.p_darticle1').addClass('rightout').removeClass('rightin');
			}
			if($('.p_darticle4').hasClass('rightin')){
				$('.p_darticle4').addClass('rightout').removeClass('rightin');
			}
			if($('.p_darticle5').hasClass('rightin')){
				$('.p_darticle5').addClass('rightout').removeClass('rightin');
			}
			$('.p_detailfun0').removeClass('p_detailfunup');
			$('.p_detailfun1').removeClass('p_detailfunup');
			$('.p_detailfun2').removeClass('p_detailfunup');
			$('.p_detailfun4').removeClass('p_detailfunup');
			$('.p_detailfun5').removeClass('p_detailfunup');
			$('.p_detailfun3').addClass('p_detailfunup');
			$('.p_darticle3').addClass('rightin').removeClass('rightout').addClass('p_left0');
		}
	});

	$('.p_detailfun4').on('tap',function(){
		if($('.p_detailfun4').hasClass('p_detailfunup')){
				$('.p_detailfun4').removeClass('p_detailfunup');
				$('.p_darticle4').removeClass('rightin').addClass('rightout');
				$('.p_detailfun0').addClass('p_detailfunup');
				$('.p_darticle0').addClass('bottomin').removeClass('bottomout');
		}else{
			if($('.p_darticle0').hasClass('bottomin')){
				$('.p_darticle0').addClass('bottomout').removeClass('bottomin');
			}
			if($('.p_darticle2').hasClass('rightin')){
				$('.p_darticle2').addClass('rightout').removeClass('rightin');
			}
			if($('.p_darticle3').hasClass('rightin')){
				$('.p_darticle3').addClass('rightout').removeClass('rightin');
			}
			if($('.p_darticle1').hasClass('rightin')){
				$('.p_darticle1').addClass('rightout').removeClass('rightin');
			}
			if($('.p_darticle5').hasClass('rightin')){
				$('.p_darticle5').addClass('rightout').removeClass('rightin');
			}
			$('.p_detailfun0').removeClass('p_detailfunup');
			$('.p_detailfun1').removeClass('p_detailfunup');
			$('.p_detailfun2').removeClass('p_detailfunup');
			$('.p_detailfun3').removeClass('p_detailfunup');
			$('.p_detailfun5').removeClass('p_detailfunup');
			$('.p_detailfun4').addClass('p_detailfunup');
			$('.p_darticle4').addClass('rightin').removeClass('rightout').addClass('p_left0');
		}
	});
$('.p_detailfun5').on('tap',function(){
		if($('.p_detailfun5').hasClass('p_detailfunup')){
				$('.p_detailfun5').removeClass('p_detailfunup');
				$('.p_darticle5').removeClass('rightin').addClass('rightout');
				$('.p_detailfun0').addClass('p_detailfunup');
				$('.p_darticle0').addClass('bottomin').removeClass('bottomout');
		}else{
			if($('.p_darticle0').hasClass('bottomin')){
				$('.p_darticle0').addClass('bottomout').removeClass('bottomin');
			}
			if($('.p_darticle2').hasClass('rightin')){
				$('.p_darticle2').addClass('rightout').removeClass('rightin');
			}
			if($('.p_darticle3').hasClass('rightin')){
				$('.p_darticle3').addClass('rightout').removeClass('rightin');
			}
			if($('.p_darticle1').hasClass('rightin')){
				$('.p_darticle1').addClass('rightout').removeClass('rightin');
			}
			if($('.p_darticle4').hasClass('rightin')){
				$('.p_darticle4').addClass('rightout').removeClass('rightin');
			}
			$('.p_detailfun0').removeClass('p_detailfunup');
			$('.p_detailfun1').removeClass('p_detailfunup');
			$('.p_detailfun2').removeClass('p_detailfunup');
			$('.p_detailfun3').removeClass('p_detailfunup');
			$('.p_detailfun4').removeClass('p_detailfunup');
			$('.p_detailfun5').addClass('p_detailfunup');
			$('.p_darticle5').addClass('rightin').removeClass('rightout').addClass('p_left0');
		}
	});

	//咨询
	   $('.p_consultingbtn').on('tap',function(){
			$('.p_rechargead').show().addClass('rightin').removeClass('leftout').removeClass('rightout');
			$('.p_receiptbg').show().addClass('ddin').removeClass('ddout');	
		});
		$('.p_returndd1').on('tap',function(){
			$('.p_rechargead').removeClass('rightin').addClass('rightout');
			$('.p_receiptbg').addClass('ddin').removeClass('ddout');
			$.later(function(){
				$('.p_receiptbg').hide();
			}, 800, false);		
		});
	 //商品咨询
	if($('#p_zxscroller').length > 0){
	    var zxscroller = $('#p_zxscroller'),
			zxscroll = new iScroll('p_zxscroller',{onBeforeScrollStart:function(){zxscroller.iScroll('refresh');}});
	}

	//图片大小
	$('.p_indexbanner').css("height",window.innerWidth - 410+'px');


})