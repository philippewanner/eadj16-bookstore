package ch.bfh.eadj.bookstore.repository;

import ch.bfh.eadj.bookstore.AbstractTest;
import ch.bfh.eadj.bookstore.entity.Customer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CustomerRepositoryTest extends AbstractTest {

	private CustomerRepository customerRepository;

	@Before
	public void setUpBeforeTest() {
		customerRepository = new CustomerRepository(em);
	}

	@Test
	public void searchByName() {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Customer namedQuery <<<<<<<<<<<<<<<<<<<<");

		Customer customer = customerRepository.findByName("Hans");
		Assert.assertNotNull(customer);
		Assert.assertEquals("hans@muster.ch", customer.getEmail());

		customer = customerRepository.findByName("Muster");
		Assert.assertNotNull(customer);
		Assert.assertEquals("hans@muster.ch", customer.getEmail());

		// TODO: case insensitive
		/*
		customer = customerRepository.findByName("hans");
		Assert.assertNotNull(customer);
		Assert.assertEquals("hans@muster.ch", customer.getEmail());

		customer = customerRepository.findByName("muster");
		Assert.assertNotNull(customer);
		Assert.assertEquals("hans@muster.ch", customer.getEmail());
		*/
	}
}