/*!
 * 选择颜色和规格插件 chooseCS
 * 基于zepto
 * @author simboo
 */
;
(function($) {
    /**
     * ChooseCS Class
     */
    ChooseCS = function(ele, options) {
        //元素保存到实例中
        this.$ele = $(ele);
        //暂存body
        this.$body = $(document.body);
        //保存配置
        this.options = $.extend(true, {}, ChooseCS.DEFAULTS, options);
        //变量
        //filters_predefine扩展集合,作为内部属性使用,仅声明,后续步骤扩展
        this.filters_produced = [];
        this.quantity = 1; //购买数量
        this.init_imgthumbsrc = ""; //初始的图片占位地址
        this.colors = []; //颜色集
        this.specs = []; //规格集
        //this.activeindex = this.options.initialstate.activeindex; //激活页签索引值
        //初始化
        this.init();
        //event声明
        //筛选项点击事件
        this.$ele.on('click.chooseCS.condnbtn', '[jhk="condn-btn"]', $.proxy(this.condBtnClickHandler, this));
        //减号点击事件
        this.$ele.on('click.chooseCS.minus', '[jhk="opera_minus"]', $.proxy(this.quantityMinus, this));
        //加号点击事件
        this.$ele.on('click.chooseCS.add', '[jhk="opera_add"]', $.proxy(this.quantityAdd, this));
        //this.$ele.find('.weui_chooseCS_item').on('click.chooseCS.head', $.proxy(this.headClickhandler, this));
    };
    //插件版本
    ChooseCS.VERSION = '1.0.0';
    //默认配置(all)
    ChooseCS.DEFAULTS = {
        filters_predefine: [{ //默认筛选属性声明
            property: "color",
            description: "颜色分类"
        }, {
            property: "spec",
            description: "型号规格"
        }]
    };
    //返回所选商品id
    ChooseCS.prototype.getProductId = function() {
        var this_instance = this;
        //如果是外部调用
        if (arguments[0].externalcall) {
            //重置参数列表
            var params = {};
            params.callback = arguments[0].params[0];
            //如果选中按钮数量等于筛选条件数量
            if (this_instance.$ele.find('.condn-btn.chosen').length !== this_instance.filters_produced.length) {
                params.callback(null);
            } else {
                var result_product = this_instance._getProduct();
                params.callback(result_product.id);
                console.log("filter_result's value is as following:");
                console.log(filter_result);
                //then
                //根据集合中列出的属性名和属性值筛选出商品id
            }
        }
    };
    //根据(充足)筛选条件获取商品
    ChooseCS.prototype._getProduct = function() {
        var this_instance = this;
        var filter_result = [];
        var $chosenbtns = this_instance.$ele.find('.condn-btn.chosen');
        $chosenbtns.each(function(index, ele) {
            var property = $(ele).closest('.condn').data('filter-property');
            filter_result.push({
                property: property,
                value: $(ele).text()
            });
        });
        return $.grep(this_instance.options.initialdatas, function(item) {
            var filter_result_counter = [];
            if (filter_result.length === 0) {}
            if (filter_result.length === 1) {
                if (item[filter_result[0].property] == filter_result[0].value) {
                    return true;
                }
            }
            if (filter_result.length === 2) {
                if (item[filter_result[0].property] == filter_result[0].value && item[filter_result[1].property] == filter_result[1].value) {
                    return true;
                }
            }
        })[0];
    };
    //返回所选商品id和数量
    ChooseCS.prototype.getProductIdAndQuantity = function() {
        var this_instance = this;
        //如果是外部调用
        if (arguments[0].externalcall) {
            //重置参数列表
            var params = {};
            params.callback = arguments[0].params[0];
            //如果无筛选条件(筛选条件集合length=0)
            if (this_instance.filters_produced.length === 0) {
                //正常流程,传出正常参数,但id赋0
                params.callback(0, this_instance.quantity);
                return;
            }
            //如果选中按钮数量不等于筛选条件数量
            if (this_instance.$ele.find('.condn-btn.chosen').length !== this_instance.filters_produced.length) {
                //非正常流程,传出null
                params.callback(null);
            } else {
                var filter_result = [];
                var $chosenbtns = this_instance.$ele.find('.condn-btn.chosen');
                $chosenbtns.each(function(index, ele) {
                    var property = $(ele).closest('.condn').data('filter-property');
                    filter_result.push({
                        property: property,
                        value: $(ele).text()
                    });
                });
                var result_product = $.grep(this_instance.options.initialdatas, function(item) {
                    var filter_result_counter = [];
                    if (filter_result.length === 0) {}
                    if (filter_result.length === 1) {
                        if (item[filter_result[0].property] == filter_result[0].value) {
                            return true;
                        }
                    }
                    if (filter_result.length === 2) {
                        if (item[filter_result[0].property] == filter_result[0].value && item[filter_result[1].property] == filter_result[1].value) {
                            return true;
                        }
                    }
                })[0];
                params.callback(result_product.id, this_instance.quantity);
                //console.log("filter_result's value is as following:");
                //console.log(filter_result);
                //then
                //根据集合中列出的属性名和属性值筛选出商品id
            }
        }
    };
    //返回所需商品数量
    ChooseCS.prototype.getQuantity = function() {
        var this_instance = this;
        if (arguments[0].externalcall) {
            //重置参数列表
            var params = {};
            params.callback = arguments[0].params[0];
            if (params.callback) {
                params.callback(this_instance.quantity);
            }
        }
    };
    //减少数量
    ChooseCS.prototype.quantityMinus = function(event) {
        var this_instance = this;
        var $opera_minus = $(event.target);
        if (this_instance.quantity > 1) {
            $opera_minus.closest('.addminus').children('b').text(--this_instance.quantity);
        }
    };
    //增加数量
    ChooseCS.prototype.quantityAdd = function(event) {
        var this_instance = this;
        var $opera_add = $(event.target);
        if (this_instance.quantity < 9998) {
            $opera_add.closest('.addminus').children('b').text(++this_instance.quantity);
        }
    };
    //刷新仪表盘
    ChooseCS.prototype.refreshDashBoard = function() {
        var this_instance = this;
        //缩略图
        var $board_imgthumb = this_instance.$ele.find('[jhk="imgthumb"]');
        //价格
        var $board_price = this_instance.$ele.find('[jhk="price"]');
        //库存
        var $board_stock = this_instance.$ele.find('[jhk="stock"]');
        //筛选条件说明
        var $board_chosenconds_desc = this_instance.$ele.find('[jhk="chosenconds-desc"]');
        //筛选条件
        var $board_chosenconds = this_instance.$ele.find('[jhk="chosenconds"]');
        var $chosenbtns = this_instance.$ele.find('.condn-btn.chosen');
        //如果选中按钮数量不等于筛选条件数量
        if ($chosenbtns.length !== this_instance.filters_produced.length) {
            $board_chosenconds_desc.text("请选择颜色或型号");
            $board_chosenconds.text("");
            if (this_instance.options.initialdatas.length) {
                $board_imgthumb.attr("src", this_instance.options.initialdatas[0].thumbnail);
            } else {
                $board_imgthumb.attr("src", this_instance.init_imgthumbsrc);
            }
            $board_price.text("0");
            $board_stock.text("0");
        } else {
            var result_product = this_instance._getProduct();
            var chosenconds_html = "";
            $board_imgthumb.attr("src", result_product.thumbnail);
            $board_price.text(result_product.price);
            $board_stock.text(result_product.stock);
            for (var k = 0; k < $chosenbtns.length; k++) {
                chosenconds_html = chosenconds_html + $chosenbtns.eq(k).text() + "  ";
            }
            $board_chosenconds_desc.text("已选");
            $board_chosenconds.text(chosenconds_html);
        }
    };
    //筛选项点击处理句柄
    ChooseCS.prototype.condBtnClickHandler = function(event) {
        var this_instance = this;
        var $condbtn = $(event.target);
        //如果是单条件
        if (this_instance.filters_produced.length === 1) {
            //所有禁用态不响应点击
            if ($condbtn.hasClass('sorry_no')) {
                return;
            }
            $condbtn.toggleClass('chosen').siblings().not(".sorry_no").removeClass('chosen');
            //刷新仪表盘
            this_instance.refreshDashBoard();
        }
        //如果是双条件模式
        if (this_instance.filters_produced.length === 2) {
            //所有禁用态不响应点击
            if ($condbtn.hasClass('sorry_no')) {
                return;
            }
            //联动组
            var $condn_neighbor = $condbtn.closest('.condn').siblings('.condn');
            var self_filterproperty = $condbtn.closest('.condn').data("filter-property");
            var neighbor_filterproperty = $condn_neighbor.data("filter-property");
            //有效记录集合
            var effectitems = [];
            //有效联动组属性集合
            var effect_neighbor_filterproperty = [];
            //$condn_neighbor.find('.condn-btn.chosen').length
            //如果联动无选定项
            effectitems = $.grep(this_instance.options.initialdatas, function(item) {
                if (item[self_filterproperty] == $condbtn.text()) {
                    return true;
                }
            });
            //console.log("effectitems's value is as following:");
            //console.log(effectitems);
            for (var i = 0; i < effectitems.length; i++) {
                if ($.inArray(effectitems[i][neighbor_filterproperty], effect_neighbor_filterproperty) < 0) { //如果已经在数组中
                    effect_neighbor_filterproperty.push(effectitems[i][neighbor_filterproperty]);
                }
            }
            var $neighbor_btns = $condn_neighbor.find('.condn-btn').addClass('sorry_no');
            $neighbor_btns.each(function(index, ele) {
                if ($.inArray($(ele).text(), effect_neighbor_filterproperty) >= 0) {
                    $(ele).removeClass('sorry_no');
                }
            });
            //如果联动组已选定并且当前项是取消选择
            if ($condn_neighbor.find('.condn-btn.chosen').length && $condbtn.hasClass('chosen')) {
                $condbtn.removeClass('chosen');
                $condn_neighbor.find('.condn-btn.chosen').siblings().removeClass('sorry_no');
                //刷新仪表盘
                this_instance.refreshDashBoard();
                return;
            }
            //如果联动组未选定并且当前项是取消选择
            if ($condn_neighbor.find('.condn-btn.chosen').length === 0 && $condbtn.hasClass('chosen')) {
                $condbtn.removeClass('chosen');
                $condn_neighbor.find('.condn-btn').removeClass('sorry_no');
                //刷新仪表盘
                this_instance.refreshDashBoard();
                return;
            }
            $condbtn.toggleClass('chosen').siblings().not(".sorry_no").removeClass('chosen');
            //刷新仪表盘
            this_instance.refreshDashBoard();
        }
    };
    //加工筛选集合
    ChooseCS.prototype.getProcessFilters = function() {
        var this_instance = this;
        //初始数据集
        var datas = this_instance.options.initialdatas;
        //this_instance.filterproperties : ["color", "spec"]
        var filter_pdf = this_instance.options.filters_predefine;
        //扩展筛选集合的content属性
        for (var j = 0; j < filter_pdf.length; j++) {
            if (!this_instance.filters_produced[j]) {
                this_instance.filters_produced[j] = {};
            }
            $.extend(true, this_instance.filters_produced[j], filter_pdf[j], {
                content: []
            });
        }
        //var colors = [];
        //var specs = [];
        for (var i = 0; i < datas.length; i++) {
            for (var k = 0; k < this_instance.filters_produced.length; k++) {
                //向扩展筛选集合content属性中加入初始数据中扫描到的值
                if ($.inArray(datas[i][this_instance.filters_produced[k].property], this_instance.filters_produced[k].content) < 0) { //如果不在数组中
                    this_instance.filters_produced[k].content.push(datas[i][this_instance.filters_produced[k].property]);
                }
            }
        }
        var filters_temp = [];
        for (var k1 = 0; k1 < this_instance.filters_produced.length; k1++) {
            //如果该预定义筛选条件无效
            if (this_instance.filters_produced[k1].content.length != 1 || this_instance.filters_produced[k1].content[0] != "") {
                filters_temp.push(this_instance.filters_produced[k1]);
            }
        }
        this_instance.filters_produced = filters_temp;
        console.log("filters_produced is");
        console.log(this_instance.filters_produced);
        return this_instance.filters_produced;
    };
    //搭建html结构 
    ChooseCS.prototype.build = function() {
        var def = $.Deferred();
        var this_instance = this;
        setTimeout(function() {
            //加工生成 filters_produced 集合作为模板数据集
            //var tpldata=this_instance.getProcessFilters();
            var tpldata = {
                filters_produced: this_instance.getProcessFilters()
            };
            var compilerChooseColorsize = Handlebars.compile($("#" + this_instance.options.tplid).html());
            this_instance.$ele.html(compilerChooseColorsize(tpldata));
            def.resolve();
        }, 0);
        return def.promise();
    };
    //初始化提示框
    ChooseCS.prototype.init = function() {
        var this_instance = this;
        //搭建结构
        $.when(this_instance.build())
            .done(function() {
                //数据声明后赋
                this_instance.init_imgthumbsrc = this_instance.$ele.find('[jhk="imgthumb"]').attr("src");
                //处理当初始数据集里只有一个的情况(不显示操作按钮)
                if (this_instance.options.initialdatas.length) {
                    var obj = this_instance.options.initialdatas[0];
                    //商品缩略图
                    this_instance.$ele.find('[jhk="imgthumb"]').attr("src", obj.thumbnail);
                    //价格
                    this_instance.$ele.find('[jhk="price"]').text(obj.price);
                    //库存
                    this_instance.$ele.find('[jhk="stock"]').text(obj.stock);
                    //筛选条件说明
                    this_instance.$ele.find('[jhk="chosenconds-desc"]').text("");
                }
                //发送初始化完毕事件
                this_instance.$ele.trigger($.Event('initialized:chooseCS'));
            })
            .fail(function() {
                console.log("网络异常");
            });
    };
    /**
     * ChooseCS Plugin
     * 提示框插件定义
     */
    function Plugin(option) {
        var initial_arguments = arguments;
        //如果是空元素并且选择符.NULL
        if (this.length === 0 && this.selector == ".NULL") {
            return null;
        }
        // each function
        return this.each(function() {
            var $this = $(this);
            //试获取对象实例
            var data_plugin = $this.data('TH.chooseCS');
            //var options = typeof option == 'object' && option;
            if (!data_plugin) { //如果不存在对象实例,默认是插件初始化动作
                if (typeof option === 'object' || !option) { //如果option是json或没有任何参数
                    data_plugin = new ChooseCS(this, option);
                    $this.data('TH.chooseCS', data_plugin);
                    //data_plugin.init();
                }
                //初始化完成不再进行其他操作
            } else { //已存在对象实例,默认是调用插件方法
                if (typeof option == 'string') {
                    data_plugin[option]({
                        externalcall: true,
                        params: Array.prototype.slice.call(initial_arguments, 1)
                    });
                    //data_plugin[option](Array.prototype.slice.call(initial_arguments, 1));
                } else {
                    $.info('Method ' + option + ' does not exist on Zepto.chooseCS');
                }
            }
        });
    }
    //如果之前存在其他的chooseCS插件，暂存
    var old = $.fn.chooseCS;
    //挂载到$原型链
    $.fn.chooseCS = Plugin;
    //构造器指向类ChooseCS
    $.fn.chooseCS.Constructor = ChooseCS;
    //ChooseCS noConflict
    $.fn.chooseCS.noConflict = function() {
        $.fn.chooseCS = old;
        return this;
    };
    //暂不支持data-api
})(Zepto);
