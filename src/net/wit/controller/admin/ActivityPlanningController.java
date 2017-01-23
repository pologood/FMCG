package net.wit.controller.admin;

import net.wit.Message;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.weixin.model.DataBlock;
import net.wit.controller.weixin.model.TenantListModel;
import net.wit.entity.*;
import net.wit.entity.model.CouponSumerModel;
import net.wit.service.*;
import net.wit.util.DateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2016/8/1.
 */
@Controller("activityPlanningController")
@RequestMapping("/admin/activity_planning")
public class ActivityPlanningController extends BaseController {

    @Resource(name = "activityPlanningServiceImpl")
    private ActivityPlanningService activityPlanningService;

    @Resource(name = "adPositionServiceImpl")
    private AdPositionService adPositionService;

    @Resource(name = "singleProductPositionServiceImpl")
    private SingleProductPositionService singleProductPositionService;

    @Resource(name = "couponServiceImpl")
    private CouponService couponService;

    @Resource(name = "couponCodeServiceImpl")
    private CouponCodeService couponCodeService;

    @Resource(name = "tenantServiceImpl")
    private TenantService tenantService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(ModelMap modelMap, String searchValue, Pageable page) {
        modelMap.addAttribute("page", activityPlanningService.openPage(page, searchValue));
        return "/admin/activity_planning/list";
    }

