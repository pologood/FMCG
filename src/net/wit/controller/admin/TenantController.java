package net.wit.controller.admin;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.wit.Filter;
import net.wit.Filter.Operator;
import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.*;
import net.wit.entity.Authen.AuthenStatus;
import net.wit.entity.Tag.Type;
import net.wit.entity.Tenant.Status;
import net.wit.entity.Tenant.TenantType;
import net.wit.service.*;
import net.wit.support.EntitySupport;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("adminTenantController")
@RequestMapping("/admin/tenant")
public class TenantController extends BaseController {
    /**
     * 每页记录数
     */
    private static final int PAGE_SIZE = 10;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "areaServiceImpl")
    private AreaService areaService;

    @Resource(name = "communityServiceImpl")
    private CommunityService communityService;

    @Resource(name = "tenantCategoryServiceImpl")
    private TenantCategoryService tenantCategoryService;

    @Resource(name = "tagServiceImpl")
    private TagService tagService;

    @Resource(name = "authenServiceImpl")
    private AuthenService authenService;

    @Resource(name = "adminServiceImpl")
    private AdminService adminService;

    @Resource(name = "productServiceImpl")
    private ProductService productService;

    @Resource(name = "qrcodeServiceImpl")
    private QrcodeService qrcodeService;

    @Resource(name = "adServiceImpl")
    private AdService adService;

    @Resource(name= "adPositionServiceImpl")
    private AdPositionService adPositionService;

    @Resource(name = "activityDetailServiceImpl")
    private ActivityDetailService activityDetailService;

    @Resource(name = "activityRulesServiceImpl")
    private ActivityRulesService activityRulesService;

    @Resource(name = "employeeServiceImpl")
    private EmployeeService employeeService;

    @Resource(name = "unionTenantServiceImpl")
    private UnionTenantService unionTenantService;

    /**
     * 获取社区信息
     */
    @RequestMapping(value = "/get_community", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<Long, String> getCommunity(Long areaId) {
        Map<Long, String> data = new HashMap<Long, String>();
        Area area = areaService.find(areaId);
        List<Community> communitys = communityService.findList(area);
        for (Community community : communitys) {
            data.put(community.getId(), community.getName());
        }
        return data;
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        Tenant tenant = tenantService.find(id);
        model.addAttribute("tags", tagService.findList(Type.tenant));
        model.addAttribute("uniontags", tagService.findList(Type.tenantUnion));
        model.addAttribute("tenantCategoryTree", tenantCategoryService.findTree());
        model.addAttribute("tenant", tenant);

        Set<Tag> authenTags = new HashSet<Tag>();
        Iterator iterator = (tenant.getAuthen()).iterator();
        while (iterator.hasNext()) {
            Authen authen = (Authen) iterator.next();
            try {
                if (authen.getAuthenStatus() == AuthenStatus.success) {
                    authenTags.add(authen.getTag());
                }
            } catch (Exception er) {
                //System.out.println(er.toString());
            }
        }
        model.addAttribute("tagSet", authenTags);
        model.addAttribute("tenantTypes", Tenant.TenantType.values());
        try {
            model.addAttribute("members", memberService.findList(tenant));
            model.addAttribute("salesmans", memberService.findList(new Tenant(1L)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "admin/tenant/edit";
    }


    /**
     * 列表
     * String qrCodeStatus:是否有二维码
     * 1：有
     * 0：没有
     * String marketableSize:商品上架数量
     * 分隔符',',例子：1,3 代表数量在1到3之间
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Date beginDate, Date endDate, Status status, Long tagId, Long areaId,String tenantType, Long tenantCategoryId,
                       String qrCodeStatus, String marketableSize,
                       String keyword, Pageable pageable, ModelMap model) {
        try {
            Tag tag = tagService.find(tagId);
            ArrayList<Tag> tags = new ArrayList<Tag>();

            if (tag != null) {
                tags.add(tag);
            }

//			if(beginDate!=null){
//				//beginDate=new Date();
//				Long time=beginDate.getTime();
//				Long begin=time-24*60*60*1000*7;
//				beginDate=new Date(begin);
//			}
//			if(endDate!=null){
//				Long time=endDate.getTime();
//				Long end=time+24*60*60*1000-1;
//				endDate=new Date(end);
//			}
            Admin admin = adminService.getCurrent();
            Area area = null;
            if ( admin.getId().equals(1L)) {
                area = areaService.find(areaId);
            } else {
                if (admin.getEnterprise() != null) {
                    area = admin.getEnterprise().getArea();
                }
            }
            Set<TenantCategory> tenantCategories = new HashSet<TenantCategory>();
            TenantCategory tenantCategory = tenantCategoryService.find(tenantCategoryId);
            if (tenantCategory != null) {
                tenantCategories.add(tenantCategory);
            }
            List<Filter> filter = new ArrayList<Filter>();
            if (tenantType != null) {
                if ("tenant".equals(tenantType)) {
                    filter.add(new Filter("tenantType", Operator.eq, TenantType.tenant));
                } else if ("supplier".equals(tenantType)) {
                    filter.add(new Filter("tenantType", Operator.eq, TenantType.suppier));
                } else {
                    filter.add(new Filter("tenantType", Operator.eq, TenantType.retailer));
                }
            }

//            if (qrCodeStatus!=null) {
//                filter.add(new Filter("qrcodes", Operator.eq,  new Qrcode()));
//            }
            filter.add(new Filter("tenantCategory", Operator.eq, tenantCategory));
            pageable.setFilters(filter);
            pageable.setOrderProperty(null);
            keyword = pageable.getSearchValue();
            pageable.setSearchValue(null);
            Page page = tenantService.openPage(pageable, area, beginDate, endDate, tenantCategories, tags, keyword, status, null,qrCodeStatus,marketableSize);
            model.addAttribute("page", page);
            model.addAttribute("status", status);
            model.addAttribute("tagId", tagId);
            model.addAttribute("tags", tagService.findList(Type.tenant));
            model.addAttribute("beginDate", beginDate);
            model.addAttribute("endDate", endDate);
            model.addAttribute("qrCodeStatus", qrCodeStatus);
            model.addAttribute("marketableSize", marketableSize);
			model.addAttribute("keyword",keyword);
            model.addAttribute("tenantCategorys",tenantCategoryService.findList(null,null,null));
            model.addAttribute("tenantCategoryId",tenantCategoryId);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }

        return "/admin/tenant/list";
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/edit_qrcode", method = RequestMethod.GET)
    public String edit_qrcode(Long id, ModelMap model) {
        Tenant tenant = tenantService.find(id);
        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("tenant", Operator.isNull, null));
        List<Qrcode> qrcodes = qrcodeService.findList(null, filters, null);

        model.addAttribute("tenant", tenant);
        model.addAttribute("qrcodes", qrcodes);
        return "admin/tenant/editQrcode";
    }

    /**
     * 列表
     */
    @RequestMapping(value = "/report_list", method = RequestMethod.GET)
    public String report(Date beginDate, Date endDate, Status status, Long tagId, String tenantType, Long tenantCategoryId, String keyword, Pageable pageable, ModelMap model) {
        try {
            Tag tag = tagService.find(tagId);
            ArrayList<Tag> tags = new ArrayList<Tag>();

            if (tag != null) {
                tags.add(tag);
            }

//			if(beginDate!=null){
//				//beginDate=new Date();
//				Long time=beginDate.getTime();
//				Long begin=time-24*60*60*1000*7;
//				beginDate=new Date(begin);
//			}
//			if(endDate!=null){
//				Long time=endDate.getTime();
//				Long end=time+24*60*60*1000-1;
//				endDate=new Date(end);
//			}
            Admin admin = adminService.getCurrent();
            Area area = null;
            if (admin != null && admin.getId() == 1l) {
                area = null;
            } else {
                if (admin != null && admin.getEnterprise() != null) {
                    area = admin.getEnterprise().getArea();
                }
            }
            Set<TenantCategory> tenantCategories = new HashSet<TenantCategory>();
            TenantCategory tenantCategory = tenantCategoryService.find(tenantCategoryId);
            if (tenantCategory != null) {
                tenantCategories.add(tenantCategory);
            }
            if (tenantType != null) {
                List<Filter> filter = new ArrayList<Filter>();
                if ("tenant".equals(tenantType)) {
                    filter.add(new Filter("tenantType", Operator.eq, TenantType.tenant));
                } else if ("supplier".equals(tenantType)) {
                    filter.add(new Filter("tenantType", Operator.eq, TenantType.suppier));
                } else {
                    filter.add(new Filter("tenantType", Operator.eq, TenantType.retailer));
                }
                pageable.setFilters(filter);
            }
            pageable.setOrderProperty(null);
            keyword = pageable.getSearchValue();
            pageable.setSearchValue(null);
            Page<Tenant> page = tenantService.openPage(pageable, area, beginDate, endDate, tenantCategories, tags, keyword, status, null);
            List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();

            for (Tenant tenant : page.getContent()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name", tenant.getName());
                map.put("host", tenant.getMember().getUsername());
                map.put("time", tenant.getCreateDate());
                map.put("score", tenant.getScore());
                map.put("hit", tenant.getHits());
                map.put("sales", tenant.getSales());
                map.put("freight", tenant.getFreight());
                Map<String, BigDecimal> mapp = productService.getStockAmount(tenant.getId());
                if (tenant.getTenantType() == TenantType.suppier) {
                    map.put("balance", tenant.getBalance());
                    map.put("freezeBalance", tenant.getFreezeBalance());
                    map.put("stock", mapp.get("SuppilerAmount"));
                } else {
                    map.put("balance", tenant.getMember().getBalance());
                    map.put("freezeBalance", tenant.getMember().getFreezeBalance());
                    map.put("stock", mapp.get("amount"));
                }
                map.put("isMarked", tenant.getName());
                map.put("noMarked", tenant.getName());
                maps.add(map);
            }
            model.addAttribute("maps", maps);
            model.addAttribute("page", page);
            model.addAttribute("status", status);
            model.addAttribute("tagId", tagId);
            model.addAttribute("tags", tagService.findList(Type.tenant));
            model.addAttribute("beginDate", beginDate);
            model.addAttribute("endDate", endDate);
//			model.addAttribute("tenantType",tenantType);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }

        return "/admin/tenant/report_list";
    }

    /**
     * 查看
     */
    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String view(Long id, ModelMap model) {
        Tenant tenant=tenantService.find(id);
        List<Employee> employees=employeeService.findList(tenant,null);
        List<Filter> filter = new ArrayList<Filter>();
        filter.add(new Filter("status", Filter.Operator.eq, UnionTenant.Status.confirmed));
        List<UnionTenant> unionTenants=unionTenantService.findUnionTenant(null,tenant,filter);
        model.addAttribute("tenant", tenant);
        model.addAttribute("employeeSize", employees.size());
        model.addAttribute("productSize", tenant.getProducts().size());
        model.addAttribute("promotionSize", tenant.getPromotions().size());
        model.addAttribute("favoriteMemberSize", tenant.getFavoriteMembers().size());
        model.addAttribute("deliveryCenterSize", tenant.getDeliveryCenters().size());
        model.addAttribute("unionTenantSize", unionTenants.size());
        return "/admin/tenant/view";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Tenant tenant, Status status, Long tenantCategoryId, long areaId, String shortName, Long[] tagIds, Long[] unionTagIds, RedirectAttributes redirectAttributes) {
        try {
            Tenant saveTenant = null;
            if (tenant.getId() != null && tenant.getId() != 0) {
                saveTenant = tenantService.find(tenant.getId());
            } else {
                saveTenant = EntitySupport.createInitTenant();
            }
            tenant.setMember(saveTenant.getMember());
            //BeanUtils.copyProperties(tenant, saveTenant, new String[] { "member", "code", "host", "software", "score", "totalAssistant", "totalScore", "scoreCount", "hits", "weekHits", "monthHits", "createDate", "modifyDate", "status", "logo",
            //		"licensePhoto", "tenantImages", "introduction", "paymentMethod", "paymentKey", "partner", "relations", "thumbnail" });
            saveTenant.setLogo(tenant.getLogo());
            saveTenant.setTenantType(tenant.getTenantType());
            saveTenant.setName(tenant.getName());
            saveTenant.setAddress(tenant.getAddress());
            saveTenant.setLinkman(tenant.getLinkman());
            saveTenant.setTelephone(tenant.getTelephone());
            saveTenant.setLicenseCode(tenant.getLicenseCode());
            saveTenant.setShortName(tenant.getName());
            saveTenant.setThumbnail(tenant.getThumbnail());
            saveTenant.setQq(tenant.getQq());
            boolean isUnion = true;
            if (tenant.getIsUnion() == null) {
                isUnion = false;
            }
            saveTenant.setIsUnion(isUnion);
            saveTenant.setRangeInfo(tenant.getRangeInfo());
            saveTenant.setBrokerage(tenant.getBrokerage());
            saveTenant.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
            saveTenant.setArea(areaService.find(areaId));
            saveTenant.setStatus(status);
            saveTenant.setTenantCategory(tenantCategoryService.find(tenantCategoryId));
            saveTenant.setGeneralize(tenant.getGeneralize());
            saveTenant.setAgency(tenant.getAgency());
            tenantService.save(saveTenant);

            addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            addFlashMessage(redirectAttributes, ERROR_MESSAGE);
        }
        return "redirect:list.jhtml";
    }

    /**
     * 审核列表
     */
    @RequestMapping(value = "/checkList", method = RequestMethod.GET)
    public String checkList(Status status, Pageable pageable, ModelMap model) {
        List<Filter> filters = pageable.getFilters();
        if (status == null) {
            status = Status.confirm;
        }
        filters.add(new Filter("status", Operator.eq, status));
        model.addAttribute("status", status);
        model.addAttribute("page", tenantService.findPage(pageable));
        return "/admin/tenant/checkList";
    }

    /**
     * 审核内容详情
     */
    @RequestMapping(value = "/checkEdit", method = RequestMethod.GET)
    public String checkEdit(Long id, ModelMap model, RedirectAttributes redirectAttributes) {
        Tenant tenant = tenantService.find(id);
        Set<Tag> authenTags = new HashSet<Tag>();
        Iterator iterator = (tenant.getAuthen()).iterator();
        while (iterator.hasNext()) {
            Authen authen = (Authen) iterator.next();
            try {
                //Tag tag = new Tag();
                authenTags.add(authen.getTag());
            } catch (Exception er) {
                System.out.println(er.toString());
            }
        }
        model.addAttribute("tagSet", authenTags);
        model.addAttribute("tags", tagService.findList(Type.tenant));
        model.addAttribute("tenantCategoryTree", tenantCategoryService.findTree());
        model.addAttribute("uniontags", tagService.findList(Type.tenantUnion));
        model.addAttribute("tenant", tenant);
        return "admin/tenant/checkEdit";
    }

    /**
     * 审查商家认证标签，及状态修改
     */
    @RequestMapping(value = "/checkSave", method = RequestMethod.POST)
    public String checkSave(Long id, Tenant tenant, Status status, AuthenStatus authenStatus, Long tenantCategoryId, Long[] tagIds, Long[] unionTagIds, RedirectAttributes redirectAttributes) {
        Tenant saveTenant = null;
        saveTenant = tenantService.find(id);
        saveTenant.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
        saveTenant.setStatus(status);
        saveTenant.setTenantType(tenant.getTenantType());
        saveTenant.setTenantCategory(tenantCategoryService.find(tenantCategoryId));
        tenantService.save(saveTenant);
        Iterator iterator = (saveTenant.getAuthen()).iterator();
        while (iterator.hasNext()) {
            Authen saveAuthen = (Authen) iterator.next();
            saveAuthen.setAuthenStatus(authenStatus);
            authenService.update(saveAuthen);
        }
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:checkList.jhtml";
    }

    /**
     * 商家认证审核列表
     */
    @RequestMapping(value = "/authenList", method = RequestMethod.GET)
    public String authenList(Date beginDate, Date endDate, AuthenStatus authenStatus, Pageable pageable, ModelMap model) {
        try {
            if (authenStatus == null) {
                authenStatus = AuthenStatus.wait;
            }

//			if(beginDate!=null){
//				//beginDate=new Date();
//				Long time=beginDate.getTime();
//				Long begin=time-24*60*60*1000*3;
//				beginDate=new Date(begin);
//			}
//			if(endDate!=null){
//				Long time=endDate.getTime();
//				Long end=time+24*60*60*1000-1;
//				endDate=new Date(end);
//			}

            Admin admin = adminService.getCurrent();
            Area area = null;
            if (admin != null && admin.getId() == 1l) {
                area = null;
            } else {
                if (admin != null && admin.getEnterprise() != null) {
                    area = admin.getEnterprise().getArea();
                }
            }
            Page page = authenService.findPage(area, authenStatus, beginDate, endDate, pageable);
            model.addAttribute("beginDate", beginDate);
            model.addAttribute("endDate", endDate);
            model.addAttribute("authenStatus", authenStatus);
            model.addAttribute("page", page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "/admin/tenant/authenList";
    }

    /**
     * 商家认证审核内容详情
     */
    @RequestMapping(value = "/authenEdit", method = RequestMethod.GET)
    public String authenEdit(Long id, ModelMap model, RedirectAttributes redirectAttributes) {
        Tenant tenant = tenantService.find(id);
        Set<Tag> authenTags = new HashSet<Tag>();
        Iterator iterator = (tenant.getAuthen()).iterator();
        while (iterator.hasNext()) {
            Authen authen = (Authen) iterator.next();
            try {
                if (authen.getAuthenStatus() == AuthenStatus.success) {
                    authenTags.add(authen.getTag());
                }
            } catch (Exception er) {
                //System.out.println(er.toString());
            }
        }
        model.addAttribute("tagSet", authenTags);
        model.addAttribute("tags", tagService.findList(Type.tenant));
        model.addAttribute("tenantCategoryTree", tenantCategoryService.findTree());
        model.addAttribute("uniontags", tagService.findList(Type.tenantUnion));
        model.addAttribute("tenant", tenant);
        return "admin/tenant/authenEdit";
    }

    /**
     * 商家认证审查标签，及状态修改
     */
    @RequestMapping(value = "/authenSave", method = RequestMethod.POST)
    public String authenSave(Long id, Tenant tenant, AuthenStatus[] authenStatus, Long tenantCategoryId, Long[] tagIds, Long[] unionTagIds, RedirectAttributes redirectAttributes) {
        Tenant saveTenant = null;
        saveTenant = tenantService.find(id);
//		Set<Tag> tags = new HashSet<Tag>();
        Set<Authen> authens = saveTenant.getAuthen();
        List<Authen> authenList = new ArrayList<Authen>(authens);
        if (authens != null && authens.size() > 0) {
            for (int i = 0; i < authenList.size(); i++) {
                Authen authen = authenList.get(i);
                authen.setAuthenStatus(authenStatus[i]);
//				if(authenStatus[i]==AuthenStatus.success){
//					tags.add(authen.getTag());
//				}
                authenService.update(authen);
            }
        }
        saveTenant.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
        //saveTenant.setTags(tags);
        saveTenant.setTenantType(tenant.getTenantType());
        saveTenant.setTenantCategory(tenantCategoryService.find(tenantCategoryId));
        tenantService.save(saveTenant);
//		if(authens!=null&&authens.size()>0){
//			for(int i=0;i<authenList.size();i++){
//				Authen authen=authenList.get(i);
//				authen.setAuthenStatus(authenStatus[i]);
//				authenService.update(authen);
//			}
//		}
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:authenList.jhtml";
    }

    /**
     * 店铺开通审核列表
     */
    @RequestMapping(value = "/checkOpenList", method = RequestMethod.GET)
    public String checkOpenList(Date beginDate, Date endDate, Status status, Pageable pageable, ModelMap model) {
        List<Filter> filters = pageable.getFilters();
        if (status == null) {
            status = Status.confirm;
        }
        filters.add(new Filter("status", Operator.eq, status));
        pageable.setFilters(filters);

//		if(beginDate!=null){
//			//beginDate=new Date();
//			Long time=beginDate.getTime();
//			Long begin=time-24*60*60*1000*3;
//			beginDate=new Date(begin);
//		}
//		if(endDate!=null){
//			Long time=endDate.getTime();
//			Long end=time+24*60*60*1000-1;
//			endDate=new Date(end);
//		}

        Admin admin = adminService.getCurrent();
        Area area = null;
        if (admin != null && admin.getId() == 1l) {
            area = null;
        } else {
            if (admin != null && admin.getEnterprise() != null) {
                area = admin.getEnterprise().getArea();
            }
        }
        String keyword = pageable.getSearchValue();
        pageable.setSearchValue(null);
        pageable.setSearchProperty(null);
        Page page = tenantService.openPage(pageable, area, beginDate, endDate, null, null, keyword, status, null);
        model.addAttribute("beginDate", beginDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("status", status);
        model.addAttribute("page", page);
        return "/admin/tenant/checkOpenList";
    }

    /**
     * 店铺开通审核内容详情
     */
    @RequestMapping(value = "/checkOpenEdit", method = RequestMethod.GET)
    public String checkOpenEdit(Long id, ModelMap model, RedirectAttributes redirectAttributes) {
        Tenant tenant = tenantService.find(id);
        Set<Tag> authenTags = new HashSet<Tag>();
        Iterator iterator = (tenant.getAuthen()).iterator();
        while (iterator.hasNext()) {
            Authen authen = (Authen) iterator.next();
            try {
                if (authen.getAuthenStatus() == AuthenStatus.success) {
                    authenTags.add(authen.getTag());
                }
            } catch (Exception er) {
                System.out.println(er.toString());
            }
        }
        model.addAttribute("tagSet", authenTags);
        model.addAttribute("tags", tagService.findList(Type.tenant));
        model.addAttribute("tenantCategoryTree", tenantCategoryService.findTree());
        model.addAttribute("uniontags", tagService.findList(Type.tenantUnion));
        model.addAttribute("tenant", tenant);
        return "admin/tenant/checkOpenEdit";
    }


    /**
     * 店铺开通审查标签，及状态修改
     */
    @RequestMapping(value = "/checkOpenSave", method = RequestMethod.POST)
    public String checkOpenSave(Long id, Tenant tenant, Status status, Long tenantCategoryId, Long[] tagIds, Long[] unionTagIds, RedirectAttributes redirectAttributes) {
        Tenant saveTenant = null;
        saveTenant = tenantService.find(id);
        //saveTenant.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
        saveTenant.setStatus(status);
        saveTenant.setTenantType(tenant.getTenantType());
        saveTenant.setTenantCategory(tenantCategoryService.find(tenantCategoryId));
        tenantService.save(saveTenant);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:checkOpenList.jhtml";
    }

    /**
     * 员工列表
     */
    @RequestMapping(value = "/memberlist", method = RequestMethod.GET)
    public String memberlist(Long id, Integer pageNumber, ModelMap model) {
        Tenant tenant = tenantService.find(id);

        Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
        model.addAttribute("page", memberService.findPage(tenant,null, pageable));
        model.addAttribute("id", id);
        return "admin/tenant/memberlist";
    }

    /**
     * 业务员列表
     */
    @RequestMapping(value = "/salesMan", method = RequestMethod.GET)
    public String salesMan(Long id, ModelMap model) {
        Tenant tenant = tenantService.find(id);
        model.addAttribute("page", memberService.findPage(tenant, null,null));
        model.addAttribute("id", id);
        return "/admin/tenant/salesMan";
    }

    /**
     * 选择业务员
     */
    @RequestMapping(value = "/saveSalesman", method = RequestMethod.POST)
    public String saveSalesman(Long id, Long salesmanId, ModelMap model) {
        Member salesman = memberService.find(salesmanId);
        Tenant tenant = tenantService.find(id);
        tenantService.save(tenant);
        return "redirect:salesMan.jhtml";
    }

    /**
     * 移除业务员
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public
    @ResponseBody
    Message remove(Long id) {
        Tenant tenant = tenantService.find(id);
        tenantService.save(tenant);
        return SUCCESS_MESSAGE;
    }

    /**
     * 获取商家
     */
    @RequestMapping(value = "/getTenant", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, String> getTenant(Long id) {
        Map<String, String> data = new HashMap<String, String>();
        Tenant tenant = tenantService.find(id);
        data.put("id", id.toString());
        data.put("name", tenant.getName());
        return data;
    }


    @Resource(name = "logServiceImpl")
    private LogService logService;

    /**
     * 获取获取审核记录
     */
    @RequestMapping(value = "/audit/record", method = RequestMethod.POST)
    public
    @ResponseBody
    List<Map<String, Object>> auditRecord(Long id, Pageable pageable) {
        pageable.setSearchProperty("operation");
        pageable.setSearchValue("店铺审核");
        Page<Log> logPage = logService.findPage(pageable);

        List<Map<String, Object>> list = new ArrayList<>();
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Log log : logPage.getContent()) {
            String status = "";
            if (log.getParameter().indexOf("id = " + id) != -1) {
                Map<String, Object> map = new HashMap<>();
                if (log.getParameter().indexOf("status = none") != -1) {
                    status = "待审核";
                } else if (log.getParameter().indexOf("status = confirm") != -1) {
                    status = "已审核";
                } else if (log.getParameter().indexOf("status = success") != -1) {
                    status = "已开通";
                } else if (log.getParameter().indexOf("status = fail") != -1) {
                    status = "已关闭";
                }

                map.put("parameter", status);
                map.put("operator", log.getOperator());
                map.put("createDate", time.format(log.getCreateDate()));
                list.add(map);
            }
        }

        return list;
    }

    /**
     * 广告位
     * @param id
     * @param pageable
     * @param model
     * @return
     */
    @RequestMapping(value = "/adList", method = RequestMethod.GET)
    public String adList(Long id,Pageable pageable, ModelMap model) {
        Tenant tenant = tenantService.find(id);
        Page<Ad> page=adService.findMyPage(tenant, pageable);
        Long num=page.getTotal();
        model.addAttribute("num",num);
        model.addAttribute("page", page);
        model.addAttribute("tenant", tenant);
        model.addAttribute("menu", "tenant_management");
        return "/admin/tenant/adList";
    }

    /**
     * 广告添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String adAdd(Long tenantId, ModelMap model) {
        Member member = memberService.getCurrent();
        Long[] ads={80L,119L,120L,121L,122L,123L};//广告位编码
        List<AdPosition> list = adPositionService.findList(ads);
        model.addAttribute("types", Ad.Type.values());

        model.addAttribute("linkTypes", Ad.LinkType.values());
        model.addAttribute("adPositions", list);
        Tenant tenant = tenantService.find(tenantId);
        List<Product> products = productService.findMyList(tenant, null, null, null, null, null, null, null, null, true,
                null, null, null, null, null,null, null);
        model.addAttribute("products", products);
        model.addAttribute("member", member);
        model.addAttribute("menu", "tenant_management");
        model.addAttribute("tenant",tenant);
        return "/admin/tenant/addAd";
    }

    /**
     * 广告保存
     */
    @RequestMapping(value = "/saveAd", method = RequestMethod.POST)
    public String adSave(Ad ad,Long tenantId, Long adPositionId, RedirectAttributes redirectAttributes) {
        Member member = memberService.getCurrent();
        Tenant tenant = tenantService.find(tenantId);
        AdPosition adPosition = adPositionService.find(adPositionId);
        ad.setAdPosition(adPosition);
        ad.setTenant(tenant);
        if (!isValid(ad)) {
            return ERROR_VIEW;
        }
        if (ad.getType() == Ad.Type.text) {
            ad.setPath(null);
        } else {
            ad.setContent(null);
        }
        adService.save(ad);
        activityDetailService.addPoint(null, tenant, activityRulesService.find(5L));
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:adList.jhtml";
    }

    /**
     * 广告编辑
     */
    @RequestMapping(value = "/editAd", method = RequestMethod.GET)
    public String adEdit(Long id, Long tenantId, ModelMap model) {
        Member member = memberService.getCurrent();
        model.addAttribute("member", member);
        Long[] ads={80L,119L,120L,121L,122L,123L};//广告位编码
        List<AdPosition> list=adPositionService.findList(ads);
        model.addAttribute("adPositions", list);
        model.addAttribute("types", Ad.Type.values());
        model.addAttribute("linkTypes", Ad.LinkType.values());
        model.addAttribute("ad", adService.find(id));
        Tenant tenant = tenantService.find(tenantId);
        List<Product> products = productService.findMyList(tenant, null, null, null, null, null, null, null, null, true,
                null, null, null, null, null,null, null);
        model.addAttribute("products", products);
        model.addAttribute("menu", "tenant_management");
        model.addAttribute("tenant",tenant);
        return "/admin/tenant/editAd";
    }

    /**
     * 广告更新
     */
    @RequestMapping(value = "/updatePosition", method = RequestMethod.POST)
    public String adUpdate(Ad ad, Long tenantId,Long adPositionId, RedirectAttributes redirectAttributes, String path) {
        Member member = memberService.getCurrent();
        Tenant tenant = tenantService.find(tenantId);
        ad.setAdPosition(adPositionService.find(adPositionId));
        ad.setTenant(tenant);
        if (!isValid(ad)) {
            return ERROR_VIEW;
        }
        if (ad.getType() == Ad.Type.text) {
            ad.setPath(null);
        } else {
            ad.setContent(null);
        }
        adService.update(ad);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:adList.jhtml";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody Message adDelete(Long[] ids) {
        adService.delete(ids);
        return SUCCESS_MESSAGE;
    }
}
