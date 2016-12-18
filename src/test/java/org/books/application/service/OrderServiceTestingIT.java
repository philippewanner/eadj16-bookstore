package org.books.application.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.books.DbUtil;
import org.books.application.dto.PurchaseOrder;
import org.books.application.dto.PurchaseOrderItem;
import org.books.application.dto.Registration;
import org.books.application.exception.*;
import org.books.persistence.TestDataProvider;
import org.books.persistence.dto.BookInfo;
import org.books.persistence.entity.*;
import org.books.persistence.enumeration.CreditCardType;
import org.jboss.logging.Logger;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.junit.Ignore;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author Philippe & jlue4
 */
public class OrderServiceTestingIT {

    private static final String ORDER_SERVICE_NAME = "java:global/bookstore/OrderService";
    
    private static final String CATALOG_SERVICE_NAME = "java:global/bookstore/CatalogService";

    private final static Logger LOGGER = Logger.getLogger(CatalogServiceBeanIT.class.getName());

    private OrderService orderService;

    private Long customerNumber = 0L;

    private PurchaseOrder purchaseOrder = null;

    private SalesOrder salesOrder = null;
    
    private static final int THREAD_COUNT = 100;
    
   
    @BeforeClass
    public void setup() throws NamingException, SQLException, CustomerAlreadyExistsException {
        LOGGER.info(">>>>>> "+Thread.currentThread().getStackTrace()[1].getMethodName()+" <<<<<<");

        DbUtil.executeSql("delete from SALESORDER_SALESORDERITEM");
        DbUtil.executeSql("delete from SALESORDERITEM");
        DbUtil.executeSql("delete from SALESORDER");
        
        DbUtil.executeSql("delete from Customer");
        DbUtil.executeSql("delete from UserLogin");

        deleteBooks();

        orderService = (OrderService) new InitialContext().lookup(ORDER_SERVICE_NAME);
        assertNotNull(orderService);

        addBooks();

        createCustomer();
        purchaseOrder = createPurchaseOrder();
    }

    @AfterClass
    public void tearDown() throws SQLException {
        LOGGER.info(">>>>>> "+Thread.currentThread().getStackTrace()[1].getMethodName()+" <<<<<<");

    }
    
    @Test
    public void placeOrder() throws PaymentFailedException, BookNotFoundException, CustomerNotFoundException, OrderNotFoundException {
        LOGGER.info(">>>>>> "+Thread.currentThread().getStackTrace()[1].getMethodName()+" <<<<<<");

        // Given
        PurchaseOrder purchaseOrder = this.purchaseOrder;

        // When
        salesOrder = orderService.placeOrder(purchaseOrder);
// FIXME
        // Then
        assertNotNull(salesOrder);
        assertEquals(salesOrder.getCustomer().getNumber(), purchaseOrder.getCustomerNr());
        assertEquals(salesOrder.getNumber(), orderService.findOrder(salesOrder.getNumber()).getNumber());
        assertEquals(salesOrder.getCustomer().getEmail(), orderService.findOrder(salesOrder.getNumber()).getCustomer().getEmail());
    }

        
    @Test(threadPoolSize = THREAD_COUNT, invocationCount = THREAD_COUNT)
    public void placeOrderX() throws PaymentFailedException, BookNotFoundException, CustomerNotFoundException, OrderNotFoundException {
        LOGGER.info(">>>>>> "+Thread.currentThread().getStackTrace()[1].getMethodName()+" <<<<<<");

        // Given
        PurchaseOrder purchaseOrder = this.purchaseOrder;

        // When
        salesOrder = orderService.placeOrder(purchaseOrder);

        // Then
        assertNotNull(salesOrder);
        assertEquals(salesOrder.getCustomer().getNumber(), purchaseOrder.getCustomerNr());
        assertEquals(salesOrder.getNumber(), orderService.findOrder(salesOrder.getNumber()).getNumber());
        assertEquals(salesOrder.getCustomer().getEmail(), orderService.findOrder(salesOrder.getNumber()).getCustomer().getEmail());
    }

    
    @Test(expectedExceptions = PaymentFailedException.class)
    public void placeOrder_throwsPaymentFailedException() throws PaymentFailedException, BookNotFoundException, CustomerNotFoundException, NamingException {
        LOGGER.info(">>>>>> "+Thread.currentThread().getStackTrace()[1].getMethodName()+" <<<<<<");

        // Given
        PurchaseOrder purchaseOrder = this.createPurchaseOrder();

        // When
        orderService.placeOrder(purchaseOrder);

        // Then
        assert(false); // should never get there
    }

    @Test(expectedExceptions = OrderNotFoundException.class, dependsOnMethods = "placeOrder")
    public void findOrder_throwsOrderNotFoundException() throws OrderNotFoundException {
        LOGGER.info(">>>>>> "+Thread.currentThread().getStackTrace()[1].getMethodName()+" <<<<<<");

        // Given
        Long orderNumberToFind = null;

        // When
        orderService.findOrder(orderNumberToFind);

        // Then (should never get here, since throw exception)
        assert(false);

    }

    @Ignore
    @Test(dependsOnMethods = "placeOrder")
    public void findOrder() throws OrderNotFoundException, PaymentFailedException, BookNotFoundException, CustomerNotFoundException {
        LOGGER.info(">>>>>> "+Thread.currentThread().getStackTrace()[1].getMethodName()+" <<<<<<");

        // Given
        Long salesOrderNumberToFind = salesOrder.getNumber();

        // When
        SalesOrder salesOrderFound = orderService.findOrder(salesOrderNumberToFind);

        // Then
        assertEquals(salesOrder, salesOrderFound);

    }

    @Test(dependsOnMethods = "placeOrder")
    public void searchForOrdersByCustomerAndYear() {
        LOGGER.info(">>>>>> "+Thread.currentThread().getStackTrace()[1].getMethodName()+" <<<<<<");
    }

    @Test(dependsOnMethods = "findOrder")
    public void cancelAnOrder() {
        LOGGER.info(">>>>>> "+Thread.currentThread().getStackTrace()[1].getMethodName()+" <<<<<<");

    }

    private void createCustomer() throws NamingException, CustomerAlreadyExistsException {
        final String ACCOUNT_SERVICE_NAME = "java:global/bookstore/CustomerService";

        CustomerService cs = (CustomerService) new InitialContext().lookup(ACCOUNT_SERVICE_NAME);
        assertNotNull(cs);

        Registration registration = new Registration();
        Address address=new Address("725 5th Avenue", "New York", "NY 10022", "NY", "United States");
        CreditCard cc = new CreditCard(CreditCardType.MASTER_CARD, "1234567890123456", 8, 2018);
        registration.setCustomer(new Customer("Donald", "Trump", "Donald@Trump.org", address, cc));
        registration.setPassword("md5");
        Long number = cs.registerCustomer(registration);

        assertNotNull(number);

        customerNumber = number;
    }

    private PurchaseOrder createPurchaseOrder() throws NamingException {
        PurchaseOrder po = new PurchaseOrder();

        po.setCustomerNr(customerNumber);

        List<PurchaseOrderItem> items = getPOItems();

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
