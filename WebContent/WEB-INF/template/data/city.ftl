var cities=[
        [#assign i=-1]
        [#assign arr=["a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"]]
        [#list arr as str]
            [#if str_index==0]
                [#list areas as city]
                    [#if phonetic(city.name)==str]
                        [#assign i=city_index]
                        [#break /]
                    [/#if]
                [/#list]
                [#if i!=-1]
                    {"${str}":[
                        [#list areas as city]
                            [#if phonetic(city.name)==str]
                                [#if city_index==i]
                                    {"id":"${city.id}","name":"${city.name}","tag":"0","phonetic":"${phonetic(city.name)}"}
                                [#else]
                                    ,{"id":"${city.id}","name":"${city.name}","tag":"0","phonetic":"${phonetic(city.name)}"}
                                [/#if]
                            [/#if]
                        [/#list]
                    ]}
                [/#if]
            [#else]
                [#list areas as city]
                    [#if phonetic(city.name)==str]
                        [#assign i=city_index]
                        [#break /]
                    [/#if]
                [/#list]
                [#if i!=-1]
                    ,{"${str}":[
                        [#list areas as city]
                            [#if phonetic(city.name)==str]
                                [#if city_index==i]
                                    {"id":"${city.id}","name":"${city.name}","tag":"0","phonetic":"${phonetic(city.name)}"}
                                [#else]
                                    ,{"id":"${city.id}","name":"${city.name}","tag":"0","phonetic":"${phonetic(city.name)}"}
                                [/#if]
                            [/#if]
                        [/#list]
                    ]}
                [/#if]
            [/#if]
            [#assign i=-1]
        [/#list]

    ]