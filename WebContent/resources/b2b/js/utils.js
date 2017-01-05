// JavaScript Document
function getToday()
{
    var todaysDate = new Date();
    var today = todaysDate.getDate() + "��";
    var dayArray = new Array("������","����һ","���ڶ�","������","������","������","������");
    var monthArray = new Array("1��","2��","3��","4��","5��","6��","7��","8��","9��","10��","11��","12��");
    var thisYear;
    if (todaysDate.getYear() < 1000)
    {
	    thisYear = todaysDate.getYear() + 1900;
    }
    else
    {
	    thisYear = todaysDate.getYear();
    }
    thisYear = thisYear + "��";
    document.write(thisYear + monthArray[todaysDate.getMonth()] + today + " " + dayArray[todaysDate.getDay()]);
}

//��������ѡ��Ŀؼ�
function FindDate(strInputBoxName)
{
  var strContralName;
	strContralName = strInputBoxName;
	intLeft=window.event.screenX+window.event.offsetX-window.event.srcElement.offsetWidth+3;
	intTop=window.event.screenY-window.event.offsetY-2;
	var myObject = new Object();
	myObject = eval(strInputBoxName);
	window.showModalDialog("../include/calendar.html",myObject,"dialogHeight: '107.5pt'; dialogWidth: '142.5pt'; dialogTop: "+intTop.toString()+"px; dialogLeft: "+intLeft.toString()+"px; edge: Raised; center: no; help: No; resizable: No; status: No;scrolling:NO");
}

function gotoUrl(strUrl){
	window.location.href = strUrl;
}



//�ύForm����֤��������������������������
function isLeapYear(year)
{
   if((year%4==0&&year%100!=0)||(year%400==0))
   {
      return true;
   }

   return false;
}

function chkFloat(strValue)
{

	if( strValue == "")
	{
	    return false;
	}

	if( isNaN(strValue) )
	{
		return false;
	}

	return true;
}

function dateValidation(value, errMsg)
{
	var regexp,Format;
	var year,month,day;
	var iyear,imonth,iday;
	var dateArray;

	format="yyyy-mm-dd";
	regexp=/^([0-9]{4})-([0-9]{1,2})-([0-9]{1,2})$/;

	iyear=1;
	imonth=2;
	iday=3;

	if(regexp.test(value))
	{
		dateArray=value.match(regexp);

		year=dateArray[iyear];
		month=dateArray[imonth];
		day=dateArray[iday];

		if(month<1||month>12)
		{
			alert(errMsg+"���·�Խ�ޣ��·ݱ�����1��12֮�䡣");
			return false;
		}

		if(day<1||day>31)
		{
			alert(errMsg+"������Խ�ޣ����ӱ�����1��31֮�䡣");
			return false;
		}
		else
		{
			if(month==2)
			{
				if(isLeapYear(year)&&day>29)
				{
					alert(errMsg+"������Խ�ޣ�����2�·�ֻ��29�졣");
					return false;
				}

				if(!isLeapYear(year)&&day>28)
				{
					alert(errMsg+"������Խ�ޣ�ƽ��2�·�ֻ��28�졣");
					return false;
				}
			}

			if((month==4||month==6||month==9||month==11)&&(day>30))
			{
				alert(errMsg+"������Խ�ޣ�С��ֻ��30�졣");
				return false;
			}
		}
	}
	else
	{
		alert(errMsg+"�ĸ�ʽ������ȷ�ĸ�ʽ��YYYY-MM-DD��");
		return false;
	}

	return true;
}

