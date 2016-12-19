package org.books.application.service;

import org.books.application.dto.PurchaseOrder;
import org.books.application.dto.PurchaseOrderItem;
import org.books.application.exception.*;
import org.books.persistence.dto.BookInfo;
import org.books.persistence.dto.OrderInfo;
import org.books.persistence.entity.Book;
import org.books.persistence.entity.Customer;
import org.books.persistence.entity.SalesOrder;
import org.books.persistence.entity.SalesOrderItem;
import org.books.persistence.enumeration.OrderStatus;
import org.books.persistence.repository.BookRepository;
import org.books.persistence.repository.CustomerRepository;
import org.books.persistence.repository.OrderRepository;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.ejb.Timer;
import javax.inject.Inject;
import javax.jms.*;
import javax.jms.Queue;
import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author Philippe
 *
 * The service allows to: place an order, find an order by number search for
 * orders by customer and year cancel an order
 */
@Stateless(name = "OrderService")
public class OrderServiceBean extends AbstractService implements OrderService {

    @EJB
    private OrderRepository orderRepository;

    @EJB
    private CustomerRepository customerRepository;

    @EJB
    private BookRepository bookRepository;

    @EJB
    private MailService mailService;

    @Inject
    @JMSConnectionFactory("jms/connectionFactory")
    private JMSContext jmsContext;

    @Resource(lookup = "jms/orderQueue")
    private Queue queue;

    //@Re//source
    //private UserTransaction userTransaction;
    @Override
    public void cancelOrder(Long orderNr) throws OrderNotFoundException, OrderAlreadyShippedException {

        SalesOrder orderFound = orderRepository.findByNumber(orderNr);
        if (orderFound == null) {

            logInfo("Order nr " + orderNr + " not found");
            throw new OrderNotFoundException();
        }
        if (orderFound.getStatus().equals(OrderStatus.SHIPPED)) {

            logInfo("Order nr " + orderNr + " already shipped");
            throw new OrderAlreadyShippedException();
        }

        orderFound.setStatus(OrderStatus.CANCELED);
        orderRepository.update(orderFound);
    }

    @Override
    public SalesOrder findOrder(Long orderNr) throws OrderNotFoundException {

        SalesOrder orderFound = orderRepository.findByNumber(orderNr);
        if (orderFound == null) {

            logInfo("Order nr " + orderNr + " not found");
            throw new OrderNotFoundException();
        }

        return orderFound;
    }

    @Override
    public SalesOrder placeOrder(PurchaseOrder purchaseOrder) throws CustomerNotFoundException, BookNotFoundException, PaymentFailedException {
        SalesOrder order = createSalesOrder(purchaseOrder);

        orderRepository.persist(order);
        orderRepository.flush();

        if (order != null) {
            sendToQueue(order);
        }

        return order;
    }

    @Override
    public List<OrderInfo> searchOrders(Long customerNr, Integer year) throws CustomerNotFoundException {

        Customer customer = customerRepository.find(customerNr);
        if (customer == null) {

            logInfo("Customer nr " + customerNr + " not found");
            throw new CustomerNotFoundException();
        }

        return orderRepository.searchByCustomerAndYear(customer, year);
    }

    @Schedule(hour = "*", minute = "15")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void shipOrders(Timer timer) {
        List<SalesOrder> orders = orderRepository.findByStatus(OrderStatus.PROCESSING);
        for (SalesOrder order : orders) {
            try {
                //userTransaction.begin();
                orderRepository.lock(order, LockModeType.PESSIMISTIC_WRITE);
                order.setStatus(OrderStatus.SHIPPED);
                //userTransaction.commit();

                mailService.sendEmail(order.getCustomer().getEmail(), "Order " + order.getNumber(),
                        "Order set to state shipped");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sendToQueue(SalesOrder order) {
        try {
            JMSProducer producer = jmsContext.createProducer();
            MapMessage msg = jmsContext.createMapMessage();
            msg.setLong("orderNr", order.getNumber());
            producer.send(queue, msg);
        } catch (JMSException e) {
            logError(e.toString());
        }
    }

    private SalesOrder createSalesOrder(PurchaseOrder po) {
        SalesOrder so = new SalesOrder();

        Customer c = customerRepository.find(po.getCustomerNr());
        so.setDate(new Date());

        so.setCustomer(c);
        so.setAddress(c.getAddress());
        so.setAmount(getAmount(po.getItems()));
        so.setCreditCard(c.getCreditCard());
        so.setSalesOrderItems(getSoItems(po.getItems()));
        so.setStatus(OrderStatus.ACCEPTED);

        return so;
    }

    private Set<SalesOrderItem> getSoItems(List<PurchaseOrderItem> items) {
        Set<SalesOrderItem> soItems = new HashSet<>();

        for (PurchaseOrderItem poi : items) {
            boolean added = false;
            for (SalesOrderItem soi : soItems) {

                if (soi.getBook().getIsbn().equals(poi.getBookInfo().getIsbn())) {
                    Integer newQuantity = soi.getQuantity() + poi.getQuantity();
                    BigDecimal newPrice
                            = soi.getPrice().add(
                                    poi.getBookInfo().getPrice().multiply(BigDecimal.valueOf(poi.getQuantity())));

                    soi.setPrice(newPrice);
                    soi.setQuantity(newQuantity);
                    added = true;
                    break;
                }
            }
            if (!added) {
                SalesOrderItem newSoi = new SalesOrderItem();
                Book book = getBook(poi.getBookInfo());
                if (book != null) {
                    newSoi.setBook(book);
                    BigDecimal price = book.getPrice().multiply(BigDecimal.valueOf(poi.getQuantity()));
                    newSoi.setPrice(price);
                    newSoi.setQuantity(poi.getQuantity());

                    soItems.add(newSoi);
                }
            }

        }

        return soItems;
    }

    private Book getBook(BookInfo bi) {
        List<Book> books = bookRepository.findByISBN(bi.getIsbn());

        if (books.size() > 0) {
            return books.get(0);
        }

        return null;
    }

    private BigDecimal getAmount(List<PurchaseOrderItem> items) {
        BigDecimal amount = BigDecimal.valueOf(0);

        for (PurchaseOrderItem i : items) {
            amount = amount.add(BigDecimal.valueOf(i.getQuantity()).multiply(i.getBookInfo().getPrice()));
        }
        return amount;
    }
}