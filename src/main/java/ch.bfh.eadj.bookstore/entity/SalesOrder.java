package ch.bfh.eadj.bookstore.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class SalesOrder {

	@Id
	private Long number;
	private LocalDate date;
	private BigDecimal amount;
}