var _gaq = _gaq || [];
if ($S.stores.pageMeta.exception_tracking) {
    /* load errorception */
    var _errs = ["56557d16455c179b7c00004f"];
    (function(a, b) {
        a.onerror = function() {
            _errs.push(arguments)
        };
        var d = function() {
            var a = b.createElement("script")
                , c = b.getElementsByTagName("script")[0];
            a.src = "//d15qhc0lu1ghnk.cloudfront.net/beacon.js";
            a.async = !0;
            c.parentNode.insertBefore(a, c)
        };
        a.addEventListener ? a.addEventListener("load", d, !1) : a.attachEvent("onload", d)
    })(window, document);
}
// Google Analytics (Universal) Setup
(function(i, s, o, g, r, a, m) {
    i['GoogleAnalyticsObject'] = r;
    i[r] = i[r] || function() {
            (i[r].q = i[r].q || []).push(arguments)
        }
        ,
        i[r].l = 1 * new Date();
    a = s.createElement(o),
        m = s.getElementsByTagName(o)[0];
    a.async = 1;
    a.src = g;
    m.parentNode.insertBefore(a, m)
})(window, document, 'script', '//www.google-analytics.com/analytics.js', '__ga');
// configuration for keen.io
var Keen = Keen || {
        configure: function(e) {
            this._cf = e
        },
        addEvent: function(e, t, n, i) {
            this._eq = this._eq || [],
                this._eq.push([e, t, n, i])
        },
        setGlobalProperties: function(e) {
            this._gp = e
        },
        onChartsReady: function(e) {
            this._ocrq = this._ocrq || [],
                this._ocrq.push(e)
        }
    };
(function() {
    var e = document.createElement("script");
    e.type = "text/javascript",
        e.async = !0,
        e.src = ("https:" == document.location.protocol ? "https://" : "http://") + "dc8na2hxrj29i.cloudfront.net/code/keen-2.1.0-min.js";
    var t = document.getElementsByTagName("script")[0];
    t.parentNode.insertBefore(e, t)
})();
// Configure the Keen object with your Project ID and (optional) access keys.
Keen.configure({
    projectId: $S.global_conf.KEEN_IO_PROJECT_ID,
    writeKey: $S.global_conf.KEEN_IO_WRITE_KEY
});
var _strk = _strk || []
window.runAfterDomBinding = {
    add: function(name, fn) {
        _strk.push(['Page.didMount', fn])
    }
}