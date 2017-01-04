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

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.MemberBankDao;
import net.wit.dao.MemberCapitalDao;
import net.wit.entity.Member;
import net.wit.entity.MemberBank;
import net.wit.entity.MemberCapital;
import net.wit.entity.PlatformCapital;
import net.wit.service.MemberBankService;
import net.wit.service.MemberCapitalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: memberCapitalServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014年7月30日 上午9:06:30
 */
@Service("memberCapitalServiceImpl")
public class MemberCapitalServiceImpl extends BaseServiceImpl<MemberCapital, Long> implements MemberCapitalService {

	@Resource(name = "memberCapitalDaoImpl")
	private MemberCapitalDao memberCapitalDao;

	@Resource(name = "memberCapitalDaoImpl")
	public void setBaseDao(MemberCapitalDao memberCapitalDao) {
		super.setBaseDao(memberCapitalDao);
	}

	public Page<MemberCapital> findPageByDate(Date begin_date, Date end_date,String keyword, Pageable pageable) {
		return memberCapitalDao.findPageByDate(begin_date,end_date,keyword,pageable);
	}

	public List<MemberCapital> findListByDate(Date begin_date, Date end_date, String keyword,List<Filter> filters) {
		return memberCapitalDao.findListByDate(begin_date,end_date,keyword,filters);
	}

}
