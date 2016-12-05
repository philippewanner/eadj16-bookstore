package org.books.persistence.repository;

import org.books.persistence.AbstractTest;
import org.books.persistence.dto.CustomerInfo;
import org.books.persistence.entity.Customer;
import org.books.persistence.entity.Login;
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

		List<CustomerInfo> customers = repository.findInfosByName("Hans");
		assertNotNull(customers);
		assertFalse(customers.isEmpty());
		assertEquals("hans@muster.ch", customers.get(0).getEmail());
	}

	@Test
	public void insertDelete() {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Customer insertDelete <<<<<<<<<<<<<<<<<<<<");
		em.getTransaction().begin();

		Login login = userRepository.findByName("bookstore");

		Customer customer = new Customer();
		customer.setName("RepoCustomer");
		customer.setFirstName("a");
		customer.setEmail("a");
		customer.setNumber(new Random().nextLong());
		customer.setLogin(login);

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

	@Test(expected = RollbackException.class)
	public void unique() {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Customer unique <<<<<<<<<<<<<<<<<<<<");

		em.getTransaction().begin();

		Customer customer = new Customer();
		customer.setName("a");
		customer.setFirstName("a");
		customer.setEmail("a");
		customer.setNumber(123456L);
		customer.setLogin(new Login());

		repository.persist(customer);
		em.getTransaction().commit();
	}

	@Test(expected = ConstraintViolationException.class)
	public void notNull() {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Customer notNull <<<<<<<<<<<<<<<<<<<<");

		try {
			em.getTransaction().begin();

			Customer customer = new Customer();
			customer.setLogin(new Login());
			repository.persist(customer);
		} finally {
			em.getTransaction().rollback();
		}
	}

	@Test(expected = ConstraintViolationException.class)
	public void noUser() {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Customer notNull <<<<<<<<<<<<<<<<<<<<");

		try {
			em.getTransaction().begin();

			Customer customer = new Customer();
			customer.setName("a");
			customer.setFirstName("a");
			customer.setEmail("a");
			customer.setNumber(new Random().nextLong());

			repository.persist(customer);
		} finally {
			em.getTransaction().rollback();
		}
	}
}