package net.wit.controller.app.member;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.app.BaseController;
import net.wit.controller.app.model.*;
import net.wit.entity.*;
import net.wit.entity.ProductChannel.Type;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 购物频道控制层
 * Created by thwapp on 2016/2/22.
 */
@Controller("appMemberChannelController")
@RequestMapping("/app/member/channel")
public class ChannelController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "productChannelServiceImpl")
    private ProductChannelService productChannelService;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "tagServiceImpl")
    private TagService tagService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    /**
     * 集市频道列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public DataBlock list() {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        List<ProductChannel> productChannelList = productChannelService.findByType(Type.product);

        return DataBlock.success(ProductChannelModel.bindData(productChannelList),"执行成功");
    }

    /**
     * 集市频道详情
     */
    @RequestMapping(value = "/infoList", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock infoList(Long id,Long[] tagIds,Pageable pageable) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }

        ProductChannel channel = productChannelService.find(id);
        if (channel == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        Area area = areaService.getCurrent();

        List<Tag> tags = tagService.findList(tagIds);
        Page<Tenant> page = tenantService.findPage(channel.getTenantCategorys(),tags, area, null, null, null, null, pageable);

        List<TenantListModel> models = TenantListModel.bindData(page.getContent());

        return DataBlock.success(models,"执行成功");
    }
}
