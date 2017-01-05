/**
 * 挑货网826活动浮标canvas动画
 * @return {[type]}   [description]
 */
$(function() {
    //如果存在相应的活动
    if ($(".activity.a826").length) {
        $script(_TH_.base + '/resources/wap/3.0/js/plugin/konva/konva.min.js', function() {
            $(".activity.a826").removeClass('none anm-out');
            var stage = new Konva.Stage({
                container: 'activity826', //canvas id
                width: $("#activity826").width(),
                height: $("#activity826").height()
            });
            var stageW = stage.getWidth();
            var stageH = stage.getHeight();
            var ft_height = 0.2 * stageH; //22px
            var circle_radius = stageW * 0.7727 / 2;
            //var btn_width = circle_radius * 2 * 0.8;
            //static_layer
            var static_layer = new Konva.Layer();
            //dynamic_layer
            var dynamic_layer = new Konva.Layer();
            //"点击进入" 链接
            var txt_clickme = new Konva.Text({
                x: stageW / 2,
                y: (stageH - ft_height / 2),
                text: '点击进入',
                fontSize: 5,
                //fontFamily: '',
                fill: '#fff',
                lineHeight: 0.8
            });
            txt_clickme.setOffset({
                x: txt_clickme.getWidth() / 2,
                y: txt_clickme.getHeight() / 2,
            });
            //"点击进入" 背景框
            var rect_btn = new Konva.Rect({
                x: stageW / 2,
                y: (stageH - ft_height / 2),
                width: circle_radius * 2 * 0.8,
                height: ft_height * 0.85,
                fill: '#6b23e0',
                //stroke: 'black',
                strokeWidth: 0,
                cornerRadius: ft_height * 0.273
            });
            rect_btn.setOffset({
                x: rect_btn.getWidth() / 2,
                y: rect_btn.getHeight() / 2,
            });
            //下半圆
            var arc1 = new Konva.Arc({
                x: stageW / 2,
                y: (stageH - ft_height) / 2,
                innerRadius: 0,
                outerRadius: circle_radius, //圆半径
                angle: 180,
                fill: '#f943ba',
                //stroke: 'black',
                strokeWidth: 0
            });
            //上半圆
            var arc2 = new Konva.Arc({
                x: stageW / 2,
                y: (stageH - ft_height) / 2,
                innerRadius: 0,
                outerRadius: circle_radius,
                angle: 180,
                rotation: 180,
                fill: '#6b23e0',
                //stroke: 'black',
                strokeWidth: 0
            });
            //文字:826狂欢节 
            var txt_826 = new Konva.Text({
                x: stageW / 2,
                y: (stageH - ft_height) / 2,
                text: '826狂欢节',
                fontSize: 7,
                fill: '#fff'
            });
            txt_826.setOffset({
                x: txt_826.getWidth() / 2,
                y: -txt_826.getHeight() / 2,
            });
            //测试线条
            var testline = new Konva.Line({
                points: [55, 44, 60, 44],
                stroke: 'black',
                strokeWidth: 10
            });
            //文字:跨店随机减
            var txt_randomcut = new Konva.Text({
                x: stageW / 2,
                y: (stageH - ft_height) / 2,
                text: '跨店随机减',
                fontSize: 5,
                fontStyle: 'italic',
                //fill: '#fff100',
                fillLinearGradientColorStops: [0, '#fff', 0.4, '#fff100', 1, '#fff100']
            });
            txt_randomcut.setOffset({
                x: txt_randomcut.getWidth() / 2,
                y: txt_randomcut.getHeight() * 1.5,
            });
            //随机减渐变色
            txt_randomcut.fillPriority("linear-gradient");
            txt_randomcut.fillLinearGradientStartPoint({
                x: txt_randomcut.x() - txt_randomcut.offsetX(),
                y: txt_randomcut.y() - txt_randomcut.offsetY()
            });
            txt_randomcut.fillLinearGradientEndPoint({
                x: txt_randomcut.x() - txt_randomcut.offsetX() + txt_randomcut.width(),
                y: txt_randomcut.y() - txt_randomcut.offsetY()
            });
            //txt_randomcut.fillLinearGradientColorStops(0, '#943421', 1, '#328415');
            //balloon1
            var balloon1 = new Konva.Circle({
                x: 5,
                y: 5,
                radius: 5,
                //fill: 'red',
                fillRadialGradientStartRadius: 5,
                fillRadialGradientEndRadius: 15,
                fillRadialGradientStartPoint: {
                    x: 6,
                    y: -6
                },
                fillRadialGradientEndPoint: {
                    x: 6,
                    y: -6
                },
                fillRadialGradientColorStops: [0, '#fff', 1, '#6b23e0']
            });
            var balloon2 = new Konva.Circle({
                x: stageW - 5,
                y: 5,
                radius: 5,
                //fill: 'red',
                fillRadialGradientStartRadius: 5,
                fillRadialGradientEndRadius: 15,
                fillRadialGradientStartPoint: {
                    x: 6,
                    y: -6
                },
                fillRadialGradientEndPoint: {
                    x: 6,
                    y: -6
                },
                fillRadialGradientColorStops: [0, '#fff', 1, '#fdb420']
            });
            arc2.on('touchstart', function() {
                console.log("soga");
            });
            // add the shape to the static_layer
            static_layer.add(rect_btn);
            static_layer.add(arc1);
            static_layer.add(arc2);
            static_layer.add(txt_clickme);
            static_layer.add(txt_826);
            static_layer.add(txt_randomcut);
            // add the shape to the dynamic_layer
            dynamic_layer.add(balloon1);
            dynamic_layer.add(balloon2);
            //dynamic_layer.add(testline);
            // add the static_layer to the stage
            stage.add(static_layer);
            // add the dynamic_layer to the stage
            stage.add(dynamic_layer);
            //
            stage.on('touchstart', function() {
                location.href = _TH_.base + "/wap/activity/index/"+_TH_.activityid+".jhtml";
                //console.log("this is stage");
            });
            balloon2.setY(stageH);
            var lasttoptime = 0;
            var anm_balloon = new Konva.Animation(function(frame) {
                //var valueY = balloon2.getY();
                if (balloon2.getY() < -20) {
                    //anm_balloon.stop();
                    balloon2.setY(stageH);
                    balloon2.setX(Math.random() * stageW * 0.333 + 5);
                    balloon2.fillRadialGradientColorStops([0, '#fff', 1, randomColorValue()]);
                }
                if (balloon1.getY() < -20) {
                    //anm_balloon.stop();
                    balloon1.setY(stageH);
                    balloon1.setX(stageW * 0.667 + Math.random() * stageW * 0.333 - 5);
                    balloon1.fillRadialGradientColorStops([0, '#fff', 1, randomColorValue()]);
                }
                //lasttoptime=
                balloon2.setY(balloon2.getY() - 0.5);
                balloon1.setY(balloon1.getY() - 0.4);
            }, dynamic_layer);
            $(".activity.a826").addClass('anm-in');
            anm_balloon.start();
        });
    }

});
