    $(function () {
        var login = {
            name: 'login',
            url:  '#login',
            template: '#tpl_login'
        };
        pageManager.push(login);

        auth();
    });
    
 /* ==========================================================================
     登录入口函数  login(fn) {   }
 ============================================================================ */
     
    function login(fn) {
        pageManager.go("login");
        var timeout_login=0;
        var interval_login;
        var $weui_login = $('#weui_login');
        $weui_login.show();
        $weui_login.find('#dialog1 #idNo').unbind('click');
        $weui_login.find('#dialog1 #idOk').unbind('click');
        $weui_login.find('#dialog1 #idNo').bind('click', function () {
            $weui_login.hide();
        });
        $('#dialog1 #idOk').bind('click', function () {
           var mob = $weui_login.find("#mobile").val();
           if (mob=="") {
             return;
           }
           var cpa = $weui_login.find("#captcha").val();
           if (cpa=="") {
             return;
           }
           ajaxPost({
             url:'http://localhost:8080/tiaohuo/app/captcha-login.jhtml',
             data:{mobile:mob,captcha:cpa},
             success:function (dataBlock) {
                if (dataBlock.message.type!="success") {
                  showDialog2('出错了',dataBlock.message.content);
                }
                fn();
             }
           });
            $weui_login.hide();
        });
        $weui_login.find('#button').bind('click',function () {
           var mob = $weui_login.find("#mobile").val();
           if (mob=="") {
             return;
           }
           ajaxPost({
             url:'http://localhost:8080/tiaohuo/app/send_mobile.jhtml',
             data:{mobile:mob},
             success:function (dataBlock) {
                if (dataBlock.message.type!="success") {
                  showDialog2('出错了',dataBlock.message.content);
                }
                timeout_login = 0;
                interval_login = setInterval(function () {
                   timeout_login = timeout_login+1;
                   if (timeout_login>59) {
                      clearInterval(interval_login);
                      $('#weui_login').find('#button').html("重新获取");
                   } else {
                      $('#weui_login').find('#button').html(timeout_login+"秒");
                   }
                },1000);
             }
           });
        });
    }
    
 /* ==========================================================================
     判为是否登录  auth() {   }
 ============================================================================ */
    function auth() {
           ajaxGet({
             url:'http://localhost:8080/tiaohuo/app/auth_status.jhtml',
             data:{},
             success:function (dataBlock) {
                if (dataBlock.message.type!="success") {
                   showDialog2('出错了',dataBlock.message.content);
                }
                //return dataBlock.data;
                 if(!dataBlock.data){
                     login(function(){
                         window.location.reload();
                     });
                 }
             }
           });
    }