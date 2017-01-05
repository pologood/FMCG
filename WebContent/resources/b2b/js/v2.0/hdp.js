//<![CDATA[
$(function(){
	(function(){
		var curr = 0;
		var iColorVal = ["#c30101","#c1c1c1","#83c1b1","#e53444","#1c70ac"];
		$("#jsNav .trigger").each(function(i){
			$(this).click(function(){
				curr = i;
				$("#js img").eq(i).fadeIn("slow").parent("a").siblings("a").children("img").hide();
				$(this).siblings(".trigger").removeClass("imgSelected").end().addClass("imgSelected");
				$(".login_index_div").css("background",iColorVal[i]);
				return false;
			});
		});
		
		var pg = function(flag){
			//flag:true��ʾǰ���� false��ʾ��
			if (flag) {
				if (curr == 0) {
					todo = 2;
				} else {
					todo = (curr - 1) % 5;//3
				}
			} else {
				todo = (curr + 1) % 5;//3
			}
			$("#jsNav .trigger").eq(todo).click();
		};
		
		//ǰ��
		$("#prev").click(function(){
			pg(true);
			return false;
		});
		
		//��
		$("#next").click(function(){
			pg(false);
			return false;
		});
		
		//�Զ���
		var timer = setInterval(function(){
			todo = (curr + 1) % 5;//3
			$("#jsNav .trigger").eq(todo).click();
		},5000);
		
		//�����ͣ�ڴ�������ʱֹͣ�Զ���
		$("#jsNav a").hover(function(){
				clearInterval(timer);
			},
			function(){
				timer = setInterval(function(){
					todo = (curr + 1) % 5;//3
					$("#jsNav .trigger").eq(todo).click();
				},5500);			
			}
		);
	})();
});
//]]>

//����ͼ�� www.lanrentuku.com

