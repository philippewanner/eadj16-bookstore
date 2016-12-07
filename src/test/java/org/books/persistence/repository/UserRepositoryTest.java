package org.books.persistence.repository;

import org.books.persistence.AbstractTest;
import org.books.persistence.entity.Login;
import org.books.persistence.enumeration.UserGroup;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolationException;

public class UserRepositoryTest extends AbstractTest {

	private UserRepository repository;

	@Before
	public void setUpBeforeTest() {
		repository = new UserRepository();
		repository.setEntityManager(em);
	}

	@Test
	public void searchByName() {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Login namedQuery <<<<<<<<<<<<<<<<<<<<");

		Login login = repository.findByUserName("bookstore");
		Assert.assertNotNull(login);
		Assert.assertEquals("bookstore", login.getPassword());
	}

	@Test
	public void update() {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Login update <<<<<<<<<<<<<<<<<<<<");

		Login login = repository.findByUserName("bookstore");
		Assert.assertNotNull(login);

		login.setPassword("md5");

		em.getTransaction().begin();
		repository.update(login);
		em.getTransaction().commit();

		login = repository.findByUserName("bookstore");
		Assert.assertNotNull(login);
		Assert.assertEquals("md5", login.getPassword());

		login.setPassword("bookstore");
		em.getTransaction().begin();
		repository.update(login);
		em.getTransaction().commit();
	}

	@Test
	public void insertDelete() {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Login insertDelete <<<<<<<<<<<<<<<<<<<<");

		em.getTransaction().begin();

		Login login = new Login();
		login.setUserName("employee");
		login.setPassword("employee");
		login.setGroup(UserGroup.EMPLOYEE);

		repository.persist(login);

		em.getTransaction().commit();

		em.getTransaction().begin();

		login = repository.findByUserName("employee");
		Assert.assertNotNull(login);
		Assert.assertEquals("employee", login.getPassword());

		repository.delete(login);

		em.getTransaction().commit();

		login = repository.findByUserName("employee");
		Assert.assertNull(login);
	}

	@Test(expected = RollbackException.class)
	public void unique() {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Login unique <<<<<<<<<<<<<<<<<<<<");

		em.getTransaction().begin();

		Login login = new Login();
		login.setUserName("bookstore");
		login.setPassword("md5");

		repository.persist(login);
		em.getTransaction().commit();
	}


	@Test(expected = ConstraintViolationException.class)
	public void notNull() {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> Login notNull <<<<<<<<<<<<<<<<<<<<");

		try {
			em.getTransaction().begin();

			Login login = new Login();
			repository.persist(login);
		} finally {
			em.getTransaction().rollback();
		}
	}
}