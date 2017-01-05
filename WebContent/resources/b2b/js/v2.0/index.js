function updateCss(cssid){
	document.getElementById(cssid).style.color = "#9c9fa4";
}
function updateCss2(){
	var strCss = document.getElementById("cssId").value;
	if (strCss != "" && strCss == "HS±àÂë")
	{
		document.getElementById("cssId").style.color = "#9c9fa4";
	}
}
function updateCss1(cssid){
	document.getElementById(cssid).style.color = "#333";
	var strCss = document.getElementById("cssId").value;
	if(strCss != "" && strCss == "HS±àÂë") document.getElementById(cssid).value = "";
}

function is_number1(e)
{
	var strCss = document.getElementById("cssId").value;
	if (strCss == "")
	{
		document.getElementById("cssId").style.color = "#9c9fa4";
		document.getElementById("cssId").value = "HS±àÂë";
	}
}

function is_number2(e)
{
	var strCss = document.getElementById("cssId").value;
	if (strCss != "" && strCss == "HS±àÂë")
	{
		document.getElementById("cssId").style.color = "#9c9fa4";
		document.getElementById("cssId").value = "";
	}
}

function openwin(url) {
	window.open (url, "newwindow", "height=400, width=400, resizable=yes,toolbar=no, menubar=no, scrollbars=yes");
}

