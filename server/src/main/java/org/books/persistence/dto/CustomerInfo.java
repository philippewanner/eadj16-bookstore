/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.persistence.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author jlue4
 */
@XmlRootElement
public class CustomerInfo implements Serializable {

	private Long number;
	private String firstName;
	private String lastName;
	private String email;

	/*
	* Constructor for Fischlin Unit Tests
	 */
	public CustomerInfo() {
	}

	/*
	* Constructor for Fischlin Unit Tests
	 */
	public CustomerInfo(Long number, String firstName, String lastName, String email) {
		this.number = number;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
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

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}