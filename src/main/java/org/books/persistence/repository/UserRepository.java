package org.books.persistence.repository;

import org.books.persistence.entity.Login;

import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Lukas
 */
@Stateless
public class UserRepository extends AbstractRepository<Login, Long> {

	public UserRepository() {
		super(Login.class);
	}

	public Login findByUserName(String name) {
		Map<String, Object> parameters = new HashMap<>(1);
		parameters.put("name", name);

		List<Login> logins = findByNamedQuery("Login.findByUserName", parameters);

		// Always 1 or 0, because of unique constraint
		return logins.size() == 1 ? logins.get(0) : null;
	}
}