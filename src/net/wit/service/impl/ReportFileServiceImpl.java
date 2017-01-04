package net.wit.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ReportFileDao;
import net.wit.entity.Admin;
import net.wit.entity.ReportFile;
import net.wit.service.ReportFileService;

@Service("reportFileServiceImpl")
public class ReportFileServiceImpl extends BaseServiceImpl<ReportFile, Long> implements ReportFileService{

	@Resource(name = "reportFileDaoImpl")
	private ReportFileDao reportFileDao;

	@Resource(name = "reportFileDaoImpl")
	public void setBaseDao(ReportFileDao reportFileDao) {
		super.setBaseDao(reportFileDao);
	}
	
	/* (non-Javadoc)
	 * @see net.wit.service.impl.ReportFileService#findPage(net.wit.entity.Admin, net.wit.Pageable)
	 */
	@Override
	public Page<ReportFile> findPage(Admin admin, Pageable pageable){
		return reportFileDao.findPage(admin, pageable);
	}
}
