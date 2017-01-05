/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.wit.net
 * License: http://www.wit.net/license
 * 
 * JavaScript - Input
 * Version: 3.0
 */

$().ready( function() {

	if ($.tools != null) {
		var $tab = $("#tab");
		var $title = $("#inputForm :input[title], #inputForm label[title]");

		// Tab效果
		$tab.tabs("table.tabContent, div.tabContent", {
			tabs: "input"
		});
	
		// 表单提示
		$title.tooltip({
			position: "center right",
			offset: [0, 4],
			effect: "fade"
		});
	}

	// 验证消息
	if($.validator != null) {
		$.extend($.validator.messages, {
		    required: message("b2b.validate.required"),
			email: message("b2b.validate.email"),
			url: message("b2b.validate.url"),
			date: message("b2b.validate.date"),
			dateISO: message("b2b.validate.dateISO"),
			pointcard: message("b2b.validate.pointcard"),
			number: message("b2b.validate.number"),
			digits: message("b2b.validate.digits"),
			minlength: $.validator.format(message("b2b.validate.minlength")),
			maxlength: $.validator.format(message("b2b.validate.maxlength")),
			rangelength: $.validator.format(message("b2b.validate.rangelength")),
			min: $.validator.format(message("b2b.validate.min")),
			max: $.validator.format(message("b2b.validate.max")),
			range: $.validator.format(message("b2b.validate.range")),
			accept: message("b2b.validate.accept"),
			equalTo: message("b2b.validate.equalTo"),
			remote: message("b2b.validate.remote"),
			integer: message("b2b.validate.integer"),
			positive: message("b2b.validate.positive"),
			negative: message("b2b.validate.negative"),
			decimal: message("b2b.validate.decimal"),
			pattern: message("b2b.validate.pattern"),
			extension: message("b2b.validate.extension")
		});
		
		$.validator.setDefaults({
			errorClass: "fieldError",
			ignore: ".ignore",
			ignoreTitle: true,
			errorPlacement: function(error, element) {
				var fieldSet = element.closest("span.fieldSet");
				if (fieldSet.size() > 0) {
					error.appendTo(fieldSet);
				} else {
					error.insertAfter(element);
				}
			},
			submitHandler: function(form) {
				$(form).find(":submit").prop("disabled", true);
				form.submit();
			}
		});
	}

});

// 编辑器
if(typeof(KindEditor) != "undefined") {
	KindEditor.ready(function(K) {
		editor = K.create("#editor", {
			height: "350px",
			items: [
				"source", "|", "undo", "redo", "|", "preview", "print", "template", "cut", "copy", "paste",
				"plainpaste", "wordpaste", "|", "justifyleft", "justifycenter", "justifyright",
				"justifyfull", "insertorderedlist", "insertunorderedlist", "indent", "outdent", "subscript",
				"superscript", "clearhtml", "quickformat", "selectall", "|", "fullscreen", "/",
				"formatblock", "fontname", "fontsize", "|", "forecolor", "hilitecolor", "bold",
				"italic", "underline", "strikethrough", "lineheight", "removeformat", "|", "image",
				"flash", "media", "insertfile", "table", "hr", "emoticons", "baidumap", "pagebreak",
				"anchor", "link", "unlink"
			],
			langType: wit.locale,
			syncType: "form",
			filterMode: false,
			pagebreakHtml: '<hr class="pageBreak" \/>',
			allowFileManager: true,
			filePostName: "file",
			fileManagerJson: wit.base + "/file/browser.jhtml",
			uploadJson: wit.base + "/file/upload.jhtml",
			uploadImageExtension: setting.uploadImageExtension,
			uploadFlashExtension: setting.uploadFlashExtension,
			uploadMediaExtension: setting.uploadMediaExtension,
			uploadFileExtension: setting.uploadFileExtension,
			extraFileUploadParams: {
				token: getCookie("token")
			},
			afterChange: function() {
				this.sync();
			}
		});
	});
}