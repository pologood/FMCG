/**
 * ====================================================
 * 文件名称: TenantComparatorByDistance.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2015年1月13日			Administrator(创建:创建文件)
 * ====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 */
package net.wit.support;

import net.wit.entity.Coupon;
import net.wit.entity.Location;

import java.util.Comparator;

/**
 * 红包按商家距离排序
 */
public class CouponComparatorByDistance implements Comparator<Coupon> {
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public int compare(Coupon o1, Coupon o2) {
        int comp = 0;
        if(o1.getTenant()==null&&o2.getTenant()!=null){
            comp=0;
        }
        if(o1.getTenant()!=null&&o2.getTenant()==null){
            comp=1;
        }
        if(o1.getTenant()==null&&o2.getTenant()!=null){
            comp=-1;
        }
        if(o1.getTenant()!=null&&o2.getTenant()!=null){
            double d1 = o1.getTenant().distatce(this.location);
            double d2 = o2.getTenant().distatce(this.location);
            if (d1 >= 0 && d2 >= 0) {
                if (d1 > d2) {
                    comp = 1;
                } else if (d1 < d2) {
                    comp = -1;
                } else {
                    comp = 0;
                }
            }
            if (d1 >= 0 && d2 == -1) {
                comp = -1;
            }
            if (d1 == -1 && d2 >= 0) {
                comp = 1;
            }
        }
        if (comp == 0) {
            comp = o1.getId().compareTo(o2.getId());
        }
        return comp;
    }
}
