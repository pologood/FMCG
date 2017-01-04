package net.wit.dao;

import net.wit.entity.InvitationCode;

/**
 * Created by WangChao on 2016-1-11.
 */
public interface InvitationCodeDao extends BaseDao<InvitationCode,Long>{
   public InvitationCode findByCode(String code);
}
