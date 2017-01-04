package net.wit.controller.app.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.wit.Page;
import net.wit.Pageable;
import net.wit.entity.Ad;
import net.wit.entity.Area;
import net.wit.entity.Product;

public class PageModel extends BaseModel {
	

	/** 页码 */
	private int pageNumber;

	/** 每页记录数 */
	private int pageSize;

	/** 记录数总数 */
	private int totalPages;
	
	/** 记录数总数 */
	private long total;

	
	public int getPageNumber() {
		return pageNumber;
	}


	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}


	public int getPageSize() {
		return pageSize;
	}


	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


	public int getTotalPages() {
		return totalPages;
	}


	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}


	public long getTotal() {
		return total;
	}


	public void setTotal(long total) {
		this.total = total;
	}


	public void copyFrom(Page page) {
		this.pageNumber = page.getPageNumber();
		this.pageSize = page.getPageSize();
		this.totalPages = page.getTotalPages();
		this.total = page.getTotal();
	}
		
}
