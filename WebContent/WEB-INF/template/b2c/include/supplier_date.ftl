<div class="imenu">
    <div class="seone">
        <p data-value="${dateRange}">${dateRange}</p>
        <ul>
            <li data-value="今天" onclick="selectToday()">今天</li>
            <li data-value="昨天" onclick="selectYesterday()">昨天</li>
            <li data-value="本周" onclick="selectWeek()">本周</li>
            <li data-value="本月" onclick="selectMonth()">本月</li>
            <li data-value="本年" onclick="selectYear()">本年</li>
        </ul>
    </div>
    <input name="begin_date" type="text" value="[#if begin_date??]${begin_date?string('MM/dd/yyyy')}[/#if]"
           id="dpd1" class="ls-input">
    <input name="end_date" type="text" value="[#if end_date??]${end_date?string('MM/dd/yyyy')}[/#if]" id="dpd2"
           class="ls-input">
[#if seller??&&seller?has_content||status??&&status?has_content]
    <div class="select">
        [#if seller??&&seller?has_content]
            <p data-value="${(seller.id)!}">${(seller.name)!}</p>
            <ul>
                [#list list as tenants]
                    <li data-value="${tenants.tenant.name}" onclick="getAccountId(${tenants.tenant.id})">
                    ${tenants.tenant.name}
                    </li>
                [/#list]
            </ul>
        [/#if]
        [#if status??&&status?has_content]
            <p data-value="${status}" id="current_account">${status}</p>
            <ul>
                <li data-value="已结算" onclick="getAccountId(this)">
                    已结算
                </li>
                <li data-value="未结算" onclick="getAccountId(this)">
                    未结算
                </li>
            </ul>
        [/#if]
    </div>
[/#if]
    <!--  <i class="iconfont" style="float:left;line-height: 66px;color:#fff;">&#xe608;</i> -->
    <input name="keyWords" value="${keyWords!}" type="text" placeholder="输入条形码/商品名称" class="sp-input">
    <a href="#" class="ex-item bg-pink" onclick="supplierSearch()">
        <i class="iconfont" style="font-size:20px;">&#xe609;</i>&nbsp;查询</a>
    <!--<a href="#" class="ex-item bg-dark-blue"><i class="iconfont" style="font-size:20px;">&#xe60a;</i>&nbsp;打印</a>-->
</div>
<script type="text/javascript">
    $(function () {
        $('#js_inform').fdatepicker();
        var nowTemp = new Date();
        var now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0);
        //第一个日期
        var checkin = $('#dpd1').fdatepicker({
            onRender: function (date) {
//                return date.valueOf() < now.valueOf() ? 'disabled' : '';
            }
        }).on('changeDate', function (ev) {
            if (ev.date.valueOf() > checkout.date.valueOf()) {
                var newDate = new Date(ev.date);
                newDate.setDate(newDate.getDate() + 1);
                checkout.update(newDate);
            }
            checkin.hide();
            $('#dpd2')[0].focus();
        }).data('datepicker');
        //第二个日期
        var checkout = $('#dpd2').fdatepicker({
            onRender: function (date) {
                return date.valueOf() <= checkin.date.valueOf() ? 'disabled' : '';
            }
        }).on('changeDate', function (ev) {
            checkout.hide();
        }).data('datepicker');


        $(".select p").click(function (e) {
            $(".select").toggleClass('open');
            e.stopPropagation();
        });
        $(".select ul li").click(function (e) {
            var _this = $(this);
            $(".select > p").text(_this.text());
            $(".select > p").attr("data-value", _this.attr('data-value'));
            _this.addClass("Selected").siblings().removeClass("Selected");
            $(".select").removeClass("open");
            e.stopPropagation();
        });
        $(document).on('click', function () {
            $(".select").removeClass("open");
        });

        //报表日期范围选择
        $(".seone p").click(function (e) {
            $(".seone").toggleClass('open');
            e.stopPropagation();
        });
        $(".seone ul li").click(function (e) {
            var _this = $(this);
            $(".seone > p").text(_this.text());
            $(".seone > p").attr("data-value", _this.attr('data-value'));
            _this.addClass("Selected").siblings().removeClass("Selected");
            $(".seone").removeClass("open");
            e.stopPropagation();
        });
        $(document).on('click', function () {
            $(".seone").removeClass("open");
        });

    });

    function supplierSearch() {
        $("#dateRange").val($(".seone > p").text());
        //$("#sellerId").val($(".select > p").attr("data-value"));
        //$("#pageNumber").val(1);
        $('#listForm').submit();
    }

    function selectToday() {
        var d1 = new Date();
        var d2 = new Date(d1);
        d2.setDate(d2.getDate() + 1);
        $("#dpd1").val(dateFormat(d1));
        $("#dpd2").val(dateFormat(d2));
    }
    function selectYesterday() {
        var d1 = new Date();
        var d2 = new Date(d1);
        d1.setDate(d1.getDate() - 1);
        $("#dpd1").val(dateFormat(d1));
        $("#dpd2").val(dateFormat(d2));
    }
    function selectWeek() {
        var d1 = new Date();
        var d2 = new Date(d1);
        if (d1.getDay() == 7) {
            d1.setDate(d1.getDate() - 6);
        } else {
            d1.setDate(d1.getDate() - d1.getDay() + 1);
        }
        d2.setDate(d2.getDate() + 1);
        $("#dpd1").val(dateFormat(d1));
        $("#dpd2").val(dateFormat(d2));
    }

    function selectMonth() {
        var d1 = new Date();
        var d2 = new Date(d1);
        d1.setDate(1);
        d2.setDate(d2.getDate() + 1);
        $("#dpd1").val(dateFormat(d1));
        $("#dpd2").val(dateFormat(d2));
    }

    function selectYear() {
        var d1 = new Date();
        var d2 = new Date(d1);
        d1.setMonth(0);
        d1.setDate(1);
        d2.setDate(d2.getDate() + 1);
        $("#dpd1").val(dateFormat(d1));
        $("#dpd2").val(dateFormat(d2));
    }

    function dateFormat(d) {
        var date = new Date(d);
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var day = date.getDate();
        return (month < 10 ? ("0" + month) : month) + "/" + (day < 10 ? ("0" + day) : day) + "/" + year;
    }

    function getAccountId(obj) {
        $("#status").val($(obj).attr("data-value"));
    }


</script>
