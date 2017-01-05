/**
 * page js
 * @for page/whostrap/test/tip
 */
if (_TH_.tscpath == "/wap/tenant/union") {
    $(function() {
        //var soga=Lockr.get('username');
        //console.log(soga);
        //var itemlist_page = 0;
        if (!Lockr.get('itemlist_page')) {
            Lockr.set('itemlist_page', 1);
        } else {
            console.log("pagenow is the local");
            console.log(Lockr.get('itemlist_page'));
        }
        $(".itemlist_rap").scrollLoader({
            url: "/tiaohuo/wap/tenant/get_tenant_product/201.jhtml",
            pagenow: Lockr.get('itemlist_page')
        });
        $(".itemlist_rap").on('pageContentLoaded:ScrollLoader', function(event, pagenum) {
            //event.preventDefault();
            console.log("pagenum's value is as following:");
            console.log(pagenum);
        });
        //
        $(window).on('beforeunload', function(event) {
            event.preventDefault();
            console.log("you leave this page");
            //离开之前保存当前浏览到的页面
            //Lockr.set('itemlist_page', $(".itemlist_rap").scrollLoader("getPageNow"));
            $(".itemlist_rap").scrollLoader("getPageNow", function(pagenow) {
                //从scrollLoader处拿到当前页码保存到本地
                Lockr.set('itemlist_page', pagenow);
                console.log("pagenow's value is as following:");
                console.log(pagenow);
            });
        });
        //
        if (!$.fn.picLazyLoad) {
            //$script(_TH_.base + '/resources/wap/3.0/js/plugin/lazyload/lazyload.min.js', function() {
            //});
        } else {

        }
        //仅延迟加载
        $script(_TH_.base + '/resources/wap/3.0/js/plugin/lazyload/lazyload.min.js');
    });
}
