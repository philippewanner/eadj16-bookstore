package org.books.application.service;

import org.books.DbUtil;
import org.books.application.dto.PurchaseOrder;
import org.books.application.dto.PurchaseOrderItem;
import org.books.application.dto.Registration;
import org.books.application.exception.*;
import org.books.persistence.TestDataProvider;
import org.books.persistence.dto.BookInfo;
import org.books.persistence.dto.OrderInfo;
import org.books.persistence.entity.*;
import org.books.persistence.enumeration.CreditCardType;
import org.books.persistence.enumeration.OrderStatus;
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
 * @author Philippe & jlue4
 */
public class OrderServiceIT {

    // JNDI names
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
    public void setup() throws NamingException, SQLException {

        logInfoClassAndMethodName(Thread.currentThread().getStackTrace());
        DbUtil.clearDatabase();

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

        DbUtil.clearDatabase();
    }

    @Test
    public void placeOrder()
            throws PaymentFailedException, BookNotFoundException, CustomerNotFoundException, OrderNotFoundException {

        logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

        // Given
        Customer newCustomer = this.createNewCustomer();
        PurchaseOrder purchaseOrder = new PurchaseOrder(newCustomer.getNumber(), getPOItems());

        // When
        SalesOrder salesOrderPersisted = orderService.placeOrder(purchaseOrder);

        // Then
        SalesOrder salesOrderFound = orderService.findOrder(salesOrderPersisted.getNumber());
        assertNotNull(salesOrderFound);
        assertEquals(salesOrderPersisted.getNumber(), salesOrderFound.getNumber());
    }

    @Test
    public void placeOrderNew()
            throws PaymentFailedException, BookNotFoundException, CustomerNotFoundException, OrderNotFoundException {

        logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

        // Given
        Customer newCustomer = this.createNewCustomer();
        PurchaseOrder purchaseOrder = new PurchaseOrder(newCustomer.getNumber(), getPOItemsNew());

        // When
        SalesOrder salesOrderPersisted = orderService.placeOrder(purchaseOrder);

        // Then
        SalesOrder salesOrderFound = orderService.findOrder(salesOrderPersisted.getNumber());
        assertNotNull(salesOrderFound);
        assertEquals(salesOrderPersisted.getNumber(), salesOrderFound.getNumber());
    }

    @Test(threadPoolSize = THREAD_COUNT, invocationCount = THREAD_COUNT, dependsOnMethods = "placeOrder")
    public void placeOrderX()
            throws PaymentFailedException, BookNotFoundException, CustomerNotFoundException, OrderNotFoundException {
        LOGGER.info(">>>>>> " + Thread.currentThread().getStackTrace()[1].getMethodName() + " <<<<<<");

        // Given
        Customer newCustomer = this.createNewCustomer();
        PurchaseOrder purchaseOrder = new PurchaseOrder(newCustomer.getNumber(), getPOItems());

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
        PurchaseOrder purchaseOrder = new PurchaseOrder(newCustomer.getNumber(), getPOItems());

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
        PurchaseOrder purchaseOrder = new PurchaseOrder(newCustomer.getNumber(), getPOItems());

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
        PurchaseOrder purchaseOrder = new PurchaseOrder(newCustomer.getNumber(), getPOItems());

        // When
        orderService.placeOrder(purchaseOrder);
    }

    @Test(expectedExceptions = CustomerNotFoundException.class)
    public void placeOrder_customerNotFound()
            throws PaymentFailedException, BookNotFoundException, CustomerNotFoundException {

        logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

        // Given
        PurchaseOrder purchaseOrder = new PurchaseOrder(9999999L, getPOItems());

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
        PurchaseOrder purchaseOrder = new PurchaseOrder(newCustomer.getNumber(), getPOItems());

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
        PurchaseOrder purchaseOrder = new PurchaseOrder(newCustomer.getNumber(), getPOItems());

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
        PurchaseOrder purchaseOrder = new PurchaseOrder(newCustomer.getNumber(), getPOItems());

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
        PurchaseOrder purchaseOrder = new PurchaseOrder(newCustomer.getNumber(), getPOItems());
        BookInfo bookInfo = new BookInfo("978-3-455-65045-7", "title", new BigDecimal(20000));
        purchaseOrder.getItems().add(new PurchaseOrderItem(bookInfo, 4));

        // When
        orderService.placeOrder(purchaseOrder);
    }

