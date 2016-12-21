package org.books.application.service;

import org.books.DbUtil;
import org.books.application.dto.PurchaseOrder;
import org.books.application.dto.PurchaseOrderItem;
import org.books.application.dto.Registration;
import org.books.application.exception.BookAlreadyExistsException;
import org.books.application.exception.BookNotFoundException;
import org.books.application.exception.CustomerAlreadyExistsException;
import org.books.application.exception.CustomerNotFoundException;
import org.books.application.exception.OrderNotFoundException;
import org.books.application.exception.PaymentFailedException;
import org.books.persistence.TestDataProvider;
import org.books.persistence.dto.BookInfo;
import org.books.persistence.entity.Address;
import org.books.persistence.entity.Book;
import org.books.persistence.entity.CreditCard;
import org.books.persistence.entity.Customer;
import org.books.persistence.entity.SalesOrder;
import org.books.persistence.enumeration.CreditCardType;
import org.jboss.logging.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 *
 * @author Philippe & jlue4
 */
public class OrderServiceTestingIT {

    private static final String ORDER_SERVICE_NAME = "java:global/bookstore/OrderService";
   private static final String CUSTOMER_SERVICE_NAME = "java:global/bookstore/CustomerService";
    
    private static final String CATALOG_SERVICE_NAME = "java:global/bookstore/CatalogService";

    private final static Logger LOGGER = Logger.getLogger(CatalogServiceBeanIT.class.getName());
   private static final int THREAD_COUNT = 100;
   private static final String MASTERCARD_VALID_ACCOUNT_NUMBER = "5105105105105100";
   private static final String VISA_VALID_ACCOUNT_NUMBER = "4111111111111111";
   private static final String AMERICANEXPRESS_VALID_ACCOUNT_NUMBER = "378734493671000";

   private OrderService orderService;
   private CustomerService customerService;
   private CatalogService catalogService;

    //private PurchaseOrder purchaseOrder;
    private SalesOrder salesOrder = null;
   private static int counter;

   @BeforeClass
    public void setup() throws NamingException, SQLException, CustomerAlreadyExistsException {

        logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

        DbUtil.executeSql("delete from SALESORDER_SALESORDERITEM");
        DbUtil.executeSql("delete from SALESORDERITEM");
        DbUtil.executeSql("delete from SALESORDER");
        
        DbUtil.executeSql("delete from Customer");
        DbUtil.executeSql("delete from UserLogin");

        deleteBooks();

      InitialContext initialContext = new InitialContext();
      orderService = (OrderService) initialContext.lookup(ORDER_SERVICE_NAME);
        assertNotNull(orderService);
      customerService = (CustomerService) initialContext.lookup(CUSTOMER_SERVICE_NAME);
      assertNotNull(customerService);
      catalogService = (CatalogService) initialContext.lookup(CATALOG_SERVICE_NAME);
      assertNotNull(catalogService);

        addBooks();
    }

