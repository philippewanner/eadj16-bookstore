/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author jlue4
 */
@XmlRootElement
public class BookInfo implements Serializable {

	private Long id;
	private String isbn;
	private String title;
	private BigDecimal price;

	/*
	* Constructor for Fischlin Unit Tests
	 */
	public BookInfo() {}

	/*
	* Constructor for Fischlin Unit Tests
	 */
	public BookInfo(String isbn, String title, BigDecimal price) {
		this(null, isbn, title, price);
	}

	public BookInfo(Long id, String isbn, String title, BigDecimal price) {
		this.id = id;
		this.isbn = isbn;
		this.title = title;
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the isbn
	 */
	public String getIsbn() {
		return isbn;
	}

	/**
	 * @param isbn the isbn to set
	 */
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
