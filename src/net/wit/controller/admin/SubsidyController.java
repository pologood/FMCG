package net.wit.controller.admin;





import net.wit.*;
import net.wit.controller.store.model.DataBlock;
import net.wit.entity.*;
import net.wit.entity.Message;
import net.wit.service.*;
import net.wit.support.EntitySupport;
import net.wit.util.SettingUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by Administrator on 2016/7/15.
 */
@Controller("adminSubsidyController")
@RequestMapping("/admin/subsidy")
public class SubsidyController extends BaseController {

    @Resource(name = "fileServiceImpl")
    private FileService fileService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    @Resource(name = "subsidiesServiceImpl")
    private SubsidiesService subsidiesService;

    @Resource(name = "subsidiesItemServiceImpl")
    private SubsidiesItemService subsidiesItemService;

    /**
     * 列表
     */
    @RequestMapping(value = "/subsidy_list", method = RequestMethod.GET)
    public String list(Pageable pageable, ModelMap model) {
        model.addAttribute("page", subsidiesService.findPage(pageable));
        return "/admin/subsidy/subsidy_list";
    }

    /** 登记详情列表导出 */
    @RequestMapping(value = "/confirm_subsidy", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock markedListExport(MultipartFile file, String type,String remark,String message) {
        List<Map<String,String>> lists= null;
        BigDecimal amount=BigDecimal.ZERO;
        Integer count=0;
        try {
            lists = subsidiesService.readExl(file);
        } catch (Exception e) {
            return DataBlock.error("导入excel失败！");
        }

        Subsidies.Type t=null;
        if(type!=null){
            if("recharge".equals(type)){
                t= Subsidies.Type.recharge;
            }else if("receipts".equals(type)){
                t= Subsidies.Type.receipts;
            }else if("profit".equals(type)){
                t= Subsidies.Type.profit;
            }else{
                t= Subsidies.Type.other;
            }
        }
        try {
            List<SubsidiesItem> subsidiesItemlist=new ArrayList<SubsidiesItem>();

            Subsidies subsidies=new Subsidies();
            subsidies.setType(t);
            subsidies.setRemark(remark);
            subsidies.setMessage(message);
            subsidies.setCount(lists.size());
            subsidies.setAmount(amount);
            subsidies.setSubsidiesItems(subsidiesItemlist);
            subsidiesService.save(subsidies);

            for(Map<String,String> map:lists){
                if(map.get("obj5")!=null&&map.get("obj2")!=null){
                    amount=amount.add(new BigDecimal(map.get("obj5")));//总金额
                    count=count+1;
                    SubsidiesItem subsidiesItem=new SubsidiesItem();
                    subsidiesItem.setAmount(new BigDecimal(map.get("obj5")));
                    subsidiesItem.setTenantName(map.get("obj1"));
                    subsidiesItem.setUsername(map.get("obj2"));
                    subsidiesItem.setSubsidies(subsidies);
                    subsidiesItem.setStatus(SubsidiesItem.Status.no);
                    subsidiesItemService.save(subsidiesItem);
                    subsidies.getSubsidiesItems().add(subsidiesItem);
                }
            }
            subsidies.setAmount(amount);
            subsidies.setCount(count);
            subsidiesService.update(subsidies);
        } catch (Exception e) {
            e.printStackTrace();
            return DataBlock.error("读取数据失败！");
        }
        return DataBlock.success(lists,"操作成功!");
    }

    /** 审查数据 */
    @RequestMapping(value = "/confirm_recharge", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock confirmRecharge(Long id) {
        Map<String,Object> map=new HashMap<String,Object>();
        Subsidies subsidies=subsidiesService.find(id);
        if(subsidies==null){
            return DataBlock.error("充值失败!");
        }
        try {
            map=subsidiesService.recharge(subsidies);
        } catch (Exception e) {
            e.printStackTrace();
            return DataBlock.error("充值失败!");
        }
        return DataBlock.success(map,"充值成功!");
    }


    /**
     * 补贴项列表
     */
    @RequestMapping(value = "/child_subsidy_list", method = RequestMethod.GET)
    public String childSubsidy(Long id,Pageable pageable,ModelMap model) {
        Subsidies subsidies=subsidiesService.find(id);
        List<Filter> filter = new ArrayList<Filter>();
        filter.add(new Filter("subsidies", Filter.Operator.eq, subsidies));
        pageable.setFilters(filter);
        Page<SubsidiesItem> page=subsidiesItemService.findPage(pageable);
        model.addAttribute("page", page);
        return "/admin/subsidy/child_subsidy_list";
    }







}
