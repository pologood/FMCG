//高度自适应

$(function() {
	var isResizing;
	var setContainerWH = function() {
		if (!isResizing) {
			isResizing = true;
			var ww = $(window).width();
			var wh = $(window).height();

			$(".window").width(ww).height(wh);			

			isResizing = false;
		}
	}
	$(window).bind('resize', setContainerWH);
	setContainerWH();
})
