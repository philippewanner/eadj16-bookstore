package ch.bfh.eadj.bookstore.repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.Order;

/**
 * Philippe
 */
public class OrderRepository extends AbstractRepository<Order, Integer> {

	public OrderRepository(EntityManager em) {
		super(em);
	}

	//search(Customer, Integer) List<OrderInfo>
}
