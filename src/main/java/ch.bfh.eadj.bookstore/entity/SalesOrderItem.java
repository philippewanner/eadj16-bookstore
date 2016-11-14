package ch.bfh.eadj.bookstore.entity;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class SalesOrderItem extends BaseEntity {

	private Integer quantity;

	private BigDecimal price;
}