function timeValidation(value, errMsg)
{
	var regexp,Format;
	var hour,minute,secend;
	var ihour,iminute,isecend;
	var timeArray;

	format="hh:mm:ss";
	regexp=/^([0-9]{1,2}):([0-9]{1,2}):([0-9]{1,2})$/;

	ihour = 1;
	iminute = 2;
	isecend = 3;

	if(regexp.test(value)){
		timeArray=value.match(regexp);

		hour=timeArray[ihour];
		minute=timeArray[iminute];
		secend=timeArray[isecend];

		if(hour<0||hour>23)
		{
			alert(errMsg+"��СʱԽ�ޣ�Сʱ������0��23֮�䡣");
		 	return false;
		}

		if(minute<0||minute>59)
		{
			alert(errMsg+"�ķ���Խ�ޣ����ӱ�����0��59֮�䡣");
		 	return false;
		}

		if(secend<0||secend>59)
		{
			alert(errMsg+"����Խ�ޣ��������0��59֮�䡣");
		 	return false;
		}
	}
	else
	{
		alert(errMsg+"�ĸ�ʽ������ȷ�ĸ�ʽ��HH:MM:SS��");
		return false;
	}
	return true;
}
//����Ƿ�Ϊ����
/*
function isNumber(value)
{
	var str=value;
	for(var i=0;i<str.length;i++)
	{
		var code=str.charCodeAt(i);
		if(code<45 || (code==45 && i!=0) || code==47 || code>57 || (code==46 && i==0) || (code==46&& i==(str.length-1)))
		{
			return false;
		}
	}
	return true;
}
*/
function isValidString(elementObj, errMsg)
{
	var value = elementObj.value;
	var cx_canbenull = elementObj.cx_canbenull;
	var cx_min = elementObj.cx_min;
	var cx_max = elementObj.cx_max;
	var errMsg = "��"+elementObj.cx_errormsg+"��";

	if( value == "")
	{
		if( cx_canbenull == "no" )
		{
			alert(errMsg + "�Ǳ����������Ϣ��");
			return false;
		}
		else
		{
			return true;
		}
	}

	if( cx_min != null && cx_max != null )
	{
		if( value.length >= cx_min && value.length <= cx_max )
		{
			return true;
		}
		else
		{
			alert(errMsg+"�ĳ���Խ�ޣ�"+ errMsg +"�ĳ��ȱ�����"+ cx_min +"~"+ cx_max +"֮�䡣");
			return false;
		}
	}
	else if( cx_min != null )
	{
		if( value.length < cx_min )
		{
			alert(errMsg+"�ĳ���Խ�ޣ�"+ errMsg +"�ĳ�������Ҫ"+ cx_min +"λ����");
			return false;
		}
		else
		{
			return true;
		}
	}
	else if(cx_max != null)
	{
		if( value.length > cx_max )
		{
			alert(errMsg+"�ĳ���Խ�ޣ�"+ errMsg +"�ĳ������Ϊ"+ cx_max +"λ����");
			return false;
		}
		else
		{
			return true;
		}
	}

  return true;
}

function isValidInteger(elementObj)
{
	var value = elementObj.value;
	var cx_canbenull = elementObj.cx_canbenull;
	var cx_min = elementObj.cx_min;
	var cx_max = elementObj.cx_max;
	var errMsg = "��"+elementObj.cx_errormsg+"��";

	if( value == "")
	{
		if( cx_canbenull == "no" )
		{
			alert(errMsg + "�Ǳ����������Ϣ��");
			return false;
		}
		else
		{
			return true;
		}
	}

	if( !isNumber(value) )
	{
		alert(errMsg + "�������������֡�");
		return false;
	}

	value = parseInt(value, 10);
	if( cx_min != null && cx_max != null )
	{
		if( value >= cx_min && value <= cx_max )
		{
			return true;
		}
		else
		{
			alert(errMsg+"Խ�ޣ�"+ errMsg +"������"+ cx_min +"~"+ cx_max +"֮�䡣");
			return false;
		}
	}
	else if( cx_min != null )
	{
		if( value <cx_min )
		{
			alert(errMsg+"Խ�ޣ�"+ errMsg +"������ڻ����"+ cx_min +"��");
			return false;
		}
		else
		{
			return true;
		}
	}
	else if(cx_max != null)
	{
		if( value > cx_max )
		{
			alert(errMsg+"Խ�ޣ�"+ errMsg +"����С�ڻ����"+ cx_max +"��");
			return false;
		}
		else
		{
			return true;
		}
	}

	return true;
}

