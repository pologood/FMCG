package net.wit.controller.admin;

import net.wit.*;
import net.wit.Message;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/7/15.
 */
@Controller("adminUnionController")
@RequestMapping("/admin/union")
public class UnionController extends BaseController {

    @Resource(name = "unionServiceImpl")
    private UnionService unionService;

    @Resource(name = "fileServiceImpl")
    private FileService fileService;

    @Resource(name = "productImageServiceImpl")
    private ProductImageService productImageService;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    /**
     * 联盟列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Pageable pageable, ModelMap model) {
        Page<Union> page =unionService.findPage(pageable);
        model.addAttribute("page", page);
        return "/admin/union/list";
    }

    /**
     * 联盟商家的列表
     */
    @RequestMapping(value = "/tenant_list", method = RequestMethod.GET)
    public String tenantList(Long id, Date beginDate, Date endDate,String keyword,Pageable pageable,ModelMap model) {
        Union union=unionService.find(id);
        if(union==null){
            return "redirect:list.jhtml";
        }
        List<Filter> filter = new ArrayList<Filter>();
        filter.add(new Filter("unions", Filter.Operator.eq, union));
        pageable.setFilters(filter);
        Page page = tenantService.openPage(pageable, null, beginDate, endDate, null, null, keyword, null, null,null,null);
        model.addAttribute("page", page);
        model.addAttribute("union",union);
        model.addAttribute("beginDate", beginDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("keyword",keyword);
        return "/admin/union/tenant_list";
    }

    /**
     * 添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {
        return "/admin/union/add";
    }

    /**
     * 添加保存
     * @param name
     * @param file
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(String name, MultipartFile file,BigDecimal price, BigDecimal brokerage_send) {
        Union union=new Union();
        union.setName(name);
        union.setBrokerage(brokerage_send);
        union.setTenantNumber(0);
        union.setPrice(price);
        if (file != null && !file.isEmpty()) {
            if (!fileService.isValid(FileInfo.FileType.image, file)) {
                return "redirect:add.jhtml";
            } else {
                ProductImage img = new ProductImage();
                img.setFile(file);
                productImageService.build(img);
                union.setImage(img.getThumbnail());
            }
        }
        unionService.save(union);
        return "redirect:list.jhtml";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        Union union=unionService.find(id);
        model.addAttribute("union",union);
        return "/admin/union/edit";
    }


    /**
     * 联盟信息编辑提交
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Long unionId,String name, MultipartFile file, BigDecimal brokerage_send,BigDecimal price ) {
        Union union=unionService.find(unionId);
        if(union==null){
            return "redirect:edit.jhtml";
        }
        union.setName(name);
        union.setBrokerage(brokerage_send);
        union.setPrice(price);
        if (file != null && !file.isEmpty()) {
            if (!fileService.isValid(FileInfo.FileType.image, file)) {
                return "redirect:edit.jhtml";
            } else {
                ProductImage img = new ProductImage();
                img.setFile(file);
                productImageService.build(img);
                union.setImage(img.getThumbnail());
            }
        }
        unionService.update(union);
        return "redirect:list.jhtml";
    }


    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public Message delete(Long[] ids) {
        if(ids!=null){
            for(Long id:ids){
                Union union=unionService.find(id);
                if(union==null){
                    continue;
                }
                if(union.getTenantNumber()==0){
                    unionService.delete(union);
                }else{
                    return Message.warn("商盟存在商家，不可删除。");
                }
            }
        }
        return Message.success("操作成功");
    }
}
