/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.books.application.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.books.DbUtil;
import org.books.application.dto.PurchaseOrder;
import org.books.application.dto.PurchaseOrderItem;
import org.books.application.dto.Registration;
import org.books.application.exception.BookAlreadyExistsException;
import org.books.application.exception.BookNotFoundException;
import org.books.application.exception.CustomerAlreadyExistsException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.exception.PaymentFailedException;
import org.books.persistence.TestDataProvider;
import org.books.persistence.dto.BookInfo;
import org.books.persistence.entity.Address;
import org.books.persistence.entity.Book;
import org.books.persistence.entity.CreditCard;
import org.books.persistence.entity.Customer;
import org.jboss.logging.Logger;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author jlue4
 */
public class OrderServiceTestingIT {

    private static final String ORDER_SERVICE_NAME = "java:global/bookstore/OrderService";
    
    final String CATALOG_SERVICE_NAME = "java:global/bookstore/CatalogService";

    private final static Logger LOGGER = Logger.getLogger(CatalogServiceBeanIT.class.getName());

    private OrderService service;

    private Long customerNumber = 0L;

    private PurchaseOrder purchaseOrder = null;
    
   
    @BeforeClass
    public void setup() throws NamingException, SQLException, CustomerAlreadyExistsException {
        LOGGER.info(">>>>>>>>>>>>>>>>>>> OrderService setup <<<<<<<<<<<<<<<<<<<<");

        DbUtil.executeSql("delete from Customer");
        DbUtil.executeSql("delete from UserLogin");

        deleteBooks();
        

        service = (OrderService) new InitialContext().lookup(ORDER_SERVICE_NAME);
        assertNotNull(service);

        addBooks();

        createCustomer();
        purchaseOrder = createPurchaseOrder();
    }

    @AfterClass
    public void tearDown() throws SQLException {
        LOGGER.info(">>>>>>>>>>>>>>>>>>> OrderService tearDown <<<<<<<<<<<<<<<<<<<<");

    }

    @Test
    public void placeOrders() {
        LOGGER.info(">>>>>>>>>>>>>>>>>>> placeOrders <<<<<<<<<<<<<<<<<<<<");

        boolean orderDone = false;
        try {
            service.placeOrder(purchaseOrder);
            orderDone = true;
        } catch (BookNotFoundException e) {
        } catch (CustomerNotFoundException ce) {
        } catch (PaymentFailedException pe) {
        }

        assertEquals(orderDone, true);
    }

    @Test(dependsOnMethods = "placeOrders")
    public void findAnOrderByNumber() {
        LOGGER.info(">>>>>>>>>>>>>>>>>>> findAnOrderByNumber <<<<<<<<<<<<<<<<<<<<");

    }

    @Test(dependsOnMethods = "placeOrders")
    public void searchForOrdersByCustomerAndYear() {
        LOGGER.info(">>>>>>>>>>>>>>>>>>> searchForOrdersByCustomerAndYear <<<<<<<<<<<<<<<<<<<<");
    }

    @Test(dependsOnMethods = "findAnOrderByNumber")
    public void cancelAnOrder() {
        LOGGER.info(">>>>>>>>>>>>>>>>>>> cancelAnOrder <<<<<<<<<<<<<<<<<<<<");

    }

    private void createCustomer() throws NamingException, CustomerAlreadyExistsException {
        final String ACCOUNT_SERVICE_NAME = "java:global/bookstore/CustomerService";

        CustomerService cs = (CustomerService) new InitialContext().lookup(ACCOUNT_SERVICE_NAME);
        assertNotNull(cs);

        Registration registration = new Registration();
        registration.setCustomer(new Customer("Donald", "Trump", "Donald@Trump.org", new Address(), new CreditCard()));
        registration.setPassword("md5");
        Long number = cs.registerCustomer(registration);

        assertNotNull(number);

        customerNumber = number;
    }

    private PurchaseOrder createPurchaseOrder() throws NamingException {
        PurchaseOrder po = new PurchaseOrder();

        po.setCustomerNr(customerNumber);

        List<PurchaseOrderItem> items;
        items = getPOItems();

        po.setItems(items);

        return po;
    }

    private List<PurchaseOrderItem> getPOItems() throws NamingException {
        
        CatalogService catalogService = (CatalogService) new InitialContext().lookup(CATALOG_SERVICE_NAME);
        assertNotNull(catalogService);
        
        List<PurchaseOrderItem> items = new ArrayList();

        List<BookInfo> books = catalogService.searchBooks("*");
        for (BookInfo b : books) {
            PurchaseOrderItem poi = new PurchaseOrderItem(b, 99);
            items.add(poi);
        }

        return items;
    }

    private void deleteBooks() throws SQLException {
        DbUtil.executeSql("delete from BOOK");
    }

    private void addBooks() throws NamingException {        
        
        CatalogService catalogService = (CatalogService) new InitialContext().lookup(CATALOG_SERVICE_NAME);
        assertNotNull(catalogService);
        
        List<Book> books = TestDataProvider.getBooks();
        for (Book b : books) {
            try {
                catalogService.addBook(b);
            } catch (BookAlreadyExistsException ex) {
                LOGGER.warn("book not added " + b.getTitle());
            }
        }
    }
}
