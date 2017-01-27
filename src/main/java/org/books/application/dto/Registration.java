package org.books.application.dto;

import org.books.persistence.entity.Customer;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement
public class Registration implements Serializable {

	private Customer customer;
	private String	password;

	public Registration() {
	}

	public Registration(Customer customer, String password) {
		this.customer = customer;
		this.password = password;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}