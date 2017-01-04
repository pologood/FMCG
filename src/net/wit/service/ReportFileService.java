package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Admin;
import net.wit.entity.ReportFile;

/** 
 * @ClassName: ReportFileService 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author shenjc 
 * @date 2015年10月29日 上午10:20:51  
 */
public interface ReportFileService extends BaseService<ReportFile, Long>{

	Page<ReportFile> findPage(Admin admin, Pageable pageable);

}