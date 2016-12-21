package org.books.persistence.entity;

import org.books.persistence.enumeration.OrderStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class SalesOrder implements Serializable {

	/**
	 * Type of "Set" because unique required but not ordered
	 */
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<SalesOrderItem> salesOrderItems = new HashSet<>();

	@Embedded
	private Address address;

	@Embedded
	private CreditCard creditCard;

	@ManyToOne(optional = false)
	private Customer customer;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long number;

	@Temporal(TemporalType.DATE)
	private Date date;

	@NotNull
	private BigDecimal amount;

	@NotNull
	private OrderStatus status;
        
        @Version
        private Long version;

	public Set<SalesOrderItem> getSalesOrderItems() {
		return salesOrderItems;
	}

	public void setSalesOrderItems(Set<SalesOrderItem> salesOrderItems) {
		this.salesOrderItems = salesOrderItems;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public CreditCard getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(CreditCard creditCard) {
		this.creditCard = creditCard;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}
}