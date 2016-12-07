package org.books.persistence.repository;

import org.books.persistence.dto.OrderInfo;
import org.books.persistence.entity.Customer;
import org.books.persistence.entity.SalesOrder;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.validation.Valid;
import javax.validation.constraints.Past;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Philippe
 */
@Stateless
public class OrderRepository extends AbstractRepository<SalesOrder, Long> {

	public OrderRepository() {
		super(SalesOrder.class);
	}

	/**
     * Get an order with a particular order number.
     *
     * @param number particular order number.
     * @return the particular order found or null if none exists.
     */
    public SalesOrder findByNumber(Long number) {

        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("number", number);

        List<SalesOrder> salesOrder = findByNamedQuery("SalesOrder.findByNumber", parameters);

        // Always 1 or 0, because of unique constraint
        return salesOrder.size() == 1 ? salesOrder.get(0) : null;
    }

    /**
     * Search information on all orders of a particular customer and a
     * particular year.
     *
     * @param customer particular customer search criteria.
     * @param year particular year search criteria.
     * @return a list of OrderInfo.
     */
    public List<OrderInfo> searchByCustomerAndYear(@Valid Customer customer, @Past Integer year) {

        Map<String, Object> parameters = new HashMap<>(2);
        parameters.put("customer", customer);
        parameters.put("year", year);

        List<OrderInfo> orderInfos = findByNamedQuery(OrderInfo.class, "SalesOrder.searchByCustomerAndYear", parameters);
        return orderInfos;
    }

   /**
     * sum of amount and number of positions and average amount per position on
     * all orders of all customers of a particular year group by customer
     *
     */
    public List<Object> sumAmountNumberPositionsAvgAmountPerYear(@Past Integer year) {

        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("year", year);

        Query q = em.createNamedQuery("SalesOrder.sumOrdersGroupedByCustomerForYear");

        for (String key : parameters.keySet()) {
            q.setParameter(key, parameters.get(key));
        }

        return q.getResultList();

    }
}
