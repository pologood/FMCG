{
	"areas":[
			         [#list areas as city]
		    	           [#if city_index=0]
			                  {"id":"${city.id}","name":"${city.name}","tag":"0","phonetic":"${phonetic(city.name)}"}
                           [#else]
			                 ,{"id":"${city.id}","name":"${city.name}","tag":"0","phonetic":"${phonetic(city.name)}"}
                           [/#if]
			          [/#list]
	],
	"timestamp":1000
}