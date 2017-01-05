var categorys;
function initCategory() {
  categorys = [
   [@product_category_root_list]
			[#list productCategories as productCategory]
			    [#if productCategory_index=0] 
					    {id:"${productCategory.id}",name:"${productCategory.name}",tag:"0",image:"${productCategory.image}",childrens:[
					[#else]
					   ,{id:"${productCategory.id}",name:"${productCategory.name}",tag:"0",image:"${productCategory.image}",childrens:[
					[/#if]
			             [#list productCategory.children as children1]
			               [#if children1_index=0] 
			                  {id:"${children1.id}",name:"${children1.name}",tag:"0",image:"${children1.image}",childrens:[
			               [#else]
			                 ,{id:"${children1.id}",name:"${children1.name}",tag:"0",image:"${children1.image}",childrens:[
			               [/#if]
			                    [#list children1.children as children2]
			                      [#if children2_index=0] 
			                         {id:"${children2.id}",name:"${children2.name}",tag:"0",image:"${children2.image}"}
			                      [#else]
			                        ,{id:"${children2.id}",name:"${children2.name}",tag:"0",image:"${children2.image}"}
			                      [/#if]
			                    [/#list]
			               ]}
			             [/#list]
					   ]}
			[/#list]
	 [/@product_category_root_list]   
	]
}
function getCategoryChildRens(id) {
	for(var i=0;i<categorys.length;i++)
	{
	  if (categorys[i].id==id) {
	     return categorys[i].childrens;
	  }
	}	
}