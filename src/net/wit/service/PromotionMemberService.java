/**
 *====================================================
 * 文件名称: PromotionMemberService.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年11月3日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 */
package net.wit.service;

import net.wit.entity.PromotionMember;

/**
 * @ClassName: PromotionMemberService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014年11月3日 下午3:11:27
 */
public interface PromotionMemberService extends BaseService<PromotionMember, Long> {

	public PromotionMember findBySn(String sn);

}