    @AfterClass
    public void tearDown() throws SQLException {

        logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

        DbUtil.executeSql("delete from SALESORDER_SALESORDERITEM");
        DbUtil.executeSql("delete from SALESORDERITEM");
        DbUtil.executeSql("delete from SALESORDER");
        
        DbUtil.executeSql("delete from Customer");
        DbUtil.executeSql("delete from UserLogin");
        
    }
    @Test
    public void placeOrder() throws PaymentFailedException, BookNotFoundException, CustomerNotFoundException, OrderNotFoundException, NamingException {

        logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

        // Given
       Long customerNr = this.getNewPersistedCustomerNumber();
        PurchaseOrder purchaseOrder = this.createPurchaseOrder(customerNr);

        // When
        salesOrder = orderService.placeOrder(purchaseOrder);
// FIXME
        // Then
        assertEquals(salesOrder.getCustomer().getNumber(), purchaseOrder.getCustomerNr());
        assertEquals(salesOrder.getNumber(), orderService.findOrder(salesOrder.getNumber()).getNumber());
        assertEquals(salesOrder.getCustomer().getEmail(), orderService.findOrder(salesOrder.getNumber()).getCustomer().getEmail());
    }

        
    @Test(threadPoolSize = THREAD_COUNT, invocationCount = THREAD_COUNT)
    public void placeOrderX() throws PaymentFailedException, BookNotFoundException, CustomerNotFoundException, OrderNotFoundException {
        LOGGER.info(">>>>>> "+Thread.currentThread().getStackTrace()[1].getMethodName()+" <<<<<<");

        // Given
       Long customerNr = this.getNewPersistedCustomerNumber();
        PurchaseOrder purchaseOrder = this.createPurchaseOrder(customerNr);

        // When
        salesOrder = orderService.placeOrder(purchaseOrder);

        // Then
        assertNotNull(salesOrder);
        assertEquals(salesOrder.getCustomer().getNumber(), purchaseOrder.getCustomerNr());
        assertEquals(salesOrder.getNumber(), orderService.findOrder(salesOrder.getNumber()).getNumber());
        assertEquals(salesOrder.getCustomer().getEmail(), orderService.findOrder(salesOrder.getNumber()).getCustomer().getEmail());
    }

    
    @Test(expectedExceptions = PaymentFailedException.class)
    public void placeOrder_throwsPaymentFailedException()
          throws PaymentFailedException, BookNotFoundException, CustomerNotFoundException, NamingException,
          CustomerAlreadyExistsException {

        logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

        // Given
       Long customerNr = this.getNewPersistedCustomerNumber();
        PurchaseOrder purchaseOrder = this.createPurchaseOrder(customerNr);
       CreditCard invalidMasterCard = new CreditCard(CreditCardType.MASTER_CARD, "00", 2020, 01);
       Customer customer = customerService.findCustomer(purchaseOrder.getCustomerNr());
       customer.setCreditCard(invalidMasterCard);
       customerService.updateCustomer(customer);

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
       Long customerNr = this.getNewPersistedCustomerNumber();
        PurchaseOrder purchaseOrder = this.createPurchaseOrder(customerNr);
        SalesOrder salesOrder = orderService.placeOrder(purchaseOrder);
        Long salesOrderNumberToFind = salesOrder.getNumber();

        // When
        SalesOrder salesOrderFound = orderService.findOrder(salesOrderNumberToFind);

        // Then
        assertEquals(salesOrder.getNumber(), salesOrderFound.getNumber());

    }

    @Test(dependsOnMethods = "placeOrder")
    public void searchForOrdersByCustomerAndYear() {

        logInfoClassAndMethodName(Thread.currentThread().getStackTrace());
    }

    @Test(dependsOnMethods = "findOrder")
    public void cancelAnOrder() {

        logInfoClassAndMethodName(Thread.currentThread().getStackTrace());
    }

    private Long getNewPersistedCustomerNumber() {

        Address address=new Address("725 5th Avenue", "New York", "NY 10022", "NY", "United States");
       CreditCard cc = new CreditCard(CreditCardType.MASTER_CARD, MASTERCARD_VALID_ACCOUNT_NUMBER, 8, 2018);

       Customer customer = new Customer("Donald", "Trump", "Donald"+counter+"@Trump.org", address, cc);
       counter++;

       final String password = "md5";
        Registration registration = new Registration(customer, password);

       try {
          return customerService.registerCustomer(registration);
       } catch (CustomerAlreadyExistsException e) {
          e.printStackTrace();
          return null;
       }
    }

    private PurchaseOrder createPurchaseOrder(Long customerNumber) {
        PurchaseOrder po = new PurchaseOrder();

        po.setCustomerNr(customerNumber);

        List<PurchaseOrderItem> items = getPOItems();

        po.setItems(items);

        return po;
    }    
    
    private List<PurchaseOrderItem> getPOItems() {
        
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
