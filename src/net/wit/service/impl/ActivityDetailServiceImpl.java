package net.wit.service.impl;

import com.sun.xml.internal.bind.v2.TODO;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.*;
import net.wit.entity.*;
import net.wit.service.ActivityDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2016/7/13.
 */
@Service("activityDetailServiceImpl")
public class ActivityDetailServiceImpl extends BaseServiceImpl<ActivityDetail, Long> implements ActivityDetailService {

    @Resource(name = "activityDetailDaoImpl")
    private ActivityDetailDao activityDetailDao;

    @Resource(name = "activityDetailDaoImpl")
    public void setBaseDao(ActivityDetailDao activityDetailDao) {
        super.setBaseDao(activityDetailDao);
    }

    @Resource(name = "memberDaoImpl")
    private MemberDao memberDao;

    @Resource(name = "tenantDaoImpl")
    private TenantDao tenantDao;


    @Resource(name = "activityRulesDaoImpl")
    private ActivityRulesDao activityRulesDao;

    public Page<ActivityDetail> openPage(Tenant tenant, ActivityRules activityRules, Member member, Pageable pageable){
        return activityDetailDao.openPage(tenant,activityRules,member,pageable);
    }

    public boolean isActivity(Member member, Tenant tenant, ActivityRules activityRules){
        return activityDetailDao.isActivity(member,tenant,activityRules);
    }

    @Transactional
    public void addPoint(Member member, Tenant tenant, ActivityRules activityRules){

        if(activityRules == null){
            return;
        }

        //判断是什么类型的任务，判断任务是否完成
        if(activityRules.getType() == ActivityRules.Type.growth || activityRules.getType() == ActivityRules.Type.daily){  //成长任务 or 每日任务
            if(activityDetailDao.isActivity(member ,tenant ,activityRules)){
                return;
            }
        }

        ActivityDetail activityDetail = new ActivityDetail();
        activityDetail.setFinishTime(new Date());
        activityDetail.setAmount(new BigDecimal("0.00"));

        if(member != null){
            activityDetail.setMember(member);
            //用户添加积分
            Long memberPoint = member.getPoint()==null ? 0L : member.getPoint();
            member.setPoint(memberPoint + activityRules.getPoint());
            memberDao.merge(member);
        }
        if(tenant != null){
            activityDetail.setTenant(tenant);
            //商家添加积分
            Long tenantPoint = tenant.getPoint()==null ? 0L : tenant.getPoint();
            tenant.setPoint(tenantPoint + activityRules.getPoint());
            tenantDao.merge(tenant);
        }

        activityDetail.setActivityRules(activityRules);
        if(activityRules.getType() == ActivityRules.Type.activity){  //活动任务
            activityDetail.setAmount(activityRules.getAmount());
            //历史记录不是保存在xx_activity_inventory中
        }else{ //成长任务，每日任务
            activityDetail.setPoint(activityRules.getPoint());
        }
        activityDetailDao.persist(activityDetail);

        Long[] ids1 = {1l,2l,3l,4l,5l,6l,7l,8l,9l};
        addPoints(member,tenant,ids1,10l);

        Long[] ids11 = {11l,12l,13l};
        addPoints(member,tenant,ids11,14l);

        Long[] ids2 = {15l,16l,17l,18l,19l,20l};
        addPoints(member,tenant,ids2,21l);

        Long[] ids3 = {22l,23l,24l,25l,26l,27l};
        addPoints(member,tenant,ids3,28l);

        Long[] ids4 = {29l,30l,31l,32l};
        addPoints(member,tenant,ids4,33l);

        Long[] ids5 = {34l,35l,36l};
        addPoints(member,tenant,ids5,37l);

        Long[] ids6 = {38l,39l};
        addPoints(member,tenant,ids6,40l);

        Long[] ids7 = {41l,42l,43l,44l,45l,46l};
        addPoints(member,tenant,ids7,47l);

        Long[] ids8 = {48l,49l,50l,51l,52l};
        addPoints(member,tenant,ids8,53l);
    }


    private void addPoints(Member member, Tenant tenant,Long[] ids,Long pointId){

        if(member==null&&tenant==null&&ids==null){
            return;
        }

        if(ids.length>0){
            String result="";
            for(Long id:ids){
                ActivityRules activityRules =activityRulesDao.find(id);

                if(activityRules.getType() == ActivityRules.Type.growth || activityRules.getType() == ActivityRules.Type.daily){  //成长任务 or 每日任务
                    if(activityDetailDao.isActivity(member ,tenant ,activityRules)){
                        result+="0";
                    }
                }
            }

            if(result!=""&&result.length()==ids.length){
                ActivityRules _activityRules =activityRulesDao.find(pointId);

                if(activityDetailDao.isActivity(member ,tenant ,_activityRules)){
                    return;
                }

                ActivityDetail activityDetail = new ActivityDetail();
                activityDetail.setFinishTime(new Date());
                activityDetail.setAmount(new BigDecimal("0.00"));

                if(member != null){
                    activityDetail.setMember(member);
                    //用户添加积分
                    Long memberPoint = member.getPoint()==null ? 0L : member.getPoint();
                    member.setPoint(memberPoint + _activityRules.getPoint());
                    memberDao.merge(member);
                }
                if(tenant != null){
                    activityDetail.setTenant(tenant);
                    //商家添加积分
                    Long tenantPoint = tenant.getPoint()==null ? 0L : tenant.getPoint();
                    tenant.setPoint(tenantPoint + _activityRules.getPoint());
                    tenantDao.merge(tenant);
                }

                activityDetail.setActivityRules(_activityRules);
                if(_activityRules.getType() == ActivityRules.Type.activity){  //活动任务
                    activityDetail.setAmount(_activityRules.getAmount());
                    //历史记录不是保存在xx_activity_inventory中
                }else{ //成长任务，每日任务
                    activityDetail.setPoint(_activityRules.getPoint());
                }
                activityDetailDao.persist(activityDetail);
            }
        }
    }
}
