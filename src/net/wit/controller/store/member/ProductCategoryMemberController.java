package net.wit.controller.store.member;

import net.wit.Filter;
import net.wit.Message;
import net.wit.controller.store.model.DataBlock;
import net.wit.entity.Member;
import net.wit.entity.ProductCategory;
import net.wit.entity.ProductCategoryMember;
import net.wit.service.MemberService;
import net.wit.service.ProductCategoryMemberService;
import net.wit.service.ProductCategoryService;
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
 * 会员常用商品类目
 * Created by WangChao on 2016-5-19.
 */
@Controller("storeMemberProductCategoryMemberController")
@RequestMapping("store/member/productCategoryMember")
public class ProductCategoryMemberController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "productCategoryServiceImpl")
    private ProductCategoryService productCategoryService;

    @Resource(name = "productCategoryMemberServiceImpl")
    private ProductCategoryMemberService productCategoryMemberService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public
    @ResponseBody
    DataBlock add(Long id) {
        Map<String,Object> map=new HashMap<>();
        try {
            Member member = memberService.getCurrent();
            ProductCategory productCategory = productCategoryService.find(id);
            if (productCategory == null) {
                return DataBlock.error("商品类目不存在");
            }
            List<Filter> filters=new ArrayList<>();
            filters.add(new Filter("member", Filter.Operator.eq,member));
            List<ProductCategoryMember> productCategoryMembers=productCategoryMemberService.findList(null,filters,null);
            if(productCategoryMembers.size()>=30){
                return DataBlock.error("常用类目最多只能添加30个");
            }
            if(!productCategoryMembers.isEmpty()){
                for (ProductCategoryMember pm : productCategoryMembers) {
                    if(pm.getProductCategory()==productCategory){
                        productCategoryMemberService.delete(pm);
                    }
                }
            }
            ProductCategoryMember productCategoryMember = new ProductCategoryMember();
            productCategoryMember.setMember(member);
            productCategoryMember.setProductCategory(productCategory);
            ProductCategoryMember pcmember = productCategoryMemberService.update(productCategoryMember);
            map.put("id",pcmember.getId());
            map.put("productCategoryId",pcmember.getProductCategory().getId());
            String name=pcmember.getProductCategory().getName();
            String pName="";
            String ppName="";
            if(pcmember.getProductCategory().getParent()!=null){
                if(pcmember.getProductCategory().getParent().getParent()!=null){
                    ppName=pcmember.getProductCategory().getParent().getParent().getName()+">";
                }
                pName=pcmember.getProductCategory().getParent().getName()+">";
            }
            map.put("fullName",ppName+pName+name);
            map.put("productCategoryTreePath",pcmember.getProductCategory().getTreePath());
        } catch (Exception e) {
            e.printStackTrace();
            return DataBlock.error("未知错误");
        }
        return DataBlock.success(map,"已加为常用类目");
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody Message delete(Long... ids){
        try {
            productCategoryMemberService.delete(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("删除失败");
        }
        return Message.success("操作成功");
    }
}
