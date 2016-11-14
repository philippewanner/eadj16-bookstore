package ch.bfh.eadj.bookstore.repository;

import ch.bfh.eadj.bookstore.entity.Customer;

import javax.persistence.EntityManager;

/*
Lukas
 */
public class CustomerRepository extends AbstractRepository<Customer, Long> {

	public CustomerRepository(EntityManager em) {
		super(em);
	}
}