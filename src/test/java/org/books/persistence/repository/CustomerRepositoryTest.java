package org.books.persistence.repository;

import org.books.persistence.AbstractTest;
import org.books.persistence.dto.CustomerInfo;
import org.books.persistence.entity.Customer;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class CustomerRepositoryTest extends AbstractTest {

	private CustomerRepository repository;
	private UserRepository userRepository;

	@Before
	public void setUpBeforeTest() {
		repository = new CustomerRepository();
		userRepository = new UserRepository();

		repository.setEntityManager(em);
		userRepository.setEntityManager(em);
	}

	@Test
	public void searchInfoByName() {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> CustomerInfo namedQuery <<<<<<<<<<<<<<<<<<<<");

		List<CustomerInfo> customers = repository.search("Hans");
		assertNotNull(customers);
		assertFalse(customers.isEmpty());
		assertEquals("hans@muster.ch", customers.get(0).getEmail());

		Customer customer = repository.find(customers.get(0).getNumber());
		assertNotNull(customer);
		assertEquals("hans@muster.ch", customer.getEmail());
	}

	@Test
	public void insertDelete() {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Customer insertDelete <<<<<<<<<<<<<<<<<<<<");
		em.getTransaction().begin();

		Customer customer = new Customer();
		customer.setLastName("RepoCustomer");
		customer.setFirstName("a");
		customer.setEmail("a");
		customer.setNumber(new Random().nextLong());

		repository.persist(customer);

		em.getTransaction().commit();

		assertNotNull(customer.getNumber());

		customer = repository.find(customer.getNumber());

		assertNotNull(customer);
		assertEquals("RepoCustomer", customer.getLastName());

		em.getTransaction().begin();
		customer = repository.find(customer.getNumber());
		repository.delete(customer);
		em.getTransaction().commit();

		customer = repository.find(customer.getNumber());

		assertNull(customer);
	}

	@Test(expected = RollbackException.class)
	public void unique() {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Customer unique <<<<<<<<<<<<<<<<<<<<");

		em.getTransaction().begin();

		Customer customer = new Customer();
		customer.setLastName("a");
		customer.setFirstName("a");
		customer.setEmail("hans@muster.ch");

		repository.persist(customer);
		em.getTransaction().commit();
	}

	@Test(expected = ConstraintViolationException.class)
	public void notNull() {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Customer notNull <<<<<<<<<<<<<<<<<<<<");

		try {
			em.getTransaction().begin();

			Customer customer = new Customer();
			repository.persist(customer);
		} finally {
			em.getTransaction().rollback();
		}
	}
}