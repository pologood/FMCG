<div style="height: 100px;background-color: #ffffff;"></div>
<script type="text/javascript">
    var hasSpecication = '${hasSpecication}';
    var _status = "${status}", _isMarketable = "${isMarketable}";
    var compiler = Handlebars.compile($("#wap-list-item").html());
    var $data = '';
    var $spec = document.getElementById("spec");
    var $price = document.getElementById("price");
    var $stock = document.getElementById("stock");
    var $thumbnail = document.getElementById("thumbnail");
    var $productId = document.getElementById("productId");
    var spec = document.getElementById("kind-items-spec");
    var color = document.getElementById("color");
    var v_color = '';

    var ii = 1, _type = '';
    function productShop(type) {
        _type = type;
        ajaxGet({
            url: "${base}/wap/product/view.jhtml",
            data: {
                id:${product.id}
            },
            success: function (data) {
                $data = data;
                /* 无颜色分类测试*/
                $.map(data, function (item, index) {
                    if (item) {
                        //return item.spec="";
                    }
                });
                specInit('${product.id}', data);

                //使用取得的数据填充模板
                function fillData_itemscolor(data) {
                    $("#kind-items-color").html(compiler(data));
                }

                //
                if (data.length == 1 && data[0].color == "") {//不存在颜色分类时
                    //不需要填充样式分类
                    $("#kind-items-color").closest('.box_bd').hide().prev(".box_hd").hide();
                } else {//否则
                    fillData_itemscolor(data);
                }
                //显示弹出框
                showDetailDialog($('#product_details'));
            }
        });
    }

    function addCart(id, type) {

        if (_status != "success") {
            showToast2({content: "不好意思,该商品暂时不支持购买!"});
            return;
        }

        if (_isMarketable != "true") {
            showToast2({content: "当前商品以下架,如有需要请尝试联系店主或导购!"});
            return;
        }

        if (ii < 1) {
            showToast2({content: '亲，确认下购买的数量！！！'});
            return;
        }

        if (id == '') {
            id = $productId.value;
        }
        ajaxPost({
            url: '${base}/app/b2b/cart/add.jhtml',
            data: {
                id: id,
                type: type,
                quantity: ii
            },
            success: function (data) {
                if (data.message.type == "success") {
                    if ("buy" == _type) {
                        location.href = "${base}/wap/member/order/orderPay.jhtml";
                        return false;
                    }
                    if (hasSpecication != '') {
                        showDetailDialog($('#product_details'));
                    }
                    $('#count').html(data.data.count);
                    showToast2({content: "添加购物车成功"});
                } else {
                    showDialog2("提示", data.message.content);
                    //showToast2({content:"添加购物车成功"});
                    $('#dialog2').find('.weui_dialog').css('zIndex', '502');
                    $('#dialog2').find('.weui_mask').css('zIndex', '500');
                }
            }
        });
    }
    //型号初始化
    function specInit(id, data) {
        color.innerHTML = data[0]['color'];
        v_color = data[0]['color'];
        //spec obj
        var oo = data[0]['spec'];
        var styleHtml = '';
        if (oo.length > 0) {//如果型号不为空
            for (var j = 0; j < oo.length; j++) {
                if (j == 0) {
                    if (oo[j].spec == "") {
                        $("#kind-items-spec").closest('.box_bd').hide().prev(".box_hd").hide();
                    } else {
                        styleHtml += '<button class="choosed" onclick="getStyle();choosedHighLight();" id="' + oo[j].id + '">' + oo[j].spec + '</button>';
                    }
                } else {
                    styleHtml += '<button class="" onclick="getStyle();choosedHighLight();" id="' + oo[j].id + '">' + oo[j].spec + '</button>';
                }
            }
        } else {//如果型号为空
            $("#kind-items-spec").closest('.box_bd').hide().prev(".box_hd").hide();
        }
        //构造按钮
        spec.innerHTML = styleHtml;

        $spec.innerHTML = data[0].spec;
        $price.innerHTML = "￥" + data[0].price;
        $stock.innerHTML = data[0].stock;
        $thumbnail.setAttribute("src", data[0].thumbnail);
        $productId.setAttribute('value', data[0].id);
    }


    function selectNumber(type) {
        var productNumber = document.getElementById('quantity');
        ii = productNumber.value;

        if ('minus' == type) {
            if (ii <= 1) {
                return;
            }
            ii--;
        } else if ('add' == type) {
            ii++;
        }

        productNumber.setAttribute("value", ii);
    }

    function getSpec() {
        var $styleHtml = '';
        var object = event.srcElement.id;
        color.innerHTML = object;
        v_color = object;
        for (var i = 0; i < $data.length; i++) {
            var o = $data[i]['color'];
            if (object == o) {
                var oo = $data[i]['spec'];

                for (var j = 0; j < oo.length; j++) {
                    var first_should_claname = "";
                    if (j == 0) {//
                        //first_should_claname="choosed";
                    }
                    var spec_str = '<button class="' + first_should_claname + '" onclick="getStyle();choosedHighLight();" id="' + oo[j].id + '">' + oo[j].spec + '</button>';
                    $styleHtml = $styleHtml + spec_str;
                }
            }
        }
        spec.innerHTML = $styleHtml;
    }

    function getStyle() {
        var object = event.srcElement.id;
        for (var i = 0; i < $data.length; i++) {
            if (v_color == $data[i]['color']) {
                var oo = $data[i]['spec'];
                for (var j = 0; j < oo.length; j++) {
                    if (object == oo[j].id) {
                        $productId.setAttribute('value', oo[j].id);
                        $spec.innerHTML = oo[j].spec;
                        $price.innerHTML = "￥" + oo[j].price;
                        $stock.innerHTML = oo[j].stock;
                        $thumbnail.setAttribute("src", oo[j].thumbnail);
                    }
                }
            }
        }
    }
    function choosedHighLight() {
        var e = arguments[0] || window.event;
        var ele = e.srcElement || e.target;
        if (!$(ele).hasClass("sorry_no")) {
            $(ele).addClass('choosed');
            $(ele).siblings("button").removeClass('choosed');
        }
    }

    var hasFavorite = '${hasFavorite}';
    var _url = hasFavorite == "" ? "${base}/wap/bound/indexNew.jhtml?redirectUrl=${base}/wap/product/content/${product.id}/product.jhtml" : "${base}/wap/tenant/consulting/TELEPHONE.jhtml";
    function contactTenant() {
        ajaxPost({
            url: "${base}/wap/product/contact/" + $(".box_st.PPPD .box_st_name").attr("storeid") + ".jhtml",
            success: function (message) {
                if (message.message.type != "success") {
                    showToast2({content: message.message.content});
                    return false;
                }
                //联系店家配置信息
                var contact_config = {
                    title: '联系列表',
                    clastr: {
                        fortitle: "weui_cell tellists-title TTC",
                        forbody: "tellists-ctn TTC"
                    },
                    showmask: true,
                    removecancel: true,
                    data: message.data,
                    url: _url
                };

                showActionSheet2(contact_config);
            }
        });
        return false;
    }


    function Collection() {
        if (hasFavorite == "") {
            showToast2({content: "亲，您还没绑定，暂时不能收藏！"});
            location.href = "${base}/wap/bound/indexNew.jhtml?redirectUrl=${base}/wap/product/content/${product.id}/product.jhtml";
            return;
        }
        if (hasFavorite == "0") {
            ajaxPost({
                url: "${base}/app/member/favorite/product/delete.jhtml?id=${product.id}",
                success: function (message) {
                    hasFavorite = '1';
                    $("#addFavorite i").removeClass("color-orangered");
                    $("#addFavorite i").html('&#xe62c;');
                    $("#addFavorite>span").text("收藏");
                    showToast({content: '已取消收藏'});
                }
            });
        } else {
            ajaxPost({
                url: "${base}/app/member/favorite/product/add.jhtml?id=${product.id}",
                success: function (message) {
                    hasFavorite = '0';
                    $("#addFavorite i").addClass("color-orangered");
                    $("#addFavorite i").html('&#xe655;');
                    $("#addFavorite>span").text("已收藏");
                    showToast({content: '收藏成功'});
                }
            });
        }
    }
