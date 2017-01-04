package net.wit.dao.impl;

import java.util.Collections;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ReportFileDao;
import net.wit.entity.Admin;
import net.wit.entity.ReportFile;

/**
 * Dao - 报表功能
 * 
 * @author rsico Team
 * @version 3.0
 */
@Repository("reportFileDaoImpl")
public class ReportFileDaoImpl  extends BaseDaoImpl<ReportFile, Long> implements ReportFileDao{

	/* (non-Javadoc)
	 * @see net.wit.dao.impl.ReportFileDao#findPage(net.wit.entity.Admin, net.wit.Pageable)
	 */
	@Override
	public Page<ReportFile> findPage(Admin admin, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ReportFile> criteriaQuery = criteriaBuilder.createQuery(ReportFile.class);
		Root<ReportFile> root = criteriaQuery.from(ReportFile.class);
		criteriaQuery.select(root);
		if(admin!=null){
			criteriaQuery.where(criteriaBuilder.equal(root.get("admin"), admin));
		}
		return super.findPage(criteriaQuery, pageable);
	}
}
