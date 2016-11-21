package ch.bfh.eadj.bookstore.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * SalesOrderItem represents a subset of an order.
 */

@Entity
public class SalesOrderItem extends BaseEntity {

	@ManyToOne(optional = false, cascade = CascadeType.REFRESH)
	private Book book;

	@NotNull
	private Integer quantity;

	@NotNull
	private BigDecimal price;

	public SalesOrderItem() {}

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
