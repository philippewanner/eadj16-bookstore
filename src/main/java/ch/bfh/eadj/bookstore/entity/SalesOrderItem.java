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
}
