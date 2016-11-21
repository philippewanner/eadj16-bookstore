package ch.bfh.eadj.bookstore.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

import ch.bfh.eadj.bookstore.common.definition.Definitions.OrderStatus;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "number" }) })
public class SalesOrder extends BaseEntity {

	/**
	 * Type of "Set" because unique required but not ordered
	 */
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<SalesOrderItem> salesOrderItems;

	@Embedded
	private Address address;

	@Embedded
	private CreditCard creditCard;

	@ManyToOne(optional = false)
	private Customer customer;

	@NotNull
	private Long number;

    @Temporal(TemporalType.DATE)
	private Date date;

        @NotNull
        private BigDecimal amount;

        @NotNull
        private OrderStatus status;

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