var fs = require('fs');
var gulp = require('gulp');
var moment = require('moment');
var jshint = require('gulp-jshint');
var uglify = require('gulp-uglify');
var del = require('del');
var extend = require('gulp-extend');
var rename = require('gulp-rename');
var sourcemaps = require('gulp-sourcemaps');
var notify = require('gulp-notify');
var concat = require('gulp-concat');
var vinylPaths = require('vinyl-paths');
var rev = require('gulp-rev');


/**
 * js-vendor任务进行之前的清理工作
 * @param  {[type]} ) {                 } [description]
 * @return {[type]}   [description]
 */
gulp.task('js-vendor-cacheclean', function() {
    return gulp.src([
            'js/vendor*.js',
            'js/!main.js'
        ])
        .pipe(vinylPaths(del))
        .pipe(gulp.dest('./js/history'));
});
/**
 * js-vendor,合并处理vendor文件夹下的js文件
 * @return {[type]}   [description]
 */
gulp.task('js-vendor', ['js-vendor-cacheclean'], function() {
    return gulp.src([
            './Jspre/vendor/scriptjs/script.js',
            './Jspre/vendor/zepto/zepto1.1.6-release-olive.js',
            './Jspre/vendor/handlebars/handlebars-v2.0.0.js',
            './Jspre/vendor/lockr/lockr.js',
            './Jspre/vendor/store2/store2.js'
        ])
        //.pipe(jshint())
        //.pipe(jshint.reporter('jshint-stylish'))
        .pipe(concat('vendor.js'))
        .pipe(gulp.dest('./js'))
        .pipe(rev())
        .pipe(gulp.dest('./js'))
        .pipe(rename({ suffix: '.min' }))
        .pipe(uglify())
        .pipe(gulp.dest('./js'))
        .pipe(rev.manifest('Jspre/manifest/js-vendor.json', {
            base: 'Jspre',
            merge: true
        }))
        .pipe(gulp.dest('./Jspre'))
        .pipe(notify({ message: 'task js-vendor end' }));
});

/**
 * js-lib任务进行之前的清理工作
 * @param  {[type]} ) {                 } [description]
 * @return {[type]}   [description]
 */
gulp.task('js-lib-cacheclean', function() {
    return gulp.src([
            'js/lib*.js',
            'js/!main.js'
        ])
        .pipe(vinylPaths(del))
        .pipe(gulp.dest('./js/history'));
});
/**
 * js-lib,合并处理lib文件夹下的js文件
 * @return {[type]}   [description]
 */
gulp.task('js-lib', ['js-lib-cacheclean'], function() {
    return gulp.src([
            './Jspre/lib/hbs/*.js',
            './Jspre/lib/browser/[^_]*.js',
            './Jspre/lib/color/*.js',
            './Jspre/lib/time/*.js',
            './Jspre/lib/math/*.js',
            './Jspre/lib/position/*.js',
            './Jspre/lib/size/*.js',
            './Jspre/lib/string/*.js',
            './Jspre/lib/interface/*.js',
            './Jspre/lib/shape/*.js'
        ])
        .pipe(jshint())
        .pipe(jshint.reporter('jshint-stylish'))
        .pipe(concat('lib.js'))
        .pipe(gulp.dest('./js'))
        .pipe(rev())
        .pipe(gulp.dest('./js'))
        .pipe(rename({ suffix: '.min' }))
        .pipe(uglify())
        .pipe(gulp.dest('./js'))
        .pipe(rev.manifest('Jspre/manifest/js-lib.json', {
            base: 'Jspre',
            merge: true
        }))
        .pipe(gulp.dest('./Jspre'))
        .pipe(notify({ message: 'task js-lib end' }));
});
/**
 * js-whostrap前的清理工作
 * @param  {[type]} ) 
 * @return {[type]}   [description]
 */
gulp.task('js-whostrap-cacheclean', function() {
    return gulp.src([
            'js/whostrap*.js',
            'js/!main.js'
        ])
        .pipe(vinylPaths(del))
        .pipe(gulp.dest('./js/history'));
});
//task js-whostrap
gulp.task('js-whostrap', ['js-whostrap-cacheclean'], function() {
    return gulp.src([
            'Jspre/whostrap/base.js',
            'Jspre/whostrap/dialog.js',
            'Jspre/whostrap/tip.js',
            'Jspre/whostrap/positionscroller.js',
            'Jspre/whostrap/tabnav.js',
            'Jspre/whostrap/actionsheet.js'
        ])
        .pipe(jshint())
        .pipe(jshint.reporter('jshint-stylish'))
        .pipe(concat('whostrap.js'))
        .pipe(gulp.dest('./js'))
        .pipe(rev())
        .pipe(gulp.dest('./js'))
        .pipe(rename({ suffix: '.min' }))
        .pipe(uglify())
        .pipe(gulp.dest('./js'))
        .pipe(rev.manifest('Jspre/manifest/js-whostrap.json', {
            base: 'Jspre',
            merge: true
        }))
        .pipe(gulp.dest('./Jspre'))
        .pipe(notify({ message: 'task js-whostrap end' }));
});

//输出相关日志到manifest/log文件夹
gulp.task('manifest', function() {
    var tiemstamp = moment().format('YYYYMMDD-hhmmss');
    gulp.src(['Jspre/manifest/*.json'])
        .pipe(extend('rev-manifest-' + tiemstamp + '.json')) // gulp-extend
        .pipe(gulp.dest('./Jspre/manifest/log'));
});
/**
 * 发布所有js任务
 * @dependency js-lib,js-whostrap
 */
gulp.task('build-js', ['js-lib', 'js-whostrap']);
/**
 * 发布所有js任务，输出相关日志到manifest/log文件夹
 * @dependency build-js
 */
gulp.task('build-js-and-manifest', ['build-js'], function() {
    gulp.start('manifest');
});
/*==================================================================*/
/**
 * js-page,合并处理page文件夹下的js文件
 * @return {[type]}   [description]
 */
gulp.task('js-page', function() {
    return gulp.src([
            './Jspre/page/**/[^_]*.js'
        ])
        .pipe(jshint())
        .pipe(jshint.reporter('jshint-stylish'))
        .pipe(gulp.dest('./js/page'))
        .pipe(rev())
        .pipe(gulp.dest('./js/page'))
        .pipe(rename({ suffix: '.min' }))
        .pipe(uglify())
        .pipe(gulp.dest('./js/page'))
        .pipe(notify({ message: 'task js-page end' }));
});
/*==================================================================*/
/**
 * js-module,合并处理module文件夹下的js文件
 * @return {[type]}   [description]
 */
gulp.task('js-module', function() {
    return gulp.src([
            './Jspre/module/**/[^_]*.js'
        ])
        .pipe(jshint())
        .pipe(jshint.reporter('jshint-stylish'))
        .pipe(gulp.dest('./js/module'))
        .pipe(rev())
        .pipe(gulp.dest('./js/module'))
        .pipe(rename({ suffix: '.min' }))
        .pipe(uglify())
        .pipe(gulp.dest('./js/module'))
        .pipe(notify({ message: 'task js-module end' }));
});
/*==================================================================*/
gulp.task('watch-lib', function() {
    gulp.watch('./Jspre/lib/**/*.js', ['js-lib']);
});
gulp.task('watch-whostrap', function() {
    gulp.watch('./Jspre/whostrap/**/*.js', ['js-whostrap']);
});
gulp.task('watch-page', function() {
    gulp.watch('./Jspre/page/**/*.js', ['js-page']);
});
gulp.task('watch-module', function() {
    gulp.watch('./Jspre/module/**/*.js', ['js-module']);
});