    @Test(expectedExceptions = BookNotFoundException.class)
    public void placeOrder_invalidIsbn()
            throws PaymentFailedException, BookNotFoundException, CustomerNotFoundException, OrderNotFoundException {

        logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

        // Given
        Customer newCustomer = this.createNewCustomer();
        PurchaseOrder purchaseOrder = new PurchaseOrder(newCustomer.getNumber(), getPOItemInvalid());

        // When
        SalesOrder salesOrderPersisted = orderService.placeOrder(purchaseOrder);

        // Then
        SalesOrder salesOrderFound = orderService.findOrder(salesOrderPersisted.getNumber());
        assertNotNull(salesOrderFound);
        assertEquals(salesOrderPersisted.getNumber(), salesOrderFound.getNumber());
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
        PurchaseOrder purchaseOrder = new PurchaseOrder(newCustomer.getNumber(), getPOItems());

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
    public void findOrder()
            throws OrderNotFoundException, PaymentFailedException, BookNotFoundException, CustomerNotFoundException,
            NamingException {

        logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

        // Given
        Customer newCustomer = this.createNewCustomer();
        PurchaseOrder purchaseOrder = new PurchaseOrder(newCustomer.getNumber(), getPOItems());
        SalesOrder salesOrder = orderService.placeOrder(purchaseOrder);
        Long salesOrderNumberToFind = salesOrder.getNumber();

        // When
        SalesOrder salesOrderFound = orderService.findOrder(salesOrderNumberToFind);

        // Then
        assertEquals(salesOrder.getNumber(), salesOrderFound.getNumber());
    }

    @Test(dependsOnMethods = "placeOrder")
    public void searchOrders() throws PaymentFailedException, BookNotFoundException, CustomerNotFoundException {

        logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

        // Given
        Customer newCustomer = this.createNewCustomer();
        final int NB_PURCHASE = 5;
        for (int i = 0; i < NB_PURCHASE; i++) {
            PurchaseOrder purchaseOrder = new PurchaseOrder(newCustomer.getNumber(), getPOItems());
            orderService.placeOrder(purchaseOrder);
        }

        // When
        List<OrderInfo> orderInfosFound = orderService.searchOrders(newCustomer.getNumber(), 2016);

        // Then
        assertEquals(NB_PURCHASE, orderInfosFound.size());
    }

    @Test(expectedExceptions = CustomerNotFoundException.class, dependsOnMethods = "placeOrder")
    public void searchOrders_customerNotFound() throws CustomerNotFoundException {

        logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

        // Given
        Long customerNumber = 999999999L;

        // When
        orderService.searchOrders(customerNumber, 2016);
    }

    @Test(dependsOnMethods = "findOrder")
    public void cancelAnOrder()
            throws PaymentFailedException, BookNotFoundException, CustomerNotFoundException, OrderNotFoundException,
            OrderAlreadyShippedException {

        logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

        // Given
        Customer newCustomer = this.createNewCustomer();
        PurchaseOrder purchaseOrder = new PurchaseOrder(newCustomer.getNumber(), getPOItems());
        SalesOrder salesOrder = orderService.placeOrder(purchaseOrder);

        // When
        orderService.cancelOrder(salesOrder.getNumber());

        // Then
        SalesOrder salesOrderActual = orderService.findOrder(salesOrder.getNumber());
        assertNotNull(salesOrderActual);
        assertNotNull(salesOrderActual.getNumber());
        assertEquals(salesOrderActual.getStatus(), OrderStatus.CANCELED);
    }

    @Test(expectedExceptions = OrderNotFoundException.class, dependsOnMethods = "findOrder")
    public void cancelAnOrder_orderNotFoundException() throws OrderNotFoundException, OrderAlreadyShippedException {

        logInfoClassAndMethodName(Thread.currentThread().getStackTrace());

        // Given
        Long orderNr = 99999999L;

        // When
        orderService.cancelOrder(orderNr);
    }

    private Customer createNewCustomer(CreditCard creditCard) {

        Address address = new Address("725 5th Avenue", "New York", "NY 10022", "NY", "United States");
        Customer customer = new Customer("Donald", "Trump", "Donald" + counter + "@Trump.org", address, creditCard);
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

    private List<PurchaseOrderItem> getPOItemInvalid() {

        List<PurchaseOrderItem> items = new ArrayList<>();

        String isbn = "123-4-567-89098-7";
        String title = "not existing";
        BigDecimal price = BigDecimal.valueOf(0);

        PurchaseOrderItem poi = new PurchaseOrderItem(new BookInfo(isbn, title, price), 1);
        items.add(poi);

        return items;
    }

    private Customer createNewCustomer() {

        CreditCard cc = new CreditCard(CreditCardType.MASTER_CARD, MASTERCARD_VALID_ACCOUNT_NUMBER, 8, 2018);

        return this.createNewCustomer(cc);
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

    private List<PurchaseOrderItem> getPOItemsNew() {

        List<PurchaseOrderItem> items = new ArrayList<>();

        try {
            Book book = catalogService.findBook("0672337452");
            PurchaseOrderItem poi = new PurchaseOrderItem(new BookInfo(book.getIsbn(), book.getTitle(), book.getPrice()), 99);
            items.add(poi);            
        } catch (BookNotFoundException ex) {
            LOGGER.warn("getPOItemsNew error " + ex);            
        }

        return items;
    }

    private void addBooks() {

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

        LOGGER.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> \n  Class name \t: " + className + " \n  Method name \t: "
                + methodName + "()\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }
}
