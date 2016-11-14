package ch.bfh.eadj.entity;

import java.math.BigDecimal;
import javax.persistence.Entity;

import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by philippewanner on 14.11.16.
 */

@Entity
public class SalesOrderItem extends BaseEntity{
    
    @Id
    private Long id;
       
    @ManyToOne
    private Book book;    
    
    private Integer quantity;
    
    private BigDecimal price;
}
