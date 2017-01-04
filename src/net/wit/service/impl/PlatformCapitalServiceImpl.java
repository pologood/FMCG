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

import cn.jpush.api.push.model.Platform;
import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.MemberBankDao;
import net.wit.dao.PlatformCapitalDao;
import net.wit.entity.Member;
import net.wit.entity.MemberBank;
import net.wit.entity.PlatformCapital;
import net.wit.service.MemberBankService;
import net.wit.service.PlatformCapitalService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @ClassName: PlatformCapitalServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014年7月30日 上午9:06:30
 */
@Service("platformCapitalServiceImpl")
public class PlatformCapitalServiceImpl extends BaseServiceImpl<PlatformCapital, Long> implements PlatformCapitalService {

	@Resource(name = "platformCapitalDaoImpl")
	private PlatformCapitalDao platformCapitalDao;

	@Resource(name = "platformCapitalDaoImpl")
	public void setBaseDao(PlatformCapitalDao platformCapitalDao) {
		super.setBaseDao(platformCapitalDao);
	}

	public Page<PlatformCapital> findPageByDate(Date begin_date, Date end_date, Pageable pageable) {
		return platformCapitalDao.findPageByDate(begin_date,end_date,pageable);
	}

	public List<PlatformCapital> findListByDate(Date begin_date, Date end_date, List<Filter> filters) {
		return platformCapitalDao.findListByDate(begin_date,end_date,filters);
	}

}
