/*
 * This decorates Handlebars.js with the ability to load
 * templates from an external source, with light caching.
 * 
 * To render a template, pass a closure that will receive the 
 * template as a function parameter, eg, 
 *   hbsTO.render('templateName', function(t) {
 *       $('#somediv').html( t() );
 *   });
 * Source: https://github.com/wycats/handlebars.js/issues/82
 * little modified by simboo
 */
var HbsTemplateOperation = function() {
    this.cached = {};
};
var hbsTO = new HbsTemplateOperation();
$.extend(HbsTemplateOperation.prototype, {
    render: function(name, callback) {
        if (hbsTO.isCached(name)) {
            callback(hbsTO.cached[name]);
        } else {
            $.get(hbsTO.urlFor(name), function(raw) {
                hbsTO.store(name, raw);
                hbsTO.render(name, callback);
            });
        }
    },
    renderSync: function(name, callback) {
        if (!hbsTO.isCached(name)) {
            hbsTO.fetch(name);
        }
        hbsTO.render(name, callback);
    },
    prefetch: function(name) {
        $.get(hbsTO.urlFor(name), function(raw) {
            hbsTO.store(name, raw);
        });
    },
    fetch: function(name) {
        // synchronous, for those times when you need it.
        if (!hbsTO.isCached(name)) {
            var raw = $.ajax({ 'url': hbsTO.urlFor(name), 'async': false }).responseText;
            hbsTO.store(name, raw);
        }
    },
    isCached: function(name) {
        return !!hbsTO.cached[name];
    },
    store: function(name, raw) {
        hbsTO.cached[name] = Handlebars.compile(raw);
    },
    urlFor: function(name) {
        return _TH_.base + "/resources/wap/3.0/js/template/" + name + ".hbs";
    }
});
