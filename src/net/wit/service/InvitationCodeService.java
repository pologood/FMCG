package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Admin;
import net.wit.entity.Enterprise;
import net.wit.entity.InvitationCode;

/**
 * Created by WangChao on 2016-1-11.
 */
public interface InvitationCodeService extends BaseService<InvitationCode,Long>{
    Page<InvitationCode> findPage(Admin admin, Pageable pageable);
    InvitationCode findByCode(String code);
}
