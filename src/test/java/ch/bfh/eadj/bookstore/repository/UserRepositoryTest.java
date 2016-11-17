package ch.bfh.eadj.bookstore.repository;

import ch.bfh.eadj.bookstore.AbstractTest;
import ch.bfh.eadj.bookstore.entity.Group;
import ch.bfh.eadj.bookstore.entity.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolationException;

public class UserRepositoryTest extends AbstractTest {

	private UserRepository repository;

	@Before
	public void setUpBeforeTest() {
		repository = new UserRepository(em);
	}

	@Test
	public void searchByName() {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> User namedQuery <<<<<<<<<<<<<<<<<<<<");

		User user = repository.findByName("bookstore");
		Assert.assertNotNull(user);
		Assert.assertEquals("bookstore", user.getPassword());
	}

	@Test
	public void update() {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> User update <<<<<<<<<<<<<<<<<<<<");

		User user = repository.findByName("bookstore");
		Assert.assertNotNull(user);

		user.setPassword("md5");

		em.getTransaction().begin();
		repository.update(user);
		em.getTransaction().commit();

		user = repository.findByName("bookstore");
		Assert.assertNotNull(user);
		Assert.assertEquals("md5", user.getPassword());

		user.setPassword("bookstore");
		em.getTransaction().begin();
		repository.update(user);
		em.getTransaction().commit();
	}

	@Test
	public void insertDelete() {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> User insertDelete <<<<<<<<<<<<<<<<<<<<");

		em.getTransaction().begin();

		User user = new User();
		user.setName("employee");
		user.setPassword("employee");
		Group group = repository.findGroupByName("employee");
		user.addGroup(group);

		repository.persist(user);

		em.getTransaction().commit();

		em.getTransaction().begin();

		user = repository.findByName("employee");
		Assert.assertNotNull(user);
		Assert.assertEquals("employee", user.getPassword());

		repository.delete(user);

		em.getTransaction().commit();

		user = repository.findByName("employee");
		Assert.assertNull(user);
	}

	@Test(expected = IllegalArgumentException.class)
	public void insertError() {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> User insertError <<<<<<<<<<<<<<<<<<<<");

		try {
			em.getTransaction().begin();

			User user = new User();
			user.setName("employee");
			user.setPassword("employee");

			repository.persist(user);
		} finally {
			em.getTransaction().rollback();
		}
	}

	@Test(expected = RollbackException.class)
	public void unique() {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> User unique <<<<<<<<<<<<<<<<<<<<");

		em.getTransaction().begin();

		User user = new User();
		user.setName("bookstore");
		user.setPassword("md5");

		Group group = repository.findGroupByName("customer");
		user.addGroup(group);

		repository.persist(user);
		em.getTransaction().commit();
	}


	@Test(expected = ConstraintViolationException.class)
	public void notNull() {
		LOGGER.info(">>>>>>>>>>>>>>>>>>> User notNull <<<<<<<<<<<<<<<<<<<<");

		try {
			em.getTransaction().begin();

			User user = new User();
			Group group = repository.findGroupByName("customer");
			user.addGroup(group);

			repository.persist(user);
		} finally {
			em.getTransaction().rollback();
		}
	}
}