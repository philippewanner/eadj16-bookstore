package org.books.persistence.application;

import org.books.application.dto.Registration;
import org.books.application.exception.CustomerAlreadyExistsException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.exception.InvalidPasswordException;
import org.books.application.service.CustomerService;
import org.books.persistence.dto.CustomerInfo;
import org.books.persistence.entity.Address;
import org.books.persistence.entity.CreditCard;
import org.books.persistence.entity.Customer;
import org.books.persistence.enumeration.CreditCardType;
import org.jboss.logging.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.List;

import static org.testng.Assert.*;

public class CustomerServiceTestIT {

	private static final String ACCOUNT_SERVICE_NAME = "java:global/bookstore/CustomerService";

	private final static Logger LOGGER = Logger.getLogger(CustomerServiceTestIT.class.getName());

	private CustomerService service;

	@BeforeClass
	public void lookup() throws NamingException {
		service = (CustomerService) new InitialContext().lookup(ACCOUNT_SERVICE_NAME);
		assertNotNull(service);
	}

	@Test
	public void register() throws CustomerAlreadyExistsException {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Customer register <<<<<<<<<<<<<<<<<<<<");

		Registration registration = new Registration();
		registration.setCustomer(new Customer("Lukas", "Kalt", "lukas@kalt.ch", new Address(), new CreditCard()));
		registration.setPassword("md5");
		Long number = service.registerCustomer(registration);

		assertNotNull(number);
	}

	@Test(dependsOnMethods = "register", expectedExceptions = CustomerAlreadyExistsException.class)
	public void register_nok() throws CustomerAlreadyExistsException {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Customer register_nok <<<<<<<<<<<<<<<<<<<<");

		Registration registration = new Registration();
		registration.setCustomer(new Customer("Lukas", "Kalt", "lukas@kalt.ch", new Address(), new CreditCard()));
		registration.setPassword("md5");
		service.registerCustomer(registration);
	}

	@Test(dependsOnMethods = "register")
	public void find() throws CustomerNotFoundException {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Customer find <<<<<<<<<<<<<<<<<<<<");

		Customer customer = service.findCustomer("lukas@kalt.ch");
		assertNotNull(customer);

		customer = service.findCustomer(customer.getNumber());
		assertNotNull(customer);
	}

	@Test(expectedExceptions = CustomerNotFoundException.class)
	public void find_notFound() throws CustomerNotFoundException {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Customer find_notFound <<<<<<<<<<<<<<<<<<<<");

		service.findCustomer("notfound");
	}

	@Test(dependsOnMethods = "register")
	public void search() throws CustomerNotFoundException {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Customer search <<<<<<<<<<<<<<<<<<<<");

		List<CustomerInfo> customers = service.searchCustomers("kalt");
		assertNotNull(customers);
		assertFalse(customers.isEmpty());
		assertEquals("lukas@kalt.ch", customers.get(0).getEmail());
	}

	@Test(dependsOnMethods = "register")
	public void authenticate_ok() throws CustomerNotFoundException, InvalidPasswordException {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Customer authenticate_ok <<<<<<<<<<<<<<<<<<<<");

		service.authenticateCustomer("lukas@kalt.ch", "md5");
	}

	@Test(dependsOnMethods = "register", expectedExceptions = InvalidPasswordException.class)
	public void authenticate_wrongPassword() throws CustomerNotFoundException, InvalidPasswordException {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Customer authenticate_wrongPassword <<<<<<<<<<<<<<<<<<<<");

		service.authenticateCustomer("lukas@kalt.ch", "md6");
	}

	@Test(expectedExceptions = CustomerNotFoundException.class)
	public void authenticate_notFound() throws CustomerNotFoundException, InvalidPasswordException {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Customer authenticate_notFound <<<<<<<<<<<<<<<<<<<<");

		service.authenticateCustomer("notfound", "md5");
	}

	@Test(dependsOnMethods = "register")
	public void changePassword() throws CustomerNotFoundException {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Customer changePassword <<<<<<<<<<<<<<<<<<<<");

		service.changePassword("lukas@kalt.ch", "md6");
		service.changePassword("lukas@kalt.ch", "md5");
	}

	@Test(expectedExceptions = CustomerNotFoundException.class)
	public void changePassword_notFound() throws CustomerNotFoundException {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Customer changePassword_notFound <<<<<<<<<<<<<<<<<<<<");

		service.changePassword("notfound", "md6");
	}

	@Test(dependsOnMethods = "register")
	public void update() throws CustomerNotFoundException, CustomerAlreadyExistsException {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Customer update <<<<<<<<<<<<<<<<<<<<");

		Customer customer = service.findCustomer("lukas@kalt.ch");
		assertNotNull(customer);

		CreditCard creditCard = new CreditCard();
		creditCard.setExpirationMonth(12);
		creditCard.setExpirationYear(2016);
		creditCard.setNumber("5555 5555 5555 5555");
		creditCard.setType(CreditCardType.MASTER_CARD);
		customer.setCreditCard(creditCard);

		service.updateCustomer(customer);

		customer = service.findCustomer("lukas@kalt.ch");
		assertNotNull(customer);
		assertNotNull(customer.getCreditCard());
		assertEquals(CreditCardType.MASTER_CARD, customer.getCreditCard().getType());
	}

	@Test(dependsOnMethods = "register")
	public void update_email() throws CustomerNotFoundException, CustomerAlreadyExistsException {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Customer update_email <<<<<<<<<<<<<<<<<<<<");

		Customer customer = service.findCustomer("lukas@kalt.ch");
		assertNotNull(customer);

		customer.setEmail("kalt@lukas.ch");
		service.updateCustomer(customer);

		customer = service.findCustomer("kalt@lukas.ch");
		assertNotNull(customer);

		customer.setEmail("lukas@kalt.ch");
		service.updateCustomer(customer);

	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void update_noNumber() throws CustomerNotFoundException, CustomerAlreadyExistsException {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Customer update_noNumber <<<<<<<<<<<<<<<<<<<<");

		service.updateCustomer(new Customer());
	}

	@Test(expectedExceptions = CustomerNotFoundException.class)
	public void update_notFound() throws CustomerNotFoundException, CustomerAlreadyExistsException {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Customer update_notFound <<<<<<<<<<<<<<<<<<<<");

		Customer customer = new Customer();
		customer.setNumber(-1L);
		service.updateCustomer(customer);
	}
}