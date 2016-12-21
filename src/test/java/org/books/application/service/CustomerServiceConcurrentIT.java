package org.books.application.service;

import org.books.DbUtil;
import org.books.application.dto.Registration;
import org.books.application.exception.CustomerAlreadyExistsException;
import org.books.persistence.entity.Address;
import org.books.persistence.entity.CreditCard;
import org.books.persistence.entity.Customer;
import org.jboss.logging.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.assertNotNull;

public class CustomerServiceConcurrentIT {

	private static final String ACCOUNT_SERVICE_NAME = "java:global/bookstore/CustomerService";

	private final static Logger LOGGER = Logger.getLogger(CustomerServiceIT.class.getName());

	private static final int THREAD_COUNT = 100;

	private static AtomicInteger nextID = new AtomicInteger(0);

	private CustomerService service;

	@BeforeClass
	public void lookup() throws NamingException, SQLException {
		service = (CustomerService) new InitialContext().lookup(ACCOUNT_SERVICE_NAME);
		assertNotNull(service);

	}

	@AfterClass
	public void tearDown() throws SQLException {
		DbUtil.clearDatabase();
	}

	@Test(threadPoolSize = THREAD_COUNT, invocationCount = THREAD_COUNT)
	public void register() throws CustomerAlreadyExistsException {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Customer register <<<<<<<<<<<<<<<<<<<<");

		Registration registration = new Registration();
		registration.setCustomer(new Customer("Lukas"
				+ Integer.toString(nextID.incrementAndGet()), "Kalt",
				"lukas" + Integer.toString(nextID.incrementAndGet()) + "@kalt.ch", new Address(), new CreditCard()));
		registration.setPassword("md5");
		Long number = service.registerCustomer(registration);

		assertNotNull(number);
	}
}
