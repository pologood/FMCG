package net.wit.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import net.wit.dao.ContactProductDao;
import net.wit.entity.ContactProduct;
import net.wit.service.ContactProductService;

@Repository("contactProductServiceImpl")
public class ContactProductServiceImpl extends BaseServiceImpl<ContactProduct, Long> implements ContactProductService{
	@Resource(name = "contactProductDaoImpl")
	private ContactProductDao contactProductDao;
	
	@Resource(name = "contactProductDaoImpl")
	public void setBaseDao(ContactProductDao contactProductDao) {
		super.setBaseDao(contactProductDao);
	}
}
