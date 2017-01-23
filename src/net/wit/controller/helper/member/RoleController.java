package net.wit.controller.helper.member;

import net.wit.*;
import net.wit.Message;
import net.wit.controller.weixin.model.DataBlock;
import net.wit.controller.helper.model.BarcodeModel;
import net.wit.controller.helper.model.TenantRulesModel;
import net.wit.controller.helper.model.TenantRulesRoleModel;
import net.wit.entity.*;
import net.wit.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.constraints.Null;
import java.util.*;

@Controller("helperMemberRoleController")
@RequestMapping("/helper/member/role")
public class RoleController extends BaseController {

    @Resource(name = "memberServiceImpl")
    private MemberService memberService;

    @Resource(name = "tenantRulesServiceImpl")
    private TenantRulesService tenantRulesService;

    @Resource(name = "tenantRulesRoleServiceImpl")
    private TenantRulesRoleService tenantRulesRoleService;

    @Resource(name = "roleServiceImpl")
    private RoleService roleService;

    @Resource(name = "employeeServiceImpl")
    private EmployeeService employeeService;

    /**
     * 添加
     *
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(String redirectUrl, ModelMap model) {
        List<TenantRules> ruless = tenantRulesService.findTree(2);

        model.addAttribute("role", new Role());
        model.addAttribute("readRulesIds", new Long[0]);
        model.addAttribute("editRulesIds", new Long[0]);
        model.addAttribute("ruless", ruless);
        model.addAttribute("types", TenantRules.Type.values());
        model.addAttribute("redirectUrl", redirectUrl);
        return "helper/member/role/add";
    }

    /**
     * 保存
     * 注：拥有编辑权限时，查看权限一定要拥有
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Role role,
                       String  redirectUrl,
                       Long[] readRulesIds, Long[] updateRulesIds, /*读权限 、修改权限 */
                       Long[] addRulesIds, Long[] expRulesIds,/*新增权限、导出权限*/
                       Long[] delRulesIds, Long[] refillRulesIds,/*删除权限 、充值权限 */
                       Long[] cashRulesIds, Long[] confirmRulesIds,/*提现权限 、确认受理权限 */
                       Long[] dismissalRulesIds, Long[] modifyPriceRulesIds,/*拒绝受理权限 、调价权限 */
                       Long[] upMarketRulesIds, Long[] downMarketRulesIds,/*商品上架权限 、商品下架权限 */
                       Long[] printRulesIds, Long[] paymentRulesIds, Long[] appliedRulesIds,/*打印权限 、缴款权限 、审核权限 */
                       Long[] statisticsRulesIds, Long[] shareRulesIds, Long[] superviseRulesIds,/*统计权限 、分享权限 、管理权限 */
                       Long[] refuseReturnRulesIds, Long[] agreeReturnRulesIds, Long[] sendGoodsRulesIds,/*拒绝退货、同意退货 、发货 */
                       Long[] cancelOrderRulesIds, Long[] closeRulesIds, Long[] openRulesIds,Long[] qrCodeRulesIds,/*取消订单 、关闭、二维码*/
                       RedirectAttributes redirectAttributes) {

        //数组转List

        List<Long> _readRulesIds = new ArrayList<Long>();
        if (readRulesIds != null && readRulesIds.length > 0) {
            _readRulesIds = new ArrayList(Arrays.asList(readRulesIds));
        }
        List<Long> _editRulesIds = new ArrayList<Long>();
        if (updateRulesIds != null && updateRulesIds.length > 0) {
            _editRulesIds = new ArrayList(Arrays.asList(updateRulesIds));
        }
        List<Long> _addRulesIds = new ArrayList<Long>();
        if (addRulesIds != null && addRulesIds.length > 0) {
            _addRulesIds = new ArrayList(Arrays.asList(addRulesIds));
        }
        List<Long> _expRulesIds = new ArrayList<Long>();
        if (expRulesIds != null && expRulesIds.length > 0) {
            _expRulesIds = new ArrayList(Arrays.asList(expRulesIds));
        }
        List<Long> _deleteRulesIds = new ArrayList<Long>();
        if (delRulesIds != null && delRulesIds.length > 0) {
            _deleteRulesIds = new ArrayList(Arrays.asList(delRulesIds));
        }
        List<Long> _refillRulesIds = new ArrayList<Long>();
        if (refillRulesIds != null && refillRulesIds.length > 0) {
            _refillRulesIds = new ArrayList(Arrays.asList(refillRulesIds));
        }
        List<Long> _cashRulesIds = new ArrayList<Long>();
        if (cashRulesIds != null && cashRulesIds.length > 0) {
            _cashRulesIds = new ArrayList(Arrays.asList(cashRulesIds));
        }
        List<Long> _confirmRulesIds = new ArrayList<Long>();
        if (confirmRulesIds != null && confirmRulesIds.length > 0) {
            _confirmRulesIds = new ArrayList(Arrays.asList(confirmRulesIds));
        }
        List<Long> _dismissalRulesIds = new ArrayList<Long>();
        if (dismissalRulesIds != null && dismissalRulesIds.length > 0) {
            _dismissalRulesIds = new ArrayList(Arrays.asList(dismissalRulesIds));
        }
        List<Long> _modifyPriceRulesIds = new ArrayList<Long>();
        if (modifyPriceRulesIds != null && modifyPriceRulesIds.length > 0) {
            _modifyPriceRulesIds = new ArrayList(Arrays.asList(modifyPriceRulesIds));
        }
        List<Long> _upMarketRulesIds = new ArrayList<Long>();
        if (upMarketRulesIds != null && upMarketRulesIds.length > 0) {
            _upMarketRulesIds = new ArrayList(Arrays.asList(upMarketRulesIds));
        }
        List<Long> _downMarketRulesIds = new ArrayList<Long>();
        if (downMarketRulesIds != null && downMarketRulesIds.length > 0) {
            _downMarketRulesIds = new ArrayList(Arrays.asList(downMarketRulesIds));
        }
        List<Long> _printRulesIds = new ArrayList<Long>();
        if (printRulesIds != null && printRulesIds.length > 0) {
            _printRulesIds = new ArrayList(Arrays.asList(printRulesIds));
        }
        List<Long> _paymentRulesIds = new ArrayList<Long>();
        if (paymentRulesIds != null && paymentRulesIds.length > 0) {
            _paymentRulesIds = new ArrayList(Arrays.asList(paymentRulesIds));
        }
        List<Long> _appliedRulesIds = new ArrayList<Long>();
        if (appliedRulesIds != null && appliedRulesIds.length > 0) {
            _appliedRulesIds = new ArrayList(Arrays.asList(appliedRulesIds));
        }
        List<Long> _statisticsRulesIds = new ArrayList<Long>();
        if (statisticsRulesIds != null && statisticsRulesIds.length > 0) {
            _statisticsRulesIds = new ArrayList(Arrays.asList(statisticsRulesIds));
        }
        List<Long> _shareRulesIds = new ArrayList<Long>();
        if (shareRulesIds != null && shareRulesIds.length > 0) {
            _shareRulesIds = new ArrayList(Arrays.asList(shareRulesIds));
        }
        List<Long> _superviseRulesIds = new ArrayList<Long>();
        if (superviseRulesIds != null && superviseRulesIds.length > 0) {
            _superviseRulesIds = new ArrayList(Arrays.asList(superviseRulesIds));
        }

        List<Long> _refuseReturnRulesIds = new ArrayList<Long>();
        if (refuseReturnRulesIds != null && refuseReturnRulesIds.length > 0) {
            _refuseReturnRulesIds = new ArrayList(Arrays.asList(refuseReturnRulesIds));
        }
        List<Long> _agreeReturnRulesIds = new ArrayList<Long>();
        if (agreeReturnRulesIds != null && agreeReturnRulesIds.length > 0) {
            _agreeReturnRulesIds = new ArrayList(Arrays.asList(agreeReturnRulesIds));
        }
        List<Long> _sendGoodsRulesIds = new ArrayList<Long>();
        if (sendGoodsRulesIds != null && sendGoodsRulesIds.length > 0) {
            _sendGoodsRulesIds = new ArrayList(Arrays.asList(sendGoodsRulesIds));
        }
        List<Long> _cancelOrderRulesIds = new ArrayList<Long>();
        if (cancelOrderRulesIds != null && cancelOrderRulesIds.length > 0) {
            _cancelOrderRulesIds = new ArrayList(Arrays.asList(cancelOrderRulesIds));
        }

        List<Long> _closeRulesIds = new ArrayList<Long>();
        if (closeRulesIds != null && closeRulesIds.length > 0) {
            _closeRulesIds = new ArrayList(Arrays.asList(closeRulesIds));
        }
        List<Long> _openRulesIds = new ArrayList<Long>();
        if (openRulesIds != null && openRulesIds.length > 0) {
            _openRulesIds = new ArrayList(Arrays.asList(openRulesIds));
        }
        List<Long> _qrCodeRulesIds = new ArrayList<Long>();
        if (qrCodeRulesIds != null && qrCodeRulesIds.length > 0) {
            _qrCodeRulesIds = new ArrayList(Arrays.asList(qrCodeRulesIds));
        }

        //获取当前商家
        Member member = memberService.getCurrent();
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return ERROR_VIEW;
        }

        try {
            //角色
            //role
            role.setIsSystem(false);
            role.setTenant(tenant);
            role.setRoleType(Role.RoleType.helper);
            roleService.save(role);
            //遍历查看权限列表
            for (Long rulesId : _readRulesIds) {
                TenantRulesRole tenantRulesRole = new TenantRulesRole();
                tenantRulesRole.setRole(role);
                TenantRules tenantRules = tenantRulesService.find(rulesId);
                tenantRulesRole.setRules(tenantRules);
                tenantRulesRole.setReadAuthority(true);
                //查看是否拥有编辑权限
                tenantRulesRole.setReadAuthority(true);
                tenantRulesRole.setUpdateAuthority((_editRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setAddAuthority((_addRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setDelAuthority((_deleteRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setExpAuthority((_expRulesIds.contains(rulesId) ? true : false));

                tenantRulesRole.setRefillAuthority((_refillRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setCashAuthority((_cashRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setConfirmAuthority((_confirmRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setDismissalAuthority((_dismissalRulesIds.contains(rulesId) ? true : false));

                tenantRulesRole.setModifyPriceAuthority((_modifyPriceRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setUpMarketAuthority((_upMarketRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setDownMarketAuthority((_downMarketRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setPrintAuthority((_printRulesIds.contains(rulesId) ? true : false));

                tenantRulesRole.setPaymentAuthority((_paymentRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setAppliedAuthority((_appliedRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setStatisticsAuthority((_statisticsRulesIds.contains(rulesId) ? true : false));

                tenantRulesRole.setShareAuthority((_shareRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setSuperviseAuthority((_superviseRulesIds.contains(rulesId) ? true : false));

                tenantRulesRole.setRefuseReturnAuthority((_refuseReturnRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setAgreeReturnAuthority((_agreeReturnRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setSendGoodsAuthority((_sendGoodsRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setCancelOrderAuthority((_cancelOrderRulesIds.contains(rulesId) ? true : false));

                tenantRulesRole.setCloseAuthority((_closeRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setOpenAuthority((_openRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setQrCodeAuthority((_qrCodeRulesIds.contains(rulesId) ? true : false));

                tenantRulesRoleService.save(tenantRulesRole);
            }
            addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
            if (redirectUrl!=null){
                return  "redirect:"+redirectUrl;
            }
            return "redirect:/helper/member/role/list.jhtml";
        } catch (Exception e) {
            e.printStackTrace();
            addFlashMessage(redirectAttributes, ERROR_MESSAGE);
            return "redirect:/helper/member/role/add.jhtml";
        }

    }

    /**
     * 编辑
     *
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Long id, ModelMap model) {
        List<TenantRules> ruless = tenantRulesService.findTree(2);
        Role role = roleService.find(id);

        //该角色拥有的权限的规则
        Map<String, List<Long>> havsRulesIds = new HashMap<>();
        for (TenantRules.Type type : TenantRules.Type.values()) {
            List<Long> readRulesIds = tenantRulesRoleService.findRulesIdsByRole(role, type.name());
            havsRulesIds.put(type.name(), readRulesIds);
        }

        model.addAttribute("ruless", ruless);
        model.addAttribute("role", role);
        model.addAttribute("types", TenantRules.Type.values());
        model.addAttribute("havsRulesIds", havsRulesIds);
        return "helper/member/role/edit";
    }


    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(ModelMap model, Pageable pageable, String keyWord) {
        Member member = memberService.getCurrent();
        Tenant tenant = member.getTenant();
        if (tenant == null) {
            return ERROR_VIEW;
        }
        //添加页面筛选条件
//        try{

        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("tenant", Filter.Operator.eq, tenant));
        pageable.setFilters(filters);
        Page<Role> page = roleService.findPage(Role.RoleType.helper, pageable);
        model.addAttribute("keyWord", keyWord);
        model.addAttribute("page", page);
        model.addAttribute("types", TenantRules.Type.values());
//        }catch (Exception e){
//            int i=1;
//        }
        return "/helper/member/role/list";
    }

    /**
     * 更新
     * 注：拥有编辑权限时，查看权限一定要拥有
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Role role,
                         Long[] readRulesIds, Long[] updateRulesIds, /*读权限 、修改权限 */
                         Long[] addRulesIds, Long[] expRulesIds,/*新增权限、导出权限*/
                         Long[] delRulesIds, Long[] refillRulesIds,/*删除权限 、充值权限 */
                         Long[] cashRulesIds, Long[] confirmRulesIds,/*提现权限 、确认受理权限 */
                         Long[] dismissalRulesIds, Long[] modifyPriceRulesIds,/*拒绝受理权限 、调价权限 */
                         Long[] upMarketRulesIds, Long[] downMarketRulesIds,/*商品上架权限 、商品下架权限 */
                         Long[] printRulesIds, Long[] paymentRulesIds, Long[] appliedRulesIds,/*打印权限 、缴款权限 、审核权限 */
                         Long[] statisticsRulesIds, Long[] shareRulesIds, Long[] superviseRulesIds,/*统计权限 、分享权限 、管理权限 */
                         Long[] refuseReturnRulesIds, Long[] agreeReturnRulesIds, Long[] sendGoodsRulesIds,/*拒绝退货、同意退货 、发货 */
                         Long[] cancelOrderRulesIds, Long[] closeRulesIds, Long[] openRulesIds,Long[] qrCodeRulesIds,/*取消订单 、关闭、二维码*/
                         RedirectAttributes redirectAttributes) {

        List<Long> _readRulesIds = new ArrayList<Long>();
        if (readRulesIds != null && readRulesIds.length > 0) {
            _readRulesIds = new ArrayList(Arrays.asList(readRulesIds));
        }
        List<Long> _editRulesIds = new ArrayList<Long>();
        if (updateRulesIds != null && updateRulesIds.length > 0) {
            _editRulesIds = new ArrayList(Arrays.asList(updateRulesIds));
        }
        List<Long> _addRulesIds = new ArrayList<Long>();
        if (addRulesIds != null && addRulesIds.length > 0) {
            _addRulesIds = new ArrayList(Arrays.asList(addRulesIds));
        }
        List<Long> _expRulesIds = new ArrayList<Long>();
        if (expRulesIds != null && expRulesIds.length > 0) {
            _expRulesIds = new ArrayList(Arrays.asList(expRulesIds));
        }
        List<Long> _deleteRulesIds = new ArrayList<Long>();
        if (delRulesIds != null && delRulesIds.length > 0) {
            _deleteRulesIds = new ArrayList(Arrays.asList(delRulesIds));
        }
        List<Long> _refillRulesIds = new ArrayList<Long>();
        if (refillRulesIds != null && refillRulesIds.length > 0) {
            _refillRulesIds = new ArrayList(Arrays.asList(refillRulesIds));
        }
        List<Long> _cashRulesIds = new ArrayList<Long>();
        if (cashRulesIds != null && cashRulesIds.length > 0) {
            _cashRulesIds = new ArrayList(Arrays.asList(cashRulesIds));
        }
        List<Long> _confirmRulesIds = new ArrayList<Long>();
        if (confirmRulesIds != null && confirmRulesIds.length > 0) {
            _confirmRulesIds = new ArrayList(Arrays.asList(confirmRulesIds));
        }
        List<Long> _dismissalRulesIds = new ArrayList<Long>();
        if (dismissalRulesIds != null && dismissalRulesIds.length > 0) {
            _dismissalRulesIds = new ArrayList(Arrays.asList(dismissalRulesIds));
        }
        List<Long> _modifyPriceRulesIds = new ArrayList<Long>();
        if (modifyPriceRulesIds != null && modifyPriceRulesIds.length > 0) {
            _modifyPriceRulesIds = new ArrayList(Arrays.asList(modifyPriceRulesIds));
        }
        List<Long> _upMarketRulesIds = new ArrayList<Long>();
        if (upMarketRulesIds != null && upMarketRulesIds.length > 0) {
            _upMarketRulesIds = new ArrayList(Arrays.asList(upMarketRulesIds));
        }
        List<Long> _downMarketRulesIds = new ArrayList<Long>();
        if (downMarketRulesIds != null && downMarketRulesIds.length > 0) {
            _downMarketRulesIds = new ArrayList(Arrays.asList(downMarketRulesIds));
        }
        List<Long> _printRulesIds = new ArrayList<Long>();
        if (printRulesIds != null && printRulesIds.length > 0) {
            _printRulesIds = new ArrayList(Arrays.asList(printRulesIds));
        }
        List<Long> _paymentRulesIds = new ArrayList<Long>();
        if (paymentRulesIds != null && paymentRulesIds.length > 0) {
            _paymentRulesIds = new ArrayList(Arrays.asList(paymentRulesIds));
        }
        List<Long> _appliedRulesIds = new ArrayList<Long>();
        if (appliedRulesIds != null && appliedRulesIds.length > 0) {
            _appliedRulesIds = new ArrayList(Arrays.asList(appliedRulesIds));
        }
        List<Long> _statisticsRulesIds = new ArrayList<Long>();
        if (statisticsRulesIds != null && statisticsRulesIds.length > 0) {
            _statisticsRulesIds = new ArrayList(Arrays.asList(statisticsRulesIds));
        }
        List<Long> _shareRulesIds = new ArrayList<Long>();
        if (shareRulesIds != null && shareRulesIds.length > 0) {
            _shareRulesIds = new ArrayList(Arrays.asList(shareRulesIds));
        }
        List<Long> _superviseRulesIds = new ArrayList<Long>();
        if (superviseRulesIds != null && superviseRulesIds.length > 0) {
            _superviseRulesIds = new ArrayList(Arrays.asList(superviseRulesIds));
        }

        List<Long> _refuseReturnRulesIds = new ArrayList<Long>();
        if (refuseReturnRulesIds != null && refuseReturnRulesIds.length > 0) {
            _refuseReturnRulesIds = new ArrayList(Arrays.asList(refuseReturnRulesIds));
        }
        List<Long> _agreeReturnRulesIds = new ArrayList<Long>();
        if (agreeReturnRulesIds != null && agreeReturnRulesIds.length > 0) {
            _agreeReturnRulesIds = new ArrayList(Arrays.asList(agreeReturnRulesIds));
        }
        List<Long> _sendGoodsRulesIds = new ArrayList<Long>();
        if (sendGoodsRulesIds != null && sendGoodsRulesIds.length > 0) {
            _sendGoodsRulesIds = new ArrayList(Arrays.asList(sendGoodsRulesIds));
        }
        List<Long> _cancelOrderRulesIds = new ArrayList<Long>();
        if (cancelOrderRulesIds != null && cancelOrderRulesIds.length > 0) {
            _cancelOrderRulesIds = new ArrayList(Arrays.asList(cancelOrderRulesIds));
        }

        List<Long> _closeRulesIds = new ArrayList<Long>();
        if (closeRulesIds != null && closeRulesIds.length > 0) {
            _closeRulesIds = new ArrayList(Arrays.asList(closeRulesIds));
        }
        List<Long> _openRulesIds = new ArrayList<Long>();
        if (openRulesIds != null && openRulesIds.length > 0) {
            _openRulesIds = new ArrayList(Arrays.asList(openRulesIds));
        }

        List<Long> _qrCodeRulesIds = new ArrayList<Long>();
        if (qrCodeRulesIds != null && qrCodeRulesIds.length > 0) {
            _qrCodeRulesIds = new ArrayList(Arrays.asList(qrCodeRulesIds));
        }
        //id=7&username=店长&readRulesIds=200&editRulesIds=200
        try {
            //角色
            Role upRole = roleService.find(role.getId());
            upRole.setName(role.getName());
            upRole.setDescription(role.getDescription());
//            role.setTenant(memberService.getCurrent().getTenant());
            roleService.update(upRole);
            //角色规则
            List<TenantRulesRole> tenantRulesRoles = tenantRulesRoleService.findByRoleId(role.getId());
            //遍历以前的拥有的权限，修改用户此次拥有权限，删除用户取消授权的规则
            if (tenantRulesRoles.size() > 0) {
                for (TenantRulesRole tenantRulesRole : tenantRulesRoles) {
                    Long rulesId = tenantRulesRole.getRules().getId();
                    if (_readRulesIds.contains(rulesId)) {
                        //权限修改
                        tenantRulesRole.setReadAuthority(true);
                        tenantRulesRole.setUpdateAuthority((_editRulesIds.contains(rulesId) ? true : false));
                        tenantRulesRole.setAddAuthority((_addRulesIds.contains(rulesId) ? true : false));
                        tenantRulesRole.setDelAuthority((_deleteRulesIds.contains(rulesId) ? true : false));
                        tenantRulesRole.setExpAuthority((_expRulesIds.contains(rulesId) ? true : false));

                        tenantRulesRole.setRefillAuthority((_refillRulesIds.contains(rulesId) ? true : false));
                        tenantRulesRole.setCashAuthority((_cashRulesIds.contains(rulesId) ? true : false));
                        tenantRulesRole.setConfirmAuthority((_confirmRulesIds.contains(rulesId) ? true : false));
                        tenantRulesRole.setDismissalAuthority((_dismissalRulesIds.contains(rulesId) ? true : false));

                        tenantRulesRole.setModifyPriceAuthority((_modifyPriceRulesIds.contains(rulesId) ? true : false));
                        tenantRulesRole.setUpMarketAuthority((_upMarketRulesIds.contains(rulesId) ? true : false));
                        tenantRulesRole.setDownMarketAuthority((_downMarketRulesIds.contains(rulesId) ? true : false));
                        tenantRulesRole.setPrintAuthority((_printRulesIds.contains(rulesId) ? true : false));

                        tenantRulesRole.setPaymentAuthority((_paymentRulesIds.contains(rulesId) ? true : false));
                        tenantRulesRole.setAppliedAuthority((_appliedRulesIds.contains(rulesId) ? true : false));
                        tenantRulesRole.setStatisticsAuthority((_statisticsRulesIds.contains(rulesId) ? true : false));

                        tenantRulesRole.setShareAuthority((_shareRulesIds.contains(rulesId) ? true : false));
                        tenantRulesRole.setSuperviseAuthority((_superviseRulesIds.contains(rulesId) ? true : false));

                        tenantRulesRole.setRefuseReturnAuthority((_refuseReturnRulesIds.contains(rulesId) ? true : false));
                        tenantRulesRole.setAgreeReturnAuthority((_agreeReturnRulesIds.contains(rulesId) ? true : false));
                        tenantRulesRole.setSendGoodsAuthority((_sendGoodsRulesIds.contains(rulesId) ? true : false));
                        tenantRulesRole.setCancelOrderAuthority((_cancelOrderRulesIds.contains(rulesId) ? true : false));

                        tenantRulesRole.setCloseAuthority((_closeRulesIds.contains(rulesId) ? true : false));
                        tenantRulesRole.setOpenAuthority((_openRulesIds.contains(rulesId) ? true : false));
                        tenantRulesRole.setQrCodeAuthority((_qrCodeRulesIds.contains(rulesId) ? true : false));
                        tenantRulesRoleService.update(tenantRulesRole);

                        _readRulesIds.remove(rulesId);
                    } else {
                        tenantRulesRoleService.delete(tenantRulesRole);
                    }
                }
            }
            //遍历剩下的查看权限列表
            for (Long rulesId : _readRulesIds) {
                //是否存在记录
                TenantRulesRole tenantRulesRole = new TenantRulesRole();
                tenantRulesRole.setRole(role);
                TenantRules tenantRules = tenantRulesService.find(rulesId);
                tenantRulesRole.setRules(tenantRules);
                //权限设置
                tenantRulesRole.setReadAuthority(true);
                tenantRulesRole.setUpdateAuthority((_editRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setAddAuthority((_addRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setDelAuthority((_deleteRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setExpAuthority((_expRulesIds.contains(rulesId) ? true : false));

                tenantRulesRole.setRefillAuthority((_refillRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setCashAuthority((_cashRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setConfirmAuthority((_confirmRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setDismissalAuthority((_dismissalRulesIds.contains(rulesId) ? true : false));

                tenantRulesRole.setModifyPriceAuthority((_modifyPriceRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setUpMarketAuthority((_upMarketRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setDownMarketAuthority((_downMarketRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setPrintAuthority((_printRulesIds.contains(rulesId) ? true : false));

                tenantRulesRole.setPaymentAuthority((_paymentRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setAppliedAuthority((_appliedRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setStatisticsAuthority((_statisticsRulesIds.contains(rulesId) ? true : false));

                tenantRulesRole.setShareAuthority((_shareRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setSuperviseAuthority((_superviseRulesIds.contains(rulesId) ? true : false));

                tenantRulesRole.setRefuseReturnAuthority((_refuseReturnRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setAgreeReturnAuthority((_agreeReturnRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setSendGoodsAuthority((_sendGoodsRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setCancelOrderAuthority((_cancelOrderRulesIds.contains(rulesId) ? true : false));

                tenantRulesRole.setCloseAuthority((_closeRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setOpenAuthority((_openRulesIds.contains(rulesId) ? true : false));
                tenantRulesRole.setQrCodeAuthority((_qrCodeRulesIds.contains(rulesId) ? true : false));
                tenantRulesRoleService.save(tenantRulesRole);
            }
            addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
            return "redirect:/helper/member/role/list.jhtml";
        } catch (Exception e) {
            e.printStackTrace();
            addFlashMessage(redirectAttributes, ERROR_MESSAGE);
            return "redirect:/helper/member/role/edit.jhtml?id=" + role.getId();
        }

    }


    /**
     * 删除
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Message delete(Long[] ids) {
        try {
            //
            //检查是否有员工绑定了角色
            for (Long id : ids) {

                Role role = roleService.find(id);
                if (role.getIsSystem()) {
                    return Message.error("删除失败,内置角色【" + role.getName() + "】不允许删除！");
                }


                Pageable pageable = new Pageable();
                List<Filter> filter = new ArrayList<>();
                filter.add(new Filter("role", Filter.Operator.like, "%" + id + "%"));
                pageable.setFilters(filter);
                Page<Employee> employee = employeeService.findPage(pageable);
                if (employee.getContent().size() > 0) {
                    return Message.error("删除失败,请先解除拥有该角色【" + role.getName() + "】的员工");
                }


            }
            //删除操作
            for (Long id : ids) {
                List<TenantRulesRole> listTenantRulesRole = tenantRulesRoleService.findByRoleId(id);
                for (TenantRulesRole itemTenantRulesRole : listTenantRulesRole) {
                    tenantRulesRoleService.delete(itemTenantRulesRole);
                }
            }
            roleService.delete(ids);

        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("删除失败");
        }
        return Message.success("操作成功");
    }

    @RequestMapping(value = "/isRoleName", method = RequestMethod.POST)
    @ResponseBody
    public Message isRoleName(String name) {

        //角色名称查重
        boolean isActivityName = roleService.isRoleName(name, Role.RoleType.helper, memberService.getCurrent().getTenant(), null);

        if (isActivityName) {
            return Message.error("当前角色名称已经存在,请重新填写角色名称！");
        }
        return Message.success("success");
    }

    @RequestMapping(value = "/getRules", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock getRules(Long id) {
        //

        List<TenantRulesRole> tenantRulesRole = tenantRulesRoleService.findByRoleId(id);
        return DataBlock.success(TenantRulesRoleModel.bindData(tenantRulesRole), "执行成功");
    }
}


