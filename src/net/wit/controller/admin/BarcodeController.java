package net.wit.controller.admin;

import net.wit.*;
import net.wit.Message;
import net.wit.entity.*;
import net.wit.service.*;
import org.apache.commons.collections.IteratorUtils;
import org.apache.http.client.fluent.Response;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Administrator on 2016/7/23 0023.
 */
@Controller("adminBarcodeController")
@RequestMapping("/admin/barcode")
public class BarcodeController extends BaseController  {

    @Resource(name = "barcodeServiceImpl")
    private BarcodeService barcodeService;
    @Resource(name = "productCategoryServiceImpl")
    private ProductCategoryService  productCategoryService;
    @Resource(name = "brandServiceImpl")
    private BrandService  brandService;

    @Resource(name = "productImageServiceImpl")
    private ProductImageService productImageService;

    @Resource(name = "tagServiceImpl")
    private TagService tagService;

    /**
     * 列表页面
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Pageable pageable,String searchValue, ModelMap model) {

//        pageable.setSearchValue(null);
        model.addAttribute("page", barcodeService.openPage(searchValue,pageable));
        return "/admin/barcode/list";
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable Long id, Pageable pageable, ModelMap model) {
        barcodeService.delete(id);
        model.addAttribute("page", barcodeService.findPage(pageable));
        return "/admin/barcode/list";
    }
    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    Message delete(Long[] ids) {
        if (ids.length >= barcodeService.count()) {
            return Message.error("admin.common.deleteAllNotAllowed");
        }
        barcodeService.delete(ids);
        return SUCCESS_MESSAGE;
    }
    /**
     * 编辑页面
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable Long id, ModelMap model) {
        model.addAttribute("productCategoryTree", productCategoryService.findTree());
        model.addAttribute("barcode", barcodeService.find(id));
        model.addAttribute("expertTags", tagService.openPage(null,Tag.Type.expert).getContent());
        return "/admin/barcode/edit";
    }
    /**
     * 更新
     */
    @RequestMapping(value = "/edit/update", method = RequestMethod.POST)
    public String update(Barcode barcode,
                         Long productCategoryId ,
                         Long barcodeCategoryId,
                         Long brandId,
                         RedirectAttributes redirectAttributes){
//        int i=1;
//        return ERROR_VIEW;
        try {
            Barcode barcodeUp=barcodeService.find(barcode.getId());
            if(barcodeUp==null){
                return ERROR_VIEW;
               // return   Message.error("条码错误");
            }
//          行业标签
            if (barcode.getTag().getId()!=null){
                barcodeUp.setTag(tagService.find(barcode.getTag().getId()));
            }
            //单位
            if (barcode.getUnitName()!=null&&barcode.getUnitName()!=""){
                barcodeUp.setUnitName(barcode.getUnitName());
            }
            //进价
//            barcodeUp.setOutPrice(BigDecimal.ZERO);
            //条形码
            barcodeUp.setBarcode(barcode.getBarcode());
            //名称
            barcodeUp.setName(barcode.getName());
            //分类
            barcodeUp.setProductCategory(productCategoryService.find(productCategoryId));
            //品牌
            barcodeUp.setBrand(brandService.find(brandId));
            //市场价格
            if (barcode.getOutPrice()!=null){
                barcodeUp.setOutPrice(barcode.getOutPrice());
            }
            //图片
            Iterator<ProductImage> it = barcode.getProductImages().iterator();
            while(it.hasNext()) {
                ProductImage productImage = it.next();
                if (productImage == null || productImage.isEmpty()) {
                    it.remove();
                    continue;
                }
            }
            for (ProductImage productImage : barcode.getProductImages()) {
                if (productImage.getLocal() != null) {
                    productImage.setLocalFile(new File(productImage.getLocal()));
                    productImage.setSource(productImage.getLocal());
                    productImageService.build(productImage);
                }
            }
            Collections.sort(barcode.getProductImages());

            barcodeUp.setProductImages(barcode.getProductImages());
            //商品详情
            barcodeUp.setIntroduction(barcode.getIntroduction());
            barcodeUp.setDescriptionapp(barcode.getDescriptionapp());
            barcodeService.update(barcodeUp);
            return "redirect:/admin/barcode/list.jhtml";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR_VIEW;
        }

    }


    /**
     * 添加页面
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add( ModelMap model) {
        model.addAttribute("productCategoryTree", productCategoryService.findTree());
        model.addAttribute("expertTags", tagService.openPage(null,Tag.Type.expert).getContent());
        model.addAttribute("barcode", new Barcode());
        return "/admin/barcode/add";
    }
    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Barcode barcode,
                       Long productCategoryId ,
                       Long brandId,
                       RedirectAttributes redirectAttributes){
//        int i=1;
//        return ERROR_VIEW;
        try {
            Barcode barcodeUp=new Barcode();
//          行业标签
            if (barcode.getTag().getId()!=null){
                barcodeUp.setTag(tagService.find(barcode.getTag().getId()));
            }
            //单位
            if (barcode.getUnitName()==null||barcode.getUnitName()==""){
                barcodeUp.setUnitName(" ");
            }else{
                barcodeUp.setUnitName(barcode.getUnitName());
            }
            //拼音
            barcodeUp.setSpell("");
            //进价
            barcodeUp.setInPrice(BigDecimal.ZERO);
            //条形码
            barcodeUp.setBarcode(barcode.getBarcode());
            //名称
            barcodeUp.setName(barcode.getName());
            //分类
            barcodeUp.setProductCategory(productCategoryService.find(productCategoryId));
            //品牌
            barcodeUp.setBrand(brandService.find(brandId));
            //市场价格
            if (barcode.getOutPrice()==null){
                barcodeUp.setOutPrice(BigDecimal.ZERO);
            }else {
                barcodeUp.setOutPrice(barcode.getOutPrice());
            }
            //图片
            Iterator<ProductImage> it = barcode.getProductImages().iterator();
            while(it.hasNext()) {
                ProductImage productImage = it.next();
                if (productImage == null || productImage.isEmpty()) {
                    it.remove();
                    continue;
                }
            }
            for (ProductImage productImage : barcode.getProductImages()) {
                if (productImage.getLocal() != null) {
                    productImage.setLocalFile(new File(productImage.getLocal()));
                    productImage.setSource(productImage.getLocal());
                    productImageService.build(productImage);
                }
            }
            Collections.sort(barcode.getProductImages());

            barcodeUp.setProductImages(barcode.getProductImages());


            //商品详情
            barcodeUp.setIntroduction(barcode.getIntroduction());
            barcodeUp.setDescriptionapp(barcode.getDescriptionapp());

            barcodeService.save(barcodeUp);
            return "redirect:/admin/barcode/list.jhtml";
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR_VIEW;
        }

    }

}