</script>
<div style="width:100%;height:60px;background-color:#fff;"></div>
<section id="actionbar-container">
    <div id="actionbar" class="pd-cl action-bar weui_cell">
        <a class="support weui_cell_primary am-padding-horizontal-s" href="javascript:contactTenant();">
            <i class="font-default iconfont">&#xe66f;</i>
            <br/>
            联系
        </a>
        <a class="support weui_cell_primary am-padding-horizontal-s " id="addFavorite" href="javascript:Collection();">
            <i class="font-default iconfont [#if hasFavorite=='0']color-orangered[/#if]">[#if hasFavorite=='0']
                &#xe655;[#else]&#xe62c;[/#if]</i>
            <br/>
            <span>收藏</span>
        </a>
        <a class="support weui_cell_primary am-padding-horizontal-s" href="${base}/wap/cart/list.jhtml">
            <i class="font-default iconfont">&#xe66c;</i>
            <br/>
            购物车
        </a>
        <button class="cart weui_cell_bd font-default" onclick="productShop('gouwuc')">加入购物车
            <div class="ct">
                <span id="count">${cart}</span>
            </div>
        </button>
        <button class="buy weui_cell_bd font-default" onclick="productShop('buy')">立即购买</button>
    </div>
</section>
<!-- 底部固定actionbar-container重制->pdbottombar-->
<section class="cf ft-bs1 pdbottombar" style="display:none">
    <div class="fl pdbottombar-L">
        <div class="table">
            <div class="table-row">
                <div class="table-cell">
                    <a href="javascript:;" class="light-grey L-it1">
                        <i class="iconfont">&#xe66f;</i>
                        <p>电话</p>
                    </a>
                </div>
                <div class="table-cell">
                    <a href="javascript:;" class="light-grey L-it2">
                        <i class="iconfont">&#xe62c;</i>
                        <p>收藏</p>
                    </a>
                </div>
                <div class="table-cell">
                    <a href="javascript:;" class="light-grey L-it3">
                        <i class="iconfont shoppingcar">&#xe66c;<span class="carnumtipR one">9</span></i>
                        <p>购物车</p>
                    </a>
                </div>
            </div>
        </div>
    </div>
    <div class="fr pdbottombar-R">
        <div class="table">
            <div class="table-row">
                <a href="javascript:;" class="table-cell R-it1">加入购物车</a>
                <a href="javascript:;" class="table-cell R-it2">立即购买</a>
            </div>
        </div>
    </div>
</section>