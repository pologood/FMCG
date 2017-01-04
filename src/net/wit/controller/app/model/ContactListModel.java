package net.wit.controller.app.model;

import java.util.*;

import net.wit.entity.Contact;
import net.wit.entity.Contact.Type;

public class ContactListModel extends BaseModel {
	private Long id;
	private String content;
	private Long hits;
	private Type type;
	private MemberListModel member;

	//private TenantListModel tenant;
	
	/* 图片 */
	private List<ProductImageModel> productImages;

	/* 点赞 */
	private List<MemberListModel> praises;

	/* 商品 */
	private List<ContactProductModel> products;

	/* 回复 */
	private Set<ContactListModel> replyContacts;

	/*  */
	private ContactListModel forContact;

	private Date createDate ;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getHits() {
		return hits;
	}

	public void setHits(Long hits) {
		this.hits = hits;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public MemberListModel getMember() {
		return member;
	}

	public void setMember(MemberListModel member) {
		this.member = member;
	}

//	public TenantListModel getTenant() {
//		return tenant;
//	}
//
//	public void setTenant(TenantListModel tenant) {
//		this.tenant = tenant;
//	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public List<ProductImageModel> getProductImages() {
		return productImages;
	}

	public void setProductImages(List<ProductImageModel> productImages) {
		this.productImages = productImages;
	}

	public List<MemberListModel> getPraises() {
		return praises;
	}

	public void setPraises(List<MemberListModel> praises) {
		this.praises = praises;
	}

	public List<ContactProductModel> getProducts() {
		return products;
	}

	public void setProducts(List<ContactProductModel> products) {
		this.products = products;
	}

	public Set<ContactListModel> getReplyContacts() {
		return replyContacts;
	}

	public void setReplyContacts(Set<ContactListModel> replyContacts) {
		this.replyContacts = replyContacts;
	}

	public ContactListModel getForContact() {
		return forContact;
	}

	public void setForContact(ContactListModel forContact) {
		this.forContact = forContact;
	}

	public void copyAll(Contact contact) {
		this.id = contact.getId();
		this.content = contact.getContent();
		this.hits = contact.getHits();
		this.type = contact.getType();
		MemberListModel member = new MemberListModel();
		member.copyFrom(contact.getMember(),null);
		this.productImages = ProductImageModel.bindData(contact.getImages());
		this.praises = MemberListModel.bindData(contact.getPraises(), null);
		this.member = member;
		this.products = ContactProductModel.bindData(contact.getProducts());
		Set<ContactListModel> replyContact = new HashSet<ContactListModel>();
		for(Contact con:contact.getReplyContacts()){
			ContactListModel replyCon = new ContactListModel();
            replyCon.copyAll(con);
			replyContact.add(replyCon);
		}
		this.replyContacts = replyContact;
		this.createDate = contact.getCreateDate();
//		TenantListModel tenantModel= new TenantListModel();
//		tenantModel.copyFrom(contact.getMember().getTenant());
//		this.tenant = tenantModel;
	}

	public void copyFrom(Contact contact) {
		this.id = contact.getId();
		this.content = contact.getContent();
		this.hits = contact.getHits();
		this.type = contact.getType();
		MemberListModel members = new MemberListModel();
		members.copyFrom(contact.getMember(),null);
		this.productImages = ProductImageModel.bindData(contact.getImages());
		this.praises = MemberListModel.bindData(contact.getPraises(), null);
		this.member = members;
		this.createDate = contact.getCreateDate();
//		TenantListModel tenantModel= new TenantListModel();
//		tenantModel.copyFrom(contact.getMember().getTenant());
//		this.tenant = tenantModel;
	}
	
	public static List<ContactListModel> bindData(List<Contact> contacts) {
		List<ContactListModel> models = new ArrayList<ContactListModel>();
		for (Contact contact : contacts) {
			ContactListModel model = new ContactListModel();
			model.copyAll(contact);
			models.add(model);
		}
		return models;
	}
	
	public static List<ContactListModel> bindRelData(List<Contact> contacts) {
		List<ContactListModel> models = new ArrayList<ContactListModel>();
		for (Contact contact : contacts) {
			ContactListModel model = new ContactListModel();
			model.copyAll(contact);
			models.add(model);
		}
		return models;
	}

	public static List<ContactListModel> bindMessage(List<Contact> contacts) {
		List<ContactListModel> models = new ArrayList<ContactListModel>();
		for (Contact contact : contacts) {
			if (contact.getReplyContacts().size()>0) {
				for (Contact reply:contact.getReplyContacts()) {
				   ContactListModel model = new ContactListModel();
				   model.copyMessage(reply);
			       models.add(model);
				}
			} else {
				ContactListModel model = new ContactListModel();
		    	model.copyMessage(contact);
			    models.add(model);
			}
		}
		return models;
	}

	public void copyMessage(Contact contact) {
		this.id = contact.getId();
		this.content = contact.getContent();
		this.hits = contact.getHits();
		this.type = contact.getType();
		MemberListModel member = new MemberListModel();
		member.copyFrom(contact.getMember(),null);
		this.productImages = ProductImageModel.bindData(contact.getImages());
		this.praises = MemberListModel.bindData(contact.getPraises(), null);
		this.member = member;
		this.products = ContactProductModel.bindData(contact.getProducts());
		Contact forContact = contact.getForContact();
		if (forContact!=null) {
			ContactListModel parent = new ContactListModel();
			parent.copyMessage(forContact);
			this.forContact = parent;
		}
		this.replyContacts = null;
		this.createDate = contact.getCreateDate();

	}
}
