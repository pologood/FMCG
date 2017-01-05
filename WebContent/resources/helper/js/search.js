// 加入收藏 兼容360和IE6
function shoucang(sTitle,sURL)
{
try
{
window.external.addFavorite(sURL,sTitle);
}
catch (e)
{
try
{
window.sidebar.addPanel(sTitle, sURL, "");
}
catch (e)
{
alert("加入收藏失败，请使用Ctrl+D进行添加");
}
}
} 

function getBrandTags(obj)
{
	var str = '';
	var str2 = '';
	var str3 = '';
	var iNt01 = {};
	var iNt02 = {};
	var myDiv='';
	//所有后台字母遍历并储存
	for (var n in brandPhonetic)
	{
		str+="<li>"+brandPhonetic[n].toLocaleUpperCase()+"</li>";
		myDiv += '<div phonetic="'+brandPhonetic[n].toLocaleUpperCase()+'" class="car_con"></div>';
	}
	//创建DIV，并追加给tabbranddiv
	$("#TabBrandDiv").html(myDiv);
	//遍历所有热门品牌
	for (var t in brands)
	{
		iNt01 = brands[t].tags.match(",24,");
		iNt02 = brands[t].tags.match(","+$(obj).attr("tagid")+",");
		if (iNt01 && iNt02 != null)
		{
			str2+='<a href="javascript:;"><img src="'+brands[t].logo+'" brandId="'+brands[t].id+'" alt="'+brands[t].name+'"></a>';
		}
		
	}
	//匹配乘用或商用车型相应的字母开头
	for (var i=0;i<brandPhonetic.length;i++)
	{
		for (var j = 0;j<brands.length;j++)
		{
			if (brandPhonetic[i].toLocaleUpperCase() == brands[j].phonetic.toLocaleUpperCase() && brands[j].tags.match(","+$(obj).attr("tagid")+",") )			{
				for (var r=0;r<$("#TabBrandDiv div").length;r++)
				{
					if ($("#TabBrandDiv div").eq(r).attr("phonetic") == brands[j].phonetic.toLocaleUpperCase())
					{
						$("#TabBrandDiv div").eq(r).append('<a href="javascript:;"><img src="'+brands[j].logo+'" brandId="'+brands[j].id+'" alt="'+brands[j].name+'"></a>');
					}
				}
			}
		}
	}
	$(obj).addClass("btn_bg3").siblings(".btn_bg4").removeClass("btn_bg3");
	$("#TabBrandBtn").html(str);
	$("#TabBrandBtn li").eq(0).css({background:"#AE1E23",color:"#fff"});
	$("#TabBrandDiv div").eq(0).html(str2);
	$(".car_con").eq(0).css("display","block");
	$("#TabBrandBtn li").bind('click',tabBrand);
	$("#TabBrandDiv img").bind('click',function(){
		var _this = this;
		getBrandAlt(_this);
	});
}
function tabBrand()
{
	$(this).css({background:"#AE1E23",color:"#fff"}).siblings("li").css({background:"#fff",color:"#555"});
	for (var i=0;i<$(".car_con").length;i++)
	{
		if ($(this).html() == $(".car_con").eq(i).attr("phonetic"))
		{
			$(".car_con").eq(i).show().siblings(".car_con").hide();
		}else {
			$(".car_con").eq(i).hide();		
		}
	}
}

