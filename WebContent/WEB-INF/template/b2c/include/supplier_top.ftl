<ul class="balance-item">
    <li class="bg-pink" style=" width: 54.5%; ">
        <div class="f-left balance">
            <div class="box"><p>余额：</p><span class="sum" title="${member.balance}">￥${(member.tenant.balance)!}</span></div>
            <div class="box"><p>冻结金额：</p><span class="small-sum" id="withdrawBalance">￥${(member.tenant.freezeBalance)!}</span></div>
        </div>
        <a href="${base}/b2c/member/cash/withdraw_index.jhtml" class="withdraw">提现</a>
        <a href="${base}/b2c/member/supplier/bill.jhtml" class="withdraw">查看账单</a>
        <div style=" width: 55%; height: 28px; overflow: hidden; margin-bottom: 10px; color: #fff; padding-left: 4%; padding-top: 6px; ">
            <p style=" float: left; font-size: 18px; line-height: 36px; text-overflow: ellipsis; -o-text-overflow: ellipsis; -khtml-text-overflow: ellipsis; -moz-text-overflow: ellipsis; -webkit-text-overflow: ellipsis; overflow: hidden; ">库存金额：</p>
            <span class="small-sum" id="withdrawBalance">￥${SuppilerAmount}</span>
        </div>
    </li>
    <li class="bg-light_blue">
        <div class="contact">
            <span>联系方式</span>
            <h4>
                <i class="iconfont" style="font-size:28px;">&#xe605;</i>
                ${setting.phone}
            </h4>
        </div>
    </li>
</ul>
