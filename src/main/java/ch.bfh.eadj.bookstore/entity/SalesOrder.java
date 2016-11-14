package ch.bfh.eadj.bookstore.entity;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class SalesOrder extends BaseEntity {

	public enum OrderStatus {
		CANCELLED, ORDERED, PAYED, DELIVERED
	}

	private LocalDate date;

	private BigDecimal amount;

	private OrderStatus status;
}