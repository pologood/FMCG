package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Admin;
import net.wit.entity.ReportFile;

public interface ReportFileDao extends BaseDao<ReportFile, Long>{

	Page<ReportFile> findPage(Admin admin, Pageable pageable);

}