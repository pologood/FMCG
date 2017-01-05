<!-- div  class="elevator">
    <ul>
    [#list rootCategory as category]
        [#assign d="false"]
        [#list category.products as product]
            [#list product.tags as tag]
                [#if tag.id=5]
                    [#assign d="true"]
                [/#if]
            [/#list]
        [/#list]
        [#if d=="true"]
            <li number="${category_index+1}"><a href="javascript:;">${category.name}</a></li>
        [/#if]
    [/#list]
    </ul>
</div -->