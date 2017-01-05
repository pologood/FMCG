$(function() {
	var $getCode = $("#getCode");
	var ii;
	var count = 60;
	function refreshTime() {
		count = count - 1;
		if (count == 0) {
			count = 60;
			$getCode.prop('disabled', false);
			$getCode.html("点击获取");
			clearInterval(ii);
			return false;
		}
		$getCode.html(count + "秒后重新获取");
	}

	$getCode.on("click", function() {
		$.ajax({
			url : "${base}/wap/member/send_captcha.jhtml",
			dataType : "json",
			type : "post",
			success : function(data) {
				if (data.type == 'success') {
					invokTips("success", "发送成功");
					ii = setInterval(refreshTime, 1000);
					return;
				} else {
					invokTips(data.type, data.content);
				}
			}
		});
	});
	wapScroll("wrapper");
	$('[data-toggle="modalhelper"]').on("click", function(h) {
		var t = $(this).attr("data-type");
		var data = $(this).attr("data-options");
		var g = $(this).attr("data-title");
		var e = $("#modal-helper");
		e.find(".am-popup-title").text(g);
		e.find(".am-popup-bd").text("");
		Handlebars.registerHelper("expression", function() {
			var exps = [];
			try {
				// 最后一个参数作为展示内容，也就是平时的options。不作为逻辑表达式部分
				var arg_len = arguments.length;
				var len = arg_len - 1;
				for (var j = 0; j < len; j++) {
					exps.push(arguments[j]);
				}
				var result = eval(exps.join(' '));
				if (result) {
					return arguments[len].fn(this);
				} else {
					return arguments[len].inverse(this);
				}
			} catch (e) {
				throw new Error('Handlerbars Helper “expression” can not deal with wrong expression:' + exps.join(' ') + ".");
			}
		});
		compiler = Handlebars.compile($("#wap-member-" + t).html());
		e.find(".am-popup-bd").html(compiler(eval('(' + data + ')')));
		var rules = {};
		var messages = {};
		if (t == "info-editpassword" || t == "info-editpaypwd") {
			rules = {
				oldPass : "required",
				type : "required",
				newPass : "required",
				reNewPass : {
					required : true,
					equalTo : "#newPass"
				}
			};
			messages = {
				oldPass : "原始密码必填",
				newPass : "新密码必填",
				reNewPass : {
					required : "",
					equalTo : "两次输入密码不一致"
				}
			};
			$("#submitForm").validate({
				rules : rules,
				messages : messages,
				submitHandler : function() {
					$.ajax({
						url : "${base}/common/public_key.jhtml",
						type : "GET",
						data : {
							local : true
						},
						dataType : "json",
						cache : false,
						success : function(data1) {
							var rsaKey = new RSAKey();
							rsaKey.setPublic(b64tohex(data1.modulus), b64tohex(data1.exponent));
							$("#newPass").val(hex2b64(rsaKey.encrypt($("#newPass").val())));
							$("#oldPass").val(hex2b64(rsaKey.encrypt($("#oldPass").val())));
							$("#submitForm").ajaxSubmit(function(message) {
								invokTips(message.type, message.content);
								setTimeout(function() {
									location.reload(true);
								}, 1000);
							});
						}
					});
				}
			});
		} else if (t == "info-findpaypwd") {
			rules = {
				securityCode : "required",
				newPass : "required",
				reNewPass : {
					required : true,
					equalTo : "#newPass"
				}
			};
			messages = {
				securityCode : "验证码必填",
				newPass : "新密码必填",
				reNewPass : {
					required : "",
					equalTo : "两次输入密码不一致"
				}
			};
			$("#submitForm").validate({
				rules : rules,
				messages : messages,
				submitHandler : function() {
					$.ajax({
						url : "${base}/common/public_key.jhtml",
						type : "GET",
						data : {
							local : true
						},
						dataType : "json",
						cache : false,
						success : function(data1) {
							var rsaKey = new RSAKey();
							rsaKey.setPublic(b64tohex(data1.modulus), b64tohex(data1.exponent));
							$("#newPass").val(hex2b64(rsaKey.encrypt($("#newPass").val())));
							$("#submitForm").ajaxSubmit(function(message) {
								invokTips(message.type, message.content);
								setTimeout(function() {
									location.reload(true);
								}, 1000);
							});
						}
					});
				}
			});
		} else {
			if (t == "info-area") {
				var $areaId = $("#areaId");
				$areaId.lSelect({
					url : "${base}/common/area.jhtml"
				});
				rules = {
					areaId : "required",
					type : "required"
				};
				messages = {
					areaId : "区域必填"
				}
			} else if (t == "info-name") {
				rules = {
					name : "required",
					type : "required"
				};
				messages = {
					name : "姓名必填"
				}
			} else if (t == "info-auth") {
			} else if (t == "info-sex") {
				rules = {
					sex : "required",
					type : "required"
				};
				messages = {
					name : "性别必填"
				}
			}
			$("#submitForm").validate({
				rules : rules,
				messages : messages,
				submitHandler : function() {
					$("#submitForm").ajaxSubmit(function(message) {
						invokTips(message.type, message.content);
						setTimeout(function() {
							location.reload(true);
						}, 1000);
					});
				}
			});
		}

		e.modal("open");
		return false;
	});

	var $selectImg = $("#selectImg");
	$selectImg.on('click', function() {
		$("#headImg").click();
	});

});
function changeHeadImg(file, id, img) {
	preivew(file, img);
	if ($("#" + id).val().length > 0) {
		$("#headForm").submit();
	}
}