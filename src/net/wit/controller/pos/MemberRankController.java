/*
 * Copyright 2005-2013 rsico. All rights reserved.
 * Support: http://www.rsico.cn
 * License: http://www.rsico.cn/license
 */
package net.wit.controller.pos;

import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.wit.controller.app.model.DataBlock;
import net.wit.controller.app.model.MemberRankModel;
import net.wit.entity.MemberRank;
import net.wit.entity.Tenant;
import net.wit.service.MemberRankService;
import net.wit.service.TenantService;

/**
 * Controller - 会员等级
 * @author rsico Team
 * @version 3.0
 */
@Controller("posMemberRankController")
@RequestMapping("/pos/member_rank")
public class MemberRankController extends BaseController {

	@Resource(name = "memberRankServiceImpl")
	private MemberRankService memberRankService;
	@Resource(name = "tenantServiceImpl")
	private TenantService tenantService;

	/**
	 * 获取平台会员等级
	 */
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody DataBlock member_Rank(Long tenantId,String key) {
	       Tenant tenant = tenantService.find(tenantId);
	        if (tenant==null) {
	        	return DataBlock.error(DataBlock.TENANT_INVAILD);
	        } 
			ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
			String myKey = DigestUtils.md5Hex(tenantId.toString()+bundle.getString("appKey"));
			if (!myKey.equals(key)) {
				return DataBlock.error("通讯密码无效");
			}
	    List<MemberRank> ranks =  memberRankService.findAll();
	    for (MemberRank rank:ranks) {
	    	if (rank.getIsDefault()) {
	    		ranks.remove(rank);
	    		break;
	    	}
	    }
		return DataBlock.success(MemberRankModel.bindData(ranks),"success");
	}
}