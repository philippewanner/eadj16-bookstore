package org.books.application.service;

import org.books.DbUtil;
import org.books.application.exception.*;
import org.books.persistence.entity.Address;
import org.books.persistence.entity.CreditCard;
import org.books.persistence.entity.Customer;
import org.books.persistence.entity.SalesOrder;
import org.books.persistence.enumeration.OrderStatus;
import org.jboss.logging.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.Assert.assertNull;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

public class OrderServiceBeanTestIT {

    private static final String ORDER_SERVICE_NAME = "java:global/bookstore/OrderService";

    private final static Logger LOGGER = Logger.getLogger(OrderServiceBeanTestIT.class.getName());

    private OrderService orderService;

    @BeforeClass
    public void lookup() throws NamingException {

        this.orderService = (OrderService) new InitialContext().lookup(ORDER_SERVICE_NAME);
        assertNotNull(orderService);
    }

    @AfterClass
    public void tearDown() throws SQLException {
        DbUtil.executeSql("delete from Customer where email = 'lukas@kalt.ch'");
        DbUtil.executeSql("delete from UserLogin where userName = 'lukas@kalt.ch'");
    }

    // void cancelOrder(Long orderNr) throws OrderNotFoundException, OrderAlreadyShippedException;
    @Test
    public void shouldCancelAnOrder_dummyTest() throws OrderNotFoundException, OrderAlreadyShippedException {

        // Given
        Long orderNumber = 234545L;

        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setStatus(OrderStatus.ACCEPTED);
        salesOrder.setCustomer(new Customer("Lukas", "Kalt", "lukas@kalt.ch", new Address(), new CreditCard()));
        salesOrder.setAmount(new BigDecimal(10.0));
        salesOrder.setDate(new Date());
        salesOrder.setNumber(orderNumber);

        // When
        orderService.cancelOrder(orderNumber);

        // Then
        SalesOrder salesOrderAfter = orderService.findOrder(orderNumber);
        assertNull(salesOrderAfter);
    }

    //SalesOrder findOrder(Long orderNr) throws OrderNotFoundException;

    //SalesOrder placeOrder(PurchaseOrder purchaseOrder) throws CustomerNotFoundException, BookNotFoundException, PaymentFailedException;

    //List<OrderInfo> searchOrders(Long customerNr, Integer year) throws CustomerNotFoundException;

}
