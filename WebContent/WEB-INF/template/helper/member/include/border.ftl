<div class="span4 border2">
    <div class="account_pic">
        <a style="cursor: pointer;" [#if owner??]onclick="roleRoot('${base}/helper/member/tenant/edit.jhtml','owner,manager')"[/#if]>
            <img class="logo_bg2"
                 src="[#if owner??]${owner.tenant.logo}[#else]${base}/resources/helper/images/head_img01.gif[/#if]"/>
        </a>
    </div>

    <div class="account_user">
        <p>
[@helperRole url='helper/member/deposit/statistics.jhtml']
            <a style="cursor: pointer;" [#if owner??]onclick="roleRoot('${base}/helper/member/tenant/edit.jhtml','${helperRole.retUrl}');"[/#if]>
            [#if owner??]
					${owner.tenant.shortName!owner.name!mosaic(owner.username, 3, "~~~")}
				[/#if]
            </a>
[/@helperRole]
        </p>
    </div>

    <div class="account_level">
        <img src="${base}/resources/helper/img/level_xin_0[#if owner??]${owner.tenant.score}[#else]0[/#if].png" alt="">
    </div>
    <!-- 此处插入的img为等级图片，替换顺序为level_xin_00~~~level_xin_10 -->
</div>
<ul class="account">
    <li>
        <div class="li_account">账户余额</div>
        <div class="li_balance"><a style="cursor: pointer;">[#if owner??]${owner.balance?string("0.00")}[#else]0.00[/#if]元</a></div>
        <div style=" float: right; width: 50%; margin-top: -45px; ">
        [#if owner??]<a style="cursor: pointer;" onclick="roleRoot('${base}/helper/member/deposit/fill.jhtml','owner,account');"
                        class="button_money">充值</a>[/#if]
        [#if owner??]<a style="cursor: pointer;" onclick="roleRoot('${base}/helper/member/cash/index.jhtml','owner,account');"
                        class="button_money">提现</a>[/#if]</div>
    </li>
    <li>
        <div class="li_account">冻结金额</div>
        <div class="li_balance"><a style="cursor: pointer;">[#if owner??]${owner.freezeBalance?string("0.00")}[#else]0.00[/#if]元</a></div>
    </li>
    <li>
        <div class="li_account">库存金额</div>
        <div class="li_balance"><a style="cursor: pointer;">[#if StockAmount??]${StockAmount?string("0.00")}[#else]0.00[/#if]元</a></div>
    </li>
    <li style="border-right:0px;">

[@helperRole url='helper/member/deposit/statistics.jhtml']
        <div class="li_account" style="cursor: pointer;"
             onclick="roleRoot('${base}/helper/member/deposit/thismonthlist.jhtml','${helperRole.retUrl}');">本月账单明细</div>
        <div class="li_account" style="cursor: pointer;"
             onclick="roleRoot('${base}/helper/member/deposit/statistics.jhtml','${helperRole.retUrl}');">本月账单统计</div>
[/@helperRole]
    </li>
</ul>