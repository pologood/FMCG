$(function() {
    /**
     * 把选出项(popup-selecteditem)和它对应的弹出层(.popupmenu)  建立联系
     * @param  {[type]} selecteditem_claname 选中项类名字符串
     * @param  {[type]} popupmenu_claname    弹出框类名字符串
     * @return {[type]}                      [description]
     */
    function popupmaker(selecteditem_claname, popupmenu_claname) {
        $(selecteditem_claname).click(function() {
            console.log("you click me");
            $(popupmenu_claname).toggle();
        });

        $(popupmenu_claname).find('a').click(function(event) {

            var clicktext = $(this).text();
            $(selecteditem_claname).find('.am-navbar-label').text(clicktext);
            $(popupmenu_claname).toggle();
        });
    }
    //make
    popupmaker(".popup-selecteditem", ".popupmenu");
    console.log("hello soga");
    //配置actionsheet
    /* 底部滑动测试，未实现*/
    /* test begin*/
    var actionSheet = {
        name: 'actionsheet',
        url: '#actionsheet',
        template: '#tpl_actionsheet',
        events: {
            '#showActionSheet': {
                click: function(event) {
                    var mask = $('#mask');

                    var weuiActionsheet = $('#weui_actionsheet');
                    weuiActionsheet.addClass('weui_actionsheet_toggle');
                    mask.show().addClass('weui_fade_toggle').one('click', function() {
                        hideActionSheet(weuiActionsheet, mask);
                    });

                    $('#actionsheet_cancel').one('click', function() {
                        hideActionSheet(weuiActionsheet, mask);
                    });
                    weuiActionsheet.unbind('transitionend').unbind('webkitTransitionEnd');

                    function hideActionSheet(weuiActionsheet, mask) {
                        weuiActionsheet.removeClass('weui_actionsheet_toggle');
                        mask.removeClass('weui_fade_toggle');
                        weuiActionsheet.on('transitionend', function() {
                            mask.hide();
                        }).on('webkitTransitionEnd', function() {
                            mask.hide();
                        })
                    }
                    //event.preventDefault();
                    return false;
                }
            }
        }
    };
    
    //pageManager.push(wraper).push(actionSheet).default('wraper').init();
    /* test end*/
});
