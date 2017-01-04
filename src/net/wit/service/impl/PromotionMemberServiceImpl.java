/**
 *====================================================
 * 文件名称: PromotionMemberServiceImpl.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年11月3日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 */
package net.wit.service.impl;

import javax.annotation.Resource;

import net.wit.dao.PromotionMemberDao;
import net.wit.entity.PromotionMember;
import net.wit.service.PromotionMemberService;

import org.springframework.stereotype.Service;

/**
 * @ClassName: PromotionMemberServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014年11月3日 下午3:12:02
 */
@Service("promotionMemberServiceImpl")
public class PromotionMemberServiceImpl extends BaseServiceImpl<PromotionMember, Long> implements PromotionMemberService {

	@Resource(name = "promotionMemberDaoImpl")
	private PromotionMemberDao promotionMemberDao;

	@Resource(name = "promotionMemberDaoImpl")
	public void setBaseDao(PromotionMemberDao promotionMemberDao) {
		super.setBaseDao(promotionMemberDao);
	}

	public PromotionMember findBySn(String sn) {
		return promotionMemberDao.getPromotionMemberBysn(sn);
	}

}
