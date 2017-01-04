/**
 *====================================================
 * 文件名称: MemberBankServiceImpl.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年7月30日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.service.impl;

import java.util.List;

import javax.annotation.Resource;

import net.wit.dao.MemberBankDao;
import net.wit.entity.Member;
import net.wit.entity.MemberBank;
import net.wit.service.MemberBankService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName: MemberBankServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014年7月30日 上午9:06:30
 */
@Service("memberBankServiceImpl")
public class MemberBankServiceImpl extends BaseServiceImpl<MemberBank, Long> implements MemberBankService {

	@Resource(name = "memberBankDaoImpl")
	private MemberBankDao memberBankDao;

	@Resource(name = "memberBankDaoImpl")
	public void setBaseDao(MemberBankDao memberBankDao) {
		super.setBaseDao(memberBankDao);
	}

	public List<MemberBank> findListByMember(Member member) {
		return memberBankDao.findListByMember(member);
	}

	@Override
	@Transactional
	public MemberBank update(MemberBank memberBank) {
		return memberBankDao.merge(memberBank);
	}
}
