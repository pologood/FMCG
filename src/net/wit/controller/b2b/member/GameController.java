/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.b2b.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.wit.Message;
import net.wit.Pageable;
import net.wit.controller.b2c.BaseController;
import net.wit.entity.Game;
import net.wit.entity.GamePrice;
import net.wit.entity.Member;
import net.wit.entity.Sn;
import net.wit.service.GamePriceService;
import net.wit.service.GameService;
import net.wit.service.MemberService;
import net.wit.service.SmsSendService;
import net.wit.service.SnService;
import net.wit.webservice.TerminalXmlService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller - 支付
 * 
 * @author rsico Team
 * @version 3.0
 */
@Controller("b2bMemberGameController")
@RequestMapping("/b2b/member/game")
public class GameController extends BaseController {
	/** 每页记录数 */
	private static final int PAGE_SIZE = 10;


	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "gameServiceImpl")
	private GameService gameService;
	@Resource(name = "snServiceImpl")
	private SnService snService;
	@Resource(name = "smsSendServiceImpl")
	private SmsSendService smsSendService;
	@Resource(name = "gamePriceServiceImpl")
	private GamePriceService gamePriceService;
	
	/**
	 * 计算支付手续费
	 */
	@RequestMapping(value = "/calculate_fee", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> calculateFee(Long pid) {
		Map<String, Object> data = new HashMap<String, Object>();
		GamePrice gamePrice = gamePriceService.find(pid);
		data.put("message", SUCCESS_MESSAGE);
		data.put("fee",gamePrice.getFee());
		return data;
	}

	/**
	 * 读价格表
	 */
	@RequestMapping(value = "/get_price", method = RequestMethod.POST)
	public @ResponseBody
	Map<Long, String> getPrice(int cardId) {
		Map<Long, String> data = new HashMap<Long, String>();
		List<GamePrice> prices = gamePriceService.findbyCardId(cardId);
		for (GamePrice price : prices) {
			data.put(price.getId(), price.getPriceName());
		}
		return data;
	}

	/**
	 * 读价格表
	 */
	@RequestMapping(value = "/get_area", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, String> getArea(int cardId,Long pid) {
		TerminalXmlService terminalService = new TerminalXmlService();
		GamePrice gamePrice = gamePriceService.find(pid);
		return terminalService.getDirectAreaInfo(cardId, gamePrice.getPriceId());
	}
	
	/**
	 * 读价格表
	 */
	@RequestMapping(value = "/get_server", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, String> getServer(String area,int cardId,Long pid) {
		TerminalXmlService terminalService = new TerminalXmlService();
		GamePrice gamePrice = gamePriceService.find(pid);
		return terminalService.getDirectSrvInfo(area,cardId, gamePrice.getPriceId());
	}
	

	/**
	 * 提交
	 */
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public String submit(String account,Long pid, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
		Member member = memberService.getCurrent();
		if (member == null) {
			return ERROR_VIEW;
		}
    	if (account == null) {
			return ERROR_VIEW;
		}
    	Game entity = new Game();
    	entity.setSn(snService.generate(Sn.Type.game));
    	entity.setAccount(account);
     	entity.setChargeCount(1);
    	GamePrice price = gamePriceService.find(pid);
       	entity.setPriceId(price.getPriceId());
    	entity.setCardId(price.getCardId());
     	entity.setAmount(price.getDenomination());				
    	entity.setFee(price.getFee());				
    	entity.setCost(price.getCost());
    	entity.setMember(member);
    	
    	gameService.fill(entity);
		model.addAttribute("game", entity);
	    if ("01".equals(entity.getRespCode())) {	
		   model.addAttribute("status",Message.success("您的游戏充值申请已提交成功，系统正在处理请稍候。。。"));
	    } else {
		   model.addAttribute("status",Message.error(entity.getRespMsg()));
	    }
	    if ("99".equals(entity.getRespCode()) ) {
		   return ERROR_VIEW;
	    } else {
	       return "b2b/member/game/notify";
	    }
	    
	}

	/**
	 * 检查余额
	 */
	@RequestMapping(value = "/check_balance", method = RequestMethod.POST)
	public @ResponseBody
	Map<String, Object> checkBalance() {
		Map<String, Object> data = new HashMap<String, Object>();
		Member member = memberService.getCurrent();
		data.put("balance", member.getBalance() );
		return data;
	}

	/**
	 * 充值
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(ModelMap model,HttpServletRequest request) {
		Map<String, Object> data = new HashMap<String, Object>();
		
		List<GamePrice> prices = gamePriceService.findbyTypeId(0);
		for (GamePrice price : prices) {
		  data.put(String.valueOf(price.getCardId()) ,price);	
		}
		Member member = memberService.getCurrent();
		model.addAttribute("member",member);
		model.addAttribute("default",81);
		model.addAttribute("cards", data.values());
		return "b2b/member/game/index";
	}
	
	/**
	 * 充值Q币Q点
	 */
	@RequestMapping(value = "/qIndex", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> qIndex(HttpServletRequest request) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<GamePrice> prices = gamePriceService.findbyTypeId(0);
		for (GamePrice price : prices) {
			data.put(String.valueOf(price.getCardId()) ,price.getCardName());	
		}
		
		return data;
	}
	
	
	

	/**
	 * 充值
	 */
	@RequestMapping(value = "/game", method = RequestMethod.GET)
	public String game(ModelMap model,HttpServletRequest request) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<GamePrice> prices = gamePriceService.findbyTypeId(1);
		for (GamePrice price : prices) {
		  data.put(String.valueOf(price.getCardId()) ,price);	
		}
		Member member = memberService.getCurrent();
		model.addAttribute("member",member);
		model.addAttribute("default",81);
		model.addAttribute("cards", data.values());
		return "b2b/member/game/game";
	}
	
	/**
	 * 通知
	 */
	@RequestMapping(value = "/notify", method = RequestMethod.GET)
	public @ResponseBody 
	Map<String, Object> notify(Long id,ModelMap model) {
		Map<String, Object> data = new HashMap<String, Object>();
		Game game = gameService.find(id);
		gameService.notify(game);
		if ("0".equals(game.getRetCode()) ) {
			data.put("code", "1");
			data.put("msg", "恭喜！您的游戏充值申请已经成功充值，");
		} else {
			data.put("code", game.getRetCode());
			data.put("msg","抱歉！您的游戏充值申请充值失败，原因："+game.getRetMsg() );
		}
		return data;
	}

	/**
	 * 列表
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Integer pageNumber,ModelMap model) {
		Member member = memberService.getCurrent();
		Pageable pageable = new Pageable(pageNumber, PAGE_SIZE);
		model.addAttribute("member", member);
		model.addAttribute("page", gameService.findPage(member, pageable));
		return "b2b/member/game/list";
	}
	
}