function isValidNumber(elementObj)
{
	var value = elementObj.value;
	var cx_canbenull = elementObj.cx_canbenull;
	var cx_min = elementObj.cx_min;
	var cx_max = elementObj.cx_max;
	var errMsg = "��"+elementObj.cx_errormsg+"��";

	if( value == "")
	{
		if( cx_canbenull == "no" )
		{
			alert(errMsg + "�Ǳ����������Ϣ��");
			return false;
		}
		else
		{
			return true;
		}
	}

	if( !isNumber(value) )
	{
		alert(errMsg + "����ȫ����������ɡ�");
		return false;
	}

	if( cx_min != null && cx_max != null )
	{
		if( value.length >= cx_min && value.length <= cx_max ){
			return true;
		}
		else
		{
			alert(errMsg+"�ĳ���Խ�ޣ�"+ errMsg +"�ĳ��ȱ�����"+ cx_min +"~"+ cx_max +"֮�䡣");
			return false;
		}
	}
	else if( cx_min != null )
	{
		if( value.length >= cx_min )
		{
			return true;
		}
		else
		{
			alert(errMsg+"�ĳ���Խ�ޣ�"+ errMsg +"�ĳ�������Ҫ"+ cx_min +"λ����");
			return false;
		}
	}
	else if(cx_max != null)
	{
		if( value.length < cx_max )
		{
			return true;
		}
		else
		{
			alert(errMsg+"�ĳ���Խ�ޣ�"+ errMsg +"�ĳ������Ϊ"+ cx_max +"λ����");
			return false;
		}
	}else
		return true;
}

function isValidFloat(elementObj)
{
	var value = elementObj.value;
	var cx_canbenull = elementObj.cx_canbenull;
	var cx_min = elementObj.cx_min;
	var cx_max = elementObj.cx_max;
	var errMsg = "��"+elementObj.cx_errormsg+"��";

	if( value == "")
	{
		if( cx_canbenull == "no" ){
			alert(errMsg + "�Ǳ����������Ϣ��");
			return false;
		}
		else
		{
			return true;
		}
	}

	if( isNaN(value) )
	{
		alert(errMsg + "�����Ǹ�������");
		return false;
	}

	value = parseFloat(value);
	if( cx_min != null && cx_max != null )
	{
		if( value >= parseFloat(cx_min) && value <= parseFloat(cx_max) ){
			return true;
		}
		else
		{
			alert(errMsg+"Խ�ޣ�"+ errMsg +"������"+ cx_min +"~"+ cx_max +"֮�䡣");
			return false;
		}
	}
	else if( cx_min != null )
	{
		if( value >= parseFloat(cx_min) )
		{
			return true;
		}
		else
		{
			alert(errMsg+"Խ�ޣ�"+ errMsg +"������ڻ����"+ cx_min +"��");
			return false;
		}
	}
	else if(cx_max != null)
	{
		if( value <= parseFloat(cx_max) )
		{
			return true;
		}
		else
		{
			alert(errMsg+"Խ�ޣ�"+ errMsg +"����С�ڻ����"+ cx_max +"��");
			return false;
		}
	}

  return true;
}

function isValidDate(elementObj)
{
	var value = elementObj.value;
	var cx_canbenull = elementObj.cx_canbenull;
	var errMsg = "��"+elementObj.cx_errormsg+"��";

	if( value == "")
	{
		if( cx_canbenull == "no" )
		{
			alert(errMsg + "�Ǳ����������Ϣ��");
			return false;
		}
		else
		{
			return true;
		}
	}

	return dateValidation(value, errMsg);
}

function isValidTime(elementObj){
	var value = elementObj.value;
	var cx_canbenull = elementObj.cx_canbenull;
	var errMsg = "��"+elementObj.cx_errormsg+"��";

	if( value == "")
	{
		if( cx_canbenull == "no" )
		{
			alert(errMsg + "�Ǳ����������Ϣ��");
			return false;
		}
		else
		{
			return true;
		}
	}

	return timeValidation(value, errMsg);
}

function isValidDateTime(elementObj)
{
	return true;
}

function verify(formObj){
	var els = formObj.all;
	for(var i=0; i < els.length; i++)
	{
		if((els[i].tagName == "INPUT" && els[i].type == "text") ||(els[i].tagName == "SELECT")	)
		{
			var errMsg = els[i].cx_errormsg;
			switch(els[i].cx_type){
				case "string":
					if(!isValidString(els[i]))
					{
						els[i].focus();
						return false;
					}
					break;

				case "integer":
					if(!isValidInteger(els[i]))
					{
						els[i].focus();
						return false;
					}
					break;

				case "number":
					if(!isValidNumber(els[i]))
					{
						els[i].focus();
						return false;
					}
					break;

				case "float":
					if(!isValidFloat(els[i]))
					{
						els[i].focus();
						return false;
					}
					break;

				case "date":
					if(!isValidDate(els[i]))
					{
						els[i].focus();
						return false;
					}
					break;

				case "time":
					if(!isValidTime(els[i]))
					{
						els[i].focus();
						return false;
					}
					break;

				case "datetime":
					if(!isValidDateTime(els[i]))
					{
						els[i].focus();
						return false;
					}
					break;
			}
		}
	}
	return true;
}

