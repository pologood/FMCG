package net.wit.dao.impl;



import org.springframework.stereotype.Repository;

import net.wit.dao.ContactProductDao;
import net.wit.entity.ContactProduct;

@Repository("contactProductDaoImpl")
public class ContactProductDaoImpl extends BaseDaoImpl<ContactProduct, Long> implements ContactProductDao {}
