package net.wit.service.impl;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.Setting;
import net.wit.dao.CouponNumberDao;
import net.wit.dao.DepositDao;
import net.wit.dao.MemberDao;
import net.wit.dao.RebateDao;
import net.wit.entity.*;
import net.wit.entity.Rebate.OrderType;
import net.wit.service.CouponNumberService;
import net.wit.service.MessageService;
import net.wit.support.EntitySupport;

import net.wit.util.SettingUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.LockModeType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 16/11/12.
 */
@Service("couponNumberServiceImpl")
public class CouponNumberServiceImpl extends BaseServiceImpl<CouponNumber,Long> implements CouponNumberService{

    @Resource(name = "couponNumberDaoImpl")
    private CouponNumberDao couponNumberDao;

    @Resource(name = "rebateDaoImpl")
    private RebateDao rebateDao;

    @Resource(name = "memberDaoImpl")
    private MemberDao memberDao;

    @Resource(name = "depositDaoImpl")
    private DepositDao depositDao;
    
    @Resource(name = "messageServiceImpl")
    private MessageService messageService;

    @Resource(name = "couponNumberDaoImpl")
    public void setBaseDao(CouponNumberDao couponNumberDao){
        super.setBaseDao(couponNumberDao);
    }

    public  Long getLastCode(Coupon coupon){
        return  couponNumberDao.getLastCode(coupon);
    }

    public List<CouponNumber> findList(Coupon coupon, Member member, Member guideMember,Long code){
        return  couponNumberDao.findList(coupon,member,guideMember,code);
    }

