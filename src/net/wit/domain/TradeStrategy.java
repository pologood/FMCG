/**
 *====================================================
 * 文件名称: TradeStrategy.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年5月14日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.domain;

import net.wit.entity.Area;
import net.wit.entity.Member;
import net.wit.entity.Order;
import net.wit.entity.Product;
import net.wit.entity.Tenant;

/**
 * @ClassName: TradeStrategy
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014年5月14日 下午3:54:01
 */
public interface TradeStrategy {

	public Tenant distribution(Member member, Area area, Product product, Integer quantity, Tenant defaultTenant);

	public void smsOrder(Order order);
}
