package org.books.application.service;

import org.books.application.dto.PurchaseOrder;
import org.books.application.exception.*;
import org.books.persistence.dto.OrderInfo;
import org.books.persistence.entity.SalesOrder;

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
 *
 */

@Stateless
public class OrderServiceBean implements OrderService {

    @Override
    public void cancelOrder(Long orderNr) throws OrderNotFoundException, OrderAlreadyShippedException {

    }

    @Override
    public SalesOrder findOrder(Long orderNr) throws OrderNotFoundException {
        return null;
    }

    @Override
    public SalesOrder placeOrder(PurchaseOrder purchaseOrder) throws CustomerNotFoundException, BookNotFoundException, PaymentFailedException {
        return null;
    }

    @Override
    public List<OrderInfo> searchOrders(Long customerNr, Integer year) throws CustomerNotFoundException {
        return null;
    }
}
