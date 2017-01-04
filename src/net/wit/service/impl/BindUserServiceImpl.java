package net.wit.service.impl;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import net.wit.dao.AreaDao;
import net.wit.dao.BindUserDao;
import net.wit.dao.MemberDao;
import net.wit.dao.TenantDao;
import net.wit.entity.Area;
import net.wit.entity.BindUser;
import net.wit.entity.BindUser.Type;
import net.wit.entity.Member;
import net.wit.entity.MemberRank;
import net.wit.entity.Tenant;
import net.wit.service.BindUserService;
import net.wit.support.EntitySupport;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

/**
 * Service - 绑定登录
 * 
 * @author mayt
 * @version 3.0
 */
@Service("bindUserServiceImpl")
public class BindUserServiceImpl extends BaseServiceImpl<BindUser, String> implements BindUserService {

	
	@Resource(name = "tenantDaoImpl")
	private TenantDao tenantDao;
	@Resource(name = "bindUserDaoImpl")
	private BindUserDao bindUserDao;
	@Resource(name = "memberDaoImpl")
	private MemberDao memberDao;
	@Resource(name = "areaDaoImpl")
	private AreaDao areaDao;
	
	@Resource(name = "bindUserDaoImpl")
	public void setBaseDao(BindUserDao bindUserDao) {
		super.setBaseDao(bindUserDao);
	}

	public BindUser findByUsername(String username,Type type) {
		return bindUserDao.findByUsername(username,type);
	}

	public BindUser findByMember(Member member,Type type) {
		return bindUserDao.findByMember(member,type);
	}


	public BindUser findBnindUser(String username,Member member,Type type) {
		return bindUserDao.findBnindUser(username,member,type);
	}
}
