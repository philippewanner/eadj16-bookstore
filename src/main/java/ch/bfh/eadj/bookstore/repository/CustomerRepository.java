package ch.bfh.eadj.bookstore.repository;

import ch.bfh.eadj.bookstore.dto.CustomerInfo;
import ch.bfh.eadj.bookstore.entity.Customer;
import ch.bfh.eadj.bookstore.entity.User;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Lukas
 */
public class CustomerRepository extends AbstractRepository<Customer, Long> {

	public CustomerRepository(EntityManager em) {
		super(em);
	}

	public Customer findByName(String name) {
		Map<String, Object> parameters = new HashMap<>(1);
		parameters.put("name", name);

		// TODO: Anforderung nachfragen: LIKE oder =?
		List<Customer> customers = findByNamedQuery("Customer.findByName", parameters);
		return customers.get(0);
	}

	public List<CustomerInfo> findInfosByName(String name) {
		Map<String, Object> parameters = new HashMap<>(1);
		parameters.put("name", name);

		return findByNamedQuery(CustomerInfo.class, "Customer.findInfosByName", parameters);
	}
}