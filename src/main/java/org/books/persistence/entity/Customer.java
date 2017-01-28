package org.books.persistence.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import org.books.persistence.dto.CustomerInfo;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "email" }) })
@XmlRootElement
public class Customer implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long number;
	@NotNull
	private String firstName;
	@NotNull
	private String lastName;
	@NotNull
	private String email;

	@Embedded
	private Address address;
	@Embedded
	private CreditCard creditCard;

	@Version
	private Long version;

	public Customer() {
	}

	public Customer(Long number, String firstName, String lastName, String email, Address address, CreditCard creditCard) {
		this.number = number;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.address = address;
		this.creditCard = creditCard;
	}

	public Customer(String firstName, String lastName, String email, Address address, CreditCard creditCard) {
		this(null, firstName, lastName, email, address, creditCard);
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

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String name) {
		this.lastName = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

    public CustomerInfo getCustomerInfo() {
        CustomerInfo ci = new CustomerInfo(this.number, this.firstName, this.lastName, this.email);        
        return ci;
    }
}