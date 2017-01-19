package net.wit.support;

import net.wit.entity.Employee;
import net.wit.entity.Location;

import java.util.Comparator;

/**
 * 员工按商家距离排序
 */
public class EmployeeComparatorByDistance implements Comparator<Employee> {
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public int compare(Employee o1, Employee o2) {
        int comp = 0;
        if (o1.getTenant() == null && o2.getTenant() != null) {
            comp = 0;
        }
        if (o1.getTenant() != null && o2.getTenant() == null) {
            comp = 1;
        }
        if (o1.getTenant() == null && o2.getTenant() != null) {
            comp = -1;
        }
        if (o1.getTenant() != null && o2.getTenant() != null) {
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
