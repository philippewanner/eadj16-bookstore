package org.books.persistence.repository;

import org.books.persistence.dto.CustomerInfo;
import org.books.persistence.entity.Customer;

import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lukas
 */
@Stateless
public class CustomerRepository extends AbstractRepository<Customer, Long> {

	public CustomerRepository() {
		super(Customer.class);
	}

	public List<CustomerInfo> findInfosByName(String name) {
		Map<String, Object> parameters = new HashMap<>(1);
		parameters.put("name", name);

		return findByNamedQuery(CustomerInfo.class, "Customer.findInfosByName", parameters);
	}
}