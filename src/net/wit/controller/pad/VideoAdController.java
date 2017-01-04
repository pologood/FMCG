package net.wit.controller.pad;

import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.controller.pad.model.VideoAdModel;
import net.wit.entity.*;
import net.wit.service.EquipmentService;
import net.wit.service.MemberService;
import net.wit.service.VideoAdService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller("padVideoAdController")
@RequestMapping("/pad/videoad")
/**
 * 视频广告
 * Created by Administrator on 2016/11/9.
 */

public class VideoAdController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "videoAdServiceImpl")
    private VideoAdService videoAdService;

    @Resource(name = "equipmentServiceImpl")
    private EquipmentService equipmentService;

    //列表
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list(String uuid) {
        Equipment equipment = equipmentService.findByUUID(uuid);

        Tenant tenant = equipment.getTenant();
        if (tenant == null) {
            return DataBlock.error("还没有开通店铺，快去申请吧。");
        }
        List<Video> date = videoAdService.findList(tenant);
        return DataBlock.success(VideoAdModel.bindData(date), "执行成功");
    }
}
