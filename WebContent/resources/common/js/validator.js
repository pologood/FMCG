//��֤�ʻ�������Ϣ
var checkAccount={
	minLength:6,
	maxLength:20,
	checkF:function(strValue){
		var rule;
		var nullRule=/^\s+$/;
		var msgs=new Array();
		if(nullRule.exec(strValue)||strValue==""){
			msgs[msgs.length++]="����û�������ʻ�����";
		}else{
			if(strValue.length<this.minLength||strValue.length>this.maxLength){
				msgs[msgs.length++]="�������ʻ���������"+this.minLength+"��"+this.maxLength+"λ����";
			}
			rule=/^[0-9a-zA-Z_]*$/;
			if(!rule.exec(strValue)){
				msgs[msgs.length++]="�������ʻ��������Ϲ���";
			} 
		}
		return msgs;
	}
}


//��֤�ʼ���ַ������Ϣ
var checkEmail={
	checkF:function(strValue){
		var nullRule=/^\s+$/;
		var msgs=new Array();
		if((nullRule.exec(strValue)||strValue=="")){
			msgs[msgs.length++]="����û�������ʼ���ַ��";
		}else{
			if(!chkemail(strValue)){
				msgs[msgs.length++]="��������ʼ���ʽ����ȷ��";
			}
		}
		return msgs;
	}
}

//��֤����������Ϣ
var checkPassword={
	minLength:6,
	maxLength:20,
	checkF:function(strValue1,strValue2){
		var nullRule=/^\s+$/;
		var msgs=new Array();
		if(nullRule.exec(strValue1)||strValue1==""){
			msgs[msgs.length++]="����û�������¼���룡";
		}else{
			if(strValue1.length<this.minLength||strValue1.length>this.maxLength){
				msgs[msgs.length++]="����������������"+this.minLength+"��"+this.maxLength+"λ����";
			}
			if(nullRule.exec(strValue2)||strValue2==""){
				msgs[msgs.length++]="����û������ȷ�����룡";
			}else{
				if(strValue1!=strValue2){
					msgs[msgs.length++]="������ĵ�¼�����ȷ�����벻һ�£�";
				}
			}
		}
		return msgs;
	}
}

var checkLoginPassword={
	minLength:6,
	maxLength:20,
	emsg:'����û�������¼���룡',
	checkF:function(strValue){
		var nullRule=/^\s+$/;
		var msgs=new Array();
		if(nullRule.exec(strValue)||strValue==""){
			msgs[msgs.length++]=this.emsg;
		}
		return msgs;
	}
}

//��֤��֤��������Ϣ
var checkVerifyCode={
	checkF:function(strValue){
		var nullRule=/^\s+$/;
		var msgs=new Array();
		if(nullRule.exec(strValue)||strValue==""){
			msgs[msgs.length++]="����û��������֤�룡";
		}
	 	return msgs;
	}
}


var checkTerminal={
	checkF:function(strValue){
		var nullRule=/^\s+$/;
		var msgs=new Array();
		if(nullRule.exec(strValue)||strValue==0){
			msgs[msgs.length++]="����û��ѡ���ն˺ţ�";
		}
	 	return msgs;
	}
}

