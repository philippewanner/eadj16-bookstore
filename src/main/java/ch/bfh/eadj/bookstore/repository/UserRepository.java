package ch.bfh.eadj.bookstore.repository;

import ch.bfh.eadj.bookstore.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Lukas
 */
public class UserRepository extends AbstractRepository<User, String> {

	public UserRepository(EntityManager em) {
		super(em);
	}

	public User findByName(String name) {
		Map<String, Object> parameters = new HashMap<>(1);
		parameters.put("name", name);

		List<User> users = findByNamedQuery("User.findByName", parameters);
		return users.get(0);
	}
}