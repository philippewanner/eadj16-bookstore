package ch.bfh.eadj.bookstore.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by philippewanner on 14.11.16.
 */
@Entity
public class SalesOrder {

    @Id
    private Long number;
    
    private LocalDate date;
    
    private BigDecimal amount;
    
    private OrderStatus status;
    
    public enum OrderStatus {
        CANCELLED, ORDERED, PAYED, DELIVERED
    }

    
}
