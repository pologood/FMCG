package net.wit.controller.app.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.wit.entity.Contact;
import net.wit.entity.Contact.Type;

public class ContactModel extends BaseModel {
	/*编号*/
	private Long id;
	/*内容*/
	private String content;
	/*类型*/
	private Type type;
	/*是否显示*/
	private Boolean isShow;
	/*会员*/
	private MemberModel member;
	/*咨询*/
	private ContactModel forContact;
	/*回复*/
	private Set<ContactModel> replyContacts;
	/*商品*/
	private List<ContactProductModel> products;
	/*点赞*/
	private List<MemberModel> praises;
	/*图片*/
	private List<ProductImageModel> images;
	/*点击数*/
	private Long hits;
	
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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Boolean getIsShow() {
		return isShow;
	}

	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}

	public MemberModel getMember() {
		return member;
	}

	public void setMember(MemberModel member) {
		this.member = member;
	}

	public ContactModel getForContact() {
		return forContact;
	}

	public void setForContact(ContactModel forContact) {
		this.forContact = forContact;
	}

	public Set<ContactModel> getReplyContacts() {
		return replyContacts;
	}

	public void setReplyContacts(Set<ContactModel> replyContacts) {
		this.replyContacts = replyContacts;
	}

	public List<ContactProductModel> getProducts() {
		return products;
	}

	public void setProducts(List<ContactProductModel> products) {
		this.products = products;
	}

	public List<MemberModel> getPraises() {
		return praises;
	}

	public void setPraises(List<MemberModel> praises) {
		this.praises = praises;
	}

	public List<ProductImageModel> getImages() {
		return images;
	}

	public void setImages(List<ProductImageModel> images) {
		this.images = images;
	}

	public Long getHits() {
		return hits;
	}

	public void setHits(Long hits) {
		this.hits = hits;
	}

	public void copyFrom(Contact contact) {
		this.id = contact.getId();
		this.content = contact.getContent();
		MemberModel member = new MemberModel();
		member.copyFrom(contact.getMember());
		this.member = member;
		this.images = ProductImageModel.bindData(contact.getImages());
		this.praises = MemberModel.bindData(contact.getPraises());
		this.products = ContactProductModel.bindData(contact.getProducts());
		Set<ContactModel> replyContact = new HashSet<ContactModel>();
		for(Contact con:contact.getReplyContacts()){
			ContactModel replyCon = new ContactModel();
            replyCon.copyFrom(con);
			replyContact.add(replyCon);
		}
		this.replyContacts = replyContact;
	}
	
	public static List<ContactModel> bindData(List<Contact> contacts){
		List<ContactModel> models = new ArrayList<ContactModel>();
		for(Contact contact:contacts){
			ContactModel model = new ContactModel();
			model.copyFrom(contact);
			models.add(model);
		}
		return models;
	}
}
