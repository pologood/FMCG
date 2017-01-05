var tenantCategorys;
function initTenantCategory() {
	tenantCategorys = [
   		[@tenant_category_root_list]
			[#list tenantCategories as tenantCategory]
			    [#if tenantCategory_index=0] 
					    {id:"${tenantCategory.id}",name:"${tenantCategory.name}",tag:"0",image:"${tenantCategory.image}",childrens:[
					[#else]
					   ,{id:"${tenantCategory.id}",name:"${tenantCategory.name}",tag:"0",image:"${tenantCategory.image}",childrens:[
					[/#if]
			             [#list tenantCategory.children as children1]
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
	 	[/@tenant_category_root_list]
	]
}
function getTenantCategoryChildRens(id) {
	for(var i=0;i<tenantCategorys.length;i++)
	{
	  if (tenantCategorys[i].id==id) {
	     return tenantCategorys[i].childrens;
	  }
	}	
}