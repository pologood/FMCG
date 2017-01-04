/**
 *====================================================
 * 文件名称: MemberBankService.java
 * 修订记录：
 * No    日期				作者(操作:具体内容)
 * 1.    2014年7月30日			Administrator(创建:创建文件)
 *====================================================
 * 类描述：(说明未实现或其它不应生成javadoc的内容)
 * 
 */
package net.wit.service;

import net.wit.Filter;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;
import net.wit.entity.MemberBank;
import net.wit.entity.PlatformCapital;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: MemberBankService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Administrator
 * @date 2014年7月30日 上午9:05:40
 */
public interface PlatformCapitalService extends BaseService<PlatformCapital, Long> {

    Page<PlatformCapital> findPageByDate(Date begin_date, Date end_date, Pageable pageable);
    List<PlatformCapital> findListByDate(Date begin_date, Date end_date, List<Filter> filters);
}
