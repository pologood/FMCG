package net.wit.dao;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Tenant;
import net.wit.entity.VisitRecord;
import net.wit.entity.WifiRecord;

import java.nio.file.WatchEvent;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface WifiRecordDao extends BaseDao<WifiRecord,Long>{

  /**
   * 客访问量统计
   */
  List<Map<String,Object>> findList(String datetime,Tenant tenant);

  /**
   * 会员访问量统计
   */
  List<Map<String,Object>> findMemberList(String datetime, Tenant tenant);

  /**
   * 智能识别统计
   */
  Page<Map<String,Object>> findSummaryPage(Date start_time,Date end_time,Tenant tenant,Pageable pageable);

  }


