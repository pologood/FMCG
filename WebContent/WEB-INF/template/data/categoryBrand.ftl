var categoryBrands;
function initCategoryBrand() {
categoryBrands = [
[@product_brand_root_list]
    [#list productBrands as productBrand]
        [#if productBrand_index=0]
        {id:"${productBrand.id}",name:"${productBrand.name}",tag:"0",image:"${productBrand.image}",childrens:[{childrens:[
        [#else]
        ,{id:"${productBrand.id}",name:"${productBrand.name}",tag:"0",image:"${productBrand.image}",childrens:[{childrens:[
        [/#if]
        [#list productBrand.brands as brand]
            [#if brand_index=0]
            {id:"${brand.id}",name:"${brand.name}",tag:"0",image:"${brand.logo}",childrens:[
            [#else]
            ,{id:"${brand.id}",name:"${brand.name}",tag:"0",image:"${brand.logo}",childrens:[
            [/#if]
        ]}
        [/#list]
    ]}]}
    [/#list]
[/@product_brand_root_list]
]}
function getBrandChildRens(id) {
for(var i=0;i<categoryBrands.length;i++)
{
if (categoryBrands[i].id==id) {
return categoryBrands[i].childrens;
}
}
}