//�Ƿ�0-9������
function IsDigit(cCheck) { return (('0'<=cCheck) && (cCheck<='9')); }

//�Ƿ�Ϊ��ĸ
function IsAlpha(cCheck) { return ((('a'<=cCheck) && (cCheck<='z')) || (('A'<=cCheck) && (cCheck<='Z'))) }

//�ʼ���ʽ���
function chkemail(str) {
  if (str.length<5) {return false;}
  var at_location = str.indexOf("@");
  var dot_location = str.lastIndexOf(".");
  if(at_location == -1 || dot_location == -1 || at_location > dot_location ) {return false;}
  if(at_location == 0) {return false;}
  if(dot_location - at_location <=1) {return false;}
  if(str.length - dot_location <=1) {return false;}

  return true;
}

//�����������볤��
function JHshStrLen(sString)
{
var sStr,iCount,i,strTemp ;

iCount = 0 ;
sStr = sString.split("");
for (i = 0 ; i < sStr.length ; i ++)
{
strTemp = escape(sStr[i]);
if (strTemp.indexOf("%u",0) == -1) // ��ʾ�Ǻ���
{
iCount = iCount + 1 ;
}
else
{
iCount = iCount + 2 ;
}
}
return iCount ;
}


function left(num) {
    return this.substring(0,num);
}
function right(num) {
    return this.substring(this.length - num);
}

//Ajax���ܵ��� 
var XMLHttpReq=false;
//����һ��XMLHttpRequest����
function createXMLHttpRequest(){
	if(window.XMLHttpRequest){ //Mozilla 
		XMLHttpReq=new XMLHttpRequest();
	}else if(window.ActiveXObject){
		try{
			XMLHttpReq=new ActiveXObject("Msxml2.XMLHTTP");
		}catch(e){
			try{
				XMLHttpReq=new ActiveXObject("Microsoft.XMLHTTP");
			}catch(e){}
		}
	}
}

//����������
function send(url,id){
	ajxObjID = id;
	createXMLHttpRequest();
	XMLHttpReq.open("get",url,true);
	XMLHttpReq.setRequestHeader( "Content-Type", "text/html;charset=GBK" );
	XMLHttpReq.onreadystatechange=proce;   //ָ����Ӧ�ĺ���
	XMLHttpReq.send(null);  //��������
}

function ajaxSend(url){
	createXMLHttpRequest();
	XMLHttpReq.open("get",url,true);
	XMLHttpReq.setRequestHeader( "Content-Type", "text/html;charset=gb2312" );
	XMLHttpReq.onreadystatechange=returnProce;   //ָ����Ӧ�ĺ���
	XMLHttpReq.send(null);  //��������
}

function returnProce(){
	if(XMLHttpReq.readyState==4){ //����״̬
		if(XMLHttpReq.status==200){//��Ϣ�ѳɹ����أ���ʼ������Ϣ  
				//document.getElementById('showFare').innerHTML = XMLHttpReq.responseText;
			dealResponse(XMLHttpReq.responseText);
		}else{
			window.alert("�������ҳ�����쳣");
		}
 	}
}

function Ajax(resType) {
	
	var aj = new Object();
	
	aj.createXMLHttpRequest = function(){
		var request = null;
		if(window.XMLHttpRequest){ //Mozilla 
			request = new XMLHttpRequest();
		}else if(window.ActiveXObject){
			try{
				request = new ActiveXObject("Msxml2.XMLHTTP");
			}catch(e){
				try{
					request = new ActiveXObject("Microsoft.XMLHTTP");
				}catch(e){}
			}
		}
		return request;
	}
	aj.request = aj.createXMLHttpRequest();
	
	aj.returnP = function(){
		if(aj.request.readyState == 4 && aj.request.status == 200) {

			if(resType=="HTML"){
				aj.responseMethod(aj.request.responseText);
			}else if(resType=="XML"){
				aj.responseMethod(aj.request.responseXML);
			}
		}
	}
	
	aj.getUrl = function(url,resMethod){
		aj.request.open("get",url,true);
		aj.request.setRequestHeader( "Content-Type", "text/html;charset=gb2312" );
		aj.request.onreadystatechange = aj.returnP;
		aj.responseMethod = resMethod;
		aj.request.send(null);
	}
	
	return aj
	
}

