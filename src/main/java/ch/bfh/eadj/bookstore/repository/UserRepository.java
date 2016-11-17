package ch.bfh.eadj.bookstore.repository;

import ch.bfh.eadj.bookstore.entity.Group;
import ch.bfh.eadj.bookstore.entity.User;

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

	@Override
	public void persist(User user) {
		if (user.getGroups() == null || user.getGroups().isEmpty()) {
			throw new IllegalArgumentException("User must have at least one group");
		}
		super.persist(user);
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