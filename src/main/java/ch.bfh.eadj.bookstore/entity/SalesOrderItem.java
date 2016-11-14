package ch.bfh.eadj.bookstore.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class SalesOrderItem {

	@Id
	private Long id;
	private Integer quantity;
	private BigDecimal price;
}