var DocUtil={
  get:function(id,doc){
    if(doc)return id?(doc.all?doc.all[id]:doc.getElementById(id)):null;
    return id?(document.all?document.all[id]:document.getElementById(id)):null;
  }
}

//##############################################################################################################

//##############################################################################################################
/**=======================================================
*==========����Ϊ��ѯ���ڵĸ�ʽ���ʹ�С�Ƚ�=============
=========================================================*/

//������ڸ�ʽ
function checkDateFormat(str)
{
	var r = str.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/); 
	if(r==null)return false; 
	var d= new Date(r[1], r[3]-1, r[4]); 
	return (d.getFullYear()==r[1]&&(d.getMonth()+1)==r[3]&&d.getDate()==r[4]);
}
var index=0;
//�Ƚ����ڴ�С
function checkDate(bdateId,edateId){
	var bdate=bdateId.value;
	var edate=edateId.value;
	if(bdate!="" && !checkDateFormat(bdate)){
		alert("��ʼ���� ��ʽ����!");
		return false; 
	}
	if(edate!="" && !checkDateFormat(edate)){
		alert("�������� ��ʽ����!");
		return false;
	}
	if(bdate!="" && edate!="" && bdate > edate){
		alert("��ʼ����ӦС�ڽ�������!");
		return false;
	}
	return true;
	 
}
//========================================================//
//���ļ��
function isChinese(temp){  
	 var re = /[^\u4e00-\u9fa5]/;  
	 if(re.test(temp)) return false;  
	 return true;  
}
//�������,������ĸ�����ֻ��»��߻���
function isRuleName(temp){  
	 var re = /^[\u4e00-\u9fa50-9a-zA-Z_]*$/;
	 if(re.test(temp)) return true;  
	 return false;  
}
//���ּ��
function isNumber(temp){
	var strNum="0123456789";
	var i,j,ch;
	for(i=0;i<temp.length;i++){
		ch=temp.charAt(i);
		//if ch is not a numbers,the value of the 'j' should be -1
		j=strNum.indexOf(ch);
		if(-1==j){
			return false;
		}
	}
	return true;
}

//ȡ�ճ�۸�
    function getMenuAirPrice(objValue,divId,priceId){
        if(objValue!=-1){
            document.getElementById(divId).innerHTML= "<img src='/images/b2c/loading.gif' hspace='5' align='absmiddle' />���Ժ򡭡�";
            var ajaxObj = new Ajax('HTML');
			ajaxObj.getUrl('/phone/airPrice.jsp?serialId='+objValue+'&priceId='+priceId,function(str){
                document.getElementById(divId).innerHTML = str;

            });
		}
    }

   function checkall(target,controller){

	    if(target!=undefined&&controller!=undefined){
			if(target.length!=undefined){
				for(var i=0;i<target.length;i++){

					target[i].checked = controller.checked;
				}
			}else{
				target.checked = controller.checked;

			}
		}else{
			if(document.all.id!=undefined){
				if(document.all.id.length!=undefined){
					for(var i=0;i<document.all.id.length;i++){
						document.all.id[i].checked = document.all.checkAll.checked;

					}
				}else{
					document.all.id.checked = document.all.checkAll.checked;

				}
			}
		}
	}

  function getMenuCardPrice(objValue,divId,priceId){
        if(objValue!=-1){
        	document.getElementById(divId).innerHTML= "<img src='/images/b2c/loading.gif' hspace='5' align='absmiddle' />���Ժ򡭡�";
			var ajaxObj = new Ajax('HTML');
			ajaxObj.getUrl('/phone/airPrice.jsp?serialId='+objValue+'&priceId='+priceId,function(str){
				document.getElementById(divId).innerHTML = str;
				var str1= str.replace("<font color='red' style='font-size:22px;'>","");
				str1= str1.replace("Ԫ</font>","");
				document.getElementById("price").value = str1;
            });
		}
    }
