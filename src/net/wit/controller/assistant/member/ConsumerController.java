/**
 *====================================================
 * 文件名称: ConsumberController.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014-9-11			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.controller.assistant.member;

import net.wit.Filter;
import net.wit.Filter.Operator;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.controller.assistant.model.MemberListModel;
import net.wit.controller.assistant.model.MemberModel;
import net.wit.controller.assistant.model.TradeListModel;
import net.wit.entity.*;
import net.wit.entity.Consumer.Status;
import net.wit.service.*;
import net.wit.weixin.main.MenuManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * @ClassName: ConsumberController
 * @Description: TODO(会员管理)
 * @author Administrator
 * @date 2014-9-11 上午11:31:34
 */
@Controller("assistantMemberConsumerController")
@RequestMapping("/assistant/member/consumer")
public class ConsumerController extends BaseController {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "consumerServiceImpl")
	private ConsumerService consumerService;
	@Resource(name = "tradeServiceImpl")
	private TradeService tradeService;
	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;
	@Resource(name = "commissionServiceImpl")
	private CommissionService commissionService;
	
	/**
	 * 查看
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock view(Long id) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		
		Tenant tenant = member.getTenant();
		if (tenant==null) {
			DataBlock.error(DataBlock.TENANT_INVAILD);
		}

	    member = memberService.find(id);
		if (member == null) {
			return DataBlock.error("无效会员id");
		}
		
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("tenant", Operator.eq, tenant));
		filters.add(new Filter("member", Operator.eq, member));
	    List<Consumer> consumers = consumerService.findList(10, filters, null);
	    
		Map<String, Object> data = new HashMap<String, Object>();
		MemberModel model = new MemberModel();
	    if (consumers.size()==0) {
			model.copyFrom(member);
	    } else {
		    Consumer consumer = consumers.get(0);
			model.copyConsumer(consumer);
	    }
		data.put("member", model); 
		data.put("orders",member.getOrders().size());
		data.put("amount",member.getAmount());
		return DataBlock.success(data,"执行成功");
	}
	
	/**
	 * 修改等级
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public @ResponseBody DataBlock update(Long id,Long memberRankId,Pageable pageable) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		
		Tenant tenant = member.getTenant();
		if (tenant==null) {
			DataBlock.error(DataBlock.TENANT_INVAILD);
		}
		
		Member current = memberService.find(id);
		if (current==null) {
			return DataBlock.error("会员的ID无效");
		}
		MemberRank memberRank = memberRankService.find(memberRankId);
		if (memberRank==null) {
			return DataBlock.error("会员等级ID无效");
		}
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter("tenant", Operator.eq, tenant));
		filters.add(new Filter("member", Operator.eq, current));
	    List<Consumer> consumers = consumerService.findList(10, filters, null);
	    
	    Consumer consumer = null;
	    
	    if (consumers.size()==0) {
		        consumer = new Consumer();
			    consumer.setMember(current);
			    consumer.setStatus(Status.enable);
			    consumer.setTenant(tenant);
			    consumer.setMemberRank(memberRank);
	    } else {
		    consumer = consumers.get(0);
	    }
		consumer.setMemberRank(memberRank);
		consumer.setStatus(Status.enable);
		consumerService.save(consumer);
		return DataBlock.success("success","设置会员成功");
	}
	/**
	 * 我的会员
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody DataBlock list(Location location,Pageable pageable) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		
		Tenant tenant = member.getTenant();
		if (tenant==null) {
			DataBlock.error(DataBlock.TENANT_INVAILD);
		}
		
		Page<Consumer> page = consumerService.findPage(tenant, Status.enable, pageable);
		return DataBlock.success(MemberListModel.bindConsumer(page.getContent(), location),"执行成功");
	}
	
	/**
	 * 附近的人
	 */
	@RequestMapping(value = "/nearby", method = RequestMethod.GET)
	public @ResponseBody DataBlock nearby(Location location,Pageable pageable) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Tenant tenant = member.getTenant();
		if (tenant==null) {
			DataBlock.error(DataBlock.TENANT_INVAILD);
		}
        Page<Member> page = memberService.findNearBy(location, pageable);
		return DataBlock.success(MemberListModel.bindData(page.getContent(), location),"执行成功");
	}
	
	/**
	 * 关注我的
	 */
	@RequestMapping(value = "/favorite", method = RequestMethod.GET)
	public @ResponseBody DataBlock favorite(Location location,Pageable pageable) {
		Member member = memberService.getCurrent();
		if (member==null) {
			DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		Tenant tenant = member.getTenant();
		if (tenant==null) {
			DataBlock.error(DataBlock.TENANT_INVAILD);
		}

		Page<Consumer> page = consumerService.findPage(tenant, Status.none, pageable);
		return DataBlock.success(MemberListModel.bindConsumer(page.getContent(), location),"执行成功");
	}
	
	/**
	 * 列表
	 */
	@RequestMapping(value = "/order", method = RequestMethod.GET)
	@ResponseBody
	public DataBlock order(Long id,Pageable pageable) {
		Member member = memberService.find(id);
		Page<Trade> page = tradeService.findPage(member, pageable);
		return DataBlock.success(TradeListModel.bindData(page.getContent()),"执行成功");
	}
	
	/**
	 * 消费者二维码
	 */
	@RequestMapping(value = "/qrcode/json",method = RequestMethod.GET)
	@ResponseBody
	public DataBlock qrcodeJson(Long id,HttpServletRequest request, HttpServletResponse response) {
		Member member = memberService.find(id);
		try {
			ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
			// 第三方用户唯一凭证
			String url = bundle.getString("WeiXinSiteUrl") + "/weixin/member/becomevip.jhtml?extension=" + (member != null ? member.getUsername() : "");
            return DataBlock.success(url,"获取成功");
			
		} catch (Exception e) {
			return DataBlock.error("获取二维码串失败");
		}
	}
	
	/**
	 * 分享地址
	 */
	@RequestMapping(value = "/share", method = RequestMethod.GET)
	public @ResponseBody DataBlock share(Long id,String type) {
		//Member consumer = memberService.find(id);
		//if (consumer == null) {
		//	return DataBlock.error("此会员不存在");
		//}
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		
		
		Tenant tenant = member.getTenant();
		//if (tenant==null) {
		//	return DataBlock.error(DataBlock.TENANT_INVAILD);
		//}
		
		ResourceBundle bundle = PropertyResourceBundle.getBundle("config");
		String url = "";
	    if (!"app".equals(type)) {
	    	url = MenuManager.codeUrlO2(URLEncoder.encode(bundle.getString("WeiXinSiteUrl") + "/weixin/member/becomevip.jhtml?extension=" + (member != null ? member.getUsername() : "")));
	    } else {
	    	url = bundle.getString("WeiXinSiteUrl") + "/weixin/member/becomevip.jhtml?extension=" + (member != null ? member.getUsername() : "");
	    }
	    
	    Map<String,String> data = new HashMap<String,String>();
	    data.put("url",url);
	    data.put("title","亲,“"+member.getDisplayName()+"”为您推荐，快去看看吧。");
	    data.put("thumbnail",member.getHeadImg());
	    if (tenant!=null) {
	        data.put("description",member.getTenant().getScopeOfBusiness());
	    } else {
	        data.put("description","聚焦同城好店，这座城市，你想要的，我来实现。");
	    }
		return DataBlock.success(data,"执行成功");
	}

	/**
	 * 统计
	 */
	
	@RequestMapping(value = "/count",method = RequestMethod.GET)
	@ResponseBody
	public DataBlock count(HttpServletRequest request, HttpServletResponse response) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return DataBlock.error(DataBlock.SESSION_INVAILD);
		}
		try {
			Map<String,Object> data = new HashMap<String,Object>();
			data.put("total_member",memberService.count(new Filter("member", Operator.eq, member)));
			data.put("total_tenant",commissionService.count(new Filter("member", Operator.eq, member)));
			data.put("profit_member",BigDecimal.ZERO);
			data.put("commission_tenant",BigDecimal.ZERO);
            return DataBlock.success(data,"获取成功");
			
		} catch (Exception e) {
			return DataBlock.error("统计出错了");
		}
	}
	
	
}
