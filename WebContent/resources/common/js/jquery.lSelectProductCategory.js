/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.wit.net
 * License: http://www.wit.net/license
 * 
 * JavaScript - lSelect
 * Version: 3.0
 */

(function($) {
	$.fn.extend({
		lSelectProductCategory: function(options) {
			var settings = {
				//choose: "请选择...",
				emptyValue: "",
				cssStyle: {"margin-right": "4px","width": "223px","height":"250px","background":"none"},
				url: null,
				type: "GET"
			};
			$.extend(settings, options);
			
			var cache = {};
			return this.each(function() {
				var $input = $(this);
				var id = $input.val();
				var treePath = $input.attr("treePath");
				var selectName = $input.attr("name") + "_select";
				if (treePath != null && treePath != "") {
					var ids = (treePath + id + ",").split(",");
					var $position = $input;
					for (var i = 1; i < ids.length; i ++) {
						$position = addSelect($position, ids[i - 1], ids[i]);
					}
				} else {
					addSelect($input, null, null);
				}
				
				function addSelect($position, parentId, currentId) {
					$position.nextAll().remove();
					if ($position.is("select") && (parentId == null || parentId == "")) {
						return false;
					}
					if (cache[parentId] == null) {
						$.ajax({
							url: settings.url,
							type: settings.type,
							data: {
								parentId: parentId != null ? parentId : null
							},
							dataType: "json",
							cache: false,
							async: false,
							success: function(data) {
								cache[parentId] = data;
							}
						});
					}
					var data = cache[parentId];
					if ($.isEmptyObject(data)) {
						return false;
					}
					var select_index=$(".category_select").size();
					var search_div='<div';
					if(select_index==1){
						search_div+=' style="position: relative;top: -276px;left: 227px;"';
					}
					if(select_index==2){
						search_div+=' style="position: relative;top: -552px;left: 454px;"';
					}
					search_div+='><input id="search_text'+select_index+'" class="text search_text" type="text" style="width: 157px;"/><input id="search_btn'+select_index+'" type="button" class="button" value="搜索"/></div>';
					var select = '<select';
					if(select_index==1){
						select+=' style="position: relative;top: -276px;left: 227px;"';
					}
					if(select_index==2){
						select+=' style="position: relative;top: -552px;left: 454px;"';
					}
					select+=' size="12" name="' + selectName + '" id="category_select'+select_index+'" class="category_select">';
					if (settings.emptyValue != null && settings.choose != null) {
						select += '<option value="' + settings.emptyValue + '">' + settings.choose + '</option>';
					}
					$.each(data, function(value, name) {
						if(value == currentId) {
							select += '<option value="' + value + '" selected="selected">' + name + '</option>';
						} else {
							select += '<option value="' + value + '">' + name + '</option>';
						}
					});
					select += '</select>';
					return $(select).css(settings.cssStyle).insertAfter($(search_div).insertAfter($position)).bind("click", function() {
						var $this = $(this);
						if ($this.val() == "") {
							var $prev = $this.prev("select[name=" + selectName + "]");
							if ($prev.size() > 0) {
								$input.val($prev.val());
							} else {
								$input.val(settings.emptyValue);
							}
						} else {
							$input.val($this.val());
						}
						addSelect($this, $this.val(), null);
						options.fn();
					});
				}

			});
			
		}
	});
})(jQuery);