package org.books.application.service;

import org.books.application.dto.PurchaseOrder;
import org.books.application.exception.*;
import org.books.persistence.dto.OrderInfo;
import org.books.persistence.entity.Customer;
import org.books.persistence.entity.SalesOrder;
import org.books.persistence.enumeration.OrderStatus;
import org.books.persistence.repository.CustomerRepository;
import org.books.persistence.repository.OrderRepository;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author Philippe
 *
 * The service allows to:
 *      place an order,
 *      find an order by number
 *      search for orders by customer and year
 *      cancel an order
 */

@Stateless(name = "OrderService")
public class OrderServiceBean extends AbstractService implements OrderService {

    @EJB
    private OrderRepository orderRepository;

    @EJB
    private CustomerRepository customerRepository;

    @Override
    public void cancelOrder(Long orderNr) throws OrderNotFoundException, OrderAlreadyShippedException {

        SalesOrder orderFound = orderRepository.findByNumber(orderNr);
        if(orderFound == null) {

            logInfo("Order nr "+orderNr + " not found");
            throw new OrderNotFoundException();
        }
        if(orderFound.getStatus().equals(OrderStatus.SHIPPED)){

            logInfo("Order nr "+orderNr + " already shipped");
            throw new OrderAlreadyShippedException(); }

        orderFound.setStatus(OrderStatus.CANCELED);
        orderRepository.update(orderFound);
    }

    @Override
    public SalesOrder findOrder(Long orderNr) throws OrderNotFoundException {

        SalesOrder orderFound = orderRepository.findByNumber(orderNr);
        if(orderFound == null) {

            logInfo("Order nr "+orderNr + " not found");
            throw new OrderNotFoundException();
        }

        return orderFound;
    }

    @Override
    public SalesOrder placeOrder(PurchaseOrder purchaseOrder) throws CustomerNotFoundException, BookNotFoundException, PaymentFailedException {
        return null;
    }

    @Override
    public List<OrderInfo> searchOrders(Long customerNr, Integer year) throws CustomerNotFoundException {

        Customer customer = customerRepository.find(customerNr);
        if( customer == null ) {

            logInfo("Customer nr "+customerNr + " not found");
            throw new CustomerNotFoundException();
        }

        List<OrderInfo> orderInfos = orderRepository.searchByCustomerAndYear(customer, year);
        return orderInfos;
    }
}