function getBrandAlt(ithis)
{
	var id=$(ithis).attr("brandId");
	var name=$(ithis).attr("alt");
	if ($(".car_title").attr("trueid") == "headerSerachName")
	{
		$("#"+$(".car_title").attr("trueid")).text(name);
		$("#headerBrandId").val(id);
		$(".car_title").attr("trueid","");
	}else {
		$("#brandId").val(id);
		$("#brandSeriesId").val("");
		$("#pageNumber").val(1);
		$("#brandName").text(name);
		$("#listForm").submit();
	}
	$("#layer_div,#contentid").hide();
}
//清除所有品牌
function deleteAllBrand()
{
	$("#layer_div,#contentid").hide();
	
	if ($(".car_title").attr("trueid") == "headerSerachName")
	{
		$("#headerSerachName").text("全部品牌");	
		$("#brandId").val(0);
		$("#headerBrandId").val(0);
		$(".car_title").attr("trueid","");
	}else {
		$("#brandName").text("全部品牌");
		$("#brandId").val(1);
		$("#headerBrandId").val(1);
		$("#brandSeriesId").val("");
		$("#pageNumber").val(1);
		$("#listForm").submit();
	}
}
//点击显示隐藏层
function tabLayer(myId,iVal,othis)
{
	document.getElementById(myId).style.display = iVal;//显示隐藏弹出窗
	document.getElementById("layer_div").style.display = iVal;//显示隐藏弹出窗
	if (iVal=='block') {
		initBrand();
		$(".car_title").attr("trueid",$(othis).attr('id'));
		var html="";
		var idx=0;
		//动态获取后台乘用车，商用车的默认值
		for (var k in brandTags)
		{   if (brandTags[k].id!='24') {
			  if (idx==0) {
				 html = '<input type="button" tagid="'+brandTags[k].id+'" onClick="getBrandTags(this)" value="'+brandTags[k].name+'" class="btn_bg4 btn_bg3 f_left clear" />';
			  } else {
				 html = html+'<input type="button" tagid="'+brandTags[k].id+'" onClick="getBrandTags(this)" value="'+brandTags[k].name+'" class="btn_bg4 f_left" />';
			  }
			  idx++;
		    }
		}
		$("#brand_panel").html(html);
		getBrandTags('.btn_bg3');
	}else {
		$(".car_title").attr("trueid","");
	}
}
//点击隐藏认证弹出窗
function authenLayer(obj,ival)
{
	document.getElementById("authen_layer_div").style.display = ival;//显示隐藏弹出窗
	$("#"+obj).css("display",ival);//显示隐藏弹出窗
}
function productTab(obj){
	$(obj).css("color","red").parent().siblings("li").children("a").css("color","#000");//移入鼠标，当前字体颜色变红，其他变黑
	$(".car_con").eq($(obj).parent().index()).show().siblings(".car_con").hide();//获取index值控制DIV对应显示
}

