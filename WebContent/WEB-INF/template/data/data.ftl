var total = ${total};
$().ready(function() {
   $("#total").text(total+"个配件等您选购");
});

var brands;
var brandPhonetic;
var brandTags;
function initBrand() {
  brandTags = [
		    	[#list tags as tag]
		    	   [#if tag_index=0]
                       {id:"${tag.id}",name:"${tag.name}"}
                   [#else]
                      ,{id:"${tag.id}",name:"${tag.name}"}
                   [/#if]
				[/#list]
	]
  brandPhonetic=${brandPhonetic};
  brands = [
   [@brand_list]
		    	[#list brands as brand]
		    	   [#if brand_index=0]
                 {id:"${brand.id}",name:"${brand.name}",phonetic:"${brand.phoneticChar}",logo:"${brand.logo}",tags:",[#list brand.tags as tag]${tag.id},[/#list]"}
             [#else]
                ,{id:"${brand.id}",name:"${brand.name}",phonetic:"${brand.phoneticChar}",logo:"${brand.logo}",tags:",[#list brand.tags as tag]${tag.id},[/#list]"}
             [/#if]
					[/#list]
	 [/@brand_list]   
	]
}
var citys;
function initCity() {
  citys = [
   [@area_children_list]
			[#list areas as area]
		    	   [#if area_index=0]
					      {id:"${area.id}",name:"${area.name}",childrens:[
             [#else]
					     ,{id:"${area.id}",name:"${area.name}",childrens:[
             [/#if]
					      [@area_children_list areaId=area.id]
			             [#list areas as city]
		    	           [#if city_index=0]
			                  {id:"${city.id}",name:"${city.name}",childrens:[
                     [#else]
			                 ,{id:"${city.id}",name:"${city.name}",childrens:[
                     [/#if]
         					      [@area_children_list areaId=city.id]
			                    [#list areas as qy]
			                      [#if qy_index=0] 
			                         {id:"${qy.id}",name:"${qy.name}"}
			                      [#else]
			                        ,{id:"${qy.id}",name:"${qy.name}"}
			                      [/#if]
			                    [/#list]
			                  [/@area_children_list]   
			               ]}
			             [/#list]
			          [/@area_children_list]   
					   ]}
			[/#list]
	 [/@area_children_list]   
	]
}
var categorys;
function initCategory() {
  categorys = [
   [@product_category_root_list]
			[#list productCategories as productCategory]
			    [#if productCategory_index=0] 
					    {id:"${productCategory.id}",name:"${productCategory.name}",tag:"0",childrens:[
					[#else]
					   ,{id:"${productCategory.id}",name:"${productCategory.name}",tag:"0",childrens:[
					[/#if]
			             [#list productCategory.children as children1]
			               [#if children1_index=0] 
			                  {id:"${children1.id}",name:"${children1.name}",tag:"0",childrens:[
			               [#else]
			                 ,{id:"${children1.id}",name:"${children1.name}",tag:"0",childrens:[
			               [/#if]
			                    [#list children1 as children2]
			                      [#if children1_index=0] 
			                         {id:"${children2.id}",name:"${children2.name}",tag:"0"}
			                      [#else]
			                        ,{id:"${children2.id}",name:"${children2.name}",tag:"0"}
			                      [/#if]
			                    [/#list]
			               ]}
			             [/#list]
					   ]}
			[/#list]
	 [/@product_category_root_list]   
	]
}

var categoryes;
function initCategoryes() {
  categoryes = [
		     	[@product_category_root_list]
		    	[#list productCategories as productCategory]
					 [#if productCategory_index=0] 
					    {id:"${productCategory.id}",name:"${productCategory.name}",tag:"0",childrens:[
					[#else]
					   ,{id:"${productCategory.id}",name:"${productCategory.name}",tag:"0",childrens:[
					[/#if]
						
						[#list productCategory.children as children1]
							[#if children1_index=0] 
			                  {id:"${children1.id}",name:"${children1.name}",tag:"0",childrens:[
			               [#else]
			                 ,{id:"${children1.id}",name:"${children1.name}",tag:"0",childrens:[
			               [/#if]
						    [#list children1.children as children2]
								[#if children2_index=0]
			                         {id:"${children2.id}",name:"${children2.name}",tag:"0"}
			                    [#else]
			                        ,{id:"${children2.id}",name:"${children2.name}",tag:"0"}
			                    [/#if]
						    [/#list]
							]}
						[/#list]
						]}
					[/#list]
	    	[/@product_category_root_list]
  ]
}

var friendlinkes;
function initfriendlinks() {
  friendlinkes = [
		     	[@friend_link_list]
		    	[#list friendLinks as frindlink]
				[#if frindlink_index=0]
					{name:"${frindlink.name}",link:"${frindlink.url}",logo:"${frindlink.logo}"}
					[#else]
					   ,{name:"${frindlink.name}",link:"${frindlink.url}",logo:"${frindlink.logo}"}
					[/#if]
					[/#list]
	    	[/@friend_link_list]
  ]
}


