package org.books.persistence.entity;

import org.books.persistence.enumeration.BookBinding;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Book extends BaseEntity {

	private String isbn;

	private String title;

	private String authors;

	private String publisher;

	private Integer publicationYear;

	@Enumerated(EnumType.STRING)
	private BookBinding binding;

	private Integer numberOfPages;

	private BigDecimal price;
        
        public Book(){}

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
	 * @return the authors
	 */
	public String getAuthors() {
		return authors;
	}

	/**
	 * @param authors the authors to set
	 */
	public void setAuthors(String authors) {
		this.authors = authors;
	}

	/**
	 * @return the publisher
	 */
	public String getPublisher() {
		return publisher;
	}

	/**
	 * @param publisher the publisher to set
	 */
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	/**
	 * @return the publicationYear
	 */
	public Integer getPublicationYear() {
		return publicationYear;
	}

	/**
	 * @param publicationYear the publicationYear to set
	 */
	public void setPublicationYear(Integer publicationYear) {
		this.publicationYear = publicationYear;
	}

	/**
	 * @return the binding
	 */
	public BookBinding getBinding() {
		return binding;
	}

	/**
	 * @param binding the binding to set
	 */
	public void setBinding(BookBinding binding) {
		this.binding = binding;
	}

	/**
	 * @return the numberOfPages
	 */
	public Integer getNumberOfPages() {
		return numberOfPages;
	}

	/**
	 * @param numberOfPages the numberOfPages to set
	 */
	public void setNumberOfPages(Integer numberOfPages) {
		this.numberOfPages = numberOfPages;
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