    public Page<CouponNumber> openPage(Coupon coupon, Member member, Member guideMember, Long code, Pageable pageable){
        return  couponNumberDao.openPage(coupon,member,guideMember,code,pageable);
    }
	public BigDecimal resolveBrokerage(BigDecimal amount,BigDecimal brokerage,BigDecimal ownerBrokerage,Member member,CouponCode couponCode,PayBill payBill) {
		Setting setting = SettingUtils.get();
		if (amount.compareTo(BigDecimal.ZERO)==0) {
			return BigDecimal.ZERO;
		}
		
		List<CouponNumber> g = couponNumberDao.resolveOpen(amount, brokerage, member, couponCode);
		
		BigDecimal amt = amount;
		BigDecimal rev = BigDecimal.ZERO;
		BigDecimal bkg = BigDecimal.ZERO;
		BigDecimal bkgo = BigDecimal.ZERO;
		for (int i=0;i<g.size();i++) {
			
			if (amt.compareTo(BigDecimal.ZERO)<=0) {
				break;
			}
			
			CouponNumber n = g.get(i);
			BigDecimal b = BigDecimal.ZERO;
			if (amt.compareTo(n.getBalance())>0) {
			    b = n.getBalance();
			} else {
				b = amt;
			}
			BigDecimal bk = BigDecimal.ZERO;
			BigDecimal bko = BigDecimal.ZERO;
			if (n.getGuideMember()!=null&&brokerage.compareTo(BigDecimal.ZERO)>0) {
			   bk =  brokerage.multiply(b).divide(amount).setScale(2, BigDecimal.ROUND_DOWN);
			}
			if (n.getGuideMember()!=null&&ownerBrokerage.compareTo(BigDecimal.ZERO)>0&&n.getGuideMember().getTenant()!=null&&(n.getGuideMember().getTenant().getStatus().equals(Tenant.Status.confirm)||n.getGuideMember().getTenant().getStatus().equals(Tenant.Status.success)) ) {
				bko =  ownerBrokerage.multiply(b).divide(amount).setScale(2, BigDecimal.ROUND_DOWN);
			}
			amt = amt.subtract(b);
			rev = rev.add(b);
			bkg = bkg.add(bk);
			bkgo = bkgo.add(bko);
			
			if (bk.compareTo(BigDecimal.ZERO)>0&&n.getGuideMember()!=null) {
				Member guideMember = n.getGuideMember();
                memberDao.lock(guideMember, LockModeType.PESSIMISTIC_WRITE);
                guideMember.setBalance(guideMember.getBalance().add(bk));
                memberDao.merge(guideMember);
                Deposit buyDeposit = new Deposit();
                buyDeposit.setType(Deposit.Type.profit);
                buyDeposit.setStatus(Deposit.Status.complete);
                buyDeposit.setCredit(bk);
                buyDeposit.setDebit(BigDecimal.ZERO);
                buyDeposit.setBalance(guideMember.getBalance());
                buyDeposit.setOperator("system");
                buyDeposit.setMember(guideMember);
                buyDeposit.setMemo("推广现金券导购返利");
                buyDeposit.setOrder(null);
                buyDeposit.setStatus(Deposit.Status.none);
                depositDao.persist(buyDeposit);
        		Rebate rebate = new Rebate();
        		rebate.setType(Rebate.Type.extension);
        		rebate.setMember(guideMember);
        		rebate.setAmount(amount);
        		rebate.setBrokerage(bk);
        		rebate.setPercent(setting.getGuidePercent());
        		rebate.setStatus(Rebate.Status.success);
        		rebate.setOrderType(OrderType.payBill);
        		rebate.setDescription("推广现金券导购返利");
        		rebate.setPayBill(payBill);
        		rebateDao.persist(rebate);
                try {
                    Message buyMessage = EntitySupport.createInitMessage(Message.Type.account,
                            "您的会员" + member.getDisplayName() + "，核销现金券给你带来" +  bk + "元 返利。",
                            null, guideMember, null);
                    buyMessage.setDeposit(buyDeposit);
                    messageService.save(buyMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
				
			}

			if (bko.compareTo(BigDecimal.ZERO)>0&&n.getGuideMember()!=null) {
				Member guideOwnerMember = n.getGuideMember().getTenant().getMember();
				memberDao.lock(guideOwnerMember, LockModeType.PESSIMISTIC_WRITE);
				guideOwnerMember.setBalance(guideOwnerMember.getBalance().add(bko));
				memberDao.merge(guideOwnerMember);
				Deposit buyDeposit = new Deposit();
				buyDeposit.setType(Deposit.Type.profit);
				buyDeposit.setStatus(Deposit.Status.complete);
				buyDeposit.setCredit(bko);
				buyDeposit.setDebit(BigDecimal.ZERO);
				buyDeposit.setBalance(guideOwnerMember.getBalance());
				buyDeposit.setOperator("system");
				buyDeposit.setMember(guideOwnerMember);
				buyDeposit.setMemo("推广现金券店主返利");
				buyDeposit.setOrder(null);
				buyDeposit.setStatus(Deposit.Status.none);
				depositDao.persist(buyDeposit);
				Rebate rebate = new Rebate();
				rebate.setType(Rebate.Type.extension);
				rebate.setMember(guideOwnerMember);
				rebate.setAmount(amount);
				rebate.setBrokerage(bko);
				rebate.setPercent(setting.getGuideOwnerPercent());
				rebate.setStatus(Rebate.Status.success);
				rebate.setOrderType(OrderType.payBill);
				rebate.setDescription("推广现金券店主返利");
				rebate.setPayBill(payBill);
				rebateDao.persist(rebate);
				try {
					Message buyMessage = EntitySupport.createInitMessage(Message.Type.account,
							"您的会员" + member.getDisplayName() + "，核销现金券给你带来" +  bko + "元 返利。",
							null, guideOwnerMember, null);
					buyMessage.setDeposit(buyDeposit);
					messageService.save(buyMessage);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			
			n.setBalance(n.getBalance().subtract(b));
			if (n.getBalance().compareTo(BigDecimal.ZERO)<=0) {
				n.setStatus(CouponNumber.Status.used);;
			}
			if(n.getUseTimes()!=null){
				n.setUseTimes(n.getUseTimes()+1L);
			}else {
				n.setUseTimes(1L);
			}

			if(n.getBrokerage()!=null){
				n.setBrokerage(n.getBrokerage().add(bk));
			}else {
				n.setBrokerage(bk);
			}
			couponNumberDao.persist(n);
		}
		payBill.setGuideOwnerBrokerage(bkgo);
		return bkg;
	}
	public BigDecimal resolveBrokerage(BigDecimal amount,BigDecimal brokerage,BigDecimal ownerBrokerage,Member member,CouponCode couponCode,Trade trade) {
		Setting setting = SettingUtils.get();
		if (amount.compareTo(BigDecimal.ZERO)==0) {
			return BigDecimal.ZERO;
		}
		
		List<CouponNumber> g = couponNumberDao.resolveOpen(amount, brokerage, member, couponCode);
		
		BigDecimal amt = amount;
		BigDecimal rev = BigDecimal.ZERO;
		BigDecimal bkg = BigDecimal.ZERO;
		BigDecimal bkgo = BigDecimal.ZERO;
		for (int i=0;i<g.size();i++) {
			
			if (amt.compareTo(BigDecimal.ZERO)<=0) {
				break;
			}
			
			CouponNumber n = g.get(i);
			BigDecimal b = BigDecimal.ZERO;
			if (amt.compareTo(n.getBalance())>0) {
			    b = n.getBalance();
			} else {
				b = amt;
			}
			BigDecimal bk = BigDecimal.ZERO;
			BigDecimal bko = BigDecimal.ZERO;
			if (n.getGuideMember()!=null) {
			   bk =  brokerage.multiply(b).divide(amount).setScale(2, BigDecimal.ROUND_DOWN);
			}
			if (n.getGuideMember()!=null&&ownerBrokerage.compareTo(BigDecimal.ZERO)>0&&n.getGuideMember().getTenant()!=null&&(n.getGuideMember().getTenant().getStatus().equals(Tenant.Status.confirm)||n.getGuideMember().getTenant().getStatus().equals(Tenant.Status.success)) ) {
				bko =  ownerBrokerage.multiply(b).divide(amount).setScale(2, BigDecimal.ROUND_DOWN);
			}
			amt = amt.subtract(b);
			rev = rev.add(b);
			bkg = bkg.add(bk);
			bkgo = bkgo.add(bko);

			if (bk.compareTo(BigDecimal.ZERO)>0&&n.getGuideMember()!=null) {
				Member guideMember = n.getGuideMember();
                memberDao.lock(guideMember, LockModeType.PESSIMISTIC_WRITE);
                guideMember.setBalance(guideMember.getBalance().add(bk));
                memberDao.merge(guideMember);
                Deposit buyDeposit = new Deposit();
                buyDeposit.setType(Deposit.Type.profit);
                buyDeposit.setStatus(Deposit.Status.complete);
                buyDeposit.setCredit(bk);
                buyDeposit.setDebit(BigDecimal.ZERO);
                buyDeposit.setBalance(guideMember.getBalance());
                buyDeposit.setOperator("system");
                buyDeposit.setMember(guideMember);
                buyDeposit.setMemo("推广现金券导购返利");
                buyDeposit.setOrder(null);
                buyDeposit.setStatus(Deposit.Status.none);
                depositDao.persist(buyDeposit);
        		Rebate rebate = new Rebate();
        		rebate.setType(Rebate.Type.extension);
        		rebate.setMember(guideMember);
        		rebate.setAmount(amount);
        		rebate.setBrokerage(bk);
        		rebate.setPercent(setting.getGuidePercent());
        		rebate.setStatus(Rebate.Status.success);
        		rebate.setOrderType(OrderType.order);
        		rebate.setDescription("推广现金券导购返利");
        		rebate.setTrade(trade);
        		rebateDao.persist(rebate);
                try {
                    Message buyMessage = EntitySupport.createInitMessage(Message.Type.account,
                            "您的会员" + member.getDisplayName() + "，核销现金券给你带来" +  bk + "元 返利。",
                            null, guideMember, null);
                    buyMessage.setDeposit(buyDeposit);
                    messageService.save(buyMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
				
			}

			if (bko.compareTo(BigDecimal.ZERO)>0&&n.getGuideMember()!=null) {
				Member guideOwnerMember = n.getGuideMember().getTenant().getMember();
				memberDao.lock(guideOwnerMember, LockModeType.PESSIMISTIC_WRITE);
				guideOwnerMember.setBalance(guideOwnerMember.getBalance().add(bko));
				memberDao.merge(guideOwnerMember);
				Deposit buyDeposit = new Deposit();
				buyDeposit.setType(Deposit.Type.profit);
				buyDeposit.setStatus(Deposit.Status.complete);
				buyDeposit.setCredit(bko);
				buyDeposit.setDebit(BigDecimal.ZERO);
				buyDeposit.setBalance(guideOwnerMember.getBalance());
				buyDeposit.setOperator("system");
				buyDeposit.setMember(guideOwnerMember);
				buyDeposit.setMemo("推广现金券店主返利");
				buyDeposit.setOrder(null);
				buyDeposit.setStatus(Deposit.Status.none);
				depositDao.persist(buyDeposit);
				Rebate rebate = new Rebate();
				rebate.setType(Rebate.Type.extension);
				rebate.setMember(guideOwnerMember);
				rebate.setAmount(amount);
				rebate.setBrokerage(bko);
				rebate.setPercent(setting.getGuideOwnerPercent());
				rebate.setStatus(Rebate.Status.success);
				rebate.setOrderType(OrderType.order);
				rebate.setDescription("推广现金券店主返利");
				rebate.setTrade(trade);
				rebateDao.persist(rebate);
				try {
					Message buyMessage = EntitySupport.createInitMessage(Message.Type.account,
							"您的会员" + member.getDisplayName() + "，核销现金券给你带来" +  bko + "元 返利。",
							null, guideOwnerMember, null);
					buyMessage.setDeposit(buyDeposit);
					messageService.save(buyMessage);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			n.setBalance(n.getBalance().subtract(b));
			if (n.getBalance().compareTo(BigDecimal.ZERO)<=0) {
				n.setStatus(CouponNumber.Status.used);;
			}
			if(n.getUseTimes()!=null){
				n.setUseTimes(n.getUseTimes()+1L);
			}else {
				n.setUseTimes(1L);
			}

			if(n.getBrokerage()!=null){
				n.setBrokerage(n.getBrokerage().add(bk));
			}else {
				n.setBrokerage(bk);
			}

			couponNumberDao.persist(n);
		}
		trade.setProviderAmount(bkgo);
		return bkg;
	}

	public Page<Map<String, Object>> findCouponNumberPage(Coupon coupon,String keyword,Pageable pageable){
		return couponNumberDao.findCouponNumberPage(coupon,keyword,pageable);
	}

	public List<Map<String, Object>> findCouponNumberList(Coupon coupon, String keyword){
		return couponNumberDao.findCouponNumberList(coupon,keyword);
	}
	
}
