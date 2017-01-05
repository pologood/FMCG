[#-- @ftlroot "../../../" --]
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta name="baidu-site-verification" content="7EKp4TWRZT"/>
[@seo type = "index"]
    <title> [#if seo.title??][@seo.title?interpret /][#else]首页[/#if]</title>
    [#if seo.keywords??]
        <meta name="keywords" content="[@seo.keywords?interpret /]"/>
    [/#if]
    [#if seo.description??]
        <meta name="description" content="[@seo.description?interpret /]"/>
    [/#if]
[/@seo]
    <link href="${base}/resources/helper/css/common.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/helper/css/account.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/helper/css/product.css" type="text/css" rel="stylesheet"/>
    <link href="${base}/resources/helper/font/iconfont.css" type="text/css" rel="stylesheet"/>

    <script type="text/javascript" src="${base}/resources/helper/js/jquery.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/list.js"></script>
    <script type="text/javascript" src="${base}/resources/helper/js/common.js"></script>
    <script src="${base}/resources/helper/js/amazeui.min.js"></script>
    <script type="text/javascript" src="${base}/resources/admin/js/jquery.table2excel.js"></script>
    <script type="text/javascript">
        $().ready(function () {

            var $listForm = $("#listForm");
            var $moreButton = $("#moreButton");
            var $unionCancelButton = $("#unionCancelButton");
            var $dialogOverlay = $("#dialogOverlay");
            var $selectAll = $("#selectAll");
            var $ids = $("#listTable input[name='ids']");
            var $deleteButtons = $("#deleteButtonProduct");
            var $downMarketButton = $("#downMarketButton");
            var $changePriceButton = $("#changePriceButton");
            var $pageTotal = $("#pageTotal");
            var $listTable = $("#listTable");

        [@flash_message /]

            // 更多选项
            $moreButton.click(function () {
                $.dialog({
                    title: "${message("admin.product.moreOption")}",
                [@compress single_line = true]
                    content: '' +
                    '<table id="moreTable" class="input">' +
                    '<tr><th>${message("Product.productCategory")}:<\/th>' +
                    '<td><select name="productCategoryId">' +
                    '<option value="">${message("admin.common.choose")}<\/option>' +
                        [#list productCategoryTree as productCategory]
                        '<option value="${productCategory.id}"[#if productCategory.id == productCategoryId] selected="selected"[/#if]>'+
                            [#if productCategory.grade != 0]
                                [#list 1..productCategory.grade as i]
                                '&nbsp;&nbsp;'+
                                [/#list]
                            [/#if]
                        '${productCategory.name}<\/option>'+
                        [/#list]
                    '<\/select><\/td><\/tr>' +
                    '<tr><th>${message("Product.brand")}:<\/th>' +
                    '<td><select name="brandId">' +
                    '<option value="">${message("admin.common.choose")}<\/option>' +
                        [#list brands as brand]
                        '<option value="${brand.id}"[#if brand.id == brandId] selected="selected"[/#if]>${brand.name}<\/option>'+
                        [/#list]
                    '<\/select><\/td><\/tr>' +
                    '<tr><th>${message("Product.promotions")}:<\/th>' +
                    '<td><select name="promotionId"><option value="">${message("admin.common.choose")}<\/option>' +
                        [#list promotions as promotion]
                        '<option value="${promotion.id}"[#if promotion.id == promotionId] selected="selected"[/#if]>${promotion.name}<\/option>'+
                        [/#list]
                    '<\/select><\/td><\/tr><tr><th>${message("Product.tags")}:<\/th><td><select name="tagId"><option value="">${message("admin.common.choose")}<\/option>' +
                        [#list tags as tag]
                        '<option value="${tag.id}"[#if tag.id == tagId] selected="selected"[/#if]>${tag.name}<\/option>'+
                        [/#list]
                    '<\/select><\/td><\/tr>' +
                        [#if versionType==1]
                        '<tr><th>供应商:<\/th><td><select name="supplierId">' +
                        '<option value="">${message("admin.common.choose")}<\/option>' +
                            [#list suppliers as supplier]
                            '<option value="${supplier.parent.id}"[#if supplier.parent.id == supplierId] selected="selected"[/#if]>${supplier.parent.name}<\/option>'+
                            [/#list]
                        '<\/select><\/td><\/tr>' +
                        [/#if]
                    '<\/table>',
                [/@compress]
                    width: 470,
                    modal: true,
                    ok: "${message("admin.dialog.ok")}",
                    cancel: "${message("admin.dialog.cancel")}",
                    onOk: function () {
                        $("#moreTable :input").each(function () {
                            var $this = $(this);
                            $("#" + $this.attr("name")).val($this.val());
                        });

                        $listForm.submit();
                    }
                });
            });

            //下架
            $(".downMarketable").click(function () {
                if (confirm("下架后商品将无法被搜索到，确认下架？")) {
                    $.ajax({
                        url: "downMarketable.jhtml",
                        data: {id: $(this).attr("productId")},
                        dataType: "json",
                        type: "get",
                        success: function (message) {
                            $.message("success", message.content);
                            location.reload();
                        }
                    });
                }
            });

            $dialogOverlay.click(function () {
                $.dialog({
                    title: "${message("admin.product.moreOption")}",
                [@compress single_line = true]
                    content: '<table id="moreTable" class="moreTable"><tr><th>${message("Product.productCategory")}:<\/th><td><select name="productCategoryId"><option value="">${message("admin.common.choose")}<\/option>' +
                        [#list productCategoryTree as productCategory]
                        '<option value="${productCategory.id}"[#if productCategory.id == productCategoryId] selected="selected"[/#if]>'+
                            [#if productCategory.grade != 0]
                                [#list 1..productCategory.grade as i]
                                '&nbsp;&nbsp;'+
                                [/#list]
                            [/#if]
                        '${productCategory.name}<\/option>'+
                        [/#list]
                    '<\/select><\/td><\/tr><tr><th>${message("Product.brand")}:<\/th><td><select name="brandId"><option value="">${message("admin.common.choose")}<\/option>' +
                        [#list brands as brand]
                        '<option value="${brand.id}"[#if brand.id == brandId] selected="selected"[/#if]>${brand.name}<\/option>'+
                        [/#list]
                    '<\/select><\/td><\/tr><tr><th>${message("Product.promotions")}:<\/th><td><select name="promotionId"><option value="">${message("admin.common.choose")}<\/option>' +
                        [#list promotions as promotion]
                        '<option value="${promotion.id}"[#if promotion.id == promotionId] selected="selected"[/#if]>${promotion.name}<\/option>'+
                        [/#list]
                    '<\/select><\/td><\/tr><tr><th>${message("Product.tags")}:<\/th><td><select name="tagId"><option value="">${message("admin.common.choose")}<\/option>' +
                        [#list tags as tag]
                        '<option value="${tag.id}"[#if tag.id == tagId] selected="selected"[/#if]>${tag.name}<\/option>'+
                        [/#list]
                    '<\/select><\/td><\/tr><\/table>',
                [/@compress]
                    width: 470,
                    modal: true,
                    ok: null,
                    cancel: null
                });
            });

            /**
             * 商品批量下架
             */
            $downMarketButton.click(function () {
                var $this = $(this);
                if ($this.hasClass("disabled")) {
                    return false;
                }
                var $checkedIds = $("#listTable input[name='ids']:enabled:checked");
                $.dialog({
                    type: "warn",
                    content: '<div style="padding-right: 40px;"><h2 style="font-size: 16px;font-weight: 400;color: #000;">您确定要下架选中的商品么？</h2></div>',
                    width: 360,
                    onOk: function () {
                        $.ajax({
                            url: "downMarketables.jhtml",
                            data: $checkedIds.serialize(),
                            dataType: "json",
                            type: "get",
                            success: function (message) {
                                if (message.type == "success") {
                                    $.message("success", message.content);
                                    window.setTimeout(function () {
                                        window.location.reload();
                                    }, 600);
                                }
                                $downMarketButton.addClass("disabled");
                                $selectAll.prop("checked", false);
                                $checkedIds.prop("checked", false);
                            }
                        });
                    }
                });
            });

            $deleteButtons.click(function () {
                var $this = $(this);
                if ($this.hasClass("disabled")) {
                    return false;
                }
                var $checkedIds = $("#listTable input[name='ids']:enabled:checked");
                $.dialog({
                    type: "warn",
                    content: '<div style="padding-right: 40px;"><h2 style="font-size: 16px;font-weight: 400;color: #000;">您确定要删除您选中的商品么？</h2><span style="display: block;position: relative;margin-top: 5px;line-height: 16px;"><i style="position: absolute;top: 0;left: 0;font-style: normal;color: #eb3341;">温馨提示：</i><span style="padding-left: 65px;display: block;">如商品参加限时折扣、买赠搭配，此类活动也将被删除。</span></span></div>',
                    ok: message("helper.dialog.ok"),
                    cancel: message("helper.dialog.cancel"),
                    width: 360,
                    onOk: function () {
                        $.ajax({
                            url: "delete.jhtml",
                            type: "POST",
                            data: $checkedIds.serialize(),
                            dataType: "json",
                            cache: false,
                            success: function (message) {
                                $.message(message);
                                if (message.type == "success") {
                                    $pageTotal.text(parseInt($pageTotal.text()) - $checkedIds.size());
                                    $checkedIds.closest("tr").remove();
                                    if ($listTable.find("tr").size() <= 1) {
                                        setTimeout(function () {
                                            location.reload(true);
                                        }, 3000);
                                    }
                                }
                                $deleteButtons.addClass("disabled");
                                $selectAll.prop("checked", false);
                                $checkedIds.prop("checked", false);
                            }
                        });
                    }
                });
            });

            // 全选
            $selectAll.click(function () {
                var $this = $(this);
                var $enabledIds = $("#listTable input[name='ids']:enabled");
                if ($this.prop("checked")) {
                    $enabledIds.prop("checked", true);
                    if ($enabledIds.filter(":checked").size() > 0) {
                        $deleteButtons.removeClass("disabled");
                        $downMarketButton.removeClass("disabled");
                        $changePriceButton.removeClass("disabled");
                    } else {
                        $deleteButtons.addClass("disabled");
                        $downMarketButton.addClass("disabled");
                        $changePriceButton.addClass("disabled");
                    }
                } else {
                    $enabledIds.prop("checked", false);
                    $deleteButtons.addClass("disabled");
                    $downMarketButton.addClass("disabled");
                    $changePriceButton.addClass("disabled");
                }
            });

            // 选择
            $ids.click(function () {
                var $this = $(this);
                if ($this.prop("checked")) {
                    $this.closest("tr").addClass("selected");
                    $deleteButtons.removeClass("disabled");
                    $downMarketButton.removeClass("disabled");
                    $changePriceButton.removeClass("disabled");
                } else {
                    $this.closest("tr").removeClass("selected");
                    if ($("#listTable input[name='ids']:enabled:checked").size() > 0) {
                        $deleteButtons.removeClass("disabled");
                        $downMarketButton.removeClass("disabled");
                        $changePriceButton.removeClass("disabled");
                    } else {
                        $deleteButtons.addClass("disabled");
                        $downMarketButton.addClass("disabled");
                        $changePriceButton.addClass("disabled");
                    }
                }
            });

            $changePriceButton.click(function () {
                var $this = $(this);
                if ($this.hasClass("disabled")) {
                    return false;
                }
                var $checkedIds = $("#listTable input[name='ids']:enabled:checked");
                $.ajax({
                    url: 'view.jhtml',
                    type: 'get',
                    data: $checkedIds.serialize(),
                    dataType: 'json',
                    success: function (data) {
                        if (data.message.type == "success") {
                            var content = '<div style="height: 350px;overflow-y: auto;">' +
                                    '<form id="priceForm"><table class="list" style="table-layout: fixed;">' +
                                    '<thead>' +
                                    '<tr>' +
                                    '<th width="180" style="padding-left:5px"><input type="checkbox" id="checkAll"/><a href="javascript:;">名称</a></th>' +
                                    '<th><a href="javascript:;">销售价</a></th>' +
                                    [#list memberRanks as memberRank]
                                    '<th><a href="javascript:;">${memberRank.name}</a></th>' +
                                    [/#list]
                                    '</tr>' +
                                    '</thead>' +
                                    '<tbody>';
                            $.each(data.data, function (i, products) {
                                $.each(products, function (index, product) {
                                    content += '<tr class="productTr">' +
                                            '<td style="overflow: hidden;text-overflow: ellipsis;white-space: nowrap;';
                                    if (index != 0) {
                                        content += 'padding-left: 25px;';
                                    }
                                    content += '"><input type="checkbox" name="ids" value="' + product.id + '" />' + product.fullName + '</td>' +
                                            '<td><input marketPrice="' + product.marketPrice + '" name="price_' + product.id + '" class="text" value="' + product.price + '" type="text" style="width: 100px;"/></td>' +
                                            [#list memberRanks as memberRank]
                                            '<td><input type="text" class="text memberPrice" memberRankName="${memberRank.name}" memberRankId="${memberRank.id}" name="memberPrice_' + product.id + '_${memberRank.id}" value="'+ (product.memberPrice.memberPrice_${memberRank.id} == null ? '' : product.memberPrice.memberPrice_${memberRank.id}) + '" style="width: 100px;"/></td>' +
                                            [/#list]
                                            '<tr/>';
                                });
                            });
                            content += '<tr>' +
                                    '<td style="text-align: center;">统一价格：</td>' +
                                    '<td><input name="unitPrice" class="text" type="text" style="width: 100px;"/></td>' +
                                    [#list memberRanks as memberRank]
                                    '<td><input name="unitMemberPrice_${memberRank.id}" class="text" type="text" style="width: 100px;"/></td>' +
                                    [/#list]
                                    '</tr>' +
                                    '</tbody>' +
                                    '</table>' +
                                    '</div>';
                            $.dialog({
                                title: '批量改价',
                                content: content,
                                width: 1000,
                                onOk: function () {
                                    var pass = true;
                                    var unitePrice = $("input[name='unitPrice']").val().trim();
                                    var $checked = $("#priceForm input[name='ids']:checked");
                                    if ($checked.size() == 0) {
                                        $.message("error", "请选择需要改价的商品");
                                        return false;
                                    }
                                    $.each($checked, function () {
                                        if (pass) {
                                            var $this = $(this);
                                            var price = $this.parent().next().children().val();
                                            var marketPrice = $this.parent().next().children().attr("marketPrice");
                                            var productName = $this.parent().text();
                                            if (unitePrice == "") {
                                                if (isNaN(price)) {
                                                    pass = false;
                                                    $.message("error", "商品（" + productName + "）的销售价输入有误");
                                                } else if (Number(price) > Number(marketPrice)) {
                                                    pass = false;
                                                    $.message("error", "商品（" + productName + "）的销售价不能大于其市场价（" + marketPrice + "元）");
                                                }
                                            } else {
                                                if (isNaN(unitePrice)) {
                                                    pass = false;
                                                    $.message("error", "统一价格（销售价）输入有误");
                                                } else if (Number(unitePrice) > Number(marketPrice)) {
                                                    pass = false;
                                                    $.message("error", "统一价格（销售价）不能大于商品（" + productName + "）的市场价（" + marketPrice + "元）");
                                                }
                                            }
                                            var memberPricePass = true;
                                            $.each($this.parents(".productTr").find(".memberPrice"), function () {
                                                if (memberPricePass) {
                                                    var memberRankId = $(this).attr("memberRankId");
                                                    var memberRankName = $(this).attr("memberRankName");
                                                    var memberPrice = $(this).val().trim();
                                                    var unitMemberPrice = $("[name='unitMemberPrice_" + memberRankId + "']").val().trim();
                                                    if (unitMemberPrice == "") {
                                                        if (memberPrice != "" && isNaN(memberPrice)) {
                                                            memberPricePass = false;
                                                            pass = false;
                                                            $.message("error", "商品（" + productName + "）的" + memberRankName + "价格输入有误");
                                                        }
                                                    } else {
                                                        if (isNaN(unitMemberPrice)) {
                                                            memberPricePass = false;
                                                            pass = false;
                                                            $.message("error", "统一价格（" + memberRankName + "）输入有误");
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    });
                                    if (!pass) {
                                        return false;
                                    }
                                    $.ajax({
                                        url: "price.jhtml",
                                        data: $("#priceForm").serialize(),
                                        dataType: "json",
                                        type: "get",
                                        success: function (message) {
                                            $.message(message);
                                            if (message.type == "success") {
                                                window.setTimeout(function () {
                                                    window.location.reload();
                                                }, 600);
                                            } else {
                                                return false;
                                            }
                                        }
                                    });
                                }
                            });
                        } else {
                            $.message(data.message);
                        }
                    }
                });

            });

            $("#multiUpMarketButton").click(function(){
                var content='<div style="height: 350px;overflow-y: auto;">' +
                        '<form id="spForm">' +
                        '<div style="padding:15px 0;">' +
                        '<span style="padding-left:10px;">选择供应商：</span>' +
                        '<select id="supplierSelect">' +
                        '<option value="">--请选择--</option>' +
                        [#list suppliers as suppler]
                        '<option value="${suppler.parent.id}">${(suppler.parent.name)!}</option>' +
                        [/#list]
                        '</select>' +
                        '</div>' +
                        '<table class="list" style="table-layout: fixed;" id="spTable">' +
                        '<tr>' +
                        '<thead>' +
                        '<th style="padding-left:5px"><input type="checkbox" id="spCheckAll"/>名称</th>' +
                        '<th>上架数量</th>' +
                        '</thead>' +
                        '<tbody id="supplierProduct">' +
                        '<tbody>' +
                        '</tr>' +
                        '</table>' +
                        '</form>' +
                        '</div>';
                $.dialog({
                    title: '批量上架',
                    content: content,
                    width: 600,
                    onOk: function () {
                        var $checked=$("#spTable input[name='ids']:checked");
                        if($checked.size()==0){
                            $.message("error","请选择需要上架的商品");
                            return false;
                        }
                        var flag=true;
                        $.each($checked, function(){
                            var $this=$(this);
                            var stock=$("input[name='upMarketStock_"+$this.val()+"']").val().trim();
                            if(stock===""||isNaN(stock)){
                                flag=false;
                            }
                        });
                        if(!flag){
                            $.message("error","请输入正确的上架数量");
                            return false;
                        }
                        $.ajax({
                            url:'checkFullName.jhtml',
                            data:$("#spTable input[name='fullNames']:enabled:checked").serialize(),
                            dataType:'json',
                            type:'get',
                            success:function(data){
                                console.log(data);
                                if(data.message.type=="success"){
                                    if(data.data.length>0){
                                        var msg='<div style="padding-right: 40px;">' +
                                                '<h2 style="font-size: 16px;font-weight: 400;color: #000;">您的店铺存在下列相同名称的商品，是否上架？</h2>' +
                                                '<span style="display: block;position: relative;margin-top: 5px;line-height: 16px;">' +
                                                '<span style="display: block;">';
                                        $.each(data.data,function(k,v){
                                            msg+="【"+ v +"】";
                                        });
                                        msg+='</span>' +
                                                '</span>' +
                                                '</div>';
                                        $.dialog({
                                            type: "warn",
                                            content: msg,
                                            ok: message("helper.dialog.ok"),
                                            cancel: message("helper.dialog.cancel"),
                                            width: 360,
                                            onOk: function () {
                                                spUpMarket();
                                            }
                                        });
                                    }else if(data.data.length==0){
                                        spUpMarket();
                                    }
                                }else{
                                    $.message(data.message);
                                }

                            }
                        });
                        return false;
                    }
                });
            });

            $("body").on("change", "#priceForm #checkAll", function () {
                $("#priceForm input[name='ids']").prop("checked", $("#priceForm #checkAll").prop("checked"));
            }).on("change", "#priceForm input[name='ids']", function () {
                var allChecked = true;
                $.each($("#priceForm input[name='ids']"), function () {
                    if (allChecked) {
                        if (!$(this).prop("checked")) {
                            allChecked = false;
                        }
                    }
                });
                $("#priceForm #checkAll").prop("checked", allChecked);
            }).on("change","#supplierSelect",function(){
                $.ajax({
                    url:'supplier/products.jhtml',
                    type:'get',
                    data:{
                        id:$("#supplierSelect").val()
                    },
                    dataType:'json',
                    success:function(data){
                        if(data.message.type=="success"){
                            var arr=[];
                            $.each(data.data,function(i,products){
                                $.each(products,function(index,product){
                                    if(index==0){
                                        arr.push('<tr><td><input type="checkbox" value="'+product.id+'" name="ids" /><input style="display: none;" type="checkbox" value="'+product.fullName+'" name="fullNames" />'+product.fullName+'</td><td><input name="upMarketStock_'+product.id+'" /></td></tr>');
                                    }else{
                                        arr.push('<tr><td style="padding-left: 25px;"><input type="checkbox" value="'+product.id+'" name="ids" /><input style="display: none;" type="checkbox" value="'+product.fullName+'" name="fullNames" />'+product.fullName+'</td><td><input name="upMarketStock_'+product.id+'" /></td></tr>');
                                    }
                                });
                            });
                            $("#supplierProduct").html(arr.join(""));
                        }else{
                            $.message(data.message);
                        }
                    }
                });
            }).on("change","#spTable #spCheckAll",function(){
                $("#spTable input[name='ids']").prop("checked",$("#spTable #spCheckAll").prop("checked"));
                $("#spTable input[name='ids']").next().prop("checked",$("#spTable #spCheckAll").prop("checked"));
            }).on("change","#spTable input[name='ids']",function(){
                var $this=$(this);
                if($(this).prop("checked")){
                    $this.next().prop("checked",true);
                }else{
                    $this.next().prop("checked",false);
                }
                var allChecked = true;
                $.each($("#spTable input[name='ids']"), function () {
                    if (allChecked) {
                        if (!$this.prop("checked")) {
                            allChecked = false;
                        }
                    }
                });
                $("#spTable #spCheckAll").prop("checked", allChecked);
            });

            //========================导出数据===========================
            $("#export_ss").click(function(){
                $.message("success","正在帮您导出，请稍后");
                $.ajax({
                    url:"${base}/helper/member/product/isMarketableList_export.jhtml",
                    type:"post",
                    data:{
                        productCategoryId:"${productCategoryId}",
                        brandId:"${brandId}",
                        searchValue:"${searchValue}",
                        promotionId:"${promotionId}",
                        tagId:"${tagId}",
                        supplierId:"${supplierId}",
                        isMarketable:"[#if isMarketable??]${isMarketable?string("true", "false")}[/#if]",
                        isList:"[#if isList??]${isList?string("true", "false")}[/#if]",
                        isTop:"[#if isTop??]${isTop?string("true", "false")}[/#if]",
                        isGift:"[#if isGift??]${isGift?string("true", "false")}[/#if]",
                        isOutOfStock:"[#if isOutOfStock??]${isOutOfStock?string("true", "false")}[/#if]",
                        isStockAlert:"[#if isStockAlert??]${isStockAlert?string("true", "false")}[/#if]"
                    },
                    async:false,
                    dataType:"json",
                    success:function(data){
                        var html='<table style="display:none;" class="table2excel">'+
                        '<thead>'+
                            '<tr>'+
                                '<th>商品名称</th>'+
                                '<th>所属商家</th>'+
                                '<th>所属供应商</th>'+
                                '<th>商品条型码</th>'+
                                '<th>商品编码</th>'+
                                '<th>商品分类</th>'+
                                '<th>所有库存</th>'+
                                '<th>可用库存</th>'+
                                '<th>销售价</th>'+
                                '<th>成本价</th>'+
                                '<th>单位</th>'+
                                '<th>是否上下架</th>'+
                            '</tr>'+
                        '</thead>'+
                        '<tbody>;'
                        $.each(data,function(i,obj){
                            html+=
                            '<tr>'+
                                '<td>'+obj.name+'</td>'+
                                '<td>'+obj.tenantName+'</td>'+
                                '<td>'+obj.supplierName+'</td>'+
                                '<td>'+obj.barcode+'</td>'+
                                '<td>'+obj.sn+'</td>'+
                                '<td>'+obj.productCategory+'</td>'+
                                '<td>'+obj.stock+'</td>'+
                                '<td>'+obj.availableStock+'</td>'+
                                '<td>'+obj.price+'</td>'+
                                '<td>'+obj.cost+'</td>'+
                                '<td>'+obj.unit+'</td>'+
                                '<td>'+obj.isMarket+'</td>'+
                            '</tr>';
                        });
                        html+='</tbody>'+
                        '</table>';
                        $("#trade_wrap").html(html);
                    }
                });
                //导出数据到excel
                $(".table2excel").table2excel({
                    exclude: ".noExl",
                    name: "商品列表",
                    filename: "商品列表",
                    fileext: ".xlsx",
                    exclude_img: true,
                    exclude_links: false,
                    exclude_inputs: true 
                });
            });

        });

        function spUpMarket(){
            $.ajax({
                url: "spUpMarket.jhtml",
                data: $("#spForm").serialize(),
                dataType: "json",
                type: "get",
                success: function (data) {
                    $.message(data.message);
                    if(data.message.type=="success"){
                        window.setTimeout(function () {
                            window.location.reload();
                        }, 600);
                    }
                }
            });
        }

        function showStocks(id) {
            $.ajax({
                url: 'view.jhtml',
                type: 'get',
                data: {ids: id},
                dataType: 'json',
                success: function (data) {
                    if (data.message.type == "success") {
                        var content = '<div style="height: 260px;overflow-y: auto;">' +
                                '<form id="priceForm"><table class="list" style="table-layout: fixed;">' +
                                '<thead>' +
                                '<tr>' +
                                '<th width="200"><a href="javascript:;">名称</a></th>' +
                                '<th><a href="javascript:;">库存</a></th>' +
                                '<th><a href="javascript:;">可用库存</a></th>' +
                                '<th><a href="javascript:;">锁定库存</a></th>' +
                                '</tr>' +
                                '</thead>' +
                                '<tbody>';
                        $.each(data.data, function (i, products) {
                            $.each(products, function (index, product) {
                                content += '<tr>' +
                                        '<td style="overflow: hidden;text-overflow: ellipsis;white-space: nowrap;">' + product.fullName + '</td>' +
                                        '<td>' + (Number(product.stock) < 0 ? 0 : product.stock) + '</td>' +
                                        '<td>' + (Number(product.availableStock) < 0 ? 0 : product.availableStock) + '</td>' +
                                        '<td>' + product.allocatedStock + '</td>' +
                                        '<tr/>';
                            });
                        });
                        content += '</tbody>' +
                                '</table>' +
                                '</div>';
                        $.dialog({
                            title: '库存明细',
                            content: content,
                            width: 550,
                            cancel: null
                        });
                    } else {
                        $.message(data.message);
                    }
                }
            });
        }

        function getPropuctPreview(url) {

            $.dialog({
                title: "${message("admin.product.moreOption")}",
            [@compress single_line = true]
                content: '<div class="bill-record">' +
                '<iframe id="billIframe" name="billIframe" frameborder="0" src="' + url + '" width="100%" height="580" scrolling="yes">' +
                '</iframe>' +
                '</div>',
            [/@compress]
                width: 470,
                height: 650,
                modal: true,
                ok: null,
                cancel: null
            });
        }

    </script>
</head>
<body>

[#include "/helper/include/header.ftl" /]
[#include "/helper/member/include/navigation.ftl" /]
<div class="desktop">
    <div class="container bg_fff">

    [#include "/helper/member/include/border.ftl" /]

    [#include "/helper/member/include/menu.ftl" /]

        <div class="wrapper" id="wrapper">


            <div class="page-nav page-nav-app vip-guide-nav" id="app_head_nav">
                <div class="js-app-header title-wrap" id="app_0000000844">
                    <img class="js-app_logo app-img" src="${base}/resources/helper/images/upload3.png"/>
                    <dl class="app-info">
                        <dt class="app-title" id="app_name">我的商品</dt>
                        <dd class="app-status" id="app_add_status">
                        </dd>
                        <dd class="app-intro" id="app_desc">查询我发布的宝贝，维护当前售价及库存状态等。</dd>
                    </dl>
                </div>
                <ul class="links" id="mod_menus">
                    <li><a class="on" hideFocus="" href="${base}/helper/member/product/isMarketableList.jhtml">已上架商品</a>
                    </li>
                [@helperRole url="helper/member/product/notMarketablelist.jhtml" ]
                    [#if helperRole.retUrl!="0"]
                        <li><a class="" hideFocus=""
                               href="${base}/helper/member/product/notMarketablelist.jhtml">已下架商品</a>
                        </li>
                    [/#if]
                [/@helperRole]
                </ul>

            </div>
            <form id="listForm" action="isMarketableList.jhtml" method="get">
                <input type="hidden" id="productCategoryId" name="productCategoryId" value="${productCategoryId}"/>
                <input type="hidden" id="brandId" name="brandId" value="${brandId}"/>
                <input type="hidden" id="promotionId" name="promotionId" value="${promotionId}"/>
                <input type="hidden" id="tagId" name="tagId" value="${tagId}"/>
                <input type="hidden" id="supplierId" name="supplierId" value="${supplierId}"/>
                <input type="hidden" id="isMarketable" name="isMarketable"
                       value="[#if isMarketable??]${isMarketable?string("true", "false")}[/#if]"/>
                <input type="hidden" id="isList" name="isList"
                       value="[#if isList??]${isList?string("true", "false")}[/#if]"/>
                <input type="hidden" id="isTop" name="isTop"
                       value="[#if isTop??]${isTop?string("true", "false")}[/#if]"/>
                <input type="hidden" id="isGift" name="isGift"
                       value="[#if isGift??]${isGift?string("true", "false")}[/#if]"/>
                <input type="hidden" id="isOutOfStock" name="isOutOfStock"
                       value="[#if isOutOfStock??]${isOutOfStock?string("true", "false")}[/#if]"/>
                <input type="hidden" id="isStockAlert" name="isStockAlert"
                       value="[#if isStockAlert??]${isStockAlert?string("true", "false")}[/#if]"/>
                <div class="bar">
                    <div class="buttonWrap">
                    [@helperRole url="helper/member/product/isMarketableList.jhtml" type="downMarket"]
                        [#if helperRole.retOper!="0"]
                            <a id="downMarketButton" href="javascript:;" class="iconButton disabled">
                                <span class="falseIcon">&nbsp;</span>下架
                            </a>
                        [/#if]
                    [/@helperRole]

                    [@helperRole url="helper/member/product/isMarketableList.jhtml" type="del"]
                        [#if helperRole.retOper!="0"]
                            <a href="javascript:;" id="deleteButtonProduct" class="iconButton disabled">
                                <span class="deleteIcon">&nbsp;</span>${message("admin.common.delete")}
                            </a>
                        [/#if]
                    [/@helperRole]
                        <a href="${base}/helper/member/product/isMarketableList.jhtml" class="iconButton">
                            <span class="refreshIcon">&nbsp;</span>${message("admin.common.refresh")}
                        </a>
                        <div class="menuWrap">
                            <a href="javascript:;" id="filterSelect" class="button">
                            ${message("admin.product.filter")}<span class="arrow">&nbsp;</span>
                            </a>
                            <div class="popupMenu">
                                <ul id="filterOption" class="check">
                                    <li class="separator">
                                        <a href="javascript:;" name="isList" val="true"[#if isList?? && isList]
                                           class="checked"[/#if]>${message("admin.product.isList")}</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;" name="isList" val="false"[#if isList?? && !isList]
                                           class="checked"[/#if]>${message("admin.product.notList")}</a>
                                    </li>
                                    <li class="separator">
                                        <a href="javascript:;" name="isTop" val="true"[#if isTop?? && isTop]
                                           class="checked"[/#if]>${message("admin.product.isTop")}</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;" name="isTop" val="false"[#if isTop?? && !isTop]
                                           class="checked"[/#if]>${message("admin.product.notTop")}</a>
                                    </li>
                                    <li class="separator">
                                        <a href="javascript:;" name="isGift" val="true"[#if isGift?? && isGift]
                                           class="checked"[/#if]>${message("admin.product.isGift")}</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;" name="isGift" val="false"[#if isGift?? && !isGift]
                                           class="checked"[/#if]>${message("admin.product.nonGift")}</a>
                                    </li>
                                    <li class="separator">
                                        <a href="javascript:;" name="isOutOfStock"
                                           val="false"[#if isOutOfStock?? && !isOutOfStock]
                                           class="checked"[/#if]>${message("admin.product.isStack")}</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;" name="isOutOfStock"
                                           val="true"[#if isOutOfStock?? && isOutOfStock]
                                           class="checked"[/#if]>${message("admin.product.isOutOfStack")}</a>
                                    </li>
                                    <li class="separator">
                                        <a href="javascript:;" name="isStockAlert"
                                           val="false"[#if isStockAlert?? && !isStockAlert]
                                           class="checked"[/#if]>${message("admin.product.normalStore")}</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;" name="isStockAlert"
                                           val="true"[#if isStockAlert?? && isStockAlert]
                                           class="checked"[/#if]>${message("admin.product.isStockAlert")}</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <a href="javascript:;" id="moreButton" class="button">${message("admin.product.moreOption")}</a>
                        <div class="menuWrap">
                            <a href="javascript:;" id="pageSizeSelect" class="button">
                            ${message("admin.page.pageSize")}<span class="arrow">&nbsp;</span>
                            </a>
                            <div class="popupMenu">
                                <ul id="pageSizeOption">
                                    <li>
                                        <a href="javascript:;"[#if page.pageSize == 10] class="current"[/#if] val="10">10</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;"[#if page.pageSize == 20] class="current"[/#if] val="20">20</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;"[#if page.pageSize == 50] class="current"[/#if] val="50">50</a>
                                    </li>
                                    <li>
                                        <a href="javascript:;"[#if page.pageSize == 100] class="current"[/#if]
                                           val="100">100</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <a href="javascript:;" id="changePriceButton" class="button disabled">批量改价</a>
                        <a href="javascript:;" id="multiUpMarketButton" class="button">批量上架</a>
                        <a href="javascript:;" id="export_ss" class="button">导出</a>
                    </div>
                    <div class="menuWrap">
                        <div class="search">
                            <input type="text" id="searchValue" name="searchValue" value="${page.searchValue}"
                                   placeholder="搜索商品名称、货号"/>
                            <button type="submit">&nbsp;</button>
                        </div>
                    </div>
                </div>
                <table id="listTable" class="list">
                    <tr>
                        <th class="check">
                            <input type="checkbox" id="selectAll"/>
                        </th>
                        <th>
                            <a href="javascript:;" class="sort" name="sn">${message("Product.sn")}</a>
                        </th>
                        [#if versionType=="1"]
                        <th>
                            <a href="javascript:;" class="sort" >条形码</a>
                        </th>
                        [/#if]
                        <th>
                            <a href="javascript:;" class="sort" name="name">${message("Product.name")}</a>
                        </th>
                        <th>
                            <a href="javascript:;">图片</a>
                        </th>
                        <th>
                            <a href="javascript:;" class="sort"
                               name="productCategory">${message("Product.productCategory")}</a>
                        </th>
                        <th>
                            <a href="javascript:;" class="sort" name="price">${message("Product.price")}</a>
                        </th>
                        <th>
                            <a href="javascript:;" class="sort" name="stock">${message("Product.stock")}</a>
                        </th>
                        <th>
                            <a href="javascript:;" class="sort"
                               name="isMarketable">${message("Product.isMarketable")}</a>
                        </th>
                        <th>
                            <span>${message("admin.common.handle")}</span>
                        </th>
                    </tr>
                [#list page.content as product]
                    <tr>
                        <td>
                            <input type="checkbox" name="ids" value="${product.id}"/>
                        </td>
                        <td>
                        ${product.sn}
                        </td>
                        [#if versionType=="1"]
                        <td>
                        ${(product.barcode)!}
                        </td>
                        [/#if]
                        <td>
								<span title="${product.fullName}">
									${abbreviate(product.fullName, 30, "..")}
                                        [#if product.isGift]
                                            <span class="gray">[${message("admin.product.gifts")}]</span>
                                        [/#if]
								</span>
                            [#list product.validPromotions as promotion]
                                <span class="promotion">${abbreviate(promotion.name,10,".")}</span>
                            [/#list]
                        </td>
                        <td>
                            [#if product.thumbnail?has_content]
                                <img src="${product.thumbnail}" alt="" width="30px" height="30px"/>
                            [/#if]
                        </td>
                        <td>
                            [#if product.productCategoryTenant??]${abbreviate(product.productCategoryTenant.name,16,"..")!}[/#if]
                        </td>
                        <td>
                        ${currency(product.price)}
                        </td>
                        <td style="cursor: pointer;" title="点击查看库存明细" onclick="showStocks('${product.id}')">
                        ${(product.goods.stock)!}
                        </td>
                        <td>
                            <span class="${product.isMarketable?string("true", "false")}Icon">&nbsp;</span>
                        </td>
                        <td>
                            [@helperRole url="helper/member/product/isMarketableList.jhtml" type="downMarket"]
                                [#if helperRole.retOper!="0"]
                                    <a href="javascript:;" class="downMarketable" productId="${product.id}">[下架]</a>
                                [/#if]
                            [/@helperRole]

                            [@helperRole url="helper/member/product/isMarketableList.jhtml" type="update"]
                                [#if helperRole.retOper!="0"]
                                    <a href="${base}/helper/member/product/edit.jhtml?id=${product.id}">[${message("shop.common.edit")}]</a>
                                [/#if]
                            [/@helperRole]
                            [@helperRole url="helper/member/product/isMarketableList.jhtml" type="read"]
                                [#if helperRole.retOper!="0"]
                                    <a target="_blank" href="${base}/[#if versionType=="1"]b2b[#else]b2c[/#if]/product/detail/${product.id}.jhtml">[查看]</a>
                                    [#if versionType==0]
                                        <a style="cursor: pointer;"
                                           onclick="getPropuctPreview('${base}/wap/product/content/${product.id}/preview.jhtml')">[预览]</a>
                                    [/#if]
                                [/#if]
                            [/@helperRole]
                        </td>
                    </tr>
                [/#list]
                </table>
            [#if !page.content?has_content]
                <p class="nothing">${message("box.member.noResult")}</p>
            [/#if]
            [@pagination pageNumber = page.pageNumber totalPages = page.totalPages]
                [#include "/helper/member/include/pagination.ftl"]
            [/@pagination]
            </form>
        </div>
    </div>
</div>
<div id="trade_wrap"></div>
[#include "/helper/include/footer.ftl" /]
</body>
</html>
