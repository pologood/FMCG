/**
 *====================================================
 * 文件名称: TenantController.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014-9-11			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.controller.store.member;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.store.model.DataBlock;
import net.wit.entity.*;
import net.wit.entity.TenantFreight.Type;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: FreightController
 * @Description: 运费模板管理
 * @author ruanx
 * @date 2017-1-9 上午11:31:34
 */
@Controller("storeMemberTenantFreightController")
@RequestMapping("/store/member/tenant/freight")
public class TenantFreightController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	
	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	@Resource(name = "activityDetailServiceImpl")
	private ActivityDetailService activityDetailService;

	@Resource(name = "activityRulesServiceImpl")
	private ActivityRulesService activityRulesService;

	@Resource(name = "tenantFreightTemplateServiceImpl")
	private TenantFreightTemplateService tenantFreightTemplateService;

	@Resource(name = "tenantFreightServiceImpl")
	private TenantFreightService tenantFreightService;

	@Resource(name = "areaServiceImpl")
	private AreaService areaService;

	/**
	 * 查看模版
	 */
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String view(Long id, Type type, ModelMap modelMap , Pageable pageable) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		if (tenant == null) {
			return ERROR_VIEW;			
		}
		modelMap.addAttribute("name",tenantFreightTemplateService.find(id).getName());
		modelMap.addAttribute("id",id);
		modelMap.addAttribute("type",type);
		modelMap.addAttribute("tenant", tenant);
		modelMap.addAttribute("member", member);
		modelMap.addAttribute("menu", "tenant_freight");
		modelMap.addAttribute("page", tenantFreightService.findByFreightsTemplate(id,type,pageable));
		return "/store/member/freight/edits";
	}

	/**
	 * 新增模版
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap modelMap) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		if (tenant == null) {
			return ERROR_VIEW;
		}
		modelMap.addAttribute("tenant", tenant);
		modelMap.addAttribute("member", member);
		modelMap.addAttribute("menu", "tenant_freight");
		return "/store/member/freight/add";
	}
	
	/**
	 * 修改区域邮费
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock update(Long id,Integer firstWeight,BigDecimal firstPrice,Integer continueWeight,BigDecimal continuePrice,Long templateId) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error("请先登录");
		}
		Tenant tenant = member.getTenant();
		TenantFreight freights = tenantFreightService.find(id);
		if(freights!=null&&freights.getTenant().getId().equals(tenant.getId())){
			TenantFreightTemplate tenantFreightTemplate = tenantFreightTemplateService.find(templateId);
			freights.setFirstWeight(firstWeight);
			freights.setFirstPrice(firstPrice);
			freights.setContinueWeight(continueWeight);
			freights.setContinuePrice(continuePrice);
			tenantFreightService.update(freights);
			tenantFreightTemplate.setModifyDate(new Date());
			tenantFreightTemplateService.update(tenantFreightTemplate);
			return DataBlock.success("保存成功","执行成功");
		}
		return DataBlock.error("区域不存在！");
		
	}

	/**
	 * 模版列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(ModelMap modelMap, Pageable pageable) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		if (tenant == null) {
			return ERROR_VIEW;
		}
		Page page = tenantFreightTemplateService.findPage(tenant,pageable);
		modelMap.addAttribute("size", page.getContent().size());
		modelMap.addAttribute("page", page);
		modelMap.addAttribute("tenant", tenant);
		modelMap.addAttribute("member", member);
		modelMap.addAttribute("menu", "tenant_freight");
		return "/store/member/freight/list";
	}

	/**
	 * 添加区域
	 * areaId  区域id
	 * id      模版id
	 * name    模版名称
	 * type    模版类型  weifgt：记重   piece：计价
	 * tag     0：新增区域   非0：区域明细id
	 */
	@RequestMapping(value = "/addArea", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock addArea(Long areaId,Long id,String name,Type type,Long tag) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
        Area area = areaService.find(areaId);
		TenantFreight freights = tenantFreightService.findByArea(tenant,area);
		if(freights!=null){
			return DataBlock.error("该区域已设置过运费！","执行成功");
		}
		//新增
		TenantFreight tenantFreight = new TenantFreight();
		TenantFreightTemplate tenantFreightTemplate = new TenantFreightTemplate();
		if(tag==0l){
		    //新增模版,新增区域
			if(id==null){
				tenantFreightTemplate.setTenant(tenant);
				tenantFreightTemplate.setName(name);
				tenantFreightTemplate.setDefault(true);
				tenantFreightTemplateService.save(tenantFreightTemplate);
			}else{
				//新增区域
				tenantFreightTemplate = tenantFreightTemplateService.find(id);
			}
			tenantFreight.setFirstWeight(1);
			tenantFreight.setFirstPrice(BigDecimal.ZERO);
			tenantFreight.setContinueWeight(0);
			tenantFreight.setContinuePrice(BigDecimal.ZERO);
			tenantFreight.setTenant(tenant);
			tenantFreight.setArea(area);
			tenantFreight.setFreightType(type);
			tenantFreight.setTenantFreightTemplate(tenantFreightTemplate);
			tenantFreightService.save(tenantFreight);
		}else{
			//新增区域
			tenantFreight = tenantFreightService.find(tag);
			tenantFreight.setArea(area);
			tenantFreightService.update(tenantFreight);
		}
		return DataBlock.success(id==null?tenantFreightTemplate.getId():id,"执行成功");

	}

	/**
	 * 删除区域
	 * areaId  区域id
	 * id      明细id
	 */
	@RequestMapping(value = "/delArea", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock delArea(Long areaId,Long id) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		Area area = areaService.find(areaId);
		TenantFreight freights = tenantFreightService.findByArea(tenant,area);
		TenantFreightTemplate tenantFreightTemplate = tenantFreightTemplateService.find(freights.getTenantFreightTemplate().getId());
		if(freights!=null&&freights.getId().equals(id)){
			tenantFreightService.delete(id);
			tenantFreightTemplate.setModifyDate(new Date());
			tenantFreightTemplateService.update(tenantFreightTemplate);
			return DataBlock.success("删除成功","执行成功");
		}
		return DataBlock.error("该区域不存在！");
	}

	/**
	 * 删除模版
	 * id  id
	 */
	@RequestMapping(value = "/delTemplate", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock delTemplate(Long id,Pageable pageable) {
		Member member = memberService.getCurrent();
		Tenant tenant = member.getTenant();
		TenantFreightTemplate tenantFreightTemplate = tenantFreightTemplateService.find(id);
		if(tenantFreightTemplate!=null&&tenantFreightTemplate.getTenant().getId().equals(tenant.getId())){
			List<TenantFreight> lists = tenantFreightTemplate.getFreights();
			for(TenantFreight tenantFreight:lists){
				tenantFreightService.delete(tenantFreight);
			}
			tenantFreightTemplateService.delete(id);
			Page page = tenantFreightTemplateService.findPage(tenant,pageable);
			return DataBlock.success(page==null?0:page.getContent().size(),"执行成功");
		}
		return DataBlock.error("该模版不存在！");
	}

	/**
	 * 修改模版名称
	 */
	@RequestMapping(value = "/updateTemplateName", method = RequestMethod.POST)
	@ResponseBody
	public DataBlock updateTemplateName(Long id,String name) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error("请先登录");
		}
		Tenant tenant = member.getTenant();
		if(id!=null&&!"".equals(id)){
			TenantFreightTemplate tenantFreightTemplate = tenantFreightTemplateService.find(id);
			if(tenantFreightTemplate!=null&&tenantFreightTemplate.getTenant().getId().equals(tenant.getId())){
				tenantFreightTemplate.setName(name);
				tenantFreightTemplate.setModifyDate(new Date());
				tenantFreightTemplateService.update(tenantFreightTemplate);
				return DataBlock.success("保存成功","执行成功");
			}
		}else{
			TenantFreightTemplate tenantFreightTemplate = new TenantFreightTemplate();
			tenantFreightTemplate.setName(name);
			tenantFreightTemplate.setTenant(tenant);
			tenantFreightTemplate.setDefault(true);
			tenantFreightTemplateService.save(tenantFreightTemplate);
			return DataBlock.success(tenantFreightTemplate.getId(),"执行成功");
		}
		return DataBlock.error("模版不存在！");
	}
}
