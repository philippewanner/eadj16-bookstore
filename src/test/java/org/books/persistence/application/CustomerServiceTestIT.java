package org.books.persistence.application;

import org.books.application.dto.Registration;
import org.books.application.exception.CustomerAlreadyExistsException;
import org.books.application.service.CustomerService;
import org.books.persistence.entity.Customer;
import org.books.persistence.entity.Login;
import org.junit.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class CustomerServiceTestIT {

	private static final String ACCOUNT_SERVICE_NAME = "java:global/bookstore/CustomerService";

	private CustomerService service;

	@BeforeClass
	public void lookup() throws NamingException {
		service = (CustomerService) new InitialContext().lookup(ACCOUNT_SERVICE_NAME);
		Assert.assertNotNull(service);
	}

	@Test
	public void first() throws CustomerAlreadyExistsException {
		Registration registration = new Registration();
		Login login = new Login();
		registration.setCustomer(new Customer(1L, "Hans", "Muster", "hans@muster.ch", login));
		Long id = service.registerCustomer(registration);

		Assert.assertNotNull(id);
	}
}