package ch.bfh.eadj.bookstore.entity;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by philippewanner on 14.11.16.
 */

@Entity
public class SalesOrderItem {
    
    @Id
    private Long id;
    
    private Integer quantity;
    
    private BigDecimal price;
}
