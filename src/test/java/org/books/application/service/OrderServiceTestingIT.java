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
import org.jboss.logging.Logger;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

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
    
   
    @BeforeClass
    public void setup() throws NamingException, SQLException, CustomerAlreadyExistsException {

        logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

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

        logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

    }

    @Test
    public void placeOrder() throws PaymentFailedException, BookNotFoundException, CustomerNotFoundException, OrderNotFoundException, NamingException {

        logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

        // Given
        PurchaseOrder purchaseOrder = this.createPurchaseOrder();

        // When
        salesOrder = orderService.placeOrder(purchaseOrder);
// FIXME
        // Then
        assertNotNull(salesOrder);
        assertNotNull(salesOrder.getId());
        //assertEquals(salesOrder, orderService.findOrder(salesOrder.getNumber()));
    }

    @Test(expectedExceptions = PaymentFailedException.class)
    public void placeOrder_throwsPaymentFailedException() throws PaymentFailedException, BookNotFoundException, CustomerNotFoundException, NamingException {

        logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

        // Given
        PurchaseOrder purchaseOrder = this.createPurchaseOrder();

        // When
        orderService.placeOrder(purchaseOrder);

        // Then
        assert(false); // should never get there
    }

    @Test(expectedExceptions = OrderNotFoundException.class, dependsOnMethods = "placeOrder")
    public void findOrder_throwsOrderNotFoundException() throws OrderNotFoundException {

        logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

        // Given
        Long orderNumberToFind = null;

        // When
        orderService.findOrder(orderNumberToFind);

        // Then (should never get here, since throw exception)
        assert(false);

    }

    @Test(dependsOnMethods = "placeOrder")
    public void findOrder() throws OrderNotFoundException, PaymentFailedException, BookNotFoundException, CustomerNotFoundException, NamingException {

        logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

        // Given
        PurchaseOrder purchaseOrder = this.createPurchaseOrder();
        SalesOrder salesOrder = orderService.placeOrder(purchaseOrder);
        Long salesOrderNumberToFind = salesOrder.getNumber();

        // When
        SalesOrder salesOrderFound = orderService.findOrder(salesOrderNumberToFind);

        // Then
        assertEquals(salesOrder, salesOrderFound);

    }

    @Test(dependsOnMethods = "placeOrder")
    public void searchForOrdersByCustomerAndYear() {

        logInfoClassAndMethodName(Thread.currentThread().getStackTrace());
    }

    @Test(dependsOnMethods = "findOrder")
    public void cancelAnOrder() {

        logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

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

    private void logInfoClassAndMethodName(StackTraceElement[] stackTraceElements) {

        final String methodName = stackTraceElements[1].getMethodName();
        final String className = stackTraceElements[1].getClassName();

        LOGGER.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> \n  Class name \t: " + className + " \n  Method name \t: " + methodName + "()\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }
}
