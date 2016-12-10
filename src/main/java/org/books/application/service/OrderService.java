package org.books.application.service;

import org.books.application.dto.PurchaseOrder;
import org.books.application.exception.*;
import org.books.persistence.dto.OrderInfo;
import org.books.persistence.entity.SalesOrder;

import java.util.List;

/**
 *
 * The order service provides operations to manage the orders of a bookstore.
 * Author: Philippe
 *
 * The objective of this task is to implement the order service of the bookstore application.

 Implement a stateless session bean specified by the remote interface OrderService.
 The service allows to
 place an order
 find an order by number
 search for orders by customer and year
 cancel an order
 Implement integration tests to verify the functionality of the service.

 */
public interface OrderService {

    /**
     * Cancels an order.
     * @param orderNr the number of the order
     * @throws OrderNotFoundException if no order with the specified number exists.
     * @throws OrderAlreadyShippedException if the order has already been shipped.
     */
    void cancelOrder(Long orderNr) throws OrderNotFoundException,
            OrderAlreadyShippedException;

    /**
     * Finds an order by number.
     * @param orderNr the number of the order.
     * @throws OrderNotFoundException if no order with the specified number exists.
     * @return the data of the found order.
     */
    SalesOrder findOrder(Long orderNr) throws OrderNotFoundException;

    /**
     * Places an order on the bookstore.
     * @param purchaseOrder the data of the order to be placed.
     * @return the data of the placed order
     * @throws CustomerNotFoundException if no customer with the specified number exists.
     * @throws BookNotFoundException if an order item references a non-existent book.
     * @throws PaymentFailedException if a payment error occurred.
     */
    SalesOrder placeOrder(PurchaseOrder purchaseOrder) throws CustomerNotFoundException,
            BookNotFoundException,
            PaymentFailedException;

    /**
     * Searches for orders by customer and year.
     * @param customerNr the number of the customer
     * @param year the year of the orders
     * @return a list of matching orders (may be empty)
     * @throws CustomerNotFoundException if no customer with the specified email address exists
     */
    List<OrderInfo> searchOrders(Long customerNr, Integer year) throws CustomerNotFoundException;

}