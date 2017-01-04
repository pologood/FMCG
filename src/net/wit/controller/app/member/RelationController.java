package net.wit.controller.app.member;

import net.wit.controller.app.model.*;
import net.wit.entity.Tenant;
import net.wit.entity.TenantRelation.Status;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.app.BaseController;
import net.wit.entity.Member;
import net.wit.entity.TenantRelation;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by thwapp on 2016/2/22.
 */
@Controller("appMemberRelationController")
@RequestMapping("/app/member/relation")
public class RelationController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "memberRankServiceImpl")
    private MemberRankService memberRankService;

    @Resource(name = "tenantRelationServiceImpl")
    private TenantRelationService tenantRelationService;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "activityDetailServiceImpl")
    private ActivityDetailService activityDetailService;

    @Resource(name = "activityRulesServiceImpl")
    private ActivityRulesService activityRulesService;

	/**
	 * 添加
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody DataBlock addTenant(Long id) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		if (member.getTenant()==null) {
			return DataBlock.error("您不是商家不能加盟。");
		}
		Tenant tenant = tenantService.find(id);
		if (tenant == null) {
			return DataBlock.error("商家ID无效");
		}
		
		Tenant myTenant = member.getTenant();
		
		Page<TenantRelation> rl = tenantRelationService.findRelation(tenant, myTenant, null);
		if (rl.getTotal()>0) {
			return DataBlock.error("已经加盟了");
		}
		
		TenantRelation relation = new TenantRelation();
		relation.setParent(tenant);
		relation.setStatus(TenantRelation.Status.none);
		relation.setTenant(tenant);
		relation.setMemberRank(memberRankService.findDefault());
		tenantRelationService.save(relation);
//添加联盟商家
        if(!activityDetailService.isActivity(null,myTenant, activityRulesService.find(34L))){
            activityDetailService.addPoint(null,myTenant,activityRulesService.find(34L));
        }

		return DataBlock.success("success","申请成功");
	}

    /**
     * 查询供应商/查询代理商
     * @param type 1表示供应商、0表示代理商
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public DataBlock list(String type, Pageable pageable) {
        Member member = memberService.getCurrent();
        if (member == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }

        Page<TenantRelation> page = new Page<TenantRelation>();
        List<TenantRelationListModel> tenantRelationListModelList = new ArrayList<TenantRelationListModel>();
        TenantRelationListModel tenantRelationListModel = new TenantRelationListModel();
        if("0".equals(type)){//查询代理商
            page = tenantRelationService.findPage(member.getTenant(),null,pageable);
            tenantRelationListModelList = tenantRelationListModel.bindTenant(page.getContent());
        }else if("1".equals(type)){//查询供应商
            page = tenantRelationService.findParent(member.getTenant(),Status.fail,pageable);
            tenantRelationListModelList = tenantRelationListModel.bindParent(page.getContent());
        }
        return DataBlock.success(tenantRelationListModelList,"执行成功");
    }

    /**
     * 供应商/代理商
     * @return TenantRelationModel
     */
    @RequestMapping(value = "/view")
    @ResponseBody
    public DataBlock view(Long id,String type) {
        Member member = memberService.getCurrent();
        if (member==null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }

        TenantRelation tenantRelation = tenantRelationService.find(id);
        TenantRelationModel tenantRelationModel = new TenantRelationModel();
        
        MemberRankModel memberRankModel = new MemberRankModel();
        if (tenantRelation.getMemberRank()!=null) {
           memberRankModel.copyFrom(tenantRelation.getMemberRank());
        }
        tenantRelationModel.setMemberRank(memberRankModel);
        tenantRelationModel.setStatus(tenantRelation.getStatus());
        tenantRelationModel.setId(id);
        if ("0".equals(type)) {
            tenantRelationModel.copyFrom(tenantRelation.getTenant());
        }else if("1".equals(type)){
            tenantRelationModel.copyFrom(tenantRelation.getParent());
        }

        return DataBlock.success(tenantRelationModel,"执行成功");
    }

    /**
     * 修改等级
     * @param id             商盟编号
     * @param memberRankId   等级编号
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody DataBlock update(Long id,  Long memberRankId, Status status) {
        Member member = memberService.getCurrent();
        if (member==null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }

        TenantRelation tenantRelation = tenantRelationService.find(id);
        if (tenantRelation == null) {
            return DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        if (memberRankId!=null) {
           tenantRelation.setMemberRank(memberRankService.find(memberRankId));
        }
        if (status!=null) {
           tenantRelation.setStatus(status);
        }
        tenantRelationService.update(tenantRelation);
        return DataBlock.success("success","修改成功！！！");
    }

    /**
     *  代理授权
     * @param id 商铺编号
     * @param type 是否授权 0表示同意授权，1表示拒绝授权
     * @return
     */
    @RequestMapping(value = "/grant", method = RequestMethod.POST)
    public @ResponseBody DataBlock grant(Long id,Long memberRankId,String type) {
        Member member = memberService.getCurrent();
        if (member==null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        if (member.getTenant()==null) {
            return DataBlock.error("您不是商家不能加盟。");
        }
        TenantRelation relation = tenantRelationService.find(id);
        if (relation == null) {
            return DataBlock.error("商盟ID无效");
        }
        String success ="";
        if("0".equals(type)){
            relation.setStatus(Status.success);
            success = "授权成功";
        }else if("1".equals(type)){
            relation.setStatus(Status.fail);
            success = "取消成功";
        }
        if(memberRankId!=null){
            relation.setMemberRank(memberRankService.find(memberRankId));
        }
        tenantRelationService.update(relation);

        if(relation.getStatus()==Status.success){
            if(!activityDetailService.isActivity(null,member.getTenant(), activityRulesService.find(35L))){
                activityDetailService.addPoint(null,member.getTenant(),activityRulesService.find(35L));
            }
        }

        return DataBlock.success("success",success);
    }

    /**
     * 取消代理/取消申请
     * @param id
     * @return
     */
    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public @ResponseBody DataBlock cancel(Long id) {
        Member member = memberService.getCurrent();
        if (member==null) {
            DataBlock.error(DataBlock.SESSION_INVAILD);
        }
        tenantRelationService.delete(id);
        return DataBlock.success("success","取消成功");
    }
}