$(function(){
	//搜索引擎，商品跟商家输入框切换
	$(".jq_get_ul h4").click(function(){
		$(this).addClass("select_btn02").siblings("h4").removeClass("select_btn02");
		$(".search_input_div").eq($(this).index()).show().siblings(".search_input_div").hide();
		$(".search_input_div").eq($(this).index()).children("input").eq(0).val("");
		$("#headerSerachName").text("选择车型");
		$("#headerBrandId").val("");
		/*if (navigator.appName == "Microsoft Internet Explorer" && document.documentMode < 10)
		{
			$("#product").val("请输入商品名称、件号、分类或适用车型等").css({color:"#999"});
			$("#tenant").val("请输入商家名称、所在地或主营范围等").css({color:"#999"});
		}*/
	});
	/*if (navigator.appName == "Microsoft Internet Explorer")
	{
		$("#product").val("请输入商品名称、件号、分类或适用车型等");
		$("#tenant").val("请输入商家名称、所在地或主营范围等");
	}
	$(".enter_input01").focus(function(){
		if (navigator.appName == "Microsoft Internet Explorer")
		{
			if ($(this).val() == $(this).attr("placeholder"))
			{
				$(this).val("");
			}
		}
	});
	$(".enter_input01").blur(function(){
		if (navigator.appName == "Microsoft Internet Explorer")
		{
			if ($(this).val() == $(this).attr("placeholder") || $(this).val() == "")
			{
				$(this).val($(this).attr("placeholder")).css({color:"#999"});
			}
		}
	});*/
	$(".prodUl li").click(function(){
		$(this).addClass("cur").siblings("li").removeClass("cur");
		$(".showSearchDiv").eq($(this).index()).show().siblings(".showSearchDiv").hide();
	});
	//清除搜索商品框默认值
	$.extend({
		jqFocus:function(id,str)
			{
				if ($(id).val() == str)
				{
					$(id).val("");
				}
			},
		jqBlur:function(id,str)
			{
				if ($.trim($(id).val()) == "")
				{
					$(id).val(str);
				}
			},
		jqPassFocus:function(id,id2,str)
			{
				if ($(id).val() == str)
				{
					$(id).hide();
					$(id2).show().focus();
				}
			},
		jqPassBlur:function(id,id2,str)
			{
				if ($(id).val() == "")
				{
					$(id).hide();
					$(id2).show().val(str);
				}
			}
	});
	/*头部公共导航显示，隐藏*/
	$(".two_nav_ul>li").mouseover(function(){
		$(this).addClass("active01").siblings("li").removeClass("active01");
		var iHei = $(".two_nav_ul>li").length;
		var iLen = $(this).children("ul").children("li").length;

		if (iHei >= iLen)
		{
			$(this).children("ul").css("height",iHei*30+"px");
		}else {
			$(this).children("ul").css("height",iLen*30+"px");
		}
	});
	$(".three_nav_ul>li").hover(function(){
		$(this).children("a").addClass("active02").parent("li").siblings("li").children("a").removeClass("active02");
		
		var iHei = $(this).parent("ul").height();
		var iLen = $(this).children("ul").height();;
		if (iHei >= iLen)
		{
			$(this).children("ul").css({height:iHei-6+"px"});
		}else {
			$(this).children("ul").css({height:iLen+"px"});
		}
	},function(){
			$(".three_nav_ul>li").children("a").removeClass("active02");
		});
	$(".public_nav_dl>dt h1,.shop_nav_dl>dt h1").mouseover(function(){
			$(".two_nav_ul>li,.shop_two_nav_ul>li").removeClass("active01");
			$(".three_nav_ul>li,.shop_three_nav_ul>li").removeClass("active02");
			
		});
	for (var k=0;k<$(".two_nav_ul>li").length; k++)
	{
		$(".two_nav_ul>li").eq(k).children("span").attr("class","incon_span0"+(k+1));
	}
	$(".car_bg").css("left",Math.round(($(window).width()-812)/2)+"px");
	$(".authen_busi_div").css("left",Math.round(($(window).width()-680)/2)+"px");
	//我的云仓全部商品搜索
	$(".oneUl>li").mouseover(function(){
		var $oneUl = $(".oneUl");
		var $twoUl = $(this).children(".twoUl");
		if ($oneUl.children("li").length > $twoUl.children("li").length){
			$twoUl.css("height",$oneUl.children("li").length*30+"px");
		};
	});
	$(".twoUl>li").mouseover(function(){
		var $twoUl = $(".twoUl");
		var $threeUl = $(this).children(".threeUl");
		if ($twoUl.height() > $threeUl.height()){
			$threeUl.css("height",($twoUl.height()-2)+"px");
		}
	});
	//控制浏览器变动，控制弹出层的位置
	$(window).resize(function(){
		if ($(window).width() > 980)
		{
			var iLeftVal = Math.round(($(window).width()-812)/2);
			var iLeftVal02 = Math.round(($(window).width()-$(".authen_busi_div").height())/2);
			var iLeftVal02 = Math.round(($(window).width()-680)/2);
			var iTopval = Math.round(($(window).height()-520)/2);
			var iRightVal = Math.round(($(window).width()-980)/2);
			$(".car_bg").css("left",iLeftVal+"px");
			$(".authen_busi_div").css("left",iLeftVal02+"px");
			if (iTopval > 0)
			{
				$(".car_bg,.authen_busi_div").css("top",iTopval+"px");
			}else {
				$(".car_bg,.authen_busi_div").css("top",0);	
			}
			$(".float_car_div").css("right",iRightVal-42+"px");
		}else {
			$(".car_bg,.authen_busi_div").css("left",0);
			$(".car_bg,.authen_busi_div").css("top",0);
			$(".float_car_div").css("right",0);
		}
	})
	if ($(window).width() > 980)
		{
			$(".float_car_div").css("right",Math.round(($(window).width()-980)/2)-42+"px");
		}else {
			$(".float_car_div").css("right",0);
		}
});