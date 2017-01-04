package net.wit.controller.app.model;

import java.util.Set;

public class ReplyContactsModel extends BaseModel {

	private Long id;
	private String content;
	private Set<ContactModel> contact;
	private Set<MemberListModel> member;
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
	
	public Set<ContactModel> getContact() {
		return contact;
	}
	public void setContact(Set<ContactModel> contact) {
		this.contact = contact;
	}
	public Set<MemberListModel> getMember() {
		return member;
	}
	public void setMember(Set<MemberListModel> member) {
		this.member = member;
	}
	
}
