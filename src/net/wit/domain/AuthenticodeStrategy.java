/**
 *====================================================
 * 文件名称: AuthenticodeStrategy.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年8月19日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.domain;

import net.wit.entity.Order;
import net.wit.entity.Trade;

/**
 * @ClassName: AuthenticodeStrategy
 * @Description: 验证码发送方式
 * @author Administrator
 * @date 2014年8月19日 上午10:03:53
 */
public interface AuthenticodeStrategy {

	/** 生成订单相关商品验证码 */
	public void createAuthentiCode(Order order);

	/** 发送验证码 */
	public void sendNotify(Order order);
	/** 发送验证码 */
	public void sendNotify(Trade trade);

}
