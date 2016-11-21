package ch.bfh.eadj.bookstore.repository;

import ch.bfh.eadj.bookstore.AbstractTest;
import ch.bfh.eadj.bookstore.entity.SalesOrder;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
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
}
