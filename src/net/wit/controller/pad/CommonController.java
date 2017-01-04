package net.wit.controller.pad;

import net.wit.Setting;
import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.EquipmentModel;
import net.wit.entity.Equipment;
import net.wit.service.EquipmentService;
import net.wit.util.SettingUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 购物屏检测
 * Created by ruanx on 2016/11/11.
 */
@Controller("padCommonController")
@RequestMapping("/pad/common")
public class CommonController {

    @Resource(name = "equipmentServiceImpl")
    private EquipmentService equipmentService;

    /**
     * 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0
     * @param version1
     * @param version2
     * @return
     */
    public int compareVersion(String version1, String version2) throws Exception {
        if (version1 == null || version2 == null) {
            throw new Exception("compareVersion error:illegal params.");
        }
        String[] versionArray1 = version1.split("\\.");//注意此处为正则匹配，不能用"."；
        String[] versionArray2 = version2.split("\\.");
        int idx = 0;
        int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值
        int diff = 0;
        while (idx < minLength
                && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
            ++idx;
        }
        //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }

    /**
     * 版本检测  店家助手 appname=helper  大屏 appname=pad  买家挑货网  appname=buyer
     */
    @RequestMapping(value = "/version", method = RequestMethod.GET)
    public @ResponseBody
    DataBlock version(String appname, String version) {
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        try {
            String curVersion = null;
            String maxVersion = null;
            String codeVersion = null;
            if (bundle.containsKey(appname+".version")) {
                curVersion = bundle.getString(appname+".version");
            }
            if (bundle.containsKey(appname+".max.version")) {
                maxVersion = bundle.getString(appname+".max.version");
            }

            if (bundle.containsKey(appname+".code")) {
                codeVersion = bundle.getString(appname+".code");
            }
            if (curVersion==null) {
                curVersion = "1.0.0";
            }
            if (maxVersion==null) {
                maxVersion = "1.0.0";
            }
            if (codeVersion==null) {
                codeVersion = "0";
            }
            String url = null;
            if (bundle.containsKey(appname+".url")) {
                url = bundle.getString(appname+".url");
            }
            Map<String,String> data = new HashMap<String,String>();

            if (compareVersion(version,maxVersion)<0) {
                if (compareVersion(version,curVersion)<0) {
                    data.put("url",url);
                    data.put("type","force");
                    data.put("size","11.3M");
                    data.put("descr","1.店招图片优化，更清晰。\r2.门店编码自动分配，更便捷。\r3.其他bug修复。");
                    data.put("version", curVersion);
                    data.put("codeVersion", codeVersion);
                } else {
                    data.put("url",url);
                    data.put("size","11.3M");
                    data.put("type","wake");
                    data.put("descr","1.店招图片优化，更清晰。\r2.门店编码自动分配，更便捷。\r3.其他bug修复。");
                    data.put("version", curVersion);
                    data.put("codeVersion", codeVersion);
                }
            } else {
                data.put("url",url);
                data.put("type","none");
                data.put("size","11.3M");
                data.put("descr","你已经最新版本了");
                data.put("version", curVersion);
                data.put("codeVersion", codeVersion);
            }
            return DataBlock.success(data,"success");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return DataBlock.error("检测版本失败");
        }
    }

    /**
     * 检测PAD参数
     */
    @RequestMapping(value = "/equipment", method = RequestMethod.GET)
    public @ResponseBody DataBlock Equipment(String uuid) {
        Equipment equipment = equipmentService.findByUUID(uuid);
        if (equipment==null) {
            equipment = new net.wit.entity.Equipment();
            equipment.setStatus(Equipment.Status.none);
            equipment.setUuid(uuid);
            equipment.setOperatingSystem(Equipment.OperatingSystem.andriod);
            equipment.setDeposit(BigDecimal.ZERO);
            equipment.setFee(BigDecimal.ZERO);
            equipment.setTotalAmount(BigDecimal.ZERO);
            equipment.setTotalSaleAmount(BigDecimal.ZERO);
        }
        equipment.setModifyDate(new Date());
        equipmentService.save(equipment);
        EquipmentModel model = new EquipmentModel();
        model.copyFrom(equipment);
        return DataBlock.success(model,"执行成功");
    }

    /**
     * 根据信息
     */
    @RequestMapping(value = "/copyright", method = RequestMethod.GET)
    public @ResponseBody DataBlock copyright() {
        Map<String, Object> data = new HashMap<String, Object>();
        Setting setting = SettingUtils.get();
        ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
        data.put("address", setting.getAddress());
        data.put("siteName", setting.getSiteName());
        data.put("certText", setting.getCerttext());
        data.put("phone", setting.getPhone());
//		if bundle.containsKey("company")
        data.put("company", bundle.containsKey("company")?bundle.getString("company"):"安徽威思通电子商务有限公司");
        data.put("qq", setting.getEnterpriseqq());
        data.put("helper",setting.getSiteUrl()+"/wap/invite/download.jhtml");
        data.put("buyer",setting.getSiteUrl()+"/www/app-index.html");
        return DataBlock.success(data,"执行成功");
    }
}
