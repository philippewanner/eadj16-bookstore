package ch.bfh.eadj.bookstore.repository;

import ch.bfh.eadj.bookstore.AbstractTest;
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
	}
}