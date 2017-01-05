<div class="slogen container">
    <!--ul>
        <li><i class="icon01"></i><span>数据互通</span></li>
        <li><i class="icon02"></i><span>正品保障</span></li>
        <li><i class="icon03"></i><span>闪电发货</span></li>
        <li><i class="icon04"></i><span>无忧退货</span></li>
        <li><i class="icon05"></i><span>400-880-68568</span></li>
    </ul-->

[@ad_position id=98 areaId=area.id count=1/]
</div>

<div class="slogen container">
    <!--ul>
        <li><i class="icon01"></i><span>数据互通</span></li>
        <li><i class="icon02"></i><span>正品保障</span></li>
        <li><i class="icon03"></i><span>闪电发货</span></li>
        <li><i class="icon04"></i><span>无忧退货</span></li>
        <li><i class="icon05"></i><span>400-880-68568</span></li>
    </ul-->
[@ad_position id=124 areaId=area.id count=1/]
</div>
[@friend_link_list]
    [#if friendLinks??&&friendLinks?has_content]
    <div style="clear:both;width:1200px;height: 24px; margin:0 auto;">
        <dl>
            <dt style="display: inline-block;float: left;"> 友情链接：</dt>

            [#list friendLinks as frindlink]
                <dd style="display: inline-block;float: left;padding-left: 6px;">
                    <a href="${frindlink.url}"  target="_blank"><span>${frindlink.name}</span></a>
                </dd>
            [/#list]

        </dl>
    </div>
    [/#if]
[/@friend_link_list]