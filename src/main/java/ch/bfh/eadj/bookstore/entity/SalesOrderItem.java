package ch.bfh.eadj.bookstore.entity;

import java.math.BigDecimal;
import javax.persistence.Entity;

import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by philippewanner on 14.11.16.
 */

@Entity
public class SalesOrderItem extends BaseEntity {

	@ManyToOne(optional = false)
	private Book book;

	private Integer quantity;

	private BigDecimal price;

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
