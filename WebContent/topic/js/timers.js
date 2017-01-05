function getStyleClass(oParent,sClass){
	var oC=oParent.getElementsByTagName('*');
	var oResult=[];
	for(var i=0;i<oC.length;i++){
		if(oC[i].className==sClass){
			 oResult.push(oC[i]);
			}
	}
}
window.onload=function(){
	var oTm=document.getElementById('timer');
	var oD=document.getElementById("zDD");
	var oH=document.getElementById("zHH");
	var oM=document.getElementById("zMM");
	var oS=document.getElementById("zSS");
	var timer=null;
	var flag=true;
	var iNow=0;
	var d,h,m,s;
	if(!flag){
		clearInterval(timer);
		}
		<!--活动时间倒计时-->
	function GetRTime(){
       var EndTime= new Date('2016/09/06 23:59:59');
	   var StartTime=new Date('2016/08/28 00:00:00 ')
       var NowTime = new Date();
       var t =EndTime.getTime() - NowTime.getTime();
	   var st=StartTime.getTime() - NowTime.getTime();
	   
        d=Math.floor(t/1000/60/60/24);
        h=Math.floor(t/1000/60/60%24);
        m=Math.floor(t/1000/60%60);
        s=Math.floor(t/1000%60);
		
		sd=Math.floor(st/1000/60/60/24);
        sh=Math.floor(st/1000/60/60%24);
        sm=Math.floor(st/1000/60%60);
        ss=Math.floor(st/1000%60);
		
       oD.innerHTML = sd ;
       oH.innerHTML = sh;
       oM.innerHTML = sm ;
       oS.innerHTML = ss ;
	   
	   if(sd<=0 && sh<=0 && sm<=0 && ss<=0){
		 oTm.style.backgroundImage="url(img/timer.jpg)";
		 oD.innerHTML = d;
         oH.innerHTML = h;
         oM.innerHTML = m;
         oS.innerHTML = s;
}
	   
	   
   if(d<=0 && h<=0 && m<=0 && s<=0){
		 oTm.style.backgroundImage="url(img/timer_end.jpg)";
		 oD.innerHTML = '';
         oH.innerHTML = '';
         oM.innerHTML = '';
         oS.innerHTML = '';
		 clearInterval(timer);
		 flag=false;
}
 }

  timer=setInterval(GetRTime,0);
    <!--立即抢购倒计时显示-->
    function showImg(){
		var oMain=document.getElementById('main');
		var oImg=getStyleClass(oMain,dis_img);
		
         }
		showImg();
}