/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.assistant;

import net.wit.controller.assistant.BaseController;
import net.wit.controller.assistant.model.DataBlock;
import net.wit.controller.assistant.model.MemberRankModel;
import net.wit.entity.MemberRank;
import net.wit.service.MemberRankService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Controller - 会员等级
 * @author rsico Team
 * @version 3.0
 */
@Controller("assistantMemberRankController")
@RequestMapping("/assistant/member_rank")
public class MemberRankController extends BaseController {

	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;

	/**
	 * 获取平台会员等级
	 */
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody DataBlock member_Rank() {
	    List<MemberRank> ranks =  memberRankService.findAll();
	    for (MemberRank rank:ranks) {
	    	if (rank.getIsDefault()) {
	    		ranks.remove(rank);
	    		break;
	    	}
	    }
		return DataBlock.success(MemberRankModel.bindData(ranks),"执行成功");
	}
}