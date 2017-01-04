package net.wit.service;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;
import net.wit.entity.Qrcode;
import net.wit.entity.Tenant;

import java.util.List;

public interface QrcodeService extends BaseService<Qrcode, Long> {

    Qrcode findbyUrl(String url);

    Qrcode findbyTenant(Tenant tenant);

    boolean tenantExists(Tenant tenant);
    Page<Qrcode> openPage(String keword, Pageable pageable);
    List<Qrcode> findUnLockList(Integer count, Member member);
}
