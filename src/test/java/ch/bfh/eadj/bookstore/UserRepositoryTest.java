package ch.bfh.eadj.bookstore;

import ch.bfh.eadj.bookstore.entity.User;
import ch.bfh.eadj.bookstore.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserRepositoryTest extends AbstractTest {

	private UserRepository userRepository;

	@Before
	public void setUpBeforeTest() {
		userRepository = new UserRepository(em);
	}

	@Test
	public void searchByName() {
		User user = userRepository.findByName("bookstore");
		Assert.assertNotNull(user);
		Assert.assertEquals("bookstore", user.getPassword());
	}
}