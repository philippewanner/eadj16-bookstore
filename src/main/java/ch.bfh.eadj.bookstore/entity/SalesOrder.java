package ch.bfh.eadj.bookstore.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by philippewanner on 14.11.16.
 */
@Entity
public class SalesOrder {

    @Id
    private Long number;
    
    
    public enum OrderStatus {
        CANCELLED, ORDERED, PAYED, DELIVERED
    }

    
}
