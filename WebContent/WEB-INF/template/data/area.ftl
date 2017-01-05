var citys;
function initCity() {
  citys = [
			         [#list areas as city]
		    	           [#if city_index=0]
			                  {id:"${city.id}",name:"${city.name}",status:"${city.status}",phonetic:"${phonetic(city.name)}",tag:0}
                           [#else]
			                 ,{id:"${city.id}",name:"${city.name}",status:"${city.status}",phonetic:"${phonetic(city.name)}",tag:0}
                           [/#if]
			          [/#list]
	]
}
function getAreas(phonetic) {
    var areas = [];
	for(var i=0;i<citys.length;i++)
	{
	  if (citys[i].phonetic==phonetic) {
	     areas.push(citys[i]);
	  }
	}	
	return areas;
}


function getTagAreas(status) {
    var areas = [];
	for(var i=0;i<citys.length;i++)
	{
	  if (citys[i].status==status) {
	     areas.push(citys[i]);
	  }
	}	
	return areas;
}