package org.books.persistence.repository;

import org.books.persistence.entity.Group;
import org.books.persistence.entity.User;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Lukas
 */
public class UserRepository extends AbstractRepository<User, Long> {

	public UserRepository(EntityManager em) {
		super(em);
	}

	public User findByName(String name) {
		Map<String, Object> parameters = new HashMap<>(1);
		parameters.put("name", name);

		List<User> users = findByNamedQuery("User.findByName", parameters);

		// Always 1 or 0, because of unique constraint
		return users.size() == 1 ? users.get(0) : null;
	}

	public Group findGroupByName(String name) {
		Map<String, Object> parameters = new HashMap<>(1);
		parameters.put("name", name);

		List<Group> groups = findByNamedQuery(Group.class, "Group.findByName", parameters);

		// Always 1 or 0, because of unique constraint
		return groups.size() == 1 ? groups.get(0) : null;
	}
}