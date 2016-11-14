package ch.bfh.eadj.bookstore.repository;

import ch.bfh.eadj.bookstore.AbstractTest;
import ch.bfh.eadj.bookstore.dto.CustomerInfo;
import ch.bfh.eadj.bookstore.entity.Customer;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CustomerRepositoryTest extends AbstractTest {

	private CustomerRepository repository;

	@Before
	public void setUpBeforeTest() {
		repository = new CustomerRepository(em);
	}

	@Test
	public void searchByName() {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Customer namedQuery <<<<<<<<<<<<<<<<<<<<");

		Customer customer = repository.findByName("Hans");
		assertNotNull(customer);
		assertEquals("hans@muster.ch", customer.getEmail());

		customer = repository.findByName("Muster");
		assertNotNull(customer);
		assertEquals("hans@muster.ch", customer.getEmail());

		customer = repository.findByName("hans");
		assertNotNull(customer);
		assertEquals("hans@muster.ch", customer.getEmail());

		customer = repository.findByName("muster");
		assertNotNull(customer);
		assertEquals("hans@muster.ch", customer.getEmail());
	}

	@Test
	public void searchInfoByName() {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> CustomerInfo namedQuery <<<<<<<<<<<<<<<<<<<<");

		List<CustomerInfo> customers = repository.findInfosByName("Hans");
		assertNotNull(customers);
		assertFalse(customers.isEmpty());
		assertEquals("hans@muster.ch", customers.get(0).getEmail());
	}

	@Test
	public void insertDelete() {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Customer insertDelete <<<<<<<<<<<<<<<<<<<<");
		em.getTransaction().begin();

		Customer customer = new Customer();
		customer.setName("RepoCustomer");
		repository.persist(customer);

		em.getTransaction().commit();

		assertNotNull(customer.getId());

		customer = repository.find(customer.getId());

		assertNotNull(customer);
		assertEquals("RepoCustomer", customer.getName());

		em.getTransaction().begin();
		customer = repository.find(customer.getId());
		repository.delete(customer);
		em.getTransaction().commit();

		customer = repository.find(customer.getId());

		assertNull(customer);
	}
}