    /**
     * 统计
     *
     * @param type
     * @param id
     * @param pageable
     * @param request
     * @return
     */
    @RequestMapping(value = "/sumer", method = RequestMethod.GET)
    public String sumer(CouponSumerModel.Type type, Long id, Pageable pageable, HttpServletRequest request, ModelMap model) {
        Coupon coupon = couponService.find(id);
        if (coupon == null) {
            return ERROR_VIEW;
        }
        Page<CouponSumerModel> page = couponService.sumer(coupon, type, pageable);
        model.addAttribute("type", type);
        model.addAttribute("id", id);
        model.addAttribute("page", page);
        model.addAttribute("total", couponService.count(coupon, type));
        return "/admin/activity_planning/sumer";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(ModelMap modelMap) {
        return "/admin/activity_planning/add";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(ActivityPlanning activityPlanning, BigDecimal[] amounts, Integer[] counts, BigDecimal[] minimumPrices, Long[] tenantIds, String[] activityNames, RedirectAttributes redirectAttributes) {
        activityPlanningService.save(activityPlanning);

        Set<Coupon> coupons = new HashSet<>();
        if (amounts != null && amounts.length > 0) {
            for (int i = 0; i < amounts.length; i++) {
                if (amounts[i] != null) {
                    Coupon coupon = new Coupon();
                    coupon.setType(Coupon.Type.coupon);
                    coupon.setAmount(amounts[i]);
                    coupon.setCount(counts[i]);
                    coupon.setMinimumPrice(minimumPrices[i]);
                    coupon.setStartDate(activityPlanning.getBeginDate());
                    coupon.setEndDate(activityPlanning.getEndDate());
                    coupon.setIsEnabled(true);
                    coupon.setMinimumQuantity(null);
                    coupon.setMaximumQuantity(null);
                    coupon.setMaximumPrice(null);
                    coupon.setName(coupon.getAmount() + "元代金券");
                    coupon.setUsedCount(0);
                    coupon.setSendCount(0);
                    coupon.setPoint(0L);
                    coupon.setEffectiveDays(0);
                    coupon.setIsReceiveMore(true);
                    coupon.setPrefix("c");
                    coupon.setStatus(Coupon.Status.confirmed);
                    coupon.setPriceExpression("price-".concat(coupon.getAmount().toString()));
                    coupon.setTenant(null);
                    coupon.setIsExchange(false);
                    coupon.setActivityPlanning(activityPlanning);
                    couponService.save(coupon);
                    int _days = (int) ((activityPlanning.getEndDate().getTime() - activityPlanning.getBeginDate().getTime()) / 86400000);

                    System.out.println(_days);
                    Long couponCount = couponCodeService.findAllCount(coupon);
                    if (couponCount == null || !(couponCount.compareTo(coupon.getCount().longValue()) == 0)) {
                        if (activityPlanning.getOnOff() == ActivityPlanning.OnOff.on) {
                            int days = (int) ((activityPlanning.getEndDate().getTime() - activityPlanning.getBeginDate().getTime()) / 86400000);
                            for (int ii = 0; ii < days; ii++) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(activityPlanning.getBeginDate());
                                calendar.add(Calendar.DATE, ii);
                                couponCodeService.build(coupon, counts[i], DateUtil.setEndDay(calendar));
                            }
                        }
                    }
                    coupons.add(coupon);
                }
            }
        }

        Set<Tenant> tenants = new HashSet<>();
        if (tenantIds != null && tenantIds.length > 0) {
            for (Long id : tenantIds) {
                Tenant tenant = tenantService.find(id);
                if (tenant != null) {
                    tenants.add(tenant);
                }
            }
        }

        Set<AdPosition> adPositions = new HashSet<>();
        Set<SingleProductPosition> singleProductPositions = new HashSet<>();

        if (activityNames != null && activityNames.length > 0) {
            for (String activityName : activityNames) {
                if (activityPlanning.getType().equals(ActivityPlanning.Type.unionActivity)) {
                    SingleProductPosition singleProductPosition = new SingleProductPosition();
                    singleProductPosition.setType(SingleProductPosition.Type.single);
                    singleProductPosition.setName(activityName);
                    singleProductPosition.setDescription(activityName);
                    singleProductPosition.setActivityPlanning(activityPlanning);
                    singleProductPositionService.save(singleProductPosition);
                    singleProductPositions.add(singleProductPosition);
                } else {
                    AdPosition adPosition = new AdPosition();
                    adPosition.setName("平台活动>" + activityName);
                    adPosition.setDescription(activityName);
                    adPosition.setType(AdPosition.Type.activity);
                    adPosition.setWidth(10);
                    adPosition.setHeight(10);
                    adPosition.setTemplate("");
                    adPosition.setAds(null);
                    adPosition.setProductChannel(null);
                    adPosition.setActivityPlanning(activityPlanning);
                    adPositionService.save(adPosition);
                    adPositions.add(adPosition);
                }

            }
        }

        activityPlanning.setCoupons(coupons);
        activityPlanning.setTenants(tenants);
        if (adPositions.size() > 0) {
            activityPlanning.setAdPositions(adPositions);
        }
        if (singleProductPositions.size() > 0) {
            activityPlanning.setSingleProductPositions(singleProductPositions);
        }
        activityPlanningService.update(activityPlanning);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Long id, ModelMap modelMap) {
        modelMap.addAttribute("plan", activityPlanningService.find(id));
        modelMap.addAttribute("id", id);
        return "/admin/activity_planning/edit";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(ActivityPlanning activityPlanning, Long activityPlanningId,
                         Long[] couponIds, BigDecimal[] amounts, Integer[] counts, BigDecimal[] minimumPrices,
                         Long[] tenantIds, String delCoupon, String delTen, String delActivity, String delSingleProduct,
                         Long[] activityIds, String[] activityNames, RedirectAttributes redirectAttributes) {
        ActivityPlanning planning = activityPlanningService.find(activityPlanningId);
        planning.setName(activityPlanning.getName());
        planning.setTenantMaximumOrders(activityPlanning.getTenantMaximumOrders());
        planning.setActivityMaximumOrders(activityPlanning.getActivityMaximumOrders());
        planning.setOnOff(activityPlanning.getOnOff());
        planning.setBeginDate(activityPlanning.getBeginDate());
        planning.setEndDate(activityPlanning.getEndDate());
        planning.setIntroduction(activityPlanning.getIntroduction());

        if (delCoupon != null) {
            String[] _delCoupon = delCoupon.split(",");
            for (String couponId : _delCoupon) {
                Coupon cou = couponService.find(Long.parseLong(couponId));
                if (cou != null) {
                    if (planning.getCoupons().contains(cou)) {
                        planning.getCoupons().remove(cou);
                    }
                    couponService.delete(cou);
                }
            }
        }

        if (delActivity != null) {
            String[] _delActivity = delActivity.split(",");
            for (String activityId : _delActivity) {
                AdPosition adPos = adPositionService.find(Long.parseLong(activityId));

                if (adPos != null) {
                    if (planning.getAdPositions().contains(adPos)) {
                        planning.getAdPositions().remove(adPos);
                    }
                    adPositionService.delete(adPos);
                }
            }
        }

        if (delSingleProduct != null) {
            String[] _delSingleProduct = delSingleProduct.split(",");
            for (String singlePoiId : _delSingleProduct) {
                SingleProductPosition singleProductPosition = singleProductPositionService.find(Long.parseLong(singlePoiId));

                if (singleProductPosition != null) {
                    if (planning.getSingleProductPositions().contains(singleProductPosition)) {
                        planning.getSingleProductPositions().remove(singleProductPosition);
                    }
                    singleProductPositionService.delete(singleProductPosition);
                }
            }
        }

        if (delTen != null) {
            String[] _delTen = delTen.split(",");
            for (String tenantId : _delTen) {
                Tenant tenant = tenantService.find(Long.parseLong(tenantId));
                if (planning.getTenants().contains(tenant)) {
                    planning.getTenants().remove(tenant);
                }

            }
        }

        Set<Coupon> coupons = new HashSet<>();
        if (couponIds != null && couponIds.length > 0) {
            for (int i = 0; i < couponIds.length; i++) {
                Coupon coupon = new Coupon();
                if (couponIds[i] != 0) {
                    coupon = couponService.find(couponIds[i]);
                    coupon.setAmount(amounts[i]);
                    coupon.setCount(counts[i]);
                    coupon.setMinimumPrice(minimumPrices[i]);
                    coupon.setStartDate(planning.getBeginDate());
                    coupon.setEndDate(planning.getEndDate());
                    coupon.setIsEnabled(true);
                    coupon.setMinimumQuantity(null);
                    coupon.setMaximumQuantity(null);
                    coupon.setMaximumPrice(null);
                    coupon.setName(coupon.getAmount() + "元代金券");
                    coupon.setEffectiveDays(0);
                    coupon.setIsReceiveMore(true);
                    coupon.setPrefix("c");
                    coupon.setStatus(Coupon.Status.confirmed);
                    coupon.setPriceExpression("price-".concat(coupon.getAmount().toString()));
                    coupon.setTenant(null);
                    coupon.setIsExchange(false);
                    coupon.setActivityPlanning(planning);
                    couponService.update(coupon);
                } else {
                    coupon.setType(Coupon.Type.coupon);
                    coupon.setAmount(amounts[i]);
                    coupon.setCount(counts[i]);
                    coupon.setMinimumPrice(minimumPrices[i]);
                    coupon.setStartDate(planning.getBeginDate());
                    coupon.setEndDate(planning.getEndDate());
                    coupon.setIsEnabled(true);
                    coupon.setMinimumQuantity(null);
                    coupon.setMaximumQuantity(null);
                    coupon.setMaximumPrice(null);
                    coupon.setName(coupon.getAmount() + "元代金券");
                    coupon.setUsedCount(0);
                    coupon.setSendCount(0);
                    coupon.setPoint(0L);
                    coupon.setEffectiveDays(0);
                    coupon.setIsReceiveMore(true);
                    coupon.setPrefix("c");
                    coupon.setStatus(Coupon.Status.confirmed);
                    coupon.setPriceExpression("price-".concat(coupon.getAmount().toString()));
                    coupon.setTenant(null);
                    coupon.setIsExchange(false);
                    coupon.setActivityPlanning(planning);
                    couponService.save(coupon);
                }
                Long couponCount = couponCodeService.findAllCount(coupon);
                if (couponCount == null || !(couponCount.compareTo(coupon.getCount().longValue()) == 0)) {
                    if (planning.getOnOff() == ActivityPlanning.OnOff.on) {
                        int days = (int) ((planning.getEndDate().getTime() - planning.getBeginDate().getTime()) / 86400000);

                        for (int ii = 0; ii < days; ii++) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(activityPlanning.getBeginDate());
                            calendar.add(Calendar.DATE, ii);
                            couponCodeService.build(coupon, counts[i], DateUtil.setEndDay(calendar));
                        }
                    }
                }
                coupons.add(coupon);
            }
        }

        Set<Tenant> tenants = new HashSet<>();
        if (tenantIds != null && tenantIds.length > 0) {
            for (Long tenantId : tenantIds) {
                Tenant tenant = tenantService.find(tenantId);
                if (tenant != null) {
                    tenants.add(tenant);
                }
            }
        }

        Set<AdPosition> adPositions = new HashSet<>();
        Set<SingleProductPosition> singleProductPositions = new HashSet<>();
        if (activityIds != null && activityIds.length > 0) {
            for (int i = 0; i < activityIds.length; i++) {

                if (activityPlanning.getType().equals(ActivityPlanning.Type.unionActivity)) {
                    SingleProductPosition singleProductPosition = new SingleProductPosition();

                    if (activityIds[i] != 0) {
                        singleProductPosition = singleProductPositionService.find(activityIds[i]);
                        singleProductPosition.setType(SingleProductPosition.Type.single);
                        singleProductPosition.setName(activityNames[i]);
                        singleProductPosition.setDescription(activityNames[i]);
                        singleProductPosition.setActivityPlanning(planning);
                        singleProductPositionService.update(singleProductPosition);
                    } else {
                        singleProductPosition.setType(SingleProductPosition.Type.single);
                        singleProductPosition.setName(activityNames[i]);
                        singleProductPosition.setDescription(activityNames[i]);
                        singleProductPosition.setActivityPlanning(planning);
                        singleProductPositionService.save(singleProductPosition);
                    }
                    singleProductPositions.add(singleProductPosition);
                } else {
                    AdPosition adPosition = new AdPosition();
                    if (activityIds[i] != 0) {
                        adPosition = adPositionService.find(activityIds[i]);
                        adPosition.setName("平台活动>" + activityNames[i]);
                        adPosition.setDescription(activityNames[i]);
                        adPosition.setType(AdPosition.Type.activity);
                        adPosition.setWidth(10);
                        adPosition.setHeight(10);
                        adPosition.setTemplate("");
                        adPosition.setActivityPlanning(planning);
                        adPositionService.update(adPosition);
                    } else {
                        adPosition.setName("平台活动>" + activityNames[i]);
                        adPosition.setDescription(activityNames[i]);
                        adPosition.setType(AdPosition.Type.activity);
                        adPosition.setWidth(10);
                        adPosition.setHeight(10);
                        adPosition.setTemplate("");
                        adPosition.setAds(null);
                        adPosition.setProductChannel(null);
                        adPosition.setActivityPlanning(planning);
                        adPositionService.save(adPosition);
                    }

                    adPositions.add(adPosition);
                }


            }
        }

        planning.setCoupons(coupons);
        planning.setTenants(tenants);
        if (adPositions.size() > 0) {
            planning.setAdPositions(adPositions);
        }
        if (singleProductPositions.size() > 0) {
            planning.setSingleProductPositions(singleProductPositions);
        }
        activityPlanningService.update(planning);
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list.jhtml";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Message delete(Long[] ids) {
        boolean isBegin = true;
        if (ids != null && ids.length > 0) {
            for (Long id : ids) {
                ActivityPlanning activityPlanning = activityPlanningService.find(id);

                if (activityPlanning.getOnOff() == ActivityPlanning.OnOff.on) {
                    isBegin = false;
                }
            }
        }
        if (!isBegin) {
            return Message.error("已开始活动不能删除！");
        }

        try {
            activityPlanningService.delete(ids);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.error("刪除失败");
        }

        return SUCCESS_MESSAGE;
    }

    @RequestMapping(value = "/add/count", method = RequestMethod.POST)
    public
    @ResponseBody
    Message addCount(Long id, Integer count) {
        Coupon coupon = couponService.find(id);
        if (coupon == null) {
            return ERROR_MESSAGE;
        }

        if (coupon.getExpired()) {
            couponCodeService.build(coupon, count, DateUtil.setEndDay(new Date()));
            coupon.setCount(coupon.getCount() + count);
            couponService.update(coupon);
        }

        return SUCCESS_MESSAGE;
    }

    @RequestMapping(value = "/getTenant", method = RequestMethod.POST)
    @ResponseBody
    public DataBlock getTenant(String keyword, Pageable pageable) {
        Page<Tenant> page = tenantService.openPage(pageable, null, null, null, null, null, keyword, Tenant.Status.success, Tenant.OrderType.scoreDesc);
        return DataBlock.success(TenantListModel.bindData(page.getContent()), "执行成功");
    }

    @RequestMapping(value = "/isActivityName", method = RequestMethod.POST)
    @ResponseBody
    public Message isActivityName(String name) {
        boolean isActivityName = activityPlanningService.isAcitivtyName(name);

        if (isActivityName) {
            return Message.error("当前活动已经存在,请重新填写活动名称！");
        }
        return Message.success("success");
    }
}
