package net.wit.service.impl;

import javax.annotation.Resource;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Member;
import org.springframework.stereotype.Service;

import net.wit.dao.QrcodeDao;
import net.wit.dao.ReturnsDao;
import net.wit.entity.Qrcode;
import net.wit.entity.Tenant;
import net.wit.service.QrcodeService;

import java.util.List;

@Service("qrcodeServiceImpl")
public class QrcodeServiceImpl extends BaseServiceImpl<Qrcode, Long> implements QrcodeService{
	
	@Resource(name = "qrcodeDaoImpl")
	private QrcodeDao qrcodeDao;
	
	@Resource(name = "qrcodeDaoImpl")
	public void setBaseDao(QrcodeDao qrcodeDao){
		super.setBaseDao(qrcodeDao);
	};
	public Qrcode findbyUrl(String url) {
		return qrcodeDao.findbyUrl(url);
	}
	public Qrcode findbyTenant(Tenant tenant) {
		return qrcodeDao.findbyTenant(tenant);
	}

	public boolean tenantExists(Tenant tenant) {
		return qrcodeDao.tenantExists(tenant);
	}

	public Page<Qrcode> openPage(String keyword, Pageable pageable){
		return qrcodeDao.openPage(keyword,pageable);
	}

	@Override
	public List<Qrcode> findUnLockList(Integer count, Member member) {
		return qrcodeDao.findUnLockList(count,member);
	}
}
