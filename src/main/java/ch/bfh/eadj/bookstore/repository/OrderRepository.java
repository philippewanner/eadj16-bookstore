package ch.bfh.eadj.bookstore.repository;

import ch.bfh.eadj.bookstore.dto.OrderInfo;
import ch.bfh.eadj.bookstore.entity.Customer;
import ch.bfh.eadj.bookstore.entity.SalesOrder;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import javax.validation.constraints.Past;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 * Philippe
 */
public class OrderRepository extends AbstractRepository<SalesOrder, Integer> {

    public OrderRepository(EntityManager em) {
        super(em);
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

        Map<String, Object> parameters = new HashMap<>(2);
        parameters.put("year", year);

        Query q = em.createNamedQuery("SalesOrder.sumOrdersGroupedByCustomerForYear");

        for (String key : parameters.keySet()) {
            q.setParameter(key, parameters.get(key));
        }

        return q.getResultList();

    }
}
