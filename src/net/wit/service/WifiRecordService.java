package net.wit.service;

import com.fr.third.org.apache.poi.hssf.record.formula.functions.T;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;
import net.wit.entity.Product;
import net.wit.entity.Tenant;
import net.wit.entity.WifiRecord;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 访问记录
 * @author Administrator
 *
 */
public interface WifiRecordService extends BaseService<WifiRecord,Long>{
    /**
     * 客访问量统计
     */
    List<Map<String,Object>> findList(String datetime, Tenant tenant);
    /**
     * 会员访问量统计
     */
    List<Map<String,Object>> findMemberList(String datetime, Tenant tenant);
    /**
     * 智能识别统计
     */
    Page<Map<String,Object>> findSummaryPage(Date start_time,Date end_time,Tenant tenant,Pageable pageable);
}
