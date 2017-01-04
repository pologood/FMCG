package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Admin;
import net.wit.entity.Commission;

public interface CommissionService extends BaseService<Commission, Long> {
	Page<Commission> findPage(Admin admin, Pageable pageable);
}
