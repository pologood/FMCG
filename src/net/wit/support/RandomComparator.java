/**
 *====================================================
 * 文件名称: TenantComparatorByDistance.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2015年1月13日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.support;

import java.util.Comparator;

import net.wit.entity.CouponCode;

/**
 * @ClassName: TenantComparatorByDistance
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2015年1月13日 下午2:37:30
 */
public class RandomComparator implements Comparator<CouponCode> {

	public RandomComparator() {
	}

	@Override
	public int compare(CouponCode o1, CouponCode o2) {
		int comp = 0;
		double d1 = Math.random();
		double d2 = Math.random();
		if (d1>d2) {
			comp = 1;
		} else 
			if (d1<d2) {
				comp = -1;
			} else {
				comp = 0;
			}
		return comp;
	}
}
