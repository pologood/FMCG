package net.wit.service.impl;


import net.wit.Filter;
import net.wit.Order;
import net.wit.Page;
import net.wit.Pageable;
import net.wit.dao.ContactDao;
import net.wit.entity.Area;
import net.wit.entity.ContactProduct;
import net.wit.entity.Member;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import net.wit.entity.Contact;
import net.wit.service.ContactService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 社交圈
 * @author thwapp
 *
 */
@Service("contactServiceImpl")
public class ContactServiceImpl extends BaseServiceImpl<Contact, Long> implements ContactService {


	@Resource(name = "contactDaoImpl")
	private ContactDao contactDao;

//	@Resource(name = "staticServiceImpl")
//	private StaticService staticService;

	@Resource(name = "contactDaoImpl")
	public void setBaseDao(ContactDao contactDao) {
		super.setBaseDao(contactDao);
	}

	@Transactional(readOnly = true)
	public List<Contact> findList(Member member, ContactProduct product, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders) {
		return contactDao.findList(false, member, product, isShow, count, filters, orders);
	}

	@Transactional(readOnly = true)
	public List<Contact> findList(Boolean hasRepaly, Member member, ContactProduct product, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders) {
		return contactDao.findList(hasRepaly, member, product, isShow, count, filters, orders);
	}

	@Transactional(readOnly = true)
    @Cacheable("contact")
	public List<Contact> findList(Member member, ContactProduct product, Boolean isShow, Integer count, List<Filter> filters, List<Order> orders, String cacheRegion) {
		return contactDao.findList(false, member, product, isShow, count, filters, orders);
	}

	@Transactional(readOnly = true)
    @Cacheable("contact")
	public Page<Contact> findPage(Contact.Type type, Member member, ContactProduct product, Boolean isShow, Area area, List<Long> tenants, Pageable pageable) {
		return contactDao.findPage(type, member, product, isShow, area,tenants,pageable);
	}

	@Transactional(readOnly = true)
	@Cacheable("contact")
	public Page<Contact> findMessage(Member member,  Pageable pageable) {
		return contactDao.findMessage(member, pageable);
	}

	@Transactional(readOnly = true)
    @Cacheable("contact")
	public Page<Contact> findMyPage(Member member, ContactProduct product, Boolean isShow, Contact.Type type, Pageable pageable) {
		return contactDao.findMyPage(member, product, isShow, type,pageable);
	}

	@Transactional(readOnly = true)
	public Long count(Member member, ContactProduct product, Boolean isShow,Contact.Type type) {
		return contactDao.count(member, product, isShow,type);
	}

	@CacheEvict(value = { "contact" }, allEntries = true)
	@Transactional
	public void reply(Contact contact, Contact replyContact) {
		if (contact == null || replyContact == null) {
			return;
		}
		replyContact.setIsShow(true);
		replyContact.setProducts(contact.getProducts());
		replyContact.setForContact(contact);
		contactDao.persist(replyContact);

		contact.getReplyContacts().add(replyContact);
		contact.setIsShow(true);
		contactDao.merge(contact);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "contact"  }, allEntries = true)
	public void save(Contact contact) {
		super.save(contact);
		List<ContactProduct> product = contact.getProducts();
		if (product != null) {
			contactDao.flush();
			//staticService.build(product);
		}
	}

	@Override
	@Transactional
	@CacheEvict(value = { "contact"  }, allEntries = true)
	public Contact update(Contact contact) {
		Contact pContact = super.update(contact);
		List<ContactProduct> product = pContact.getProducts();
		if (product != null) {
			contactDao.flush();
			//staticService.build(product);
		}
		return pContact;
	}

	@Override
	@Transactional
	@CacheEvict(value = { "contact" }, allEntries = true)
	public Contact update(Contact consultation, String... ignoreProperties) {
		return super.update(consultation, ignoreProperties);
	}

	@Override
	@Transactional
	@CacheEvict(value = {"contact"  }, allEntries = true)
	public void delete(Long id) {
		super.delete(id);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "contact"  }, allEntries = true)
	public void delete(Long... ids) {
		super.delete(ids);
	}

	@Override
	@Transactional
	@CacheEvict(value = { "contact" }, allEntries = true)
	public void delete(Contact contact) {
		if (contact != null) {
			super.delete(contact);
			List<ContactProduct> product = contact.getProducts();
			if (product != null) {
				contactDao.flush();
				//staticService.build(product);
			}
		}
	}

}
