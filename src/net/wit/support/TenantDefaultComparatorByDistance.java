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

import net.wit.entity.Location;
import net.wit.entity.Tenant;

/**
 * @ClassName: TenantComparatorByDistance
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2015年1月13日 下午2:37:30
 */
public class TenantDefaultComparatorByDistance implements Comparator<Tenant> {
	private Location location;

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public int compare(Tenant o1, Tenant o2) {
		int comp = 0;
		double d1 = o1.distatce(this.location);
		double d2 = o2.distatce(this.location);
		if (d1 >=0 && d2 == -1) {
			comp = -1;
		} else
		if (d1 == -1 && d2 >= 0) {
			comp = 1;
		} else
		if (d1>=0 && d2>=0) {
			long dd1 = Math.round(d1 / 500);
			long dd2 = Math.round(d2 / 500);
			if (dd1>dd2) {
				comp=1;
			} else if (dd1<dd2) {
				comp=-1;
			} else {
				comp=0;
			}
		};
		if (comp == 0) {
			comp = o1.getScore().compareTo(o2.getScore());
		}
		if (comp == 0) {
			comp = o1.getMonthSales().compareTo(o2.getMonthSales());
		}		
		if (comp == 0) {
			comp = o1.getTotalScore().compareTo(o2.getTotalScore());
		}		
		if (comp == 0) {
			comp = o1.getHits().compareTo(o2.getHits());
		}
		if (comp == 0) {
			comp = o1.getId().compareTo(o2.getId());
		}
		return comp;
	}
}
