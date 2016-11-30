package org.books.persistence.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "number" }) })
public class Customer extends BaseEntity {

	@NotNull
	private Long number;
	@NotNull
	private String firstName;
	@NotNull
	private String name;
	@NotNull
	private String email;

	@OneToOne(optional = false)
	@NotNull
	private User user;
	@Embedded
	private Address address;
	@Embedded
	private CreditCard creditCard;

	public Customer() {
	}

	public Customer(Long number, String firstName, String name, String email, User user) {
		this.number = number;
		this.firstName = firstName;
		this.name = name;
		this.email = email;
		this.user = user;
	}

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
}