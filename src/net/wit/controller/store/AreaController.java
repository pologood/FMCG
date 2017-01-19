package net.wit.controller.store;

import net.wit.controller.store.model.DataBlock;
import net.wit.entity.Area;
import net.wit.service.AreaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 区域
 * Created by ruanx on 2017/1/11.
 */
@Controller("storeAreaController")
@RequestMapping("/store/area")
public class AreaController {
    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    /**
     * 省市区
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> all() {
        List<Area> areas = areaService.findRoots();
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> state = new HashMap<>();
        state.put("expanded",false);
        for (Area area : areas) {
            Map<String, Object> map = new HashMap<>();
            map.put("text", area.getName());
            map.put("href", area.getId());
            map.put("tags", area.getFullName());
            map.put("state", state);
            //中国不用放
            if(area.getId()!=3323L){
                list.add(map);
            }
            Area parent = areaService.find(area.getId());
            List<Map<String, Object>> clist = new ArrayList<>();
            if(parent!=null&&area.getId()!=3323L){
                List<Area>  childrens = new ArrayList<>(parent.getChildren());
                for (Area children : childrens) {
                Map<String, Object> map1 = new HashMap<>();
                    map1.put("text", children.getName());
                    map1.put("href", children.getId());
                    map1.put("tags", children.getFullName());
                    clist.add(map1);
                    Area parent1 = areaService.find(children.getId());
                    List<Map<String, Object>> slist = new ArrayList<>();
                    if(parent1!=null){
                        List<Area>  sons = new ArrayList<>(parent1.getChildren());
                        if(sons.size()>0){
                            for (Area son : sons) {
                                Map<String, Object> map2 = new HashMap<>();
                                map2.put("text", son.getName());
                                map2.put("href", son.getId());
                                map2.put("tags", son.getFullName());
                                slist.add(map2);
                            }
                            map1.put("nodes", slist);
                        }
                    }
                }
                map.put("nodes", clist);
            }
        }
        return list;
    }
}
