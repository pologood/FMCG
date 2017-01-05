/**
 * jQuery InputBox - A Custom Select/Checkbox/Radio Plugin
 * ------------------------------------------------------------------
 *
 * jQuery InputBox is a plugin for custom Select/Checkbox/Radio Plugin.
 *
 * @version        1.0.0
 * @since          2013.09.23
 * @author         charles.yu
 *
 * ------------------------------------------------------------------
 *
 *  eg:
 *	1、Select
 *  //class: selected 表示默认选中,如有多个selected默认选中第一个
 *  <div name="city" type="selectbox">
 *		<div class="opts">
 *			<a href="javascript:;" val="050101">苏州</a>
 *			<a href="javascript:;" val="050101" class="selected">无锡</a>
 *			<a href="javascript:;" val="050101" class="selected">常州</a>
 *		</div>
 *	</div>
 *  $('div[name="city"]').inputbox();
 *
 *	2、Checkbox
 *	//class: all 表示是全选|全不选按钮，如果是全选按钮，则其"name"为控制对象的"name+All"; checked 表示默认选中
 *	<div class="cbt all" name="checkAll" type="checkbox"></div>
 *  <div class="cbt checked" name="check"  type="checkbox" val="1" ></div>
 *	<div class="cbt checked" name="check"  type="checkbox" val="2" ></div>
 *	<div class="cbt" name="check"  type="checkbox" val="3"></div>
 *  $('.cbt').inputbox();
 *
 *	3、Radio
 *  //class: checked 表示默认选中，如有多个selected默认选中最后一个
 *	<div name="rbt checked" type="radiobox" val="cn"></div><span>中文</span>
 *	<div name="rbt" type="radiobox" val="en"></div><span>英文</span>
 *	$('div[name="rbt"]').inputbox();
 */
;(function($){

	var checkbox = {
		//初始化checkbox
		init: function(o) {
			var $o = $(o),
				_name = $o.attr('name'),
				_value = $o.attr('val')? $o.attr('val'): '',
				_isChecked = $o.hasClass('checked')? true : false;

			$o.addClass('cb');
			$o.append($('<input type="hidden" name="' + _name + '" value="' + _value + '" />'));
			$o.click(checkbox.toggle);
			if($o.hasClass('all')) $o.click(checkbox.allOrNone);
			if(_isChecked){
				$o.removeClass('checked');
				$o.click();
			}
		},
		toggle: function(e) {
			$(this).toggleClass('cb_active').children().attr('checked', ($(this).hasClass('cb_active') ? true : false));
		},
		allOrNone: function(e) {
			var cbAllName = $(this).attr('name');
			if(cbAllName.length > 3){
				var cbOneName = cbAllName.substring(0, cbAllName.length - 3);
				var isChecked = $(this).hasClass('cb_active')? true : false;
				if(isChecked){
					$('.cb[name='+ cbOneName +']').not($('.cb_active[name='+ cbOneName +']')).click();
				}else{
					$('.cb_active[name='+ cbOneName +']').click();
				}
			}
		}
	};

	var radiobox = {
		//初始化radiobox
		init: function(o) {
			var $o = $(o),
				_name = $o.attr('name'),
				_value = $o.attr('val')? $o.attr('val'): '';
				_isChecked = $o.hasClass('checked')? true : false;

			$o.addClass('rb');
			$o.append($('<input type="hidden" name="' + _name + '" value="' + _value + '" />'));
			$o.click(radiobox.toggle);
			if(_isChecked){
				$o.removeClass('checked');
				$o.click();
			}
		},
		toggle: function() {
			var $o = $(this),
				_name = $o.attr('name');

			$('[name="'+ _name +'"]').removeClass('rb_active').children().attr('checked', false);
			$o.addClass('rb_active').children().attr('checked', true);
		}
	},

	_init = function(o){
		var type = $(o).attr('type');
		if(type == 'selectbox'){
			selectbox.init(o);
		}else if(type == 'checkbox'){
			checkbox.init(o);
		}else if(type == 'radiobox'){
			radiobox.init(o);
		}
	};

	$.fn.inputbox = function(options){
        opts = $.extend({}, $.fn.inputbox.defaults, options);

        return this.each(function(){
            _init(this);
        });
    };

    $.fn.inputbox.defaults = {
		//type: 'selectbox',//selectbox|checkbox|radiobox
		width : 'auto',
		height : 24
    };
})(jQuery);
