package ch.bfh.eadj.bookstore.repository;

import ch.bfh.eadj.bookstore.AbstractTest;
import ch.bfh.eadj.bookstore.dto.OrderInfo;
import ch.bfh.eadj.bookstore.entity.Customer;
import ch.bfh.eadj.bookstore.entity.SalesOrder;
import ch.bfh.eadj.bookstore.entity.User;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertNull;

public class OrderRepositoryTest extends AbstractTest{

    private OrderRepository orderRepository;

    @Before
    public void setUpBeforeTest() {
        orderRepository = new OrderRepository(em);
    }

    @Test
    public void searchByNumber_existingNumber() {

        // Given
        SalesOrder salesOrder = getPersistedSalesOrder();

        // When
        SalesOrder actualSalesOrder = orderRepository.findByNumber(2L);

        // Then
        assertNotNull(actualSalesOrder);
        assertEquals(salesOrder.getNumber(), actualSalesOrder.getNumber());
    }

    @Test
    public void searchByNumber_nonExistingNumber() {

        // Given

        // When
        SalesOrder actualSalesOrder = orderRepository.findByNumber(29L);

        // Then
        assertNull(actualSalesOrder);

    }

    @Test
    public void searchByCustomerAndYear_matchingCustomerAndYear(){

        // Given
        SalesOrder salesOrder = getPersistedSalesOrder();
        LocalDate expectedYear = salesOrder.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Integer year = 1989;
        Customer customer = getPersistedCustomer();

        // When
        List<OrderInfo> foundOrderInfos = this.orderRepository.searchByCustomerAndYear(customer, year);

        // Then
        assertNotNull(foundOrderInfos);
        assertTrue(foundOrderInfos.size() > 0);
        LocalDate localDate = foundOrderInfos.get(0).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        assertEquals(expectedYear.getYear(), localDate.getYear());
    }

    @Test
    public void searchByCustomerAndYear_onlyMatchingYear(){

        // Given
        Integer year = 1989;
        Customer customer = new Customer(900L, "fakeFirst", "fakeLast", "fakeEmail", new User());

        // When
        List<OrderInfo> foundOrderInfos = this.orderRepository.searchByCustomerAndYear(customer, year);

        // Then
        assertNotNull(foundOrderInfos);
        assertEquals(0, foundOrderInfos.size());
    }

    @Test
    public void searchByCustomerAndYear_onlyMatchingCustomer(){

        // Given
        Integer year = 2011;
        Customer customer = new Customer(900L, "fakeFirst", "fakeLast", "fakeEmail", new User());

        // When
        List<OrderInfo> foundOrderInfos = this.orderRepository.searchByCustomerAndYear(customer, year);

        // Then
        assertNotNull(foundOrderInfos);
        assertEquals(0, foundOrderInfos.size());
    }

    @Test
    public void searchByCustomerAndYear_notAtAllMatching(){

        // Given
        Integer year = 2011;
        Customer customer = getPersistedCustomer();

        // When
        List<OrderInfo> foundOrderInfos = this.orderRepository.searchByCustomerAndYear(customer, year);

        // Then
        assertNotNull(foundOrderInfos);
        assertEquals(0, foundOrderInfos.size());
    }
}
