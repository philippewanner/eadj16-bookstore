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
import java.math.BigDecimal;
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

   private static int counter;

   @BeforeClass
    public void setup() throws NamingException, SQLException, CustomerAlreadyExistsException {

        logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

        DbUtil.executeSql("delete from SALESORDER_SALESORDERITEM");
        DbUtil.executeSql("delete from SALESORDERITEM");
        DbUtil.executeSql("delete from SALESORDER");
        
        DbUtil.executeSql("delete from Customer");
        DbUtil.executeSql("delete from UserLogin");

        // Delete books
         DbUtil.executeSql("delete from BOOK");

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
       Customer newCustomer = this.createNewCustomer();
        PurchaseOrder purchaseOrder = this.createPurchaseOrder(newCustomer.getNumber());

        // When
        SalesOrder salesOrderPersisted = orderService.placeOrder(purchaseOrder);

        // Then
       SalesOrder salesOrderFound = orderService.findOrder(salesOrderPersisted.getNumber());
       assertNotNull(salesOrderFound);
        assertEquals(salesOrderPersisted.getNumber(), salesOrderFound.getNumber());
    }

        
    @Test(threadPoolSize = THREAD_COUNT, invocationCount = THREAD_COUNT, dependsOnMethods = "placeOrder")
    public void placeOrderX() throws PaymentFailedException, BookNotFoundException, CustomerNotFoundException, OrderNotFoundException {
        LOGGER.info(">>>>>> "+Thread.currentThread().getStackTrace()[1].getMethodName()+" <<<<<<");

        // Given
       Customer newCustomer = this.createNewCustomer();
        PurchaseOrder purchaseOrder = this.createPurchaseOrder(newCustomer.getNumber());

        // When
       SalesOrder salesOrderPersisted = orderService.placeOrder(purchaseOrder);

        // Then
        assertNotNull(salesOrderPersisted);
        assertEquals(salesOrderPersisted.getNumber(), orderService.findOrder(salesOrderPersisted.getNumber())
                                                                   .getNumber());
    }

    
    @Test(expectedExceptions = PaymentFailedException.class)
    public void placeOrder_invalidMasterCard()
          throws PaymentFailedException, BookNotFoundException, CustomerNotFoundException, NamingException,
          CustomerAlreadyExistsException {

        logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

        // Given
       CreditCard invalidMasterCard = new CreditCard(CreditCardType.MASTER_CARD, "00", 1, 2020);
       Customer newCustomer = this.createNewCustomer(invalidMasterCard);
       assertNotNull(newCustomer);
       PurchaseOrder purchaseOrder = this.createPurchaseOrder(newCustomer.getNumber());

        // When
        orderService.placeOrder(purchaseOrder);
    }

   @Test
   public void placeOrder_validMasterCard()
         throws PaymentFailedException, BookNotFoundException, CustomerNotFoundException, NamingException,
         CustomerAlreadyExistsException {

      logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

      // Given
      CreditCard validMasterCard = new CreditCard(CreditCardType.MASTER_CARD, MASTERCARD_VALID_ACCOUNT_NUMBER, 1,
                                                  2020);
      Customer newCustomer = this.createNewCustomer(validMasterCard);
      assertNotNull(newCustomer);
      PurchaseOrder purchaseOrder = this.createPurchaseOrder(newCustomer.getNumber());

      // When
      SalesOrder salesOrder = orderService.placeOrder(purchaseOrder);

      // Then
      assertNotNull(salesOrder);
      assertNotNull(salesOrder.getNumber());
   }

   @Test(expectedExceptions = PaymentFailedException.class)
   public void placeOrder_creditCardExpired()
         throws PaymentFailedException, BookNotFoundException, CustomerNotFoundException {

      logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

      // Given
      CreditCard expiredCreditCard = new CreditCard(CreditCardType.AMERICAN_EXPRESS,
                                                    AMERICANEXPRESS_VALID_ACCOUNT_NUMBER, 1, 2000);
      Customer newCustomer = this.createNewCustomer(expiredCreditCard);
      assertNotNull(newCustomer);
      PurchaseOrder purchaseOrder = this.createPurchaseOrder(newCustomer.getNumber());

      // When
      orderService.placeOrder(purchaseOrder);
   }

   @Test(expectedExceptions = CustomerNotFoundException.class)
   public void placeOrder_customerNotFound()
         throws PaymentFailedException, BookNotFoundException, CustomerNotFoundException {

      logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

      // Given
      Customer customerNotInDb = new Customer();
      customerNotInDb.setNumber(9999999L);
      PurchaseOrder purchaseOrder = this.createPurchaseOrder(customerNotInDb.getNumber());

      // When
      orderService.placeOrder(purchaseOrder);
   }

   @Test(expectedExceptions = PaymentFailedException.class)
   public void placeOrder_invalidVisa()
         throws PaymentFailedException, BookNotFoundException, CustomerNotFoundException, NamingException,
         CustomerAlreadyExistsException {

      logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

      // Given
      CreditCard invalidVisa = new CreditCard(CreditCardType.VISA, "00", 1, 2020);
      Customer newCustomer = this.createNewCustomer(invalidVisa);
      assertNotNull(newCustomer);
      PurchaseOrder purchaseOrder = this.createPurchaseOrder(newCustomer.getNumber());

      // When
      orderService.placeOrder(purchaseOrder);
   }

   @Test
   public void placeOrder_validVisa()
         throws PaymentFailedException, BookNotFoundException, CustomerNotFoundException, NamingException,
         CustomerAlreadyExistsException {

      logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

      // Given
      CreditCard validVisa = new CreditCard(CreditCardType.VISA, VISA_VALID_ACCOUNT_NUMBER, 1, 2020);
      Customer newCustomer = this.createNewCustomer(validVisa);
      assertNotNull(newCustomer);
      PurchaseOrder purchaseOrder = this.createPurchaseOrder(newCustomer.getNumber());

      // When
      SalesOrder salesOrder = orderService.placeOrder(purchaseOrder);

      // Then
      assertNotNull(salesOrder);
      assertNotNull(salesOrder.getNumber());
   }

   @Test(expectedExceptions = PaymentFailedException.class)
   public void placeOrder_invalidAmericanExpress()
         throws PaymentFailedException, BookNotFoundException, CustomerNotFoundException, NamingException,
         CustomerAlreadyExistsException {

      logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

      // Given
      CreditCard invalidAmericanExpress = new CreditCard(CreditCardType.AMERICAN_EXPRESS, "00", 1, 2020);
      Customer newCustomer = this.createNewCustomer(invalidAmericanExpress);
      assertNotNull(newCustomer);
      PurchaseOrder purchaseOrder = this.createPurchaseOrder(newCustomer.getNumber());

      // When
      orderService.placeOrder(purchaseOrder);
   }

   @Test(expectedExceptions = PaymentFailedException.class)
   public void placeOrder_exceedLimitAmount()
         throws PaymentFailedException, BookNotFoundException, CustomerNotFoundException {

      logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

      // Given
      Customer newCustomer = this.createNewCustomer();
      assertNotNull(newCustomer);
      PurchaseOrder purchaseOrder = this.createPurchaseOrder(newCustomer.getNumber());
      BookInfo bookInfo = new BookInfo("isbn", "title", new BigDecimal(20000));
      purchaseOrder.getItems().add(new PurchaseOrderItem(bookInfo, 4));

      // When
      orderService.placeOrder(purchaseOrder);
   }

   @Test
   public void placeOrder_validAmericanExpress()
         throws PaymentFailedException, BookNotFoundException, CustomerNotFoundException, NamingException,
         CustomerAlreadyExistsException {

      logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

      // Given
      CreditCard validAmericanExpress = new CreditCard(CreditCardType.AMERICAN_EXPRESS,
                                                       AMERICANEXPRESS_VALID_ACCOUNT_NUMBER, 1, 2020);
      Customer newCustomer = this.createNewCustomer(validAmericanExpress);
      assertNotNull(newCustomer);
      PurchaseOrder purchaseOrder = this.createPurchaseOrder(newCustomer.getNumber());

      // When
      SalesOrder salesOrder = orderService.placeOrder(purchaseOrder);

      // Then
      assertNotNull(salesOrder);
      assertNotNull(salesOrder.getNumber());
   }

    @Test(expectedExceptions = OrderNotFoundException.class, dependsOnMethods = "placeOrder")
    public void findOrder_throwsOrderNotFoundException() throws OrderNotFoundException {

        logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

        // Given

        // When
        orderService.findOrder(null);
    }

    @Test(dependsOnMethods = "placeOrder")
    public void findOrder() throws OrderNotFoundException, PaymentFailedException, BookNotFoundException, CustomerNotFoundException, NamingException {

        logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

        // Given
       Customer newCustomer = this.createNewCustomer();
        PurchaseOrder purchaseOrder = this.createPurchaseOrder(newCustomer.getNumber());
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

    private Customer createNewCustomer(CreditCard creditCard){

       Address address=new Address("725 5th Avenue", "New York", "NY 10022", "NY", "United States");
       Customer customer = new Customer("Donald", "Trump", "Donald"+counter+"@Trump.org", address, creditCard);
       counter++;

       final String password = "md5";
       Registration registration = new Registration(customer, password);

       try {
          customer.setNumber(customerService.registerCustomer(registration));
          return customer;
       } catch (CustomerAlreadyExistsException e) {
          e.printStackTrace();
          return null;
       }
    }

    private Customer createNewCustomer() {

       CreditCard cc = new CreditCard(CreditCardType.MASTER_CARD, MASTERCARD_VALID_ACCOUNT_NUMBER, 8, 2018);

       return this.createNewCustomer(cc);
    }

    private PurchaseOrder createPurchaseOrder(Long customerNumber) {
        PurchaseOrder po = new PurchaseOrder();

        po.setCustomerNr(customerNumber);

        List<PurchaseOrderItem> items = getPOItems();

        po.setItems(items);

        return po;
    }    
    
    private List<PurchaseOrderItem> getPOItems() {
        
        List<PurchaseOrderItem> items = new ArrayList<>();

        List<BookInfo> books = catalogService.searchBooks("*");
        for (BookInfo b : books) {
            PurchaseOrderItem poi = new PurchaseOrderItem(b, 99);
            items.add(poi);
        }

        return items;
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
