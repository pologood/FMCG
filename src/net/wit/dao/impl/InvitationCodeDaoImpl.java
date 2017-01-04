package net.wit.dao.impl;

import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import net.wit.dao.InvitationCodeDao;
import net.wit.entity.InvitationCode;

/**
 * Created by WangChao on 2016-1-11.
 */
@Repository("invitationCodeDaoImpl")
public class InvitationCodeDaoImpl extends BaseDaoImpl<InvitationCode,Long> implements InvitationCodeDao{
   public InvitationCode findByCode(String code) {
		if (code == null) {
			return null;
		}
		try {
			String jpql = "select invitation from InvitationCode invitation where lower(invitation.code) = lower(:code)";
			return entityManager.createQuery(jpql, InvitationCode.class).setFlushMode(FlushModeType.COMMIT).setParameter("code", code).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
   }
}