var checkName={
	checkF:function(strValue){
		var nullRule=/^\s+$/;
		var msgs=new Array();
		if(nullRule.exec(strValue)||strValue==0){
			msgs[msgs.length++]="����û������������";
		}
	 	return msgs;
	}
}
 var checkMobile={
	checkF:function(strValue){
		var phoneRule=/^[0-9]+$/;
        var nullRule=/^\s+$/;
        var msgs=new Array();
        if(nullRule.exec(strValue)||strValue==0){
            msgs[msgs.length++]="����δ�����ֻ����룡";
        }else
        if(phoneRule.exec(strValue)==null||strValue.length !=11){
			msgs[msgs.length++]="��������ֻ������ʽ����ȷ��";
		}
	 	return msgs;
	}
}

 var checkCertId={
	checkF:function(strValue){
		var phoneRule=/^[0-9]+$/;
        var nullRule=/^\s+$/;
        var msgs=new Array();
        if(nullRule.exec(strValue)||strValue==0){
            msgs[msgs.length++]="����δ��֤�����룡";
        }else
        if(phoneRule.exec(strValue)==null){
			msgs[msgs.length++]="�������֤�������ʽ����ȷ��";
		}
	 	return msgs;
	}
}

 var checkCertType={
	checkF:function(strValue){

        var nullRule=/^\s+$/;
        var msgs=new Array();
        if(nullRule.exec(strValue)||strValue.length ==0){
			msgs[msgs.length++]="������δѡ��֤�����ͣ�";
		}
	 	return msgs;
	}
}



 var checkDate={
	checkF:function(strValue){
		var nullRule=/^\s+$/;
		var rule=/^((1[8-9][0-9][0-9])|(2[0-9][0-9][0-9]))-((0?[1-9])|(1[0-2]))-((0?[1-9])|([1-2][0-9])|(3[0-1]))*$/;
		var msgs=new Array();
		if(!nullRule.exec(strValue)&&strValue!=""){
			if(!rule.exec(strValue)){
				msgs[msgs.length++]="������ĳ������ڸ�ʽ����ȷ��";
			}
		}
	 	return msgs;
	}
}

var checkFormNull={
	txtValue:'',
	minLength:6,
	maxLength:20,
	errmsg:'',
	doubleRule:/^[+]?\d+(\.\d+)?$/,
	checkF:function(strValue){
		var nullRule=/^\s+$/;
		var msgs=new Array();
		if(nullRule.exec(strValue)||strValue==0){
			msgs[msgs.length++]=this.errmsg;
		}
	 	return msgs;
	},
	checkD:function(strValue){
		var msgs=new Array();
		var rule=/^\d*$/;
		if(!rule.exec(strValue)){
			msgs[msgs.length++]=this.errmsg;
		}
		return msgs;
	},
	checkL:function(strValue){
		var msgs=new Array();
		if(strValue<this.txtValue){
			msgs[msgs.length++]=this.errmsg;
		}
		return msgs;
	},
    	checkM:function(strValue){
		var msgs=new Array();
		if(strValue>this.txtValue){
			msgs[msgs.length++]=this.errmsg;
		}
		return msgs;
	},
    checkL2:function(strValue){
		var nullRule=/^\s+$/;
		var msgs=new Array();
		if(!nullRule.exec(strValue)&&strValue!=""){
			if(strValue.length<this.minLength || strValue.length>this.maxLength){
				msgs[msgs.length++]=this.errmsg;
			}
		}
		return msgs;
	},
	checkDValue:function(strValue){
		var msgs=new Array();
		if(!this.doubleRule.exec(strValue)){
			msgs[msgs.length++]=this.errmsg;
		}
		return msgs;
	},
	checkS:function(strValue){
		var msgs=new Array();
		if(strValue==0){
			msgs[msgs.length++]=this.errmsg;
		}
		return msgs;
	},
        checkMobile:function(strValue){
		var msgs=new Array();
		if(strValue==0||strValue.length !=11){
			msgs[msgs.length++]=this.errmsg;
		}
		return msgs;
	},
    checkDateValue:function(strValue){
		var nullRule=/^\s+$/;
		var rule=/^((1[8-9][0-9][0-9])|(2[0-9][0-9][0-9]))-((0?[1-9])|(1[0-2]))-((0?[1-9])|([1-2][0-9])|(3[0-1]))*$/;
		var msgs=new Array();
		if(!nullRule.exec(strValue)&&strValue!=""){
			if(!rule.exec(strValue)){
				msgs[msgs.length++]=this.errmsg;
			}
		}
		
		return msgs;
	}
	
}



function joinMsg(arryMsg){
	var count = 1;
	var emsg = "";

	for(i=0;i<arryMsg.length;i++){
		for(j=0;j<arryMsg[i].length;j++){
			emsg=emsg+count+". "+arryMsg[i][j]+"\n";
			count++;
		}
	}
	return emsg;
}

