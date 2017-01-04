package net.wit.dao.impl;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import net.wit.dao.SmsSendDao;
import net.wit.entity.Shipping;
import net.wit.entity.SmsSend;

import org.springframework.stereotype.Repository;

/**
 * Dao实现类 - 短信发送
 * ============================================================================
 * 版权所有 2008-2010 rsico.com,并保留所有权利。
 * ----------------------------------------------------------------------------
 * 提示：在未取得rsico商业授权之前,您不能将本软件应用于商业用途,否则rsico将保留追究的权力。
 * ----------------------------------------------------------------------------
 * 官方网站：http://www.rsico.cn
 * ----------------------------------------------------------------------------
 * KEY: SHOPUNION818712BD2CE092B961D2E741A622D073
 * ============================================================================
 */

@Repository("smsSendDaoImpl")
public class SmsSendDaoImpl extends BaseDaoImpl<SmsSend, String> implements SmsSendDao {

	public Long findSendCount(String mobile,SmsSend.Type type) {
		if (mobile == null) {
			return Long.valueOf(0);
		}
		String jpql = "select count(smsSend) from SmsSend smsSend where smsSend.mobiles = :mobile and smsSend.type = :type and date_format(smsSend.createDate,'%Y-%c-%d') = date_format(now(),'%Y-%c-%d')";
		try {
			return entityManager.createQuery(jpql,Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("mobile", mobile).setParameter("type", type).getSingleResult();
		} catch (NoResultException e) {
			return Long.valueOf(0);
		}
	}
}